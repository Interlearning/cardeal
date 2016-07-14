package br.com.cardeal.model;

import br.com.cardeal.interfaces.ShipmentDevolutionInterface;

public class ItensShipmentDevolution implements ShipmentDevolutionInterface 
{
	
	private Product product;
	private PurchaseOrderItem purchaseOrderItem;
	private double quantidade;
	private String unidade;	
	private double tara;
	private double pesoLiq;
	private double pesoBruto;
	private String destino;
	private Stock stock;
	private boolean checked = false;
			
	public double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidade) {
		this.unidade = unidade;
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
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public PurchaseOrderItem getPurchaseOrderItem() {
		return purchaseOrderItem;
	}
	public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
		this.purchaseOrderItem = purchaseOrderItem;
	}
	
}
