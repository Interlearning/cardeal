package br.com.cardeal.dao;

import java.util.List;
import br.com.cardeal.model.Enterprise;
import br.com.cardeal.model.IdentifyLogisticPallet;
import br.com.cardeal.model.IdentifyLogisticProduct;

public interface EnterpriseDao {

	Enterprise find(int id);	

	void add(Enterprise enterprise);
	
	void refresh(Enterprise enterprise);
	
	void update(Enterprise enterprise);
	
	List<Enterprise> list();
	
	String delete(Enterprise enterprise);

	void updateupdateWhitSeqLogistics(Enterprise enterprise, IdentifyLogisticProduct identifyLogisticProduct, IdentifyLogisticPallet identifyLogisticPallet);

	boolean exportObjEnterpriseToExcel(String fileFullName);

}