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

import br.com.cardeal.dao.PartnerDao;
import br.com.cardeal.enums.PartnerStyle;
import br.com.cardeal.filter.PartnerFilter;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.Partner;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PartnerDlg extends JDialog 
{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private PartnerFilter filter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		try {
			PartnerDlg dialog = new PartnerDlg();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PartnerDlg()
	{
		createDlg(null);
	}
	public PartnerDlg( PartnerFilter filter ) 
	{
		createDlg(filter);
	}
	
	private void createDlg( PartnerFilter filter )
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(10, 10, 900, 700);
		getContentPane().setLayout(new BorderLayout());
		
		this.filter = filter;
		
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
		
		
		String[] columns = {"Cód. Interno", "Cód. Externo", "Nome", "Cnpj"};
		Object[][] data = getFornecedor();

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
		
		table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(0).setWidth(5);
        
        table.getColumnModel().getColumn(1).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setWidth(10);
        
		table.getColumnModel().getColumn(2).setPreferredWidth(40);
        table.getColumnModel().getColumn(2).setWidth(40);
        
        table.getColumnModel().getColumn(3).setPreferredWidth(15);
        table.getColumnModel().getColumn(3).setWidth(15);
        
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(50, 50));
        
		JLabel lblHeading = new JLabel("Fornecedor");
		lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,30));
		
		getContentPane().add(lblHeading,BorderLayout.PAGE_START);
		getContentPane().add(scrollPane,BorderLayout.CENTER);		
	}
	
	private Object[][] getFornecedor() 
	{
		PartnerDao dao = GuiGlobals.getDaoFactory().getPartnerDao();
		
		if ( filter == null )
		{
			filter = new PartnerFilter();
			filter.setAllPartners(true);
			filter.setPartnerStyle(PartnerStyle.UNDEFINED);
			filter.setEnabledToShowBlocked(false);
		}		
		
		Partner = dao.list(filter);
				
		Object[][] data = new Object[Partner.size()][4];
		for(int i = 0; i < Partner.size(); i++) {
			Partner p = Partner.get(i);
			data[i][0] = p.getId();
			data[i][1] = p.getCodigoExterno();
			data[i][2] = p.getName();
			data[i][3] = p.getCnpj();
		}
		
		return data;
	}

	private List<Partner> Partner = null;
	private Partner selectedFornecedor = null;
	
	public Partner getSelectedFornecedor() {
		return selectedFornecedor;
	}

	private void close() {
		this.setVisible(false);
	}
	
	private void confirm() {
		if(Partner != null && table.getSelectedRow() >= 0) {
			selectedFornecedor = Partner.get(table.getSelectedRow());
			this.setVisible(false);
		}
	}

}
