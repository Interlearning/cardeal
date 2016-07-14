package br.com.cardeal.views;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.function.Predicate;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.lang.StringUtils;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.OrderStatus;
import br.com.cardeal.enums.ShipmentOpcaoMenu;
import br.com.cardeal.enums.ShipmentTypeOperation;
import br.com.cardeal.enums.StockStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.globals.ShipmentData;
import br.com.cardeal.globals.ShipmentProcessWorker;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.ItensShipment;
import br.com.cardeal.model.OrderItem;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Stock;
import br.com.cardeal.model.Unit;
import br.com.cardeal.services.ShipmentService;
import br.com.cardeal.table.TableItensShipmentModel;

public class ShipmentPanel extends JPanel  implements OperationWindow, PropertyChangeListener
{
	private static final long serialVersionUID = 1L;
	public final int ACTION_UPDATE_GRID_ON_LABEL_BOX = 0;
	public final int ACTION_UPDATE_GRID_ON_PRODUCT = 1;
	public final int ACTION_UPDATE_GRID_ON_LABEL_PALLET = 2;
	public final int ACTION_UPDATE_GRID_ON_PRODUCT_FIFO = 3;
	public final int ACTION_UPDATE_GRID_ON_LABEL_BOX_SSCC = 4;
	public final int ACTION_UPDATE_GRID_ON_LABEL_BOX_FROM_FIFO = 5;
	public final int ACTION_UPDATE_GRID_FROM_SSCC_OF_LABEL_BOX = 6;
	public final int ACTION_UPDATE_GRID_FROM_SSCC_OF_LABEL_PALLET = 7;
	private int CURRENT_ACTION;
	private JLabel lblPedido;
	private JLabel lblCliente;
	private JButton btnEnter1;
	private JTextField txtOrderId;	
	private JTextField txtScanner;
	private JButton btnSearch;
	private JButton btnSair;
	private JButton btnLedScanner;
	private JButton btnConsultaSaldo;
	private JButton btnEstornaItem;
	private JButton btnComplementoItem;
	private JButton btnFecharPedido;
	private JButton btnFIFO;
	private JButton btnMateriaPrima;
	private JButton btnEtiqueta;	
	private JPanel panel;	
	private JPanel panelMsgCodeBar;
	private JLabel lblMsgCodebar;
	private JScrollPane scrollPane;	
	private JTable table;
	private TableItensShipmentModel tableModelItens;
	private List<ItensShipment> itensGranel = null;
	private List<ItensShipment> itensMP = null;
	private String codbarScanner = "";		
	private ShipmentService shipmentService;
	private Renderizador renderizador;	
	private int userHasPermission = 0;
	private boolean isCanExecOperation = true;
	
	private ShipmentProcessWorker processWorker;
	private ProgressMonitor progressMonitor;
	private JButton startButton;
	private JTextArea taskOutput;

