package br.com.cardeal.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MessageRun extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = new JPanel();
	private String title;
	private String message;
	
	public void start(String title, String message)
	{
		this.message = message;
		this.title = title;
		
		new Thread() { 
			@Override 
			public void run() 
			{ 
				initDlg();
				setVisible(true);
			} 
		}.start();
	}
	
	private void initDlg()
	{
		setTitle( title );
		setModalityType(ModalityType.APPLICATION_MODAL);

		setBounds(230, 70, 450, 200);
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		List<String> messageList = textPrepare(message, 50);
		int line = 40;
		for ( String text : messageList )
		{
			JLabel lblMsg = new JLabel(text);
			lblMsg.setBounds(100, line, 800, 30);			
			lblMsg.setFont(new Font("Tahoma", Font.BOLD, 20));
			contentPanel.add(lblMsg);
			lblMsg = null;
			
			line += 55;
		}
	}
	
	public void close()
	{
		dispose();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		repaint();
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
	
			for (int nlx = 0; nlx < alTxtAux.length; nlx++)
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
