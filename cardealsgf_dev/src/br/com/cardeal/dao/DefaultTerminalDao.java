
package br.com.cardeal.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.filter.TerminalFilter;
import br.com.cardeal.model.Terminal;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultTerminalDao implements TerminalDao {

	private final Session session;

	public DefaultTerminalDao(Session session) {
		this.session = session;
	}

	public Terminal find(Terminal terminal) {		
		return (Terminal) session.createCriteria(Terminal.class)				
			    .add( Restrictions.eq("id", terminal.getId()) )
			    .uniqueResult();
	}
	
	public Terminal find(String company_id, String terminal_id ) {		
		return findForIdString(company_id, terminal_id);
	}

	public void add(Terminal terminal) {
		session.save(terminal);
	}

	public void refresh(Terminal terminal) {
		session.refresh(terminal);
	}

	public void update(Terminal terminal) {
		session.merge(terminal);
	}

	public void delete(Terminal terminal) {
		terminal = find(terminal);
		if(terminal != null) {
			session.delete(terminal);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Terminal> list( TerminalFilter filter ) {
		
		List<Terminal> list;
		Criteria c = session.createCriteria(Terminal.class);
		
		if ( filter != null ){
			
			if(filter.getCompany() != null)
				c.add(Restrictions.eq("company.id", filter.getCompany().getId()));
			
		}
		
		list = (List<Terminal>) c.list();
		
		return list;
	}	
	
	private Terminal findForIdString( String company_id, String terminal_id ){
		return (Terminal) session.createCriteria(Terminal.class)
				.add( Restrictions.eq("company.id", company_id) )
			    .add( Restrictions.eq("idTerminal", terminal_id) )
			    .uniqueResult();
	}
	
	@Override
	public boolean exportObjTerminalToExcel( TerminalFilter filter, String fileFullName ){
		
		List<Terminal> terminals = list(filter);
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( terminals != null && fileFullName != null )
		{
					
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Terminais");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("ID");
		        cabecalho.createCell(1).setCellValue("DESCRIÇÃO");		        
		        cabecalho.createCell(2).setCellValue("MODELO DE IMPRESSORA");
		        cabecalho.createCell(3).setCellValue("NOME DA IMPRESSORA");
		        cabecalho.createCell(4).setCellValue("PORTA SERIAL DA IMPRESSORA");
		        cabecalho.createCell(5).setCellValue("BITS POR SEGUNDO DA IMPRESSORA ( BAUD RATE )");
		        cabecalho.createCell(6).setCellValue("BITS DE DADOS DA IMPRESSORA ( DATA BITS )");
		        cabecalho.createCell(7).setCellValue("PARIDADE DA IMPRESSORA ( PARITY )");
		        cabecalho.createCell(8).setCellValue("BITS DE PARADA DA IMPRESSORA ( STOP BITS )");		        
		        cabecalho.createCell(9).setCellValue("MODELO DA BALANÇA B1");		        
		        cabecalho.createCell(10).setCellValue("PORTA SERIAL DA BALANÇA B1");
		        cabecalho.createCell(11).setCellValue("BITS POR SEGUNDO DA BALANÇA B1 ( BAUD RATE )");
		        cabecalho.createCell(12).setCellValue("BITS DE DADOS DA BALANÇA B1 ( DATA BITS )");
		        cabecalho.createCell(13).setCellValue("PARIDADE DA BALANÇA B1 ( PARITY )");
		        cabecalho.createCell(14).setCellValue("BITS DE PARADA DA BALANÇA B1 ( STOP BITS )");
		        cabecalho.createCell(15).setCellValue("TEMPO DE ESPERA PARA CAPTURAR PESO NA BALANÇA B1");
		        cabecalho.createCell(16).setCellValue("MODELO DA BALANÇA B2");		        
		        cabecalho.createCell(17).setCellValue("PORTA SERIAL DA BALANÇA B2");
		        cabecalho.createCell(18).setCellValue("BITS POR SEGUNDO DA BALANÇA B2 ( BAUD RATE )");
		        cabecalho.createCell(19).setCellValue("BITS DE DADOS DA BALANÇA B2 ( DATA BITS )");
		        cabecalho.createCell(20).setCellValue("PARIDADE DA BALANÇA B2 ( PARITY )");
		        cabecalho.createCell(21).setCellValue("BITS DE PARADA DA BALANÇA B2 ( STOP BITS )");
		        cabecalho.createCell(22).setCellValue("TEMPO DE ESPERA PARA CAPTURAR PESO NA BALANÇA B2");
		        cabecalho.createCell(23).setCellValue("DIRETÓRIO TEMPORÁRIO DO TERMINAL");
		        		        		
		        linha++;
		        Row dados;
		        for ( Terminal terminal : terminals){
		        
			        dados = sheet.createRow( linha );
			
			        dados.createCell(0).setCellValue(terminal.getIdTerminal());
			        dados.createCell(1).setCellValue(terminal.getDescription());			        
			        dados.createCell(2).setCellValue( terminal.getPrinterModel().getName() );
			        dados.createCell(3).setCellValue( terminal.getPrinterName() );
			        dados.createCell(4).setCellValue( terminal.getPrinterPort() );
			        dados.createCell(5).setCellValue( terminal.getPrinterBaudRate() );
			        dados.createCell(6).setCellValue( terminal.getPrinterDataBits() );
			        dados.createCell(7).setCellValue( terminal.getPrinterParity() );
			        dados.createCell(8).setCellValue( terminal.getPrinterStopBits() );			        
			        dados.createCell(9).setCellValue( terminal.getScale1Model().getName() );			        
			        dados.createCell(10).setCellValue( terminal.getScale1Port() );
			        dados.createCell(11).setCellValue( terminal.getScale1BaudRate() );
			        dados.createCell(12).setCellValue( terminal.getScale1DataBits() );
			        dados.createCell(13).setCellValue( terminal.getScale1Parity() );
			        dados.createCell(14).setCellValue( terminal.getScale1StopBits() );
			        dados.createCell(15).setCellValue( terminal.getTimeWait1() );
			        dados.createCell(16).setCellValue( terminal.getScale2Model().getName() );			        
			        dados.createCell(17).setCellValue( terminal.getScale2Port() );
			        dados.createCell(18).setCellValue( terminal.getScale2BaudRate() );
			        dados.createCell(19).setCellValue( terminal.getScale2DataBits() );
			        dados.createCell(20).setCellValue( terminal.getScale2Parity() );
			        dados.createCell(21).setCellValue( terminal.getScale2StopBits() );
			        dados.createCell(22).setCellValue( terminal.getTimeWait2() );
			        dados.createCell(23).setCellValue( terminal.getTempDirectory() );
			       
			        linha++;
			        
		        }
	        	
		        wb.write(stream);
		        stream.flush();
		        stream.close();
				isImportOk = true;
				
			} catch (FileNotFoundException e) {				
				e.printStackTrace();
				
			}catch (IOException e) {
				e.printStackTrace();
				
			}

		}
		
		return isImportOk;
	}
	
}
