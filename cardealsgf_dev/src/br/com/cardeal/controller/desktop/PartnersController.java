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
import br.com.cardeal.dao.PartnerDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.PartnerStyle;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.filter.PartnerFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Partner;
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
public class PartnersController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private final PartnerDao dao;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;

	public PartnersController
		(
		PartnerDao dao
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
	
	private void fillLists(boolean removeUndefined) {
		if(removeUndefined)
			result.include("partnerStyles", ArrayUtils.removeElement(PartnerStyle.values(), PartnerStyle.values()[0]));
		else
			result.include("partnerStyles", PartnerStyle.values());
	}	
	
	@Path("/partners")
	public void list() {
		validPermission();
		User user = userInfo.getUser();
		
		result.include("user", user);
		fillLists(false);
        result.include("partners", null);
    }

	
	@Path("/partners/list")
	public void list(PartnerFilter filter) {
		
		User user = userInfo.getUser();
		
		fillLists(false);
        List<Partner> partners = new ArrayList<Partner>();
        List<Partner> partnersFromDatabase = this.dao.list(filter);
        for (Partner partner : partnersFromDatabase) {
            Partner newPartner = new Partner();
            newPartner.setId(partner.getId());
            newPartner.setName(partner.getName());
            newPartner.setCodigoExterno(partner.getCodigoExterno());
            newPartner.setCnpj(partner.getCnpj());
            newPartner.setPartnerStyle(partner.getPartnerStyle());
            partners.add(newPartner);
        }

        result.include("partners", partners);
        result.include("filter", filter);
		result.include("user", user);
    }

	@Path("/partners/add")
	@Post
	@Public
	public void add(final Partner partner) {
		
		User user = userInfo.getUser();
		
		partner.setCnpj(Utils.clearCnpj(partner.getCnpj()));
		
		validInsert( partner );
		
		this.dao.add(partner);
		
		result.include("user", user);
		result.include("notice", "Cliente/Fornecedor " + partner.getId() + " adicionado com sucesso");
		result.redirectTo(this).list();
	}

	@Path("/partners/view/{partner.id}")
	@Get
	public void view(Partner partner) {
		User user = userInfo.getUser();
		fillLists(true);
		if(partner != null)
			result.include("partner", dao.findById(partner.getId()));
		else
		    result.include("partner", null);
		
		result.include("user", user);
	}

	public void insert() {
	    result.forwardTo(this).view(null);
	}
	
	@Path("/partners/remove/{partner.id}")
	@Get	
	public void remove(Partner partner) 
	{
	    String deleteMessage = this.dao.delete(partner);
	    if ( deleteMessage.equals("OK") )
	    {
			result.include("notice", "Cliente/Fornecedor " + partner.getId() + " removida com sucesso");
	    }
	    else
	    {
	    	result.include("notice", "Cliente/Fornecedor " + partner.getId() + " NÃO PODE SER REMOVIDA ==>  " + deleteMessage);
	    }
		result.use(Results.logic()).redirectTo(PartnersController.class).list();
	}	

	public void update(final Partner partner) {
		
		partner.setCnpj(Utils.clearCnpj(partner.getCnpj()));
		
		validUpdate( partner );
		
		this.dao.update(partner);
		result.include("notice", "Cliente/Fornecedor " + partner.getId() + " atualizado com sucesso");
		result.use(Results.logic()).redirectTo(PartnersController.class).list();
	}
	
	@Path("/partners/search")
	public void searchPartner(int code) {
//		String code;
//		try {
//			code = id;
//		}
//		catch(Exception e) {
//			result.use(json()).from("").serialize();
//			return;
//		}
		
		Partner p = this.dao.findById( code );
		if(p != null)
			result.use(json()).from(p.getName()).serialize();
		else
			result.use(json()).from("").serialize();			
	}
	
	void validPermission()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.CADASTRO_CLIENTE_FORNECEDOR), "partner", "user_not_insert_partners");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validInsert( final Partner partner ){
		
		validUpdate( partner );
		
	}
	
	void validUpdate( final Partner partner ){
		
		final Partner recPartner = dao.findByCnpj(partner.getCnpj());
		validator.checking(new Validations() {{
		    boolean cnpjExists = (recPartner != null) && (recPartner.getId() != partner.getId()); 
		    that(!cnpjExists, "partner", "cgc_already_exists");
		}});	
		validator.onErrorUse(Results.logic()).redirectTo(PartnersController.class).view(partner);		
	
	}
	
	public void exportPartnersToExcel(PartnerFilter filter)
	{
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "cadastroDeClientesFornecedores");
		String fileFullName = getExportName( nameFile );
		
		if ( dao.exportObjPartnerToExcel(filter, fileFullName) ) {
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do cadastro!");
			result.use(Results.logic()).redirectTo(PartnersController.class).list();
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
