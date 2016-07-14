package br.com.cardeal.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.enums.OrderStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.filter.OrderFilter;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.Order;
import br.com.cardeal.model.OrderItem;
import br.com.cardeal.model.Product;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultOrderDao implements OrderDao 
{

	private final Session session;

	public DefaultOrderDao(Session session) {
		this.session = session;
	}

	public Order find(long id) {		
		return (Order) session.createCriteria(Order.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}
	
	public OrderItem findItem(long id) {		
		return (OrderItem) session.createCriteria(OrderItem.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}
	
	public Order findByIdImport(String id) {		
		return (Order) session.createCriteria(Order.class)
			    .add( Restrictions.eq("idPedidoImport", id) )
			    .uniqueResult();
	}
	
	public OrderItem findItemByIdImport(String idPedidoImport, int idProduct, int index ) {		
		
		Order order = findByIdImport( idPedidoImport );
		
		return (OrderItem) session.createCriteria(OrderItem.class)
			    .add( Restrictions.eq("product.id", idProduct) )
			    .add( Restrictions.eq("order.id", order.getId()) )
			    .add( Restrictions.eq("index", index) )
			    .uniqueResult();		
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderItem> findItemByTypeStock(String idPedidoImport, int idProduct, TypeStock typeStock ) {		
		
		Order order = findByIdImport( idPedidoImport );
		Criteria c = session.createCriteria(OrderItem.class);
		c.add( Restrictions.eq("product.id", idProduct) )
		.add( Restrictions.eq("order.id", order.getId()) )
		.add( Restrictions.eq("typeStock", typeStock) );
		
		return (List<OrderItem>)c.list();
	}
	
	public OrderItem findItemByIdImport(Order order, Product product, int index ) {		
		
		return (OrderItem) session.createCriteria(OrderItem.class)
			    .add( Restrictions.eq("product.id", product.getId()) )
			    .add( Restrictions.eq("order.id", order.getId()) )
			    .add( Restrictions.eq("index", index) )
			    .uniqueResult();		
	}

	public void add(Order order) {
		session.save(order);
	}

	public void refresh(Order order) {
		session.refresh(order);
	}
	
	public void refreshItem(OrderItem orderItem) {
		session.refresh(orderItem);
	}

	public void update(Order order) {
		session.merge(order);
	}

	public void delete(Order order) {
		order = find(order.getId());
		if(order != null) {
			session.delete(order);
		}
	}
	
	public int getNextIndex( Order order ){		
		return ( (Integer) session.createCriteria(OrderItem.class) 
						 .setProjection(Projections.max("index"))
						 .add( Restrictions.eq("order.id", order.getId() ) )
						 .uniqueResult() ) + 1; 
		
	}
	
	@Override
	public void addItem(OrderItem item) {
		session.save(item);		
	}
	
	public void updateItem(OrderItem orderItem) {
		session.merge(orderItem);
	}
	
	public void deleteItem(OrderItem orderItem) 
	{
		orderItem = findItem(orderItem.getId());
		if(orderItem != null) {
			session.delete(orderItem);
		}
	}
	
	public void deleteItens(List<OrderItem> orderItens) 
	{
		for ( OrderItem orderItem : orderItens )
		{
			deleteItem( orderItem );
		}
	}

	@SuppressWarnings("unchecked")
	public List<Order> list(OrderFilter filter) {
		List<Order> list;
		
		if(filter == null)
			list =  (List<Order>)(session.createCriteria(Order.class)).list();
		else {
			Criteria c = session.createCriteria(Order.class);
			
			if(filter.getDate1() != null)
				c.add(Restrictions.ge("creationDate", filter.getDate1()));
			
			if(filter.getDate2() != null)
				c.add(Restrictions.le("creationDate", filter.getDate2()));
			
			if(filter.getId() > 0)
				c.add(Restrictions.eq("id", filter.getId()));

			if(filter.getPartnerId() > 0)
				c.createCriteria("partner").add(Restrictions.eq("id", filter.getPartnerId()));
			
			if( filter.getIdOrderImportDe() != null && !filter.getIdOrderImportDe().isEmpty() )
				c.add(Restrictions.ge("idPedidoImport", filter.getIdOrderImportDe()));
			
			if( filter.getIdOrderImportAte() != null && !filter.getIdOrderImportAte().isEmpty() )
				c.add(Restrictions.le("idPedidoImport", filter.getIdOrderImportAte()));
			
			if(filter.getOrderStatus() != OrderStatus.ALLORDERS)
				c.add( Restrictions.eq("status", filter.getOrderStatus()));

			list =  (List<Order>)c.list();
		}
		
		return list;
	}
	
	@SuppressWarnings({ "resource" })
	@Override
	public boolean exportObjOrdersToExcel(OrderFilter filter, String fileFullName) 
	{
		
		List<Order> orders = GuiGlobals.getDaoFactory().getOrderDao().list(filter);
		List<OrderItem> orderItens = new ArrayList<OrderItem>(); 
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( orders != null && orders.size() > 0 && fileFullName != null )
		{
			//Cria um Arquivo Excel
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Pedidos");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("PEDIDO");
		        cabecalho.createCell(1).setCellValue("DATA");
		        cabecalho.createCell(2).setCellValue("CLIENTE");
		        cabecalho.createCell(3).setCellValue("STATUS");
		        cabecalho.createCell(4).setCellValue("ITEM");
		        cabecalho.createCell(5).setCellValue("PRODUTO");
		        cabecalho.createCell(6).setCellValue("QTD. SOLICITADA");
		        cabecalho.createCell(7).setCellValue("UND. SOLICITADA");
		        cabecalho.createCell(8).setCellValue("QTD. EXPEDIDA");
		        cabecalho.createCell(9).setCellValue("QTD. FALTANTE");
		        cabecalho.createCell(10).setCellValue("FILIAL");
		        cabecalho.createCell(11).setCellValue("TERMINAL");
		
		        linha++;
		        Row dados;
		        for ( Order order : orders)
		        {
			        orderItens.addAll(order.getItemsCleaning());
			        for ( OrderItem orderItem : orderItens )
			        {
			        	dados = sheet.createRow( linha );
				        dados.createCell(0).setCellValue(order.getIdPedidoImport());
				        dados.createCell(1).setCellValue(order.getCreationDateFormat());
				        dados.createCell(2).setCellValue(order.getPartner().getName().trim());
				        dados.createCell(3).setCellValue(order.getStatus().getName());
				        dados.createCell(4).setCellValue(orderItem.getIndex());
				        dados.createCell(5).setCellValue(orderItem.getProduct().toString().trim());
				        dados.createCell(6).setCellValue(orderItem.getQtyRequestedFormatted());
				        dados.createCell(7).setCellValue(orderItem.getUnit().getId());
				        dados.createCell(8).setCellValue(orderItem.getQtyIssuedFormatted());
				        dados.createCell(9).setCellValue(orderItem.getQtdMissingFormatted());
				        dados.createCell(10).setCellValue(orderItem.getCompany().getId());
				        dados.createCell(11).setCellValue(orderItem.getTerminal().getIdTerminal());
				        linha++;
			        }
			        orderItens.clear();
			        linha++;
		        }
	        	
		        wb.write(stream);
		        stream.flush();
		        stream.close();
				isImportOk = true;
				
			} 
	        catch (FileNotFoundException e) 
	        {				
				e.printStackTrace();
			}
	        catch (IOException e) 
	        {
				e.printStackTrace();
			}
		}
		
		return isImportOk;
	}
	
}
