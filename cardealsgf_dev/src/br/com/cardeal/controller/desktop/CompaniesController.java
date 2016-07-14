package br.com.cardeal.controller.desktop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import br.com.cardeal.dao.CompanyDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Company;
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
public class CompaniesController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private final CompanyDao dao;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;

	public CompaniesController
		(
		CompanyDao dao
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
	

	
	@Path("/companies")
	public void list() 
	{
		validPermission();
		User user = userInfo.getUser();
		
        List<Company> companies = new ArrayList<Company>();
        List<Company> companiesFromDatabase = this.dao.list();
        for (Company company : companiesFromDatabase) {
            Company newCompany = new Company();
            newCompany.setId(company.getId());
            newCompany.setName(company.getName());
            companies.add(newCompany);
        }

        result.include("companies", companies);
        result.include("user", user);
    }

	@Path("/companies/add")
	@Post
	@Public
	public void add(final Company company) {
		User user = userInfo.getUser();
		
		validInsert( company );
	    
		this.dao.add(company);
		
		result.include("user", user);
		result.include("notice", "Companhia " + company.getId() + " adicionada com sucesso");
		result.redirectTo(this).list();
	}

	@Path("/companies/view/{company.id}")
	@Get
	public void view(Company company) {
		User user = userInfo.getUser();
		
		if(company != null)
			result.include("company", dao.find(company.getId()));
		else
		    result.include("company", null);
		
		result.include("user", user);
	}

	public void insert() {
	    result.forwardTo(this).view(null);
	}
	
	@Path("/companies/remove/{company.id}")
	@Get	
	public void remove(Company company) {
	    this.dao.refresh(company);
	    String deleteMessage = this.dao.delete(company);
	    if ( deleteMessage.equals("OK") )
	    {
			result.include("notice", "Filial " + company.getId() + " removida com sucesso");
	    }
	    else
	    {
	    	result.include("notice", "FILIAL " + company.getId() + " NÃO PODE SER REMOVIDA ==>  " + deleteMessage);
	    }
	    result.use(Results.logic()).redirectTo(CompaniesController.class).list();
	}	

	public void update(final Company company) {
		
		validUpdate( company );	
		
		this.dao.update(company);
		result.include("notice", "Companhia " + company.getId() + " atualizada com sucesso");
		result.use(Results.logic()).redirectTo(CompaniesController.class).list();
	}

	void validPermission()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.CADASTRO_FILIAIS), "company", "user_not_insert_companies");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validInsert(final Company company ){
		
		validator.checking(new Validations() {{
			
			that( company.getId() != null , "company", "id_null");
			
		    // checks if there is already an unit with the specified login
		    boolean idExists = dao.find(company.getId()) == null;
		    that(idExists, "company", "id_already_exists");
		}});

		// redirects to the index page if any validation errors occur.
	    if(validator.hasErrors()) {
	    	company.setId("  ");
	    	result.include("company", company);
	    }
		validator.onErrorForwardTo(this).view(company);
		
		validUpdate( company );
		
	}
	
	void validUpdate( final Company company ){
		
		validator.checking(new Validations() {{
	    	that(company.getId() != null,"company","id_null");
		}});	
		validator.onErrorUse(Results.logic()).redirectTo(CompaniesController.class).view(company);
	
	}
	
	public void exportCompaniesToExcel(){
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "cadastroDeFiliais");
		String fileFullName = getExportName( nameFile );
		
		if ( dao.exportObjCompanyToExcel(fileFullName) ) {
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do cadastro!");
			result.use(Results.logic()).redirectTo(CompaniesController.class).list();
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
