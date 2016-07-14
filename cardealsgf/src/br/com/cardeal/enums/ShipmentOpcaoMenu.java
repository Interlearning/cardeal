package br.com.cardeal.enums;

public enum ShipmentOpcaoMenu {
	FIFO ("Fifo"),
	MATERIA_PRIMA ("Matéria-Prima"),
	ETIQUETA ("Etiqueta"),
	CANCELAR ("Cancelar");
	
	private String titulo;
	
	ShipmentOpcaoMenu ( String titulo ){
		this.setTitulo(titulo);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
