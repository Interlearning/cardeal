package br.com.cardeal.filter;

import java.util.Date;

import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.Utils;

public class StockFilter {
	
	private long id;
	private long id_2;
	private int productId;
	private int productId_2;
	private int palletIdDe;
	private int palletIdAte;
	private String batch;
	private long partnerId;
	private Date enterDateDe;
	private Date enterDateAte;
	private Date manufactureDateDe;	
	private Date manufactureDateAte;
	private Date closeDateDe;
	private Date closeDateAte;
	private boolean onlyStocked = true;
	private boolean onlyFifo = false;
	private String terminalId;
	private String terminalId_2;
	private long idOrderImport;
	private boolean asc = false;	
	private String idMasc;
	private String idMasc_2;
	private String companyIdDe;
	private String companyIdAte;
	private boolean notPallet = false;
	private TypeStock typeStock;
	private String orderBy;
	private int page = 0;
	
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
	public Date getManufactureDateDe() {
		return manufactureDateDe;
	}
	public void setManufactureDateDe(Date manufactureDateDe) {
		this.manufactureDateDe = manufactureDateDe;
	}
	public Date getManufactureDateAte() {
		return manufactureDateAte;
	}
	public void setManufactureDateAte(Date manufactureDateAte) {
		this.manufactureDateAte = manufactureDateAte;
	}	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFormattedId() {
		if ( id > 0)
			return Utils.formatSerial(id);
		else
			return "";
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public String getIdMasc() {
		return idMasc;
	}
	public void setIdMasc(String idMasc) {
		this.idMasc = idMasc;
	}
	
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public long getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}
	public boolean isOnlyStocked() {
		return onlyStocked;
	}
	public void setOnlyStocked(boolean onlyStocked) {
		this.onlyStocked = onlyStocked;
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
	public int getPalletIdDe() {
		return palletIdDe;
	}
	public String getFormattedIdPalletDe() {
		return Utils.formatPallet( palletIdDe );
	}
	public void setPalletIdDe(int palletIdDe) {
		this.palletIdDe = palletIdDe;
	}
	public int getPalletIdAte() {
		return palletIdAte;
	}
	public String getFormattedIdPalletAte() {
		return Utils.formatPallet( palletIdAte );
	}
	public void setPalletIdAte(int palletIdAte) {
		this.palletIdAte = palletIdAte;
	}
	public boolean isOnlyFifo() {
		return onlyFifo;
	}
	public void setOnlyFifo(boolean onlyFifo) {
		this.onlyFifo = onlyFifo;
	}
	public long getIdOrderImport() {
		return idOrderImport;
	}
	public void setIdOrderImport(long idOrderImport) {
		this.idOrderImport = idOrderImport;
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
	public String getFormattedId_2() {
		if ( id_2 > 0)
			return Utils.formatSerial(id_2);
		else
			return "";
	}
	public void setId_2(long id_2) {
		this.id_2 = id_2;
	}
	public int getProductId_2() {
		return productId_2;
	}
	public void setProductId_2(int productId_2) {
		this.productId_2 = productId_2;
	}
	public String getIdMasc_2() {
		return idMasc_2;
	}
	public void setIdMasc_2(String idMasc_2) {
		this.idMasc_2 = idMasc_2;
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
	public Date getCloseDateDe() {
		return closeDateDe;
	}
	public void setCloseDateDe(Date closeDateDe) {
		this.closeDateDe = closeDateDe;
	}
	public Date getCloseDateAte() {
		return closeDateAte;
	}
	public void setCloseDateAte(Date closeDateAte) {
		this.closeDateAte = closeDateAte;
	}
	public boolean isNotPallet() {
		return notPallet;
	}	
	public void setNotPallet(boolean notPallet) {
		this.notPallet = notPallet;
	}
	public TypeStock getTypeStock() {
		return typeStock;
	}
	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
}
