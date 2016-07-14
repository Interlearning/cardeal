package br.com.cardeal.globals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import br.com.cardeal.printer.PrinterModel;
import br.com.cardeal.scale.Scale;
import br.com.cardeal.scale.ScaleExecutor;
import br.com.cardeal.scale.ScaleModel;
import br.com.cardeal.views.Config;
import br.com.cardeal.enums.ModeloEtiqueta;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.ArquivoConfigBancoDeDados;
import br.com.cardeal.model.ArquivoConfigTerminal;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.Enterprise;
import br.com.cardeal.model.Etiqueta;
import br.com.cardeal.model.ServerSetup;
import br.com.cardeal.model.Terminal;

public class Setup 
{
	private static Terminal terminal;
	private static Company company;
	private static String labelsPath = "";
	private static String embalaPath = "";
	private static String tempPath = "C:/temp";
	private static String palletLabelPath = "";
	private static String printerName = "IntermecGenerica";
	private static PrinterModel printerModel;
	private static String printerPort = "COM9";
	private static ArquivoConfigTerminal configTerminal = null;
	private static ArquivoConfigBancoDeDados configSGBD = null;
	
	public static boolean buildConfigIni( boolean showDlgConfig ) 
	{
		Properties p = null;
		String logProcess = null;
		boolean continuar = false;
		
		while ( !continuar )
		{
		
			p = GuiGlobals.getPropertiesConfig();
			
			if ( p != null ) 
			{
				try 
		        {
			        logProcess = p.getProperty("gera_log");
			        /*
			         * Configuracao utilizada para copiar layouts do servidor para as estações - Antonio pediu para retirar.
			        ipServer = p.getProperty("ip_server");
			        portaServer = p.getProperty("porta_server");
			        */
			        
			        GuiGlobals.setLogProcess( logProcess );
			        
			        configSGBD = new ArquivoConfigBancoDeDados( p.getProperty("model_sgbd"), p.getProperty("ip_sgbd"), p.getProperty("porta_sgbd"), p.getProperty("database_sgbd"), p.getProperty("user_sgbd"), p.getProperty("psw_sgbd") );
			        configTerminal = new ArquivoConfigTerminal( p.getProperty("path"), p.getProperty("filial"), p.getProperty("terminal") );
			        
		        	continuar = configSGBD.validAtributes( !showDlgConfig ) && configTerminal.validAtributes( !showDlgConfig  );
			        
			        if ( !continuar )
			        {
			        	p.clear();
			        	if ( showDlgConfig )
			        		getDlgConfig(configTerminal, configSGBD);
			        	else
							break;
			        	
			        }
			        
			        if ( continuar )
			        {	        	
			        	GuiGlobals.setConfigTerminal( configTerminal );
			        	GuiGlobals.setConfigSGBD( configSGBD );
			        }
			        
		        } 
		        catch (Exception e) 
		        {
					e.printStackTrace();
				}
		        
			}
			else{
				continuar = false;
				if ( showDlgConfig )
					getDlgConfig(null, null);
				else
					break;
			}
			
			if ( !showDlgConfig )
				break;
			
		}
		
		return continuar;
		
	}
	
