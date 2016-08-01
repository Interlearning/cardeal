package br.com.cardeal.services;

import java.util.List;
import br.com.cardeal.enums.Operation;
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.filter.ProductFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.Etiqueta;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Stock;
import br.com.cardeal.model.Terminal;
import br.com.cardeal.views.ProductsDlg;

public class PackingService {
			
	private Terminal terminal;
	private Company company;
		
	public PackingService() {					
		setTerminal( Setup.getTerminal() );
		setCompany( Setup.getCompany() );
	}	

	public Product findProductByIdMasc( String idMasc ) {		
		return GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( idMasc );
	}

	public Pallet getPalletByProduct(Product product) {
		return GuiGlobals.getDaoFactory().getStockDao().getPalletFor(product, getTerminal(), getCompany());
	}
	
	public Pallet getPalletByProductStocked(Product product, boolean questionNewPallet) {
		GuiGlobals.refreshDaoFactory();
		return GuiGlobals.getDaoFactory().getStockDao().getPalletForStocked(product, getTerminal(), getCompany(), questionNewPallet);
	}
	
	/* WJSP 17/07/2015
	 * 
	 */
	public Partner findPartnerById(int idPartner){
		return GuiGlobals.getDaoFactory().getPartnerDao().findById(idPartner);		
	}
		
	/* WJSP 21/07/2015
	 * 
	 */
	public Stock findStockedById(long idStock){
		return GuiGlobals.getDaoFactory().getStockDao().findStocked(idStock);
	}
	
	
	public void updatePallet(Pallet pallet) {
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try{
			GuiGlobals.getDaoFactory().getPalletDao().update(pallet);
			GuiGlobals.getDaoFactory().commit();			
		}
		catch( Exception e){
			GuiGlobals.getDaoFactory().rollback();
			GuiGlobals.showMessageDlg(e.getMessage());
		}
		
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Stock findLastStockRollBack( TypeStock typeStock) 
	{
		
		Stock lastStock = GuiGlobals.getDaoFactory().getStockDao().findLastStock(getTerminal(), getCompany(), typeStock);
		
		/**
		 *  Verifico no historico se o ultimo estorno realizado foi antes do solicitado
		 */
		if ( lastStock != null ) {
			
			if(lastStock.getPallet() != null) {
				Pallet pallet = GuiGlobals.getDaoFactory().getStockDao().findPallet(lastStock.getPallet().getId());
				if(pallet != null && pallet.getStatus() == PalletStatus.CLOSED) {
					GuiGlobals.showMessageDlg(GuiGlobals.getBundle().getString("STR000026"));
					return null;				
				}
			}
			
			HistoricService historicService = new HistoricService();
			long historicMaxIdStockRollBack = historicService.getMaxIdStockHistoricRollBackByDate( DateTimeUtils.now() );
			
			if ( historicMaxIdStockRollBack > 0){
				
				if ( historicMaxIdStockRollBack > lastStock.getId() ){
					GuiGlobals.showMessageDlg( GuiGlobals.getBundle().getString("STR000019") );
					lastStock = null;
				}
			}
			
		}
		else{
			GuiGlobals.showMessage( GuiGlobals.getBundle().getString("STR000018") );
		}
		
		return lastStock;
	}

	public Pallet findPalletByStock(Stock stock) {
		return GuiGlobals.getDaoFactory().getStockDao().findPallet(stock.getPallet().getId());
	}

	public Product selectProduct() {
		
		Product productSelected = null;
		GuiGlobals.waitCursor();
		
		refreshDaoFactory();
				
		ProductsDlg dlg = new ProductsDlg( getProducts(null) );
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);
		if(dlg.getSelectedProduct() != null) {
			productSelected = dlg.getSelectedProduct();
		}
		
		return productSelected;
		
	}
	
	private void refreshDaoFactory() {
		GuiGlobals.refreshDaoFactory();
	}

	public List<Product> getProducts( ProductFilter filter ){
		
		List<Product> products = null;
		
		if ( filter == null ){
			filter = new ProductFilter();
		}
		filter.setEnabledToShowBlocked(false);
		products = GuiGlobals.getDaoFactory().getProductDao().list(filter);
		
		return products;
	}

	public void rollBack(Stock stock) {
		
		try {
						
			StockService service = new StockService();
			service.leaveStock(stock, Operation.STOCK_ROLLBACK, Setup.getTerminal());
			service.geraEmbala(stock, "S"); // E = Entrada / S = Estorno
			
			GuiGlobals.showMessage("Estorno OK");
			
		}
		catch(Exception e) {
			GuiGlobals.showMessage("Falha Banco de Dados");			
		}
		
	}
	
	public String rulesEtqHowSell( Product product, int qtdCaixas, int qtdPecas, boolean isExcecaoPesoPadrao){
		
		String retornoRulesEtqHowSell = "";
		
		if( ( ( product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT ) || ( product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT && qtdPecas == product.getTargetQty() ) ) && !isExcecaoPesoPadrao ){
			
			if ( product.getUnit().getId().trim().toUpperCase().equals("KG") ){
				retornoRulesEtqHowSell = getUnitKgFormatEtq( product.getUnit().getId() );
			}
			else{
				String qtdEmbFormat = String.format("%03d", qtdCaixas);
				retornoRulesEtqHowSell = qtdEmbFormat.concat(" ").concat( product.getUnit().getId() );
			}
		}
		else{
			
			if(qtdPecas == 0){
				
				if ( isExcecaoPesoPadrao ){
					retornoRulesEtqHowSell = getUnitKgFormatEtq("KG");
				}
				else {
					retornoRulesEtqHowSell = "001 EMB";
				}
				
			}			
			else{
				
				if ( product.getUnit().getId().trim().toUpperCase().equals("KG") ){
					retornoRulesEtqHowSell =  getUnitKgFormatEtq( product.getUnit().getId() );
				}
				else{
				
					if ( qtdPecas > product.getTargetQty() && !isExcecaoPesoPadrao) {
						retornoRulesEtqHowSell = "001 EMB";
					}
					else{
						String qtdPcsFormat = String.format("%03d", qtdPecas);
						retornoRulesEtqHowSell = qtdPcsFormat.concat(" EMB");
						
					}
				}
				
			}
				
		}
		
		return retornoRulesEtqHowSell;
		
	}
	
	private String getUnitKgFormatEtq( String unit ){
		return "    ".concat( unit.toUpperCase().trim() );
	}

	/**
	 * Método que identifica se o produto deve ser expedido via leitor
	 * 
	 * @param product
	 * @return booelan
	 */
	public boolean isViaLeitor( Product product ) {
				
		return (	( product.getPalletQty() > 0 ) || 
						(  (  
								product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT
								|| product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT
						    )
						    && !product.isVirtual()
						    && !product.isFifoEnabled()
						)						
				   );
	}

	public void refreshProduct(Product product) {		
		GuiGlobals.getDaoFactory().getProductDao().refresh(product);
	}
	
	public void refreshEtiqueta(Etiqueta etiqueta) {
		GuiGlobals.getDaoFactory().getEtiquetaDao().refresh(etiqueta);
	}
	
}
