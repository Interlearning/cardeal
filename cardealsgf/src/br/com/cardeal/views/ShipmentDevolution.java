package br.com.cardeal.views;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import br.com.cardeal.enums.OrderStatus;
import br.com.cardeal.enums.PurchaseOrderStatus;
import br.com.cardeal.enums.TypeReceipt;
import br.com.cardeal.filter.StockFilter;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.ReceiptData;
import br.com.cardeal.globals.ReceiptProcessWorker;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.interfaces.ShipmentDevolutionInterface;
import br.com.cardeal.model.ItensShipmentDevolution;
import br.com.cardeal.model.ItensShipmentDoneDevolution;
import br.com.cardeal.model.ItensShipmentPartialDevolution;
import br.com.cardeal.model.Order;
import br.com.cardeal.model.OrderItem;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.model.StockTotal;
import br.com.cardeal.services.ShipmentDevolutionService;
import br.com.cardeal.table.TableItensShipmentDevolutionModel;
import br.com.cardeal.table.TableItensShipmentDoneDevolutionModel;
import br.com.cardeal.table.TableItensShipmentPartialDevolutionModel;

public class ShipmentDevolution extends JPanel  implements OperationWindow 
{
	private static final long serialVersionUID = 1L;
	private static final int OPTION_ADD_ITEM = 1;
	private static final int OPTION_ADD_ITENS = 2;
	
	private static int CURRENT_DIALOG = 0;
	private static final int DIALOG_DEVOLUTION = 1;
	private static final int DIALOG_PARTIAL_DEVOLUTION = 2;
	private static final int DIALOG_DONE_DEVOLUTION = 3;
	
	private JPanel panelHeader;
	private JLabel lblOrder;
	private JLabel lblPartner;
	private JLabel lblPartnerDescription;
	private JTextField txtOrder;
	private JButton btnEnter1;
	private JScrollPane scrollPanelOfDevolution;
	private JScrollPane scrollPanelOfPartialDevolution;
	private JScrollPane scrollPanelOfDoneDevolution;
	private JTable tableOfDevolution;
	private JTable tableOfPartialDevolution;
	private JTable tableOfDoneDevolution;
	private TableItensShipmentDevolutionModel tableModelItensOfDevolution;
	private TableItensShipmentPartialDevolutionModel tableModelItensOfPartialDevolution;
	private TableItensShipmentDoneDevolutionModel tableModelItensOfDoneDevolution;
	private JTextField txtScanner;
	private JPanel panelMsgCodeBar;
	private JLabel lblMsgCodebar;
	private JButton btnLedScanner;
	private JButton btnPartialDevolution;
	private JButton btnSelectAll;
	private JButton btnConfirm;
	private JButton btnSair;
	private Order orderShipment;
	private Partner partner;
	private PurchaseOrder purchaseOrder;
	private String codbarScanner = "";
	private ReceiptProcessWorker worker;
	private ShipmentDevolutionService service = new ShipmentDevolutionService();
	
	public ShipmentDevolution() 
	{
		createDialog();
    	createComponents();
    	addComponetsOnDialog();
	}
	
