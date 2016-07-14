package br.com.cardeal.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import br.com.cardeal.enums.ShipmentTypeOperation;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Setup;

public class ShipmentArchieveExp {

	private String idOrderImport;	
	private String nameArqExp;
	private long stockId;
	private int itemOnOrder;
	private String idMascProduct;
	private double netStock;
	private double taraStock;
	private int primaryQty;
	private String unitExp;
	private int palletId;
	private ShipmentTypeOperation typeOperation;
	private TypeStock typeStock;
	private Date dateProduction;
	private String dirArq;
	private FileWriter arqExp;
	private PrintWriter gravarArqExp;
		
	public ShipmentArchieveExp( String idOrderImport ) throws IOException{
		this.idOrderImport = idOrderImport;
		nameArqExp = this.idOrderImport +".exp";
		dirArq = getDirArq();
		arqExp = new FileWriter(dirArq + nameArqExp,true);
		gravarArqExp = new PrintWriter(arqExp);
	}
	
	public String getIdOrderImport() {
		return idOrderImport;
	}

	public void setIdOrderImport(String idOrderImport) {
		this.idOrderImport = idOrderImport;
	}
	
	public String getNameArqExp() {
		return nameArqExp;
	}

	public void setNameArqExp(String nameArqExp) {
		this.nameArqExp = nameArqExp;
	}
	
	public String getDirArq() {
		
		ServerSetup server = GuiGlobals.getDaoFactory().getServerSetupDao().find();
		
		if ( server != null ){
			return server.getStoreDirectory() + GuiGlobals.getSeparador();
		}
		
		return null;		
	}
	
	public boolean isCreated(){
		return ( arqExp != null && gravarArqExp != null );
	}
	
