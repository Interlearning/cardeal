
package br.com.cardeal.dao;

import java.util.List;

import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.filter.OrderFilter;
import br.com.cardeal.model.Order;
import br.com.cardeal.model.OrderItem;
import br.com.cardeal.model.Product;

public interface OrderDao {

	Order find(long id);
	
	OrderItem findItem(long id);

	void add(Order order);
	
	void refresh(Order order);
	
	void update(Order order);
	
	List<Order> list(OrderFilter filter);
	
	void delete(Order order);
	
	void addItem(OrderItem item);
	
	void updateItem(OrderItem item);
	
	void deleteItem(OrderItem item);

	Order findByIdImport(String id);
	
	OrderItem findItemByIdImport(String id, int idProduct, int index);
	
	List<OrderItem> findItemByTypeStock(String idOrderImport, int idProduct, TypeStock typeStock);

	OrderItem findItemByIdImport(Order order, Product product, int index);

	int getNextIndex(Order order);

	void deleteItens(List<OrderItem> orderItensGranel);

	boolean exportObjOrdersToExcel(OrderFilter filter, String fileFullName);

	void refreshItem(OrderItem item);

}