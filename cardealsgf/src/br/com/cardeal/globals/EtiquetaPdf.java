package br.com.cardeal.globals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.cardeal.log.LogDeProcessamento;

public class EtiquetaPdf {
		
	private static int FILE_NOT_EXISTS = -1;
	private static int DOCUMENT_NOT_CREATE = -2;
	private static int ERROR_EDIT_PDF = -3;
	private static int STATE = 0;
	
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font fontSimple = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
//	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
//	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	
	private File file = null;
	private Document document = null;
	private OutputStream out = null;
	private PdfPTable table;
  	
	public EtiquetaPdf( String fileName ){
		
		file = new File( fileName );
		document = new Document();
		
		try {
			
			out = new FileOutputStream(file);
			PdfWriter.getInstance(document, out);
			document.open();
			
		} catch (FileNotFoundException e) {
			STATE = FILE_NOT_EXISTS;
			classDestroy();
			e.printStackTrace();
			LogDeProcessamento.gravaLog( "console", e.getClass().getName() + "\n" + e.getMessage(), true);
			
		} catch (DocumentException e) {
			STATE = DOCUMENT_NOT_CREATE;
			classDestroy();
			e.printStackTrace();
			LogDeProcessamento.gravaLog( "console", e.getClass().getName() + "\n" + e.getMessage(), true);
		}
		
	}
	
	public void addMetaData(String title, String keyWords){

		try{
			Image figura = Image.getInstance("C:\\Leitura_Cardeal\\cardeal.jpg");
			document.add(figura);
		}catch(Exception e){
			e.printStackTrace();
			LogDeProcessamento.gravaLog( "console", e.getClass().getName() + "\n" + e.getMessage(), true);
		}
		
		
		document.addTitle(title);
		document.addKeywords( (keyWords!=null)?keyWords:"relatório");
		document.addAuthor("Sistema Cardeal SGF");
		document.addCreator("Sistema Cardeal SGF");
	
	}
	
	public void addTitlePage( String title ){
		
		Paragraph preface = new Paragraph();
		
	    addEmptyLine(preface, 1);
	    preface.add(new Paragraph(title, catFont));
	    try {
			document.add(preface);
		} catch (DocumentException e) {
			STATE = ERROR_EDIT_PDF;
			classDestroy();
			e.printStackTrace();
			LogDeProcessamento.gravaLog( "console", e.getClass().getName() + "\n" + e.getMessage(), true);
		}
		
	}
	
	public void newPage(){
		document.newPage();
	}
	
