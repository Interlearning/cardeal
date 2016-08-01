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
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.PackingData;
import br.com.cardeal.globals.PackingProcessWorker;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Stock;
import br.com.cardeal.services.PackingService;

//PackingPanel2 - tela referente o processo de Estoque em Trânsito.
public class PackingPanel2 extends JPanel  implements OperationWindow, PackingPanelWindows {

private static final long serialVersionUID = 1L;
	
	private int progress = 0;
	int qtdCaixas;
	
	/* WJSP 15/06/2015
	 * Necessário esta variavel para as validações de quantidades multiplas
	 * Após pressionar o botão confirmar, ao voltar para a tela o objeto product está com o TargetQty 
	 * setado com o valor da quantidade de peças escolhidas em tela (devido operação weightPerPiece)
	 * então a validação seria feita em cima da quantidade de peças escolhidas anteriormente e não pelo cadastro do Produto 
	 */
	int defaultQtdPecas;
	
	double defaultTaraTotalCx;
	
	// WJSP 11/06/2015 -- Ordenação dos componentes em Tela para facilitar desenvolvimento e manutenção 
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
			
	// Componentes lado Esquerdo -- embaixo do panel "Pesagem"
	private JLabel lblQtdPecas;
	private JTextField txtQtdPecas;
	
	// WJSP 09/06/2015

	private JLabel lblQtdEtiquetas;
	private JTextField txtQtdEtiquetas;	
	
	/* WJSP 09/06/2015
	 * Alterado nome label de lblQtdCaixas para lblPesoLiquido 
	 * Alterado nome textField de txtQtdCaixas para txtPesoLiquido em 26/05/2015				  
	 */
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
	private JButton btnReimprimir;		
	private JButton btnEstornar;
	private JButton btnSair;

	private JLabel lblCheckRecebimento;
	private JLabel lblCheckDevolucao;
	private JButton btnRecebimento;
	private JButton btnDevolucao;
	private boolean isRecebimento = true;
	
	private PackingService packingService;

