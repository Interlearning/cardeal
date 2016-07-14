package br.com.cardeal.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.enums.Operation;
import br.com.cardeal.filter.HistoricFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Setup;
import br.com.cardeal.model.Historic;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultHistoricDao implements HistoricDao {

	private final Session session;

	public DefaultHistoricDao(Session session) {
		this.session = session;
	}

	public Historic find(long id) {		
		return (Historic) session.createCriteria(Historic.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}

	public void add(Historic historic) {
		session.save(historic);
	}

	public void refresh(Historic historic) {
		session.refresh(historic);
	}

	public void update(Historic historic) {
		session.merge(historic);
	}

	public void delete(Historic historic) {
		historic = find(historic.getId());
		if(historic != null) {
			session.delete(historic);
		}
	}
	
	public void delete(List<Historic> historics) 
	{
		if ( historics != null )
		{
			for ( Historic historic : historics) 
			{
				session.delete(historic);
			}
		}
	}

	@SuppressWarnings("resource")
	@Override
	public boolean exportObjHistoricToExcel(HistoricFilter filter){
		
		List<Historic> historical = list(filter);
		DaoFactory daoFactory = new DaoFactory();
		short line = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		String fileName = "historic.xls";

			/**
			 * se pallets for direferente de null gera arquivo excel
			 */
			if(historical != null){
				
				Workbook wb = new HSSFWorkbook();
				
					try {
						/**
						 * buferriza a planilha
						 */
							stream = new FileOutputStream(daoFactory.getServerSetupDao().find().getStoreDirectory() + GuiGlobals.getSeparador() + fileName );
						
						/**
						 * instancia objetos para montar a planilha
						 */
							Sheet sheet = wb.createSheet("Historico");
							Row header = sheet.createRow(line);
							
						/**
						 * gera as celulas da planilha
						 */
							header.createCell(0).setCellValue("Data");
							header.createCell(1).setCellValue("Usuário");
							header.createCell(2).setCellValue("Filial");
							header.createCell(3).setCellValue("Terminal");
							header.createCell(4).setCellValue("Ação");
							
							// o for ira varrer  o hibernate
							line++;
							
							for(Historic historic : historical){
						
								/**
								 * resgata dados da base e usa o objeto Row
								 * para armazenar na variavel data.
								 * armazenando em data, cria uma nova celula e 
								 * seta o valor para dentro dela
								 */
								Row data = sheet.createRow( line );
								
								data.createCell(0).setCellValue(historic.getDate());
								data.createCell(1).setCellValue(historic.getUser().getName());
								data.createCell(2).setCellValue(historic.getCompany().getId());
								data.createCell(3).setCellValue(historic.getTerminal().getId());
								data.createCell(4).setCellValue(historic.getOperation().getName());
								line++;
							}
								/**
								 * gera stream e grava dados na planilha
								 */
								wb.write(stream);
								stream.flush();
								stream.close();
								isImportOk = true;
								
						} catch(FileNotFoundException e){
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
			}
		
			return isImportOk;
	}

	
	
	@SuppressWarnings("unchecked")
	public List<Historic> list(HistoricFilter filter) {
		List<Historic> list;
		
		if(filter == null) {
			list =  (List<Historic>)(session.createCriteria(Historic.class)).list();
		}
		else 
		{
			Criteria c = session.createCriteria(Historic.class);
			
			if(filter.getProductId() > 0){					
				c.createCriteria("product").add(Restrictions.eq("id", filter.getProductId()));
			}

			if(filter.getPartnerId() > 0){					
				c.createCriteria("partner").add(Restrictions.eq("id", filter.getPartnerId()));
			}
			
			if(filter.getUser() != null && filter.getUser().getId() > 0){					
				c.createCriteria("user").add(Restrictions.eq("id", filter.getUser().getId() ) );
			}

			if(filter.getBatch() != null && filter.getBatch().length() > 0){					
				c.add(Restrictions.eq("batch", filter.getBatch()));
			}
			
			if(filter.getDate1() != null){					
				c.add(Restrictions.ge("date", filter.getDate1()));
			}				
			
			if(filter.getDate2() != null){					
				c.add(Restrictions.le("date", filter.getDate2()));
			}
			
			if(filter.getOrder() != null)
			{								
				c.createCriteria("order").add(Restrictions.eq("id", filter.getOrder().getId() ) );				
			}
			
			if(filter.getOrderItem() != null)
			{								
				c.createCriteria("orderItem").add(Restrictions.eq("id", filter.getOrderItem().getId() ) );				
			}
			
			if(filter.getStockId() > 0)
			{								
				c.createCriteria("stock").add(Restrictions.eq("id", filter.getStockId() ) );				
			}
			
			c.addOrder(Order.desc("id"));

			list =  (List<Historic>)c.list();
			
		}
		
		return list;
	}

	@Override
	public long getMaxIdStockHistoricRollBackByDate(Date date) {
		
		long idReturn = 0;
		Date dateInit = DateTimeUtils.buildInitDate(date);
		
		Criteria c = session.createCriteria(Historic.class);
		c.add(Restrictions.eq("operation", Operation.STOCK_ROLLBACK));
		c.add(Restrictions.eq("terminal.id", Setup.getTerminal().getId()));
		c.add(Restrictions.ge("date", dateInit));
		
		@SuppressWarnings("unchecked")
		List<Historic> historics = (List<Historic>) c.list();
		
		if ( historics != null && historics.size() > 0){
			Collections.sort(historics, new CustomComparatorDesc());
			idReturn = historics.get( historics.size()-1 ).getStock().getId();
		}		
		
		return idReturn;
	}
	
	private class CustomComparatorDesc implements Comparator<Historic> {
	    public int compare(Historic p1, Historic p2) {
	    	return p1.getStock().getId() < p2.getStock().getId() ? -1 : (p1.getStock().getId() > p2.getStock().getId() ? +1 : 0);
	    }
	}

}
