package br.com.cardeal.views;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.services.MessageService;

public class Main {

	private static SplashJProgressBar splash;
	private JFrame frmMain;
	private JPanel panelTop;
	private JLabel lblOperation;
	private JLabel lblVersao;
	private JLabel lblEnterprise;
	private JLabel lblCompany;
	private JLabel lblTerminal;
	private JLabel lblOperator;
	private JPanel panel_1;
	private JPanel panelBody;
	private OperationWindow currentWindow = null;
	private JPanel panelMsg;
	private JLabel lblMsg;
	private JPanel panel;
	private JLabel lblLiquido;
	private JPanel panel_2;
	private JLabel lblTara;
	private JLabel lblTare;
	private JLabel lblNet;
	private JSlider sliderTare;
	private JSlider sliderNet;
	private JLabel lblScale;
	private JLabel lblDate;
	
	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		}
		catch(Exception e) {
			
		}
	}
		
	private static void logSplash(String msg) {
		SplashJProgressBar.getjLabelTextoDinamicoPlugins().setText(msg);
		sleep(200);		
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		splash = new SplashJProgressBar();					
		logSplash("Carregando sistema...");
		
		//EventQueue.invokeLater(new Runnable() {
			//public void run() {
				try {
					setLookAndFeel();

					logSplash("Criando módulo principal...");					
					Main window = new Main();					
					GuiGlobals.setMain(window);
					
					logSplash("Carregando Tela Inicial...");					
					window.frmMain.setVisible(true);
					
					splash.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			//}
		//});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		logSplash("Inicializando componentes...");					
		initialize();

		logSplash("Lendo configurações do Sistema...");					
		localInitializations();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMain = new JFrame();
		GuiGlobals.setMainFrame(frmMain);
		frmMain.setUndecorated(true);
		
		frmMain.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				startLogin();
			}
			@Override
			public void windowClosed(WindowEvent arg0) {
				close();
			}
		});
		frmMain.setResizable(false);
		frmMain.setTitle("Terminal de Opera\u00E7\u00F5es");
		frmMain.setFont(new Font("Tahoma", Font.PLAIN, 24));
		frmMain.setBounds(0, 0, 1024, 768);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMain.getContentPane().setLayout(null);
		
		panelTop = new JPanel();
		panelTop.setBackground(SystemColor.inactiveCaption);
		panelTop.setBounds(0, 0, 1024, 58);
		frmMain.getContentPane().add(panelTop);
		panelTop.setLayout(null);
		
		lblEnterprise = new JLabel("<Empresa>");
		lblEnterprise.setHorizontalAlignment(SwingConstants.LEFT);
		lblEnterprise.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEnterprise.setBounds(12, -2, 361, 34);
		panelTop.add(lblEnterprise);
		
		lblCompany = new JLabel("<Filial>");
		lblCompany.setHorizontalAlignment(SwingConstants.LEFT);
		lblCompany.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCompany.setBounds(12, 15, 161, 34);
		panelTop.add(lblCompany);
		
		lblOperation = new JLabel("<operation>");
		lblOperation.setHorizontalAlignment(SwingConstants.CENTER);
		lblOperation.setFont(new Font("Tahoma", Font.BOLD, 38));
		lblOperation.setBounds(160, 1, 665, 60);
		panelTop.add(lblOperation);		
		
		lblOperator = new JLabel("<operator>");
		lblOperator.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOperator.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOperator.setBounds(826, -2, 186, 34);
		panelTop.add(lblOperator);
		
		lblTerminal = new JLabel("<terminal>");
		lblTerminal.setHorizontalAlignment(SwingConstants.LEFT);
		lblTerminal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTerminal.setBounds(12, 35, 136, 24);
		panelTop.add(lblTerminal);
		
		lblVersao = new JLabel("Versão 1.0 - 21/07/2016");
		lblVersao.setHorizontalAlignment(SwingConstants.CENTER);
		lblVersao.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblVersao.setBounds(160, 0, 665, 10);
		panelTop.add(lblVersao);
		
		lblDate = new JLabel("<date>");
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(826, 25, 186, 34);
		panelTop.add(lblDate);
		
		panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaption);
		panel_1.setBounds(0, 662, 1024, 106);
		frmMain.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		panelMsg = new JPanel();
		panelMsg.setBorder(new LineBorder(new Color(192, 192, 192)));
		panelMsg.setForeground(new Color(255, 255, 0));
		panelMsg.setBackground(new Color(0, 0, 0));
		panelMsg.setBounds(0, 0, 582, 106);
		panel_1.add(panelMsg);
		panelMsg.setLayout(new CardLayout(0, 0));
		
		lblMsg = new JLabel("------");
		lblMsg.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMsg.setForeground(new Color(30, 144, 255));
		lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
		panelMsg.add(lblMsg, "name_23613456598893");
		
		panel = new JPanel();
		panel.setBackground(new Color(0, 0, 0));
		panel.setBorder(new LineBorder(new Color(192, 192, 192)));
		panel.setBounds(580, 0, 444, 106);
		panel_1.add(panel);
		panel.setLayout(null);
		
		lblLiquido = new JLabel("L\u00EDquido");
		lblLiquido.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLiquido.setForeground(new Color(30, 144, 255));
		lblLiquido.setBounds(7, 3, 86, 22);
		panel.add(lblLiquido);
		
		panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 0, 0));
		panel_2.setBorder(new LineBorder(new Color(192, 192, 192)));
		panel_2.setBounds(314, 0, 130, 106);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		lblTara = new JLabel("Tara");
		lblTara.setBounds(7, 3, 57, 22);
		lblTara.setForeground(new Color(30, 144, 255));
		lblTara.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_2.add(lblTara);
		
		lblTare = new JLabel("0 kg");
		lblTare.setForeground(new Color(30, 144, 255));
		lblTare.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblTare.setBounds(7, 31, 115, 41);
		panel_2.add(lblTare);
		
		sliderTare = new JSlider();
		sliderTare.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scaleTareSimulatorChange();
			}
		});
		sliderTare.setBounds(0, 88, 148, 15);
		panel_2.add(sliderTare);
		
		lblNet = new JLabel("0 kg");
		lblNet.setVerticalAlignment(SwingConstants.TOP);
		lblNet.setForeground(new Color(30, 144, 255));
		lblNet.setFont(new Font("Tahoma", Font.PLAIN, 44));
		lblNet.setBounds(7, 26, 305, 67);
		panel.add(lblNet);
		
		sliderNet = new JSlider();
		sliderNet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				scaleNetSimulatorChange();
			}
		});
		sliderNet.setBounds(2, 88, 310, 15);
		panel.add(sliderNet);
		
		lblScale = new JLabel("B1");
		lblScale.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				changeScale();
			}
		});
		lblScale.setHorizontalAlignment(SwingConstants.RIGHT);
		lblScale.setForeground(new Color(30, 144, 255));
		lblScale.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblScale.setBounds(250, 3, 54, 34);
		panel.add(lblScale);
		
		panelBody = new JPanel();
		panelBody.setBackground(SystemColor.info);
		panelBody.setBounds(0, 59, 1024, 600);
		frmMain.getContentPane().add(panelBody);
		panelBody.setLayout(null);
	}
	
	private static void setLookAndFeel() {
		//Set Look & Feel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}			
	}
	
	private void localInitializations() {
		GuiGlobals.waitCursor();
		try {
			//seta aparencia da aplicacao
			setLookAndFeel();
			
			//carrega setup da maquina
			logSplash("Lendo configuração deste terminal no servidor...");
			Setup.buildSetup();
			lblEnterprise.setText("Empresa " + GuiGlobals.getEnterprise().getName().trim() );
			lblCompany.setText("Filial " + Setup.getCompany().getId());
			lblTerminal.setText("Terminal " + Setup.getTerminal().getIdTerminal());
			
			
			//atualiza interface gráfica das balanças
			logSplash("Inicializando balanças...");			
			initializeScales();		
			
			//inicia timer que atualiza dados na tela
			timer = new Timer();
			timer.schedule(new TimerTask() {
	
	            @Override
	            public void run() {
	            	//atualiza o relogio
	            	updateClockAction();
	            	
	            	//atualiza peso da balanca corrente
	            	refreshScaleDisplay();
	            }
			}, 0, 400);
		}
		finally {
			GuiGlobals.defaultCursor();
		}
	}		

	private void updateClockAction() {
		lblDate.setText(DateTimeUtils.getCurrentTime());
	}
		
	//=======================================================================================
	// FUNÇÕES DE BALANÇA
	//=======================================================================================
	private int scaleIndex = -1;
	private Timer timer = null;	
	
	public int getScaleIndex() {
		return scaleIndex;
	}
	
	public void initializeScales() {
		sliderNet.setValue(0);
		sliderTare.setValue(0);
		if(GuiGlobals.getScales().size() == 0) {
			lblNet.setText("------");
			lblTare.setText("------");
			lblScale.setText("");
			sliderNet.setVisible(false);
			sliderTare.setVisible(false);
		}
		else {
			scaleIndex = 0;
			refreshScaleDisplay();
			sliderNet.setVisible(GuiGlobals.getScales().get(scaleIndex).isSimulated());
			sliderTare.setVisible(GuiGlobals.getScales().get(scaleIndex).isSimulated());
		}
	}
	
	public void refreshScaleDisplay() {		
		lblScale.setText("B" + String.valueOf(scaleIndex + 1));
		if ( GuiGlobals.getScales().size() > 0 )
		{
			lblNet.setText(GuiGlobals.getScales().get(scaleIndex).getFormattedNet());
			lblTare.setText(GuiGlobals.getScales().get(scaleIndex).getFormattedTare());
			if(GuiGlobals.getScales().get(scaleIndex).isSimulated()) {
				sliderNet.setValue((int) (sliderNet.getMaximum() * GuiGlobals.getScales().get(scaleIndex).getNet() / GuiGlobals.getScales().get(scaleIndex).getMaximum()));
				sliderTare.setValue((int) (sliderTare.getMaximum() * GuiGlobals.getScales().get(scaleIndex).getTare() / GuiGlobals.getScales().get(scaleIndex).getMaximum()));
			}		
		}
	}
	
	private void scaleNetSimulatorChange() {
	    JSlider source = sliderNet;
	    if (scaleIndex >= 0) {
	        int pos = (int)source.getValue();
	        double net = GuiGlobals.getScales().get(scaleIndex).getMaximum() * pos / 100;
	        GuiGlobals.getScales().get(scaleIndex).setNet(net);
			lblNet.setText(GuiGlobals.getScales().get(scaleIndex).getFormattedNet());
	    }
	}
	
	private void scaleTareSimulatorChange() {
	    JSlider source = sliderTare;
	    if (scaleIndex >= 0) {
	        int pos = (int)source.getValue();
	        double tare = GuiGlobals.getScales().get(scaleIndex).getMaximum() * pos / 100;
	        GuiGlobals.getScales().get(scaleIndex).setTare(tare);
			lblTare.setText(GuiGlobals.getScales().get(scaleIndex).getFormattedTare());
	    }
	}
	
	private void changeScale() {
		if(GuiGlobals.getScales().size() > scaleIndex + 1) {
			scaleIndex++;
		}
		else {
			if(GuiGlobals.getScales().size() > 0 && GuiGlobals.getScales().size() == scaleIndex + 1)
				scaleIndex = 0;
		}
		refreshScaleDisplay();
	}
	
	//=======================================================================================
	// FUNÇÕES DE NAVEGAÇÃO
	//=======================================================================================

	public OperationWindow getCurrentOperationWindow() {
		return currentWindow;
	}
	
	public void showWindow(OperationWindow window) {
		//Esconde a janela anterior
		if(currentWindow != null) {
			currentWindow.getPanel().setVisible(false);
			panelBody.remove(currentWindow.getPanel());
		}
		
		//mostra a nova janela
		currentWindow = window;
		panelBody.add(window.getPanel());
		this.lblOperation.setText( window.getTitle() );
		if(window.getInitialMessage() != null)
			this.showMessage(window.getInitialMessage());
		window.getPanel().setBounds(0, 0, panelBody.getWidth(), panelBody.getHeight());
		window.getPanel().setVisible(true);
		panelBody.invalidate();
		panelBody.repaint();
		
		//executa inicializacoes especificas da janela
		window.start();
	}
	
	public void dailyProdReport() {
		showWindow(new DailyProdReportPanel());		
	}
	
	public void palletsProdReport() {
		showWindow(new PalletProdReportPanel());		
	}

	public void reports() {
		showWindow(new ReportsPanel());		
	}

	public void packing() {
		showWindow(new PackingPanel());		
	}
	
	public void shipmentDevolution() {
		showWindow(new ShipmentDevolution());		
	}
	
	public void receiptPurshase() {
		showWindow(new ReceiptPurshase());			
	}
	
	public void menuPacking() {
		showWindow(new MenuPacking());		
	}
	
	public void packing2() {
		showWindow(new PackingPanel2());		
	}
	
	public void packing3() {
		showWindow(new PackingPanel3());		
	}
	
	public void packing4() {
		showWindow(new PackingPanelRepalletizar());		
	}
	
	public void packing5() {
		showWindow(new PackingPanelPalletVirtual());		
	}
	
	public void packing6() {
		showWindow(new PackingPanelAbreCaixa());		
	}
	
	/* -- WJSP 21/05/2015
	 * Alterado de packingFIFO para packingExcecaoPesoPadrao em 28/05/2015
	 */
	public void packingExcecaoPesoPadrao(){
		showWindow(new PackingPanelExcecaoPesoPadrao());
	}
	
	public void packingRecebimento() {
		showWindow(new MenuReceiptPanel());		
	}
	
	
	public void shipment() {
		showWindow(new ShipmentPanel());		
	}

	public void exitOperation() {
		showWindow(new MenuPanel());		
	}

	public void startLogin() {
		showWindow(new LoginPanel());
	}
	
	public void login() {
		this.lblOperator.setText(GuiGlobals.getUserInfo().getUser().getName());
		showWindow(new MenuPanel());
	}
	
	public void close() {
		if(timer != null)
			timer.cancel();
		GuiGlobals.closeDb();
		GuiGlobals.closeDevices();
		frmMain.setVisible(false);
		System.exit(0);
	}
	
	public void logoff() {
		GuiGlobals.getUserInfo().logout();
		this.lblOperator.setText("");
		startLogin();
	}
	
	public void showMessage(String message) 
	{
		lblMsg.setForeground(new Color(30, 144, 255));
		this.lblMsg.setText(message);
//		lblMsg.paintImmediately(lblMsg.bounds().x, lblMsg.bounds().y, lblMsg.bounds().width, lblMsg.bounds().height);	
	}

	public void showNet(String message) 
	{
		lblMsg.setForeground(new Color(30, 144, 255));
		this.lblMsg.setText(message);
	}

	public void showMessagePiscante(String message) 
	{
		new MessageService(lblMsg, message).start();
	}
	
	public JLabel getLblOperation() {
		return lblOperation;
	}

	public void setLblOperation(JLabel lblOperation) {
		this.lblOperation = lblOperation;
	}

	public JLabel getLblMsg() {
		return lblMsg;
	}

	public void setLblMsg(JLabel lblMsg) {
		this.lblMsg = lblMsg;
	}
	
}
