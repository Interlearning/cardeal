
package br.com.cardeal.dao;


import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.cardeal.model.ServerSetup;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultServerSetupDao implements ServerSetupDao {

	private final Session session;

	public DefaultServerSetupDao(Session session) {
		this.session = session;
	}

	public ServerSetup find() {		
		return (ServerSetup) session.createCriteria(ServerSetup.class)
			    .add( Restrictions.eq("id", 1) )
			    .uniqueResult();
	}

	public void update(ServerSetup serverSetup) {
		session.merge(serverSetup);
	}

	@Override
	public void create() {
		ServerSetup ss = new ServerSetup();
		ss.setId(1);
		session.save(ss);
	}
}
