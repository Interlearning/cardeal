package br.com.cardeal.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.model.Unit;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultUnitDao implements UnitDao {

	private final Session session;

	public DefaultUnitDao(Session session) {
		this.session = session;
	}

	public Unit find(String id) {		
		return (Unit) session.createCriteria(Unit.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}

	public void add(Unit unit) {
		session.save(unit);
	}

	public void refresh(Unit unit) {
		session.refresh(unit);
	}

	public void update(Unit unit) {
		session.merge(unit);
	}
	
	
	/**
	 * @author samuel
	 * metodo alterado para acertar 
	 * um erro 500 que explodia na tela quando o
	 * banco rejeitava a exclusão de uma fk 
	 * 
	 */
	public void delete(Unit unit) {
	
		 try {
			 if (unit != null) {
		        session.delete(unit);
		        session.flush();
			 }
		    } catch (Exception e) {
		        throw e;
		    } finally {
		        session.clear();
		    }
	}

	@SuppressWarnings("unchecked")
	public List<Unit> list() {
		List<Unit> list =  (List<Unit>)(session.createCriteria(Unit.class)).list();
		return list;
	}
	
	@Override
	public boolean exportObjUnitToExcel( String fileFullName ){
		
		List<Unit> units = list();
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( units != null && fileFullName != null )
		{
					
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Unidades");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("ID");
		        cabecalho.createCell(1).setCellValue("DESCRIÇÃO");
		        		
		        linha++;
		        Row dados;
		        for ( Unit unit : units){
		        
			        dados = sheet.createRow( linha );
			
			        dados.createCell(0).setCellValue(unit.getId());
			        dados.createCell(1).setCellValue(unit.getDescription());
			       
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
