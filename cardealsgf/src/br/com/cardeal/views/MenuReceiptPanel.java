package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.ProcessWorker;
import br.com.cardeal.globals.GuiGlobals;

public class MenuReceiptPanel extends JPanel  implements OperationWindow 
{
	private static final long serialVersionUID = 1L;
	private JButton btnCompra;
	private JButton btnDevolucao;
	private JButton btnSair;
	
	public MenuReceiptPanel() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		btnCompra = new JButton("Compras");
		btnCompra.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnCompra.setBounds(366, 150, 315, 108);
		btnCompra.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_RECEBIMENTO_COMPRAS) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.RECEBIMENTO_COMPRAS ) )
					{
						GuiGlobals.getMain().receiptPurshase();
					}
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
			}
		});
		add(btnCompra);
		
		btnDevolucao = new JButton("Devolução");
		btnDevolucao.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnDevolucao.setBounds(366, 300, 315, 108);
		btnDevolucao.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_RECEBIMENTO_DEVOLUCAO) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.RECEBIMENTO_DEVOLUCAO ) )
						GuiGlobals.getMain().shipmentDevolution();
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
			}
		});
		add(btnDevolucao);
		
		btnSair = new JButton("");
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnSair.setBounds( 917, 480, 103, 94);
		btnSair.setIcon(new ImageIcon(MenuReceiptPanel.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.getMain().exitOperation();
			}
		});
		
		add(btnSair);
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public String getInitialMessage() {
		return "";
	}

	@Override
	public void start() {
		
	}

	@Override
	public String getTitle() {
		return "Menu Recebimento";
	}

}
	
