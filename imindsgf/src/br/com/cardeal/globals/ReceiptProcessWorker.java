package br.com.cardeal.globals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.SwingWorker;
import br.com.cardeal.enums.PurchaseOrderStatus;
import br.com.cardeal.enums.StockStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.filter.StockFilter;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.model.ShipmentItensDevolutionDetails;
import br.com.cardeal.model.Stock;
import br.com.cardeal.services.StockService;
import br.com.cardeal.views.ReceiptPurshase;

public class ReceiptProcessWorker extends SwingWorker<Integer, String> {

	private ReceiptData receiptData;
	private volatile boolean terminated = false;	
	
	@SuppressWarnings("unused")
	private static void failIfInterrupted() throws InterruptedException {
//	    if (Thread.currentThread().isInterrupted()) {
//	    	throw new InterruptedException("Processamento interrompido");
//	    }
	}	
	
	public void terminate() 
	{
		terminated = true;
	}
	
	public ReceiptProcessWorker(final ReceiptData receiptData) {
		this.receiptData = receiptData;
		terminated = false;
	}	
	
	@Override
	protected Integer doInBackground() throws Exception {
		
		switch(receiptData.getAction()) {
		
		case ReceiptData.ACTION_WAITING_FOR_WEIGHT_MANUAL:
			if (addProductManual() > 0 )
				return ReceiptData.ACTION_WAITING_FOR_WEIGHT_MANUAL;
			else
				return -2;
		
		case ReceiptData.ACTION_SAVE_ORDER:
			return saveAction();
			
		case ReceiptData.ACTION_SAVE_ORDER_TO_DEVOLUTION:
			return saveActionByDevolution(); 
			
		case ReceiptData.ACTION_FINISHED_ORDER_TO_DEVOLUTION:
			return finishedActionByDevolution();
			
		case ReceiptData.ACTION_REVERSE_ORDER:
			return -1; //printBox();
			
		case ReceiptData.ACTION_DELETE_ITEM:
			return deleteAction();
			
		case ReceiptData.ACTION_ORDER_FINISHED:
			return orderFinished();
			
		case ReceiptData.ACTION_WAITING_FOR_WEIGHT:
			return waitingForWeight();
			
		case ReceiptData.ACTION_SAVE_ORDER_FROM_WAITING_FOR_WEIGHT:
			return saveAction(false, false, false);
				
		}
		
		return 0;
	}
	
