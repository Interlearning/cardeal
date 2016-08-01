package br.com.cardeal.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import br.com.cardeal.enums.Operation;
import br.com.cardeal.enums.StockStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.filter.HistoricFilter;
import br.com.cardeal.filter.StockFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.Enterprise;
import br.com.cardeal.model.Historic;
import br.com.cardeal.model.IdentifyLogisticPallet;
import br.com.cardeal.model.IdentifyLogisticProduct;
import br.com.cardeal.model.Order;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.model.ServerSetup;
import br.com.cardeal.model.Stock;
import br.com.cardeal.model.StockManuten;
import br.com.cardeal.model.Terminal;
import br.com.cardeal.model.Unit;
import br.com.cardeal.model.User;

public class StockService {
		
	private User user;
	private ServerSetup serverConfig;
	
	public StockService() {
		user = GuiGlobals.getUserInfo().getUser();
		setDefaultServerConfig();
	}

	public StockService(User user) {
		this.user = user;
		setDefaultServerConfig();
	}
	
	private void setDefaultServerConfig(){
		serverConfig = GuiGlobals.getDaoFactory().getServerSetupDao().find();
	}
	
	//------------------------------------------------
	//remove um item numerado e que existe em estoque
	//------------------------------------------------
	public void leaveStock(Stock stock, Operation operation, Terminal terminal)
	{
		leaveStock(stock, operation, terminal, false, "99") ;
	}
	
	public void leaveStock(Stock stock, Operation operation, Terminal terminal, boolean createManuten, String motivo) 
	{
		//exclui o item numerado do estoque	
		stock.setAvailableNet(0);
		stock.setAvailableQty(0);
		stock.setStatus(StockStatus.NON_STOCKED);
		
		//instancia um historico
		Historic h = new Historic();
		h.setBatch(stock.getBatch());
		h.setDate(DateTimeUtils.now());
		h.setNet(stock.getNet());
		h.setOperation(operation);
		h.setOrder(null);
		h.setPartner(stock.getPartner());
		h.setPrimaryQty(stock.getPrimaryQty());
		h.setProduct(stock.getProduct());
		h.setSecondaryQty(1);
		h.setStock(stock);
		h.setTare(stock.getTare());
		h.setTerminal(terminal);
		h.setCompany(stock.getCompany());
		h.setUser(user);
		h.setPallet(stock.getPallet());
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try 
		{
			GuiGlobals.getDaoFactory().getStockDao().update(stock);
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			GuiGlobals.getDaoFactory().commit();
			
			GuiGlobals.getDaoFactory().getStockDao().refresh(stock);
			
			if ( createManuten )
			{
				geraManuten(stock, "A", motivo);
			}
		}
		catch(Exception e) 
		{
			LogDeProcessamento.gravaLog("error", "Erro na atualização de estoque: " + e.getMessage(), true);
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();
		}
	}
	
	public void addStockMPfromPurchaseOrder(PurchaseOrderItem purchaseOrdemItem) 
	{
		addStockMPfromPurchaseOrder(purchaseOrdemItem, true);
	}
	public void addStockMPfromPurchaseOrder(PurchaseOrderItem purchaseOrdemItem, boolean isCommit) 
	{
		Stock stock = null;
		boolean incluiMP = false;
		
		stock = GuiGlobals.getDaoFactory().getStockDao().findProductByTypeStock(purchaseOrdemItem.getProduct(), purchaseOrdemItem.getProduct().getTypeStock() );
		
		if ( stock == null ) // se nao tem produto no estoque
		{ 
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
			stock.setProduct(purchaseOrdemItem.getProduct());
			stock.setStatus(StockStatus.STOCKED);
			stock.setTare(0);
			stock.setTypeStock(purchaseOrdemItem.getProduct().getTypeStock());
			stock.setUser(GuiGlobals.getUserInfo().getUser());
			incluiMP = true;
		}
		
		stock.setNet( stock.getNet() + purchaseOrdemItem.getNet() );
		stock.setPrimaryQty( stock.getPrimaryQty() + new Double( purchaseOrdemItem.getQuantity() ).intValue() );
		
		// instancia historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.RECEIPT_PURCHASE);
		h.setPurchaseOrder( purchaseOrdemItem.getPurchaseOrder() );
		h.setPartner( purchaseOrdemItem.getPurchaseOrder().getPartner() );
		h.setPrimaryQty( new Double( purchaseOrdemItem.getQuantity() ).intValue() );
		h.setNet(purchaseOrdemItem.getNet());
		h.setProduct( stock.getProduct() );
		h.setSecondaryQty(0);
		h.setStock( stock );
		h.setTare(0);
		h.setTerminal(Setup.getTerminal());
		h.setCompany(Setup.getCompany());
		h.setUser(GuiGlobals.getUserInfo().getUser());
		h.setPallet(null);
		
		if ( isCommit )
		{
			GuiGlobals.getDaoFactory().beginTransaction();
			try
			{
				addStockMPFromPurshaseOrderAux( incluiMP, stock, h);
				GuiGlobals.getDaoFactory().commit();
			}
			catch(Exception e)
			{
				GuiGlobals.getDaoFactory().rollback();
				e.printStackTrace();
			}
		}
		else
		{
			addStockMPFromPurshaseOrderAux( incluiMP, stock, h);
		}
		
	}
	
