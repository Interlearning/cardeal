
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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.enums.PartnerStyle;
import br.com.cardeal.filter.PartnerFilter;
import br.com.cardeal.model.Partner;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultPartnerDao implements PartnerDao {

	private final Session session;

	public DefaultPartnerDao(Session session) {
		this.session = session;
	}

	public Partner findById(int id) {		
		return (Partner) session.createCriteria(Partner.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}
	
	public Partner findByIdExternal(String idExternal) {		
		return (Partner) session.createCriteria(Partner.class)
			    .add( Restrictions.eq("codigoExterno", idExternal) )
			    .uniqueResult();
	}
	
	public Partner findByName(String name) {		
		return (Partner) session.createCriteria(Partner.class)
			    .add( Restrictions.like("name", name).ignoreCase() )
			    .uniqueResult();
	}

	public Partner findByCnpj(String cnpj) {		
		return (Partner) session.createCriteria(Partner.class)
			    .add( Restrictions.eq("cnpj", cnpj) )
			    .uniqueResult();
	}

	public void add(Partner partner) {
		session.save(partner);
	}

	public void refresh(Partner partner) {
		session.refresh(partner);
	}

	public void update(Partner partner) {
		session.merge(partner);
	}

	public String delete(Partner partner) 
	{
		try 
		{
			partner = findById(partner.getId());
			if (partner != null) 
			{
				session.delete(partner);
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
	public List<Partner> list(PartnerFilter filter) {
		
		List<Partner> list;
		if(filter == null)
			list = (List<Partner>)(session.createCriteria(Partner.class)).list();
		else {
			Criteria c = session.createCriteria(Partner.class);
			
			if(filter.getText() != null) {

				c.add(Restrictions.disjunction()
						.add( Restrictions.like("name", "%" + filter.getText() + "%"))
						.add( Restrictions.eq("id", filter.getText())) 
						);				
			}
			
			if(filter.getPartnerStyle() != null && filter.getPartnerStyle() != PartnerStyle.UNDEFINED) {
				c.add( Restrictions.eq("partnerStyle", filter.getPartnerStyle()));
			}
			
			if( filter.isAllPartners() ) 
			{			
				Criterion param1 = Restrictions.eq("partnerStyle", PartnerStyle.SUPPLIER);
				Criterion param2 = Restrictions.eq("partnerStyle", PartnerStyle.CUSTOMER_AND_SUPPLIER); 
				LogicalExpression orExp = Restrictions.or(param1,param2); 
				c.add(orExp);
				
			}
			
			if( filter.isOnlyCodExternal() ) 
			{
				c.add( Restrictions.and( Restrictions.isNotNull("codigoExterno"), Restrictions.ne("codigoExterno","") ) );				
			}
			
			list =  (List<Partner>)c.list();
			
		}
		return list;
	}
	
	@Override
	public boolean exportObjPartnerToExcel( PartnerFilter filter, String fileFullName ){
		
		List<Partner> partners = list(filter);
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( partners != null && fileFullName != null )
		{
					
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Clientes-Fornecedores");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("ID");
		        cabecalho.createCell(1).setCellValue("CNPJ");
		        cabecalho.createCell(2).setCellValue("CODIGO EXTERNO");
		        cabecalho.createCell(3).setCellValue("NOME");
		        cabecalho.createCell(4).setCellValue("TIPO");
		        		
		        linha++;
		        Row dados;
		        for ( Partner partner : partners){
		        
			        dados = sheet.createRow( linha );
			
			        dados.createCell(0).setCellValue(partner.getId());
			        dados.createCell(1).setCellValue(partner.getFormattedCnpj());
			        dados.createCell(2).setCellValue(partner.getCodigoExterno());
			        dados.createCell(3).setCellValue(partner.getName());
			        dados.createCell(4).setCellValue(partner.getPartnerStyle().getName());
			       
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
