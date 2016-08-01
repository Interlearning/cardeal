package br.com.cardeal.enums;

public enum Operation {
	UNDEFINED(0, "Todas"),
	PACKING(1, "Embalagem"),
	RECEIVING(2, "Recebimento"),
	SALE(3, "Expedi��o"),
	PRODUCTION_APPOINTMENT(4, "Apontamento de Produ��o"),
	STOCK_REMOVING(5, "Baixa Manual de Estoque"),
	PACK_DECOMPOSE(6, "Decompor Embalagem"),
	STOCK_ROLLBACK(7, "Estorno de embalagem"),
	STOCK_MANUAL_INCLUDE(8, "Inclus�o manual de estoque"),
	STOCK_MANUAL_CHANGE(9, "Altera��o manual de estoque"),
	PALLET_REMOVING(10, "Baixa Manual de Pallet"),
	PALLET_OPEN(11, "Abertura de Pallet"),
	PALLET_CLOSED(12, "Fechamento de Pallet"),
	PALLET_UPDATE(13, "Altera��o de informa��es do Pallet"),
	OPERATION_OPEN_BOX(14, "Opera��o abre caixa"),
	SHIPMENT_ITEM(15, "Expedi��o de item"),
	SHIPMENT_ITEM_ROLLBACK(16, "Expedi��o - Estorno de item"), 
	COMPLEMENTO_GRANEL(17, "Expedi��o - Complemento de item"), 
	COMPLEMENTO_GRANEL_ROWBACK(18, "Expedi��o - Complemento de item estornado"), 
	SHIPMENT_STOCK_REMOVING_MP(19, "Expedi��o - Expedindo MAT�RIA-PRIMA"),
	SHIPMENT_STOCK_REMOVING(20, "Expedi��o - Baixa de estoque"),
	SHIPMENT_STOCK_ROLLBACK(21, "Expedi��o - Estorno de item"),
	SHIPMENT_FINISHED_ORDER(22, "Expedi��o - Fechamento de pedido"),
	STOCK_REMOVING_MP(23, "Baixa manual de mat�ria-prima"),
	PARTNER_REMOVING(24, "Exclus�o de Cliente/Fornecedor"),
	PARTNER_UPDATE(25, "Altera��o de informa��es do Cliente/Fornecedor"),
	PARTNER_INCLUDE_BY_IMPORT(26, "Inclus�o de Cliente/Fornecedor autom�tica via importa��o de pedido."),
	PARTNER_INCLUDE(27, "Inclus�o de Cliente/Fornecedor"),
	RECEIPT_DEVOLUTION(28, "Devolu��o pelo m�dulo de Recebimento"),
	SHIPMENT_STOCK_CANCEL(29, "Cancelamento de pedido da expedi��o"),
	SHIPMENT_DELETE_ORDER(30, "Exclus�o de pedido da expedi��o"), 
	CHANGE_STOCK_KG (31, "Ajuste de Estoque"), 
	SHIPMENT_STOCK_REMOVING_MP_ROLLBACK (32, "Expedi��o - Estorno de item MAT�RIA-PRIMA"),
	RECEIPT_PURCHASE (33, "Recebimento - Compras"),
	;

	private final int id;
	private final String name;
	Operation(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
}
