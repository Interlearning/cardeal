package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import br.com.cardeal.globals.GuiGlobals;

public class TelaPiscante extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnSair;
	private JDialog thisDlg;
	
	public TelaPiscante( String message ) 
	{
		setTitle("   ******************************************************* A T E N Ç Ã O ***********************************************");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(0, 0, 1024, 768);
		setLayout(null);
		
		thisDlg = this;
		
		List<String> messageList = textPrepare(message, 50);
		int line = 30;
		for ( String text : messageList )
		{
			
			JLabel lblMsg = new JLabel(text);
			lblMsg.setBounds(50, line, 1024, 50);			
			lblMsg.setFont(new Font("Tahoma", Font.PLAIN, 40));
			add(lblMsg);
			lblMsg = null;
			
			line += 55;
		}
		
		btnSair = new JButton("");
		btnSair.setBounds( 900, 620, 103, 94);
		btnSair.setIcon(new ImageIcon(ShipmentPanel.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {								
				dispose();
			}
		});
		add(btnSair);
		
		piscaTela();
			
	}

	private void piscaTela() 
	{
		new Thread() { 
			@Override 
			public void run() 
			{ 
				while ( true )
				{
					thisDlg.getContentPane().setBackground(Color.RED);
					GuiGlobals.sleep(500);
					thisDlg.getContentPane().setBackground(Color.WHITE);
					GuiGlobals.sleep(500);
				}
			} 
		}.start();

	}
	
	private List<String> textPrepare( String clTexto, int nlLimDesc )
	{
		List<String> alTxtSplit = new ArrayList<String>();
		String clTxt = clTexto.trim();
		String clTxtAux = "";
		String[] alTxtAux = null;
		int nlCount	= 0;	
	
		if ( clTxt.length() <= nlLimDesc )
		{
			alTxtSplit.add( clTxt );
		}
		else
		{
	
			clTxt = clTxt.replace("  ", " ");
			alTxtAux = clTxt.split(" ");
	
			for (int nlx = 0; nlx < alTxtAux.length; nlx++)  //nlx := 1 To Len( alTxtAux )
			{
	
				nlCount += ( alTxtAux[nlx] + " " ).length();
	
				if ( nlCount <= nlLimDesc )
					clTxtAux += alTxtAux[nlx] + " ";
				else
				{
					alTxtSplit.add( clTxtAux );
					nlCount = ( alTxtAux[nlx] + " " ).length();
					clTxtAux = alTxtAux[nlx] + " ";
				}
	
			}
	
			if ( !clTxtAux.isEmpty() )
				alTxtSplit.add( clTxtAux );		
			
		}
	
		return alTxtSplit;
	}
	
}
