package br.com.cardeal.services;

import br.com.cardeal.enums.Operation;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Historic;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.User;

public class PartnerService {

	private User user;
	
	public PartnerService() {
		user = GuiGlobals.getUserInfo().getUser();		
	}

	public PartnerService(User user) {
		this.user = user;		
	}
	
	//------------------------
	//remove um Partner -
	//------------------------	
	public void removePartner( Partner partner )
	{
		//instancia um historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.PARTNER_REMOVING);
		h.setOrder(null);
		h.setPartner(partner);
		h.setPrimaryQty(0);
		h.setProduct(null);
		h.setSecondaryQty(0);
		h.setStock(null);
		h.setTare(0);
		h.setTerminal(Setup.getTerminal());
		h.setCompany(Setup.getCompany());
		h.setUser(user);
		h.setPallet(null);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
			//grava o historico na base
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			GuiGlobals.getDaoFactory().getPartnerDao().delete(partner);		
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			GuiGlobals.getDaoFactory().rollback();
		}
		
	}	
	
	public void updatePartner(Partner partner) throws Exception {
		
		//instancia um historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.PARTNER_UPDATE);
		h.setOrder(null);
		h.setPartner(partner);
		h.setPrimaryQty(0);
		h.setProduct(null);
		h.setSecondaryQty(0);
		h.setStock(null);
		h.setTare(0);
		h.setTerminal(Setup.getTerminal());
		h.setCompany(Setup.getCompany());
		h.setUser(user);
		h.setPallet(null);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {									
			GuiGlobals.getDaoFactory().getPartnerDao().update(partner);
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
			throw(e);
		}
	}
	
	public void addPartnerByImport( Partner partner ) {
		addPartner( partner, Operation.PARTNER_INCLUDE_BY_IMPORT );
	}
	
	public void addPartner( Partner partner, Operation operation ) {
		
		Operation operationDefault = ( operation != null ? operation : Operation.PARTNER_INCLUDE );
		
		//instancia um historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation( operationDefault );
		h.setOrder(null);
		h.setPartner(partner);
		h.setPrimaryQty(0);
		h.setProduct(null);
		h.setSecondaryQty(0);
		h.setStock(null);
		h.setTare(0);
		h.setTerminal(Setup.getTerminal());
		h.setCompany(Setup.getCompany());
		h.setUser(user);
		h.setPallet(null);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {			
			GuiGlobals.getDaoFactory().getPartnerDao().add(partner);
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();			
		}
	}
	
}
