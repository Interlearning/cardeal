
package br.com.cardeal.dao;


import java.util.List;

import br.com.cardeal.model.Profile;



public interface ProfileDao {

	Profile find(int id);

	void add(Profile profile);
	
	void refresh(Profile profile);
	
	void update(Profile profile);
	
	List<Profile> list();
	
	String delete(Profile profile);

	boolean exportObjProfileToExcel(String fileFullName);

}