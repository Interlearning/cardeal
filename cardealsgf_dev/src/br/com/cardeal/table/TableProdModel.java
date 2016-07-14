package br.com.cardeal.table;

import java.awt.Font;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import br.com.cardeal.model.StockTotal;

public class TableProdModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<StockTotal> total;
	
	public TableProdModel(List<StockTotal> total) {
		this.total = total;
	}

	public List<StockTotal> getTotal() {
		return total;
	}

	public void setTotal(List<StockTotal> total) {
		this.total = total;
	}

	public void initializeLayout(JTable table) {
		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 20));
		table.setFont(new Font("SansSerif", Font.PLAIN, 20));
		table.setRowHeight(26);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn column = null;
		for (int i = 0; i <= 4; i++) {
		    column = table.getColumnModel().getColumn(i);
		    if(column == null)
		    	continue;
	    	switch(i) {
	    	case 0: column.setPreferredWidth(130);break;
	    	case 1: column.setPreferredWidth(480);break;
	    	case 2: column.setPreferredWidth(102);break;
	    	case 3: column.setPreferredWidth(102);break;
	    	case 4: column.setPreferredWidth(200);break;
	    	default : column.setPreferredWidth(100);break;
	    	}
		}	
	}
	
    public String getColumnName(int column) {
    	switch(column) {
    	case 0: return "Produto";
    	case 1: return "Descrição";
    	case 2: return "Caixas";
    	case 3: return "Peças";
    	case 4: return "Peso Líquido(kg)";
    	default : return "";
    	}
    }
    
    public boolean isCellEditable(int row, int column) {
    	return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int column) {
    	return String.class;
    }
    
	
	
	@Override
	public int getRowCount() {
		if(total == null)
			return 0;
		else
			return total.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(total == null)
			return "";
		
		switch(columnIndex) {
		case 0: return  total.get(rowIndex).getProduct().getIdMasc();
		case 1: return total.get(rowIndex).getProduct().getDescription();
		case 2: return String.valueOf(total.get(rowIndex).getSecondaryQty());
		case 3: return String.valueOf(total.get(rowIndex).getPrimaryQty());
		case 4: return String.format("%.3f", total.get(rowIndex).getNet());
		}
		
		return "";
	}

}
