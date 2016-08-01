package br.com.cardeal.scale;

public class Scale {
	
	private volatile ScaleStatus status = ScaleStatus.NOT_CONNECTED;
	private volatile double net;
	private volatile double tare;
	private volatile double gross;

	private ScaleModel manufacturer;
	private boolean simulated = false;
	private int decimals = 3;
	private double maximum = 10.0;
	private boolean settled = true;
	private ScaleUnit unit;
	private String port;
	private int baudRate;
	private int stopBits;
	private int dataBits;
	private int parity;
	
	public ScaleStatus getStatus() {
		return status;
	}
	public void setStatus(ScaleStatus status) {
		this.status = status;
	}
	public boolean isSettled() {
		return settled;
	}
	public void setSettled(boolean settled) {
		this.settled = settled;
	}
	public ScaleModel getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(ScaleModel manufacturer) {
		this.manufacturer = manufacturer;
	}
	public boolean isSimulated() {
		return simulated;
	}
	public void setSimulated(boolean simulated) {
		this.simulated = simulated;
	}
	public double getNet() {
		return net;
	}
	public void setNet(double net) {
		this.net = net;
	}
	public double getTare() {
		return tare;
	}
	public void setTare(double tare) {
		this.tare = tare;
	}
	public double getGross() {
		return gross;
	}
	public void setGross(double gross) {
		this.gross = gross;
	}
	public String getFormattedNet() {
		String result = String.format("%." + String.valueOf(decimals) + "f", net) + " kg";
		result = result.replace('.', ',');
		if(!settled)
			result = "~ " + result;
		return result; 		
	}
	public String getFormattedTare() {
		String result = String.format("%." + String.valueOf(decimals) + "f", tare) + " kg";;
		result = result.replace('.', ',');
		return result; 		
	}
	public int getDecimals() {
		return decimals;
	}
	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}
	public double getMaximum() {
		return maximum;
	}
	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}
	public ScaleUnit getUnit() {
		return unit;
	}
	public void setUnit(ScaleUnit unit) {
		this.unit = unit;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public int getBaudRate() {
		return baudRate;
	}
	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}
	public int getStopBits() {
		return stopBits;
	}
	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}
	public int getDataBits() {
		return dataBits;
	}
	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}
	public int getParity() {
		return parity;
	}
	public void setParity(int parity) {
		this.parity = parity;
	}
}
