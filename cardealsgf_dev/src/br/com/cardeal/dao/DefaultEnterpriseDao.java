
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
import br.com.caelum.vraptor.ioc.Component;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.Enterprise;
import br.com.cardeal.model.IdentifyLogisticPallet;
import br.com.cardeal.model.IdentifyLogisticProduct;

@Component
public class DefaultEnterpriseDao implements EnterpriseDao {

	private final Session session;

	public DefaultEnterpriseDao(Session session) {
		this.session = session;
	}

	public Enterprise find(int id) {		
		return (Enterprise) session.createCriteria(Enterprise.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}	

	public void add(Enterprise enterprise) {		
		session.save(enterprise);
	}

	public void refresh(Enterprise enterprise) {
		session.refresh(enterprise);
	}

	public void update(Enterprise enterprise) {
		session.merge(enterprise);
	}
	
	public String delete(Enterprise enterprise) 
	{
		try 
		{
			enterprise = find(enterprise.getId());
			if (enterprise != null) 
			{
				session.delete(enterprise);
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
	public List<Enterprise> list() {
		List<Enterprise> list =  (List<Enterprise>)(session.createCriteria(Enterprise.class)).list();
		return list;
	}

	@Override
	public void updateupdateWhitSeqLogistics(Enterprise enterprise, IdentifyLogisticProduct identifyLogisticProduct, IdentifyLogisticPallet identifyLogisticPallet) {
		
		GuiGlobals.refreshDaoFactory();
		GuiGlobals.getDaoFactory().beginTransaction();
		try {			
			
			if ( identifyLogisticProduct != null ) GuiGlobals.getDaoFactory().getIdentifyLogisticProductDao().update( identifyLogisticProduct );
			if ( identifyLogisticPallet != null ) GuiGlobals.getDaoFactory().getIdentifyLogisticPalletDao().update( identifyLogisticPallet );
						
			update(enterprise);
			
			GuiGlobals.getDaoFactory().commit();
			
		}
		catch(Exception e) {
			e.printStackTrace();
			GuiGlobals.getDaoFactory().rollback();
			throw(e);
		}
	}
	
	@Override
	public boolean exportObjEnterpriseToExcel( String fileFullName )
	{
		
		List<Enterprise> enterprises = list();
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( enterprises != null && fileFullName != null )
		{
					
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Empresas");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("ID");
		        cabecalho.createCell(1).setCellValue("CAIXA - SEQUENCIAL ATUAL");
		        cabecalho.createCell(2).setCellValue("PALETE - SEQUENCIAL ATUAL");
		        cabecalho.createCell(3).setCellValue("CAIXA - VARIAVEL LOGISTICA");	        
		        cabecalho.createCell(4).setCellValue("PALETE - VARIAVEL LOGISTICA");
		        cabecalho.createCell(5).setCellValue("CAIXA - SEQUENCIAL MAXIMO");
		        cabecalho.createCell(6).setCellValue("PALETE - SEQUENCIAL MAXIMO");
		        cabecalho.createCell(7).setCellValue("NOME");
		        		
		        linha++;
		        Row dados;
		        for ( Enterprise enterprise : enterprises){
		        
			        dados = sheet.createRow( linha );
			
			        dados.createCell(0).setCellValue(enterprise.getId());
			        dados.createCell(1).setCellValue(enterprise.getCurrentIdBaseBox());
			        dados.createCell(2).setCellValue(enterprise.getCurrentIdBasePallet());
			        dados.createCell(3).setCellValue(enterprise.getCurrentVarLogisctBox());		        
			        dados.createCell(4).setCellValue(enterprise.getCurrentVarLogisctPallet());
			        dados.createCell(5).setCellValue(enterprise.getMaxIdSequenceLogisticBox());
			        dados.createCell(6).setCellValue(enterprise.getMaxIdSequenceLogisticPallet());
			        dados.createCell(7).setCellValue(enterprise.getName());
			        
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
