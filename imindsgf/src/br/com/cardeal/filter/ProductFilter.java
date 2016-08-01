package br.com.cardeal.filter;

import java.io.Serializable;
import br.com.cardeal.enums.StockStyle;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.enums.WeighingStyle;

public class ProductFilter implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -8471889651820716518L;
	private String idMasc;
	private String description;
	private TypeStock typeStock = TypeStock.TODOS;
	private StockStyle stockStyle = StockStyle.UNDEFINED;
	private WeighingStyle packStyle = WeighingStyle.UNDEFINED;
	private boolean enabledToShowBlocked = true;
	private boolean filterToMP;
	
	public WeighingStyle getPackStyle() {
		return packStyle;
	}
	public void setPackStyle(WeighingStyle packStyle) {
		this.packStyle = packStyle;
	}
	public boolean isEnabledToShowBlocked() {
		return enabledToShowBlocked;
	}
	public void setEnabledToShowBlocked(boolean enabledToShowBlocked) {
		this.enabledToShowBlocked = enabledToShowBlocked;
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
	public TypeStock getTypeStock() {
		return typeStock;
	}
	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}
	public boolean isFilterToMP() {
		return filterToMP;
	}
	public void setFilterToMP(boolean filterToMP) {
		this.filterToMP = filterToMP;
	}
	public String getIdMasc() {
		return idMasc;
	}
	public void setIdMasc(String idMasc) {
		this.idMasc = idMasc;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
