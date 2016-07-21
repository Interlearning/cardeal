package br.com.cardeal.model;

import br.com.cardeal.globals.NumberUtils;

public class StockTotalReport {
	
	private Product product;
	
	private long primaryQty = 0;

	private long secondaryQty = 0;
	
	private long totEmb = 0;
	
	private double net = 0.0;
	
	private double tare = 0.0;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public long getPrimaryQty() {
		return primaryQty;
	}

	public void setPrimaryQty(long primaryQty) {
		this.primaryQty = primaryQty;
	}

	public long getSecondaryQty() {
		return secondaryQty;
	}

	public void setSecondaryQty(long secondaryQty) {
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

	public long getTotEmb() {
		return totEmb;
	}

	public void setTotEmb(long totEmb) {
		this.totEmb = totEmb;
	}
	
}
