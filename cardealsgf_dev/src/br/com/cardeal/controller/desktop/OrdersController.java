package br.com.cardeal.controller.desktop;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.OrderStatus;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.filter.OrderFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Order;
import br.com.cardeal.model.User;
import br.com.cardeal.services.ShipmentService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;

@Resource
public class OrdersController {

    private final Validator validator;
    private final UserInfo userInfo;
    private final Result result;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;
    
	public OrdersController(UserInfo userInfo, Result result, Validator validator, ServletContext servletContext, HttpServletResponse servletResponse) 
	{
		this.userInfo = userInfo;
		this.result = result;
		this.validator = validator;
		this.servletContext = servletContext;
		this.servletResponse = servletResponse;
	}
	
	@Path("/orders")
	@Get
	public void list() 
	{
		validPermission();
		User user = userInfo.getUser();
		
		result.include("user", user);
        result.include("orders", null);
        result.include("listStatus", OrderStatus.values());
	}
	
	void validPermission()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.MANUTENCAO_EXPEDIÇÃO), "manutenção", "user_not_access_manuten_shipment");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	@Path("/orders/viewItens/{orderId}")
	@Get
	public void viewItens(long orderId) {
		User user = userInfo.getUser();		
		Order order = GuiGlobals.getDaoFactory().getOrderDao().find(orderId); 
		result.include("order", order);
		result.include("user", user);		
	}
	
	@Path("/orders/list")
	@Post	
	@Get
	public void list(OrderFilter filter, String date1, String date2) {
		User user = userInfo.getUser();

		getFilterAjust(filter, date1, date2);
		
		List<Order> orders = GuiGlobals.getDaoFactory().getOrderDao().list(filter);
		
        result.include("orders", orders);			
        result.include("listStatus", OrderStatus.values());
        
        result.include("orderIdDe", filter.getIdOrderImportDe());
        result.include("orderIdAte", filter.getIdOrderImportAte());
        result.include("enterDateDe", date1);
        result.include("enterDateAte", date1);
        result.include("orderStatus", filter.getOrderStatus());
        
        result.include("date1", date1);
        result.include("date2", date2);
        result.include("filter", filter);
		result.include("user", user);
		GuiGlobals.closeDb();
    }
	
	public void exportOrdersToExcel( OrderFilter filter, String date1, String date2 )
	{
		getFilterAjust(filter, date1, date2);
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "pedidos_expedicao");
		String fileFullName = getExportName( nameFile );
		
		if ( GuiGlobals.getDaoFactory().getOrderDao().exportObjOrdersToExcel(filter, fileFullName) ) 
		{
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação dos pedidos!");
			result.include("filter", filter);
			result.use(Results.logic()).redirectTo(OrdersController.class).list(filter, date1, date2);
		}
	}
	
	public void showExcelOnBrowser( String fileFullName )
	{
		byte[] arquivo = null;
		File file = new File( fileFullName );
		
		try {
			arquivo = GuiGlobals.fileToByte( file );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		servletResponse.setHeader("Content-disposition", "attachment;filename="+fileFullName);
		servletResponse.setContentType("application/vnd.ms-excel");
		servletResponse.setContentLength(arquivo.length);
		
		ServletOutputStream ouputStream;
		try {
			ouputStream = servletResponse.getOutputStream();
			ouputStream.write(arquivo, 0, arquivo.length);
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getExportName( String nameFile ){
		
		String path = servletContext.getRealPath("/") + GuiGlobals.getSeparadorIvertido() + "stocks_export"; 
		
		if (!new File(path).exists()) { // Verifica se o diretório existe.   
	    	(new File(path)).mkdir();   // Cria o diretório   
	    } 
		
		return path + GuiGlobals.getSeparadorIvertido() + nameFile;
		
	}

	private String getFileNameToExport( TypeExportReport opcao, String nameArq){
		String fileName = DateTimeUtils.getDateForDb() + "_" + DateTimeUtils.getTimeForDb() ;
		return fileName + "_" + nameArq + "." + opcao.getExtensao();
	}
	
	private void getFilterAjust(OrderFilter filter, String date1, String date2) 
	{
		try 
		{
			if(filter == null)
				return;
			
			try {
				if(date1 != null)
					filter.setDate1(DateTimeUtils.strToDateTime(date1 + " 00:00"));
				else
					filter.setDate1(null);
			} catch (Exception e) {
				validator.add(new ValidationMessage("date.invalid", "Formato de data inválida"));
				validator.onErrorUse(Results.logic()).redirectTo(OrdersController.class).list(filter, date1, DateTimeUtils.getDateAndTime());
			}
			
			try {
				if(date2 != null) {
					filter.setDate2(DateTimeUtils.strToDateTime(date2+ " 23:59"));
					filter.setDate2(DateTimeUtils.buildEndDate(filter.getDate2()));
				}
				else
					filter.setDate2(null);
			} catch (Exception e) {
				validator.add(new ValidationMessage("date.invalid", "Formato de data inválida"));
				validator.onErrorUse(Results.logic()).redirectTo(OrdersController.class).list(filter, date1, DateTimeUtils.getDateAndTime());
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}	

	public void insert() 
	{
	    result.forwardTo(this).view(null);
	}
	

	@Path("/orders/view/{order.id}")
	@Get
	public void view(Order order) {
		User user = userInfo.getUser();
		
		if(order != null) {
			order = GuiGlobals.getDaoFactory().getOrderDao().find(order.getId());
		}
		result.include("order", order);
		result.include("user", user);
		GuiGlobals.closeDb();
	}

	@Path("/orders/remove/{order.id}")
	@Get	
	public void remove(Order order) {
		order = GuiGlobals.getDaoFactory().getOrderDao().find(order.getId());
		if( order.getStatus() != OrderStatus.STARTED ) 
		{			
			ShipmentService shipmentService = new ShipmentService(order);
			if ( shipmentService.removeOrder() )
			{
				result.include("notice", "Pedido " + order.getIdPedidoImport() + " removido com sucesso");
			}
			else
			{
				validator.add(new ValidationMessage("", "Pedido " + order.getIdPedidoImport() + " NÃO PODE SER REMOVIDO!"));
				validator.add(new ValidationMessage("", "Verifique se foi feito a devolução deste pedido antes de tentar excluir!"));
				validator.onErrorUse(Results.logic()).redirectTo(OrdersController.class).list();
			}
		}
		else 
		{
			GuiGlobals.closeDb();
			validator.add(new ValidationMessage("", "Pedidos já iniciados não podem ser excluídos. Em vez disto, faça seu cancelamento."));
			validator.onErrorUse(Results.logic()).redirectTo(OrdersController.class).list();			
		}
		
		result.use(Results.logic()).redirectTo(OrdersController.class).list();
		GuiGlobals.closeDb();
	}	

	@Path("/orders/cancel/{order.id}")
	@Get	
	public void cancel(OrderFilter filter, String date1, String date2, Order order) 
	{
		order = GuiGlobals.getDaoFactory().getOrderDao().find(order.getId());
		if(order.getStatus() != OrderStatus.CANCELED) 
		{
			ShipmentService shipmentService = new ShipmentService(order);
			shipmentService.cancelOrder();
			result.include("notice", "Pedido " + order.getIdPedidoImport() + " cancelado com sucesso");			
		}
		else {
			GuiGlobals.closeDb();
			validator.add(new ValidationMessage("", "Pedidos já encerrado!"));
			validator.onErrorUse(Results.logic()).redirectTo(OrdersController.class).list();			
		}
				
		result.use(Results.logic()).redirectTo(OrdersController.class).list(filter, date1, date2);
		GuiGlobals.closeDb();
	}
	
//	@Post
//	@Path("/orders/add")
//	public void add(final Order order) {	
//		//valida partner
//		Partner partner = partnerDao.findById(order.getPartner().getId());
//		if(partner == null) {
//			validator.add(new ValidationMessage("", "Cliente " + String.valueOf(order.getPartner().getId()) + " não cadastrado."));
//			validator.onErrorUse(Results.logic()).redirectTo(OrdersController.class).view(order);							
//		}
//		
//		//valida os itens do pedido
//		int i = 1;
//		for(OrderItem item : order.getItems()) {
//			Product p = prodDao.find(item.getProduct().getId());
//			if(p == null) {
//				validator.add(new ValidationMessage("", "Produto " + String.valueOf(item.getProduct().getId()) + " não cadastrado."));
//				validator.onErrorUse(Results.logic()).redirectTo(OrdersController.class).view(order);							
//			}
//			item.setIndex(i++);
//			item.setProduct(p);
//			item.setOrder(order);
//			
//		}
//
//		//adiciona o header do pedido
//		order.setPartner(partner);
//		order.setStatus(OrderStatus.NOT_STARTED);
//		order.setAvailableFrom(DateTimeUtils.now());
//		order.setCreationDate(DateTimeUtils.now());
//		orderDao.add(order);
//		
//		//adiciona os itens do pedido
//		for(OrderItem item : order.getItems()) {
//			orderDao.addItem(item);
//		}
//		
//		result.include("notice", "Pedido " + order.getId() + " adicionado com sucesso");			
//		result.use(Results.logic()).redirectTo(OrdersController.class).list();
//	}	
}