	private void createDialog() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
	}
	
	private void iniComponents()
	{
		//---------------------------------------------
		//- Início de criação de objetos do cabeçalho -
		//---------------------------------------------
		panelHeader = new JPanel();
		lblOrder = new JLabel("Pedido");
		btnEnter1 = new JButton("ENTER");
        txtOrder = new JTextField();
        lblPartner = new JLabel("Cliente:");
		lblPartnerDescription = new JLabel("---");
		//-------------------------------------------
		// - Fim de criação de objetos do cabeçalho -
		//-------------------------------------------
		
		scrollPanelOfDevolution = new JScrollPane();
		tableOfDevolution = new JTable();
		scrollPanelOfPartialDevolution = new JScrollPane();
		tableOfPartialDevolution = new JTable();
		scrollPanelOfDoneDevolution = new JScrollPane();
		tableOfDoneDevolution = new JTable();
		panelMsgCodeBar = new JPanel();
		lblMsgCodebar = new JLabel();
		btnLedScanner = new JButton("SCANNER");
		btnPartialDevolution = new JButton("<html><center>Devolução<br>Parcial</center></html>");
		btnSelectAll = new JButton("<html><center>Marcar<br>Todos</center></html>");
		btnConfirm = new JButton("Confirmar");
		btnSair = new JButton("");
		txtScanner= new JTextField();
	}
	
	private void createComponents() 
	{
		iniComponents();
		
		panelHeader.setBounds(0, 0, 1022, 68);
		panelHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelHeader.setLayout(null);
		
		lblOrder.setBounds(17, 18, 96, 29);		
		lblOrder.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
        btnEnter1.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				searchOrder();
			}
		});
       
        txtOrder.setText("");
        txtOrder.setBounds(100, 5, 132, 51);        
        txtOrder.setFont(new Font("Tahoma", Font.PLAIN, 30));
        txtOrder.setColumns(6);        
        txtOrder.addMouseListener(new MouseAdapter() 
        {
			@Override
			public void mousePressed(MouseEvent e) 
			{				
				GuiGlobals.virtualKeyboardAction(null, txtOrder, null, null, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter1, null);
			}
		});		
        
        lblPartner.setBounds(245, 5, 115, 51);		
        lblPartner.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
		lblPartnerDescription.setBounds(340, 5, (panelHeader.getWidth() - 340), 51);		
		lblPartnerDescription.setForeground(new Color(0, 128, 0));
		lblPartnerDescription.setFont(new Font("Tahoma", Font.BOLD, 28));
		
		scrollPanelOfDevolution.setBounds(1, 68, 1022, 412);
		scrollPanelOfDevolution.getVerticalScrollBar().setPreferredSize(new Dimension(50, 0));
		scrollPanelOfDevolution.getHorizontalScrollBar().setPreferredSize(new Dimension(50, 0));
		
		tableOfDevolution.addMouseListener( new MouseListener() {
			
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
		tableOfDevolution.getSelectionModel().addListSelectionListener(  
			    new ListSelectionListener(){  
			        public void valueChanged(ListSelectionEvent e){  
			            JScrollBar vertBart= scrollPanelOfDevolution.getVerticalScrollBar();  
			            vertBart.setValue(tableOfDevolution.getRowHeight()*tableOfDevolution.getSelectedRow());  
			        }  
			    }  
			);  
		
		scrollPanelOfDevolution.setViewportView(tableOfDevolution);
		clearTableOfDevolution();
		
		scrollPanelOfPartialDevolution.setBounds(1, 68, 1022, 412);
		scrollPanelOfPartialDevolution.getVerticalScrollBar().setPreferredSize(new Dimension(50, 0));
		scrollPanelOfPartialDevolution.getHorizontalScrollBar().setPreferredSize(new Dimension(50, 0));
		
		scrollPanelOfPartialDevolution.setViewportView(tableOfPartialDevolution);
		clearTableOfPartialDevolution();
		
		scrollPanelOfDoneDevolution.setBounds(1, 68, 1022, 412);
		scrollPanelOfDoneDevolution.getVerticalScrollBar().setPreferredSize(new Dimension(50, 0));
		scrollPanelOfDoneDevolution.getHorizontalScrollBar().setPreferredSize(new Dimension(50, 0));
		
		scrollPanelOfDoneDevolution.setViewportView(tableOfDoneDevolution);
		clearTableOfDoneDevolution();
		
		panelMsgCodeBar.setBorder(new LineBorder(new Color(192, 192, 192)));
		panelMsgCodeBar.setForeground(new Color(255, 255, 0));
		panelMsgCodeBar.setBackground(new Color(0, 0, 0));
		panelMsgCodeBar.setBounds(0, 575, 1022, 25);
		panelMsgCodeBar.setLayout(new CardLayout(0, 0));
		
		lblMsgCodebar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMsgCodebar.setForeground(new Color(30, 144, 255));
		lblMsgCodebar.setHorizontalAlignment(SwingConstants.LEFT);
		
		btnLedScanner.setBounds(5, 480, 108, 94);		
		btnLedScanner.setBackground(Color.RED);
		btnLedScanner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if ( btnLedScanner.getBackground() == Color.RED )
				{
					setLedScannerEnableBox();					
				}
				else
				{				
					setLedScannerDisableBox();				
				}
				
			}

		});
		
		btnPartialDevolution.setBounds(123, 480, 128, 94);
		btnPartialDevolution.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnPartialDevolution.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				CURRENT_DIALOG = DIALOG_PARTIAL_DEVOLUTION;
				setDlgOperation();
			}
		});		
		
		btnSelectAll.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnSelectAll.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				selectAll();
				setLedScannerEnableBox();
			}
		});
		
		btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnConfirm.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				if ( CURRENT_DIALOG == DIALOG_DEVOLUTION )
				{
					CURRENT_DIALOG = DIALOG_DONE_DEVOLUTION;
					setDlgOperation();
					setLedScannerEnableBox();
				}
				else if ( CURRENT_DIALOG == DIALOG_PARTIAL_DEVOLUTION )
				{
					CURRENT_DIALOG = DIALOG_DEVOLUTION;
					setDlgOperation();
					setLedScannerEnableBox();
				}
				else if ( CURRENT_DIALOG == DIALOG_DONE_DEVOLUTION )
				{
					int opcao = GuiGlobals.showMessageDlgYesNo("Confirma devolução ?");
					if(opcao == JOptionPane.YES_OPTION)
					{
						purchaseOrder.setStatus( PurchaseOrderStatus.FINISHED_RETURNED );
						ReceiptData receiptData = new ReceiptData();
				  		receiptData.setPurchaseOrder(purchaseOrder);  		
				  		receiptData.setAction(ReceiptData.ACTION_FINISHED_ORDER_TO_DEVOLUTION);  		
				  		updatePurchaseOrder( receiptData ); 
					}
				}
			}
		});
		
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnSair.setBounds( 917, 480, 103, 94);
		btnSair.setIcon(new ImageIcon(ShipmentDevolution.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if ( CURRENT_DIALOG == DIALOG_DEVOLUTION )
				{
					GuiGlobals.getMain().exitOperation();
				}
				else
				{
					CURRENT_DIALOG = DIALOG_DEVOLUTION;
					setDlgOperation();
				}
			}
		});
		
		txtScanner.setBounds(1110, 5, 132, 51);		
		txtScanner.addKeyListener(new KeyAdapter() 
		{			
			
			@SuppressWarnings("static-access")
			public void keyReleased(KeyEvent e){  
				
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
							service.checkItemDevolutionBySscc( codbarScanner.trim() );
						}
						else if ( codbarScanner.length() == 10 ) 
						{
							service.checkItemDevolutionByLabelBox( codbarScanner.trim() );
						}
						else if ( codbarScanner.length() == 8 )
						{
							service.checkItemDevolutionByLabelPallet( codbarScanner.trim() );
						}
						else
						{
							GuiGlobals.getMain().showMessage("<html><center>Tamanho do código de etiqueta inválido!<br>" + codbarScanner.trim() + "</center></html>");
						}
	            		
	            		codbarScanner = "";
	            		
	            		CURRENT_DIALOG = DIALOG_DEVOLUTION;
	            		setDlgOperation();
					}					
				
				}
				else{
					keyText = e.getKeyText(key);
		            codbarScanner += keyText;
				}
				
			}
			
		 });
		
		CURRENT_DIALOG = DIALOG_DEVOLUTION;
		setDlgOperation();
	}
	
	private void addComponetsOnDialog()
	{
		add(panelHeader);
		panelHeader.add(lblOrder);
		panelHeader.add(txtOrder);
		panelHeader.add(lblPartner);
		panelHeader.add(lblPartnerDescription);
		add(scrollPanelOfDevolution);
		add(scrollPanelOfPartialDevolution);
		add(scrollPanelOfDoneDevolution);
		add(panelMsgCodeBar);
		panelMsgCodeBar.add(lblMsgCodebar);
		add(btnLedScanner);
		add(btnPartialDevolution);
		add(btnSelectAll);
		add(btnConfirm);
		add(btnSair);
		add(txtScanner);
	}

	private void setDlgOperation() 
	{
		if ( vldChangDlg() )
		{
			if ( CURRENT_DIALOG == DIALOG_PARTIAL_DEVOLUTION )		
			{
				btnLedScanner.setVisible(false);
				btnPartialDevolution.setVisible(false);
				scrollPanelOfPartialDevolution.setVisible(true);
				scrollPanelOfDoneDevolution.setVisible(false);
				scrollPanelOfDevolution.setVisible(false);
				btnSelectAll.setVisible(true);
				btnSelectAll.setBounds(5, 480, 108, 94);
				btnConfirm.setBounds(123, 480, 148, 94);
				setLedScannerDisableBox();
				GuiGlobals.getMain().getLblOperation().setText("DEVOLUÇÃO PARCIAL");
				service.addItensToPartialDevolution(purchaseOrder, tableModelItensOfDevolution.getDadosDaGrid().get( tableOfDevolution.getSelectedRow() ).getPurchaseOrderItem(), tableModelItensOfPartialDevolution);
			}
			else if ( CURRENT_DIALOG == DIALOG_DONE_DEVOLUTION )
			{
				
				btnLedScanner.setVisible(false);
				btnPartialDevolution.setVisible(false);
				scrollPanelOfPartialDevolution.setVisible(false);
				scrollPanelOfDoneDevolution.setVisible(true);
				scrollPanelOfDevolution.setVisible(false);
				btnSelectAll.setVisible(false);
				btnConfirm.setBounds(5, 480, 148, 94);
				setLedScannerDisableBox();
				GuiGlobals.getMain().getLblOperation().setText("EFETIVAR DEVOLUÇÃO ?");
				service.addItensToConfirm(purchaseOrder, tableModelItensOfDoneDevolution);
			}
			else
			{
				setLedScannerEnableBox();
				btnLedScanner.setVisible(true);
				btnPartialDevolution.setVisible(true);
				scrollPanelOfPartialDevolution.setVisible(false);
				scrollPanelOfDoneDevolution.setVisible(false);
				scrollPanelOfDevolution.setVisible(true);
				btnSelectAll.setVisible(true);
				btnSelectAll.setBounds(263, 480, 108, 94);
				btnConfirm.setBounds(387, 480, 148, 94);
				GuiGlobals.getMain().getLblOperation().setText("DEVOLUÇÃO");
				service.addItensToConfirm(purchaseOrder, tableModelItensOfDoneDevolution);
				service.addItensToDevolution(purchaseOrder, tableModelItensOfDevolution, tableModelItensOfDoneDevolution);
			}
			refreshGrid();
		}
	}

	private boolean vldChangDlg() 
	{
		boolean processOk = false;
		
		if ( CURRENT_DIALOG != DIALOG_DEVOLUTION )		
		{
			if ( purchaseOrder != null && purchaseOrder.getId() > 0 )
			{
				processOk = true;
			}
			else
			{
				GuiGlobals.showMessage("Nenhum pedido informado!");
			}
		}
		else
		{
			processOk = true;
		}
		
		return processOk;
	}

	private void selectAll() 
	{
		if ( CURRENT_DIALOG == DIALOG_PARTIAL_DEVOLUTION )
		{
			List<ShipmentDevolutionInterface> itens = new ArrayList<>();  
			itens.addAll( tableModelItensOfPartialDevolution.getDadosDaGrid() );
			service.checkAllItensDevolution( itens );
		}
		else if ( CURRENT_DIALOG == DIALOG_DEVOLUTION )
		{
			List<ShipmentDevolutionInterface> itens = new ArrayList<>();  
			itens.addAll( tableModelItensOfDevolution.getDadosDaGrid() );
			service.checkAllItensDevolution(itens);
		}
		
		refreshGrid();
	}

	/**
	 * Busca de pedido da expedição
	 */
	public void searchOrder() 
	{
		clearStuff();
		orderShipment = GuiGlobals.getDaoFactory().getOrderDao().findByIdImport( txtOrder.getText().trim() );
		
		if( orderShipment == null || orderShipment.getId() == 0 ) 
		{
			GuiGlobals.showMessage("Pedido não encontrado!");
		}
		else if ( OrderStatus.CANCELED.equals( orderShipment.getStatus() ) )
		{
			orderShipment = null;
			GuiGlobals.showMessage("Pedido cancelado!");
		}
		else if ( !OrderStatus.FINISHED.equals( orderShipment.getStatus() ) )
		{
			orderShipment = null;
			GuiGlobals.showMessage("Pedido não foi finalizado!");
		}
		else 
		{
			GuiGlobals.getMain().showMessage("");
			partner = orderShipment.getPartner();
			lblPartnerDescription.setText( partner.getName().trim().toUpperCase() );
			getPurchaseOrder();
		}
	}
	
	private void getPurchaseOrder()
	{
		PurchaseOrder purchaseOrderNew = null;
		boolean callImport = true;
		
		purchaseOrder = GuiGlobals.getDaoFactory().getPurchaseOrderDao().findByOrder(orderShipment);
		
		if ( purchaseOrder == null )
		{
			purchaseOrderNew = new PurchaseOrder();
			purchaseOrderNew.setStatus( PurchaseOrderStatus.STARTED_RETURNED );
			purchaseOrderNew.setOrderShipment(orderShipment);
			purchaseOrderNew.setCompany( Setup.getCompany() );
			purchaseOrderNew.setDateReceipt( new Date() );
			purchaseOrderNew.setPartner(partner);
			purchaseOrderNew.setTypeReceipt(TypeReceipt.DEVOLUCAO);
			
			purchaseOrder = purchaseOrderNew;
		}
		else
		{
			GuiGlobals.getDaoFactory().getPurchaseOrderDao().refresh( purchaseOrder );
			
			if( PurchaseOrderStatus.FINISHED_RETURNED.equals(purchaseOrder.getStatus()) ) 
			{
				GuiGlobals.showMessage("Pedido já foi extornado!");
				callImport = false;
			}
			else  
			{
				service.addItensToConfirm(purchaseOrder, tableModelItensOfDoneDevolution);
				
				/**
				 * Verificação dos itens
				 */
				if ( purchaseOrder.getItems() == null )
				{
					List<PurchaseOrderItem> itens = GuiGlobals.getDaoFactory().getPurchaseOrderDao().listItensFromPurchaseOrder( purchaseOrder.getId() );
					purchaseOrder.setItems( itens );
				}
			}
		}
		
		if ( callImport )
		{
			if ( purchaseOrder != null && purchaseOrder.getId() > 0 )
			{
				importDataFromReceipt();
			}
			else
			{
				importDataFromOrderShipment();
			}
		}
	}
	
	private void mantemGridDados( int opcao, List<ItensShipmentDevolution> itens )
	{
		for ( ItensShipmentDevolution item : itens)
		{
			switch ( opcao ) {
			case OPTION_ADD_ITENS:
				mantemGridDados(OPTION_ADD_ITEM, item, false);
				break;
				
			default:
				break;
			}
		
		}
		
		refreshGrid();
	}
	
	private void mantemGridDados(int opcao, ItensShipmentDevolution itemRowProduct) 
	{
		mantemGridDados( opcao, itemRowProduct, true);
	}
	
	private void mantemGridDados(int opcao, ItensShipmentDevolution itemRowProduct, boolean refresh) 
	{
		
		switch ( opcao ) {
		case OPTION_ADD_ITEM:
			service.insertDataOnGridOfDevolution(itemRowProduct, tableModelItensOfDevolution);
			break;
			
		default:
			break;
		}
		
		if ( refresh ) refreshGrid();		
	}
	
	private void importDataFromReceipt() 
	{
		
		List<ItensShipmentDevolution> itensGrid = new ArrayList<ItensShipmentDevolution>();
		
		for ( PurchaseOrderItem purchaseOrderItem : purchaseOrder.getItems() )
		{
			ItensShipmentDevolution itemRow = new ItensShipmentDevolution();
			itemRow.setChecked(service.isItemCheckedToDevolutionConfirm(purchaseOrderItem, tableModelItensOfDoneDevolution));
			itemRow.setPurchaseOrderItem(purchaseOrderItem);
			itemRow.setProduct( purchaseOrderItem.getProduct() );
			itemRow.setQuantidade( purchaseOrderItem.getQuantity() );
			itemRow.setUnidade( purchaseOrderItem.getUnit().getId() );
			itemRow.setTara( purchaseOrderItem.getTare() ); 
			itemRow.setPesoLiq( purchaseOrderItem.getNet() );
			itemRow.setPesoBruto( purchaseOrderItem.getNet() + purchaseOrderItem.getTare()  );
			itemRow.setDestino( purchaseOrderItem.getTypeStock().getSigla() );
			
			itensGrid.add( itemRow );				
		}
		
		mantemGridDados( OPTION_ADD_ITENS, itensGrid );
		GuiGlobals.showMessage("Pedido importado com sucesso!");
		
	}
	
	private void importDataFromOrderShipment() 
	{
		for ( OrderItem orderItem : orderShipment.getItems() )
		{
			if ( orderItem.getQtyIssued() > 0 )
			{
				/**
				 * Buscando tara total no estoque
				 */
				double tara = 0.0;
				StockFilter filter = new StockFilter();
				filter.setAsc(true);
				filter.setOnlyStocked(false);
				filter.setProductId( orderItem.getProduct().getId() );
				filter.setIdOrderImport( orderShipment.getId() );
				
				List<StockTotal> stockTotal = GuiGlobals.getDaoFactory().getStockDao().totalize(filter);
				
				if ( stockTotal != null && stockTotal.size() > 0 )
				{				
					tara = stockTotal.get(0).getTare();
				}
				
				filter = null;
				
				PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();	
				
				if ( "KG".equalsIgnoreCase( orderItem.getUnit().getId() ) )
				{
					purchaseOrderItem.setNet( orderItem.getQtyIssued() );
				}
				else
				{
					purchaseOrderItem.setNet( orderItem.getProduct().getNetWeight() );
				}	
				purchaseOrderItem.setTypeStock(  orderItem.getTypeStock() );
				purchaseOrderItem.setNet( orderItem.getQtyIssued() );
				purchaseOrderItem.setProduct( orderItem.getProduct() );
				purchaseOrderItem.setPurchaseOrder(purchaseOrder);
				purchaseOrderItem.setQuantity( orderItem.getPrimaryQty() );
				purchaseOrderItem.setTare( tara );
				purchaseOrderItem.setCompany( Setup.getCompany() );
				purchaseOrderItem.setOrder( orderShipment );		
				purchaseOrderItem.setUnit( orderItem.getUnit() );
				
				if ( purchaseOrder.getItems() == null )
				{
					purchaseOrder.setItems( new ArrayList<PurchaseOrderItem>() );
				}
				
				purchaseOrder.getItems().add( purchaseOrderItem );
			}
			
		}
		
		if ( purchaseOrder.getItems() != null && purchaseOrder.getItems().size() > 0 )
		{
			ReceiptData receiptData = new ReceiptData();
			receiptData.setPurchaseOrder( purchaseOrder );
			receiptData.setAction(ReceiptData.ACTION_SAVE_ORDER_TO_DEVOLUTION);			
			updatePurchaseOrder( receiptData );
		}
		
	}
	
	private void updatePurchaseOrder(final ReceiptData receiptData) {
		
		worker = new ReceiptProcessWorker(receiptData);
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			
			  @Override
			  public void propertyChange(final PropertyChangeEvent event) {
				
				  switch (event.getPropertyName()) {
				  case "progress":
					  break;
		        	
				  case "state":
		        	
					  switch ((StateValue) event.getNewValue()) {
					  case DONE:
		        		
						  if ( worker != null )
						  {
						  
							  try {
								  
								  final int count = worker.get();
								  if (count == 0)
								  {
									  GuiGlobals.showMessage("Falha no processamento!");
								  }
								  else if (count == ReceiptData.ACTION_SAVE_ORDER)
								  {
									  mantemGridDados( OPTION_ADD_ITEM, receiptData.getItemShipmentDevolutionRowManut() );
									  GuiGlobals.showMessage("Inclusão realizada com sucesso!");									  
								  }								  
								  else if ( count == ReceiptData.ACTION_SAVE_ORDER_TO_DEVOLUTION )
								  {
									  service.addItensToDevolution(purchaseOrder, tableModelItensOfDevolution);
									  refreshGrid();
									  GuiGlobals.showMessage("Pedido importado com sucesso!");
								  }
								  else if (count == ReceiptData.ACTION_FINISHED_ORDER_TO_DEVOLUTION) 
								  {
									  GuiGlobals.showMessage("Itens devolvidos com sucesso!");
									  restartAplication();
								  }								  
								
							  } 
							  catch (InterruptedException | ExecutionException e) {
								  e.printStackTrace();
							  }
							  catch (final CancellationException e) {
								  e.printStackTrace();
							  } 
							  catch (final Exception e) {
								  e.printStackTrace();
							  }
				        
						  }
						  else
						  {
							  System.out.println("worker esta nulo");
						  }
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
		  worker.execute();
	}
	
	
	private void restartAplication() 
	{		
		GuiGlobals.getMain().shipmentDevolution();		
	}
	
	private void clearTableOfDevolution() 
	{
		List<ItensShipmentDevolution> itens = new ArrayList<ItensShipmentDevolution>();
		tableModelItensOfDevolution = new TableItensShipmentDevolutionModel(itens);
		tableOfDevolution.setModel(tableModelItensOfDevolution);
		tableModelItensOfDevolution.initializeLayout(tableOfDevolution);
	}
	
	private void clearTableOfPartialDevolution() 
	{
		List<ItensShipmentPartialDevolution> itens = new ArrayList<ItensShipmentPartialDevolution>();
		tableModelItensOfPartialDevolution = new TableItensShipmentPartialDevolutionModel(itens);
		tableOfPartialDevolution.setModel(tableModelItensOfPartialDevolution);
		tableModelItensOfPartialDevolution.initializeLayout(tableOfPartialDevolution);
	}
	
	private void clearTableOfDoneDevolution()
	{
		List<ItensShipmentDoneDevolution> itens = new ArrayList<ItensShipmentDoneDevolution>();
		tableModelItensOfDoneDevolution = new TableItensShipmentDoneDevolutionModel(itens);
		tableOfDoneDevolution.setModel(tableModelItensOfDoneDevolution);
		tableModelItensOfDoneDevolution.initializeLayout(tableOfDoneDevolution);
	}
	
	private void clearStuff() 
	{		
		tableModelItensOfDevolution.getDadosDaGrid().clear();
		tableModelItensOfDoneDevolution.getDadosDaGrid().clear();
		tableModelItensOfPartialDevolution.getDadosDaGrid().clear();
		
		refreshGrid();
	}
	
	public void refreshGrid()
	{
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() 
		    {
		    	if ( CURRENT_DIALOG == DIALOG_PARTIAL_DEVOLUTION )		
				{
		    		tableOfPartialDevolution.repaint();
		    		tableOfPartialDevolution.updateUI();
		    		tableOfPartialDevolution.setShowVerticalLines(true);
		    		tableOfPartialDevolution.setShowHorizontalLines(true);
				}
		    	else if ( CURRENT_DIALOG == DIALOG_DONE_DEVOLUTION )		
				{
		    		tableOfDoneDevolution.repaint();
		    		tableOfDoneDevolution.updateUI();
		    		tableOfDoneDevolution.setShowVerticalLines(true);
		    		tableOfDoneDevolution.setShowHorizontalLines(true);
				}
		    	else
		    	{
		    		tableOfDevolution.repaint();
		    		tableOfDevolution.updateUI();
		    		tableOfDevolution.setShowVerticalLines(true);
		    		tableOfDevolution.setShowHorizontalLines(true);
		    	}
		    }
		  });		
		
		setLedScannerEnableBox();
	}
	
	private void setLedScannerEnableBox()
	{
		if ( tableModelItensOfDevolution.getDadosDaGrid().size() > 0 ) 
		{
			btnLedScanner.setBackground( Color.GREEN );			
			txtScanner.requestFocus();
		}
	}
	
	private void setLedScannerDisableBox()
	{			
		btnLedScanner.setBackground( Color.RED );		
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

	@Override
	public String getTitle() {
		return "DEVOLUÇÃO";
	}
	
}
	
