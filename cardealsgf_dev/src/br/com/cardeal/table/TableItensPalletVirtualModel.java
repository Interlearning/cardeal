package br.com.cardeal.table;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.model.ItensPalletVirtual;
import br.com.cardeal.model.TypeOperationPalletVirtual;

public class TableItensPalletVirtualModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private Object[][] colunsGrid;
	private List<ItensPalletVirtual> dadosDaGrid = new ArrayList<>();
	
	// WJSP 06/08/2015
	private int totalPecas = 0;
	private String totalLiq = "0";
	private String totalBruto = "0";
		
	public TableItensPalletVirtualModel(List<ItensPalletVirtual> dadosDaGrid) {
		this.dadosDaGrid.addAll(dadosDaGrid);
		setColunsGrid();
	}

	// Cria as colunas da Grid 
	private void setColunsGrid() 
	{		
		colunsGrid = new String[8][2];
		
		colunsGrid[0][0] = new String("Código");
		colunsGrid[0][1] = new String("F");
		
		colunsGrid[1][0] = new String("Tipo");
		colunsGrid[1][1] = new String("T");
		
		colunsGrid[2][0] = new String("Tara");
		colunsGrid[2][1] = new String("T");
		
		colunsGrid[3][0] = new String("Qtde Caixas");
		colunsGrid[3][1] = new String("T");
		
		colunsGrid[4][0] = new String("Peso Liq.");
		colunsGrid[4][1] = new String("T");
		
		colunsGrid[5][0] = new String("Peso Bruto");
		colunsGrid[5][1] = new String("T");
		
		colunsGrid[6][0] = new String("Total Liq.");
		colunsGrid[6][1] = new String("F");
		
		colunsGrid[7][0] = new String("Total Bruto");
		colunsGrid[7][1] = new String("F");

	}

	public List<ItensPalletVirtual> getDadosGrid() {
		return dadosDaGrid;
	}

	public void setDadosDaGrid(List<ItensPalletVirtual> dadosDaGrid) {
		this.dadosDaGrid = dadosDaGrid;
	}

	/**
	 * 
	 * @param table
	 */
	public void initializeLayout(JTable table) 
	{
		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));
		table.setFont(new Font("SansSerif", Font.PLAIN, 20));
		table.setRowHeight(40);		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);		
		TableColumn column = null;
		for (int i = 0; i <= 7; i++) {
		    column = table.getColumnModel().getColumn(i);
		    if(column == null)
		    	continue;
	    	switch(i) {
	    	case 0: column.setPreferredWidth(145);break;
	    	case 1: column.setPreferredWidth(165);break;
	    	case 2: column.setPreferredWidth(135);break;
	    	case 3: column.setPreferredWidth(115);break;
	    	case 4: column.setPreferredWidth(95);break;
	    	case 5: column.setPreferredWidth(110);break;
	    	case 6: column.setPreferredWidth(90);break;
	    	case 7: column.setPreferredWidth(110);break;
	    	
	    	}
		}	
				
	}
	
	// retorna o nome que será exibido na coluna (o índice da primeira coluna é 0);
    public String getColumnName(int column) {
    	switch(column) {
    	case 0: return (String) colunsGrid[0][0];
    	case 1: return (String) colunsGrid[1][0];
    	case 2: return (String) colunsGrid[2][0];
    	case 3: return (String) colunsGrid[3][0];
    	case 4: return (String) colunsGrid[4][0];
    	case 5: return (String) colunsGrid[5][0];
    	case 6: return (String) colunsGrid[6][0];
    	case 7: return (String) colunsGrid[7][0];
    	default : return "";
    	}
    }
    
    public boolean isCellEditable(int row, int column) 
    {  
    	return colunsGrid[column][1].equals("T");    	
    }  

    @SuppressWarnings("unchecked")
	@Override    
	public Class getColumnClass(int column)
    {
    	if(column == 1)
    	{
    		return getValueAt(0, column).getClass(); 
    	}
    	else{    	
    		return Integer.class; //getValueAt(0, column).getClass();
    	}
    }
	
	@Override
	// retorna o número de linhas que a tabela tem
	public int getRowCount() {
		if(dadosDaGrid == null)
			return 0;
		else
			return dadosDaGrid.size();
	}

	@Override
	//  retorna o número de colunas que a tabela tem
	public int getColumnCount() {
		return colunsGrid.length;
	}

	@Override
	// retorna o conteúdo da célula especificada;
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if(dadosDaGrid == null)	return "";
		
		Object returnValue = null;
		ItensPalletVirtual item = dadosDaGrid.get(rowIndex);
		
		switch(columnIndex) {
		case 0: returnValue =  item.getItensPallet(); break;
		case 1: returnValue =  item.getType();  break;
		case 2: returnValue =  item.getPesoCaixa();  break;
		case 3: returnValue =  item.getQtde();  break;
		case 4: returnValue =  item.getPesoLiq();  break;
		case 5: returnValue =  item.getPesoBruto();  break;
		case 6: returnValue =  item.getTotalLiq();  break;
		case 7: returnValue =  item.getTotalBruto();  break;

		}
		
		return returnValue;
	}
	
	 @Override
	 // método que a JTable chama quando uma célula é editada;
	 public void setValueAt(Object value, int rowIndex, int columnIndex) {
		 
		 ItensPalletVirtual item = dadosDaGrid.get(rowIndex);
         
        switch (columnIndex) {
        
        case 0:
        	item.setItensPallet( (String) value);
            break;
            
        case 1:
        	item.setType( (TypeOperationPalletVirtual) value );
        	calcWeightOfRow( item );
            break;
            
        case 2:
        	item.setPesoCaixa( (String) value);
        	calcWeightOfRow( item );
            break;
            
        case 3:
        	item.setQtde((Integer) value);
        	calcWeightOfRow( item );        	
            break;
            
        case 4:        	
        	item.setPesoLiq( (String) value);
        	calcWeightOfRow( item );        	
            break;
            
        case 5:
        	item.setPesoBruto( (String) value);
        	calcWeightOfRow( item );
            break;  
            
        case 6:
        	item.setTotalLiq( (String) value);
            break;  
        case 7:
        	item.setTotalBruto( (String) value);
            break;
        }  
        
        // Atualiza os dados da linha
        this.fireTableRowsUpdated(rowIndex, rowIndex);
    }
	 
	 public void calcWeightOfRow( ItensPalletVirtual item )
	 {
		 if ( item.getType().getName().equals( TypeOperationPalletVirtual.TYPE_STANDARD )  )
     	{
     		item.setTotalLiq(Utils.formatWeight( (Integer) item.getQtde() * Double.parseDouble( item.getPesoLiq())));
         	item.setTotalBruto(Utils.formatWeight( (Integer) item.getQtde() * Double.parseDouble( item.getPesoBruto())));
     	}
     	else if ( item.getType().getName().equals( TypeOperationPalletVirtual.TYPE_VARIABLE )  )
     	{        		
     		item.setTotalLiq(Utils.formatWeight( Double.parseDouble( item.getPesoLiq() ) ) );
     		item.setTotalBruto(Utils.formatWeight( ( (Integer) item.getQtde() * Double.parseDouble( item.getPesoCaixa() ) ) + Double.parseDouble( item.getPesoBruto() ) ) );
     	}
	 }

	public void totalizeValues()
	{
		int totalPc = 0;
		float pesoLiq = 0;
		float pesoBruto = 0;
						
		for(int i =0; i < dadosDaGrid.size();i++){			
			totalPc += dadosDaGrid.get(i).getQtde();	
			pesoLiq += Float.parseFloat(dadosDaGrid.get(i).getTotalLiq());
			pesoBruto += Float.parseFloat(dadosDaGrid.get(i).getTotalBruto());
		}			
		this.totalPecas = totalPc;	
		this.totalLiq = Utils.formatWeight(pesoLiq);
		this.totalBruto = Utils.formatWeight(pesoBruto); 
	}
	
	public int getTotalPecas(){		
		return totalPecas;
	}
	
	public String getTotalLiq(){
		return totalLiq;
	}
	
	public String getTotalBruto(){
		return totalBruto;
	}
}
