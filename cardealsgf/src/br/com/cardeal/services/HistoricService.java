package br.com.cardeal.services;

import java.util.Date;
import br.com.cardeal.globals.GuiGlobals;

public class HistoricService {
			
	public HistoricService() {
		super();	
	}

	public long getMaxIdStockHistoricRollBackByDate(Date date){
		return GuiGlobals.getDaoFactory().getHistoricDao().getMaxIdStockHistoricRollBackByDate(date);
	}
		
}
