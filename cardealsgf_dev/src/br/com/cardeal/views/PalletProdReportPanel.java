package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;
import br.com.cardeal.filter.StockFilter;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.table.TableProdModel;
import br.com.cardeal.table.TableStockModel;
import br.com.cardeal.table.TempTableEmptyModel;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Stock;
import javax.swing.JScrollPane;
import java.awt.Rectangle;
import javax.swing.JTable;

public class PalletProdReportPanel extends JPanel implements OperationWindow {

	private static final long serialVersionUID = 1L;	
	private JPanel panel;
	private JTextField txtPalletId;	
	private JTable table;

	public PalletProdReportPanel() {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);	
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 1018, 80);
		add(panel);
		panel.setLayout(null);
				
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiGlobals.getMain().exitOperation();
			}
		});
		btnSair.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Exit.png")));
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnSair.setBounds(570, 10, 210, 63);
		panel.add(btnSair);
		
		JButton btnSearch = new JButton("Pesquisar");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTotal();
			}
		});
		btnSearch.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Apply.png")));
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnSearch.setBounds(350, 10, 210, 63);
		panel.add(btnSearch);
		
		JLabel lblPalletId = new JLabel("<html>Nº do Palete<br> + Filial</html>");
		lblPalletId.setBounds(12, 22, 150, 29);
		lblPalletId.setFont(new Font("Tahoma", Font.BOLD, 22));
		panel.add(lblPalletId);
		
		txtPalletId = new JTextField();		
		txtPalletId.setBounds(170, 10, 170, 55);		
		txtPalletId.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtPalletId.setColumns(10);
		txtPalletId.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				productMousePressed();
			}
		});
		panel.add(txtPalletId);
									
		//--------------------------------------
		//grid
		//--------------------------------------
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(new Rectangle(17, 69, 553, 72));
		scrollPane.setBounds(0, 100, 1018, 500);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(50, 0));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(50, 0));
		add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
	
	public void productMousePressed() {
		GuiGlobals.virtualKeyboard(null, txtPalletId, null, null);		
	}

	public String getTitle() {
		return "Produção de Paletes";
	}

	public JPanel getPanel() {
		return this;
	}

	public String getInitialMessage() {
		return "";
	}
	
	public void start() {		
	}		
	
	private void showTotal() 
	{
		GuiGlobals.waitCursor();
		try 
		{
			clearTable();
			List<Stock> stocks = searchProduction();
			
			if ( stocks != null && stocks.size() > 0 )
			{
				GuiGlobals.showMessage("", false);
				fillTable(stocks);
			}
			
		}
		finally 
		{
			GuiGlobals.defaultCursor();
		}
	}
	
	private void clearTable() {
		TableModel tableModel = table.getModel();
		
		//Se havia um total sendo mostrado, libera a memória
		if(tableModel != null && tableModel instanceof TableProdModel) {
			((TableProdModel)tableModel).setTotal(null);
		}
		
		//Associa um modelo vazio ï¿½ tabela
		TempTableEmptyModel empty = new TempTableEmptyModel();
		table.setModel(empty);		
	}
	
	private void fillTable(List<Stock> stocks) {
		//limpa dados anteriores presentes na tabela
		this.clearTable();

		//Cria um modelo de tabela prï¿½prio
		TableStockModel tableModel = new TableStockModel(stocks);
		
		//associa o modelo ï¿½ tabela
		table.setModel(tableModel);
		tableModel.initializeLayout(table);
	}
	
	private List<Stock> searchProduction() 
	{
		String codbarScanner = txtPalletId.getText();
		
		if ( codbarScanner.isEmpty() || codbarScanner.length() < 3 )
		{
			GuiGlobals.showMessage("id digitado inválido!");
			return null;
		}
		
		int palletId = Integer.parseInt(codbarScanner.substring(0, codbarScanner.length()-2));
		String filial = codbarScanner.substring(codbarScanner.length()-2, codbarScanner.length());

		Pallet pallet = GuiGlobals.getDaoFactory().getStockDao().findPalletStocked(palletId);
		
		if ( pallet == null || pallet.getId() == 0 )
		{
			GuiGlobals.showMessage("Palete ".concat(txtPalletId.getText().trim()).concat(" não encontrado!"));
			return null;
		}
		// Antonio pediu para retirar esta validacao do sistema
//		if ( !Setup.getCompany().getId().equals(filial))
//		{
//			GuiGlobals.showMessage("Etiqueta não pertence a filial!");
//			return null;
//		}
		
		if ( pallet.getStocks().size() == 0 )
		{
			TelaPiscante tela = new TelaPiscante("O Palete [" + pallet.getIdFormatted() + "] não tem caixas em estoque. Foram excluídas ou já foram expedidas!");
			tela.setVisible(true);
			return null;
		}
		
		StockFilter filter = new StockFilter();				
		filter.setOnlyStocked(true);		
		filter.setTerminalId(Setup.getTerminal().getIdTerminal());
		filter.setTerminalId_2(Setup.getTerminal().getIdTerminal());
		filter.setPalletIdDe( palletId );
		filter.setPalletIdAte( palletId );
		filter.setAsc(true);
		
		GuiGlobals.refreshDaoFactory();
		List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().list(filter);
		return stocks; 
	}
		
}
