package br.com.cardeal.globals;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import org.apache.commons.lang.StringUtils;
import br.com.cardeal.dao.StockDao;
import br.com.cardeal.enums.Operation;
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.printer.PrintingService;
import br.com.cardeal.services.PackingService;
import br.com.cardeal.services.PalletService;
import br.com.cardeal.services.StockService;
import br.com.cardeal.views.PackingPanelWindows;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.Etiqueta;
import br.com.cardeal.model.IdentifyLogisticPallet;
import br.com.cardeal.model.IdentifyLogisticProduct;
import br.com.cardeal.model.ItensPalletVirtual;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.ServerSetup;
import br.com.cardeal.model.Stock;
import br.com.cardeal.model.Unit;

public class PackingProcessWorker extends SwingWorker<Integer, String> {

	private static final int BOX = 1;
	private static final int PALLET = 2;
	private static final int PALLET_VIRUTAL = 3;
	private PackingData packingData;	
	private Stock stock;
	private Pallet stockTotalVirtual = null;
	private Pallet pallet;
	private volatile boolean terminated = false;
	private String printErrorMsg;
	private ServerSetup serverSetup;
	private StockService service = new StockService();
	private boolean isPackingPanel = false;
	
	@SuppressWarnings("unused")
	private static void failIfInterrupted() throws InterruptedException {
	    if (Thread.currentThread().isInterrupted()) {
	    	throw new InterruptedException("Processamento interrompido");
	    }
	}	
	
	public void terminate() {
		terminated = true;
	}
	
	public PackingProcessWorker(final PackingData packingData) {
		this.packingData = packingData;
		initServer();
	}
	
	public PackingProcessWorker(final PackingData packingData, boolean isPackingPanel) {
		this.isPackingPanel = isPackingPanel;
		this.packingData = packingData;
		initServer();
	}
	
	public PackingProcessWorker(final PackingData packingData, final Stock stock) {
		this.packingData = packingData;
		this.stock = stock;
		initServer();
	}
	
	public PackingProcessWorker(final PackingData packingData, final Stock stock, boolean isPackingPanel) {
		this.packingData = packingData;
		this.stock = stock;
		this.isPackingPanel = isPackingPanel; 
		initServer();
	}
	
	public PackingProcessWorker(final PackingData packingData, final Pallet pallet, boolean isPackingPanel) {
		this.packingData = packingData;
		this.pallet = pallet;
		this.isPackingPanel = isPackingPanel;
		initServer();
	}
	
	public PackingProcessWorker(final PackingData packingData, final Pallet pallet) {
		this.packingData = packingData;
		this.pallet = pallet;
		initServer();
	}
	
	private void initServer(){
		serverSetup = GuiGlobals.getDaoFactory().getServerSetupDao().find();
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		switch(packingData.getAction()) {
		
			case PackingData.ACTION_PACK:
				return packAction();
			
			case PackingData.ACTION_CLOSE_PALLET:
				return closePalletAction();
				
			case PackingData.ACTION_PRINT_BOX:
				return printBox();
				
			case PackingData.ACTION_PRINT_PALLET:
				return printPallet();
				
			case PackingData.ACTION_REPALLETIZAR:
				return repalletizarAction();
				
			case PackingData.ACTION_PALLLET_VIRTUAL:
				return palletVirtualAction();
		}
		
		return 0;
	}
	
	private int printBox() {
		if(print(stock, false))
			return 1;
		else {
			publish(printErrorMsg);
			return 0;
		}
	}
	
	private int printPallet() {
		if(print(pallet))
			return 1;
		else {
			publish(printErrorMsg);
			return 0;
		}
	}
	
	private int closePalletAction() {
		
		if(!Setup.hasPrinter()) {
			publish("Não existe impressora configurada");
			return 0;
		}
		
		closePallet();
		return 1;		
	}
	
	private int repalletizarAction() 
	{
		if(!Setup.hasPrinter()) 
		{
			publish("Não existe impressora configurada");
			return 0;
		}
		
		repalletizar();
		return 1;		
	}
	
	private int palletVirtualAction()
	{
		if(!Setup.hasPrinter()) 
		{
			publish("Não existe impressora configurada");
			return 0;
		}
		
		if ( !isLabelOk( PALLET_VIRUTAL ) )
		{
			return 0;
		}
		
		int contadorProdutos = 0;
		int qtyBoxes = 0;
		double pesoLiquido = 0.0;
		double pesoBruto = ( packingData.getTare() + packingData.getTaraEmbalagem() );
		String codProdutos1 = "";
		String codProdutos2 = "";
		String codProdutos3 = "";
		List<ItensPalletVirtual> itens = packingData.getItensPalletVirtual();
		
		for ( ItensPalletVirtual item : itens )
		{
			if ( !item.getItensPallet().isEmpty() && Double.parseDouble( item.getTotalLiq() ) > 0 && Double.parseDouble( item.getTotalBruto() ) > 0)
			{
			
				contadorProdutos++;
				
				if ( contadorProdutos <= 3 )
				{
					codProdutos1 += item.getItensPallet().trim() + " ";
				}
				else if ( contadorProdutos <= 12 )
				{
					codProdutos2 += item.getItensPallet().trim() + " ";
				}
				else
				{
					codProdutos3 += item.getItensPallet().trim() + " ";
				}
				
				qtyBoxes += item.getQtde();
				pesoLiquido += Double.parseDouble( item.getTotalLiq() );
				pesoBruto += Double.parseDouble( item.getTotalBruto() );
				
			}
		}
		
		LabelMap map = new LabelMap();
		map.put("descfornecedor", packingData.getPartner().getName().trim() );
		map.put("fornecedor", packingData.getPartner().getCodigoExterno() );
		map.put("produtos1", codProdutos1);
		map.put("produtos2", codProdutos2);
		map.put("produtos3", codProdutos3);
		map.put("tarapallet", NumberUtils.transform(NumberUtils.truncate(packingData.getTare(), 3), 14, 3, false, false) );
		map.put("tarastrech", NumberUtils.transform(NumberUtils.truncate(packingData.getTaraEmbalagem(), 3), 14, 3, false, false) );
		map.put("volcaixas", StringUtils.leftPad( String.valueOf( qtyBoxes ), 3, "0" ) );
		map.put("pesoliq", NumberUtils.transform(NumberUtils.truncate(pesoLiquido, 3), 14, 3, false, false) );
		map.put("pesobruto", NumberUtils.transform(NumberUtils.truncate(pesoBruto, 3), 14, 3, false, false) );
		
		sendToPrint(PALLET_VIRUTAL, map, false);
		
		return 1;
	}
	
