package br.com.cardeal.importacao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import br.com.cardeal.dao.OrderDao;
import br.com.cardeal.dao.PartnerDao;
import br.com.cardeal.dao.ProductDao;
import br.com.cardeal.dao.UnitDao;
import br.com.cardeal.enums.LayoutCabecalhoPedido;
import br.com.cardeal.enums.LayoutItensPedido;
import br.com.cardeal.enums.OrderStatus;
import br.com.cardeal.enums.PartnerStyle;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.Order;
import br.com.cardeal.model.OrderItem;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Unit;
import br.com.cardeal.services.PartnerService;

public class ImportacaoPedidosExpedicao implements ImportacaoInterface {

	private String pathPurchaseShipmentImport;
	private String filePurchaseShipmentImport;
	private String extensionCabec = ".hea";
	private String extensionItens = ".ped";
	private String fileName;	
	
	public ImportacaoPedidosExpedicao( String fileName ) {
		this.fileName = fileName.trim();
		
		pathPurchaseShipmentImport = GuiGlobals.getDaoFactory().getServerSetupDao().find().getReadDirectory().trim();
		setFilePurchaseShipmentImport( extensionCabec );
	}

	public String getFilePurchaseShipmentImport() {
		return filePurchaseShipmentImport;
	}

	public void setFilePurchaseShipmentImport(String extension) {
		this.filePurchaseShipmentImport = pathPurchaseShipmentImport + GuiGlobals.getSeparador() + this.fileName + extension;
	}

	@Override
	public String getFileImport() {
		return filePurchaseShipmentImport;
	}
	
