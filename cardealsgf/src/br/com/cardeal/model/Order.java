package br.com.cardeal.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import br.com.cardeal.enums.OrderStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.DateTimeUtils;

@Entity
@Table(name="Ordert")
public class Order {
	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private long id;
	
	@OneToMany(mappedBy="order")
	List<OrderItem> items;
	
	@Transient
	List<OrderItem> itemsCleaning;
	
	@ManyToOne
	private Partner partner;
	
	private String idPedidoImport;
	
	private Date availableFrom;
	
	private Date creationDate;
	
	private OrderStatus status;
	
	@ManyToOne
	private UserInProcess userInProcess;
	
	@ManyToOne
	private Company company;
	
	@ManyToOne
	private Terminal terminal;
	
	private String codEmpTotvs;

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public Date getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(Date availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getCreationDateFormat() 
	{
		if(creationDate == null)
			return "";
		else
			return DateTimeUtils.getDate( creationDate, "dd/MM/yyyy HH:mm" );		
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public List<OrderItem> getItems() {
		return items;
	}
	
	public List<OrderItem> getItemsCleaning() 
	{
		itemsCleaning = new ArrayList<OrderItem>();
		
		for ( OrderItem orderItem : items )
		{
			if ( !TypeStock.GRANEL.equals( orderItem.getTypeStock() ) && !TypeStock.MATERIA_PRIMA.equals( orderItem.getTypeStock() ))
			{
				itemsCleaning.add(orderItem);
			}
		}
		return itemsCleaning;
	}
	
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public String getIdPedidoImport() {
		return idPedidoImport;
	}

	public void setIdPedidoImport(String idPedidoImport) {
		this.idPedidoImport = idPedidoImport;
	}

	public UserInProcess getUserInProcess() {
		return userInProcess;
	}

	public void setUserInProcess(UserInProcess userInProcess) {
		this.userInProcess = userInProcess;
	}

	public List<OrderItem> cloneItens() 
	{
		List<OrderItem> orderItens = new ArrayList<OrderItem>();
		
		for ( OrderItem item : items )
		{
			OrderItem newItem = new OrderItem();
			newItem.setBatch(item.getBatch());
			newItem.setCompany(item.getCompany());
			newItem.setIndex(item.getIndex());
			newItem.setOrder(item.getOrder());
			newItem.setPrimaryQty(item.getPrimaryQty());
			newItem.setProduct(item.getProduct());
			newItem.setQtdMissing(item.getQtdMissing());
			newItem.setQtyIssued(item.getQtyIssued());
			newItem.setQtyRequested(item.getQtyRequested());
			newItem.setTerminal(item.getTerminal());
			newItem.setTotExpHowSell(item.getTotExpHowSell());
			newItem.setTypeOperation(item.getTypeOperation());
			newItem.setTypeStock(item.getTypeStock());
			newItem.setUnit(item.getUnit());
			newItem.setUnitHowSell(item.getUnitHowSell());
			newItem.setDetailItem(item.isDetailItem());
			
			orderItens.add(newItem);
		}
		return orderItens;
	}

	public String getCodEmpTotvs() {
		return codEmpTotvs;
	}

	public void setCodEmpTotvs(String codEmpTotvs) {
		this.codEmpTotvs = codEmpTotvs;
	}

}