	private int packAction() throws Exception 
	{
		
		//tem que haver uma impressora configurada 
		if(!Setup.hasPrinter()) {
			publish("Não existe impressora configurada");
			return 0;
		}
		
		/**
		 * Verifica se o diretorio de gravação de arquivos esta ativo
		 */
		if ( !Setup.verifyPaths() ) return 0;
		
		Product product = packingData.getProduct();
		PackingPanelWindows objPackingPanel = packingData.getObjPackingPanel();		
		
		if ( objPackingPanel != null ) {
					
			double net = 0;
			double netStorage = 0;
			double tare = 0;
			double netMinCalc = 0;
			double netMaxCalc = 0;
			double taraTotalCx = Double.parseDouble( objPackingPanel.getTxtTaraTotal().getText() );
			int contadorValid = 0;
			
			if ( product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT  || packingData.isForceVariableWeight() )
			{
				
				GuiGlobals.showMessage("Estabilizando peso...", false);
				
				boolean processOk = false;
				while ( !processOk ){
					
					GuiGlobals.sleep(60);
					
					processOk = GuiGlobals.isSettledScale();				
					
					if ( GuiGlobals.getCurrentNet() == 0 && contadorValid == 3){
						GuiGlobals.showMessage("Coloque o produto na balança e confirme...");
						GuiGlobals.sleep(100);
						return 0;
					}
					contadorValid++;
					
				}
				
				if ( !processOk ) {
					GuiGlobals.showMessage("", false);
					return 0;					
				}
				else{
					GuiGlobals.showMessage("...", false);
					GuiGlobals.sleep(Setup.getTerminal().getTimeWait1InMilis());
				}
					
				net = GuiGlobals.getCurrentNet();
				tare = ( taraTotalCx > 0 ) ? taraTotalCx : GuiGlobals.getCurrentTare();
				
				//se o produto for de peso variável e as taras de embalagem e Caixa estão programadas,
				//a balança não pode apresentar tara para evitar erros
				if(product.getTareEmbala() > 0 || product.getTareBox() > 0) {
					if(GuiGlobals.getCurrentTare() > 0) {
						GuiGlobals.showMessageDlg("Produto de peso variável com tara pré-configurada deve ser pesado na balança sem tara.");
						return 0;
					}
					else {
						tare = taraTotalCx;
					}
				}					
				
				if(!objPackingPanel.getTxtQtdPecas().getText().equals("0")){
					
					int qtdPecasCadastro;
					/*
					 * Verifico os valores do Cadastro de Produto e Quantidade de Peças
					 * Pois se forem 0, não será possível realizar a divisão para obter peso minimo 
					 */
					if(product.getTargetQty() == 0 ){
						qtdPecasCadastro = 1;
					}						
					else{												
						qtdPecasCadastro = product.getTargetQty();
					}										
					
					//se existe limites de peso, validar agora
					if( product.getMinWeight() > 0 ) {
						
						//calculo proporcional com o que foi digitado na interface
						//netMinCalc = product.getMinWeight() + ( product.getTargetWeight()/Integer.parseInt(txtQtdPecas.getText() ) * product.getMinWeight() );
						netMinCalc = (product.getMinWeight() * packingData.getPrimaryQty())/qtdPecasCadastro;
											
						if (net < netMinCalc){
							GuiGlobals.showMessage("Peso menor que o mínimo permitido para este produto");
							return 0;	
						}
									
					}
					
					if(product.getMaxWeight() > 0 ) {
						
						//calculo proporcional com o que foi digitado na interface
						//netMaxCalc = product.getMaxWeight() + ( product.getTargetWeight()/Integer.parseInt(txtQtdPecas.getText() ) * product.getMaxWeight() );
						netMaxCalc = (product.getMaxWeight() * packingData.getPrimaryQty())/qtdPecasCadastro;
						
						if (net > netMaxCalc){
							GuiGlobals.showMessage("Peso maior que o máximo permitido para este produto");
							return 0;	
						}
									
					}
					
				}
				
				/**
				 * Se for peso variavel, retirar a tara do peso liquido, pois o produto é pesado com todas as taras, ou seja, como peso bruto
				 */
				net -= tare;
				netStorage = net;
				
			}
			else 
			{
				
				double netDlg = Double.parseDouble( objPackingPanel.getTxtPesoLiquido().getText() );
				netStorage = ( netDlg > 0 ) ? netDlg : ( ( product.getTargetWeight() / product.getTargetQty() ) * packingData.getPrimaryQty() );

				if ( product.getTargetQty() > 0 ){
					net = ( ( product.getTargetQty() == packingData.getPrimaryQty() ) ? product.getNetWeight() : ( product.getNetWeight() / product.getTargetQty() ) * packingData.getPrimaryQty() );
				}
				else{
					net = netStorage;
				}
				
				tare = taraTotalCx;
			}
			if(net <= 0) {
				GuiGlobals.showMessage("Peso inválido para a operação");
				return 0;
			}
			
			packingData.setNet(net);
			packingData.setNetStorage(netStorage);
			packingData.setTare(tare);
			
		}
		
		if ( !isLabelOk( BOX ) )
		{
			return 0;
		}
		
		int processed = 0;
		int contadorEtiquetas = 0;
		long id = 0;
		boolean isVirtualNotPalletAndNotExceptionStandardWeight = ( packingData.getProduct().isVirtual() && packingData.getPallet() == null && !packingData.isExceptionStandardWeight() );
		for(int i = 0; i < packingData.getQtdEtiquetas(); i++) 
		{
			//verifica se quer interromper a execução
			if(terminated) {
				publish("Processamento interrompido");
				processed = 3;
				break;
			}
			
			if ( !packingData.getProduct().isVirtual() )
				GuiGlobals.getPrintResultZebra(); // Verifica status da impressora
			
			id = 0;
			Stock stock = null;			
			if ( packingData.getTypeStock() != TypeStock.EMBALAGEM )
			{
				stock = GuiGlobals.getDaoFactory().getStockDao().findProductByTypeStock(packingData.getProduct(), packingData.getTypeStock() );
				if ( stock != null && stock.getId() > 0 )
				{
					id = stock.getId();
					
					double netCalc = packingData.getNetStorage();
					int primaryQtyCalc = packingData.getPrimaryQty();
					int secondaryQty = packingData.getSecondaryQty();
					
					if ( packingData.getAddOrRemove() != null && packingData.getAddOrRemove().equals("D") )
					{
						netCalc = ( netCalc * -1 );
						primaryQtyCalc = ( primaryQtyCalc * -1 );
						secondaryQty = ( secondaryQty * -1 );
					}
					
					stock.setPrimaryQty( ( stock.getPrimaryQty() + primaryQtyCalc ) );			
					stock.setSecondaryQty( ( stock.getSecondaryQty() + secondaryQty ) );
					stock.setNet( NumberUtils.roundNumber( ( stock.getNet() + netCalc ), 3 ));
				}
			}
			
			/**
			 * Verifica se o diretorio de gravação de arquivos esta ativo
			 */
			if ( !Setup.verifyPaths() ) return 0;
			
			if ( id == 0 )
			{
				/**
				 * pega o ID da proxima caixa em estoque						
				 */
				try 
				{
					id = service.generateStockId();
				}
				catch(Exception e) {
					LogDeProcessamento.gravaLog(null, e.getMessage());
					publish(e.getMessage());
					break;				
				}
				
				stock = new Stock();
				stock.setPrimaryQty(packingData.getPrimaryQty());			
				stock.setSecondaryQty(packingData.getSecondaryQty());
				stock.setNet(NumberUtils.roundNumber( packingData.getNetStorage(), 3 ));
				
			}
			
			//monta o estoque desta movimentação			
			stock.setId(id);
			stock.setBatch(packingData.getBatch());
			stock.setEnterDate(DateTimeUtils.now());
			stock.setManufactureDate(packingData.getDate());
			stock.setExpirationDate(packingData.getExpirationDate());
			/**
			 * Regra definida por Antonio em 09/11/2015
			 * Se for exceção peso padrão e o "como vende" (howSell) não for kilo, no embala.txt 
			 * o campo peso liquido na etiqueta deve ser a quantidade de peças, que é a unidade
			 * cobrada do cliente.
			 */
			if ( packingData.isExceptionStandardWeight() && !( "KG".equalsIgnoreCase( packingData.getEtqHowSell().trim() ) ) )
			{
				stock.setNetEtq( NumberUtils.roundNumber( packingData.getPrimaryQty(), 3 ) );
			}
			else
			{
				stock.setNetEtq( NumberUtils.roundNumber( packingData.getNet(), 3 ) );  // Peso Liquido Etiqueta
			}
			
			 
			stock.setTare( NumberUtils.roundNumber( packingData.getTare(), 3 ));
			stock.setTaraEmbalagem( NumberUtils.roundNumber( packingData.getTaraEmbalagem(), 3 ) );
			stock.setTareBox( NumberUtils.roundNumber( packingData.getTaraCaixa(), 3 ) );			
			stock.setProduct(packingData.getProduct());
			stock.setPartner(packingData.getPartner());
			stock.setCompany(Setup.getCompany());
			stock.setPallet(packingData.getPallet());
			stock.setUser(packingData.getUser());
			stock.setTerminal(packingData.getTerminal());			
			stock.setInFifo(packingData.isInFifo());
			stock.setTypeStock(packingData.getTypeStock());
			stock.setHowSell(packingData.getEtqHowSell());
			stock.setPrimaryQtyOperation( packingData.getPrimaryQtyOperation() );
			
			Etiqueta etiqueta = packingData.getProduct().getLabelFileName();
			if ( etiqueta != null && etiqueta.isEtqEspPaoAcucar()  ){				
				IdentifyLogisticProduct identifyLogisticProduct = service.generateIdentifyLogisticProduct();				
				stock.setIdentifyLogisticProduct(identifyLogisticProduct);
				stock.setSscc( getSSCCetq( stock ) );
			}
			
			try {
				
				if ( !service.geraEmbala(stock, "E") )
				{
					stock = null;
					processed = 2;
					break;
				}
				else
				{
					service.enterStock(stock, Operation.PACKING);
					
					if ( packingData.getTypeStock() != TypeStock.EMBALAGEM )
					{
						service.geraKardex(stock, packingData.getAddOrRemove(), getCodFornecedorToKardex(), null, packingData.getValidateDateLoteExternal(), packingData.getLoteExternal(), "B");
					}
				}
								
				if ( isVirtualNotPalletAndNotExceptionStandardWeight )
				{
					GuiGlobals.showMessage("Processando...", false);
				}
				else
				{
					if(packingData.getQtdEtiquetas() > 1)
						GuiGlobals.showMessage("Imprimindo " + String.valueOf(i + 1) + "...", false);
					else 
						GuiGlobals.showMessage("Imprimindo...", false);
				}
				if ( !print(stock, false ) )
				{
					service.leaveStock(stock, Operation.STOCK_ROLLBACK, Setup.getTerminal());
					LogDeProcessamento.gravaLog("console", "Não foi possível imprimir a etiqueta " + String.valueOf( stock.getId() ) + " - " + printErrorMsg );
					GuiGlobals.showMessage( "Não foi possível imprimir a etiqueta " + String.valueOf( stock.getId() ) );					
					publish(printErrorMsg);
					stock = null;
					break;
				}
								
				contadorEtiquetas++;
				refreshQtyOnDlg(objPackingPanel);
				processed = 1;
				
				if(packingData.getPallet() != null) 
				{
					// adiciona etiqueta ao Pallet
					packingData.getPallet().setTotalEtq((packingData.getPallet().getTotalEtq() + 1));
					
					//verifica se esta etiqueta completou o pallet...
					if(packingData.getPallet().getTotalEtq() >= packingData.getProduct().getPalletQty()) {					
						closePallet();
						stock = null;
						break;					
					} 
				}
				else if ( isVirtualNotPalletAndNotExceptionStandardWeight )
				{
					setTotalizeStockForVirtual( stock );
				}
				
			}
			catch(Exception e) {
				service.leaveStock(stock, Operation.STOCK_ROLLBACK, Setup.getTerminal());
				publish(e.getMessage());
				LogDeProcessamento.gravaLog(null, e.getMessage());
				e.printStackTrace();
				stock = null;
				break;
			}
			
			stock = null;
			
		}
		
		if ( processed == 1 )
		{
		
			if ( isVirtualNotPalletAndNotExceptionStandardWeight && stockTotalVirtual != null )
			{
				
				/**
				 * Recalcula a informacao de como vende (HowSell)
				 */
				PackingService packingService = new PackingService();
				stockTotalVirtual.setHowSell( packingService.rulesEtqHowSell(stockTotalVirtual.getProduct(), stockTotalVirtual.getSecondaryQty(), stockTotalVirtual.getPrimaryQty(), true) );
				
				GuiGlobals.showMessage("Imprimindo etiqueta informativa...", false);
				if(!print(stockTotalVirtual)){				
					LogDeProcessamento.gravaLog(null, "Não foi possível imprimir a etiqueta mãe do processamento de embalagem virtual!");
				}
			}
			System.gc();
			
			if ( product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT  || packingData.isForceVariableWeight() ){
				
				if ( objPackingPanel != null ){								
					objPackingPanel.refreshQtds();
				}
				waitZero( product );			
			}
			
			if(packingData.getQtdEtiquetas() == contadorEtiquetas ) {
				publish("Operação OK");			
			}
			
		}
	    return processed;
	    
	  }