	public void finish(){
		
		gravarArqExp.close();		
		try {
			arqExp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void print() {
		
		String linhaExp = "";
		
		// 1 número do pedido (nome do arquivo expedição)
  		linhaExp += getIdOrderImport() + ";";
  		
  		// 2 número da caixa. Será 00000000 quando for expedição via balança ou manual
  		linhaExp += getStockIdFormat() + ";";
  		
  		// 3 número da fabrica
  		linhaExp += StringUtils.leftPad( String.valueOf( Setup.getCompany().getId() ), 3, "0" ) + ";";
  		
  		// 4 número do terminal
  		linhaExp += StringUtils.leftPad( String.valueOf( Setup.getTerminal().getIdTerminal() ), 2, "0" ) + ";";
  		
  		// 5 item do pedido
  		linhaExp += getItemOnOrderFormat() + ";";
  		
  		// 6 código do produto
  		linhaExp += getIdMascProduct() + ";";
  		
  		// 7 senha do operador
  		linhaExp += StringUtils.leftPad(GuiGlobals.getUserInfo().getUser().getPassword(), 4, "0" ) + ";";
  		
  		// 8 peso líquido kg
  		linhaExp +=  getNetStockFormat() + ";";
  		
  		// 9 tara. Será 000,000 quando for expedição via pistola,  ou manual.
  		linhaExp +=  getTaraStockFormat() + ";";
  		
  		// 10 quantidade de peças, sacos etc. expedidas via balança. Será 01 para caixas, isto é, expedição for via pistola ou pallet.
  		linhaExp +=  getPrimaryQtyFormat() + ";";
  		
  		// 11 unidade do produto expedido ( cx, kg, pc, etc.)
  		linhaExp += getUnitExp() + ";";
  		
  		// 12 data da operação
  		linhaExp += DateTimeUtils.getDate(DateTimeUtils.now(), "yyMMdd") + ";";
  		
  		// 13 hora e minuto da operação
  		linhaExp += DateTimeUtils.getDate(DateTimeUtils.now(), "HHmm") + ";";
  		
  		// 14 endereço do pallet onde estava a caixa. Sera 000/0000 quando nao existir
  		linhaExp += "000/0000" + ";";
  		
  		// 15 número do pallet onde está a caixa. Será 00000 quando não for operação com controle por pallet ou a comunicação com 
  		//TechWork não se estabelecer.
  		linhaExp += getPalletIdFormat() + ";";
  		
  		// 16 tipo operação 1 - pistola, 3 - pallet, 5 - manual, 6 - com balança
  		linhaExp += getTypeOperationFormat() + ";";
  		
  		// 17 flag expedição: C com consulta pedido ou S sem consulta pedido
  		// Definicao do Antonio em 30/06/2015
  		linhaExp += "C" + ";";
  		
  		// 18 idade animal digitada em expedição via balança
  		// Definicao do Antonio em 30/06/2015
  		linhaExp += "00" + ";";
  		
  		// 19 unidade da idade ( DIAS , MESES, ANOS )
  		// Definicao do Antonio em 30/06/2015
  		linhaExp += "     " + ";";
  		
  		// 20 sexo animal digitado em expedição via balança (MACHO ou FEMEA)
  		// Definicao do Antonio em 30/06/2015
  		linhaExp += "     " + ";";																						
  		
  		// 21 PH medido do animal e digitado em expedição via balança
  		// Definicao do Antonio em 30/06/2015
  		linhaExp += "0" + ";";
  		
  		// 22 identifica de onde veio a carne (a origem) que está sendo expedida para baixa de estoque de produtos Semi-Acabados ou acabados.
  		linhaExp += getTypeStockFormat() + ";";
  		
  		// 23 Data em que a caixa foi produzida
  		linhaExp += getDateProduction();
  		
  		gravarArqExp.println(linhaExp);
		
	}

	public String getDateProduction() {		
		return ( (dateProduction == null) ? "00000000" : DateTimeUtils.getDate(dateProduction, "yyyyMMdd") );
	}
	
	public void setDateProduction( Date dateProduction ) {
		this.dateProduction = dateProduction;
	}

	public long getStockId() {
		return stockId;
	}
	
	public String getStockIdFormat() {
		
		String format;
		
		if ( stockId == 0 ){
			format = "00000000";
  		}
  		else{
  			format = StringUtils.leftPad( String.valueOf( stockId ), 8, "0" );
  		}
		
		return format;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
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

	public double getNetStock() {
		return netStock;
	}
	
	public String getNetStockFormat() {
		return NumberUtils.transform(NumberUtils.roundNumber(netStock, 3), 4, 3 );
	}

	public void setNetStock(double netStock) {
		this.netStock = netStock;
	}

	public double getTaraStock() {
		return taraStock;
	}
	
	public String getTaraStockFormat() {
		return NumberUtils.transform(NumberUtils.roundNumber(taraStock, 3), 3, 3 );
	}

	public void setTaraStock(double taraStock) {
		this.taraStock = taraStock;
	}

	public int getPrimaryQty() {
		return primaryQty;
	}
	
	public String getPrimaryQtyFormat() {
		return StringUtils.leftPad( String.valueOf( primaryQty ), 3, "0" );	
	}

	public void setPrimaryQty(int primaryQty) {
		this.primaryQty = primaryQty;
	}

	public String getUnitExp() {
		return unitExp;
	}

	public void setUnitExp(String unitExp) {
		this.unitExp = unitExp;
	}

	public int getPalletId() {
		return palletId;
	}
	
	public String getPalletIdFormat() {		
		return StringUtils.leftPad( String.valueOf( palletId ), 5, "0" );
	}

	public void setPalletId(int palletId) {
		this.palletId = palletId;
	}

	public ShipmentTypeOperation getTypeOperation() {
		return typeOperation;
	}

	public String getTypeOperationFormat(){
		return (typeOperation == null) ? " " : typeOperation.getId();
	}
	
	public void setTypeOperation(ShipmentTypeOperation shipmentTypeOperation) {
		this.typeOperation = shipmentTypeOperation;
	}

	public TypeStock getTypeStock() {
		return typeStock;
	}
	
	public String getTypeStockFormat() {
		
		String valueRet;
		
		if ( typeStock == TypeStock.GRANEL ){
			valueRet = "G";
  		}
  		else{
  			valueRet =  "E";
  		}
		
		return valueRet;
	}

	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}

	public void clear() 
	{
		idOrderImport = null;	
		stockId = 0;
		itemOnOrder = 0;
		idMascProduct = null;
		netStock = 0.0;
		taraStock = 0.0;
		primaryQty = 0;
		unitExp = null;
		palletId = 0;
		typeOperation = null;
		typeStock = null;
		dateProduction = null;
	}
	
}
