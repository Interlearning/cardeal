package br.com.cardeal.enums;

public enum ProcessWorker {
	RECEBIMENTO ("Recebimento")
	,EXPEDICAO ("Expedição")
	,EMBALAGEM_EMBALA ("Embalagem")
	,EMBALAGEM_TRANSITO ("Estoque Trânsito")
	,EMBALAGEM_INSUMO ("Estoque Insumo")
	,EMBALAGEM_REPALETIZAR ("Repalletizar")
	,EMBALAGEM_PALLET_VIRTUAL ("Palete Virtual")
	,ABRECAIXA ("Abre Caixa")
	,PRODUCAO_DIARIA ("Relatório - Produção Diária")
	,EXCECAO_PESO_PADRAO ("Exceção Peso Padrão")
	,RECEBIMENTO_COMPRAS ("Compras")
	,RECEBIMENTO_DEVOLUCAO ("Devolução de Expedição")
	;
	
	private final String name;
	
	ProcessWorker(String name) { 		
		this.name = name;		
	}
	
	public String getName() {
		return name;
	}
	
}
