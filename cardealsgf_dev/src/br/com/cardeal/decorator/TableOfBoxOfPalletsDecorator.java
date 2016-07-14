package br.com.cardeal.decorator;

import org.displaytag.decorator.TableDecorator;
import br.com.cardeal.model.StockOfPalletModel;

public class TableOfBoxOfPalletsDecorator extends TableDecorator {
	
	public String getCompany()
    {
		StockOfPalletModel stock = (StockOfPalletModel) this.getCurrentRowObject();
        return stock.getCompany().getId();
    }
	
	public String getId()
    {
		StockOfPalletModel stock = (StockOfPalletModel) this.getCurrentRowObject();
        return stock.getIdFormatSerial();
    }
	
	public String getProduct()
    {
		StockOfPalletModel stock = (StockOfPalletModel) this.getCurrentRowObject();
        return stock.getProduct().getDescription();
    }
	
	public String getUnitDesc()
    {
		StockOfPalletModel stock = (StockOfPalletModel) this.getCurrentRowObject();
        return stock.getUnitDesc();
    }
	
	public String getPrimaryQty()
    {
		StockOfPalletModel stock = (StockOfPalletModel) this.getCurrentRowObject();
        return String.valueOf( stock.getPrimaryQty() );
    }
	
	public String getNet()
    {
		StockOfPalletModel stock = (StockOfPalletModel) this.getCurrentRowObject();
        return String.valueOf( stock.getNet() );
    }
	
	public String getBatch()
    {
		StockOfPalletModel stock = (StockOfPalletModel) this.getCurrentRowObject();
        return stock.getBatch();
    }
	
	public String getManufactureDateFormat()
    {
		StockOfPalletModel stock = (StockOfPalletModel) this.getCurrentRowObject();
        return stock.getManufactureDateFormat();
    }
		
	public String getCheck()
	{
		StockOfPalletModel stock = (StockOfPalletModel) this.getCurrentRowObject();
		String checked = "";
		
		if ( stock.isChecked() ) checked = "checked";
		
		return " <input type='checkbox' name='" + stock.getId() + "' " + checked + "> ";
	}

}
