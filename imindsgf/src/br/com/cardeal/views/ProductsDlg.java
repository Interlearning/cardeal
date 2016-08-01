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
import br.com.cardeal.dao.ProductDao;
import br.com.cardeal.filter.ProductFilter;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.Product;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductsDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;	
	private List<Product> products = null;
	private Product selectedProduct = null;

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		try {
			ProductsDlg dialog = new ProductsDlg();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} */

	/**
	 * Create the dialog.
	 */
	
	public ProductsDlg( List<Product> products ){
		this.products = products;		
		initDialog( null );
	}
		
	public ProductsDlg(ProductFilter filterWindow ) {				
		initDialog( filterWindow );
	}
	
	public ProductsDlg() {
		initDialog( null );
	}
	
	void initDialog( ProductFilter filterWindow ){
		
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
		
		
		String[] columns = {GuiGlobals.getBundle().getString("STR000020"), "Nome", "Tipo Pesagem"};
		Object[][] data = getProducts(filterWindow);
		
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
		
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(0).setWidth(40);
        
		table.getColumnModel().getColumn(1).setPreferredWidth(350);
        table.getColumnModel().getColumn(1).setWidth(350);
        
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(50, 50));
        
		JLabel lblHeading = new JLabel("Produtos");
		lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,30));
		
		//getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(lblHeading,BorderLayout.PAGE_START);
		getContentPane().add(scrollPane,BorderLayout.CENTER);	
		
	}
	
	private Object[][] getProducts(ProductFilter filterWindow) {

		Object[][] data = null;
				
		if ( products == null ){
		
		
			ProductDao dao = GuiGlobals.getDaoFactory().getProductDao();
			
			ProductFilter filter;
			/* WJSP em 28/05/2015
			 * Alterado instancia de ProductFilter para receber filterWindow
			 * Por padr�o objeto ProductFilter � criado com os valores
			 * 
			 * enabledToShowBlocked	:	true	
			 * materialStyle	   	:   0 (todos)
			 * packStyle			:	0 (todos)
			 * stockStyle			: 	0 (todos)
			 */		
					
			if(filterWindow == null){
				filter = new ProductFilter();
			}
			else{
				filter = filterWindow;
			}
			
			filter.setEnabledToShowBlocked(false);
			products = dao.list(filter);
			
		}
		
		data = setGridDados();
		
		return data;
	}
	
	private Object[][] setGridDados(){
		
		Object[][] data;
		
		if ( products != null && products.size() > 0) {
		
			data = new Object[products.size()][3];
			for(int i = 0; i < products.size(); i++) {
				Product p = products.get(i);
				data[i][0] = p.getIdMasc();
				data[i][1] = p.getDescription();
				data[i][2] = p.getWeighingStyle().getName();
			}
			
		}
		else{
			data = new Object[1][3];
			data[0][0] = "";
			data[0][1] = "";
			data[0][2] = "";
		}
		
		return data;
		
	}
	
	public Product getSelectedProduct() {
		return selectedProduct;
	}

	private void close() {
		this.setVisible(false);
	}
	
	private void confirm() {
		if(products != null && table.getSelectedRow() >= 0) {
			selectedProduct = products.get(table.getSelectedRow());
			GuiGlobals.getDaoFactory().getProductDao().refresh(selectedProduct);
			this.setVisible(false);
		}
	}	

}
