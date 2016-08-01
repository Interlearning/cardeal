package br.com.cardeal.globals;

import java.io.IOException;
import java.net.Socket;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

@SuppressWarnings("deprecation")
public class HibernateUtil {
	
	private static Configuration configuration;
	private static SessionFactory factory;
	/* Se deixar com este bloco de codigo. Ao alterar algo no banco de dados o usuario tera que sair do sistema e entrar novamente
	static {
		// configura o hibernate
		Configuration conf = new AnnotationConfiguration();
		conf.configure();
		factory = conf.buildSessionFactory();		
	}
	*/
	
//	static {
//		// configura o hibernate
//		configuration = getConfigSgbd();		
//	}
	
	public static Session getSession() 
	{	
		configuration = getConfigSgbd();
		Session session = null;
		
		if( GuiGlobals.getConfigSGBD() != null )
		{
			if ( isServerConnected() ) 
			{
				try
				{
					factory = configuration.configure().buildSessionFactory();
					session = factory.openSession();
				}
				catch ( Exception e )
				{
					e.printStackTrace();
					GuiGlobals.showMessageDlg("Falha de integridade de dados. Erro: " + e.getMessage() );
					System.exit(0);
				}
				
			}
		}
		else
		{
			factory = configuration.configure().buildSessionFactory();
			session = factory.openSession();
		}
		
		return session;
	}
	
	public static boolean isServerConnected() 
	{
		boolean connected = true;
		
		if( GuiGlobals.getConfigSGBD() != null )
		{
			Socket s = new Socket();
			try
			{
				s = new Socket(GuiGlobals.getConfigSGBD().getUrlServer(),GuiGlobals.getConfigSGBD().getPortServerInt());
			}
			catch (Exception e) 
			{ 
				e.printStackTrace();
			}
			
			if( s.isConnected() )
			{
				connected = true;
				try 
				{
					s.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				connected = false;
			}
		}
		
		return connected;
	}
	
//	public static Session getSession() {		
//		configuration = new AnnotationConfiguration();		
//		configuration.configure();
//		factory = configuration.buildSessionFactory();
//		return factory.openSession();
//	}
		
	private static Configuration getConfigSgbd()
	{						
		Configuration config = null;
				
		if ( GuiGlobals.getConfigSGBD() != null ) 
		{
		
	        try {

	        	config = new AnnotationConfiguration();	        	
	        	config.setProperty("hibernate.dialect", GuiGlobals.getConfigSGBD().getDialect() );
	        	config.setProperty("hibernate.connection.driver_class", GuiGlobals.getConfigSGBD().getDriver() );
				config.setProperty("hibernate.connection.username", GuiGlobals.getConfigSGBD().getUser() );
				config.setProperty("hibernate.connection.password", GuiGlobals.getConfigSGBD().getPassWordDecrypt() );
				config.setProperty("hibernate.connection.url", GuiGlobals.getConfigSGBD().getFormattedUrl() );
				
	        }
	        catch (Exception e) {
	        	config = null;
				e.printStackTrace();
			}
	        	       
		}
		else
		{
//			System.out.println("Objeto de configuração não foi iniciado!");
		}
		
		if ( config == null) 
			config = new AnnotationConfiguration();
		
		return config;
		
	}
	
}
