package br.com.cardeal.enums;

public enum LayoutCabecalhoPedido {

	NUMERO_PEDIDO (0),
	NOME_CLIENTE (1),
	OBSERVACOES (2),
	PLACA_VEICULO (3),
	NOME_TRANSPORTADORA (4),
	AUTORIZACAO_EMBARQUE (6),
	DATA_MINIMA (7),
	DATA_MAXIMA (8);
	
	private int posicao;
	
	private LayoutCabecalhoPedido( int posicao ) {
		this.posicao = posicao;
	}

	public int getPosicao() {
		return posicao;
	}

}
