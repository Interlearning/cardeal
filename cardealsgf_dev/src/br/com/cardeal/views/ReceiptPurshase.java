package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.LineBorder;
import br.com.cardeal.enums.PurchaseOrderStatus;
import br.com.cardeal.enums.TypeOperation;
import br.com.cardeal.enums.TypeReceipt;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.MaxLengthTextDocument;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.ReceiptData;
import br.com.cardeal.globals.ReceiptProcessWorker;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.ItensReceipt;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.PurchaseOrder;
import br.com.cardeal.model.PurchaseOrderItem;
import br.com.cardeal.services.ReceiptPurchaseService;
import br.com.cardeal.table.TableItensReceiptModel;

public class ReceiptPurshase extends JPanel  implements OperationWindow 
{
	private static final long serialVersionUID = 1L;
	public static final int OPTION_ADD_ITEM = 1;
	private JPanel panelHeader;
	private JLabel lblNote;
	private JLabel lblPartner;
	private JLabel lblPartnerDescription;
	private JLabel lblTile;
	private JLabel lblOperation;
	private JTextField txtNote;
	private JTextField txtPartner;
	private JButton btnSearchPartner;
	private JButton btnEnter1;
	private JButton btnEnter2;
	private JButton btnEnter3;
	private JButton btnSair;
	private Partner partner;
	private Product product;
	private ReceiptPurchaseService service;
	private PurchaseOrder purchaseOrder;
	private JComboBox<String> cmbOperation;
	private JButton btnVisualizerItensReceipt;
	private JButton btnAddManual;
	private JLabel lblBatchExternal;
	private JTextField txtBatchExternal;
	private JLabel lblDateValidateBatchExternal;
	private JLabel lblProduct;
	private JTextField txtProduct;
	private JLabel lblPrimaryQty;
	private JTextField txtPrimaryQty;
	private JLabel lblTare;
	private JTextField txtTare;
	private JLabel lblWeight;
	private JTextField txtWeight;
	private TableItensReceiptModel tableModelItens;
	private ReceiptProcessWorker worker;
	private ReceiptProcessWorker workerForWeighing;
	private JTable table;
	private JScrollPane scrollPanel;
	private JTextField txtDay;
	private JLabel lblBarraDay;
	private JTextField txtMonth;
	private JLabel lblBarraMonth;
	private JTextField txtYear;
	private JButton btnBackToWeighing;
	private JButton btnSearchProduct;
	private JButton btnEnter4;
	private JButton btnEnter5;
	private JButton btnEnter6;
	private boolean inDlgToWeghing = false;
	private JButton btnFinishPurshaseOrder;
	
	public ReceiptPurshase() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		service = new ReceiptPurchaseService();
		
		panelHeader = new JPanel();
		panelHeader.setBounds(0, 0, 1022, 68);
		panelHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(panelHeader);
		panelHeader.setLayout(null);
		
