/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cardeal.globals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Sergio
 */
public class Writer 
{
    private String arqUsed;
    private String dirUsed;
    private String extensionUsed;
    private String arquivo;
    private FileWriter arq;
    private PrintWriter gravarArq;
            
    public Writer( String dir, String nameArq, String extension, boolean clearArq) throws IOException, Exception
    {
        initObject( dir, nameArq, extension );
        
        if ( clearArq && fileExists() )
        {
            close();
            new File( arquivo ).delete();
            destroyObjects();
            initObject( dir, nameArq, extension );
        }        
        
    }
    
    public Writer( String dir, String nameArq, String extension) throws IOException, Exception
    {
        initObject(dir, nameArq, extension );        
    }
    
    private void initObject( String dir, String nameArq, String extension ) throws IOException, Exception
    {
        arqUsed = ( ( nameArq == null && !nameArq.isEmpty() ) ? DateTimeUtils.getDateAndTime() : nameArq );        
        extensionUsed = ( ( extension == null && !extension.isEmpty() ) ? ".txt" : extension );
        if ( dir != null )
            dirUsed = ( ( !dir.isEmpty() && dir.substring( dir.length()-1, dir.length() ).contains("\\/") ) ? dir : dir + GuiGlobals.getSeparador() );
        
        if ( !( dirUsed == null || dirUsed.isEmpty() ) )
        {
            arquivo = dirUsed+arqUsed+extensionUsed;
            arq = new FileWriter(arquivo, true);
            gravarArq = new PrintWriter(arq);
        }
        else
        {
        	GuiGlobals.showMessageErrorDlg("Falha na criação do arquivo", " Parâmetros enviados: \n dir=" + dir + "\n nameArq=" + nameArq + "\n extension=" + extension);
        }
    }
    
    public void write( String text )
    {
        gravarArq.print(text);
    }
    
    public void writeln( String text )
    {
        gravarArq.println(text);
    }
    
    public boolean fileExists()
    {
        boolean exists = false;
        File file = new File( arquivo );

        exists = file.exists();
        file = null;

        return exists;
    }
    
    public void close() throws IOException
    {
        if( gravarArq != null )
            gravarArq.close();
        
        if( arq != null )
            arq.close();
    }

    private void destroyObjects() 
    {
        arqUsed = null;
        dirUsed = null;
        extensionUsed = null;
        arquivo = null;
        arq = null;
        gravarArq = null;
    }
    
}