	/**
	 * 
	 * @return isPrint retornar se pode ou não imprimir etiqueta
	 */
	private boolean verifyIfPrintOperation() 
	{
		boolean isPrint = true;

		//--------------------------------------------------------------
		//- Operação de estoque em trânsito não deve imprimir etiqueta -
		//--------------------------------------------------------------
		if ( packingData.getTypeStock() == TypeStock.TRANSITO )
		{
			isPrint = false;
		}
		
		return isPrint;
	}

	private String getCodFornecedorToKardex() 
	{	
		return ( ( packingData.getPartner() != null ) ? String.valueOf( packingData.getPartner().getId() ) : null );
	}

	private boolean isLabelOk(int label) 
	{
		String labelSet = getLabel( label );
		
		if ( labelSet == null || labelSet.trim().length() == 0 ) {
			publish("Não existe etiqueta associada ao produto");
			return false;
		}
		return true;
	}

	private void refreshQtyOnDlg(PackingPanelWindows objPackingPanel) {
		
		if ( objPackingPanel != null ){				
			objPackingPanel.addProgress(1);
			objPackingPanel.refreshQtds();
		}
		
	}

	private void setTotalizeStockForVirtual( Stock stockForTotalize ) 
	{
		
		if ( stockTotalVirtual == null )
		{
			stockTotalVirtual = new Pallet();
			stockTotalVirtual.setBatch( stockForTotalize.getBatch() );
			stockTotalVirtual.setCloseDate(null);
			stockTotalVirtual.setCompany( stockForTotalize.getCompany() );
			stockTotalVirtual.setExpirationDate( stockForTotalize.getExpirationDate() );
			stockTotalVirtual.setId(0);
			stockTotalVirtual.setIdentifyLogisticPallet( new IdentifyLogisticPallet() );
			stockTotalVirtual.setManufactureDate( stockForTotalize.getManufactureDate() );
			stockTotalVirtual.setNet( stockForTotalize.getNet() );
			stockTotalVirtual.setOpenDate( DateTimeUtils.now() );
			stockTotalVirtual.setPrimaryQty( stockForTotalize.getPrimaryQty() );
			stockTotalVirtual.setProduct( stockForTotalize.getProduct() );			
			stockTotalVirtual.setSecondaryQty( stockForTotalize.getSecondaryQty() );
			stockTotalVirtual.setStatus( PalletStatus.CLOSED );
			
			List<Stock> stocks = new ArrayList<Stock>();
			stocks.add( stockForTotalize );
			stockTotalVirtual.setStocks(stocks);			
			stockTotalVirtual.setStrech(0);			
			stockTotalVirtual.setTare( stockForTotalize.getTare() );
			stockTotalVirtual.setTareCantoneira( 0 );
			stockTotalVirtual.setTareOfPacks( 0 );
			stockTotalVirtual.setTarePack( 0 );
			stockTotalVirtual.setTareRack( 0 );
			stockTotalVirtual.setTerminal( stockForTotalize.getTerminal() );
			stockTotalVirtual.setTotalEtq( 1 );
			
		}
		else
		{
			stockTotalVirtual.setNet( stockTotalVirtual.getNet() + stockForTotalize.getNet() ); 
			stockTotalVirtual.setTare( stockTotalVirtual.getTare() + stockForTotalize.getTare() );
			stockTotalVirtual.setPrimaryQty( stockTotalVirtual.getPrimaryQty() + stockForTotalize.getPrimaryQty() );			
			stockTotalVirtual.setSecondaryQty( stockTotalVirtual.getSecondaryQty() + stockForTotalize.getSecondaryQty() );
			stockTotalVirtual.setTotalEtq( stockTotalVirtual.getTotalEtq() + 1 );
			
		}
		
	}

