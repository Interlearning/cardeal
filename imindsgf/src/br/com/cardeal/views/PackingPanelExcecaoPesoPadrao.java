package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CancellationException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker.StateValue;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.filter.ProductFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.PackingData;
import br.com.cardeal.globals.PackingProcessWorker;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Stock;
import br.com.cardeal.services.PackingService;

public class PackingPanelExcecaoPesoPadrao extends JPanel  implements OperationWindow, PackingPanelWindows {

	private static final long serialVersionUID = 1L;	
	
	private PackingService packingService;
	
	double pesoLiquidoEtq = 0;
	
	double pesoLiquidoTotal = 0;
	
	// Tamanho Fonte letras lado esquerdo -- embaixo do panel "Pesagem"
	private int tamanhofonte = 22;
	
	// SetBounds JLabels lado esquerdo -- embaixo do panel "Pesagem"
	private int posicaoX = 20;
	private int largura = 175;
	private int altura = 29;
	
	// SetBound JTextField das Labels lado esquerdo -- embaixo do panel "Pesagem"
	private int posicaoX2 = 220;
	private int largura2 = 200;
	private int altura2 = 50;
	
	// Tela (painel) 
	private JPanel panel; 
	
	// 1 linha de componentes
	private JLabel lblMessageUser1;
	private JLabel lblMessageUser2;
	private JLabel lblMessageUser3;
	private JLabel lblProduto;
	private JTextField txtProductId;
	private JButton btnSearchProd;
	private JButton btnWeighingStyle;
	private JLabel lblNomeProduto;  // Descrição do Produto em verde
	
	// painel Pesagem 
	private JPanel panelPesagem;
	private JLabel lblPesagem;
	private JLabel lblWeighingStyle;  // label retorno get estilo Pesagem -> Variavel/Padrï¿½o
	private JLabel lblFIFO;
	private JLabel lblStockStyle;  // label retorno get estilo FIFO -> não/Sim
	private JLabel lblVirtual;	
	
	// Componentes lado Esquerdo -- embaixo do panel "Pesagem"
	private JLabel lblQtdPecas;
	private JTextField txtQtdPecas;
	
	// WJSP 03/06/2015
	private JLabel lblQtdEtiquetas;
	private JTextField txtQtdEtiquetas;	
		
	private JLabel lblPesoLiquido;
	private JTextField txtPesoLiquido;
	private JLabel lblTaraTotal;
	private JTextField txtTaraTotal;
	private JLabel lblData;	
	private JTextField txtDay;
	private JTextField txtMonth;
	private JTextField txtYear;
	private JLabel lblBarra1;
	private JLabel lblBarra2;
	private JLabel lblLote;
	private JTextField txtLote;
	
	// Botï¿½es lado direito 
	private JButton btnConfirm;
	private JButton btnReimprimir;
	private JButton btnEstornar;
	private JButton btnSair;
	
	private JLabel lblHowSell;	
	private int progress = 0;
	private int qtdSolicitada = 0;
	
	@Override
	public JTextField getTxtTaraTotal(){
		return txtTaraTotal;
	}

	@Override
	public JTextField getTxtQtdPecas(){
		return txtQtdPecas;
	}
	
	@Override
	public JTextField getTxtPesoLiquido(){
		return txtPesoLiquido;
	}
	
	public PackingPanelExcecaoPesoPadrao() {
		
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 1018, 212);
		add(panel);
		panel.setLayout(null);
		
