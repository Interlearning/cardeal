package br.com.cardeal.printer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import jssc.SerialPort;
import jssc.SerialPortException;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.log.Logger;
import br.com.cardeal.model.ListSerialPortParams;

public class PrintSerial {
	
	public static final int LIST_BAUDRATE = 0;
	public static final int LIST_DATABITS = 1;
	public static final int LIST_STOPBITS = 2;
	public static final int LIST_PARITY = 3;
	
	private long TIMEOUT = 4000;
	private String portName;
	private SerialPort serialPort = null;
	private int errorCode = 0;
	private String errorDescription = "";
	
	public PrintSerial(){}
	
	public PrintSerial(String portName)
	{
		this.portName = portName;
	}

	public boolean printIntermec(String portName, List<String> list) {
		this.portName = portName;
		if( open()) {
			try {
				purge();
				int count = 0;
				for(String line : list) {
					serialPort.writeString(line);
					serialPort.writeString("\r\n");
					//if(!xonxoff())
					//	return false;
					count++;
					if(count == 5) {
						count = 0;
						sleep(30);
					}
				}
				return getPrintResultIntermec();
			} catch (SerialPortException e) {
				return false;
			}
			finally {
				close();
			}
		}
		else
			return false;
	}
	
	public boolean printZebra(String portName, List<String> list) {
		
		this.portName = portName;
		FileOutputStream os ; 
		PrintStream ps = null;
		boolean isPrintOk = false;
		
		if ( portName.contains("LPT") ) {
		
			try { 
		        os = new FileOutputStream(portName); 
		        ps = new PrintStream(os); 
		        for(String line : list) 
		        {
		        	ps.println( line );
				}
		        isPrintOk = true;
				
		    } catch (FileNotFoundException e) { 
		        GuiGlobals.showMessageDlg("Não foi possível enviar os dados para impressão na porta " + portName + " parece estar inacessível\n"+e.getMessage());
		        isPrintOk = false;
		    } 
			finally 
			{
				if (ps != null){
					ps.close();
				}
				 
			}
			
		}
	    
		if ( portName.contains("COM") ) 
		{
			if( open() ) 
			{
				getPrintResultZebra();
				purge();
				try 
				{
					String lineFull = "";
//					int qtyTickets = 1;
					for(String line : list) 
					{
						lineFull += line;
						
						/**
						 * Logica para escrever as etiquetas em memoria e mandar de uma vez para a impressora
						 * Isso faz com que a impressora imprima mais rapido, contudo precisa mudar a logica da tabela Stock para
						 * ter um flag do que já foi impresso e manter o controle. Como hoje é enviado para a impressora registro a registro
						 * esta logica nao faz sentido até o momento.
						 */
						/*
						if ( line.toUpperCase().trim().contains("^XZ") )
						{
							qtyTickets++;
						}
						
						//Envia 20 etiquetas por vez para não estourar a memoria
						if ( qtyTickets == 20 )
						{
							if ( !writeOnPort(lineFull) )
							{
								LogDeProcessamento.gravaLog("error", "Falha na escrita: " + lineFull);
							}
							qtyTickets = 1;
							lineFull = "";
							sleep(5000); // Aguarda 5 segundos para a liberação de memória da impressora.
						}
						*/
						
					}
					if ( lineFull.isEmpty() )
					{
						isPrintOk = false;
						errorDescription = "Envio de informações vazias para impressão!";
						LogDeProcessamento.gravaLog("printError", errorDescription, true );
					}
					else if ( writeOnPort(lineFull) )
					{
						isPrintOk = true;
						sleep(250);
					}
					
				} 
				catch (Exception e) 
				{
					errorDescription = "Erro na impressão: " + e.getMessage();
					LogDeProcessamento.gravaLog("error", errorDescription);
					isPrintOk = false;
				}
				finally 
				{
					close();
				}
			}
			else{
				isPrintOk = false;
			}			
		}
		
		return isPrintOk;
		
	}
	
