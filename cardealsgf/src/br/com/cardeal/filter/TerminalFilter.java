package br.com.cardeal.filter;

import br.com.cardeal.enums.StockStyle;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.model.Company;

public class TerminalFilter {
	
	private String text;	
	private StockStyle stockStyle = StockStyle.UNDEFINED;
	private WeighingStyle packStyle = WeighingStyle.UNDEFINED;	
	private Company company;
	
	public WeighingStyle getPackStyle() {
		return packStyle;
	}
	public void setPackStyle(WeighingStyle packStyle) {
		this.packStyle = packStyle;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public StockStyle getStockStyle() {
		return stockStyle;
	}
	public void setStockStyle(StockStyle stockStyle) {
		this.stockStyle = stockStyle;
	}
	public WeighingStyle getWeighingStyle() {
		return packStyle;
	}
	public void setWeighingStyle(WeighingStyle packStyle) {
		this.packStyle = packStyle;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
}
