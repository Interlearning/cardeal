package br.com.cardeal.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import br.com.cardeal.model.ItensShipment;
import br.com.cardeal.table.TableItensShipmentModel;

public class Renderizador implements TableCellRenderer {
	
	private final JLabel componenteRenderizador;
	TableItensShipmentModel tableModelItens;	
	
	public Renderizador( TableItensShipmentModel tableModelItens ) {
        componenteRenderizador = new JLabel();
        componenteRenderizador.setOpaque(true);
        this.tableModelItens = tableModelItens;
    }
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object conteudo, boolean selecionada, boolean focada, int lin, int col) {
        // atualizar componente renderizador
        componenteRenderizador.setText(String.valueOf(conteudo));
        componenteRenderizador.setFont(new Font("Tahoma", Font.PLAIN, 18));
//        componenteRenderizador.setHorizontalAlignment(getAlinhamento(col));
        componenteRenderizador.setBackground( getCor(table, lin, col, selecionada, focada) );
        return componenteRenderizador;
    }

    // escolhe a cor a partir da linha
    private Color getCor(JTable table, int linha,int coluna, boolean selecionada, boolean focada) {
    	
    	ItensShipment itemRow = tableModelItens.getdadosDaGrid().get(linha);    	
    	
    	if ( selecionada && coluna == 1 ){
    		return Color.LIGHT_GRAY;
    	}   	        
    	
    	if ( itemRow.getQtdExpedida() == itemRow.getQtdSolicitada() ) {
            return Color.GREEN;
        }
    	else if ( itemRow.getQtdExpedida() < itemRow.getQtdSolicitada() && itemRow.getQtdExpedida() > 0) {
            return Color.YELLOW;
        }
    	else if ( itemRow.getQtdExpedida() > itemRow.getQtdSolicitada() ) {
            return Color.BLUE;
        }
        
        return Color.RED;
    }	

}
