package br.com.cardeal.model;

import java.util.Date;

import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.NumberUtils;

public class ItensReceipt 
{
	private Product product;	
	private int quantidade;
	private double tara;
	private double pesoLiq;
	private String batchExternal;
	private Date dateValidateBatchExternal;
			
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public double getTara() {
		return tara;
	}
	public double getTaraRounded() {
		return NumberUtils.roundNumber(tara, 3);
	}
	public void setTara(double tara) {
		this.tara = tara;
	}
	public String getTaraFormatted() {		
		return NumberUtils.transform(NumberUtils.truncate(tara, 3), 3, 3, false, false);
	}
	public double getPesoLiq() {
		return pesoLiq;
	}
	public double getPesoLiqRounded() {
		return NumberUtils.roundNumber(tara, 3);
	}
	public String getPesoLiqFormatted() {		
		return NumberUtils.transform(NumberUtils.truncate(pesoLiq, 3), 3, 3, false, false);
	}
	public void setPesoLiq(double pesoLiq) {
		this.pesoLiq = pesoLiq;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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
	public void setDateValidateBatchExternal(Date dateValidateBatchExternal) {
		this.dateValidateBatchExternal = dateValidateBatchExternal;
	}
	public Object getDateValidateBatchExternalFormatted() 
	{
		return DateTimeUtils.formatDate( dateValidateBatchExternal );
	}
	
			
}
