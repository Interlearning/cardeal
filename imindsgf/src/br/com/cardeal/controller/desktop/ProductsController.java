package br.com.cardeal.controller.desktop;

import static br.com.caelum.vraptor.view.Results.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.ArrayUtils;
import br.com.cardeal.dao.DaoFactory;
import br.com.cardeal.dao.ProductDao;
import br.com.cardeal.dao.UnitDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.filter.ProductFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Etiqueta;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;

@Resource
public class ProductsController {

    private final Validator validator;
    private final Result result;
    private final ProductDao dao;
    private final UserInfo userInfo;
    private final UnitDao unitDao;
    private final DaoFactory daoFactory;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;

	public ProductsController(
			ProductDao dao
			, UserInfo userInfo 
			, UnitDao unitDao 
			, Result result
			, Validator validator 			
			, DaoFactory daoFactory
			, ServletContext servletContext
			, HttpServletResponse servletResponse
			) {
		this.dao = dao;
		this.userInfo = userInfo;
		this.unitDao = unitDao;
		this.result = result;
		this.validator = validator;		
		this.daoFactory = daoFactory;
		this.servletContext = servletContext;		
		this.servletResponse = servletResponse;
	}

	private void fillLists(boolean removeUndefined) 
	{
		result.include("units", unitDao.list());		
		result.include("labelsBox", GuiGlobals.listLabelsBox());
		result.include("labelsPallet", GuiGlobals.listLabelsPallet());
		result.include("listTypeStock", TypeStock.getValuesToProducts());		
		
		if(removeUndefined) {
			result.include("weighingStyles", ArrayUtils.removeElement(WeighingStyle.values(), WeighingStyle.values()[0]));
			
		}
		else {
			result.include("weighingStyles", WeighingStyle.values());			
		}
	}
	
	@Path("/products")
	@Get
	public void list() {	
		validPermission();
		User user = userInfo.getUser();
		
		result.include("user", user);
        result.include("products", null);
        fillLists(false);
	}	
	
	@Path("/products/list")
	public void list(ProductFilter filter) {
		User user = userInfo.getUser();
		userInfo.setProductFilter(filter);
		
		result.include("user", user);
		fillLists(false);
        List<Product> products = new ArrayList<Product>();
        
        GuiGlobals.refreshDaoFactory();
        ProductDao productDao = GuiGlobals.getDaoFactory().getProductDao();
        
        List<Product> productsFromDatabase = productDao.list(filter);
        for (Product product : productsFromDatabase) {
            Product newProduct = product.clone();
            products.add(newProduct);
        }
        
        result.include("products", products);
        result.include("filter", filter);
    }

	@Path("/products/add")
	@Post
	@Public
	public void add(final Product product, String etiquetaCaixa, String etiquetaPallet) {
		
		User user = userInfo.getUser();
		
		product.setIdMasc( product.getIdMasc() );		
		product.setCreationDate( DateTimeUtils.now() );
		product.setLabelFileName(null);
		product.setLabelPalletFileName(null);
		
		if ( etiquetaCaixa != null && !etiquetaCaixa.isEmpty() ){
			Etiqueta labelBox = GuiGlobals.getDaoFactory().getEtiquetaDao().find( Integer.valueOf( etiquetaCaixa ) );
			if (labelBox !=null) product.setLabelFileName(labelBox);
		}		
		
		if ( etiquetaPallet != null && !etiquetaPallet.isEmpty() ){
			Etiqueta labelPallet = GuiGlobals.getDaoFactory().getEtiquetaDao().find( Integer.valueOf( etiquetaPallet ) );
			if (labelPallet !=null) product.setLabelPalletFileName(labelPallet);
		}
		
		validInsert( product );
		
		this.dao.add(product);

		result.include("user", user);
		result.include("notice", "Produto " + product.getIdMasc() + " adicionado com sucesso");
		result.redirectTo(this).list();
		
	}

	@Path("/products/view/{product.id}")
	@Get
	public void view(Product product) {
		
		User user = userInfo.getUser();
		
		result.include("user", user);
		
		if(product != null && product.getId() > 0) {
			product = dao.find( product.getId() );
			if(product != null) {
				product = product.clone();
			}
		}
				
		fillLists(true);
		result.include("product", product);
		result.include("user", user);
	}

	public void insert() {
	    result.forwardTo(this).view(null);
	}
	
