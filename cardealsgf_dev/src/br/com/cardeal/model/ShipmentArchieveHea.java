package br.com.cardeal.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;

import br.com.cardeal.globals.GuiGlobals;

public class ShipmentArchieveHea 
{
	private String idOrderImport; // pedido
	private String nameClient; // nome do cliente
	private String observation; // observação
	private String nameDriver; // Nome do motorista
	private String plaqueCar; // placa do veiculo
	private String nameShippingCompany; // transportadora
	private String authorizationShipment; // Autorização de embarque
	private String dateMinBox; // Data minima da caixa
	private String dateMaxBox; // Data maxima da caixa
	
	private String nameArqHea;
	private String dirArq;
	private FileWriter arqHea;
	private PrintWriter gravarArqHea;
	
	public ShipmentArchieveHea( String idOrderImport ) throws IOException
	{
		this.setIdOrderImport(idOrderImport);
		nameArqHea = this.idOrderImport +".hea";
		dirArq = getDirArq();
		arqHea = new FileWriter(dirArq + nameArqHea, true);
		gravarArqHea = new PrintWriter(arqHea);
	}

	public String getIdOrderImport() {
		return idOrderImport;
	}

	public void setIdOrderImport(String idOrderImport) {
		this.idOrderImport = idOrderImport;
	}

	public String getNameClient() {
		return nameClient;
	}
	
	public String getNameClientFormat() {
		return StringUtils.rightPad( nameClient, 30, " " ).substring(0, 30);
	}

	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
	}

	public String getObservation() {
		return observation;
	}
	
	public String getObservationFormat() 
	{
		if ( observation == null ) observation = "";
		return StringUtils.leftPad( observation, 60, " " );
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getNameDriver() {
		return nameDriver;
	}
	
	public String getNameDriverFormat() 
	{
		if ( nameDriver == null ) nameDriver = "";
		return StringUtils.leftPad( nameDriver, 30, " " );
	}

	public void setNameDriver(String nameDriver) {
		this.nameDriver = nameDriver;
	}

	public String getPlaqueCar() {
		return plaqueCar;
	}
	
	public String getPlaqueCarFormat() 
	{
		if ( plaqueCar == null ) plaqueCar = "";
		return StringUtils.leftPad( plaqueCar, 7, " " );
	}

	public void setPlaqueCar(String plaqueCar) {
		this.plaqueCar = plaqueCar;
	}

	public String getNameShippingCompany() {
		return nameShippingCompany;
	}
	
	public String getNameShippingCompanyFormat() 
	{
		if ( nameShippingCompany == null ) nameShippingCompany = "";
		return StringUtils.leftPad( nameShippingCompany, 30, " " );
	}

	public void setNameShippingCompany(String nameShippingCompany) {
		this.nameShippingCompany = nameShippingCompany;
	}

	public String getAuthorizationShipment() {
		return authorizationShipment;
	}
	
	public String getAuthorizationShipmentFormat() 
	{
		if ( authorizationShipment == null ) authorizationShipment = "";
		return StringUtils.leftPad( authorizationShipment, 10, "0" );
	}

	public void setAuthorizationShipment(String authorizationShipment) {
		this.authorizationShipment = authorizationShipment;
	}

	public String getDateMinBox() {
		return dateMinBox;
	}
	
	public String getDateMinBoxFormat() 
	{
		if ( dateMinBox == null ) dateMinBox = "";
		return StringUtils.leftPad( dateMinBox, 8, "0" );
	}

	public void setDateMinBox(String dateMinBox) {
		this.dateMinBox = dateMinBox;
	}

	public String getDateMaxBox() {
		return dateMaxBox;
	}
	
	public String getDateMaxBoxFormat() 
	{
		if ( dateMaxBox == null ) dateMaxBox = "";
		return StringUtils.leftPad( dateMaxBox, 8, "0" );
	}

	public void setDateMaxBox(String dateMaxBox) {
		this.dateMaxBox = dateMaxBox;
	}
	
	public void print() 
	{
		
		String linhaTot = "";
		
  		// 1 número do pedido (nome do arquivo expedição)
  		linhaTot +=  getIdOrderImport() + ";";
  		
  		// 2 nome do cliente
  		linhaTot += getNameClientFormat() + ";";
  		
  		// 3 observações
  		linhaTot += getObservationFormat() + ";";
  		
  		// 4 nome do motorista		  		
  		linhaTot += getNameDriverFormat() + ";";
  		
  		// 5 placa do veiculo
  		linhaTot += getPlaqueCarFormat() + ";";
  		
  		// 6 nome da transportadora  		
  		linhaTot += getNameShippingCompanyFormat() + ";";
  		
  		// 7 autorizacao de embarque 
  		linhaTot += getAuthorizationShipmentFormat() + ";";
  		
  		// 8 Data mínima da caixa 
  		linhaTot += getDateMinBoxFormat() + ";";
  		
  		// 9 Data maxima da caixa 
  	  	linhaTot += getDateMaxBoxFormat();
  		
  		gravarArqHea.println(linhaTot);
		
	}
	
	public boolean isCreated(){
		return ( arqHea != null && gravarArqHea != null );
	}
	
	public void finish(){
		
		gravarArqHea.close();		
		try {
			arqHea.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getDirArq() {
		
		ServerSetup server = GuiGlobals.getDaoFactory().getServerSetupDao().find();
		
		if ( server != null ){
			return server.getStoreDirectory() + GuiGlobals.getSeparador();
		}
		
		return null;		
	}
}
