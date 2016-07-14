package br.com.cardeal.model;

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
import br.com.cardeal.enums.PurchaseOrderStatus;
import br.com.cardeal.enums.TypeReceipt;

@Entity
@Table(name="PurchaseOrder")
public class PurchaseOrder {
	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private long id;
	
	@OneToMany(mappedBy="purchaseOrder")
	List<PurchaseOrderItem> items;
	
	@ManyToOne
	private Partner partner;
	
	@ManyToOne
	private Order orderShipment;
	
	private TypeReceipt typeReceipt;
	
	private String note;
	
	private Date dateReceipt;
	
	private PurchaseOrderStatus status;
	
	@ManyToOne
	private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
		
	public PurchaseOrderStatus getStatus() {
		return status;
	}

	public void setStatus(PurchaseOrderStatus status) {
		this.status = status;
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

	public Order getOrderShipment() {
		return orderShipment;
	}

	public void setOrderShipment(Order orderShipment) {
		this.orderShipment = orderShipment;
	}

	public TypeReceipt getTypeReceipt() {
		return typeReceipt;
	}

	public void setTypeReceipt(TypeReceipt typeReceipt) {
		this.typeReceipt = typeReceipt;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDateReceipt() {
		return dateReceipt;
	}

	public void setDateReceipt(Date dateReceipt) {
		this.dateReceipt = dateReceipt;
	}

	public List<PurchaseOrderItem> getItems() {
		return items;
	}

	public void setItems(List<PurchaseOrderItem> items) {
		this.items = items;
	}
}
