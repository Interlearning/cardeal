package br.com.cardeal.model;

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Utils;

public class StockOfPalletModel {

	private boolean checked;
	
	private boolean inFifo;
	
	private long id;

	private Product product;
	
	private int primaryQty;

	private double net;
	
	private Date manufactureDate;

	private Date expirationDate;

	private String batch;

	private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getUnitDesc() {
		if (product != null && product.getUnit().getId() != null
				&& product.getUnit().getId().length() > 0)
			return product.getUnit().getId();
		else
			return "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
	
	public double getNet() {
		return net;
	}
	
	public String getNetFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(net, 3), 14, 3, false, true);
	}
	
	

	public void setNet(double net) {
		this.net = net;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getFifoDesc() {
		if (inFifo)
			return GuiGlobals.getBundle().getString("STR000011");
		else
			return GuiGlobals.getBundle().getString("STR000012");
	}

	public Date getManufactureDate() {
		return manufactureDate;
	}

	public String getManufactureDateFormat() {
		return DateTimeUtils.getDate(manufactureDate, "dd/MM/yyyy");
	}

	public String getManufactureDateTimeFormat() {
		return DateTimeUtils.getDate(manufactureDate, "dd/MM/yyyy HH:mm");
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getExpirationDateFormat() {
		return DateTimeUtils.getDate(expirationDate, "dd/MM/yyyy");
	}

	public String getExpirationDateTimeFormat() {
		return DateTimeUtils.getDate(expirationDate, "dd/MM/yyyy HH:mm");
	}

	public String getIdFormatSerial() {
		return Utils.formatSerial(id);
	}

	public String getIdFormated() {
		return StringUtils.leftPad( String.valueOf(id), 8);
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
