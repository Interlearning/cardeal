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
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.filter.PartnerFilter;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.PackingData;
import br.com.cardeal.globals.PackingProcessWorker;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.model.ItensPalletVirtual;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.TypeOperationPalletVirtual;
import br.com.cardeal.table.TableItensPalletVirtualModel;
import br.com.cardeal.table.TypesPalletVirtualCellEditor;
import br.com.cardeal.table.TypesPalletVirtualCellRenderer;

public class PackingPanelPalletVirtual extends JPanel  implements OperationWindow 
{
	private static final long serialVersionUID = 1L;
	
	private JPanel panel;
	private JLabel lblCodFornecedor;
    private JLabel lblPesoPallet;
    private JLabel lblPesoStrech;
    private JLabel lblItensPallet;
    private JLabel lblTotal;
    private JLabel lblTotalPecas;
    private JLabel lblTotalLiq;
    private JLabel lblTotalBruto;
    private JLabel lblTotalBrutoComTaraTexto;
    private JLabel lblTotalBrutoComTaraValor;
    
    private JTextField txtCodFornecedor;
    private JTextField txtPesoPallet;
    private JTextField txtPesoStrech;
    private JTextField txtItensPallet1;
    private JTextField txtItensPallet2;
    private JTextField txtItensPallet3;
    private JTextField txtItensPallet4;
    private JTextField txtItensPallet5;
    private JTextField txtItensPallet6;
    private JTextField txtItensPallet7;
    private JTextField txtItensPallet8;
    private JTextField txtItensPallet9;
    private JTextField txtItensPallet10;
    private JTextField txtItensPallet11;
    private JTextField txtItensPallet12;
    private JTextField txtItensPallet13;
    private JTextField txtItensPallet14;
        
    private JButton btnSearch;
    private JButton btnSair;
    private JButton btnImprimir;
    private JButton btnLimpaTela;
    private JButton btnEnter;
    
    private JScrollPane jScrollPane;
    private JTable table;  
    private TableItensPalletVirtualModel tableModel;

