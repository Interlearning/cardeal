package br.com.cardeal.importacao.entities;

public class ItensPedido {

	private String numeroPedido;
	private String itemPedido;
	
	public ItensPedido(){
		super();
	}
	
	public String getNumeroPedido() {
		return numeroPedido;
	}
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	public String getItemPedido() {
		return itemPedido;
	}
	public void setItemPedido(String itemPedido) {
		this.itemPedido = itemPedido;
	}
	
}
