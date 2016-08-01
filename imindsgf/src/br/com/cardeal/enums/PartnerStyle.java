package br.com.cardeal.enums;

public enum PartnerStyle {
	UNDEFINED(0, "Todos"),
	CUSTOMER(1, "Cliente"),
	SUPPLIER(2, "Fornecedor"),
	CUSTOMER_AND_SUPPLIER(3, "Cliente e Fornecedor");

	private final int id;
	private final String name;
	PartnerStyle(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
}
