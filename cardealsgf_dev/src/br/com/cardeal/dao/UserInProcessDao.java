package br.com.cardeal.dao;

import java.util.List;
import br.com.cardeal.model.UserInProcess;

public interface UserInProcessDao 
{
	UserInProcess find(long id);

	void add(UserInProcess userInProcess);
	
	void refresh(UserInProcess userInProcess);
	
	void update(UserInProcess userInProcess);
	
	void delete(UserInProcess userInProcess);
	
	List<UserInProcess> listAll();
}
