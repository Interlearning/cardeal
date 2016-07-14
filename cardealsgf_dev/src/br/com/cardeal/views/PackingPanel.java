package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.PackingData;
import br.com.cardeal.globals.PackingProcessWorker;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Etiqueta;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Stock;
import br.com.cardeal.services.PackingService;

public class PackingPanel extends JPanel  implements OperationWindow, PackingPanelWindows {

	private static final long serialVersionUID = 1L;
	
	int qtdCaixas;
	int defaultQtdPecas;	
	double defaultTaraTotalCx;
	 
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
	private JLabel lblProduto;
	private JTextField txtProductId;
	private JButton btnSearchProd;
	private JLabel lblNomeProduto;  // Descrição do Produto em verde
	
	// painel Pesagem 
	private JPanel panelPesagem;
	private JLabel lblPesagem;
	private JLabel lblWeighingStyle;  // label retorno get estilo Pesagem -> Variavel/Padrão
	private JLabel lblFIFO;
	private JLabel lblStockStyle;  // label retorno get estilo FIFO -> Não/Sim
	private JLabel lblVirtual;	
	
	// painel Pallet
	private JPanel panelPallet;
	private TitledBorder titleContador = new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Contador", TitledBorder.LEADING, TitledBorder.TOP, null, null);
	private TitledBorder titlePallet = new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Palete", TitledBorder.LEADING, TitledBorder.TOP, null, null);		
	private JLabel lblPalletAtual;
	private JLabel lblPallet;
	private JLabel lblQtdePallet;
	private JLabel lblPalletQty;
	private JLabel lblCantoneira;
	private JTextField txtPalletCantoneira;
	private JLabel lblTara;
	private JTextField txtPalletTare;
	private JLabel lblStrech;
	private JTextField txtPalletStrech;
	private JLabel lblRack;
	private JTextField txtPalletRack;
	private JLabel lblHowSell;
			
	// Componentes lado Esquerdo -- embaixo do panel "Pesagem"
	private JLabel lblQtdPecas;
	private JTextField txtQtdPecas;
	
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
	
	// Botões lado direito
	private JButton btnConfirm;
	private JButton btnFecharPallet;	
	private JButton btnReimprimir;		
	private JButton btnEstornar;
	private JButton btnSair;

	private JLabel lblQtdeEtq;
	private JLabel lblTextoEtq;
	
	private int progress = 0;	
	private int qtdSolicitada = 0;	
	private int qtyEtiquetasToPrint;
	
	private PackingService packingService;
	
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
	
	public PackingPanel() {
		
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 1018, 212);
		add(panel);
		panel.setLayout(null);
		
		// 1 linha de componentes
		lblProduto = new JLabel("Produto");
		lblProduto.setBounds(17, 18, 96, 29);
		panel.add(lblProduto);
		lblProduto.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
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
		
		// painel Pesagem	
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
		panelPesagem.add(lblWeighingStyle);
		lblWeighingStyle.setForeground(Color.BLACK);
		lblWeighingStyle.setFont(new Font("Tahoma", Font.BOLD, 18));
		
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
		
		// painel Pallet 
		panelPallet = new JPanel();
		panelPallet.setLayout(null);
		titlePallet.setTitleFont( new Font("Tahoma", Font.PLAIN, 18) );
		titleContador.setTitleFont( new Font("Tahoma", Font.PLAIN, 18) ); 
		panelPallet.setBorder( titlePallet );		
		panelPallet.setBounds(365, 69, 642, 127);
		panel.add(panelPallet);
		
		lblPalletAtual = new JLabel("Nr.");
		lblPalletAtual.setBounds(12, 31, 39, 29);
		panelPallet.add(lblPalletAtual);
		lblPalletAtual.setForeground(Color.BLACK);
		lblPalletAtual.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		lblPallet = new JLabel("------------");
		lblPallet.setBounds(60, 31, 113, 29);
		panelPallet.add(lblPallet);
		lblPallet.setForeground(Color.BLACK);
		lblPallet.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		lblQtdePallet = new JLabel("Qtde.");
		lblQtdePallet.setBounds(192, 31, 59, 29);
		panelPallet.add(lblQtdePallet);
		lblQtdePallet.setForeground(Color.BLACK);
		lblQtdePallet.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		lblPalletQty = new JLabel("------------");
		lblPalletQty.setBounds(250, 31, 129, 29);
		panelPallet.add(lblPalletQty);
		lblPalletQty.setForeground(Color.BLACK);
		lblPalletQty.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		lblCantoneira = new JLabel("Cantoneira");
		lblCantoneira.setForeground(Color.BLACK);
		lblCantoneira.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCantoneira.setBounds(370, 31, 100, 29);
		panelPallet.add(lblCantoneira);
				
