/**
	 * ANTES DE MAIS NADA:
	 * 
	 * DEVE-SE REFERENCIAR A BIBLIOTECA ITEXT NO PROJETO QUE ESTÁ ESTA CLASSE.
	 * PARA ISSO VÁ EM:
	 * 
	 * Project > Properties > Java Build Paths > Libraries...
	 * depois clique em ADD External Jars..
	 * Localize a pasta onde se encontra a biblioteca (ele está junto com o arquivo que você acabou de baixar)
	 * Selecione-a, clique em Abrir e depois em OK.
	 * 
	 *  Pronto... agora seus IMPORT'S irão funcionar normalment
*/

package br.com.cardeal.table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import br.com.cardeal.globals.GuiGlobals;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Image;

public class metodosItext {
	/**
	 * Sistema: Gerador de PDF's
	 * Desenvolvido por: Hugo Cesar Ferreira
	 * Ótimos estudos.
	 */

/*	public static void main(String args[]){

		gerarPdfSimples();
		adicionarCelulas();
		definirCelulas();
		tabelaComAlinhamento();
		tabelaRowspan();
		tabelaComImagem();

	}*/

	//Construtor
	public metodosItext()
	{

	}
	
	

	/**
	 * Método simples, que apenas gera um PDF com alguns parágrafos
	 */	
	public static void gerarPdfSimples()
	{
		//Variáveis comuns, que serão adicionadas aos parágrafos
		String titulo = "Meu primeiro PDF gerado com Java e iText";
		String parNull = "";
		String par1 = "Parágrafo 1";
		String par2 = "Parágrafo 2.";

		//Variáveis que armazenam o nome do arquivo e seu local
		String nomeDoArquivo = "pdfSimples";
		String localDoArquivo = "c://";

		/**
		 * OBS: Se não especificar o nome do arquivo, ele será gerado na pasta raiz do projeto (óbvio). ^^
		 */	

		// criação do objeto documento
		Document document = new Document();
		try 
		{
			PdfWriter.getInstance(document, new FileOutputStream(localDoArquivo+ nomeDoArquivo	+ ".pdf"));
			document.open(); //Abrindo o documento

			// adicionando os parágrafos ao documento
			document.add(new Paragraph(titulo));
			document.add(new Paragraph(parNull));
			document.add(new Paragraph(par1));
			document.add(new Paragraph(par2));		
			
			//Fechando o documento
			document.close();
		} 

		catch (DocumentException de) {
			System.err.println(de.getMessage());
		} 

		catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

	}

	// -----------------------------------------------------------------------------------//
	// -----------------------------------------------------------------------------------//

