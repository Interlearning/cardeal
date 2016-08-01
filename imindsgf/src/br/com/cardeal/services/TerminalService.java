package br.com.cardeal.services;

import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.Terminal;

public class TerminalService {
			
	public TerminalService() {
		super();	
	}

	public void addTerminal(Terminal terminal) {
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			GuiGlobals.getDaoFactory().getTerminalDao().add(terminal);
			GuiGlobals.getDaoFactory().commit();
		}
		catch (Exception e){
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
		}
				
	}

	public void updateTerminal(Terminal terminal) {
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			GuiGlobals.getDaoFactory().getTerminalDao().update(terminal);
			GuiGlobals.getDaoFactory().commit();
		}
		catch (Exception e){
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
		}
		
	}

	public String removeTerminal(Terminal terminal) 
	{
		String msgReturn = "OK";
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			GuiGlobals.getDaoFactory().getTerminalDao().refresh(terminal);
			GuiGlobals.getDaoFactory().getTerminalDao().delete(terminal);
			GuiGlobals.getDaoFactory().commit();
		}
		catch (Exception e){
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
			msgReturn = e.getMessage();
		}
		
		return msgReturn;
		
	}	
	
}
