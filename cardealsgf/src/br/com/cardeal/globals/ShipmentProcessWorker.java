package br.com.cardeal.globals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.SwingWorker;
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.ItensShipment;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Stock;
import br.com.cardeal.views.MessageRun;
import br.com.cardeal.views.TelaPiscante;

public class ShipmentProcessWorker extends SwingWorker<Integer, String>{

    private ShipmentData shipmentData;    
    private Stock stock;
    private int stateShipmentProcess;
    private String left = "\\";
    private String right = "/";
    private String center = "--";
    private String currentProgressAnimation = left;

	public ShipmentProcessWorker(ShipmentData shipmentData){
    	this.shipmentData = shipmentData;
    }
	
	 public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}
 
    @Override
    public Integer doInBackground() throws Exception {      
        	
    	switch(shipmentData.getAction()) {
		
			case ShipmentData.ACTION_FIFO:
				return processFifo();
				
			case ShipmentData.ACTION_LABEL_PALLET:
				return updateProductOnGridOfLabelPallet();
			
			default:
				return 0;
    	}
				
	}        
        
   private int processFifo()
   {
	   double qtdDigitada = shipmentData.getQtdDigitada();
	   GuiGlobals.showMessage("Aguarde processamento", false);
	   List<Stock> stocks = shipmentData.getShipmentService().getProductsFifoOnStock( shipmentData.getProduct() );
	   List<Stock> stocksUtilized = new ArrayList<Stock>();
	   
	   /**
	    * Ordeno resultado por data de producao
	    */
	   stocks.sort( new Comparator<Stock>() 
		{  
			public int compare(Stock s1, Stock s2) 
			{  
				Stock p1 = s1;  
				Stock p2 = s2;  
				return p2.getManufactureDate().compareTo( p1.getManufactureDate() ) < 0 ? +1 : (p2.getManufactureDate().compareTo( p1.getManufactureDate() ) > 0 ? -1 : 0);  
           }
		});
	   
	   MessageRun msgRun = new MessageRun();
	   msgRun.start("Aguarde", "Aguarde processamento ... ");
	   if ( stocks != null && stocks.size() > 0 )
	   {
		   ItensShipment itemSelected = shipmentData.getItemSelected();
		   List<Stock> stocksToUpdateGrid = new ArrayList<Stock>();

		   for ( Stock stock : stocks )
		   {	
			   boolean found = false;
			   if ( !stocksToUpdateGrid.isEmpty() && stocksToUpdateGrid.size() > 0 )
				{
					for ( Stock stockToUpdate : stocksToUpdateGrid )
					{
						if ( stockToUpdate.getProduct().equals( stock.getProduct() ) )
						{
							found = true;
							stockToUpdate.setHowSell( stock.getHowSell() );
							stockToUpdate.setPrimaryQty( stockToUpdate.getPrimaryQty() + stock.getPrimaryQty() );
							stockToUpdate.setSecondaryQty( stockToUpdate.getSecondaryQty() + stock.getSecondaryQty() );
							stockToUpdate.setNet( stockToUpdate.getNet() + stock.getNet() );
							break;
						}
					}
				}
				
				if ( !found )
				{
					stocksToUpdateGrid.add(stock.cloneStock());
				}
				
				stock.setOrder( shipmentData.getShipmentService().getOrder() );
				qtdDigitada = qtdDigitada - ( new Double( shipmentData.getShipmentService().getQtyIssued(stock, itemSelected.getUnitId() ) ).intValue() );
				
				GuiGlobals.showMessage("Aguarde processamento ... " + currentProgressAnimation, false);
				
				if ( currentProgressAnimation.equals(left) )
				{
					currentProgressAnimation = right;
				}
				else if (currentProgressAnimation.equals(right))
				{
					currentProgressAnimation = center;
				}
				else
				{
					currentProgressAnimation = left;
				}
				
				stocksUtilized.add(stock);
				if ( qtdDigitada <= 0 ) break; 
		   }
		   
		   for ( Stock stock : stocksToUpdateGrid )
		   {
			   shipmentData.getDlgShipment().searchOnGridAndUpdate( stock, shipmentData.getShipmentTypeOperation() );			   
			   GuiGlobals.showMessage("Aguarde processamento ... ", false);
		   }
			
			shipmentData.getShipmentService().refreshMultipleStocks( stocksUtilized );
			GuiGlobals.showMessage("", false);
			msgRun.close();
	   }
	   else
	   {
		   GuiGlobals.showMessage("Não foi encontrado itens em estoque!");
	   }
	   
	   return ShipmentData.ACTION_FIFO;
	   
   }
   
   private int updateProductOnGridOfLabelPallet(){

	   String codbarScanner = shipmentData.getCodBar();
	   boolean vldInitOk = true;
	   
	   if ( codbarScanner != null && codbarScanner.length() == 8 )
	   {
			int idEtiqueta = Integer.parseInt( codbarScanner.substring(0, 6) );	
			Company company = shipmentData.getShipmentService().getCompanyById( codbarScanner.substring(6, 8) );
			
			if( company == null ) 
			{				
				GuiGlobals.getMain().showMessagePiscante("<html><center>Filial não encontrada!<br>" + codbarScanner.substring(8, 10).trim() + "</center></html>");
				vldInitOk = false;
			}
			// Antonio pediu para retirar esta validacao do sistema
//			else if ( !( company.getId().equals( Setup.getCompany().getId() ) ) )
//			{
//				GuiGlobals.getMain().showMessagePiscante("<html><center>Etiqueta não pertence a filial!<br>" + codbarScanner.trim() + "</center></html>");
//				vldInitOk = false;
//			}
			
			if ( vldInitOk )
			{
				GuiGlobals.closeDb();
				Pallet pallet = GuiGlobals.getDaoFactory().getStockDao().findPalletStocked(idEtiqueta);
				
				if ( pallet != null && pallet.getId() > 0 )
				{
					if ( pallet.getStatus() == PalletStatus.DELETED )
					{
						GuiGlobals.showMessageDlg("Palete [" + codbarScanner + "] foi excluído!");
						TelaPiscante tela = new TelaPiscante("Palete [" + codbarScanner + "] foi excluído!");
						tela.setVisible(true);
					}
					else if ( pallet.getStatus() == PalletStatus.OPEN )
					{
						GuiGlobals.showMessage("Palete [" + codbarScanner + "] está em montagem!");
						TelaPiscante tela = new TelaPiscante("Palete [" + codbarScanner + "] está em montagem!");
						tela.setVisible(true);
					}
					else if ( pallet.getOrder() != null )
					{
						GuiGlobals.showMessage("Palete [" + codbarScanner + "] já expedido - Nº pedido [" + pallet.getOrder().getIdPedidoImport() + "]!");
						TelaPiscante tela = new TelaPiscante("Palete [" + codbarScanner + "] já expedido - Nº pedido [" + pallet.getOrder().getIdPedidoImport() + "]!");
						tela.setVisible(true);
					}
					else if ( pallet.getStocks().size() == 0 )
					{
						TelaPiscante tela = new TelaPiscante("O Palete [" + codbarScanner + "] não tem caixas em estoque. Foram excluídas ou já foram expedidas!");
						tela.setVisible(true);
					}
					else if ( shipmentData.getDlgShipment().searchProductOnGrid( pallet.getProduct() ) == null )
					{
						GuiGlobals.showMessage("Produto [" + pallet.getProduct().getIdMasc() + "] não existe neste pedido!");
					}
					else
					{
						List<Stock> stocks = shipmentData.getShipmentService().getStocksByIdPallet( pallet.getId() );
						List<Stock> stocksToUpdateGrid = new ArrayList<Stock>();
						
						if ( stocks != null && stocks.size() > 0)
						{
						
							MessageRun msgRun = new MessageRun();
							msgRun.start("Aguarde", "Aguarde processamento ... ");
							
							for ( Stock stock : stocks )
							{
							
								boolean found = false; 
								
								stock.setOrder( shipmentData.getShipmentService().getOrder() );	
								
								if ( !stocksToUpdateGrid.isEmpty() && stocksToUpdateGrid.size() > 0 )
								{
									for ( Stock stockToUpdate : stocksToUpdateGrid )
									{
										if ( stockToUpdate.getProduct().equals( stock.getProduct() ) )
										{
											found = true;
											stockToUpdate.setHowSell( stock.getHowSell() );
											stockToUpdate.setPrimaryQty( stockToUpdate.getPrimaryQty() + stock.getPrimaryQty() );
											stockToUpdate.setSecondaryQty( stockToUpdate.getSecondaryQty() + stock.getSecondaryQty() );
											stockToUpdate.setNet( stockToUpdate.getNet() + stock.getNet() );
											break;
										}
									}
								}
								
								if ( !found )
								{
									stocksToUpdateGrid.add(stock.cloneStock());
								}
								
								GuiGlobals.showMessage("Aguarde processamento ... " + currentProgressAnimation, false);
								
								if ( currentProgressAnimation.equals(left) )
								{
									currentProgressAnimation = right;
								}
								else if (currentProgressAnimation.equals(right))
								{
									currentProgressAnimation = center;
								}
								else
								{
									currentProgressAnimation = left;
								}
							}
							
							for ( Stock stock : stocksToUpdateGrid )
							{
								GuiGlobals.showMessage("Aguarde processamento ... ", false);
								shipmentData.getDlgShipment().searchOnGridAndUpdate( stock, shipmentData.getShipmentTypeOperation() );
							}
							
							shipmentData.getShipmentService().refreshMultipleStocks(stocks);
							
							pallet.setOrder( shipmentData.getShipmentService().getOrder() );
							shipmentData.getShipmentService().updatePallet(pallet);
							GuiGlobals.showMessage("", false);
							msgRun.close();
						}
					}
				}
				else
				{
					String msg = "";
					boolean showDglPisc = true;
					if ( codbarScanner.trim().equals("00000001") )
					{
						msg = "Etiqueta Informativa!";
						showDglPisc = false;
					}
					else
					{
						msg = "Etiqueta Palete [" + codbarScanner + "] não encontrada ou foi já expedida!";
					}
					
					GuiGlobals.showMessage( msg );
					if ( showDglPisc )
					{
						TelaPiscante tela = new TelaPiscante( msg );
						tela.setVisible(true);
					}
				}
				
			}
			
			codbarScanner = "";
			
		}
		
		return ShipmentData.ACTION_LABEL_PALLET;
	}
   
	public int getStateShipmentProcess() {
		return stateShipmentProcess;
	}
	
	public void setStateShipmentProcess(int stateShipmentProcess) {
		this.stateShipmentProcess = stateShipmentProcess;
	}
}
