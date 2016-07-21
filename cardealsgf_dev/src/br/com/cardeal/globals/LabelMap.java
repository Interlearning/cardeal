package br.com.cardeal.globals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.printer.PrinterModel;


public class LabelMap {
	private HashMap<String, String> hashMap = new HashMap<String, String>();
	
	public void put(String key, String value) {
		hashMap.put(key, value);
	}
	
	public String get(String key) {
		String ret = hashMap.get(key);
		if(ret == null)
			return " ";
		else
			return ret;
	}
	
	public InputStream translateLabel(String fileName, List<String> varlines) {
		String path = fileName;		
		try {
			File file = new File(path);	
			if(!file.exists())
				return null;
	        BufferedReader in = new BufferedReader(new FileReader(path));
	        byte buffer[] = new byte[(int) (file.length() * 2)];
	        int index = 0;
	        String str;
	        while ((str = in.readLine()) != null) {
		        boolean found = true;
	        	while(found) {
		        	//procura por {
		        	int pos1 = str.indexOf('{');
		        	int pos2 = str.indexOf('}');
		        	found = false;
		        	if( pos1 > 0 && pos2 > 0) {
		        		if(varlines != null) {
		        			varlines.add(str + "\r\n");
		        		}
		        		found = true;
		        		String key = str.substring(pos1 + 1, pos2);
		        		String outLine = str.substring(0, pos1) + this.get(key) + str.substring(pos2 + 1, str.length());
		        		str = outLine;
		        	}
	        	}
        		System.arraycopy(str.getBytes(), 0, buffer, index, str.length());
        		index += str.length();	        		
        		buffer[index++] = 0x0d;
        		buffer[index++] = 0x0a;
	        }
	        in.close();	       
	        
	        ByteArrayInputStream ret = new ByteArrayInputStream(buffer, 0, index);
	        return ret;
	    } catch (IOException e) {
	    	return null;
		}
	}

	public List<String> translateVariablesOnly(List<String> varlines) {
        List<String> list = new ArrayList<String>();
        for(String str : varlines) {
	        boolean found = true;
        	while(found) {
	        	//procura por {
	        	int pos1 = str.indexOf('{');
	        	int pos2 = str.indexOf('}');
	        	found = false;
	        	if( pos1 > 0 && pos2 > 0) {
	        		found = true;
	        		String key = str.substring(pos1 + 1, pos2);
	        		String outLine = str.substring(0, pos1) +
	        		      this.get(key) + str.substring(pos2 + 1, str.length());
	        		str = outLine;
	        	}
        	}
        	list.add(str);        	
        }		
        return list;
	}
	
	public List<String> translateLabelToList(String fileName, List<String> varlines, PrinterModel printerModel, boolean isUseFormulario) 
	{
		if ( printerModel == PrinterModel.ZEBRA && isUseFormulario )
		{
			return translateLabelToListTypeZebraVariables(fileName, varlines);
		}
		else
		{
			return translateLabelToList(fileName, varlines);
		}
	}
	
	public List<String> translateLabelToListTypeZebraVariables(String fileName, List<String> varlines) 
	{
		String path = fileName;

		try 
		{
			File file = new File(path);	
			if(!file.exists()) {
				
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
	        List<String> list = new ArrayList<String>();
	        String key;
	        String outLine;
	        String str;
	        int contVars = 0;
	        
	        while ((str = in.readLine()) != null) {
		        boolean found = true;
	        	while(found) {
		        	//procura por {
		        	int pos1 = str.indexOf('{');
		        	int pos2 = str.indexOf('}');
		        	found = false;
		        	if( pos1 > 0 && pos2 > 0) 
		        	{
		        		key = str.substring(pos1 + 1, pos2);
		        		contVars++;
		        		if(varlines != null) {
//		        			varlines.add(str + "\r\n");
		        			varlines.add("^FN" + contVars + "^FD{" + key + "}^FS");
		        		}
		        		found = true;
//		        		outLine = str.substring(0, pos1) + this.get(key) + str.substring(pos2 + 1, str.length());
		        		outLine = str.substring(0, pos1) + "FN" + contVars + str.substring(pos2 + 1, str.length());
		        		str = outLine;
		        	}
	        	}
	        	
	        	list.add(str);
	        }
	        
	        in.close();	       
	        return list;
	    } 
		catch (IOException e) 
		{
			LogDeProcessamento.gravaLog("erro", e.getMessage() );
	    	return null;
		}
	}
	
	public List<String> translateLabelToList(String fileName, List<String> varlines) 
	{
		String path = fileName;

		try 
		{
			File file = new File(path);	
			if(!file.exists()) {
				
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
	        List<String> list = new ArrayList<String>();
	        String key;
	        String outLine;
	        String str;
	        
	        while ((str = in.readLine()) != null) {
		        boolean found = true;
	        	while(found) {
		        	//procura por {
		        	int pos1 = str.indexOf('{');
		        	int pos2 = str.indexOf('}');
		        	found = false;
		        	if( pos1 > 0 && pos2 > 0) {
		        		if(varlines != null) {
		        			varlines.add(str + "\r\n");
		        		}
		        		found = true;
		        		key = str.substring(pos1 + 1, pos2);
		        		outLine = str.substring(0, pos1) + this.get(key) + str.substring(pos2 + 1, str.length());
		        		str = outLine;
		        	}
	        	}
	        	
	        	list.add(str);
	        }
	        
	        in.close();	       
	        return list;
	    } 
		catch (IOException e) 
		{
			LogDeProcessamento.gravaLog("erro", e.getMessage() );
	    	return null;
		}
	}
}
