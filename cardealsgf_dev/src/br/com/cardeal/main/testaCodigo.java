package br.com.cardeal.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.cardeal.globals.HibernateUtil;
import br.com.cardeal.model.Stock;
import br.com.cardeal.printer.PrintSerial;

public class testaCodigo 
{

	public static void main(String... args)
	{
		//testingPrinter();
		testHBN();
	}
	
	
	private static void testHBN(){
		
		Session session = HibernateUtil.getSession();
		
		Criteria c = session.createCriteria(Stock.class, "stock");	
		c.createAlias("product","prod",Criteria.INNER_JOIN);
		
		c.add(Restrictions.ge("prod.idMasc",0));
		c.add(Restrictions.le("prod.idMasc", 109));
		
		/*
		c.add(Restrictions.ge("product.id",0));
			
		c.add(Restrictions.ge("product.id",0));  // ok para idMasc
				
		c.add(Restrictions.le("product.id", 109)); // ok para idMasc
		*/
		
		c.addOrder(Order.asc("id"));
		c.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		
		String teste = "01";
		
		teste = teste;
	}
	
	
	
	
	private static void testingPrinter() 
	{
		int qtyTickets = 6;
		String port = "COM1";
		boolean all = false;
		
		List<String> conteudoEtiqueta = new ArrayList<String>();
		
		/**
		 * Configurando o layout a ser utilizado pela impressora com a definição das variaveis
		 */
		String etiqueta = ""
		.concat("^XA")
		.concat("^CON") // Desabilitando o cache
		.concat("^DFFORMAT^FS")
		.concat("^PW791")
		.concat("^FT35,59^A0N,41,108^FH_^FN1^FS")
		.concat("^FO26,13^GB715,494,2^FS")
		.concat("^FO224,163^GB0,63,1^FS")
		.concat("^FO26,86^GB566,0,1^FS")
		.concat("^FO411,164^GB0,62,1^FS")
		.concat("^FT286,35^A0N,18,31^FH_^FDPALLET^FS")
		.concat("^FO434,14^GB0,67,1^FS")
		.concat("^FO275,15^GB0,72,1^FS")
		.concat("^FT287,67^A0N,25,45^FH_^FN2^FS")
		.concat("^FO593,472^GB148,0,1^FS")
		.concat("^FO411,229^GB0,68,1^FS")
		.concat("^FO224,229^GB0,67,1^FS")
		.concat("^FT443,34^A0N,17,26^FH_^FDLOTE^FS")
		.concat("^FO28,296^GB564,0,1^FS")
		.concat("^FT33,216^A0N,25,28^FH_^FN3^FS")
		.concat("^FT421,281^A0N,25,28^FH_^FN4^FS")
		.concat("^FT233,281^A0N,25,28^FH_^FN5 kg^FS")
		.concat("^FT33,281^A0N,25,28^FH_^FN6 kg^FS")
		.concat("^FT233,216^A0N,25,28^FH_^FN7^FS")
		.concat("^FT33,182^A0N,16,16^FH_^FDDATA DE FABRICAÇÃO^FS")
		.concat("^FT443,68^A0N,25,43^FH_^FN8^FS")
		.concat("^FO26,161^GB566,0,1^FS")
		.concat("^FT233,247^A0N,13,28^FH_^FDTARA ^FS")
		.concat("^FO28,327^GB564,0,1^FS")
		.concat("^FO28,377^GB563,0,1^FS")
		.concat("^FT233,182^A0N,16,16^FH_^FDDATA DE VALIDADE^FS")
		.concat("^FO28,228^GB565,0,1^FS")
		.concat("^FT418,182^A0N,17,21^FH_^FN9^FS")
		.concat("^FT33,248^A0N,14,19^FH_^FDPESO BRUTO^FS")
		.concat("^FT418,208^A0N,16,26^FH_^FN10^FS")
		.concat("^FT421,249^A0N,15,19^FH_^FDPESO LIQUIDO^FS")
		.concat("^FT35,316^A0N,20,21^FH_^FN11^FS")
		.concat("^BY4,3,71^FT55,455^BCN,,Y,N")
		.concat("^FN12^FS")
		.concat("^FT28,369^A0N,20,21^FH_^FN13^FS")
		.concat("^FT30,343^A0N,20,21^FH_^FN14^FS")
		.concat("^FT31,152^A0N,28,28^FH_^FN15^FS")
		.concat("^FO592,15^GB0,492,1^FS")
		.concat("^BY3,3,107^FT709,446^BCB,,Y,N")
		.concat("^FN16^FS")
		.concat("^FT598,494^A0N,17,24^FH_^FN17^FS")
		.concat("^FT423,477^A0N,54,45^FH_^FN18^FS")
		.concat("^FT32,117^A0N,28,28^FH_^FN19^FS")
		.concat("^PQ1,0,1,Y")
		.concat("^XZ");
		conteudoEtiqueta.add(etiqueta);
		
		if ( !all )
		{
			PrintSerial serial = new PrintSerial();	
			serial.printZebra(port, conteudoEtiqueta);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
		/**
		 * Configurando variaveis para ser utilizado no ultimo layout enviado para a impressora
		 */
		for ( int i = 0; i < qtyTickets; i++)
		{
			etiqueta = ""
			.concat("^XA")
			.concat("^CON") // Desabilitando o cache
			.concat("^XFFORMAT^FS")
			.concat("^FN1^FD1105^FS")
			.concat("^FN2^FD---^FS")
			.concat("^FN3^FD03/04/2016^FS")
			.concat("^FN4^FD0,900^FS")
			.concat("^FN5^FD0,175^FS")
			.concat("^FN6^FD1,075^FS")
			.concat("^FN7^FD02/04/2017^FS")
			.concat("^FN8^FD000007^FS")
			.concat("^FN9^FD001 CX C/020 UNI.^FS")
			.concat("^FN10^FDDE 45 g^FS")
			.concat("^FN11^FDMantenha em local seco, fresco e arejado^FS")
			.concat("^FN12^FD>;" + StringUtils.leftPad( String.valueOf(i), 8, "0") + "01^FS")
			.concat("^FN13^FD^FS")
			.concat("^FN14^FDPRODUTO ISENTO DE REGISTRO NO MINISTÉRIO DA AGRICULTURA^FS")
			.concat("^FN15^FD^FS")
			.concat("^FN16^FD>;>817896447101826^FS")
			.concat("^FN17^FD9502F 19:25 1^FS")
			.concat("^FN18^FD001 CX^FS")
			.concat("^FN19^FDMINGAU-GATO SABOR PEIXE^FS")
			.concat("^XZ");
			conteudoEtiqueta.add(etiqueta);
			
			if ( !all )
			{
				PrintSerial serial = new PrintSerial();	
				serial.printZebra(port, conteudoEtiqueta);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
		}

		if ( all )
		{
			PrintSerial serial = new PrintSerial();	
			serial.printZebra(port, conteudoEtiqueta);
		}
		
		
	}				
}