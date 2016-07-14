package br.com.cardeal.controller.desktop;

import br.com.cardeal.printer.PrintSerial;
import br.com.cardeal.printer.PrinterModel;
import br.com.cardeal.scale.ScaleModel;
import br.com.cardeal.scale.ScaleUnit;
import br.com.cardeal.services.TerminalService;
import br.com.cardeal.dao.TerminalDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.filter.TerminalFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Terminal;
import br.com.cardeal.model.User;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;

@Resource
public class TerminalsController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private String viewPg;
    private final TerminalDao dao;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;
        
	public TerminalsController
		(
		TerminalDao dao
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
	
	private void fillLists() {
		result.include("scaleModels", ScaleModel.values());				
		result.include("scaleUnits", ScaleUnit.values());				
		result.include("printerModels", PrinterModel.values());				
	}
	
	@Path("/terminals")
	@Get
	public void list() {
		validPermission();
		User user = userInfo.getUser();
		
		result.include("user", user);
        result.include("terminals", GuiGlobals.getDaoFactory().getTerminalDao().list(null));
        fillLists();
	}
	
	void validPermission()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.MANUTENCAO_TERIMNAIS), "manutenção", "user_not_access_manuten_terminal");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	@Path("/terminals/add")
	@Post
	@Public
	public void add(final Terminal terminal) {
		User user = userInfo.getUser();
		
		fillLists();
		
	    validInsert( terminal );
	    
	    TerminalService terminalService = new TerminalService();
	    terminalService.addTerminal( terminal );
				
		result.include("user", user);
		result.include("notice", "Terminal adicionado com sucesso");
		result.redirectTo(this).list();
	}
	
	private void validInsert( final Terminal terminal ){
		
		validator.checking(new Validations() {{
		    
	    	boolean IdDoesNotExist = GuiGlobals.getDaoFactory().getTerminalDao().find(terminal.getCompany().getId(), terminal.getIdTerminal()) == null;
	    	boolean companyDoesNotExist = GuiGlobals.getDaoFactory().getCompanyDao().find(terminal.getCompany().getId()) != null;
	    	
		    that(IdDoesNotExist, "terminal", "id_already_exists");
		    that(companyDoesNotExist, "terminal", "id_company_not_exists");
		    
		    if( !IdDoesNotExist || companyDoesNotExist){
		    	viewPg = "ADD";
		    }
		    
		}});
		
		validator.onErrorUsePageOf(this).viewAdd(terminal);
		
	}

	@Path("/terminals/viewAdd/{terminal.company.id}/{terminal.id}")
	@Get
	public void viewAdd(final Terminal terminal) {
		
		User user = userInfo.getUser();		
		result.include("user", user);
		result.include("terminal", terminal);		
		result.include("baudrate", PrintSerial.getListParam( PrintSerial.LIST_BAUDRATE ) );
		result.include("databits", PrintSerial.getListParam( PrintSerial.LIST_DATABITS ));
		result.include("stopbits", PrintSerial.getListParam( PrintSerial.LIST_STOPBITS ));
		result.include("parity", PrintSerial.getListParam( PrintSerial.LIST_PARITY ));
		
		fillLists();
	}
	
	@Path("/terminals/viewUpd/{terminal.id}")
	@Get
	public void viewUpd(final Terminal terminal) {
		
		User user = userInfo.getUser();
		result.include("user", user);
		
		this.dao.refresh(terminal);				
		result.include("terminal", terminal);
		result.include("baudrate", PrintSerial.getListParam( PrintSerial.LIST_BAUDRATE ) );
		result.include("databits", PrintSerial.getListParam( PrintSerial.LIST_DATABITS ));
		result.include("stopbits", PrintSerial.getListParam( PrintSerial.LIST_STOPBITS ));
		result.include("parity", PrintSerial.getListParam( PrintSerial.LIST_PARITY ));
		
		fillLists();
	}

	public void insert() {
		User user = userInfo.getUser();
		
		viewPg = "ADD";
		result.include("viewPg", viewPg);
	    result.forwardTo(this).viewAdd(null);
		result.include("user", user);
	}
	
	@Path("/terminals/remove/{terminal.id}")
	@Get	
	public void remove(final Terminal terminal) {
		
		this.dao.refresh(terminal);	    		
		TerminalService terminalService = new TerminalService();
		String msgReturnDelete = terminalService.removeTerminal( terminal );
	    if ( msgReturnDelete.equals("OK") )	    	
	    	result.include("notice", "Terminal " + terminal.getDescription() + " removido com sucesso!");
	    else
	    {
	    	result.include("notice", "Terminal " + terminal.getDescription() + " não pode ser removido! \n ERRO Retornado do banco de dados: " + msgReturnDelete);	    	
	    }
	    	
	    
		result.use(Results.logic()).redirectTo(TerminalsController.class).list();
	}	

	public void update(final Terminal terminal) {
				
		TerminalService terminalService = new TerminalService();
	    terminalService.updateTerminal( terminal );
		result.include("notice", "Terminal " + terminal.getDescription() + " atualizado com sucesso");
		result.use(Results.logic()).redirectTo(TerminalsController.class).list();
	}
	
	public void exportTerminalsToExcel(TerminalFilter filter){
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "cadastroDeTerminais");
		String fileFullName = getExportName( nameFile );
		
		if ( dao.exportObjTerminalToExcel(filter, fileFullName) ) {
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do cadastro!");
			result.use(Results.logic()).redirectTo(TerminalsController.class).list();
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
