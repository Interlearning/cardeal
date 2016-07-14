package br.com.cardeal.enums;

public enum WeighingStyle {
	UNDEFINED(0, "Todos"),
	VARIABLE_WEIGHT(1, "Peso Variável"),
	STANDARD_WEIGHT(2, "Peso Padrão");

	private final int id;
	private final String name;
	WeighingStyle(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
}
