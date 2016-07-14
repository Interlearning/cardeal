package br.com.cardeal.printer;

public enum PrinterModel {
	
	NONE(0, "Nenhuma"),
	INTERMEC(1, "Intermec"),
	ZEBRA(2, "Zebra"),
	SIMULATOR(3, "Simulador");

	private final int id;
	private final String name;
	PrinterModel(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
	
}
