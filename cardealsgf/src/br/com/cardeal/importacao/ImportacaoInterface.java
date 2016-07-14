package br.com.cardeal.importacao;

public interface ImportacaoInterface {
		
	String getFileImport();
	boolean sendToSGBD( String[] dados);
	void execute(boolean removeOnFinal);
	
}
