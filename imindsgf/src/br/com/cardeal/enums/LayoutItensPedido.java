package br.com.cardeal.enums;

public enum LayoutItensPedido {
	
	NUMERO_PEDIDO (0),
	ITEM_PEDIDO (1),
	CODIGO_PRODUTO (2),
	QUANTIDADE_FALTA_EXPEDIR (3),
	QUANTIDADE_EXPEDIDA (4),
	PESO_TOTAL_EXPEDIDO (5),
	UNIDADE_MEDIDA (6),
	DATA_MINIMA (7),
	DATA_MAXIMA (8),
	FAIXA_DE_PESO (9);
	
	private int posicao;
	
	private LayoutItensPedido( int posicao ) {
		this.posicao = posicao;
	}

	public int getPosicao() {
		return posicao;
	}

}