	private void addStockMPFromPurshaseOrderAux( boolean incluiMP, Stock stock, Historic h)
	{
		if ( incluiMP )
		{
			GuiGlobals.getDaoFactory().getStockDao().add(stock);
		}
		else{
			GuiGlobals.getDaoFactory().getStockDao().update(stock);
		}
		
		// insere historico
		GuiGlobals.getDaoFactory().getHistoricDao().add(h);
	}
	
	public boolean leaveStockMPfromOrder(Order order, Product product, double netPicked, int qtyPicked,  int itemOrder, Unit unitOfKg, boolean isCommit) 
	{
		boolean incluiMP = false;
		Stock stock = null;
		
		stock = GuiGlobals.getDaoFactory().getStockDao().findProductByTypeStock(product, TypeStock.MATERIA_PRIMA);
		
		if ( stock == null ) // se nao tem produto no estoque
		{ 
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
			stock.setTypeStock(TypeStock.MATERIA_PRIMA);
			stock.setUser(GuiGlobals.getUserInfo().getUser());
			incluiMP = true;
		}

		stock.setNet( stock.getNet() - netPicked );
		stock.setPrimaryQty( stock.getPrimaryQty() - qtyPicked );
		
		// instancia historico
		Historic h = new Historic();
		h.setBatch(null);
		h.setDate(DateTimeUtils.now());
		h.setNet(0);
		h.setOperation(Operation.SHIPMENT_STOCK_REMOVING_MP);
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
		
		if ( isCommit )
		{
			GuiGlobals.getDaoFactory().beginTransaction();
			
			try
			{
				manutenMP( incluiMP, stock, h);
				GuiGlobals.getDaoFactory().commit();
			}
			catch(Exception e){
				GuiGlobals.getDaoFactory().rollback();
				e.printStackTrace();
			}
		}
		else
		{
			manutenMP( incluiMP, stock, h);
		}
		
		return true;
	}
	
	private void manutenMP( boolean incluiMP, Stock stock, Historic h)
	{
		if ( incluiMP )
		{
			GuiGlobals.getDaoFactory().getStockDao().add(stock);
		}
		else{
			GuiGlobals.getDaoFactory().getStockDao().update(stock);
		}
		GuiGlobals.getDaoFactory().getHistoricDao().add(h);
	}
	
	public void leaveStockKg(Stock stock, Operation operation, Terminal terminal, boolean createManuten, boolean isNonStocked) 
	{
		//exclui o item numerado do estoque	
		stock.setAvailableNet(0);
		stock.setAvailableQty(0);
		if ( isNonStocked )
			stock.setStatus(StockStatus.NON_STOCKED);
		
		//instancia um historico
		Historic h = new Historic();
		h.setBatch(stock.getBatch());
		h.setDate(DateTimeUtils.now());
		h.setNet(stock.getNet());
		h.setOperation(operation);
		h.setOrder(null);
		h.setPartner(stock.getPartner());
		h.setPrimaryQty(stock.getPrimaryQty());
		h.setProduct(stock.getProduct());
		h.setSecondaryQty(1);
		h.setStock(stock);
		h.setTare(stock.getTare());
		h.setTerminal(terminal);
		h.setCompany(stock.getCompany());
		h.setUser(user);
		h.setPallet(stock.getPallet());
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try 
		{
			GuiGlobals.getDaoFactory().getStockDao().update(stock);
			
			//grava o historico na base
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			
			GuiGlobals.getDaoFactory().commit();
			GuiGlobals.getDaoFactory().getStockDao().refresh(stock);
			
			if ( createManuten )
			{
				geraManuten(stock, "A", "99");
			}
		}
		catch(Exception e) 
		{
			GuiGlobals.getDaoFactory().rollback();
			throw(e);
		}
	}
	