	public static void buildSetup() 
	{

		boolean continuar = buildConfigIni( true );
		Enterprise enterprise = null;		
		        
        if ( continuar )
        {			        	
        	
	        ServerSetup ss = GuiGlobals.getDaoFactory().getServerSetupDao().find();
	        if(ss != null) {
	        	labelsPath = ss.getLabelsDirectory();
	        	embalaPath = ss.getStoreDirectory();
	        	palletLabelPath = ss.getStoreDirectory();
	        }
	        
	        company = GuiGlobals.getDaoFactory().getCompanyDao().find( configTerminal.getCompany().trim() );
	        
	        continuar = verifyPaths();
	        
	        if (continuar && company == null){
	        	GuiGlobals.showMessageDlg("Não foi encontrado o cadastro da filial configurado no arquivo config.ini --> [filial=" + configTerminal.getCompany() + "] !");
	        	System.exit(0);
	        }

	        if ( continuar ) {
		        //busca configuracao remota do terminal
		        terminal = GuiGlobals.getDaoFactory().getTerminalDao().find(company.getId(), configTerminal.getTerminal() );
		        
		        if (terminal == null){
		        	GuiGlobals.showMessageDlg("Não foi encontrado o cadastro do terminal configurado no arquivo config.ini --> [terminal=" + configTerminal.getTerminal() + "] !");
		        	System.exit(0);
		        }
		        else if (	terminal.getPrinterModel() != PrinterModel.SIMULATOR && 
		        			terminal.getPrinterModel() != PrinterModel.NONE &&
		        			(
		        			terminal.getPrinterBaudRate() == -1
		        			|| terminal.getPrinterDataBits() == -1
		        			|| terminal.getPrinterParity() == -1
		        			|| terminal.getPrinterStopBits() == -1
		        			))
		        {
		        	GuiGlobals.showMessageDlg("Parâmetros de comunicação com a impressora não foram definidos, isso significa que não será emitida etiquetas na embalagem. Verifique no cadastro do Terminal !");
		        }

		        if ( continuar && !refreshLabelsPrinterZebra( false ) ){				        	
		        	System.exit(0);
		        }
		        
	        }
	        
	        if ( continuar ){
	        	
	        	List<Enterprise> enterprises = GuiGlobals.getDaoFactory().getEnterpriseDao().list();		        	
	        	if(enterprises == null || enterprises.isEmpty()){
	        		GuiGlobals.showMessageDlg("Não foi encontrado empresa cadastrada no sistema. Cadastre a empresa no sistema para continuar!");
	        		System.exit(0);
	        	}
	        	else{
	        		enterprise = enterprises.get(0);
	        		GuiGlobals.setEnterprise( enterprise );
	        	}
	        }
	        
        }
        	
        if ( continuar ) 
        {
        	tempPath = terminal.getTempDirectory();
        	
        	printerModel = terminal.getPrinterModel();
        	printerName = terminal.getPrinterName();
        	printerPort = terminal.getPrinterPort();

        	List<Scale> scales = new ArrayList<Scale>();	
        	List<ScaleExecutor> executors = new ArrayList<ScaleExecutor>();	

        	if(terminal.getScale1Model() != ScaleModel.NONE) {
        		Scale s1 = new Scale();
        		s1.setManufacturer(terminal.getScale1Model());
        		s1.setUnit(terminal.getScale1Unit());
        		s1.setSimulated(terminal.getScale1Model() == ScaleModel.SIMULATOR);
        		s1.setPort( terminal.getScale1Port() );
        		s1.setBaudRate(terminal.getScale1BaudRate() );
        		s1.setDataBits( terminal.getScale1DataBits() );
        		s1.setStopBits( terminal.getScale1StopBits() );
        		s1.setParity( terminal.getScale1Parity() );			        		
        		scales.add(s1);
        		if(!s1.isSimulated()) {
        			ScaleExecutor executor = new ScaleExecutor(s1);
        			executor.start();
        			executors.add(executor);
        		}
        	}
        	if(terminal.getScale2Model() != ScaleModel.NONE) {
        		Scale s2 = new Scale();
        		s2.setManufacturer(terminal.getScale2Model());
        		s2.setUnit(terminal.getScale1Unit());
        		s2.setSimulated(terminal.getScale2Model() == ScaleModel.SIMULATOR);
        		s2.setPort( terminal.getScale2Port() );
        		s2.setBaudRate(terminal.getScale2BaudRate() );
        		s2.setDataBits( terminal.getScale2DataBits() );
        		s2.setStopBits( terminal.getScale2StopBits() );
        		s2.setParity( terminal.getScale2Parity() );
        		scales.add(s2);	
        		if(!s2.isSimulated()) {
        			ScaleExecutor executor = new ScaleExecutor(s2);
        			executor.start();
        			executors.add(executor);
        		}	        		
        	}
        	
        	GuiGlobals.setScales(scales);
        	GuiGlobals.setScaleExecutors(executors);		        
        }
		
		if (!continuar){
			System.exit(0);
		}
		
	}
	
	private static void getDlgConfig( ArquivoConfigTerminal configTerminal, ArquivoConfigBancoDeDados configSGBD )
	{
		Config config = new Config(configTerminal, configSGBD);	
		
		while ( config.getOperation() == 0 )
		{
			GuiGlobals.sleep(500);
		}
		
		if ( config.getOperation() == Config.CANCELED )
    		System.exit(0);
	}
	
	@SuppressWarnings("resource")
	public static boolean verifyPaths() {
		
		boolean processOk = false;
		
		try {
			new FileWriter( embalaPath + GuiGlobals.getSeparador() + "EMBALA.txt",true);			
			processOk = true;
		} 
		catch (IOException e) 
		{
			processOk = false;
			e.printStackTrace();
			GuiGlobals.showMessageDlg("Diretório de gravação de arquivos texto não foi encontrado! \n" + e.getMessage());
		}
		
		return processOk;
	}

	public static String getRootPathLabelBox(){
		return "caixa";
	}
	
	public static String getRootPathLabelPallet(){
		return "pallet";
	}

