package br.com.cardeal.scale;

import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.log.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

public class ToledoProtocol implements ScaleProtocol
{
	private SerialPort serialPort = null; 
	private Scale scale;
	private long TIMEOUT = 500;
	
	public ToledoProtocol(Scale scale) {
		this.scale = scale;
		scale.setStatus(ScaleStatus.NOT_CONNECTED);
	}

	@Override
	public boolean isConnected() {
		return (serialPort != null && serialPort.isOpened());
	}
	
	@Override
	public boolean open() 
	{
		serialPort = new SerialPort(scale.getPort());
	    try {
	        serialPort.openPort();
	        serialPort.setParams(scale.getBaudRate(), 
	        					scale.getDataBits(),
	        					scale.getStopBits(),
	        					scale.getParity());
	        
//	        serialPort.setParams(SerialPort.BAUDRATE_4800, 
//	                              SerialPort.DATABITS_7,
//	                              SerialPort.STOPBITS_2,
//	                              SerialPort.PARITY_EVEN);
	         
			return true;	         
	    }
	    catch (SerialPortException e) {
	    	LogDeProcessamento.gravaLog(null, "Falha ao conectar-se a balanca toledo: " + e.getMessage() );
	    	Logger.log("Falha ao conectar-se a balanca toledo: " + e.getMessage());
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
						
		boolean identifing = (	frame[2] == 'p' || frame[2] == 'x' || frame[2] == '{' || frame[2] == 'y' || frame[2] == 's' // Formato de string 1
								|| frame[2] == '8' || frame[2] == '0' || frame[2] == '1' || frame[2] == '9' || frame[2] == ';' || frame[2] == '3'); // Formato de string 2
		
		boolean estabilized = (	frame[2] == 'p' || frame[2] == 'q' || frame[2] == 's' // Formato de string 1
								|| frame[2] == '0' || frame[2] == '3' || frame[2] == '1'); // Formato de string 2
		
		boolean netWithTare =	frame[2] == 's' // Formato de string 1
								|| frame[2] == '3' || frame[2] == ';'; // Formato de string 2 ( PARA PESO NEGATIVO )
				
		try {
			if( size >= 2 && identifing )
			{
				scale.setStatus(ScaleStatus.OK);						
				
				String taraKG = new String( frame, 10, 3);
				String taraG = new String( frame, 13, 3);
				double tare = Double.parseDouble( taraKG.concat(".").concat( taraG ) );
				
				String pesoKG = new String( frame, 4, 3);
				String pesoG = new String( frame, 7, 3);								
				double net = Double.parseDouble( pesoKG.concat(".").concat( pesoG ) ) * ( ( netWithTare ) ? -1 : 1 );
				
				/**
				 * Se nao enviar comando de estavel na string, checo se o peso anterior é igual ao atual, pois varia bastante internamente
				 * a informacao de estavel.
				 */
				if ( !estabilized ) estabilized = ( ( net == scale.getNet() ) && ( tare == scale.getTare() ) );
				
				scale.setTare(tare);
				scale.setNet(net);
				scale.setGross(net + tare);					
				scale.setSettled( estabilized );

			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			scale.setStatus(ScaleStatus.COMM_FAIL);			
		}
	}
	
	@Override
	public void read() {
		
        try {
        	purge();
			serialPort.writeBytes("EQN".getBytes());
			byte[] buffer = new byte[32];
			int index = 0;
			long init = System.currentTimeMillis();
			
			while(System.currentTimeMillis() - init < TIMEOUT ) {
				int count = serialPort.getInputBufferBytesCount();
				if(count > 0) {
					
					for(int i = 0; i < count; i++) {
						byte[] received = serialPort.readBytes(1);
						buffer[index++] = received[0];
						if(received[0] == 0x0d) {							
							parseFrame(buffer, index);
							return;
						}
					}					
				}
				else
					sleep(5);
			}
			
			scale.setStatus(ScaleStatus.COMM_FAIL);
			
		} 
        catch (SerialPortException e) 
        {
			e.printStackTrace();
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
