package br.com.cardeal.filter;

import java.util.Date;

import br.com.cardeal.enums.TypeStock;


public class StockFilterManuten {
	
	private long id;
	private long id_2;
	private String productIdMasc;
	private String productIdMasc_2;	
	private Date enterDateDe;	
	private Date enterDateAte;	
	private Date manufactureDateDe;	
	private Date manufactureDateAte;
	private String companyIdDe;
	private String companyIdAte;
	private String terminalId;
	private String terminalId_2;	
	private boolean asc = false;
	private TypeStock typeStock;
	private String motivo;
	
	public Date getEnterDateDe() {
		return enterDateDe;
	}
	public void setEnterDateDe(Date enterDateDe) {
		this.enterDateDe = enterDateDe;
	}
	public Date getEnterDateAte() {
		return enterDateAte;
	}
	public void setEnterDateAte(Date enterDateAte) {
		this.enterDateAte = enterDateAte;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}		
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getTerminalId_2() {
		return terminalId_2;
	}
	public void setTerminalId_2(String terminalId_2) {
		this.terminalId_2 = terminalId_2;
	}	
	public boolean isAsc() {
		return asc;
	}
	public void setAsc(boolean asc) {
		this.asc = asc;
	}
	public long getId_2() {
		return id_2;
	}
	public void setId_2(long id_2) {
		this.id_2 = id_2;
	}
	public String getProductIdMasc() {
		return productIdMasc;
	}
	public void setProductIdMasc(String productIdMasc) {
		this.productIdMasc = productIdMasc;
	}
	public String getProductIdMasc_2() {
		return productIdMasc_2;
	}
	public void setProductIdMasc_2(String productIdMasc_2) {
		this.productIdMasc_2 = productIdMasc_2;
	}
	public TypeStock getTypeStock() {
		return typeStock;
	}
	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}
	public Date getManufactureDateDe() {
		return manufactureDateDe;
	}
	public void setManufactureDateDe(Date manufactureDateDe) {
		this.manufactureDateDe = manufactureDateDe;
	}
	public Date getManufactureDateAte() {
		return manufactureDateAte;
	}
	public void setManufactureDateAte(Date manufactureAte) {
		this.manufactureDateAte = manufactureAte;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getCompanyIdDe() {
		return companyIdDe;
	}
	public void setCompanyIdDe(String companyIdDe) {
		this.companyIdDe = companyIdDe;
	}
	public String getCompanyIdAte() {
		return companyIdAte;
	}
	public void setCompanyIdAte(String companyIdAte) {
		this.companyIdAte = companyIdAte;
	}
	
	
}
