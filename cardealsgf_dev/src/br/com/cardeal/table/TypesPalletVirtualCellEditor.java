package br.com.cardeal.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import br.com.cardeal.model.TypeOperationPalletVirtual;
 
@SuppressWarnings("serial")
public class TypesPalletVirtualCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
 
    private TypeOperationPalletVirtual type;
    private List<TypeOperationPalletVirtual> listTypes;
     
    public TypesPalletVirtualCellEditor(List<TypeOperationPalletVirtual> listTypes) {
        this.listTypes = listTypes;
    }
     
    @Override
    public Object getCellEditorValue() {
        return this.type;
    }
 
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
    {
        if (value instanceof TypeOperationPalletVirtual) {
            this.type = (TypeOperationPalletVirtual) value;
        }
         
        JComboBox<TypeOperationPalletVirtual> comboType = new JComboBox<TypeOperationPalletVirtual>();
         
        for (TypeOperationPalletVirtual item : listTypes) 
        {
        	comboType.addItem(item);        	
        }
        comboType.setSelectedItem(type);
        comboType.addActionListener(this);
                
        if (isSelected) 
        {
        	comboType.setBackground(table.getSelectionBackground());
        } else {
        	comboType.setBackground(table.getSelectionForeground());
        }       
         
        return comboType;
    }
 
    @SuppressWarnings("unchecked")
	@Override
    public void actionPerformed(ActionEvent event) {        
		JComboBox<TypeOperationPalletVirtual> comboType = (JComboBox<TypeOperationPalletVirtual>) event.getSource();
        this.type = (TypeOperationPalletVirtual) comboType.getSelectedItem();
    }
 
}

