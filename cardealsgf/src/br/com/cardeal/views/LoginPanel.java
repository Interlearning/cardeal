package br.com.cardeal.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.User;

public class LoginPanel extends JPanel implements OperationWindow{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label_1;
	private JPasswordField txtPassword;
	private JButton button;
	private JButton button_1;
	
	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		setLayout(null);
		setBounds(0, 59, 1018, 600);
		//Retirado a pedido de Antonio
//		label = new JLabel("Login");
//		label.setFont(new Font("Tahoma", Font.PLAIN, 24));
//		label.setBounds(416, 183, 68, 29);
//		add(label);
//		
//		txtLogin = new JTextField();
//		txtLogin.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				GuiGlobals.virtualKeyboard(null, txtLogin, null, null);
//			}
//		});
//		txtLogin.setFont(new Font("Tahoma", Font.PLAIN, 24));
//		txtLogin.setColumns(10);
//		txtLogin.setBounds(501, 180, 164, 43);
//		add(txtLogin);
		
		label_1 = new JLabel("Senha");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
		label_1.setBounds(416, 255, 73, 29);
		add(label_1);
		
		txtPassword = new JPasswordField();
		txtPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {	
				productMousePressed();
			}
		});
		txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 24));
		txtPassword.setBounds(501, 250, 164, 43);
		add(txtPassword);
		
		button = new JButton("OK");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		button.setFont(new Font("Tahoma", Font.PLAIN, 28));
		button.setActionCommand("OK");
		button.setBounds(401, 334, 139, 62);
		add(button);
		
		button_1 = new JButton("Cancelar");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 28));
		button_1.setActionCommand("Cancelar");
		button_1.setBounds(552, 334, 155, 62);
		add(button_1);
	}
	
	public void productMousePressed() {
		GuiGlobals.virtualKeyboard(null, txtPassword, "login", this);		
	}
	
	public void login() {
//		String id = txtLogin.getText();
		
//		Retirado o login a pedido de Antonio
//		if(id == null || id.length() == 0) {
//			GuiGlobals.getMain().showMessage("Informe o Login do operador");
//			return;
//		}
		
		@SuppressWarnings("deprecation")
		String psw = txtPassword.getText();
		if(psw == null || psw.length() == 0) {
			GuiGlobals.getMain().showMessage("Informe a Senha do operador");
			return;
		}
		
		User user = null;
		GuiGlobals.waitCursor();
		try {
			user = GuiGlobals.getDaoFactory().getUserDao().findPsw(psw);
		}
		finally {
			GuiGlobals.defaultCursor();
		}
		if(user == null) {
			GuiGlobals.getMain().showMessage("Senha não encontrada no sistema!");
			return;			
		}
		
		
		GuiGlobals.getUserInfo().login(user);
		GuiGlobals.getMain().login();
	}
	
	private void logout() {
		GuiGlobals.getMain().close();		
	}
	
	public String getTitle() {
		return "Login";
	}

	public JPanel getPanel() {
		return this;
	}

	public String getInitialMessage() {
		return GuiGlobals.getBundle().getString("STR000001");
	}
	public void start() {
	}
}
