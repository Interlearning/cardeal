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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import br.com.cardeal.dao.ProductDao;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.model.Product;

public class AdicionarItemDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtValueCodigo;
	private JTextField txtValueQuantidade;
	private JTextField txtPesoLiquido;
	private JButton btnConfirmar;
	private JButton btnCancelar;
	private JButton btnSearchProduto;
	private JLabel lblPesoLiquido;
	private JLabel lblCodigoProduto;
	private JLabel lblQuantidade;	
	private Product product = null;
	private JButton btnEnter;
	
	public AdicionarItemDlg getOuter() {
		return AdicionarItemDlg.this;
	}


	/**
	 * Create the dialog.
	 */
	public AdicionarItemDlg() {
		setTitle("ADICIONAR ITEM");
		setModalityType(ModalityType.APPLICATION_MODAL);

		setBounds(150, 70, 830, 529);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtValueCodigo = new JTextField();
		
		txtValueCodigo.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				validaAdicionaItem();
				txtPesoLiquido.setText(""); 
				txtValueQuantidade.setText("");
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				validaAdicionaItem();
								
			}
		});
		
		btnEnter = new JButton("ENTER");
        btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				searchProduct();
			}
		});
		
		txtValueCodigo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				GuiGlobals.virtualKeyboardAction(getOuter(), txtValueCodigo, null, this, null, null, null, null, null, null, null, null, null, null, null, null, btnEnter, null);
			}
		});
		txtValueCodigo.setHorizontalAlignment(SwingConstants.CENTER);
		txtValueCodigo.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtValueCodigo.setColumns(10);
		txtValueCodigo.setBounds(390, 30, 250, 65);
		
		contentPanel.add(txtValueCodigo);
		
		btnSearchProduto = new JButton("");
		btnSearchProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listProducts();
						
			}
		});
		btnSearchProduto.setIcon(new ImageIcon(AdicionarItemDlg.class.getResource("/32x32/Search.png")));
		btnSearchProduto.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnSearchProduto.setBounds(653, 30, 78, 65);
		contentPanel.add(btnSearchProduto);
		
		txtValueQuantidade = new JTextField();
		txtValueQuantidade.setEditable(false);
		txtValueQuantidade.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				validaAdicionaItem();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				validaAdicionaItem();
			}
		});
			
		txtValueQuantidade.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mousePressedTxtValueQuantidade();		
			}
		});
		txtValueQuantidade.setHorizontalAlignment(SwingConstants.CENTER);
		txtValueQuantidade.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtValueQuantidade.setColumns(10);
		txtValueQuantidade.setBounds(390, 150, 250, 65);
		txtValueQuantidade.selectAll();
		contentPanel.add(txtValueQuantidade);
		
		txtPesoLiquido = new JTextField();
		txtPesoLiquido.setEditable(false);
		txtPesoLiquido.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				validaAdicionaItem();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				validaAdicionaItem();
			}
		});
		
		txtPesoLiquido.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mousePressedTxtPesoLiquido();		
			}
		});
		txtPesoLiquido.setHorizontalAlignment(SwingConstants.CENTER);
		txtPesoLiquido.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtPesoLiquido.setColumns(10);
		txtPesoLiquido.setBounds(390, 270, 250, 65);
		txtPesoLiquido.selectAll();
		txtPesoLiquido.setEnabled(false);
		contentPanel.add(txtPesoLiquido);
		
		lblCodigoProduto = new JLabel("CÓDIGO DO PRODUTO");
		lblCodigoProduto.setHorizontalAlignment(SwingConstants.CENTER);
		lblCodigoProduto.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblCodigoProduto.setBounds(10, 49, 400, 29);
		contentPanel.add(lblCodigoProduto);
		
		lblQuantidade = new JLabel("QUANTIDADE DO PROD.");
		lblQuantidade.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuantidade.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblQuantidade.setBounds(10, 169, 400, 29);
		contentPanel.add(lblQuantidade);
		
		lblPesoLiquido = new JLabel("PESO LÍQUIDO.");
		lblPesoLiquido.setHorizontalAlignment(SwingConstants.CENTER);
		lblPesoLiquido.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblPesoLiquido.setBounds(10, 289, 400, 29);
		contentPanel.add(lblPesoLiquido);
		
		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setEnabled(false);
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm();			
			}
		});
		btnConfirmar.setIcon(new ImageIcon(AdicionarItemDlg.class.getResource("/32x32/Apply.png")));
		btnConfirmar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnConfirmar.setBounds(230, 390, 175, 74);
		contentPanel.add(btnConfirmar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				product = null;
				setVisible(false);
			}
		});
		btnCancelar.setIcon(new ImageIcon(AdicionarItemDlg.class.getResource("/32x32/Cancel.png")));
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCancelar.setBounds(419, 390, 175, 74);
		contentPanel.add(btnCancelar);
	}
	
	public void mousePressedTxtPesoLiquido() {
		GuiGlobals.virtualKeyboard(getOuter(), txtPesoLiquido, "validateWeight", this);		
	}


	public void mousePressedTxtValueQuantidade() {
		
		if ( product == null )
			searchProduct();
		
		GuiGlobals.virtualKeyboard(getOuter(), txtValueQuantidade, "validateQty", this);
		
		
	}
	
	public void validateQty() {
		
		double value = 0;
		
		try {
			double newVal = NumberUtils.roundNumber(Double.parseDouble(txtValueQuantidade.getText()), 0);
			if(newVal < 0 || newVal > 999)
				txtValueQuantidade.setText(String.valueOf(value).replace(".0", ""));
			else
				txtValueQuantidade.setText(String.valueOf(newVal).replace(".0", ""));
		}
		catch(Exception e) {
			txtValueQuantidade.setText(String.valueOf(value).replace(".0", ""));
		}
	}
	
	public void validateWeight() {
		
		double value = 0;
		
		try {
			double newVal = Double.parseDouble(txtPesoLiquido.getText());
			if(newVal < 0 || newVal > 999.999)
				txtPesoLiquido.setText(Utils.formatWeight(value));
			else
				txtPesoLiquido.setText(Utils.formatWeight(newVal));
		}
		catch(Exception e) {
			txtPesoLiquido.setText(Utils.formatWeight(value));
		}
	}


	private void listProducts() {
		GuiGlobals.waitCursor();
		ProductsDlg dlg = new ProductsDlg();
		GuiGlobals.defaultCursor();
		dlg.setVisible(true);
		
		product = dlg.getSelectedProduct();
		
		if(product != null) {
			txtValueCodigo.setText(product.getIdMasc());
			searchProduct();
		}
		
	}
	
	
	private void validaAdicionaItem(){
		
		btnConfirmar.setEnabled(false);
		txtPesoLiquido.setEnabled(false);
		
		
		if ( !( "".equals( txtValueCodigo.getText().trim() ) ) && product != null ) {
			
			int qty = Integer.parseInt( String.valueOf( NumberUtils.roundNumber( Double.parseDouble(txtValueQuantidade.getText()) , 0) ).replace(".0", "") );
		
			if (	( qty > 0 ) 
					&& 
					(	(	product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT && ( !txtPesoLiquido.getText().trim().equals("") && Double.valueOf( txtPesoLiquido.getText() ) > 0 )  ) 
						|| 
						product.getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT
					)
				){				
				btnConfirmar.setEnabled(true);
				
				
			}
			
			if ( product.getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT ){
			
				txtPesoLiquido.setEnabled(true);
				
			}
			
		}
				
	}
	
	public void searchProduct() {
		
		//valida o codigo digitado
		int prodId = 0;
		try {
			prodId = Integer.parseInt(txtValueCodigo.getText());
		}
		catch(Exception e){}
		if(prodId == 0) {
			GuiGlobals.getMain().showMessage(GuiGlobals.getBundle().getString("STR000022"));
			return;
		}
		
		//Busca o produto na base de dados
		ProductDao dao = GuiGlobals.getDaoFactory().getProductDao();
		product = dao.findFilterBlocked( txtValueCodigo.getText() );
		
		if(product == null) {
			GuiGlobals.getMain().showMessage(GuiGlobals.getBundle().getString("STR000027"));
		}
		else{
			GuiGlobals.getMain().showMessage("");
		}

	}

	public void confirm() {
			
		setVisible(false);
		
	}
			
	public Product getSelectedProduct() {
		return product;
	}
	
	public JTextField getTxtValueQuantidade() {
		return txtValueQuantidade;
	}


	public void setTxtValueQuantidade(JTextField txtValueQuantidade) {
		this.txtValueQuantidade = txtValueQuantidade;
	}
	
	public JTextField getTxtPesoLiquido() {
		return txtPesoLiquido;
	}


	public void setTxtPesoLiquido(JTextField txtPesoLiquido) {
		this.txtPesoLiquido = txtPesoLiquido;
	}

}





