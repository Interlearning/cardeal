package br.com.cardeal.controller.desktop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import br.com.cardeal.dao.DaoFactory;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.filter.HistoricFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Historic;
import br.com.cardeal.model.ServerSetup;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;

@Resource
public class HistoricsController 
{

	private final Validator validator;
	private final UserInfo userInfo;
    private final Result result;    
    @SuppressWarnings("unused")
	private final ServletContext context;    
    private final DaoFactory daoFactory;  

	public HistoricsController(
			Validator validator,
			UserInfo userInfo,
			Result result, 			 
			ServletContext context,
			DaoFactory daoFactory
			) 
	{
		this.result = result;		
		this.context = context;
		this.daoFactory = daoFactory;
		this.validator = validator;
		this.userInfo = userInfo;
	}		
	
	@Path("/historics")
	@Get
	public void list() {		
		validPermission();
        result.include("historics", null);
        
	}	
	
	void validPermission()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.RELATORIO_PRODUCAO_HISTORICO), "historic", "user_not_access_report_historic");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	@Path("/historics/list")
	public void list(String dateBegin, String dateEnd, HistoricFilter filter) {
				
		if ( dateBegin != null ){
			
			try {
				filter.setDate1( DateTimeUtils.strToDateTime(dateBegin) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		
		if ( dateEnd != null ){
			try {
				filter.setDate2( DateTimeUtils.strToDateTime(dateEnd) );
			} catch (Exception e) {			
				e.printStackTrace();
			}
		}
		
        List<Historic> historics = new ArrayList<Historic>();
        historics = this.daoFactory.getHistoricDao().list(filter);
         
        /**
         * @author samuel
         * comentei estes dois campos para que o java nao encaminhe
         * para o jsp as datas de inicio e fim, que deixam os campos 
         * preenchidos e o java script nao consegue ter poder de limpa-los
         */
        	result.include("date1", dateBegin);
        	result.include("date2", dateEnd);
        
        result.include("historics", historics);
       // result.include("filter", filter);
    }	

	
	/**
	 * metodo cria e exporta dados do grid de consulta para uma planilha de 
	 * excel.
	 * Salva os dados no diretorio
	 * @author samuel
	 * @param filter bool
	 */
	public void exportObjHistoricToExcel(String dateBegin, String dateEnd, HistoricFilter filter){
		
		
		if ( dateBegin != null ){
			
			try {
				filter.setDate1( DateTimeUtils.strToDateTime(dateBegin) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		if ( dateEnd != null ){
			try {
				filter.setDate2( DateTimeUtils.strToDateTime(dateEnd) );
			} catch (Exception e) {			
				e.printStackTrace();
			}
		}
	
		List<Historic> historics = GuiGlobals.getDaoFactory().getHistoricDao().list(filter);
	
	if ( historics != null ) {
		
		String fileFullName = getExportName( TypeExportReport.EXCEL_CSV, "historico" );
		
		OutputStream out;
		File file = new File(fileFullName);
		
		try {
			out = new FileOutputStream(file);
			
			// Editando cabe√ßalho das caixas do pallet
			out.write("Data".getBytes());
			out.write(";".getBytes());	
			out.write("Usu·rio".getBytes());
			out.write(";".getBytes());
			out.write("Filial".getBytes());
			out.write(";".getBytes());
			out.write("Terminal".getBytes());
			out.write(";".getBytes());				
			out.write("AÁ„o".getBytes());
			out.write(";".getBytes());				
			out.write("\r\n".getBytes());
			
			// Editando dados das caixas do pallet
			for(Historic historic : historics) {
				
				out.write(String.valueOf(historic.getDateTimeFormat()).getBytes());
				out.write(";".getBytes());				
				out.write(String.valueOf(historic.getUser().getName()).getBytes());
				out.write(";".getBytes());
				out.write(String.valueOf(historic.getCompany().getId()).getBytes());
				out.write(";".getBytes());
				out.write(String.valueOf(historic.getTerminal().getId()).getBytes());
				out.write(";".getBytes());
				out.write(String.valueOf(historic.getOperation().getName()).getBytes());
				out.write(";".getBytes());
				out.write("\r\n".getBytes());
			}			
			out.close();
		    			    
		    result.include("notice", "Arquivo CSV exportado com sucesso para: " + fileFullName);
			result.use(Results.logic()).redirectTo(HistoricsController.class).list();
		    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}else{
		result.include("notice", "Pallet  n„o encontrado!");
		result.use(Results.logic()).redirectTo(HistoricsController.class).list();
	
GuiGlobals.closeDb(); // Fecha conex√µes com o banco
		
	}
}
private String getExportName( TypeExportReport opcao, String nameArq ){
		
		ServerSetup serverSetup = GuiGlobals.getDaoFactory().getServerSetupDao().find();
		String path = serverSetup.getStoreDirectory();
		String fileName = DateTimeUtils.getDateForDb() + "_" + DateTimeUtils.getTimeForDb() ;
		GuiGlobals.closeDb(); // Fecha conex√µes com o banco
		return path + GuiGlobals.getSeparador() + fileName + "_" + nameArq + "." + opcao.getExtensao();
		
	}
	
}
