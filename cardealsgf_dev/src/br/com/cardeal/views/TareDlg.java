package br.com.cardeal.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.com.cardeal.globals.Utils;
import br.com.cardeal.globals.GuiGlobals;

public class TareDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double value;
	private boolean confirmed = false;
	
	private String title = "";
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtValue;
	
	public TareDlg getOuter() {
		return TareDlg.this;
	}

	
	/* WJSP 30/06/2015
	 * Dialog de Tara Total será igual para os txt's de Pallet
	 */
	public TareDlg(double value,String title){
		this.value = value;
		this.title = title;
		setTitle(title);
		
		initializeComponents();
	}
	
	public TareDlg(double value) {
		this.value = value;
		this.title = "Tara da Caixa";
		setTitle(title);
		initializeComponents();
	}

	/**
	 * Create the dialog.
	 */
	//public TareDlg(double value) {
	
	public void initializeComponents(){
		//setTitle("TARA DA CAIXA");
		setModalityType(ModalityType.DOCUMENT_MODAL);
		//this.value = value;
		
		setBounds(100, 100, 442, 337);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtValue = new JTextField();
		txtValue.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mousePressedEvent();		
			}
		});
		txtValue.setText(Utils.formatWeight(value));
		txtValue.setHorizontalAlignment(SwingConstants.CENTER);
		txtValue.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtValue.setColumns(10);
		txtValue.setBounds(70, 71, 285, 65);
		txtValue.selectAll();
		contentPanel.add(txtValue);
		
		/* WJSP 30/06/2015
		 * Label de Acordo com o título do txt pressionado
		 */
		//JLabel lblQuantidade = new JLabel("Tara da Caixa (kg)");
		JLabel lblQuantidade = new JLabel(title);
		
		lblQuantidade.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuantidade.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblQuantidade.setBounds(12, 29, 400, 29);
		contentPanel.add(lblQuantidade);
		
		JButton button_2 = new JButton("Confirmar");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				validateValue();
				confirmed = true;
				setVisible(false);
			}
		});
		button_2.setIcon(new ImageIcon(TareDlg.class.getResource("/32x32/Apply.png")));
		button_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		button_2.setBounds(29, 174, 175, 74);
		contentPanel.add(button_2);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnCancelar.setIcon(new ImageIcon(TareDlg.class.getResource("/32x32/Cancel.png")));
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCancelar.setBounds(218, 174, 175, 74);
		contentPanel.add(btnCancelar);
		
		setModal(true);  // WJSP 05/06/2015 --  Jdialog sempre em foco -> para não permiir abrir mais de um por tela 
	}
	
	public void mousePressedEvent() {
		GuiGlobals.virtualKeyboard(getOuter(), txtValue, "validateValue", this);				
	}
	
	public void validateValue() {
		try {
			double newVal = Double.parseDouble(txtValue.getText());
			if(newVal < 0 || newVal > 1000)
				txtValue.setText(Utils.formatWeight(value));
			else
				value = newVal;
		}
		catch(Exception e) {
			txtValue.setText(Utils.formatWeight(value));
		}
	}


	public double getValue() {
		
		return value;
	}


	public boolean isConfirmed() {
		return confirmed;
	}

}