	private static boolean refreshLabelsPrinterZebra( boolean copyLabelsFromServer ) {
		
		String pathLabelsDefault = "http://" + GuiGlobals.getIpServer() + ":" + GuiGlobals.getPortaServer() + GuiGlobals.getSeparadorIvertido() + "cardealsgf" + GuiGlobals.getSeparadorIvertido() + "resources" + GuiGlobals.getSeparadorIvertido() + "labels" + GuiGlobals.getSeparadorIvertido() ;
		boolean copiaOk = false;
		boolean verifyLocal = false;
		String pathLabelsTo;
		String pathLabelsToBox;
		String pathLabelsToPallet;
		
		List<Etiqueta> etiquetas = GuiGlobals.getDaoFactory().getEtiquetaDao().list();
		
		if ( etiquetas == null || etiquetas.size() == 0 ){
			GuiGlobals.getDaoFactory().getEtiquetaDao().initCadLabels();
		}
		
		pathLabelsTo = Setup.getLabelsPath() + GuiGlobals.getSeparador();
		pathLabelsTo = pathLabelsTo.replace(GuiGlobals.getSeparador()+GuiGlobals.getSeparador(), GuiGlobals.getSeparador());		
		pathLabelsToBox = pathLabelsTo.concat(GuiGlobals.getSeparador()+getRootPathLabelBox());
		pathLabelsToPallet = pathLabelsTo.concat(GuiGlobals.getSeparador()+getRootPathLabelPallet());
		
		createDir(pathLabelsTo);  
		createDir( pathLabelsToBox );  
		createDir( pathLabelsToPallet );
		
		if ( copyLabelsFromServer ) {
			
			etiquetas = GuiGlobals.getDaoFactory().getEtiquetaDao().list();
		
			for ( Etiqueta etiqueta : etiquetas ){
	
				if ( !verifyLocal ){
				
					try {
						
						AcessURL url = new AcessURL(pathLabelsDefault + etiqueta.getNameEtq() );
						
						if ( etiqueta.getModeloEtiqueta() == ModeloEtiqueta.CAIXA ){
							url.copyFileByURL(pathLabelsToBox + GuiGlobals.getSeparador() + etiqueta.getNameEtq() );
						}
						else if ( etiqueta.getModeloEtiqueta() == ModeloEtiqueta.PALLET ){
							url.copyFileByURL(pathLabelsToPallet + GuiGlobals.getSeparador() + etiqueta.getNameEtq() );
						}				
						copiaOk = true;
					} catch (IOException e) {				
						verifyLocal = true;
						LogDeProcessamento.gravaLog(null, "Não foi encontrado a URL " + pathLabelsDefault + etiqueta.getNameEtq());
						LogDeProcessamento.gravaLog(null, e.getMessage());			
					} catch (Exception e) {				
						verifyLocal = true;
						LogDeProcessamento.gravaLog(null, "Não foi encontrado a URL " + pathLabelsDefault + etiqueta.getNameEtq());
						LogDeProcessamento.gravaLog(null, e.getMessage());
					}
					
				}
				
				if ( verifyLocal){
					
					pathLabelsDefault = new File(".").getAbsolutePath() + GuiGlobals.getSeparador() + "WebContent" + GuiGlobals.getSeparador() + "resources" + GuiGlobals.getSeparador() + "labels" + GuiGlobals.getSeparador();
					if ( !( new File(pathLabelsDefault).isDirectory() ) ){
						GuiGlobals.showMessageDlg("Não foi encontrado o diretório de layouts de etiquetas. Favor entrar em contato com o T.I.!");
						return false;
					}
					
					try {
						
						if ( etiqueta.getModeloEtiqueta() == ModeloEtiqueta.CAIXA ){
							Utils.copyFile( new File(pathLabelsDefault + etiqueta.getNameEtq()), new File(pathLabelsToBox + GuiGlobals.getSeparador() + etiqueta.getNameEtq()));					
						}
						else if ( etiqueta.getModeloEtiqueta() == ModeloEtiqueta.PALLET ){
							Utils.copyFile( new File(pathLabelsDefault + etiqueta.getNameEtq()), new File(pathLabelsToPallet + GuiGlobals.getSeparador() + etiqueta.getNameEtq()));
						}
						copiaOk = true;
						
					} catch (Exception e) {				
						copiaOk = false;
						e.printStackTrace();
						break;
					}
					
				}
				
			}
			
		}
		else{
			copiaOk = true;
		}
		
		return copiaOk;
		
	}
	
	private static void createDir(String dir){
		File diretorio = new File(dir);
		if (!diretorio.exists()) {  
			diretorio.mkdirs();  
		}
		diretorio = null;
	}

	public static Company getCompany() {
		return company;
	}

	public static boolean hasPrinter() {
		return true;
	}

	public static String getLabelsPath() {
		return labelsPath;
	}

	public static String getTempPath() {
		return tempPath;
	}

	public static String getPrinterName() {
		return printerName;
	}
	public static Terminal getTerminal() {
		return terminal;
	}

	public static PrinterModel getPrinterModel() {
		return printerModel;
	}


	public static String getPrinterPort() {
		return printerPort;
	}

	public static String getEmbalaPath() {
		return embalaPath;
	}

	public static String getPalletLabelPath() {
		return palletLabelPath;
	}


}
