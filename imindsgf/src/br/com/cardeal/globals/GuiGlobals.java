package br.com.cardeal.globals;

import java.awt.Cursor;
import java.awt.Window;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang.StringUtils;
import br.com.cardeal.dao.DaoFactory;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.ModeloEtiqueta;
import br.com.cardeal.enums.ProcessWorker;
import br.com.cardeal.filter.EtiquetaFilter;
import br.com.cardeal.views.Main;
import br.com.cardeal.keyboard.DialogVirtualKeyboard;
import br.com.cardeal.model.ArquivoConfigBancoDeDados;
import br.com.cardeal.model.ArquivoConfigTerminal;
import br.com.cardeal.model.Enterprise;
import br.com.cardeal.model.Etiqueta;
import br.com.cardeal.printer.PrintSerial;
import br.com.cardeal.scale.Scale;
import br.com.cardeal.scale.ScaleExecutor;
import br.com.cardeal.services.ControlAccessService;
import br.com.cardeal.interceptor.UserInfo;

public class GuiGlobals {
	
	private static UserInfo userInfo = new UserInfo();
	private static JFrame mainFrame;
	private static Main main;
	private static DaoFactory daoFactory = null;
	private static List<Scale> scales;
	private static List<ScaleExecutor> scaleExecutors;
	private static String logProcess = "N";
	private static String ipServer;
	private static String portaServer;
	private static Enterprise enterprise;
	private static Locale ptBR = new Locale("pt","BR");
	private static ResourceBundle bundle = ResourceBundle.getBundle("messages", ptBR);
	private static ArquivoConfigTerminal configTerminal;
	private static ArquivoConfigBancoDeDados configSGBD;
		
	public static List<Scale> getScales() {
		return scales;
	}

	public static void setScales(List<Scale> scales) {
		GuiGlobals.scales = scales;
	}
	
	public static List<ScaleExecutor> getScaleExecutors() {
		return scaleExecutors;
	}

	public static void setScaleExecutors(List<ScaleExecutor> scaleExecutors) {
		GuiGlobals.scaleExecutors = scaleExecutors;
	}

