package br.com.cardeal.enums;

public enum LayoutProduto {
	
	CODIGO_PRODUTO (1),
	NOME_PRODUTO(2),
	FIFO(3),
	TIPO_PESAGEM(4),
	VIRTUAL(5),
	UM_VENDA(6),
	UM_ETIQUETA(7),
	QUANTIDADE_POR_EMBALAGEM(8),
	PESO_LIQUIDO(9),
	TARA_PACOTE(10),
	TARA_CAIXA(11),
	PESO_PADRAO(13),
	DIAS_VALIDADE(14),
	QTDE_EMB_FECHAMENTO_PALLET(16),
	DESCRICAO_CONSERVACAO(17),
	DESCRICAO_SIF(18),
	EAN_13 (19),
	DUN_14 (20),
	TIPO_ESTOQUE(21),
	ALTERA_PECAS(22),
	ALTERA_ETIQUETA(23),
	PESO_MINIMO(25),
	PESO_MAXIMO(26),
	CODIGO_SIF(27),
	PERCENTUAL_LIBERA_BALANCA(28),
	PREFIXO_GS1(29),
	SEQUENCIA_PROCESSO(30);
	
	private int posicao;
	
	private LayoutProduto( int posicao ) {
		this.posicao = posicao;
	}

	public int getPosicao() {
		return posicao;
	}

}
