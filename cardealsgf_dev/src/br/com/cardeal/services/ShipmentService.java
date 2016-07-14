package br.com.cardeal.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import br.com.cardeal.dao.StockDao;
import br.com.cardeal.enums.Operation;
import br.com.cardeal.enums.OrderStatus;
import br.com.cardeal.enums.ShipmentTypeOperation;
import br.com.cardeal.enums.StockStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.filter.HistoricFilter;
import br.com.cardeal.filter.StockFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.importacao.ImportacaoPedidosExpedicao;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.Historic;
import br.com.cardeal.model.IndexByProductToArqExp;
import br.com.cardeal.model.ItensShipment;
import br.com.cardeal.model.Order;
import br.com.cardeal.model.OrderItem;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.model.ServerSetup;
import br.com.cardeal.model.ShipmentArchieveExp;
import br.com.cardeal.model.ShipmentArchieveHea;
import br.com.cardeal.model.ShipmentArchieveTot;
import br.com.cardeal.model.Stock;
import br.com.cardeal.model.StockTotal;
import br.com.cardeal.model.Unit;
import br.com.cardeal.model.UserInProcess;

public class ShipmentService 
{
			
	private String idOrderImport;
	private Order order;
	private String warnings;

	public ShipmentService() {}
	
	public ShipmentService(Order order) {
		this.order = order;
	}
	
	public ShipmentService( String idOrderImport ) 
	{
		setIdOrderImport(idOrderImport);
		refreshService(idOrderImport);
	}
	
	public ShipmentService( String idOrderImport, boolean isRefresh ) 
	{
		setIdOrderImport(idOrderImport);
		if ( isRefresh ) refreshService(idOrderImport);
	}
	
	public void refreshService(String idOrderImport) 
	{
		this.idOrderImport = idOrderImport;
		order = findOrderByIdImport( idOrderImport );
		
		if ( order == null )
		{
			GuiGlobals.showMessage("Procurando pedido...", false);			
			importOrder();			
			GuiGlobals.showMessage("", false);
			order = findOrderByIdImport( idOrderImport );			
		}
		
		if ( order != null )
		{
			GuiGlobals.getDaoFactory().getOrderDao().refresh(order);
			UserInProcess userInProcess = new UserInProcess(GuiGlobals.getUserInfo().getUser(), GuiGlobals.getEnterprise(), Setup.getCompany(), Setup.getTerminal() );
			
			if ( order.getUserInProcess() != null )
			{
				if ( !order.getUserInProcess().equals( userInProcess ) )
				{				
					GuiGlobals.getMain().showMessagePiscante(	"<html><left>Pedido em execução!<br>" +
																" Usuário: " + order.getUserInProcess().getUser().getName() + "<br>" +
																" Filial: " + order.getUserInProcess().getCompany().getId() + "<br>" +
																" Terminal: " + order.getUserInProcess().getTerminal().getIdTerminal() + "<br>" +
																"</center></html>");
					order = null;
				}
				
			}
			else
			{
				newUserInProcess( userInProcess );
				order.setUserInProcess(userInProcess);
				updateOrder(order);
			}
		}
		
	}	

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	public double getQtyIssued(Stock stock) 
	{
		return getQtyIssued(stock, stock.getProduct(), stock.getProduct().getUnit());
	}
	
	public double getQtyIssued(Stock stock, String unitId) 
	{
		Unit unit = GuiGlobals.getDaoFactory().getUnitDao().find(unitId);
		return getQtyIssued(stock, stock.getProduct(), unit);
	}
	
	public double getQtyIssued(Stock stock, Product product, Unit unit) 
	{
		double qtyIssuedNow;
		
		if ( isUnitForEmb(product, unit) )
		{
			qtyIssuedNow = stock.getSecondaryQty();
		}						
		else if ( isUnitForKg(unit) ){
			qtyIssuedNow = stock.getNet();
		}
		else 
		{
			qtyIssuedNow = stock.getPrimaryQty();
		}
		return qtyIssuedNow;
	}
	
	public boolean isUnitForEmb(Product product, Unit unit)
	{
		return ( !unit.getId().toUpperCase().trim().equals( "EMB" ) && unit.getId().equals( product.getUnitEmb().getId() ) );
	}
	
	public boolean isUnitForKg( Unit unit )
	{
		return ( unit.getId().toUpperCase().equals("KG") );
	}
	
	public boolean isUnitForKg( String unitId )
	{
		Unit unit = GuiGlobals.getDaoFactory().getUnitDao().find(unitId);
		return isUnitForKg( unit );
	}
	
	public boolean isUnitForPc(Product product, Unit unit)
	{
		return ( unit.getId().toUpperCase().trim().equals( "EMB" ) || !isUnitForEmb(product, unit) && !isUnitForKg(unit) );
	}
	
