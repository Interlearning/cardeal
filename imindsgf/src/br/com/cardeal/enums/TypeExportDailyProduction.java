package br.com.cardeal.enums;


public enum TypeExportDailyProduction {

	PDF(1, "pdf"),
	EXCEL(2, "csv");

	private final int id;
	private final String extensao;
	TypeExportDailyProduction(int id, String extensao) { this.id = id; this.extensao = extensao;}
	public int getValue() { return id; }	
	public String getExtensao() { return extensao; }	
	
}
