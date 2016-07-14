package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

import br.com.cardeal.filter.StockFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.table.TableProdModel;
import br.com.cardeal.table.TempTableEmptyModel;
import br.com.cardeal.model.StockTotal;
import javax.swing.JScrollPane;
import java.awt.Rectangle;
import javax.swing.JTable;

public class DailyProdReportPanel extends JPanel implements OperationWindow {

	private static final long serialVersionUID = 1L;
	private JTextField txtDay;
	private JTextField txtMonth;
	private JTextField txtYear;
	private JTextField txtDay_2;
	private JTextField txtMonth_2;
	private JTextField txtYear_2;
	private JPanel panel;
	private JTextField txtTerminalDe;
	private JTextField txtTerminalAte;
	private JButton btnTerminalDe;
	private JButton btnTerminalAte;
	private JTable table;

	public DailyProdReportPanel() {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		JLabel label = new JLabel("Data De");
		label.setFont(new Font("Tahoma", Font.BOLD, 22));
		label.setBounds(12, 22, 95, 29);
		add(label);
		
		txtDay = new JTextField();
		txtDay.setHorizontalAlignment(SwingConstants.CENTER);
		txtDay.setFont(new Font("Tahoma", Font.PLAIN, 24));
		txtDay.setColumns(10);
		txtDay.setBounds(119, 10, 48, 51);
		txtDay.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtDay,"getNewDate");				
			}
		});
		add(txtDay);
		
		JLabel label_2 = new JLabel("/");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 24));
		label_2.setBounds(172, 21, 19, 29);
		add(label_2);
		
		txtMonth = new JTextField();
		txtMonth.setHorizontalAlignment(SwingConstants.CENTER);
		txtMonth.setFont(new Font("Tahoma", Font.PLAIN, 24));
		txtMonth.setColumns(10);
		txtMonth.setBounds(190, 10, 48, 51);
		txtMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtDay,"getNewDate");
			}
		});
		add(txtMonth);
		
		JLabel label_3 = new JLabel("/");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 24));
		label_3.setBounds(242, 21, 19, 29);
		add(label_3);
		
		txtYear = new JTextField();
		txtYear.setHorizontalAlignment(SwingConstants.CENTER);
		txtYear.setFont(new Font("Tahoma", Font.PLAIN, 24));
		txtYear.setColumns(10);
		txtYear.setBounds(259, 10, 85, 51);
		txtYear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtDay,"getNewDate");
			}
		});
		add(txtYear);
		
		JLabel label_4 = new JLabel("Data Até");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 22));
		label_4.setBounds(12, 79, 110, 29);
		add(label_4);
		
		txtDay_2 = new JTextField();
		txtDay_2.setHorizontalAlignment(SwingConstants.CENTER);
		txtDay_2.setFont(new Font("Tahoma", Font.PLAIN, 24));
		txtDay_2.setColumns(10);
		txtDay_2.setBounds(119, 70, 48, 51);
		txtDay_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtDay_2,"getNewDate2");
			}
		});
		add(txtDay_2);
		
		JLabel label_5 = new JLabel("/");
		label_5.setFont(new Font("Tahoma", Font.BOLD, 24));
		label_5.setBounds(172, 82, 19, 29);
		add(label_5);
		
		txtMonth_2 = new JTextField();
		txtMonth_2.setHorizontalAlignment(SwingConstants.CENTER);
		txtMonth_2.setFont(new Font("Tahoma", Font.PLAIN, 24));
		txtMonth_2.setColumns(10);
		txtMonth_2.setBounds(190, 70, 48, 51);
		txtMonth_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtDay_2,"getNewDate2");
			}
		});
		add(txtMonth_2);
		
		JLabel label_6 = new JLabel("/");
		label_6.setFont(new Font("Tahoma", Font.BOLD, 24));
		label_6.setBounds(242, 82, 19, 29);
		add(label_6);
		
		txtYear_2 = new JTextField();
		txtYear_2.setHorizontalAlignment(SwingConstants.CENTER);
		txtYear_2.setFont(new Font("Tahoma", Font.PLAIN, 24));
		txtYear_2.setColumns(10);
		txtYear_2.setBounds(259, 70, 85, 51);
		txtYear_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtDay_2,"getNewDate2");
			}
		});
		add(txtYear_2);
		
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiGlobals.getMain().exitOperation();
			}
		});
		btnSair.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Exit.png")));
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnSair.setBounds(234, 153, 210, 63);
		add(btnSair);
		
		JButton btnSearch = new JButton("Pesquisar");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTotal();
			}
		});
		btnSearch.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Apply.png")));
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnSearch.setBounds(12, 153, 210, 63);
		add(btnSearch);

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 1018, 222);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblTerminal = new JLabel("Terminal De");
		lblTerminal.setBounds(540, 22, 150, 29);
		lblTerminal.setFont(new Font("Tahoma", Font.BOLD, 22));
		panel.add(lblTerminal);
		
		txtTerminalDe = new JTextField();
		txtTerminalDe.setEditable(false);
		txtTerminalDe.setBounds(700, 10, 150, 55);		
		txtTerminalDe.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtTerminalDe.setColumns(10);
		panel.add(txtTerminalDe);
		
		btnTerminalDe = new JButton("");
		btnTerminalDe.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Search.png")));
		btnTerminalDe.setBounds(850, 10, 78, 51);
		btnTerminalDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listTerminal(1);
			}
		});
		
		panel.add(btnTerminalDe);
		
		JLabel lblTerminalAte = new JLabel("Terminal Até");
		lblTerminalAte.setBounds(540, 79, 150, 29);
		lblTerminalAte.setFont(new Font("Tahoma", Font.BOLD, 22));
		panel.add(lblTerminalAte);
		
		txtTerminalAte = new JTextField();
		txtTerminalAte.setEditable(false);
		txtTerminalAte.setBounds(700, 70, 150, 55);
		txtTerminalAte.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtTerminalAte.setColumns(10);
		panel.add(txtTerminalAte);
		
		btnTerminalAte = new JButton("");
		btnTerminalAte.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Search.png")));
		btnTerminalAte.setBounds(850, 70, 78, 51);
		btnTerminalAte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listTerminal(2);
			}
		});
		panel.add(btnTerminalAte);
									
		//--------------------------------------
		//grid
		//--------------------------------------
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(new Rectangle(17, 69, 553, 72));
		scrollPane.setBounds(0, 225, 1018, 380);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(50, 0));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(50, 0));
		add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
	
	public void validAction(JTextComponent textComponent, String methodName){		
			GuiGlobals.isEnabledAction(textComponent,methodName,this);				
	}
	
	public void getNewDate() {
		DateDlg dlg = new DateDlg(txtDay.getText(), txtMonth.getText(), txtYear.getText(), "Data Fabricação");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {
			txtDay.setText(dlg.getDay());
			txtMonth.setText(dlg.getMonth());
			txtYear.setText(dlg.getYear());
		}
	}
	
	public void getNewDate2() {
		DateDlg dlg = new DateDlg(txtDay_2.getText(), txtMonth_2.getText(), txtYear_2.getText(), "Data Fabricação");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {
			txtDay_2.setText(dlg.getDay());
			txtMonth_2.setText(dlg.getMonth());
			txtYear_2.setText(dlg.getYear());
		}
	}

	public String getTitle() {
		return "Produção Diária";
	}

	public JPanel getPanel() {
		return this;
	}

	public String getInitialMessage() {
		return "";
	}
	
	public void start() {
		today();
		today_2();
	}
	
	private void today() {
	// Carrega com a data atual o campo data De.	
		txtDay.setText(DateTimeUtils.getCurrentDay());
		txtMonth.setText(DateTimeUtils.getCurrentMonth());
		txtYear.setText(DateTimeUtils.getCurrentYear());

	}
	
	private void today_2() {
	// Carrega com a data atual o campo data atï¿½.
	    txtDay_2.setText(DateTimeUtils.getCurrentDay());
	    txtMonth_2.setText(DateTimeUtils.getCurrentMonth());
	    txtYear_2.setText(DateTimeUtils.getCurrentYear());
	//
	}
	
	private void showTotal() {
		GuiGlobals.waitCursor();
		try {
			clearTable();
			List<StockTotal> total = searchProduction();
			fillTable(total);
		}
		finally {
			GuiGlobals.defaultCursor();
		}
	}
	
	private void clearTable() {
		TableModel tableModel = table.getModel();
		
		//Se havia um total sendo mostrado, libera a memória
		if(tableModel != null && tableModel instanceof TableProdModel) {
			((TableProdModel)tableModel).setTotal(null);
		}
		
		TempTableEmptyModel empty = new TempTableEmptyModel();
		table.setModel(empty);		
	}
	
	private void fillTable(List<StockTotal> total) {
		this.clearTable();

		TableProdModel tableModel = new TableProdModel(total);
		
		table.setModel(tableModel);
		tableModel.initializeLayout(table);
	}
	
	private List<StockTotal> searchProduction() {
		Date date1 = DateTimeUtils.buildInitDate(
				Integer.parseInt(txtDay.getText()),
				Integer.parseInt(txtMonth.getText()),
				Integer.parseInt(txtYear.getText())
				);
		
		Date date2 = DateTimeUtils.buildEndDate(
				Integer.parseInt(txtDay_2.getText()),
				Integer.parseInt(txtMonth_2.getText()),
				Integer.parseInt(txtYear_2.getText())
				);
		
		// Tratamento de filtro por terminal.
		String terminal = txtTerminalDe.getText();
		String terminal_2 = txtTerminalAte.getText();
		
		StockFilter filter = new StockFilter();
		filter.setBatch(null);
		filter.setEnterDateDe(date1);
		filter.setEnterDateAte(date2);
		filter.setOnlyStocked(true);
		filter.setPartnerId(0);
		filter.setProductId(0);
		filter.setCompanyIdDe(Setup.getCompany().getId());
		filter.setCompanyIdAte(Setup.getCompany().getId());
		filter.setTerminalId(terminal);
		filter.setTerminalId_2(terminal_2);
		
		GuiGlobals.refreshDaoFactory();
		List<StockTotal> total = GuiGlobals.getDaoFactory().getStockDao().totalize(filter);
		return total; 
	}
	
	private void listTerminal(int deAte) {
		
		JTextField txtField = (deAte == 1) ? txtTerminalDe : txtTerminalAte;
				
		GuiGlobals.waitCursor();
		TerminalDlg dlg = new TerminalDlg();
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);
		if(dlg.getSelectedTerminal() != null) {
			
			txtField.setText(String.valueOf(dlg.getSelectedTerminal()));

		}
	}
		
}
