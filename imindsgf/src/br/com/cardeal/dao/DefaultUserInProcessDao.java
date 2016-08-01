package br.com.cardeal.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.model.UserInProcess;

public class DefaultUserInProcessDao implements UserInProcessDao 
{
	private final Session session;

	/**
	 * Creates a new UserDao. You can receive dependencies through constructor,
	 * because this class is annotated with @Component. This class can be used
	 * as dependency of another class, as well.
	 * @param session Hibernate's Session.
	 */
	public DefaultUserInProcessDao(Session session) {
		this.session = session;
	}

	@Override
	public UserInProcess find(long id) {		
		return (UserInProcess) session.createCriteria(UserInProcess.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}
	
	@Override
	public void add(UserInProcess userInProcess) {		
		session.save(userInProcess);
	}

	@Override
	public void refresh(UserInProcess userInProcess) {
		session.refresh(userInProcess);
	}

	@Override
	public void update(UserInProcess userInProcess) {
		session.merge(userInProcess);
	}

	@Override
	public void delete(UserInProcess userInProcess) {
		userInProcess = find(userInProcess.getId());
		if(userInProcess != null) {
			session.delete(userInProcess);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInProcess> listAll() {
		
		List<UserInProcess> list; // =  (List<UserInProcess>)(session.createCriteria(UserInProcess.class)).list();
		
		Criteria c = session.createCriteria(UserInProcess.class);
		list =  (List<UserInProcess>) c.list();
		
		return list;
		
	}
}