    public PackingPanelPalletVirtual()
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
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 1020, 232);
		add(panel);
		panel.setLayout(null);
				
		lblCodFornecedor = new JLabel();
        txtCodFornecedor = new JTextField();		
        lblPesoPallet = new JLabel();
        txtPesoPallet = new JTextField();
		lblPesoStrech = new JLabel();		
        txtPesoStrech = new JTextField();
		lblItensPallet = new JLabel();
		
		btnSearch = new JButton();
		btnLimpaTela = new JButton("Limpar Dados");        
		btnImprimir = new JButton("Imprimir");		
		btnSair = new JButton();        		
        txtItensPallet1 = new JTextField();
        txtItensPallet2 = new JTextField();
        txtItensPallet3 = new JTextField();        
        txtItensPallet4 = new JTextField();        
        txtItensPallet5 = new JTextField();        
        txtItensPallet6 = new JTextField();        
        txtItensPallet7 = new JTextField();        
        txtItensPallet8 = new JTextField();        
        txtItensPallet9 = new JTextField();        
        txtItensPallet10 = new JTextField();       
        txtItensPallet11 = new JTextField();        
        txtItensPallet12 = new JTextField();        
        txtItensPallet13 = new JTextField();        
        txtItensPallet14 = new JTextField();
        
        table = new JTable();
        jScrollPane = new JScrollPane();
        
        lblTotal = new JLabel();
        lblTotalBrutoComTaraTexto = new JLabel();
        lblTotalBrutoComTaraValor = new JLabel();
		lblTotalPecas = new JLabel();
		lblTotalLiq = new JLabel();
		lblTotalBruto = new JLabel();
	}
	
	private void createComponents() 
	{
        lblCodFornecedor.setFont(new java.awt.Font("Tahoma", 0, 20));
        lblCodFornecedor.setText("Código Fornecedor");
        lblCodFornecedor.setBounds(17, 10, 176, 49);

        txtCodFornecedor.setText("");
		txtCodFornecedor.setFont(new java.awt.Font("Tahoma", 0, 24));
        txtCodFornecedor.setBounds(200, 18, 205, 38);
        txtCodFornecedor.setEditable(false);
                
		btnSearch.setIcon(new ImageIcon(ShipmentPanel.class.getResource("/32x32/Search.png")));
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnSearch.setBounds(410, 16, 50, 42);		
		btnSearch.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GuiGlobals.showMessage("", false);
				searchPartner();				
			}
		});		
        
		btnEnter = new JButton("ENTER");
        btnEnter.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent evt) 
			{
				validateWeightPesoPallet();
				validateWeightPesoStrech();
				refreshTotals();
			}
		});
		
        lblPesoPallet.setFont(new java.awt.Font("Tahoma", 0, 20));
        lblPesoPallet.setText("Peso do Palete");
        lblPesoPallet.setBounds(17, 60, 176, 49);

        txtPesoPallet.setText("0.000");
        txtPesoPallet.setFont(new java.awt.Font("Tahoma", 0, 24));
        txtPesoPallet.setBounds(200, 58, 205, 38);
        txtPesoPallet.setHorizontalAlignment(JTextField.RIGHT);
        txtPesoPallet.addMouseListener(new MouseAdapter() 
        {
        	@Override
			public void mousePressed(MouseEvent e) 
        	{
        		mousePressedForTxtPesoPallet();        		
			}
        });
        
		lblPesoStrech.setFont(new java.awt.Font("Tahoma", 0, 20));
        lblPesoStrech.setText("Peso do Strech");
        lblPesoStrech.setBounds(17, 100, 176, 49);
               
        txtPesoStrech.setText("0.000");
        txtPesoStrech.setFont(new java.awt.Font("Tahoma", 0, 24));
        txtPesoStrech.setBounds(200, 98, 205, 38);
        txtPesoStrech.setHorizontalAlignment(JTextField.RIGHT);
        txtPesoStrech.addMouseListener(new MouseAdapter() 
        {
        	@Override
			public void mousePressed(MouseEvent e) 
        	{
        		mousePressedForTxtPesoStrech();
			}
        });
        txtPesoStrech.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				refreshTotals();				
			}
		});
        
		lblItensPallet.setFont(new java.awt.Font("Tahoma", 0, 20));
        lblItensPallet.setText("Itens do Palete");
        lblItensPallet.setBounds(17, 150, 176, 49);
                       		
		btnLimpaTela = new JButton("Limpar Dados");
        btnLimpaTela.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnLimpaTela.setBounds(526, 18, 180, 103);
        btnLimpaTela.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent e) 
			{
				restartAplication();
			}
		});		
		
		btnImprimir = new JButton("Imprimir");
		btnImprimir.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Print.png")));
		btnImprimir.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnImprimir.setBounds(715, 18, 180, 103);
		btnImprimir.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if ( dataToPrintOk() )
				{
					printPalletVirtual();
				}
			}
		});
		
		btnSair.setBounds(904, 18, 90, 103);
		btnSair.setIcon(new ImageIcon(PackingPanel.class.getResource("/32x32/Exit.png")));
		btnSair.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{				
				GuiGlobals.getMain().menuPacking();
			}
		});
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 24));
		        
		txtItensPallet1.setText("");
        txtItensPallet1.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet1.setBounds(200, 148, 100, 38);
        txtItensPallet1.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(0,txtItensPallet1);        		
			}
        });                
        
        txtItensPallet2.setText("");
        txtItensPallet2.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet2.setBounds(310, 148, 100, 38);
        txtItensPallet2.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{				
        		updateHow(1,txtItensPallet2);  
			}
        });        
        
        txtItensPallet3.setText("");
        txtItensPallet3.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet3.setBounds(420, 148, 100, 38);
        txtItensPallet3.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{			
        		updateHow(2,txtItensPallet3);
			}
        });
        
        txtItensPallet4.setText("");
        txtItensPallet4.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet4.setBounds(530, 148, 100, 38);
        txtItensPallet4.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(3,txtItensPallet4);
			}
        });        
        
        txtItensPallet5.setText("");
        txtItensPallet5.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet5.setBounds(640, 148, 100, 38);
        txtItensPallet5.addMouseListener(new MouseAdapter() 
        {
        	@Override
         	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(4,txtItensPallet5);
			}
        });        
        
        txtItensPallet6.setText("");
        txtItensPallet6.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet6.setBounds(750, 148, 100, 38);
        txtItensPallet6.addMouseListener(new MouseAdapter() 
        {
        	@Override
           	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(5,txtItensPallet6);
    		}
        });        
        
        txtItensPallet7.setText("");
        txtItensPallet7.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet7.setBounds(860, 148, 100, 38);
        txtItensPallet7.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(6,txtItensPallet7);
			}
        });        
        
        txtItensPallet8.setText("");
        txtItensPallet8.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet8.setBounds(200, 188, 100, 38);
        txtItensPallet8.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(7,txtItensPallet8);
			}
        });        
        
        txtItensPallet9.setText("");
        txtItensPallet9.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet9.setBounds(310, 188, 100, 38);
        txtItensPallet9.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(8,txtItensPallet9);
			}
        });        
        
        txtItensPallet10.setText("");
        txtItensPallet10.setFont(new java.awt.Font("Tahoma", 0, 20)); 
        txtItensPallet10.setBounds(420, 188, 100, 38);
        txtItensPallet10.addMouseListener(new MouseAdapter() 
        {
        	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(9,txtItensPallet10);
			}
        });        
        
        txtItensPallet11.setText("");
        txtItensPallet11.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet11.setBounds(530, 188, 100, 38);
        txtItensPallet11.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(10,txtItensPallet11);
			}
        });        
        
        txtItensPallet12.setText("");
        txtItensPallet12.setFont(new java.awt.Font("Tahoma", 0, 20)); 
        txtItensPallet12.setBounds(640, 188, 100, 38);
        txtItensPallet12.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(11,txtItensPallet12);
			}
        });        
        
        txtItensPallet13.setText("");
        txtItensPallet13.setFont(new java.awt.Font("Tahoma", 0, 20)); 
        txtItensPallet13.setBounds(750, 188, 100, 38);
        txtItensPallet13.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mousePressed(MouseEvent e) 
        	{
        		updateHow(12,txtItensPallet13);
			}
        });
        
        txtItensPallet14.setText("");
        txtItensPallet14.setFont(new java.awt.Font("Tahoma", 0, 20));
        txtItensPallet14.setBounds(860, 188, 100, 38);        
        txtItensPallet14.addMouseListener(new MouseAdapter() 
        {
        	@Override
			public void mousePressed(MouseEvent e) 
        	{
        		updateHow(13,txtItensPallet14);
			}
        });
        
        jScrollPane.setBounds(1, 231, 1021, 318);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(50,0));
		
		table.getTableHeader().setReorderingAllowed(false);  // Bloqueia a reordenação de colunas (Trava em seus lugares)
        table.getTableHeader().setResizingAllowed(false);	 // Impede o redimensionamento do tamanho da coluna 
        
        table.addMouseListener(new MouseAdapter() 
        {
        	
        	@Override        	
        	public void mouseClicked(MouseEvent e) 
        	{        		           		        	
        		int linha = table.getSelectedRow();
        		int coluna = table.getSelectedColumn();
        		
        		
        		Object value = tableModel.getValueAt(linha, coluna);	        			        	
        		
        		switch(coluna)
        		{	        		
	        		case 2:   		        			
	        			TareDlg pesoCxDlg = new TareDlg(Float.parseFloat(value.toString()),"Peso da Caixa (Kg)");
	        			pesoCxDlg.setVisible(true);
	        			
	        			if(pesoCxDlg.isConfirmed()) {			
	        				value = Utils.formatWeight(pesoCxDlg.getValue()); // retorna Double
	        			} 
	        			else{
	        				return;
	        			}
	        			
	        			break;
	        		case 3:        			        			
	        			QuantityDlg dlg = new QuantityDlg(0,99999,(Integer)value,"Quantidade de Peças");
	        			dlg.setVisible(true);
	        			
	        			if(dlg.isConfirmed()) {
	        				value = dlg.getValue();
	        			}
	        			else{
	        				return;
	        			}
	        			
	        			break;    
	        		case 4:		        			
	        			TareDlg pesoLiqDlg = new TareDlg(Float.parseFloat(value.toString()),"Peso Liquido");
	        			pesoLiqDlg.setVisible(true);
	        			
	        			if(pesoLiqDlg.isConfirmed()) {			
	        				value = Utils.formatWeight(pesoLiqDlg.getValue()); // retorna Double
	        			} 
	        			else{
	        				return;
	        			}		   
	        			
	        			break;
	        		case 5:		        			
	        			TareDlg pesoBrtDlg = new TareDlg(Float.parseFloat(value.toString()),"Peso Bruto");
	        			pesoBrtDlg.setVisible(true);
	        			
	        			if(pesoBrtDlg.isConfirmed()) {			
	        				value = Utils.formatWeight(pesoBrtDlg.getValue()); // retorna Double
	        			} 
	        			else{
	        				return;
	        			}		     
	        			
	        			break;
        		}
        		        		
        		tableModel.setValueAt((Object)value, linha, coluna);
        		tableModel.calcWeightOfRow( tableModel.getDadosGrid().get( table.getSelectedRow() ) );
        			        		
        		refreshTotals();
	        		
        	}
		});        
        
        jScrollPane.setViewportView(table);
		clearTable();
		
		lblTotal.setFont(new Font("Tahoma", 0, 16));
		lblTotal.setText("Total");
		lblTotal.setBounds(17, 550, 176, 20);
        
        lblTotalPecas.setFont(new Font("Tahoma",0,16));
        lblTotalPecas.setText("0");
        lblTotalPecas.setHorizontalAlignment(JTextField.RIGHT);
        lblTotalPecas.setBounds(383, 550, 176, 20);
        
        lblTotalLiq.setFont(new Font("Tahoma",0,16));
        lblTotalLiq.setText("0.000");
        lblTotalLiq.setHorizontalAlignment(JTextField.RIGHT);
        lblTotalLiq.setBounds(678, 550, 176, 20);
        
        lblTotalBruto.setFont(new Font("Tahoma",0,16));
        lblTotalBruto.setText("0.000");
        lblTotalBruto.setHorizontalAlignment(JTextField.RIGHT);
        lblTotalBruto.setBounds(788, 550, 176, 20);
        
        lblTotalBrutoComTaraTexto.setFont(new Font("Tahoma", 0, 16));
        lblTotalBrutoComTaraTexto.setText("Total Geral Bruto:");
        lblTotalBrutoComTaraTexto.setBounds(17, 575, 176, 20);
        
        lblTotalBrutoComTaraValor.setFont(new Font("Tahoma", 0, 16));
        lblTotalBrutoComTaraValor.setText("0.000");
        lblTotalBrutoComTaraValor.setBounds(190, 575, 176, 20);
        
        GuiGlobals.showMessage("", false);

	}
	
	private boolean dataToPrintOk()
	{
		boolean isPrintOk = true;
		
		if ( txtCodFornecedor.getText().trim().isEmpty() )
		{
			GuiGlobals.showMessage("Código do fornecedor é obrigatório!");
			isPrintOk = false;
		}
		else
		{
			isPrintOk = false;
			List<ItensPalletVirtual> itens = tableModel.getDadosGrid();
			
			for( ItensPalletVirtual item: itens )
			{
				if ( !item.getItensPallet().isEmpty() && Double.parseDouble( item.getPesoBruto() ) > 0 && Double.parseDouble( item.getPesoLiq() ) > 0)
				{
					isPrintOk = true;
					break;
				}
			}
			
			if ( !isPrintOk )
			{
				GuiGlobals.showMessage("Não há itens para impressão da etiqueta!");
			}
		}
		
		return isPrintOk;
	}
	
	private void printPalletVirtual()
	{
		List<ItensPalletVirtual> itens = tableModel.getDadosGrid();
		Partner partner = GuiGlobals.getDaoFactory().getPartnerDao().findByIdExternal( txtCodFornecedor.getText().trim() );
		
		PackingData packingData = new PackingData();
		packingData.setAction( PackingData.ACTION_PALLLET_VIRTUAL );
		packingData.setItensPalletVirtual( itens );
		packingData.setTare( Double.parseDouble( txtPesoPallet.getText() ) ); // usado para a tara do palete
		packingData.setTaraEmbalagem( Double.parseDouble( txtPesoStrech.getText() ) ); // usado para a tara do strch / cantoneira		
		packingData.setPartner(partner);
		
		GuiGlobals.waitCursor();
		PackingProcessWorker worker = new PackingProcessWorker(packingData,true);
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
		    public void propertyChange(final PropertyChangeEvent event) {
				
				switch (event.getPropertyName()) {
		        case "progress":
		        	break;
		        case "state":
		        	switch ((StateValue) event.getNewValue()) {
		        	case DONE:		        		
				        
		        		GuiGlobals.showMessage("Operação Ok!");
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
		worker.execute();
	}
	
	private void clearTable() 
	{
		
		List<ItensPalletVirtual> itens = new ArrayList<ItensPalletVirtual>();
				
		for (int i = 0; i <= 13 ; i++) {
			
			ItensPalletVirtual item = new ItensPalletVirtual();
			
			item.setItensPallet("");
			item.setType( new TypeOperationPalletVirtual("Padrão") );
			item.setPesoCaixa("0.000");
			item.setQtde(0);
			item.setPesoLiq("0.000");
			item.setPesoBruto("0.000");
			item.setTotalLiq("0.000");
			item.setTotalBruto("0.000");
			
			itens.add( item ); 
		}
		
		lblTotalPecas.setText("0");
		lblTotalLiq.setText("0.000");
		lblTotalBruto.setText("0.000");
		lblTotalBrutoComTaraValor.setText("0.000");
		
		tableModel = new TableItensPalletVirtualModel(itens);
		
		List<TypeOperationPalletVirtual> listTypes = new ArrayList<>();
		listTypes.add(new TypeOperationPalletVirtual(TypeOperationPalletVirtual.TYPE_STANDARD));
		listTypes.add(new TypeOperationPalletVirtual(TypeOperationPalletVirtual.TYPE_VARIABLE));

		table.setModel(tableModel);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setDefaultRenderer(TypeOperationPalletVirtual.class, new TypesPalletVirtualCellRenderer());
		table.setDefaultEditor(TypeOperationPalletVirtual.class, new TypesPalletVirtualCellEditor(listTypes));
		tableModel.initializeLayout(table);	
		jScrollPane.getVerticalScrollBar().setValue(0); 
	}
	
	public void refreshTotals()
	{
		tableModel.totalizeValues();		
		lblTotalPecas.setText(String.valueOf(tableModel.getTotalPecas()));
		lblTotalLiq.setText(tableModel.getTotalLiq());
		lblTotalBruto.setText(tableModel.getTotalBruto());
		double totalGeral = Double.parseDouble( tableModel.getTotalBruto() ) + Double.parseDouble( txtPesoPallet.getText() ) + Double.parseDouble( txtPesoStrech.getText() );
		lblTotalBrutoComTaraValor.setText( NumberUtils.transform(NumberUtils.truncate(  totalGeral, 3), 0, 3, false, false) );
	}
	
	private void searchPartner() 
	{
		PartnerFilter filter = new PartnerFilter();		
		filter.setEnabledToShowBlocked(false);
		filter.setOnlyCodExternal(true);
		
		PartnerDlg dlg = new PartnerDlg( filter );
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);
		if( dlg.getSelectedFornecedor() != null ) 
		{
			txtCodFornecedor.setText( dlg.getSelectedFornecedor().getCodigoExterno() );			
		}
	}

	private void addComponetsOnDialog()
	{
		panel.add( lblCodFornecedor );
		panel.add( txtCodFornecedor );
		panel.add( lblPesoPallet );
		panel.add( txtPesoPallet );
		panel.add( lblPesoStrech );
		panel.add( txtPesoStrech );
		panel.add( lblItensPallet );
		panel.add( btnSearch );
		panel.add( btnLimpaTela );
		panel.add( btnImprimir );
		panel.add( btnSair );		
		panel.add( txtItensPallet1 );
		panel.add( txtItensPallet2 );
		panel.add( txtItensPallet3 );
		panel.add( txtItensPallet4 );
		panel.add( txtItensPallet5 );
		panel.add( txtItensPallet6 );
		panel.add( txtItensPallet7 );
		panel.add( txtItensPallet8 );
		panel.add( txtItensPallet9 );
		panel.add( txtItensPallet10 );
		panel.add( txtItensPallet11 );
		panel.add( txtItensPallet12 );
		panel.add( txtItensPallet13 );
		panel.add( txtItensPallet14 );
		add( jScrollPane );
		add( lblTotal );
        add( lblTotalPecas );
        add( lblTotalLiq );
        add( lblTotalBruto );
        add( lblTotalBrutoComTaraTexto );
        add( lblTotalBrutoComTaraValor );
	}
	
	public void updateHow(int line, JTextComponent txtField)
	{
		GuiGlobals.showMessage("", false);
		GuiGlobals.virtualKeyboard(null, txtField, "updateHowExec", this, line);
	}
	
	public void updateHowExec(int line, JTextComponent txtField)
	{
		boolean isItemEmpty = false;		
		List<ItensPalletVirtual> rowChange = tableModel.getDadosGrid();
		ItensPalletVirtual itemChange = rowChange.get(line);
		Product produto = null;
		
		if( !txtField.getText().isEmpty() )
		{
			produto = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( txtField.getText().trim() );		
			
			if ( produto != null )
			{
				
				if( produto.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT)
				{
					itemChange.setItensPallet( produto.getIdMasc() );
					itemChange.setType( new TypeOperationPalletVirtual(TypeOperationPalletVirtual.TYPE_STANDARD));	
					itemChange.setPesoCaixa(Utils.formatWeight( produto.getTareBox()));
					itemChange.setQtde( 1);					
					itemChange.setPesoLiq(Utils.formatWeight( produto.getTargetWeight() ));				
					itemChange.setPesoBruto(Utils.formatWeight( produto.getTargetWeight() + produto.getTareBox()));
					tableModel.calcWeightOfRow( itemChange );													
				}
				else
				{
					isItemEmpty = true;			
				}
					
			}
			else
			{
				GuiGlobals.getMain().showMessage("Produto [" + txtField.getText().trim() + "] não encontrado!");
			}
		}
		else
		{
			isItemEmpty = true;
		}
		
		if ( isItemEmpty )
		{
			if ( produto != null )
			{
				itemChange.setItensPallet( produto.getIdMasc() );
			}
			else
			{
				itemChange.setItensPallet( "" );
			}
			itemChange.setType(new TypeOperationPalletVirtual(TypeOperationPalletVirtual.TYPE_VARIABLE));				
			itemChange.setPesoCaixa("0.000");
			itemChange.setQtde(0);
			itemChange.setPesoLiq("0.000");
			itemChange.setPesoBruto("0.000");
			itemChange.setTotalLiq("0.000");
			itemChange.setTotalBruto("0.000");
		}
		 					
		refreshTotals();
		rowChange.set(line, itemChange);
		table.changeSelection(line, 0, false, false); // SetFocus na linha inserida 
		table.repaint();
	}

	private void restartAplication() 
	{		
		createComponents();				
	}

	private void mousePressedForTxtPesoPallet() 
	{
		GuiGlobals.virtualKeyboardAction(null, txtPesoPallet, null, this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter, null);
	}
	
	private void mousePressedForTxtPesoStrech() 
	{
		GuiGlobals.virtualKeyboardAction(null, txtPesoStrech, null, this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter, null);
	}
	
	public void validateWeightPesoPallet() 
	{
		validateWeight( txtPesoPallet );
	}
	
	public void validateWeightPesoStrech() 
	{
		validateWeight( txtPesoStrech );
	}
	
	private void validateWeight( JTextField txtCurrent) 
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
	
	@Override
	public String getTitle() 
	{
		return "Palete Virtual";
	}

	@Override
	public JPanel getPanel() 
	{
		return this;
	}

	@Override
	public String getInitialMessage() 
	{
		return "";
	}

	@Override
	public void start() {}
	
}