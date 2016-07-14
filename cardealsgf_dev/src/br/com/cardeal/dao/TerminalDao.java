
package br.com.cardeal.dao;

import java.util.List;

import br.com.cardeal.filter.TerminalFilter;
import br.com.cardeal.model.Terminal;

public interface TerminalDao {

	Terminal find(Terminal terminal);
	
	Terminal find(String company_id, String terminal_id );

	void add(Terminal terminal);
	
	void refresh(Terminal terminal);
	
	void update(Terminal terminal);
	
	List<Terminal> list( TerminalFilter filter );
	
	void delete(Terminal terminal);

	boolean exportObjTerminalToExcel(TerminalFilter filter, String fileFullName);	

}