package br.com.cardeal.scale;

public interface ScaleProtocol {
	public boolean open();
	public void close();
	public boolean isConnected();
	public void read();
	public void zero();
	public void tare();
	public void tare(double value);
}