	@Override
	  protected void process(final List<String> chunks) {
	    // Updates the messages text area
	    for (final String string : chunks) {
	    	GuiGlobals.showMessage(string);
	    }
	  }	

	  private void closePallet() 
	  {		  
		  StockDao dao = GuiGlobals.getDaoFactory().getStockDao();
		  Pallet pallet = dao.findPalletStocked(packingData.getPallet().getId());
		  
		  Etiqueta etiqueta = pallet.getProduct().getLabelPalletFileName();
		  if ( etiqueta != null && etiqueta.isEtqEspPaoAcucar()  ){
				
				try {
										
					IdentifyLogisticPallet identifyLogisticPallet = service.generateIdentifyLogisticPallet();					
					pallet.setIdentifyLogisticPallet(identifyLogisticPallet);
					
				} 
				catch (Exception e) 
				{
					LogDeProcessamento.gravaLog(null, e.getMessage());
					e.printStackTrace();
				}
			}
		  
		  packingData.setPallet(pallet);
		  dao.closePallet(pallet);
		  
		  print(pallet);
		  
	  }
	  
	  private void repalletizar() 
	  {		
		  Pallet pallet = packingData.getPallet();
		  pallet.setStatus(PalletStatus.REPALLET);
		  
		  PalletService palletService = new PalletService();
		  
		  try 
		  {
			  palletService.updatePallet(pallet, Setup.getTerminal());			  
		  } 
		  catch (Exception e) 
		  {
			  publish("Falha na gravação do Pallet");
			  e.printStackTrace();
		  }
		  
		  try 
		  {
			  if(print(pallet)){
				  publish("Impressão OK");
			  }
		  } 
		  catch (Exception e) 
		  {
			  publish("Falha na impressão da etiqueta!");
			  e.printStackTrace();
		  }

	  }
	  
	  private String truncate(String line, int size) {
		  if(line == null)
			  line = " ";
		  if(line.length() > size) {
			  line = line.substring(0, size);
		  }
		  return line;
	  }
	  
