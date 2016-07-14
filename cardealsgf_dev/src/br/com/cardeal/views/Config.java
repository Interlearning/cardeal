/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cardeal.views;

import br.com.cardeal.enums.ModeloBancoDeDados;
import br.com.cardeal.services.BancoDeDadosConfigService;
import br.com.cardeal.services.TerminalConfigService;
import br.com.cardeal.model.ArquivoConfigBancoDeDados;
import br.com.cardeal.model.ArquivoConfigTerminal;

/**
 *
 * @author Sergio
 */
public class Config extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates new form Config
     */
    public Config( ArquivoConfigTerminal configTerminal, ArquivoConfigBancoDeDados configSGBD ) {
    	this.configTerminal = configTerminal;
    	this.configSGBD = configSGBD;
        initComponents();
        showDlg();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

    	jtpTablePanel = new javax.swing.JTabbedPane();
        jpFolderTerminal = new javax.swing.JPanel();
        lblInstalationDir = new javax.swing.JLabel();
        txtInstalationDir = new javax.swing.JTextField();
        txtInstalationDir.setText( getPath() );
        lblCodFil = new javax.swing.JLabel();
        txtCodCompany = new javax.swing.JTextField();
        txtCodCompany.setText( getCompany() );
        lblCodTerminal = new javax.swing.JLabel();
        txtCodTerminal = new javax.swing.JTextField();
        txtCodTerminal.setText( getTerminal() );
        jpFolderBancoDeDados = new javax.swing.JPanel();
        lblModelo = new javax.swing.JLabel();
        jcbModelo = new javax.swing.JComboBox<>();
        jcbModelo.setModel(new javax.swing.DefaultComboBoxModel<>(ModeloBancoDeDados.valuesOfString()));
        jcbModelo.setToolTipText("");
        jcbModelo.setSelectedItem( getModelo() );
        lblUrlServer = new javax.swing.JLabel();
        txtUrlServer = new javax.swing.JTextField();
        txtUrlServer.setText( getUrlServer() );
        lblPortServer = new javax.swing.JLabel();
        txtPortServer = new javax.swing.JTextField();
        txtPortServer.setText( getPortServer() );
        lblDataBase = new javax.swing.JLabel();
        txtDataBase = new javax.swing.JTextField();
        txtDataBase.setText( getDataBase() );
        lblUser = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        txtUser.setText( getUser() );
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        txtPassword.setText( getPassword() );
        jpPanelButtons = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        jsSeparatorFolderAndButtons = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Configura��o");
        setResizable(false);

        lblInstalationDir.setText("Diret�rio de instala��o");
        
        txtInstalationDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInstalationDirActionPerformed(evt);
            }
        });

        lblCodFil.setText("C�digo da filial");

        txtCodCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodCompanyActionPerformed(evt);
            }
        });

        lblCodTerminal.setText("C�digo do Terminal");

        txtCodTerminal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodTerminalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpFolderTerminalLayout = new javax.swing.GroupLayout(jpFolderTerminal);
        jpFolderTerminal.setLayout(jpFolderTerminalLayout);
        jpFolderTerminalLayout.setHorizontalGroup(
            jpFolderTerminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFolderTerminalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpFolderTerminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCodFil)
                    .addComponent(lblCodTerminal)
                    .addComponent(txtCodTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInstalationDir)
                    .addComponent(txtInstalationDir, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpFolderTerminalLayout.setVerticalGroup(
            jpFolderTerminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFolderTerminalLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblInstalationDir)
                .addGap(6, 6, 6)
                .addComponent(txtInstalationDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCodFil)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodCompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCodTerminal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(382, Short.MAX_VALUE))
        );

        jtpTablePanel.addTab("Terminal", jpFolderTerminal);

        lblModelo.setText("Modelo");

        lblUrlServer.setText("IP / URL do Servidor");

        txtUrlServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUrlServerActionPerformed(evt);
            }
        });

        lblPortServer.setText("Porta do Servidor");

        txtPortServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPortServerActionPerformed(evt);
            }
        });

        lblDataBase.setText("Data Base");

        txtDataBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataBaseActionPerformed(evt);
            }
        });

        lblUser.setText("Usu�rio de acesso ao Banco de Dados");

        lblPassword.setText("Senha de acesso ao Banco de Dados");

        javax.swing.GroupLayout jpFolderBancoDeDadosLayout = new javax.swing.GroupLayout(jpFolderBancoDeDados);
        jpFolderBancoDeDados.setLayout(jpFolderBancoDeDadosLayout);
        jpFolderBancoDeDadosLayout.setHorizontalGroup(
            jpFolderBancoDeDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFolderBancoDeDadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpFolderBancoDeDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUrlServer, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModelo)
                    .addComponent(lblUrlServer)
                    .addComponent(lblPortServer)
                    .addComponent(jcbModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDataBase)
                    .addComponent(txtPortServer, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataBase, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUser)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpFolderBancoDeDadosLayout.setVerticalGroup(
            jpFolderBancoDeDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFolderBancoDeDadosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblModelo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUrlServer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUrlServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPortServer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPortServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDataBase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDataBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUser)
                .addGap(1, 1, 1)
                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(189, Short.MAX_VALUE))
        );

        jtpTablePanel.addTab("Banco de Dados", jpFolderBancoDeDados);

        jpPanelButtons.setPreferredSize(new java.awt.Dimension(100, 100));

        btnClose.setText("Fechar");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnOk.setText("Confirmar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpPanelButtonsLayout = new javax.swing.GroupLayout(jpPanelButtons);
        jpPanelButtons.setLayout(jpPanelButtonsLayout);
        jpPanelButtonsLayout.setHorizontalGroup(
            jpPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPanelButtonsLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
            .addComponent(jsSeparatorFolderAndButtons)
        );
        jpPanelButtonsLayout.setVerticalGroup(
            jpPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPanelButtonsLayout.createSequentialGroup()
                .addComponent(jsSeparatorFolderAndButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jpPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpTablePanel)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpPanelButtons, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtpTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 82, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 607, Short.MAX_VALUE)
                    .addComponent(jpPanelButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private String getModelo() 
    {
    	String modelo = null;
    	
    	if ( configSGBD != null && configSGBD.getModelo() != null )
    	{
    		modelo = configSGBD.getModelo().getName();
    	}    	
    	
		return modelo;
	}

	private String getPassword() 
    {
    	String psw = "";
    	
    	if ( configSGBD != null && configSGBD.getPassWord() != null && !configSGBD.getPassWord().isEmpty() )
    	{
    		psw = configSGBD.getPassWordDecrypt();
    	}    	
    	
		return psw;
	}

	private String getUser() 
	{
		String user = "";
    	
    	if ( configSGBD != null && configSGBD.getUser() != null && !configSGBD.getUser().isEmpty() )
    	{
    		user = configSGBD.getUser();
    	}    	
    	
		return user;
	}

	private String getDataBase() 
	{
		String dataBase = "";
    	
    	if ( configSGBD != null && configSGBD.getDataBase() != null && !configSGBD.getDataBase().isEmpty() )
    	{
    		dataBase = configSGBD.getDataBase();
    	}    	
    	
		return dataBase;
	}

	private String getPortServer() 
	{
		String port = "";
    	
    	if ( configSGBD != null && configSGBD.getPortServer() != null && !configSGBD.getPortServer().isEmpty() )
    	{
    		port = configSGBD.getPortServer();
    	}    	
    	
		return port;
	}

	private String getUrlServer() 
	{
		String server = "";
    	
    	if ( configSGBD != null && configSGBD.getUrlServer() != null && !configSGBD.getUrlServer().isEmpty() )
    	{
    		server = configSGBD.getUrlServer();
    	}    	
    	
		return server;
	}

	private String getCompany() 
	{
    	String company = "";
    	
    	if ( configTerminal != null && configTerminal.getCompany() != null && !configTerminal.getCompany().isEmpty() )
    	{
    		company = configTerminal.getCompany();
    	}    	
    	
		return company;
	}
    
    private String getTerminal() 
    {
    	String terminal = "";
    	
    	if ( configTerminal != null && configTerminal.getTerminal() != null && !configTerminal.getTerminal().isEmpty() )
    	{
    		terminal = configTerminal.getTerminal();
    	}    	
    	
		return terminal;
	}
    
	private String getPath() 
    {

    	String path = "";
    	
    	if ( configTerminal == null || (  configTerminal.getInstalationDir() != null && configTerminal.getInstalationDir().isEmpty() ) )
    	{
    		path = ArquivoConfigTerminal.getRootPath();
    	}
    	else
    	{
    		path = configTerminal.getInstalationDir();
    	}
    	
		return path;
	}

	private void txtCodTerminalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodTerminalActionPerformed
        
    }//GEN-LAST:event_txtCodTerminalActionPerformed

    private void txtUrlServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUrlServerActionPerformed
        
    }//GEN-LAST:event_txtUrlServerActionPerformed

    private void txtPortServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPortServerActionPerformed
        
    }//GEN-LAST:event_txtPortServerActionPerformed

    private void txtDataBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataBaseActionPerformed
        
    }//GEN-LAST:event_txtDataBaseActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
    	setOperation(CANCELED);
        close();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
    	
    	setOperation(CONFIRMED);
    	createConfigurationTerminal();
    	createConfigurationBancoDeDados();
    	