	public PackingPanel2() {

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
		
		btnRecebimento = new JButton("");
		btnRecebimento.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Ok.png")));
		btnRecebimento.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				btnRecebimento.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Ok.png")));
				btnDevolucao.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Close.png")));
				isRecebimento = true;
			}
		});
		btnRecebimento.setFont(new Font("Tahoma", Font.BOLD, 30));
		btnRecebimento.setBounds(370, 74, 50, 50);
		panel.add(btnRecebimento);
		
		lblCheckRecebimento = new JLabel("Recebimento");
		lblCheckRecebimento.setBounds(435, 77, 180, 40);
		lblCheckRecebimento.setForeground(Color.BLACK);
		lblCheckRecebimento.setFont(new Font("Tahoma",Font.BOLD,tamanhofonte));
		panel.add(lblCheckRecebimento);
		
		btnDevolucao = new JButton("");
		btnDevolucao.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Close.png")));
		btnDevolucao.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				btnDevolucao.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Ok.png")));
				btnRecebimento.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Close.png")));
				isRecebimento = false;
			}
		});
		btnDevolucao.setFont(new Font("Tahoma", Font.BOLD, 30));
		btnDevolucao.setBounds(370, 139, 50, 50);
		panel.add(btnDevolucao);
		
		lblCheckDevolucao = new JLabel("Devolução");
		lblCheckDevolucao.setBounds(435, 142, 180, 40);
		lblCheckDevolucao.setForeground(Color.BLACK);
		lblCheckDevolucao.setFont(new Font("Tahoma",Font.BOLD,tamanhofonte));
		panel.add(lblCheckDevolucao);

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
				if(product != null){
				
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
						
						/* WJSP 11/06/2015
						 * Validação para Peso Padrão 
						 */
						if(product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT){											
							
							/*
							 * Verifico os valores do Cadastro de Produto e Quantidade de Peças
							 * Pois se forem 0, não será possível realizar a divisão para obter Quantidade de Caixas
							 */
							if(product.getTargetQty() == 0 ){
								qtdPecasCadastro = 1;
							}						
							else{
								//qtdPecasCadastro = defaultQtdPecas; // product.getTargetQty();								
								qtdPecasCadastro = product.getTargetQty();
							}
							
							if(!txtQtdPecas.getText().equals(("0"))){
								qtdPecasTela = getPrimaryQty();  // txtQtdPecas já tratado com ParseInt
							}
							
							/* WJSP 23/06/2015
							 * Se a Quantidade de Peças escolhida na Interface for maior do que no Cadastro (Peso Padrão)
							 * Deve-se permite apenas quantidades multiplas (MOD == 0)
							 */
							if( qtdPecasTela >= qtdPecasCadastro){						
								restoDiv = qtdPecasTela % qtdPecasCadastro;												
								
								if(restoDiv != 0){									
									txtQtdPecas.setText(String.valueOf(defaultQtdPecas));
									//product.setTargetQty(defaultQtdPecas);									
									txtPesoLiquido.setText(Utils.formatWeight(product.getTargetWeight()));
									txtTaraTotal.setText(Utils.formatWeight(defaultTaraTotalCx));
									qtdCaixas = 1;
									GuiGlobals.getMain().showMessage("Operação inválida, Informe quantidades multiplas do Produto");
								}
								else{
									qtdCaixas = qtdPecasTela / qtdPecasCadastro;																																
								
									/* WJSP 23/06/2015
									 * Ajustado regra de totalizadores de Peso Liquido e Tara Cx
									 * 
									 * Peso Liquido -> (Peso Padrão/Qtd por Emabalagem (cadastro)) * Qtd de Peças escolhidas em tela 
									 * 
									 * Tara CX -> Tara da Caixa + (tara (pacote,saco,pote) * Qtd de Peças escolhidas em tela)
									 */
									
									//txtPesoLiquido.setText(String.valueOf(NumberUtils.truncate(product.weightPerPiece() * qtdPecasTela, 3)));																			
									//txtTaraTotal.setText(String.valueOf(NumberUtils.truncate(product.getTareBox() +(product.getTareEmbala() * qtdPecasTela),3)));
									txtPesoLiquido.setText(Utils.formatWeight(product.weightPerPiece() * qtdPecasTela));
									txtTaraTotal.setText(Utils.formatWeight(product.getTareBox() +(product.getTareEmbala() * qtdPecasTela)));								
								}
								//txtPesoLiquido.setText(Utils.formatWeight(product.getTargetWeight()));
							}
							else{
								/*qtdCaixas = 1;
								txtPesoLiquido.setText(txtQtdPecas.getText()); */
								txtQtdPecas.setText(String.valueOf(defaultQtdPecas));
								//product.setTargetQty(defaultQtdPecas);									
								txtPesoLiquido.setText(Utils.formatWeight(product.getTargetWeight()));
								txtTaraTotal.setText(Utils.formatWeight(defaultTaraTotalCx));
								qtdCaixas = 1;
								GuiGlobals.getMain().showMessage("Operação inválida, Informe quantidades maiores para o Produto");
							}						
						}		
						else{
							
							/* WJSP 25/06/2015
							 * Tratamento para Cadastro Peso Variavel Qtd = 0 para considerar o peso da Embalagem 
							 */						
							if(getPrimaryQty() == 0){
								txtTaraTotal.setText(String.valueOf(NumberUtils.truncate(product.getTareBox() +(product.getTareEmbala() * 1),3)));
							}
							else{
								txtTaraTotal.setText(String.valueOf(NumberUtils.truncate(product.getTareBox() +(product.getTareEmbala() * getPrimaryQty()),3)));
							}
							
						}
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
	
	private void clearStuff() {
		product = null;
		pallet = null;		

		qtdCaixas = 1;
		
		lblNomeProduto.setText("");
		lblStockStyle.setText("------------");
		lblWeighingStyle.setText("------------");
		txtQtdPecas.setText("0");
		txtQtdEtiquetas.setText("1");
		txtQtdPecas.setEnabled(false);
		txtQtdEtiquetas.setEnabled(false);
		txtPesoLiquido.setText("0.000"); 
		txtTaraTotal.setText("0.000");	
		
		progress = 0;
		showCurrentDay(); 
		showBatch(txtDay.getText()+ "/"+txtMonth.getText()+"/"+txtYear.getText()+" 00:00");
		
//		btnFifo.setText("Não");
//		btnFifo.setSelected(false);
//		btnFifo.setEnabled(false);
	}
	
	private void showStuff() {
		if(product == null) {
			clearStuff();
			return;
		}
		
		//GuiGlobals.getMain().showMessage("Produto " + product.getReducedDescription() + " encontrado");
		
		lblNomeProduto.setText(product.getDescription());
		
		if(product.isFifoEnabled() && product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT) {
			lblStockStyle.setText("Sim");
			}
		else {
			lblStockStyle.setText("Não");			
		}
		
		lblWeighingStyle.setText(product.getWeighingStyle().toString());
		
		
		if ( "0".equals( txtQtdPecas.getText().trim() )){
			txtQtdPecas.setText(String.valueOf(product.getTargetQty()));
			txtPesoLiquido.setText(Utils.formatWeight(product.getTargetWeight()));
			//txtPesoLiquido.setEnabled(true);
			
			//txtTaraTotal.setText(String.valueOf(defaultTaraTotalCx).replace(",", "."));
			txtTaraTotal.setText(Utils.formatWeight(defaultTaraTotalCx));
			
		}
		
		/*
		txtQtdPecas.setText(String.valueOf(product.getTargetQty()));
		txtQtdEtiquetas.setText("1");
		txtTaraTotal.setText(Utils.formatWeight(tareBox));
		*/		
		
		txtQtdEtiquetas.setEnabled(product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT);
		txtQtdPecas.setEnabled(product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT);
		
	}
	
	private void listProducts() {
		GuiGlobals.waitCursor();
		ProductsDlg dlg = new ProductsDlg();
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);
		if(dlg.getSelectedProduct() != null) {
			txtProductId.setText(String.valueOf(dlg.getSelectedProduct().getIdMasc()));
			searchProduct();
		}
	}
	
	public void searchProduct() {
		//limpa os campos da tela
		clearStuff();
		
	/*	//valida o codigo digitado
		int prodId = 0;
		try {
			prodId = Integer.parseInt(txtProductId.getText());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if(prodId == 0) {
			GuiGlobals.getMain().showMessage("Código inválido de produto.");
			return;
		}
		
		//Busca o produto na base de dados
		ProductDao dao = GuiGlobals.getDaoFactory().getProductDao();
		product = dao.findFilterBlocked( txtProductId.getText().trim() ); */
		
		product = packingService.findProductByIdMasc( txtProductId.getText().trim() ); 
		
		if(product == null) {
			GuiGlobals.getMain().showMessage("Produto não encontrado");
			enableStuff(false);
		}
		else if(product.isBlocked()) 
		{
			GuiGlobals.getMain().showMessage("Produto está bloqueado");
			enableStuff(false);
		}
		else {
/*          //Pesquisa qual o pallet aberto para este produto
			GuiGlobals.getDaoFactory().beginTransaction();
			pallet = GuiGlobals.getDaoFactory().getStockDao().getPalletFor(product, Setup.getTerminalId());
			GuiGlobals.getDaoFactory().commit();
			
			//carrega dados do produto em variaveis da tela
			this.tareBox = product.getTareBox();        */
			
			GuiGlobals.getMain().showMessage("");
			enableStuff(true);
			defaultQtdPecas = product.getTargetQty(); // Quantidade de Peças Default do Cadastro de Produtos
			
			if(defaultQtdPecas == 0){
				defaultTaraTotalCx = NumberUtils.truncate(product.getTareBox() +(product.getTareEmbala() * 1),3);  // Tara Padrão da Caixa
			}
			else{
				defaultTaraTotalCx = NumberUtils.truncate(product.getTareBox() +(product.getTareEmbala() * product.getTargetQty()),3);  // Tara Padrão da Caixa
			}	
			

			//mostra dados na tela
			showStuff();
			
			//Limpa mensagem de erro
			GuiGlobals.getMain().showMessage("");
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

		int value = Integer.parseInt(txtQtdEtiquetas.getText());
		return value;	
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
	
	private Date getManufactureDate()
	{
		return DateTimeUtils.validateDate(txtDay.getText(), txtMonth.getText(), txtYear.getText());
	}
	
	public void confirm() {
		
		String dun14 = null;
		int primaryQty = Integer.parseInt(txtQtdPecas.getText());

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
		packingData.setExpirationDate(  product.getExpirationDate( getManufactureDate() ) );		
		packingData.setDun14( dun14 );
		packingData.setInFifo( product.isFifoEnabled() );
		packingData.setBatch(txtLote.getText());
		packingData.setCompany(Setup.getCompany());
		packingData.setPartner(null);
		packingData.setPrimaryQty(primaryQty);
		packingData.setProduct(product);
		packingData.setSecondaryQty(qtdCaixas);
		packingData.setQtdEtiquetas(getQtdEtiquetas()); 
		packingData.setTaraCaixa(product.getTareBox());
		packingData.setTaraEmbalagem(product.getTareEmbala());
		packingData.setUser(GuiGlobals.getUserInfo().getUser());
		packingData.setTerminal(Setup.getTerminal());
		packingData.setSecondPrint(false);
		packingData.setChangedQty(primaryQty != product.getTargetQty());
		packingData.setTypeStock(TypeStock.TRANSITO);
		packingData.setAddOrRemove( ( isRecebimento ) ? "R" : "D" );
		packingData.setObjPanel(PackingPanel2.this);
		
		worker = new PackingProcessWorker(packingData);
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
		    public void propertyChange(final PropertyChangeEvent event) {
				switch (event.getPropertyName()) {
		        case "progress":
		        	//searchProgressBar.setIndeterminate(false);
		        	//searchProgressBar.setValue((Integer) event.getNewValue());
		        	break;
		        case "state":
		        	switch ((StateValue) event.getNewValue()) {
		        	case DONE:		        		
				        try {
				        	
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
				        					        	
			    			showStuff();
			    			
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
		worker.execute();
	}

	public String getTitle() {
		return "Estoque em Trânsito";
	}

	public JPanel getPanel() {
		return this;
	}

	public String getInitialMessage() {
		return "Esta operação realiza movimentos de entrada em estoque";
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
		btnConfirm.setIcon(new ImageIcon(PackingPanel2.class.getResource("/32x32/Apply.png")));
		//btnConfirm.setBackground(new Color(34, 139, 34));
		btnStatus = 0;
	}
	
	private void setBtnToCancel() {
		btnConfirm.setText("Cancelar");
		btnConfirm.setIcon(new ImageIcon(PackingPanel2.class.getResource("/32x32/Cancel.png")));
		//btnConfirm.setBackground(new Color(255, 0, 0));
		btnStatus = 1;
	}

	@SuppressWarnings("unused")
	private void refreshPalletTare() {
		if(pallet == null)
			return;
		
		double tare = 0;
		double strech = 0;
		
/*		try {
			tare = Double.parseDouble(txtPalletTare.getText().replace(',', '.'));			
		}
		catch(Exception e) {
			GuiGlobals.showMessage("O valor da tara do pallet é inválido");			
		} */
		
/*		try {
			strech = Double.parseDouble(txtPalletStrech.getText().replace(',', '.'));			
		}
		catch(Exception e) {
			GuiGlobals.showMessage("O peso do strech do pallet é inválido");			
		} */
		
		pallet.setStrech(strech);
		pallet.setTare(tare);
		GuiGlobals.getDaoFactory().beginTransaction();
		GuiGlobals.getDaoFactory().getPalletDao().update(pallet);
		GuiGlobals.getDaoFactory().commit();
	}
	
	@SuppressWarnings("unused")
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
		//packingData.setPallet(pallet); //este é o pallet inicial. Se este packingData for para muitas caixas, 
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
				    			//pallet = GuiGlobals.getDaoFactory().getStockDao().getPalletFor(product, Setup.getTerminal(), Setup.getCompany());
				    			showStuff();
				        	}
			    			
				        } catch (final CancellationException e) {
				        } catch (final Exception e) {
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
		ReprintDlg dlg = new ReprintDlg(TypeStock.TRANSITO,true); // WJSP 16/07/2015
		dlg.searchLastLabels(false,true);
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);		
	}
	
private void rollback() {
		
		boolean processOk = true;
		
		GuiGlobals.waitCursor();
		
		//pega a ultima caixa embalada neste terminal
		Stock stock = packingService.findLastStockRollBack( TypeStock.TRANSITO );
		
		if(stock == null) {			
			processOk = false;
		}
		
		if ( processOk ){
		
			RollbackDlg dlg = new RollbackDlg(stock, TypeStock.TRANSITO);
			
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
			txtTaraTotal.setText(Utils.formatWeight(dlg.getValue()));
		}
	}
		
	public void changeQtdEtiquetas(){
		QuantityDlg dlg = new QuantityDlg(1,9999,getQtdEtiquetas(),"Quantidade de Etiquetas");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {					
			txtQtdEtiquetas.setText(String.valueOf(dlg.getValue()));		
		}
	}
	
	public void changeQtdPecas(){				
		QuantityDlg dlg = new QuantityDlg(0,99999,getPrimaryQty(),"Quantidade de Peças"); // WJSP Alterado range de Qtd de Peças 0 - 9999 antes 1-9999
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {
			txtQtdPecas.setText(String.valueOf(dlg.getValue()));						
		}
	}
	
	

	public void validAction(JTextComponent textComponent, String methodName){		
			GuiGlobals.isEnabledAction(textComponent,methodName,this);				
	}
	
	// Habilita ou desabilita os componentes da tela de acordo com as validações 
	private void enableStuff(boolean enable){		
		
		txtQtdPecas.setEnabled(enable);
		txtQtdEtiquetas.setEnabled(enable);
		//txtPesoLiquido.setEnabled(enable);
		txtTaraTotal.setEnabled(enable);
		txtDay.setEnabled(enable);
		txtMonth.setEnabled(enable);
		txtYear.setEnabled(enable);
		btnConfirm.setEnabled(enable);		
		btnEstornar.setEnabled(enable);
		
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


	@Override
	public JTextField getTxtTaraTotal() {
		return txtTaraTotal;
	}


	@Override
	public JTextField getTxtQtdPecas() {
		return txtQtdPecas;
	}


	@Override
	public JTextField getTxtPesoLiquido() {
		return txtPesoLiquido;
	}
}
