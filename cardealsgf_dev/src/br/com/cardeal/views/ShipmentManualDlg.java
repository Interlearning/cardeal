package br.com.cardeal.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;

import br.com.cardeal.enums.ShipmentOpcaoMenu;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.model.ItensShipment;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.StockTotal;
import br.com.cardeal.model.Unit;
import br.com.cardeal.services.ShipmentService;

public class ShipmentManualDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtIdMasc;	
	private JTextField txtValueQuantidade;	
	private JTextField txtValueWeight;
	private JButton btnConfirmar;
	private JButton btnCancelar;
	private JButton btnSearchCodigo;	
	private JLabel lblCodigo;
	private JLabel lblQuantidade;
	private JLabel lblPeso;
	private JRadioButton radioButtonBox;
	private JRadioButton radioButtonPallet;
	private boolean confirmed = false;
	private JButton btnEnter1;
	private JButton btnEnter2;
	private int fieldSelected;
	private ShipmentService shipmentService;
	private Unit unitItemSelected;
	private List<StockTotal> saldo;
	private ShipmentOpcaoMenu selectedOption;
	private ShipmentPanel dlgPrincipal;
	private ItensShipment itemSelected;
		
	/**
	 * Create the dialog.
	 */
	public ShipmentManualDlg( final ShipmentOpcaoMenu selectedOption, ItensShipment itemSelectedToClone, final ShipmentService shipmentService, final ShipmentPanel dlgPrincipal ) 
	{
		this.shipmentService = shipmentService;
		this.selectedOption = selectedOption;
		this.dlgPrincipal = dlgPrincipal;

		if ( itemSelectedToClone != null )
			this.itemSelected = itemSelectedToClone.clone();
				
		setTitle(selectedOption.getTitulo());
		setModalityType(ModalityType.APPLICATION_MODAL);

		setBounds(230, 70, 700, 529);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtIdMasc = new JTextField();
		if ( selectedOption == ShipmentOpcaoMenu.FIFO || selectedOption == ShipmentOpcaoMenu.MATERIA_PRIMA ) 
		{
			itemSelected = checkIsFifoOrMP();
			generateSaldo(selectedOption);
			
			if ( itemSelected != null )
				unitItemSelected = GuiGlobals.getDaoFactory().getUnitDao().find( itemSelected.getUnitId().substring(0, 2).toUpperCase() );
						
			lblCodigo = new JLabel("PRODUTO");
			lblCodigo.setBounds(10, 49, 400, 29);
			txtIdMasc.setBounds(330, 30, 265, 65);
			
			btnEnter2 = new JButton("ENTER");
	        btnEnter2.addActionListener(new ActionListener() 
	        {
				public void actionPerformed(ActionEvent evt) 
				{
					Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( txtIdMasc.getText() );
					itemSelected = dlgPrincipal.searchProductOnGrid( product ).clone();
					generateSaldo(selectedOption);
					if ( itemSelected != null )
					{
						unitItemSelected = GuiGlobals.getDaoFactory().getUnitDao().find( itemSelected.getUnitId().substring(0, 2).toUpperCase() );
						trigger(0, true);
					}
				}
			});
			
			btnSearchCodigo = new JButton("");
			btnSearchCodigo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					listProducts();
				}
			});
			btnSearchCodigo.setIcon(new ImageIcon(ShipmentManualDlg.class.getResource("/32x32/Search.png")));
			btnSearchCodigo.setFont(new Font("Tahoma", Font.BOLD, 18));
			btnSearchCodigo.setBounds(600, 30, 78, 65);
			contentPanel.add(btnSearchCodigo);
			
			lblQuantidade = new JLabel("<html><center>QUANTIDADE " + ( (itemSelected != null) ? ( shipmentService.isUnitForKg( unitItemSelected ) ? "(PC)" : "(" + unitItemSelected.getId().trim() + ")" ) : "" ) + "</center></html>");
			lblQuantidade.setHorizontalAlignment(SwingConstants.CENTER);
			lblQuantidade.setFont(new Font("Tahoma", Font.BOLD, 24));
			lblQuantidade.setBounds(02, 160, 400, 50);
			contentPanel.add(lblQuantidade);
			
			btnEnter1 = new JButton("ENTER");
	        btnEnter1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) 
				{
					trigger(fieldSelected, false);
				}
			});
			
			txtValueQuantidade = new JTextField();
			txtValueQuantidade.setEditable( ( selectedOption != ShipmentOpcaoMenu.MATERIA_PRIMA ) );
			txtValueQuantidade.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					mousePressedForTxtValueQuantidade();
				}
			});
			
			txtValueQuantidade.setHorizontalAlignment(SwingConstants.CENTER);
			txtValueQuantidade.setFont(new Font("Tahoma", Font.PLAIN, 40));
			txtValueQuantidade.setColumns(10);
			txtValueQuantidade.setBounds(330, 150, 265, 65);
			txtValueQuantidade.selectAll();			
			contentPanel.add(txtValueQuantidade);
			
			lblPeso = new JLabel("PESO");
			lblPeso.setHorizontalAlignment(SwingConstants.CENTER);
			lblPeso.setFont(new Font("Tahoma", Font.BOLD, 24));
			lblPeso.setBounds(10, 286, 400, 29);
			contentPanel.add(lblPeso);
			
			txtValueWeight = new JTextField();
			txtValueWeight.setEditable( ( selectedOption != ShipmentOpcaoMenu.MATERIA_PRIMA ) );
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
			
			if ( itemSelected != null )
			{
				trigger(0, true);
			}
			
		}
		else
		{
			radioButtonBox = new JRadioButton("Embalagem", true);
			radioButtonBox.setBounds(150, 50, 400, 29);
			radioButtonBox.setFont(new Font("Tahoma", Font.BOLD, 24));
			radioButtonBox.addItemListener( new ItemListener() {				
				@Override
				public void itemStateChanged(ItemEvent e) {
					
					if ( radioButtonBox.isSelected() ){
						radioButtonPallet.setSelected(false);
					}
					txtIdMasc.requestFocus();
					
				}
			});
			contentPanel.add(radioButtonBox);
			
			radioButtonPallet = new JRadioButton("Palete", false);
			radioButtonPallet.setFont(new Font("Tahoma", Font.BOLD, 24));
			radioButtonPallet.setBounds(150, 100, 400, 29);
			radioButtonPallet.addItemListener( new ItemListener() {				
				@Override
				public void itemStateChanged(ItemEvent e) {
					
					if ( radioButtonPallet.isSelected() ){
						radioButtonBox.setSelected(false);
					}
					txtIdMasc.requestFocus();
					
				}
			});
			contentPanel.add(radioButtonPallet);
			
			lblCodigo = new JLabel("ETIQUETA + FILIAL");
			lblCodigo.setBounds(150, 180, 400, 29);
			txtIdMasc.setBounds(85, 230, 510, 65);			
		}		
		lblCodigo.setHorizontalAlignment(SwingConstants.CENTER);
		lblCodigo.setFont(new Font("Tahoma", Font.BOLD, 24));		
		contentPanel.add(lblCodigo);
				
		txtIdMasc.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mousePressedForTxtValueCodigo();		
			}
		});
		txtIdMasc.setHorizontalAlignment(SwingConstants.CENTER);
		txtIdMasc.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtIdMasc.setColumns(10);		
		txtIdMasc.selectAll();
		if ( ( selectedOption == ShipmentOpcaoMenu.FIFO || selectedOption == ShipmentOpcaoMenu.MATERIA_PRIMA ) && itemSelected != null )
		{
			txtIdMasc.setText( itemSelected.getProduct().getIdMasc() );
		}
		contentPanel.add(txtIdMasc);
				
		btnConfirmar = new JButton("Confirmar");		
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm();			
			}
		});
		btnConfirmar.setIcon(new ImageIcon(ShipmentManualDlg.class.getResource("/32x32/Apply.png")));
		btnConfirmar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnConfirmar.setBounds(175, 390, 175, 74);
		contentPanel.add(btnConfirmar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtIdMasc.setText("");
				setVisible(false);
			}
		});
		btnCancelar.setIcon(new ImageIcon(ShipmentManualDlg.class.getResource("/32x32/Cancel.png")));
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCancelar.setBounds(375, 390, 175, 74);
		contentPanel.add(btnCancelar);
	}

	private void generateSaldo(final ShipmentOpcaoMenu selectedOption) 
	{
		if ( selectedOption == ShipmentOpcaoMenu.FIFO ) 
		{
			if ( itemSelected != null ) saldo = shipmentService.getSaldo( itemSelected.getProduct(), true );
		}
	}	

	private ItensShipment checkIsFifoOrMP() 
	{
		boolean validOk = true;
		GuiGlobals.showMessage("", false);
		if ( selectedOption == ShipmentOpcaoMenu.FIFO && itemSelected != null && !itemSelected.getProduct().isFifoEnabled() )
		{
			GuiGlobals.showMessage("Produto [" + itemSelected.getProduct().getIdMasc() + "] não é FIFO!");
			validOk = false;
		}	
		
		if ( selectedOption == ShipmentOpcaoMenu.MATERIA_PRIMA && itemSelected != null && itemSelected.getProduct().getTypeStock() != TypeStock.MATERIA_PRIMA )
		{
			GuiGlobals.showMessage("Produto [" + itemSelected.getProduct().getIdMasc() + "] não é Matéria-Prima!");
			validOk = false;
		}
		
		if ( !validOk )
		{
			txtIdMasc.setText("");
			itemSelected = null;
			if ( txtValueQuantidade != null ) txtValueQuantidade.setText("");
			if ( txtValueWeight != null ) txtValueWeight.setText("");			
			if ( unitItemSelected != null ) unitItemSelected = null;
		}
		
		return itemSelected;
	}

	private boolean trigger(int opcao, boolean isInitDialog) 
	{				
		double qtyFromCalc = 0.0;
		double weightFromCalc = 0.0;
		boolean isRestart = false;
		boolean processOk = true;
		
		checkIsFifoOrMP();
		
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
			else if ( selectedOption == ShipmentOpcaoMenu.FIFO )
			{
				/**
				 * Se for gatilho do campo quantidade para o campo peso
				 */
				if ( opcao == 1 )
				{				
					validateQty();
					qtyFromCalc = Double.valueOf( txtValueQuantidade.getText() );
					
					if ( isSaldoOk( itemSelected, qtyFromCalc, 0.0 ) )
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
					else
					{
						isRestart = true;
						processOk = false;
						GuiGlobals.showMessageDlg("Não existe saldo para quantidade digitada!");
					}
				}
				/**
				 * Se for gatilho do campo peso para o campo quantidade
				 */
				else if ( opcao == 2 )
				{
					weightFromCalc = Double.valueOf( txtValueWeight.getText().replace(",", ".") );
					if ( isSaldoOk( itemSelected, 0.0, weightFromCalc ) )
					{
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
					else
					{
						isRestart = true;
						processOk = false;
						GuiGlobals.showMessageDlg("Não existe saldo para quantidade digitada!");
					}
				}
			}
			else
			{
				validateQty();
				validateWeight();
				qtyFromCalc = getQty();				
				weightFromCalc = getNet();
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

	private boolean isSaldoOk(ItensShipment itemSelected, double qtyFromCalc, double weightFromCalc ) 
	{
		boolean saldoOk = false;
		
		if ( saldo != null && saldo.size() > 0 )
		{
		
			if ( shipmentService.isUnitForEmb(itemSelected.getProduct(), unitItemSelected ) )
			{
				saldoOk = ( qtyFromCalc <= saldo.get(0).getSecondaryQty() );
			}
			else if ( shipmentService.isUnitForPc( itemSelected.getProduct(), unitItemSelected ) )
			{
				saldoOk = ( qtyFromCalc <= saldo.get(0).getPrimaryQty() );
			}
			else if ( shipmentService.isUnitForKg( unitItemSelected ) )
			{
				saldoOk = ( weightFromCalc <= saldo.get(0).getNet() );
			}
			
		}
		
		return saldoOk;
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
		else if ( itemSelected.getProduct().isStandardWeight() && itemSelected.getProduct().getTargetWeight() == 0 )
		{
			GuiGlobals.showMessageDlg("Produto padrão sem quantidade por embalagem definida. Não será possível calcular os múltiplos!");
		}
		
		return weightFromCalc;
	}

	private void listProducts() 
	{
		GuiGlobals.waitCursor();
		GuiGlobals.showMessage("", false);
		ProductsDlg dlg = new ProductsDlg();
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);
		if(dlg.getSelectedProduct() != null) {
			txtIdMasc.setText(String.valueOf(dlg.getSelectedProduct().getIdMasc()));
			this.itemSelected = dlgPrincipal.searchProductOnGrid( dlg.getSelectedProduct() ).clone();
			if ( itemSelected != null ){
				trigger(0, true);
			}
		}
	}
	
	private void mousePressedForTxtValueQuantidade() 
	{	
		fieldSelected = 1;
		GuiGlobals.virtualKeyboardAction(ShipmentManualDlg.this, txtValueQuantidade, "validateQty", this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter1, null);
	}
	
	private void mousePressedForTxtValueWeight() {
		fieldSelected = 2;
		GuiGlobals.virtualKeyboardAction(ShipmentManualDlg.this, txtValueWeight, "validateWeight", this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter1, null);
	}
	
	private void mousePressedForTxtValueCodigo() 
	{
		if ( btnEnter2 == null )
			GuiGlobals.virtualKeyboard(ShipmentManualDlg.this, txtIdMasc, null, null);
		else
			GuiGlobals.virtualKeyboardAction(ShipmentManualDlg.this, txtIdMasc, null, this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter2, null);
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
	
	public JTextField getTxtValueCodigo() {
		return txtIdMasc;
	}

	public void setTxtValueCodigo(JTextField txtValueCodigo) {
		this.txtIdMasc = txtValueCodigo;
	}

	public JTextField getTxtValueWeight() {
		return txtValueWeight;
	}

	public void setTxtValueWeight(JTextField txtValueWeight) {
		this.txtValueWeight = txtValueWeight;
	}

	public void confirm() 
	{
		if ( ShipmentOpcaoMenu.MATERIA_PRIMA.equals(selectedOption) || ShipmentOpcaoMenu.FIFO.equals(selectedOption) )
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
		}
		else
		{
			confirmed = true;
		}
		
		if ( confirmed )
		{
			if ( selectedOption == ShipmentOpcaoMenu.ETIQUETA  )
			{
				if ( radioButtonBox.isSelected() )
				{
					txtIdMasc.setText( StringUtils.leftPad(txtIdMasc.getText().trim(), 10, "0") );
				}
				else if ( radioButtonPallet.isSelected() )
				{
					txtIdMasc.setText( StringUtils.leftPad(txtIdMasc.getText().trim(), 8, "0") );
				}
			}
			
			setVisible(false);
		}
	}
	
	public JTextField getTxtValueQuantidade() {
		return txtValueQuantidade;
	}

	public void setTxtValueQuantidade(JTextField txtValueQuantidade) {
		this.txtValueQuantidade = txtValueQuantidade;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	
	public boolean isBoxSelected(){
		return radioButtonBox.isSelected();
	}
	
	public boolean isPalletSelected(){
		return radioButtonPallet.isSelected();
	}
	
	public int getQty() {
		return Integer.parseInt( txtValueQuantidade.getText() );
	}
	
	public double getNet() {
		return Double.parseDouble( txtValueWeight.getText() );
	}

}