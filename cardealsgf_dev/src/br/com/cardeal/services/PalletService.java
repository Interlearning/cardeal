package br.com.cardeal.services;

import java.util.List;
import br.com.cardeal.enums.Operation;
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.Enterprise;
import br.com.cardeal.model.Historic;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Stock;
import br.com.cardeal.model.Terminal;
import br.com.cardeal.model.User;

public class PalletService {
		
	private User user;
	
	public PalletService() {
		user = GuiGlobals.getUserInfo().getUser();		
	}

	public PalletService(User user) {
		this.user = user;		
	}
	
	//------------------------------------------------
	//remove um item numerado e que existe em estoque
	//------------------------------------------------
	public void leaveStock(Pallet pallet, Operation operation, Terminal terminal) 
	{
		leaveStock(pallet, operation, terminal, "99");
	}
	public void leaveStock(Pallet pallet, Operation operation, Terminal terminal, String motivo) 
	{
		boolean isPalletUpdateOk = false;
		
		//-----------------
		//- Exclui pallet -
		//-----------------
		pallet.setStatus(PalletStatus.DELETED);
		
		//-------------------
		//- Grava historico -
		//-------------------
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(operation);
		h.setOrder(null);
		h.setPartner(null);
		h.setPrimaryQty(0);
		h.setProduct(pallet.getProduct());
		h.setSecondaryQty(0);
		h.setStock(null);
		h.setTare(0);
		h.setTerminal(terminal);
		h.setCompany(pallet.getCompany());
		h.setUser(user);
		h.setPallet(pallet);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
						
			GuiGlobals.getDaoFactory().getPalletDao().update(pallet);
						
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			
			GuiGlobals.getDaoFactory().commit();
			isPalletUpdateOk = true;
			
		}
		catch(Exception e) 
		{
			if ( !isPalletUpdateOk ) GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
			LogDeProcessamento.gravaLog("error", "Falha na atualização do palete. ->" + e.getMessage(), true);
		}
		
		if ( isPalletUpdateOk )
		{
			//-----------------------------
			//- Busca as caixas do pallet -
			//-----------------------------
			List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().listStockOfPalletStocked( pallet.getId() );
			
			//--------------------------------------
			//- Baixa estoque das caixas do pallet -
			//--------------------------------------
			StockService stockService = new StockService();
			for ( Stock stock : stocks ){
				stockService.leaveStock(stock, Operation.STOCK_ROLLBACK, terminal, true, motivo);
			}
		}
	}
	
	//------------------------------------------------
	//insere um item em estoque.	
	//------------------------------------------------
	public Pallet generatePalletId( Product product, Terminal terminal) throws Exception {
		
		//instancia o estoque nulo na base para pegar o proximo ID
		Pallet pallet = new Pallet();
		pallet.setOpenDate(DateTimeUtils.now());
	    pallet.setProduct(product);
	    pallet.setStatus(PalletStatus.OPEN);
	    pallet.setTerminal(terminal);
	    pallet.setCompany(Setup.getCompany());
	    
	    //instancia um historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.PALLET_OPEN);
		h.setOrder(null);
		h.setPartner(null);
		h.setPrimaryQty(0);
		h.setProduct(pallet.getProduct());
		h.setSecondaryQty(0);
		h.setStock(null);
		h.setTare(0);
		h.setTerminal(terminal);
		h.setCompany(pallet.getCompany());
		h.setUser(user);
		h.setPallet(pallet);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {

			// insere pallet
		    GuiGlobals.getDaoFactory().getPalletDao().add(pallet);		
			
			//grava o historico na base
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			
			GuiGlobals.getDaoFactory().commit();
			return pallet;
		}
		catch(Exception e) {			
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
			throw(e);
		}		
	}
	
	public Pallet generatePalletIdNoCommited( Product product, Terminal terminal) throws Exception {
		
		//instancia o estoque nulo na base para pegar o proximo ID
		Pallet pallet = new Pallet();
		pallet.setOpenDate(DateTimeUtils.now());
	    pallet.setProduct(product);
	    pallet.setStatus(PalletStatus.OPEN);
	    pallet.setTerminal(terminal);
	    pallet.setCompany(Setup.getCompany());
		
	    //instancia um historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.PALLET_OPEN);
		h.setOrder(null);
		h.setPartner(null);
		h.setPrimaryQty(0);
		h.setProduct(pallet.getProduct());
		h.setSecondaryQty(0);
		h.setStock(null);
		h.setTare(0);
		h.setTerminal(terminal);
		h.setCompany(pallet.getCompany());
		h.setUser(user);
		h.setPallet(pallet);
	    
		try {			
		    
			//insere pallet
		    GuiGlobals.getDaoFactory().getPalletDao().add(pallet);		
			
			//grava o historico na base
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			
			return pallet;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw(e);
		}		
	}
	
	public void removePallet( Pallet pallet ) throws Exception {
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
			GuiGlobals.getDaoFactory().getPalletDao().delete(pallet);		
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			e.printStackTrace();
			GuiGlobals.getDaoFactory().rollback();
			throw(e);
		}		
	}	
	
	public void updatePallet(Pallet pallet, Terminal terminal ) throws Exception {
		
		//instancia um historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.PALLET_UPDATE);
		h.setOrder(null);
		h.setPartner(null);
		h.setPrimaryQty(0);
		h.setProduct(pallet.getProduct());
		h.setSecondaryQty(0);
		h.setStock(null);
		h.setTare(0);
		h.setTerminal(terminal);
		h.setCompany(pallet.getCompany());
		h.setUser(user);
		h.setPallet(pallet);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
									
			GuiGlobals.getDaoFactory().getPalletDao().update(pallet);
			
			//grava o historico na base
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
			throw(e);
		}
	}
	
	public void closePallet(Pallet pallet, Terminal terminal ) {
		
		Enterprise enterprise = null;
		
		//insere o estoque na base						
		pallet.setCloseDate(DateTimeUtils.now());
		pallet.setStatus(PalletStatus.CLOSED);
		
		if ( pallet.getIdentifyLogisticPallet() != null ){
			enterprise = GuiGlobals.getEnterprise();
			enterprise.setCurrentIdBasePallet( pallet.getIdentifyLogisticPallet().getIdBase() );
			enterprise.setCurrentVarLogisctPallet( pallet.getIdentifyLogisticPallet().getVariavelLogistica() );
			GuiGlobals.setEnterprise(enterprise);
		}
		
		//instancia um historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.PALLET_CLOSED);
		h.setOrder(null);
		h.setPartner(null);
		h.setPrimaryQty(0);
		h.setProduct(pallet.getProduct());
		h.setSecondaryQty(0);
		h.setStock(null);
		h.setTare(0);
		h.setTerminal(terminal);
		h.setCompany(pallet.getCompany());
		h.setUser(user);
		h.setPallet(pallet);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
			
			// insere pallet
			GuiGlobals.getDaoFactory().getPalletDao().update(pallet);
			
			//grava o historico na base
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			
			if ( enterprise != null ){
				GuiGlobals.getDaoFactory().getEnterpriseDao().update(enterprise);
			}
			
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();			
		}
	}
	
}
