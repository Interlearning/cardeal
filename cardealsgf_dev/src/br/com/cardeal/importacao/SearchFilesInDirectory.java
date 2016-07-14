package br.com.cardeal.importacao;

import java.io.File;

public class SearchFilesInDirectory {

	private String path;
	
	public SearchFilesInDirectory( String path ){
		this.path = path;
	}
	
	public File[] getFilesInDirectory(){
		
		if( path != null ){
			
			File diretorio = new File( path );
			return diretorio.listFiles();
			
		}
		
		return null;
		
	}
	
}
