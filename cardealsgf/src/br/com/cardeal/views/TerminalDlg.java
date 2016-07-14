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

import br.com.cardeal.dao.TerminalDao;
import br.com.cardeal.filter.TerminalFilter;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Terminal;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TerminalDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private List<Terminal> terminal = null;
	private Terminal selectedTerminal = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TerminalDlg dialog = new TerminalDlg();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TerminalDlg() {
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
		
		
		String[] columns = {"Id", "Descrição"};
		Object[][] data = getTerminal();

		/*
		Object[][] data = {
				{"1000", "MORTADELA", "PESO PADRÃO"},
				{"2000", "LINGUIÇA", "PESO VARIÁVEL"},
				{"3000", "BELISKO", "PESO PADRÃO"},
		};
		*/
		
		table = new JTable(data, columns);
		table.setRowSelectionAllowed(false);
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
		
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(0).setWidth(40);
        
		table.getColumnModel().getColumn(1).setPreferredWidth(350);
        table.getColumnModel().getColumn(1).setWidth(350);
        
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(50, 50));
        
		JLabel lblHeading = new JLabel("Terminais");
		lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,30));
		
		getContentPane().add(lblHeading,BorderLayout.PAGE_START);
		getContentPane().add(scrollPane,BorderLayout.CENTER);		
	}
	
	private Object[][] getTerminal() {
		
		TerminalDao dao = GuiGlobals.getDaoFactory().getTerminalDao();
		TerminalFilter filter = new TerminalFilter();
		
		filter.setCompany(Setup.getCompany());
		terminal = dao.list( filter );
				
		Object[][] data = new Object[terminal.size()][3];
		for(int i = 0; i < terminal.size(); i++) {
			Terminal p = terminal.get(i);
			data[i][0] = p.getIdTerminal();
			data[i][1] = p.getDescription();
		}
		
		return data;
	}
	
	public Terminal getSelectedTerminal() {
		return selectedTerminal;
	}

	private void close() {
		this.setVisible(false);
	}
	
	private void confirm() {
		if(terminal != null && table.getSelectedRow() >= 0) {
			selectedTerminal = terminal.get(table.getSelectedRow());
			this.setVisible(false);
		}
	}

}
