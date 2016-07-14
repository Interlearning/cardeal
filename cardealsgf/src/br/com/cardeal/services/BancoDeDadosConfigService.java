/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cardeal.services;

import br.com.cardeal.enums.ModeloBancoDeDados;
import br.com.cardeal.interfaces.GeneratorArchieveOfConfiguration;
import br.com.cardeal.model.ArquivoConfigBancoDeDados;
import br.com.cardeal.model.ArquivoConfigTerminal;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Utils;

/**
 *
 * @author Sergio
 */
public class BancoDeDadosConfigService 
{    
    private ModeloBancoDeDados modelo;
    private String urlServer;
    private String portServer;
    private String dataBase;
    private String user;
    private String passWord;
    private String instalationDir;
    
    public BancoDeDadosConfigService( ModeloBancoDeDados modelo, String urlServer, String portServer, String dataBase, String user, String passWord, String instalationDir )
    {
        this.modelo = modelo;
        this.urlServer = urlServer;
        this.portServer = portServer;
        this.dataBase = dataBase;
        this.user = user;
        this.passWord = passWord;
        this.instalationDir = instalationDir;
    }   
    
    public boolean create()
    {
        boolean continueProcess = true;
        boolean processOk = false;
        String message = "";
        
        if ( getUrlServer() == null || getUrlServer().isEmpty() )
        {
            continueProcess = false;
            message += "IP/URL inv√°lida \n";
        }
        
        if ( getPortServer() == null || getPortServer().isEmpty() )
        {
            continueProcess = false;
            message += "Porta inv·lido \n";
        }
        
        if ( getDataBase() == null || getDataBase().isEmpty() )
        {
            continueProcess = false;
            message += "DataBase inv·lido \n";
        }
        
        if ( getUser() == null || getUser().isEmpty() )
        {
            continueProcess = false;
            message += "Usu·rio inv·lido \n";
        }
        
        if ( getPassWord() == null || getPassWord().isEmpty() )
        {
            continueProcess = false;
            message += "Senha inv√°lida \n";
        }
        
        if ( continueProcess )
        {
            try
            {
                if ( Utils.createDir( getInstalationDir() ) )
                {

                    GeneratorArchieveOfConfiguration config = new ArquivoConfigBancoDeDados( getInstalationDir(), getModelo(), getUrlServer(), getPortServer(), getDataBase(), getUser(), getPassWord() );
                    processOk = config.generate();
                    
                }
            }
            catch ( Exception e )
            {
            	GuiGlobals.showMessageErrorDlg("Falha na criaÁ„o do arquivo config.ini !", e.getMessage() );
            }
        }
        else
        {
        	GuiGlobals.showMessageDlg("ATEN«√O!", message);
        }
        
        return processOk;
        
    }

    public ModeloBancoDeDados getModelo() {
        return modelo;
    }

    public void setModelo(ModeloBancoDeDados modelo) {
        this.modelo = modelo;
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

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getInstalationDir() {
        return instalationDir;
    }

    public void setInstalationDir(String instalationDir) {
        this.instalationDir = instalationDir;
    }
}
