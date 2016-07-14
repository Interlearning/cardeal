package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.PackingData;
import br.com.cardeal.globals.PackingProcessWorker;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;

public class PackingPanelRepalletizar extends JPanel  implements OperationWindow {

	private static final long serialVersionUID = 2L;
	private int tamanhofonte = 22;
	private int posicaoX = 20;
	private int largura = 175;
	private int altura = 29;
	private int posicaoX2 = 220;
	private int largura2 = 200;
	private int altura2 = 50;
	private JPanel panel; 
	private JLabel lbl_Pallet;
	private JTextField txtPalletId;
	private JLabel lblNomeProduto;
	private JPanel panelPesagem;
	private JLabel lblPesagem;
	private JLabel lblWeighingStyle;
	private JLabel lblFIFO;
	private JLabel lblStockStyle;
	private JLabel lblVirtual;	
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
	private JLabel lblQtdeEtq;
	private JLabel lblTextoEtq;		
	private JLabel lblQtdPecas;
	private JTextField txtQtdPecas;	
	private JLabel lblQtdEtiquetas;
	private JTextField txtQtdEtiquetas;	
	private JLabel lblPesoLiquido;
	private JTextField txtPesoLiquido;	
	private JLabel lblTaraPallet;
	private JTextField txtTaraTotal;
	private JLabel lblData;
	private JTextField txtDay;
	private JTextField txtMonth;
	private JTextField txtYear;
	private JLabel lblBarra1;
	private JLabel lblBarra2;
	private JLabel lblDataClosed;	
	private JTextField txtDayClosed;
	private JTextField txtMonthClosed;
	private JTextField txtYearClosed;
	private JLabel lblBarra3;
	private JLabel lblBarra4;
	private JButton btnConfirm;
	private JButton btnSair;
	private Product product = null;
	private Pallet pallet = null;
	private PackingProcessWorker worker;
	private int btnStatus = 0;
	
