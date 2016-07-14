package br.com.cardeal.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.cardeal.enums.TypeOperation;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.NumberUtils;

@Entity
public class PurchaseOrderItem 
{
	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private long id;
	
	@Column(name="index_in_order")
	private int index;
	
	@ManyToOne
	private PurchaseOrder purchaseOrder;
	
	@ManyToOne
	private Product product;
	
	private double quantity;
	
	@ManyToOne
	private Unit unit;
	
	private double net;
	
	private double tare;
	
	private TypeStock typeStock;
	
	@ManyToOne
	private Company company;

	@ManyToOne
	private Order order;
	
	private String batchExternal;
	
	private String batchInternal;
	
	private Date dateValidateBatchExternal;
	
	private TypeOperation typeOperation;
	
	private boolean devolution;

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

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public double getQuantity() {
		return quantity;
	}
	
	public String getQuantityFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(getQuantity(), 3), 4, 0, false, false);
	}
	
	public String getQuantityFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(getQuantity(), 3), 4, 0, false, true);
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getNet() {
		return net;
	}
	
	public String getNetFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(getNet(), 3), 14, 3, false, false);
	}
	
	public String getNetFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(getNet(), 3), 14, 3, false, true);
	}

	public void setNet(double net) {
		this.net = net;
	}

	public double getTare() {
		return tare;
	}
	
	public String getTareFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(getTare(), 3), 14, 3, false, false);
	}
	
	public String getTareFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(getTare(), 3), 14, 3, false, true);
	}

	public void setTare(double tare) {
		this.tare = tare;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public TypeStock getTypeStock() {
		return typeStock;
	}

	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}

	public double getGrossWeight() {
		return net + tare;
	}
	
	public String getGrossWeightFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(getGrossWeight(), 3), 14, 3, false, false);
	}

	public String getBatchExternal() {
		return batchExternal;
	}

	public void setBatchExternal(String batchExternal) {
		this.batchExternal = batchExternal;
	}

	public Date getDateValidateBatchExternal() {
		return dateValidateBatchExternal;
	}
	
	public String getDateValidateBatchExternalFormat() {
		return DateTimeUtils.getDate(dateValidateBatchExternal, "dd/MM/yyyy HH:mm");
	}
	
	public String getDateValidateBatchExternalFormatted() {
		return DateTimeUtils.formatDate(dateValidateBatchExternal);
	}

	public void setDateValidateBatchExternal(Date dateValidateBatchExternal) {
		this.dateValidateBatchExternal = dateValidateBatchExternal;
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
		PurchaseOrderItem other = (PurchaseOrderItem) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	public void setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}

	public boolean isNotDevolutionYeat() 
	{
		return !isDevolution();
	}

	public boolean isDevolution() {
		return devolution;
	}

	public void setDevolution(boolean devolution) {
		this.devolution = devolution;
	}

	public String getBatchInternal() {
		return batchInternal;
	}

	public void setBatchInternal(String batchInternal) {
		this.batchInternal = batchInternal;
	}
}