	/**
	 * Cancela um pedido	
	 * @param order
	 */
	public void cancelOrder() 
	{
		if ( order != null && order.getId() > 0 ) {
			
			GuiGlobals.getDaoFactory().beginTransaction();
			
			try{

				// altera status do pedido
				order.setStatus(OrderStatus.CANCELED);					
					
				// instancia historico
				Historic h = new Historic();
				h.setBatch(null);
				h.setDate(DateTimeUtils.now());
				h.setNet(0);
				h.setOperation(Operation.SHIPMENT_STOCK_CANCEL);
				h.setOrder( order );
				h.setPartner(order.getPartner());
				h.setPrimaryQty(0);
				h.setProduct( null );
				h.setSecondaryQty(0);
				h.setStock( null );
				h.setTare(0);
				h.setTerminal(Setup.getTerminal());
				h.setCompany(Setup.getCompany());
				h.setUser(GuiGlobals.getUserInfo().getUser());
				h.setPallet(null);
					
				// Atualiza tabela pedido
				GuiGlobals.getDaoFactory().getOrderDao().update( order );
									
				// insere historico
				GuiGlobals.getDaoFactory().getHistoricDao().add(h);
				
				GuiGlobals.getDaoFactory().commit();
									
				
			}
			catch( Exception e){
				GuiGlobals.getDaoFactory().rollback();
				GuiGlobals.showMessageDlg("Falha na atualização do pedido. " + e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
	
	public void rollBackItem( ItensShipment item ){
		
		int idProduct = item.getProduct().getId();
						
		StockFilter filter = new StockFilter();
		filter.setIdOrderImport( order.getId() );
		filter.setProductId( idProduct );
		filter.setOnlyStocked(false);
		List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().list(filter);
		
		if ( stocks != null && stocks.size() > 0 ) 
		{		
			GuiGlobals.getDaoFactory().beginTransaction();
			
			try{
	
				/**
				 * Atualiza estoque
				 */
				for ( Stock stock : stocks ){
					
					// altera status do estoque
					stock.setStatus(StockStatus.STOCKED);
					stock.setOrder(null);
					
					Pallet pallet = null;
					if ( stock.getPallet() != null && stock.getPallet().getOrder() != null && stock.getPallet().getId() > 0 )
					{
						pallet = stock.getPallet();
						pallet.setOrder( null );
					}
					
					// instancia historico
					Historic h = new Historic();
					h.setBatch(null);
					h.setDate(DateTimeUtils.now());
					h.setNet(0);
					h.setOperation(Operation.SHIPMENT_STOCK_ROLLBACK);
					h.setOrder( order );
					h.setPartner(order.getPartner());
					h.setPrimaryQty(0);
					h.setProduct( stock.getProduct() );
					h.setSecondaryQty(0);
					h.setStock( stock );
					h.setTare(0);
					h.setTerminal(Setup.getTerminal());
					h.setCompany(Setup.getCompany());
					h.setUser(GuiGlobals.getUserInfo().getUser());
					h.setPallet(null);
					
					// Atualiza tabela pallet
					if ( pallet != null )
					{
						GuiGlobals.getDaoFactory().getPalletDao().update( pallet );
					}
					
					// Atualiza tabela stock
					GuiGlobals.getDaoFactory().getStockDao().update( stock );
										
					// insere historico
					GuiGlobals.getDaoFactory().getHistoricDao().add(h);
					
				}
				
				
				/**
				 * Atualiza tabela de pedidos
				 */
				OrderItem orderItem = GuiGlobals.getDaoFactory().getOrderDao().findItemByIdImport( idOrderImport, idProduct, item.getItem() );
				orderItem.setPrimaryQty( 0 );
				orderItem.setQtyIssued( 0 );
				orderItem.setTotExpHowSell( 0 );
				orderItem.setUnitHowSell(null);
				
				GuiGlobals.getDaoFactory().getOrderDao().updateItem( orderItem );
				
				//-------------------
				//- Grava historico -
				//-------------------
				Historic h = new Historic();
				h.setBatch(null);
				h.setDate(DateTimeUtils.now());
				h.setNet(0);
				h.setOperation(Operation.SHIPMENT_ITEM_ROLLBACK);
				h.setOrder( orderItem.getOrder() );
				h.setOrderItem( orderItem );
				h.setPartner(order.getPartner());
				h.setPrimaryQty(0);
				h.setProduct( orderItem.getProduct() );
				h.setSecondaryQty(0);
				h.setStock( null );
				h.setTare(0);
				h.setTerminal(Setup.getTerminal());
				h.setCompany(Setup.getCompany());
				h.setUser(GuiGlobals.getUserInfo().getUser());
				h.setPallet(null);
				
				GuiGlobals.getDaoFactory().getHistoricDao().add(h);
				
				GuiGlobals.getDaoFactory().commit();
									
				
			}
			catch( Exception e){
				GuiGlobals.getDaoFactory().rollback();
				GuiGlobals.showMessageDlg("Falha na atualização do itens. " + e.getMessage());
				e.printStackTrace();
			}
			
		}
		
	}

	public void updateOrderItem(OrderItem orderItem) {
		
		if ( orderItem != null ){
					
			order.setStatus( OrderStatus.STARTED );
			
			// instancia historico
			Historic h = new Historic();
			h.setBatch(null);
			h.setDate(DateTimeUtils.now());
			h.setNet(0);
			h.setOperation(Operation.SHIPMENT_ITEM);
			h.setOrder( order );
			h.setOrderItem( orderItem );
			h.setPartner(order.getPartner());
			h.setPrimaryQty(0);
			h.setProduct( orderItem.getProduct() );
			h.setSecondaryQty(0);
			h.setStock( null );
			h.setTare(0);
			h.setTerminal(Setup.getTerminal());
			h.setCompany(Setup.getCompany());
			h.setUser(GuiGlobals.getUserInfo().getUser());
			h.setPallet(null);
			
			GuiGlobals.getDaoFactory().beginTransaction();
			
			try{
				
				GuiGlobals.getDaoFactory().getOrderDao().update(order);
				GuiGlobals.getDaoFactory().getOrderDao().updateItem( orderItem );								
				GuiGlobals.getDaoFactory().getHistoricDao().add(h);				
				GuiGlobals.getDaoFactory().commit();
				
				GuiGlobals.getDaoFactory().getOrderDao().refresh(order);
				
			}catch(Exception e){
				GuiGlobals.getDaoFactory().rollback();
				e.printStackTrace();
			}
			
		}
		
	}

	public void leaveStock(Stock stock) {
		
		StockService service = new StockService();
		service.leaveStock(stock, Operation.SHIPMENT_STOCK_REMOVING, Setup.getTerminal());
		
	}
	
	public void leaveStockMP(Stock stock) 
	{
		
		StockService service = new StockService();
		service.leaveStock(stock, Operation.SHIPMENT_STOCK_REMOVING_MP, Setup.getTerminal());
	}
	
	public List<Stock> getProductsFifoOnStock( Product product ) 
	{
		StockFilter filter = new StockFilter();
		filter.setOnlyStocked(true);
		filter.setOnlyFifo(true);		
		filter.setProductId( product.getId() );
		filter.setNotPallet(true);
		filter.setOrderBy("manufactureDate");
		filter.setAsc(true);
		
		return GuiGlobals.getDaoFactory().getStockDao().list(filter);
	}

	public Company getCompanyById(String idFilial) {
		return GuiGlobals.getDaoFactory().getCompanyDao().find( idFilial );
	}

	public Stock getStockByIdAndCompany(long idEtiqueta, Company company) {		
		return GuiGlobals.getDaoFactory().getStockDao().findWithCompanyOnlyStocked( idEtiqueta, company );
	}

	public OrderItem getOrderItemByIdImportAndProductAndIndex(Product product, int index) 
	{		
		GuiGlobals.getDaoFactory().getOrderDao().refresh(order);
		return GuiGlobals.getDaoFactory().getOrderDao().findItemByIdImport(order,product,index);
	}

	public Order findOrderByIdImport(String idImport) {		
		GuiGlobals.refreshDaoFactory();
		return GuiGlobals.getDaoFactory().getOrderDao().findByIdImport( idImport );		
	}

	public String getDirArq() {
		
		ServerSetup server = GuiGlobals.getDaoFactory().getServerSetupDao().find();
		
		if ( server != null ){
			return server.getStoreDirectory() + GuiGlobals.getSeparador();
		}
		
		return null;		
	}

	public void importOrder() {
		
		ImportacaoPedidosExpedicao importar = new ImportacaoPedidosExpedicao( idOrderImport );		
		importar.execute(true);
		
	}

	public boolean createArqExpAndTotAndHea() 
	{
		boolean processOk = true;
		List<OrderItem> itensTot = new ArrayList<OrderItem>();
		List<OrderItem> orderItens;
		List<IndexByProductToArqExp> idItensForProducts = new ArrayList<IndexByProductToArqExp>();
		
		GuiGlobals.getDaoFactory().getOrderDao().refresh(order);
		orderItens = order.cloneItens();
		
		try
		{
			ShipmentArchieveTot arqTot = new ShipmentArchieveTot( idOrderImport );
			ShipmentArchieveExp arqExp = new ShipmentArchieveExp( idOrderImport );
			ShipmentArchieveHea arqHea = new ShipmentArchieveHea( idOrderImport );
			
			if ( !arqTot.isCreated() )
			{			
	  			GuiGlobals.showMessageDlg("Arquivo " + arqTot.getNameArqTot() + " não será gerado pois não foi configurado no sistema. Para configurar, o administrador deve indicar o diretório em 'Manutenção->Configurações' !");
	  			processOk = false;
	  		}
			
			if ( processOk && !arqExp.isCreated() )
			{			
	  			GuiGlobals.showMessageDlg("Arquivo " + arqExp.getNameArqExp() + " não será gerado pois não foi configurado no sistema. Para configurar, o administrador deve indicar o diretório em 'Manutenção->Configurações' !");
	  			arqTot.finish();
	  			processOk = false;
	  		}			
						
			if ( processOk )
			{						
				processOk = false;
				
				/**
				 * Escrevendo arquivo .HEA
				 */
				arqHea.setIdOrderImport(idOrderImport);
				arqHea.setNameClient( order.getPartner().getName().trim() );
				arqHea.setAuthorizationShipment( order.getCodEmpTotvs() );
				arqHea.setDateMinBox("00000000");
				arqHea.setDateMaxBox("99999999");
				arqHea.print();
				arqHea.finish();
				
				/**
	  			 * Escrevendo arquivo .exp
	  			 */
				for ( OrderItem item : orderItens )
				{
					String unitExp = item.getUnit().getId();
					int idItem = getItemToArqExp(idItensForProducts, item, orderItens);
					String idMascProduct = item.getProduct().getIdMasc();
					
					if ( !item.isDetailItem() )
						contabilizaTotal(itensTot, item, unitExp, idMascProduct);
			  		
					if ( item.getQtyIssued() > 0 )
					{
						if ( TypeStock.EMBALAGEM.equals( item.getTypeStock() ) )
				  		{
				  			StockFilter filter = new StockFilter();
				  			filter.setIdOrderImport( order.getId() );
				  			filter.setProductId( item.getProduct().getId() );
				  			filter.setOnlyStocked(false);
				  			filter.setAsc(true);
				  			List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().list(filter);
				  			
				  			if ( stocks != null && stocks.size() > 0 )
				  			{
					  			for ( Stock stock : stocks )
					  			{
					  				arqExp.clear();
					  				arqExp.setIdOrderImport(idOrderImport);
					  				arqExp.setStockId( stock.getId() );
					  				arqExp.setItemOnOrder( idItem );
					  				arqExp.setIdMascProduct( idMascProduct );
					  				arqExp.setNetStock( stock.getNet() );
					  				arqExp.setTaraStock( stock.getTare() );
					  				arqExp.setPrimaryQty( new Double( stock.getHowSelled() ).intValue() ); // Como vende
					  				arqExp.setUnitExp( unitExp );
					  				arqExp.setDateProduction( stock.getManufactureDate() );
					  				
					  				if ( stock.getPallet() == null )
					  				{
					  					arqExp.setPalletId( 0 );
					  				}
					  				else
					  				{
					  					arqExp.setPalletId( stock.getPallet().getId() );
					  				}
					  				
					  				arqExp.setTypeOperation( item.getTypeOperation() );				  		
					  				arqExp.setTypeStock( item.getTypeStock() );
					  				
					  				arqExp.print();
							  		
					  			}
				  			}
					  		
				  		}
						else if ( item.isDetailItem() )
				  		{
				  			arqExp.clear();
				  			arqExp.setIdOrderImport(idOrderImport);
				  			arqExp.setStockId( 0 );
			  				arqExp.setItemOnOrder( idItem );
			  				arqExp.setIdMascProduct( idMascProduct );
			  				arqExp.setNetStock( item.getQtyIssued() );
			  				arqExp.setTaraStock( 0 );
			  				arqExp.setPrimaryQty( item.getPrimaryQty() );
			  				arqExp.setUnitExp( unitExp );
			  				arqExp.setPalletId( 0 );
			  				arqExp.setTypeOperation( item.getTypeOperation() );				  		
			  				arqExp.setTypeStock( item.getTypeStock() );
			  				
			  				arqExp.print();
				  		}
					}
				}
				
				if ( itensTot.size() > 0 )
				{
				
					/**
					 * Escrevendo arquivo .TOT
					 */
					itensTot.sort( new Comparator<OrderItem>() 
					{  
						public int compare(OrderItem orderItem1, OrderItem orderItem2) 
						{  
			                OrderItem p1 = orderItem1;  
			                OrderItem p2 = orderItem2;  
			                return p2.getIndex() < p1.getIndex() ? -1 : (p2.getIndex() > p1.getIndex() ? +1 : 0);  
			            }
					});
					for ( OrderItem itemTot : itensTot )
					{
						arqTot.setItemOnOrder( itemTot.getIndex() );
						arqTot.setIdMascProduct( itemTot.getProduct().getIdMasc() );
						arqTot.setNet( itemTot.getTotExpHowSell() );
						arqTot.setPrimaryQty( itemTot.getPrimaryQty() );
						arqTot.setUnitIssued( itemTot.getUnit().getId() );
					
						arqTot.print();
					}
					
				}
		
				arqTot.finish();
				arqExp.finish();
			}
	
			processOk = true;
			
		} catch (IOException e) {
			e.printStackTrace();
			warnings = e.getMessage();
		}
		
		return processOk;
  				
	}

	private int getItemToArqExp(List<IndexByProductToArqExp> idItensForProducts, OrderItem currentItem, List<OrderItem> orderItens) 
	{
		int index = 0;
		IndexByProductToArqExp indexByProduct = new IndexByProductToArqExp();
		indexByProduct.setProduct(currentItem.getProduct());
		
		if ( idItensForProducts == null || idItensForProducts.size() == 0 )
		{
			if ( TypeStock.EMBALAGEM.equals(currentItem.getTypeStock()) || TypeStock.MATERIA_PRIMA.equals(currentItem.getTypeStock()) )
			{
				indexByProduct.setIndex(currentItem.getIndex());
				idItensForProducts.add(indexByProduct);
				index = currentItem.getIndex();
			}
			else
			{
				for( OrderItem item : orderItens)
				{
					if ( TypeStock.EMBALAGEM.equals(item.getTypeStock()) || currentItem.getProduct().equals(item.getProduct()))
					{
						indexByProduct.setIndex(item.getIndex());
						index = item.getIndex();
						break;
					}
				}
			}
		}
		else
		{
			int posFounded = idItensForProducts.indexOf(indexByProduct);
			if ( posFounded >= 0 )
			{
				index = idItensForProducts.get(posFounded).getIndex();
			}
			else
			{
				indexByProduct.setIndex(currentItem.getIndex());
				idItensForProducts.add(indexByProduct);
				index = currentItem.getIndex();
			}
		}
		
		return index;
	}

	private void contabilizaTotal(List<OrderItem> itensTot, OrderItem item, String unitExp, String idMascProduct) 
	{
		if ( !TypeStock.GRANEL.equals( item.getTypeStock() ) )
		{
			if ( !itensTot.isEmpty() )
			{
				boolean found = false;
				
				for ( OrderItem itemTot : itensTot )
				{
					if (	itemTot.getProduct().getIdMasc().trim().equals( idMascProduct.trim() ) 
							&& itemTot.getUnit().getId().equals( unitExp )
							&& itemTot.getTypeStock().equals( item.getTypeStock() )
						)
					{
						found = true;
						itemTot.setPrimaryQty(itemTot.getPrimaryQty() + item.getPrimaryQty() );
						itemTot.setQtyIssued( itemTot.getQtyIssued() + item.getQtyIssued() );
						itemTot.setQtdMissing( itemTot.getQtdMissing() + item.getQtdMissing() );
						itemTot.setQtyRequested( itemTot.getQtyRequested() + item.getQtyRequested() );
						itemTot.setTotExpHowSell(itemTot.getTotExpHowSell() + item.getTotExpHowSell());
						break;
					}
				}
				
				if ( !found )
				{
					itensTot.add( item );
				}
				
			}
			else
			{
				itensTot.add( item );
			}
		}
	}

	public String getIdOrderImport() {
		return idOrderImport;
	}

	public void setIdOrderImport(String idOrderImport) {
		this.idOrderImport = StringUtils.leftPad(idOrderImport,6, "0" ) ;
	}

	public boolean leaveStockMPfromOrder(Product product, double netPicked, int qtyPicked,  int itemOrder, Unit unitOfKg) 
	{
		boolean processOk = false;
		StockService service = new StockService();
		OrderItem orderItem = new OrderItem();
		
		orderItem.setCompany(Setup.getCompany());
		orderItem.setTerminal(Setup.getTerminal());
		orderItem.setIndex(itemOrder);
		orderItem.setOrder(order);
		orderItem.setProduct(product);
		orderItem.setUnit(unitOfKg);
		orderItem.setQtyIssued(netPicked);
		orderItem.setQtyRequested(netPicked);
		orderItem.setTypeStock( TypeStock.MATERIA_PRIMA );
		orderItem.setPrimaryQty(qtyPicked);
		orderItem.setTypeOperation(ShipmentTypeOperation.MANUAL);
		orderItem.setTotExpHowSell(netPicked);
		orderItem.setDetailItem(true);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			
			GuiGlobals.getDaoFactory().getOrderDao().addItem( orderItem );
			processOk = service.leaveStockMPfromOrder(order, product, netPicked, qtyPicked,  itemOrder, unitOfKg, false);
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e){
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
			return false;
		}
		
		return processOk;
	}
	
	public boolean leaveStockGranel(Product product, double netPicked, int qtyPicked,  int itemOrder, Unit unitOfKg) 
	{
		
		boolean incluiGranel = false;		
		OrderItem orderItem = new OrderItem();
		
		orderItem.setCompany(Setup.getCompany());
		orderItem.setTerminal(Setup.getTerminal());
		orderItem.setIndex(itemOrder);
		orderItem.setOrder(order);
		orderItem.setProduct(product);
		orderItem.setUnit(unitOfKg);
		orderItem.setQtyIssued(netPicked);
		orderItem.setQtyRequested(netPicked);
		orderItem.setTypeStock( TypeStock.GRANEL );
		orderItem.setPrimaryQty(qtyPicked);
		orderItem.setTypeOperation(ShipmentTypeOperation.MANUAL);
		orderItem.setTotExpHowSell(netPicked);
		orderItem.setDetailItem(true);
		
		Stock stock = GuiGlobals.getDaoFactory().getStockDao().findProductByTypeStock(product, TypeStock.GRANEL);
		
		if ( stock == null ){ // se nao tem produto granel no estoque
			stock = new Stock();
			stock.setCompany(Setup.getCompany());
			stock.setTerminal(Setup.getTerminal());
			stock.setEnterDate( DateTimeUtils.now() );
			stock.setInFifo(false);
			stock.setManufactureDate( DateTimeUtils.now() );
			stock.setNet(0);
			stock.setNetEtq(0);
			stock.setPrimaryQty(0);
			stock.setSecondaryQty(0);
			stock.setProduct(product);
			stock.setStatus(StockStatus.STOCKED);
			stock.setTare(0);
			stock.setTypeStock(TypeStock.GRANEL);
			stock.setUser(GuiGlobals.getUserInfo().getUser());
			incluiGranel = true;
		}

		stock.setNet( stock.getNet() - netPicked );
		stock.setPrimaryQty( stock.getPrimaryQty() - qtyPicked );
		
		// instancia historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.COMPLEMENTO_GRANEL);
		h.setOrder( order );
		h.setPartner(order.getPartner());
		h.setPrimaryQty(0);
		h.setProduct( stock.getProduct() );
		h.setSecondaryQty(0);
		h.setStock( stock );
		h.setTare(0);
		h.setTerminal(Setup.getTerminal());
		h.setCompany(Setup.getCompany());
		h.setUser(GuiGlobals.getUserInfo().getUser());
		h.setPallet(null);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			
			GuiGlobals.getDaoFactory().getOrderDao().addItem( orderItem );
			
			if ( incluiGranel ){
				GuiGlobals.getDaoFactory().getStockDao().add(stock);
			}
			else{
				GuiGlobals.getDaoFactory().getStockDao().update(stock);
			}
			
			// insere historico
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e){
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
		}
		
		return true;		
	}

	public void rollBackItemGranel(ItensShipment item) 
	{

		Product product = item.getProduct();
		Stock stock = GuiGlobals.getDaoFactory().getStockDao().findProductByTypeStock(product, TypeStock.GRANEL);
		
		if ( stock != null ){
			
			/**
			 * Atualiza tabela de pedidos
			 */
			List<OrderItem> orderItensGranel = GuiGlobals.getDaoFactory().getOrderDao().findItemByTypeStock( idOrderImport, product.getId(), TypeStock.GRANEL );
			OrderItem orderItemShipment = GuiGlobals.getDaoFactory().getOrderDao().findItemByIdImport( idOrderImport, product.getId(), item.getItem() );
			
			for ( OrderItem orderItemGranel : orderItensGranel)
			{
				
				/**
				 * Atualiza estoque
				 */
				stock.setNet( stock.getNet() + orderItemGranel.getQtyRequested() );
				stock.setPrimaryQty( stock.getPrimaryQty() + orderItemGranel.getPrimaryQty() );
				
			}
			
			orderItemShipment.setQtyIssued( 0 );
			orderItemShipment.setTotExpHowSell( 0 );
			orderItemShipment.setPrimaryQty( 0 );
			orderItemShipment.setQtdMissing( orderItemShipment.getQtyRequested() );
			
			// instancia historico
			Historic h = new Historic();
			h.setBatch(null);
			h.setDate(DateTimeUtils.now());
			h.setNet(0);
			h.setOperation(Operation.COMPLEMENTO_GRANEL_ROWBACK);
			h.setOrder( order );
			h.setPartner(order.getPartner());
			h.setPrimaryQty(0);
			h.setProduct( stock.getProduct() );
			h.setSecondaryQty(0);
			h.setStock( stock );
			h.setTare(0);
			h.setTerminal(Setup.getTerminal());
			h.setCompany(Setup.getCompany());
			h.setUser(GuiGlobals.getUserInfo().getUser());
			h.setPallet(null);
			
			GuiGlobals.getDaoFactory().beginTransaction();
			
			try{
								
				GuiGlobals.getDaoFactory().getStockDao().update(stock);
				GuiGlobals.getDaoFactory().getOrderDao().updateItem(orderItemShipment);
				GuiGlobals.getDaoFactory().getOrderDao().deleteItens(orderItensGranel);
				
				// insere historico
				GuiGlobals.getDaoFactory().getHistoricDao().add(h);
				GuiGlobals.getDaoFactory().commit();
			}
			catch(Exception e){
				GuiGlobals.getDaoFactory().rollback();
				e.printStackTrace();
			}
			
		}
		else{
			GuiGlobals.showMessage("Produto [" + product.getIdMasc() + "] sem estoque [" + TypeStock.GRANEL.getDescricao().trim() + "]!");
		}
		
	}

	public int getNextItem() {
		return GuiGlobals.getDaoFactory().getOrderDao().getNextIndex( order );
	}

	public Stock getProductMP( Product product ) {		
		return GuiGlobals.getDaoFactory().getStockDao().findProductByTypeStock(product, TypeStock.MATERIA_PRIMA);
	}

	public boolean closeOrder() 
	{
		boolean processok = false;
		boolean isHappenShipment = isHappenShipment();
		
		if ( isHappenShipment )
		{
			order.setStatus( OrderStatus.FINISHED );
		}
		else
		{
			order.setStatus( OrderStatus.CANCELED );
		}
		
		// instancia historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.SHIPMENT_FINISHED_ORDER);
		h.setOrder( order );
		h.setOrderItem( null );
		h.setPartner(order.getPartner());
		h.setPrimaryQty(0);
		h.setProduct( null );
		h.setSecondaryQty(0);
		h.setStock( null );
		h.setTare(0);
		h.setTerminal(Setup.getTerminal());
		h.setCompany(Setup.getCompany());
		h.setUser(GuiGlobals.getUserInfo().getUser());
		h.setPallet(null);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			
			GuiGlobals.getDaoFactory().getOrderDao().update(order);
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);				
			GuiGlobals.getDaoFactory().commit();
			
			GuiGlobals.getDaoFactory().getOrderDao().refresh(order);
			
			processok = true;
			
		}catch(Exception e){
			GuiGlobals.getDaoFactory().rollback();
			warnings = e.getMessage();
			e.printStackTrace();
		}
		
		return processok;
		
	}

