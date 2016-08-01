package br.com.cardeal.globals;

import br.com.cardeal.enums.ShipmentTypeOperation;
import br.com.cardeal.model.ItensShipment;
import br.com.cardeal.model.Product;
import br.com.cardeal.services.ShipmentService;
import br.com.cardeal.views.ShipmentPanel;

public class ShipmentData {
	
	public static final int ACTION_FIFO = 0;
	public static final int ACTION_LABEL_PALLET = 1;
	
	private int action;
	private ShipmentService shipmentService;
	private Product product;
	private double qtdDigitada;
	private ShipmentPanel dlgShipment;
	private ShipmentTypeOperation shipmentTypeOperation; 
	private String codBar;
	private ItensShipment itemSelected;

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public ShipmentService getShipmentService() {
		return shipmentService;
	}

	public void setShipmentService(ShipmentService shipmentService) {
		this.shipmentService = shipmentService;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getQtdDigitada() {
		return qtdDigitada;
	}

	public void setQtdDigitada(double qtdDigitada) {
		this.qtdDigitada = qtdDigitada;
	}

	public ShipmentPanel getDlgShipment() {
		return dlgShipment;
	}

	public void setDlgShipment(ShipmentPanel dlgShipment) {
		this.dlgShipment = dlgShipment;
	}

	public ShipmentTypeOperation getShipmentTypeOperation() {
		return shipmentTypeOperation;
	}

	public void setShipmentTypeOperation(ShipmentTypeOperation shipmentTypeOperation) {
		this.shipmentTypeOperation = shipmentTypeOperation;
	}

	public String getCodBar() {
		return codBar;
	}

	public void setCodBar(String codBar) {
		this.codBar = codBar;
	}

	public void setItemSelected(ItensShipment itemSelected) 
	{
		this.itemSelected = itemSelected;
	}
	
	public ItensShipment getItemSelected()
	{
		return itemSelected;
	}
	
}
