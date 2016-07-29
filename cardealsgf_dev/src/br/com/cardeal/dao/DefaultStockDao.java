
package br.com.cardeal.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import br.com.cardeal.model.Product;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.enums.StockStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.filter.StockFilter;
import br.com.cardeal.filter.StockFilterManuten;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.Stock;
import br.com.cardeal.model.StockTotal;
import br.com.cardeal.model.StockTotalReport;
import br.com.cardeal.model.Terminal;
import br.com.cardeal.services.PalletService;
import br.com.caelum.vraptor.ioc.Component;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

@Component
public class DefaultStockDao implements StockDao 
{
	public static final int LIST_STOCK_QTY_PER_PAGE = 1000;
	private final Session session;

	public DefaultStockDao(Session session) {
		this.session = session;
	}

	public Stock find(long id) {		
		return (Stock) session.createCriteria(Stock.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}
	
	@Override
	public Stock findBySSCC(String sscc) {
		return (Stock) session.createCriteria(Stock.class)
			    .add( Restrictions.eq("sscc", sscc) )
			    .uniqueResult();
	}
	
	public Stock findStocked(long id) {		
		return (Stock) session.createCriteria(Stock.class)
			    .add( Restrictions.eq("id", id) )
			    .add(Restrictions.eq("status",StockStatus.STOCKED))
			    .uniqueResult();
	}	
	
	public Stock findWithCompany(long id, Company company) {		
		return (Stock) session.createCriteria(Stock.class)
			    .add( Restrictions.eq("id", id) )
			    .add(Restrictions.eq("company.id",company.getId()))		
			    .uniqueResult();
	}
	
	public Stock findWithCompanyOnlyStocked(long id, Company company) {		
		return (Stock) session.createCriteria(Stock.class)
			    .add( Restrictions.eq("id", id) )
			    .add(Restrictions.eq("company.id",company.getId()))			    
			    .uniqueResult();
	}

	public void add(Stock stock) {
		session.save(stock);
	}

	public void refresh(Stock stock) {
		session.refresh(stock);
	}

	public void update(Stock stock) {
		
		boolean isZero = false;
		
		if(stock.getPrimaryQty() == 0){
			stock.setPrimaryQty(1);
			isZero = true;
		}	
		
		session.merge(stock);
		
		if(isZero){
			stock.setPrimaryQty(0);
		}		
	}	

	public void delete(Stock stock) {
		
		if(stock != null && stock.getId() > 0) 
		{
			session.delete(stock);
		}
	}
	
	public List<StockTotalReport> listSumStocked(StockFilter filter, boolean isGroupByProduct)
	{
		Criteria c = session.createCriteria(Stock.class, "stock");
		
		addConditionsByFilter(filter, c);
		
		ProjectionList proList = Projections.projectionList();
		proList.add( Projections.count("id").as("totEmb") );
		proList.add( Projections.sum("primaryQty").as("primaryQty") );
		proList.add( Projections.sum("secondaryQty").as("secondaryQty") );
		proList.add( Projections.sum("net").as("net") );
		
		if ( isGroupByProduct )
			proList.add( Projections.property("product").as("product") );
	
		if ( isGroupByProduct )
			proList.add( Projections.groupProperty("product") );
		
		c.setProjection(proList);
		c.setResultTransformer(Transformers.aliasToBean(StockTotalReport.class));
		
	return (List<StockTotalReport>) c.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Stock> list(StockFilter filter) 
	{
		Criteria c = session.createCriteria(Stock.class, "stock");
		
		if(filter != null) 
		{
			if ( filter.isLimitPage() )
			{
				int page = filter.getPage();
				int qtyPerPage = LIST_STOCK_QTY_PER_PAGE;
				if ( page > 0 )
				{
					int firstResult = ( ( page == 1 ) ? 1 : ( ( page - 1 ) * qtyPerPage ) );
					c.setFirstResult( firstResult );
				}
				c.setMaxResults( qtyPerPage );
			}
			
			addConditionsByFilter(filter, c);

			c.addOrder(Order.asc("company.id"));
			String fieldOrderBy = "";
			
			if ( filter.getOrderBy() != null && !filter.getOrderBy().isEmpty() )
			{
				fieldOrderBy = filter.getOrderBy();
			}
			
			if( filter.isAsc() )
			{
				if ( !fieldOrderBy.isEmpty() ) c.addOrder(Order.asc(fieldOrderBy));
			}
			else
			{
				if ( !fieldOrderBy.isEmpty() ) c.addOrder(Order.desc(fieldOrderBy));
			}
			
			if ( !fieldOrderBy.isEmpty() && !fieldOrderBy.trim().toLowerCase().equals("id") ) c.addOrder(Order.asc("id"));
			
		}		
		
		return c.list();
	}	
	
	private void addConditionsByFilter(StockFilter filter, Criteria c) 
	{
		if(filter.isOnlyStocked())
			c.add( Restrictions.eq("status", StockStatus.STOCKED) );
		
		if(filter.isOnlyFifo())
			c.add( Restrictions.eq("inFifo", filter.isOnlyFifo() ) );
		
		if(filter.getId() > 0)
			c.add(Restrictions.ge("id", filter.getId()));
		
		if(filter.getId_2() > 0)
			c.add(Restrictions.le("id", filter.getId_2()));

		if(filter.getProductId() > 0)
			c.add(Restrictions.eq("product.id", filter.getProductId()));
		
		if( filter.getIdMasc() != null && !filter.getIdMasc().isEmpty() )
		{
			Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc() );
			c.add(Restrictions.ge("product.id", product.getId()));
		}
		
		if( filter.getIdMasc_2() != null && !filter.getIdMasc_2().isEmpty() )
		{
			Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc_2() );
			c.add(Restrictions.le("product.id", product.getId()));
		}
		
		if ( filter.getCompanyIdDe() != null && !filter.getCompanyIdDe().isEmpty() )
			c.add(Restrictions.ge("company.id", filter.getCompanyIdDe()));
		
		if ( filter.getCompanyIdAte() != null && !filter.getCompanyIdAte().isEmpty() )
			c.add(Restrictions.le("company.id", filter.getCompanyIdAte()));
		
		if( ( filter.getCompanyIdDe() != null && !filter.getCompanyIdDe().isEmpty() ) && ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() ) ) {
			
			Terminal terminalDe = GuiGlobals.getDaoFactory().getTerminalDao().find(filter.getCompanyIdDe(), filter.getTerminalId());
			
			if ( terminalDe != null )
				c.add(Restrictions.ge("terminal.id", terminalDe.getId()));
		}
					