	  private boolean print(Stock stock, boolean isPrintProductVirtualTotal) {
		  
		  boolean result = false;
		  
		  /**
		   * Se produto é virtual e não for exceção peso padrao, entao não imprime etiqueta
		   * ou outras verificações especificas
		   */
		  if (	stock.getProduct().isVirtual() && isPackingPanel && !isPrintProductVirtualTotal 
				|| !verifyIfPrintOperation() )
		  {
			  result = true;
		  }
		  else 
		  {
		  
			  LabelMap map = new LabelMap();		  
			  String pesoImp = null;
			  double pesoCalc = 0;
			  String descSif = stock.getProduct().getDescriptionSif();
			  int heightTextSifByLine = 55;
			  int heightTextConservByLine = 57;
			  int qtdAcrescCaracteresDesc = 0;
			  String codbar1;
			  String codbar2;
			  String codbar3;
			  String codbarLote = "";
			  String codbar2Aux1 = "";
			  String gtin;
			  DigitoVerificador dv;
			  boolean trataGramas = true;
			  boolean isVariableWieght = stock.getProduct().getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT;
			  String serialNumberPallet;
			  
			  if(stock.getPallet() != null)
				  serialNumberPallet = Utils.formatPallet(stock.getPallet().getId());
			  else
				  serialNumberPallet = "---";
			  
			  /**
			   * Tratamento dos códigos de barras das etiquetas especificas pao de açucar
			   */
			  Etiqueta etiqueta = stock.getProduct().getLabelFileName();
			  if ( etiqueta != null && etiqueta.isEtqEspPaoAcucar() ){
			  
				  qtdAcrescCaracteresDesc = 9;
				  
				  codbarLote = "(10){lote}";
				  if ( isVariableWieght || ( !isPackingPanel && stock.getPrimaryQty() == 0 )  ){
					  
					  codbar1 = "(01){gtin}(3103){liquidocodbar}(3303){pesbrcodbar}(30){qtde}";
					  codbar2 = "(15){datevalcodbar}(11){datefabcodbar}(703{seqprocess}){processador}";
					  codbar3 = "(00){sscc}";
					  
					  gtin = "9".concat( packingData.getProduct().getEan13().substring(0, 12) );
					  dv = new DigitoVerificador(gtin, DigitoVerificador.DUN14, true);
					  gtin = dv.getCodigo();
					  
					  codbar1 = codbar1.replace("{liquidocodbar}"	, StringUtils.leftPad(Utils.formatWeight(stock.getNet()).replace(".", ""),6,"0"));
					  codbar1 = codbar1.replace("{pesbrcodbar}"	, StringUtils.leftPad(Utils.formatWeight(stock.getNet() + stock.getTare()).replace(".", ""),6,"0"));
					  codbar1 = codbar1.replace("{qtde}"				, getQtdForCodBar( stock.getPrimaryQty() ));
					  
					  codbar2 = codbar2.replace("{datevalcodbar}"	, DateTimeUtils.getDate(stock.getExpirationDate(), "yyMMdd"));
					  codbar2 = codbar2.replace("{datefabcodbar}"	, DateTimeUtils.getDate(stock.getManufactureDate(), "yyMMdd"));
					  				  
				  }
				  else{
					  codbar1 = "(01){gtin}(15){datevalcodbar}(11){datefabcodbar}";
					  codbar2 = "(703{seqprocess}){processador}";
					  codbar3 = "(00){sscc}";
					  
					  gtin = packingData.getProduct().getDun14().trim();
					  
					  codbar1 = codbar1.replace("{datevalcodbar}"	, DateTimeUtils.getDate(stock.getExpirationDate(), "yyMMdd"));
					  codbar1 = codbar1.replace("{datefabcodbar}"	, DateTimeUtils.getDate(stock.getManufactureDate(), "yyMMdd"));
				  }
				  			  
				  String processador = getProcessorEtq( stock.getProduct() );
				  String sscc = stock.getSscc();
				  
				  codbar1 = codbar1.replace("{gtin}"			, gtin);
				  codbar2 = codbar2.replace("{processador}"		, processador );
				  codbar2 = codbar2.replace("{seqprocess}"		, stock.getProduct().getSequenciaProcessProduct());
				  codbar2Aux1 = codbar2;
				  codbarLote = codbarLote.replace("{lote}"		, stock.getBatch());
				  codbar2 = codbar2 + codbarLote;
				  codbar3 = codbar3.replace("{sscc}"			, sscc );
				  
				  map.put("gtin", gtin);
				  map.put("processador", processador);
				  map.put("sscc", sscc);
				  
				  map.put("serialnumber", stock.getIdFormatSerial().concat("-").concat(Utils.formatFilial(Integer.parseInt(stock.getCompany().getId()))));
				  map.put("pallet", "/ " + serialNumberPallet );
				  map.put("codbar1", codbar1);
				  map.put("codbar1Aux", ">;>8" + codbar1.replace("(", "").replace(")", "") );
				  map.put("codbar2", codbar2);
				  map.put("codbar2Aux1", ">;>8" + codbar2Aux1 + ">8" + codbarLote);
//				  map.put("codbar2Aux2", ">8" + codbarLote);
				  map.put("codbar3", codbar3);
				  map.put("codbar3Aux", ">;>8" + codbar3.replace("(", "").replace(")", "") );
				  map.put("empdesc", GuiGlobals.getEnterprise().getName().trim() );
				  
				  String descriptionConservation = stock.getProduct().getDescriptionConservation();
				  
				  if ( descriptionConservation != null ){
					  String[] infoSif = stock.getProduct().getDescriptionConservation().split("#");					  
					  if ( infoSif.length > 0 ) map.put("sifTitulo", infoSif[0]);
					  if ( infoSif.length > 1 ) map.put("sifTemperatura", infoSif[1]);
				  }
				 				  
				  /**
				   * ******************************************************************************
				   */
			  }
			  
			  map.put("cod"			, packingData.getProduct().getIdMasc());
			  map.put("nomeprod1"	,  truncate(stock.getProduct().getDescription(), packingData.getProduct().getQtyCaracteresDesc() + qtdAcrescCaracteresDesc));
			  
			  if ( stock.getProduct().getDescription().trim().length() > stock.getProduct().getQtyCaracteresDesc() ) {
				  map.put("nomeprod2", truncate(stock.getProduct().getDescription().substring(packingData.getProduct().getQtyCaracteresDesc(), packingData.getProduct().getDescription().trim().length()), packingData.getProduct().getQtyCaracteresDesc()));  
			  }
			  else{
				  map.put("nomeprod2", "");
			  }		  
			  
			  map.put("descConserv"	, truncate(stock.getProduct().getDescriptionConservation(), heightTextConservByLine));
			  
			  if ( descSif != null && !descSif.isEmpty() ){
				  
				  map.put("descSif"		, truncate(descSif, heightTextSifByLine));
				 
				  if ( descSif.trim().length() > heightTextSifByLine ) {			 
					  map.put("descSif2" , truncate(stock.getProduct().getDescriptionSif().substring(heightTextSifByLine,packingData.getProduct().getDescriptionSif().length()),heightTextSifByLine));
				  }
				  else{
					  map.put("descSif2", "");
				  }		
				  
			  }
			  else{
				  map.put("descSif", "");
			  }
			  			  
			  map.put("filial"			, Utils.formatFilial(Integer.parseInt(stock.getCompany().getId())));
			  map.put("dun14"			, ">;>8" + stock.getProduct().getDun14Etq());		  
			  map.put("qtde"		  	, Utils.formatQty(stock.getPrimaryQty()));  
			  map.put("datefab"			, DateTimeUtils.formatDate(stock.getManufactureDate()));
			  map.put("dateval"			, DateTimeUtils.formatDate(stock.getExpirationDate()));
			  map.put("lote"			, stock.getBatch());
			  map.put("serie"			, ">;" + stock.getIdFormatSerial()+Utils.formatFilial(Integer.parseInt(stock.getCompany().getId())));
			  map.put("pesbr"			, Utils.formatWeight(stock.getNet() + stock.getTare()).replace(".", ","));			  
			  map.put("taracaixa"		, Utils.formatWeight(stock.getTareBox()).replace(".", ","));
			  map.put("taraemb"			, Utils.formatWeight(stock.getTaraEmbalagem()).replace(".", ","));
			  map.put("tara"			, Utils.formatWeight(stock.getTare()).replace(".", ","));			  
			  map.put("terminal"		, stock.getTerminal().getIdTerminal());
			  map.put("liquido"			, Utils.formatWeight(stock.getNet()).replace(".", ","));
			  map.put("unidade"			, stock.getHowSell() );			  
			  map.put("pesbrcodbar"		, StringUtils.leftPad(Utils.formatWeight(stock.getNet() + stock.getTare()).replace(".", ""),6,"0"));		  
			  map.put("liquidocodbar"	, StringUtils.leftPad(Utils.formatWeight(stock.getNet()).replace(".", ""),6,"0"));
			  map.put("datevalcodbar"	, DateTimeUtils.getDate(stock.getExpirationDate(), "yyMMdd")); 
			  map.put("datefabcodbar"	, DateTimeUtils.getDate(stock.getManufactureDate(), "yyMMdd")); 
			 			  
			  if(isPackingPanel){ 
				  
				  int qtdPecas = stock.getPrimaryQtyOperation();
				  
				  if( qtdPecas == 0 || ( isVariableWieght && qtdPecas != stock.getProduct().getTargetQty() ) )
				  {
					  
					  if ( qtdPecas > 0 ) {
						  map.put("qtdedesc", "001 EMB C/" + String.format("%03d", qtdPecas) + " UNI." ) ;
					  }
					  else{
						  map.put("qtdedesc", "001 EMB" );
					  }
					  
					  trataGramas = false;
					  
				  }
				  else
				  {
					  if ( isVariableWieght ) trataGramas = false;
					  Unit unit = stock.getProduct().getUnitEtq();
					  String unidade = (unit !=null) ? unit.getId() : "";
					  pesoCalc = stock.getProduct().weightPerPiece();	
					  
					  String txtPrintQty = String.format("%03d", stock.getSecondaryQty()) + " " + unidade;
					  
					  if( qtdPecas != 0 || ( isVariableWieght && qtdPecas > 0 ) )				  
					  {					
						  txtPrintQty = txtPrintQty.concat( " C/" + String.format("%03d", qtdPecas) + " UNI." );		  
					  }
					  
					  map.put("qtdedesc", txtPrintQty );
				  }
				  
			  }
			   		  
			  else{			
	
				  /**
				   * Tratamento para interface de Excecao Peso Padrao
				   */
				  
				  if(stock.getPrimaryQty() == 0 || stock.getPrimaryQtyOperation() == 0 )				  
				  {					
					
					  map.put("qtdedesc"	, "001 EMB" );				  
					  trataGramas = false;			  
					  
				  }
				  else{

					  String qtdPcsFormat = String.format("%03d", stock.getPrimaryQty());					  
					  map.put("qtdedesc", "001 EMB C/" + qtdPcsFormat + " UNI." );					  
				  }
	
				  if(stock.getProduct().getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT  && !packingData.isForceVariableWeight()) {			  			 
					  pesoCalc = stock.getProduct().weightPerPiece();			 			  
				  }		
				  
			  }		
			  
			  if( trataGramas && pesoCalc > 0)
			  {			 

				  if ( pesoCalc < 1.00 )
				  {
					  pesoImp = Utils.format1( pesoCalc * 1000 ).replace(".0", "") + " g"; 
				  }
				  else 
				  {
					  pesoImp = Utils.format1( pesoCalc ).replace(".", ",") + " kg";
				  }
				  map.put("pesodesc", "DE " + pesoImp );	
			  }
			  else
			  {
				  map.put("pesodesc", "");
			  }
			  
			  map.put("pallet", serialNumberPallet);
			  String operation = stock.getTerminal().getIdTerminal().trim() + String.format("%02d", stock.getUser().getId()); 
			  		 
			  operation += stock.getOperation(isPackingPanel);
			 
			  operation += DateTimeUtils.formatDate(DateTimeUtils.now(), "HH:mm");
			  if(packingData.isSecondPrint())
				  operation += " 2";
			  else
				  operation += " 1";
			  
			  map.put("operacao"	, operation);
	  
			  result = sendToPrint(BOX, map, true);
			  
			  if(!result)
			  {
				  printErrorMsg = PrintingService.getMessage();
				  LogDeProcessamento.gravaLog("printerError", printErrorMsg);
			  }
		  
		  }
		  return result;
	  }

