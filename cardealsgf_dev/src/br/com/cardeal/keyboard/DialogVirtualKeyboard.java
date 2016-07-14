package br.com.cardeal.keyboard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.MaxLengthTextDocument;

public class DialogVirtualKeyboard extends JDialog 
{

	private static final long serialVersionUID = 1L;
    //private PanelVirtualKeyboard panelVirtualKeyboard;
	private JTextComponent textComponent;
	private Method method = null;
	

	private Object callingObject = null;
    
	private JButton btn1;
	private JButton btn2;
	private JButton btn3;
	private JButton btn4;
	private JButton btn5;
	private JButton btn6;
	private JButton btn7;
	private JButton btn8;
	private JButton btn9;
	private JButton btn0;
	private JButton btnDot;
	private JButton btnBack;
	private JButton btnEnter;    
	private JButton btnClear;
	private JButton btnLetters;
	private JButton btnNumbers;
	
	private JButton btnA;
	private JButton btnB;
	private JButton btnC;
	private JButton btnD;
	private JButton btnE;
	private JButton btnF;
	private JButton btnG;
	private JButton btnH;
	private JButton btnI;
	private JButton btnJ;
	private JButton btnK;
	private JButton btnL;
	private JButton btnM;
	private JButton btnN;
	private JButton btnO;
	private JButton btnP;
	private JButton btnQ;
	private JButton btnR;
	private JButton btnS;
	private JButton btnT;
	private JButton btnU;
	private JButton btnV;
	private JButton btnW;
	private JButton btnX;
	private JButton btnY;
	private JButton btnZ;
	private JButton btn«;
	private JButton btnMenos;
	private JButton btnMais;
	private JButton btnBarra;

