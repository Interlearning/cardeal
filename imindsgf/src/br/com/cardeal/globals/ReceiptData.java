package br.com.cardeal.globals;

import java.util.List;

import br.com.cardeal.model.ItensReceipt;
import br.com.cardeal.model.ItensShipmentDevolution;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.Stock;

public class ReceiptData {
	
	public static final int ACTION_SAVE_ORDER = 1;
	public static final int ACTION_SAVE_ORDER_TO_DEVOLUTION = 2;
	public static final int ACTION_REVERSE_ORDER = 3;
	public static final int ACTION_DELETE_ITEM = 4;
	public static final int ACTION_ORDER_FINISHED = 5;
	public static final int ACTION_FINISHED_ORDER_TO_DEVOLUTION = 6;
	public static final int ACTION_WAITING_FOR_WEIGHT = 7;
	public static final int ACTION_WAITING_FOR_WEIGHT_FINISHED = 8;
	public static final int ACTION_SAVE_ORDER_FROM_WAITING_FOR_WEIGHT = 9;
	public static final int ACTION_WAITING_FOR_WEIGHT_MANUAL = 10;
		
	private PurchaseOrder purchaseOrder;
	private int action;
	private ItensReceipt itemRowManut;
	private ItensShipmentDevolution itemShipmentDevolutionRowManut;
	private List<ItensReceipt> itensGrid;
	private List<Stock> stocksDeleted;
	private Object object;
			
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
	
	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public int getAction() {
		return action;
	}
	
	public void setAction(int action) {
		this.action = action;
	}

	public ItensReceipt getItemRowManut() {
		return itemRowManut;
	}

	public void setItemRowManut(ItensReceipt itemRowManut) {
		this.itemRowManut = itemRowManut;
	}

	public List<ItensReceipt> getItensGrid() {
		return itensGrid;
	}

	public void setItensGrid(List<ItensReceipt> itensGrid) {
		this.itensGrid = itensGrid;
	}

	public List<Stock> getStocksDeleted() {
		return stocksDeleted;
	}

	public void setStocksDeleted(List<Stock> stocksDeleted) {
		this.stocksDeleted = stocksDeleted;
	}

	public ItensShipmentDevolution getItemShipmentDevolutionRowManut() {
		return itemShipmentDevolutionRowManut;
	}

	public void setItemShipmentDevolutionRowManut(ItensShipmentDevolution itemShipmentDevolutionRowManut) {
		this.itemShipmentDevolutionRowManut = itemShipmentDevolutionRowManut;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}

