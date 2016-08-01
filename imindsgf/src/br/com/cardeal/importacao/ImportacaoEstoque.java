package br.com.cardeal.importacao;

import br.com.cardeal.dao.DaoFactory;
import br.com.cardeal.dao.ProductDao;
import br.com.cardeal.enums.LayoutEstoque;
import br.com.cardeal.enums.Operation;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Stock;
import br.com.cardeal.services.StockService;

public class ImportacaoEstoque implements ImportacaoInterface {

	private String fileStockImport;
	private String fileName;
	private Product product = null;
	
	public ImportacaoEstoque( String fileName ) {
		this.fileName = fileName.trim();
		setFileProductImport();
	}

	public String getFileProductImport() {
		return fileStockImport;
	}

	public void setFileProductImport() {
		this.fileStockImport = fileName;
	}

	@Override
	public String getFileImport() {
		return fileStockImport;
	}

	@Override
	public boolean sendToSGBD(String[] dados) {
		
		int prodId = 0;
		try 
		{
			prodId = Integer.parseInt(dados[LayoutEstoque.CODIGO_PRODUTO.getPosicao()]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(prodId == 0) 
		{
			GuiGlobals.getMain().showMessage("Código inválido de produto.");
			return false;
		}
		
		DaoFactory daoFactory = GuiGlobals.getDaoFactory();
		
		//Busca o produto na base de dados
		ProductDao dao = daoFactory.getProductDao();
		product = dao.findFilterBlocked( dados[LayoutEstoque.CODIGO_PRODUTO.getPosicao()] );
				
		Company company = daoFactory.getCompanyDao().find( dados[LayoutEstoque.NUMERO_FABRICA.getPosicao()] );
		Pallet pallet = daoFactory.getPalletDao().find( Integer.parseInt(dados[LayoutEstoque.NUMERO_PALLET.getPosicao()]) );
		
		StockService service = new StockService();
		Stock stock = new Stock();
		long id = 0;
		try 
		{
			id = service.generateStockId();
		} 
		catch (Exception e2) 
		{
			e2.printStackTrace();
			return false;
		}
		
		stock.setId(id);
		stock.setIdOld(Long.parseLong(dados[LayoutEstoque.NUMERO_CAIXA.getPosicao()]));
//		stock.setBatch(packingData.getBatch());
		try {
			stock.setManufactureDate(DateTimeUtils.strToDate(dados[LayoutEstoque.DATA_FABRICACAO.getPosicao()].trim(), "yyMMdd" ));
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
			return false;
		}

		stock.setNet(Double.parseDouble(dados[LayoutEstoque.PESO_LIQUIDO.getPosicao()].replace(",", ".")) );
		stock.setTare(Double.parseDouble(dados[LayoutEstoque.TARA_CAIXA.getPosicao()].replace(",", ".")) );
		stock.setPrimaryQty(Integer.parseInt(dados[LayoutEstoque.NUMERO_PECAS.getPosicao()]) );
		stock.setPallet( pallet );
		stock.setCompany( company );
		stock.setProduct(product);
				
		try {
			service.enterStock(stock, Operation.PACKING);		
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
		
	}

	@Override
	public void execute( boolean removeOnFinal ) {
		ReadFile readFileCab = new ReadFile(this);
		try {
			readFileCab.read(false, null, 0, removeOnFinal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