	public void devolutionByReceipt(Stock stock) {
		
		Historic h = new Historic();
		h.setBatch(stock.getBatch());
		h.setDate(DateTimeUtils.now());
		h.setNet(stock.getNet());
		h.setOperation(Operation.RECEIPT_DEVOLUTION);
		h.setOrder(null);
		h.setPartner(stock.getPartner());
		h.setPrimaryQty(stock.getPrimaryQty());
		h.setProduct(stock.getProduct());
		h.setSecondaryQty(1);
		h.setStock(stock);
		h.setTare(stock.getTare());
		h.setTerminal(Setup.getTerminal());
		h.setCompany(stock.getCompany());
		h.setUser(user);
		h.setPallet(stock.getPallet());
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try {
			
			GuiGlobals.getDaoFactory().getStockDao().update(stock);
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			
			GuiGlobals.getDaoFactory().commit();
			GuiGlobals.getDaoFactory().getStockDao().refresh(stock);
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			throw(e);
		}
		
	}
	
	public void leaveStockMP(Stock stock, Operation operation, Terminal terminal) {
		
		//exclui o item numerado do estoque	
		stock.setAvailableNet(0);
		stock.setAvailableQty(0);
				
		//instancia um historico
		Historic h = new Historic();
		h.setBatch(stock.getBatch());
		h.setDate(DateTimeUtils.now());
		h.setNet(stock.getNet());
		h.setOperation(operation);
		h.setOrder(null);
		h.setPartner(stock.getPartner());
		h.setPrimaryQty(stock.getPrimaryQty());
		h.setProduct(stock.getProduct());
		h.setSecondaryQty(1);
		h.setStock(stock);
		h.setTare(stock.getTare());
		h.setTerminal(terminal);
		h.setCompany(stock.getCompany());
		h.setUser(user);
		h.setPallet(stock.getPallet());
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try {
			
			GuiGlobals.getDaoFactory().getStockDao().update(stock);
			
			//grava o historico na base
			GuiGlobals.getDaoFactory().getHistoricDao().add(h);
			
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			throw(e);
		}
	}

