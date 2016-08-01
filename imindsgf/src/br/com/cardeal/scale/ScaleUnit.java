package br.com.cardeal.scale;

public enum ScaleUnit {
	
	KG(1, "Kg"),
	G(2, "g");

	private final int id;
	private final String name;
	ScaleUnit(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
}
