
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
import br.com.cardeal.model.Company;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultCompanyDao implements CompanyDao {

	private final Session session;

	public DefaultCompanyDao(Session session) {
		this.session = session;
	}

	public Company find(String id) {		
		return (Company) session.createCriteria(Company.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}

	public void add(Company company) {		
		session.save(company);
	}

	public void refresh(Company company) {
		session.refresh(company);
	}

	public void update(Company company) {
		session.merge(company);
	}

	public String delete(Company company) 
	{
		try 
		{
			company = find(company.getId());
			if (company != null) 
			{
				session.delete(company);
				session.flush();
			}
		} 
		catch (Exception e) 
		{
			return e.getCause().getMessage();
		} 
		finally 
		{
			session.clear();
		}
		
		return "OK";

	}

	@SuppressWarnings("unchecked")
	public List<Company> list() {
		List<Company> list =  (List<Company>)(session.createCriteria(Company.class)).list();
		return list;
	}
	
	@Override
	public boolean exportObjCompanyToExcel( String fileFullName ){
		
		List<Company> companies = list();
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( companies != null && fileFullName != null )
		{
					
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Filiais");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("ID");
		        cabecalho.createCell(1).setCellValue("DESCRIÇÃO");
		        		
		        linha++;
		        Row dados;
		        for ( Company company : companies){
		        
			        dados = sheet.createRow( linha );
			
			        dados.createCell(0).setCellValue(company.getId());
			        dados.createCell(1).setCellValue(company.getName());
			       
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
