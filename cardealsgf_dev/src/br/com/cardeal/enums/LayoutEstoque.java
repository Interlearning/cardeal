package br.com.cardeal.enums;

public enum LayoutEstoque {
	
	NUMERO_CAIXA (0),
	NUMERO_FABRICA (1),
	CODIGO_PRODUTO (2),
	PESO_LIQUIDO (3),
	NUMERO_PECAS (4),
	SOMA_TARAS (5),
	TARA_CAIXA (6),
	DATA_FABRICACAO (7),
	ENDERECO_PALLET (8),
	NUMERO_PALLET (9);
	
	
	
	private int posicao;
	
	private LayoutEstoque( int posicao ) {
		this.posicao = posicao;
	}

	public int getPosicao() {
		return posicao;
	}

}
