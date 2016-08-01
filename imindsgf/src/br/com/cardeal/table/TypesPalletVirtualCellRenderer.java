package br.com.cardeal.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.cardeal.model.TypeOperationPalletVirtual;
 
@SuppressWarnings("serial")
public class TypesPalletVirtualCellRenderer extends DefaultTableCellRenderer {
     
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	
        if (value instanceof TypeOperationPalletVirtual) {
        	TypeOperationPalletVirtual type = (TypeOperationPalletVirtual) value;
            setText(type.getName());
        }
         
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getSelectionForeground());
        }
         
        return this;
    }
     
}