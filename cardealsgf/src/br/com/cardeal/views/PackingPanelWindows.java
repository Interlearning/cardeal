package br.com.cardeal.views;

import javax.swing.JTextField;

public interface PackingPanelWindows {
	public void addProgress( int qtd );
	public void refreshQtds();
	public JTextField getTxtTaraTotal();
	public JTextField getTxtQtdPecas();
	public JTextField getTxtPesoLiquido();
}