	/**
	 * Método simples, que usa o recurso de adicionar novas células a uma tabela pré-definida.
	 */
	public static void adicionarCelulas(String fileName)
	{

		//1 - Primeiro se cria o documento
		Document document = new Document();

		try {
			//2 - Depois define o nome de arquivo
			PdfWriter.getInstance(document, new FileOutputStream(fileName));

			//3 - Abre o Documento
			document.open();

			//4 - Define as colunas que terá a tabela, suas propriedades, e 'popula' a tabela.
			PdfPTable table = new PdfPTable(1);

			//Aqui se cria a nova célula, com seus devidos atributos, e a adiciona à tabela.
			PdfPCell cell = new PdfPCell(new Paragraph("Título mesclado"));
			cell.setColspan(1);
			table.addCell(cell);

			table.addCell("1.1");
			table.addCell("2.1");
			table.addCell("3.1");
			table.getDefaultCell().setGrayFill(0.8f);
			table.addCell("1.2");
			table.addCell("2.2");
			table.addCell("3.2");

			table.getDefaultCell().setGrayFill(0f);
			table.getDefaultCell().setBorderColor(new BaseColor(255, 0, 0));

			table.addCell("cell test1");
			table.getDefaultCell().setColspan(2);
			table.getDefaultCell().setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
			table.addCell("cell test2");

			document.add(table);
			
			//Fechando o documento
			document.close();
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

	}
	// -----------------------------------------------------------------------------------//
	// -----------------------------------------------------------------------------------//

	/**
	 * Método que gera uma tabela com células de tamanhos prédifinidos, diferentes umas das outras
	 */
	public static void definirCelulas(String fileName,String cCodFornec,String cPesoBruto,String cPesoLiquido,String cQtdePecas,String cPesoPallet,String cPesoStrech,String cProduto1,String cProdutos,int n)
	{

		//Aqui não só foi criado o documento, como também definido o tipo de papel que será gerado o arquivo. A4
		Document document = new Document(PageSize.A4, 36, 36, 36, 36);
		
		Font f = new Font(FontFamily.HELVETICA, 9, Font.BOLD); // modificando a fonte 
		Font f1 = new Font(FontFamily.HELVETICA, 12, Font.BOLD); // modificando a fonte 
		
		try {

			PdfWriter.getInstance(document,	new FileOutputStream(fileName));
			document.open(); //Abrindo o documento
			
			//Deve-se adicionar corretamente o endereço da imagem.
//			Image image = Image.getInstance("C:\\Leitura_Cardeal\\cardeal.jpg");
//			image.setAlignment(Element.ALIGN_LEFT);
//			document.add(image);
			
			
			//1° linha do pdf
			float[] widths = {0f, 0f, 75f, 25f}; // Largura de cada coluna

			PdfPTable table = new PdfPTable(widths);
			
//			PdfPCell header = new PdfPCell(new Paragraph("Cód.Fornecedor:")); //mudar a posição do texto dentro da tabela
//			header.setHorizontalAlignment(Element.ALIGN_RIGHT);
			
//			PdfPCell header1 = new PdfPCell(new Paragraph("                  Cód. Fornecedor:")); 
//			header1.setHorizontalAlignment(Element.ALIGN_LEFT); //mudar a posição do texto dentro da tabela
			
			PdfPCell valor1 = new PdfPCell(new Paragraph(cCodFornec,f1)); //setando a fonte modicada para celula desejada
			
			table.addCell("01");
			table.addCell("02");
			table.getDefaultCell().setBorderColor(new BaseColor(1).WHITE); //Mudando a cor da borda da célula abaixo para branco
			table.addCell("                                         Cód. Fornecedor:");
			table.getDefaultCell().setBorderColor(new BaseColor(1).BLACK); //Mudando a cor da borda da célula abaixo para preto
			table.addCell(valor1);
			table.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
			table.setWidthPercentage(60);
			document.add(table);
			
			//2° linha do pdf
			widths[0] = 0f;
			widths[1] = 0f;
			widths[2] = 75f;
			widths[3] = 25f;
			table.setWidths(widths);
			
			PdfPTable table1 = new PdfPTable(widths);
			
			PdfPCell valor2 = new PdfPCell(new Paragraph(cProduto1,f1)); //setando a fonte modicada para celula desejada
						
			table1.addCell("01");
			table1.addCell("02");
			table1.getDefaultCell().setBorderColor(new BaseColor(1).WHITE); //Mudando a cor da borda da célula abaixo para branco
			table1.addCell("                                               Cód. Produto:");
			table1.getDefaultCell().setBorderColor(new BaseColor(1).BLACK); //Mudando a cor da borda da célula abaixo para preto
			table1.addCell(valor2);
			table1.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
			table1.setWidthPercentage(60);
			document.add(table1);
			
			//3° linha do pdf
			
			widths[0] = 100f;
			widths[1] = 0f;
			widths[2] = 0f;
			widths[3] = 0f;
			table.setWidths(widths);
			
			PdfPTable table2 = new PdfPTable(widths);
			
			if(n >= 11){
				PdfPCell header = new PdfPCell(new Paragraph(cProdutos,f)); 
				header.setHorizontalAlignment(Element.ALIGN_LEFT); //mudar a posição do texto dentro da tabela
				table2.addCell(header);
				table2.addCell("02");
				table2.addCell("10");
				table2.addCell("20");
				table2.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
				table2.setWidthPercentage(60);
				document.add(table2);
			}
			else{ 
			
			PdfPCell header = new PdfPCell(new Paragraph(cProdutos,f)); 
			header.setHorizontalAlignment(Element.ALIGN_LEFT); //mudar a posição do texto dentro da tabela	
			table2.addCell(header);
			table2.addCell("02");
			table2.addCell("10");
			table2.addCell("20");
			table2.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
			table2.setWidthPercentage(60);
			document.add(table2);
			
			}
			
//			table2.setSpacingAfter(10); // verificar pular linha
			
			//4° linha do pdf
			widths[0] = 0f;
			widths[1] = 0f;
			widths[2] = 75f;
			widths[3] = 25f;
			table.setWidths(widths);
			
			PdfPTable table3 = new PdfPTable(widths);
			
			PdfPCell valor3 = new PdfPCell(new Paragraph(cPesoBruto,f1)); //setando a fonte modicada para celula desejada
			
			table3.addCell("");
			table3.addCell("");
//			table3.getDefaultCell().setBorderColor(new BaseColor(1).WHITE); //Mudando a cor da borda da célula abaixo para branco
//			table3.getDefaultCell().setBorderColorLeft(new BaseColor(1).WHITE); // verificar a retirada da borda esuqerda da linha
			table3.addCell("           Peso Bruto Carga(Produto + Pallet):");
			table3.getDefaultCell().setBorderColor(new BaseColor(1).BLACK); //Mudando a cor da borda da célula abaixo para preto
			table3.addCell(valor3);
			table3.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
			table3.setWidthPercentage(60);
			document.add(table3);
			
			//5° linha do pdf
			widths[0] = 0f;
			widths[1] = 0f;
			widths[2] = 75f;
			widths[3] = 25f;
			table.setWidths(widths);
			
			PdfPTable table4 = new PdfPTable(widths);
			
			PdfPCell valor4 = new PdfPCell(new Paragraph(cPesoLiquido,f1)); //setando a fonte modicada para celula desejada
						
			table4.addCell("");
			table4.addCell("");
			table4.getDefaultCell().setBorderColor(new BaseColor(1).WHITE); //Mudando a cor da borda da célula abaixo para branco
			table4.addCell("                                      Peso líquido carga:");
			table4.getDefaultCell().setBorderColor(new BaseColor(1).BLACK); //Mudando a cor da borda da célula abaixo para preto
			table4.addCell(valor4);
			table4.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
			table4.setWidthPercentage(60);
			document.add(table4);
			
			document.add(new Paragraph(" ")); // Pular linha no pdf
			
			//6° linha do pdf
			widths[0] = 0f;
			widths[1] = 0f;
			widths[2] = 75f;
			widths[3] = 25f;
			table.setWidths(widths);
			
			PdfPTable table5 = new PdfPTable(widths);
			
			PdfPCell valor5 = new PdfPCell(new Paragraph(cQtdePecas,f1)); //setando a fonte modicada para celula desejada
						
			table5.addCell("");
			table5.addCell("");
			table5.getDefaultCell().setBorderColor(new BaseColor(1).WHITE); //Mudando a cor da borda da célula abaixo para branco
			table5.addCell("                                      Volume em caixas:");
			table5.getDefaultCell().setBorderColor(new BaseColor(1).BLACK); //Mudando a cor da borda da célula abaixo para preto
			table5.addCell(valor5);
			table5.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
			table5.setWidthPercentage(60);
			document.add(table5);
			
			
			//7° linha do pdf
			widths[0] = 0f;
			widths[1] = 0f;
			widths[2] = 75f;
			widths[3] = 25f;
			table.setWidths(widths);
			
			PdfPTable table6 = new PdfPTable(widths);
			
			PdfPCell valor6 = new PdfPCell(new Paragraph(cPesoPallet,f1)); //setando a fonte modicada para celula desejada
						
			table6.addCell("");
			table6.addCell("");
			table6.getDefaultCell().setBorderColor(new BaseColor(1).WHITE); //Mudando a cor da borda da célula abaixo para branco
			table6.addCell("                                                           Pallet:");
			table6.getDefaultCell().setBorderColor(new BaseColor(1).BLACK); //Mudando a cor da borda da célula abaixo para preto
			table6.addCell(valor6);
			table6.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
			table6.setWidthPercentage(60);
			document.add(table6);
			
			
			//8° linha do pdf
			widths[0] = 0f;
			widths[1] = 0f;
			widths[2] = 75f;
			widths[3] = 25f;
			table.setWidths(widths);
			
			PdfPTable table7 = new PdfPTable(widths);
			
			PdfPCell valor7 = new PdfPCell(new Paragraph(cPesoStrech,f1)); //setando a fonte modicada para celula desejada
						
			table7.addCell("");
			table7.addCell("");
			table7.getDefaultCell().setBorderColor(new BaseColor(1).WHITE); //Mudando a cor da borda da célula abaixo para branco
			table7.addCell("                                   Strech / Cantoneiras:");
			table7.getDefaultCell().setBorderColor(new BaseColor(1).BLACK); //Mudando a cor da borda da célula abaixo para preto
			table7.addCell(valor7);
			table7.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
			table7.setWidthPercentage(60);
			document.add(table7);
			
			document.add(new Paragraph(" ")); // Pular linha no pdf
			
			//9° linha do pdf
			widths[0] = 100f;
			widths[1] = 0f;
			widths[2] = 0f;
			widths[3] = 0f;
			table.setWidths(widths);
			
			PdfPTable table8 = new PdfPTable(widths);
			
			table8.getDefaultCell().setBorderColor(new BaseColor(1).WHITE); //Mudando a cor da borda da célula abaixo para branco
			table8.addCell("       Obs: Anexar 01 única etiqueta em cada pallet.");
			table8.setHorizontalAlignment(Element.ALIGN_CENTER); // verificar
			table8.addCell("");
			table8.addCell("");
			table8.addCell("");
			table8.setHorizontalAlignment(Element.ALIGN_CENTER); //Alinhamento da linha do pdf no meio.
			table8.setWidthPercentage(60);
			document.add(table8);
			
			
/*			widths[0] = 0f;
			widths[1] = 0f;
			widths[2] = 75f;
			widths[3] = 25f;
			table.setWidths(widths);
			
			table.addCell("1");
			table.addCell("2");
			table.addCell("3");
			table.addCell("4");
			document.add(table);
			
			widths[0] = 50f;
			widths[1] = 0f;
			widths[2] = 0f;
			widths[3] = 50f;
			table.setWidths(widths);
			document.add(table);
			
			table.addCell("");
			table.addCell("");
			table.addCell("Cód.Fornecedor");
			table.addCell("0");
			
			
			table.addCell("");
			table.addCell("");
			table.addCell("Cód.Produto");
			table.addCell("101");
			
			document.add(table);
			widths[0] = 100f;
			widths[1] = 0f;
			widths[2] = 0f;
			widths[3] = 0f;
			table.setWidths(widths);
			table.addCell("Teste 123");
			
			document.add(table);
			
			//Adicionando um parágrafo apenas para pular linha
/*			document.add(new Paragraph("Mudando a Porcentagem:\n\n"));

			widths[0] = 20f;
			widths[1] = 20f;
			widths[2] = 10f;
			widths[3] = 50f;
			table.setWidths(widths);
			document.add(table);

			widths[0] = 40f;
			widths[1] = 40f;
			widths[2] = 20f;
			widths[3] = 300f;
			//Rectangle r = new Rectangle(PageSize.A4.getRight(72), PageSize.A4.getTop(72));
			//table.setWidthPercentage(widths, r);
			document.add(new Paragraph("Usando uma porcentagem de largura absoluta:\n\n"));
			document.add(table);
			document.add(new Paragraph("Definindo sem Porcentagem:\n\n"));
			table.setTotalWidth(300);
			//table.setLockedWidth(true);
			document.add(table);*/
			
			//Fechando o documento
			document.close();
			
			//Mensagem que aparece no console caso tudo dê certo
			GuiGlobals.showMessage("Arquivo pdf gerado com sucesso!");
			
		} catch (Exception de) {
			de.printStackTrace();
		}

	}
	// -----------------------------------------------------------------------------------//
	// -----------------------------------------------------------------------------------//


	/**
	 * Método que gera um documento com tabelas alinhadas diferentemente.
	 */
	public static void tabelaComAlinhamento(String fileName)
	{

		//O PageSize é pra definir o tamanho que serão geradas as páginas
		Document document = new Document(PageSize.A4);

		try
		{
			
			File file = new File( fileName );
			document = new Document();
			
			OutputStream out = new FileOutputStream(file);
			PdfWriter.getInstance(document, out);
			document.open();
			
//			PdfPTable table = new PdfPTable(1);

			//Definindo a cor da borda da tabela
//			table.getDefaultCell().setBorderColor(new BaseColor(0,0,0));

			/**
			 * Se for adicionada uma nova célula a tabela, deve-se especificar novamente para esta célula
			 * todos os atributos que deseja mudar
			 */
		
			/**PdfPCell cell = new PdfPCell(new Paragraph("Título com colspan=3"));
			cell.setColspan(3);
			cell.setBorderColor(new BaseColor(0,0,0));*/


			float[] widths = {0.1f};
			PdfPTable table = new PdfPTable(widths);
//			table.addCell(cell);
			table.addCell("1.1");
			table.addCell("2.1");
			table.addCell("3.1");
			table.addCell("1.2");
			table.addCell("2.2");
			table.addCell("3.2");
			table.addCell("1.1");
			table.addCell("1.1");
		
			
			widths[0] = 20f;
			widths[1] = 20f;
			widths[2] = 10f;
			widths[3] = 50f;
			table.setWidths(widths);
//			document.add(table);

/*			//Definindo uma nova célula com borda colorida
			cell = new PdfPCell(new Paragraph("Célula com borda vermelha"));
			cell.setBorderColor(new BaseColor(255, 0, 0));
			table.addCell(cell);

			//Definindo uma nova célula com fundo Cinza e mesclada em duas colunas
			cell = new PdfPCell(new Paragraph("Célula com fundo Cinza e colspan=2"));
			cell.setColspan(2);
			cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
			table.addCell(cell);
			document.add(table); 

			//Adicionando a tabela ocupando 100% do documento
			table.setWidthPercentage(100);
			document.add(table);*/

			//Adicionando a tabela ocupando 50% do documento e alinhada a direita.
			table.setWidthPercentage(50);
			table.setHorizontalAlignment(Element.ALIGN_RIGHT);
			document.add(table);

/*			//Adicionando a tabela alinhada a Esquerda, sem nenhum redimensionamento
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			document.add(table); */
			
			//Fechando o documento
			document.close();			

		} catch (Exception erro) {
			erro.printStackTrace();
		}

		
	}
	// -----------------------------------------------------------------------------------//
	// -----------------------------------------------------------------------------------//

	/**
	 * Método que gera uma tabela dentro da outra, fazendo um tipo de Rowspan
	 */
	public static void tabelaRowspan(String fileName ){
		
		Document document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
		try {				
		
			File file = new File( fileName );
			document = new Document();
			
			OutputStream out = new FileOutputStream(file);
			PdfWriter.getInstance(document, out);
			document.open();

//			PdfWriter.getInstance(document,	new FileOutputStream("Tabela dentro de Tabela.pdf"));
//			document.open(); //Abrindo o documento


			PdfPTable table = new PdfPTable(4);
			PdfPTable nested1 = new PdfPTable(2);
			nested1.addCell("1.1");
			nested1.addCell("1.2");
			PdfPTable nested2 = new PdfPTable(1);
			nested2.addCell("2.1");
			nested2.addCell("2.2");

			for (int k = 0; k < 24; ++k) {
				if (k == 1) {
					table.addCell(nested1);
				}
				else if (k == 20) {
					table.addCell(nested2);
				}
				else {
					table.addCell("cell " + k);
				}
			}

			document.add(table);

			//Fechando o documento
			document.close();

		} catch (Exception de) {
			de.printStackTrace();
		}

	}
	// -----------------------------------------------------------------------------------//
	// -----------------------------------------------------------------------------------//

	/**
	 * Método que gera uma tabela com imagem imbutida.
	 */
	public static void tabelaComImagem(String fileFullName)
	{

		Document document = new Document();

		try {
			PdfWriter.getInstance(document, new FileOutputStream(fileFullName));
			document.open(); //Abrindo o documento

			//Deve-se adicionar corretamente o endereço da imagem.
			Image image = Image.getInstance("C:\\Leitura_Cardeal\\cardeal.jpg");

			/**
			 * Aqui é uma forma diferente de definir quantas linhas e colunas terá..
			 * faz isso e adiciona no PdfPTable();
			 * ficanso assim ==> PdfPTable table = new PdfPTable(widths);
			 */
			//float[] widths = {0f, 0f};

			//Definindo a tabela com duas colunas
			PdfPTable table = new PdfPTable(1);
			
			PdfPTable table1 = new PdfPTable(2);

			//Para inserir cor na borda da tabela, deve-se importar a classe Color
			table.getDefaultCell().setBorderColor(BaseColor.BLUE);
			
//			table.addCell("Esse é meu Cachorro");
			table.addCell(image);
			table.setWidthPercentage(10);
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			
			table1.addCell("Esse é meu Cachorro");
			table1.addCell(image);

			/**
				table.addCell("This two");
				table.addCell(new PdfPCell(image, true));

				table.addCell("This three");
				table.addCell(new PdfPCell(image, false));*/
			document.add(table);
			document.add(table1);
			
			//Fechando o documento
			document.close();		
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		
	}
	// -----------------------------------------------------------------------------------//
	// -----------------------------------------------------------------------------------//

	/**
	 * Método que gera uma tabela simples, instanciada da classe Table.
	 * Não tem tantos recursos quanto a PdfPTable, mas é bom pra se ter uma noção básica
	 */
	public static void tabelaSimples() 
	{
		
		
		Document document = new Document();

		try {

			PdfWriter.getInstance(document, new FileOutputStream("tabelaSimples.pdf"));
			document.open();

			// adicionando os parágrafo ao documento
			document.add(new Paragraph("DADOS DO CONTATO:"));

			// step 4: we create a table and add it to the document
			PdfPTable table = new PdfPTable(2); // 2 rows, 2 columns
			table.addCell("0.0");
			table.addCell("0.1");
			table.addCell("1.0");
			table.addCell("1.1");
			document.add(table);
			
			//Fechando o documento
			document.close();			

		}
		catch (DocumentException de) {
			System.err.println(de.getMessage());
		}
		catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		
	}
}


