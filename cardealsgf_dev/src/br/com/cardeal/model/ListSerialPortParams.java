package br.com.cardeal.model;

public class ListSerialPortParams {

	private int param;
	private String description;
	
	public ListSerialPortParams( int param, String description){
		this.param = param;
		this.description = description;
	}
	
	public int getParam() {
		return param;
	}
	public void setParam(int param) {
		this.param = param;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}