	//--------------------------------------------------------------------------------
	//estorna um item do estoque, desde que seja o ultimo item embalado em um terminal
	//--------------------------------------------------------------------------------
	public void rollbackStock(Stock stock, Operation operation, Terminal terminal) {
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
			rollbackStockNotCommit(stock, operation, terminal);			
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			throw(e);
		}
	}
	
	public void rollbackStockNotCommit(Stock stock, Operation operation, Terminal terminal) {
		
		//exclui o item numerado do estoque	
		GuiGlobals.getDaoFactory().getStockDao().delete(stock);		
		
		//instancia um historico
		Historic h = new Historic();
		h.setBatch(stock.getBatch());
		h.setDate(DateTimeUtils.now());
		h.setNet(stock.getNet());
		h.setOperation(operation);
		h.setOrder(null);
		h.setPartner(stock.getPartner());
		h.setPrimaryQty(stock.getPrimaryQty());
		h.setProduct(stock.getProduct());
		h.setSecondaryQty(1);
		h.setStock(stock);
		h.setTare(stock.getTare());
		h.setTerminal(terminal);
		h.setCompany(stock.getCompany());
		h.setUser(user);
		h.setPallet(stock.getPallet());
		
		//grava o historico na base
		GuiGlobals.getDaoFactory().getHistoricDao().add(h);
		
	}
	
	//------------------------------------------------
	//insere um item em estoque.
	//Mesmo que seja um item granel, será criado um novo item em estoque
	//com uma numeração
	//------------------------------------------------

	public long generateStockId() throws Exception {
		
		Stock stock = new Stock();
		long idCreated = 0;
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
			//insere o estoque nulo na base para pegar o proximo ID			
			GuiGlobals.getDaoFactory().getStockDao().add(stock);		
			GuiGlobals.getDaoFactory().commit();
			
		}
		catch(Exception e) {
			e.printStackTrace();
			GuiGlobals.getDaoFactory().rollback();
			throw(e);
		}
		
		idCreated = stock.getId();
		
		return idCreated;
	}
	
	public IdentifyLogisticProduct generateIdentifyLogisticProduct() throws Exception {
		
		IdentifyLogisticProduct identifyLogisticProduct = new IdentifyLogisticProduct();		
		GuiGlobals.refreshDaoFactory();
		/**
		 *  Aplica regra do sequencial logistico
		 */
		int idMax = 0;
		try{
			idMax = GuiGlobals.getDaoFactory().getIdentifyLogisticProductDao().getMaxId();
		}
		catch( Exception e){			
		}
		
		GuiGlobals.getDaoFactory().beginTransaction();
		if ( idMax > 0){
			
			int maxVarLogistc = GuiGlobals.getDaoFactory().getIdentifyLogisticProductDao().getMaxVarLogistc();
			int maxIdBase = GuiGlobals.getDaoFactory().getIdentifyLogisticProductDao().getMaxIdBase( maxVarLogistc );
			
			if ( maxIdBase == GuiGlobals.getEnterprise().getMaxIdSequenceLogisticBox() ){
				identifyLogisticProduct.setIdBase(1);
				
				if ( maxVarLogistc == 9 ){
					identifyLogisticProduct.setVariavelLogistica( ( 0 ) );
				}
				else{
					identifyLogisticProduct.setVariavelLogistica( ( maxVarLogistc + 1 ) );
				}
				
			}
			else{
				identifyLogisticProduct.setIdBase( ( maxIdBase + 1 ) );
				identifyLogisticProduct.setVariavelLogistica( maxVarLogistc );
			}
		
		}
		else{
			/**
			 * Primeira registro da tabela
			 */
			identifyLogisticProduct.setIdBase(1);
			identifyLogisticProduct.setVariavelLogistica(0);
		}
		
		/**
		 * Inclui registro
		 */		
		try {
			GuiGlobals.getDaoFactory().getIdentifyLogisticProductDao().add( identifyLogisticProduct );	
			GuiGlobals.getDaoFactory().commit();			
		}
		catch(Exception e) {
			e.printStackTrace();
			GuiGlobals.getDaoFactory().rollback();
			throw(e);
		}		
		
		return identifyLogisticProduct;
	}
	
	public IdentifyLogisticPallet generateIdentifyLogisticPallet() throws Exception {
		
		IdentifyLogisticPallet identifyLogisticPallet = new IdentifyLogisticPallet();		
		GuiGlobals.refreshDaoFactory();
		/**
		 *  Aplica regra do sequencial logistico
		 */
		int idMax = 0;
		try{
			idMax = GuiGlobals.getDaoFactory().getIdentifyLogisticPalletDao().getMaxId();
		}
		catch( Exception e){
			
		}
		
		GuiGlobals.getDaoFactory().beginTransaction();
		if ( idMax > 0){
			
			int maxVarLogistc = GuiGlobals.getDaoFactory().getIdentifyLogisticPalletDao().getMaxVarLogistc();
			int maxIdBase = GuiGlobals.getDaoFactory().getIdentifyLogisticPalletDao().getMaxIdBase( maxVarLogistc );
			
			if ( maxIdBase == GuiGlobals.getEnterprise().getMaxIdSequenceLogisticBox() ){
				identifyLogisticPallet.setIdBase(1);
				
				if ( maxVarLogistc == 9 ){
					identifyLogisticPallet.setVariavelLogistica( ( 0 ) );
				}
				else{
					identifyLogisticPallet.setVariavelLogistica( ( maxVarLogistc + 1 ) );
				}
				
			}
			else{
				identifyLogisticPallet.setIdBase( ( maxIdBase + 1 ) );
				identifyLogisticPallet.setVariavelLogistica( maxVarLogistc );
			}
		
		}
		else{
			/**
			 * Primeira registro da tabela
			 */
			identifyLogisticPallet.setIdBase(1);
			identifyLogisticPallet.setVariavelLogistica(0);
		}
		
		/**
		 * Inclui registro
		 */		
		try {
			GuiGlobals.getDaoFactory().getIdentifyLogisticPalletDao().add( identifyLogisticPallet );	
			GuiGlobals.getDaoFactory().commit();			
		}
		catch(Exception e) {
			e.printStackTrace();
			GuiGlobals.getDaoFactory().rollback();
			throw(e);
		}
		
		return identifyLogisticPallet;
	}
	
	public void removeStock(Stock stock) throws Exception {
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
			GuiGlobals.getDaoFactory().getStockDao().delete(stock);		
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();			
			throw(e);
		}		
	}
	
	public void enterStock(Stock stock, Operation operation) throws Exception {
		
		Enterprise enterprise = null;
		
		stock.setStatus(StockStatus.STOCKED);
		stock.setAvailableNet(stock.getNet());
		stock.setAvailableQty(stock.getAvailableQty());
		if ( stock.getPallet() != null && stock.getPallet().getId() == 0 ){
			stock.setPallet(null);
		}
		
		if ( stock.getIdentifyLogisticProduct() != null ){
			enterprise = GuiGlobals.getEnterprise();
			enterprise.setCurrentIdBaseBox( stock.getIdentifyLogisticProduct().getIdBase() );
			enterprise.setCurrentVarLogisctBox( stock.getIdentifyLogisticProduct().getVariavelLogistica() );
			GuiGlobals.setEnterprise(enterprise);
		}
		
		Historic h = new Historic();
		h.setBatch(stock.getBatch());
		h.setDate(DateTimeUtils.now());
		h.setNet(stock.getNet());
		h.setOperation(operation);
		h.setOrder(null);
		h.setPartner(stock.getPartner());
		h.setPrimaryQty(stock.getPrimaryQty());
		h.setProduct(stock.getProduct());
		h.setSecondaryQty(1);
		h.setStock(stock);
		h.setTare(stock.getTare());
		h.setTerminal(stock.getTerminal());
		h.setCompany(stock.getCompany());
		h.setUser(user);
		h.setPallet(stock.getPallet());
		
		GuiGlobals.getDaoFactory().beginTransaction();
		
		try {
			
			GuiGlobals.getDaoFactory().getStockDao().update(stock);
			
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
			throw(e);
		}
	}
	
	public boolean geraEmbala(Stock stock, String typeOperationTxt){
					
		boolean processOk	= false;
		String linha = "";
		if(stock.getPallet() == null){
			linha = "00000;";																												// 1 Numero do Pallet	
		}
		else{
			linha = String.format("%05d", stock.getPallet().getId() ) + ";";												// 1 Numero do Pallet
		}
		linha += String.format("%08d", stock.getId() ) + ";";																	// 2 Numero da caixa
		linha += StringUtils.leftPad( stock.getCompany().getId(), 3, "0" ) + ";";										// 3 Unidade de producao 
		
		if ( stock.getProduct().getDun14() == null || stock.getProduct().getDun14().isEmpty() ){
			linha += StringUtils.leftPad( "0", 14, "0" ) + ";";
		}
		else{
			linha += stock.getProduct().getDun14() + ";";																			// 4 Dun 14
		}
		linha += StringUtils.leftPad( stock.getTerminal().getIdTerminal(), 2, "0" ) + ";";							// 5 Numero do terminal
		linha += stock.getProduct().getIdMasc() + ";";										// 6 Codigo do Produto
		linha += "  " + ";";																													// 7 Classificacao
		linha += String.format("%04d", stock.getUser().getId() ) + ";";                         							// 8 Senha do operador
		linha += String.format("%03d", stock.getPrimaryQty() ) + ";";													// 9 quantidade pacotes na caixa 		
		linha += StringUtils.leftPad( String.format("%.3f", stock.getNetEtq() ), 8, "0" ).replace(".", ",") + ";";	// 10 peso líquido kg (impresso na etiqueta)
		linha += StringUtils.leftPad( String.format("%.3f", stock.getNet() ), 8, "0" ).replace(".", ",") + ";";	// 11 peso líquido kg (real, lido da balança)
		linha += " "+ ";";																													// 12 faixa classificação (0,1,2 ,3 ou espaï¿½o qdo não hï¿½ faixa 
		linha += StringUtils.leftPad( String.format("%.3f", getTareForEmbala(stock) ), 7, "0" ).replace(".", ",") + ";";// 13 tara embalagem (caixa)
		linha += StringUtils.leftPad( String.format("%.3f", stock.getTaraEmbalagem() ), 7, "0" ).replace(".", ",") + ";";// 14 tara da embalagem primï¿½ria (geralmente saco plï¿½stico) 
		linha += DateTimeUtils.getDate(stock.getExpirationDate(), "yyMMdd") + ";";								// 15 data de validade se resfriado ou maturado 
		linha += "000000" + ";";																											// 16 data de validade se congelado 
		linha += DateTimeUtils.getDate(stock.getEnterDate(), "yyMMdd") + ";";										// 17 data da operacao	
		linha += DateTimeUtils.getDate(stock.getEnterDate(), "HHmm") + ";";											// 18 hora e minuto da operaï¿½ï¿½o
		linha += stock.getBatch() + ";";																								// 19 número do lote	
		linha += DateTimeUtils.getDate(stock.getManufactureDate(), "yyMMdd") + ";";								// 20 data do lote	
		linha += "000/0000" + ";";																										// 21 endereï¿½o do pallet (onde estï¿½ a caixa) 
		linha += "000000000" + ";";																									// 22 contrato, informado no TS1 pelo operador	
		linha += "R" + ";";																													// 23 Tipo de pallet 	ï¿½Cï¿½ ï¿½ congelado      ï¿½Rï¿½ ï¿½ resfriado 
		linha += "4" + ";";																		// 24 Layout da etiqueta utilizada (modelo 1, 2 ,3, 4 ou 5 de etiqueta) 
		linha += "0" + ";";																		// 25 Idioma utilizado para imprimir a etiqueta ( 0 a 5 )	
		linha += "O" + ";";																		// 26 origem do produto: ï¿½Gï¿½ - granel        ï¿½Oï¿½ - outros   Espaï¿½o ï¿½ não informado	1 
		linha += "0000" + ";";																	// 27 cï¿½digo PRIMEIRO produto contido na caixa 
		linha += "00" + ";";																	// 28 número Peças do PRIMEIRO produto contido na caixa 
		linha += "0000,000" + ";";																// 29 peso líquido do PRIMEIRO produto contido na caixa 
		linha += "0000" + ";";																	// 30 cï¿½digo SEGUNDO produto contido na caixa 
		linha += "00" + ";";																	// 31 número Peças do SEGUNDO produto contido na caixa 
		linha += "0000,000" + ";";																// 32 peso líquido do SEGUNDO produto contido na caixa 
		linha += "0000" + ";";																	// 33 cï¿½digo TERCEIRO produto contido na caixa 
		linha += "00" + ";";																	// 34 número Peças do TERCEIRO produto contido na caixa 
		linha += "0000,000" + ";";																// 35 peso líquido do TERCEIRO produto contido na caixa 
		linha += "0000" + ";";																	// 36 cï¿½digo QUARTO produto contido na caixa 
		linha += "00" + ";";																	// 37 número Peças do QUARTO produto contido na caixa 
		linha += "0000,000" + ";";																// 38 peso líquido do QUARTO produto contido na caixa 
		linha += "0000" + ";";																	// 39 cï¿½digo QUINTO produto contido na caixa 
		linha += "00" + ";";																	// 40 número Peças do QUINTO produto contido na caixa 
		linha += "0000,000" + ";";																// 41 peso líquido do QUINTO produto contido na caixa 
		linha += "0000" + ";";																	// 42 cï¿½digo SEXTO produto contido na caixa 
		linha += "00" + ";";																	// 43 número Peças do SEXTO produto contido na caixa 
		linha += "0000,000" + ";";																// 44 peso líquido do SEXTO produto contido na caixa 
		linha += typeOperationTxt + ";";														// 45 tipo de  operacao: E =entrada de embalagem    S =estorno ( PENDENTE PEGAR O TIPO DE OPERACAO DA TABELA HISTORICO )
		linha += "N" ;																			// 46 Tipo de embalagem: N= normal , S= sequestro	

		processOk = writeOnPathSystem(linha, "EMBALA");
        
        return processOk;
	}
	
	public boolean geraManuten(Stock stock, String operacao, String motivo)
	{	
		boolean processOk	= false;
		String linha = "";
		
		if ( motivo != null && motivo.isEmpty() )
		{
			motivo = "99";
		}
		
		linha += stock.getIdFormatSerial() + ";";									// 1 Numero da caixa
		linha += StringUtils.leftPad( stock.getCompany().getId(), 3, "0" ) + ";";	// 2 Unidade da fabrica
		linha += stock.getProduct().getIdMasc() + ";";								// 3 Código do produto
		linha += StringUtils.leftPad( stock.getHowSelledFormat(), 8, "0" ).replace(".", ",") + ";";	// 4 peso líquido
		linha += operacao + ";";													// 5 código de operação no registro: •	A – caixa foi apagada manualmente / •	I  – caixa foi inserida manualmente
		linha += motivo + ";"; 														// 6 Motivo da manutção
		linha += DateTimeUtils.getDate(stock.getEnterDate(), "yyMMdd") + ";";		// 7 data da operacao
		linha += DateTimeUtils.getDate(stock.getEnterDate(), "HHmm") + ";";			// 8 hora e minuto da operação
		linha += "000/0000" + ";";													// 9 Endereço do palete
		if(stock.getPallet() == null){
			linha += "00000";	
		}
		else{
			linha += String.format("%05d", stock.getPallet().getId() );				// 10 Numero do Pallet
		}
			
		processOk = writeOnPathSystem(linha, "MANUTEN");
        
        return processOk;
	}

	private double getTareForEmbala(Stock stock) {
		
		double tareReturn = stock.getTareBox();
		if ( tareReturn > 0 && stock.getTare() != NumberUtils.roundNumber(( stock.getTareBox() + stock.getTaraEmbalagem() ), 3) ){
			tareReturn = stock.getTare();
		}
		
		return tareReturn;
	}

	/**
	 * 
	 * @param stock
	 * @param addOrRemove R (Recebimento) ou D (Devolução)
	 * @return
	 */
	public boolean geraKardex(Stock stock, String addOrRemove) 
	{
		return geraKardex(stock, addOrRemove, null, null, null, null, "M");
	}
	public boolean geraKardex(Stock stock, String addOrRemove, String codPartner, String note, Date dataValidade, String loteExterno, String tipo) 
	{
		boolean processOk	= false;
		String linha = "";
		
		if ( stock != null && stock.getProduct() != null )
		{
		
			linha += stock.getTerminal().getIdTerminal() + ";";	// 1 Terminal
			linha += "0" + stock.getCompany().getId() + ";"; // Filial
			linha += ( note != null ? note : "000000" ) + ";"; // Nota
			linha += stock.getProduct().getIdMasc() + ";";					// Cod. Produto
			linha += stock.getProduct().getDescription().trim() + ";";		// Descricao do produto
			linha += stock.getProduct().getUnit().getId() + ";";			// Unidade de medida
			linha += stock.getTypeStock().getSigla() + ";";					// Tipo de Estoque
			linha += StringUtils.leftPad( String.format("%.3f", stock.getNet() ), 8, "0" ).replace(".", ",") + ";";	// Quantidade ajustada (em Kg)
			
			linha += addOrRemove + ";"; // 6 Flag ( entrada saida )
			
			if ( codPartner != null && !codPartner.isEmpty() )
				linha += codPartner + ";";						// Cod. Fornecedor
			
			
			if ( dataValidade != null )
				linha += DateTimeUtils.getDate(dataValidade, "yyMMdd") + ";";// Data da Validade do lote
			
			if ( loteExterno != null && !loteExterno.isEmpty() )
				linha += loteExterno.trim() + ";";								// Lote externo
			
			linha += StringUtils.leftPad( GuiGlobals.getUserInfo().getUser().getPassword(), 4, "0" ) + ";"; // Senha do operador
			
			linha += StringUtils.leftPad( String.format("%.3f", getTareForEmbala(stock) ), 6, "0" ).replace(".", ",") + ";";// tara embalagem (caixa)
			
			linha += StringUtils.leftPad( String.valueOf( stock.getPrimaryQty() ), 3, "0" ).replace(".", ",") + ";";// peças
			
			linha += DateTimeUtils.getDate(stock.getEnterDate(), "yyMMdd") + ";"; // data da operacao	
			
			linha += DateTimeUtils.getDate(stock.getEnterDate(), "HHmm") + ";"; //  hora e minuto da operaï¿½ï¿½o
			
			linha += tipo; // M=Manual / B=Balança
				
	        processOk = writeOnPathSystem(linha, "KARDEX");
	        
		}
        
        return processOk;
	}

	private boolean writeOnPathSystem(String linha, String nomeArquivo) 
	{
		boolean processOk = true;
		
		if ( serverConfig != null && serverConfig.getStoreDirectory() != null ) 
		{
			FileWriter arq = getPathAndFile( nomeArquivo );	
			
			if ( arq != null )
			{
				PrintWriter gravarArq = new PrintWriter(arq);
				
				gravarArq.println(linha);
				gravarArq.close();
				try 
				{
					arq.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
        }
        else
        {
        	processOk = false;
        	if ( GuiGlobals.getConfigSGBD() == null )
			{
				LogDeProcessamento.gravaLog("error", "Arquivo " + nomeArquivo + ".txt não será gerado pois não foi configurado no sistema. Para configurar, o administrador deve indicar o diretório em 'Manutenção->Configurações' !", true);
			}
        	else
        	{
        		GuiGlobals.showMessageDlg("Arquivo " + nomeArquivo + ".txt não será gerado pois não foi configurado no sistema. Para configurar, o administrador deve indicar o diretório em 'Manutenção->Configurações' !");
        	}
        }
		return processOk;
	}
	
	private FileWriter getPathAndFile(String nomeArquivo)
	{
		String pathAndFile = "";
		FileWriter arq = null;
		boolean processOk = true;
		String msgError = "";
		
		try 
		{
			pathAndFile = serverConfig.getStoreDirectory().replace("\\", GuiGlobals.getSeparador() ).replace("//", GuiGlobals.getSeparador() ) + GuiGlobals.getSeparador() + nomeArquivo + ".txt";
			arq = new FileWriter(pathAndFile, true); // caminho do arquivo -> se não existir é criado o arquivo, caso exista é editado.
		}
		catch (IOException e) 
		{
			processOk = false;
			arq = null;
			e.printStackTrace();
			msgError += "Arquivo " + nomeArquivo + ".txt não pode ser gerado! " + e.getMessage();
		}
		
		if ( !processOk )
		{
			try 
			{
				pathAndFile = serverConfig.getStoreDirectoryFull().replace("\\", GuiGlobals.getSeparador() ).replace("//", GuiGlobals.getSeparador() ) + GuiGlobals.getSeparador() + nomeArquivo + ".txt";
				arq = new FileWriter(pathAndFile, true); // caminho do arquivo -> se não existir é criado o arquivo, caso exista é editado.
			}
			catch (IOException e) 
			{
				processOk = false;
				arq = null;
				e.printStackTrace();
				msgError += "Arquivo " + nomeArquivo + ".txt não pode ser gerado! " + e.getMessage();
			}
		}
		
		if ( !processOk && !msgError.isEmpty() )
		{
			if ( GuiGlobals.getConfigSGBD() == null )
			{
				LogDeProcessamento.gravaLog(null, msgError, true);
			}
			else
			{
				GuiGlobals.showMessageDlg(msgError);
			}
		}
		
		return arq;
	}
	
	public List<Stock> getStocksFromTypeStock( Product product, TypeStock typeStock )
	{
		List<Stock> stocks = null;
		if ( product != null && !product.getIdMasc().isEmpty() && typeStock != null )
		{
			StockFilter filter = new StockFilter();
			filter.setOnlyStocked(true);
			filter.setProductId(product.getId());
			filter.setProductId_2(product.getId());
			filter.setTypeStock(typeStock);
			
			stocks = GuiGlobals.getDaoFactory().getStockDao().list(filter);
		}
		return stocks;
	}
	
	public Stock getStockKgFromTypeStock( Product product, TypeStock typeStock )
	{
		Stock stock = null;
		if ( product != null && !product.getIdMasc().isEmpty() && typeStock != null && typeStock != TypeStock.EMBALAGEM )
		{			
			List<Stock> stocks = getStocksFromTypeStock(product, typeStock);			
			if ( stocks.size() == 1 ) stock = stocks.get(0);
		}
		return stock;
	}
	
	public Stock getStockKgFromTypeStock( String productIdMasc, TypeStock typeStock )
	{
		Stock stock = null;
		Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc(productIdMasc);
		
		if ( product != null )
		{
			stock = getStockKgFromTypeStock( product, typeStock );
		}
		
		return stock;
	}

	public boolean leaveStocKg(StockManuten stockManuten) 
	{
		boolean retorno = false;
		Product product = GuiGlobals.getDaoFactory().getProductDao().find( stockManuten.getIdProduct() );
		Stock stock = getStockKgFromTypeStock( product, stockManuten.getTypeStock() );
		stock.setNet( stock.getNet() + stockManuten.getQuantityChange() );
		stock.setPrimaryQty( stock.getPrimaryQty() + stockManuten.getQuantityChangePrimaryQty() );
		stock.setSecondaryQty( stock.getSecondaryQty() + stockManuten.getQuantityChangeBox() ); 
		leaveStockKg(stock, Operation.CHANGE_STOCK_KG, Setup.getTerminal(), true, false);
		retorno = geraKardex(stock, ( ( stockManuten.getQuantityChange() > 0 ) ? "R" : "D" ), null, null, null, null, "M" );
		
		return retorno;
	}

	public void removeAllTypeStockGranel() 
	{
		StockFilter filter = new StockFilter();
		filter.setTypeStock(TypeStock.GRANEL);
		
		List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().list(filter);
		
		HistoricFilter historicFilter = null;
		List<Historic> historics = null;
		for ( Stock stock : stocks )
		{
			
			historicFilter = new HistoricFilter();
			historicFilter.setStockId( stock.getId() );
			historics = GuiGlobals.getDaoFactory().getHistoricDao().list(historicFilter);
			
			GuiGlobals.getDaoFactory().beginTransaction();
			
			try 
			{
				
				if ( historics != null && historics.size() > 0 )
				{
					GuiGlobals.getDaoFactory().getHistoricDao().delete(historics);
				}
				
				GuiGlobals.getDaoFactory().getStockDao().delete(stock);
				GuiGlobals.getDaoFactory().commit();
			}
			catch(Exception e) {
				GuiGlobals.getDaoFactory().rollback();
				e.printStackTrace();			
				throw(e);
			}
			
			historicFilter = null;
			historics = null;
		}
		
	}

}
