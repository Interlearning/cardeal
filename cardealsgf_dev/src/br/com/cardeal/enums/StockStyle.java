package br.com.cardeal.enums;

public enum StockStyle {
	UNDEFINED(0, "Todos"),
	DISCRETE(1, "Por Embalagem"),
	BULK(2, "Granel");

	private final int id;
	private final String name;
	StockStyle(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
}
