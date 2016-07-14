package br.com.cardeal.scale;

public class ScaleProtocolFactory {
	
	public static ScaleProtocol create(Scale scale) {
		
		switch( scale.getManufacturer() ) 
		{
			case ALFA: return new AlfaProtocol(scale);
			case TOLEDO: return new ToledoProtocol(scale);			
			default: break;		
		}
		
		return null;
	}
}
