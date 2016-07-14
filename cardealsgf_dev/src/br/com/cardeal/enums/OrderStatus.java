package br.com.cardeal.enums;

public enum OrderStatus {
	
	ALLORDERS(0, "Todos"),
	NOT_STARTED(1, "Não Iniciado"),
	STARTED(2, "Iniciado"),
	FINISHED(3, "Finalizado"),
	CANCELED(4, "Cancelado");
	

	private final int id;
	private final String name;
	OrderStatus(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
}
