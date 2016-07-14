package br.com.cardeal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import br.com.cardeal.enums.ShipmentTypeOperation;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.NumberUtils;

@Entity
public class OrderItem 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="index_in_order")
	private int index;
	
	@ManyToOne
	private Order order;
	
	@ManyToOne
	private Product product;
	
	@ManyToOne
	private Unit unit;
	
	private String unitHowSell;
	
	private double totExpHowSell; // quantidade solicitada
	
	@Column(length=18)
	private String batch;
	
	private double qtyRequested; // quantidade solicitada
	
	private double qtyIssued; // quantidade expedida
	
	@Column(nullable = false, columnDefinition = "int default 0")
	private int primaryQty; // quantidade de peças
	
	@Transient
	private double qtdMissing; // quantidade faltante

	@ManyToOne
	private Company company;
	
	@ManyToOne
	private Terminal terminal;
	
	private TypeStock typeStock;
	
	private ShipmentTypeOperation typeOperation;
	
	private boolean detailItem;

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
	
	public double getQtyRequested() {
		return qtyRequested;
	}
	
	public String getQtyRequestedFormatted() {
		return NumberUtils.transform(NumberUtils.roundNumber(qtyRequested, 3), 14, 3, false, false);
	}

	public void setQtyRequested(double qtyMissing) {
		this.qtyRequested = qtyMissing;
	}

	public double getQtyIssued() {
		return qtyIssued;
	}
	
	public String getQtyIssuedFormatted() {
		return NumberUtils.transform(NumberUtils.roundNumber(qtyIssued, 3), 14, 3, false, false);
	}

	public void setQtyIssued(double qtyIssued) {
		this.qtyIssued = qtyIssued;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}		

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public TypeStock getTypeStock() {
		return typeStock;
	}

	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}
	
	public double getQtdMissing() {
		return qtdMissing;
	}
	
	public String getQtdMissingFormatted() {
		return NumberUtils.transform(NumberUtils.roundNumber((qtyRequested - qtyIssued), 3), 14, 3, false, false);
	}

	public void setQtdMissing(double qtdMissing) {
		this.qtdMissing = qtdMissing;
	}

	public String getUnitHowSell() {
		return unitHowSell;
	}

	public void setUnitHowSell(String unitHowSell) {
		this.unitHowSell = unitHowSell;
	}

	public double getTotExpHowSell() {
		return totExpHowSell;
	}

	public void setTotExpHowSell(double totExpHowSell) {
		this.totExpHowSell = totExpHowSell;
	}

	public ShipmentTypeOperation getTypeOperation() {
		return typeOperation;
	}

	public void setTypeOperation(ShipmentTypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}

	public int getPrimaryQty() {
		return primaryQty;
	}

	public void setPrimaryQty(int primaryQty) {
		this.primaryQty = primaryQty;
	}

	public boolean isDetailItem() {
		return detailItem;
	}

	public void setDetailItem(boolean detailItem) {
		this.detailItem = detailItem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderItem other = (OrderItem) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
