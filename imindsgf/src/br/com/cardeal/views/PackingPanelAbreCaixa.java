package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.apache.commons.lang.StringUtils;

import br.com.cardeal.enums.Operation;
import br.com.cardeal.enums.StockStatus;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.ServerSetup;
import br.com.cardeal.model.Stock;
import br.com.cardeal.services.StockService;

public class PackingPanelAbreCaixa extends JPanel  implements OperationWindow {

	private static final long serialVersionUID = 1L;	
	private JLabel lblEtiqueta;
	private JTextField txtEtiqueta;
	private JButton btnSair;
	private JButton btnEnter1;
	private String codbarScanner = "";
	
	public PackingPanelAbreCaixa() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(0, 59, 1018, 600);
		setLayout(null);
		
		txtEtiqueta = new JTextField();
		txtEtiqueta.setBounds(366, 150, 288, 51);		
		txtEtiqueta.addKeyListener(new KeyAdapter() 
		{
			@SuppressWarnings("static-access")
			public void keyReleased(KeyEvent e)
			{  
				int key = e.getKeyCode();
	            String keyText;
	            
				if ( key == KeyEvent.VK_ENTER ) {
	            
					codbarScanner = codbarScanner.replaceAll("\\D", ""); //Troca tudo que não for dígito por vazio
					if ( codbarScanner.length() >= 10 )
					{
		            	openBox();
		            }
					txtEtiqueta.setText("");
					codbarScanner = "";				
				}
				else{
					keyText = e.getKeyText(key);
		            codbarScanner += keyText;
				}
				
			}
			
		 });
		