	public static DaoFactory startDb() {
		try {
			daoFactory = new DaoFactory();
			return daoFactory;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static DaoFactory refreshDaoFactory(){		
//		closeDb();
//		return startDb();		
		return getDaoFactory();
	}
	
	public static void closeDb() {
		try {
			if(daoFactory != null)
				daoFactory.close();
				daoFactory = null;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static InputStream byteToInputStream(byte[] bytes) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return bais;
	}

	public static DaoFactory getDaoFactory() 
	{
		
		if ( !HibernateUtil.isServerConnected() )
		{
			GuiGlobals.showMessageDlg("SERVIDOR INDISPONÍVEL!", "Atenção, o servidor está indisponível. O sistema será abortado!");
			System.exit(0);
		}
		
		if(daoFactory == null)
			return startDb();
		else
			return daoFactory;
	}	
	
	public static void closeDevices() {
		
		if ( scaleExecutors != null ) {
		
			for(ScaleExecutor executor : scaleExecutors) {
				executor.terminate();
			}
			
		}
	}

	public static UserInfo getUserInfo() {
		return userInfo;
	}
	
	public static JFrame getMainFrame() {
		return mainFrame;
	}

	public static void setMainFrame(JFrame frame) {
		mainFrame = frame;
	}
	
	public static void virtualKeyboard(Window parent, JTextComponent textComponent, String methodName, Object callingObject) 
	{
		virtualKeyboard(parent, textComponent, methodName, callingObject, true, false, true);
	}
	
	public static void virtualKeyboard(Window parent, JTextComponent textComponent, String methodName, Object callingObject, boolean onlyNumeric, boolean onlyLetters, boolean isSelectAll) 
	{
		if(textComponent.isEnabled()) 
		{
			if ( isSelectAll )
				textComponent.selectAll();
			
			Method method = null;
			if(methodName != null && callingObject != null) {
				try {
					method = callingObject.getClass().getMethod(methodName, new Class[] {});
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
			if(parent == null)
				parent = mainFrame;
			new DialogVirtualKeyboard(parent, false, textComponent, method, callingObject, onlyNumeric, onlyLetters);
		}
	}
	
	public static void isEnabledAction(JTextComponent textComponent, String methodName, Object callingObject){
		if(textComponent.isEnabled() && callingObject !=null ){
			Method method = null;			
			try {
				method = callingObject.getClass().getMethod(methodName, new Class[] {});
				method.invoke(callingObject, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}		
	}
	
	public static void virtualKeyboard(Window parent, JTextComponent textComponent, String methodName, Object callingObject, int indice) 
	{
		if(textComponent.isEnabled()) 
		{
			textComponent.selectAll();
			Method method = null;
			if(methodName != null && callingObject != null) {
				try {
					method = callingObject.getClass().getMethod(methodName, int.class, JTextComponent.class);
				} 
				catch (NoSuchMethodException e) 
				{
					e.printStackTrace();
				} 
				catch (SecurityException e) 
				{
					e.printStackTrace();
				}
			}
			if(parent == null)
				parent = mainFrame;
			new DialogVirtualKeyboard(parent, false, textComponent, method, callingObject, indice);
		}
	}
	
	
	public static void virtualKeyboardAction(Window parent, JTextComponent textComponent, String methodName, Object callingObject
			, JButton bnt1, JButton btn2, JButton btn3, JButton btn4, JButton btn5, JButton btn6, JButton btn7
			 , JButton btn8, JButton btn9, JButton btn0, JButton btnDot, JButton btnBack, JButton btnEnter, JButton btnClear
			) {
		if(textComponent.isEnabled()) 
		{
			textComponent.selectAll();
			Method method = null;
			if(methodName != null && callingObject != null) {
				try {
					method = callingObject.getClass().getMethod(methodName, new Class[] {});
				} catch (NoSuchMethodException e) {
				} catch (SecurityException e) {
				}
			}
			if(parent == null)
				parent = mainFrame;
			new DialogVirtualKeyboard(parent, false, textComponent, method, callingObject, bnt1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnDot, btnBack, btnEnter, btnClear);
		}
	}

	public static Main getMain() {
		return main;
	}

	public static void setMain(Main main) {
		GuiGlobals.main = main;
	}

	public static void setUserInfo(UserInfo userInfo) {
		GuiGlobals.userInfo = userInfo;
	}
	
	public static void waitCursor() {
		if(main != null)
			main.getCurrentOperationWindow().getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));		
	}
	
	public static void defaultCursor() {
		if(main != null)
			main.getCurrentOperationWindow().getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));		
	}
	
	public static void showMessageDlg(String titulo, String msg) {
		JOptionPane.showMessageDialog(null, msg, titulo, 1);		
	}
	
	public static void showMessageDlg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Atenção", 1);		
	}
	
	public static void showMessageErrorDlg(String messageUser, String messageException ) 
    {
        JOptionPane.showMessageDialog(null, messageUser + "\n ERROR: " + messageException, "ATENÇÃO!", 1);		
    }
	
	public static void showMessage(String msg) 
	{
		showMessage(msg, true);
	}
	public static void showMessage(String msg, boolean piscante)
	{
		if ( piscante )
		{
			main.showMessagePiscante(msg);
		}
		else
		{
			main.showMessage(msg);
		}
	}
	
	public static double getCurrentNet() {
		int scaleIndex = main.getScaleIndex();
		if(scaleIndex >= 0) {
			return scales.get(scaleIndex).getNet();
		}
		else
			return -999;
	}
	
	public static boolean isSettledScale()
	{
		
		if ( isScaleSimulated() )
		{
			return true;
		}

		int scaleIndex = main.getScaleIndex();
		if(scaleIndex >= 0) 
		{
			return scales.get(scaleIndex).isSettled();
		}
		else
			return false;
		
	}
	
	public static boolean isScaleSimulated()
	{
		int scaleIndex = main.getScaleIndex();
		
		if ( GuiGlobals.getScales().get(scaleIndex).isSimulated() )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static Scale getCurrentScale() {
		int scaleIndex = main.getScaleIndex();
		if(scaleIndex >= 0) {
			return scales.get(scaleIndex);
		}
		else
			return null;
		
	}
	
	public static double getCurrentTare() {
		int scaleIndex = main.getScaleIndex();
		if(scaleIndex >= 0) {
			return scales.get(scaleIndex).getTare();
		}
		else
			return -999;
	}
	
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
	
	public static String getSeparador(){
		
		String separador = "\\";		
		if ( "Linux".equals( System.getProperty("os.name").trim() ) ) separador = "/";
		return separador;
	}
	
	public static String getSeparadorIvertido(){
		
		String separador = "/";		
		if ( "Linux".equals( System.getProperty("os.name").trim() ) ) separador = "\\";
		return separador;
	}
	
	public static String getLogProcess() {
		return logProcess;
	}

	public static void setLogProcess(String logProcess) {
		
		if ( logProcess == null || logProcess.isEmpty() )
		{
			GuiGlobals.logProcess = "N";
		}
		else
		{
			GuiGlobals.logProcess = logProcess;	
		}
	}
	
	public static double round( double numero, int nCasasDec ){
		
		DecimalFormat df = new DecimalFormat("###."+StringUtils.leftPad("", nCasasDec, "0"));  
		
		return Double.parseDouble( df.format(numero).replace(",", ".") );
	}
	
	public static boolean insertControlAccess( ProcessWorker process ){
		
		boolean continuar = false;
		
		ControlAccessService controlAccessService = new ControlAccessService();
		try {
			continuar = controlAccessService.insertAccess(process);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return continuar;
		
	}
	
	public static void removeAllControlAccess(){
		
		ControlAccessService controlAccessService = new ControlAccessService();
		try {
			controlAccessService.removeAccessOfFilialAndTerminal();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static ResourceBundle getBundle() {
		
		if ( bundle == null){
			ptBR = new Locale("pt","BR");
			bundle = ResourceBundle.getBundle("messages", ptBR);
		}
		
		return bundle;
	}

	public static void setBundle(ResourceBundle bundle) {
		GuiGlobals.bundle = bundle;
	}

	public static String getIpServer() {
		return ipServer;
	}

	public static void setIpServer(String ipServer) {
		GuiGlobals.ipServer = ipServer;
	}

	public static String getPortaServer() {
		return portaServer;
	}

	public static void setPortaServer(String portaServer) {
		GuiGlobals.portaServer = portaServer;
	}

	public static void setEnterprise(Enterprise enterprise) {
		GuiGlobals.enterprise = enterprise;
	}
	
	public static Enterprise getEnterprise() {
		return enterprise;
	}
	
//	public static List<String> listLabels( ServletContext context) {
//		List<String> list = new ArrayList<String>();
//		list.add("");
//		String labelsPath = context.getRealPath("") + GuiGlobals.getSeparador() + "resources" + GuiGlobals.getSeparador() + "labels";
//		
//		File folder = new File(labelsPath);
//	    File[] listOfFiles = folder.listFiles();
//	    if (listOfFiles == null)
//	    	return list;
//	    
//	    for (int i = 0; i < listOfFiles.length; i++) {
//	    	if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(".txt"))
//	    		list.add(listOfFiles[i].getName());
//	    }	
//	    return list;
//	}
	
	public static List<Etiqueta> listLabelsBox() {
		
		EtiquetaFilter filter = new EtiquetaFilter();
		filter.setModeloEtiqueta( ModeloEtiqueta.CAIXA );
		List<Etiqueta> listEtiquetas = GuiGlobals.getDaoFactory().getEtiquetaDao().list( filter );
		
	    return listEtiquetas;
		
	}
	
	public static List<Etiqueta> listLabelsPallet() {
		
		EtiquetaFilter filter = new EtiquetaFilter();
		filter.setModeloEtiqueta( ModeloEtiqueta.PALLET );
		List<Etiqueta> listEtiquetas = GuiGlobals.getDaoFactory().getEtiquetaDao().list( filter );
		
	    return listEtiquetas;
	    
	}	
	
	public static String getCurrentMacAddress(){
		
		String macRetorno = "";
		
		/**
		 * Desta maneira funciona no windows
		 */
//		InetAddress ip;
//		
//		try {
//				
//			ip = InetAddress.getLocalHost();
//			NetworkInterface network = NetworkInterface.getByInetAddress(ip);				
//			byte[] mac = network.getHardwareAddress();				
//			StringBuilder sb = new StringBuilder();
//			
//			for (int i = 0; i < mac.length; i++) {
//				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
//			}
//			
//			macRetorno = sb.toString();
//				
//		} catch (UnknownHostException e) {			
//			e.printStackTrace();
//			
//		} catch (SocketException e){				
//			e.printStackTrace();
//				
//		}
		
		try {
			Enumeration<NetworkInterface> eth = NetworkInterface.getNetworkInterfaces();
			while (eth.hasMoreElements()) {
	            NetworkInterface eth0 = eth.nextElement();
	            byte[] mac = eth0.getHardwareAddress();
	            StringBuilder sb = new StringBuilder();
	            if (mac != null) {
	                for (int i = 0; i < mac.length; i++) {
	                    String aux = String.format("%02X%s", mac[i],
	                            (i < mac.length - 1) ? "-" : "");
	                    sb.append(aux);
	                }
	                if (sb.toString().length() <= 18) {
	                	macRetorno = sb.toString();
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return macRetorno;
		
	}
	
	public static String getCurrentIPAddress(){
		
		String ipRetorno = "";
		InetAddress ip;
		
		try {
				
			ip = InetAddress.getLocalHost();
			ipRetorno = ip.getHostAddress();
				
		} catch (UnknownHostException e) {			
			e.printStackTrace();
				
		}
		
		return ipRetorno;
		
	}
	
	public static Properties getPropertiesConfig()
	{
		
		String fileName;
		Properties p = null;
		FileInputStream fileInputStream = null;
		
		try 
		{
			fileName = ArquivoConfigTerminal.getRootPath() + GuiGlobals.getSeparador() + "config.ini";
			fileInputStream = new FileInputStream(fileName);
		} 
		catch (IOException e) 
		{
			GuiGlobals.showMessageDlg(e.getMessage());
			System.out.println("Não foi encontrado o arquivo config.ini...");
		}
				
		if ( fileInputStream != null ) {
		
	        try {
	        	p = new Properties();
	        	p.load( fileInputStream );
	        	fileInputStream.close();
	        }
	        catch (Exception e) {
				p = null;
	        	e.printStackTrace();
			}
	        
		}
		
		return p;
		
	}
	
	public static byte[] fileToByte(File imagem) throws Exception {  
		
        @SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(imagem);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        byte[] buffer = new byte[8192];  
        int bytesRead = 0;  
        while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {  
            baos.write(buffer, 0, bytesRead);  
        }  
        return baos.toByteArray();  
    }

	public static int showMessageDlgYesNo(String string) {		
		return JOptionPane.showConfirmDialog (null, string,"Atenção!", JOptionPane.YES_NO_OPTION);
	}

	public static ArquivoConfigTerminal getConfigTerminal() {
		return configTerminal;
	}

	public static void setConfigTerminal(ArquivoConfigTerminal configTerminal) {
		GuiGlobals.configTerminal = configTerminal;
	}

	public static ArquivoConfigBancoDeDados getConfigSGBD() {
		return configSGBD;
	}

	public static void setConfigSGBD(ArquivoConfigBancoDeDados configSGBD) {
		GuiGlobals.configSGBD = configSGBD;
	}

	public static boolean isPermitted( ComponentPermission permission ) 
	{
		UserInfo info = GuiGlobals.getUserInfo();
		return info.isPermittedComponentUpdate( permission );
	}
	
	public static void getPrintResultZebra()
	{
		switch(Setup.getPrinterModel()) 
		{
			case INTERMEC:
				break;
				
			case NONE:
				break;
				
			case ZEBRA:
				PrintSerial prntSerial = new PrintSerial();
				prntSerial.setPortName(Setup.getPrinterPort());	
				prntSerial.getPrintResultZebra(false, true);
//				int timeOut = 4000;
//				long init = System.currentTimeMillis();
//				while(System.currentTimeMillis() - init < timeOut) 
//				{
//					prntSerial.getPrintResultZebra(false, true);
//				}
				break;

			case SIMULATOR:
				break;		  
		  }
		
	}
	
}
