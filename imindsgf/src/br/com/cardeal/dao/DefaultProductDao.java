
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
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.filter.ProductFilter;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.model.Product;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultProductDao implements ProductDao {

	private final Session session;

	public DefaultProductDao(Session session) {
		this.session = session;
	}

	public Product find(int id) {		
		return (Product) session.createCriteria(Product.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}

	public Product findFilterBlocked(String idMasc) {		
		return (Product) session.createCriteria(Product.class)
			    .add( Restrictions.eq("idMasc", idMasc) )
			    .add( Restrictions.eq("blocked", false) )
			    .uniqueResult();
	}

	public void add(Product product) {
		session.save(product);
	}

	public void refresh(Product product) {
		session.refresh(product);
	}

	public void update(Product product) {
		session.merge(product);
	}

	/**
	 * public void delete(Product product) {
	 
		product = find(product.getId());
		if(product != null) {
			session.delete(product);
		}
	}
	
	
	/**
	 * @author samuel
	 * metodo alterado para acertar 
	 * um erro 500 que explodia na tela quando o
	 * banco rejeitava a exclusão de uma fk 
	 * 
	 */
	public void delete(Product product) 
	{
		try 
		{
			product = find(product.getId());
			if (product != null) 
			{
				session.delete(product);
				session.flush();
			}
		} 
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			session.clear();
		}
	}
	@SuppressWarnings("unchecked")
	public List<Product> list(ProductFilter filter) {
		List<Product> list;
		
		if(filter == null)
			list =  (List<Product>)(session.createCriteria(Product.class)).list();
		else 
		{
			Criteria c = session.createCriteria(Product.class);
			if( filter.getDescription() != null) 
			{
				c.add(Restrictions.like("description", "%" + filter.getDescription() + "%"));
			}
			
			if(filter.getIdMasc() != null) 
			{
				c.add( Restrictions.eq("idMasc", filter.getIdMasc()));
			}
			
			if ( filter.getTypeStock() != TypeStock.TODOS) {
				c.add( Restrictions.eq("typeStock", filter.getTypeStock()));
			}
			
			if ( filter.isFilterToMP() )
			{
				c.add( Restrictions.not( Restrictions.eq("typeStock", TypeStock.EMBALAGEM ) ) );
				c.add( Restrictions.not( Restrictions.eq("typeStock", TypeStock.GRANEL ) ) );
				c.add( Restrictions.not( Restrictions.eq("typeStock", TypeStock.TODOS ) ) );
				c.add( Restrictions.not( Restrictions.eq("typeStock", TypeStock.TRANSITO ) ) );
			}
			
			if(filter.getWeighingStyle() != WeighingStyle.UNDEFINED) {
				c.add( Restrictions.eq("weighingStyle", filter.getWeighingStyle()));
			}
			if(!filter.isEnabledToShowBlocked()) {
				c.add( Restrictions.eq("blocked", false));
			}
			
			list =  (List<Product>)c.list();
		}
		
		return list;
	}

	@Override
	public Product findByEan13(String ean13) {
		return (Product) session.createCriteria(Product.class)
			    .add( Restrictions.eq("ean13", ean13) )
			    .uniqueResult();
	}

	@Override
	public Product findByDun14(String dun14) {
		return (Product) session.createCriteria(Product.class)
			    .add( Restrictions.eq("dun14", dun14) )
			    .uniqueResult();
	}
	
	@Override
	public Product findByIdMasc(String idMasc) 
	{
		return (Product) session.createCriteria(Product.class)
			    .add( Restrictions.eq("idMasc", idMasc) )
			    .uniqueResult();
	}
		
	@Override
	public boolean exportObjProductToExcel( ProductFilter filter, String fileFullName )
	{
		
		List<Product> products = list(filter);
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( products != null && fileFullName != null )
		{
					
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Produtos");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("ID");
		        cabecalho.createCell(1).setCellValue("DESCRIÇÃO");
		        cabecalho.createCell(2).setCellValue("QTD. CARCTERES NA ETQ.");
		        cabecalho.createCell(3).setCellValue("FRASE CONSERVAÇÃO");	        
		        cabecalho.createCell(4).setCellValue("FRASE SIF");
		        cabecalho.createCell(5).setCellValue("EAN13");
		        cabecalho.createCell(6).setCellValue("DUN14");
		        cabecalho.createCell(7).setCellValue("MODELO ETIQUETA CAIXA");
		        cabecalho.createCell(8).setCellValue("MODELO ETIQUETA PALETE");
		        cabecalho.createCell(9).setCellValue("DATA CRIAÇÃO");
		        cabecalho.createCell(10).setCellValue("PERMITE ALTERAR QTD CAIXA ?");
		        cabecalho.createCell(11).setCellValue("PERMITE ALTERAR QTD PEÇA ?");
		        cabecalho.createCell(12).setCellValue("PESO MAXIMO");
		        cabecalho.createCell(13).setCellValue("PESO MÍNIMO");
		        cabecalho.createCell(14).setCellValue("TARA CAIXA");
		        cabecalho.createCell(15).setCellValue("TARA EMBALAGEM");
		        cabecalho.createCell(16).setCellValue("PESO LIQUIDO (ETIQUETA)");
		        cabecalho.createCell(17).setCellValue("UNIDADE");
		        cabecalho.createCell(18).setCellValue("QTD DIAS DE VALIDADE");
		        cabecalho.createCell(19).setCellValue("QTD PARA FECHAR 1 PALLET");
		        cabecalho.createCell(20).setCellValue("TIPO ESTOQUE");
		        cabecalho.createCell(21).setCellValue("TIPO PESAGEM");
		        cabecalho.createCell(22).setCellValue("BLOQUEADO ?");
		        cabecalho.createCell(23).setCellValue("FIFO ?");
		        cabecalho.createCell(24).setCellValue("PESO PADRÃO");
		        cabecalho.createCell(25).setCellValue("% PARA REMOVER PRODUTO DA BALANÇA");
		        cabecalho.createCell(26).setCellValue("GS1 - PREFIXO DA EMPRESA");
		        cabecalho.createCell(27).setCellValue("SEQUÊNCIA DE PROCESSO");
		        cabecalho.createCell(28).setCellValue("QUANTIDADE POR EMBALAGEM");
		        cabecalho.createCell(29).setCellValue("VIRTUAL ?");
		        cabecalho.createCell(20).setCellValue("UNIDADE EMBALAGEM (EXPEDIÇÃO)");
		        cabecalho.createCell(31).setCellValue("UNIDADE PARA VENDA");
		        cabecalho.createCell(32).setCellValue("PERCENTUAL LIBERADO PARA COMPLEMENTO");
		        cabecalho.createCell(33).setCellValue("CODIGO SIF");
		
		        linha++;
		        Row dados;
		        for ( Product product : products){
		        
			        dados = sheet.createRow( linha );
			
			        dados.createCell(0).setCellValue(product.getIdMasc());
			        dados.createCell(1).setCellValue(product.getDescription());
			        dados.createCell(2).setCellValue(product.getQtyCaracteresDesc());
			        dados.createCell(3).setCellValue(product.getDescriptionConservation());		        
			        dados.createCell(4).setCellValue(product.getDescriptionSif());
			        dados.createCell(5).setCellValue(product.getEan13());
			        dados.createCell(6).setCellValue(product.getDun14());
			        if ( product.getLabelFileName() != null )
			        {
			        	dados.createCell(7).setCellValue(product.getLabelFileName().getNameEtq());
			        }
			        if ( product.getLabelPalletFileName() != null )
			        {
			        	dados.createCell(8).setCellValue(product.getLabelPalletFileName().getNameEtq());
			        }
			        
			        dados.createCell(9).setCellValue( DateTimeUtils.formatDate(product.getCreationDate(), "dd/MM/yyyy") );
			        dados.createCell(10).setCellValue(product.isChangeQtyBoxEnabled());
			        dados.createCell(11).setCellValue(product.isChangeQtyPecasEnabled());
			        dados.createCell(12).setCellValue(product.getMaxWeight());
			        dados.createCell(13).setCellValue(product.getMinWeight());
			        dados.createCell(14).setCellValue(product.getTareBox());
			        dados.createCell(15).setCellValue(product.getTareEmbala());
			        dados.createCell(16).setCellValue(product.getTargetWeight());
			        dados.createCell(17).setCellValue(product.getUnit().getId());
			        dados.createCell(18).setCellValue(product.getExpirationDays());
			        dados.createCell(19).setCellValue(product.getPalletQty());
			        dados.createCell(20).setCellValue(product.getTypeStock().getDescricao());
			        dados.createCell(21).setCellValue(product.getWeighingStyle().getName());
			        dados.createCell(22).setCellValue(product.isBlocked());
			        dados.createCell(23).setCellValue(product.isFifoEnabled());
			        dados.createCell(24).setCellValue(product.getNetWeight());
			        dados.createCell(25).setCellValue(product.getPercentRemoveProductOnScale());
			        dados.createCell(26).setCellValue(product.getPrefixEnterpriseGS1());
			        dados.createCell(27).setCellValue(product.getSequenciaProcessProduct());
			        dados.createCell(28).setCellValue(product.getTargetQty());
			        dados.createCell(29).setCellValue(product.isVirtual());
			        dados.createCell(20).setCellValue(product.getUnitEmb().getId());
			        dados.createCell(31).setCellValue(product.getUnitEtq().getId());
			        dados.createCell(32).setCellValue(product.getPercentShipmentComplement());
			        dados.createCell(33).setCellValue(product.getCodSif());
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