	private int addProductManual()
	{
		if ( stabilizingWeight() < 0 )
			return -2;
		
		if(terminated) 
		{
			return -2;
		}
		
		ReceiptPurshase dlg = (ReceiptPurshase)this.receiptData.getObject();
		ReceiptData receiptDataToAdd = dlg.addProduct(ReceiptData.ACTION_SAVE_ORDER_FROM_WAITING_FOR_WEIGHT, false, true);
		
		if ( receiptDataToAdd != null )
		{
			this.receiptData.setPurchaseOrder( receiptDataToAdd.getPurchaseOrder() );
			this.receiptData.setItemRowManut( receiptDataToAdd.getItemRowManut() );
			try 
			{
				if (  ReceiptData.ACTION_SAVE_ORDER == saveAction(false, false, false) )
				{
					dlg.mantemGridDados( dlg.OPTION_ADD_ITEM, receiptData.getItemRowManut() );
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			if ( waitZero( dlg.getProduct() ) < 0 )
				return -2;
		}
		else
		{
			if( waitZero( null ) < 0 )
				return -2;
		}
		return 1;
	}
	
	private int stabilizingWeight()
	{
		boolean processOk = false;
		while ( !processOk )
		{
			
			if(terminated) 
			{
				return -2;
			}
			
			GuiGlobals.sleep(100);
			
			processOk = GuiGlobals.isSettledScale();				
			
			if ( GuiGlobals.getCurrentNet() == 0 )
			{
				String msg = "Aguardando produto...";
				if ( !GuiGlobals.getMain().getLblMsg().getText().equals( msg ) )
					GuiGlobals.showMessage(msg, false);
				processOk = false;
			}
			else
			{
				GuiGlobals.showMessage("Estabilizando peso...", false);
				if ( GuiGlobals.isScaleSimulated() ) GuiGlobals.sleep(4000);
			}
		}
		return 1;
		
	}
	
	private Integer waitingForWeight() 
	{
		ReceiptPurshase dlg = (ReceiptPurshase)this.receiptData.getObject();
		while ( dlg.isInDlgToWeghing() )
		{
			if(terminated) 
			{
				return -2;
			}
			
			if ( !dlg.isFieldsRequiredOk(false) )
			{
//				GuiGlobals.showMessage("Aguardando parâmetros...", false);
				GuiGlobals.sleep(500);
			}
			else
			{
				
				if (addProductManual() < 0 )
					return -2;
			}
		}
		return ReceiptData.ACTION_WAITING_FOR_WEIGHT_FINISHED;
	}
	
	private int waitZero( Product product ) 
	{
		double currentNet = 0.0;
		double valueOpen = 0.0;
		double percentRemoveOfScale = ( product != null && product.getPercentRemoveProductOnScale() > 0 ) ? product.getPercentRemoveProductOnScale()/100 : 0 ;
		GuiGlobals.showMessage("Retire o produto da balança...");
		
		currentNet = GuiGlobals.getCurrentNet();
		valueOpen = ( currentNet * percentRemoveOfScale );
		while(currentNet > valueOpen ) 
		{			
			Thread.yield();
			GuiGlobals.sleep(60);
			currentNet = GuiGlobals.getCurrentNet();
			if(terminated) 
			{
				return -2;
			}
		}		
		GuiGlobals.showMessage("", false);
		GuiGlobals.sleep(500);
		return 1;
	}

	private Integer finishedActionByDevolution() {
		
		int processed = 0;
		
		try {
			if ( saveAction(false, false, false) == ReceiptData.ACTION_SAVE_ORDER )
			{
				
				List<ShipmentItensDevolutionDetails> itensCheckedDevolution = GuiGlobals.getDaoFactory().getPurchaseOrderDao().getItensDetailsDevolutionCheckedByPurchaseOrder(receiptData.getPurchaseOrder());
				StockService stockService = new StockService();
				
				for ( ShipmentItensDevolutionDetails itemDevolution : itensCheckedDevolution)
				{
					Stock stock = null;
					if	( itemDevolution.getStock() != null && itemDevolution.getStock().getId() > 0 )
					{
						stock = itemDevolution.getStock();
						stock.setStatus( StockStatus.STOCKED );
						stock.setOrder(null);
						stockService.devolutionByReceipt( stock );
					}
					else if	( itemDevolution.getProduct() != null && itemDevolution.getProduct().getId() > 0 )
					{
						Stock stockMP = GuiGlobals.getDaoFactory().getStockDao().findProductByTypeStock(itemDevolution.getProduct(), itemDevolution.getTypeStock());
						
						if ( stockMP != null && stockMP.getId() > 0 )
						{
							stockMP.setNet( stockMP.getNet() + itemDevolution.getNetWeight() );
							stockMP.setPrimaryQty( stockMP.getPrimaryQty() + (int) itemDevolution.getQty() );
							stockService.devolutionByReceipt( stockMP );
							
							stock = new Stock();
							stock.setId(0);
							stock.setProduct( stockMP.getProduct() );
							stock.setNet( itemDevolution.getNetWeight() );
							stock.setTypeStock( stockMP.getTypeStock() );
							stock.setCompany( Setup.getCompany() );
							stock.setEnterDate( new Date() );
						}
						else
						{
							GuiGlobals.showMessage("<html><center>Não foi encontrado no estoque o produto<br>[" + itemDevolution.getProduct().getIdMasc().trim() + "] no estoque de [" + itemDevolution.getProduct().getTypeStock().getDescricao().trim() + "]</center></html>");
							LogDeProcessamento.gravaLog("error", "Não foi encontrado no estoque o produto [" + itemDevolution.getProduct().getIdMasc().trim() + "] no estoque de [" + itemDevolution.getProduct().getTypeStock().getDescricao().trim() + "]");
						}
					}
					
					if	( stock != null )
					{
						String codPartner = null;
						
						if ( stock.getOrder() != null && stock.getOrder().getId() > 0 && stock.getOrder().getPartner() != null && stock.getOrder().getPartner().getId() > 0 )
						{
							codPartner = String.valueOf(stock.getOrder().getPartner().getId());
						}
						
						stockService.geraKardex(stock, "E", codPartner, null, null,null, "M");
						stockService.geraManuten(stock, "I","99");
					}
					else
					{
						LogDeProcessamento.gravaLog("error", "Registro gravado de maneira errada. Não tem produto nem estoque a ser devolvido. ID da tabela ShipmentItensDevolutionDetails [" + String.valueOf( itemDevolution.getId() ) + "]");
					}
				}
				
				processed = ReceiptData.ACTION_FINISHED_ORDER_TO_DEVOLUTION;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return processed;
	}

	private int saveActionByDevolution()
	{
		
		int processed = 0;
		
		try {
			if ( saveAction(true, true, false) == ReceiptData.ACTION_SAVE_ORDER )
			{
				processed = ReceiptData.ACTION_SAVE_ORDER_TO_DEVOLUTION;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return processed;
		
	}
	
	private Integer deleteAction() {
		
		int processed = 0;
		
		GuiGlobals.getDaoFactory().getPurchaseOrderDao().refresh( receiptData.getPurchaseOrder() );
		
		if ( receiptData.getPurchaseOrder() != null && receiptData.getPurchaseOrder().getItems().size() > 0 && receiptData.getItemRowManut() != null )
		{
			try
			{
				for ( PurchaseOrderItem purchaseOrderItem : receiptData.getPurchaseOrder().getItems() )
				{
					
					if ( purchaseOrderItem.getProduct().getIdMasc().equals( receiptData.getItemRowManut().getProduct().getIdMasc() ) )
					{
						GuiGlobals.getDaoFactory().beginTransaction();
						GuiGlobals.getDaoFactory().getPurchaseOrderDao().deleteItem( purchaseOrderItem );
						GuiGlobals.getDaoFactory().commit();
					}
					
				}
				processed = ReceiptData.ACTION_DELETE_ITEM;
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
			
		}
		
		return processed;
	}
	
	private int orderFinished()
	{
		
		int processed = 0;
		
		try 
		{
			processed = saveAction(false, true, true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return processed;
		
	}

	private int saveAction() throws Exception 
	{
		return saveAction(false, true, false);
	}
	
	private int saveAction( boolean isDevolution, boolean deleteItensBeforeSave, boolean isPurchaseFinalized ) throws Exception 
	{
		int processed = 0;
		int index = 1;
		List<PurchaseOrderItem> purchaseOrderItens = new ArrayList<PurchaseOrderItem>();
		PurchaseOrder purchaseOrder = receiptData.getPurchaseOrder();
		StockService stockService = new StockService();
				
		if(terminated) 
		{
			return -2;
		}
		
		purchaseOrderItens.addAll( receiptData.getPurchaseOrder().getItems() );		
		
		try
		{			
			GuiGlobals.getDaoFactory().beginTransaction();
			
			if ( purchaseOrder.getId() > 0 )
			{
				GuiGlobals.getDaoFactory().getPurchaseOrderDao().update( purchaseOrder );
			}
			else
			{
				GuiGlobals.getDaoFactory().getPurchaseOrderDao().add( purchaseOrder );
			}
			
			if ( deleteItensBeforeSave )
			{
				/**
				 * Deleto todos os itens do pedido para incluir os itens que o usuario manipulou
				 */
				GuiGlobals.getDaoFactory().getPurchaseOrderDao().deleteAllItensFromPurchaseOrder( purchaseOrder );
			}
			
			for ( PurchaseOrderItem purchaseOrdemItem : purchaseOrderItens )
			{
				purchaseOrdemItem.setIndex(index);
				GuiGlobals.getDaoFactory().getPurchaseOrderDao().addItem(purchaseOrdemItem);
				index++;
				
				if ( isDevolution )
				{
					/**
					 * Adicionando registro em tabela auxiliar de devolução
					 * Esses itens são os itens de estoque expedido, referente a cada purchaseOrdemItem
					 */
					if	(	TypeStock.GRANEL.equals( purchaseOrdemItem.getTypeStock() ) 
							|| TypeStock.MATERIA_PRIMA.equals( purchaseOrdemItem.getTypeStock() ))
					{
						ShipmentItensDevolutionDetails itemDevolution = new ShipmentItensDevolutionDetails();
						itemDevolution.setPurchaseOrderItem(purchaseOrdemItem);
						itemDevolution.setChecked(false);
						itemDevolution.setProduct(purchaseOrdemItem.getProduct());
						itemDevolution.setNetWeight(purchaseOrdemItem.getNet());
						itemDevolution.setGrossWeight(purchaseOrdemItem.getGrossWeight());
						itemDevolution.setQty(purchaseOrdemItem.getQuantity());
						itemDevolution.setTare(purchaseOrdemItem.getTare());
						itemDevolution.setUnit(purchaseOrdemItem.getUnit());
						itemDevolution.setTypeStock(purchaseOrdemItem.getTypeStock());
						GuiGlobals.getDaoFactory().getPurchaseOrderDao().addShipmentItemDevolution(itemDevolution);
					}
					else
					{
						StockFilter filter = new StockFilter();
						filter.setAsc(true);
						filter.setOnlyStocked(false);
						filter.setProductId( purchaseOrdemItem.getProduct().getId() );
						filter.setIdOrderImport( purchaseOrdemItem.getOrder().getId() );
						
						List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().list(filter);
						
						if ( stocks != null && stocks.size() > 0 )
						{
							for ( Stock stock : stocks )
							{
								ShipmentItensDevolutionDetails itemDevolution = new ShipmentItensDevolutionDetails();
								itemDevolution.setPurchaseOrderItem(purchaseOrdemItem);
								itemDevolution.setChecked(false);
								itemDevolution.setStock( stock );
								GuiGlobals.getDaoFactory().getPurchaseOrderDao().addShipmentItemDevolution(itemDevolution);
							}
						}
					}
				}
			}
			
			GuiGlobals.getDaoFactory().commit();
			GuiGlobals.getDaoFactory().getSession().flush();

			if ( isPurchaseFinalized )
			{
				GuiGlobals.getDaoFactory().getPurchaseOrderDao().refresh(purchaseOrder);
				
				for ( PurchaseOrderItem purchaseOrdemItem : purchaseOrder.getItems() )
				{
					stockService.addStockMPfromPurchaseOrder( purchaseOrdemItem );
				}
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		
		if( purchaseOrder.getStatus() == PurchaseOrderStatus.FINISHED_PURCHASE_ORDER )
		{
			processed = ReceiptData.ACTION_ORDER_FINISHED;
		}
		else
		{
			processed = ReceiptData.ACTION_SAVE_ORDER;
		}
		
		return processed;
	}

	@Override
	protected void process(final List<String> chunks) {

		for (final String string : chunks) {
			GuiGlobals.showMessage(string);
		}
	    
	}	
	  
}
