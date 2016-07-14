package br.com.cardeal.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;

public class LogDeProcessamento 
{
	
	private static String diretorioDefault;
	private static String defaultExtension = ".txt";
	
	public static String criaDiretorio(String raiz, String novoDiretorio){  
		   
		String nomeDiretorio = null;
	    String separador = java.io.File.separator;   
	    
	    nomeDiretorio = raiz + separador + novoDiretorio + separador;   
	    if (!new File(nomeDiretorio).exists()) { // Verifica se o diretório existe.   
	    	(new File(nomeDiretorio)).mkdir();   // Cria o diretório   
	    }   
	    
	    return nomeDiretorio;
	}
	
	public static void gravaLog( String arquivoLog, String texto) {
		gravaLog( arquivoLog, texto, false);
	}
	
	public static PrintWriter getPrinterWritter( String arquivoLog ) 
	{
		String arquivo = getArchieve( arquivoLog );
		FileWriter arq;
		PrintWriter gravarArq = null;
		try 
		{
			arq = new FileWriter(arquivo, true);
			gravarArq = new PrintWriter(arq);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return gravarArq;
	}
	
	private static String getArchieve( String arquivoLog ) 
	{
		//-------------------------------------------------
		//- Buscando data atual e transformando em String -
		//-------------------------------------------------
		String dataString = null;
		try{
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date today = Calendar.getInstance().getTime();        
			dataString = df.format(today);
		}
		catch (Exception e) {
			System.out.println("Falha no parser da data para String: " + e.toString());
		}
		
		String nomeArquivo;
		String raiz;
		String newDirectory;
		
		if (arquivoLog != null){
			nomeArquivo = arquivoLog + defaultExtension;
		}
		else{
			nomeArquivo = dataString+"_log_processamento" + defaultExtension;
		}
		
		raiz = getTempPath();
		newDirectory = "logs";
		
		try 
		{
			diretorioDefault = criaDiretorio(raiz, newDirectory);
		} 
		catch (Exception ex) 
		{   
			System.out.println("Erro ao criar o diretório" + ex.toString());
	    }
		
		return diretorioDefault+nomeArquivo;
	}

	private static String getTempPath() 
	{
		String path = Setup.getTempPath();
		
		if ( path == null || path.isEmpty() )
		{
			path = "c:\\temp";
		}
		
		return path;
	}

	public static void gravaLog( String arquivoLog, String texto, boolean printHora) 
	{
		if ( printHora )
		{
			texto = "=== " + getHora( true ) + " === " + texto;
		}
		
		if ( isLogProcess() ) 
		{
			try
			{
				String arquivo = getArchieve(arquivoLog);
				
				FileWriter arq = new FileWriter(arquivo, true);
				PrintWriter gravarArq = new PrintWriter(arq);
				
				if ( printHora )
				{
					gravarArq.println("");
				}
				gravarArq.println(texto);
				gravarArq.close();
				arq.close();
			}
			catch (IOException e){
				System.out.println("Erro na criacao do arquivo:" + e.toString());
			}
			
		}
		else{
			System.out.println(texto);
		}
		
	}
	
	private static boolean isLogProcess()
	{
		return ( "S".equals( GuiGlobals.getLogProcess().toUpperCase().trim() ) );
	}
	
	public static String getHora( boolean separaComPontos ) {  
	      
	    // cria um StringBuilder  
	    StringBuilder sb = new StringBuilder(); 
	    int seconds;
	    String secondsFormat;
	  
	    // cria um GregorianCalendar que vai conter a hora atual  
	    GregorianCalendar d = new GregorianCalendar();  
	      
	    // anexa do StringBuilder os dados da hora  
	    sb.append( d.get( GregorianCalendar.HOUR_OF_DAY ) );  
	    if ( separaComPontos ){
	    	sb.append( ":" );
	    }
	    sb.append( d.get( GregorianCalendar.MINUTE ) );  
	    if ( separaComPontos ){
	    	sb.append( ":" );
	    }  
	    seconds = d.get( GregorianCalendar.SECOND );
	    secondsFormat = ""+seconds;
	    if (seconds<10){
	    	secondsFormat = "0"+seconds;
	    }
	    
	    sb.append( secondsFormat );  
	      
	    // retorna a String do StringBuilder  
	    return sb.toString();  
	      
	}

	public static String getDiretorioDefault() {
		return diretorioDefault;
	}

	public static String getDefaultExtension() {
		return defaultExtension;
	} 

}
