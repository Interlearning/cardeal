
package br.com.cardeal.dao;


import br.com.cardeal.model.ServerSetup;



public interface ServerSetupDao {

	ServerSetup find();

	void update(ServerSetup serverSetup);
	
	void create();
	
}