	private boolean writeOnPort( String texto )
	{
		return writeOnPort( texto, true );
	}
	
	private boolean writeOnPort( String texto, boolean utf8 )
	{
		long init = System.currentTimeMillis();
		while(System.currentTimeMillis() - init < TIMEOUT) 
		{
			try 
			{
				if ( utf8 )
				{				
					serialPort.writeBytes( texto.getBytes("UTF-8") );
				}
				else
				{
					serialPort.writeString( texto );
				}
				return true;
				
			} 
			catch (SerialPortException e) 
			{
				errorDescription = "Falha na comunicação com a porta: " + "SerialPortException> " + e.getMessage();
				LogDeProcessamento.gravaLog("serialError", errorDescription, true );			
				e.printStackTrace();
			} 
			catch (UnsupportedEncodingException e) 
			{
				errorDescription = "Falha na comunicação com a porta: " + "UnsupportedEncodingException> " + e.getMessage();
				LogDeProcessamento.gravaLog("serialError", errorDescription, true );
				e.printStackTrace();
			}
			catch (Exception e) 
			{
				errorDescription = "Falha na comunicação com a porta: " + "Exception> " + e.getMessage();
				LogDeProcessamento.gravaLog("serialError", errorDescription, true );
				e.printStackTrace();
			}
		}
		
		errorCode = -1;
		errorDescription = "Sem resposta da impressora";
		LogDeProcessamento.gravaLog("serialError", errorDescription, true );
	
		return false;
	}
	
	private String readFromPort()
	{
		String line = null;;		
		int count;
		try 
		{
			count = serialPort.getInputBufferBytesCount();			
			if(count > 0) 
			{
				line = new String(serialPort.readBytes(count), 0, count).trim();
			}
		}
		catch (SerialPortException e) 
		{
			errorDescription = "Falha na comunicação com a porta: " + e.getMessage();
			LogDeProcessamento.gravaLog("serialError",errorDescription + "\r\n", true );
			e.printStackTrace();
		}
		return line;
	}

	public void getPrintResultZebra() 
	{
		getPrintResultZebra(true, false);
	}
	
	public void getPrintResultZebra(boolean isPurge, boolean closePort) 
	{
		
		String line = "";                                      
		boolean result = false;

		if ( isConnected() || open() ) 
		{
			try 
			{
				long init = System.currentTimeMillis();
				while ( line.isEmpty() )
				{					
					while( !result )
					{
						result = writeOnPort("~HS");          
						if ( !result ) 
						{
							LogDeProcessamento.gravaLog("serialError", "Nao conseguiu escrever ~HS na porta!");
							if ( isPurge ) purge();
							sleep(30);
						}
					}
					
					line = readFromPort();
					
					if ( line != null && !line.isEmpty() )
					{
						if ( parseFrameStatus(line).equals("OK") )
						{
							break;
						}
						else
						{
							init = System.currentTimeMillis(); // Nao deixo o time-out parar pois foi encontrado um status e o sistema deve aguardar
						}
						
					}
					result = false;
					sleep(700);
					    
					line = "";
					
					if ( ( System.currentTimeMillis() - init ) > TIMEOUT )
					{
						GuiGlobals.showMessage("Impressora sem resposta...", false);
					}
				}
			} 			
			catch (Exception e) 
			{
				errorDescription = "Falha na comunicação com a porta: " + e.getMessage();
				LogDeProcessamento.gravaLog("serialError",errorDescription + "\r\n", true );
				e.printStackTrace();
			}
			finally
			{
				if ( closePort ) close();
			}
		}
	}