	@Override
	public boolean sendToSGBD(String[] dados) {
		
		boolean tudoOk = true;
		String[] itens;
		String dado = null;
		String pedido = dados[LayoutCabecalhoPedido.NUMERO_PEDIDO.getPosicao()].trim();		
		
		//----------------------------------
		//- Verifica se o pedido já existe -
		//----------------------------------
		OrderDao dao = GuiGlobals.getDaoFactory().getOrderDao();
		Order orderCheck = dao.findByIdImport( pedido );
		
		if(orderCheck != null) {
			GuiGlobals.showMessage("Pedido " + pedido + " já importado anteriormente!");
			return false;
		}
		
		//-----------------------
		//- Cabecalho do pedido -
		//-----------------------
		Order order = new Order();
		order.setAvailableFrom(DateTimeUtils.now());
		order.setCreationDate(DateTimeUtils.now());
		order.setCompany( Setup.getCompany() );
		order.setTerminal( Setup.getTerminal() );
		order.setStatus(OrderStatus.NOT_STARTED);
		order.setIdPedidoImport( pedido );
		
		PartnerDao partnerDAO = GuiGlobals.getDaoFactory().getPartnerDao();
		Partner partner = partnerDAO.findByName( dados[LayoutCabecalhoPedido.NOME_CLIENTE.getPosicao()].trim() );
		
		if ( partner == null){			
			partner = new Partner();
			partner.setName( dados[LayoutCabecalhoPedido.NOME_CLIENTE.getPosicao()].trim() );
			partner.setPartnerStyle(PartnerStyle.CUSTOMER);
			
			PartnerService partnerService = new PartnerService();
			partnerService.addPartnerByImport(partner);
		}
		
		order.setPartner(partner);
		order.setCodEmpTotvs( dados[LayoutCabecalhoPedido.AUTORIZACAO_EMBARQUE.getPosicao()].trim() );
		
		//---------
		//- Itens -
		//---------
		setFilePurchaseShipmentImport( extensionItens );
		ReadFile readFileItens = new ReadFile(this);
		List<String> itensString = null;
		List<OrderItem> orderItens = null;
		
		try 
		{		
			itensString = readFileItens.read(true, order.getIdPedidoImport(), LayoutItensPedido.NUMERO_PEDIDO.getPosicao(), false);
			
			ProductDao daoProduct = GuiGlobals.getDaoFactory().getProductDao();
			UnitDao daoUnit = GuiGlobals.getDaoFactory().getUnitDao();
			
			orderItens = new ArrayList<OrderItem>();
			for(int i = 0; i < itensString.size(); i++)
			{
				itens = itensString.get(i).split(";");
				
				OrderItem orderItem = new OrderItem();
				
				Product product = daoProduct.findByIdMasc( itens[LayoutItensPedido.CODIGO_PRODUTO.getPosicao()] );
				
				if ( product == null || product.getId() == 0 ) 
				{
					GuiGlobals.showMessageDlg("Produto não existe: " + itens[LayoutItensPedido.CODIGO_PRODUTO.getPosicao()] );
					LogDeProcessamento.gravaLog(null,"IMPORTACAO DE PEDIDOS - Produto " + itens[LayoutItensPedido.CODIGO_PRODUTO.getPosicao()] + " não cadastrado."); 
					tudoOk = false; 
					break; 
				} 
				
				GuiGlobals.getDaoFactory().getProductDao().refresh(product);
				
				if ( product.isBlocked() ) 
				{
					GuiGlobals.showMessageDlg("Produto [" + product.getIdMasc() + "] está bloqueado. Importação será abortada!" );
					LogDeProcessamento.gravaLog(null,"Produto [" + product.getIdMasc() + "] está bloqueado! Importação abortada"); 
					tudoOk = false; 
					break; 
				}
				
				orderItem.setOrder(order);
				orderItem.setIndex(Integer.parseInt( itens[LayoutItensPedido.ITEM_PEDIDO.getPosicao()] ));
				orderItem.setProduct(product);
				
				Unit unit =  daoUnit.find( itens[LayoutItensPedido.UNIDADE_MEDIDA.getPosicao()] );
				if ( unit == null ) {
					GuiGlobals.showMessageDlg("Unidade de medida [" + itens[LayoutItensPedido.UNIDADE_MEDIDA.getPosicao()] + "] não existe. Por favor, cadastre e tente importar o pedido novamente!" );
					LogDeProcessamento.gravaLog(null,"IMPORTACAO DE PEDIDOS - Unidade de medida " + itens[LayoutItensPedido.UNIDADE_MEDIDA.getPosicao()] + " não cadastrado."); 
					tudoOk = false; 
					break;
				}
				orderItem.setUnit( unit );
				
				dado = itens[LayoutItensPedido.QUANTIDADE_FALTA_EXPEDIR.getPosicao()];
				orderItem.setQtyRequested( Double.parseDouble( dado.replace(',', '.')  ) );
				
				dado = itens[LayoutItensPedido.QUANTIDADE_EXPEDIDA.getPosicao()];
				orderItem.setQtyIssued( Double.parseDouble( dado.replace(',', '.')  )  );	
				
				orderItem.setCompany( Setup.getCompany() );
				orderItem.setTerminal( Setup.getTerminal() );
				orderItem.setTypeStock( product.getTypeStock() );
							
				orderItens.add( orderItem );
				orderItem = null;
				itens = null;
				product = null;
			}
		} 
		catch (Exception e) 
		{
			tudoOk = false;
			LogDeProcessamento.gravaLog(null,"IMPORTACAO DE PEDIDOS - Falha na leitura do arquivo de itens.");
			e.printStackTrace();
		}
		
		if ( tudoOk && order != null && orderItens.size() > 0) 
		{
			readFileItens.deleteFile( true );
			
			order.setItems(orderItens);
			OrderDao orderDao =  GuiGlobals.getDaoFactory().getOrderDao();
			
			GuiGlobals.getDaoFactory().beginTransaction();

			try
			{
				orderDao.add(order);
				for(OrderItem item : order.getItems()) 
				{
					orderDao.addItem(item);
				}
				GuiGlobals.getDaoFactory().commit();
			}
			catch( Exception e )
			{
				GuiGlobals.getDaoFactory().rollback();
				e.printStackTrace();				
			}
		}
		
		return tudoOk;
	}

	@Override
	public void execute( boolean removeOnFinal ) 
	{
		ReadFile readFileCab = new ReadFile(this);
		try 
		{
			readFileCab.read(false, null, 0, removeOnFinal);
		}
		catch (FileNotFoundException e) 
		{
			GuiGlobals.showMessageDlg("Arquivo não encontrado!");
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}	

}
