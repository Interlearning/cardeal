package br.com.cardeal.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.com.cardeal.enums.ShipmentOpcaoMenu;

public class ShipmentMenuManualDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private JButton btnFIFO;
	private JButton btnMateriaPrima;
	private JButton btnEtiqueta;
	private JButton btnCancelar;
	private ShipmentOpcaoMenu opcao;	

	/**
	 * Create the dialog.
	 */
	public ShipmentMenuManualDlg() {
		
		setTitle("Opções");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		setBounds(230, 70, 345, 530);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		btnFIFO = new JButton("FIFO");
		btnFIFO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opcao = ShipmentOpcaoMenu.FIFO;
				setVisible(false);
			}
		});
		btnFIFO.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnFIFO.setBounds(10, 30, 315, 108);		
		contentPanel.add(btnFIFO);
		
		btnMateriaPrima = new JButton("Matéria Prima");
		btnMateriaPrima.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opcao = ShipmentOpcaoMenu.MATERIA_PRIMA;
				setVisible(false);
			}
		});
		btnMateriaPrima.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnMateriaPrima.setBounds(10, 148, 315, 108);		
		contentPanel.add(btnMateriaPrima);
		
		btnEtiqueta = new JButton("Etiqueta");
		btnEtiqueta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opcao = ShipmentOpcaoMenu.ETIQUETA;
				setVisible(false);
			}
		});
		btnEtiqueta.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnEtiqueta.setBounds(10, 266, 315, 108);		
		contentPanel.add(btnEtiqueta);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opcao = ShipmentOpcaoMenu.CANCELAR;
				setVisible(false);
			}
		});
		btnCancelar.setIcon(new ImageIcon(ShipmentMenuManualDlg.class.getResource("/32x32/Cancel.png")));
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCancelar.setBounds(10, 384, 315, 108);		
		contentPanel.add(btnCancelar);
	}

	public ShipmentOpcaoMenu getOpcao() {
		return opcao;
	}	

}





