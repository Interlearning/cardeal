package br.com.cardeal.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.model.ItensShipment;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Unit;
import br.com.cardeal.services.ShipmentService;

public class ComplementoItemDlg extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();	
	private JTextField txtValueQuantidade;
	private JButton btnConfirmar;
	private JButton btnCancelar;
	private JLabel lblCodigoProduto;
	private JLabel lblLabelProduto;
	private JLabel lblQuantidade;	
	private JLabel lblPeso;
	private JTextField txtValueWeight;	
	private boolean confirmed = false;
	private ItensShipment itemSelected;
	private int fieldSelected;
	private JButton btnEnter1;
	private JButton btnEnter2;
	private ShipmentPanel dlgPrincipal;
	private Unit unitItemSelected;
	private ShipmentService shipmentService;
	
	public ComplementoItemDlg getOuter() {
		return ComplementoItemDlg.this;
	}

	public ComplementoItemDlg( Product product )
	{
		initDlg( product, 0.0 );
	}
	
	/**
	 * Create the dialog.
	 */
	public ComplementoItemDlg( ItensShipment itemSelected, final ShipmentPanel dlgPrincipal, final ShipmentService shipmentService ) 
	{		
		this.itemSelected = itemSelected;		
		this.dlgPrincipal = dlgPrincipal;
		this.shipmentService = shipmentService;
		initDlg(itemSelected.getProduct(), itemSelected.getQtdExpedida() );
	}
	
	private void initDlg(Product product, double qty)
	{
		setTitle("Complementar Item");
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		if ( itemSelected != null )
			unitItemSelected = GuiGlobals.getDaoFactory().getUnitDao().find( itemSelected.getUnitId().substring(0, 2).toUpperCase() );

		setBounds(230, 70, 830, 529);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblLabelProduto = new JLabel("PRODUTO: ");
		lblLabelProduto.setHorizontalAlignment(SwingConstants.CENTER);
		lblLabelProduto.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblLabelProduto.setBounds(75, 49, 250, 29);
		contentPanel.add(lblLabelProduto);
		
		lblCodigoProduto = new JLabel(product.getIdMasc() );
		lblCodigoProduto.setHorizontalAlignment(SwingConstants.CENTER);
		lblCodigoProduto.setFont(new Font("Tahoma", Font.BOLD, 44));		
		lblCodigoProduto.setBounds(335, 29, 200, 60);
		contentPanel.add(lblCodigoProduto);

		btnEnter1 = new JButton("ENTER");
        btnEnter1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) 
			{
				trigger(fieldSelected, false);
			}
		});
        
		lblQuantidade = new JLabel("QUANTIDADE (" + ( (itemSelected != null) ? ( shipmentService.isUnitForKg( unitItemSelected ) ? "PC" : unitItemSelected.getId() ) : "" ) + ")");
		lblQuantidade.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuantidade.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblQuantidade.setBounds(02, 160, 400, 50);
		contentPanel.add(lblQuantidade);
		
		txtValueQuantidade = new JTextField();		
		txtValueQuantidade.setHorizontalAlignment(SwingConstants.CENTER);
		txtValueQuantidade.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtValueQuantidade.setColumns(10);
		txtValueQuantidade.setBounds(330, 150, 265, 65);
		txtValueQuantidade.selectAll();
		txtValueQuantidade.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) 
			{
				mousePressedForTxtValueQuantidade();	
			}
		});
		txtValueQuantidade.setText(Utils.formatWeight(0));
		contentPanel.add(txtValueQuantidade);
						
		btnEnter2 = new JButton("ENTER");
        btnEnter2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) 
			{
				Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( itemSelected.getProduct().getIdMasc() );
				itemSelected = dlgPrincipal.searchProductOnGrid( product ).clone();					
				if ( itemSelected != null ){
					trigger(0, true);
				}
			}
		});		
		
		lblPeso = new JLabel("PESO");
		lblPeso.setHorizontalAlignment(SwingConstants.CENTER);
		lblPeso.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblPeso.setBounds(10, 286, 400, 29);
		contentPanel.add(lblPeso);
		
		txtValueWeight = new JTextField();		
		txtValueWeight.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mousePressedForTxtValueWeight();
			}
		});
		
		txtValueWeight.setHorizontalAlignment(SwingConstants.CENTER);
		txtValueWeight.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtValueWeight.setColumns(10);
		txtValueWeight.setBounds(330, 270, 265, 65);
		txtValueWeight.selectAll();
		contentPanel.add(txtValueWeight);
		
		btnConfirmar = new JButton("Confirmar");		
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm();			
			}
		});
		btnConfirmar.setIcon(new ImageIcon(ComplementoItemDlg.class.getResource("/32x32/Apply.png")));
		btnConfirmar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnConfirmar.setBounds(230, 390, 175, 74);
		contentPanel.add(btnConfirmar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				setVisible(false);
			}
		});
		btnCancelar.setIcon(new ImageIcon(ComplementoItemDlg.class.getResource("/32x32/Cancel.png")));
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCancelar.setBounds(419, 390, 175, 74);
		contentPanel.add(btnCancelar);
		
		if ( itemSelected != null )
		{
			trigger(0, true);
		}
	}
	
	private void mousePressedForTxtValueQuantidade() 
	{	
		fieldSelected = 1;
		GuiGlobals.virtualKeyboardAction(ComplementoItemDlg.this, txtValueQuantidade, "validateQty", this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter1, null);
	}
	
	private void mousePressedForTxtValueWeight() {
		fieldSelected = 2;
		GuiGlobals.virtualKeyboardAction(ComplementoItemDlg.this, txtValueWeight, "validateWeight", this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter1, null);
	}
	
	private double getQtyToTrigger(double weightFromCalc) {
		
		double qtyFromCalc;
		boolean isProductStandardWeight = ( itemSelected.getProduct().isStandardWeight() && itemSelected.getProduct().getTargetWeight() > 0 );
		
		if ( isProductStandardWeight )
		{					
			if ( shipmentService.isUnitForEmb(itemSelected.getProduct(), unitItemSelected ) )
			{
				qtyFromCalc = weightFromCalc / itemSelected.getProduct().getNetWeight();
			}
			else
			{
				qtyFromCalc = weightFromCalc / itemSelected.getProduct().standartWeightUnit();
			} 					
		}
		else
		{
			qtyFromCalc = 0;
		}
		return qtyFromCalc;
	}

	private double getWeightToTrigger(double qtyFromCalc) {
		
		double weightFromCalc = 0.0;
		boolean isProductStandardWeight = ( itemSelected.getProduct().isStandardWeight() && itemSelected.getProduct().getTargetWeight() > 0 );
		
		if ( isProductStandardWeight )
		{
			if ( shipmentService.isUnitForEmb(itemSelected.getProduct(), unitItemSelected ) )
			{
				weightFromCalc = qtyFromCalc * itemSelected.getProduct().getNetWeight();
			}
			else if ( shipmentService.isUnitForPc( itemSelected.getProduct(), unitItemSelected ) )
			{
				weightFromCalc = qtyFromCalc * itemSelected.getProduct().standartWeightUnit();
			} 
			else if ( shipmentService.isUnitForKg( unitItemSelected ) )
			{
				//--------------------------------------------------
				// Quando kilo, a quantidade é na unidade de peças -
				//--------------------------------------------------
				weightFromCalc = qtyFromCalc * itemSelected.getProduct().standartWeightUnit();

//				if ( shipmentService.isUnitForEmb(itemSelected.getProduct(), itemSelected.getProduct().getUnitEmb() ) )
//				{
//					weightFromCalc = qtyFromCalc * itemSelected.getProduct().getNetWeight();
//				}
//				else if ( shipmentService.isUnitForPc( itemSelected.getProduct(), itemSelected.getProduct().getUnitEmb() ) )
//				{
//					weightFromCalc = qtyFromCalc * itemSelected.getProduct().standartWeightUnit();
//				} 
			}			
		}
		
		return weightFromCalc;
	}
	
	private boolean trigger(int opcao, boolean isInitDialog) 
	{				
		double qtyFromCalc = 0.0;
		double weightFromCalc = 0.0;
		boolean isRestart = false;
		boolean processOk = true;
		
		if ( itemSelected != null )
		{
		
			/**
			 * Se for apenas inicio de tela, carregar as quantidades conforme item posicionado
			 */
			if ( isInitDialog )
			{
				if ( shipmentService.isUnitForKg( unitItemSelected ) )
				{
					weightFromCalc = itemSelected.getQtdFaltante();
					qtyFromCalc = getQtyToTrigger(weightFromCalc);
				}
				else
				{
					qtyFromCalc = itemSelected.getQtdFaltante();				
					weightFromCalc = getWeightToTrigger(qtyFromCalc);
				}
				
			}
			else if ( itemSelected.getProduct().isStandardWeight() )
			{
				/**
				 * Se for gatilho do campo quantidade para o campo peso
				 */
				if ( opcao == 1 )
				{				
					validateQty();
					qtyFromCalc = Double.valueOf( txtValueQuantidade.getText() );
					
					if ( isValueMultiple(opcao, itemSelected, qtyFromCalc) )
					{
						weightFromCalc = getWeightToTrigger(qtyFromCalc);
					}
					else
					{
						isRestart = true;
						processOk = false;
						GuiGlobals.showMessageDlg(	"<html>Quantidade digitada não é múltiplo!" +
													"<br><br>Múltiplos permitidos: <br>" +
													"Caixa: 1 <br>" + 
													"Peça: " + itemSelected.getProduct().getTargetQty() + "<br>" +
													"Kilo: " + itemSelected.getProduct().getNetWeight() + "<br>" +
													"</html>");
					}
				}
				/**
				 * Se for gatilho do campo peso para o campo quantidade
				 */
				else if ( opcao == 2 )
				{
					weightFromCalc = Double.valueOf( txtValueWeight.getText().replace(",", ".") );
					if ( isValueMultiple(opcao, itemSelected, weightFromCalc) )
					{
						qtyFromCalc = getQtyToTrigger(weightFromCalc);
					}
					else
					{
						isRestart = true;
						processOk = false;
						GuiGlobals.showMessageDlg(	"<html>Quantidade digitada não é múltiplo!" + 
													"<br><br>Múltiplos permitidos: <br>" +
													"Caixa: 1 <br>" + 
													"Peça: " + itemSelected.getProduct().getTargetQty() + "<br>" +
													"Kilo: " + itemSelected.getProduct().getNetWeight() + "<br>" +
													"</html>");
					}
				}
				
			}
			else
			{
				weightFromCalc = Double.valueOf( txtValueWeight.getText().replace(",", ".") );
				qtyFromCalc = Double.valueOf( txtValueQuantidade.getText() );
			}
			
			if ( isRestart )
			{
				trigger(opcao, true);
			}
			else
			{
				txtValueWeight.setText( NumberUtils.transform(NumberUtils.truncate(weightFromCalc, 3), 14, 3, false, false) );		
				txtValueQuantidade.setText( NumberUtils.transform(NumberUtils.truncate(qtyFromCalc, 3), 14, 3, false, false) );
				validateQty();
				validateWeight();
			}
		
		}
		
		return processOk;
		
	}
		
	private boolean isValueMultiple(int opcao, ItensShipment itemSelected, double qtyFromCalc) {
		
		boolean processOk = true;
		double resultFromCalc;
		
		/**
		 * Se for gatilho do campo quantidade para o campo peso
		 */
		if ( opcao == 1 )
		{
			if ( !shipmentService.isUnitForEmb( itemSelected.getProduct(), unitItemSelected ) )
			{
				resultFromCalc = ( qtyFromCalc % itemSelected.getProduct().getTargetQty() );
				processOk = ( resultFromCalc == 0 );
			} 
			
		}
		/**
		 * Se for gatilho do campo peso para o campo quantidade
		 */
		else if ( opcao == 2 )
		{
			resultFromCalc = ( qtyFromCalc % itemSelected.getProduct().getNetWeight() );
			processOk = ( resultFromCalc == 0 );
		}
		return processOk;
	}
	
	public void validateQty() {
		
		double value = 0;
		
		try 
		{
			double newVal = NumberUtils.roundNumber(Double.parseDouble(txtValueQuantidade.getText().replace(",", ".")), 0);
			
			if(newVal < 0 || newVal > 999)
			{
				txtValueQuantidade.setText(String.valueOf(value).replace(".0", ""));
				GuiGlobals.showMessage("Quantidade ultrapassa 999 unidades!");
			}
			else
			{
				txtValueQuantidade.setText(String.valueOf(newVal).replace(".0", ""));
			}
		}
		catch(Exception e) {
			txtValueQuantidade.setText(String.valueOf(value).replace(".0", ""));
		}
	}
	
	public void validateWeight() {
		
		double value = 0;
		
		try {
			double newVal = Double.parseDouble(txtValueWeight.getText().replace(",", "."));
			if(newVal < 0 || newVal > 99999.999)
				txtValueWeight.setText(Utils.formatWeight(value));
			else
				txtValueWeight.setText(Utils.formatWeight(newVal));
		}
		catch(Exception e) {
			txtValueWeight.setText(Utils.formatWeight(value));
		}
	}

	public void confirm() 
	{
		if ( Integer.valueOf(txtValueQuantidade.getText().trim()) <= 0 )
		{
			confirmed = false;
			GuiGlobals.showMessage("Quantidade de peças deve ser maior que zero!");
		}
		if ( Double.valueOf(txtValueWeight.getText().trim()) <= 0 )
		{
			confirmed = false;
			GuiGlobals.showMessage("Peso deve ser maior que zero!");
		}
		else if( !trigger(1,false) )
		{
			confirmed = false;
		}
		else
		{
			confirmed = true;
		}
		
		if ( confirmed )
		{
			setVisible(false);	
		}
	}
	
	public int getQty() {
		return Integer.parseInt( txtValueQuantidade.getText() );
	}
	
	public double getNet() {
		return Double.parseDouble( txtValueWeight.getText() );
	}

	public boolean isConfirmed() {
		return confirmed;
	}


	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

}





