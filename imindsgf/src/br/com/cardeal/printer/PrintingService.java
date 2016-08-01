package br.com.cardeal.printer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.LabelMap;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.log.Logger;

public class PrintingService 
{
	
	private static String message = "";
	private static String lastLabel = "";
	private static List<String> varlines = new ArrayList<String>();
	
	//====================================================================
	// FUNCOES PARA IMPRESSAO EM INTERMEC FINGERPRINT
	//====================================================================
	
	public static void clearPrintStatus() {
		lastLabel = "";
		varlines = new ArrayList<String>();
	}
	
	public static boolean printZebra(LabelMap map, String labelFileName) 
	{
		return printZebra(map, labelFileName, true); 
	}
	
	public static boolean printZebra(LabelMap map, String labelFileName, boolean isUseFormulario) 
	{
		//se a etiqueta a ser impressa é diferente da última, imprime tudo,
		//caso contrário só imprime as variáveis
		if(labelFileName.equals(lastLabel) && isUseFormulario )
		{			
			return printZebraVariables(map);
		}
		else
		{
			return printZebraFull(map, labelFileName, isUseFormulario );
		}
	}
	
	public static boolean printIntermec(LabelMap labelMap, String labelFileName) 
	{
		//se a etiqueta a ser impressa é diferente da última, imprime tudo,
		//caso contrário só imprime as variáveis
		if(labelFileName.equals(lastLabel))
			return printIntermecVariables(labelMap);
		else
			return printIntermecFull(labelMap, labelFileName);
	}
	
	public static boolean printSimulator(LabelMap labelMap, String labelFileName) 
	{
		return printSimulator(labelMap, labelFileName, true);
	}
	
	public static boolean printSimulator(LabelMap labelMap, String labelFileName, boolean isUseFormulario) {
		varlines = new ArrayList<String>();
		lastLabel = "";
		List<String> listForm = labelMap.translateLabelToList(labelFileName, varlines, PrinterModel.ZEBRA, isUseFormulario);
		if(listForm != null && listForm.size() > 0) 
		{
			logDebugLabel(labelFileName, listForm);
			
			List<String> list = new ArrayList<String>();
			List<String> listVars = labelMap.translateVariablesOnly(varlines);	

			list.add("^XA");
			list.add("^XFFORMAT^FS");
			list.addAll(listVars);
			list.add("^XZ");
			
			logDebugLabel(labelFileName+"_vars", list);
			
			return true;
		}
		else {
			message = "Não foi possível ler o arquivo de template " + labelFileName;
			return false;
		}
	}

	private static void logDebugLabel(String labelFileName, List<String> list) {
		//salva em arquivo
		String path = labelFileName + ".out";
		File file = new File(path);			
		OutputStream out = null;
		
		if(file.exists())
			file.delete();
		try 
		{
			out = new FileOutputStream(file);
			for(String line : list) {
				out.write(line.getBytes("UTF-8"));
				out.write("\r\n".getBytes());
			}				
		    out.close();
		    out = null;
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if ( out !=null )
			{
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			    out = null;
			}
		}
	
	}

	public static boolean printZebraFull(LabelMap labelMap, String labelFileName)
	{
		return printZebraFull(labelMap, labelFileName, true);
	}
	
	public static boolean printZebraFull(LabelMap labelMap, String labelFileName, boolean isUseFormulario) 
	{
		varlines = new ArrayList<String>();
		lastLabel = "";
		List<String> list = labelMap.translateLabelToList(labelFileName, varlines, PrinterModel.ZEBRA, isUseFormulario);
		if(list != null && list.size() > 0) 
		{
//			logDebugLabel(labelFileName, list);
			PrintSerial prnSerial = new PrintSerial();
			if(prnSerial.printZebra(Setup.getPrinterPort(), list)) // Envio do formulario
			{			
				lastLabel = labelFileName;
				return ( ( isUseFormulario ) ? printZebraVariables(labelMap) : true );
			}
			else 
			{
				message = prnSerial.getErrorDescription();
				return false;
			}
		}
		else {			
			message = "Não foi possível ler o arquivo de template " + labelFileName;
			GuiGlobals.showMessageDlg(message);
			return false;
		}
		
	}
	
