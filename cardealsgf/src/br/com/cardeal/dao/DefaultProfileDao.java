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

import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.model.Profile;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultProfileDao implements ProfileDao {

	private final Session session;

	public DefaultProfileDao(Session session) {
		this.session = session;
	}

	public Profile find(int id) {		
		return (Profile) session.createCriteria(Profile.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}

	public void add(Profile profile) {
		session.save(profile);
	}

	public void refresh(Profile profile) {
		session.refresh(profile);
	}

	public void update(Profile profile) {
		session.merge(profile);
	}
	
	public String delete(Profile profile) 
	{
		try 
		{
			profile = find(profile.getId());
			if (profile != null) 
			{
				session.delete(profile);
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
	public List<Profile> list() {
		List<Profile> list =  (List<Profile>)(session.createCriteria(Profile.class)).list();
		return list;
	}
	
	@Override
	public boolean exportObjProfileToExcel( String fileFullName ){
		
		List<Profile> profiles = list();
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( profiles != null && fileFullName != null )
		{
					
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Perfis de Acesso");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("ID");
		        cabecalho.createCell(1).setCellValue("DESCRIÇÃO");
		        cabecalho.createCell(2).setCellValue("PERMITIDO ?");
		        		
		        linha++;
		        Row dados;
		        for ( Profile profile : profiles)
		        {
		        
		        	dados = sheet.createRow( linha );
			
			        dados.createCell(0).setCellValue(profile.getId());
			        dados.createCell(1).setCellValue(profile.getName());			       
			        linha++;
			        
			        if ( profile.getName().trim().toUpperCase().equals("ADMINISTRADORES") || ( profile.getDeniedRoles() != null && profile.getDeniedRoles().size() > 0 ) )
			        {
			        	List<ComponentPermission> componentsPermission = ComponentPermission.getAllComponents();
			        	List<String> deniedRoles = profile.getDeniedRoles();

			        	Row dadosPermissoes = sheet.createRow( linha );
			        	for ( ComponentPermission component : componentsPermission )
			        	{
			        		dadosPermissoes = sheet.createRow( linha );
				        	dadosPermissoes.createCell(0).setCellValue("");
				        	dadosPermissoes.createCell(1).setCellValue("PERMISSÃO " + component.getRoleFormattedToDeniedRoles() );
			        		if ( profile.getName().trim().toUpperCase().equals("ADMINISTRADORES") || deniedRoles.contains( component.getRoleFormattedToDeniedRoles() ) )
			        		{
			        			dadosPermissoes.createCell(2).setCellValue("SIM");
			        		}
			        		else
			        		{
			        			dadosPermissoes.createCell(2).setCellValue("NÃO");
			        		}
			        		linha++;
			        	}
			        	
				        	
			        }
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