		if( ( filter.getCompanyIdAte() != null && !filter.getCompanyIdAte().isEmpty() ) && ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() ) ){
			
			Terminal terminalAte = GuiGlobals.getDaoFactory().getTerminalDao().find( filter.getCompanyIdAte(), filter.getTerminalId_2());
			
			if ( terminalAte != null )
				c.add(Restrictions.le("terminal.id", terminalAte.getId()));
		}	

		if(filter.getPartnerId() > 0)
			c.add(Restrictions.eq("partner.id", filter.getPartnerId()));

		if(filter.getBatch() != null && filter.getBatch().length() > 0)
			c.add(Restrictions.eq("batch", filter.getBatch()));
		
		if(filter.getEnterDateDe() != null)
			c.add(Restrictions.ge("enterDate", filter.getEnterDateDe()));
		
		if(filter.getEnterDateAte() != null) 
			c.add(Restrictions.le("enterDate", filter.getEnterDateAte()));
		
		if(filter.getManufactureDateDe() != null)
			c.add(Restrictions.ge("manufactureDate", filter.getManufactureDateDe()));
		
		if(filter.getManufactureDateAte() != null) 
			c.add(Restrictions.le("manufactureDate", filter.getManufactureDateAte()));
		
		if(filter.getPalletIdDe() > 0)
			c.add(Restrictions.ge("pallet.id", filter.getPalletIdDe()));
		
		if(filter.getPalletIdAte() > 0)
			c.add(Restrictions.le("pallet.id", filter.getPalletIdAte()));
		
		if( filter.isNotPallet() )
			c.add( Restrictions.isNull("pallet.id") );
		
		if(filter.getIdOrderImport() > 0) 
			c.add(Restrictions.le("order.id", filter.getIdOrderImport()));
				
		if(filter.getTypeStock() != null && filter.getTypeStock() != TypeStock.TODOS )
			c.add(Restrictions.eq("typeStock", filter.getTypeStock()));
		
		if ( ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() ) || ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() ) )
		{
			c.createAlias("stock.terminal", "terminal"); // inner join by default
			
			if ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() )
			{
				c.add(Restrictions.ge("terminal.idTerminal", filter.getTerminalId()));
			}
			
			if ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() )
			{
				c.add(Restrictions.le("terminal.idTerminal", filter.getTerminalId_2()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Stock> listWeb(StockFilter filter) 
	{
		Criteria c = session.createCriteria(Stock.class, "stock");
		
		if(filter != null) 
		{
			if ( filter.isLimitPage() )
			{
				int page = filter.getPage();
				int qtyPerPage = LIST_STOCK_QTY_PER_PAGE;
				if ( page > 0 )
				{
					int firstResult = ( ( page == 1 ) ? 1 : ( ( page - 1 ) * qtyPerPage ) );
					c.setFirstResult( firstResult );
				}
				c.setMaxResults( qtyPerPage );
			}
			
			addConditionsByFilterWeb(filter, c);

			c.addOrder(Order.asc("company.id"));
			String fieldOrderBy = "";
			
			if ( filter.getOrderBy() != null && !filter.getOrderBy().isEmpty() )
			{
				fieldOrderBy = filter.getOrderBy();
			}
			
			if( filter.isAsc() )
			{
				if ( !fieldOrderBy.isEmpty() ) c.addOrder(Order.asc(fieldOrderBy));
			}
			else
			{
				if ( !fieldOrderBy.isEmpty() ) c.addOrder(Order.desc(fieldOrderBy));
			}
			
			if ( !fieldOrderBy.isEmpty() && !fieldOrderBy.trim().toLowerCase().equals("id") ) c.addOrder(Order.asc("id"));
			
		}		
		
		return c.list();
	}	
	
	private void addConditionsByFilterWeb(StockFilter filter, Criteria c) 
	{
		if(filter.isOnlyStocked())
			c.add( Restrictions.eq("status", StockStatus.STOCKED) );
		
		if(filter.isOnlyFifo())
			c.add( Restrictions.eq("inFifo", filter.isOnlyFifo() ) );
		
		if(filter.getId() > 0)
			c.add(Restrictions.ge("id", filter.getId()));
		
		if(filter.getId_2() > 0)
			c.add(Restrictions.le("id", filter.getId_2()));

		if(filter.getProductId() > 0)
			c.add(Restrictions.eq("product.id", filter.getProductId()));
		
		c.createAlias("product", "prod",Criteria.INNER_JOIN);
		
		if( filter.getIdMasc() != null && !filter.getIdMasc().isEmpty() )
		{
			
			//Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc() );
			
			c.add(Restrictions.ge("prod.idMasc", filter.getIdMasc()));
		}
		
		if( filter.getIdMasc_2() != null && !filter.getIdMasc_2().isEmpty() )
		{
			//Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc_2() );
			c.add(Restrictions.le("prod.idMasc", filter.getIdMasc_2()));
		}
		
		if ( filter.getCompanyIdDe() != null && !filter.getCompanyIdDe().isEmpty() )
			c.add(Restrictions.ge("company.id", filter.getCompanyIdDe()));
		
		if ( filter.getCompanyIdAte() != null && !filter.getCompanyIdAte().isEmpty() )
			c.add(Restrictions.le("company.id", filter.getCompanyIdAte()));
		
		if( ( filter.getCompanyIdDe() != null && !filter.getCompanyIdDe().isEmpty() ) && ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() ) ) {
			
			Terminal terminalDe = GuiGlobals.getDaoFactory().getTerminalDao().find(filter.getCompanyIdDe(), filter.getTerminalId());
			
			if ( terminalDe != null )
				c.add(Restrictions.ge("terminal.id", terminalDe.getId()));
		}
					
		if( ( filter.getCompanyIdAte() != null && !filter.getCompanyIdAte().isEmpty() ) && ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() ) ){
			
			Terminal terminalAte = GuiGlobals.getDaoFactory().getTerminalDao().find( filter.getCompanyIdAte(), filter.getTerminalId_2());
			
			if ( terminalAte != null )
				c.add(Restrictions.le("terminal.id", terminalAte.getId()));
		}	

		if(filter.getPartnerId() > 0)
			c.add(Restrictions.eq("partner.id", filter.getPartnerId()));

		if(filter.getBatch() != null && filter.getBatch().length() > 0)
			c.add(Restrictions.eq("batch", filter.getBatch()));
		
		if(filter.getEnterDateDe() != null)
			c.add(Restrictions.ge("enterDate", filter.getEnterDateDe()));
		
		if(filter.getEnterDateAte() != null) 
			c.add(Restrictions.le("enterDate", filter.getEnterDateAte()));
		
		if(filter.getManufactureDateDe() != null)
			c.add(Restrictions.ge("manufactureDate", filter.getManufactureDateDe()));
		
		if(filter.getManufactureDateAte() != null) 
			c.add(Restrictions.le("manufactureDate", filter.getManufactureDateAte()));
		
		if(filter.getPalletIdDe() > 0)
			c.add(Restrictions.ge("pallet.id", filter.getPalletIdDe()));
		
		if(filter.getPalletIdAte() > 0)
			c.add(Restrictions.le("pallet.id", filter.getPalletIdAte()));
		
		if( filter.isNotPallet() )
			c.add( Restrictions.isNull("pallet.id") );
		
		if(filter.getIdOrderImport() > 0) 
			c.add(Restrictions.le("order.id", filter.getIdOrderImport()));
				
		if(filter.getTypeStock() != null && filter.getTypeStock() != TypeStock.TODOS )
			c.add(Restrictions.eq("typeStock", filter.getTypeStock()));
		
		if ( ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() ) || ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() ) )
		{
			c.createAlias("stock.terminal", "terminal"); // inner join by default
			
			if ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() )
			{
				c.add(Restrictions.ge("terminal.idTerminal", filter.getTerminalId()));
			}
			
			if ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() )
			{
				c.add(Restrictions.le("terminal.idTerminal", filter.getTerminalId_2()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Stock> listManuten(StockFilterManuten filter) {
		List<Stock> list;
		
		Criteria c = session.createCriteria(Stock.class, "stock");
		
		/**
		 * Regra estabelecida por Antonio em 28/10/2015
		 * Manutencao de estoque somente para estoque não paletizado.
		 */
		c.add( Restrictions.eq("status", StockStatus.STOCKED) );
		c.add( Restrictions.isNull("pallet.id") );
		
		if(filter != null) {
			
			if(filter.getId() > 0)
				c.add(Restrictions.ge("id", filter.getId()));
			
			if(filter.getId_2() > 0)
				c.add(Restrictions.le("id", filter.getId_2()));

			if( filter.getProductIdMasc() != null && !filter.getProductIdMasc().isEmpty() )
			{
				Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getProductIdMasc() );
				c.add(Restrictions.ge("product.id", product.getId()));
			}
			
			if( filter.getProductIdMasc_2() != null && !filter.getProductIdMasc_2().isEmpty() )
			{
				Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getProductIdMasc_2() );
				c.add(Restrictions.le("product.id", product.getId()));
			}
			
			if(filter.getEnterDateDe() != null)
				c.add(Restrictions.ge("enterDate", filter.getEnterDateDe()));
			
			if(filter.getEnterDateAte() != null) 
				c.add(Restrictions.le("enterDate", filter.getEnterDateAte()));
			
			if(filter.getManufactureDateDe() != null)
				c.add(Restrictions.ge("manufactureDate", filter.getManufactureDateDe()));
			
			if(filter.getManufactureDateAte() != null) 
				c.add(Restrictions.le("manufactureDate", filter.getManufactureDateAte()));
			
			if(filter.getTypeStock() != null && filter.getTypeStock() != TypeStock.TODOS )
				c.add(Restrictions.eq("typeStock", filter.getTypeStock()));
			
			if ( filter.getCompanyIdDe() != null && !filter.getCompanyIdDe().isEmpty() )
				c.add(Restrictions.ge("company.id", filter.getCompanyIdDe()));
			
			if ( filter.getCompanyIdAte() != null && !filter.getCompanyIdAte().isEmpty() )
				c.add(Restrictions.le("company.id", filter.getCompanyIdAte()));
			
			if ( ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() ) || ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() ) )
			{
				c.createAlias("stock.terminal", "terminal"); // inner join by default
				
				if ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() )
				{
					c.add(Restrictions.ge("terminal.idTerminal", filter.getTerminalId()));
				}
				
				if ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() )
				{
					c.add(Restrictions.le("terminal.idTerminal", filter.getTerminalId_2()));
				}
			}
			
		}		
		c.addOrder(Order.asc("id"));
		
		list =  (List<Stock>)c.list();
		return list;
	}
	
	private class CustomComparator implements Comparator<StockTotal> {
	    public int compare(StockTotal o1, StockTotal o2) {
	        if (o1.getProduct().getId() < o2.getProduct().getId())
	        	return -1;
	        else
	        	return 1;
	    }
	}	

	@Override
	public List<StockTotal> totalize(StockFilter filter) 
	{
		List<Stock> stocks = list(filter);
		return totalize(stocks);
	}
	
	@Override
	public List<StockTotal> totalizeStock( StockFilter filter ) 
	{
		List<Stock> stocks = list(filter);
		
		return totalizeGeral(stocks);
	}

	private  List<StockTotal> totalizeGeral(List<Stock> stocks) 
	{
	
		List<StockTotal> stockGeral = new ArrayList<StockTotal>();
		
		StockTotal st = new StockTotal();
		st.setNet(0);
		st.setPrimaryQty(0);
		st.setSecondaryQty(0);		
		st.setTotEmb(0);
		
		stockGeral.add(st);
		
		for(Stock s : stocks) 
		{
			st.setNet(st.getNet() + s.getNet());
			st.setPrimaryQty(st.getPrimaryQty() + s.getPrimaryQty());
			st.setSecondaryQty(st.getSecondaryQty() + s.getSecondaryQty());			
			st.setTotEmb(st.getTotEmb()+1);
		}
		
	    return stockGeral;
	}
	
	private  List<StockTotal> totalize(List<Stock> stocks) 
	{
		return totalize(stocks, null);
	}
	private  List<StockTotal> totalize(List<Stock> stocks, Pallet pallet) 
	{
		boolean refreshPallet = ( pallet != null && pallet.getId() > 0 );
		HashMap<Product, StockTotal> map = new HashMap<Product, StockTotal>();
		
		for(Stock s : stocks) 
		{
			if ( refreshPallet )
			{
				if ( pallet.getManufactureDate() == null || pallet.getManufactureDate().compareTo(s.getManufactureDate()) > 0 )
				{
					pallet.setManufactureDate(s.getManufactureDate());
					if ( s.getManufactureDate() != null )
						pallet.setExpirationDate( s.getProduct().getExpirationDate( s.getManufactureDate() ) );
				}
			}
			
			if(map.containsKey(s.getProduct())) 
			{
				StockTotal st = map.get(s.getProduct());
				st.setNet(st.getNet() + s.getNet());
				st.setPrimaryQty(st.getPrimaryQty() + s.getPrimaryQty());
				st.setSecondaryQty(st.getSecondaryQty() + s.getSecondaryQty());
				st.setTare(st.getTare() + s.getTare());
				st.setTotEmb(st.getTotEmb()+1);
			}
			else 
			{
				StockTotal st = new StockTotal();
				
				//WJSP 26/07/2016
				st.setCompany(s.getCompany());
				
				st.setNet(s.getNet());
				st.setPrimaryQty(s.getPrimaryQty());
				st.setProduct(s.getProduct());
				st.setSecondaryQty(s.getSecondaryQty());
				st.setTare(s.getTare());
				st.setTotEmb(1);
				
				map.put(s.getProduct(), st);
			}
		}
		
		ArrayList<StockTotal> sts = new ArrayList<StockTotal>(); 
		@SuppressWarnings("rawtypes")
		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
	        sts.add((StockTotal) pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }		
	    
	    Collections.sort(sts, new CustomComparator());
	    
	    return sts;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Pallet getPalletFor(Product product, Terminal terminal, Company company) {
		//verifica se o produto precisa de pallet
		if(product == null || product.getPalletQty() == 0)
			return null;
		
		//procura por pallets abertos no mesmo terminal/produto
	    //Deveria sempre haver somente 1 pallet aberto
	    //para um terminal/produto, mas a base de dados permite mais de um
		Criteria c = session.createCriteria(Pallet.class);
		c.createCriteria("terminal").add(Restrictions.eq("id", terminal.getId()));
		c.createCriteria("company").add(Restrictions.eq("id", company.getId()));
	    c.add(Restrictions.eq("status", PalletStatus.OPEN));
	    c.createCriteria("product").add(Restrictions.eq("id", product.getId()));
	    c.addOrder(Order.desc("openDate"));
		List<Pallet> pallets = (List<Pallet>)c.list();
	   	    
	    //se nao existe pallet aberto nestas condicoes, cria um novo
	    //caso contrário, verifica se o pallet aberto não está cheio	    
	    if(pallets != null && pallets.size() > 0) {
	    	for(Pallet pallet : pallets) {
	    		//pega o estoque de caixas que tem o mesmo palletId	
				List<Stock> stocks = listStockOfPalletStocked(pallet.getId());
				if(stocks.size() < pallet.getProduct().getPalletQty()) {
					fillPalletInfo(pallet, stocks, false);
					return pallet;				
				}
	    	}
	    }
	    
	    //cria um novo pallet	    
	    Pallet pallet = new Pallet();
	    PalletService ps = new PalletService();
	    try {
			pallet = ps.generatePalletId(product, terminal);
		} catch (Exception e) {
			e.printStackTrace();
			GuiGlobals.getDaoFactory().rollback();
		}
	    	    
	    return pallet;
	}
	
	/* WJSP 26/06/2015
	 * Criado função igual getPalletFor para atender itens totalizador de itens STOCKED tela EMBALAGEM e REPALLETIZAR
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Pallet getPalletForStocked(Product product, Terminal terminal, Company company, boolean questionNewPallet) {
		//verifica se o produto precisa de pallet
		if(product == null || product.getPalletQty() == 0)
			return null;
		
		//procura por pallets abertos no mesmo terminal/produto
	    //Deveria sempre haver somente 1 pallet aberto
	    //para um terminal/produto, mas a base de dados permite mais de um
		Criteria c = session.createCriteria(Pallet.class);
		c.createCriteria("terminal").add(Restrictions.eq("id", terminal.getId()));
		c.createCriteria("company").add(Restrictions.eq("id", company.getId()));
	    c.add(Restrictions.eq("status", PalletStatus.OPEN));
	    c.createCriteria("product").add(Restrictions.eq("id", product.getId()));
	    c.addOrder(Order.desc("openDate"));
		List<Pallet> pallets = (List<Pallet>)c.list();
	   	    
	    //se nao existe pallet aberto nestas condicoes, cria um novo
	    //caso contrário, verifica se o pallet aberto não está cheio		
	    if(pallets != null && pallets.size() > 0) {
	    	for(Pallet pallet : pallets) {
	    		//pega o estoque de caixas que tem o mesmo palletId	e itens STOCKED
				List<Stock> stocks = listStockOfPalletStocked(pallet.getId());
				if(stocks.size() < pallet.getProduct().getPalletQty()) {
					fillPalletInfo(pallet, stocks, false);
					return pallet;				
				}
	    	}
	    }
	    
	    //cria um novo pallet
	    Pallet pallet = null;
	    if ( questionNewPallet  )
	    {
//	    	int opcao = GuiGlobals.showMessageDlgYesNo("Palete finalizado. Deseja gerar código para o próximo palete do produto atual ?");
//	    	questionNewPallet = ( opcao == JOptionPane.YES_OPTION );
	    	questionNewPallet = false;
	    }
	    else
	    {
	    	questionNewPallet = true;
	    }
	    
	    if ( questionNewPallet )
	    {
		    pallet = new Pallet();
		    PalletService ps = new PalletService();
		    try {
				pallet = ps.generatePalletId(product, terminal);
			} catch (Exception e) {
				e.printStackTrace();
				GuiGlobals.getDaoFactory().rollback();
			}
	    }
	    	    
	    return pallet;
	}
	
	@Override
	public Pallet findPallet(int palletId) {
		Criteria c = session.createCriteria(Pallet.class);
	    c.add(Restrictions.eq("id", palletId));
		Pallet pallet = (Pallet)c.uniqueResult();
		
		if(pallet == null)
			return null;
		
		List<Stock> stocks = listStockOfPalletStocked(pallet.getId());
		fillPalletInfo(pallet, stocks, true);
		return pallet;
	}
		
	@Override
	public Pallet findPalletStocked(int palletId) 
	{
		Criteria c = session.createCriteria(Pallet.class);
	    c.add(Restrictions.eq("id", palletId));
	    c.add(Restrictions.not(Restrictions.eq("status", PalletStatus.DELETED)));
		Pallet pallet = (Pallet)c.uniqueResult();
		
		if(pallet == null)
			return null;
		
		List<Stock> stocks = listStockOfPalletStocked(pallet.getId());
		fillPalletInfo(pallet, stocks, true);
		return pallet;
	}
	
	@Override
	public List<Stock> listStockByOrderId(PurchaseOrder purchaseOrder) {
		
		Criteria consult = session.createCriteria(Stock.class);
		consult.add( Restrictions.eq( "status", StockStatus.STOCKED ) );
		consult.add( Restrictions.eq( "order.id", purchaseOrder.getId() ) );
		consult.addOrder( Order.asc( "product.id") );
		
		@SuppressWarnings("unchecked")
		List<Stock> stocks = (List<Stock>) consult.list();
		return stocks;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Stock> listStockOfPalletStocked(int palletId) {		
		Criteria c1 = session.createCriteria(Stock.class);						
		c1.createCriteria("pallet").add(Restrictions.eq("id", palletId));				
		c1.add(Restrictions.eq("status", StockStatus.STOCKED));
		
		List<Stock> stocks = (List<Stock>)c1.list();
		return stocks;
	}
	
	private void fillPalletInfo(Pallet pallet, List<Stock> stocks, boolean keepStocks) 
	{
		List<StockTotal> sts = totalize(stocks, pallet);
		if(sts.size() > 0) {
			StockTotal st = sts.get(0);
			pallet.setNet( st.getNet() );
			pallet.setPrimaryQty( st.getPrimaryQty());
			pallet.setSecondaryQty( st.getSecondaryQty() );
			pallet.setTareOfPacks( st.getTare() );
			pallet.setTotalEtq(st.getTotEmb());
			pallet.setEmbQty(st.getTotEmb());
		}
		else{
			pallet.setNet( 0 );
			pallet.setPrimaryQty( 0 );
			pallet.setSecondaryQty( 0 );
			pallet.setTareOfPacks( 0 );
			pallet.setTotalEtq( 0 );
		}
		
		if(keepStocks)
			pallet.setStocks(stocks);
				
	}	
	
	public List<Pallet> listPallets(StockFilter filter) 
	{
		return listPallets(filter, true);
	}
	@SuppressWarnings("unchecked")
	public List<Pallet> listPallets(StockFilter filter, boolean isAddPalletZeroStock) 
	{
		List<Pallet> list;
		List<Pallet> listOk = new ArrayList<Pallet>();
		Criteria c = session.createCriteria(Pallet.class, "pallet");
		
		c.add(Restrictions.not( Restrictions.eq("status", PalletStatus.DELETED) ) );
		
		
		if(filter != null)
		{
			int page = filter.getPage();
			int qtyPerPage = LIST_STOCK_QTY_PER_PAGE;
			if ( page > 0 )
			{
				int firstResult = ( ( page == 1 ) ? 1 : ( ( page - 1 ) * qtyPerPage ) );
				c.setFirstResult( firstResult );
			}
			c.setMaxResults( qtyPerPage );
						
			if(filter.getPalletIdDe() > 0)
				c.add(Restrictions.ge("id", (int)filter.getPalletIdDe()));
			
			if(filter.getPalletIdAte() > 0)
				c.add(Restrictions.le("id", (int)filter.getPalletIdAte()));

			if( filter.getIdMasc() != null && !filter.getIdMasc().isEmpty() )
			{
				Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc() );
				c.add(Restrictions.ge("product.id", product.getId()));
			}
			
			if( filter.getIdMasc_2() != null && !filter.getIdMasc_2().isEmpty() )
			{
				Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc_2() );
				c.add(Restrictions.le("product.id", product.getId()));
			}
			
			if ( filter.getCompanyIdDe() != null && !filter.getCompanyIdDe().isEmpty() )
				c.add(Restrictions.ge("company.id", filter.getCompanyIdDe()));
			
			if ( filter.getCompanyIdAte() != null && !filter.getCompanyIdAte().isEmpty() )
				c.add(Restrictions.le("company.id", filter.getCompanyIdAte()));
			
			if(filter.getEnterDateDe() != null)
				c.add(Restrictions.ge("openDate", filter.getEnterDateDe()));
			
			if(filter.getEnterDateAte() != null)
				c.add(Restrictions.le("openDate", filter.getEnterDateAte()));
			
			if(filter.getManufactureDateDe() != null)
				c.add(Restrictions.ge("manufactureDate", filter.getManufactureDateDe()));
			
			if(filter.getManufactureDateAte() != null)
				c.add(Restrictions.le("manufactureDate", filter.getManufactureDateAte()));
			
			if(filter.getCloseDateDe() != null)
				c.add(Restrictions.ge("closeDate", filter.getCloseDateDe()));
			
			if(filter.getCloseDateAte() != null)
				c.add(Restrictions.le("closeDate", filter.getCloseDateAte()));
			
			if ( ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() ) || ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() ) )
			{
				c.createAlias("pallet.terminal", "terminal"); // inner join by default
				
				if ( filter.getTerminalId() != null && !filter.getTerminalId().isEmpty() )
				{
					c.add(Restrictions.ge("terminal.idTerminal", filter.getTerminalId()));
				}
				
				if ( filter.getTerminalId_2() != null && !filter.getTerminalId_2().isEmpty() )
				{
					c.add(Restrictions.le("terminal.idTerminal", filter.getTerminalId_2()));
				}
			}
			
		}
		
		c.addOrder(Order.desc("id"));
		
		list =  (List<Pallet>)c.list();
		for(Pallet p : list) 
		{			
			List<Stock> stocks = listStockOfPalletStocked(p.getId());
			if ( stocks.size() > 0 || PalletStatus.OPEN.equals(p.getStatus()) || ( stocks.size() == 0 && isAddPalletZeroStock ) )
			{
				fillPalletInfo(p, stocks, false);
				listOk.add(p);
			}
				
		}
		
		return listOk;
	}

	@Override
	public void closePallet(Pallet pallet) 
	{
		PalletService ps = new PalletService();
		ps.closePallet(pallet, Setup.getTerminal());
	}

	@Override
	public Pallet findLastPallet(Terminal terminal) {
		String hql = "from Pallet as u where u.id = (select max(u1.id) from Pallet as u1 where u1.terminal.id = :terminalId and u1.status = :status)";
		Query query = session.createQuery(hql);
		query.setParameter("terminalId", terminal.getId());
		query.setParameter("status", PalletStatus.CLOSED);
		Pallet pallet = (Pallet)query.uniqueResult();
		
		if(pallet == null)
			return null;
		
		List<Stock> stocks = listStockOfPalletStocked(pallet.getId());
		fillPalletInfo(pallet, stocks, true);
		return pallet;
	}
		
	@Override
	public Pallet findLastPalletStocked(Terminal terminal, Company company) 
	{
		String hql = "from Pallet as u where u.id = (select max(u1.id) from Pallet as u1 where u1.terminal.id = :terminalId and u1.company.id = :companyId and u1.status = :status)";
		Query query = session.createQuery(hql);
		query.setParameter("terminalId", terminal.getId());
		query.setParameter("companyId", company.getId());
		query.setParameter("status", PalletStatus.CLOSED);
		Pallet pallet = (Pallet)query.uniqueResult();
		
		if(pallet == null)
			return null;
		
		List<Stock> stocks = listStockOfPalletStocked(pallet.getId());
		fillPalletInfo(pallet, stocks, true);
		return pallet;
	}
	
	

	@Override
	public Stock findLastStock(Terminal terminal, Company company, TypeStock typeStock) {
		
		String hql = "from Stock as u where u.id = (select max(u1.id) from Stock as u1 where u1.terminal.id = :terminalId and u1.company.id = :companyId and typeStock = :typeStock and status = :status)";
		Query query = session.createQuery(hql);
		query.setParameter("terminalId", terminal.getId());
		query.setParameter("companyId", company.getId());
		query.setParameter("typeStock", typeStock);
		query.setParameter("status", StockStatus.STOCKED);
		
		Stock stock = (Stock)query.uniqueResult();
		return stock;
	}

	@Override
	public Stock findProductByTypeStock(Product product, TypeStock typeStock) {
		return (Stock) session.createCriteria(Stock.class)
				.add( Restrictions.eq("product.id", product.getId()) )
			    .add( Restrictions.eq("typeStock", typeStock) )
			    .uniqueResult();
	}
	
	@Override
	public boolean exportObjPalletsToExcel(StockFilter filter, String fileFullName)
	{
		List<Pallet> pallets = listPallets(filter, false);
		short line = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
	
		/**
		 * se pallets for direferente de null gera arquivo excel
		 */
		if(pallets != null){
			
			@SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
			
			try 
			{
				/**
				 * buferriza a planilha
				 */
				stream = new FileOutputStream( fileFullName );
				
				/**
				 * instancia objetos para montar a planilha
				 */
				Sheet sheet = wb.createSheet("estoque_atual");
				Row header = sheet.createRow(line);
					
				/**
				 * gera as celulas da planilha
				 */
				header.createCell(0).setCellValue("Nr.Série");
				header.createCell(1).setCellValue("Produto");
				header.createCell(2).setCellValue("Embalagens");
				header.createCell(3).setCellValue("Caixas");
				header.createCell(4).setCellValue("Peças");
				header.createCell(5).setCellValue("Peso Liquido KG");
				header.createCell(6).setCellValue("Tara de embalagens KG");
				header.createCell(7).setCellValue("Tara Pallet KG");
				header.createCell(8).setCellValue("Strech KG");
				header.createCell(9).setCellValue("Rack KG");
				header.createCell(10).setCellValue("Cantoneira KG");
				header.createCell(11).setCellValue("Iniciado em");
				header.createCell(12).setCellValue("Fechado em");
				header.createCell(13).setCellValue("Produção");
				header.createCell(14).setCellValue("Status");
					
				// o for ira varrer  o hibernate
				line++;
				
				int totEmb = 0;
		        int secondaryQty = 0;
		        int primaryQty = 0;
		        double net = 0.0;
		        double tareEmb = 0.0;
		        double tarePallet = 0.0;
		        double tareStrech = 0.0;
		        double tareRack = 0.0;
		        double tareCantoneira = 0.0;
					
		        Row data = null;
				for(Pallet pallet : pallets){
			
					/**
					 * resgata dados da base e usa o objeto Row
					 * para armazenar na variavel data.
					 * armazenando em data, cria uma nova celula e 
					 * seta o valor para dentro dela
					 */
					data = sheet.createRow( line );
					
					data.createCell(0).setCellValue(pallet.getIdFormatted());
					data.createCell(1).setCellValue(pallet.getProduct().getDescription());
					data.createCell(2).setCellValue(pallet.getEmbQty());
					data.createCell(3).setCellValue(pallet.getSecondaryQty());
					data.createCell(4).setCellValue(pallet.getPrimaryQty());
					data.createCell(5).setCellValue(pallet.getNet());
					data.createCell(6).setCellValue(pallet.getTareOfPacks());
					data.createCell(7).setCellValue(pallet.getTare());
					data.createCell(8).setCellValue(pallet.getStrech());
					data.createCell(9).setCellValue(pallet.getTareRack());
					data.createCell(10).setCellValue(pallet.getTareCantoneira());
					data.createCell(11).setCellValue(pallet.getOpenDateDesc());
					data.createCell(12).setCellValue(pallet.getCloseDateDesc());
					data.createCell(13).setCellValue(pallet.getManufactureDateDesc());
					data.createCell(14).setCellValue(pallet.getStatusDesc());
					
					totEmb += pallet.getEmbQty();
			        secondaryQty += pallet.getSecondaryQty();
			        primaryQty += pallet.getPrimaryQty();
			        net += pallet.getNet();
			        tareEmb += pallet.getTareOfPacks();
			        tarePallet += pallet.getTare();
			        tareStrech += pallet.getStrech();
			        tareRack += pallet.getTareRack();
			        tareCantoneira += pallet.getTareCantoneira();
					
					line++;
				}
				
				data = sheet.createRow( line );
				data.createCell(0).setCellValue("TOTAL");
				data.createCell(2).setCellValue(totEmb);
				data.createCell(3).setCellValue(secondaryQty);
				data.createCell(4).setCellValue(primaryQty);		        
				data.createCell(5).setCellValue(net);
				data.createCell(6).setCellValue(tareEmb);
				data.createCell(7).setCellValue(tarePallet);
				data.createCell(8).setCellValue(tareStrech);
				data.createCell(9).setCellValue(tareRack);
				data.createCell(10).setCellValue(tareCantoneira);
		        
				/**
				 * gera stream e grava dados na planilha
				 */
				wb.write(stream);
				stream.flush();
				stream.close();
				isImportOk = true;
						
			} 
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	
		GuiGlobals.closeDb();
		return isImportOk;
			 
	}
	
	
	@SuppressWarnings("resource")
	@Override
	public boolean exportObjStockToExcelCurrentStock(StockFilter filter) {
		
		if ( filter != null )
			filter.setOrderBy("id");
		
		List<Stock> currentStock = list(filter);
		DaoFactory daoFactory = new DaoFactory();
		short line = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		String fileName = "currentStock.xls";
		
			/**
			 * se currentStock for direferente de null gera arquivo excel
			 */
			if(currentStock != null){
				Workbook wb = new HSSFWorkbook();
				
					try{
						
						/**
						 * buferriza a planilha
						 */
						stream = new FileOutputStream(daoFactory.getServerSetupDao().find().getStoreDirectory() + GuiGlobals.getSeparador() + fileName );
						
						/**
						 * instancia objetos para montar a planilha
						 */
						Sheet sheet = wb.createSheet("estoque_atual");
						Row header = sheet.createRow(line);
						
						/**
						 * gera as celulas da planilha
						 */
						header.createCell(0).setCellValue("ID");
						header.createCell(1).setCellValue("Nr. Série");
						header.createCell(2).setCellValue("Produto");
						header.createCell(3).setCellValue("Unidade");
						header.createCell(4).setCellValue("Peças");
						header.createCell(5).setCellValue("Peso Kg");
						header.createCell(6).setCellValue("Pallet");
						header.createCell(7).setCellValue("Lote");
						header.createCell(8).setCellValue("Entrada");
						header.createCell(9).setCellValue("Estoque");
						
						// o for ira varrer  o hibernate
						line++;
						
						for(Stock stock : currentStock){
							
								/**
								 * resgata dados da base e usa o objeto Row
								 * para armazenar na variavel data.
								 * armazenando em data, cria uma nova celula e 
								 * seta o valor para dentro dela
								 */
								Row data = sheet.createRow( line );
								
								data.createCell(0).setCellValue(stock.getId());
								data.createCell(1).setCellValue(stock.getIdFormatSerial());
								data.createCell(2).setCellValue(stock.getProduct().getDescription());
								data.createCell(3).setCellValue(stock.getUnitDesc());
								data.createCell(4).setCellValue(stock.getPrimaryQty());
								data.createCell(5).setCellValue(stock.getNet());
								data.createCell(6).setCellValue(stock.getPallet().getId());
								data.createCell(7).setCellValue(stock.getBatch());
								data.createCell(8).setCellValue(stock.getEnterDate());
								data.createCell(9).setCellValue(stock.getTypeStock().name());
								
								line++;
						}
						
							/**
							 * gera stream e grava dados na planilha
							 */
							wb.write(stream);
							stream.flush();
							stream.close();
							isImportOk = true;
							
					} catch(FileNotFoundException e){
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			
			return isImportOk;
	}
	
	@Override
	public boolean exportObjStockTotalToExcel(StockFilter filter, String fileFullName )
	{
		if ( filter != null )
			filter.setOrderBy("id");
		
		List<StockTotal> totais = totalize(filter);
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( totais != null && totais.size() > 0 && fileFullName != null )
		{
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Totais");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("PRODUTO");
		        cabecalho.createCell(1).setCellValue("EMBALAGENS");

		        cabecalho.createCell(2).setCellValue("CAIXAS");
		        cabecalho.createCell(3).setCellValue("PEÇAS");	        
		        cabecalho.createCell(4).setCellValue("PESO LÍQUIDO (kg)");
		
		        int totEmb = 0;
		        int secondaryQty = 0;
		        int primaryQty = 0;
		        double net = 0.0;
		        
		        linha++;
		        Row dados;
		        for ( StockTotal total : totais)
		        {
			        dados = sheet.createRow( linha );
			        dados.createCell(0).setCellValue(total.getProduct().toString());
			        dados.createCell(1).setCellValue(total.getTotEmb());

			        dados.createCell(2).setCellValue(total.getSecondaryQty());
			        dados.createCell(3).setCellValue(total.getPrimaryQty());		        
			        dados.createCell(4).setCellValue(total.getNet());
			        linha++;
			        
			        totEmb += total.getTotEmb();
			        secondaryQty += total.getSecondaryQty();
			        primaryQty += total.getPrimaryQty();
			        net += total.getNet();
			        
		        }
		        
		        dados = sheet.createRow( linha );
		        dados.createCell(0).setCellValue("TOTAL");
		        dados.createCell(1).setCellValue(totEmb);
		        dados.createCell(2).setCellValue(secondaryQty);
		        dados.createCell(3).setCellValue(primaryQty);		        
		        dados.createCell(4).setCellValue(net);
	        	
		        wb.write(stream);
		        stream.flush();
		        stream.close();
				isImportOk = true;
				
			} 
	        catch (FileNotFoundException e) 
	        {				
				e.printStackTrace();
			}
	        catch (IOException e) 
	        {
				e.printStackTrace();
			}
		}
		
		return isImportOk;
	}
	
	@Override
	public boolean exportObjStockToExcel(StockFilter filter, String fileFullName )
	{
		if ( filter != null )
			filter.setOrderBy("id");
		
		List<Stock> stocks = list(filter);
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( stocks != null && stocks.size() > 0 && fileFullName != null )
		{
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Totais");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("FILIAL");
		        cabecalho.createCell(1).setCellValue("NR. SÉRIE");
		        cabecalho.createCell(2).setCellValue("PRODUTO");
		        cabecalho.createCell(3).setCellValue("DESCRIÇÃO");
		        cabecalho.createCell(4).setCellValue("EMBALAGENS");
		        cabecalho.createCell(5).setCellValue("CAIXAS");
		        cabecalho.createCell(6).setCellValue("PEÇAS");
		        cabecalho.createCell(7).setCellValue("PESO LÍQUIDO (kg)");
		        cabecalho.createCell(8).setCellValue("TIPO ESTOQUE");
		        cabecalho.createCell(9).setCellValue("PALETE");
		        cabecalho.createCell(10).setCellValue("FLAG");
		
		        int totEmb = 0;
		        int secondaryQty = 0;
		        int primaryQty = 0;
		        double net = 0.0;
		        
		        linha++;
		        Row dados;
		        for ( Stock stock : stocks)
		        {
			        dados = sheet.createRow( linha );
			        dados.createCell(0).setCellValue(stock.getCompany().getId());
			        dados.createCell(1).setCellValue(stock.getIdFormatSerial());			        			        
			        dados.createCell(2).setCellValue(stock.getProduct().getIdMasc());

			        dados.createCell(3).setCellValue(stock.getProduct().getDescription());
			        dados.createCell(4).setCellValue(1);		        
			        dados.createCell(5).setCellValue(stock.getSecondaryQty());
			        dados.createCell(6).setCellValue(stock.getPrimaryQty());
			        dados.createCell(7).setCellValue(stock.getNet());
			        dados.createCell(8).setCellValue(stock.getTypeStock().getDescricao());
			        dados.createCell(9).setCellValue( ( ( stock.getPallet() == null ) ? Utils.formatPallet(0): stock.getPallet().getIdFormatted() ) );
			        dados.createCell(10).setCellValue(stock.getOperation());
			        linha++;
			        
			        totEmb += 1;
			        secondaryQty += stock.getSecondaryQty();
			        primaryQty += stock.getPrimaryQty();
			        net += stock.getNet();
		        }
		        
		        dados = sheet.createRow( linha );
		        dados.createCell(0).setCellValue("TOTAL");
		        dados.createCell(1).setCellValue("");
		        dados.createCell(2).setCellValue("");
		        dados.createCell(3).setCellValue("");
		        dados.createCell(4).setCellValue(totEmb);		        
		        dados.createCell(5).setCellValue(secondaryQty);
		        dados.createCell(6).setCellValue(primaryQty);
		        dados.createCell(7).setCellValue(net);
		        dados.createCell(8).setCellValue("");
		        dados.createCell(9).setCellValue("");
		        dados.createCell(10).setCellValue("");
	        	
		        wb.write(stream);
		        stream.flush();
		        stream.close();
				isImportOk = true;
				
			} 
	        catch (FileNotFoundException e) 
	        {				
				e.printStackTrace();
			}
	        catch (IOException e) 
	        {
				e.printStackTrace();
			}
		}
		
		return isImportOk;
	}
}