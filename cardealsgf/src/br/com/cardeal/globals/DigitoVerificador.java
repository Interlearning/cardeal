package br.com.cardeal.globals;

import java.util.LinkedList;

/**
 * 
 * @author Sergio Artero
 *
 */
public class DigitoVerificador {

	private String digitos;
	private boolean onlyUseFormula = false;
	private LinkedList<Integer> valorInteiros = new LinkedList<Integer>();
	private int tipo = 0;
	public final static int EAN13 = 1;
	public final static int DUN14 = 2;
	
	/**
	 * 
	 * @param digitosEAN13
	 */	
	public DigitoVerificador( String digitos, int tipo ){
		
		/**
		 * Valida pre-requisitos para o funcionamento correto
		 */
		if ( validData( digitos, tipo ) ){
			this.digitos = digitos.trim();
		}
		
	}
	
	public DigitoVerificador( String digitos, int tipo, boolean onlyUseFormula ){
		
		this.onlyUseFormula = onlyUseFormula;
		
		/**
		 * Valida pre-requisitos para o funcionamento correto
		 */
		if ( validData( digitos, tipo ) ){
			this.digitos = digitos.trim();
		}
		
	}
	
	@SuppressWarnings("unused")
	private boolean validData( String digitos, int tipo ){

		boolean allOk = true;
		int qtyDigitos;
		char[] arrayValores = digitos.trim().toCharArray();
		
		if ( digitos == null ){
			allOk = false;
			System.out.println("Parâmetro não informado!");
		}
		
		if ( allOk && tipo != EAN13 && tipo != DUN14 ){
			allOk = false;
			System.out.println("Tipo de cálculo não informado: EAN13 = " + EAN13 + " DUN14 = " + DUN14);
		}
		else{
			this.tipo = tipo;
		}		
		
		if( allOk && !onlyUseFormula ) {
			
			qtyDigitos = ( isEAN13() ) ? 12 : 13;
			
			if (arrayValores.length != qtyDigitos ){
				System.out.println("É necessário " + qtyDigitos + " digitos para calcular. Foi informado " + arrayValores.length + " digitos.");
				allOk = false;
			}
		}
		
		if ( allOk ){
		
			for (char c : arrayValores) {
				
				if ( String.valueOf(c).matches( "[^0-9]" ) ){
					System.out.println("Existem caracteres inválidos nos digitos informados no parâmetro: '" + digitos + "' => '" + String.valueOf(c) + "'");
					allOk = false;
					break;
				}
				else{
					valorInteiros.add( Integer.parseInt( String.valueOf(c) ) );
				}
				
			}
			
		}
		
		return allOk;
		
	}
	
	/**
	 * 
	 * @return codigo EAN 13 / DUN 14
	 */
	public String getCodigo(){
		return digitos + getDigitoVerificador();
	}		

	public int getDigitoVerificador() {
		
		int soma = 0;
		int digitoVerificador = 0;
		
		/**
		 * 1º parte da formula
		 */
		soma = getSoma();
		
		/**
		 * 2º parte da formula
		 * Formula para encontrar o digito verificado a partir da somatoria no passo anterior
		 */
		if( soma % 10 != 0 ){
			digitoVerificador = ( ( (soma / 10) + 1 ) * 10 ) - soma;
		}
		
		return digitoVerificador;
		
	}
	
	/**
	 * Contem a primeira parte da regra para encontrar o digito verificador
	 * @return soma da multiplicacao dos numeros, conforme regras
	 */
	private int getSoma(){
		
		int soma = 0;
		int imparOuPar = 1;
		int multiplicadorImpar = ( ( tipo == EAN13 ) ? 1 : 3 );
		int multiplicadorPar = ( ( tipo == EAN13 ) ? 3 : 1 );
		
		for (int digito : valorInteiros) {
			
			if(imparOuPar % 2 == 0){
				soma += ( digito * multiplicadorPar );				
			}else{			
				soma += ( digito * multiplicadorImpar );
			}
			
			imparOuPar++;
			
		}
		
		return soma;
		
	}
	
	public boolean isEAN13(){
		return tipo == EAN13;
	}
	
	public boolean isDUN14(){
		return tipo == DUN14;
	}
	
	private boolean isOk(){
		return digitos != null && tipo != 0;
	}

	public void imprimiDados(){
		
		if ( isOk() ){
			System.out.println("Seu código calculado é: " + getCodigo() );
		}
	}

	public boolean isOnlyUseFormula() {
		return onlyUseFormula;
	}

	public void setOnlyUseFormula(boolean onlyUseFormula) {
		this.onlyUseFormula = onlyUseFormula;
	}
	
}
