package br.com.cardeal.table;

import java.awt.Font;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.ItensShipment;

public class TableItensShipmentModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;	
	private Object[][] colunsGrid;
	private List<ItensShipment> dadosDaGrid;
	
	public TableItensShipmentModel(List<ItensShipment> dadosDaGrid) {
		this.dadosDaGrid = dadosDaGrid;
		setColunsGrid();
	}
	
	private void setColunsGrid() 
	{
		colunsGrid = new String[9][2];
		
		colunsGrid[0][0] = new String("Item");
		colunsGrid[0][1] = new String("F");
		
		colunsGrid[1][0] = new String(GuiGlobals.getBundle().getString("STR000020"));
		colunsGrid[1][1] = new String("F");
		
		colunsGrid[2][0] = new String(GuiGlobals.getBundle().getString("STR000021"));
		colunsGrid[2][1] = new String("F");
		
		colunsGrid[3][0] = new String("Qtd. Sol.");
		colunsGrid[3][1] = new String("F");
		
		colunsGrid[4][0] = new String("UM.Sol.");
		colunsGrid[4][1] = new String("F");
		
		colunsGrid[5][0] = new String("Expedido");
		colunsGrid[5][1] = new String("F");
		
		colunsGrid[6][0] = new String("Falta");
		colunsGrid[6][1] = new String("F");
		
		colunsGrid[7][0] = new String("Tot.Venda");
		colunsGrid[7][1] = new String("F");
		
		colunsGrid[8][0] = new String("UM.Venda");
		colunsGrid[8][1] = new String("F");
	}

	public List<ItensShipment> getdadosDaGrid() {
		return dadosDaGrid;
	}

	public void setDadosDaGrid(List<ItensShipment> dadosDaGrid) {
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
		table.setRowHeight(39);		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);		
		TableColumn column = null;
		for (int i = 0; i <= (getColumnCount()-1); i++) {
		    column = table.getColumnModel().getColumn(i);
		    if(column == null)
		    	continue;
	    	switch(i) {
	    	case 0: column.setPreferredWidth(50); break;
	    	case 1: column.setPreferredWidth(75);break;
	    	case 2: column.setPreferredWidth(250);break;
	    	case 3: column.setPreferredWidth(110);break;
	    	case 4: column.setPreferredWidth(78);break;
	    	case 5: column.setPreferredWidth(110);break;
	    	case 6: column.setPreferredWidth(110);break;
	    	case 7: column.setPreferredWidth(110);break;
	    	case 8: column.setPreferredWidth(110);break;
	    	default : column.setPreferredWidth(100);break;
	    	}
		}	
				
	}
	
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
    	case 8: return (String) colunsGrid[8][0];
    	default : return "";
    	}
    }
    
    public boolean isCellEditable(int row, int column) {  
    	return "T".equals( colunsGrid[column][1] );   
    }  

    @SuppressWarnings({ "unchecked", "rawtypes" })
   	public Class getColumnClass(int column) 
    {
       return String.class;
    }
	
	@Override
	public int getRowCount() {
		if(dadosDaGrid == null)
			return 0;
		else
			return dadosDaGrid.size();
	}

	@Override
	public int getColumnCount() {
		return colunsGrid.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(dadosDaGrid == null)
			return "";
		
		switch(columnIndex) 
		{
			case 0: return dadosDaGrid.get(rowIndex).getItem();
			case 1: return dadosDaGrid.get(rowIndex).getProduct().getIdMasc();
			case 2: return dadosDaGrid.get(rowIndex).getProduct().getDescription().trim();
			case 3: return dadosDaGrid.get(rowIndex).getQtdSolicitadaFormatted();
			case 4: return dadosDaGrid.get(rowIndex).getUnitId();
			case 5: return dadosDaGrid.get(rowIndex).getQtdExpedidaFormatted();
			case 6: return dadosDaGrid.get(rowIndex).getQtdFaltanteFormatted();
			case 7: return dadosDaGrid.get(rowIndex).getTotalFormatted();
			case 8: return dadosDaGrid.get(rowIndex).getTotalUnitId();		
		}
		
		return "";
	}

}