	private String parseFrameStatus(String line) 
	{
		String cRet = "OK";
		String[] strings = line.split(String.valueOf( ( char ) 13 )+String.valueOf( ( char ) 10 ));
		String[] parametrosString1 = ( strings.length > 0 ) ? strings[0].split(",") : null;
		String[] parametrosString2 = ( strings.length > 1 ) ? strings[1].split(",") : null;
//		String[] parametrosString3 = ( strings.length > 2 ) ? strings[2].split(",") : null; // NAO ESTA SENDO UTILIZADO

		if ( parametrosString1 != null )
		{
			if ( parametrosString1.length > 5 && parametrosString1[5].equals("1") )
			{
				GuiGlobals.showMessage("Aguardando... Buffer cheio", false); 
				cRet = "RETRY";
			}
			else if ( parametrosString1.length > 1 && parametrosString1[1].equals("1") )
			{
				GuiGlobals.showMessage("Aguardando... Falta papel", false);
				cRet = "RETRY";
			}
			else if ( parametrosString1.length > 2 && parametrosString1[2].equals("1") )
			{
				GuiGlobals.showMessage("Aguardando... Impressora em pausa", false); 
				cRet = "RETRY";
			}
//			else if ( parametrosString1 != null && parametrosString1.length > 7 )
//			{
//				if ( parametrosString1[7].equals("1") )
//				{
//					System.out.println("Em progresso...");
//				}
//				else if ( parametrosString1[7].equals("0") )
//				{
//					System.out.println("Impressao finalizada");
//				}
//			}
			
		}
		
		if ( parametrosString2 != null && cRet.equals("OK") )
		{
			if ( parametrosString2.length > 2 && parametrosString2[2].equals("1") )
			{
				GuiGlobals.showMessage("Aguardando... Impressora Aberta", false); 
				cRet = "RETRY";
			}
			else if ( parametrosString2.length > 3 && parametrosString2[3].equals("1") )
			{
				GuiGlobals.showMessage("Aguardando... Falta Ribbon", false); 
				cRet = "RETRY";
			}
		}
		
		return cRet;
	}

