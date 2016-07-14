package br.com.cardeal.dao;

import java.util.List;

import br.com.cardeal.enums.ProcessWorker;
import br.com.cardeal.filter.ControlAccessFilter;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.ControlAccess;
import br.com.cardeal.model.Terminal;

public interface ControlAccessDao {

	ControlAccess find(int id);

	void add(ControlAccess controlAccess);
	
	void refresh(ControlAccess controlAccess);
	
	void update(ControlAccess controlAccess);
	
	List<ControlAccess> list( ControlAccessFilter filter );
	
	void delete(ControlAccess controlAccess);
	
	ControlAccess findKeyAccess( Company company, Terminal terminal, ProcessWorker process );

}