	public ShipmentPanel() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 1022, 68);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(panel);
		panel.setLayout(null);
		
		lblPedido = new JLabel("Pedido");
		lblPedido.setBounds(17, 18, 96, 29);
		panel.add(lblPedido);
		lblPedido.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
		lblCliente = new JLabel("Cliente: ");
		lblCliente.setBounds(330, 18, 620, 29);
		panel.add(lblCliente);
		lblCliente.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
		btnEnter1 = new JButton("ENTER");
        btnEnter1.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				searchOrder();
			}
		});
		txtOrderId = new JTextField();
		txtOrderId.setBounds(110, 5, 132, 51);
		txtOrderId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearStuff();
			}
		});
		txtOrderId.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {				
				GuiGlobals.virtualKeyboardAction(null, txtOrderId, null, null, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter1, null);
			}
		});		
		txtOrderId.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtOrderId.setColumns(10);
		panel.add(txtOrderId);
		
		txtScanner= new JTextField();
		txtScanner.setBounds(1110, 5, 132, 51);		
		txtScanner.addKeyListener(new KeyAdapter() {			
			
			@SuppressWarnings("static-access")
			public void keyReleased(KeyEvent e)
			{  
				if ( isCanExecOperation )
				{
					int key = e.getKeyCode();
		            String keyText;
		            
					if ( key == KeyEvent.VK_ENTER ) 
					{
		            
						if ( btnLedScanner.getBackground() == Color.GREEN ) 
						{
						
							codbarScanner = codbarScanner.replaceAll("\\D", ""); //Troca tudo que não for dígito por vazio
							lblMsgCodebar.setText( codbarScanner );
							
							if ( codbarScanner.length() >= 14 )
							{
								actionShipmentUpdateGrid( ACTION_UPDATE_GRID_ON_LABEL_BOX_SSCC, null, ShipmentTypeOperation.LEITOR );
							}
							else if ( codbarScanner.length() == 10 ) 
							{
								actionShipmentUpdateGrid( ACTION_UPDATE_GRID_ON_LABEL_BOX, null, ShipmentTypeOperation.LEITOR );
							}
							else if ( codbarScanner.length() == 8 )
							{
								actionShipmentUpdateGrid( ACTION_UPDATE_GRID_ON_LABEL_PALLET, ShipmentTypeOperation.LEITOR );
							}
							else
							{
								GuiGlobals.getMain().showMessage("<html><center>Tamanho do código de etiqueta inválido!<br>" + codbarScanner.trim() + "</center></html>");
							}
		            		
		            		codbarScanner = "";	            		
						}					
					
					}
					else{
						keyText = e.getKeyText(key);
			            codbarScanner += keyText;
					}
				}
			}
			
		 });
		panel.add(txtScanner);
				
		btnSearch = new JButton("");
		btnSearch.setIcon(new ImageIcon(ShipmentPanel.class.getResource("/32x32/Search.png")));
		btnSearch.setBounds(247, 5, 78, 51);
		panel.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiGlobals.showMessage("", false);
				searchOrder();				
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 24));
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(1, 68, 1022, 412);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(50, 0));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(50, 0));
		add(scrollPane);
		
		table = new JTable();
		table.addMouseListener( new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
								
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				setLedScannerEnableBox();
			}
		} );
		
		/**
		 * Listener para posicionar o scroll conforme linha posicionada
		 */
		table.getSelectionModel().addListSelectionListener(  
			    new ListSelectionListener(){  
			        public void valueChanged(ListSelectionEvent e){  
			            JScrollBar vertBart= scrollPane.getVerticalScrollBar();  
			            vertBart.setValue(table.getRowHeight()*table.getSelectedRow());  
			        }  
			    }  
			);  
		
		scrollPane.setViewportView(table);
		clearTable();
		
		btnLedScanner = new JButton("SCANNER");
		btnLedScanner.setBounds(5, 480, 108, 94);		
		btnLedScanner.setBackground(Color.RED);
		btnLedScanner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if ( btnLedScanner.getBackground() == Color.RED ){
					setLedScannerEnableBox();					
				}
				else
				{				
					setLedScannerDisableBox();				
//					getMenuForManualShipment(null);					
				}
				
			}

		});
		add(btnLedScanner);
		
		btnFecharPedido = new JButton("<html>Fechar <br> Pedido</html>");
		btnFecharPedido.setBounds(119, 480, 108, 94);
		btnFecharPedido.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				GuiGlobals.showMessage("", false);
				if ( tableModelItens.getdadosDaGrid().size() > 0 )					
				{
					if( closeOrder() ) 
					{
						shipmentService.clearUserInProcess();
						restartDlg();
					}
				}
				setLedScannerEnableBox();
			}
			
		});
		btnFecharPedido.setFont(new Font("Tahoma", Font.BOLD, 20));
		add(btnFecharPedido);
		
		btnFIFO = new JButton("<html><center>FIFO <br> Manual</center></html>");
		btnFIFO.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				if ( isItensOk() )
				{
					getMenuForManualShipment( ShipmentOpcaoMenu.FIFO );
				}
				setLedScannerEnableBox();
			}
		});
		btnFIFO.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnFIFO.setBounds(233, 480, 108, 94);		
		add(btnFIFO);
		
		btnMateriaPrima = new JButton("<html><center>Matéria <br> Prima</center></html>");
		btnMateriaPrima.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				if ( GuiGlobals.isPermitted(ComponentPermission.EXPEDICAO_MATERIA_PRIMA) )
				{
					if ( isItensOk() )
					{
						getMenuForManualShipment( ShipmentOpcaoMenu.MATERIA_PRIMA );
					}
				}
				else
				{
					GuiGlobals.showMessage( GuiGlobals.getBundle().getString("user_not_permission") );
				}
				setLedScannerEnableBox();
				
			}
		});
		btnMateriaPrima.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnMateriaPrima.setBounds(347, 480, 108, 94);		
		add(btnMateriaPrima);
		
		btnEtiqueta = new JButton("<html><center>Etiqueta <br> Manual</center></html>");
		btnEtiqueta.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				if ( isItensOk() )
				{
					getMenuForManualShipment( ShipmentOpcaoMenu.ETIQUETA );
				}
				setLedScannerEnableBox();
			}
		});
		btnEtiqueta.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnEtiqueta.setBounds(461, 480, 108, 94);		
		add(btnEtiqueta);
		
		btnComplementoItem = new JButton("<html>Comple <br> mento</html>");
		btnComplementoItem.setBounds(575, 480, 108, 94);
		btnComplementoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);				
				if ( GuiGlobals.isPermitted( ComponentPermission.EXPEDICAO_COMPLEMENTAR_ITENS ) ) { // Verifica permissao para complementar itens
					if ( isItensOk() )
					{
						complementProduct();
					}
				}
				else{
					GuiGlobals.showMessage(GuiGlobals.getBundle().getString("user_not_permission"));
				}
				setLedScannerEnableBox();
				
			}
		});
		btnComplementoItem.setFont(new Font("Tahoma", Font.BOLD, 20));
		add(btnComplementoItem);
		
		btnConsultaSaldo = new JButton("Saldo");
		btnConsultaSaldo.setBounds(689, 480, 108, 94);
		btnConsultaSaldo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				if ( isItensOk() )
				{
				
					SaldoProdReportPanel dlg = new SaldoProdReportPanel( getProductSelected( null ), shipmentService );
					GuiGlobals.defaultCursor();
					if ( dlg.getData() != null && dlg.getData().length > 0 ){ 
						dlg.setVisible(true);
					}
					else
					{
						GuiGlobals.showMessage("Produto sem saldo!");
					}
				}				
				setLedScannerEnableBox();
				
			}

		});
		btnConsultaSaldo.setFont(new Font("Tahoma", Font.BOLD, 20));
		add(btnConsultaSaldo);
		
		btnEstornaItem = new JButton("<html><center>Estornar <br> Item</center></html>");
		btnEstornaItem.setBounds(803, 480, 108, 94);
		btnEstornaItem.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnEstornaItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				if ( isItensOk() )
				{
					if ( tableModelItens.getdadosDaGrid().get( table.getSelectedRow() ).getQtdExpedida() > 0 )
					{
						extornaItem(true);
					}
					else
					{
						GuiGlobals.showMessage("Não há itens para estornar!");
					}
				}
				setLedScannerEnableBox();
			}

		});
		add(btnEstornaItem);
		
		btnSair = new JButton("");
		btnSair.setBounds( 917, 480, 103, 94);
		btnSair.setIcon(new ImageIcon(ShipmentPanel.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ( shipmentService != null )
				{
					shipmentService.checkStatusOrderFromExit();
					shipmentService.clearUserInProcess();
				}
					GuiGlobals.getMain().exitOperation();
			}
		});
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 24));
		add(btnSair);
		
		panelMsgCodeBar = new JPanel();
		panelMsgCodeBar.setBorder(new LineBorder(new Color(192, 192, 192)));
		panelMsgCodeBar.setForeground(new Color(255, 255, 0));
		panelMsgCodeBar.setBackground(new Color(0, 0, 0));
		panelMsgCodeBar.setBounds(0, 575, 1022, 25);
		panelMsgCodeBar.setLayout(new CardLayout(0, 0));
		add(panelMsgCodeBar);
		
		lblMsgCodebar = new JLabel();
		lblMsgCodebar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMsgCodebar.setForeground(new Color(30, 144, 255));
		lblMsgCodebar.setHorizontalAlignment(SwingConstants.LEFT);
		panelMsgCodeBar.add(lblMsgCodebar);
		
		setLedScannerEnableBox();
		
	}
	
	private boolean isItensOk()
	{
		boolean isOk = false;
		if ( tableModelItens.getdadosDaGrid().size() > 0 )					
		{
			if ( table.getSelectedRow() >= 0 )
			{
				isOk = true;
			}
			else
			{
				GuiGlobals.showMessage("Não há item selecionado para esta operação!");
			}
		}
		else
		{
			GuiGlobals.showMessage("Não há itens!");
		}
		return isOk;
	}
	
	public Product getProductSelected( Product productRefresh ) 
	{		
		Product product;
		
		if ( productRefresh == null )
			product = tableModelItens.getdadosDaGrid().get( table.getSelectedRow() ).getProduct();
		else
			product = productRefresh;
		
		GuiGlobals.getDaoFactory().getProductDao().refresh(product);
		
		return product;
	}
	
	public void getMenuForManualShipment( ShipmentOpcaoMenu shipmentOpcao ) {
		
		GuiGlobals.showMessage("", false);
		
		if ( shipmentOpcao == null )
		{
			ShipmentMenuManualDlg shipmentMenuDlg = new ShipmentMenuManualDlg();
			GuiGlobals.defaultCursor();
			shipmentMenuDlg.setVisible(true);
			shipmentOpcao = shipmentMenuDlg.getOpcao();
		}
		
		if ( shipmentOpcao != ShipmentOpcaoMenu.CANCELAR ) 
		{
			ItensShipment itemSelected = null;
			
			if ( ( shipmentOpcao == ShipmentOpcaoMenu.FIFO || shipmentOpcao == ShipmentOpcaoMenu.MATERIA_PRIMA ) && table.getSelectedRow() >=0 )
			{
				itemSelected = tableModelItens.getdadosDaGrid().get( table.getSelectedRow() );
			}
			
			ShipmentManualDlg shipmentManualDlg = new ShipmentManualDlg(  shipmentOpcao, itemSelected, shipmentService, ShipmentPanel.this );
			GuiGlobals.defaultCursor();
			shipmentManualDlg.setVisible(true);
			
			if ( shipmentManualDlg.isConfirmed() ){
			
				String etiqueta = shipmentManualDlg.getTxtValueCodigo().getText().trim();
				long idDigitedByUser = Long.valueOf( etiqueta );
				
				if ( shipmentOpcao == ShipmentOpcaoMenu.ETIQUETA ) 
				{
					codbarScanner = etiqueta;
					lblMsgCodebar.setText( etiqueta );
					
					if ( ( shipmentManualDlg.isBoxSelected() || shipmentManualDlg.isPalletSelected() ) && codbarScanner.length() >= 14 ) // Se for etiqueta caixa ou palete do modelo pao de açucar
					{
						actionShipmentUpdateGrid( ACTION_UPDATE_GRID_ON_LABEL_BOX_SSCC, null, ShipmentTypeOperation.LEITOR );
					}
					else if ( shipmentManualDlg.isBoxSelected() && codbarScanner.length() == 10 )
					{
						actionShipmentUpdateGrid( ACTION_UPDATE_GRID_ON_LABEL_BOX, null, ShipmentTypeOperation.LEITOR );					
					}
					else if  ( shipmentManualDlg.isPalletSelected() && codbarScanner.length() == 8){
						actionShipmentUpdateGrid( ACTION_UPDATE_GRID_ON_LABEL_PALLET, ShipmentTypeOperation.PALLET );						
					}
					else
					{
						GuiGlobals.getMain().showMessage("<html><center>Tamanho do código de etiqueta inválido!<br>" + codbarScanner.trim() + "</center></html>");
					}
					
				}							
				else if ( shipmentOpcao == ShipmentOpcaoMenu.FIFO ) {
					
					String idMasc = StringUtils.leftPad( String.valueOf(idDigitedByUser) , 4, "0");

					if ( !idMasc.isEmpty() ) {
					
						Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( idMasc );
						getProductSelected(product);
														
						if ( product != null && product.isFifoEnabled() )
						{
							double qtdDigitada; 
							if ( shipmentService.isUnitForKg( itemSelected.getUnitId() ) )
							{
								qtdDigitada = Double.parseDouble( shipmentManualDlg.getTxtValueWeight().getText() );
							}
							else
							{
								qtdDigitada = Double.parseDouble( shipmentManualDlg.getTxtValueQuantidade().getText() ); 
							}
							
							actionShipmentUpdateGrid( ACTION_UPDATE_GRID_ON_PRODUCT_FIFO, product, qtdDigitada, itemSelected );
						}
						else{
							
							/**
							 * Tratativa de mensagem para usuario
							 */										
							if ( product == null ){
								GuiGlobals.showMessage("Produto " + idMasc + " não encontrado!");
							}
							
							if ( product != null && !product.isFifoEnabled() ){
								GuiGlobals.showMessage("Produto " + product.getIdMasc() + " não é FIFO!");
							}
							
						}
					}
					
				}
				else if ( shipmentOpcao == ShipmentOpcaoMenu.MATERIA_PRIMA ) {
					
					String idMasc = StringUtils.leftPad( String.valueOf(idDigitedByUser) , 4, "0");
					
					if ( !idMasc.isEmpty() ) 
					{
					
						Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( idMasc );
						getProductSelected(product);
						
						if ( product != null )
						{
						
							double netPicked = shipmentManualDlg.getNet();
							int qtyPicked = shipmentManualDlg.getQty();
							
							int item = shipmentService.getNextItem();
							Unit unitItemSelected = GuiGlobals.getDaoFactory().getUnitDao().find( itemSelected.getUnitId().substring(0, 2).toUpperCase() );
							
							if ( shipmentService.leaveStockMPfromOrder(product, netPicked, qtyPicked, item, unitItemSelected) )
							{
								
								ItensShipment itemRow = new ItensShipment();
								
								itemRow.setItem(item);
								itemRow.setProduct( product );
								itemRow.setQtdSolicitada( netPicked );
								itemRow.setUnitId( itemSelected.getUnitId() );
								itemRow.setQtdExpedida( netPicked );
								itemRow.setQtdFaltante( 0 );
								itemRow.setTotal(netPicked);
								itemRow.setTotalUnitId( product.getUnit().getId() );
								itemRow.setTypeStock(TypeStock.MATERIA_PRIMA);
								itemRow.setTypeOperation(ShipmentTypeOperation.MANUAL);
								
								/**
								 * Antonio pediu para nao acrescentar a linha na grid quando Granel, apenas acrescentar no produto, em 12-01-2016
								 */
//								addItenShipmentOnGrid( itemRow, true );
								
								if ( itensMP == null ) itensMP = new ArrayList<ItensShipment>();
								itensMP.add(itemRow);
								
								Stock stock = new Stock();								
								stock.setCompany(Setup.getCompany());
								stock.setTerminal(Setup.getTerminal());
								stock.setEnterDate( DateTimeUtils.now() );
								stock.setInFifo(false);
								stock.setManufactureDate( DateTimeUtils.now() );										
								stock.setNet(netPicked);
								stock.setNetEtq(netPicked);
								stock.setPrimaryQty(qtyPicked);
								stock.setSecondaryQty(0);
								stock.setProduct(product);
								stock.setStatus(StockStatus.STOCKED);
								stock.setTare(0);
								stock.setTypeStock(TypeStock.MATERIA_PRIMA);
								stock.setUser(GuiGlobals.getUserInfo().getUser());
								
								searchOnGridAndUpdate( stock, ShipmentTypeOperation.MANUAL, netPicked );
								renderizator();
							}

						}
						else{
							GuiGlobals.showMessage("Produto " + idMasc + " não encontrado!");
						}						
					}
				}
				
			}
			
		}
							
		setLedScannerEnableBox();	
		
	}
	
	@SuppressWarnings("unchecked")
	public void extornaItem( boolean isQuestion ) 
	{
		
		if ( table.getSelectedRow() >= 0 )
		{
			
			ItensShipment itemSelected = tableModelItens.getdadosDaGrid().get( table.getSelectedRow() );
			Product product = itemSelected.getProduct();
			getProductSelected(product);
			
			setLedScannerDisableBox();
			int dialogResult = ( ( isQuestion ) ? GuiGlobals.showMessageDlgYesNo("Deseja estornar a expedição do produto [" + product + "]") : JOptionPane.YES_OPTION );
			if(dialogResult == JOptionPane.YES_OPTION){
				
				if ( itensGranel != null )
				{
					int posProduct = itensGranel.indexOf( itemSelected );
					
					if ( posProduct > -1 )
					{
						@SuppressWarnings("rawtypes")
						CustomPredicate filter = new CustomPredicate<>();
						filter.varc1 = itemSelected;
						shipmentService.rollBackItemGranel( itemSelected );
						itensGranel.removeIf( filter );
					}
				}
				//- Necessario para funcionar a devolução
				if ( itensMP != null )
				{
					int posProduct = itensMP.indexOf( itemSelected );
					
					if ( posProduct > -1 )
					{
						@SuppressWarnings("rawtypes")
						CustomPredicate filter = new CustomPredicate<>();
						filter.varc1 = itemSelected;
						shipmentService.rollBackItemMP( itemSelected );
						itensMP.removeIf( filter );
					}
				}
				
				shipmentService.rollBackItem( itemSelected );
				/**
				 * Limpa item posicionado
				 */
				clearItemOrderSelected();
				
				refreshGrid();
				
			}			
			setLedScannerEnableBox();
		}
		else{
			GuiGlobals.showMessage("Selecione um produto para estornar!");
		}
		
	}
	
	@SuppressWarnings("hiding")
	private class CustomPredicate<ItensShipment> implements Predicate<ItensShipment> 
	{
		ItensShipment varc1;
		@Override
		public boolean test(ItensShipment varc) 
		{
			if ( varc1.equals(varc) )
			{
				return true;
			}
			return false;
		}
	    
	}	
	
	private void setLedScannerEnableBox(){
		
		if ( tableModelItens.getdadosDaGrid().size() > 0 ) {
		
			btnLedScanner.setBackground( Color.GREEN );
			txtScanner.setEnabled(true);
			txtScanner.requestFocus();
			
		}
		
	}
	
	private void setLedScannerDisableBox()
	{			
		btnLedScanner.setBackground( Color.RED );
		txtScanner.setEnabled(false);
	}
	
	public void actionShipmentUpdateGrid( int action, Object... params )
	{
		
		Product product = null;
		GuiGlobals.refreshDaoFactory();
		CURRENT_ACTION = action;
		userHasPermission = 0;
		GuiGlobals.showMessage("", false);
			 
		switch ( action ) 
		{
		
		case ACTION_UPDATE_GRID_ON_LABEL_BOX:
			
			updateProductOnGridOfLabelBox( (Stock) params[0], (ShipmentTypeOperation) params[1] );			
			break;
			
		/**
		 * Esta opcao é igual a opcao ACTION_UPDATE_GRID_ON_LABEL_BOX, apenas utilizo para identificar
		 * que a origem é processo FIFO
		 */
		case ACTION_UPDATE_GRID_ON_LABEL_BOX_FROM_FIFO:
			
			updateProductOnGridOfLabelBox( (Stock) params[0], (ShipmentTypeOperation) params[1] );			
			break;
			
		case ACTION_UPDATE_GRID_ON_LABEL_PALLET:
			
			updateProductOnGridOfLabelPallet( (ShipmentTypeOperation) params[0] );
			break;
			
		case ACTION_UPDATE_GRID_ON_LABEL_BOX_SSCC:
			
			updateProductOnGridOfLabelBoxSSCC( (Stock) params[0], (ShipmentTypeOperation) params[1] );			
			break;
			
		case ACTION_UPDATE_GRID_ON_PRODUCT:
			
			product = ( (Stock) params[0] ).getProduct();
			getProductSelected(product);
			int qtyPicked = (Integer) params[1];
			double netPicked = (Double) params[2];
						
			if ( validUpdateGrid(product, action) ){
				updateProductOnGridOfProduct( (Stock) params[0], qtyPicked, netPicked );
			}
			break;
			
		case ACTION_UPDATE_GRID_ON_PRODUCT_FIFO:
			
			product = (Product) params[0];
			if ( validUpdateGrid(product, action) ){
				updateProductFifoOnGrid((Product) params[0], (Double) params[1], (ItensShipment) params[2]);
			}
			break;
						
		default:
			break;
		}
		
		setLedScannerEnableBox();
	}
	
	public boolean validUpdateGrid( Product product, int action ){
		
		boolean validOk = true;
		
		/**
		 * Validacao de produto FIFO
		 */
		/*
		 * Antonio pediu para retirar esta validacao no dia 18/02/2016
		 * 
		if ( product.isFifoEnabled() )
		{
			
			if ( product.getPalletQty() > 0 && ( action != ACTION_UPDATE_GRID_ON_LABEL_PALLET || action != ACTION_UPDATE_GRID_ON_LABEL_BOX_SSCC ) )
			{				
				validOk = false;
				GuiGlobals.showMessageDlg( GuiGlobals.getBundle().getString("STR000028") );				
			}
			
		}
		*/
				
		return validOk;
		
	}
	
	public void updateProductFifoOnGrid(Product product, double qtdDigitada, ItensShipment itemSelected){
		
		if ( qtdDigitada > 0 )
		{
			ShipmentData shipmentData = new ShipmentData();
			shipmentData.setShipmentService(shipmentService);
			shipmentData.setItemSelected(itemSelected);
			shipmentData.setProduct(product);
			shipmentData.setQtdDigitada(qtdDigitada);
			shipmentData.setDlgShipment(ShipmentPanel.this);
			shipmentData.setAction(ShipmentData.ACTION_FIFO);
			userHasPermission = 0;
			execProcessWorker(shipmentData);
			
		}			
	}
	
	private void execProcessWorker(ShipmentData shipmentData) 
	{
		processWorker = new ShipmentProcessWorker(shipmentData);
		processWorker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
		    public void propertyChange(final PropertyChangeEvent event) 
			{
				
				switch (event.getPropertyName()) 
				{
		        case "progress":
		        	break;
		        	
		        case "state":
		        	
		        	switch ( (StateValue) event.getNewValue() ) 
		        	{
			        	case DONE:		        		
					        
			        		try 
			        		{
					        	final int count = processWorker.get();
					        	if ( count == ShipmentData.ACTION_LABEL_PALLET )
					        	{
					        		refreshGrid();
					        	}
					        	else if ( count == ShipmentData.ACTION_FIFO )
					        	{
					        		refreshGrid();
					        	}
					        	
					        } 
			        		catch (final CancellationException e) 
			        		{
					        	e.printStackTrace();
					        } 
			        		catch (final Exception e) 
			        		{
					        	e.printStackTrace();
					        }
			        		GuiGlobals.defaultCursor();
					        break;
					        
			        	case STARTED:
			        		break;
			        	case PENDING:		        		
			        		break;
		        	}
		        break;
		    }
		  }
		});
		processWorker.execute();
	}

	private void updateProductOnGridOfLabelPallet( ShipmentTypeOperation typeOperation )
	{
		ShipmentData shipmentData = new ShipmentData();
		shipmentData.setShipmentService(shipmentService);
		shipmentData.setShipmentTypeOperation(typeOperation);									
		shipmentData.setDlgShipment(ShipmentPanel.this);
		shipmentData.setCodBar( getCodbarScanner() );
		shipmentData.setAction(ShipmentData.ACTION_LABEL_PALLET);
		processWorker = null;
		processWorker = new ShipmentProcessWorker(shipmentData);
		userHasPermission = 0;
		execProcessWorker(shipmentData);
		
	}
	
	/**
	 * Metodo para atualizar grid de dados quando a origem da informação é um código de etiqueta de embalagem.
	 * 
	 * @param stockOut registro do estoque a considerar. Se este estiver nulo, o metodo busca o estoque pelo codigo de barras -> codbarScanner
	 */
	private void updateProductOnGridOfLabelBox( Stock stockOut, ShipmentTypeOperation typeOperation )
	{

		if ( codbarScanner != null && codbarScanner.length() == 10 ) 
		{
			long idEtiqueta = Long.parseLong( codbarScanner.substring(0, 8) );			
			Company company = shipmentService.getCompanyById( codbarScanner.substring(8, 10) );
			Stock stock = stockOut;
			boolean vldInitOk = true;
			
			if( company == null ) 
			{				
				GuiGlobals.getMain().showMessage("<html><center>Filial não encontrada!<br>" + codbarScanner.substring(8, 10).trim() + "</center></html>");
				vldInitOk = false;
			}
			// Antonio pediu para retirar esta validacao do sistema
//			else if ( !( company.getId().equals( Setup.getCompany().getId() ) ) )
//			{
//				GuiGlobals.getMain().showMessage("<html><center>Etiqueta não pertence a filial!<br>" + codbarScanner.trim() + "</center></html>");
//				vldInitOk = false;
//			}
			else if ( stock == null )
			{
				stock = shipmentService.getStockByIdAndCompany(idEtiqueta, company);
			}
			
			if ( vldInitOk )
			{
				if ( stock != null && stock.getId() > 0)
				{
					GuiGlobals.getDaoFactory().getStockDao().refresh(stock);
				}
				
				if ( stock == null || ( stock.getId() == 0 ) )
				{
					GuiGlobals.getMain().showMessage("<html><center>Etiqueta não encontrada!<br>" + codbarScanner.trim() + "</center></html>");
					TelaPiscante tela = new TelaPiscante("Etiqueta [" + codbarScanner.trim() + "] não encontrada!");
					tela.setVisible(true);
				}
				else if ( StockStatus.NON_STOCKED.equals( stock.getStatus() ) )
				{
					String msg;
					
					if ( stock.getOrder() != null )
					{
						msg = "Etiqueta [" + codbarScanner.trim() + "] já expedida no pedido [" + stock.getOrder().getIdPedidoImport() + "]!";
					}
					else
					{
						msg = "Etiqueta [" + codbarScanner.trim() + "] não consta mais no estoque!";
					}
					GuiGlobals.getMain().showMessage("<html><center>Etiqueta <br>" + codbarScanner.trim() + " não pode ser utilizada!</center></html>");
					TelaPiscante tela = new TelaPiscante(msg);
					tela.setVisible(true);
				}
				else if ( stock.isInFifo() )
				{
					
					if ( ( stock.getPallet() == null || stock.getPallet().getId() == 0 ) && CURRENT_ACTION != ACTION_UPDATE_GRID_ON_LABEL_BOX_FROM_FIFO)
					{
						GuiGlobals.showMessage("Produto FIFO sem palete somente FIFO Manual!");
						setHowOnFocusByProduct( stock.getProduct() );
					}
					else
					{
						updateProductOnGrid(typeOperation, stock);
					}
					
				}
				else
				{
					updateProductOnGrid(typeOperation, stock);
				}
			}
			
			codbarScanner = "";
		}	
				
	}

	private void updateProductOnGrid(ShipmentTypeOperation typeOperation, Stock stock) {
		
		Product product;
		if ( stock != null ){
			
			product = stock.getProduct();
			getProductSelected(product);
			if  ( validUpdateGrid(product, ACTION_UPDATE_GRID_ON_LABEL_BOX) ){
				
				if ( stock.getOrder() != null && stock.getOrder().getId() > 0 ){					
					TelaPiscante tela = new TelaPiscante("Etiqueta [" + codbarScanner + "] já expedida para o pedido [" + stock.getOrder().getIdPedidoImport() + "]");
					tela.setVisible(true);
				}
				else{				
					
					if ( searchOnGridAndUpdate( stock, typeOperation ) )
					{
						stock.setOrder( shipmentService.getOrder() );
						shipmentService.leaveStock( stock );
					}
					refreshGrid();						
				}
			}
			
		}
		else{				
			TelaPiscante tela = new TelaPiscante("Etiqueta [" + codbarScanner + "] não encontrada!");
			tela.setVisible(true);
		}
		
	}
	
	private void updateProductOnGridOfLabelBoxSSCC( Stock stockOut, ShipmentTypeOperation typeOperation )
	{
	
		if ( codbarScanner != null && codbarScanner.length() >= 14 ) 
		{
			CURRENT_ACTION = ACTION_UPDATE_GRID_FROM_SSCC_OF_LABEL_BOX;
			boolean existFilial = false;
			String codSscc = codbarScanner.substring(2, codbarScanner.length());
			Pallet pallet = GuiGlobals.getDaoFactory().getPalletDao().findBySSCC(codSscc);			
			Stock stock = GuiGlobals.getDaoFactory().getStockDao().findBySSCC(codSscc);
			boolean isShipment = (	stock != null
									&& stock.getId() > 0 
									&& stock.getOrder() != null
									&& stock.getOrder().getId() > 0
									&& shipmentService.getOrder() != null 
									&& stock.getStatus() == StockStatus.NON_STOCKED 
									&& stock.getOrder().getId() == shipmentService.getOrder().getId() );
			
			existFilial = !( stock == null || stock.getStatus() == StockStatus.NON_STOCKED  );
			
			if ( pallet != null && pallet.getId() > 0 && stock != null && stock.getId() > 0)
			{
				setLedScannerDisableBox();
				int opcao = GuiGlobals.showMessageDlgYesNo("O código de etiqueta é de palete ?");
				if(opcao == JOptionPane.YES_OPTION)
				{
					CURRENT_ACTION = ACTION_UPDATE_GRID_FROM_SSCC_OF_LABEL_PALLET;
					typeOperation = ShipmentTypeOperation.PALLET;
					setCodbarScanner( StringUtils.leftPad( String.valueOf( pallet.getId() ), 6, "0") + Setup.getCompany().getId().trim());
					updateProductOnGridOfLabelPallet( typeOperation );
				}
				else
				{
					updateProductOnGrid(typeOperation, stock);
				}
				setLedScannerEnableBox();
			}
			else if ( pallet != null && pallet.getId() > 0 )
			{
				setCodbarScanner( StringUtils.leftPad( String.valueOf( pallet.getId() ), 6, "0") + Setup.getCompany().getId().trim());
				updateProductOnGridOfLabelPallet( typeOperation );
			}
			else if( !existFilial || ( stock == null || ( stock.getId() == 0 ) || stock.getStatus() == StockStatus.NON_STOCKED ) ) 
			{
				
				String msgVld = "";
				if ( isShipment ) 
					msgVld = "Etiqueta já expedida para o pedido [" + stock.getOrder().getIdPedidoImport() + "]";
				else
					msgVld = "Etiqueta não encontrada!";
				
				GuiGlobals.getMain().showMessage("<html><center>" + msgVld + "<br>" + codSscc + "</center></html>");
				TelaPiscante tela = new TelaPiscante(msgVld + "\n [" + codSscc + "]");
				tela.setVisible(true);
			}			
			else
			{
				updateProductOnGrid(typeOperation, stock);
			}
			
			codbarScanner = "";
		}
		
	}
	
	private void updateProductOnGridOfProduct( Stock stock, int qtyPicked, double netPicked ){
		
		if ( stock != null && netPicked > 0 )
		{
			stock.setPrimaryQty( stock.getPrimaryQty() - qtyPicked );
			stock.setNet( stock.getNet() - netPicked );
			if ( searchOnGridAndUpdate( stock, ShipmentTypeOperation.MANUAL, 0 ) )
			{
				shipmentService.leaveStockMP( stock );
			}			
			refreshGrid();
			
		}
		
	}
	
	public boolean searchOnGridAndUpdate( Stock stock, ShipmentTypeOperation typeOperation )
	{
		return searchOnGridAndUpdate( stock, typeOperation, 0 );
	}
	
	public boolean searchOnGridAndUpdate( Stock stock, ShipmentTypeOperation typeOperation, double qtdDigitada )
	{		
		Product productFromItemSelected = null;
		Product productFromStock = stock.getProduct();
		Unit unit = null;
		double qtdExpedidaItem;						
		double qtdSolicitadaItem;
		double qtyIssuedNow = 0;
		double totalUnit = 0;
		String unitHowSell = null;
			
		ItensShipment itemFounded = searchProductOnGrid( productFromStock );
		
		if ( itemFounded != null )
		{
			OrderItem orderItem = shipmentService.getOrderItemByIdImportAndProductAndIndex(productFromStock, itemFounded.getItem());
			
			if ( orderItem != null )
			{
				productFromItemSelected = itemFounded.getProduct();
				getProductSelected(productFromItemSelected);
				unit = GuiGlobals.getDaoFactory().getUnitDao().find( itemFounded.getUnitId() );

				/**
				 * Este trecho de codigo é para realizar a expedição das quantidades solicitadas no pedido
				 */
				if ( stock.getHowSell() != null && !stock.getHowSell().isEmpty() )
				{
					unitHowSell = stock.getHowSell().replaceAll("\\d", "");
				}
				else if ( !( itemFounded.getTotalUnitId().isEmpty() ) )
				{
					unitHowSell = itemFounded.getTotalUnitId();
				}
				
				if ( qtdDigitada != 0 )
				{
					qtyIssuedNow = qtdDigitada;
				}
				else
				{
					if ( unitHowSell != null && !( unitHowSell.isEmpty() ) )
					{
						
						Unit unitSeted; 
						
						/**
						 * Tratamento para quando a expedição esta com uma etiqueta embalada pelo modulo de
						 * exceção peso padrao
						 */
						if ( unitHowSell.trim().equals("EMB") )
						{
							unitSeted = new Unit();
							unitSeted.setId("EMB");
						}
						else
						{
							unitSeted = unit;	
						}
						
						qtyIssuedNow = shipmentService.getQtyIssued(stock, productFromItemSelected, unitSeted );
					}
					else
					{
						qtyIssuedNow = shipmentService.getQtyIssued(stock, productFromItemSelected, unit);
					}
				}
				
				qtdSolicitadaItem = itemFounded.getQtdSolicitada();
				qtdExpedidaItem = NumberUtils.roundNumber(itemFounded.getQtdExpedida()+qtyIssuedNow, 3);
				
				if ( qtdExpedidaItem > qtdSolicitadaItem )
				{
					
					/**
					 * Melhoria realizada em 23/02/2016. Se for expedição de caixa, e a quantidade for superior, 
					 * o usuario em questao deve ter acesso para liberar quantidade superior
					 */
					if (	( userHasPermission == 0 && GuiGlobals.isPermitted( ComponentPermission.EXPEDICAO_QTD_MAIOR_SOLICITADO ) )
							|| userHasPermission == 1
						)
					{
						userHasPermission = 1;
						
						if (	CURRENT_ACTION == ACTION_UPDATE_GRID_ON_LABEL_BOX 
								|| CURRENT_ACTION == ACTION_UPDATE_GRID_ON_PRODUCT
								|| CURRENT_ACTION == ACTION_UPDATE_GRID_ON_LABEL_BOX_FROM_FIFO
								|| CURRENT_ACTION == ACTION_UPDATE_GRID_FROM_SSCC_OF_LABEL_BOX 
						   )
						{
							setLedScannerDisableBox();
							if ( !( GuiGlobals.showMessageDlgYesNo("Produto [" + productFromStock.getIdMasc() + "] - Quantidade acima do solicitado. Deseja continuar ?" ) == JOptionPane.YES_OPTION ) )
							{
								setLedScannerEnableBox();
								return false;								
							}
							setLedScannerEnableBox();
						}
						
						GuiGlobals.showMessage("Produto [" + productFromStock.getIdMasc() + "] - Qtd. acima do solicitado!");
					}
					else
					{
						if ( userHasPermission == 0 )
						{
							userHasPermission = -1;
							GuiGlobals.showMessageDlg("Produto [" + productFromStock.getIdMasc() + "] - Qtd. acima do solicitado! \n O usuário conectado ao terminal não tem acesso para liberar quantidade maior que o solicitado!");
							
							if ( CURRENT_ACTION == ACTION_UPDATE_GRID_ON_LABEL_PALLET )
							{
								extornaItem(false);
							}
							
						}
						return false;
					}
				}					
				
				itemFounded.setQtdExpedida( qtdExpedidaItem );										
				itemFounded.setQtdFaltante( qtdSolicitadaItem - qtdExpedidaItem);
				
				/**
				 * Este trecho de codigo é para realizar a expedição das quantidades de total de como vende
				 */
				if ( stock.getHowSell() != null && !stock.getHowSell().isEmpty() )
				{
					unitHowSell = stock.getHowSell().replaceAll("\\d", "").trim();
				}
				else 
				{
					unitHowSell = itemFounded.getProduct().getUnit().getId().trim();
				}
				
				if ( unitHowSell != null && !( unitHowSell.isEmpty() ) )
				{
					
					Unit unitSeted; 
					
					/**
					 * Tratamento para quando a expedição esta com uma etiqueta embalada pelo modulo de
					 * exceção peso padrao
					 */
					if ( unitHowSell.trim().equals("EMB") )
					{
						unitSeted = new Unit();
						unitSeted.setId("EMB");
					}
					else
					{
						unitSeted = productFromItemSelected.getUnit();	
					}
					
					qtyIssuedNow = shipmentService.getQtyIssued(stock, productFromItemSelected, unitSeted );
				}
				totalUnit = itemFounded.getTotal();
				
				itemFounded.setTotal( NumberUtils.roundNumber(totalUnit + qtyIssuedNow, 3) );
				itemFounded.setTotalUnitId( unitHowSell );				
				itemFounded.setTypeOperation(typeOperation);				
				
				orderItem.setUnitHowSell(unitHowSell);
				orderItem.setTotExpHowSell(totalUnit + qtyIssuedNow);
				orderItem.setQtyIssued( qtdExpedidaItem );
				orderItem.setCompany( Setup.getCompany() );
				orderItem.setTerminal( Setup.getTerminal() );
				if ( !TypeStock.GRANEL.equals(stock.getTypeStock() ) ) // atualiza tipo de operação somente se nao for granel
				{
					orderItem.setTypeOperation(typeOperation);
				}
				orderItem.setPrimaryQty( orderItem.getPrimaryQty() + stock.getPrimaryQty());
				orderItem.setTypeStock(productFromItemSelected.getTypeStock());
				
				shipmentService.updateOrderItem( orderItem );
				
			}
			else{
				GuiGlobals.showMessageDlg("Não foi possivel encontrar o item na tabela de Pedido!");
				return false;
			}
			
		}
		else
		{
			GuiGlobals.showMessage("Produto [" + productFromStock.getIdMasc() + "] não existe neste pedido!");
			return false;
		}
		
		return true;
		
	}
	
	public ItensShipment searchProductOnGrid( Product product )
	{
		ItensShipment item = null;
		
		for ( int nlx = 0; nlx <  tableModelItens.getdadosDaGrid().size(); nlx++)
		{
			
			if(  tableModelItens.getdadosDaGrid().get( nlx ).getProduct().getIdMasc().equals( product.getIdMasc() ) && tableModelItens.getdadosDaGrid().get( nlx ).getTypeStock() != TypeStock.GRANEL )
			{
				item = tableModelItens.getdadosDaGrid().get( nlx );
				setHowOnFocus(nlx);
				break;
			}
		}
		
		return item;
	}
	
	private void setHowOnFocusByProduct( Product product )
	{
		if ( product != null && !product.getIdMasc().isEmpty() )
		{
			for ( int nlx = 0; nlx <  tableModelItens.getdadosDaGrid().size(); nlx++)
			{
				if ( tableModelItens.getdadosDaGrid().get( nlx ).getProduct().getIdMasc().equals( product.getIdMasc() ) )
				{
					setHowOnFocus(nlx);
					break;
				}
			}
		}
	}
	
	private void setHowOnFocus(int line) 
	{
		
		/**
		 * Este comando junto com o table.getSelectionModel().addListSelectionListener
		 * faz posicionar na linha da JTable
		 */
		if ( tableModelItens.getdadosDaGrid().size() >= ( line + 1 ) ) table.setRowSelectionInterval(line, line);
		
	}

	private void clearItemOrderSelected()
	{
		tableModelItens.getdadosDaGrid().get( table.getSelectedRow() ).setQtdExpedida( 0 );
		tableModelItens.getdadosDaGrid().get( table.getSelectedRow() ).setTotal(0.0);
		tableModelItens.getdadosDaGrid().get( table.getSelectedRow() ).setTotalUnitId("");
		tableModelItens.getdadosDaGrid().get( table.getSelectedRow() ).setQtdFaltante( tableModelItens.getdadosDaGrid().get( table.getSelectedRow() ).getQtdSolicitada() );
	}
	
	private void clearStuff() {		
		tableModelItens.getdadosDaGrid().clear();
		refreshGrid();
	}
	
	private void complementProduct()
	{
		Product product;
		ItensShipment itemShipment;
		
		if ( table.getSelectedRow() >= 0 )
		{
			itemShipment = tableModelItens.getdadosDaGrid().get( table.getSelectedRow() );
			product = shipmentService.findProduct( itemShipment.getProduct() );
			getProductSelected(product);
			
			if ( !product.isFifoEnabled() )
			{					
				if ( product.getPercentShipmentComplement() > 0 )
				{						
					ComplementoItemDlg dlg = new ComplementoItemDlg( itemShipment, ShipmentPanel.this, shipmentService );
					GuiGlobals.defaultCursor();
					dlg.setVisible(true);
					
					if ( dlg.isConfirmed() )
					{						
						double netPicked = dlg.getNet();
						int qtyPicked = dlg.getQty();
						
						if ( netPicked > 0 )
						{
							boolean processOk = true;
							double percent = ( ( product.getPercentShipmentComplement() > 100 )? 100.00 : product.getPercentShipmentComplement() );								
														
							if ( product.isStandardWeight() )
							{
								if ( percent < 100.00 )
								{
									processOk = false;
									GuiGlobals.showMessage("Produto peso padrão somente lberação 100%");
								}
							}
							
							if ( processOk )
							{
								double limitShipment = 0.0;
								Unit unitItemSelected = GuiGlobals.getDaoFactory().getUnitDao().find( itemShipment.getUnitId().substring(0, 2).toUpperCase() );
								double qtyComplmentUsed = getCurrentComplement( product );
								double qtyIssued = ( itemShipment.getQtdExpedida() - qtyComplmentUsed );
								
								qtyIssued = ( qtyIssued < 0 ) ? 0 : qtyIssued;
								limitShipment = ( qtyIssued * ( percent / 100 ) );
							
								if ( percent >= 100.00 || ( ( netPicked + qtyComplmentUsed ) <= limitShipment ) )
								{								
									int item = shipmentService.getNextItem();
									
									if ( shipmentService.leaveStockGranel(product, netPicked, qtyPicked, item, unitItemSelected) )
									{
										
										ItensShipment itemRow = new ItensShipment();
										
										itemRow.setItem(item);
										itemRow.setProduct( product );
										itemRow.setQtdSolicitada( netPicked );
										itemRow.setUnitId( itemShipment.getUnitId() );
										itemRow.setQtdExpedida( netPicked );
										itemRow.setQtdFaltante( 0 );
										itemRow.setTotal(netPicked);
										itemRow.setTotalUnitId( product.getUnit().getId() );
										itemRow.setTypeStock(TypeStock.GRANEL);
										itemRow.setTypeOperation(ShipmentTypeOperation.MANUAL);
										
										/**
										 * Antonio pediu para nao acrescentar a linha na grid quando Granel, apenas acrescentar no produto, em 12-01-2016
										 */
	//									addItenShipmentOnGrid( itemRow, true );
										
										if ( itensGranel == null ) itensGranel = new ArrayList<ItensShipment>();
										itensGranel.add(itemRow);
										
										Stock stock = new Stock();								
										stock.setCompany(Setup.getCompany());
										stock.setTerminal(Setup.getTerminal());
										stock.setEnterDate( DateTimeUtils.now() );
										stock.setInFifo(false);
										stock.setManufactureDate( DateTimeUtils.now() );										
										stock.setNet(netPicked);
										stock.setNetEtq(netPicked);
										if ( shipmentService.isUnitForEmb(product, unitItemSelected) )
										{
											stock.setPrimaryQty(0);
											stock.setSecondaryQty(qtyPicked);
										}
										else
										{
											stock.setPrimaryQty(qtyPicked);
											stock.setSecondaryQty(0);
										}
										stock.setProduct(product);
										stock.setStatus(StockStatus.STOCKED);
										stock.setTare(0);
										stock.setTypeStock(TypeStock.GRANEL);
										stock.setUser(GuiGlobals.getUserInfo().getUser());
										
										/**
										 * Como a quantidade do complemento sempre é em kg, então 
										 * é necessário alterar a quantidade para a unidade de medida
										 * solicita 
										 */
										netPicked = shipmentService.getQtyIssued(stock, product, unitItemSelected);
										
										searchOnGridAndUpdate( stock, ShipmentTypeOperation.MANUAL, netPicked );
										renderizator();
									}								
									
								}
								else
								{
									if ( qtyComplmentUsed > 0 )
									{
										GuiGlobals.showMessageDlg("A soma da quantidade de complemento já existente para este produto [" + String.valueOf( qtyComplmentUsed ) + "] mais a quantidade atual [" + String.valueOf( netPicked ) + "] ultrapassa o percentual liberado!");
									}
									else if ( limitShipment == 0 && percent < 100.00 && itemShipment.getQtdExpedida() == 0 )
									{
										GuiGlobals.showMessage("Não há itens expedidos para aplicar o %");
									}
									else
									{
										GuiGlobals.showMessage("Quantidade digitada maior que percentual liberado!");
									}
								}
							}
						}
					}
					
				}
				else
				{
					GuiGlobals.showMessage("Produto com " + String.valueOf( product.getPercentShipmentComplement() ) + "% para complemento!");
				}
			}
			else{
				GuiGlobals.showMessage("Produto FIFO não é permitido complemento!");
			}
			
		}
		else
		{
			GuiGlobals.showMessage("Selecione um produto para complemento!");
		}
		
	}
	
	private double getCurrentComplement( Product product ) 
	{		
		double qty = 0;
		
		if ( itensGranel != null && itensGranel.size() > 0 )
		{
			for ( ItensShipment item : itensGranel )
			{
				if ( item.getProduct().getIdMasc().equals( product.getIdMasc() ) && item.getTypeStock() == TypeStock.GRANEL )
				{
					qty += item.getQtdExpedida();
				}
			}
		}
		
		return qty;
	}

	private String concatGranel(){
		return " (GRANEL)";
	}
	
	public void searchOrder() 
	{	
		GuiGlobals.showMessage("", false);
		String idOrderImport = txtOrderId.getText().trim();	
		GuiGlobals.closeDb();
		
		if ( shipmentService == null ) 
		{
			shipmentService = null;
			shipmentService = new ShipmentService(idOrderImport, false);
		}
		
		if ( shipmentService != null ) 
		{
			clearStuff();
			shipmentService.clearUserInProcess();
			shipmentService.refreshService( idOrderImport );
			
			if ( shipmentService.getOrder() != null && ( shipmentService.getOrder().getStatus() == OrderStatus.FINISHED || shipmentService.getOrder().getStatus() == OrderStatus.CANCELED ) )
			{
				GuiGlobals.showMessage("Pedido já finalizado!");
				showComponentsToShipment(false);
			}
			else
			{
				showComponentsToShipment(true);
				setHowOnFocus(0);
			}
			
			showStuff();
		}
	}
	
	private void showComponentsToShipment(boolean show) 
	{
		if ( show )
		{
			setLedScannerEnableBox();
		}
		else
		{
			setLedScannerDisableBox();
		}
		
		isCanExecOperation = show;
		
		btnConsultaSaldo.setEnabled(show);
		btnComplementoItem.setEnabled(show);
		btnMateriaPrima.setEnabled(show);
		btnFecharPedido.setEnabled(show);
		btnEstornaItem.setEnabled(show);
		btnEtiqueta.setEnabled(show);
		btnFIFO.setEnabled(show);
		btnLedScanner.setEnabled(show);
		btnSearch.setEnabled(show);
	}

	private void showStuff() {
		
		if(shipmentService.getOrder() == null) {
			clearStuff();
			return;
		}
		
		lblCliente.setText( "Cliente: " + shipmentService.getOrder().getPartner().getName().trim() );
		
		/**
		 * Ordeno o array por codigo do item
		 */
		shipmentService.getOrder().getItems().sort( new Comparator<OrderItem>() {  
            
			public int compare(OrderItem orderItem1, OrderItem orderItem2) {  
                OrderItem p1 = orderItem1;  
                OrderItem p2 = orderItem2;  
                return p1.getIndex() < p2.getIndex() ? -1 : (p1.getIndex() > p2.getIndex() ? +1 : 0);  
            }
			
        });
		
		for ( OrderItem orderItem : shipmentService.getOrder().getItems() )
		{
		
			ItensShipment itemRow = new ItensShipment();
			TypeStock typeStock = orderItem.getTypeStock();
			Product product = orderItem.getProduct();
			getProductSelected(product);
			String unit;
						
			if ( typeStock == TypeStock.GRANEL )
			{
				unit = product.getUnit().getId().concat( concatGranel() );
			}
			else{
				unit = orderItem.getUnit().getId(); 
			}			
			
			itemRow.setItem(orderItem.getIndex());
			itemRow.setProduct( product );
			itemRow.setQtdSolicitada( orderItem.getQtyRequested() );
			itemRow.setUnitId( unit );
			itemRow.setQtdExpedida(orderItem.getQtyIssued());
			itemRow.setQtdFaltante( orderItem.getQtyRequested() - orderItem.getQtyIssued() );
			itemRow.setTotal(orderItem.getTotExpHowSell());
			itemRow.setTotalUnitId( orderItem.getUnitHowSell() );
			itemRow.setTypeStock( typeStock );
			itemRow.setTypeOperation(orderItem.getTypeOperation());
			/**
			 * Antonio pediu para nao acrescentar a linha na grid quando Granel, apenas acrescentar no produto, em 12-01-2016
			 */
			if ( typeStock == TypeStock.GRANEL )
			{
				if ( itensGranel == null ) itensGranel = new ArrayList<ItensShipment>();
				itensGranel.add(itemRow);
			}
			else 
			{
				if ( typeStock == TypeStock.MATERIA_PRIMA ) // Necessario para funcionar corretamente a devolução 
				{
					if ( itensMP == null ) itensMP = new ArrayList<ItensShipment>();
					itensMP.add(itemRow);
				}
				
			}
			
			if ( !orderItem.isDetailItem() )
			{
				addItenShipmentOnGrid( itemRow, false );
			}
			
		}
		
		rendererGrid();
		refreshGrid();	
	}
	
	private void addItenShipmentOnGrid( ItensShipment itemRow, boolean isRenderer ){
		
		tableModelItens.getdadosDaGrid().add( itemRow );
		
		if ( isRenderer ){
			renderizator();
		}
		
	}
	
	private void renderizator() 
	{
		rendererGrid();
		refreshGrid();
	}

	private void rendererGrid()
	{	
		table.setDefaultRenderer(table.getColumnClass(1), renderizador);			
	}
			
	public void refreshGrid()
	{
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() 
		    {
		    	table.repaint();
		    	table.updateUI();
		    	table.setShowVerticalLines(true);
				table.setShowHorizontalLines(true);
		    }
		  });		
		
		setLedScannerEnableBox();
	}
	
	public boolean closeOrder() {
		
		boolean processOk = false;

		setLedScannerDisableBox();
		int dialogResult = GuiGlobals.showMessageDlgYesNo("Confirma a finalização da expedição ?");
		if(dialogResult == JOptionPane.YES_OPTION)
		{
			if ( !isOrderHasExp() )
			{
				GuiGlobals.showMessageDlg("Pedido não pode ser finalizado, pois não contém itens expedidos!");
				return false;
			}
			
			processOk = shipmentService.closeOrder();
			if ( processOk )
			{
				try
				{
					processOk = shipmentService.createArqExpAndTotAndHea();
				}
				catch( Exception e)
				{
					e.printStackTrace();
				}
				
				if ( processOk )
				{
					GuiGlobals.showMessageDlg("Pedido finalizado com sucesso!");
				}
				else
				{
					GuiGlobals.showMessageDlg("Não foi possível finalizar o pedido: ".concat( shipmentService.getWarnings() ) );
				}
				
			}
			else
			{
				GuiGlobals.showMessageDlg("Não foi possível finalizar o pedido: ".concat( shipmentService.getWarnings() ) );
			}
		}
		setLedScannerEnableBox();
		
		return processOk;
					
	}
	
	/**
	 * Metodp para identificar se houve expedição no pedido em questão
	 * @return
	 */
	private boolean isOrderHasExp() 
	{
		boolean llProcOk = false;
				
		for ( ItensShipment item : tableModelItens.getdadosDaGrid() )
		{
			if ( item.getQtdExpedida() > 0 )
			{
				llProcOk = true;
				break;
			}
		}
		return llProcOk;
	}

	private void clearTable() {

		List<ItensShipment> itens = new ArrayList<ItensShipment>();
					
		tableModelItens = new TableItensShipmentModel(itens);
		renderizador = new Renderizador( tableModelItens );
		table.setModel(tableModelItens);
		tableModelItens.initializeLayout(table);	
	}
	
	@Override
	public String getTitle() {
		return "Expedição";
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public String getInitialMessage() {
		return "";
	}

	@Override
	public void start() {
	}

	private void restartDlg() {
		GuiGlobals.getMain().shipment();				
	}
	
	public String getCodbarScanner() {
		return codbarScanner;
	}

	public void setCodbarScanner(String codbarScanner) {
		this.codbarScanner = codbarScanner;
	}

	/**
	 * Invoked when task's progress property changes.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressMonitor.setProgress(progress);
			String message = String.format("Completed %d%%.\n", progress);
			progressMonitor.setNote(message);
			taskOutput.append(message);
			if (progressMonitor.isCanceled() || processWorker.isDone()) {
				Toolkit.getDefaultToolkit().beep();
				if (progressMonitor.isCanceled()) {
					processWorker.cancel(true);
					taskOutput.append("Task canceled.\n");
				} else {
					taskOutput.append("Task completed.\n");
				}
				startButton.setEnabled(true);
			}
		}

	}

}
