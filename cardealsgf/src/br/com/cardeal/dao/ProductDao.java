
package br.com.cardeal.dao;


import java.util.List;

import br.com.cardeal.filter.ProductFilter;
import br.com.cardeal.model.Product;



public interface ProductDao {

	Product findFilterBlocked(String idMasc);

	Product find(int id);

	void add(Product product);
	
	void refresh(Product product);
	
	void update(Product product);
	
	List<Product> list(ProductFilter filter);
	
	void delete(Product product);

	Product findByEan13(String ean13);
	
	Product findByDun14(String dun14);
	
	Product findByIdMasc(String id);
	
	boolean exportObjProductToExcel(ProductFilter filter, String fileFullName);
}