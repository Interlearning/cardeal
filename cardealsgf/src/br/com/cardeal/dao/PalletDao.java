
package br.com.cardeal.dao;


import java.util.List;

import br.com.cardeal.filter.PalletFilter;
import br.com.cardeal.model.Pallet;



public interface PalletDao {

	Pallet find(int id);

	void add(Pallet pallet);
	
	void refresh(Pallet pallet);
	
	void update(Pallet pallet);
	
	void delete(Pallet pallet);

	List<Pallet> list(PalletFilter filter);

	Pallet findBySSCC(String codSscc);

}