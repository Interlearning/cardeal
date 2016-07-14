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
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.UserInfo;

import javax.swing.UIManager;

public class ReportsPanel extends JPanel implements OperationWindow {

	private static final long serialVersionUID = 1L;
	private JButton btnDailyProduction;
	private JButton btnPalletProduction;
	private JButton btnSair;

	public ReportsPanel() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		UserInfo info = GuiGlobals.getUserInfo();
		
		btnDailyProduction = new JButton("Produ\u00E7\u00E3o Di\u00E1ria");
		btnDailyProduction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiGlobals.getMain().dailyProdReport();
			}
		});
		btnDailyProduction.setBackground(UIManager.getColor("Button.background"));
		btnDailyProduction.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnDailyProduction.setBounds(160, 69, 315, 108);
		add(btnDailyProduction);
		
		if ( !info.isPermittedComponentUpdate( ComponentPermission.MENU_OPERACOES_RELATORIOS_PRODUCAO_DIARIA ) ) {
			btnDailyProduction.setEnabled(false);
		}
		
		btnPalletProduction = new JButton("Produ\u00E7\u00E3o Paletes");
		btnPalletProduction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiGlobals.getMain().palletsProdReport();
			}
		});
		btnPalletProduction.setBackground(UIManager.getColor("Button.background"));
		btnPalletProduction.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnPalletProduction.setBounds(534, 69, 315, 108);
		add(btnPalletProduction);
		
		if ( !info.isPermittedComponentUpdate( ComponentPermission.MENU_OPERACOES_RELATORIOS_PALETE ) ) {
			btnPalletProduction.setEnabled(false);
		}
		
		btnSair = new JButton("Sair");
		btnSair.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiGlobals.getMain().exitOperation();
			}
		});
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnSair.setBounds(160, 208, 315, 108);
		add(btnSair);
	}

	public String getTitle() {
		return "Menu de Relatórios";
	}

	public JPanel getPanel() {
		return this;
	}

	public String getInitialMessage() {
		return "";
	}
	public void start() {
	}
}
