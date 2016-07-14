package br.com.cardeal.scale;

import br.com.cardeal.log.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

public class AlfaProtocol implements ScaleProtocol
{	
	private SerialPort serialPort = null; 
	private Scale scale;
	private long TIMEOUT = 500;
	
	public AlfaProtocol(Scale scale) {
		this.scale = scale;		
		scale.setStatus(ScaleStatus.NOT_CONNECTED);
	}

	@Override
	public boolean isConnected() {
		return (serialPort != null && serialPort.isOpened());
	}
	
	@Override
	public boolean open() {

		serialPort = new SerialPort(scale.getPort());
	    try {
	        serialPort.openPort();
	        serialPort.setParams(scale.getBaudRate(), 
	        					scale.getDataBits(),
	        					scale.getStopBits(),
	        					scale.getParity());
	        
//	        serialPort.setParams(SerialPort.BAUDRATE_19200, 
//	                              SerialPort.DATABITS_8,
//	                              SerialPort.STOPBITS_1,
//	                              SerialPort.PARITY_NONE);
	         
			return true;	         
	    }
	    catch (SerialPortException e) {
	    	Logger.log("Falha ao conectar-se a balanca alfa: " + e.getMessage());
	    	Logger.logException(e);
	    	return false;
	    }		 		
	}
	
	@Override
	public void close() {
		if(isConnected())
			try {
				serialPort.closePort();
				serialPort = null;
				scale.setStatus(ScaleStatus.NOT_CONNECTED);
			} catch (SerialPortException e) {
			}
	}

	private void purge() {
		try {
			serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
		} catch (SerialPortException e) {
		}
	}
	
	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
	
	private void parseFrame(byte[] frame, int size) {
		
		boolean settled = false;		
		
		try {
			if(size >= 2) {
				if(frame[0] == 'S') {
					if(frame[1] == 'A') {
						scale.setStatus(ScaleStatus.SATURATION);
					}
					else if(frame[1] == 'O') {
						scale.setStatus(ScaleStatus.OVERLOAD);
					}
				}
				else if(frame[0] == 'P' || frame[0] == '*') {
					
					scale.setStatus(ScaleStatus.OK);
					
					String s = new String(frame, 3, 7);
					s = s.trim();
					s = s.replace(',', '.');
					double net = Double.parseDouble(s);
					if(scale.getUnit() == ScaleUnit.G)
						net /= 1000;
										
					s = new String(frame, 13, 7);
					s = s.trim();
					s = s.replace(',', '.');
					double tare = Double.parseDouble(s);
					if(scale.getUnit() == ScaleUnit.G)
						tare /= 1000;
					
					settled = ( frame[0] == 'P' );
					
					scale.setTare(tare);
					scale.setNet(net);
					scale.setGross(net + tare);					
					scale.setSettled( settled );
				}
			}
		}
		catch(Exception e) {
			scale.setStatus(ScaleStatus.COMM_FAIL);			
		}
	}
	
	@Override
	public void read() {
        try {
        	purge();
			serialPort.writeBytes("01P\r\n".getBytes());
			byte[] buffer = new byte[32];
			int index = 0;
			long init = System.currentTimeMillis();
			
			while(System.currentTimeMillis() - init < TIMEOUT ) {
				int count = serialPort.getInputBufferBytesCount();
				if(count > 0) {
					for(int i = 0; i < count; i++) {
						byte[] received = serialPort.readBytes(1);
						buffer[index++] = received[0];
						if(received[0] == 0x0a) {
							parseFrame(buffer, index);
							return;
						}
					}
				}
				else
					sleep(10);
			}
			
			scale.setStatus(ScaleStatus.COMM_FAIL);
			
		} catch (SerialPortException e) {
			Logger.logException(e);
		}				
	}

	@Override
	public void zero() {
		
		
	}

	@Override
	public void tare() {
		
		
	}

	@Override
	public void tare(double value) {
		
		
	}	
}
