package br.com.cardeal.model;

import br.com.cardeal.enums.ShipmentTypeOperation;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.NumberUtils;

public class ItensShipment {
	
	private int item;
	private Product product;
	private String unitId;
	private double qtdSolicitada;
	private double qtdExpedida;			
	private double qtdFaltante;
	private double total;
	private String totalUnitId;	
	private TypeStock typeStock;
	private ShipmentTypeOperation typeOperation; //1 - pistola, 3 - pallet, 5 - manual, 6 - com balança
	
	public int getItem() 
	{
		return item;
	}
	
	public void setItem(int item) {
		this.item = item;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public double getQtdSolicitada() {
		return qtdSolicitada;
	}
	
	public String getQtdSolicitadaFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(qtdSolicitada, 3), 14, 3, false, false);
	}
	
	public void setQtdSolicitada(double qtdSolicitada) {
		this.qtdSolicitada = qtdSolicitada;
	}
	
	public double getQtdExpedida() {
		return qtdExpedida;
	}
	
	public String getQtdExpedidaFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(qtdExpedida, 3), 14, 3, false, false);
	}
	
	public void setQtdExpedida(double qtdExpedida) {
		this.qtdExpedida = qtdExpedida;
	}	
	
	public double getQtdFaltante() {
		return qtdFaltante;
	}
	
	public String getQtdFaltanteFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(qtdFaltante, 3), 14, 3, false, false);
	}
	
	public void setQtdFaltante(double qtdFaltante) {
		this.qtdFaltante = qtdFaltante;
	}
	
	public String getUnitId() {
		return unitId;
	}
	
	public void setUnitId(String unit) {
		this.unitId = unit;
	}
	
	public double getTotal() {
		return total;
	}
	
	public String getTotalFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(total, 3), 14, 3, false, false);
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public String getTotalUnitId() {
		return totalUnitId;
	}
	
	public void setTotalUnitId(String totalUnitId) 
	{
		this.totalUnitId = ( totalUnitId == null ) ? "" : totalUnitId;
	}	
	
	public TypeStock getTypeStock() {
		return typeStock;
	}
	
	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}	
	
	public ShipmentTypeOperation getTypeOperation() {
		return typeOperation;
	}
	
	public void setTypeOperation(ShipmentTypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}
	
	public ItensShipment clone()
	{
		ItensShipment itemCloned = new ItensShipment();
		
		itemCloned.setItem(this.item);
		itemCloned.setProduct(this.product);
		itemCloned.setUnitId(this.unitId);
		itemCloned.setQtdSolicitada(this.qtdSolicitada);
		itemCloned.setQtdExpedida(this.qtdExpedida);
		itemCloned.setQtdFaltante(this.qtdFaltante);
		itemCloned.setQtdFaltante(this.qtdFaltante);
		itemCloned.setTotal(this.total);
		itemCloned.setTypeStock(this.typeStock);	
		itemCloned.setTypeOperation(this.typeOperation);
		
		return itemCloned;
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		ItensShipment other = (ItensShipment) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
		
}
