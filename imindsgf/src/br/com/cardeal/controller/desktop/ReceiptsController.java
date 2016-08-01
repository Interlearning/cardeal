package br.com.cardeal.controller.desktop;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import br.com.cardeal.dao.DaoFactory;
import br.com.cardeal.dao.PartnerDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.filter.ReceiptFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;

@Resource
public class ReceiptsController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private final DaoFactory dao;

    public ReceiptsController
		(
		DaoFactory dao
		, PartnerDao partnerDao
		, Result result
		, UserInfo userInfo
		, Validator validator		
		) 
	{
		this.dao = dao;
		this.result = result;
		this.userInfo = userInfo;
		this.validator = validator;
	}
	
	@Path("/receipts")
	@Get
	public void list() 
	{
		validPermission();
		setVariablesOnList(null, null);
	}
	
	@Post("/receipts")
	public void list( ReceiptFilter filter ) 
	{
        List<PurchaseOrderItem> itensReceipt = new ArrayList<PurchaseOrderItem>();
        
        if ( filter != null && filter.getIdPartner() > 0 && StringUtils.isNotBlank( filter.getNote() ) )
        {
        	Partner partner = dao.getPartnerDao().findById( filter.getIdPartner() );
        	
        	if ( partner != null )
        	{
	        	PurchaseOrder purchaseOrder = dao.getPurchaseOrderDao().findByNote( partner, filter.getNote() );
	        	
	        	if ( purchaseOrder != null && purchaseOrder.getId() > 0 )
	        	{
	        		itensReceipt = dao.getPurchaseOrderDao().listItensFromPurchaseOrder( purchaseOrder.getId() );
	        	
		        	if ( itensReceipt == null || itensReceipt.size() == 0 )
		        	{
		        		result.include("notice", "Não foi encontrado os itens da nota [" + filter.getNote() + "]" );
		        	}
	        	}
	        	else
	        	{
	        		result.include("notice", "Não foi encontrado a nota [" + filter.getNote() + "] para o fornecedor [" + partner.getName().trim() + "]" );
	        	}
	        	
        	}
        	else
        	{
        		result.include("notice", "Não foi encontrado fornecedor com este id [" + filter.getIdPartner() + "]" );
        	}
        }
        else
		{
			result.include("notice", "Informe os parâmetros!");
		}
        
        setVariablesOnList(filter, itensReceipt);

    }
	
	private void setVariablesOnList(ReceiptFilter filter, List<PurchaseOrderItem> itensReceipt)
	{				
		User user = userInfo.getUser();
		result.include("user", user);
		result.include("itensReceipt", itensReceipt);
        result.include("filter", filter);        
	}
	
	@Path("/receipts/remove/{item.id}")
	@Get	
	public void remove(PurchaseOrderItem item) 
	{
		ReceiptFilter filter = null;
		dao.getPurchaseOrderDao().refreshItem(item);
		
		filter = new ReceiptFilter();
		filter.setNote(item.getPurchaseOrder().getNote());
		filter.setIdPartner(item.getPurchaseOrder().getPartner().getId());
		
	    String deleteMessage = dao.getPurchaseOrderDao().deleteItem(item);
	    if ( deleteMessage.equals("OK") )
	    {
			result.include("notice", "Item " + item.getIndex() + " removido com sucesso");
	    }
	    else
	    {
	    	result.include("notice", "ITEM " + item.getIndex() + " NÃO PODE SER REMOVIDO ==>  " + deleteMessage);
	    }
	    result.use(Results.logic()).forwardTo(ReceiptsController.class).list(filter);
	}
	
	void validPermission()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.MANUTENCAO_RECEBIMENTO), "receipt", "user_not_insert_receipt");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	@Path("/receipts/view/{item.id}")
	@Get
	public void view(PurchaseOrderItem item) 
	{		
		dao.getPurchaseOrderDao().refreshItem(item);
		ReceiptFilter filter = null;
		filter = new ReceiptFilter();
		filter.setNote(item.getPurchaseOrder().getNote());
		filter.setIdPartner(item.getPurchaseOrder().getPartner().getId());
		
		if(item != null)
			result.include("itemEdit", item);
		else
		    result.include("itemEdit", null);
		
		result.include("filter", filter);
	}
	
	@Path("/receipts/change")
	@Post
	public void change(PurchaseOrderItem itemEdit, String validateDate) 
	{
		if(validateDate != null)
		{
			try 
			{
				itemEdit.setDateValidateBatchExternal(DateTimeUtils.strToDateTime(validateDate));
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		PurchaseOrderItem itemOriginal = dao.getPurchaseOrderDao().findItem(itemEdit.getId());
		
		itemOriginal.setQuantity(itemEdit.getQuantity());
		itemOriginal.setTare(itemEdit.getTare());
		itemOriginal.setNet(itemEdit.getNet());
		itemOriginal.setBatchExternal(itemEdit.getBatchExternal());
		itemOriginal.setDateValidateBatchExternal(itemEdit.getDateValidateBatchExternal());
		
		dao.beginTransaction();
		try
		{
			dao.getPurchaseOrderDao().updateItem(itemOriginal);
			dao.commit();
		}
		catch( Exception e )
		{
			dao.rollback();
			e.printStackTrace();
		}
		
		ReceiptFilter filter = null;
		filter = new ReceiptFilter();
		filter.setNote(itemOriginal.getPurchaseOrder().getNote());
		filter.setIdPartner(itemOriginal.getPurchaseOrder().getPartner().getId());
		result.include("notice", "Item atualizado com sucesso!");
		validator.onErrorUsePageOf(this).list();
		result.use(Results.logic()).forwardTo(ReceiptsController.class).list(filter);
		
	}
}
