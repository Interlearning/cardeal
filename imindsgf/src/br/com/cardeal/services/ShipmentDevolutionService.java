package br.com.cardeal.services;

import java.util.List;
import javax.swing.JOptionPane;
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.enums.StockStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.interfaces.ShipmentDevolutionInterface;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.ItensShipmentDevolution;
import br.com.cardeal.model.ItensShipmentDoneDevolution;
import br.com.cardeal.model.ItensShipmentPartialDevolution;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.model.ShipmentItensDevolutionDetails;
import br.com.cardeal.model.Stock;
import br.com.cardeal.table.TableItensShipmentDevolutionModel;
import br.com.cardeal.table.TableItensShipmentDoneDevolutionModel;
import br.com.cardeal.table.TableItensShipmentPartialDevolutionModel;
import br.com.cardeal.views.TelaPiscante;

public class ShipmentDevolutionService 
{

	public void checkItemDevolution(ShipmentDevolutionInterface itemShipmentDevolution, boolean checked) 
	{
		itemShipmentDevolution.setChecked(checked);		
		List<ShipmentItensDevolutionDetails> itensDetailsDevolution = null;
		
		if ( TypeStock.EMBALAGEM.equals( itemShipmentDevolution.getPurchaseOrderItem().getTypeStock() ) )
		{
			itensDetailsDevolution = getItensSelectedByItemDevolutionByProductFromStock( itemShipmentDevolution.getPurchaseOrderItem() );
		}
		else
		{
			itensDetailsDevolution = getItensSelectedByItemDevolutionByProduct( itemShipmentDevolution.getPurchaseOrderItem() );
		}
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try
		{
			for ( ShipmentItensDevolutionDetails item : itensDetailsDevolution )
			{
				item.setChecked( itemShipmentDevolution.isChecked() );
				GuiGlobals.getDaoFactory().getPurchaseOrderDao().updateShipmentItemDevolution(item);
			}
			GuiGlobals.getDaoFactory().commit();
		}
		catch ( Exception e)
		{
			GuiGlobals.getDaoFactory().rollback();
			LogDeProcessamento.gravaLog("error", "Não foi possível marcar os itens a serem devolvidos no banco de dados \r\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void checkAllItensDevolution(List<ShipmentDevolutionInterface> itens) 
	{
		for ( ShipmentDevolutionInterface item : itens)
		{
			checkItemDevolution(item, true);
		}
	}

	public void addItensToConfirm(PurchaseOrder purchaseOrder, TableItensShipmentDoneDevolutionModel tableModelItensOfDoneDevolution) 
	{
		tableModelItensOfDoneDevolution.getDadosDaGrid().clear();
				
		if ( purchaseOrder != null && purchaseOrder.getId() > 0)
		{
			List<ShipmentItensDevolutionDetails> itensDetailsDevolution = GuiGlobals.getDaoFactory().getPurchaseOrderDao().getItensDetailsDevolutionCheckedByPurchaseOrder(purchaseOrder);
			
			for ( ShipmentItensDevolutionDetails item : itensDetailsDevolution )
			{
				ItensShipmentDoneDevolution itemDevolution = new ItensShipmentDoneDevolution();
				itemDevolution.setChecked(item.isChecked());
				itemDevolution.setPurchaseOrderItem(item.getPurchaseOrderItem());
				if ( item.getStock() != null && item.getStock().getId() > 0 && StockStatus.NON_STOCKED.equals(item.getStock().getStatus()))
				{
					itemDevolution.setSerial(item.getStock().getIdFormatSerial());
					itemDevolution.setDestino(item.getStock().getTypeStock().getSigla());
					itemDevolution.setPesoBruto( item.getStock().getNet() + item.getStock().getTare() );
					itemDevolution.setPesoLiq(item.getStock().getNet());
					itemDevolution.setProduct(item.getStock().getProduct());
					itemDevolution.setQuantidade(item.getStock().getPrimaryQty());
					itemDevolution.setTara(item.getStock().getTare());
					itemDevolution.setUnidade(item.getStock().getUnitDesc());
				}
				else
				{
					itemDevolution.setSerial(Utils.formatSerial(0));
					itemDevolution.setDestino(item.getTypeStock().getSigla());
					itemDevolution.setPesoBruto( item.getGrossWeight() );
					itemDevolution.setPesoLiq(item.getNetWeight());
					itemDevolution.setProduct(item.getProduct());
					itemDevolution.setQuantidade(item.getQty());
					itemDevolution.setTara(item.getTare());
					itemDevolution.setUnidade(item.getUnit().getId());
				}
				
				insertDataOnGridOfDoneDevolution( itemDevolution, tableModelItensOfDoneDevolution );
			}
		}
	}
	
	private List<ShipmentItensDevolutionDetails> getItensSelectedByItemDevolutionByProductFromStock(PurchaseOrderItem purchaseOrderItem)
	{
		return GuiGlobals.getDaoFactory().getPurchaseOrderDao().getItensDetailsDevolutionByProductFromStock(purchaseOrderItem);
	}
	
	private List<ShipmentItensDevolutionDetails> getItensSelectedByItemDevolutionByProduct(PurchaseOrderItem purchaseOrderItem)
	{
		return GuiGlobals.getDaoFactory().getPurchaseOrderDao().getItensDetailsDevolutionByProduct(purchaseOrderItem);
	}

	public void addItensToDevolution(PurchaseOrder purchaseOrder, TableItensShipmentDevolutionModel tableModelItensOfDevolution) 
	{
		addItensToDevolution(purchaseOrder, tableModelItensOfDevolution, null);
	}
	
	public void addItensToDevolution(PurchaseOrder purchaseOrder, TableItensShipmentDevolutionModel tableModelItensOfDevolution, TableItensShipmentDoneDevolutionModel tableModelItensOfDoneDevolution) 
	{
		tableModelItensOfDevolution.getDadosDaGrid().clear();
		
		if ( purchaseOrder != null && purchaseOrder.getId() > 0 )
		{
			GuiGlobals.getDaoFactory().getPurchaseOrderDao().refresh(purchaseOrder);
			
			for ( PurchaseOrderItem purchaseOrderItem : purchaseOrder.getItems() )
			{
				
				ItensShipmentDevolution itemRow = new ItensShipmentDevolution();
				
				itemRow.setChecked( ( tableModelItensOfDoneDevolution == null ) ? false : isItemCheckedToDevolutionConfirm(purchaseOrderItem, tableModelItensOfDoneDevolution) );
				itemRow.setPurchaseOrderItem(purchaseOrderItem);
				itemRow.setProduct( purchaseOrderItem.getProduct() );
				itemRow.setQuantidade( purchaseOrderItem.getQuantity() );
				itemRow.setUnidade( purchaseOrderItem.getUnit().getId() );
				itemRow.setDestino(purchaseOrderItem.getTypeStock().getSigla());
				itemRow.setTara( purchaseOrderItem.getTare() );
				itemRow.setPesoLiq( purchaseOrderItem.getNet() );
				itemRow.setPesoBruto( purchaseOrderItem.getNet() + purchaseOrderItem.getTare() );
				
				insertDataOnGridOfDevolution(itemRow, tableModelItensOfDevolution);
			}
		}
	}
	
	public void insertDataOnGridOfDevolution(ItensShipmentDevolution itemRowProduct, TableItensShipmentDevolutionModel tableModelItensOfDevolution) 
	{
		if (tableModelItensOfDevolution.getRowCount() > 0)
		{
			
			boolean achou = false;
			double primaryQty;			
			double tare;
			double pesoLiquido;
			double pesoBruto;
			
			for (int i = 0; i < tableModelItensOfDevolution.getRowCount(); i++) 
			{ // Caso exista itens faz a verredura para verificar se já existe no jtable o produto selecionado no objeto dlg.

				if	(	itemRowProduct.getProduct().equals( tableModelItensOfDevolution.getDadosDaGrid().get(i).getProduct() ) 
						&& itemRowProduct.getDestino().equals( tableModelItensOfDevolution.getDadosDaGrid().get(i).getDestino() )
					) 
				{ 
					ItensShipmentDevolution itemRowChange = tableModelItensOfDevolution.getDadosDaGrid().get(i);
		
					primaryQty = itemRowProduct.getQuantidade() + itemRowChange.getQuantidade();
					tare = NumberUtils.roundNumber(itemRowProduct.getTara() * primaryQty , 3);
					pesoLiquido = NumberUtils.roundNumber( itemRowProduct.getPesoLiq() + itemRowChange.getPesoLiq(), 3);
					pesoBruto = NumberUtils.roundNumber( itemRowProduct.getPesoBruto() + itemRowChange.getPesoBruto(), 3);
		
					itemRowChange.setQuantidade( primaryQty );
					itemRowChange.setTara( tare );
					itemRowChange.setPesoLiq( pesoLiquido );
					itemRowChange.setPesoBruto( pesoBruto );
		  					
					tableModelItensOfDevolution.getDadosDaGrid().set(i, itemRowChange);						
	
					achou = true;
					break;
				}
			}
					
			if(!achou){
				tableModelItensOfDevolution.getDadosDaGrid().add( itemRowProduct );		
			}
		}
		else
		{		
			itemRowProduct.setTara(NumberUtils.roundNumber(itemRowProduct.getTara() * itemRowProduct.getQuantidade(), 3) );
			itemRowProduct.setPesoLiq( itemRowProduct.getPesoLiq() );
			tableModelItensOfDevolution.getDadosDaGrid().add( itemRowProduct );
		}	
		
	}
	
	public void insertDataOnGridOfDoneDevolution(ItensShipmentDoneDevolution itemRowProduct, TableItensShipmentDoneDevolutionModel tableModelItensOfDoneDevolution) 
	{
		if (tableModelItensOfDoneDevolution.getRowCount() > 0)
		{
			
			boolean achou = false;
			double primaryQty;			
			double tare;
			double pesoLiquido;
			double pesoBruto;
			
			for (int i = 0; i < tableModelItensOfDoneDevolution.getRowCount(); i++) 
			{ // Caso exista itens faz a verredura para verificar se já existe no jtable o produto selecionado no objeto dlg.

				if	(	itemRowProduct.getProduct().equals( tableModelItensOfDoneDevolution.getDadosDaGrid().get(i).getProduct() ) 
						&& itemRowProduct.getDestino().equals( tableModelItensOfDoneDevolution.getDadosDaGrid().get(i).getDestino() )
					) 
				{ 
					ItensShipmentDoneDevolution itemRowChange = tableModelItensOfDoneDevolution.getDadosDaGrid().get(i);
		
					primaryQty = itemRowProduct.getQuantidade() + itemRowChange.getQuantidade();
					tare = NumberUtils.roundNumber(itemRowProduct.getTara() * primaryQty , 3);
					pesoLiquido = NumberUtils.roundNumber( itemRowProduct.getPesoLiq() + itemRowChange.getPesoLiq(), 3);
					pesoBruto = NumberUtils.roundNumber( itemRowProduct.getPesoBruto() + itemRowChange.getPesoBruto(), 3);
		
					itemRowChange.setQuantidade( primaryQty );
					itemRowChange.setTara( tare );
					itemRowChange.setPesoLiq( pesoLiquido );
					itemRowChange.setPesoBruto( pesoBruto );
		  					
					tableModelItensOfDoneDevolution.getDadosDaGrid().set(i, itemRowChange);						
	
					achou = true;
					break;
				}
			}
					
			if(!achou){
				tableModelItensOfDoneDevolution.getDadosDaGrid().add( itemRowProduct );		
			}
		}
		else
		{		
			itemRowProduct.setTara(NumberUtils.roundNumber(itemRowProduct.getTara() * itemRowProduct.getQuantidade(), 3) );
			itemRowProduct.setPesoLiq( itemRowProduct.getPesoLiq() );
			tableModelItensOfDoneDevolution.getDadosDaGrid().add( itemRowProduct );
		}	
		
	}

	public boolean isItemCheckedToDevolutionConfirm(PurchaseOrderItem purchaseOrderItem, TableItensShipmentDoneDevolutionModel tableModelItensOfDoneDevolution) 
	{
		List<ItensShipmentDoneDevolution> list = tableModelItensOfDoneDevolution.getDadosDaGrid();
		boolean check = false;
		
		for( ItensShipmentDoneDevolution item : list )
		{
			if ( item.isChecked() && item.getPurchaseOrderItem().equals( purchaseOrderItem ) )
			{
				check = true;
				break;
			}
		}
		
		return check;
	}

	public void addItensToPartialDevolution(PurchaseOrder purchaseOrder, PurchaseOrderItem purchaseOrderItemSelected, TableItensShipmentPartialDevolutionModel tableModelItensOfPartialDevolution) 
	{
		List<ItensShipmentPartialDevolution> listChecked = tableModelItensOfPartialDevolution.getDadosDaGrid();
		listChecked.clear();
		
		if ( purchaseOrder != null && purchaseOrder.getId() > 0)
		{
			List<ShipmentItensDevolutionDetails> itensDetailsDevolution = GuiGlobals.getDaoFactory().getPurchaseOrderDao().getItensDetailsDevolutionByPurchaseOrder(purchaseOrder);
			
			for ( ShipmentItensDevolutionDetails item : itensDetailsDevolution )
			{
				TypeStock currentTypeStock = null;
				if ( item.getStock() != null && item.getStock().getId() > 0 )
				{
					currentTypeStock = item.getStock().getTypeStock();
				}
				else
				{
					currentTypeStock = item.getTypeStock();
				}
				
				if ( TypeStock.EMBALAGEM.equals(currentTypeStock) && item.getPurchaseOrderItem().equals(purchaseOrderItemSelected) )
				{
					ItensShipmentPartialDevolution itemDevolution = new ItensShipmentPartialDevolution();
					itemDevolution.setChecked(item.isChecked());
					itemDevolution.setShipmentItensDevolutionDetails(item);
					itemDevolution.setPurchaseOrderItem(item.getPurchaseOrderItem());
					if ( item.getStock() != null && item.getStock().getId() > 0 && StockStatus.NON_STOCKED.equals(item.getStock().getStatus()) )
					{
						itemDevolution.setSerial(item.getStock().getIdFormatSerial());
						itemDevolution.setDestino(item.getStock().getTypeStock().getSigla());
						itemDevolution.setPesoBruto( item.getStock().getNet() + item.getStock().getTare() );
						itemDevolution.setPesoLiq(item.getStock().getNet());
						itemDevolution.setProduct(item.getStock().getProduct());
						itemDevolution.setQuantidade(item.getStock().getPrimaryQty());
						itemDevolution.setTara(item.getStock().getTare());
					}
					else
					{
						itemDevolution.setSerial(Utils.formatSerial(0));
						itemDevolution.setDestino(item.getTypeStock().getSigla());
						itemDevolution.setPesoBruto( item.getGrossWeight() );
						itemDevolution.setPesoLiq(item.getNetWeight());
						itemDevolution.setProduct(item.getProduct());
						itemDevolution.setQuantidade(item.getQty());
						itemDevolution.setTara(item.getTare());
					}
					
					listChecked.add(itemDevolution);
				}
			}
		}
	}

	public void checkItemDevolutionBySscc(String codSscc) 
	{
		Stock stock = GuiGlobals.getDaoFactory().getStockDao().findBySSCC(codSscc);
		Pallet pallet = GuiGlobals.getDaoFactory().getPalletDao().findBySSCC(codSscc);
		
		if ( pallet != null && pallet.getId() > 0 && stock != null && stock.getId() > 0)
		{
			int opcao = GuiGlobals.showMessageDlgYesNo("O código de etiqueta é de palete ?");
			if(opcao == JOptionPane.YES_OPTION)
			{
				checkItemDevolutionFromPallet( pallet );
			}
			else
			{
				checkItemDevolutionFromStock( stock );
			}
		}
		else if ( pallet != null && pallet.getId() > 0 )
		{
			checkItemDevolutionFromPallet( pallet );
		}
		else
		{
			checkItemDevolutionFromStock( stock );
		}
	}

	private void checkItemDevolutionFromPallet(Pallet pallet) 
	{
		boolean canChange = ( pallet != null && pallet.getId() > 0 && ( PalletStatus.CLOSED.equals( pallet.getStatus() ) || PalletStatus.REPALLET.equals(pallet.getStatus() ) )  );
		
		if ( canChange )
		{
			String codeSerial = null;
			GuiGlobals.getDaoFactory().beginTransaction();
			try
			{
				for ( Stock stock : pallet.getStocks() )
				{
					if ( !checkItemDevolutionFromStockOnBD(stock) )
					{
						codeSerial = getCodEtqFromStockToMessage(stock);
						break;
					}
				}
				GuiGlobals.getDaoFactory().commit();
			}
			catch ( Exception e)
			{
				GuiGlobals.getDaoFactory().rollback();
				LogDeProcessamento.gravaLog("error", "Não foi possível marcar o item de serial [" + codeSerial + "] do palete [" + getCodEtqFromPalletToMessage(pallet) + "] \r\n" + e.getMessage());
				e.printStackTrace();
			}
		}
		else if ( PalletStatus.DELETED.equals( pallet.getStatus() ) )
		{
			String msgVld = "Palete foi deletado!";
			GuiGlobals.getMain().showMessage("<html><center>" + msgVld + "<br>" + getCodEtqFromPalletToMessage(pallet) + "</center></html>");
			TelaPiscante tela = new TelaPiscante(msgVld + "\n [" + getCodEtqFromPalletToMessage(pallet) + "]");
			tela.setVisible(true);
		}
		else if ( PalletStatus.OPEN.equals( pallet.getStatus() ) )
		{
			String msgVld = "Palete está em aberto!";
			GuiGlobals.getMain().showMessage("<html><center>" + msgVld + "<br>" + getCodEtqFromPalletToMessage(pallet) + "</center></html>");
			TelaPiscante tela = new TelaPiscante(msgVld + "\n [" + getCodEtqFromPalletToMessage(pallet) + "]");
			tela.setVisible(true);
		}
	}

	private void checkItemDevolutionFromStock(Stock stock) 
	{
		boolean erro = false;
		GuiGlobals.getDaoFactory().beginTransaction();
		try
		{
			if ( checkItemDevolutionFromStockOnBD(stock) )
			{
				GuiGlobals.getDaoFactory().commit();
			}
			else
			{
				erro = true;
				GuiGlobals.getDaoFactory().rollback();
			}
		}
		catch ( Exception e)
		{
			GuiGlobals.getDaoFactory().rollback();
			LogDeProcessamento.gravaLog("error", "Não foi possível marcar o item de serial [" + getCodEtqFromStockToMessage(stock) + "] a ser devolvido no banco de dados \r\n" + e.getMessage());
			e.printStackTrace();
		}
		
		if ( erro )
		{
			String msgVld = "Etiqueta em estoque. Não pode ser devolvida!";
			GuiGlobals.getMain().showMessage("<html><center>" + msgVld + "<br>" + getCodEtqFromStockToMessage(stock) + "</center></html>");
			TelaPiscante tela = new TelaPiscante(msgVld + "\n [" + getCodEtqFromStockToMessage(stock) + "]");
			tela.setVisible(true);
		}
	}
	
	private boolean checkItemDevolutionFromStockOnBD(Stock stock) 
	{
		boolean canChange = ( stock != null && stock.getId() > 0 && StockStatus.NON_STOCKED.equals(stock.getStatus())  );
		boolean processOk = false;
		
		if ( canChange )
		{
			ShipmentItensDevolutionDetails itemDevolution = GuiGlobals.getDaoFactory().getPurchaseOrderDao().findByStock(stock);
			
			if ( itemDevolution != null && itemDevolution.getId() > 0 )
			{
				GuiGlobals.getDaoFactory().getPurchaseOrderDao().updateShipmentItemDevolution(itemDevolution);
				processOk = true;
			}
		}
		
		return processOk;
	}

	private String getCodEtqFromStockToMessage(Stock stock) 
	{
		String code = null;
		
		if ( stock != null )
		{
			code = ( (stock.getSscc().isEmpty()) ? stock.getIdFormatSerial() : stock.getSscc() );
		}
		else
		{
			code = Utils.formatSerial(0);
		}
		
		return code;
	}
	
	private String getCodEtqFromPalletToMessage(Pallet pallet) 
	{
		String code = null;
		
		if ( pallet != null )
		{
			code = ( (pallet.getSscc().isEmpty()) ? pallet.getIdFormatted() : pallet.getSscc() );
		}
		else
		{
			code = Utils.formatSerial(0);
		}
		
		return code;
	}

	public void checkItemDevolutionByLabelBox(String serial) 
	{
		Stock stock = GuiGlobals.getDaoFactory().getStockDao().find(Long.parseLong(serial));
		checkItemDevolutionFromStock(stock); 
	}

	public void checkItemDevolutionByLabelPallet(String serial) 
	{
		Pallet pallet = GuiGlobals.getDaoFactory().getPalletDao().find( Integer.parseInt(serial));
		checkItemDevolutionFromPallet(pallet);
	}
}
