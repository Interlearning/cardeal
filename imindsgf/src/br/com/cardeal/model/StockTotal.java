package br.com.cardeal.model;

import br.com.cardeal.globals.NumberUtils;

public class StockTotal {
	
	private Product product;
	
	private int primaryQty = 0;

	private int secondaryQty = 0;
	
	private int totEmb = 0;
	
	private double net = 0.0;
	
	private double tare = 0.0;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getPrimaryQty() {
		return primaryQty;
	}

	public void setPrimaryQty(int primaryQty) {
		this.primaryQty = primaryQty;
	}

	public int getSecondaryQty() {
		return secondaryQty;
	}

	public void setSecondaryQty(int secondaryQty) {
		this.secondaryQty = secondaryQty;
	}

	public double getNet() {
		return net;
	}
	
	public String getNetFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(net, 3), 14, 3, false, false);
	}

	public void setNet(double net) {
		this.net = net;
	}

	public double getTare() {
		return tare;
	}

	public void setTare(double tare) {
		this.tare = tare;
	}

	public int getTotEmb() {
		return totEmb;
	}

	public void setTotEmb(int totEmb) {
		this.totEmb = totEmb;
	}
	
}
