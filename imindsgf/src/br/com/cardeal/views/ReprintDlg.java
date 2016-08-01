package br.com.cardeal.views;

import java.awt.BorderLayout;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.EmptyBorder;

import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.PackingData;
import br.com.cardeal.globals.PackingProcessWorker;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Stock;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ReprintDlg extends JDialog 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Pallet pallet = null;
	private Stock stock = null;
	private TypeStock typeStock;
	
	/* WJSP 26/06/2015
	 * Variavel de controle para validações de reimpressão tela Embalagem e Excessão Peso Padrão 
	 */
	private boolean isPackingPanel = false;
	
	public TypeStock getTypeStock() {
		return typeStock;
	}

	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}

	private final JPanel contentPanel = new JPanel();
	private JTextField txtBoxId;
	private JTextField txtPalletId;
	private JButton btnConfirmPallet;
	private JButton btnConfirmBox;
	PackingProcessWorker worker = null;
	
	public ReprintDlg getOuter() {
		return ReprintDlg.this;
	}

	/**
	 * Create the dialog.
	 */
	public ReprintDlg(TypeStock typeStock) {
		
		/* WJSP 30/06/2015
		 * Set na variavel typeStock -> utilizado para retornar última embalagem feita pelo terminal  
		 */
		setTypeStock(typeStock);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				unload();
			}
		});
		
		initializeComponents();
		
	}
	
	public ReprintDlg(TypeStock typeStock, boolean isPackingPanel) {
		
		/* WJSP 30/06/2015
		 * Set na variavel typeStock -> utilizado para retornar última embalagem feita pelo terminal  
		 */
		setTypeStock(typeStock);		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				unload();
			}
		});
		
		this.isPackingPanel = isPackingPanel;
		
		initializeComponents();
		
	}
	
	
	/* WJSP 26/05/2015
	 * Tela Embalagem e Excessão peso Padrão tem validações diferentes para a impressão de etiqueta
	 * Construido outro método Construtor para indicar ao PackingProcessWorker qual validação seguir na hora da impressão 
	 */
	
	public void initializeComponents()
	{
		setTitle("Reimpress\u00E3o");
		setModalityType(ModalityType.DOCUMENT_MODAL);
		
		setBounds(100, 100, 601, 429);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtBoxId = new JTextField();
		txtBoxId.setText("0");
		txtBoxId.setHorizontalAlignment(SwingConstants.CENTER);
		txtBoxId.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtBoxId.setColumns(10);
		txtBoxId.setBounds(158, 36, 214, 55);
		txtBoxId.selectAll();
		txtBoxId.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				boxMousePressed();
			}
		});
		contentPanel.add(txtBoxId);		
		
		/*JLabel lblQuantidade = new JLabel("Caixa");
		lblQuantidade.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuantidade.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblQuantidade.setBounds(0, 62, 134, 29);
		contentPanel.add(lblQuantidade);*/
		
		btnConfirmBox = new JButton("Confirmar");
		btnConfirmBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printBox();
			}
		});
		btnConfirmBox.setIcon(new ImageIcon(ReprintDlg.class.getResource("/32x32/Apply.png")));
		btnConfirmBox.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnConfirmBox.setBounds(384, 35, 175, 56);
		contentPanel.add(btnConfirmBox);
		
		JButton btnCancelar = new JButton("Fechar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnCancelar.setIcon(new ImageIcon(ReprintDlg.class.getResource("/32x32/Exit.png")));
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCancelar.setBounds(209, 272, 175, 74);
		contentPanel.add(btnCancelar);
		
		JLabel lblEtiqueta = new JLabel("<html><center>Etiqueta<br>Embalagem</center></html>");
		lblEtiqueta.setHorizontalAlignment(SwingConstants.CENTER);
		lblEtiqueta.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblEtiqueta.setBounds(0, 34, 175, 60);
		contentPanel.add(lblEtiqueta);
		
		txtPalletId = new JTextField();
		txtPalletId.setText("0");
		txtPalletId.setHorizontalAlignment(SwingConstants.CENTER);
		txtPalletId.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtPalletId.setColumns(10);
		txtPalletId.setBounds(158, 139, 214, 55);
		contentPanel.add(txtPalletId);
		txtPalletId.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				palletMousePressed();
			}
		});
		
		btnConfirmPallet = new JButton("Confirmar");
		btnConfirmPallet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printPallet();
			}
		});
		btnConfirmPallet.setIcon(new ImageIcon(ReprintDlg.class.getResource("/32x32/Apply.png")));
		btnConfirmPallet.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnConfirmPallet.setBounds(384, 139, 175, 55);
		contentPanel.add(btnConfirmPallet);
		
		JLabel label = new JLabel("<html><center>Etiqueta<br>Pallet</center></html>");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 22));
		label.setBounds(0, 138, 175, 60); //setBounds(12, 139, 134, 29);
		contentPanel.add(label);
		
	/*	JLabel lblPallet = new JLabel("Pallet");
		lblPallet.setHorizontalAlignment(SwingConstants.CENTER);
		lblPallet.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblPallet.setBounds(12, 168, 134, 29);
		contentPanel.add(lblPallet);*/
		
		setModal(true);  // WJSP 11/06/2015 --  Jdialog sempre em foco -> para não permiir abrir mais de um por tela 
	}
	
	public void boxMousePressed() {
		GuiGlobals.virtualKeyboard(this, txtBoxId, null, this);		
	}
	
	public void palletMousePressed() {
		GuiGlobals.virtualKeyboard(this, txtPalletId, null, this);		
	}
	
	public void searchLastLabels() 
	{
		searchLastLabels(true, true);
	}
	
	public void searchLastLabels(boolean isEnablePallet, boolean isEnableBox) 
	{
		txtPalletId.setEnabled(isEnablePallet);
		btnConfirmPallet.setEnabled(isEnablePallet);
		txtBoxId.setEnabled(isEnableBox);
		btnConfirmBox.setEnabled(isEnableBox);
		
		if ( isEnablePallet )
		{
			pallet = GuiGlobals.getDaoFactory().getStockDao().findLastPalletStocked(Setup.getTerminal(), Setup.getCompany());
			if ( pallet != null )
			{
				txtPalletId.setText( String.valueOf( pallet.getId() ).trim() + Setup.getCompany().getId() );
			}
			else
			{
				txtPalletId.setText("0");
			}
		}

		GuiGlobals.refreshDaoFactory();
		
		if ( isEnablePallet )
		{
			stock = GuiGlobals.getDaoFactory().getStockDao().findLastStock(Setup.getTerminal(), Setup.getCompany(), getTypeStock());
			if ( stock != null ) 
			{
				txtBoxId.setText( String.valueOf( stock.getId() ).trim() + Setup.getCompany().getId() );
			}
			else
			{
				txtBoxId.setText("0");
			}
		}
	}
	
	private void unload() {		
		if(worker != null)
			worker.terminate();					
	}
	
	private void printBox() {
		
		long boxId = 0;
		try 
		{
			String codbarScanner = txtBoxId.getText();
			boxId = Long.parseLong(codbarScanner.substring(0, codbarScanner.length()-2));
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		if(boxId <= 0) 
		{
			GuiGlobals.showMessage("Embalagem inválida!");
			return;
		}
		
		//procura caixa na base
		GuiGlobals.refreshDaoFactory();
		stock = GuiGlobals.getDaoFactory().getStockDao().findStocked(boxId);
		if( stock == null || stock.getId() == 0 ) 
		{
			GuiGlobals.showMessage("Embalagem não encontrada no estoque!");
			return;
		}
		// Antonio pediu para retirar esta validacao do sistema
//		else if ( !Setup.getCompany().getId().equals(filial) )
//		{
//			GuiGlobals.showMessage("Etiqueta não pertence a filial!");
//			return;
//		}
		
		final PackingData packingData = new PackingData();
		packingData.setAction(PackingData.ACTION_PRINT_BOX);
		packingData.setDate(stock.getManufactureDate());
		packingData.setInFifo(stock.isInFifo());
		packingData.setBatch(stock.getBatch());
		packingData.setCompany(stock.getCompany());
		packingData.setNetStorage(stock.getNet());
		packingData.setNet(stock.getNetEtq());  // WJSP 25/06/2015
		packingData.setPartner(null);
		packingData.setPrimaryQty(stock.getPrimaryQty());
		packingData.setProduct(stock.getProduct());
		//packingData.setSecondaryQty(1);
		packingData.setSecondaryQty(stock.getSecondaryQty());	// WJSP 25/06/2015
		packingData.setTare(stock.getTare());
		packingData.setUser(stock.getUser());
		packingData.setTerminal(Setup.getTerminal());
		packingData.setSecondPrint(true);
		packingData.setChangedQty(stock.getPrimaryQty() != stock.getProduct().getTargetQty());
		
		if(isPackingPanel){
			worker = new PackingProcessWorker(packingData, stock,isPackingPanel);
		}
		else{
			worker = new PackingProcessWorker(packingData, stock);
		}
		
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
				        		GuiGlobals.showMessage("Impressão OK");
				        		setVisible(false);
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

	private void printPallet() 
	{
		int palletId = 0;
		
		try 
		{
			String codbarScanner = txtPalletId.getText();
			palletId = Integer.parseInt(codbarScanner.substring(0, codbarScanner.length()-2));
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		if(palletId <= 0) {
			GuiGlobals.showMessage("Pallet inválido!");
			return;
		}
				
		pallet = GuiGlobals.getDaoFactory().getStockDao().findPalletStocked(palletId);
		
		
		if( pallet == null || pallet.getId() == 0 ) 
		{
			GuiGlobals.showMessage("Pallet não encontrado no estoque!");
			return;
		}
		// Antonio pediu para retirar esta validacao do sistema
//		if( !Setup.getCompany().getId().equals(filial) ) 
//		{
//			GuiGlobals.showMessage("Etiqueta não pertence a filial!");
//			return;
//		}
		
		if(pallet.getStatus() == PalletStatus.OPEN) {
			GuiGlobals.showMessage("Pallet ainda não encerrado!");
			return;			
		}
		
		if(pallet.getStatus() == PalletStatus.DELETED) {
			GuiGlobals.showMessage("Pallet deletado!");
			return;			
		}
		
		if ( pallet.getStocks().size() == 0 )
		{
			TelaPiscante tela = new TelaPiscante("O Palete [" + pallet.getIdFormatted() + "] não tem caixas em estoque. Foram excluídas ou já foram expedidas!");
			tela.setVisible(true);
			return;
		}
		
		final PackingData packingData = new PackingData();
		packingData.setAction(PackingData.ACTION_PRINT_PALLET);
		packingData.setDate(pallet.getCloseDate());
		packingData.setNet(pallet.getNet());
		packingData.setPartner(null);
		packingData.setPrimaryQty(pallet.getPrimaryQty());
		packingData.setProduct(pallet.getProduct());
		packingData.setSecondaryQty(pallet.getSecondaryQty());
		packingData.setTare(pallet.getTare());
		packingData.setUser(GuiGlobals.getUserInfo().getUser());
		packingData.setTerminal(Setup.getTerminal());
		packingData.setSecondPrint(true);
		packingData.setChangedQty(false);
		
		if(isPackingPanel)
		{
			worker = new PackingProcessWorker(packingData, pallet,isPackingPanel);
		}
		else{
			worker = new PackingProcessWorker(packingData, pallet);
		}
		
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
				        	if(count > 0) 
				        	{
				        		GuiGlobals.showMessage("Impressão OK");
				        		setVisible(false);
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
}
