package br.com.cardeal.enums;


public enum TypeExportReport {

	PDF(1, "pdf"),
	EXCEL_CSV(2, "csv"),
	EXCEL_XLS(3, "xls");

	private final int id;
	private final String extensao;
	TypeExportReport(int id, String extensao) { this.id = id; this.extensao = extensao;}
	public int getValue() { return id; }	
	public String getExtensao() { return extensao; }	
	
}
