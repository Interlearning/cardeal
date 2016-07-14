package br.com.cardeal.scale;

public enum ScaleModel {
	
	NONE(0, "Nenhuma"),
	SIMULATOR(1, "Simulador"),
	TOLEDO(2, "Toledo"),
	ALFA(3, "Alfa");

	private final int id;
	private final String name;
	ScaleModel(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
}
