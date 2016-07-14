package br.com.cardeal.services;

import java.awt.Color;

import javax.swing.JLabel;

import br.com.cardeal.globals.GuiGlobals;

public class MessageService extends Thread 
{
	private JLabel lblMsg;
	private String message;
	
	public MessageService(){}
	
	public MessageService( JLabel lblMsg, String message )
	{
		this.lblMsg = lblMsg;
		this.message = message;
	}
	
	public void run() 
	{
		lblMsg.setText(message);
		
		for ( int i = 0; i < 15; i++)
		{
			if ( i%2 == 0 )
				lblMsg.setForeground(new Color(255, 232, 26));
			else
				lblMsg.setForeground(new Color(30, 144, 255));
			
			GuiGlobals.sleep(100);
		}
	}
}
