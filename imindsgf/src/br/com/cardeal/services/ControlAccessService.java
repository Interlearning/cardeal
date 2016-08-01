package br.com.cardeal.services;

import java.util.List;
import br.com.cardeal.dao.ControlAccessDao;
import br.com.cardeal.enums.ProcessWorker;
import br.com.cardeal.filter.ControlAccessFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.ControlAccess;
import br.com.cardeal.model.Terminal;
import br.com.cardeal.model.User;

public class ControlAccessService {
		
	private User user;	
	private Company company;
	private Terminal terminal;
	private String macAddress;
	
	public ControlAccessService() {
		GuiGlobals.refreshDaoFactory();
		user = GuiGlobals.getUserInfo().getUser();		
		company = Setup.getCompany();
		terminal = Setup.getTerminal();
		macAddress = GuiGlobals.getCurrentMacAddress();
	}
	
	public void closeService() {		
		GuiGlobals.getDaoFactory().close();
	}	
	
//	public void removeAccessOfProcess( ProcessWorker process ) throws Exception {
//		
//		ControlAccessDao controlAccessDao = daoFactory.getControlAccessDao(); 
//		ControlAccess userControlAccess = controlAccessDao.findKeyAccess(company, terminal, process);	
//		
//		if ( userControlAccess != null ){
//		
//			daoFactory.beginTransaction();
//		
//			try {
//				controlAccessDao.delete( userControlAccess );		
//				daoFactory.commit();
//			}
//			catch(Exception e) {
//				e.printStackTrace();
//				daoFactory.rollback();
//				throw(e);
//			}
//			
//		}
//		
//	}
	
	public void removeAccessOfFilialAndTerminal(){
		
		ControlAccessDao controlAccessDao = GuiGlobals.getDaoFactory().getControlAccessDao(); 
		ControlAccessFilter filter = new ControlAccessFilter();
		
		filter.setCompany(company);
		filter.setTerminal(terminal);
		
		List<ControlAccess> controlsAccess = controlAccessDao.list(filter);	
		
		if ( controlsAccess != null ){
		
			GuiGlobals.getDaoFactory().beginTransaction();
		
			try {
				
				for ( ControlAccess access : controlsAccess ){
					controlAccessDao.delete( access );
				}
				
				GuiGlobals.getDaoFactory().commit();
			}
			catch(Exception e) {
				e.printStackTrace();
				GuiGlobals.getDaoFactory().rollback();
				throw(e);
			}
			
		}
		
	}
	
	public void updateAccess( ProcessWorker process, ControlAccess userControlAccess ) throws Exception {
		
		ControlAccessDao controlAccessDao = GuiGlobals.getDaoFactory().getControlAccessDao();
		
		if ( userControlAccess == null){
			userControlAccess = controlAccessDao.findKeyAccess(company, terminal, process);
		}
		
		if ( userControlAccess != null ){
		
			GuiGlobals.getDaoFactory().beginTransaction();
		
			try {				
				userControlAccess.setDateLastAccess( DateTimeUtils.now() );
				controlAccessDao.update( userControlAccess );		
				GuiGlobals.getDaoFactory().commit();
			}
			catch(Exception e) {
				e.printStackTrace();
				GuiGlobals.getDaoFactory().rollback();
				throw(e);
			}
			
		}
		
	}
	
	public boolean insertAccess( ProcessWorker process ) throws Exception {
		
		boolean continuar = true;
		ControlAccessDao controlAccessDao = GuiGlobals.getDaoFactory().getControlAccessDao();
		ControlAccess userControlAccess = 	controlAccessDao.findKeyAccess(company, terminal, process);
		
		if ( userControlAccess == null ) {
			
			ControlAccess newUserControlAccess = new ControlAccess();
			
			newUserControlAccess.setCompany(company);
			newUserControlAccess.setProcess(process);
			newUserControlAccess.setTerminal(terminal);
			newUserControlAccess.setUser(user);
			newUserControlAccess.setDateTimeInsert( DateTimeUtils.now() );
			newUserControlAccess.setDateLastAccess( DateTimeUtils.now() );
			newUserControlAccess.setMacAddress(macAddress);
			
			GuiGlobals.getDaoFactory().beginTransaction();
			
			try {				
				controlAccessDao.add( newUserControlAccess );		
				GuiGlobals.getDaoFactory().commit();
			}
			catch(Exception e) {
				continuar = false;
				GuiGlobals.showMessageDlg("Não foi possível conectar ao servidor. Tente novamente!");
				e.printStackTrace();
				GuiGlobals.getDaoFactory().rollback();
				throw(e);
			}
			
		}
		else if ( userControlAccess.getMacAddress().equals( macAddress ) ) {
			updateAccess( process, userControlAccess );
		}
		else{
			continuar = false;
			GuiGlobals.showMessageDlg("Já existe usuário conectado neste processo [Filial: " + company.getId() + " / Terminal: " + terminal.getIdTerminal() + " / Usuario: " + userControlAccess.getUser().getName() + " / Mac Address: " + macAddress + "]");
		}
				
		return continuar;
		
	}
	
}