//        if ( createConfigurationTerminal() && createConfigurationBancoDeDados() )
//        {
//            GuiGlobals.showMessageDlg("Opera��o Finalizada!", "Arquivo criado com sucesso!");
//        }
//        else
//        {
//        	GuiGlobals.showMessageDlg("Opera��o Finalizada com falhas!", "Arquivo não foi criado com sucesso!");
//        }
        close();
    }//GEN-LAST:event_btnOkActionPerformed

    private void txtCodCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodCompanyActionPerformed
        
    }//GEN-LAST:event_txtCodCompanyActionPerformed

    private void txtInstalationDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInstalationDirActionPerformed
        
    }//GEN-LAST:event_txtInstalationDirActionPerformed

    /**
     * @param args the command line arguments
     */
    public void showDlg() 
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        this.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox<String> jcbModelo;
    private javax.swing.JPanel jpFolderBancoDeDados;
    private javax.swing.JPanel jpFolderTerminal;
    private javax.swing.JPanel jpPanelButtons;
    private javax.swing.JSeparator jsSeparatorFolderAndButtons;
    private javax.swing.JTabbedPane jtpTablePanel;
    private javax.swing.JLabel lblCodFil;
    private javax.swing.JLabel lblCodTerminal;
    private javax.swing.JLabel lblDataBase;
    private javax.swing.JLabel lblInstalationDir;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPortServer;
    private javax.swing.JLabel lblUrlServer;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTextField txtCodCompany;
    private javax.swing.JTextField txtCodTerminal;
    private javax.swing.JTextField txtDataBase;
    private javax.swing.JTextField txtInstalationDir;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPortServer;
    private javax.swing.JTextField txtUrlServer;
    private javax.swing.JTextField txtUser;
    private ArquivoConfigTerminal configTerminal;
    private ArquivoConfigBancoDeDados configSGBD;
    private int operation = 0;
    public static final int CANCELED = 1;
    public static final int CONFIRMED = 2;
    // End of variables declaration//GEN-END:variables

    private boolean createConfigurationTerminal() 
    {   
        TerminalConfigService terminalService = new TerminalConfigService( txtCodCompany.getText(), txtCodTerminal.getText(), txtInstalationDir.getText() );        
        return terminalService.create();        
    }
    
    private boolean createConfigurationBancoDeDados() 
    {
        String modeloString = (String) jcbModelo.getSelectedItem();
        ModeloBancoDeDados modelo = ModeloBancoDeDados.getModelByString( modeloString );
        BancoDeDadosConfigService sgbdService = new BancoDeDadosConfigService( modelo, txtUrlServer.getText(), txtPortServer.getText(), txtDataBase.getText(), txtUser.getText(), txtPassword.getText(), txtInstalationDir.getText() );        
        return sgbdService.create();
    }

    private void close() 
    {
        this.dispose();        
    }

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}
}