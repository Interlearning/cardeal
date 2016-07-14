package br.com.cardeal.views;

import javax.swing.JPanel;

public interface OperationWindow {
	public String getTitle();
	public JPanel getPanel();
	public String getInitialMessage();
	public void start();
}
