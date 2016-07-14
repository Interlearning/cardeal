/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cardeal.services;

import br.com.cardeal.interfaces.GeneratorArchieveOfConfiguration;
import br.com.cardeal.model.ArquivoConfigTerminal;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Utils;

/**
 *
 * @author Sergio
 */
public class TerminalConfigService 
{
    private String company;
    private String terminal;
    private String instalationDir;
    
    public TerminalConfigService( String company, String terminal, String instalationDir )
    {
        this.company = company;
        this.terminal = terminal;
        this.instalationDir = instalationDir;
    }
    
    public void setCompany( String company )
    {
        this.company = company;
    }
    
    public String getCompany()
    {
        return company;
    }
    
    public void setTerminal( String terminal )
    {
        this.terminal = terminal;
    }
    
    public String getTerminal()
    {
        return terminal;
    }
    
    public String getInstalationDir() 
    {
        return instalationDir;
    }

    public void setInstalationDir(String instalationDir) 
    {
        this.instalationDir = instalationDir;
    }
    
    public boolean create()
    {
        boolean continueProcess = true;
        boolean processOk = false;
        String message = "";
        
        if ( getCompany() == null || getCompany().isEmpty() )
        {
            continueProcess = false;
            message += "Filial inválido! \n";
        }
        
        if ( getTerminal() == null || getTerminal().isEmpty() )
        {
            continueProcess = false;
            message += "Terminal inválido \n";
        }
        
        if ( getInstalationDir() == null || getInstalationDir().isEmpty() )
        {
            continueProcess = false;
            message += "Diretório de instalação inválido \n";
        }
        
        if ( continueProcess )
        {
            try
            {
                if ( Utils.createDir( getInstalationDir() ) )
                {

                    GeneratorArchieveOfConfiguration config = new ArquivoConfigTerminal( getInstalationDir(), getCompany(), getTerminal() );
                    processOk = config.generate();
                    
                }
            }
            catch ( Exception e )
            {
                 GuiGlobals.showMessageErrorDlg("Falha na criação do arquivo config.ini !", e.getMessage() );
            }
        }
        else
        {
        	GuiGlobals.showMessageDlg("ATENÇÃO!", message);
        }
        
        return processOk;
    }
    
}
