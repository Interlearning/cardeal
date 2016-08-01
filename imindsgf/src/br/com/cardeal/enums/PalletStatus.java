package br.com.cardeal.enums;

public enum PalletStatus {
	OPEN ("Em montagem")
	, CLOSED ("Fechado")
	, DELETED ("Excluído")
	, REPALLET ("Repalletizado")
	;
		
	private final String desc;
	PalletStatus(String desc) { this.desc = desc;}	
	public String toString() { return desc; }
	public String getDesc() { return desc; }
	
}
