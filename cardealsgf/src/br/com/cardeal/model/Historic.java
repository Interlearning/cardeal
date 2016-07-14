package br.com.cardeal.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.cardeal.enums.Operation;
import br.com.cardeal.globals.DateTimeUtils;

//Grava as transacoes ocorridas em cada operacao (embalagem, expedicao, etc.)

@Entity
public class Historic {
	
	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private long id;

	@ManyToOne
	private Product product;
	
	@ManyToOne
	private Company company;

	private int primaryQty;
	
	private int secondaryQty;
	
	private double net;
	
	private double tare;
	
	private Date date;
	
	@ManyToOne
	private User user;
	
	@Column(length=18)
	private String batch;
	
	@ManyToOne
	private Partner partner;
	
	@ManyToOne
	private Terminal terminal;
	
	private Operation operation;
	
	@ManyToOne
	private Order order;
	
	@ManyToOne
	private OrderItem orderItem;
	
	@ManyToOne
	private PurchaseOrder purchaseOrder;

	@ManyToOne
	private Stock stock;
	
	@ManyToOne
	private Pallet pallet;
	
	private String observation;
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
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

	public int getSecondaryQty() {
		return secondaryQty;
	}

	public void setSecondaryQty(int secondaryQty) {
		this.secondaryQty = secondaryQty;
	}

	public double getNet() {
		return net;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Pallet getPallet() {
		return pallet;
	}

	public void setPallet(Pallet pallet) {
		this.pallet = pallet;
	}
	
	public String getDateTimeFormat() {
		return DateTimeUtils.getDate( date, "dd/MM/yyyy HH:mm" );
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
}
