package br.com.cardeal.model;

public class ItensPalletVirtual {
	
	private String itensPallet;
	private String pesoCaixa;
	private int qtde;	
	private String pesoLiq;
	private String pesoBruto;
	private String totalLiq;
	private String totalBruto;	
	private TypeOperationPalletVirtual type;
	
	public String getItensPallet() {
		return itensPallet;
	}
	public void setItensPallet(String itensPallet) {
		this.itensPallet = itensPallet;
	}
	public String getPesoCaixa() {
		return pesoCaixa;
	}
	public TypeOperationPalletVirtual getType() {
		return type;
	}
	public void setType(TypeOperationPalletVirtual type) {
		this.type = type;
	}
	public void setPesoCaixa(String pesoCaixa) {
		this.pesoCaixa = pesoCaixa;
	}
	public int getQtde() {
		return qtde;
	}
	public void setQtde(int qtde) {
		this.qtde = qtde;
	}
	public String getPesoLiq() {
		return pesoLiq;
	}
	public void setPesoLiq(String pesoLiq) {
		this.pesoLiq = pesoLiq;
	}
	public String getPesoBruto() {
		return pesoBruto;
	}
	public void setPesoBruto(String pesoBruto) {
		this.pesoBruto = pesoBruto;
	}
	public String getTotalLiq() {
		return totalLiq;
	}
	public void setTotalLiq(String totalLiq) {
		this.totalLiq = totalLiq;
	}
	public String getTotalBruto() {
		return totalBruto;
	}
	public void setTotalBruto(String totalBruto) {
		this.totalBruto = totalBruto;
	}
	
	
		
}
