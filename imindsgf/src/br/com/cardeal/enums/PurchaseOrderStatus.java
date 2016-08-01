package br.com.cardeal.enums;

public enum PurchaseOrderStatus {
		
	STARTED_PURCHASE_ORDER(0, "Iniciado Pedido de Compras"),
	FINISHED_PURCHASE_ORDER(1, "Pedido de Compras Finalizado"),
	STARTED_RETURNED(2, "Iniciado Devolução"),
	FINISHED_RETURNED(3, "Devolução Finalizada");

	private final int id;
	private final String name;
	PurchaseOrderStatus(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
}