	private boolean sendToPrint(int labelModel, LabelMap map, boolean isUseForm) {
		
		String labelSet = getLabel( labelModel );
		boolean result = false;
		
		  switch(Setup.getPrinterModel()) 
		  {
			case INTERMEC:
				result = PrintingService.printIntermec(map,  labelSet);
				break;
				
			case NONE:
				PrintingService.setMessage("Nenhuma impressora configurada");
				break;
				
			case ZEBRA:
				result = PrintingService.printZebra(map, labelSet, isUseForm );
				break;

			case SIMULATOR:
				result = PrintingService.printSimulator(map, labelSet );
				break;		  
		  }
		  
		return result;
	}

	  /**
	   * 
	   * @param quantity
	   * @return
	   */
	  private String getQtdForCodBar(int quantity) {
		
		  int qtyCaracter = String.valueOf(quantity).trim().length();
		  String varReturn;
		  
		  if ( qtyCaracter%2 == 0 ){
			  varReturn = String.valueOf(quantity).trim();
		  }
		  else{
			  varReturn = String.format("%0" + String.valueOf( qtyCaracter + 1 ) + "d", quantity );
		  }
		  
		return varReturn;
	}

	private String getSSCCetq( Stock stock ) {
		  
		  IdentifyLogisticProduct identifyLogisticProduct = stock.getIdentifyLogisticProduct();		  
		  
		  String sscc = stock.getProduct().getEan13().trim().substring(0, 3).concat( stock.getProduct().getPrefixEnterpriseGS1() );		  
		  String sequencialLogistico = String.format("%0" + ( 16 - sscc.trim().length() ) + "d", identifyLogisticProduct.getIdBase());
		  
		  sscc = String.valueOf( identifyLogisticProduct.getVariavelLogistica() ).concat( sscc.concat( sequencialLogistico ) );
		  
		  DigitoVerificador dv = new DigitoVerificador(sscc, DigitoVerificador.DUN14, true);
		  sscc = dv.getCodigo();
		  
		  return sscc;
	  }
	  