	@SuppressWarnings("unused")
	private boolean xonxoff() {
		try {
			int count = serialPort.getInputBufferBytesCount();
			if(count > 0) {
				for(int i = 0; i < count; i++) {
					byte[] received = serialPort.readBytes(1);
					System.out.println(String.format("%02X ", received[0]));
				}
			}
			return true;
		} catch (SerialPortException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean getPrintResultIntermec() 
	{
        try {
			byte[] buffer = new byte[256];
			int index = 0;
			long init = System.currentTimeMillis();
			while(System.currentTimeMillis() - init < TIMEOUT) {
				int count = serialPort.getInputBufferBytesCount();
				if(count > 0) {
					for(int i = 0; i < count; i++) {
						byte[] received = serialPort.readBytes(1);
						buffer[index++] = received[0];
						if(received[0] == 0x0a) {
							String line = new String(buffer, 0, index).trim();
							if(line.contains("printok"))
								return true;
							else if(line.contains("printerror")) {
								String[] parts = line.split(":");
								if(parts.length > 1) {
									errorCode = Integer.parseInt(parts[1]);
									evaluateError();
									return false;
								}
							}
							else {
								System.out.println(line);
								index = 0;
							}
						}
					}
				}
				else
					sleep(10);
			}
			
			errorCode = -1;
			errorDescription = "Sem resposta da impressora";
			return false;
			
		} catch (SerialPortException e) {
			Logger.logException(e);
			errorDescription = e.getMessage();
			errorCode = -2;
			return false;
		}				
	}
	
	private void evaluateError() {
		switch(errorCode) {
		case 0: errorDescription = "No error"; break;
		case 1: errorDescription = "Syntax error."; break;
		case 2: errorDescription = "Unbalanced parentheses."; break;
		case 3: errorDescription = " Feature not implemented."; break;
		case 4: errorDescription = " Evaluation syntax error."; break;
		case 5: errorDescription = " Unrecognized token."; break;
		case 6: errorDescription = " Tokenized line too long."; break;
		case 7: errorDescription = " Evaluation stack overflow."; break;
		case 8: errorDescription = " Error in exectab."; break;
		case 9: errorDescription = " Undefined token."; break;
		case 10: errorDescription = " Non-executing token."; break;
		case 11: errorDescription = " Evaluation stack underflow."; break;
		case 12: errorDescription = " Type mismatch."; break;
		case 13: errorDescription = " Line not found."; break;
		case 14: errorDescription = " Division with zero."; break;
		case 15: errorDescription = " Font not found."; break;
		case 16: errorDescription = " Bar code device not found."; break;
		case 17: errorDescription = " Bar code type not implemented."; break;
		case 18: errorDescription = " Disk full.";break;
		case 19: errorDescription = " Error in file name."; break;
		case 20: errorDescription = " Input line too long."; break;
		case 21: errorDescription = " Error stack overflow."; break;
		case 22: errorDescription = " RESUME without error."; break;
		case 23: errorDescription = " Image not found."; break;
		case 24: errorDescription = " Overflow in temporary string buffer."; break;
		case 25: errorDescription = " Wrong number of parameters."; break;
		case 26: errorDescription = " Parameter too large."; break;
		case 27: errorDescription = " Parameter too small."; break;
		case 28: errorDescription = " RETURN without GOSUB"; break;
		case 29: errorDescription = " Error in startup file."; break;
		case 30: errorDescription = " Assign to a read-only variable."; break;
		case 31: errorDescription = " Illegal file number."; break;
		case 32: errorDescription = " File is already open."; break;
		case 33: errorDescription = " Too many files open."; break;
		case 34: errorDescription = " File is not open."; break;
		case 37: errorDescription = " Cutter device not found."; break;
		case 38: errorDescription = " User break."; break;
		case 39: errorDescription = " Illegal line number."; break;
		case 40: errorDescription = " Run statement in program."; break;
		case 41: errorDescription = " Parameter out of range."; break;
		case 42: errorDescription = " Illegal bar code ratio."; break;
		case 43: errorDescription = " Memory overflow."; break;
		case 44: errorDescription = " File is write protected."; break;
		case 45: errorDescription = " Unknown store option"; break;
		case 46: errorDescription = " Store already in progress."; break;
		case 47: errorDescription = " Unknown store protocol."; break;
		case 48: errorDescription = " No store defined."; break;
		case 49: errorDescription = " NEXT without FOR."; break;
		case 50: errorDescription = " Bad store record header."; break;
		case 51: errorDescription = " Bad store address."; break;
		case 52: errorDescription = " Bad store record."; break;
		case 53: errorDescription = " Bad store checksum."; break;
		case 54: errorDescription = " Bad store record end."; break;
		case 55: errorDescription = " Remove in ROM."; break;
		case 56: errorDescription = " Illegal communication channel."; break;
		case 57: errorDescription = " Subscript out of range."; break;
		case 58: errorDescription = " Field overflow."; break;
		case 59: errorDescription = " Bad record number."; break;
		case 60: errorDescription = " Too many strings."; break;
		case 61: errorDescription = " Error in setup file."; break;
		case 62: errorDescription = " File is list protected."; break;
		case 63: errorDescription = " ENTER function."; break;
		case 64: errorDescription = " FOR without NEXT"; break;
		case 65: errorDescription = " Evaluation overflow."; break;
		case 66: errorDescription = " Bad optimizing type."; break;
		case 67: errorDescription = " Error from communication channel."; break;
		case 68: errorDescription = " Unknown execution identity."; break;
		case 69: errorDescription = " Not allowed in immediate mode."; break;
		case 70: errorDescription = " Line label not found."; break;
		case 71: errorDescription = " Line label already defined."; break;
		case 72: errorDescription = " IF without ENDIF."; break;
		case 73: errorDescription = " ENDIF without IF."; break;
		case 74: errorDescription = " ELSE without ENDIF."; break;
		case 75: errorDescription = " ELSE without IF."; break;
		case 76: errorDescription = " WHILE without WEND."; break;
		case 77: errorDescription = " WEND without WHILE."; break;
		case 78: errorDescription = " Not allowed in execution mode."; break;
		case 79: errorDescription = " Not allowed in a layout."; break;
		case 80: errorDescription = " Download timeout."; break;
		case 81: errorDescription = " Exit to system."; break;
		case 82: errorDescription = " Invalid cont environment."; break;
		case 83: errorDescription = " ETX Timeout."; break;
		case 1001: errorDescription = " Not implemented."; break;
		case 1002: errorDescription = " Memory too small."; break;
		case 1003: errorDescription = " Field out of label."; break;
		case 1004: errorDescription = " Wrong font to chosen direction."; break;
		case 1005: errorDescription = " Out of paper."; break;
		case 1006: errorDescription = " No field to print."; break;
		case 1007: errorDescription = " Lss too high."; break;
		case 1008: errorDescription = " Lss to low."; break;
		case 1009: errorDescription = " Invalid parameter."; break;
		case 1010: errorDescription = " Hardware error."; break;
		case 1011: errorDescription = " I/O error."; break;
		case 1012: errorDescription = " Too many files as opened."; break;
		case 1013: errorDescription = " Device not found."; break;
		case 1014: errorDescription = " File not found."; break;
		case 1015: errorDescription = " File is read-only."; break;
		case 1016: errorDescription = " Illegal argument."; break;
		case 1017: errorDescription = " Results are too large."; break;
		case 1018: errorDescription = " Bad file descriptor."; break;
		case 1019: errorDescription = " Invalid font."; break;
		case 1020: errorDescription = " Invalid image."; break;
		case 1021: errorDescription = " Too large argument for MAG."; break;
		case 1022: errorDescription = " Head lifted."; break;
		case 1023: errorDescription = " Incomplete label."; break;
		case 1024: errorDescription = " File too large."; break;
		case 1025: errorDescription = " File does not exist."; break;
		case 1026: errorDescription = " Label pending."; break;
		case 1027: errorDescription = " Out of transfer ribbon."; break;
		case 1028: errorDescription = " Paper type is not selected."; break;
		case 1029: errorDescription = " Printhead voltage too high."; break;
		case 1030: errorDescription = "Character is missing in chosen font."; break;
		case 1031: errorDescription = "Next label not found."; break;
		case 1032: errorDescription = " File name too long."; break;
		case 1033 : errorDescription = "Too many files are open"; break;
		case 1034: errorDescription = " Not a directory."; break;
		case 1035: errorDescription = " File pointer is not inside the file."; break;
		case 1036: errorDescription = " Subscript out of range."; break;
		case 1037: errorDescription = " No acknowledge received within specified time."; break;
		case 1038: errorDescription = " Communication checksum error."; break;
		case 1039: errorDescription = " Not mounted."; break;
		case 1040: errorDescription = " Unknown file operating system."; break;
		case 1041: errorDescription = " Error in fos structure."; break;
		case 1042: errorDescription = " Internal error in mcs."; break;
		case 1043: errorDescription = " Timer table full."; break;
		case 1044: errorDescription = " Low battery in memory card"; break;
		case 1045: errorDescription = " Media was removed."; break;
		case 1046: errorDescription = " Memory checksum error."; break;
		case 1047: errorDescription = " Interrupted system call."; break;
		case 1051: errorDescription = " Dot resistance measure out of limits."; break;
		case 1052: errorDescription = " Error in printhead."; break;
		case 1053: errorDescription = " Unable to complete a dot measurement."; break;
		case 1054: errorDescription = " Error when trying to write to device."; break;
		case 1055: errorDescription = " Error when trying to read from device."; break;
		case 1056: errorDescription = " O_BIT open error."; break;
		case 1057: errorDescription = " File exists."; break;
		case 1058: errorDescription = " Transfer ribbon is installed."; break;
		case 1059: errorDescription = " Cutter does not respond."; break;
		case 1061: errorDescription = " Wrong type of media."; break;
		case 1062: errorDescription = " Not allowed."; break;
		case 1067: errorDescription = " Is a directory."; break;
		case 1073: errorDescription = " Directory not empty."; break;
		case 1076: errorDescription = " Permission denied."; break;
		case 1077: errorDescription = " Broken pipe."; break;
		case 1081: errorDescription = " Timer expired."; break;
		case 1082: errorDescription = " Unsupported protocol."; break;
		case 1083: errorDescription = " Ribbon low."; break;
		case 1084: errorDescription = " Paper low."; break;
		case 1085: errorDescription = " Connection timed out."; break;
		case 1086: errorDescription = " Secret not found."; break;
		case 1087: errorDescription = " Paper jam."; break;
		case 1088: errorDescription = " Printhead too hot."; break;
		case 1101: errorDescription = " Illegal character in bar code."; break;
		case 1102: errorDescription = " Illegal bar code font."; break;
		case 1103: errorDescription = " Too many characters in bar code."; break;
		case 1104: errorDescription = " Bar code too large."; break;
		case 1105: errorDescription = " Bar code parameter error."; break;
		case 1106: errorDescription = " Wrong number of characters."; break;
		case 1107: errorDescription = " Illegal bar code size."; break;
		case 1108: errorDescription = " Number or rows out of range."; break;
		case 1109: errorDescription = " Number of columns out of range."; break;
		case 1112: errorDescription = " ECI syntax error."; break;
		case 1201: errorDescription = " Insufficient font data loaded."; break;
		case 1202: errorDescription = " Transformation matrix out of range."; break;
		case 1203: errorDescription = " Font format error."; break;
		case 1204: errorDescription = " Specifications not compatible with output."; break;
		case 1205: errorDescription = " Intelligent transform not supported."; break;
		case 1206: errorDescription = " Unsupported output mode requested."; break;
		case 1207: errorDescription = " Extended font not supported."; break;
		case 1208: errorDescription = " Font specifications not set."; break;
		case 1209: errorDescription = " Track kerning data not available."; break;
		case 1210: errorDescription = " Pair kerning data not available."; break;
		case 1211: errorDescription = " Other Speedo error."; break;
		case 1212: errorDescription = " No bitmap or outline device."; break;
		case 1213: errorDescription = " Speedo error six."; break;
		case 1214: errorDescription = " Squeeze or clip not supported."; break;
		case 1215: errorDescription = " Character data not available."; break;
		case 1216: errorDescription = " Unknown font."; break;
		case 1217: errorDescription = " Font format is not supported."; break;
		case 1218: errorDescription = " Correct mapping table is not found."; break;
		case 1219: errorDescription = " Font is in the wrong direction."; break;
		case 1220: errorDescription = " Error in external map table."; break;
		case 1221: errorDescription = " Map table was not found."; break;
		case 1222: errorDescription = " Double byte map table is missing."; break;
		case 1223: errorDescription = " Single byte map table is missing."; break;
		case 1224: errorDescription = " Character map function is missing."; break;
		case 1225: errorDescription = " Double byte font is not selected."; break;
		case 1301: errorDescription = " Index outside collection bounds."; break;
		case 1302: errorDescription = " Collection could not be expanded."; break;
		case 1303: errorDescription = " Parameter is not a collection."; break;
		case 1304: errorDescription = " Item not a member of the collection."; break;
		case 1305: errorDescription = " No compare function, or returns faulty"; break;
		case 1306: errorDescription = " Tried to insert a duplicate item."; break;
		case 1320: errorDescription = " No RFID support installed."; break;
		case 1321: errorDescription = " No tag found."; break;
		case 1322: errorDescription = " Access outside tag memory."; break;
		case 1323: errorDescription = " Access too long for block."; break;
		case 1324: errorDescription = " RFID inactive."; break;
		case 1601: errorDescription = "Reference Font Not Found."; break;
		case 1602: errorDescription = " Error in Wand-Device."; break;
		case 1603: errorDescription = " Error in Slave Processor."; break;
		case 1604: errorDescription = " Print Shift Error."; break;
		case 1605: errorDescription = " No Hardware Lock."; break;
		case 1606: errorDescription = " Testfeed not done."; break;
		case 1607: errorDescription = " General Print Error."; break;
		case 1608: errorDescription = " Access Denied."; break;
		case 1609: errorDescription = " Specified Feed Length Exceeded."; break;
		case 1610: errorDescription = " Illegal Character Map File."; break;
		case 1701: errorDescription = " Cutter not back in position after cut."; break;
		case 1702: errorDescription = " Cutter has not reached upper position: unsuccessful cut."; break;
		case 1703: errorDescription = " Cutter not back in position after unsuccessful cut."; break;
		case 1704: errorDescription = " Cutter open."; break;
		case 1710: errorDescription = " Power supply Generic Error."; break;
		case 1711: errorDescription = " Power supply Pending."; break;
		case 1712: errorDescription = " Power Status OK."; break;
		case 1713: errorDescription = " Power supply Power Fail."; break;
		case 1714: errorDescription = " Power supply Over Volt V24."; break;
		case 1715: errorDescription = " Power supply Under Volt V24."; break;
		case 1716: errorDescription = " Power supply Over Volt VSTM."; break;
		case 1717: errorDescription = " Power supply Under Volt VSTM."; break;
		case 1718: errorDescription = " Power supply Over Temperature."; break;
		case 1719: errorDescription = " Power supply Error."; break;
		case 1820: errorDescription = " No route to host."; break;
		case 1821: errorDescription = " Disc quota exceeded."; break;
		case 1833: errorDescription = " Connection refused."; break;
		}
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
	
	public String getErrorDescription() {
		return this.errorDescription;
	}
	
	private boolean isConnected() {
		return (serialPort != null && serialPort.isOpened());
	}
	
	private void close() {
		if(isConnected())
			try 
			{
				serialPort.closePort();
				serialPort = null;
			} 
			catch (SerialPortException e) 
			{
				LogDeProcessamento.gravaLog("serailError", "Falha no fechamento da porta serial: " + e.getMessage());
			}
	}

	private void purge() 
	{
		try 
		{
			while ( !serialPort.purgePort(SerialPort.PURGE_RXCLEAR) )
			{
				sleep(100);
			}
			while ( !serialPort.purgePort(SerialPort.PURGE_TXCLEAR) )
			{
				sleep(100);
			}
		} 
		catch (SerialPortException e) 
		{
			LogDeProcessamento.gravaLog("printing", "Falha na limpeza de bits na porta serial: " + e.getMessage());
		}
	}
	
	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
	
	
	private boolean open() 
	{
		boolean isOpen = true;
		
		if ( !isConnected() )
		{
			serialPort = new SerialPort(portName);
		    try {
		        serialPort.openPort();
		        serialPort.setParams(	Setup.getTerminal().getPrinterBaudRate(), 
						        		Setup.getTerminal().getPrinterDataBits(),
						        		Setup.getTerminal().getPrinterStopBits(),
						        		Setup.getTerminal().getPrinterParity());
		        
//		        serialPort.setParams(SerialPort.BAUDRATE_115200,
//		                              SerialPort.DATABITS_8,
//		                              SerialPort.STOPBITS_1,
//		                              SerialPort.PARITY_NONE);
		         
		        isOpen = true;	         
		    }
		    catch (SerialPortException e) 
		    {
		    	e.printStackTrace();
		    	this.errorCode = -3;
		    	this.errorDescription = "Canal serial " + portName + " inexistente ou ocupado";
		    	LogDeProcessamento.gravaLog("serialError", errorDescription + "\r\n" + e.getMessage());
		    	isOpen = false;
		    }	
		    catch (Exception e) 
		    {
		    	e.printStackTrace();
		    	this.errorDescription = "Canal serial " + portName + " inexistente ou ocupado";
		    	LogDeProcessamento.gravaLog("serialError", errorDescription + "\r\n" + e.getMessage());
		    	isOpen = false;
		    }
		}
		
		return isOpen;
			 		
	}
	
	@SuppressWarnings("null")
	public static List<String> getListParamToString( int opcao )
	{
		List<String> listParamToString = null;
		List<ListSerialPortParams> listParam = getListParam( opcao );
		
		if ( listParam != null && listParam.size() > 0)
		{
		
			for ( ListSerialPortParams serialPortParam : listParam )
			{
				listParamToString.add( serialPortParam.getDescription() );
			}
			
		}
		
		return listParamToString;
	}
	
	public static List<ListSerialPortParams> getListParam( int opcao )
	{
		List<ListSerialPortParams> list = null;
		
		switch ( opcao ) 
		{
		
			case LIST_BAUDRATE:
				list = getListBaudRate();
				break;
				
			case LIST_DATABITS:
				list = getListDataBits();
				break;
				
			case LIST_PARITY:
				list = getListParity();
				break;
				
			case LIST_STOPBITS:
				list = getListStopBits();
				break;
	
			default:
				break;
		}
		
		return list;
		
	}
	
	private static List<ListSerialPortParams> getListBaudRate() {
		
		List<ListSerialPortParams> list = new ArrayList<ListSerialPortParams>();
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_110, "BAUDRATE_110") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_115200, "BAUDRATE_115200") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_1200, "BAUDRATE_1200") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_128000, "BAUDRATE_128000") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_14400, "BAUDRATE_14400") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_19200, "BAUDRATE_19200") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_256000, "BAUDRATE_256000") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_300, "BAUDRATE_300") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_38400, "BAUDRATE_38400") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_4800, "BAUDRATE_4800") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_57600, "BAUDRATE_57600") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_600, "BAUDRATE_600") );
		list.add( new ListSerialPortParams( SerialPort.BAUDRATE_9600, "BAUDRATE_9600") );
		
		return list;
	}

	private static List<ListSerialPortParams> getListDataBits() {
		
		List<ListSerialPortParams> list = new ArrayList<ListSerialPortParams>();
		list.add( new ListSerialPortParams( SerialPort.DATABITS_5, "DATABITS_5") );
		list.add( new ListSerialPortParams( SerialPort.DATABITS_6, "DATABITS_6") );
		list.add( new ListSerialPortParams( SerialPort.DATABITS_7, "DATABITS_7") );
		list.add( new ListSerialPortParams( SerialPort.DATABITS_8, "DATABITS_8") );
		
		return list;
	}
	
	private static List<ListSerialPortParams> getListParity() {
		
		List<ListSerialPortParams> list = new ArrayList<ListSerialPortParams>();
		list.add( new ListSerialPortParams( SerialPort.PARITY_EVEN, "EVEN") );
		list.add( new ListSerialPortParams( SerialPort.PARITY_MARK, "MARK") );
		list.add( new ListSerialPortParams( SerialPort.PARITY_NONE, "NONE") );
		list.add( new ListSerialPortParams( SerialPort.PARITY_ODD, "ODD") );
		list.add( new ListSerialPortParams( SerialPort.PARITY_SPACE, "SPACE") );	
		
		return list;
	}
	
	private static List<ListSerialPortParams> getListStopBits() {
		
		List<ListSerialPortParams> list = new ArrayList<ListSerialPortParams>();
		list.add( new ListSerialPortParams( SerialPort.STOPBITS_1, "STOPBITS_1") );
		list.add( new ListSerialPortParams( SerialPort.STOPBITS_1_5, "STOPBITS_1_5") );
		list.add( new ListSerialPortParams( SerialPort.STOPBITS_2, "STOPBITS_2") );
		
		return list;
	}
	
	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}
	
}
