package br.com.cardeal.importacao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadFile {

	private String arquivo;	
	private ImportacaoInterface arquivoDeImportacao;

	public ReadFile( ImportacaoInterface arquivoDeImportacao ){
		 super();
		 this.arquivoDeImportacao = arquivoDeImportacao;
		 this.arquivo = arquivoDeImportacao.getFileImport();
	}	
	
	public List<String> read( boolean retornaInArray, String chave, int posicao ) throws Exception
	{
		return read( retornaInArray, chave, posicao, false );
	}
		
	public List<String> read( boolean retornaInArray, String chave, int posicao, boolean removeArchieveOnFinal ) throws FileNotFoundException, IOException
	{
		if (getArquivo() == null)
		{
			try 
			{
				throw new Exception("Arquivo de leitura não existe!");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		List<String> arrayReturn = new ArrayList<String>();
		FileReader file = null;
		
		file = new FileReader( getArquivo() );
		
		BufferedReader bufferArquivo = new BufferedReader( file );
        String linha;
        String[] tableLine;
        
		while((linha = bufferArquivo.readLine()) != null)
		{
			tableLine = linha.split(";");
			
			if ( retornaInArray )
			{
				if ( chave.equals( tableLine[posicao] ) ) arrayReturn.add( linha );
			}
			else 
			{                
		        if ( !arquivoDeImportacao.sendToSGBD(tableLine) )
		        {
		        	if ( removeArchieveOnFinal ) removeArchieveOnFinal = false;
		        	break;
		        }
			}
		}
        bufferArquivo.close();
        file.close();
        
        deleteFile( removeArchieveOnFinal );

		return arrayReturn;
        
	}
	
	public void deleteFile( boolean removeArchieveOnFinal )
	{
		File fileRemove = new File( getArquivo() );
        if ( removeArchieveOnFinal )
        {
        	fileRemove.delete();
        }
        else
        {
        	fileRemove.setLastModified(new Date().getTime());
        }
	}
	
	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}
}