	public PackingPanelRepalletizar() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
				
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 1018, 212);
		add(panel);
		panel.setLayout(null);
		
		// 1 linha de componentes
		lbl_Pallet = new JLabel("<html>Palete<br>+ Filial<html>");
		lbl_Pallet.setBounds(15, 10, 96, 55);
		panel.add(lbl_Pallet);
		lbl_Pallet.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
		txtPalletId = new JTextField();
		txtPalletId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearStuff();
			}
		});
		txtPalletId.setBounds(110, 5, 170, 51);
		panel.add(txtPalletId);
		txtPalletId.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				palletMousePressed();
			}
		});
		txtPalletId.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtPalletId.setColumns(10);
		
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
				validAction(txtPalletCantoneira,"changeCantoneiraPallet");	
			}
		});
		
		txtPalletCantoneira.setText("0");
		txtPalletCantoneira.setHorizontalAlignment(SwingConstants.CENTER);
		txtPalletCantoneira.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtPalletCantoneira.setColumns(10);
		txtPalletCantoneira.setBounds(490, 26, 102, 40);
		panelPallet.add(txtPalletCantoneira);

		lblTara = new JLabel("Tara");
		lblTara.setForeground(Color.BLACK);
		lblTara.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTara.setBounds(12, 76, 39, 29);
		panelPallet.add(lblTara); 
		
		txtPalletTare = new JTextField();
		txtPalletTare.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validAction(txtPalletTare,"changeTaraPallet");
			}
		});
		
		txtPalletTare.setText("0");
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
				validAction(txtPalletStrech,"changeStrechPallet");
			}
		});
		
		txtPalletStrech.setText("0");
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
				validAction(txtPalletRack,"changeRackPallet");
			}
		});
		
		txtPalletRack.setText("0");
		txtPalletRack.setHorizontalAlignment(SwingConstants.CENTER);
		txtPalletRack.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtPalletRack.setColumns(10);
		txtPalletRack.setBounds(490, 73, 102, 40);
		panelPallet.add(txtPalletRack);

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
		
		lblTaraPallet = new JLabel("Tara Total (kg)");
		lblTaraPallet.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte)); 
		lblTaraPallet.setBounds(posicaoX, 420, largura, altura);
		add(lblTaraPallet);
		
		lblData = new JLabel("Aberto em:");
		lblData.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblData.setBounds(posicaoX, 465, largura, 52);  // seria 480 -> porem devido a quebra de linha <br> ficou 465
		add(lblData);					
		
		lblDataClosed = new JLabel("Fechado em:");
		lblDataClosed.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblDataClosed.setBounds(posicaoX, 540, largura, altura);
		add(lblDataClosed);
				
		txtQtdPecas = new JTextField();
		
		txtQtdPecas.setHorizontalAlignment(SwingConstants.CENTER);
		txtQtdPecas.setText("0");
		txtQtdPecas.setFont(new Font("Tahoma", Font.PLAIN, 24));
		txtQtdPecas.setColumns(10);
		txtQtdPecas.setBounds(posicaoX2, 225, largura2, altura2);
		txtQtdPecas.setEnabled(false);
		add(txtQtdPecas);
		
		txtQtdEtiquetas = new JTextField();

		txtQtdEtiquetas.setHorizontalAlignment(SwingConstants.CENTER);
		txtQtdEtiquetas.setText("1");
		txtQtdEtiquetas.setFont(new Font("Tahoma",Font.PLAIN,tamanhofonte));
		txtQtdEtiquetas.setColumns(10);
		txtQtdEtiquetas.setBounds(posicaoX2, 285, largura2, altura2);
		txtQtdEtiquetas.setEnabled(false);
		add(txtQtdEtiquetas);
		
		txtPesoLiquido = new JTextField();
		txtPesoLiquido.setHorizontalAlignment(SwingConstants.CENTER);
		txtPesoLiquido.setText("0.000");   
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
		txtTaraTotal.setEnabled(false);
		add(txtTaraTotal);		
		
		txtDay = new JTextField();
		txtDay.setEnabled(true);		
		txtDay.setHorizontalAlignment(SwingConstants.CENTER);
		txtDay.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtDay.setColumns(10);
		txtDay.setBounds(posicaoX2, 465, 48, altura2);
		txtDay.setEnabled(false);
		add(txtDay);
		
		lblBarra1 = new JLabel("/");
		lblBarra1.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblBarra1.setBounds(posicaoX2+50 , 465, 15, altura2);
		add(lblBarra1); 
		
		txtMonth = new JTextField();
		txtMonth.setEnabled(true);		
		txtMonth.setHorizontalAlignment(SwingConstants.CENTER);
		txtMonth.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtMonth.setColumns(10);
		txtMonth.setBounds(posicaoX2+65, 465, 48, altura2);
		txtMonth.setEnabled(false);
		add(txtMonth);
		
		lblBarra2 = new JLabel("/");
		lblBarra2.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblBarra2.setBounds(posicaoX2+115 , 465, 15, altura2);
		add(lblBarra2);
		
		txtYear = new JTextField();
		txtYear.setEnabled(true);		
		txtYear.setHorizontalAlignment(SwingConstants.CENTER);
		txtYear.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtYear.setColumns(10);
		txtYear.setBounds(posicaoX2+130, 465,70, altura2);
		txtYear.setEnabled(false);
		add(txtYear);			
		
		txtDayClosed = new JTextField();
		txtDayClosed.setEnabled(true);		
		txtDayClosed.setHorizontalAlignment(SwingConstants.CENTER);
		txtDayClosed.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtDayClosed.setColumns(10);
		txtDayClosed.setBounds(posicaoX2, 525, 48, altura2);
		txtDayClosed.setEnabled(false);
		add(txtDayClosed);
		
		lblBarra3 = new JLabel("/");
		lblBarra3.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblBarra3.setBounds(posicaoX2+50 , 525, 15, altura2);
		add(lblBarra3); 
		
		txtMonthClosed = new JTextField();
		txtMonthClosed.setEnabled(true);		
		txtMonthClosed.setHorizontalAlignment(SwingConstants.CENTER);
		txtMonthClosed.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtMonthClosed.setColumns(10);
		txtMonthClosed.setBounds(posicaoX2+65, 525, 48, altura2);
		txtMonthClosed.setEnabled(false);
		add(txtMonthClosed);
		
		lblBarra4 = new JLabel("/");
		lblBarra4.setFont(new Font("Tahoma", Font.BOLD, tamanhofonte));
		lblBarra4.setBounds(posicaoX2+115 , 525, 15, altura2);
		add(lblBarra4);
		
		txtYearClosed = new JTextField();
		txtYearClosed.setEnabled(true);		
		txtYearClosed.setHorizontalAlignment(SwingConstants.CENTER);
		txtYearClosed.setFont(new Font("Tahoma", Font.PLAIN, tamanhofonte));
		txtYearClosed.setColumns(10);
		txtYearClosed.setBounds(posicaoX2+130, 525,70, altura2);
		txtYearClosed.setEnabled(false);
		add(txtYearClosed);		
		
		btnConfirm = new JButton("Confirmar");
		btnConfirm.setIcon(new ImageIcon(PackingPanelRepalletizar.class.getResource("/32x32/Apply.png")));
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnAction();
			}
		});
		btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 30));
		btnConfirm.setBounds(456, 225, 249, 285);
		add(btnConfirm);		
		
		btnSair = new JButton("Sair");
		btnSair.setBounds(734, 225, 249, 285); 
		add(btnSair);
		btnSair.setIcon(new ImageIcon(PackingPanelRepalletizar.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				GuiGlobals.getMain().menuPacking();
			}
		});	
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 30));		
		
		enableStuff(false);
	}
	
	public void productMousePressed() {
		GuiGlobals.virtualKeyboard(null, txtPalletId, "searchProduct", this);		
	}
	
	public void palletMousePressed(){
		GuiGlobals.virtualKeyboard(null, txtPalletId, "searchPallet", this);
	}
	
	private void clearStuff() {
		product = null;
		pallet = null;	

		lblNomeProduto.setText("");
		lblPallet.setText("------------");
		lblStockStyle.setText("------------");
		lblWeighingStyle.setText("------------");
		lblPalletQty.setText("------------");
		txtQtdPecas.setText("0");
		txtQtdEtiquetas.setText("1");
		txtPesoLiquido.setText("0.000"); 
		txtTaraTotal.setText("0.000");
		
		txtDay.setText("");
		txtMonth.setText("");
		txtYear.setText("");
		
		txtDayClosed.setText("");
		txtMonthClosed.setText("");
		txtYearClosed.setText("");
				
		txtPalletCantoneira.setText("0.0");
		txtPalletTare.setText("0.0");
		txtPalletStrech.setText("0.0");
		txtPalletRack.setText("0.0");		

	}
	
	private void showStuff() 
	{
		product = pallet.getProduct();  // Objeto pallet contem objeto product com a mesma estrutura da tabela Product
		
		lblPallet.setText(String.valueOf(pallet.getId())); 
		lblPalletQty.setText(String.valueOf(pallet.getSecondaryQty()));				
		
		lblNomeProduto.setText(product.getDescription());
		
		if(product.isFifoEnabled() && product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT) {
			lblStockStyle.setText("Sim");
			}
		else {
			lblStockStyle.setText("Não");			
		}
		
		lblWeighingStyle.setText(product.getWeighingStyle().toString()); 
		txtQtdPecas.setText(String.valueOf(pallet.getPrimaryQty()));
		txtQtdEtiquetas.setText(String.valueOf(pallet.getSecondaryQty()));
		txtPesoLiquido.setText(Utils.formatWeight(pallet.getNet()));				
		txtTaraTotal.setText(Utils.formatWeight(pallet.getTare() + pallet.getTareOfPacks() + pallet.getTareCantoneira() + pallet.getTareRack() + pallet.getStrech()));						
		txtPalletCantoneira.setText(Utils.formatWeight(pallet.getTareCantoneira()));
		txtPalletTare.setText(Utils.formatWeight(pallet.getTare()));
		txtPalletStrech.setText(Utils.formatWeight(pallet.getStrech()));
		txtPalletRack.setText(Utils.formatWeight(pallet.getTareRack()));	

		String[] dateParts = DateTimeUtils.formatDate(pallet.getOpenDate()).split("/");
		
		txtDay.setText(dateParts[0]);
		txtMonth.setText(dateParts[1]);
		txtYear.setText(dateParts[2]);
			
		dateParts = DateTimeUtils.formatDate(pallet.getCloseDate()).split("/");
		
		txtDayClosed.setText(dateParts[0]);
		txtMonthClosed.setText(dateParts[1]);
		txtYearClosed.setText(dateParts[2]);
		
	}
	
	public void searchPallet()
	{
		int palletId = 0;
		String filial = null;
		clearStuff();
		
		try 
		{
			String codbarScanner = txtPalletId.getText();
			palletId = Integer.parseInt(codbarScanner.substring(0, codbarScanner.length()-2));
			filial = codbarScanner.substring(codbarScanner.length()-2, codbarScanner.length());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(palletId == 0) 
		{
			GuiGlobals.showMessage("Código inválido de pallet.");
			return;
		}
				
		GuiGlobals.closeDb();
		pallet = GuiGlobals.getDaoFactory().getStockDao().findPalletStocked(palletId);
		
		if( pallet == null || pallet.getId() == 0 )
		{
			GuiGlobals.showMessage("Código inválido de pallet.");
			enableStuff(false);
		}
		// Antonio pediu para retirar esta validacao do sistema
//		else if ( !Setup.getCompany().getId().equals(filial) )
//		{
//			GuiGlobals.showMessage("Etiqueta não pertence a filial!");
//		}
		else if (pallet.getStatus() == PalletStatus.OPEN)
		{
			GuiGlobals.showMessage("Operação Inválida, Pallet não encerrado");
			enableStuff(false);
		}
		else if (pallet.getStocks().size() == 0 )
		{
			TelaPiscante tela = new TelaPiscante("O Palete [" + pallet.getIdFormatted() + "] não tem caixas em estoque. Foram excluídas ou já foram expedidas!");
			tela.setVisible(true);
		}
		else
		{
			GuiGlobals.showMessage("", false);
			enableStuff(true);
			showStuff();			
		}

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
	
	public void confirm() {
		if(product == null) {
			GuiGlobals.showMessage("Selecione um produto");
			return;
		}
		
		if ( !refreshPalletTare() ){
			return;
		}
		
		GuiGlobals.waitCursor();
		setBtnToCancel();
		
		final PackingData packingData = new PackingData();
		packingData.setAction(PackingData.ACTION_REPALLETIZAR);
		packingData.setCompany(Setup.getCompany());
		packingData.setProduct(product);
		packingData.setUser(GuiGlobals.getUserInfo().getUser());
		packingData.setPallet(pallet); 
		
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
				        	final int count = worker.get();
				        	if(count == packingData.getSecondaryQty() && product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT)
				        		waitZero();
				        	setBtnToConfirm();
			    			GuiGlobals.defaultCursor();
			    			
			    			txtPalletId.setText("");
			    			clearStuff();
			    			enableStuff(false);
			    			
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
	
	private void waitZero() {
		GuiGlobals.showMessage("Retire o produto da balança");
		
		if(GuiGlobals.getCurrentScale().isSimulated() ) {
			return;
		}
		
		while(GuiGlobals.getCurrentNet() > 0.5) {
			Thread.yield();
			GuiGlobals.sleep(100);
		}
	}

	public String getTitle() {
		return "Repalletizar";
	}

	public JPanel getPanel() {
		return this;
	}

	public String getInitialMessage() {
		return "Esta operação realiza movimentos de entrada em estoque";
	}
	
	public void start() {
		
	}
	
	private void setBtnToConfirm() {
		btnConfirm.setText("Confirmar");
		btnConfirm.setIcon(new ImageIcon(PackingPanelRepalletizar.class.getResource("/32x32/Apply.png")));
		btnStatus = 0;
	}
	
	private void setBtnToCancel() {
		btnConfirm.setText("Cancelar");
		btnConfirm.setIcon(new ImageIcon(PackingPanelRepalletizar.class.getResource("/32x32/Cancel.png")));
		btnStatus = 1;
	}
	
	private boolean refreshPalletTare() {
		
		if(pallet == null)
			return false;			
		
		double tareCantoneira = 0;
		double tare = 0;
		double strech = 0;
		double tareRack = 0;			
		double taraTotalPallet = 0;
		boolean validOk = true;
		
		GuiGlobals.showMessage("", false);
		
		try {
			tareCantoneira = Double.parseDouble(txtPalletCantoneira.getText().replace(',', '.'));
			pallet.setTareCantoneira(tareCantoneira);
			btnConfirm.setEnabled(true);
		}
		catch(Exception e) {
			GuiGlobals.showMessage("O valor da tara da Cantoneira é inválido");	
			btnConfirm.setEnabled(false);
			validOk = false;
		} 
		
		if ( validOk ){
			
			try {
				tare = Double.parseDouble(txtPalletTare.getText().replace(',', '.'));
				pallet.setTare(tare);
				btnConfirm.setEnabled(true);
			}
			catch(Exception e) {
				GuiGlobals.showMessage("O valor da tara do pallet é inválido");
				btnConfirm.setEnabled(false);
				validOk = false;
			} 
			
		}
		
		if ( validOk ){
			
			try {
				strech = Double.parseDouble(txtPalletStrech.getText().replace(',', '.'));
				pallet.setStrech(strech); 
				btnConfirm.setEnabled(true);
			}
			catch(Exception e) {
				GuiGlobals.showMessage("O peso do strech do pallet é inválido");	
				btnConfirm.setEnabled(false);
				validOk = false;
			} 
			
		}

		if ( validOk ){
			
			try {
				tareRack = Double.parseDouble(txtPalletRack.getText().replace(',', '.'));
				pallet.setTareRack(tareRack);
				btnConfirm.setEnabled(true);
			}
			catch(Exception e) {
				GuiGlobals.showMessage("O peso do Rack é inválido");
				btnConfirm.setEnabled(false);
				validOk = false;
				
			}  
			
		}
					
		taraTotalPallet = tareCantoneira + tare + strech + tareRack;  	
		txtTaraTotal.setText(Utils.formatWeight(taraTotalPallet));
		
		return validOk;
	}
		
	public void changeCantoneiraPallet(){
		TareDlg dlg = new TareDlg(Double.parseDouble(txtPalletCantoneira.getText()),"Cantoneira do Pallet (Kg)");
		dlg.setVisible(true);
		if(dlg.isConfirmed()) {			
			txtPalletCantoneira.setText(Utils.formatWeight(dlg.getValue()));
		}
		
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
	
	
	public void validAction(JTextComponent textComponent, String methodName){		
		GuiGlobals.isEnabledAction(textComponent,methodName,this);				
	}	
	
	private void enableStuff(boolean enable){
		
		txtPalletCantoneira.setEnabled(enable);
		txtPalletTare.setEnabled(enable);
		txtPalletStrech.setEnabled(enable);
		txtPalletRack.setEnabled(enable);
		btnConfirm.setEnabled(enable);					
		
		if(!enable){
			pallet = null;
			txtPalletId.setText(""); 			
		}							
	}

}
