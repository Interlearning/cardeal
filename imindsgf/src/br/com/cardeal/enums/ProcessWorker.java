package br.com.cardeal.enums;

public enum ProcessWorker {
	RECEBIMENTO ("Recebimento")
	,EXPEDICAO ("Expedi��o")
	,EMBALAGEM_EMBALA ("Embalagem")
	,EMBALAGEM_TRANSITO ("Estoque Tr�nsito")
	,EMBALAGEM_INSUMO ("Estoque Insumo")
	,EMBALAGEM_REPALETIZAR ("Repalletizar")
	,EMBALAGEM_PALLET_VIRTUAL ("Palete Virtual")
	,ABRECAIXA ("Abre Caixa")
	,PRODUCAO_DIARIA ("Relat�rio - Produ��o Di�ria")
	,EXCECAO_PESO_PADRAO ("Exce��o Peso Padr�o")
	,RECEBIMENTO_COMPRAS ("Compras")
	,RECEBIMENTO_DEVOLUCAO ("Devolu��o de Expedi��o")
	;
	
	private final String name;
	
	ProcessWorker(String name) { 		
		this.name = name;		
	}
	
	public String getName() {
		return name;
	}
	
}
