package br.com.cardeal.enums;

public enum TypeOperation {

	MANUAL(2, "5", "MANUAL"),	
	BALANCA_MANUAL(1, "6", "BALANÇA MANUAL"),
	BALANCA_AUTO(0, "6", "BALANÇA AUTO");
	
	
	private final int valor;
	private final String initial;
	private final String desc;
	
	TypeOperation(int valorOpcao, String initialOpcao, String descricao){ 
		valor = valorOpcao;
		initial = initialOpcao;
		desc = descricao;
		
	}
	public int getValor(){ 
		return valor; }
	
	public String getInitial(){ 
		return initial; 
	}
	
	public String getDescricao(){ 
		return desc; 
	}
	
	public static Object[] getElementsInArray() {
		
		TypeOperation[] tipoOperacao = TypeOperation.values();
		Object[][] data = new Object[tipoOperacao.length][3];

		for (int i = 0; i < tipoOperacao.length; i++){
			data[i][0] = tipoOperacao[i].getInitial();
			data[i][1] = tipoOperacao[i].getDescricao();
			data[i][2] = tipoOperacao[i];
		}
		
		return data;
	}
	
	public static TypeOperation getElementByInitial( String initial )
	{
		TypeOperation[] types = TypeOperation.values();		
		String initialParam = initial.trim().toUpperCase();
		
		for ( TypeOperation typeOperation : types ){
			
			if ( typeOperation.getInitial().trim().toUpperCase().equals( initialParam ) ) return typeOperation;
			
		}
		
		return null;
	}
	
	public static TypeOperation getElementByDescription( String description )
	{
		
		TypeOperation[] types = TypeOperation.values();		
		description = description.trim().toUpperCase();
		
		for ( TypeOperation typeOperation : types ){
			
			if ( typeOperation.getDescricao().trim().toUpperCase().equals( description ) ) return typeOperation;
			
		}
		
		return null;
		
	}
	
}
