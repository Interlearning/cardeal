package br.com.cardeal.enums;

public enum Operation {
	UNDEFINED(0, "Todas"),
	PACKING(1, "Embalagem"),
	RECEIVING(2, "Recebimento"),
	SALE(3, "Expedição"),
	PRODUCTION_APPOINTMENT(4, "Apontamento de Produção"),
	STOCK_REMOVING(5, "Baixa Manual de Estoque"),
	PACK_DECOMPOSE(6, "Decompor Embalagem"),
	STOCK_ROLLBACK(7, "Estorno de embalagem"),
	STOCK_MANUAL_INCLUDE(8, "Inclusão manual de estoque"),
	STOCK_MANUAL_CHANGE(9, "Alteração manual de estoque"),
	PALLET_REMOVING(10, "Baixa Manual de Pallet"),
	PALLET_OPEN(11, "Abertura de Pallet"),
	PALLET_CLOSED(12, "Fechamento de Pallet"),
	PALLET_UPDATE(13, "Alteração de informações do Pallet"),
	OPERATION_OPEN_BOX(14, "Operação abre caixa"),
	SHIPMENT_ITEM(15, "Expedição de item"),
	SHIPMENT_ITEM_ROLLBACK(16, "Expedição - Estorno de item"), 
	COMPLEMENTO_GRANEL(17, "Expedição - Complemento de item"), 
	COMPLEMENTO_GRANEL_ROWBACK(18, "Expedição - Complemento de item estornado"), 
	SHIPMENT_STOCK_REMOVING_MP(19, "Expedição - Expedindo MATÉRIA-PRIMA"),
	SHIPMENT_STOCK_REMOVING(20, "Expedição - Baixa de estoque"),
	SHIPMENT_STOCK_ROLLBACK(21, "Expedição - Estorno de item"),
	SHIPMENT_FINISHED_ORDER(22, "Expedição - Fechamento de pedido"),
	STOCK_REMOVING_MP(23, "Baixa manual de matéria-prima"),
	PARTNER_REMOVING(24, "Exclusão de Cliente/Fornecedor"),
	PARTNER_UPDATE(25, "Alteração de informações do Cliente/Fornecedor"),
	PARTNER_INCLUDE_BY_IMPORT(26, "Inclusão de Cliente/Fornecedor automática via importação de pedido."),
	PARTNER_INCLUDE(27, "Inclusão de Cliente/Fornecedor"),
	RECEIPT_DEVOLUTION(28, "Devolução pelo módulo de Recebimento"),
	SHIPMENT_STOCK_CANCEL(29, "Cancelamento de pedido da expedição"),
	SHIPMENT_DELETE_ORDER(30, "Exclusão de pedido da expedição"), 
	CHANGE_STOCK_KG (31, "Ajuste de Estoque"), 
	SHIPMENT_STOCK_REMOVING_MP_ROLLBACK (32, "Expedição - Estorno de item MATÉRIA-PRIMA"),
	RECEIPT_PURCHASE (33, "Recebimento - Compras"),
	;

	private final int id;
	private final String name;
	Operation(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }		
}