		lblNote = new JLabel("Nota");
		lblNote.setBounds(17, 18, 96, 29);		
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 24));
		panelHeader.add(lblNote);
		
		btnEnter1 = new JButton("ENTER");
        btnEnter1.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				searchNoteFromPartner();
			}
		});
        
        txtNote = new JTextField();
        txtNote.setBounds(110, 5, 132, 51);        
        txtNote.setFont(new Font("Tahoma", Font.PLAIN, 30));
        txtNote.setColumns(6);
        MaxLengthTextDocument maxLengthTxtNote = new MaxLengthTextDocument();
        maxLengthTxtNote.setMaxChars(6);
        txtNote.setDocument(maxLengthTxtNote);
        txtNote.addMouseListener(new MouseAdapter() 
        {
			@Override
			public void mousePressed(MouseEvent e) 
			{				
				GuiGlobals.virtualKeyboardAction(null, txtNote, null, null, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter1, null);
			}
		});		
        panelHeader.add(txtNote);
        
        lblPartner = new JLabel("Fornecedor");
        lblPartner.setBounds(255, 5, 120, 51);		
        lblPartner.setFont(new Font("Tahoma", Font.PLAIN, 24));
		panelHeader.add(lblPartner);
		
		btnEnter2 = new JButton("ENTER");
        btnEnter2.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				partner = service.findPartnerById( Integer.parseInt( txtPartner.getText() ) );
				refreshPartner();
				searchNoteFromPartner();
			}
		});
        
        MaxLengthTextDocument maxLengthTxtPartner = new MaxLengthTextDocument();
        maxLengthTxtPartner.setMaxChars(6);
		txtPartner = new JTextField();
		txtPartner.setBounds(385, 5, 142, 51);        
		txtPartner.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtPartner.setColumns(6);       
		txtNote.setDocument(maxLengthTxtPartner);
		txtPartner.addMouseListener(new MouseAdapter() 
        {
			@Override
			public void mousePressed(MouseEvent e) 
			{				
				GuiGlobals.virtualKeyboardAction(null, txtPartner, null, null, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter2, null);
			}
		});		
        panelHeader.add(txtPartner);
        
        btnSearchPartner = new JButton("");
		btnSearchPartner.setIcon(new ImageIcon(ReceiptPurshase.class.getResource("/32x32/Search.png")));
		btnSearchPartner.setBounds(530, 5, 78, 51);		
		btnSearchPartner.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				listPartner();
			}
		});		
		panelHeader.add(btnSearchPartner);
		
		lblPartnerDescription = new JLabel("---");
		lblPartnerDescription.setBounds(615, 5, 400, 51);		
		lblPartnerDescription.setForeground(new Color(0, 128, 0));
		lblPartnerDescription.setFont(new Font("Tahoma", Font.BOLD, 28));
		panelHeader.add(lblPartnerDescription);
		
		btnBackToWeighing = new JButton("Voltar");
		btnBackToWeighing.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnBackToWeighing.setBounds( 5, 80, 150, 50);
		btnBackToWeighing.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				disableItensReceipts();
			}
		} );
		btnBackToWeighing.setVisible(false);
		add(btnBackToWeighing);
		
		lblTile = new JLabel(getTitleParameters());
		lblTile.setBounds(185, 80, 1000, 50);		
		lblTile.setFont(new Font("Tahoma", Font.BOLD, 32));
		add(lblTile);
		
		scrollPanel = new JScrollPane();
		scrollPanel.setBounds(1, 140, 1022, 455);
		scrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(50, 0));
		scrollPanel.getHorizontalScrollBar().setPreferredSize(new Dimension(50, 0));
		scrollPanel.setVisible(false);
        add( scrollPanel );
        
        table = new JTable();        
        scrollPanel.setViewportView(table);
		
		lblOperation = new JLabel("Operação");
		lblOperation.setBounds(20, 140, 200, 50);		
		lblOperation.setFont(new Font("Tahoma", Font.BOLD, 28));
		add(lblOperation);
		
		cmbOperation = new JComboBox<String>();
		TypeOperation[] types = TypeOperation.values();
		for ( TypeOperation type : types )
		{
			cmbOperation.addItem(type.getDescricao());
		}
		cmbOperation.setBounds(230, 140, 300, 51);		
		cmbOperation.setFont(new Font("Tahoma", Font.BOLD, 28));
		cmbOperation.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				if ( cmbOperation.getSelectedItem().equals(TypeOperation.BALANCA_AUTO.getDescricao()) )
				{
					btnAddManual.setEnabled(false);
					txtWeight.setEnabled(false);
					setInDlgToWeghing(true);
					startServiceToWeighing();
				}
				else if ( cmbOperation.getSelectedItem().equals(TypeOperation.BALANCA_MANUAL.getDescricao()) )
				{
					setInDlgToWeghing(false);
					btnAddManual.setEnabled(true);
					txtWeight.setEnabled(false);
					finishServiceToWeighing();
				}
				else
				{
					setInDlgToWeghing(false);
					btnAddManual.setEnabled(true);
					txtWeight.setEnabled(true);
					finishServiceToWeighing();
				}
			}
		});
		add(cmbOperation);
		
		lblBatchExternal = new JLabel("Lote Externo");
		lblBatchExternal.setBounds(20, 200, 200, 50);		
		lblBatchExternal.setFont(new Font("Tahoma", Font.BOLD, 28));
		add(lblBatchExternal);
		
		txtBatchExternal = new JTextField();
		txtBatchExternal.setBounds(230, 200, 300, 50);		
		txtBatchExternal.setFont(new Font("Tahoma", Font.PLAIN, 28));
		txtBatchExternal.addMouseListener(new MouseAdapter() 
        {
			@Override
			public void mousePressed(MouseEvent e) 
			{				
				GuiGlobals.showMessage("", false);
				GuiGlobals.virtualKeyboard(null, txtBatchExternal, null, this, false, false, true);
			}
		});
		MaxLengthTextDocument maxLengthTxtBatchExternal = new MaxLengthTextDocument();
		maxLengthTxtBatchExternal.setMaxChars(20);
        txtBatchExternal.setDocument(maxLengthTxtBatchExternal);
		add(txtBatchExternal);
		
		lblDateValidateBatchExternal = new JLabel("<html><center>Data Validade<br>Lote Externo</center></html>");
		lblDateValidateBatchExternal.setBounds(20, 270, 200, 60);		
		lblDateValidateBatchExternal.setFont(new Font("Tahoma", Font.BOLD, 28));
		add(lblDateValidateBatchExternal);
		
		txtDay = new JTextField();
		txtDay.setFont(new java.awt.Font("Tahoma", 0, 24));
        txtDay.addMouseListener(new MouseAdapter() {
			  @Override
			  public void mousePressed(MouseEvent e) 
			  {
				  GuiGlobals.showMessage("", false);
				  getNewDate();
			  }
		  });
        txtDay.setHorizontalAlignment(SwingConstants.CENTER);
        txtDay.setFont(new Font("Tahoma", Font.PLAIN, 24));
        txtDay.setColumns(10);
        txtDay.setBounds(230, 275, 48, 51);
		add(txtDay);
		
		lblBarraDay = new JLabel("/");
		lblBarraDay.setFont(new java.awt.Font("Tahoma", 0, 24));
	    lblBarraDay.setBounds(283, 275, 15, 51);
		add(lblBarraDay);
		
		txtMonth = new JTextField();
        txtMonth.setFont(new java.awt.Font("Tahoma", 0, 24));
        txtMonth.addMouseListener(new MouseAdapter() 
        {
			  @Override
			  public void mousePressed(MouseEvent e) 
			  {
				  GuiGlobals.showMessage("", false);
				  getNewDate();
			  }
		 });
        txtMonth.setHorizontalAlignment(SwingConstants.CENTER);
        txtMonth.setFont(new Font("Tahoma", Font.PLAIN, 24));
        txtMonth.setColumns(10);
        txtMonth.setBounds(298, 275, 48, 51);
		add(txtMonth);
		
		lblBarraMonth = new JLabel("/");
		lblBarraMonth.setFont(new java.awt.Font("Tahoma", 0, 24));
	    lblBarraMonth.setText("/");
	    lblBarraMonth.setBounds(351, 275, 15, 51);
		add(lblBarraMonth);

		txtYear = new JTextField();
        txtYear.setFont(new java.awt.Font("Tahoma", 0, 24));
        txtYear.addMouseListener(new MouseAdapter() {
			  @Override
			  public void mousePressed(MouseEvent e) 
			  {
				  GuiGlobals.showMessage("", false);
				  getNewDate();
			  }
		 });
        txtYear.setHorizontalAlignment(SwingConstants.CENTER);
        txtYear.setFont(new Font("Tahoma", Font.PLAIN, 24));
        txtYear.setColumns(10);
        txtYear.setBounds(366, 275, 90, 51);
		add(txtYear);
		
		lblProduct = new JLabel("Produto");
		lblProduct.setBounds(20, 340, 200, 50);		
		lblProduct.setFont(new Font("Tahoma", Font.BOLD, 28));
		add(lblProduct);
		
		btnEnter3 = new JButton("ENTER");
        btnEnter3.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				searchProduct();
			}
		});

		txtProduct = new JTextField();
		txtProduct.setBounds(230, 340, 210, 50);		
		txtProduct.setFont(new Font("Tahoma", Font.PLAIN, 28));
		txtProduct.addMouseListener(new MouseAdapter() 
        {
			@Override
			public void mousePressed(MouseEvent e) 
			{	
				GuiGlobals.showMessage("", false);
				GuiGlobals.virtualKeyboardAction(null, txtProduct, null, null, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter3, null);
			}
		});
		add(txtProduct);
		
		btnSearchProduct = new JButton("");
		btnSearchProduct.setIcon(new ImageIcon(ReceiptPurshase.class.getResource("/32x32/Search.png")));
		btnSearchProduct.setBounds(450, 340, 75, 51);		
		btnSearchProduct.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				listProducts();
			}
		});		
		add(btnSearchProduct);
		
		lblPrimaryQty = new JLabel("<html><center>Quantidade<br>de Peças</center></html>");
		lblPrimaryQty.setBounds(20, 400, 200, 60);		
		lblPrimaryQty.setFont(new Font("Tahoma", Font.BOLD, 28));
		add(lblPrimaryQty);
		
		btnEnter6 = new JButton("ENTER");
        btnEnter6.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				validateQty();;
			}
		});
        
		txtPrimaryQty = new JTextField();
		txtPrimaryQty.setBounds(230, 405, 300, 50);		
		txtPrimaryQty.setFont(new Font("Tahoma", Font.PLAIN, 28));
		txtPrimaryQty.setText("0");
		txtPrimaryQty.addMouseListener(new MouseAdapter() 
        {
			  @Override
			  public void mousePressed(MouseEvent e) 
			  {
				  GuiGlobals.showMessage("", false);
				  mousePressedForTxtPrimaryQty(); 
			  }
		 });
		add(txtPrimaryQty);
		
		lblTare = new JLabel("Tara");
		lblTare.setBounds(20, 470, 200, 60);		
		lblTare.setFont(new Font("Tahoma", Font.BOLD, 28));
		add(lblTare);
		
		btnEnter5 = new JButton("ENTER");
        btnEnter5.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				validateTare();
			}
		});
        
		txtTare = new JTextField();
		txtTare.setBounds(230, 470, 300, 50);		
		txtTare.setFont(new Font("Tahoma", Font.PLAIN, 28));
		txtTare.setText("0.000");
		txtTare.addMouseListener(new MouseAdapter() 
        {
			  @Override
			  public void mousePressed(MouseEvent e) 
			  {
				  GuiGlobals.showMessage("", false);
				  mousePressedForTxtTare(); 
			  }
		 });
		add(txtTare);
		
		lblWeight = new JLabel("Peso");
		lblWeight.setBounds(20, 540, 200, 60);		
		lblWeight.setFont(new Font("Tahoma", Font.BOLD, 28));
		add(lblWeight);
		
		btnEnter4 = new JButton("ENTER");
        btnEnter4.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				validateWeight();
			}
		});
        
		txtWeight = new JTextField();
		txtWeight.setBounds(230, 540, 300, 50);		
		txtWeight.setFont(new Font("Tahoma", Font.PLAIN, 28));
		txtWeight.setText("0.000");
		txtWeight.addMouseListener(new MouseAdapter() 
        {
			  @Override
			  public void mousePressed(MouseEvent e) 
			  {
				  GuiGlobals.showMessage("", false);
				  mousePressedForTxtWeight(); 
			  }
		 });
		add(txtWeight);
		
		btnAddManual = new JButton("<html><center>Adicionar<br>Manualmente</center></html>");
		btnAddManual.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnAddManual.setBounds( 600, 140, 370, 210);
		btnAddManual.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				addProductManual();
			}
		});
		add(btnAddManual);
		
		btnVisualizerItensReceipt = new JButton("<html><center>Visualizar<br>Itens Recebidos</center></html>");
		btnVisualizerItensReceipt.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnVisualizerItensReceipt.setBounds( 600, 365, 370, 110);
		btnVisualizerItensReceipt.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				if ( tableModelItens.getDadosDaGrid().size() > 0 )
				{
					boolean dlgToWeghing = isInDlgToWeghing();
					setInDlgToWeghing(false);
					importDataFromPurshaseOrder();
					enableItensReceipts();
					setInDlgToWeghing(dlgToWeghing);
				}
				else
				{
					GuiGlobals.showMessage("Não há itens para visualizar!");
				}
			}
		} );
		add(btnVisualizerItensReceipt);
		
		btnFinishPurshaseOrder = new JButton("<html><center>Finalizar<br>Compra</center></html>");
		btnFinishPurshaseOrder.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnFinishPurshaseOrder.setBounds( 600, 485, 220, 110);
		btnFinishPurshaseOrder.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				if ( tableModelItens.getDadosDaGrid().size() > 0 )
				{
					finishPurchaseOrder();
				}
				else
				{
					GuiGlobals.showMessage("Não há itens para finalizar a compra!");
				}
			}
		} );
		add(btnFinishPurshaseOrder);
		
		btnSair = new JButton("Sair");
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 28));
		btnSair.setBounds( 825, 485, 148, 110);
		btnSair.setIcon(new ImageIcon(ReceiptPurshase.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				finishServiceToWeighing();
				GuiGlobals.getMain().exitOperation();
			}
		});
		
		add(btnSair);
		
		componentsToWeighing(false);
	}
	
	private void restartAplication()
	{
		GuiGlobals.getMain().receiptPurshase();
	}
	
	private void finishServiceToWeighing()
	{
		int qtdVezes = 0;
		if ( workerForWeighing != null )
		{
			while ( workerForWeighing != null && !workerForWeighing.isCancelled() )
			{
				workerForWeighing.terminate();
				workerForWeighing.cancel(true);
				GuiGlobals.sleep(500);
				qtdVezes++;
				
				if ( qtdVezes > 3 )
				{
					break;
				}
					
			}
			workerForWeighing = null;
		}
		GuiGlobals.showMessage("", false);
	}
	
	private void waitingForStabilizingWeight() 
	{
		finishServiceToWeighing();
		ReceiptData receiptData = new ReceiptData();
		receiptData.setAction(ReceiptData.ACTION_WAITING_FOR_WEIGHT_MANUAL);
		receiptData.setObject(this);
		
		if ( workerForWeighing != null )
		{
			workerForWeighing.terminate();
			workerForWeighing = null;
		}
		
		workerForWeighing = new ReceiptProcessWorker(receiptData);
		workerForWeighing.addPropertyChangeListener(new PropertyChangeListener() {
			
			  @Override
			  public void propertyChange(final PropertyChangeEvent event) 
			  {
				  switch (event.getPropertyName()) 
				  {
				  case "progress":
					  break;
		        	
				  case "state":
		        	
					  switch ((StateValue) event.getNewValue()) 
					  {
						  case DONE:
			        		
							  if ( workerForWeighing != null )
							  {
								  if ( workerForWeighing.isCancelled() )
								  {
									  System.out.println("workerForWeighing esta cancelado");
									  GuiGlobals.showMessage("", false);
									  workerForWeighing = null;
								  }
								  else
								  {
									  try 
									  {
										  
										  final int count = workerForWeighing.get();
										  if (count == 0)
										  {
											  GuiGlobals.showMessage("Falha no processamento!");
										  }
										  else if (count == ReceiptData.ACTION_WAITING_FOR_WEIGHT_MANUAL)
										  {
											  GuiGlobals.showMessage("", false);
										  }								  
										
									  } 
									  catch (InterruptedException | ExecutionException e) 
									  {
										  e.printStackTrace();
									  }
									  catch (final CancellationException e) 
									  {
										  e.printStackTrace();
									  } 
									  catch (final Exception e) {
										  e.printStackTrace();
									  }
								  }
							  }
							  else
							  {
								  GuiGlobals.showMessage("", false);
								  System.out.println("workerForWeighing esta nulo");
							  }
							  break;
						  case STARTED:
							  break;
						  case PENDING:
							  break;
						  default :
							  break;
					  }
					  break;
				  }
			  }
		  });
		workerForWeighing.execute();
	}
	
	private void startServiceToWeighing() 
	{
		ReceiptData receiptData = new ReceiptData();
		receiptData.setAction(ReceiptData.ACTION_WAITING_FOR_WEIGHT);
		receiptData.setObject(this);
		
		if ( workerForWeighing != null )
		{
			finishServiceToWeighing();
		}
		
		workerForWeighing = new ReceiptProcessWorker(receiptData);
		workerForWeighing.addPropertyChangeListener(new PropertyChangeListener() {
			
			  @Override
			  public void propertyChange(final PropertyChangeEvent event) {
				
				  switch (event.getPropertyName()) {
				  case "progress":
					  break;
		        	
				  case "state":
		        	
					  switch ((StateValue) event.getNewValue()) 
					  {
						  case DONE:
			        		
							  if ( workerForWeighing != null )
							  {
								  if ( workerForWeighing.isCancelled() )
								  {
									  System.out.println("workerForWeighing esta cancelado");
									  GuiGlobals.showMessage("", false);
									  workerForWeighing = null;
								  }
								  else
								  {
									  try 
									  {
										  
										  final int count = workerForWeighing.get();
										  if (count == 0)
										  {
											  GuiGlobals.showMessage("Falha no processamento!");
										  }
										  else if (count == ReceiptData.ACTION_WAITING_FOR_WEIGHT_FINISHED)
										  {
											  GuiGlobals.showMessage("", false);
										  }								  
										
									  } 
									  catch (InterruptedException | ExecutionException e) 
									  {
										  e.printStackTrace();
									  }
									  catch (final CancellationException e) 
									  {
										  e.printStackTrace();
									  } 
									  catch (final Exception e) 
									  {
										  e.printStackTrace();
									  }
								  }
							  }
							  else
							  {
								  GuiGlobals.showMessage("", false);
								  System.out.println("workerForWeighing esta nulo");
							  }
							  break;
						  case STARTED:
							  break;
						  case PENDING:
							  break;
						  default :
							  break;
					  }
					  break;
				  }
			  }
		  });
		workerForWeighing.execute();
	}

	private void searchProduct() 
	{
		product = service.findProductByIdMasc( txtProduct.getText() );
		
		if ( product == null || product.getId() == 0 )
		{
			GuiGlobals.showMessage("Produto [" + txtProduct.getText() + "] não encontradao!");
		}
		
		refreshProduct();
	}

	private void mousePressedForTxtWeight() 
	{
		GuiGlobals.virtualKeyboardAction(null, txtWeight, null, this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter4, null);
	}
	
	private void mousePressedForTxtTare() 
	{
		GuiGlobals.virtualKeyboardAction(null, txtTare, null, this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter5, null);
	}
	
	private void mousePressedForTxtPrimaryQty()
	{
		GuiGlobals.virtualKeyboardAction(null, txtPrimaryQty, null, this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter6, null);
	}
	
	private void validateWeight()
	{
		validateFieldOfWeight(txtWeight);
	}
	
	private void validateTare()
	{
		validateFieldOfWeight(txtTare);
	}
	
	private void validateQty() 
	{
		
		double value = 0;
		
		try 
		{
			double newVal = NumberUtils.roundNumber(Double.parseDouble(txtPrimaryQty.getText().replace(",", ".")), 0);
			
			if(newVal < 0 || newVal > 999)
			{
				txtPrimaryQty.setText(String.valueOf(value).replace(".0", ""));
				GuiGlobals.showMessage("Quantidade ultrapassa 999 unidades!");
			}
			else
			{
				txtPrimaryQty.setText(String.valueOf(newVal).replace(".0", ""));
			}
		}
		catch(Exception e) {
			txtPrimaryQty.setText(String.valueOf(value).replace(".0", ""));
		}
	}
	
	private void validateFieldOfWeight( JTextField txtCurrent ) 
	{
		double value = 0;
		
		try {
			double newVal = Double.parseDouble(txtCurrent.getText());
			if(newVal < 0 || newVal > 999.999)
				txtCurrent.setText(Utils.formatWeight(value));
			else
				txtCurrent.setText(Utils.formatWeight(newVal));
		}
		catch(Exception e) {
			txtCurrent.setText(Utils.formatWeight(value));
		}
	}
	
	private void refreshProduct() 
	{
		String idMasc = "";
		
		if ( product != null && product.getId() > 0 )
		{
			idMasc = product.getIdMasc();
			
			if ( product.isStandardWeight() )
			{
				txtPrimaryQty.setText( String.valueOf( product.getTargetQty() ) );
				validateQty();
				txtWeight.setText( String.valueOf( product.getNetWeight() ) );
				validateWeight();
			}
		}
		txtProduct.setText(idMasc);
	}

	private void listProducts() 
	{
		ProductsDlg dlg = new ProductsDlg();
		dlg.setVisible(true);
		if(dlg.getSelectedProduct() != null) 
		{
			product = dlg.getSelectedProduct();
			refreshProduct();
		}
	}
	
	private void addProductManual() 
	{
		if ( isCanAddProductManual() )
		{
			if ( getTypeOperation().BALANCA_MANUAL.equals(getTypeOperation()) )
			{				
				waitingForStabilizingWeight();
			}
			else
			{
				addProduct(ReceiptData.ACTION_SAVE_ORDER, true);
			}			
		}
	}
	
	public ReceiptData addProduct( int action, boolean addNow ) 
	{
		return addProduct( action, addNow, false ); 
	}
	
	public ReceiptData addProduct( int action, boolean addNow, boolean isInThread ) 
	{
		if ( product == null || product.getId() == 0 )
		{
			if ( isInThread )
			{
				LogDeProcessamento.gravaLog("console", "Produto [" + txtProduct.getText().trim() + "] não existe!", true);
				return null;
			}
			else
			{
				txtPrimaryQty.setText( String.valueOf( 0 ) );
				validateQty();
				txtWeight.setText( String.valueOf( 0 ) );
				validateWeight();
				TelaPiscante tela = new TelaPiscante("Produto [" + txtProduct.getText().trim() + "] não encontrado!");
				tela.setVisible(true);
				return null;
			}
		}
		else
		{
			ItensReceipt item = new ItensReceipt();
			item.setBatchExternal(txtBatchExternal.getText());
			item.setDateValidateBatchExternal(getDateBatchExternal());
			item.setProduct(product);
			item.setQuantidade( Integer.parseInt( txtPrimaryQty.getText() ) );
			item.setTara( Double.parseDouble( txtTare.getText() ) );
			item.setPesoLiq( Double.parseDouble( txtWeight.getText() ));
			
			setNewPurchaseOrder();
			
			PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();		
			purchaseOrderItem.setTypeStock(  item.getProduct().getTypeStock() );
			purchaseOrderItem.setProduct( item.getProduct() );
			purchaseOrderItem.setPurchaseOrder(purchaseOrder);
			purchaseOrderItem.setQuantity( item.getQuantidade() );
			purchaseOrderItem.setCompany( Setup.getCompany() );
			purchaseOrderItem.setBatchExternal( item.getBatchExternal() );
			purchaseOrderItem.setDateValidateBatchExternal( item.getDateValidateBatchExternal() );
			purchaseOrderItem.setUnit( item.getProduct().getUnit() );
			purchaseOrderItem.setTypeOperation(getTypeOperation());
			purchaseOrderItem.setDevolution(false);
			purchaseOrderItem.setBatchInternal(getBatchInternal());
			
			if ( getTypeOperation().BALANCA_AUTO.equals(getTypeOperation()) || getTypeOperation().BALANCA_MANUAL.equals(getTypeOperation()) )
			{
				purchaseOrderItem.setNet( GuiGlobals.getCurrentNet() );
//				purchaseOrderItem.setTare( GuiGlobals.getCurrentTare() );
				purchaseOrderItem.setTare( item.getTara() );
			}
			else
			{
				purchaseOrderItem.setNet( item.getPesoLiq() );
				purchaseOrderItem.setTare( item.getTara() );
			}
			
			if ( purchaseOrder.getItems() == null )
			{
				purchaseOrder.setItems( new ArrayList<PurchaseOrderItem>() );
			}
			
			purchaseOrder.getItems().add( purchaseOrderItem );
			
			ReceiptData receiptData = new ReceiptData();
			receiptData.setPurchaseOrder(purchaseOrder);
			receiptData.setAction(action);
			receiptData.setItemRowManut( item );
			if ( addNow ) updatePurchaseOrder( receiptData );
			
			return receiptData;
		}
		
	}

	private String getBatchInternal() 
	{
		Calendar c = Calendar.getInstance();
		c.setTime(DateTimeUtils.now());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int batch = 0;
		switch(dayOfWeek) 
		{
			case 1: batch = 7; break; //domingo
			case 2: batch = 1; break; //segunda
			case 3: batch = 2; break; //terca
			case 4: batch = 3; break; //quarta
			case 5: batch = 4; break; //quinta
			case 6: batch = 5; break; //sexta
			case 7: batch = 6; break; //sabado
		}
		return String.format("%06d", batch);
	}

	private void finishPurchaseOrder()
	{
		int opcao = GuiGlobals.showMessageDlgYesNo("Deseja finalizar operação de compras ?");
		if ( opcao == JOptionPane.YES_OPTION )
		{
			finishServiceToWeighing();
			purchaseOrder.setStatus(PurchaseOrderStatus.FINISHED_PURCHASE_ORDER);
			ReceiptData receiptData = new ReceiptData();
			receiptData.setPurchaseOrder(purchaseOrder);
			receiptData.setAction(ReceiptData.ACTION_ORDER_FINISHED);
			updatePurchaseOrder( receiptData );
			restartAplication();
		}
	}
	
	private TypeOperation getTypeOperation() 
	{
		return TypeOperation.getElementByDescription( cmbOperation.getSelectedItem().toString() );
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
						  
							  try 
							  {
								  
								  final int count = worker.get();
								  if (count == 0)
								  {
									  GuiGlobals.showMessage("Falha no processamento!");
								  }
								  else if (count == ReceiptData.ACTION_SAVE_ORDER)
								  {
									  mantemGridDados( OPTION_ADD_ITEM, receiptData.getItemRowManut() );
									  GuiGlobals.showMessage("Inclusão realizada com sucesso!");									  
								  }								  
								  else if (count == ReceiptData.ACTION_ORDER_FINISHED) 
								  {
									  GuiGlobals.showMessage("Pedido finalizado");
									  GuiGlobals.sleep(1500);
									  service.generateArqsTxt(purchaseOrder);
								  }
								
							  } 
							  catch (InterruptedException | ExecutionException e) 
							  {
								  e.printStackTrace();
							  }
							  catch (final CancellationException e) 
							  {
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
	
	public void mantemGridDados(int opcao, ItensReceipt itemRowProduct) 
	{
		switch ( opcao ) {
		case OPTION_ADD_ITEM:
			insertDataOnGrid(itemRowProduct);
			break;
			
		default:
			break;
		}
	}

	private void insertDataOnGrid(ItensReceipt itemRowProduct) 
	{
		if (tableModelItens.getRowCount() > 0)
		{
			
			boolean achou = false;
			int primaryQty;			
			double tare;
			double pesoLiquido;
			
			for (int i = 0; i < tableModelItens.getRowCount(); i++) 
			{
				if (itemRowProduct.getProduct().equals( tableModelItens.getDadosDaGrid().get(i).getProduct() ) ) 
				{
					ItensReceipt itemRowChange = tableModelItens.getDadosDaGrid().get(i);
		
					primaryQty = itemRowProduct.getQuantidade() + itemRowChange.getQuantidade();
					tare = NumberUtils.roundNumber(itemRowProduct.getTara() * primaryQty , 3);
					pesoLiquido = NumberUtils.roundNumber( itemRowProduct.getPesoLiq() + itemRowChange.getPesoLiq(), 3);
		
					itemRowChange.setQuantidade( primaryQty );
					itemRowChange.setTara( tare );
					itemRowChange.setPesoLiq( pesoLiquido );
					itemRowChange.setBatchExternal( itemRowProduct.getBatchExternal() );
					itemRowChange.setDateValidateBatchExternal( itemRowProduct.getDateValidateBatchExternal() );
		  					
					tableModelItens.getDadosDaGrid().set(i, itemRowChange);						
	
					achou = true;
					break;
				}
			}
					
			if( !achou )
			{
				tableModelItens.getDadosDaGrid().add( itemRowProduct );		
			}
		}
		else
		{		
			tableModelItens.getDadosDaGrid().add( itemRowProduct );
		}	
		
	}

	private Date getDateBatchExternal() 
	{
		Date date = null;
		
		if( !txtDay.getText().isEmpty() && !txtMonth.getText().isEmpty() && !txtYear.getText().isEmpty() )
		{
			date = DateTimeUtils.validateDate(txtDay.getText(), txtMonth.getText(), txtYear.getText());
		}
		
		return date;
	}
	
	private boolean isFieldsRequiredToPurchaseOrder()
	{
		return isFieldsRequiredToPurchaseOrder(true);
	}
	private boolean isFieldsRequiredToPurchaseOrder(boolean showMessage)
	{
		boolean canAdd = true;
		
		if ( txtNote.getText().isEmpty() )
		{
			canAdd = false;
			if ( showMessage ) GuiGlobals.showMessage("Preencha a nota!");
		}
		else if ( txtPartner.getText().isEmpty() )
		{
			canAdd = false;
			if ( showMessage ) GuiGlobals.showMessage("Preencha o fornecedor!");
		}
		
		return canAdd;
	}

	public boolean isFieldsRequiredOk() 
	{
		return isFieldsRequiredOk( true ); 
	}
	
	public boolean isFieldsRequiredOk( boolean showMessage ) 
	{
		boolean canAdd = isFieldsRequiredToPurchaseOrder(showMessage);
		
		if ( txtProduct.getText().isEmpty() )
		{
			canAdd = false;
			if ( showMessage ) GuiGlobals.showMessage("Preencha o código do produto!");
		}
		else if ( txtPrimaryQty.getText().isEmpty() || Integer.parseInt( txtPrimaryQty.getText() ) <= 0 )
		{
			canAdd = false;
			if ( showMessage ) GuiGlobals.showMessage("Preencha a quantidade de peças!");
		}
		else if ( !txtBatchExternal.getText().isEmpty() && txtDay.getText().isEmpty() )
		{
			canAdd = false;
			if ( showMessage ) GuiGlobals.showMessage("Preencha a data de validade do lote!");
		}
		
		return canAdd;
	}
	
	private boolean isCanAddProductManual() 
	{
		boolean canAdd = isFieldsRequiredOk();
	
		if (canAdd 
			&& ( txtWeight.getText().isEmpty() || Double.parseDouble( txtWeight.getText() ) <= 0 )
			&& ( getTypeOperation().equals(TypeOperation.MANUAL ) )
		   )
		{
			canAdd = false;
			GuiGlobals.showMessage("Preencha o peso!");
		}
		
		return canAdd;
	}

	private void enableItensReceipts() 
	{
		lblTile.setText("Itens Recebidos");
		lblTile.setBounds(185, 80, 1000, 50);
		componentsToWeighing(false, false);
		scrollPanel.setVisible(true);
		btnBackToWeighing.setVisible(true);
	}
	
	private void disableItensReceipts() 
	{
		lblTile.setText( getTitleParameters() );
		lblTile.setBounds(185, 80, 1000, 50);
		componentsToWeighing(true);
		scrollPanel.setVisible(false);
		btnBackToWeighing.setVisible(false);
	}

	private String getTitleParameters() 
	{
		return "PARÂMETROS PARA O RECEBIMENTO";
	}

	private void listPartner() 
	{
		GuiGlobals.waitCursor();
		PartnerDlg dlg = new PartnerDlg();
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);
		if(dlg.getSelectedFornecedor() != null) 
		{
			partner = dlg.getSelectedFornecedor();
			refreshPartner();
			searchNoteFromPartner();
		}
	}
	
	private void refreshPurchaseOrder()
	{
		service.refreshPurchaseOrder( purchaseOrder );
	}
	
	private void refreshPartner() 
	{
		if ( isExistPartner() )
		{
			txtPartner.setText(String.valueOf(partner.getId()));
			lblPartnerDescription.setText(partner.getName());
		}
	}

	public boolean isInDlgToWeghing() {
		return inDlgToWeghing;
	}

	public void setInDlgToWeghing(boolean inDlgToWeghing) {
		this.inDlgToWeghing = inDlgToWeghing;
	}

	private void searchNoteFromPartner() 
	{
		if ( !txtNote.getText().isEmpty() && isExistPartner() )
		{
			purchaseOrder = service.findPurchaseOrderByNoteAndPartner( txtNote.getText(), partner);
			
			if ( isExistPurchaseOrder() )
			{
				if ( PurchaseOrderStatus.FINISHED_PURCHASE_ORDER.equals(purchaseOrder.getStatus()) )
				{
					int opcao = GuiGlobals.showMessageDlgYesNo("O status desta nota consta como encerrado, deseja reabrir ?");
					if ( opcao == JOptionPane.YES_OPTION )
					{
						purchaseOrder.setStatus(PurchaseOrderStatus.STARTED_PURCHASE_ORDER);
						service.updatePurchaseOrder(purchaseOrder);
						initData();
						importDataFromPurshaseOrder();
						componentsToWeighing(true);
					}
				}
				else if ( PurchaseOrderStatus.STARTED_PURCHASE_ORDER.equals(purchaseOrder.getStatus()) )
				{
					importDataFromPurshaseOrder();
					componentsToWeighing(true);
				}
				else
				{
					GuiGlobals.showMessage("Falha no processamento!");
					/**
					 * Se entrar nesta condição significa que o registro foi gravado errado. Rever processo para 
					 * identificar o erro.
					 */
				}
			}
			else // nova compra
			{
				/**
				 * Cria objeto da compra
				 */
				setNewPurchaseOrder();
				
				/**
				 * Apenas prepara a JTable de itens para visualização
				 */
				initData();
				componentsToWeighing(true);
			}
		}
	}

	private void setNewPurchaseOrder()
	{
		if ( isFieldsRequiredToPurchaseOrder() )
		{
			if ( purchaseOrder == null )
			{
				purchaseOrder = new PurchaseOrder();
			}
			else if ( purchaseOrder.getId() > 0 )
			{
				GuiGlobals.getDaoFactory().getPurchaseOrderDao().refresh(purchaseOrder);
			}
			purchaseOrder.setStatus( PurchaseOrderStatus.STARTED_PURCHASE_ORDER );
			purchaseOrder.setCompany( Setup.getCompany() );
			purchaseOrder.setDateReceipt( new Date() );
			purchaseOrder.setPartner(partner);
			purchaseOrder.setTypeReceipt(TypeReceipt.COMPRA);
			purchaseOrder.setNote(txtNote.getText());
		}
	}
	
	/**
	 * habilita componentes para iniciar a pesagem
	 */
	private void componentsToWeighing(boolean enable) 
	{
		componentsToWeighing(enable, true);
	}
	private void componentsToWeighing(boolean enable, boolean visible) 
	{
		if ( txtNote.getText().isEmpty() || txtPartner.getText().isEmpty() )
		{
			enable = false;
		}
		
		cmbOperation.setEnabled(enable);
		txtBatchExternal.setEnabled(enable);
		txtDay.setEnabled(enable);
		txtMonth.setEnabled(enable);
		txtYear.setEnabled(enable);
		txtProduct.setEnabled(enable);
		btnSearchProduct.setEnabled(enable);
		txtPrimaryQty.setEnabled(enable);
		txtTare.setEnabled(enable);
		btnVisualizerItensReceipt.setEnabled(enable);
		btnFinishPurshaseOrder.setEnabled(enable);
		
		if ( txtNote.getText().isEmpty() || txtPartner.getText().isEmpty() )
		{
			btnAddManual.setEnabled(false);
			txtWeight.setEnabled(false);
		}
		else
		{
			btnAddManual.setEnabled(true);
			txtWeight.setEnabled(true);			
		}
		
		lblOperation.setVisible(visible);
		lblBatchExternal.setVisible(visible);
		lblDateValidateBatchExternal.setVisible(visible);
		lblBarraDay.setVisible(visible);
		lblBarraMonth.setVisible(visible);
		lblProduct.setVisible(visible);
		lblPrimaryQty.setVisible(visible);
		lblTare.setVisible(visible);
		lblWeight.setVisible(visible);
		cmbOperation.setVisible(visible);
		txtBatchExternal.setVisible(visible);		
		txtDay.setVisible(visible);
		txtMonth.setVisible(visible);
		txtYear.setVisible(visible);
		txtProduct.setVisible(visible);
		txtPrimaryQty.setVisible(visible);
		btnSearchProduct.setVisible(visible);
		txtTare.setVisible(visible);
		btnAddManual.setVisible(visible);
		btnVisualizerItensReceipt.setVisible(visible);
		btnAddManual.setVisible(visible);
		txtWeight.setVisible(visible);
		btnFinishPurshaseOrder.setVisible(visible);
		btnSair.setVisible(visible);
	}

	private boolean isExistPartner() 
	{
		return ( partner != null && partner.getId() > 0 );
	}
	
	private boolean isExistPurchaseOrder() 
	{
		return ( purchaseOrder != null && purchaseOrder.getId() > 0 );
	}
	
	private void initData() 
	{
		List<ItensReceipt> itens = new ArrayList<ItensReceipt>();
		tableModelItens = new TableItensReceiptModel(itens);
		table.setModel(tableModelItens);
		tableModelItens.initializeLayout(table);
		
		refreshGrid();
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
	}
	
	private void importDataFromPurshaseOrder()
	{
		clearTableModel();
		refreshPurchaseOrder();
		
		for ( PurchaseOrderItem purchaseOrderItem : purchaseOrder.getItems() )
		{
			if ( purchaseOrderItem.isNotDevolutionYeat() )
			{
				ItensReceipt item = new ItensReceipt();
				item.setBatchExternal(purchaseOrderItem.getBatchExternal());
				item.setDateValidateBatchExternal(purchaseOrderItem.getDateValidateBatchExternal());
				item.setProduct(purchaseOrderItem.getProduct());
				item.setQuantidade( new Double( purchaseOrderItem.getQuantity() ).intValue() );
				item.setTara( purchaseOrderItem.getTare() );
				item.setPesoLiq( purchaseOrderItem.getNet() );
				insertDataOnGrid(item);
			}
		}
	}
	
	private void clearTableModel() 
	{
		if ( tableModelItens == null )
		{
			initData();
		}
		
		tableModelItens.getDadosDaGrid().clear();
		refreshGrid();
	}

	private void getNewDate() 
	{
		String[] dateParts = DateTimeUtils.getCurrentDate().split("/");
		
		
		DateDlg dlg = new DateDlg(dateParts[0], dateParts[1], dateParts[2], "Data Validade");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {
			txtDay.setText(dlg.getDay());
			txtMonth.setText(dlg.getMonth());
			txtYear.setText(dlg.getYear());
		}
	}

	public Product getProduct() {
		return product;
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
		return "COMPRAS";
	}

}
	
