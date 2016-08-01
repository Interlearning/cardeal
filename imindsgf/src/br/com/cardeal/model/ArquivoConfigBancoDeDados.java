/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cardeal.model;

import br.com.cardeal.enums.ModeloBancoDeDados;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.globals.Writer;
import br.com.cardeal.interfaces.GeneratorArchieveOfConfiguration;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergio
 */
public class ArquivoConfigBancoDeDados implements GeneratorArchieveOfConfiguration
{

	private ModeloBancoDeDados modelo;
	private String urlServer;
    private String portServer;
    private String dataBase;
    private String user;
    private String passWord;
    private String instalationDir;
    private Writer write;
    private final String arquivo = "config";
    private final String extension = ".ini";
    
    public ArquivoConfigBancoDeDados(String modelo, String urlServer, String portServer, String dataBase, String user, String passWord) {
    	this.modelo = ModeloBancoDeDados.getModelByString( modelo );
        this.urlServer = urlServer;
        this.portServer = portServer;
        this.dataBase = dataBase;
        this.user = user;
        this.passWord = passWord;
    }
    
    public ArquivoConfigBancoDeDados(String instalationDir, ModeloBancoDeDados modelo, String urlServer, String portServer, String dataBase, String user, String passWord) {
        this.modelo = modelo;
        this.urlServer = urlServer;
        this.portServer = portServer;
        this.dataBase = dataBase;
        this.user = user;
        this.passWord = passWord;
        this.instalationDir = instalationDir;
    }

    @Override
    public boolean generate() {
        
        boolean processOk = false;
        Writer writeUsed = getWriter();
        
        if ( writeUsed != null )
        {
        	writeUsed.writeln("\n");
            writeUsed.writeln("#CONFIGURACOES DO BANCO DE DADOS");
            writeUsed.writeln("model_sgbd=" + modelo.getName() );
            writeUsed.writeln("ip_sgbd=" + urlServer.trim());
            writeUsed.writeln("porta_sgbd=" + portServer.trim());
            writeUsed.writeln("database_sgbd=" + dataBase.trim());
            writeUsed.writeln("user_sgbd=" + user.trim());
            writeUsed.writeln("psw_sgbd=" + Utils.encrypt( passWord.trim() ) );
            try {
                writeUsed.close();
            } catch (IOException ex) {
                Logger.getLogger(ArquivoConfigTerminal.class.getName()).log(Level.SEVERE, null, ex);
                GuiGlobals.showMessageErrorDlg("Falha no fechamento do arquivo [" + arquivo + extension + "] !", ex.getMessage() );
            }
        }
        
        if ( writeUsed.fileExists() )
        {
            processOk = true;
        }
        
        return processOk;
    }

    @Override
    public Writer getWriter() 
    {
        if ( write == null )
        {
            try 
            {
                write = new Writer( instalationDir, arquivo, extension );
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ArquivoConfigTerminal.class.getName()).log(Level.SEVERE, null, ex);
                GuiGlobals.showMessageErrorDlg("Falha na criação do arquivo [" + arquivo + extension + "] !", ex.getMessage() );
            }
            catch (Exception e) 
            {
            	GuiGlobals.showMessageErrorDlg("Falha na criação do arquivo [" + arquivo + extension + "] !", e.getMessage() );
            }
        }
        
        return write;
    }
    
    @Override
	public boolean validAtributes( boolean showMessage ) 
    {
    	boolean continuar = true;
		String msg = "";
		
		if (modelo == null || modelo.getName().isEmpty() )
		{
			msg += "Não foi encontrado o modelo do banco de dados no arquivo config.ini - Ex.: model_sgbd=SQL Server \n";
        	continuar = false;
        }
		
		if (getUrlServer() == null || getUrlServer().isEmpty() )
		{
			msg += "Não foi encontrado a url/ip do servidor de banco de dados no arquivo config.ini - Ex.: ip_sgbd=localhost \n";
        	continuar = false;
        }
        
		if ( getPortServer() == null || getPortServer().isEmpty() )
		{
			msg += "Não foi encontrado a porta do servidor de banco de dados no arquivo config.ini - Ex.: porta_sgbd=1433 \n";
        	continuar = false;
        }
		
		if ( getDataBase() == null || getDataBase().isEmpty() )
		{
			msg += "Não foi encontrado o nome do banco de dados no servidor no arquivo config.ini - Ex.: database_sgbd=cardealsgf \n";
        	continuar = false;
        }
		
		if ( getUser() == null || getUser().isEmpty() )
		{
			msg += "Não foi encontrado o usuário do banco de dados no servidor no arquivo config.ini - Ex.: user_sgbd=usersgf \n";
        	continuar = false;
        }
		
		if ( getPassWord() == null || getPassWord().isEmpty() )
		{
			msg += "Não foi encontrado senha de acesso ao banco de dados no servidor no arquivo config.ini - Ex.: psw_sgbd=[B@897yKb \n";
        	continuar = false;
        }
        
        if ( !continuar && !msg.isEmpty() && showMessage )
        {
        	GuiGlobals.showMessageErrorDlg("Falha na validação do arquivo [" + arquivo + extension + "] !", msg );
        }
        
        return continuar;
    }
    
    public String getUrlServer() {
		return urlServer;
	}

	public void setUrlServer(String urlServer) {
		this.urlServer = urlServer;
	}

	public String getPortServer() {
		return portServer;
	}
	
	public int getPortServerInt() {
		return Integer.valueOf( portServer );
	}

	public void setPortServer(String portServer) {
		this.portServer = portServer;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassWord() {
		return passWord;
	}
	
	public String getPassWordDecrypt() {
		return Utils.decrypt( passWord );
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	public ModeloBancoDeDados getModelo() {
		return modelo;
	}

	public void setModelo(ModeloBancoDeDados modelo) {
		this.modelo = modelo;
	}

	public String getFormattedUrl() 
	{
		
		String urlFormat = "";
		
		switch ( modelo ) 
		{
		case SQL_SERVER:
			urlFormat = "jdbc:sqlserver://";
			urlFormat += getUrlServer().trim();
			urlFormat += ":";
			urlFormat += getPortServer().trim();
			urlFormat += ";DatabaseName=";
			urlFormat += getDataBase().trim();
			urlFormat += ";SelectMethod=cursor";
			break;
			
		case MYSSQL:
			urlFormat = "jdbc:mysql://";
			urlFormat += getUrlServer().trim();
			urlFormat += ":";
			urlFormat += getPortServer().trim();
			urlFormat += "/";
			urlFormat += getDataBase().trim();			
			break;

		default:
			break;
		}
		
		return urlFormat;
	}

	public String getDriver() 
	{
		String driver = "";
		
		switch ( modelo ) 
		{
		case SQL_SERVER:
			driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			break;
			
		case MYSSQL:
			driver = "com.mysql.jdbc.Driver";
			break;

		default:
			break;
		}
		
		return driver;
	}
	
	public String getDialect() 
	{
		String dialect = "";
		
		switch ( modelo ) 
		{
		case SQL_SERVER:
			dialect = "org.hibernate.dialect.SQLServerDialect";
			break;
			
		case MYSSQL:
			dialect = "org.hibernate.dialect.MySQL5InnoDBDialect";
			break;

		default:
			break;
		}
		
		return dialect;
	}
    
}
