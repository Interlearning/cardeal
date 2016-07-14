package br.com.cardeal.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.model.IdentifyLogisticProduct;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultIdentifyLogisticProductDao implements IdentifyLogisticProductDao {

	private final Session session;

	public DefaultIdentifyLogisticProductDao(Session session) {
		this.session = session;
	}

	public IdentifyLogisticProduct find(String id) {		
		return (IdentifyLogisticProduct) session.createCriteria(IdentifyLogisticProduct.class)
			    .add( Restrictions.eq("id", Integer.parseInt(id)) )
			    .uniqueResult();
	}
	
	public IdentifyLogisticProduct find(int id) {		
		return (IdentifyLogisticProduct) session.createCriteria(IdentifyLogisticProduct.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}

	public void add(IdentifyLogisticProduct identify) {				
		session.save(identify);		
	}

	public void refresh(IdentifyLogisticProduct identify) {
		session.refresh(identify);
	}

	public void update(IdentifyLogisticProduct identify) {
		session.merge(identify);
	}


	public void delete(IdentifyLogisticProduct identify) {
	
		 try {
			 identify = find(String.valueOf(identify.getId()));
			 if (identify != null) {
		        session.delete(identify);
		        session.flush();
			 }
		    } catch (Exception e) {
		        throw e;
		    } finally {
		        session.clear();
		    }
	}
	
	public int getMaxIdBase( int maxVarLogistc ){
		
		IdentifyLogisticProduct identifyLogisticProduct;
		identifyLogisticProduct = (IdentifyLogisticProduct) session.createCriteria(IdentifyLogisticProduct.class) 									
									.add( Restrictions.eq("id", getMaxId()) )
									.add( Restrictions.eq("variavelLogistica", maxVarLogistc ) )
									.uniqueResult();
		
		return identifyLogisticProduct.getIdBase();		
		
	}
	
	public int getMaxIdBase(){
		
		IdentifyLogisticProduct identifyLogisticProduct;
		identifyLogisticProduct = (IdentifyLogisticProduct) session.createCriteria(IdentifyLogisticProduct.class) 									
									.add( Restrictions.eq("id", getMaxId()) )
									.uniqueResult();
		
		return identifyLogisticProduct.getIdBase();
		
	}
	
	public int getMaxVarLogistc(){		
		
		IdentifyLogisticProduct identifyLogisticProduct;
		identifyLogisticProduct = (IdentifyLogisticProduct) session.createCriteria(IdentifyLogisticProduct.class) 									
									.add( Restrictions.eq("id", getMaxId()) )
									.uniqueResult();
		
		return identifyLogisticProduct.getVariavelLogistica();
		
	}	

	@SuppressWarnings("unchecked")
	public List<IdentifyLogisticProduct> list() {
		List<IdentifyLogisticProduct> list =  (List<IdentifyLogisticProduct>)(session.createCriteria(IdentifyLogisticProduct.class)).list();
		return list;
	}
	
	@Override
	public IdentifyLogisticProduct findCurrentIdentifyByVarLogistic() {
		return find( getMaxId() );
	}

	@Override
	public int getMaxId(){		
		return (Integer) session.createCriteria(IdentifyLogisticProduct.class) 
						 .setProjection(Projections.max("id"))						 
						 .uniqueResult(); 
	}

}
