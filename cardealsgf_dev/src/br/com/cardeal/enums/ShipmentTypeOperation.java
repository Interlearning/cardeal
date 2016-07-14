package br.com.cardeal.enums;

public enum ShipmentTypeOperation {
	LEITOR("1", "LEITOR"),
	MANUAL("5", "MANUAL"),
	PALLET("3", "PALLET"),
	BALANCA("6", "COM BALANÇA");
		
	private final String id;
	private final String desc;
	
	ShipmentTypeOperation(String id, String descricao){ 		
		this.id = id;
		desc = descricao;		
	}
	
	public String getId(){ 
		return id; 
	}
	
	public String getDescricao(){ 
		return desc; 
	}
	
}
