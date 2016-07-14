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
import br.com.cardeal.importacao.ImportacaoProduto;
import br.com.cardeal.model.ServerSetup;
import javax.swing.UIManager;

public class MenuPanel extends JPanel implements OperationWindow {

	private static final long serialVersionUID = 1L;
	private JButton btnNewButton;
	private JButton btnExpedio;
	private JButton btnRecebimento;
	private JButton btnSair;
	private JButton btnAbreCaixa;
	private JButton btnRelatrios;
	private JButton btnTeste;

	public MenuPanel() {
		
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		btnNewButton = new JButton("Menu Embalagem");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_OPERACOES_MENU_EMBALAGEM) )
				{
					GuiGlobals.getMain().menuPacking();
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
			}
		});
		btnNewButton.setBackground(UIManager.getColor("Button.background"));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnNewButton.setBounds(160, 69, 315, 108);
		add(btnNewButton);
		
		btnExpedio = new JButton("Expedi\u00E7\u00E3o");
		btnExpedio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_OPERACOES_EXPEDICAO) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.EXPEDICAO ) )
						GuiGlobals.getMain().shipment();
				}
				else
				{
					GuiGlobals.showMessage(GuiGlobals.getBundle().getString("user_not_permission"));
				}
			}
		});
		btnExpedio.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnExpedio.setBounds(160, 205, 315, 108);
		add(btnExpedio);
		
		btnRecebimento = new JButton("Recebimento");
		btnRecebimento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_OPERACOES_RECEBIMENTO) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.RECEBIMENTO ) )
						GuiGlobals.getMain().packingRecebimento();		
				}
				else
				{
					GuiGlobals.showMessage(GuiGlobals.getBundle().getString("user_not_permission"));
				}
				
			}
		});
		btnRecebimento.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnRecebimento.setBounds(160, 347, 315, 108);
		add(btnRecebimento);
		
		if ( "ADMINISTRADORES".equalsIgnoreCase( GuiGlobals.getUserInfo().getUser().getProfile().getName()) ){
			
			btnTeste = new JButton("Importação");
			btnTeste.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					ServerSetup server = GuiGlobals.getDaoFactory().getServerSetupDao().find();
					GuiGlobals.showMessage("Aguarde importação...");
					new ImportacaoProduto(  server.getReadDirectory() + GuiGlobals.getSeparador() + "cadastro_produtos.csv").execute(false);
					GuiGlobals.showMessage("Importação finalizada.");
				}
			});
			btnTeste.setFont(new Font("Tahoma", Font.BOLD, 28));
			btnTeste.setBounds(160, 489, 315, 108);
			add(btnTeste);
		}
		
		btnSair = new JButton("Sair");
		btnSair.setIcon(new ImageIcon(MenuPanel.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiGlobals.removeAllControlAccess();
				GuiGlobals.getMain().startLogin();
			}
		});
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnSair.setBounds(534, 347, 315, 108);
		add(btnSair);
		
		btnAbreCaixa = new JButton("Abre Caixa");
		btnAbreCaixa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_OPERACOES_ABRE_CAIXA) )
				{
					if ( GuiGlobals.insertControlAccess( ProcessWorker.ABRECAIXA ) )
						GuiGlobals.getMain().packing6();		
				}
				else
				{
					GuiGlobals.showMessage(GuiGlobals.getBundle().getString("user_not_permission"));
				}
			}
		});		
		btnAbreCaixa.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnAbreCaixa.setBounds(534, 69, 315, 108);
		add(btnAbreCaixa);
		
		btnRelatrios = new JButton("Relat\u00F3rios");
		btnRelatrios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( GuiGlobals.isPermitted(ComponentPermission.MENU_OPERACOES_RELATORIOS) )
				{
					GuiGlobals.getMain().reports();		
				}
				else
				{
					GuiGlobals.showMessage(GuiGlobals.getBundle().getString("user_not_permission"));
				}
			}
		});
		btnRelatrios.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnRelatrios.setBounds(534, 205, 315, 108);
		add(btnRelatrios);
	}

	public String getTitle() {
		return GuiGlobals.getBundle().getString("STR000002");
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
