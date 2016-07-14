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
import br.com.cardeal.globals.GuiGlobals;

public class QuantityDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int min;
	private int max;
	private int value;
	private boolean confirmed = false;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtValue;
	
	public QuantityDlg getOuter() {
		return QuantityDlg.this;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			QuantityDlg dialog = new QuantityDlg(0, 99999, 0, "Quantidade");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public QuantityDlg(int min, int max, int value, String labelValue) {
		setModalityType(ModalityType.DOCUMENT_MODAL);
		this.min = min;
		this.max = max;
		this.value = value;
		
		setBounds(500, 100, 442, 415);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtValue = new JTextField();
		txtValue.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mousePressedTxtValue();		
			}

		});
		txtValue.setText(String.valueOf(value));
		txtValue.setHorizontalAlignment(SwingConstants.CENTER);
		txtValue.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtValue.setColumns(10);
		txtValue.setBounds(70, 71, 285, 65);
		txtValue.selectAll();
		contentPanel.add(txtValue);
		
		JButton button = new JButton("+");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				increment();
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 58));
		button.setBounds(256, 149, 98, 74);
		contentPanel.add(button);
		
		JButton button_1 = new JButton("-");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decrement();
			}
		});
		button_1.setFont(new Font("Tahoma", Font.BOLD, 58));
		button_1.setBounds(70, 149, 99, 74);
		contentPanel.add(button_1);
		
		JLabel lblQuantidade = new JLabel(labelValue);
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
		button_2.setIcon(new ImageIcon(QuantityDlg.class.getResource("/32x32/Apply.png")));
		button_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		button_2.setBounds(31, 264, 175, 74);
		contentPanel.add(button_2);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnCancelar.setIcon(new ImageIcon(QuantityDlg.class.getResource("/32x32/Cancel.png")));
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCancelar.setBounds(220, 264, 175, 74);
		contentPanel.add(btnCancelar);
		
		setModal(true); 
	}
	
	public void mousePressedTxtValue() {
		GuiGlobals.virtualKeyboard(getOuter(), txtValue, "validateValue", this);
	}
	
	private void decrement() {
		if(value > min) {
			value--;
			txtValue.setText(String.valueOf(value));
		}
	}

	private void increment() {
		if(value < max) {
			value++;
			txtValue.setText(String.valueOf(value));
		}
	}
		
	public void validateValue() {
		try {
			int newVal = Integer.parseInt(txtValue.getText());
			if(newVal < min || newVal > max)
				txtValue.setText(String.valueOf(value));
			else
				value = newVal;
		}
		catch(Exception e) {
			txtValue.setText(String.valueOf(value));
		}
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isConfirmed() {
		return confirmed;
	}	
}
