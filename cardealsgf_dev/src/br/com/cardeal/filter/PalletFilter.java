package br.com.cardeal.filter;

import java.util.Date;
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.model.Product;

public class PalletFilter {
	
	private int id;
	private Date openDate;
	private Date closeDate;
	private String terminalId;
	private PalletStatus status;
	private Product product;
	private double tare;  //peso da plataforma do pallet
	private double strech; //peso do strech + cantoneiras
	private boolean enabledToShowBlocked = true;
	
	public boolean isEnabledToShowBlocked() {
		return enabledToShowBlocked;
	}
	
	public void setEnabledToShowBlocked(boolean enabledToShowBlocked) {
		this.enabledToShowBlocked = enabledToShowBlocked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public PalletStatus getStatus() {
		return status;
	}

	public void setStatus(PalletStatus status) {
		this.status = status;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getTare() {
		return tare;
	}

	public void setTare(double tare) {
		this.tare = tare;
	}

	public double getStrech() {
		return strech;
	}

	public void setStrech(double strech) {
		this.strech = strech;
	}
	
}	
