
package br.com.cardeal.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.enums.ProcessWorker;
import br.com.cardeal.filter.ControlAccessFilter;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.ControlAccess;
import br.com.cardeal.model.Terminal;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultControlAccessDao implements ControlAccessDao {

	private final Session session;

	public DefaultControlAccessDao(Session session) {
		this.session = session;
	}

	public ControlAccess find(int id) {		
		return (ControlAccess) session.createCriteria(ControlAccess.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}
	
	public ControlAccess findKeyAccess(Company company, Terminal terminal, ProcessWorker process) {		
		
		Criteria c = session.createCriteria(ControlAccess.class);
		c.createCriteria("terminal").add(Restrictions.eq("id", terminal.getId()));
		c.createCriteria("company").add(Restrictions.eq("id", company.getId()));
		c.add(Restrictions.eq("process", process));
		
		ControlAccess controlAccess = (ControlAccess) c.uniqueResult();
		
		return controlAccess;
		
	}

	public void add(ControlAccess company) {		
		session.save(company);
	}

	public void refresh(ControlAccess company) {
		session.refresh(company);
	}

	public void update(ControlAccess company) {
		session.merge(company);
	}

	public void delete(ControlAccess company) {
		company = find(company.getId());
		if(company != null) {
			session.delete(company);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ControlAccess> list( ControlAccessFilter filter ) {
		
		List<ControlAccess> list; // =  (List<ControlAccess>)(session.createCriteria(ControlAccess.class)).list();
		
		Criteria c = session.createCriteria(ControlAccess.class);
		
		if(filter != null && filter.getCompany() != null) 
			c.createCriteria("company").add(Restrictions.eq("id", filter.getCompany().getId()));
		
		if(filter != null && filter.getTerminal() != null)
			c.createCriteria("terminal").add(Restrictions.eq("id", filter.getTerminal().getId()));
		
		list =  (List<ControlAccess>) c.list();
		
		return list;
		
	}
	
}
