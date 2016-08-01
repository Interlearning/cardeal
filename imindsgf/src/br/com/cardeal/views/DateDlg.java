package br.com.cardeal.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;

public class DateDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean confirmed = false;
	private String strDay;
	private String strMonth;
	private String strYear;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtDay;
	private JTextField txtMonth;
	private JTextField txtYear;
	
	public DateDlg getOuter() {
		return DateDlg.this;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			String[] dateParts = DateTimeUtils.getCurrentDate().split("/");
			DateDlg dialog = new DateDlg(dateParts[0], dateParts[1], dateParts[2], "Data");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DateDlg(String day, String month, String year, String labelValue) {
		setModalityType(ModalityType.DOCUMENT_MODAL);
		
		setBounds(100, 100, 442, 415);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
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
				if(validateDate()) {
					strDay = txtDay.getText();
					strMonth = txtMonth.getText();
					strYear = txtYear.getText();
					confirmed = true;
					setVisible(false);
				}
			}
		});
		button_2.setIcon(new ImageIcon(DateDlg.class.getResource("/32x32/Apply.png")));
		button_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		button_2.setBounds(31, 264, 175, 74);
		contentPanel.add(button_2);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnCancelar.setIcon(new ImageIcon(DateDlg.class.getResource("/32x32/Cancel.png")));
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCancelar.setBounds(220, 264, 175, 74);
		contentPanel.add(btnCancelar);
		
		txtDay = new JTextField(day);
		txtDay.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				GuiGlobals.virtualKeyboard(getOuter(), txtDay, null, null);
			}
		});
		txtDay.setHorizontalAlignment(SwingConstants.CENTER);
		txtDay.setFont(new Font("Tahoma", Font.PLAIN, 34));
		txtDay.setColumns(10);
		txtDay.setBounds(70, 85, 68, 51);
		contentPanel.add(txtDay);
		
		txtMonth = new JTextField(month);
		txtMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				GuiGlobals.virtualKeyboard(getOuter(), txtMonth, null, null);
			}
		});
		txtMonth.setHorizontalAlignment(SwingConstants.CENTER);
		txtMonth.setFont(new Font("Tahoma", Font.PLAIN, 34));
		txtMonth.setColumns(10);
		txtMonth.setBounds(165, 87, 65, 51);
		contentPanel.add(txtMonth);
		
		JLabel label = new JLabel("/");
		label.setFont(new Font("Tahoma", Font.BOLD, 36));
		label.setBounds(141, 89, 29, 42);
		contentPanel.add(label);
		
		txtYear = new JTextField(year);
		txtYear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				GuiGlobals.virtualKeyboard(getOuter(), txtYear, null, null);
			}
		});
		txtYear.setHorizontalAlignment(SwingConstants.CENTER);
		txtYear.setFont(new Font("Tahoma", Font.PLAIN, 34));
		txtYear.setColumns(10);
		txtYear.setBounds(256, 85, 98, 51);
		contentPanel.add(txtYear);
		
		JLabel label_1 = new JLabel("/");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 36));
		label_1.setBounds(234, 90, 29, 42);
		contentPanel.add(label_1);
		
		setModal(true);  // WJSP 05/06/2015 --  Jdialog sempre em foco -> para não permiir abrir mais de um por tela 
	}
	
	private void decrement() {
		decreaseDate();
	}

	private void increment() {
		increaseDate();
	}
	
	private void increaseDate() {
		String previous = txtYear.getText() + txtMonth.getText() + txtDay.getText();
		String result = DateTimeUtils.addDays(previous, 1);
		if(result.equals(previous)) {
			GuiGlobals.getMain().showMessage("A data informada é inválida");
			return;
		}
		txtDay.setText(result.substring(6));
		txtMonth.setText(result.substring(4, 6));
		txtYear.setText(result.substring(0, 4));		
	}
	
	private void decreaseDate() {
		String previous = txtYear.getText() + txtMonth.getText() + txtDay.getText();
		String result = DateTimeUtils.addDays(previous, -1);
		if(result.equals(previous)) {
			GuiGlobals.getMain().showMessage("A data informada é inválida");
			return;
		}
		txtDay.setText(result.substring(6));
		txtMonth.setText(result.substring(4, 6));
		txtYear.setText(result.substring(0, 4));		
	}
		
	
	private boolean validateDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateInString = txtDay.getText() + "-" + txtMonth.getText() + "-" + txtYear.getText();
	 
		try {	 
			formatter.parse(dateInString);
			return true;
		} 
		catch (ParseException e) {
			GuiGlobals.getMain().showMessage("Data inválida");
			return false;
		}	
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public String getDay() {
		return strDay;
	}

	public String getMonth() {
		return strMonth;
	}

	public String getYear() {
		return strYear;
	}	
}
