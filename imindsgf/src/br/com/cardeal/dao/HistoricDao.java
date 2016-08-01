
package br.com.cardeal.dao;

import java.util.Date;
import java.util.List;

import br.com.cardeal.filter.HistoricFilter;
import br.com.cardeal.model.Historic;

public interface HistoricDao {

	Historic find(long id);

	void add(Historic historic);
	
	void refresh(Historic historic);
	
	void update(Historic historic);
	
	List<Historic> list(HistoricFilter filter);
	
	void delete(Historic historic);
	
	void delete(List<Historic> historics);
	
	boolean exportObjHistoricToExcel(HistoricFilter filter);

	long getMaxIdStockHistoricRollBackByDate(Date date);
}