		lblProduto = new JLabel("Produto");
		lblProduto.setBounds(17, 18, 96, 29);
		panel.add(lblProduto);
		lblProduto.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		
		txtProductId = new JTextField();
		txtProductId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearStuff();
			}
		});
		txtProductId.setBounds(110, 5, 88, 51);
		panel.add(txtProductId);
		txtProductId.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				productMousePressed();
			}
		});
		txtProductId.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtProductId.setColumns(10);
		
		btnSearchProd = new JButton("");
		btnSearchProd.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Search.png")));
		btnSearchProd.setBounds(203, 5, 78, 51);
		panel.add(btnSearchProd);
		btnSearchProd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listProducts();
			}
		});
		btnSearchProd.setFont(new Font("Tahoma", Font.BOLD, 24));
		
		lblNomeProduto = new JLabel("---");
		lblNomeProduto.setBounds(293, 0, 692, 59);
		panel.add(lblNomeProduto);
		lblNomeProduto.setForeground(new Color(0, 128, 0));
		lblNomeProduto.setFont(new Font("Tahoma", Font.BOLD, 28));
		
		panelPesagem = new JPanel();
		panelPesagem.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Pesagem", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		((javax.swing.border.TitledBorder) panelPesagem.getBorder()).setTitleFont(new Font("Tahoma", Font.PLAIN, 18));
		panelPesagem.setBounds(12, 69, 350, 127);
		panel.add(panelPesagem);
		panelPesagem.setLayout(null);
		
		lblPesagem = new JLabel("Pesagem");
		lblPesagem.setBounds(12, 34, 107, 29);
		panelPesagem.add(lblPesagem);
		lblPesagem.setForeground(new Color(0, 0, 0));
		lblPesagem.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		lblWeighingStyle = new JLabel("------------");
		lblWeighingStyle.setBounds(112, 34, 178, 29);		
		lblWeighingStyle.setForeground(Color.BLACK);
		lblWeighingStyle.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelPesagem.add(lblWeighingStyle);
				
		btnWeighingStyle = new JButton("");		
		btnWeighingStyle.setBounds(112, 34, 178, 35);
		btnWeighingStyle.setVisible(false);
		btnWeighingStyle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if ( WeighingStyle.STANDARD_WEIGHT.toString().trim().equals( btnWeighingStyle.getText().trim() ) ){
					btnWeighingStyle.setText(WeighingStyle.VARIABLE_WEIGHT.toString());
				}
				else{
					btnWeighingStyle.setText(WeighingStyle.STANDARD_WEIGHT.toString());
				}
				
			}
		});
		btnWeighingStyle.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelPesagem.add(btnWeighingStyle);		
		
		lblFIFO = new JLabel("FIFO");
		lblFIFO.setBounds(12, 76, 96, 29);
		panelPesagem.add(lblFIFO);
		lblFIFO.setForeground(Color.BLACK);
		lblFIFO.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		lblStockStyle = new JLabel("------------");
		lblStockStyle.setBounds(112, 76, 178, 29);
		panelPesagem.add(lblStockStyle);
		lblStockStyle.setForeground(Color.BLACK);
		lblStockStyle.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		lblVirtual = new JLabel("");
		lblVirtual.setBounds(225, 76, 178, 29);
		panelPesagem.add(lblVirtual);
		lblVirtual.setForeground(Color.BLACK);
		lblVirtual.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		lblMessageUser1  = new JLabel(GuiGlobals.getBundle().getString("STR000023"));
		lblMessageUser1.setBounds(365, 69, 700, 30);		
		lblMessageUser1.setForeground(Color.RED);
		lblMessageUser1.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(lblMessageUser1);
		
		lblMessageUser2  = new JLabel(GuiGlobals.getBundle().getString("STR000024"));
		lblMessageUser2.setBounds(365, 100, 700, 30);		
		lblMessageUser2.setForeground(Color.RED);
		lblMessageUser2.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(lblMessageUser2);
		
		lblMessageUser3  = new JLabel(GuiGlobals.getBundle().getString("STR000025"));
		lblMessageUser3.setBounds(365, 130, 700, 30);		
		lblMessageUser3.setForeground(Color.RED);
		lblMessageUser3.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblMessageUser3);
		
		lblQtdPecas = new JLabel("Qtde. Pe\u00E7as");
		lblQtdPecas.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblQtdPecas.setBounds(posicaoX, 240, largura, altura);
		add(lblQtdPecas);
		
		lblQtdEtiquetas = new JLabel("Qtd.Etiqueta(s)");
		lblQtdEtiquetas.setFont(new Font("Tahoma",Font.BOLD,tamanhofonte));
		lblQtdEtiquetas.setBounds(posicaoX, 300, largura, altura);
		add(lblQtdEtiquetas);	
		
		lblPesoLiquido = new JLabel("P.Líquido (Kg)");
		lblPesoLiquido.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblPesoLiquido.setBounds(posicaoX, 360, largura, altura);
		add(lblPesoLiquido);
		
		lblTaraTotal = new JLabel("Tara Total (Kg)");
		lblTaraTotal.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte)); 
		lblTaraTotal.setBounds(posicaoX, 420, largura, altura);
		add(lblTaraTotal);
		
		lblData = new JLabel("<html><left>Data de<br>Produção</left></html>");
		lblData.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblData.setBounds(posicaoX, 465, largura, 52);  // seria 480 -> porem devido a quebra de linha 
		add(lblData);	
		
		lblLote = new JLabel("Lote");
		lblLote.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblLote.setBounds(posicaoX, 540, largura, altura);
		add(lblLote);
		
		lblHowSell = new JLabel("");
		lblHowSell.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblHowSell.setBounds(posicaoX + largura + 260, 540, largura, altura);
		add(lblHowSell);
		
		txtQtdPecas = new JTextField();
		txtQtdPecas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {	
		
				if(product != null){

					if( changeQtdPecas() )
					{
						
						int qtdPecasTela = Integer.parseInt( txtQtdPecas.getText() );
						txtPesoLiquido.setText("0.000");
						/**
						 * Alteracoes de Label
						 */
						if( lblWeighingStyle.isVisible() )
						{ 
							
							//-------------------------------------------------------------------------------------------
							//- Se entrou neste if, significa que o produto nao tem quantidade de peças = 0 no cadastro -
							//-------------------------------------------------------------------------------------------
							
							if ( qtdPecasTela == 0 )
							{
								lblWeighingStyle.setText(WeighingStyle.VARIABLE_WEIGHT.toString());							
							}
							else
							{
								lblWeighingStyle.setText(WeighingStyle.STANDARD_WEIGHT.toString());
							}
							
						}
						else
						{
							if ( qtdPecasTela > 0 && product.getTargetQty() == 0 )
							{
								btnWeighingStyle.setText(WeighingStyle.STANDARD_WEIGHT.toString());
								btnWeighingStyle.setEnabled(false);
							}
							else
							{
								btnWeighingStyle.setEnabled(true);
							}
								
						}
						
						/**
						 * Alteracoes de valores segundo regras da Cardeal
						 */
						if ( !isTurnOnVariable() )
						{
							
							if ( ( qtdPecasTela != product.getTargetQty() ) 
								 && (!product.getUnit().getId().equals("KG")))
							{
																				
								pesoLiquidoEtq = product.weightPerPiece() * qtdPecasTela;								
								pesoLiquidoTotal = pesoLiquidoEtq; // coluna net tabela Stock						
								txtPesoLiquido.setText( String.valueOf( pesoLiquidoEtq ) );						
								
							}															
							else
							{
								
								double targetQty = ( product.getTargetQty() == 0 ) ? 1 : product.getTargetQty();
								
								pesoLiquidoEtq = product.getNetWeight();
								
								if ( product.getTargetQty() == 0 && getPrimaryQty() > 0 )
								{
									pesoLiquidoEtq = (product.getTargetWeight() / targetQty ) * getPrimaryQty();
									pesoLiquidoTotal = pesoLiquidoEtq;
									
								}
								else if ( product.getTargetQty() == 0 )
								{
									pesoLiquidoTotal = pesoLiquidoEtq;
								}
								else
								{
									pesoLiquidoEtq = Double.parseDouble(Utils.formatWeight( product.weightPerPiece() * getPrimaryQty()));
									pesoLiquidoTotal = pesoLiquidoEtq;
								}						
								txtPesoLiquido.setText(Utils.formatWeight( (product.getTargetWeight() / targetQty ) * getPrimaryQty()));													
								
							}
							
						}
						
						lblStockStyle.setText(GuiGlobals.getBundle().getString("STR000012"));						
						txtTaraTotal.setText(Utils.formatWeight((product.getTareBox() + (product.getTareEmbala() * getPrimaryQty()))));					
						lblHowSell.setText( packingService.rulesEtqHowSell(product, 1, getPrimaryQty(), true) );
						
					}
					
				}
			}
		});
		txtQtdPecas.setText("0");
		txtQtdPecas.setHorizontalAlignment(SwingConstants.CENTER);
		txtQtdPecas.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtQtdPecas.setColumns(10);
		txtQtdPecas.setBounds(posicaoX2, 225, largura2, altura2);
		add(txtQtdPecas);		
		
		txtQtdEtiquetas = new JTextField();
		txtQtdEtiquetas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){		
				validAction(txtQtdEtiquetas, "changeQtdEtiquetas");								
			}
		});
		txtQtdEtiquetas.setHorizontalAlignment(SwingConstants.CENTER);
		txtQtdEtiquetas.setText("1");
		txtQtdEtiquetas.setFont(new Font("Tahoma",Font.PLAIN,tamanhofonte));
		txtQtdEtiquetas.setColumns(10);
		txtQtdEtiquetas.setBounds(posicaoX2, 285, largura2, altura2);
		add(txtQtdEtiquetas);
			
		txtPesoLiquido = new JTextField();
		txtPesoLiquido.setHorizontalAlignment(SwingConstants.CENTER);
		txtPesoLiquido.setText("0.000");  // Alterado de '1' para '0.000' em 26/05/2015 -- WJSP 
		txtPesoLiquido.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtPesoLiquido.setColumns(10);
		txtPesoLiquido.setBounds(posicaoX2, 345, largura2, altura2);
		txtPesoLiquido.setEnabled(false);
		add(txtPesoLiquido);	
		
		txtTaraTotal = new JTextField();
		txtTaraTotal.setText("0.000");
		txtTaraTotal.setHorizontalAlignment(SwingConstants.CENTER);
		txtTaraTotal.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtTaraTotal.setColumns(10);
		txtTaraTotal.setBounds(posicaoX2, 405, largura2, altura2);
		add(txtTaraTotal);
		txtTaraTotal.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {																
				validAction(txtTaraTotal,"changeTare");					
			}
		});	
		
		txtDay = new JTextField();
		txtDay.setEnabled(true);
		txtDay.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtDay,"getNewDate");				
			}
		});
		txtDay.setHorizontalAlignment(SwingConstants.CENTER);
		txtDay.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtDay.setColumns(10);
		txtDay.setBounds(posicaoX2, 465, 48, altura2);
		add(txtDay);
		
		lblBarra1 = new JLabel("/");
		lblBarra1.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblBarra1.setBounds(posicaoX2+50 , 465, 15, altura2);
		add(lblBarra1); 
		
		txtMonth = new JTextField();
		txtMonth.setEnabled(true);
		txtMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtDay,"getNewDate");
			}
		});
		txtMonth.setHorizontalAlignment(SwingConstants.CENTER);
		txtMonth.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtMonth.setColumns(10);
		txtMonth.setBounds(posicaoX2+65, 465, 48, altura2);
		add(txtMonth);
		
		lblBarra2 = new JLabel("/");
		lblBarra2.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblBarra2.setBounds(posicaoX2+115 , 465, 15, altura2);
		add(lblBarra2);
		
		txtYear = new JTextField();
		txtYear.setEnabled(true);
		txtYear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtDay,"getNewDate");
			}
		});
		txtYear.setHorizontalAlignment(SwingConstants.CENTER);
		txtYear.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtYear.setColumns(10);
		txtYear.setBounds(posicaoX2+130, 465,70, altura2);
		add(txtYear);
		
		txtLote = new JTextField();				
		UserInfo info = GuiGlobals.getUserInfo();
		
		if ( !info.isPermittedComponentUpdate( ComponentPermission.LOTE ) ) { // Verifica permissao para alteracao do lote
			txtLote.setEnabled(false);
		}
		txtLote.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				GuiGlobals.virtualKeyboard(null, txtLote, null, null);
			}
		});
		
		txtLote.setHorizontalAlignment(SwingConstants.CENTER);
		txtLote.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtLote.setColumns(10);
		txtLote.setBounds(posicaoX2, 525, largura2, altura2);
		add(txtLote);		
		
		btnConfirm = new JButton("Confirmar");
		btnConfirm.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Apply.png")));
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnAction();
			}
		});
		//btnConfirm.setBackground(new Color(34, 139, 34));
		btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 30));
		btnConfirm.setBounds(456, 225, 249, 285);
		btnConfirm.setEnabled(false);
		add(btnConfirm);
		
		btnSair = new JButton("Sair");
		btnSair.setBounds(734, 447, 248, 63);
		add(btnSair);
		btnSair.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				GuiGlobals.getMain().menuPacking();
			}
		});
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 24));
	
		btnReimprimir = new JButton("Reimprimir");
		btnReimprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reprint();
			}
		});
		btnReimprimir.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Print.png")));
		btnReimprimir.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnReimprimir.setBounds(734, 300, 248, 63);
		add(btnReimprimir);
		
		btnEstornar = new JButton("Estornar");
		btnEstornar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rollback();
			}
		});
		btnEstornar.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Cancel.png")));
		btnEstornar.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnEstornar.setBounds(734, 374, 248, 63);
		btnEstornar.setEnabled(false);
		add(btnEstornar);
						
		enableStuff(false);
		
	}

	//===============================================================================================
	// Regras de negocio da tela 
	//===============================================================================================
	
	private Product product = null;	
	private PackingProcessWorker worker;
	private int btnStatus = 0; //0=confirm  1=cancel
	private double tareBox = 0;
	
	/* WJSP 05/06/2015
	 * Modificado de private void para public void devido a chamada da função validAction
	 * getMethod enxerga apenas funções públicas
	 */
	public void getNewDate() {
		DateDlg dlg = new DateDlg(txtDay.getText(), txtMonth.getText(), txtYear.getText(), "Data Fabricação");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {
			txtDay.setText(dlg.getDay());
			txtMonth.setText(dlg.getMonth());
			txtYear.setText(dlg.getYear());
			
			showBatch(dlg.getDay()+"/"+dlg.getMonth()+"/"+dlg.getYear()+" 00:00");
		}
	}	
	
	public void productMousePressed() {
		GuiGlobals.virtualKeyboard(null, txtProductId, "searchProduct", this);		
	}	
	
	// Limpo os campos da tela e o objeto product da memória
	private void clearStuff() {
		product = null;		  
		tareBox = 0;

		lblHowSell.setText("");
		lblNomeProduto.setText("");
		lblStockStyle.setText("------------");
		lblWeighingStyle.setText("------------");
		lblVirtual.setText("");
		txtQtdPecas.setText("0");
		txtQtdEtiquetas.setText("1");
		txtPesoLiquido.setText("0.000"); 
		btnEstornar.setEnabled(false);
		showCurrentDay(); 
		showBatch(txtDay.getText()+ "/"+txtMonth.getText()+"/"+txtYear.getText()+" 00:00");
		
		progress = 0;
		setQtdSolicitada(1);
	}
	
	public void validAction(JTextComponent textComponent, String methodName){		
		GuiGlobals.isEnabledAction(textComponent,methodName,this);				
	}
	
	private void showStuff() {
		
		if(product == null) {
			clearStuff();
			return;
		}			
		
		if(product.isFifoEnabled() && product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT) {			
			GuiGlobals.showMessage(GuiGlobals.getBundle().getString("STR000010"));
		}		
		lblStockStyle.setText(GuiGlobals.getBundle().getString("STR000012"));
		
//		if ( product.isVirtual() ){
//			lblVirtual.setText("VIRTUAL");
//		}
//		else{
//			lblVirtual.setText("");
//		}		
		
		btnEstornar.setEnabled(true);
		lblNomeProduto.setText(product.getDescription());
		
		if ( product.getTargetQty() == 0 ){
			btnWeighingStyle.setVisible(true);			
			btnWeighingStyle.setText(product.getWeighingStyle().toString());
			lblWeighingStyle.setVisible(false);
		}
		else{
			btnWeighingStyle.setVisible(false);
			lblWeighingStyle.setVisible(true);
			lblWeighingStyle.setText(product.getWeighingStyle().toString());
		}
		txtQtdPecas.setText(String.valueOf(product.getTargetQty()));		 		 		
		txtPesoLiquido.setText(Utils.formatWeight(pesoLiquidoTotal));		
		txtTaraTotal.setText( Utils.formatWeight( product.getTareTotal() ) );
		txtQtdEtiquetas.setText("1");
		lblHowSell.setText( packingService.rulesEtqHowSell(product, 1, getPrimaryQty(), true) );
	}	
	
	private boolean isTaraTotalChange() {
		return txtTaraTotal.getText().trim().equals( Utils.formatWeight( ( product.getTareBox() + ( product.getTareEmbala() * getPrimaryQty() ) ) ).trim() );
	}
	
	private void listProducts() {
		GuiGlobals.waitCursor();
		GuiGlobals.showMessage("", false);
		
		/* WJSP 
		 * Alterado para retornar apenas produtos com peso Padrão em 26/05/2015  
		 * Istanciar um filtro para passar ao ProductsDlg() em 28/05/2015   
		 */
		ProductFilter filter = new ProductFilter();
		filter.setWeighingStyle(WeighingStyle.STANDARD_WEIGHT);
					
		ProductsDlg dlg = new ProductsDlg(filter);
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);
		if(dlg.getSelectedProduct() != null) {
			txtProductId.setText(String.valueOf(dlg.getSelectedProduct().getIdMasc()));
			searchProduct();
		}
	}
	
	// Get das informações do Produto escolhido 
	public void searchProduct() {
		
		//limpa os campos da tela e o objeto product da memória
		clearStuff();
		
		if ( packingService == null ){
			   packingService = new PackingService();
		}

		product = packingService.findProductByIdMasc( txtProductId.getText().trim() );
		
		if(product == null) {
			GuiGlobals.showMessage(GuiGlobals.getBundle().getString("STR000027"));
			enableStuff(false);
		}
		else if(product.isBlocked()) 
		{
			GuiGlobals.showMessage("Produto está bloqueado");
			enableStuff(false);
		}
		else if (product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT){
			GuiGlobals.showMessage("Produto digitado não é Peso Padrão");  // WJSP -- Validação Peso Padrão em 26/05/2015
			enableStuff(false);			
		}
		else{

			pesoLiquidoEtq = product.getNetWeight();  			
			pesoLiquidoTotal = Double.parseDouble(Utils.formatWeight( product.getTargetWeight()));	
			
			// mostra dados na tela e libera as ações da tela 
			showStuff();
			GuiGlobals.getMain().showMessage("");					
			
			enableStuff(true);
		}
			
	}
	
	private int getPrimaryQty() {
		try {
			int value = Integer.parseInt(txtQtdPecas.getText());
			return value;
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	private int getQtdEtiquetas() {
		try {
			int value = Integer.parseInt(txtQtdEtiquetas.getText());
			return value;
		}
		catch(Exception e) {
			return 0;
		}
	}

	private void showCurrentDay() {
		String[] dateParts = DateTimeUtils.getCurrentDate().split("/");
		txtDay.setText(dateParts[0]);
		txtMonth.setText(dateParts[1]);
		txtYear.setText(dateParts[2]);
	}
	
	public void btnAction() {
		if(btnStatus == 0)
			confirm();
		else
			cancel();
	}
	
	private void cancel() {
		if(worker != null)
			worker.terminate();		
	}
	
	private double getTareEmbalagem() {		
		return ( product.getTareEmbala() * getPrimaryQty() );
	}
	
	public void confirm() {
		
		String dun14 = null;
			
		//valida as quantidades
		int primaryQty = Integer.parseInt(txtQtdPecas.getText()); // Quantidade de Peças 
		
		//valida os pesos e taras
		double net = 0;
		double netStorage = 0;
		double tare = 0;		
		double taraTotalCx = Double.parseDouble(txtTaraTotal.getText());
				
		if ( isTurnOnVariable() ){				
			net = GuiGlobals.getCurrentNet();
			netStorage = net;					
			//product.setWeighingStyle(WeighingStyle.VARIABLE_WEIGHT); Quando Qtd de Peças == 0, sei que produto ï¿½ peso variavel
			tare = GuiGlobals.getCurrentTare();
			
			//se o produto for de peso variável e as taras de embalagem e Caixa estão programadas,
			//a balança não pode apresentar tara para evitar erros
			if(product.getTareEmbala() > 0 || product.getTareBox() > 0 || taraTotalCx > 0) {
				
				if(tare > 0) {
					GuiGlobals.showMessage("Produto de peso variável com tara pré-configurada deve ser pesado na balança sem tara.");
					return;
				}
				else {
					tare = taraTotalCx;
				}
				
			}			
		}
		// Senão, considero o Peso Padrão  
		else {
			net =  pesoLiquidoEtq;//product.getNetWeight();   // alimenta campo netEtq tabela Stock
			netStorage =  pesoLiquidoTotal;//product.getTargetWeight();  // alimenta campo net Tabela Stock		
			tare = taraTotalCx; // Tara Embalagem * Qtd Peças + tara da Caixa 
		}
				
		if(net <= 0) {
			GuiGlobals.showMessage("Peso inválido para a operação");
			return;
		}			
		
		//valida a data de fabricacao
		Date date = getManufactureDate();
		if(date == null) {
			GuiGlobals.showMessage("A data de fabricação é inválida");
			return;			
		}
		
		//grava e imprime
		GuiGlobals.waitCursor();
		setBtnToCancel();
		
		dun14 = product.getDun14();
		
		final PackingData packingData = new PackingData();
		packingData.setExceptionStandardWeight(true);
		packingData.setAction(PackingData.ACTION_PACK);
		packingData.setDate(date);
		packingData.setDun14( dun14 );
		packingData.setInFifo( false );
		packingData.setBatch(txtLote.getText());
		packingData.setCompany(Setup.getCompany());
		packingData.setExpirationDate(  getExpirationDate() );
		packingData.setNet(net);					// Peso Liquido Etiqueta
		packingData.setNetStorage(netStorage);		// Peso Liquido Total 
		packingData.setPartner(null);
		packingData.setPrimaryQty(primaryQty);  		// Quantidade de Peças 
		packingData.setSecondaryQty(1); 				// Quantidade de Caixas -> Exceção Peso Padrï¿½o sempre serï¿½ 1 CX (banco) e EMB (etiqueta)
		packingData.setQtdEtiquetas(getQtdEtiquetas());	// WJSP Quantidade de Etiquetas em 11/06/2015 
		packingData.setProduct(product);		
		packingData.setTare(tare);
		
		if (  !isTaraTotalChange() ){			
			packingData.setTaraCaixa( Double.parseDouble( txtTaraTotal.getText() ) );
			packingData.setTaraEmbalagem( 0 );
		}
		else{
			packingData.setTaraCaixa(product.getTareBox());
			packingData.setTaraEmbalagem( getTareEmbalagem() );
		}
		packingData.setUser(GuiGlobals.getUserInfo().getUser());
		packingData.setTerminal(Setup.getTerminal());
		packingData.setSecondPrint(false);
		packingData.setChangedQty(primaryQty != product.getTargetQty());
		packingData.setTypeStock(TypeStock.EMBALAGEM);
		packingData.setEtqHowSell( lblHowSell.getText() );
		packingData.setPrimaryQtyOperation( Integer.parseInt( txtQtdPecas.getText() ) );
		
		if( isTurnOnVariable() ){
			packingData.setObjPanel(this);
			packingData.setForceVariableWeight(true);
		}
		
		worker = new PackingProcessWorker(packingData);  
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
		    public void propertyChange(final PropertyChangeEvent event) {
				switch (event.getPropertyName()) {
		        case "progress":		        	
		        	break;
		        case "state":
		        	switch ((StateValue) event.getNewValue()) {
		        	case DONE:		        		
				        try {				        	
				        	
				        	txtQtdEtiquetas.setText("1");
				        	setBtnToConfirm();
			    			GuiGlobals.defaultCursor();
			    			
				        } catch (final CancellationException e) {
				        	e.printStackTrace();
				        } catch (final Exception e) {
				        	e.printStackTrace();
				        }
				        worker = null;
				        
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
		worker.execute();  // Chama função doInBackground da Classe PackingProcessWorker
	}
	
	private boolean isTurnOnVariable() {		
		return ( ( lblWeighingStyle.getText().trim().equals( WeighingStyle.VARIABLE_WEIGHT.toString() ) ) || ( btnWeighingStyle.getText().trim().equals( WeighingStyle.VARIABLE_WEIGHT.toString() ) ) );
	}

	private Date getExpirationDate(){
		int ajustCalc = ( product.getExpirationDays() == 0 ) ? 0 : 1;
		return DateTimeUtils.addDays(getManufactureDate(), (product.getExpirationDays()-ajustCalc));
	}
	
	private Date getManufactureDate(){
		return DateTimeUtils.validateDate(txtDay.getText(), txtMonth.getText(), txtYear.getText());
	}
	
	public String getTitle() {
		return GuiGlobals.getBundle().getString("STR000006");
	}

	public JPanel getPanel() {
		return this;
	}

	public String getInitialMessage() {
		return GuiGlobals.getBundle().getString("STR000005");
	}
	
	private void showBatch(String date) { // formato da data dd/MM/yyyy HH:mm
		
		Calendar c = Calendar.getInstance();
		if ( date == null ){
			c.setTime(DateTimeUtils.now());
		}
		else{			
			try {
				c.setTime(DateTimeUtils.strToDate(date));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int batch = 0;
		switch(dayOfWeek) {
		case 1: batch = 7; break; //domingo
		case 2: batch = 1; break; //segunda
		case 3: batch = 2; break; //terca
		case 4: batch = 3; break; //quarta
		case 5: batch = 4; break; //quinta
		case 6: batch = 5; break; //sexta
		case 7: batch = 6; break; //sabado
		}
		txtLote.setText(String.format("%06d", batch));
	}
	
	public void start() {
		showCurrentDay();
		showBatch(null);
	}
	
	private void setBtnToConfirm() {
		btnConfirm.setText("Confirmar");
		btnConfirm.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Apply.png")));
		//btnConfirm.setBackground(new Color(34, 139, 34));
		btnStatus = 0;
	}
	
	private void setBtnToCancel() {
		btnConfirm.setText("Cancelar");
		btnConfirm.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Cancel.png")));
		//btnConfirm.setBackground(new Color(255, 0, 0));
		btnStatus = 1;
	}	
	
	private void reprint() {
		GuiGlobals.waitCursor();
		ReprintDlg dlg = new ReprintDlg(TypeStock.EMBALAGEM);
		dlg.searchLastLabels();
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);		
	}
	
	private void rollback() {
		
		GuiGlobals.waitCursor();
		
		//pega a ultima caixa embalada neste terminal
		Stock stock = packingService.findLastStockRollBack( TypeStock.EMBALAGEM );
		if(stock == null) {
			GuiGlobals.defaultCursor();			
			return;
		}
		
//		Pode estornar caixa de pallet fechado - Antonio - 02-07-2015
//		if(stock.getPallet() != null) {
//			Pallet pallet = packingService.findPalletByStock( stock );
//			if(pallet != null && pallet.getStatus() == PalletStatus.CLOSED) {
//				GuiGlobals.showMessage("Esta caixa estï¿½ em um pallet fechado, e não pode ser estornada.");
//				return;				
//			}
//		}
		
		RollbackDlg dlg = new RollbackDlg(stock, TypeStock.EMBALAGEM);
		GuiGlobals.defaultCursor();
		dlg.setVisible(true); 
		if(dlg.isConfirmed()) {
			packingService.rollBack(stock);				
			showStuff();
		}
	}
		
	public void changeTare() {
		double typedTare = Double.parseDouble(txtTaraTotal.getText());
		TareDlg dlg = new TareDlg(typedTare);
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {
			this.tareBox = dlg.getValue();
			txtTaraTotal.setText(Utils.formatWeight(tareBox));
		}
	}
	
	public void changeQtdEtiquetas(){
		QuantityDlg dlg = new QuantityDlg(1,9999,getQtdEtiquetas(),"Quantidade de Etiquetas");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {
			txtQtdEtiquetas.setText(String.valueOf(dlg.getValue()));											
		}
	}
		
	public boolean changeQtdPecas(){				

		boolean confirmed = false;
		
		QuantityDlg dlg = new QuantityDlg(0,999,getPrimaryQty(),"Quantidade de Peças");
		dlg.setVisible(true);
		confirmed = dlg.isConfirmed();
		if( confirmed ) {
			txtQtdPecas.setText(String.valueOf(dlg.getValue()));
		}
		
		return confirmed;
	}
	
	// Habilita ou desabilita os componentes da tela de acordo com as validações 
	private void enableStuff(boolean enable){
				
		txtQtdPecas.setEnabled(enable);
		txtQtdEtiquetas.setEnabled(enable);		
		enableTaraTotal(enable);
		txtDay.setEnabled(enable);
		txtMonth.setEnabled(enable);
		txtYear.setEnabled(enable);
		btnConfirm.setEnabled(enable);	
		
		if(!enable){
			product = null;
			txtProductId.setText(""); 
		}
		
	}

	@Override
	public void addProgress(int qtd) {			
		setProgress( this.progress + qtd);
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public void refreshQtds() {		
	}
	
	private void enableTaraTotal( boolean enable ){
		
		if ( enable ){
			
			if ( product != null && product.getLabelFileName() != null && product.getLabelFileName().isBlockTara() ){
				enable = false;
			}
			
		}
		
		txtTaraTotal.setEnabled( enable );
		
	}

	public int getQtdSolicitada() {
		return qtdSolicitada;
	}

	public void setQtdSolicitada(int qtdSolicitada) {
		this.qtdSolicitada = qtdSolicitada;
	}
	
}