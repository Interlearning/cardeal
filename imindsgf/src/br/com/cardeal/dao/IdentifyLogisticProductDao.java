package br.com.cardeal.dao;

import java.util.List;
import br.com.cardeal.model.IdentifyLogisticProduct;

public interface IdentifyLogisticProductDao {

	IdentifyLogisticProduct find(String id);
	
	IdentifyLogisticProduct find(int id);	

	void add(IdentifyLogisticProduct identify);
	
	void refresh(IdentifyLogisticProduct identify);
	
	void update(IdentifyLogisticProduct identify);
	
	List<IdentifyLogisticProduct> list();
	
	void delete(IdentifyLogisticProduct identify);	

	int getMaxVarLogistc();

	int getMaxIdBase(int maxVarLogistc);	

	int getMaxId();

	IdentifyLogisticProduct findCurrentIdentifyByVarLogistic();

}