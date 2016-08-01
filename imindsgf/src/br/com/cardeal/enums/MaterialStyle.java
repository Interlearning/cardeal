package br.com.cardeal.enums;

public enum MaterialStyle {
	UNDEFINED(0, "Todos", "T"),
	SEMI_FINISHED(1, "Semi-Acabado", "S"),
	FINISHED(2, "Acabado", "E");
	//RAW_MATERIAL(1, "Indústria", "I"),
	//GRANEL(4, "Granel", "G");

	private final int id;
	private final String name;
	private final String sigla;
	
	MaterialStyle(int id, String name, String sigla) { this.id = id; this.name = name; this.sigla = sigla;}
	
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }
	public String getSigla() { return sigla; }		
}
