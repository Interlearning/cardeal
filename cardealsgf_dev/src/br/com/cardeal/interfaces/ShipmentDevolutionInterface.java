package br.com.cardeal.interfaces;

import br.com.cardeal.model.Product;
import br.com.cardeal.model.PurchaseOrderItem;

public interface ShipmentDevolutionInterface 
{
	void setChecked( boolean check );
	boolean isChecked();
	Product getProduct();
	PurchaseOrderItem getPurchaseOrderItem();
}
