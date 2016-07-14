
package br.com.cardeal.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.filter.PalletFilter;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Stock;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultPalletDao implements PalletDao {

	private final Session session;

	public DefaultPalletDao(Session session) {
		this.session = session;
	}

	public Pallet find(int id) {		
		return (Pallet) session.createCriteria(Pallet.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}
	
	@Override
	public Pallet findBySSCC(String sscc) {
		return (Pallet) session.createCriteria(Pallet.class)
			    .add( Restrictions.eq("sscc", sscc) )
			    .uniqueResult();
	}

	public void add(Pallet pallet) {
		session.save(pallet);
	}

	public void refresh(Pallet pallet) {
		session.refresh(pallet);
	}

	public void update(Pallet pallet) {
		session.merge(pallet);
	}

	public void delete(Pallet pallet) {
		pallet = find(pallet.getId());
		if(pallet != null) {
			session.delete(pallet);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Pallet> list(PalletFilter filter) {
		
		List<Pallet> list;
		if(filter == null)
			list = (List<Pallet>)(session.createCriteria(Pallet.class)).list();
		else {
			Criteria c = session.createCriteria(Pallet.class);
			if(filter.getId() != 0) {

				c.add(Restrictions.disjunction()
						.add( Restrictions.like("id", "%" + filter.getId() + "%"))
//						.add( Restrictions.eq("id" , filter.getText())) 
						);				
			}
			
			if(filter.getStatus() != null) {
				c.add( Restrictions.eq("status", filter.getStatus()));
			}
			
			list =  (List<Pallet>)c.list();
			
		}
		return list;
	}

}
