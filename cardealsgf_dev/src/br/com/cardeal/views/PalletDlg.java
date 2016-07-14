package br.com.cardeal.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import br.com.cardeal.dao.PalletDao;
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.filter.PalletFilter;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.Pallet;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PalletDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PalletDlg dialog = new PalletDlg();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PalletDlg() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(10, 10, 900, 700);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						confirm();
					}
				});
				okButton.setFont(new Font("Tahoma", Font.BOLD, 24));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						close();
					}
				});
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 24));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		
		String[] columns = {"Número do pallet", "Código ", "Descrição"};
		Object[][] data = getPallet();

		/*
		Object[][] data = {
				{"1000", "MORTADELA", "PESO PADRÃO"},
				{"2000", "LINGUIÇA", "PESO VARIÁVEL"},
				{"3000", "BELISKO", "PESO PADRÃO"},
		};
		*/
		
		table = new JTable(data, columns);		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				confirm();
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.PLAIN, 24));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		table.setFillsViewportHeight(true);
		table.setRowHeight(50);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(0).setWidth(20);
        
		table.getColumnModel().getColumn(1).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setWidth(40);
        
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(50, 50));
        
		JLabel lblHeading = new JLabel("Pallet");
		lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,30));
		
		//getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(lblHeading,BorderLayout.PAGE_START);
		getContentPane().add(scrollPane,BorderLayout.CENTER);		
	}
	
	private Object[][] getPallet() {
		PalletDao dao = GuiGlobals.getDaoFactory().getPalletDao();
		PalletFilter filter = new PalletFilter();
		filter.setStatus( PalletStatus.CLOSED );
		filter.setEnabledToShowBlocked(false);
		Pallet = dao.list(filter);
				
		Object[][] data = new Object[Pallet.size()][3];
		for(int i = 0; i < Pallet.size(); i++) {
			Pallet p = Pallet.get(i);
			data[i][0] = p.getId();
			data[i][1] = p.getProduct().getId();
			data[i][2] = p.getProduct().getDescription();
		}
		
		return data;
	}

	private List<Pallet> Pallet = null;
	private Pallet selectedPallet = null;
	
	public Pallet getSelectedPallet() {
		return selectedPallet;
	}

	private void close() {
		this.setVisible(false);
	}
	
	private void confirm() {
		if(Pallet != null && table.getSelectedRow() >= 0) {
			selectedPallet = Pallet.get(table.getSelectedRow());
			this.setVisible(false);
		}
	}

}
