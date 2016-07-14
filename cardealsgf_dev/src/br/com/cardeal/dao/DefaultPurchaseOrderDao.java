
package br.com.cardeal.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.enums.StockStatus;
import br.com.cardeal.model.Order;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.model.ShipmentItensDevolutionDetails;
import br.com.cardeal.model.Stock;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultPurchaseOrderDao implements PurchaseOrderDao {

	private final Session session;

	public DefaultPurchaseOrderDao(Session session) {
		this.session = session;
	}

	@Override
	public PurchaseOrder find(long id) {		
		return (PurchaseOrder) session.createCriteria(PurchaseOrder.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}
	
	@Override
	public PurchaseOrder getPurchaseOrderByOrderShipment(Order order) {		
		return (PurchaseOrder) session.createCriteria(PurchaseOrder.class)
			    .add( Restrictions.eq("orderShipment.id", order.getId()) )
			    .uniqueResult();
	}
	
	@Override
	public PurchaseOrderItem findItem(long id) {		
		return (PurchaseOrderItem) session.createCriteria(PurchaseOrderItem.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}
	
	@Override
	public PurchaseOrder findByNote(Partner partner, String note) {
		return (PurchaseOrder) session.createCriteria(PurchaseOrder.class)
				.add( Restrictions.eq("note", note) )
				.createCriteria("partner").add(Restrictions.eq("id", partner.getId()))			    
			    .uniqueResult();
	}
	
	@Override
	public PurchaseOrder findByNote(int idPartner, String note) {
		return (PurchaseOrder) session.createCriteria(PurchaseOrder.class)
				.add( Restrictions.eq("note", note) )
				.createCriteria("partner").add(Restrictions.eq("id", idPartner))			    
			    .uniqueResult();
	}

	@Override
	public void add(PurchaseOrder order) {
		session.save(order);
	}
	
	@Override
	public void addShipmentItemDevolution(ShipmentItensDevolutionDetails shipmentItemDevolution) {
		session.save(shipmentItemDevolution);
	}

	@Override
	public void refresh(PurchaseOrder order) {
		session.refresh(order);
	}
	
	@Override
	public void refreshItem(PurchaseOrderItem orderItem) {
		session.refresh(orderItem);
	}

	@Override
	public void update(PurchaseOrder order) {
		session.merge(order);
	}
	
	@Override
	public void updateItem(PurchaseOrderItem orderItem) {
		session.merge(orderItem);
	}
	
	@Override
	public void updateShipmentItemDevolution(ShipmentItensDevolutionDetails shipmentItemDevolution) {
		session.merge(shipmentItemDevolution);
	}

	@Override
	public void delete(PurchaseOrder order) {
		order = find(order.getId());
		if(order != null) {
			session.delete(order);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ShipmentItensDevolutionDetails> getItensDetailsDevolutionByProductFromStock(PurchaseOrderItem purchaseOrderItem)
	{
		Query q = session.createQuery(	"select sd from ShipmentItensDevolutionDetails sd " +
										"inner join sd.stock s " +
										"where sd.stock.id = s.id " +
										"and s.product.id = :productId " +
										"and s.status = :stockStatus " +
										"and sd.purchaseOrderItem.id = :idPurchaseOrderItem")
		.setParameter("productId", purchaseOrderItem.getProduct().getId())
		.setParameter("idPurchaseOrderItem", purchaseOrderItem.getId())
		.setParameter("stockStatus", StockStatus.NON_STOCKED );
		
		return (List<ShipmentItensDevolutionDetails>) q.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ShipmentItensDevolutionDetails> getItensDetailsDevolutionByProduct(PurchaseOrderItem purchaseOrderItem)
	{
		Query q = session.createQuery(	"select sd from ShipmentItensDevolutionDetails sd " +
										"where sd.product.id = :productId " +
										"and sd.purchaseOrderItem.id = :idPurchaseOrderItem")
		.setParameter("productId", purchaseOrderItem.getProduct().getId())
		.setParameter("idPurchaseOrderItem", purchaseOrderItem.getId());
		
		return (List<ShipmentItensDevolutionDetails>) q.list();
	}
	
	@Override
	public void addItem(PurchaseOrderItem item) {
		session.save(item);		
	}

	@Override
	public void deleteAllItensFromOrder(PurchaseOrder purchaseOrder) {

		List<PurchaseOrderItem> itens = listItensFromOrder( purchaseOrder.getId() );
		
		for( PurchaseOrderItem item : itens )
		{
			session.delete( item );
		}
		
	}
	
	@Override
	public void deleteAllItensFromPurchaseOrder(PurchaseOrder purchaseOrder) {

		List<PurchaseOrderItem> itens = listItensFromPurchaseOrder( purchaseOrder.getId() );
		
		for( PurchaseOrderItem item : itens )
		{
			session.delete( item );
		}
		
	}

	@Override
	public String deleteItem(PurchaseOrderItem purchaseOrderItem) 
	{
		try 
		{
			purchaseOrderItem = findItem(purchaseOrderItem.getId());
			if (purchaseOrderItem != null) 
			{
				session.delete(purchaseOrderItem);
				session.flush();
			}
		} 
		catch (Exception e) 
		{
			return e.getCause().getMessage();
		} 
		finally 
		{
			session.clear();
		}
		
		return "OK";

	}

	@SuppressWarnings("unchecked")
	@Override
	public PurchaseOrder findByOrder(Order order) 
	{		
		PurchaseOrder purchaseOrder = null;
		List<PurchaseOrderItem> itens; 
		
		Criteria c = session.createCriteria(PurchaseOrderItem.class)
						.createCriteria("order").add(Restrictions.eq("id", order.getId()));
		
		itens = c.list();
		
		if ( itens != null && itens.size() > 0 )
		{
			purchaseOrder = itens.get(0).getPurchaseOrder();
		}
		
		return purchaseOrder;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseOrderItem> listItensFromOrder(long idOrder) {
		
		Criteria c =	session.createCriteria(PurchaseOrderItem.class)
						.createCriteria("order").add(Restrictions.eq("id", idOrder));
		
		return c.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseOrderItem> listItensFromPurchaseOrder(long idPurchaseOrder) 
	{
		
		Criteria c =	session.createCriteria(PurchaseOrderItem.class)
						.createCriteria("purchaseOrder").add(Restrictions.eq("id", idPurchaseOrder));
		
		return c.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseOrderItem> getPurchaseOrderItensByOrderShipment(Order order) 
	{
		
		Criteria c =	session.createCriteria(PurchaseOrderItem.class)
						.createCriteria("order").add(Restrictions.eq("id", order.getId()));
		
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShipmentItensDevolutionDetails> getItensDetailsDevolutionCheckedByPurchaseOrder(PurchaseOrder purchaseOrder) 
	{
		Query q = session.createQuery(	"select sidd from ShipmentItensDevolutionDetails sidd " +
										"where sidd.purchaseOrderItem.id in ( " + 
											"select poi.id from sidd.purchaseOrderItem poi " +
											"inner join poi.purchaseOrder po " +
											"where po.id = poi.purchaseOrder.id  " +
											"and po.id = :idPurchaseOrder) " +
											"and sidd.checked = :check")
										.setParameter("idPurchaseOrder", purchaseOrder.getId())
										.setParameter("check", true);
				
		return (List<ShipmentItensDevolutionDetails>) q.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ShipmentItensDevolutionDetails> getItensDetailsDevolutionByPurchaseOrder(PurchaseOrder purchaseOrder) 
	{
		Query q = session.createQuery(	"select sidd from ShipmentItensDevolutionDetails sidd " +
										"where sidd.purchaseOrderItem.id in ( " + 
											"select poi.id from sidd.purchaseOrderItem poi " +
											"inner join poi.purchaseOrder po " +
											"where po.id = poi.purchaseOrder.id  " +
											"and po.id = :idPurchaseOrder) ")
										.setParameter("idPurchaseOrder", purchaseOrder.getId());
				
		return (List<ShipmentItensDevolutionDetails>) q.list();
	}

	@Override
	public ShipmentItensDevolutionDetails findByStock(Stock stock) 
	{
		Query q = session.createQuery(	"select sidd from ShipmentItensDevolutionDetails sidd " +
										"inner join sidd.stock s " +
										"where sidd.stock.id = s.id " + 
										"and s.id = :idStock")
										.setParameter("idStock", stock.getId());
		return (ShipmentItensDevolutionDetails) q.uniqueResult();
	}
	
}