		txtPalletCantoneira = new JTextField();
		txtPalletCantoneira.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//GuiGlobals.virtualKeyboard(null, txtPalletCantoneira, null, null);
				validAction(txtPalletCantoneira,"changeCantoneiraPallet");	
			}
		});
		txtPalletCantoneira.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				validRefresh(txtPalletCantoneira);
			}
		});
		txtPalletCantoneira.setText("0.0");
		txtPalletCantoneira.setHorizontalAlignment(SwingConstants.CENTER);
		txtPalletCantoneira.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtPalletCantoneira.setColumns(10);
		txtPalletCantoneira.setBounds(490, 26, 102, 40);
		panelPallet.add(txtPalletCantoneira);
		
		lblTara = new JLabel("Tara");
		lblTara.setForeground(Color.BLACK);
		lblTara.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTara.setBounds(12, 76, 60, 29);
		panelPallet.add(lblTara);
		
		txtPalletTare = new JTextField();
		txtPalletTare.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtPalletTare,"changeTaraPallet");
			}
		});
		txtPalletTare.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				validRefresh(txtPalletTare);
			}
		});
		txtPalletTare.setText("0.0");
		txtPalletTare.setHorizontalAlignment(SwingConstants.CENTER);
		txtPalletTare.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtPalletTare.setColumns(10);
		txtPalletTare.setBounds(60, 71, 102, 40);
		panelPallet.add(txtPalletTare);
		
		lblStrech = new JLabel("Strech");
		lblStrech.setForeground(Color.BLACK);
		lblStrech.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblStrech.setBounds(191, 78, 60, 29);
		panelPallet.add(lblStrech);
		
		txtPalletStrech = new JTextField();
		txtPalletStrech.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//GuiGlobals.virtualKeyboard(null, txtPalletStrech, null, null);
				validAction(txtPalletStrech,"changeStrechPallet");
			}
		});
		txtPalletStrech.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				validRefresh(txtPalletStrech);
			}
		});
		txtPalletStrech.setText("0.0");
		txtPalletStrech.setHorizontalAlignment(SwingConstants.CENTER);
		txtPalletStrech.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtPalletStrech.setColumns(10);
		txtPalletStrech.setBounds(250, 73, 102, 40);
		panelPallet.add(txtPalletStrech);
		
		lblRack = new JLabel("Rack");
		lblRack.setForeground(Color.BLACK);
		lblRack.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblRack.setBounds(400, 78, 60, 29);
		panelPallet.add(lblRack);
		
		txtPalletRack = new JTextField();
		txtPalletRack.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//GuiGlobals.virtualKeyboard(null, txtPalletRack, null, null);
				validAction(txtPalletRack,"changeRackPallet");
			}
		});
		txtPalletRack.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				validRefresh(txtPalletRack);
			}
		});
		txtPalletRack.setText("0.0");
		txtPalletRack.setHorizontalAlignment(SwingConstants.CENTER);
		txtPalletRack.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtPalletRack.setColumns(10);
		txtPalletRack.setBounds(490, 73, 102, 40);
		panelPallet.add(txtPalletRack);
		
		// CONFIRMAR SE AINDA É NECESSARIO ESTES LABELS
		
		lblTextoEtq = new JLabel("Quantidade: ");
		lblTextoEtq.setBounds(20, 31, 150, 29);
		panelPallet.add(lblTextoEtq);
		lblTextoEtq.setForeground(Color.BLACK);
		lblTextoEtq.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTextoEtq.setVisible(false);
		
		lblQtdeEtq = new JLabel("------------");
		lblQtdeEtq.setBounds(150, 31, 329, 29);
		panelPallet.add(lblQtdeEtq);
		lblQtdeEtq.setForeground(Color.BLACK);
		lblQtdeEtq.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblQtdeEtq.setVisible(false);
		
		// ************************************************* WJSP 09/06/2015
		// JLabels lado esquerdo -- embaixo do panel "Pesagem"
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
		lblData.setBounds(posicaoX, 465, largura, 52);  // seria 480 -> porem devido a quebra de linha <br> ficou 465
		add(lblData);	
		
		lblLote = new JLabel("Lote");
		lblLote.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblLote.setBounds(posicaoX, 540, largura, altura);
		add(lblLote);
		
		lblHowSell = new JLabel("");
		lblHowSell.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblHowSell.setBounds(posicaoX + largura + 260, 540, largura, altura);
		add(lblHowSell);
		
		//************************** WJSP 09/06/2015
		// JTextField das Labels lado esquerdo -- embaixo do panel "Pesagem"
		txtQtdPecas = new JTextField();
		txtQtdPecas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				/* WJSP 10/06/2015
				 * Permitir Qtd de Peças == 0 apenas se o 
				 * campo "Quantidade por embalagem" no cadastro de Produto  
				 * for igual a 0   
				 */		
				if(product != null) {
				
					validAction(txtQtdPecas, "changeQtdPecas");							
				
					if(txtQtdPecas.getText().equals("0") && (defaultQtdPecas != 0)){
						GuiGlobals.getMain().showMessage("Operação inválida, Produto não pode ter Qtd Peças igual a 0");
						txtQtdPecas.setText(String.valueOf(defaultQtdPecas));
						//product.setTargetQty(defaultQtdPecas);											
						qtdCaixas = 1;
						txtPesoLiquido.setText(Utils.formatWeight(product.getTargetWeight()));
					}
					else{
						
						int restoDiv = 0;	
						int qtdPecasTela = 1;
						int qtdPecasCadastro = 1;
						GuiGlobals.getMain().showMessage("");					
						
						if(product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT){											
													
							if(product.getTargetQty() == 0 ){
								qtdPecasCadastro = 1;
							}						
							else{							
								qtdPecasCadastro = product.getTargetQty();
							}
							
							if(!txtQtdPecas.getText().equals(("0"))){
								qtdPecasTela = getPrimaryQty();  // txtQtdPecas já tratado com ParseInt
							}
							
							if( qtdPecasTela >= qtdPecasCadastro){						
								restoDiv = qtdPecasTela % qtdPecasCadastro;												
								
								if(restoDiv != 0){									
									txtQtdPecas.setText(String.valueOf(defaultQtdPecas));							
									txtPesoLiquido.setText(Utils.formatWeight(product.getTargetWeight()));
									txtTaraTotal.setText(Utils.formatWeight(defaultTaraTotalCx));
									qtdCaixas = 1;
									GuiGlobals.getMain().showMessage("Operação inválida, Informe quantidades multiplas do Produto");
								}
								else{
									/**
									 * Regra estabelecida por Antonio em 03/11/2015
									 * antes: qtdCaixas = qtdPecasTela / qtdPecasCadastro;
									 * depois: 
									 */
									qtdCaixas = ( product.getTargetQty() == 0 ) ? 1 : ( qtdPecasTela / qtdPecasCadastro );
									txtPesoLiquido.setText(Utils.formatWeight(product.getTargetWeight() / qtdPecasCadastro * qtdPecasTela));
									txtTaraTotal.setText(Utils.formatWeight(product.getTareBox() +(product.getTareEmbala() * qtdPecasTela)));
								}
							}
							else{

								txtQtdPecas.setText(String.valueOf(defaultQtdPecas));					
								txtPesoLiquido.setText(Utils.formatWeight(product.getTargetWeight()));
								txtTaraTotal.setText(Utils.formatWeight(defaultTaraTotalCx));
								qtdCaixas = 1;
								GuiGlobals.getMain().showMessage("Operação inválida, Informe quantidades maiores para o Produto");
							}						
						}		
						else{
							
							txtTaraTotal.setText(Utils.formatWeight(product.getTareBox() +(product.getTareEmbala() * getPrimaryQty())));
							
						}
						
						lblHowSell.setText( packingService.rulesEtqHowSell(product, qtdCaixas, getPrimaryQty(), false) );
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
			public void mousePressed(MouseEvent e) {				
				validAction(txtQtdEtiquetas,"changeQtdEtiquetas");				
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
		txtPesoLiquido.setText("0.000");  // Alterado de '1' para '0.000' em 09/06/2015 -- WJSP 
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
				
		if ( !GuiGlobals.isPermitted( ComponentPermission.LOTE ) ) { // Verifica permissao para alteracao do lote
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
		
		btnFecharPallet = new JButton("Fechar Palete");
		btnFecharPallet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeCurrentPallet();
			}
		});
		btnFecharPallet.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Close folder.png")));
		btnFecharPallet.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnFecharPallet.setBounds(734, 226, 248, 63);
		btnFecharPallet.setEnabled(false);
		add(btnFecharPallet);
		
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
		
		packingService = new PackingService();
		
		enableStuff(false);
		
	}

	//===============================================================================================
	// Regras de negocio da tela 
	//===============================================================================================
	
	private Product product = null;
	private Pallet pallet = null;
	private PackingProcessWorker worker;
	private int btnStatus = 0; //0=confirm  1=cancel
	private double tareBox = 0;
	
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
	
	public void validAction(JTextComponent textComponent, String methodName){		
			GuiGlobals.isEnabledAction(textComponent,methodName,this);				
	}
	
	private void clearStuff() {
		product = null;
		pallet = null;
		tareBox = 0;
		
		qtdCaixas = 1;

		lblNomeProduto.setText("");
		lblPallet.setText("------------");
		lblStockStyle.setText("------------");
		lblWeighingStyle.setText("------------");
		lblVirtual.setText("");
		lblHowSell.setText("");
		lblPalletQty.setText("------------");
		txtQtdPecas.setText("0");
		txtQtdEtiquetas.setText("1");
		txtPesoLiquido.setText("0.000"); 		
		enableTaraTotal(true);
		showCurrentDay(); 
		showBatch(txtDay.getText()+ "/"+txtMonth.getText()+"/"+txtYear.getText()+" 00:00");
		txtPalletStrech.setText("0.0");
		txtPalletRack.setText("0.0");
		txtPalletCantoneira.setText("0.0");
		txtPalletTare.setText("0.0");

		progress = 0;
		qtdSolicitada = 1;
        refreshQtds();

	}
	
	private void showStuff() {
		
		if(product == null) {
			clearStuff();
			return;
		}		
		
		lblNomeProduto.setText(product.getDescription());	
		lblWeighingStyle.setText(product.getWeighingStyle().toString());
		btnEstornar.setEnabled(true);
		
//		Antonio pediu para tirar esta mensagem em 14-06-2016
//		if ( packingService.isViaLeitor( product ) ){
//			GuiGlobals.sleep(100);
//			GuiGlobals.showMessage(GuiGlobals.getBundle().getString("STR000010"));
//		}
		
		if(product.isFifoEnabled() && product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT) {
			lblStockStyle.setText(GuiGlobals.getBundle().getString("STR000011"));
			}
		else {
			lblStockStyle.setText(GuiGlobals.getBundle().getString("STR000012"));			
		}
		
		if ( product.isVirtual() ){
			lblVirtual.setText("VIRTUAL");
		}
		else{
			lblVirtual.setText("");
		}
		
		Etiqueta etiqueta = product.getLabelFileName();
		if ( etiqueta != null )
		{
			packingService.refreshEtiqueta(etiqueta);
			
			if (etiqueta.isBlockTara() )
			{
				enableTaraTotal(false);
			}
			else
			{
				enableTaraTotal(true);
			}
		}

		if(pallet == null || product.getPalletQty() == 0) {
			
			qtdSolicitada = 1;
			lblPalletAtual.setVisible(false);
			lblPallet.setVisible(false);
			lblQtdePallet.setVisible(false);
			lblPalletQty.setVisible(false);
			lblTara.setVisible(false);
			lblStrech.setVisible(false);
			lblCantoneira.setVisible(false);
			lblRack.setVisible(false);
			
			lblTextoEtq.setVisible(true);
			lblQtdeEtq.setVisible(true);
						
			txtPalletStrech.setVisible(false);
			txtPalletTare.setVisible(false);
			txtPalletCantoneira.setVisible(false);
			txtPalletRack.setVisible(false);
			
			panelPallet.setBorder( titleContador );
			btnFecharPallet.setEnabled(false);			
			
		}
		else {
			
			lblTextoEtq.setVisible(false);
			lblQtdeEtq.setVisible(false);
			
			lblPalletAtual.setVisible(true);
			lblPallet.setVisible(true);
			lblQtdePallet.setVisible(true);
			lblPalletQty.setVisible(true);
			lblTara.setVisible(true);
			lblStrech.setVisible(true);
			lblCantoneira.setVisible(true);
			lblRack.setVisible(true);

			txtPalletStrech.setVisible(true);
			txtPalletTare.setVisible(true);
			txtPalletCantoneira.setVisible(true);
			txtPalletRack.setVisible(true);
			
			lblPallet.setText(String.valueOf(pallet.getId()).replace(",", "."));
			
			lblPalletQty.setText(String.valueOf(pallet.getTotalEtq()) + "/" + String.valueOf(product.getPalletQty()));
						
			txtPalletCantoneira.setText(Utils.formatWeight(pallet.getTareCantoneira()));
			txtPalletTare.setText(Utils.formatWeight(pallet.getTare()));
			txtPalletStrech.setText(Utils.formatWeight(pallet.getStrech()));
			txtPalletRack.setText(Utils.formatWeight(pallet.getTareRack()));
			panelPallet.setBorder( titlePallet );			
						 
		}								
		
		if ( "0".equals( txtQtdPecas.getText().trim() )){
			txtQtdPecas.setText(String.valueOf(product.getTargetQty()));
			txtPesoLiquido.setText(Utils.formatWeight(product.getTargetWeight()));			
			txtTaraTotal.setText(Utils.formatWeight(defaultTaraTotalCx));
			
		}
			
		if(product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT){
			txtPesoLiquido.setText("0.000");
		}
		
		txtQtdEtiquetas.setText("1");
		txtQtdEtiquetas.setEnabled(product.isChangeQtyBoxEnabled());
		txtQtdPecas.setEnabled(product.isChangeQtyPecasEnabled());
		
		lblHowSell.setText( packingService.rulesEtqHowSell(product, qtdCaixas, getPrimaryQty(), false) );
		refreshQtds();
		isEnableObjects(true);
	}
	
	public void refreshQtds(){
		
		if ( pallet == null ){
		
			if ( progress == qtdSolicitada ) {
				lblQtdeEtq.setText( progress + "/" + qtdSolicitada + " [Faltam: " + ( qtdSolicitada - progress ) + "]" );				
			}		
			
			lblQtdeEtq.setText( progress + "/" + qtdSolicitada + " [Faltam: " + ( qtdSolicitada - progress ) + "]" );
			
		}else{
			
			if( !( qtdSolicitada > (product.getPalletQty()-pallet.getTotalEtq()) ) )
			{								
				txtQtdEtiquetas.setText(String.valueOf(qtdSolicitada));
			}							
			
			lblPalletQty.setText(String.valueOf(pallet.getTotalEtq()) + "/" + String.valueOf(product.getPalletQty()));
		}
		
		txtQtdEtiquetas.setText( String.valueOf( ( qtdSolicitada - progress ) ) );
	}
	
	private void listProducts() {
		
		product = packingService.selectProduct();	
		GuiGlobals.showMessage("", false);
		
		if ( product != null ){
			
			txtProductId.setText( product.getIdMasc() );
			searchProduct();
			
		}
		
	}
	
	public void searchProduct() {

		clearStuff();
				
		product = packingService.findProductByIdMasc( txtProductId.getText().trim() ); 
		
		if(product == null) 
		{
			GuiGlobals.showMessage(GuiGlobals.getBundle().getString("STR000027"));
			enableStuff(false);
		}
		else if(product.isBlocked()) 
		{
			GuiGlobals.showMessage("Produto está bloqueado");
			enableStuff(false);
		}
		else {
			
			GuiGlobals.closeDb();
			packingService.refreshProduct(product);
			pallet = packingService.getPalletByProductStocked(product, false);
			
			GuiGlobals.showMessage("", false);
			this.tareBox = product.getTareBox();
						
			enableStuff(true);
			
			defaultQtdPecas = product.getTargetQty(); // Quantidade de Peças Default do Cadastro de Produtos					
			defaultTaraTotalCx = Double.parseDouble( Utils.formatWeight(product.getTareTotal() ) );
			showStuff(); 
			
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
	
		GuiGlobals.showMessage("", false);
		if(btnStatus == 0){
			confirm();			
		}
		else{
			cancel();
		}
	}
	
	private void cancel() 
	{
		if(worker != null)
			worker.terminate();
		
		setBtnToConfirm();
		GuiGlobals.defaultCursor();
		showStuff();
	}
	
	private Date getManufactureDate(){
		return DateTimeUtils.validateDate(txtDay.getText(), txtMonth.getText(), txtYear.getText());
	}
	
	public void confirm() {
		
		String dun14 = null;
		int qtdEtqs = getQtdEtiquetas();
		qtyEtiquetasToPrint = qtdEtqs;
		
		if(pallet != null){
			
			boolean refreshPallet = false;
			
			if(qtdEtqs > (product.getPalletQty()-pallet.getTotalEtq())){
				GuiGlobals.showMessage("Operação Inválida, Faltam " + String.valueOf(product.getPalletQty()-pallet.getTotalEtq()) + " etiqueta(s) para encerrar pallet" );
				txtQtdEtiquetas.setText("1");
				return;
			}
			else{
				GuiGlobals.showMessage("", false);			
			}
			
			if ( pallet.getTotalEtq() == 0 ) 
			{
				/**
				 * Retirado o campo manufactureDate e expirationDate do banco de dados
				 * pois deve ser calculado conforme caixas existes e,
				 * como pode ser excluídos caixas, as datas podem ficar erradas, então,
				 * é melhor que sempre que seja feita a consulta de palete, esta informação 
				 * seja calculada em tempo de execução
				 */
//				pallet.setManufactureDate(getManufactureDate() );
//				pallet.setExpirationDate( product.getExpirationDate( getManufactureDate() ) );
				pallet.setBatch( txtLote.getText() );
				pallet.setHowSell(( ( lblHowSell.getText().trim().length() == 2 ) ? lblHowSell.getText().trim() : lblHowSell.getText().trim().substring(4, 6) ) );
				refreshPallet = true;
			}
			else 	{
				
				if ( pallet.getManufactureDate().compareTo( getManufactureDate() ) > 0  ) {
//					pallet.setManufactureDate( getManufactureDate() );				
					pallet.setBatch( txtLote.getText() );
					refreshPallet = true;
				}
			
//				if ( pallet.getExpirationDate().compareTo( product.getExpirationDate( getManufactureDate() ) ) > 0  ) {
//					pallet.setExpirationDate( product.getExpirationDate( getManufactureDate() ) );
//					refreshPallet = true;
//				}
				
				if ( pallet.getBatch() == null )
				{
					pallet.setBatch( txtLote.getText() );
				}
				
			}
			
			if ( refreshPallet ) packingService.updatePallet(pallet);
			
		}

		int primaryQty = Integer.parseInt(txtQtdPecas.getText());	
		
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
		packingData.setAction(PackingData.ACTION_PACK);
		packingData.setDate(date);
		packingData.setExpirationDate( product.getExpirationDate( getManufactureDate() ) );
		packingData.setDun14( dun14 );
		packingData.setInFifo( product.isFifoEnabled() );
		packingData.setBatch(txtLote.getText());
		packingData.setCompany(Setup.getCompany());
		packingData.setPartner(null);
		packingData.setPrimaryQty(primaryQty);			// Quantidade de Peças 
		packingData.setSecondaryQty(qtdCaixas); 		// Quantidade de Caixas 	
		packingData.setProduct(product);		

		if (  !isTaraTotalChange() ){
			
			packingData.setTaraCaixa( Double.parseDouble( txtTaraTotal.getText() ) );
			packingData.setTaraEmbalagem( 0 );
		}
		else{
			
			int primaryQtyAtual = Integer.parseInt( txtQtdPecas.getText().trim() );
			if ( ( product.getTargetQty() == 0 && product.getTareEmbala() == 0) || ( primaryQtyAtual != product.getTargetQty() && primaryQtyAtual == 0 ) ){
				packingData.setTaraEmbalagem( 0 );
				packingData.setTaraCaixa( Double.parseDouble( txtTaraTotal.getText() ) );
			}			
			else{
				packingData.setTaraEmbalagem( getTareEmbalagem() );
				packingData.setTaraCaixa(product.getTareBox());
			}
			
		}		
		packingData.setUser(GuiGlobals.getUserInfo().getUser());
		packingData.setTerminal(Setup.getTerminal());
		packingData.setEtqHowSell(lblHowSell.getText());
		packingData.setPallet(pallet);
		packingData.setSecondPrint(false);
		packingData.setChangedQty(primaryQty != product.getTargetQty());
		packingData.setTypeStock(TypeStock.EMBALAGEM);
		packingData.setObjPanel(this);
		packingData.setQtdEtiquetas(getQtdEtiquetas());
		packingData.setPrimaryQtyOperation( Integer.parseInt( txtQtdPecas.getText() ) );
		
		isEnableObjects(false);
		startService(packingData);
				
	}
	
	private double getTareEmbalagem() {		
		return ( product.getTareEmbala() * getPrimaryQty() );
	}

	private boolean isTaraTotalChange() {
		return txtTaraTotal.getText().trim().equals( Utils.formatWeight( ( product.getTareBox() + getTareEmbalagem() ) ).trim() );
	}

	private void startService(final PackingData packingData) {
		
		worker = new PackingProcessWorker(packingData,true);
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
		    public void propertyChange(final PropertyChangeEvent event) {
				
				switch (event.getPropertyName()) {
		        case "progress":
		        	break;
		        case "state":
		        	switch ((StateValue) event.getNewValue()) {
		        	case DONE:		        		
				        
		        		try 
		        		{
				        	final int count = worker.get();
				        	
				        	setBtnToConfirm();
				        	if(count == 1) {
				        					        		
					        	if(progress == qtdSolicitada ){ //&& product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT){				        		
					        		progress = 0;
					        	}
				    			
				    			pallet = packingService.getPalletByProductStocked(product, true);
				    			if ( pallet == null && qtyEtiquetasToPrint > 1)
				    				txtProductId.setText("");
				    							    			
				    			qtdSolicitada = 1;
				    			showStuff();
				    			
				        	}
				        	else if(count == 2){
				        		searchProduct();
				        	}				        	
				        	else
				        	{
				        		isEnableObjects(true);
				        	}
				        			
			    			
				        } catch (final CancellationException e) {
				        	e.printStackTrace();
				        } catch (final Exception e) {
				        	e.printStackTrace();
				        }
		        		GuiGlobals.defaultCursor();
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
		worker.execute();
		
	}

	public String getTitle() {
		return GuiGlobals.getBundle().getString("STR000007");
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
	
	private void refreshPalletTare() {
		if(pallet == null)
			return;
		
		double tare = 0;
		double strech = 0;
		double tareRack = 0;
		double tareCantoneira = 0;
		
		try {
			tare = Double.parseDouble(txtPalletTare.getText().replace(',', '.'));
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			return;
		}
		
		try {
			strech = Double.parseDouble(txtPalletStrech.getText().replace(',', '.'));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		try {
			tareRack = Double.parseDouble(txtPalletRack.getText().replace(',', '.'));		
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		try {
			tareCantoneira = Double.parseDouble(txtPalletCantoneira.getText().replace(',', '.'));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		pallet.setStrech(strech);
		pallet.setTare(tare);
		pallet.setTareRack(tareRack);
		pallet.setTareCantoneira(tareCantoneira);		
		
		packingService.updatePallet( pallet );	 
	}	
		
	public void validRefresh(JTextComponent textComponent ){
		
		if(!textComponent.getText().equals("0.0") ){
			refreshPalletTare();
		}
	}
	
	private void closeCurrentPallet() {
		if(pallet == null)
			return;
		
		if(pallet.getSecondaryQty() == 0) {
			GuiGlobals.showMessage("O pallet está vazio e não pode ser fechado");			
			return;
		}
		
		//valida a data de fabricacao
		Date date = getManufactureDate();
		if(date == null) {
			GuiGlobals.showMessage("A data de fabricação é inválida");
			return;			
		}

		GuiGlobals.waitCursor();

		final PackingData packingData = new PackingData();
		packingData.setAction(PackingData.ACTION_CLOSE_PALLET);
		packingData.setDate(date);
		packingData.setBatch(txtLote.getText());
		packingData.setCompany(Setup.getCompany());
		packingData.setPartner(null);
		packingData.setProduct(product);
		packingData.setUser(GuiGlobals.getUserInfo().getUser());
		packingData.setTerminal(Setup.getTerminal());
		packingData.setPallet(pallet); //este ï¿½ o pallet inicial. Se este packingData for para muitas caixas, 
									   //o PackingProcessWorker pode alterar este pallet
		
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
				        	final int count = worker.get();
				        	if(count > 0) {
				    			GuiGlobals.showMessage("Palete encerrado!");			

				    			//atualiza informacoes do novo pallet
//				    			pallet = GuiGlobals.getDaoFactory().getStockDao().getPalletFor(product, Setup.getTerminal(), Setup.getCompany());				    			
				    			
				    			/* WJSP 26/06/2015
				    			 * Alterado para chamada de função que filtra apenas itens STOCKED tabela Stock
				    			 */
				    			//pallet = packingService.getPalletByProduct( product );
				    			pallet = packingService.getPalletByProductStocked( product, true );
				    			if ( pallet == null )
				    			{
				    				txtProductId.setText("");
				    				product = null;
				    				enableStuff(false);
				    			}
				    			
				    			showStuff();
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
				        worker = null;
		    			GuiGlobals.defaultCursor();
				        break;
		        	case STARTED:
		        	case PENDING:
		        		break;
		      }
		      break;
		    }
		  }
		});
		worker.execute();						
	}
	
	private void reprint() {
		GuiGlobals.waitCursor();
		ReprintDlg dlg = new ReprintDlg(TypeStock.EMBALAGEM,true);
		dlg.searchLastLabels();
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);		
	}
	
	private void rollback() {
		
		boolean processOk = true;
		
		GuiGlobals.waitCursor();
		
		//pega a ultima caixa embalada neste terminal
		Stock stock = packingService.findLastStockRollBack( TypeStock.EMBALAGEM );
		
		if(stock == null) {			
			processOk = false;
		}
		
		if ( processOk ){
		
			RollbackDlg dlg = new RollbackDlg(stock, TypeStock.EMBALAGEM);
			
			dlg.setVisible(true);	// tela de Estorno de Embalagem 
			if(dlg.isConfirmed()) {
				
				packingService.rollBack(stock);				
				pallet = packingService.getPalletByProductStocked(product, false);				
				showStuff();
			}
			
		}
		
		GuiGlobals.defaultCursor();
		
	}
	
	/* WJSP 10/06/2015
	 * Modificado de private void para public void devido a chamada da função validAction
	 * getMethod enxerga apenas funções públicas
	 */	
	public void changeTare() {
		//double typedTare = Double.parseDouble(txtTaraTotal.getText());
		TareDlg dlg = new TareDlg(Double.parseDouble(txtTaraTotal.getText()),"Tara Total (Kg)");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {
			this.tareBox = dlg.getValue();
			txtTaraTotal.setText(Utils.formatWeight(tareBox));
		}
	}
	
	/* WJSP 30/06/2015
	 * Dialogs de Pallet igual ao Dialog de Tara Total
	 */	
	public void changeCantoneiraPallet(){
		TareDlg dlg = new TareDlg(Double.parseDouble(txtPalletCantoneira.getText()),"Cantoneira do Pallet (Kg)");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {			
			txtPalletCantoneira.setText(Utils.formatWeight(dlg.getValue()));
		}
		
	}
	
	private void enableTaraTotal( boolean enable ){
		
		if ( enable ){
			
			if ( product != null && product.getLabelFileName() != null && product.getLabelFileName().isBlockTara() ){
				enable = false;
			}
			
		}
		
		txtTaraTotal.setEnabled( enable );
		
	}
	
	public void changeTaraPallet(){
		TareDlg dlg = new TareDlg(Double.parseDouble(txtPalletTare.getText()),"Tara do Pallet (Kg)");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {			
			txtPalletTare.setText(Utils.formatWeight(dlg.getValue()));
		}
	}
	
	public void changeStrechPallet(){
		TareDlg dlg = new TareDlg(Double.parseDouble(txtPalletStrech.getText()),"Strech do Pallet (Kg)");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {			
			txtPalletStrech.setText(Utils.formatWeight(dlg.getValue()));
		}
	}
	
	public void changeRackPallet(){
		TareDlg dlg = new TareDlg(Double.parseDouble(txtPalletRack.getText()),"Rack do Pallet (Kg)");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {			
			txtPalletRack.setText(Utils.formatWeight(dlg.getValue()));
		}
	}
	
	
	public void changeQtdEtiquetas(){
		
		QuantityDlg dlg = new QuantityDlg(1,9999,getQtdEtiquetas(),"Quantidade de Etiquetas");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {				
			
			// 18/06/2015 -> confirmado com o Antonio, qtdsolicitada receberá a quantidade de etiqueta
			progress = 0;
			qtdSolicitada = dlg.getValue();
			
			if( pallet != null && ( qtdSolicitada > ( product.getPalletQty() - pallet.getTotalEtq()) ) ){
				GuiGlobals.showMessage("Operação Inválida, Faltam " + String.valueOf(product.getPalletQty()-pallet.getTotalEtq()) + " etiqueta(s) para encerrar pallet" );
				txtQtdEtiquetas.setText("1");
			}
			else{
				refreshQtds();
			}
				
		}
	}
	
	public void changeQtdPecas(){				
		QuantityDlg dlg = new QuantityDlg(0,999,getPrimaryQty(),"Quantidade de Peças"); // WJSP Alterado range de Qtd de Peças 0 - 9999 antes 1-9999
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {
			txtQtdPecas.setText(String.valueOf(dlg.getValue()));						
		}
	}
	
	// Habilita ou desabilita os componentes da tela de acordo com as validacoes
	private void enableStuff(boolean enable){
			
		txtPalletCantoneira.setEnabled(enable);
		txtPalletTare.setEnabled(enable);
		txtPalletStrech.setEnabled(enable);
		txtPalletRack.setEnabled(enable);
		
		if ( product != null ){
			txtQtdPecas.setEnabled(product.isChangeQtyPecasEnabled());
			txtQtdEtiquetas.setEnabled(product.isChangeQtyBoxEnabled());
		}
		else{
			txtQtdPecas.setEnabled(enable);
			txtQtdEtiquetas.setEnabled(enable);
		}		

		enableTaraTotal(enable);
		txtDay.setEnabled(enable);
		txtMonth.setEnabled(enable);
		txtYear.setEnabled(enable);
		btnConfirm.setEnabled(enable);				
		//btnFecharPallet.setEnabled(enable);
		
		if(!enable){
			product = null;
			txtProductId.setText(""); 
			
		}
		
	}
	
	private void isEnableObjects( boolean enable ){
		
		txtPalletCantoneira.setEnabled(enable);
		txtPalletTare.setEnabled(enable);
		txtPalletStrech.setEnabled(enable);
		txtPalletRack.setEnabled(enable);		
		if ( product != null ){
			txtQtdPecas.setEnabled(product.isChangeQtyPecasEnabled());
			txtQtdEtiquetas.setEnabled(product.isChangeQtyBoxEnabled());
			
		}
		else{
			txtQtdPecas.setEnabled(enable);
			txtQtdEtiquetas.setEnabled(enable);
		}
		
		enableTaraTotal(enable);
		txtDay.setEnabled(enable);
		txtMonth.setEnabled(enable);
		txtYear.setEnabled(enable);
		btnEstornar.setEnabled(enable);
		txtProductId.setEnabled(enable);
		btnSearchProd.setEnabled(enable);
		btnReimprimir.setEnabled(enable);
		btnFecharPallet.setEnabled(enable);
		btnSair.setEnabled(enable);
		
		if ( enable || product.getPalletQty() > 0 ) btnFecharPallet.setEnabled(enable);		
	}
	
	public int getProgress() {
		return progress;
	}


	public void setProgress(int progress) {			
		this.progress = progress;
	}
	
	@Override
	public void addProgress(int qtd) {			
		setProgress( this.progress + qtd);
	}
	
}
