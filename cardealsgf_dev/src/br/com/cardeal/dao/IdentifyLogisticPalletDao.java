
package br.com.cardeal.dao;


import java.util.List;
import br.com.cardeal.model.IdentifyLogisticPallet;


public interface IdentifyLogisticPalletDao {

	IdentifyLogisticPallet find(String id);
	
	IdentifyLogisticPallet find(int id);

	void add(IdentifyLogisticPallet identify);
	
	void refresh(IdentifyLogisticPallet identify);
	
	void update(IdentifyLogisticPallet identify);
	
	List<IdentifyLogisticPallet> list();
	
	void delete(IdentifyLogisticPallet identify);

	int getMaxId();

	int getMaxVarLogistc();

	int getMaxIdBase(int maxVarLogistc);

	IdentifyLogisticPallet findCurrentIdentifyByVarLogistic();

}