package br.com.cardeal.dao;

import java.util.List;

import br.com.cardeal.model.Order;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.model.ShipmentItensDevolutionDetails;
import br.com.cardeal.model.Stock;

public interface PurchaseOrderDao {

	PurchaseOrder find(long id);

	void add(PurchaseOrder order);
	
	void addShipmentItemDevolution(ShipmentItensDevolutionDetails shipmentItemDevolution);
	
	void refresh(PurchaseOrder order);
	
	void update(PurchaseOrder order);
	
	void updateShipmentItemDevolution(ShipmentItensDevolutionDetails shipmentItemDevolution);
	
	void delete(PurchaseOrder order);
	
	void addItem(PurchaseOrderItem item);
	
	PurchaseOrder findByNote(Partner partner, String note);
	
	PurchaseOrder findByNote(int idPartner, String note);

	void deleteAllItensFromOrder(PurchaseOrder purchaseOrder);
	
	void deleteAllItensFromPurchaseOrder(PurchaseOrder purchaseOrder);

	String deleteItem(PurchaseOrderItem purchaseOrderItem);

	PurchaseOrder findByOrder(Order order);

	List<PurchaseOrderItem> listItensFromPurchaseOrder(long id);

	List<PurchaseOrderItem> listItensFromOrder(long idOrder);

	List<ShipmentItensDevolutionDetails> getItensDetailsDevolutionByProductFromStock(PurchaseOrderItem purchaseOrderItem);

	List<ShipmentItensDevolutionDetails> getItensDetailsDevolutionCheckedByPurchaseOrder(PurchaseOrder purchaseOrder);
	
	List<ShipmentItensDevolutionDetails> getItensDetailsDevolutionByPurchaseOrder(PurchaseOrder purchaseOrder);

	ShipmentItensDevolutionDetails findByStock(Stock stock);

	void refreshItem(PurchaseOrderItem orderItem);

	PurchaseOrderItem findItem(long id);

	void updateItem(PurchaseOrderItem item);

	List<ShipmentItensDevolutionDetails> getItensDetailsDevolutionByProduct(PurchaseOrderItem purchaseOrderItem);

	PurchaseOrder getPurchaseOrderByOrderShipment(Order order);

	List<PurchaseOrderItem> getPurchaseOrderItensByOrderShipment(Order order);

}