	@Path("/products/remove/{product.id}")
	@Get	
	public void remove(Product product) {
	    try {
	    	daoFactory.beginTransaction();
	    	ProductDao dao = daoFactory.getProductDao();
		    dao.refresh(product);
	    	dao.delete(product);
	    	daoFactory.commit();
	    	result.include("notice", "Produto " + product.getIdMasc() + " removido com sucesso");
	    }
	    catch(Exception e) {
	    	daoFactory.rollback();
	    	
	    	//@@@@
	    	result.include("notice", "Não é possível excluir o produto  "  + product.getIdMasc() +  " porque existem relacionamentos com outras tabelas. Em vez de excluir, faça uma alteração marcando o produto como BLOQUEADO.");	    	
	    }
		result.use(Results.logic()).redirectTo(ProductsController.class).list(userInfo.getProductFilter());
	}	

	public void update(final Product product, String creationDate, String etiquetaCaixa, String etiquetaPallet) {
		
		product.setLabelFileName(null);
		product.setLabelPalletFileName(null);
				
		if ( etiquetaCaixa != null && !etiquetaCaixa.isEmpty() ){
			Etiqueta labelBox = GuiGlobals.getDaoFactory().getEtiquetaDao().find( Integer.valueOf( etiquetaCaixa ) );
			if (labelBox !=null) product.setLabelFileName(labelBox);
		}
		
		if ( etiquetaPallet != null && !etiquetaPallet.isEmpty() ){
			Etiqueta labelPallet = GuiGlobals.getDaoFactory().getEtiquetaDao().find( Integer.valueOf( etiquetaPallet ) );
			if (labelPallet !=null) product.setLabelPalletFileName(labelPallet);
		}
		
		validUpdate( product );
		
		if (creationDate != null){
			try {
				product.setCreationDate( DateTimeUtils.strToDateTime(creationDate) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.dao.update(product);
		result.include("notice", "Produto " + product.getIdMasc() + " atualizado com sucesso");
		result.use(Results.logic()).redirectTo(ProductsController.class).list();
	}
	
	@Path("/products/search")
	public void searchProduct(String id) {
		int code;
		try {
			code = Integer.parseInt(id);
		}
		catch(Exception e) {
			result.use(json()).from("").serialize();
			return;
		}
		
		Product prod = this.dao.find(code);
		if(prod != null)
			result.use(json()).from(prod.getDescription()).serialize();
		else
			result.use(json()).from("").serialize();			
	}	

	void validPermission(){
		
		validator.checking(new Validations() {{
		    that( userInfo.isPermittedComponentUpdate(ComponentPermission.CADASTRO_PRODUTOS), "product", "user_not_insert_products");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validInsert( final Product product){
		
		validator.checking(new Validations() {{
			
			boolean idDoesNotExists;
			boolean idNull = product.getIdMasc() != null;
			
			that(idNull, "product", "id_null");
			
			idDoesNotExists = dao.findByIdMasc(product.getIdMasc()) == null;
		    that(idDoesNotExists, "product", "id_already_exists");
		}});
		validator.onErrorUse(Results.logic()).redirectTo(ProductsController.class).view( product );
//		validator.onErrorUsePageOf(ProductsController.class).view( product );
		
		validUpdate( product );
		
	}
	
	void validUpdate( Product product ){
		
		if ( product.getUnit() == null )
			validator.add(new ValidationMessage("product", "Unidade de venda deve ser preenchida!"));
		
		if ( product.getUnitEtq() == null )
			validator.add(new ValidationMessage("product", "Unidade de etiqueta deve ser preenchida!"));
		
		if ( product.getWeighingStyle() == null || product.getWeighingStyle() == WeighingStyle.UNDEFINED )
			validator.add(new ValidationMessage("product", GuiGlobals.getBundle().getString("select_weighing_style")));
		
		/**
		 * Se o produto foi indicado que usa etiqueta especifica caixa, entao o preenchimento do codigo SIF é obrigatorio.
		 */
		if ( ( product.getLabelFileName() != null && product.getLabelFileName().isEtqEspPaoAcucar() ) || ( product.getLabelPalletFileName() != null && product.getLabelPalletFileName().isEtqEspPaoAcucar() ) )
		{
			if ( product.getCodSif() == null || product.getCodSif().trim().isEmpty() )
				validator.add(new ValidationMessage("product", GuiGlobals.getBundle().getString("valid_CodSif")));
		}
		
		if ( product.getNetWeight() > 0 && ( product.getMinWeight() != 0 || product.getMaxWeight() != 0 ) )
		{
			if ( product.getMinWeight() > product.getNetWeight() )
				validator.add(new ValidationMessage("product", GuiGlobals.getBundle().getString("valid_pesoMin_targetWeight")));
			
			if ( product.getMaxWeight() < product.getNetWeight() )
				validator.add(new ValidationMessage("product", GuiGlobals.getBundle().getString("valid_pesoMax_targetWeight")));
			
			if ( product.getMinWeight() > product.getMaxWeight() )
				validator.add(new ValidationMessage("product", GuiGlobals.getBundle().getString("valid_pesoMin_pesoMax")));
		}
		
//		validator.checking(new Validations() {{
//			Antonio pediu para tirar esta validacao por email em 27-04-2015
//			boolean eanDuplicated;
//			Product productExists; 
			
//			productExists = dao.findByEan13(product.getEan13());
//			eanDuplicated = ( productExists == null || productExists.getIdMasc().equals(product.getIdMasc() ) );			
//		    that(eanDuplicated, "product", "ean13_already_existis");
		    
//		    productExists = dao.findByDun14(product.getDun14());
//			eanDuplicated = ( productExists == null || productExists.getIdMasc().equals(product.getIdMasc() ) );
//		    that(eanDuplicated , "product", "dun14_already_existis");
			
			//- Validacao da especificacoes			
//		    that(product.getUnit().getId() != null , "product", " Unidade de venda deve ser preenchida");
		    
//		    that(product.getUnitEtq() != null , "product", "Unidade de etiqueta deve ser preenchida");
//		    that(product.getWeighingStyle() != null , "product", "Tipo de estoque deve ser preenchido");
		    			
//			if ( product.getTargetWeight() > 0 && ( product.getMinWeight() != 0 || product.getMaxWeight() != 0 ) )
//			{
//				that(product.getMinWeight() <= product.getTargetWeight(),"product", "valid_pesoMin_targetWeight");
//				that(product.getMaxWeight() >= product.getTargetWeight(),"product", "valid_pesoMax_targetWeight");
//				that(product.getMinWeight() <= product.getMaxWeight(),"product", "valid_pesoMin_pesoMax");
//			}
			
//			/**
//			 * Se o produto foi indicado que usa etiqueta especifica caixa, entao o preenchimento do codigo SIF é obrigatorio.
//			 */
//			if ( ( product.getLabelFileName() != null && product.getLabelFileName().isEtqEspPaoAcucar() ) || ( product.getLabelPalletFileName() != null && product.getLabelPalletFileName().isEtqEspPaoAcucar() ) ){
//				that( product.getCodSif() != null && !product.getCodSif().trim().isEmpty()  , "product", "valid_CodSif");
//			}
					    
//		    that(product.getWeighingStyle() != WeighingStyle.UNDEFINED , "product", "select_weighing_style");
		    		    
//		}});
		validator.onErrorUse(Results.logic()).redirectTo(ProductsController.class).view( product );
	
	}
	
	public void exportProductsToExcel(ProductFilter filter)
	{
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "cadastroDeProdutos");
		String fileFullName = getExportName( nameFile );
		
		if ( dao.exportObjProductToExcel(filter, fileFullName) ) 
		{
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do cadastro!");
			result.include("filter", filter);
			result.use(Results.logic()).redirectTo(ProductsController.class).list(filter);
		}
	}
	
	private String getExportName( String nameFile ){
		
		String path = servletContext.getRealPath("/") + GuiGlobals.getSeparadorIvertido() + "stocks_export"; 
		
		if (!new File(path).exists()) { // Verifica se o diretório existe.   
	    	(new File(path)).mkdir();   // Cria o diretório   
	    } 
		
		return path + GuiGlobals.getSeparadorIvertido() + nameFile;
		
	}
	
	public void showExcelOnBrowser( String fileFullName )
	{
		byte[] arquivo = null;
		File file = new File( fileFullName );
		
		try {
			arquivo = GuiGlobals.fileToByte( file );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		servletResponse.setHeader("Content-disposition", "attachment;filename="+fileFullName);
		servletResponse.setContentType("application/vnd.ms-excel");
		servletResponse.setContentLength(arquivo.length);
		
		ServletOutputStream ouputStream;
		try {
			ouputStream = servletResponse.getOutputStream();
			ouputStream.write(arquivo, 0, arquivo.length);
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getFileNameToExport( TypeExportReport opcao, String nameArq){
		String fileName = DateTimeUtils.getDateForDb() + "_" + DateTimeUtils.getTimeForDb() ;
		return fileName + "_" + nameArq + "." + opcao.getExtensao();
	}

}