	  private String getSSCCetq( Pallet pallet ) {
		  
		  IdentifyLogisticPallet identifyLogisticPallet = pallet.getIdentifyLogisticPallet();		  
		  
		  String sscc = pallet.getProduct().getEan13().trim().substring(0, 3).concat( pallet.getProduct().getPrefixEnterpriseGS1() );		  
		  String sequencialLogistico = String.format("%0" + ( 16 - sscc.trim().length() ) + "d", identifyLogisticPallet.getIdBase());
		  
		  sscc = String.valueOf( identifyLogisticPallet.getVariavelLogistica() ).concat( sscc.concat( sequencialLogistico ) );
		  
		  DigitoVerificador dv = new DigitoVerificador(sscc, DigitoVerificador.DUN14, true);
		  sscc = dv.getCodigo();
		  
		  return sscc;
	  }

	  private String getProcessorEtq(Product product) {
		  return product.getCodSif().trim();
	  }

	  private String getLabel(int opcao) {
		
		  String labelSet = null;
		  Etiqueta etq = null;
		  
		  switch (opcao) {
			case BOX:
				
				etq = packingData.getProduct().getLabelFileName();
				  
				if ( etq == null ){
					labelSet = serverSetup.getDefaultLabelBox().getNameEtq();
				}
				else{
					labelSet = etq.getNameEtq();
				}
				
				labelSet = Setup.getLabelsPath() + GuiGlobals.getSeparador() + Setup.getRootPathLabelBox() + GuiGlobals.getSeparador() + labelSet;
				  
				break;
				
			case PALLET:
				
				etq = packingData.getProduct().getLabelPalletFileName();
								  
				if ( etq == null ){
					labelSet = serverSetup.getDefaultLabelPallet().getNameEtq();
				}
				else{
					labelSet = etq.getNameEtq();
				}
				
				labelSet = Setup.getLabelsPath() + GuiGlobals.getSeparador()  + Setup.getRootPathLabelPallet() + GuiGlobals.getSeparador() + labelSet;
				
				break;
				
			case PALLET_VIRUTAL:
				
				labelSet = Setup.getLabelsPath() + GuiGlobals.getSeparador()  + Setup.getRootPathLabelPallet() + GuiGlobals.getSeparador() + "etiquetapalletvirtual.txt";
				break;
	
			default:
				break;
			}  
		  
		  return labelSet;
	  }
	  
