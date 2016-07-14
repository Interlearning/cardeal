package br.com.cardeal.enums;

import java.util.ArrayList;
import java.util.List;

public enum TypeStock 
{
	TODOS 
	(0, "","","Todos", false, false),						
	MATERIA_PRIMA 
	(1, "MP", "I","Matéria Prima", true, true),		
	EMBALAGEM 
	(2, "EA","E","Acabado", false, true),				
	GRANEL 
	(3, "EG","G","Granel", true, false),    				
	INGREDIENTES 
	(4, "IN","I","Ingredientes", true, true),		
	MATERIAL_SECUNDARIO 
	(5, "MS","I","Material Secundário", true, true),	
	MATERIAL_EMBALAGEM 
	(6, "ME","I","Material de Embalagem", true, true),
	TRANSITO 
	(7, "ET","I","Trânsito", true, false);

	private String sigla;
	private String destinoDoc;
	private String descricao;
	private boolean showInManutenStock;
	private boolean showInProducts;
	private int id;

	TypeStock( int id, String sigla, String destinoDoc, String descricao, boolean showInManutenStock, boolean showInProducts)
	{
		this.setSigla(sigla);
		this.setDestinoDoc(destinoDoc);
		this.setDescricao(descricao);
		this.setShowInManutenStock(showInManutenStock);
		this.setShowInProducts(showInProducts);
		this.setId(id);
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static List<TypeStock> getValuesToProducts() 
	{
		List<TypeStock> list = new ArrayList<TypeStock>();
		TypeStock[] typeStocks = values();
		
		for ( int i=0; i < typeStocks.length; i++ )
		{
			if ( typeStocks[i].isShowInProducts() )
			{
				list.add(typeStocks[i]);
			}
		}
		
		return list;
	}
	
	public static List<TypeStock> getValuesToManutenStock() 
	{
		List<TypeStock> list = new ArrayList<TypeStock>();
		TypeStock[] typeStocks = values();
		
		for ( int i=0; i < typeStocks.length; i++ )
		{
			if ( typeStocks[i].isShowInManutenStock() )
			{
				list.add(typeStocks[i]);
			}
		}
		
		return list;
	}

	public boolean isShowInManutenStock() {
		return showInManutenStock;
	}

	public void setShowInManutenStock(boolean showInManutenStock) {
		this.showInManutenStock = showInManutenStock;
	}
	
	public boolean isShowInProducts() {
		return showInProducts;
	}

	public void setShowInProducts(boolean showInProducts) {
		this.showInProducts = showInProducts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static TypeStock findByName(String nameTypeStock) 
	{
		TypeStock[] types = TypeStock.values();
		TypeStock typeReturn = null;
         
        for ( int i = 0; i < types.length; i++)
        {
            if ( nameTypeStock.equalsIgnoreCase( types[i].getDescricao() ) )
            {
            	typeReturn = types[i];
                break;
            }
        }
        
        return typeReturn;
	}

	public String getDestinoDoc() {
		return destinoDoc;
	}

	public void setDestinoDoc(String destinoDoc) {
		this.destinoDoc = destinoDoc;
	}
}