	private static void addEmptyLine(Paragraph paragraph, int number) {
	    
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
	public void classDestroy(){
		file = null;
		if ( document != null ){
			document.close();
		}
		document = null;
		out = null;
	}
	
	public void addSay(String title, Font font, boolean isBold, boolean addEmptyLine, int qtdLinhas){
		
		Font fontChoice = (font != null) ? font : fontSimple;
		
		if ( isBold ) fontChoice.setStyle(Font.BOLD);
		
		Paragraph paragraph = new Paragraph(title, fontChoice);
		
		if ( addEmptyLine )  addEmptyLine(paragraph, qtdLinhas);
	    
	    try {
	    	document.add(paragraph);	    	
		} catch (DocumentException e) {
			STATE = ERROR_EDIT_PDF;
			classDestroy();
			e.printStackTrace();	
			LogDeProcessamento.gravaLog( "console", e.getClass().getName() + "\n" + e.getMessage(), true);
		}
	}
	
	public void printTable( String title, ArrayList<String> campos, String[][] dados ){
	    
		addSay(title, subFont, false, true, 2);		
	    table = new PdfPTable(1);	
	    
	    addHeaderTable( campos );
//	    addRowsTable( dados );    

	    try {	    	
	    	document.add(table);
		} catch (DocumentException e) {
			STATE = ERROR_EDIT_PDF;
			classDestroy();
			e.printStackTrace();
			LogDeProcessamento.gravaLog( "console", e.getClass().getName() + "\n" + e.getMessage(), true);
		}
		
	}
	
	public void print(){
		document.close();
	}
	
	public int getState(){
		return STATE;
	}
	
	private void addHeaderTable( ArrayList<String> campos ) {
		
		for (int i = 0; i < campos.size(); i++) {	    	
	    	
	    	PdfPCell cell = new PdfPCell(new Phrase( campos.get( i ) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	    }
		
//      table.setHeaderRows(1);
		table.setWidthPercentage(30.0f);
	    table.setHorizontalAlignment(Element.ALIGN_CENTER);
	    
//	    Rectangle rect = new Rectangle(595, 841);
//	    Document document = new Document(rect)
	    
		
	}
	/*
	private void addRowsTable( List<StockTotal> total ){
		
		for (int i = 0; i < total.size(); i++) {	    	
	    	
			table.addCell(total.get( i ).getProduct().getIdMasc());
			table.addCell(total.get( i ).getProduct().getDescription());
			table.addCell(String.valueOf(total.get( i ).getSecondaryQty())); 
			table.addCell(String.valueOf(total.get( i ).getPrimaryQty()));
			table.addCell(String.valueOf(total.get( i ).getNet()));
			
	    }
		
	}
	*/
	
//  public static void main(String[] args) {
//    try {
//    	File file = new File(FILE);
//    	OutputStream out = new FileOutputStream(file);
//      Document document = new Document();
//      PdfWriter.getInstance(document, out);
//      document.open();
//      addMetaData(document);
//      addTitlePage(document);
//      addContent(document);
//      document.close();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }

  // iText allows to add metadata to the PDF which can be viewed in your Adobe
  // Reader
  // under File -> Properties
//  private static void addMetaData(Document document) {
//    document.addTitle("My first PDF");
//    document.addSubject("Using iText");
//    document.addKeywords("Java, PDF, iText");
//    document.addAuthor("Lars Vogel");
//    document.addCreator("Lars Vogel");
//  }

//  private static void addTitlePage(Document document)
//      throws DocumentException {
//    Paragraph preface = new Paragraph();
//    // We add one empty line
//    addEmptyLine(preface, 1);
//    // Lets write a big header
//    preface.add(new Paragraph("Title of the document", catFont));
//
//    addEmptyLine(preface, 1);
//    // Will create: Report generated by: _name, _date
//    preface.add(new Paragraph("Report generated by: " + System.getProperty("user.name") + ", " + new Date(), smallBold));
//    addEmptyLine(preface, 3);
//    preface.add(new Paragraph("This document describes something which is very important ", smallBold));
//
//    addEmptyLine(preface, 8);
//
//    preface.add(new Paragraph("This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).", redFont));
//
//    document.add(preface);
//    // Start a new page
//    document.newPage();
//  }

//  private static void addContent(Document document) throws DocumentException {
//    Anchor anchor = new Anchor("First Chapter", catFont);
//    anchor.setName("First Chapter");
//
//    // Second parameter is the number of the chapter
//    Chapter catPart = new Chapter(new Paragraph(anchor), 1);
//
//    Paragraph subPara = new Paragraph("Subcategory 1", subFont);
//    Section subCatPart = catPart.addSection(subPara);
//    subCatPart.add(new Paragraph("Hello"));
//
//    subPara = new Paragraph("Subcategory 2", subFont);
//    subCatPart = catPart.addSection(subPara);
//    subCatPart.add(new Paragraph("Paragraph 1"));
//    subCatPart.add(new Paragraph("Paragraph 2"));
//    subCatPart.add(new Paragraph("Paragraph 3"));
//
//    // add a list
//    createList(subCatPart);
//    Paragraph paragraph = new Paragraph();
//    addEmptyLine(paragraph, 5);
//    subCatPart.add(paragraph);
//
//    // add a table
//    createTable(subCatPart);
//
//    // now add all this to the document
//    document.add(catPart);
//
//    // Next section
//    anchor = new Anchor("Second Chapter", catFont);
//    anchor.setName("Second Chapter");
//
//    // Second parameter is the number of the chapter
//    catPart = new Chapter(new Paragraph(anchor), 1);
//
//    subPara = new Paragraph("Subcategory", subFont);
//    subCatPart = catPart.addSection(subPara);
//    subCatPart.add(new Paragraph("This is a very important message"));
//
//    // now add all this to the document
//    document.add(catPart);
//
//  }

//  private static void createTable(Section subCatPart)
//      throws BadElementException {
//    PdfPTable table = new PdfPTable(3);
//
//    // t.setBorderColor(BaseColor.GRAY);
//    // t.setPadding(4);
//    // t.setSpacing(4);
//    // t.setBorderWidth(1);
//
//    PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
//    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//    table.addCell(c1);
//
//    c1 = new PdfPCell(new Phrase("Table Header 2"));
//    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//    table.addCell(c1);
//
//    c1 = new PdfPCell(new Phrase("Table Header 3"));
//    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//    table.addCell(c1);
//    table.setHeaderRows(1);
//
//    table.addCell("1.0");
//    table.addCell("1.1");
//    table.addCell("1.2");
//    table.addCell("2.1");
//    table.addCell("2.2");
//    table.addCell("2.3");
//
//    subCatPart.add(table);
//
//  }

//  private static void createList(Section subCatPart) {
//    List list = new List(true, false, 10);
//    list.add(new ListItem("First point"));
//    list.add(new ListItem("Second point"));
//    list.add(new ListItem("Third point"));
//    subCatPart.add(list);
//  }
} 