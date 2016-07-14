package br.com.cardeal.controller.desktop;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import br.com.cardeal.dao.EnterpriseDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Enterprise;
import br.com.cardeal.model.IdentifyLogisticPallet;
import br.com.cardeal.model.IdentifyLogisticProduct;
import br.com.cardeal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;

@Resource
public class EnterpriseController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private final EnterpriseDao dao;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;
    private IdentifyLogisticProduct identifyLogisticProduct = null;
    private IdentifyLogisticPallet identifyLogisticPallet = null;

	public EnterpriseController
			(	
			EnterpriseDao dao
			, Result result
			, UserInfo userInfo
			, Validator validator
			, ServletContext servletContext
			, HttpServletResponse servletResponse
			) 
	{
		this.dao = dao;
		this.result = result;
		this.userInfo = userInfo;
		this.validator = validator;
		this.servletContext = servletContext;		
		this.servletResponse = servletResponse;
	}
	
	@Path("/enterprise")
	public void list() {
		
		validPermission();
		User user = userInfo.getUser();
		
        List<Enterprise> enterprises = this.dao.list();

        result.include("enterprises", enterprises);
        result.include("user", user);
        
    }

	@Path("/enterprise/add")
	@Post
	@Public
	public void add(final Enterprise enterprise) {
		
		User user = userInfo.getUser();
		
		validInsert( enterprise );
	    
		this.dao.add( enterprise );
		
		result.include("user", user);
		result.include("notice", "Empresa " + enterprise.getId() + " adicionada com sucesso");
		result.redirectTo(this).list();
	}

	@Path("/enterprise/view/{enterprise.id}")
	@Get
	public void view(Enterprise enterprise) {
		User user = userInfo.getUser();
		
		if(enterprise != null)
			result.include("enterprise", dao.find(enterprise.getId()));
		else
		    result.include("enterprise", null);
		
		result.include("user", user);
	}

	public void insert() {
	    result.forwardTo(this).view(null);
	}
	
	@Path("/enterprise/remove/{enterprise.id}")
	@Get	
	public void remove(Enterprise enterprise) {
	    this.dao.refresh(enterprise);
	    
	    String deleteMessage = this.dao.delete(enterprise);
	    if ( deleteMessage.equals("OK") )
	    {
	    	result.include("notice", "Empresa " + enterprise.getId() + " removida com sucesso");
	    }
	    else
	    {
	    	result.include("notice", "EMPRESA " + enterprise.getId() + " NÃO PODE SER REMOVIDA ==>  " + deleteMessage);
	    }	    
		result.use(Results.logic()).redirectTo(EnterpriseController.class).list();
	}	
		
	public void update(final Enterprise enterprise) {
		
		validUpdate( enterprise );	

		try{
			identifyLogisticProduct = GuiGlobals.getDaoFactory().getIdentifyLogisticProductDao().findCurrentIdentifyByVarLogistic();
			identifyLogisticProduct.setIdBase( enterprise.getCurrentIdBaseBox() );
			identifyLogisticProduct.setVariavelLogistica( enterprise.getCurrentVarLogisctBox() );
		}catch (Exception e) {
			e.printStackTrace();
		}

		try{
			identifyLogisticPallet = GuiGlobals.getDaoFactory().getIdentifyLogisticPalletDao().findCurrentIdentifyByVarLogistic();
			identifyLogisticPallet.setIdBase( enterprise.getCurrentIdBasePallet() );
			identifyLogisticPallet.setVariavelLogistica( enterprise.getCurrentVarLogisctPallet() );
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		this.dao.updateupdateWhitSeqLogistics( enterprise, identifyLogisticProduct, identifyLogisticPallet );
		result.include("notice", "Empresa " + enterprise.getId() + " atualizada com sucesso");
		result.use(Results.logic()).redirectTo(EnterpriseController.class).list();
	}

	void validPermission()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.CADASTRO_EMPRESAS), "enterprise", "user_not_insert_enterprises");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validInsert(final Enterprise enterprise ){
		
		validator.checking(new Validations() {{
			
		    // checks if there is already an unit with the specified login
		    boolean idExists = dao.find(enterprise.getId()) == null;
		    that(idExists, "enterprise", "id_already_exists");
		}});

		// redirects to the index page if any validation errors occur.
	    if(validator.hasErrors()) {
	    	enterprise.setId(0);
	    	result.include("enterprise", enterprise);
	    }
		validator.onErrorForwardTo(this).view(enterprise);		
		
	}
	
	void validUpdate( final Enterprise enterprise ){
		
		validator.checking(new Validations() {{
		
			that(enterprise.getCurrentIdBaseBox() <= enterprise.getMaxIdSequenceLogisticBox(),"enterprise","seq_logistic_box_invalid_number");
			that(enterprise.getCurrentIdBasePallet() <= enterprise.getMaxIdSequenceLogisticPallet(),"enterprise","seq_logistic_pallet_invalid_number");
			
			that(enterprise.getCurrentVarLogisctBox() <= 9,"enterprise","var_logistic_box_invalid_number");
			that(enterprise.getCurrentVarLogisctPallet() <= 9,"enterprise","var_logistic_pallet_invalid_number");
			
	    	that(enterprise.getId() > 0,"enterprise","id_null");
	    	
		}});	
		validator.onErrorUse(Results.logic()).redirectTo(EnterpriseController.class).view( enterprise );
	
	}
	
	public void exportEnterprisesToExcel(){
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "cadastroDeEmpresa");
		String fileFullName = getExportName( nameFile );
		
		if ( dao.exportObjEnterpriseToExcel(fileFullName) ) {
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do cadastro!");
			result.use(Results.logic()).redirectTo(EnterpriseController.class).list();
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
