package br.com.cardeal.controller.desktop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
import br.com.cardeal.dao.UnitDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Unit;
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
public class UnitsController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private final UnitDao dao;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;

	public UnitsController
		(
		UnitDao dao
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

	@Path("/units")
	@Get
	public void list() {
		validPermission();
		User user = userInfo.getUser();
		
        List<Unit> units = new ArrayList<Unit>();
        units = this.dao.list();
        
        result.include("user", user);
        result.include("units", units);
        
    }

	@Path("/units/add")
	@Post
	@Public
	public void add(final Unit unit) {
		User user = userInfo.getUser();
		
		validInsert( unit );
	    
		this.dao.add(unit);
		
		result.include("user", user);
		result.include("notice", "Unidade " + unit.getId() + " adicionada com sucesso");
		result.forwardTo(this).list();
	}

	@Path("/units/view/{unit.id}")
	@Get
	public void view(Unit unit) {
		User user = userInfo.getUser();
		
		result.include("user", user);
		
		if(unit != null)
			result.include("unit", dao.find(unit.getId()));
		else
		    result.include("unit", null);
	}

	public void insert() {
	    result.forwardTo(this).view(null);
	}
	
	@Path("/units/remove/{unit.id}")
	@Get	
	public void remove(Unit unit) {
		
		try {
			this.dao.refresh(unit);
			this.dao.delete(unit);
			
			result.include("notice", "Unidade " + unit.getId()
					+ " removida com sucesso");
			result.use(Results.logic()).redirectTo(UnitsController.class)
					.list();
		} 
		
		catch (ConstraintViolationException persistenceException) {
			result.include("notice", "Unidade " + unit.getId()	+ " nao pode ser removida");
			result.use(Results.logic()).redirectTo(UnitsController.class).list();
		}

	}
		

	public void update(final Unit unit) {
		
		validUpdate( unit );
		
		this.dao.update(unit);
		result.include("notice", "Unidade " + unit.getId() + " atualizada com sucesso");
		result.use(Results.logic()).redirectTo(UnitsController.class).list();
	}
	
	void validPermission(){
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.CADASTRO_UNIDADE), "units", "user_not_insert_units");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validInsert( final Unit unit ){
		
		validator.checking(new Validations() {{
			
			that( unit.getId() != null , "units", "id_null");
			
		    // checks if there is already an unit with the specified login
		    boolean idExists = dao.find(unit.getId()) != null;
		    that(!idExists, "units", "id_already_exists");
		}});

		// redirects to the index page if any validation errors occur.
	    if(validator.hasErrors()) {
	    	unit.setId(null);
	    	result.include("unit", unit);
	    }
		validator.onErrorForwardTo(this).view(unit);
		
		validUpdate( unit );
		
	}
	
	void validUpdate( final Unit unit ){
		
		validator.checking(new Validations() {{
	    	that(unit.getId() != null,"unit","id_null");
		}});	
		validator.onErrorUse(Results.logic()).redirectTo(UnitsController.class).view(unit);
	
	}
	
	public void exportUnitsToExcel(){
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "cadastroDeUnidades");
		String fileFullName = getExportName( nameFile );
		
		if ( dao.exportObjUnitToExcel(fileFullName) ) {
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do cadastro!");
			result.use(Results.logic()).redirectTo(UnitsController.class).list();
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
