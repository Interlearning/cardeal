package br.com.cardeal.enums;

public enum TypeReceipt {
	
	COMPRA(0, "C", "COMPRA"),
	DEVOLUCAO(1, "D", "DEVOLUÇÃO DE PEDIDO");
	
	private final int valor;
	private final String initial;
	private final String desc;
	
	TypeReceipt(int valorOpcao, String initialOpcao, String descricao){ 
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
		
		TypeReceipt[] tipoRecbimento = TypeReceipt.values();
		Object[][] data = new Object[tipoRecbimento.length][3];

		for (int i = 0; i < tipoRecbimento.length; i++){
			data[i][0] = tipoRecbimento[i]. getInitial();
			data[i][1] = tipoRecbimento[i].getDescricao();
			data[i][2] = tipoRecbimento[i];
		}
		
		return data;
	}
	
}
