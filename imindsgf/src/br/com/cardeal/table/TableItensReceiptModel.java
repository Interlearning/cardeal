package br.com.cardeal.table;

import java.awt.Font;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import br.com.cardeal.model.ItensReceipt;

public class TableItensReceiptModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private Object[][] colunsGrid;
	private List<ItensReceipt> dadosDaGrid;
	
	public TableItensReceiptModel(List<ItensReceipt> dadosDaGrid) {
		this.dadosDaGrid = dadosDaGrid;
		setColunsGrid();
	}

	private void setColunsGrid() 
	{
		colunsGrid = new String[10][2];
		
		colunsGrid[0][0] = new String("Produto");
		colunsGrid[0][1] = new String("F");
		
		colunsGrid[1][0] = new String("Descrição");
		colunsGrid[1][1] = new String("F");
		
		colunsGrid[2][0] = new String("Qtd.");
		colunsGrid[2][1] = new String("F");
		
		colunsGrid[3][0] = new String("Tara");
		colunsGrid[3][1] = new String("F");
		
		colunsGrid[4][0] = new String("P. Liquido");
		colunsGrid[4][1] = new String("F");
		
		colunsGrid[5][0] = new String("Lote Ex.");
		colunsGrid[5][1] = new String("F");
		
		colunsGrid[6][0] = new String("Dt.Validade");
		colunsGrid[6][1] = new String("F");
		
		colunsGrid[7][0] = new String("Dest.");
		colunsGrid[7][1] = new String("F");
	}

	public List<ItensReceipt> getDadosDaGrid() 
	{
		return dadosDaGrid;
	}

	public void setDadosDaGrid(List<ItensReceipt> dadosDaGrid) 
	{
		this.dadosDaGrid = dadosDaGrid;
	}

	/**
	 * 
	 * @param table
	 */
	public void initializeLayout(JTable table) 
	{
		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));
		table.setFont(new Font("SansSerif", Font.PLAIN, 16));
		table.setRowHeight(40);		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);		
		TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) 
		{
		    column = table.getColumnModel().getColumn(i);
		    if(column == null)
		    	continue;
		    
	    	switch(i) 
	    	{
		    	case 0: column.setPreferredWidth(80);break;
		    	case 1: column.setPreferredWidth(270);break;
		    	case 2: column.setPreferredWidth(80);break;
		    	case 3: column.setPreferredWidth(80);break;
		    	case 4: column.setPreferredWidth(105);break;
		    	case 5: column.setPreferredWidth(200);break;
		    	case 6: column.setPreferredWidth(100);break;
		    	case 7: column.setPreferredWidth(58);break;
	    	}
		}	
				
	}
	
    public String getColumnName(int column) 
    {
    	switch(column) 
    	{
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
    	return "T".equals( colunsGrid[column][1] );   
    }  

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int column) 
    {
    	switch(column) 
		{			
			case 0: return String.class;
			case 1: return String.class;
			case 2: return Integer.class;
			case 3: return String.class;
			case 4: return String.class;
			case 5: return String.class;
			case 6: return String.class;
			case 7: return String.class;
			default: return String.class;
		}
    }
	
	@Override
	public int getRowCount() 
	{
		if(dadosDaGrid == null)
			return 0;
		else
			return dadosDaGrid.size();
	}

	@Override
	public int getColumnCount() 
	{
		return colunsGrid.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		if(dadosDaGrid == null)
			return "";
		
		switch(columnIndex) 
		{
			case 0: return dadosDaGrid.get(rowIndex).getProduct().getIdMasc();
			case 1: return dadosDaGrid.get(rowIndex).getProduct().getDescription();
			case 2: return dadosDaGrid.get(rowIndex).getQuantidade();
			case 3: return dadosDaGrid.get(rowIndex).getTaraFormatted();
			case 4: return dadosDaGrid.get(rowIndex).getPesoLiqFormatted();
			case 5: return dadosDaGrid.get(rowIndex).getBatchExternal();
			case 6: return dadosDaGrid.get(rowIndex).getDateValidateBatchExternalFormatted();
			case 7: return dadosDaGrid.get(rowIndex).getProduct().getTypeStock().getSigla();
			default: return "";
		}
	}

}
