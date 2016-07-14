package br.com.cardeal.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.lang.StringUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;

public class ShipmentArchieveTot {

	private String idOrderImport;
	private int itemOnOrder;
	private String idMascProduct;
	private double net;
	private double primaryQty;
	private String unitIssued;
	private String nameArqTot;
	private String dirArq;
	private FileWriter arqTot;
	private PrintWriter gravarArqTot;
		
	public ShipmentArchieveTot( String idOrderImport ) throws IOException{
		this.idOrderImport = idOrderImport;
		nameArqTot = this.idOrderImport +".tot";
		dirArq = getDirArq();
		arqTot = new FileWriter(dirArq + nameArqTot,true);
		gravarArqTot = new PrintWriter(arqTot);
	}
	
	public String getIdOrderImport() {
		return idOrderImport;
	}

	public void setIdOrderImport(String idOrderImport) {
		this.idOrderImport = idOrderImport;
	}

	public int getItemOnOrder() {
		return itemOnOrder;
	}
	
	public String getItemOnOrderFormat() {
		return StringUtils.leftPad( String.valueOf( itemOnOrder ), 4, "0" );
	}

	public void setItemOnOrder(int itemOnOrder) {
		this.itemOnOrder = itemOnOrder;
	}

	public String getIdMascProduct() {
		return idMascProduct;
	}

	public void setIdMascProduct(String idMascProduct) {
		this.idMascProduct = idMascProduct;
	}

	public double getNet() {
		return net;
	}
	
	public String getNetFormat() {
		return NumberUtils.transform(NumberUtils.roundNumber(net, 3), 5, 3 );
	}	

	public void setNet(double net) {
		this.net = net;
	}

	public double getPrimaryQty() {
		return primaryQty;
	}
	
	public String getPrimaryQtyFormat() {
		/**
		 * Garantindo que o numero será inteiro
		 */
		String value = String.valueOf( getPrimaryQty() ).trim();
		int posDot = value.indexOf(".");
		
		value = value.substring(0, posDot);
		
		return StringUtils.leftPad( value, 9, "0" );
	}

	public void setPrimaryQty(double primaryQty) {
		this.primaryQty = primaryQty;
	}

	public String getUnitIssued() {
		return unitIssued;
	}

	public void setUnitIssued(String unitIssued) {
		this.unitIssued = unitIssued;
	}

	public String getNameArqTot() {
		return nameArqTot;
	}

	public void setNameArqTot(String nameArqTot) {
		this.nameArqTot = nameArqTot;
	}

	public FileWriter getArqTot() {
		return arqTot;
	}

	public void setArqTot(FileWriter arqTot) {
		this.arqTot = arqTot;
	}

	public String getDirArq() {
		
		ServerSetup server = GuiGlobals.getDaoFactory().getServerSetupDao().find();
		
		if ( server != null ){
			return server.getStoreDirectory() + GuiGlobals.getSeparador();
		}
		
		return null;		
	}
	
	public boolean isCreated(){
		return ( arqTot != null && gravarArqTot != null );
	}
	
	public void finish(){
		
		gravarArqTot.close();		
		try {
			arqTot.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void print() 
	{
		String linhaTot = "";
		
  		// 1 número do pedido (nome do arquivo expedição)
  		linhaTot +=  getIdOrderImport() + ";";
  		
  		// 2 número do item do pedido
  		linhaTot += getItemOnOrderFormat() + ";";
  		
  		// 3 código do produto 
  		linhaTot += getIdMascProduct() + ";";
  		
  		// 4 peso líquido total expedido do item 			  		
  		linhaTot += getNetFormat() + ";";
  		
  		// 5 Quantidade de peças expedidas
  		linhaTot += getPrimaryQtyFormat() + ";";
  		
  		// 6 unidade do valor expedido (pc, cx, sc, etc. Será un caso o item seja em quilos) 
  		linhaTot += getUnitIssued();
  		
  		gravarArqTot.println(linhaTot);
	}
	
}