	public static boolean printIntermecFull(LabelMap labelMap, String labelFileName) {
		varlines = new ArrayList<String>();
		lastLabel = "";
		List<String> list = labelMap.translateLabelToList(labelFileName, varlines);
		if(list != null && list.size() > 0) {
//			logDebugLabel(labelFileName, list);
			PrintSerial prnSerial = new PrintSerial();
			if(prnSerial.printIntermec(Setup.getPrinterPort(), list)) {
				lastLabel = labelFileName;
				return true;
			}
			else {
				message = prnSerial.getErrorDescription();
				return false;
			}
		}
		else {
			message = "Não foi possível ler o arquivo de template " + Setup.getLabelsPath() + GuiGlobals.getSeparador() + labelFileName;
			return false;
		}
	}
	
	
	public static boolean printIntermecVariables(LabelMap labelMap) {
		//gera as linhas de alteração somente
		List<String> list = labelMap.translateVariablesOnly(varlines);	
		list.add("Run");
		list.add("LED 0 ON");
		list.add("PRINT KEY OFF");
		list.add("CUT OFF");

		PrintSerial prnSerial = new PrintSerial();
		if(prnSerial.printIntermec(Setup.getPrinterPort(), list)) {
			return true;
		}
		else {
			message = prnSerial.getErrorDescription();
			return false;
		}
	}
	
	public static boolean printZebraVariables(LabelMap labelMap) {
		//gera as linhas de alteração somente
		List<String> list = new ArrayList<String>();
		List<String> listVars = labelMap.translateVariablesOnly(varlines);	

		list.add("^XA");
		list.add("^XFFORMAT^FS");
		list.addAll(listVars);
		list.add("^XZ");

//		logDebugLabel(lastLabel + "_vars", list);
		PrintSerial prnSerial = new PrintSerial();
		if(prnSerial.printZebra(Setup.getPrinterPort(), list)) 
		{
			return true;
		}
		else 
		{
			message = prnSerial.getErrorDescription();
			return false;
		}
	}	
	
	public static boolean printBySpool(LabelMap labelMap, String labelFileName) {
		InputStream stream = buildLabelStream(labelMap, labelFileName);
		
		if(stream != null) {					
			byte[] bytes = null;
			try {
				try {
					bytes = streamToBytes(stream);
				}
				finally {
					stream.close();
				}
			}
			catch(IOException e) {
				bytes = null;
			}
			
			if(bytes == null) {
				message = "Falha ao ler arquivo de saída de impressão";
				return false;
			}
			
			PrintService printer = null;
			PrintService[] existingServices = PrintServiceLookup.lookupPrintServices(null, null);
			for(PrintService ps : existingServices) {
				if(ps.getName().equals(Setup.getPrinterName())) {
					printer = ps;
					break;
				}
			}
			if(printer == null) {
				message = "Impressora " + Setup.getPrinterName() + " não encontrada";
				return false;
			}
			
			DocFlavor byteFlavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
			Doc bytesDoc = new SimpleDoc(bytes, byteFlavor, null);      
			PrintServiceAttributeSet aset = new HashPrintServiceAttributeSet();
			aset.add(new PrinterName(Setup.getPrinterName(), null));  
			PrintService[] services = PrintServiceLookup.lookupPrintServices(byteFlavor, aset);
			if (services.length > 0)
				printer = services[0];
				
		    DocPrintJob job = printer.createPrintJob();
		    try {
		    	PrintRequestAttributeSet printAtts = new HashPrintRequestAttributeSet();
		    	printAtts.add(new Copies(1)); 
		    	job.print(bytesDoc, printAtts);
		    } catch (PrintException pe) {
		        Logger.info(pe.getMessage());
		    }
		    
		    return true;
		}
		else {
			message = "Não foi possível ler o arquivo de template " + Setup.getLabelsPath() + GuiGlobals.getSeparador() + labelFileName;
			return false;
		}
	}
	
	private static byte[] streamToBytes(InputStream stream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = stream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();		
	}
	
	
	private static InputStream buildLabelStream(LabelMap labelMap, String labelFileName) {
		InputStream stream = labelMap.translateLabel(labelFileName, null);				
		if(stream != null) {		
			
			//salva em arquivo
			String path = labelFileName + ".out";
			File file = new File(path);			
			OutputStream out;
			try {
				if(file.exists())
					file.delete();
				out = new FileOutputStream(file);
			    byte buf[]=new byte[1024];
			    int len;
				while( (len=stream.read(buf)) > 0 )
					out.write(buf,0,len);
			    out.close();							

			    //carrega do arquivo para a stream
				if(file.exists()) {					
					stream = new FileInputStream(file);
					return stream;
				}
				
			} catch (FileNotFoundException e1) {				
				e1.printStackTrace();
				try {
					stream.close();
				} catch (IOException e) {
				}
			} catch (IOException e) {
				e.printStackTrace();
				try {
					stream.close();
				} catch (IOException e1) {
				}
			}
			return null;
		}
		else {
			return null;					
		}				
	}

	public static String getMessage() {
		return message;
	}
	
	public static void setMessage(String msg) {
		message = msg;
	}
	
}
