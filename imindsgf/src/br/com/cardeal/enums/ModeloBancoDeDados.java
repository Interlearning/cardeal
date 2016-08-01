/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cardeal.enums;

/**
 *
 * @author Sergio
 */
public enum ModeloBancoDeDados 
{
    SQL_SERVER ("SQL Server"),
    MYSSQL ("MySQL");
    
    private String name;

    public String getName() {
        return name;
    }
    
    ModeloBancoDeDados ( String name )
    {
        this.name = name;
    }
    
    public static String[] valuesOfString()
    {
        ModeloBancoDeDados[] modelos = ModeloBancoDeDados.values();
        String[] array = new String[modelos.length];
        
        for ( int i = 0; i < modelos.length; i++)
        {
            array[i] = modelos[i].getName();
        }
        
        return array;
        
    }
    
    public static ModeloBancoDeDados getModelByString( String modeloString )
    {
        ModeloBancoDeDados[] modelos = ModeloBancoDeDados.values();
        ModeloBancoDeDados modeloReturn = null;
         
        for ( int i = 0; i < modelos.length; i++)
        {
            if ( modeloString.equalsIgnoreCase( modelos[i].getName() ) )
            {
                modeloReturn = modelos[i];
                break;
            }
        }
        
        return modeloReturn;
    }
}
