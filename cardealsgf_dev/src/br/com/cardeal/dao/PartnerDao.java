
package br.com.cardeal.dao;


import java.util.List;

import br.com.cardeal.filter.PartnerFilter;
import br.com.cardeal.model.Partner;



public interface PartnerDao {

	Partner findById(int id);

	Partner findByCnpj(String cnpj);

	void add(Partner partner);
	
	void refresh(Partner partner);
	
	void update(Partner partner);
	
	List<Partner> list(PartnerFilter filter);
	
	String delete(Partner partner);

	Partner findByName(String string);

	boolean exportObjPartnerToExcel(PartnerFilter filter, String fileFullName);

	Partner findByIdExternal(String idExternal);
	
}