package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.ProcessWorker;
import br.com.cardeal.globals.GuiGlobals;

import javax.swing.UIManager;

public class MenuPacking extends JPanel implements OperationWindow {

	private static final long serialVersionUID = 1L;
	private JButton btnPacking;
	private JButton btnRepalletizar;
	private JButton btnPalletVirtual;
	private JButton btnSair;	
	private JButton btnEstoqueEmTransito;
	private JButton btnInsumo;
	private JButton btnExcecaoPeso;  

	public MenuPacking() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		btnPacking = new JButton(GuiGlobals.getBundle().getString("STR000007"));
		btnPacking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_EMBALAGEM_EMBALAGEM) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.EMBALAGEM_EMBALA ) )
						GuiGlobals.getMain().packing();
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
			}
		});
		btnPacking.setBackground(UIManager.getColor("Button.background"));
		btnPacking.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnPacking.setBounds(160, 69, 315, 108);
		add(btnPacking);
		
		btnRepalletizar = new JButton(GuiGlobals.getBundle().getString("STR000008"));
		btnRepalletizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_EMBALAGEM_REPALETIZAR) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.EMBALAGEM_REPALETIZAR ) )
						GuiGlobals.getMain().packing4();
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
			}
		});
		btnRepalletizar.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnRepalletizar.setBounds(160, 205, 315, 108);
		add(btnRepalletizar);
		
		btnPalletVirtual = new JButton(GuiGlobals.getBundle().getString("STR000013"));
		btnPalletVirtual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_EMBALAGEM_PALLET_VIRTUAL) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.EMBALAGEM_PALLET_VIRTUAL ) )
						GuiGlobals.getMain().packing5();
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
			}
		});
		btnPalletVirtual.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnPalletVirtual.setBounds(160, 347, 315, 108);
		add(btnPalletVirtual);
		
		btnEstoqueEmTransito = new JButton(GuiGlobals.getBundle().getString("STR000014"));
		btnEstoqueEmTransito.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_EMBALAGEM_ESTOQUE_TRANSITO) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.EMBALAGEM_TRANSITO ) )
						GuiGlobals.getMain().packing2();
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
			}
		});
		btnEstoqueEmTransito.setBackground(UIManager.getColor("Button.background"));
		btnEstoqueEmTransito.setFont(new Font("Tahoma", Font.BOLD, 25));
		btnEstoqueEmTransito.setBounds(534, 69, 315, 108);
		add(btnEstoqueEmTransito);
		
		btnInsumo = new JButton(GuiGlobals.getBundle().getString("STR000015"));
		btnInsumo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_EMBALAGEM_ESTOQUE_INSUMO) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.EMBALAGEM_INSUMO ) )
						GuiGlobals.getMain().packing3();
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
			}
		});
		btnInsumo.setBackground(UIManager.getColor("Button.background"));
		btnInsumo.setFont(new Font("Tahoma", Font.BOLD, 25));
		btnInsumo.setBounds(534, 205, 315, 108);
		add(btnInsumo);
				
		btnExcecaoPeso = new JButton(GuiGlobals.getBundle().getString("STR000016"));
		btnExcecaoPeso.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_EMBALAGEM_EXCECAO_PESO_PADRAO) )
				{
					if( GuiGlobals.insertControlAccess( ProcessWorker.EXCECAO_PESO_PADRAO ) )	// registra o processo da tela, por filial, terminal, processo e usu�rio
						GuiGlobals.getMain().packingExcecaoPesoPadrao();
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
			}
		});
		btnExcecaoPeso.setFont(new Font("Tahoma",Font.BOLD,28));
		btnExcecaoPeso.setBounds(160, 489, 315, 108);  // setBounds(coordenada x, coordenada y, largura, altura) 
		add(btnExcecaoPeso);
		
		btnSair = new JButton(GuiGlobals.getBundle().getString("STR000030"));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiGlobals.getMain().exitOperation();
			}
		});
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnSair.setBounds(534, 347, 315, 108);
		add(btnSair);					
	}	

	public String getTitle() {
		return GuiGlobals.getBundle().getString("STR000004");
	}

	public JPanel getPanel() {
		return this;
	}

	public String getInitialMessage() {
		return GuiGlobals.getBundle().getString("STR000003");
	}
	public void start() {
	}
}
