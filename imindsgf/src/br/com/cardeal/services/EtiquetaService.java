package br.com.cardeal.services;

import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.Etiqueta;

public class EtiquetaService {
			
	public void adiciona(Etiqueta etiqueta) {
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
			 
			GuiGlobals.getDaoFactory().getEtiquetaDao().add(etiqueta);			
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();			
		}
		
	}
	
	public void altera(Etiqueta etiqueta) {
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
			 
			GuiGlobals.getDaoFactory().getEtiquetaDao().update(etiqueta);			
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();			
		}
		
	}
	
	public void exclui(Etiqueta etiqueta) {
		
		GuiGlobals.getDaoFactory().beginTransaction();
		try {
			 
			GuiGlobals.getDaoFactory().getEtiquetaDao().delete(etiqueta);			
			GuiGlobals.getDaoFactory().commit();
		}
		catch(Exception e) {
			GuiGlobals.getDaoFactory().rollback();
			e.printStackTrace();			
		}
		
	}
	
}
