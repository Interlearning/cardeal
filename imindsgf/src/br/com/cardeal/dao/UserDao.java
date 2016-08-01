
package br.com.cardeal.dao;

import java.util.List;
import br.com.cardeal.model.User;

public interface UserDao {

	User find(String login, String password);
	
	User find(String login);

	User find(int id);

	void add(User user);
	
	void refresh(User user);
	
	void update(User user);
	
	List<User> listAll();
	
	boolean containsUserWithLogin(String login);

	void delete(User user);

	User findPsw(String psw);

	boolean containsPasswordWithLogin(User user);

	boolean exportObjUserToExcel(String fileFullName);

}