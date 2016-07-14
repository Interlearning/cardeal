package br.com.cardeal.dao;

import java.util.List;
import br.com.cardeal.model.Company;

public interface CompanyDao {

	Company find(String id);

	void add(Company company);
	
	void refresh(Company company);
	
	void update(Company company);
	
	List<Company> list();
	
	String delete(Company company);

	boolean exportObjCompanyToExcel(String fileFullName);

}