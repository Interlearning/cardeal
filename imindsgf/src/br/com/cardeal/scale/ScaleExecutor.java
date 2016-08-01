package br.com.cardeal.scale;

import br.com.cardeal.log.Logger;

public class ScaleExecutor extends Thread {
	
	private volatile boolean terminated = false;
	private volatile boolean running = false;
	private Scale scale;
	private ScaleProtocol protocol;
	
	public ScaleExecutor(Scale scale) {
		this.scale = scale;
		protocol = ScaleProtocolFactory.create(this.scale);
	}
	
	private void delay(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
	
	public void terminate() {
		terminated = true;
		while(running) {
			delay(10);
		}
	}
	
	public void run() {
		if(protocol == null) {
			Logger.log("Nao existe protocolo para a balanca em " + scale.getPort());
			return;
		}
		
		running = true;
		try {
			while(!terminated) {
				if(protocol.isConnected()) {
					protocol.read();
					delay(100);
				}
				else {
					if(!protocol.open())
						delay(5000);
				}
			}
		}
		finally {		
			protocol.close();
			running = false;
		}
	}

}