		txtEtiqueta.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				GuiGlobals.virtualKeyboardAction(null, txtEtiqueta, null, null, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter1, null);
			}
		});
		txtEtiqueta.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtEtiqueta.setColumns(10);
		add(txtEtiqueta);
		
		SwingUtilities.invokeLater(new Runnable() {  
		    public void run() {  
		    	txtEtiqueta.requestFocusInWindow();  
		    }  
		});  
		
		lblEtiqueta = new JLabel("Código da Etiqueta + Filial");
		lblEtiqueta.setBounds(370, 100, 296, 29);
		add(lblEtiqueta);
		lblEtiqueta.setFont(new Font("Tahoma", Font.PLAIN, 24));
	 
        btnEnter1 = new JButton("ENTER");
        btnEnter1.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				codbarScanner = txtEtiqueta.getText().trim();
				openBox();
				txtEtiqueta.setText("");
				codbarScanner = "";	
			}
		});
		
		btnSair = new JButton("Sair");
		btnSair.setBounds(366, 250, 288, 180);
		btnSair.setIcon(new ImageIcon(PackingPanelAbreCaixa.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				GuiGlobals.getMain().exitOperation();
			}
		});
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 24));
		add(btnSair);
		
		txtEtiqueta.setText("");
		txtEtiqueta.setSelectionStart(1);
		txtEtiqueta.requestFocus();
		
	}	
	
	
	public void openBox()
	{
		int etiqueta = 0;
		String filial = null;
		String message = null;
		try 
		{
			Stock stock = null;
			Pallet pallet = null;
			boolean existFilial = false;
			codbarScanner = codbarScanner.trim();
			if ( codbarScanner.length() >= 14 )
			{				
				stock = GuiGlobals.getDaoFactory().getStockDao().findBySSCC(codbarScanner.substring(2, codbarScanner.length()));
				pallet = GuiGlobals.getDaoFactory().getPalletDao().findBySSCC(codbarScanner.substring(2, codbarScanner.length()));
				existFilial = !( stock == null || stock.getStatus() == StockStatus.NON_STOCKED  );
				
				if ( pallet != null && pallet.getId() > 0 && stock != null && stock.getId() > 0)
				{
					int opcao = GuiGlobals.showMessageDlgYesNo("O código de etiqueta é de palete ?");
					if(opcao == JOptionPane.YES_OPTION)
					{
						message = "Não é permitido utilizar esta rotina para etiqueta de Palete!";
					}
				}
			}
			else if ( codbarScanner.length() > 8 )
			{			
				etiqueta = Integer.parseInt(codbarScanner.substring(0, codbarScanner.length()-2));
				filial = codbarScanner.substring(codbarScanner.length()-2, codbarScanner.length());
				stock = GuiGlobals.getDaoFactory().getStockDao().find(etiqueta);
				existFilial = !( stock == null || stock.getStatus() == StockStatus.NON_STOCKED );
				// Antonio pediu para retirar esta validacao do sistema
//				if ( stock != null && stock.getId() > 0 && !stock.getCompany().getId().equals(filial) )
//				{
//					message = "Etiqueta não pertence a filial!";
//				}
			}
			else
			{
				message = "<html><center>Formato de etiqueta CAIXA inválido.<br>Esta rotina não é permitida para etiqueta de palete!</html></center>";
			}
			
			if ( message == null )
			{
			
				//Validação de existencia da caixa em stock
				if( !existFilial || ( stock == null || ( stock.getId() == 0 ) ) ) 
				{				
					message = "<html><center>Etiqueta não encontrada!<br>" + txtEtiqueta.getText().trim() + "</center></html>";
				}
				/**
				 * Regras adicionadas por Antonio em 29-10-2015
				 */
				else if( stock.getProduct().isVirtual() )
				{
					GuiGlobals.showMessage("", false);
					GuiGlobals.showMessageDlg("Operação não executada! Motivo: Produto VIRTUAL.");
				}
				else if( stock.isInFifo() && stock.getPallet() == null )
				{
					GuiGlobals.showMessage("", false);
					GuiGlobals.showMessageDlg("Operação não executada! Motivo: Produto FIFO sem Pallet.");
				}			
				else
				{
					StockService service = new StockService();
					service.leaveStock(stock, Operation.STOCK_REMOVING, Setup.getTerminal());
					geraTxt(stock);
					message = "<html><center>Operação Ok!<br>" + stock.getIdFormatSerial().concat("-").concat(stock.getCompany().getId()) + "</center></html>";
				}	
			}
			
		}
		catch(Exception e){
			
			GuiGlobals.showMessageDlg(e.getMessage());
			e.printStackTrace();
			
		}
		
		if ( message != null )
		{
			GuiGlobals.showMessage(message);
			txtEtiqueta.setText("");
			codbarScanner = "";
		}
												
	}
		
	/**
	 * Geracao do arquivo ABRACX.TXT
	 * @param stock
	 */
	public void geraTxt(Stock stock) {		
		
  		String linha = "";
  		    		    		
  		linha =  stock.getIdFormatSerial()+ ";";																	// 1 número da caixa   
  		linha += StringUtils.leftPad(stock.getCompany().getId(),3, "0" )  + ";";									// 2 número da fabrica
  		linha += Setup.getTerminal() + ";";																			// 3 número do terminal que realizou a operação
 		linha += stock.getProduct().getIdMasc() + ";";																// 4 código do produto no interior da caixa
  		linha += StringUtils.leftPad( GuiGlobals.getUserInfo().getUser().getPassword(), 4, "0" ) + ";";				// 5 senha do operador	
  		linha += stock.getHowSelledFormat() + ";";	/*definido por Antonio em 08/12/2015*/								// 6 peso líquido da caixa (kg)
  		linha += DateTimeUtils.getDate(DateTimeUtils.now(), "yyMMdd") + ";";										// 7 data da operação
  		linha += DateTimeUtils.getDate(DateTimeUtils.now(), "HHmm");												// 8 hora e minuto da operação
  		
  		    		
  		//busca configuracoes do sistema
          ServerSetup serverConfig = GuiGlobals.getDaoFactory().getServerSetupDao().find();
  		
          if ( serverConfig != null && serverConfig.getStoreDirectory() != null ) {
          	        
  			try {
  				FileWriter arq = new FileWriter(serverConfig.getStoreDirectory() + GuiGlobals.getSeparador() + "ABRECX.TXT",true); // caminho do arquivo -> se não existir é criado o arquivo, caso exista é editado.			
  				PrintWriter gravarArq = new PrintWriter(arq);
  				
  				gravarArq.println(linha);
  				gravarArq.close(); 
  				arq.close(); 
  				
  			} catch (IOException e) {
  				e.printStackTrace();
  			}
          }
          else{
          	GuiGlobals.showMessageDlg("Arquivo ABRECX.TXT não será gerado pois não foi configurado no sistema. Para configurar, o administrador deve indicar o diretório em 'Manutenção->Configurações' !");
          }
		
	}
	
	@Override
	public String getTitle() {
		return "Abre Caixa";
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

}
