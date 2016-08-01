package br.com.cardeal.dao;

import java.util.List;

import br.com.cardeal.filter.EtiquetaFilter;
import br.com.cardeal.model.Etiqueta;

public interface EtiquetaDao {

	Etiqueta find(int id);

	void add(Etiqueta etiqueta);
	
	void refresh(Etiqueta etiqueta);
	
	void update(Etiqueta etiqueta);
	
	List<Etiqueta> list();
	
	String delete(Etiqueta etiqueta);

	void initCadLabels();

	List<Etiqueta> list(EtiquetaFilter filter);

	Etiqueta findByNameArq(String etiquetaPallet);

}