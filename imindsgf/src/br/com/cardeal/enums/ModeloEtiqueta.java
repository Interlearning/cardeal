package br.com.cardeal.enums;

public enum ModeloEtiqueta {
	CAIXA("Caixa"),
	PALLET("Pallet");
	
	private final String name;
	ModeloEtiqueta(String name) { this.name = name;}	
	public String toString() { return name; }
	public String getName() { return name; }		
}
