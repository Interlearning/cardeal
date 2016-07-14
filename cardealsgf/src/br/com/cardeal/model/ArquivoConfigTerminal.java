/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cardeal.model;

import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Writer;
import br.com.cardeal.interfaces.GeneratorArchieveOfConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergio
 */
public class ArquivoConfigTerminal implements GeneratorArchieveOfConfiguration
{
    private String terminal;
    private String company;
    private String instalationDir;
	private Writer write;
    private final String arquivo = "config";
    private final String extension = ".ini";
    
    public ArquivoConfigTerminal( String company, String terminal )
    {
        this.company = company;
        this.terminal = terminal;
    }
    
    public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public ArquivoConfigTerminal( String instalationDir, String company, String terminal )
    {
        this.instalationDir = instalationDir;
        this.company = company;
        this.terminal = terminal;
    }

    @Override
    public boolean generate() 
    {        
        boolean processOk = false;
        Writer writeUsed = getWriter();
        
        if ( writeUsed != null )
        {
            writeUsed.writeln("#CONFIGURACOES DO TERMINAL");
            writeUsed.writeln("filial=" + company.trim());
            writeUsed.writeln("terminal=" + terminal.trim());
            writeUsed.writeln("path=" + instalationDir.trim().replace("\\", "\\\\"));
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
                write = new Writer( instalationDir, arquivo, extension, true );
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
		
		if (getTerminal() == null)
		{
			msg += "Não foi encontrado o terminal configurado no arquivo config.ini - Ex.: terminal=01 \n";
        	continuar = false;
        }
        
        if (getCompany() == null)
        {
        	msg += "Não foi encontrado a filial configurado no arquivo config.ini - Ex.: filial=99 \n";
        	continuar = false;
        }
        
        if ( !continuar && !msg.isEmpty() && showMessage )
        {
        	GuiGlobals.showMessageErrorDlg("Falha na validação do arquivo [" + arquivo + extension + "] !", msg );
        }
        
        return continuar;
	}
    
    public String getInstalationDir() {
		return ( ( instalationDir == null || instalationDir.isEmpty() ) ? getRootPath() : instalationDir );
	}

	public void setInstalationDir(String instalationDir) 
	{
		this.instalationDir = instalationDir;
	}

	public static String getRootPath() 
	{
		return new File(".").getAbsolutePath();
	}
    
}
