package br.com.cardeal.dao;

import java.util.List;

import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.filter.StockFilter;
import br.com.cardeal.filter.StockFilterManuten;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.Stock;
import br.com.cardeal.model.StockTotal;
import br.com.cardeal.model.Terminal;

public interface StockDao {

	Stock find(long id);

	void add(Stock stock);
	
	void refresh(Stock stock);
	
	void update(Stock stock);
	
	void closePallet(Pallet pallet);
	
	List<Pallet> listPallets(StockFilter filter);
	
	List<Pallet> listPallets(StockFilter filter, boolean isAddPalletZeroStock);
	
	List<Stock> list(StockFilter filter);
	
	List<StockTotal> totalize(StockFilter filter);
	
	void delete(Stock stock);
	
	Pallet getPalletFor(Product product, Terminal terminal, Company company);
	
	Pallet findPallet(int palletId);

	Pallet findLastPallet(Terminal terminal);

	Stock findLastStock(Terminal terminal, Company company, TypeStock typeStock);
	
	boolean exportObjStockToExcelCurrentStock(StockFilter filter);
	
	boolean exportObjPalletsToExcel(StockFilter filter, String fileFullName);
	
	Stock findWithCompany(long idEtiqueta, Company company);
	
	Stock findWithCompanyOnlyStocked(long idEtiqueta, Company company);

	Stock findProductByTypeStock(Product product, TypeStock typeStock);
	
	/* WJSP 26/06/2015
	 * getPalletForStocked e listStockOfPalletStocked para filtrar por STOCKED
	 */
	Pallet getPalletForStocked(Product product, Terminal terminal,Company company, boolean questionNewPallet);
	List<Stock> listStockOfPalletStocked(int palletId);
	
	Pallet findPalletStocked(int palletId);
	Pallet findLastPalletStocked(Terminal terminal, Company company) ;
	Stock findStocked(long id);

	Stock findBySSCC(String sscc);

	List<Stock> listManuten(StockFilterManuten filterOnlyStocked);

	List<Stock> listStockByOrderId(PurchaseOrder purchaseOrder);

	List<StockTotal> totalizeStock( StockFilter filter );

	boolean exportObjStockTotalToExcel(StockFilter filter, String fileFullName);
	
	boolean exportObjStockToExcel(StockFilter filter, String fileFullName);
		
}