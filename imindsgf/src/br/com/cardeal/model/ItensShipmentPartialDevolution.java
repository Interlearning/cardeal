package br.com.cardeal.model;

import br.com.cardeal.interfaces.ShipmentDevolutionInterface;

public class ItensShipmentPartialDevolution implements ShipmentDevolutionInterface
{
	private Product product;	
	private double quantidade;
	private double tara;
	private double pesoLiq;
	private double pesoBruto;
	private String destino;
	private String serial;
	private ShipmentItensDevolutionDetails shipmentItensDevolutionDetails;
	private boolean checked = false;
	private PurchaseOrderItem purchaseOrderItem;
			
	public double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	public double getTara() {
		return tara;
	}
	public void setTara(double tara) {
		this.tara = tara;
	}
	public double getPesoLiq() {
		return pesoLiq;
	}
	public void setPesoLiq(double pesoLiq) {
		this.pesoLiq = pesoLiq;
	}
	public double getPesoBruto() {
		return pesoBruto;
	}
	public void setPesoBruto(double pesoBruto) {
		this.pesoBruto = pesoBruto;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public PurchaseOrderItem getPurchaseOrderItem() {
		return purchaseOrderItem;
	}
	public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
		this.purchaseOrderItem = purchaseOrderItem;
	}
	public ShipmentItensDevolutionDetails getShipmentItensDevolutionDetails() {
		return shipmentItensDevolutionDetails;
	}
	public void setShipmentItensDevolutionDetails(ShipmentItensDevolutionDetails shipmentItensDevolutionDetails) {
		this.shipmentItensDevolutionDetails = shipmentItensDevolutionDetails;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
}