	public static void main(String[] args) {
		try {
			DialogVirtualKeyboard dialog = new DialogVirtualKeyboard();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DialogVirtualKeyboard() {
		getContentPane().setBackground(SystemColor.menu);
		initComponents();
	}
		
    private void writeValue(java.awt.event.ActionEvent evt){
        if(textComponent == null) 
        	return;

        String text = textComponent.getText();
        StringBuffer sb = new StringBuffer();
        Document document = textComponent.getDocument();
        int maxLength = 0;
        
        if ( document instanceof MaxLengthTextDocument )
        {
        	maxLength = ((MaxLengthTextDocument) document).getMaxChars();
        }
        
        sb.append(text);
        sb.delete(textComponent.getSelectionStart(), textComponent.getSelectionEnd());
        textComponent.setText(sb.toString());
        text = textComponent.getText();
        
        int index = textComponent.getCaretPosition();
        String aditional = ((JButton)evt.getSource()).getText();
        
        if ( maxLength == 0 || textComponent.getText().concat(aditional).length() <= maxLength )
        {
	        textComponent.setText(text.concat(aditional));
	        textComponent.setCaretPosition(index+(((JButton)evt.getSource()).getText()).toString().length());
        }
    }
    
    private void backspace(java.awt.event.ActionEvent evt) {                                            
        if(textComponent==null) return;
        //Delete char on caret position
        String text = textComponent.getText();
        int index = textComponent.getCaretPosition();
        if(index>0){
            text = new StringBuffer(text).delete(index-1,index).toString();
            textComponent.setText(text);
            textComponent.setCaretPosition(index-1);
        }
    } 	  
    
    private void clear(java.awt.event.ActionEvent evt) {                                            
        if(textComponent==null) return;
        textComponent.setText("");
    } 	  
    
    private void enter(java.awt.event.ActionEvent evt) {                                      
    	this.setVisible(false);
    	if(method != null && callingObject != null)
			try {
				method.invoke(callingObject, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
        this.dispose();
    }  
    
    /* WJSP 21/07/2015
	 * parametro indice -> indica qual linha da tableModel TableItensPalletVirtualModel 
	 * ser· atualizada com as informaÁıes do produto digitado no textComponent apÛs pressionar Enter
	 * method.invoke -> chama funÁ„o -> public void incluirTabela(int indice, JTextComponent txtItem) da classe PackingPanelPalletVirtual
	 */
    private void enter(java.awt.event.ActionEvent evt, int indice) {                                      
    	this.setVisible(false);
    	if(method != null && callingObject != null)
			try {
				method.invoke(callingObject, indice,textComponent);
			} catch (Exception e) {
				e.printStackTrace();
			}
        this.dispose();
    }  
    
    public void setFocusToEnter()
    {
    	if ( btnEnter != null )
    		btnEnter.requestFocus();
    }	    
	
	
    public DialogVirtualKeyboard(java.awt.Window parent, boolean modal, JTextComponent textComponent, Method method, Object callingObject) 
    {
    	super(parent, ModalityType.APPLICATION_MODAL);
    	initDialogVirtualKeyboard(parent, modal, textComponent, method, callingObject, true);
    }
    
    /** Creates new form DialogVirtualKeyboard */
    public DialogVirtualKeyboard(java.awt.Window parent, boolean modal, JTextComponent textComponent, Method method, Object callingObject, boolean onlyNumeric) 
    {
    	super(parent, ModalityType.APPLICATION_MODAL);
    	initDialogVirtualKeyboard(parent, modal, textComponent, method, callingObject, onlyNumeric);
    }
    
    public DialogVirtualKeyboard(java.awt.Window parent, boolean modal, JTextComponent textComponent, Method method, Object callingObject, boolean onlyNumeric, boolean onlyLetters) 
    {
    	super(parent, ModalityType.APPLICATION_MODAL);
    	if ( !onlyNumeric && onlyLetters )
    		initDialogVirtualKeyboardWithLetters(parent, modal, textComponent, method, callingObject);
    	else
    		initDialogVirtualKeyboard(parent, modal, textComponent, method, callingObject, onlyNumeric);
    }
    
    private void initDialogVirtualKeyboard( java.awt.Window parent, boolean modal, JTextComponent textComponent, Method method, Object callingObject, boolean onlyNumeric )
    {
    	//super(parent, modal);
        this.method = method;
        this.callingObject = callingObject;
        this.setUndecorated(true);
        initComponents(onlyNumeric);
        this.textComponent = textComponent;        
        this.show(textComponent, -1, textComponent.getHeight());
    }
    
    private void initDialogVirtualKeyboardWithLetters( java.awt.Window parent, boolean modal, JTextComponent textComponent, Method method, Object callingObject )
    {
    	this.method = method;
        this.callingObject = callingObject;
        this.setUndecorated(true);
        initComponentsLetters();
        this.textComponent = textComponent;        
        this.show(textComponent, -1, textComponent.getHeight());
    }
    
    /* WJSP 21/07/2015
	 * parametro indice -> indica qual linha da tableModel TableItensPalletVirtualModel 
	 * ser· atualizada com as informaÁıes do produto digitado no textComponent apÛs pressionar Enter
	 */
    public DialogVirtualKeyboard(java.awt.Window parent, boolean modal, JTextComponent textComponent, Method method, Object callingObject, int indice) {
        //super(parent, modal);
    	super(parent, ModalityType.APPLICATION_MODAL);
        this.method = method;
        this.callingObject = callingObject;
        this.setUndecorated(true);
        this.textComponent = textComponent;
        initComponentsForItensPallet(indice);              
        this.show(textComponent, -1, textComponent.getHeight());
    }
    
    
    /** Creates new form DialogVirtualKeyboard */
    public DialogVirtualKeyboard( java.awt.Window parent, boolean modal, JTextComponent textComponent, Method method, Object callingObject
    		,JButton btn1, JButton btn2, JButton btn3, JButton btn4, JButton btn5, JButton btn6, JButton btn7
			 , JButton btn8, JButton btn9, JButton btn0, JButton btnDot, JButton btnBack, JButton btnEnter, JButton btnClear 
    		) {
        //super(parent, modal);
    	super(parent, ModalityType.APPLICATION_MODAL);
        this.method = method;
        this.callingObject = callingObject;
        this.setUndecorated(true);
        initComponentsAction(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnDot, btnBack, btnEnter, btnClear);
        this.textComponent = textComponent;        
        this.show(textComponent, -1, textComponent.getHeight());
    }
	

    public void show(Component invoker, int x, int y) {
		Point invokerOrigin;
		if (invoker != null) 
		{
		    invokerOrigin = invoker.getLocationOnScreen();
	
            // To avoid integer overflow
            long lx, ly;
            lx = ((long) invokerOrigin.x) +
                 ((long) x);
            ly = ((long) invokerOrigin.y) +
                 ((long) y);
            if(lx > Integer.MAX_VALUE) lx = Integer.MAX_VALUE;
            if(lx < Integer.MIN_VALUE) lx = Integer.MIN_VALUE;
            if(ly > Integer.MAX_VALUE) ly = Integer.MAX_VALUE;
            if(ly < Integer.MIN_VALUE) ly = Integer.MIN_VALUE;

            boolean valueX=false, valueY=false;
            int yl = 0, xl = 0;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            //Value of Y-Axis
            if((int)(ly+this.getHeight())>=screenSize.getHeight()){
                valueY=true;
                yl = (int) (ly-(long)invoker.getHeight()-this.getHeight());
            }
            //Value of X-Axis
            if((int)(lx+this.getWidth())>=screenSize.getWidth()){
                valueX=true;
                xl = (int) (lx-(long)this.getWidth());
            }

            if(valueX && valueY) setLocation((int) xl, (int) yl);
            else if(valueX) setLocation((int) xl, (int) ly);
            else if(valueY) setLocation((int) lx, (int) yl);
            else setLocation((int) lx, (int) ly);
		} 
		else 
		{
		    setLocation(x, y);
		}
		
	    setVisible(true);
	    setFocusToEnter();	     
    }
    
    /* WJSP 21/07/2015
	 * parametro indice -> indica qual linha da tableModel TableItensPalletVirtualModel 
	 * ser· atualizada com as informaÁıes do produto digitado no textComponent apÛs pressionar Enter
	 */
    private void initComponentsForItensPallet(final int indice) {

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 265, 172);
		getContentPane().setLayout(null);
		
		btn1 = new JButton("1");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn1.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn1.setBounds(0, 86, 63, 43);
		getContentPane().add(btn1);
		
		btn2 = new JButton("2");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn2.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn2.setBounds(62, 86, 63, 43);
		getContentPane().add(btn2);
		
		btn3 = new JButton("3");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn3.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn3.setBounds(126, 86, 63, 43);
		getContentPane().add(btn3);
		
		btn4 = new JButton("4");
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn4.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn4.setBounds(0, 43, 63, 43);
		getContentPane().add(btn4);
		
		btn5 = new JButton("5");
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn5.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn5.setBounds(62, 43, 63, 43);
		getContentPane().add(btn5);
		
		btn6 = new JButton("6");
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn6.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn6.setBounds(126, 43, 63, 43);
		getContentPane().add(btn6);
		
		btn7 = new JButton("7");
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn7.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn7.setBounds(0, 0, 63, 43);
		getContentPane().add(btn7);
		
		btn8 = new JButton("8");
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn8.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn8.setBounds(62, 0, 63, 43);
		getContentPane().add(btn8);
		
		btn9 = new JButton("9");
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn9.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn9.setBounds(126, 0, 63, 43);
		getContentPane().add(btn9);
		
		btn0 = new JButton("0");
		btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn0.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn0.setBounds(0, 129, 125, 43);
		getContentPane().add(btn0);
		
		btnDot = new JButton(".");
		btnDot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btnDot.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDot.setBounds(126, 129, 63, 43);
		getContentPane().add(btnDot);
		
		btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backspace(e);
			}
		});
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnBack.setBounds(189, 0, 76, 43);
		getContentPane().add(btnBack);
		
		btnEnter = new JButton("ENTER");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enter(e,indice);
			}
		});
		btnEnter.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEnter.setBounds(189, 86, 76, 86);
		getContentPane().add(btnEnter);
		
		btnClear = new JButton("CLR");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear(e);
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnClear.setBounds(189, 43, 76, 43);
		getContentPane().add(btnClear);
    	
		
    	addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        //pack();
        
    }    
    
    private void initComponents() 
    {
    	initComponents(true);
    }
    
    private void initComponents( boolean onlyNumeric ) 
    {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 275, 220);
		getContentPane().setLayout(null);
		
		btn1 = new JButton("1");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn1.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn1.setBounds(0, 86, 63, 43);
		getContentPane().add(btn1);
		
		btn2 = new JButton("2");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn2.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn2.setBounds(62, 86, 63, 43);
		getContentPane().add(btn2);
		
		btn3 = new JButton("3");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn3.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn3.setBounds(126, 86, 63, 43);
		getContentPane().add(btn3);
		
		btn4 = new JButton("4");
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn4.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn4.setBounds(0, 43, 63, 43);
		getContentPane().add(btn4);
		
		btn5 = new JButton("5");
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn5.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn5.setBounds(62, 43, 63, 43);
		getContentPane().add(btn5);
		
		btn6 = new JButton("6");
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn6.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn6.setBounds(126, 43, 63, 43);
		getContentPane().add(btn6);
		
		btn7 = new JButton("7");
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn7.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn7.setBounds(0, 0, 63, 43);
		getContentPane().add(btn7);
		
		btn8 = new JButton("8");
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn8.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn8.setBounds(62, 0, 63, 43);
		getContentPane().add(btn8);
		
		btn9 = new JButton("9");
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn9.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn9.setBounds(126, 0, 63, 43);
		getContentPane().add(btn9);
		
		btn0 = new JButton("0");
		btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btn0.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn0.setBounds(0, 129, 125, 43);
		getContentPane().add(btn0);
		
		btnDot = new JButton(".");
		btnDot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		btnDot.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDot.setBounds(126, 129, 63, 43);
		getContentPane().add(btnDot);
		
		btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backspace(e);
			}
		});
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnBack.setBounds(189, 0, 76, 43);
		getContentPane().add(btnBack);
		
		btnEnter = new JButton("ENTER");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enter(e);
			}
		});
		btnEnter.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEnter.setBounds(189, 86, 76, 86);
		getContentPane().add(btnEnter);
		
		btnClear = new JButton("CLR");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear(e);
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnClear.setBounds(189, 43, 76, 43);
		getContentPane().add(btnClear);
		
		if ( !onlyNumeric )
		{
			btnLetters = new JButton("ABC");
			btnLetters.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) 
				{
					includeLetters();					
				}
			});
			btnLetters.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnLetters.setBounds(189, 172, 76, 43);
			getContentPane().add(btnLetters);
		}
    			
    	addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        
    }
    
    private void includeLetters() 
    {    	
    	GuiGlobals.virtualKeyboard(null, textComponent, getMethod(method), callingObject, false, true, false);
    }
    
    private String getMethod(Method method2) 
    {
		return ( ( method != null ) ? method.getName() : null );
	}

	private void includeNumbers() 
    {    	
    	GuiGlobals.virtualKeyboard(null, textComponent, getMethod(method), callingObject, false, false, false);
    }
    
    private void initComponentsLetters() 
    {
    	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 397, 257);
		getContentPane().setLayout(null);
		
    	btnQ = new JButton("Q");
    	btnQ.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnQ.setBounds(0, 0, 63, 43);
		btnQ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		
		btnW = new JButton("W");
		btnW.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnW.setBounds(62, 0, 63, 43);
		btnW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});					
				
		btnE = new JButton("E");
		btnE.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnE.setBounds(126, 0, 63, 43);
		btnE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});				
				
		btnR = new JButton("R");
		btnR.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnR.setBounds(190, 0, 63, 43);
		btnR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
							
		btnT = new JButton("T");
		btnT.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnT.setBounds(254, 0, 63, 43);
		btnT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				writeValue(evt);
			}
		});
						
		btnY = new JButton("Y");
		btnY.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnY.setBounds(0, 43, 63, 43);
		btnY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				writeValue(evt);
			}
		});		
		
		btnU = new JButton("U");
		btnU.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnU.setBounds(62, 43, 63, 43);
		btnU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				writeValue(evt);
			}
		});	
				
		btnI = new JButton("I");
		btnI.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnI.setBounds(126, 43, 63, 43);
		btnI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				writeValue(evt);
			}
		});
					
		btnO = new JButton("O");
		btnO.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnO.setBounds(190, 43, 63, 43);
		btnO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				writeValue(evt);
			}
		});
		
		btnP = new JButton("P");
		btnP.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnP.setBounds(254, 43, 63, 43);
		btnP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				writeValue(evt);
			}
		});
				
		btnA = new JButton("A");
		btnA.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnA.setBounds(0, 86, 63, 43);
		btnA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
				
		btnS = new JButton("S");
		btnS.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnS.setBounds(62, 86, 63, 43);
		btnS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});	
				
		btnD = new JButton("D");
		btnD.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnD.setBounds(126, 86, 63, 43);
		btnD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
				
		
		
		btnF = new JButton("F");
		btnF.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnF.setBounds(190, 86, 63, 43);
		btnF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
				
		btnG = new JButton("G");
		btnG.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnG.setBounds(254, 86, 63, 43);
		btnG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
				
		btnH = new JButton("H");
		btnH.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnH.setBounds(0, 129, 63, 43);
		btnH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
				
		btnJ = new JButton("J");
		btnJ.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnJ.setBounds(62, 129, 63, 43);
		btnJ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		
		btnK = new JButton("K");
		btnK.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnK.setBounds(126, 129, 63, 43);
		btnK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});	
		
		btnL = new JButton("L");
		btnL.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnL.setBounds(190, 129, 63, 43);
		btnL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});	
		
		btn« = new JButton("«");
		btn«.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn«.setBounds(254, 129, 63, 43);
		btn«.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		
		btnZ = new JButton("Z");
		btnZ.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnZ.setBounds(0, 172, 63, 43);
		btnZ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
				
		btnX = new JButton("X");
		btnX.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnX.setBounds(62, 172, 63, 43);
		btnX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		
		btnC = new JButton("C");
		btnC.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnC.setBounds(126, 172, 63, 43);
		btnC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});	
		
		btnV = new JButton("V");
		btnV.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnV.setBounds(190, 172, 63, 43);
		btnV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});	
		
		btnB = new JButton("B");
		btnB.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnB.setBounds(254, 172, 63, 43);
		btnB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		
		btnN = new JButton("N");
		btnN.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnN.setBounds(0, 215, 63, 43);
		btnN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
				
		btnM = new JButton("M");
		btnM.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnM.setBounds(62, 215, 63, 43);
		btnM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		
		btnMenos = new JButton("-");
		btnMenos.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnMenos.setBounds(126, 215, 63, 43);
		btnMenos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});
		
		btnMais = new JButton("+");
		btnMais.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnMais.setBounds(190, 215, 63, 43);
		btnMais.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});	
		
		btnBarra = new JButton("/");
		btnBarra.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnBarra.setBounds(254, 215, 63, 43);
		btnBarra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        writeValue(evt);
			}
		});	
		
		btnBack = new JButton("BACK");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnBack.setBounds(318, 0, 76, 43);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backspace(e);
			}
		});
		
		btnClear = new JButton("CLR");
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnClear.setBounds(318, 43, 76, 43);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear(e);
			}
		});
						
		btnEnter = new JButton("ENTER");
		btnEnter.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEnter.setBounds(318, 86, 76, 86);
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enter(e);
			}
		});
		
		btnNumbers = new JButton("123");
		btnNumbers.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNumbers.setBounds(318, 172, 76, 43);
		btnNumbers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) 
			{
				includeNumbers();					
			}
		});		
				
		getContentPane().add(btnBack);
		getContentPane().add(btnEnter);
		getContentPane().add(btnClear);
		getContentPane().add(btnA);
		getContentPane().add(btnB);
		getContentPane().add(btnC);
		getContentPane().add(btnD);
		getContentPane().add(btnE);
		getContentPane().add(btnF);
		getContentPane().add(btnG);
		getContentPane().add(btnH);
		getContentPane().add(btnI);
		getContentPane().add(btnJ);
		getContentPane().add(btnK);
		getContentPane().add(btnL);
		getContentPane().add(btnM);
		getContentPane().add(btnN);
		getContentPane().add(btnO);
		getContentPane().add(btnP);
		getContentPane().add(btnQ);
		getContentPane().add(btnR);
		getContentPane().add(btnS);
		getContentPane().add(btnT);
		getContentPane().add(btnU);
		getContentPane().add(btnV);
		getContentPane().add(btnW);
		getContentPane().add(btnX);
		getContentPane().add(btnY);
		getContentPane().add(btnZ);
		getContentPane().add(btn«);
		getContentPane().add(btnMenos);
		getContentPane().add(btnMais);
		getContentPane().add(btnBarra);
		getContentPane().add(btnNumbers);
		
		addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
		
	}

	private void initComponentsAction( JButton btn1, JButton btn2, JButton btn3, JButton btn4, JButton btn5, JButton btn6, JButton btn7
    								 , JButton btn8, JButton btn9, JButton btn0, JButton btnDot, JButton btnBack, JButton btnEnter, JButton btnClear ) {

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 265, 172);
		getContentPane().setLayout(null);
		
		if ( btn1!=null ){
			this.btn1 = btn1;
		}
		else {
			this.btn1 = new JButton("1");
			this.btn1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}
		
		this.btn1.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn1.setBounds(0, 86, 63, 43);
		getContentPane().add(this.btn1);
		
		if ( btn2!=null ){
			this.btn2 = btn2;
		}
		else {
			this.btn2 = new JButton("2");
			this.btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}
		
		this.btn2.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn2.setBounds(62, 86, 63, 43);
		getContentPane().add(this.btn2);
		
		if ( btn3!=null ){
			this.btn3 = btn3;
		}
		else {
			this.btn3 = new JButton("3");
			this.btn3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}
		
		this.btn3.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn3.setBounds(126, 86, 63, 43);
		getContentPane().add(this.btn3);
		
		if ( btn4!=null ){
			this.btn4 = btn4;
		}
		else {
			this.btn4 = new JButton("4");
			this.btn4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}
		
		this.btn4.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn4.setBounds(0, 43, 63, 43);
		getContentPane().add(this.btn4);
		
		if ( btn5!=null ){
			this.btn5 = btn5;
		}
		else {
			this.btn5 = new JButton("5");
			this.btn5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}
		
		this.btn5.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn5.setBounds(62, 43, 63, 43);
		getContentPane().add(this.btn5);
		
		if ( btn6!=null ){
			this.btn6 = btn6;
		}
		else {
			this.btn6 = new JButton("6");
			this.btn6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}	
		
		this.btn6.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn6.setBounds(126, 43, 63, 43);
		getContentPane().add(this.btn6);
		
		if ( btn7!=null ){
			this.btn7 = btn7;
		}
		else {
			this.btn7 = new JButton("7");
			this.btn7.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}	
			
		this.btn7.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn7.setBounds(0, 0, 63, 43);
		getContentPane().add(this.btn7);
		
		if ( btn8!=null ){
			this.btn8 = btn8;
		}
		else {
			this.btn8 = new JButton("8");
			this.btn8.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}
		
		this.btn8.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn8.setBounds(62, 0, 63, 43);
		getContentPane().add(this.btn8);
		
		if ( btn9!=null ){
			this.btn9 = btn9;
		}
		else {
			this.btn9 = new JButton("9");
			this.btn9.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}
		
		this.btn9.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn9.setBounds(126, 0, 63, 43);
		getContentPane().add(this.btn9);
		
		if ( btn0!=null ){
			this.btn0 = btn0;
		}
		else {
			this.btn0 = new JButton("0");
			this.btn0.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}
		
		this.btn0.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btn0.setBounds(0, 129, 125, 43);
		getContentPane().add(this.btn0);
		
		if ( btnDot!=null ){
			this.btnDot = btnDot;
		}
		else {
			this.btnDot = new JButton(".");
			this.btnDot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			        writeValue(evt);
				}
			});
		}
		
		this.btnDot.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.btnDot.setBounds(126, 129, 63, 43);
		getContentPane().add(this.btnDot);
		
		
		if ( btnBack!=null ){
			this.btnBack = btnBack;
		}
		else {
			this.btnBack = new JButton("BACK");
			this.btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					backspace(e);
				}
			});
		}
		
		this.btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
		this.btnBack.setBounds(189, 0, 76, 43);
		getContentPane().add(this.btnBack);
		
		if ( btnEnter!=null ){
			this.btnEnter = btnEnter;
			this.btnEnter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeDialog();
				}
			});
		}
		else {
			this.btnEnter = new JButton("ENTER");
			this.btnEnter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enter(e);
				}
			});
		}
		
		this.btnEnter.setFont(new Font("Tahoma", Font.BOLD, 12));
		this.btnEnter.setBounds(189, 86, 76, 86);
		getContentPane().add(this.btnEnter);
		
		if ( btnClear!=null ){
			this.btnClear = btnClear;
		}
		else {
			this.btnClear = new JButton("CLR");
			this.btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clear(e);
				}
			});
		}
		
		this.btnClear.setFont(new Font("Tahoma", Font.BOLD, 12));
		this.btnClear.setBounds(189, 43, 76, 43);
		getContentPane().add(this.btnClear);
    	
		
    	addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        
    }
    
    private void closeDialog(){
    	this.dispose();
    }
    
    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {   
    	
        if(!this.isModal()){
            this.setVisible(false);
            this.dispose();
        }
        
    }
    
    public JTextComponent getTextComponent() {
		return textComponent;
	}

	public void setTextComponent(JTextComponent textComponent) {
		this.textComponent = textComponent;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getCallingObject() {
		return callingObject;
	}

	public void setCallingObject(Object callingObject) {
		this.callingObject = callingObject;
	}
}
