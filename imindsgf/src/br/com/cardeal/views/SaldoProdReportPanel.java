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
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.StockTotal;
import br.com.cardeal.services.ShipmentService;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SaldoProdReportPanel extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private Product product;
	private ShipmentService shipmentService;
	private Object[][] data = null; 

	/**
	 * Create the dialog.
	 */
	public SaldoProdReportPanel( Product product, ShipmentService shipmentService ) {
		
		setTitle("Consulta de Saldo da Filial");
		
		if ( product != null ){
			
			this.product = product;
			this.shipmentService = shipmentService;
					
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
					JButton cancelButton = new JButton("Fechar");
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
			
			
			String[] columns = {"Embalagens", "Caixas", "Peças", "Peso"};
			if ( data == null ) data = getSaldo();		
			
			if ( data != null )
			{
				table = new JTable(data, columns);				
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setEnabled(false);
				table.setFont(new Font("Tahoma", Font.PLAIN, 24));
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				table.setFillsViewportHeight(true);
				table.setRowHeight(50);
				        
		        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(50, 50));
		        
				JLabel lblHeading = new JLabel("Produto: " + product.getDescription());
				lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,30));
				
				getContentPane().add(lblHeading,BorderLayout.PAGE_START);
				getContentPane().add(scrollPane,BorderLayout.CENTER);
			}
			
		}
		
	}
	
	private Object[][] getSaldo() {
		
		GuiGlobals.refreshDaoFactory();		
		int contador = 0;		
		
		List<StockTotal> totals = shipmentService.getSaldo(product);		
		
		if ( totals != null && totals.size() > 0 )
		{
			data = new Object[totals.size()][4];
			
			for( StockTotal total : totals ){
				
				data[contador][0] = total.getTotEmb();
				data[contador][1] = total.getSecondaryQty();
				data[contador][2] = total.getPrimaryQty();			
				data[contador][3] = NumberUtils.transform(NumberUtils.truncate(total.getNet(), 3), 14, 3, false, false);
				contador++;
				
			}
		}
				
		return data;
	}

	private void close() {
		this.setVisible(false);
	}
	
	public Object[][] getData() {
		return data;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

}