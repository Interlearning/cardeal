package br.com.cardeal.table;

import javax.swing.table.AbstractTableModel;


public class TempTableEmptyModel extends AbstractTableModel  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TempTableEmptyModel() {
    }

    public String getColumnName(int column) {
        return "";
    }

    public boolean isCellEditable(int row, int column) {
    	return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int column) {
    	return String.class;
    }

    public Object getValueAt(int row, int column) {
    	return ""; 
    }

    public void setValueAt(Object value, int row, int column) {
        //fireTableCellUpdated(row, column);
    }

    public int getRowCount() {
        return 0;
    }

    public int getColumnCount() {
        return 0;
    }
}