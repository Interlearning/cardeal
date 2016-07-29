package br.com.cardeal.model;

import java.util.List;

import br.com.cardeal.globals.NumberUtils;

public class StockTotal {
	
	private Product product;
	
	private int primaryQty = 0;

	private int secondaryQty = 0;
	
	private int totEmb = 0;
	
	private double net = 0.0;
	
	private double tare = 0.0;
	
	/* WJSP 26/07/2016
	 * Colocaldo Company na classe para apresentação da Filial no relatório Web Estoque Totalizado
	 * @see private  List<StockTotal> totalize(List<Stock> stocks, Pallet pallet)  DefaultStockDao.java
	 */
	private Company company;

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
	
	// WJSP 26/07/2016
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}	
}
