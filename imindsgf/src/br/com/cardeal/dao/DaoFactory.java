package br.com.cardeal.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.cardeal.globals.HibernateUtil;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DaoFactory 
{
	private Session session;
	private Transaction transaction;
	private boolean sessionIsMine = true;

	public DaoFactory(Session session) {
		this.session = session;
		sessionIsMine = false;
	}
	
	public DaoFactory() 
	{		
		try {
			if ( session == null || session.disconnect().isClosed()  )
			{
				session = HibernateUtil.getSession();
			}
		} 
		catch (HibernateException | SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public Session getSession() {
		return session;
	}
	
	public void beginTransaction() {
		this.transaction = this.session.beginTransaction();
	}
	
	public void commit() {
		this.transaction.commit();
		this.transaction = null;
	}
	
	public boolean hasTransaction() {
		return this.transaction != null;
	}
		
	public void rollback() {
		this.transaction.rollback();
		this.transaction = null;
	}
	
	public void close() {
		if(sessionIsMine)
			this.session.close();
	}	
	
	public UserDao getUserDao() {
		return new DefaultUserDao(this.session);
	}
	
	public CompanyDao getCompanyDao(){
		return new DefaultCompanyDao(this.session);
	}
	
	public StockDao getStockDao() {
		return new DefaultStockDao(this.session);
	}
	
	public HistoricDao getHistoricDao() {
		return new DefaultHistoricDao(this.session);
	}
	
	public ProductDao getProductDao() {
		return new DefaultProductDao(this.session);
	}
	
	public TerminalDao getTerminalDao() {
		return new DefaultTerminalDao(this.session);
	}
	
	public PartnerDao getPartnerDao() {
		return new DefaultPartnerDao(this.session);
	}
	
	public ServerSetupDao getServerSetupDao() {
		return new DefaultServerSetupDao(this.session);
	}
	
	public ProfileDao getProfileDao() {
		return new DefaultProfileDao(this.session);
	}
	
	public PalletDao getPalletDao() {
		return new DefaultPalletDao(this.session);
	}	
	
	public OrderDao getOrderDao() {
		return new DefaultOrderDao(this.session);
	}	
	
	public PurchaseOrderDao getPurchaseOrderDao() {
		return new DefaultPurchaseOrderDao(this.session);
	}
	
	public ControlAccessDao getControlAccessDao() {
		return new DefaultControlAccessDao(this.session);
	}
	
	public UnitDao getUnitDao() {
		return new DefaultUnitDao(this.session);
	}

	public EnterpriseDao getEnterpriseDao() {
		return new DefaultEnterpriseDao(this.session);
	}
	
	public IdentifyLogisticProductDao getIdentifyLogisticProductDao() {
		return new DefaultIdentifyLogisticProductDao(this.session);
	}

	public EtiquetaDao getEtiquetaDao() {
		return new DefaultEtiquetaDao(this.session);
	}

	public IdentifyLogisticPalletDao getIdentifyLogisticPalletDao() {
		return new DefaultIdentifyLogisticPalletDao(this.session);
	}
	
	public UserInProcessDao getUserInProcessDao() {
		return new DefaultUserInProcessDao(this.session);
	}
}
