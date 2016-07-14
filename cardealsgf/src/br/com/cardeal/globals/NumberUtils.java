package br.com.cardeal.globals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang.StringUtils;

public class NumberUtils {

	public static double truncate(Double valor, int precisao) {  
        BigDecimal bd = BigDecimal.valueOf(valor);  
        bd = bd.setScale(precisao, BigDecimal.ROUND_DOWN);  
  
        return bd.doubleValue();  
    }  
	
	/**
	 * Este metodo transforma um numero double em string, completando com zeros a esquerda nas dezenas e zeros a direita nos decimais conforme parametros enviados
	 * Também troca ponto por virgula. Util para geracao de arquivos txt
	 */
	
	public static String transform(Double valor, int dezenas, int decimais) 
	{
		return transform(valor, dezenas, decimais, true, false);
	}
	
	public static String transform(Double valor, int dezenas, int decimais, boolean zerosLeft, boolean splitWithDot) {
		
		String valueTransform = String.valueOf(valor).trim();
		int posicaoPonto = valueTransform.indexOf(".");
		String valueFormated = "";
		
		if ( valueTransform.substring(0, 1).equals(".") ){
			valueTransform = "0".concat( valueTransform );
		}
		
		if ( zerosLeft )
		{
			valueFormated += StringUtils.leftPad( valueTransform.substring(0,posicaoPonto), dezenas, "0" ) + ".";
		}
		else
		{
			valueFormated += valueTransform.substring(0, posicaoPonto+1);
		}
		
		valueFormated += StringUtils.rightPad( valueTransform.substring(posicaoPonto+1,valueTransform.length()), decimais, "0" );
		valueFormated = valueFormated.replace(".", ( ( splitWithDot ) ? "." : "," ) );
		
		return valueFormated;
		
	}
	
	public static double	roundNumber( double number, int decimals ){
		
		BigDecimal bd = new BigDecimal( number ).setScale(decimals, RoundingMode.HALF_EVEN);
		return bd.doubleValue();
		
	}
	
}
