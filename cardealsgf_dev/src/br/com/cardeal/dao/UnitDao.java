
package br.com.cardeal.dao;


import java.util.List;

import br.com.cardeal.model.Unit;



public interface UnitDao {

	Unit find(String id);

	void add(Unit unit);
	
	void refresh(Unit unit);
	
	void update(Unit unit);
	
	List<Unit> list();
	
	void delete(Unit unit);

	boolean exportObjUnitToExcel(String fileFullName);

}