	private boolean isHappenShipment() 
	{
		boolean isHappenShipment = false;
		
		if ( order != null && order.getId() > 0 )
		{	
			if ( order.getItems() == null )
			{
				GuiGlobals.getDaoFactory().getOrderDao().refresh(order);
			}
			
			List<OrderItem> itens = order.getItemsCleaning();
			
			for ( OrderItem item : itens )
			{
				if ( item.getQtyIssued() > 0 )
				{
					isHappenShipment = true;
					break;
				}
			}
		}
		return isHappenShipment;
	}

	public Product findProduct(Product product) {
		GuiGlobals.refreshDaoFactory();
		return GuiGlobals.getDaoFactory().getProductDao().find( product.getId() );
	}

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public List<Stock> getStocksByIdPallet(int idEtiquetaPallet) {
		GuiGlobals.refreshDaoFactory();
		return GuiGlobals.getDaoFactory().getStockDao().listStockOfPalletStocked(idEtiquetaPallet);
	}

	public boolean removeOrder() 
	{
		boolean processok = false;
		
		// instancia historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.SHIPMENT_DELETE_ORDER);
		h.setOrder(null);
		h.setOrderItem( null );
		h.setPartner(order.getPartner());
		h.setPrimaryQty(0);
		h.setProduct( null );
		h.setSecondaryQty(0);
		h.setStock( null );
		h.setTare(0);
		h.setTerminal(Setup.getTerminal());
		h.setCompany(Setup.getCompany());
		h.setUser(GuiGlobals.getUserInfo().getUser());
		h.setPallet(null);
		h.setObservation("Exclusão do pedido " + order.getIdPedidoImport());
		
		/**
		 * Lista todo o historico do pedido para ser excluido antes, pois se isso nao acontece,
		 * as constrants do banco de dados impede a exclusão do pedido
		 */
		HistoricFilter historicFilter = new HistoricFilter();
		historicFilter.setOrder(order);
		
		List<Historic> historics = GuiGlobals.getDaoFactory().getHistoricDao().list(historicFilter);
		
		historicFilter.setOrder(null);
		
		for ( OrderItem item : order.getItems() )
		{
			historicFilter.setOrderItem(item);
			historics.addAll( GuiGlobals.getDaoFactory().getHistoricDao().list(historicFilter) );
		}
		
		PurchaseOrder purchaseOrder = GuiGlobals.getDaoFactory().getPurchaseOrderDao().getPurchaseOrderByOrderShipment(order);
		List<PurchaseOrderItem> purchaseOrderItens = GuiGlobals.getDaoFactory().getPurchaseOrderDao().getPurchaseOrderItensByOrderShipment(order);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			GuiGlobals.getDaoFactory().getHistoricDao().delete(historics);
			
			if ( purchaseOrderItens != null && purchaseOrderItens.size() > 0 )
			{
				for ( PurchaseOrderItem purchaseOrderItem : purchaseOrderItens )
				{
					purchaseOrderItem.setOrder(null);
					GuiGlobals.getDaoFactory().getPurchaseOrderDao().updateItem(purchaseOrderItem);
				}
			}
			
			if ( purchaseOrder != null && purchaseOrder.getId() > 0 )
			{
				purchaseOrder.setOrderShipment(null);
				GuiGlobals.getDaoFactory().getPurchaseOrderDao().update(purchaseOrder);
			}
			
			for ( OrderItem item : order.getItems() )
			{
				GuiGlobals.getDaoFactory().getOrderDao().deleteItem(item);
			}
			GuiGlobals.getDaoFactory().getOrderDao().delete(order);
			GuiGlobals.getDaoFactory().commit();
			
			processok = true;
			
		}
		catch(Exception e)
		{
			GuiGlobals.getDaoFactory().rollback();
			warnings = e.getCause().getMessage();
			e.printStackTrace();
		}
		
