package br.com.cardeal.model;

/** 
 Classe auxiliar responsavel por:
 - Alimentar o Pdf com contéudo
 - Informar qual é o alinhamento do contéudo

@author		Wesley Pinheiro
@version	1.0
@since		25/07/2016
@see		BuildPdf.java
*/

public class DadosPdf {
	
	private String conteudo;
	private int alinhamento;
	
	public DadosPdf(String conteudo, int alinhamento){
		this.conteudo = conteudo;
		this.alinhamento = alinhamento;		
	}
		
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public int getAlinhamento() {
		return alinhamento;
	}
	public void setAlinhamento(int alinhamento) {
		this.alinhamento = alinhamento;
	}	

}