	  private boolean print(Pallet pallet) 
	  {
		  
		  boolean result = false;
		  String flagProductVirutal = "";
		  PalletService ps = new PalletService();
		  GuiGlobals.showMessage("Imprimindo palete...", false);
		  
		  LabelMap map = new LabelMap();
		  
		  String codbar1;
		  String codbar2;
		  String codbar3;
		  String codbarLote = "";
		  String codbar2Aux1 = "";
		  String gtin = null;
		  DigitoVerificador dv;
		  int qtdAcrescCaracteresDesc = 0;
		  boolean isVariableWieght = pallet.getProduct().getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT;		  
		  double taraTotalPallet = pallet.getTare() + pallet.getTareCantoneira() + pallet.getTareRack() + pallet.getStrech();
		  double pesoBruto = pallet.getNet() + taraTotalPallet;
		  
		  /**
		   * Tratamento dos códigos de barras das etiquetas especificas pao de açucar
		   */
		  Etiqueta etiqueta = pallet.getProduct().getLabelPalletFileName();
		  if ( etiqueta != null && etiqueta.isEtqEspPaoAcucar() )
		  {
		  
			  qtdAcrescCaracteresDesc = 14;
			  taraTotalPallet += pallet.getTareOfPacks(); // Tara das embalagens é considerada no total somente da etiqueta pao de açucar
			  pesoBruto = pallet.getNet() + taraTotalPallet;
			  
			  codbarLote = "(10){lote}";
			  if ( isVariableWieght ){
				  codbar1 = "(02){gtin}(3103){liquidocodbar}(3303){pesbrcodbar}(37){qtde}";
				  codbar2 = "(15){datevalcodbar}(11){datefabcodbar}(703{seqprocess}){processador}";
				  codbar3 = "(00){sscc}";
				  
				  gtin = "9".concat( pallet.getProduct().getEan13().substring(0, 12) );
				  dv = new DigitoVerificador(gtin, DigitoVerificador.DUN14, true);
				  gtin = dv.getCodigo();
				  
				  codbar1 = codbar1.replace("{liquidocodbar}"	, StringUtils.leftPad(Utils.formatWeight(pallet.getNet()).replace(".", ""),6,"0"));
				  codbar1 = codbar1.replace("{pesbrcodbar}"		, StringUtils.leftPad(Utils.formatWeight(pesoBruto).replace(".", ""),6,"0"));
				  
				  codbar2 = codbar2.replace("{datevalcodbar}"	, DateTimeUtils.getDate(pallet.getExpirationDate(), "yyMMdd"));
				  codbar2 = codbar2.replace("{datefabcodbar}"	, DateTimeUtils.getDate( pallet.getManufactureDate(), "yyMMdd"));
				  				  
			  }
			  else{
				  codbar1 = "(02){gtin}(15){datevalcodbar}(11){datefabcodbar}(37){qtde}";
				  codbar2 = "(703{seqprocess}){processador}";
				  codbar3 = "(00){sscc}";
				  
				  gtin = pallet.getProduct().getDun14().trim();
				  
				  codbar1 = codbar1.replace("{datevalcodbar}"	, DateTimeUtils.getDate(pallet.getExpirationDate(), "yyMMdd"));
				  codbar1 = codbar1.replace("{datefabcodbar}"	, DateTimeUtils.getDate( pallet.getManufactureDate(), "yyMMdd"));
			  }
			  			  
			  String processador = getProcessorEtq( pallet.getProduct() );
			  String sscc = getSSCCetq( pallet );
			  pallet.setSscc(sscc); // Armazeno o sscc no objeto para salvar o conteudo quando executar o metodo closepallet
			 
			  // Atualizo o codigo sscc no banco de dados
			  try 
			  {
				  ps.updatePallet(pallet, Setup.getTerminal());
			  } 
			  catch (Exception e) 
			  {
				e.printStackTrace();
			  }
			  GuiGlobals.getDaoFactory().getPalletDao().update(pallet);
			  
			  codbar1 = codbar1.replace("{gtin}"				, gtin);
			  codbar1 = codbar1.replace("{qtde}"				, getQtdForCodBar( pallet.getPrimaryQty() ));
			  codbar2 = codbar2.replace("{processador}"	, processador );
			  codbar2 = codbar2.replace("{seqprocess}"		, pallet.getProduct().getSequenciaProcessProduct());
			  codbar2Aux1 = codbar2;
			  codbarLote = codbarLote.replace("{lote}"		, pallet.getBatch());
			  codbar2 = codbar2 + codbarLote;
			  codbar3 = codbar3.replace("{sscc}"				, sscc );
			  
			  map.put("gtin", gtin);
			  map.put("processador", processador);
			  map.put("sscc", sscc);
			  			  
			  map.put("unidade"		  , pallet.getHowSell()  );
			  map.put("codbar1", codbar1);
			  map.put("codbar1Aux", ">;>8" + codbar1.replace("(", "").replace(")", "") );
			  map.put("codbar2", codbar2);
			  map.put("codbar2Aux1", ">;>8" + codbar2Aux1 + ">8" + codbarLote);
//			  map.put("codbar2Aux2", ">8" + codbarLote);
			  map.put("codbar3", codbar3);
			  map.put("codbar3Aux", ">;>8" + codbar3.replace("(", "").replace(")", "") );
			  map.put("empdesc", GuiGlobals.getEnterprise().getName().trim() );
			  
			  String[] infoSif = pallet.getProduct().getDescriptionConservation().split("#");
			  if ( infoSif.length > 0 ) map.put("sifTitulo", infoSif[0]);
			  if ( infoSif.length > 1 ) map.put("sifTemperatura", infoSif[1]);
			  
			  /**
			   * ******************************************************************************
			   */
		  }  
		  
		  map.put("cod", pallet.getProduct().getIdMasc().trim());		  
		  map.put("nomeprod1"	, truncate(pallet.getProduct().getDescription(), pallet.getProduct().getQtyCaracteresDesc() + qtdAcrescCaracteresDesc));		 
		  if ( pallet.getProduct().getDescription().trim().length() > pallet.getProduct().getQtyCaracteresDesc() ) {
			  map.put("nomeprod2", truncate(pallet.getProduct().getDescription().substring(pallet.getProduct().getQtyCaracteresDesc(), pallet.getProduct().getDescription().trim().length()), pallet.getProduct().getQtyCaracteresDesc()));  
		  }
		  else{
			  map.put("nomeprod2", "");
		  }
		  
		  map.put("desc1"		  , truncate(pallet.getProduct().getDescriptionConservation(), 25));
		  map.put("desc2"		  , truncate(pallet.getProduct().getDescriptionSif(), 25));		  
		  map.put("dun14"		  , pallet.getProduct().getDun14Etq());
		  map.put("ean13"		  , pallet.getProduct().getEan13Etq());
		  map.put("qtde1"		  , String.format("%03d", pallet.getPrimaryQty()));
		  map.put("qtde2"		  , String.format("%03d", pallet.getSecondaryQty()).concat(" CX"));		  
		  map.put("datemount" , DateTimeUtils.formatDate(pallet.getManufactureDate()));
		  map.put("dateval"		  , DateTimeUtils.formatDate(pallet.getExpirationDate()));
		  map.put("gtin"      	  , gtin); 		  		  
		  map.put("liquidocodbar" , StringUtils.leftPad(Utils.formatWeight(pallet.getNet()).replace(".", ""),6,"0"));
		  
		  if ( pallet.getProduct().isVirtual() ) {			  
			  flagProductVirutal = "    V";
		  }
		  
		  map.put("serie", ">;" + String.format("%06d", pallet.getId())+ pallet.getCompany().getId());
		  map.put("lote", pallet.getBatch());
		  map.put("liquido", String.format("%.3f", pallet.getNet()).replace(".", ","));
		  
		  map.put("tara"			, String.format("%.3f", taraTotalPallet).replace(".", ","));
		  map.put("pesbr"			, String.format("%.3f", pesoBruto ).replace(".", ","));
		  map.put("pesbrcodbar"   , StringUtils.leftPad(Utils.formatWeight( pesoBruto ).replace(".", ""),6,"0"));
		  map.put("tarapallet"	  , String.format("%.3f", pallet.getTare()).replace(".", ","));
		  map.put("cantoneira"	  , String.format("%.3f", pallet.getTareCantoneira()).replace(".", ","));
		  map.put("rack"		  , String.format("%.3f", pallet.getTareRack()).replace(".", ","));
		  map.put("strech"		  , String.format("%.3f", pallet.getStrech()).replace(".", ","));
		  map.put("taraemb"		  , Utils.formatWeight(pallet.getTareOfPacks()).replace(".", ","));
		  
		  String operation = pallet.getTerminal().getIdTerminal().trim() + String.format("%02d", GuiGlobals.getUserInfo().getUser().getId()); 
	  		 
		  if(!isPackingPanel){
			 operation +="A ";
		  }
		  else{ 
			  
			  if( pallet.getProduct().isFifoEnabled() ){
				  operation += "F ";
			  }
			  else if(pallet.getProduct().getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT) {
				  operation += "P ";
			  }
			  else if(pallet.getProduct().getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT) {
				  operation += "V ";
			  }
		  }
		 
		  operation += DateTimeUtils.formatDate(DateTimeUtils.now(), "HH:mm");
		  if(packingData.isSecondPrint())
			  operation += " 2";
		  else
			  operation += " 1";
		  
		  map.put("operacao"	, operation);
		  
		  if(pallet.getId() > 0)
		  {
			  String filialFromEtqPaoDeAcucar = "";
			  if ( etiqueta != null && etiqueta.isEtqEspPaoAcucar() )
			  {
				  filialFromEtqPaoDeAcucar = " - " + pallet.getCompany().getId();
			  }
			  
			  map.put("pallet", Utils.formatPallet(pallet.getId()).concat( filialFromEtqPaoDeAcucar ).concat( flagProductVirutal ));
		  }
		  else
			  map.put("pallet", "---".concat( flagProductVirutal ));		  
		  
		//"02 unidades de 2,5 kg por caixa"
		  if(pallet.getProduct().getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT) {
			  map.put("qtdedesc", String.format("%02d", pallet.getPrimaryQty()) + " UNIDADES DE");			  
			  map.put("pesodesc", Utils.format1(pallet.getProduct().weightPerPiece()).replace(".", ",") + " kg POR " + pallet.getProduct().getUnit().getId());			  
		  }
		  else {
			  map.put("qtdedesc", String.valueOf(pallet.getPrimaryQty()) + " UNIDADES");			  			  
		  }
		  			  
		  result = sendToPrint(PALLET, map, true);
		  
		  if(!result)
			  LogDeProcessamento.gravaLog(null, "Não foi possível imprimir o pallet " + String.format("%06d", pallet.getId()) + " - " + printErrorMsg );
			  printErrorMsg = PrintingService.getMessage();
		  return result;
		  
	  }
	  
	  private void waitZero( Product product ) 
	  {
			
			double currentNet = 0.0;
			double valueOpen = 0.0;
			double percentRemoveOfScale = ( product.getPercentRemoveProductOnScale() > 0 ) ? product.getPercentRemoveProductOnScale()/100 : 0 ;
			publish("Retire o produto da balança");
			
			if(GuiGlobals.getCurrentScale().isSimulated() ) {
				return;
			}
			
			currentNet = GuiGlobals.getCurrentNet();
			valueOpen = ( currentNet * percentRemoveOfScale );
			while(currentNet > valueOpen ) {			
				Thread.yield();
				GuiGlobals.sleep(60);
				currentNet = GuiGlobals.getCurrentNet();
			}
			
		}
}