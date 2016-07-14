
package br.com.cardeal.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.model.IdentifyLogisticPallet;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultIdentifyLogisticPalletDao implements IdentifyLogisticPalletDao {

	private final Session session;

	public DefaultIdentifyLogisticPalletDao(Session session) {
		this.session = session;
	}

	public IdentifyLogisticPallet find(String id) {		
		return (IdentifyLogisticPallet) session.createCriteria(IdentifyLogisticPallet.class)
			    .add( Restrictions.eq("id", Integer.parseInt(id)) )
			    .uniqueResult();
	}
	
	public IdentifyLogisticPallet find(int id) {		
		return (IdentifyLogisticPallet) session.createCriteria(IdentifyLogisticPallet.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}

	public void add(IdentifyLogisticPallet identify) {		
		session.save(identify);		
	}
	
	public int getMaxIdBase( int maxVarLogistc ){
		
		IdentifyLogisticPallet identifyLogisticPallet;
		identifyLogisticPallet = (IdentifyLogisticPallet) session.createCriteria(IdentifyLogisticPallet.class) 									
									.add( Restrictions.eq("id", getMaxId()) )
									.add( Restrictions.eq("variavelLogistica", maxVarLogistc ) )
									.uniqueResult();
		
		return identifyLogisticPallet.getIdBase();		
		
	}
	
	public int getMaxIdBase(){
		
		IdentifyLogisticPallet identifyLogisticPallet;
		identifyLogisticPallet = (IdentifyLogisticPallet) session.createCriteria(IdentifyLogisticPallet.class) 									
									.add( Restrictions.eq("id", getMaxId()) )
									.uniqueResult();
		
		return identifyLogisticPallet.getIdBase();
		
	}
	
	public int getMaxVarLogistc(){		
		
		IdentifyLogisticPallet identifyLogisticPallet;
		identifyLogisticPallet = (IdentifyLogisticPallet) session.createCriteria(IdentifyLogisticPallet.class) 									
									.add( Restrictions.eq("id", getMaxId()) )
									.uniqueResult();
		
		return identifyLogisticPallet.getVariavelLogistica();
		
	}		
	
	public int getMaxId(){		
		return (Integer) session.createCriteria(IdentifyLogisticPallet.class) 
						 .setProjection(Projections.max("id"))						 
						 .uniqueResult(); 
		
	}

	public void refresh(IdentifyLogisticPallet identify) {
		session.refresh(identify);
	}

	public void update(IdentifyLogisticPallet identify) {
		session.merge(identify);
	}


	public void delete(IdentifyLogisticPallet identify) {
	
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

	@SuppressWarnings("unchecked")
	public List<IdentifyLogisticPallet> list() {
		List<IdentifyLogisticPallet> list =  (List<IdentifyLogisticPallet>)(session.createCriteria(IdentifyLogisticPallet.class)).list();
		return list;
	}

	@Override
	public IdentifyLogisticPallet findCurrentIdentifyByVarLogistic() {		
		return find( getMaxId() );
	}
	
}
