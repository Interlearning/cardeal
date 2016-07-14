package br.com.cardeal.model;

import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.NumberUtils;

public class StockManuten 
{
	private int idProduct;
	private Product product;
	private double currentQuantity;
	private double quantityChange;
	private int currentPrimaryQty;
	private int quantityChangePrimaryQty;
	private int currentQtyBox;
	private int quantityChangeBox;
	private TypeStock typeStock;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getCurrentQuantity() {
		return currentQuantity;
	}
	public void setCurrentQuantity(double currentQuantity) {
		this.currentQuantity = NumberUtils.roundNumber(currentQuantity, 3);
	}
	public double getQuantityChange() {
		return quantityChange;
	}
	public void setQuantityChange(double quantityChange) {
		this.quantityChange = quantityChange;
	}
	public TypeStock getTypeStock() {
		return typeStock;
	}
	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}
	public int getIdProduct() {
		return idProduct;
	}
	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}
	public int getQuantityChangePrimaryQty() {
		return quantityChangePrimaryQty;
	}
	public void setQuantityChangePrimaryQty(int quantityChangePrimaryQty) {
		this.quantityChangePrimaryQty = quantityChangePrimaryQty;
	}
	public int getQuantityChangeBox() {
		return quantityChangeBox;
	}
	public void setQuantityChangeBox(int quantityChangeBox) {
		this.quantityChangeBox = quantityChangeBox;
	}
	public int getCurrentPrimaryQty() {
		return currentPrimaryQty;
	}
	public void setCurrentPrimaryQty(int currentPrimaryQty) {
		this.currentPrimaryQty = currentPrimaryQty;
	}
	public int getCurrentQtyBox() {
		return currentQtyBox;
	}
	public void setCurrentQtyBox(int currentQtyBox) {
		this.currentQtyBox = currentQtyBox;
	}
	
}