		return processok;
	}

	public Stock createStockMP(Product product) {
		
		StockService service = new StockService();
		long idCreated = 0;
		try {
			idCreated = service.generateStockId();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Stock stock = GuiGlobals.getDaoFactory().getStockDao().find(idCreated);
		stock.setTypeStock(TypeStock.MATERIA_PRIMA);
		stock.setEnterDate(DateTimeUtils.now());
		stock.setCompany(Setup.getCompany());
		stock.setTerminal(Setup.getTerminal());
		stock.setProduct(product);
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			GuiGlobals.getDaoFactory().getStockDao().update(stock);
			GuiGlobals.getDaoFactory().commit();
			GuiGlobals.getDaoFactory().getStockDao().refresh(stock);			
		}catch(Exception e){
			GuiGlobals.getDaoFactory().rollback();
			warnings = e.getMessage();
			e.printStackTrace();
		}
		return stock;
	}
	
	public void updateOrder( Order order )
	{		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			GuiGlobals.getDaoFactory().getOrderDao().update( order );
			GuiGlobals.getDaoFactory().commit();
		}catch(Exception e){
			GuiGlobals.getDaoFactory().rollback();			
			e.printStackTrace();
		}
		GuiGlobals.getDaoFactory().getOrderDao().refresh(order);
	}
	
	private void newUserInProcess(UserInProcess userInProcess) 
	{
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try{
			GuiGlobals.getDaoFactory().getUserInProcessDao().add( userInProcess );
			GuiGlobals.getDaoFactory().commit();
		}catch(Exception e){
			GuiGlobals.getDaoFactory().rollback();			
			e.printStackTrace();
		}
		GuiGlobals.getDaoFactory().getUserInProcessDao().refresh(userInProcess);
	}

	public void clearUserInProcess() 
	{
		if ( order != null && order.getId() > 0 )
		{
			
			if ( order.getUserInProcess() != null && order.getUserInProcess().getId() > 0 )
			{
				UserInProcess userInProcessToRemove = order.getUserInProcess();
				order.setUserInProcess(null);
				
				GuiGlobals.getDaoFactory().beginTransaction();
				
				try{
					GuiGlobals.getDaoFactory().getOrderDao().update(order);
					GuiGlobals.getDaoFactory().getUserInProcessDao().delete( userInProcessToRemove );
					GuiGlobals.getDaoFactory().commit();
				}catch(Exception e){
					GuiGlobals.getDaoFactory().rollback();			
					e.printStackTrace();
				}
			}
			else
			{
				order.setUserInProcess(null);
				updateOrder(order);
			}
			
		}
	}
	
	public List<StockTotal> getSaldo( Product product )
	{
		return getSaldo( product, false );
	}
	
	public List<StockTotal> getSaldo( Product product, boolean onlyFifo)
	{
		StockFilter filter = new StockFilter();
		GuiGlobals.refreshDaoFactory();
		StockDao stockDao = GuiGlobals.getDaoFactory().getStockDao();
		filter.setProductId( product.getId() );
		filter.setTypeStock( product.getTypeStock() );
		filter.setOnlyFifo(onlyFifo);
		filter.setNotPallet(onlyFifo);
		
		return stockDao.totalize(filter);
	}

	public void updatePallet(Pallet pallet) 
	{
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try
		{
			GuiGlobals.getDaoFactory().getPalletDao().update( pallet );
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e)
		{
			GuiGlobals.getDaoFactory().rollback();			
			e.printStackTrace();
		}
		GuiGlobals.getDaoFactory().getPalletDao().refresh( pallet );
	}

	public void rollBackItemMP(ItensShipment item) 
	{
		Product product = item.getProduct();
		Stock stock = GuiGlobals.getDaoFactory().getStockDao().findProductByTypeStock(product, TypeStock.MATERIA_PRIMA);
		
		if ( stock != null ){
			
			/**
			 * Atualiza tabela de pedidos
			 */
			List<OrderItem> orderItensMP = GuiGlobals.getDaoFactory().getOrderDao().findItemByTypeStock( idOrderImport, product.getId(), TypeStock.MATERIA_PRIMA );
			OrderItem orderItemShipment = GuiGlobals.getDaoFactory().getOrderDao().findItemByIdImport( idOrderImport, product.getId(), item.getItem() );
			
			for ( OrderItem orderItemMP : orderItensMP)
			{
				
				/**
				 * Atualiza estoque
				 */
				stock.setNet( stock.getNet() + orderItemMP.getQtyRequested() );
				stock.setPrimaryQty( stock.getPrimaryQty() + orderItemMP.getPrimaryQty() );
				
			}
			
			orderItemShipment.setQtyIssued( 0 );
			orderItemShipment.setTotExpHowSell( 0 );
			orderItemShipment.setPrimaryQty( 0 );
			orderItemShipment.setQtdMissing( orderItemShipment.getQtyRequested() );
			
			Historic h = new Historic();
			h.setBatch(null);
			h.setDate(DateTimeUtils.now());
			h.setNet(0);
			h.setOperation(Operation.SHIPMENT_STOCK_REMOVING_MP_ROLLBACK);
			h.setOrder( order );
			h.setPartner(order.getPartner());
			h.setPrimaryQty(0);
			h.setProduct( stock.getProduct() );
			h.setSecondaryQty(0);
			h.setStock( stock );
			h.setTare(0);
			h.setTerminal(Setup.getTerminal());
			h.setCompany(Setup.getCompany());
			h.setUser(GuiGlobals.getUserInfo().getUser());
			h.setPallet(null);
			
			GuiGlobals.getDaoFactory().beginTransaction();
			
			try
			{
				GuiGlobals.getDaoFactory().getStockDao().update(stock);
				GuiGlobals.getDaoFactory().getOrderDao().updateItem(orderItemShipment);
				GuiGlobals.getDaoFactory().getHistoricDao().add(h);
				GuiGlobals.getDaoFactory().commit();
			}
			catch(Exception e)
			{
				GuiGlobals.getDaoFactory().rollback();
				e.printStackTrace();
			}
			
		}
		else{
			GuiGlobals.showMessage("Produto [" + product.getIdMasc() + "] sem estoque [" + TypeStock.MATERIA_PRIMA.getDescricao().trim() + "]!");
		}	
	}

	public void checkStatusOrderFromExit() 
	{
		if ( order != null && order.getId() > 0 )
		{
			if ( OrderStatus.STARTED.equals(order.getStatus()) && !isHappenShipment() )
			{
				order.setStatus( OrderStatus.NOT_STARTED );
			}
		}
	}

	public void refreshMultipleStocks(List<Stock> stocks) 
	{
		for ( Stock stock : stocks )
		{
			GuiGlobals.showMessage("Aguarde processamento ... ", false);
			leaveStock(stock);
		}
	}
	
}
