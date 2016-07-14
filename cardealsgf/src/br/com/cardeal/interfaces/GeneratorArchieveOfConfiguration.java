/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cardeal.interfaces;

import br.com.cardeal.globals.Writer;

/**
 *
 * @author Sergio
 */
public interface GeneratorArchieveOfConfiguration 
{
    public boolean generate();    
    public Writer getWriter();
	boolean validAtributes(boolean showMessage);
}
