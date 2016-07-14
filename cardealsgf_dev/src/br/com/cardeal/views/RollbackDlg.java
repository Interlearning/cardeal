package br.com.cardeal.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Stock;

public class RollbackDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean confirmed = false;	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtValue;
	
	public RollbackDlg getOuter() {
		return RollbackDlg.this;
	}

	public RollbackDlg(final Stock stock, final TypeStock typeStock) {		
		
		setTitle("ESTORNO DE EMBALAGEM - FILIAL: " + Setup.getCompany().getId());
		setModalityType(ModalityType.DOCUMENT_MODAL);

		setBounds(100, 100, 442, 341);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtValue = new JTextField();
		txtValue.setEditable(false);
		txtValue.setText(String.valueOf(stock.getId()));
		txtValue.setHorizontalAlignment(SwingConstants.CENTER);
		txtValue.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtValue.setColumns(10);
		txtValue.setBounds(70, 71, 285, 65);
		txtValue.selectAll();
		contentPanel.add(txtValue);
		
		JLabel lblQuantidade = new JLabel(GuiGlobals.getBundle().getString("STR000009"));
		lblQuantidade.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuantidade.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblQuantidade.setBounds(12, 29, 400, 29);
		contentPanel.add(lblQuantidade);
		
		JButton button_2 = new JButton("Confirmar");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				if ( typeStock == stock.getTypeStock() ) {
					confirmed = true;
					setVisible(false);
				}
				else{ 
					GuiGlobals.showMessage("Id da última caixa Invalido");
				}
			
			}
		});
		button_2.setIcon(new ImageIcon(RollbackDlg.class.getResource("/32x32/Apply.png")));
		button_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		button_2.setBounds(30, 179, 175, 74);
		contentPanel.add(button_2);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnCancelar.setIcon(new ImageIcon(RollbackDlg.class.getResource("/32x32/Cancel.png")));
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCancelar.setBounds(219, 179, 175, 74);
		contentPanel.add(btnCancelar);
		
		setModal(true);  // WJSP 11/06/2015 --  Jdialog sempre em foco -> para não permiir abrir mais de um por tela 
	}

	public boolean isConfirmed() {
		return confirmed;
	}
	
}
