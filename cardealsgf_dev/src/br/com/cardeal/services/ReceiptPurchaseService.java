package br.com.cardeal.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.model.ServerSetup;
import br.com.cardeal.model.Stock;

public class ReceiptPurchaseService 
{
	public Partner findPartnerById( int id ) 
	{
		return GuiGlobals.getDaoFactory().getPartnerDao().findById(id);
	}

	public PurchaseOrder findPurchaseOrderByNoteAndPartner(String note, Partner partner) 
	{
		return GuiGlobals.getDaoFactory().getPurchaseOrderDao().findByNote(partner, note );
	}

	public void updatePurchaseOrder(PurchaseOrder purchaseOrder) 
	{
		GuiGlobals.getDaoFactory().beginTransaction();
		try
		{
			GuiGlobals.getDaoFactory().getPurchaseOrderDao().update(purchaseOrder);
			GuiGlobals.getDaoFactory().commit();
			GuiGlobals.getDaoFactory().getPurchaseOrderDao().refresh(purchaseOrder);
		}
		catch (Exception e)
		{
			GuiGlobals.getDaoFactory().rollback();
			LogDeProcessamento.gravaLog("error", "Erro na atualiza��o do pedido de compra! \r\n" + e.getMessage());
		}
	}

	public Product findProductByIdMasc(String idMasc) 
	{
		return GuiGlobals.getDaoFactory().getProductDao().findByIdMasc(idMasc);
	}

	public void refreshPurchaseOrder(PurchaseOrder purchaseOrder) 
	{
		GuiGlobals.getDaoFactory().getPurchaseOrderDao().refresh(purchaseOrder);
	}

	public void generateArqsTxt(PurchaseOrder purchaseOrder) 
	{
		ServerSetup serverConfig = getConfigTxt();
  		
		if ( serverConfig != null && serverConfig.getStoreDirectory() != null ) 
		{
			generateRecebe(serverConfig, purchaseOrder);
			generateKardex(purchaseOrder);
		}
	}
	
	private void generateKardex(PurchaseOrder purchaseOrder) 
	{
		StockService stockService = new StockService();
				
		for ( PurchaseOrderItem purchaseOrderitem : purchaseOrder.getItems() )
		{
			Stock stock = new Stock();
			stock.setId(0);
			stock.setProduct(purchaseOrderitem.getProduct());
			stock.setTypeStock(purchaseOrderitem.getTypeStock());
			
			stockService.geraKardex(stock, "E", purchaseOrder.getPartner().getCodigoExterno(), purchaseOrder.getNote(), purchaseOrderitem.getDateValidateBatchExternal(), purchaseOrderitem.getBatchExternal(), "M");
		}
	}

	private ServerSetup getConfigTxt() 
	{		
		return GuiGlobals.getDaoFactory().getServerSetupDao().find();
	}

	private void generateRecebe(ServerSetup serverConfig, PurchaseOrder purchaseOrder)
	{
		FileWriter arq = null;
		PrintWriter gravarArq;
		    
		try 
		{
			arq = new FileWriter(serverConfig.getStoreDirectory() + GuiGlobals.getSeparador() + "RECEBE.txt",true);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			GuiGlobals.showMessageDlg("Arquivo RECEBE.txt n�o ser� gerado pois n�o foi configurado no sistema. Para configurar, o administrador deve indicar o diret�rio em 'Manuten��o->Configura��es' !");
		}			
		gravarArq = new PrintWriter(arq);
		
		if ( arq != null )
		{
			for ( PurchaseOrderItem purchaseOrderitem : purchaseOrder.getItems() )
			{
				Date operationDate = purchaseOrder.getDateReceipt();
		
				String linha = "";
				linha = StringUtils.leftPad( purchaseOrder.getNote().trim(), 6, "0" ) + ";";					// 1 N�mero da nota fiscal
				linha += Utils.formatSerial(0) + ";";															// 2 N�mero da caixa. Ser� 00000000 quando for recebimento via balan�a ou manual (sem balan�a, com o peso digitado)
				linha += StringUtils.leftPad( purchaseOrderitem.getCompany().getId() ,3, "0" ) + ";";			// 3 N�mero da f�brica (Ser� 000 quando for recebimento via balan�a ou manual (sem balan�a, com peso digitado)
				linha += Setup.getTerminal().getIdTerminal() + ";";												// 4 N�mero do terminal
				linha += purchaseOrderitem.getProduct().getIdMasc() + ";";										// 5 C�digo do produto
				linha += StringUtils.leftPad( GuiGlobals.getUserInfo().getUser().getPassword(), 4, "0" )  + ";";// 6 Senha do operador
				linha += StringUtils.leftPad( String.format("%.3f", purchaseOrderitem.getNet() ), 8, "0" ) + ";";// 7 Peso l�quido kg
				linha += StringUtils.leftPad( String.format("%.3f", purchaseOrderitem.getTare() ), 7, "0" ) + ";";// 8 Tara
				linha += getPrimaryQtyFormat( purchaseOrderitem.getQuantity() ) + ";";							// 9 Quantidade de pe�as, sacos etc. recebidas via balan�a. Caso o produto seja recebido via scanner, a quantidade ser� 01.
				linha += purchaseOrderitem.getUnit().getId().toUpperCase() + ";";								// 10 Unidade do produto recebido
				linha += DateTimeUtils.getDate(operationDate, "yyMMdd") + ";";									// 11 Data da opera��o
				linha += DateTimeUtils.getDate(operationDate, "HHmm") + ";";									// 12 Hora e minuto da opera��o
				linha += purchaseOrderitem.getTypeOperation().getInitial() +";";	    						// 13 Tipo opera��o �	1 � pistola	/ �	5 � manual / �	6 - com balan�a
				linha += purchaseOrderitem.getTypeStock().getDestinoDoc() + ";";								// 14 Destino do produto recebido E,S,G,I e D
				linha += purchaseOrder.getTypeReceipt().getInitial() + ";";										// 15 Motivo do recebimento
				linha += purchaseOrderitem.getBatchInternal();
				gravarArq.println( linha );
			}
			try 
			{
				gravarArq.close();
				arq.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();			
			}
		}
	}
	
	public String getPrimaryQtyFormat(double quantity) {
		/**
		 * Garantindo que o numero ser� inteiro
		 */
		String value = String.valueOf( quantity ).trim();
		int posDot = value.indexOf(".");
		
		value = value.substring(0, posDot);
		
		return StringUtils.leftPad( value, 3, "0" );
	}
}
