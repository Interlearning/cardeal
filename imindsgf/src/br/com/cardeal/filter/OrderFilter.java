package br.com.cardeal.filter;

import java.util.Date;

import br.com.cardeal.enums.OrderStatus;


public class OrderFilter {
	
	private long id;
	private long partnerId;
	private String idOrderImportDe;
	private String idOrderImportAte;
	private Date date1;	
	private Date date2;
	private OrderStatus orderStatus;
	
	
	public Date getDate1() {
		return date1;
	}
	public void setDate1(Date date1) {
		this.date1 = date1;
	}
	public Date getDate2() {
		return date2;
	}
	public void setDate2(Date date2) {
		this.date2 = date2;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}
	public String getIdOrderImportDe() {
		return idOrderImportDe;
	}
	public void setIdOrderImportDe(String idOrderImportDe) {
		this.idOrderImportDe = idOrderImportDe;
	}
	public String getIdOrderImportAte() {
		return idOrderImportAte;
	}
	public void setIdOrderImportAte(String idOrderImportAte) {
		this.idOrderImportAte = idOrderImportAte;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
}
