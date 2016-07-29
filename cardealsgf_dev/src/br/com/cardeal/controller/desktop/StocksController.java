package br.com.cardeal.controller.desktop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.pdf.PdfPCell;

import br.com.cardeal.dao.DefaultStockDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.Operation;
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.filter.StockFilter;
import br.com.cardeal.filter.StockFilterManuten;
import br.com.cardeal.globals.BuildPdf;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.DadosPdf;
import br.com.cardeal.model.Terminal;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Stock;
import br.com.cardeal.model.StockManuten;
import br.com.cardeal.model.StockOfPalletModel;
import br.com.cardeal.model.StockTotal;
import br.com.cardeal.model.StockTotalReport;
import br.com.cardeal.model.User;
import br.com.cardeal.services.PalletService;
import br.com.cardeal.services.StockService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;
/**
 * 
 * @author Sergio Artero
 * Classe de controle de relatórios e manutenção de estoque
 * 
 */
@Resource
public class StocksController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;
    
	public StocksController( Result result, UserInfo userInfo, Validator validator, ServletContext servletContext, HttpServletResponse servletResponse, HttpServletRequest servletRequest ) 
	{
		
		this.result = result;
		this.userInfo = userInfo;
		this.validator = validator;
		this.servletContext = servletContext;		
		this.servletResponse = servletResponse;
	}
	
	@Path("/stocks/producaoDiaria")
	@Get
	public void producaoDiaria() {	
		
		User user = userInfo.getUser();		
		result.include("user", user);
		
        result.include("producao", null);
        
	}
	
	@Path("/stocks/producaoDiariaMobile")
	@Get
	public void producaoDiariaMobile() {
		 producaoDiaria();
	}
	
	@Path("/stocks/producaoDiariaMobile")
	@Post	
	public void producaoDiariaMobile(StockFilter filter, String date1, String date2) 
	{
		producaoDiaria(filter, date1, date2);
	}
	
	@Path("/stocks/producaoDiaria")
	@Post	
	public void producaoDiaria(StockFilter filter, String date1, String date2) {
		User user = userInfo.getUser();
		
		if( filter == null )
			return;
		
		try 
		{
			if(date1 != null)
				filter.setEnterDateDe(DateTimeUtils.strToDateTime(date1));
			else
				filter.setEnterDateDe(null);
			
			if(date2 != null)
				filter.setEnterDateAte(DateTimeUtils.strToDateTime(date2));
			else
				filter.setEnterDateAte(null);
		} 
		catch (Exception e) 
		{
			validator.add(new ValidationMessage("date.invalid", "Formato de data inválida"));
			validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).producaoDiaria();
		}
		
		List<StockTotalReport> total = GuiGlobals.getDaoFactory().getStockDao().listSumStocked(filter, true);
		List<StockTotalReport> totalGeral = GuiGlobals.getDaoFactory().getStockDao().listSumStocked(filter, false);
		
        result.include("producao", total);
        result.include("totalGeral", totalGeral);
		result.include("user", user);
		result.include("companyIdDe", filter.getCompanyIdDe());
		result.include("companyIdAte", filter.getCompanyIdAte());
        result.include("terminalId", filter.getTerminalId());
        result.include("terminalId2", filter.getTerminalId_2());
		result.include("date1", date1);
        result.include("date2", date2);
        result.include("filter", filter);
        
        GuiGlobals.closeDb(); // Fecha conexões com o banco
    }
	
	@Path("/stocks")
	@Get
	public void list() 
	{		
		User user = userInfo.getUser();		
		result.include("user", user);
        result.include("stocks", null);
        result.include("maxlengthCompanyId", String.valueOf( Company.getLengthFieldId() ) );
        result.include("listTypeStock", TypeStock.values());
	}
	
	@Path("/stocks/listMobile")
	@Get
	public void listMobile() 
	{		
		list();
	}
	
	@Path("/stocks/listMobile")	
	@Post	
	public void listMobile(StockFilter filter, String date1, String date2, String date3, String date4, boolean showTotal) 
	{	
		execList(filter, date1, date2, date3, date4, showTotal);
	}
	
	public void listBack(StockFilter filter, String date1, String date2, String date3, String date4, boolean showTotal) 
	{
		int page = filter.getPage();
		page = ( ( page == 1 ) ? 1 : ( page - 1 ) );
		filter.setPage(page);
		result.use(Results.logic()).redirectTo(StocksController.class).list(filter, date1, date2, date3, date4, showTotal);
	}
	
	public void listForward(StockFilter filter, String date1, String date2, String date3, String date4, boolean showTotal) 
	{
		int page = filter.getPage();
		page = ( page + 1 );
		filter.setPage(page);
		result.use(Results.logic()).redirectTo(StocksController.class).list(filter, date1, date2, date3, date4, showTotal);
	}
	
	
	@Path("/stocks/list")	
	@Post	
	@Get
	public void list(StockFilter filter, String date1, String date2, String date3, String date4, boolean showTotal) 
	{		
		execList(filter, date1, date2, date3, date4, showTotal);
    }
		
	private void execList(StockFilter filter, String date1, String date2, String date3, String date4, boolean showTotal) 
	{
		User user = userInfo.getUser();		
		
		if(filter != null)		
		{
			try 
			{
				if(date1 != null)
					filter.setEnterDateDe(DateTimeUtils.strToDateTime(date1));
				else
					filter.setEnterDateDe(null);
				
				if(date2 != null)
					filter.setEnterDateAte(DateTimeUtils.strToDateTime(date2));
				else
					filter.setEnterDateAte(null);
				
				if(date3 != null)
					filter.setManufactureDateDe(DateTimeUtils.strToDateTime(date3));
				else
					filter.setManufactureDateDe(null);
				
				if(date4 != null)
					filter.setManufactureDateAte(DateTimeUtils.strToDateTime(date4));
				else
					filter.setManufactureDateAte(null);
			} 
			catch (Exception e) 
			{
				validator.add(new ValidationMessage("date.invalid", "Formato de data inválida"));
				validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).list();
			}	
			
			if( filter.getIdMasc() != null && !filter.getIdMasc().isEmpty() )
			{
				Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc() );
				if ( product == null || product.getId() == 0 )
				{
					validator.add(new ValidationMessage("product", "Produto " + filter.getIdMasc() + " não existe!"));
					validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).list();
				}
			}
			
			if( filter.getIdMasc_2() != null && !filter.getIdMasc_2().isEmpty() )
			{
				Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc_2() );
				if ( product == null || product.getId() == 0 )
				{
					validator.add(new ValidationMessage("product", "Produto " + filter.getIdMasc_2() + " não existe!"));
					validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).list();
				}
			}
			
			if(showTotal) 
			{
				filter.setLimitPage(false);
				List<StockTotal> totals = GuiGlobals.getDaoFactory().getStockDao().totalize(filter);
		        result.include("totals", totals);
			}
			else 
			{
				//filter.setLimitPage(true);
				filter.setLimitPage(false);
				filter.setAsc(false);
				filter.setOrderBy("manufactureDate");
				//List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().list(filter);
				List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().listWeb(filter);
		        result.include("stocks", stocks);			
			}
		}
		
		TypeStock typeStock;
		
		if ( filter.getTypeStock() != null )
		{
			typeStock = filter.getTypeStock();
		}
		else
		{
			typeStock = TypeStock.TODOS;
		}

		List<StockTotalReport> totalGeral = GuiGlobals.getDaoFactory().getStockDao().listSumStocked(filter, false);
		
		
		/* Set nas variaveis ${nome_da_var} do list.jsp
		 * result.include("companyIdDe", filter.getCompanyIdDe());
		 * value="${companyIdDe}"
		 */
		result.include("user", user);
		/*result.include("companyIdDe", filter.getCompanyIdDe());
		result.include("companyIdAte", filter.getCompanyIdAte());
		result.include("id", filter.getId());
		result.include("id_2", filter.getId_2());
		result.include("idMasc", filter.getIdMasc());
        result.include("idMasc_2", filter.getIdMasc_2()); */
        result.include("date1", date1);
        result.include("date2", date2);
        result.include("date3", date3);
        result.include("date4", date4);
        result.include("typeStock", filter.getTypeStock());
        result.include("page", ( filter.getPage() == 0 ) ? 1 : filter.getPage() );
        result.include("showTotal", showTotal);
        result.include("totalGeral", totalGeral);
        result.include("descTypeStock", typeStock.getDescricao());
        result.include("qtyPerPage", DefaultStockDao.LIST_STOCK_QTY_PER_PAGE );
        result.include("filter", filter);
        result.include("maxlengthCompanyId", String.valueOf( Company.getLengthFieldId() ) );
        result.include("listTypeStock", TypeStock.values());
	}

	public void exportStockTotalsToExcel( StockFilter filter, String date1, String date2, String date3, String date4 )
	{
		getFilterAjust(filter, date1, date2, date3, date4);
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "estoqueAtualTotalizado");
		String fileFullName = getExportName( nameFile );
		
		if ( GuiGlobals.getDaoFactory().getStockDao().exportObjStockTotalToExcel(filter, fileFullName) ) 
		{
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do estoque totalizado!");
			result.include("filter", filter);
			result.use(Results.logic()).redirectTo(StocksController.class).list(filter, date1, date2, date3, date4, true);
		}
	}
	
	public void exportStockToExcel( StockFilter filter, String date1, String date2, String date3, String date4 )
	{
		getFilterAjust(filter, date1, date2, date3, date4);
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "estoqueAtual");
		String fileFullName = getExportName( nameFile );
		
		if ( GuiGlobals.getDaoFactory().getStockDao().exportObjStockToExcel(filter, fileFullName) ) 
		{
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do estoque atual!");
			result.include("filter", filter);
			result.use(Results.logic()).redirectTo(StocksController.class).list(filter, date1, date2, date3, date4, true);
		}
	}
	
	private void getFilterAjust(StockFilter filter, String date1, String date2, String date3, String date4) 
	{
		try 
		{
			if(date1 != null)
				filter.setEnterDateDe(DateTimeUtils.strToDateTime(date1));
			else
				filter.setEnterDateDe(null);
			
			if(date2 != null)
				filter.setEnterDateAte(DateTimeUtils.strToDateTime(date2));
			else
				filter.setEnterDateAte(null);
			
			if(date3 != null)
				filter.setManufactureDateDe(DateTimeUtils.strToDateTime(date3));
			else
				filter.setManufactureDateDe(null);
			
			if(date4 != null)
				filter.setManufactureDateAte(DateTimeUtils.strToDateTime(date4));
			else
				filter.setManufactureDateAte(null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		if( filter.getIdMasc() != null && !filter.getIdMasc().isEmpty() )
		{
			Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc() );
			if ( product == null || product.getId() == 0 )
			{
				validator.add(new ValidationMessage("product", "Produto " + filter.getIdMasc() + " não existe!"));
				validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).list();
			}
		}
		
		if( filter.getIdMasc_2() != null && !filter.getIdMasc_2().isEmpty() )
		{
			Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getIdMasc_2() );
			if ( product == null || product.getId() == 0 )
			{
				validator.add(new ValidationMessage("product", "Produto " + filter.getIdMasc_2() + " não existe!"));
				validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).list();
			}
		}
		
	}
	
	private void getFilterAjustStockManuten(StockFilterManuten filter, String date1, String date2, String date3, String date4) 
	{
		if ( filter == null )
		{
			filter = new StockFilterManuten();
		}
		else
		{
			try 
			{
				if(date1 != null)
					filter.setEnterDateDe(DateTimeUtils.strToDateTime(date1));
				else
					filter.setEnterDateDe(null);
				
				if(date2 != null)
					filter.setEnterDateAte(DateTimeUtils.strToDateTime(date2));
				else
					filter.setEnterDateAte(null);
				
				if(date3 != null)
					filter.setManufactureDateDe(DateTimeUtils.strToDateTime(date3));
				else
					filter.setManufactureDateDe(null);
				
				if(date4 != null)
					filter.setManufactureDateAte(DateTimeUtils.strToDateTime(date4));
				else
					filter.setManufactureDateAte(null);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			if( filter.getProductIdMasc() != null && !filter.getProductIdMasc().isEmpty() )
			{
				Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getProductIdMasc() );
				if ( product == null || product.getId() == 0 )
				{
					validator.add(new ValidationMessage("product", "Produto " + filter.getProductIdMasc() + " não existe!"));
					validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).list();
				}
			}
			
			if( filter.getProductIdMasc_2() != null && !filter.getProductIdMasc_2().isEmpty() )
			{
				Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( filter.getProductIdMasc_2() );
				if ( product == null || product.getId() == 0 )
				{
					validator.add(new ValidationMessage("product", "Produto " + filter.getProductIdMasc_2() + " não existe!"));
					validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).list();
				}
			}
		}
		
	}	
	
	public void exportDailyProduction( StockFilter filter, String enterDateDe, String enterDateAte )
	{
		
		getFilterAjust(filter, enterDateDe, enterDateAte, null, null);
		
		List<StockTotal> total = GuiGlobals.getDaoFactory().getStockDao().totalize(filter);
		if ( total != null ) {
		
			String nameFile = getFileNameToExport(TypeExportReport.PDF, "producaoDiaria");
			String fileFullName = getExportName( nameFile );
			// Definição relativa das colunas no pdf. 0.05f = 5%
			float[] fieldSizes = new float[] {	0.74f,	// "Produto"
												0.08f,	// "Caixas"
												0.08f,	// "Peças"
												0.1f};	// "Peso Líquido (kg)"
			
			// Criando arquivo pdf a ser editado
			BuildPdf pdfExport = new BuildPdf( fileFullName );
			
			if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
				// Adicionando meta datas default		
				pdfExport.addMetaData("Relatório de Produção Diária", null);
				
				// Adicionando titulo do Relatório
				pdfExport.addTitlePage("Cardeal Frigorífico");
				
				// Editando cabeçalho da tabela a ser impressa
				ArrayList<String> campos = new ArrayList<String>();
				campos.add("Produto");
				campos.add("Caixas");
				campos.add("Peças");
				campos.add("Peso Líquido (kg)");
				
				//Editando dados do itens
				String[][] dados = new String[total.size()+1][campos.size()];
				int contador = 0;
				int qtyEmb = 0;
				int qtyPec = 0;
				double qtyNet = 0;
				for ( StockTotal stockTotal : total )
				{
					dados[contador][0] = stockTotal.getProduct().toString();
					dados[contador][1] = String.valueOf(stockTotal.getSecondaryQty());
					dados[contador][2] = String.valueOf(stockTotal.getPrimaryQty());
					dados[contador][3] = stockTotal.getNetFormatted();
					
					qtyEmb += stockTotal.getSecondaryQty();
					qtyPec += stockTotal.getPrimaryQty();
					qtyNet += stockTotal.getNet();
					
					contador++;
				}
				
				dados[dados.length-1][0] = "Total:";
				dados[dados.length-1][1] = String.valueOf(qtyEmb);
				dados[dados.length-1][2] = String.valueOf(qtyPec);
				dados[dados.length-1][3] = NumberUtils.transform(NumberUtils.truncate(qtyNet, 3), 14, 3, false, false);
						
				if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
				
					// Enviando cabeçalho e dados para impressï¿½o do Relatório		
					pdfExport.printTable("Relatório de Produção Diária", fieldSizes, campos, dados);
					
					if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
					
						pdfExport.print();
						showPdfOnBrowser( fileFullName );
												
					}
					
				}
				
			}
			
		}
		else{
			result.include("notice", "Arquivo PDF não pode ser gerado!");
		}
		result.use(Results.logic()).redirectTo(StocksController.class).producaoDiaria();
		
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	public void exportStockTotalsToPDF(StockFilter filter, String date1, String date2, String date3, String date4)
	{
		getFilterAjust(filter, date1, date2, date3, date4);
		
		filter.setAsc(false);
		filter.setOrderBy("id");
		List<StockTotal> total = GuiGlobals.getDaoFactory().getStockDao().totalize(filter);
		if ( total != null ) {
		
			String nameFile = getFileNameToExport(TypeExportReport.PDF, "listagemEstoqueTotalPDF");
			String fileFullName = getExportName( nameFile );
			
			// Definição relativa das colunas no pdf. 0.05f = 5%
			float[] fieldSizes = new float[] {	0.03f,	// "FILIAL"
												0.05f,  // "COD"
												0.3f,	// "DESCRIÇÃO"
												0.05f,	// "EMB"
												0.05f,	// "CAIXAS"	
												0.05f,	// "PEÇAS"
												0.1f,};	// "PESO LÍQUIDO (KG)"
			
			// Criando arquivo pdf a ser editado
			BuildPdf pdfExport = new BuildPdf( fileFullName );
			
			if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
				// Adicionando meta datas default		
				pdfExport.addMetaData("Relatório de Estoque Totalizado", null);
				
				// Adicionando titulo do Relatório
				pdfExport.addTitlePage("Cardeal Frigorífico");
				
				// Editando cabeçalho da tabela a ser impressa
				ArrayList<String> campos = new ArrayList<String>();
				campos.add("FIL");
				campos.add("COD");
				campos.add("DESCRIÇÃO");
				campos.add("EMB");
				campos.add("CAIXAS");
				campos.add("PEÇAS");
				campos.add("P.LÍQ.(kg)");
				
				List<ArrayList<DadosPdf>> dadosPdf = new ArrayList<>();
				
				int qtyEmb = 0;
				int qtyBox  = 0;
				int qtyPec = 0;
				double qtyNet = 0;
				for ( StockTotal stockTotal : total )
				{
					ArrayList<DadosPdf> linha = new ArrayList<DadosPdf>();
					
					linha.add(new DadosPdf(stockTotal.getCompany().getId(),PdfPCell.ALIGN_LEFT) );
					linha.add(new DadosPdf(stockTotal.getProduct().getIdMasc(), PdfPCell.ALIGN_LEFT));
					linha.add(new DadosPdf(stockTotal.getProduct().getDescription(), PdfPCell.ALIGN_LEFT));
					linha.add(new DadosPdf("1", PdfPCell.ALIGN_RIGHT));
					linha.add(new DadosPdf(String.valueOf(stockTotal.getSecondaryQty()), PdfPCell.ALIGN_RIGHT));
					linha.add(new DadosPdf(String.valueOf(stockTotal.getPrimaryQty()), PdfPCell.ALIGN_RIGHT));
					linha.add(new DadosPdf(stockTotal.getNetFormatted(), PdfPCell.ALIGN_RIGHT));
					
					qtyEmb++; 
					qtyBox += stockTotal.getSecondaryQty();
					qtyPec += stockTotal.getPrimaryQty();
					qtyNet += stockTotal.getNet();
					
					dadosPdf.add(linha);
				}
				
				/* Ajuste para a última linha do relatório
				 * Imprime apenas os totais por coluna
				 */
				ArrayList<DadosPdf> linha = new ArrayList<DadosPdf>();
				
				linha.add(new DadosPdf("Total", PdfPCell.ALIGN_LEFT));
				linha.add(new DadosPdf("", PdfPCell.ALIGN_RIGHT));
				linha.add(new DadosPdf("", PdfPCell.ALIGN_RIGHT));
				linha.add(new DadosPdf(String.valueOf(qtyEmb), PdfPCell.ALIGN_RIGHT));
				linha.add(new DadosPdf(String.valueOf(qtyBox), PdfPCell.ALIGN_RIGHT));
				linha.add(new DadosPdf(String.valueOf(qtyPec), PdfPCell.ALIGN_RIGHT));
				linha.add(new DadosPdf(NumberUtils.transform(NumberUtils.truncate(qtyNet, 3), 14, 3, false, false), PdfPCell.ALIGN_RIGHT));
				
				dadosPdf.add(linha);
						
				if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
				
					// Enviando cabeçalho e dados para impressão do Relatório
					pdfExport.printTableAlignmentCell("Relatório de Estoque Atual Totalizado", fieldSizes,campos, dadosPdf);
					if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
						
						pdfExport.print();
						//showPdfOnBrowser( fileFullName );
						downloadOnBrowser( fileFullName, "application/pdf");
					}
				}
			}
		}
		else{
			result.include("notice", "Arquivo PDF não pode ser gerado!");
		}
		//result.use(Results.logic()).redirectTo(StocksController.class).producaoDiaria();
		
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	public void exportStockProductionToPDF( StockFilter filter, String date1, String date2, String date3, String date4 )
	{		
		getFilterAjust(filter, date1, date2, date3, date4);
		
		filter.setAsc(true);
		filter.setOrderBy("id");
		List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().list(filter);
		if ( stocks != null && stocks.size() > 0) 
		{		
			String nameFile = getFileNameToExport(TypeExportReport.PDF, "listagemEstoquePDF");
			String fileFullName = getExportName( nameFile );
			// Definição relativa das colunas no pdf. 0.05f = 5%
			float[] fieldSizes = new float[] {	0.06f,	// "FILIAL"
												0.08f,	// "NR. SÉRIE"
												0.05f,  // "COD"
												0.3f,	// "DESCRIÇÃO"
												0.05f,	// "EMB"
												0.05f,	// "CX"	
												0.05f,	// "PC"
												0.1f,	// "P.LÍQ.(kg)"
												0.08f,	// "TIPO ESTOQUE"
												0.07f,	// "PALETE"
												0.05f,};// "FLAG"
			
			// Criando arquivo pdf a ser editado
			BuildPdf pdfExport = new BuildPdf( fileFullName );
			
			if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
				// Adicionando meta datas default		
				pdfExport.addMetaData("Relatório de Estoque Atual", null);
				
				// Adicionando titulo do Relatório
				pdfExport.addTitlePage("Cardeal Frigorífico");
				
				// Editando cabeçalho da tabela a ser impressa
				ArrayList<String> campos = new ArrayList<String>();
				campos.add("FILIAL");
				campos.add("NR. SÉRIE");
				campos.add("COD");
				campos.add("DESCRIÇÃO");
				campos.add("EMB");
				campos.add("CX");
				campos.add("PC");	        
				campos.add("P.LÍQ.(kg)");
				campos.add("TIPO ESTOQUE");
				campos.add("PALETE");
				campos.add("FLAG");
								
				List<ArrayList<DadosPdf>> dadosPdf = new ArrayList<>();
												
				int qtyEmb = 0;
				int qtyBox = 0;
				int qtyPec = 0;
				double qtyNet = 0;
				for ( Stock stock : stocks )
				{
					ArrayList<DadosPdf> linha = new ArrayList<DadosPdf>();
					
					linha.add(new DadosPdf(stock.getCompany().getId(),PdfPCell.ALIGN_LEFT) );
					linha.add(new DadosPdf(stock.getIdFormatSerial(), PdfPCell.ALIGN_LEFT));
					linha.add(new DadosPdf(stock.getProduct().getIdMasc(), PdfPCell.ALIGN_LEFT));
					linha.add(new DadosPdf(stock.getProduct().getDescription(), PdfPCell.ALIGN_LEFT));
					linha.add(new DadosPdf("1", PdfPCell.ALIGN_RIGHT));
					linha.add(new DadosPdf(String.valueOf( stock.getSecondaryQty() ), PdfPCell.ALIGN_RIGHT));
					linha.add(new DadosPdf(String.valueOf( stock.getPrimaryQty()), PdfPCell.ALIGN_RIGHT));
					linha.add(new DadosPdf(stock.getNetFormatted(), PdfPCell.ALIGN_RIGHT));
					linha.add(new DadosPdf(stock.getTypeStock().getDescricao(), PdfPCell.ALIGN_LEFT));
					
					String formatPallet = (stock.getPallet() != null) ? stock.getPallet().getIdFormatted() : Utils.formatPallet(0);
					linha.add(new DadosPdf(formatPallet, PdfPCell.ALIGN_LEFT));
					linha.add(new DadosPdf(stock.getOperation(), PdfPCell.ALIGN_CENTER));
					
					qtyEmb += 1;
					qtyBox += stock.getSecondaryQty();
					qtyPec += stock.getPrimaryQty();
					qtyNet += stock.getNet();
					
					dadosPdf.add(linha);
				}
				
				/* Ajuste para a última linha do relatório
				 * Imprime apenas os totais por coluna
				 */
				ArrayList<DadosPdf> linha = new ArrayList<DadosPdf>();
				linha.add(new DadosPdf("Total",PdfPCell.ALIGN_LEFT));
				linha.add(new DadosPdf("",PdfPCell.ALIGN_LEFT));
				linha.add(new DadosPdf("",PdfPCell.ALIGN_LEFT));
				linha.add(new DadosPdf("",PdfPCell.ALIGN_LEFT));
				linha.add(new DadosPdf(String.valueOf(qtyEmb),PdfPCell.ALIGN_RIGHT));
				linha.add(new DadosPdf(String.valueOf(qtyBox),PdfPCell.ALIGN_RIGHT));
				linha.add(new DadosPdf(String.valueOf(qtyPec),PdfPCell.ALIGN_RIGHT));
				linha.add(new DadosPdf(NumberUtils.transform(NumberUtils.truncate(qtyNet, 3), 14, 3, false, false),PdfPCell.ALIGN_RIGHT));
				linha.add(new DadosPdf("",PdfPCell.ALIGN_LEFT));
				linha.add(new DadosPdf("",PdfPCell.ALIGN_LEFT));
				linha.add(new DadosPdf("",PdfPCell.ALIGN_LEFT));
				
				dadosPdf.add(linha);
						
				if (pdfExport.getState() == 0) 
				{
					pdfExport.printTableAlignmentCell("Relatório de Estoque Atual", fieldSizes,campos, dadosPdf);
					
					if (pdfExport.getState() == 0) 
					{
						pdfExport.print();
						//showPdfOnBrowser( fileFullName );
						downloadOnBrowser( fileFullName, "application/pdf");
					}
				}
			}
		}
		else{
			result.include("notice", "Arquivo PDF não pode ser gerado!");
		}
		//result.use(Results.logic()).redirectTo(StocksController.class).producaoDiaria();
		
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	// Gera o PDF e faz o download do arquivo gerado
	public void downloadOnBrowser( String fileFullName, String contentType )
	{
		byte[] arquivo = null;
		File file = new File( fileFullName );
		
		try {
			arquivo = GuiGlobals.fileToByte( file );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		servletResponse.setHeader("Content-disposition", "attachment;filename="+fileFullName);
		servletResponse.setContentType(contentType);
		servletResponse.setContentLength(arquivo.length);
		
		ServletOutputStream ouputStream;
		try {
			ouputStream = servletResponse.getOutputStream();
			ouputStream.write(arquivo, 0, arquivo.length);
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showPdfOnBrowser( String fileFullName )
	{
		byte[] arquivo = null;
		File file = new File( fileFullName );
		
		try {
			arquivo = GuiGlobals.fileToByte( file );
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		servletResponse.setContentType("application/pdf");
		servletResponse.setContentLength(arquivo.length);
		
		ServletOutputStream ouputStream;
		try {
			ouputStream = servletResponse.getOutputStream();
			ouputStream.write(arquivo, 0, arquivo.length);
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showExcelOnBrowser( String fileFullName )
	{
		byte[] arquivo = null;
		File file = new File( fileFullName );
		
		try {
			arquivo = GuiGlobals.fileToByte( file );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		servletResponse.setHeader("Content-disposition", "attachment;filename="+fileFullName);
		servletResponse.setContentType("application/vnd.ms-excel");
		servletResponse.setContentLength(arquivo.length);
		
		ServletOutputStream ouputStream;
		try {
			ouputStream = servletResponse.getOutputStream();
			ouputStream.write(arquivo, 0, arquivo.length);
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Métodos referente ao Relatório de pallets
	 */
	
	@Path("/stocks/palletview/{palletId}")
	@Get
	public void palletview(int palletId) {
		User user = userInfo.getUser();
		
		Pallet pallet = GuiGlobals.getDaoFactory().getStockDao().findPallet(palletId); 
		result.include("pallet", pallet);
		result.include("user", user);
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	@Path("/stocks/palletviewMobile/{palletId}")
	@Get
	public void palletviewMobile(int palletId) {
		palletview(palletId);
	}
	
	@Path("/stocks/pallets")
	@Get
	public void pallets() {		
		validPermissionReportPallet();
		User user = userInfo.getUser();		
		result.include("user", user);		
        result.include("pallets", null);
	}
	
	@Path("/stocks/palletsMobile")
	@Get
	public void palletsMobile() {
		pallets();
	}
	
	@Path("/stocks/palletsMobile")
	@Post
	public void palletsMobile(StockFilter filter, String date3, String date4) {
		pallets(filter, date3, date4);
	}
	
	public void palletsBack(StockFilter filter, String date3, String date4) 
	{
		int page = filter.getPage();
		page = ( ( page == 1 ) ? 1 : ( page - 1 ) );
		filter.setPage(page);
		result.use(Results.logic()).redirectTo(StocksController.class).pallets(filter, date3, date4);
	}
	
	public void palletsForward(StockFilter filter, String date3, String date4) 
	{
		int page = filter.getPage();
		page = ( page + 1 );
		filter.setPage(page);
		result.use(Results.logic()).redirectTo(StocksController.class).pallets(filter, date3, date4);
	}
	
	@Path("/stocks/pallets")
	@Post	
	public void pallets(StockFilter filter, String date3, String date4) 
	{
		User user = userInfo.getUser();
		
		getFilterAjust(filter, date3, date4, null, null);
		filter.setLimitPage(true);
		
		List<Pallet> pallets = GuiGlobals.getDaoFactory().getStockDao().listPallets(filter, false);
		List<StockTotalReport> totalGeral = GuiGlobals.getDaoFactory().getStockDao().listSumStocked(filter, false);
		
        result.include("totalGeral", totalGeral);
        result.include("pallets", pallets);	
        
        result.include("companyIdDe", filter.getCompanyIdDe());
		result.include("companyIdAte", filter.getCompanyIdAte());
		result.include("terminalId", filter.getTerminalId());
        result.include("terminalId2", filter.getTerminalId_2());
		result.include("palletIdDe", filter.getPalletIdDe());
		result.include("palletIdAte", filter.getPalletIdAte());
		result.include("idMasc", filter.getIdMasc());
        result.include("idMasc_2", filter.getIdMasc_2());
        result.include("date3", date3);
        result.include("date4", date4);
        result.include("user", user);
        result.include("filter", filter);
        result.include("page", ( filter.getPage() == 0 ) ? 1 : filter.getPage() );
        result.include("qtyPerPage", DefaultStockDao.LIST_STOCK_QTY_PER_PAGE );
        result.include("maxlengthCompanyId", String.valueOf( Company.getLengthFieldId() ) );
        GuiGlobals.closeDb(); // Fecha conexões com o banco
    }
	
	public void exportPalletsToPDF( StockFilter filter, String date3, String date4 )
	{
		getFilterAjust(filter, date3, date4, null, null);
		List<Pallet> pallets = GuiGlobals.getDaoFactory().getStockDao().listPallets(filter, false);
			
		if ( pallets != null ) 
		{
			String nameFile = getFileNameToExport(TypeExportReport.PDF, "pallets");
			String fileFullName = getExportName( nameFile );
			// Definição relativa das colunas no pdf. 0.05f = 5%
			float[] fieldSizes = new float[] {	0.06f,	// "FILIAL"
												0.06f,	// "NR. SÉRIE"
												0.30f,	// "PRODUTO"
												0.06f,	// "CX"
												0.07f,	// "PC"	
												0.1f,	// "P.LÍQ.(kg)"
												0.06f,	// "TERMINAL"
												0.09f,	// "DT. ABERTURA"
												0.09f,	// "DT. PRODUÇÃO"
												0.11f};	// "STATUS"
			
			// Criando arquivo pdf a ser editado
			BuildPdf pdfExport = new BuildPdf( fileFullName );
			
			if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
				// Adicionando meta datas default		
				pdfExport.addMetaData("Relatório de caixas por Pallet",null);
				
				// Adicionando titulo do Relatório
				pdfExport.addTitlePage("Cardeal Frigorífico");
				
				// Editando cabeçalho da tabela das caixas
				ArrayList<String> campos = new ArrayList<String>();
				campos.add("FILIAL");
				campos.add("NR. SÉRIE");
				campos.add("PRODUTO");
				campos.add("CX");
				campos.add("PC");
				campos.add("P.LÍQ.(kg)");
				campos.add("TERMINAL");
				campos.add("DT. ABERTURA");
				campos.add("DT. PRODUÇÃO");
				campos.add("STATUS");
				
				if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
				
					String[][] dados = new String[pallets.size()+1][campos.size()];
					int contador = 0;
					int qtyPec = 0;
					double qtyNet = 0;
					for ( Pallet pallet : pallets ){
						
						dados[contador][0] = pallet.getCompany().getId();
						dados[contador][1] = pallet.getIdFormatted();
						dados[contador][2] = pallet.getProduct().toString();
						dados[contador][3] = pallet.getSecondaryQtyFormatted();
						dados[contador][4] = pallet.getPrimaryQtyFormatted();
						dados[contador][5] = pallet.getNetFormatted(); 
						dados[contador][6] = pallet.getTerminal().getIdTerminal();
						dados[contador][7] = pallet.getOpenDateFormat();
						dados[contador][8] = pallet.getManufactureDateFormat();
						dados[contador][9] = pallet.getStatusDesc();
						
						qtyPec += pallet.getPrimaryQty();
						qtyNet += pallet.getNet();
						
						contador++;
						
					}
					
					dados[dados.length-1][0] = "Total:";
					dados[dados.length-1][4] = String.valueOf(qtyPec);
					dados[dados.length-1][5] = NumberUtils.transform(NumberUtils.truncate(qtyNet, 3), 14, 3, false, false);
					
					pdfExport.printTable("", fieldSizes, campos, dados);
					
					if (pdfExport.getState() == 0) { // Se nao ocorreu erro na montagem do Relatório
					
						pdfExport.print();
						showPdfOnBrowser( fileFullName );
						
					}
					
				}
				
			}
		}
			
		result.use(Results.logic()).redirectTo(StocksController.class).pallets(filter, date3, date4);
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	public void exportPalletsToExcel( StockFilter filter, String date3, String date4 )
	{
		getFilterAjust(filter, date3, date4, null, null);
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "pallets");
		String fileFullName = getExportName( nameFile );
		
		if ( GuiGlobals.getDaoFactory().getStockDao().exportObjPalletsToExcel(filter, fileFullName) ) 
		{
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação de paletes!");
			result.include("filter", filter);
			result.use(Results.logic()).redirectTo(StocksController.class).pallets(filter, date3, date4);
		}
		
		GuiGlobals.closeDb(); // Fecha conexões com o banco
		
	}
	
	@Path("/stocks/palletsManuten")
	@Get
	public void palletsManuten() {	
		
		User user = userInfo.getUser();		
		result.include("user", user);		
        result.include("pallets", null);
        result.include("maxlengthCompanyId", String.valueOf( Company.getLengthFieldId() ) );
	}
	
	@Path("/stocks/palletsManuten")
	@Post	
	public void palletsManuten(StockFilter filter, String date1, String date2, String date3, String date4, String date5, String date6) {
		
		validPermissionPallet();
		User user = userInfo.getUser();
		
		if(filter == null)
			return;
		
		try 
		{
			if(date1 != null)
				filter.setEnterDateDe(DateTimeUtils.strToDateTime(date1));
			else
				filter.setEnterDateDe(null);
			
			if(date2 != null)
				filter.setEnterDateAte(DateTimeUtils.strToDateTime(date2));
			else
				filter.setEnterDateAte(null);
			
			if(date3 != null)
				filter.setManufactureDateDe(DateTimeUtils.strToDateTime(date3));
			else
				filter.setManufactureDateDe(null);
			
			if(date4 != null)
				filter.setManufactureDateAte(DateTimeUtils.strToDateTime(date4));
			else
				filter.setManufactureDateAte(null);
			
			if(date5 != null)
				filter.setCloseDateDe(DateTimeUtils.strToDateTime(date5));
			else
				filter.setCloseDateDe(null);
			
			if(date6 != null)
				filter.setCloseDateAte(DateTimeUtils.strToDateTime(date6));
			else
				filter.setCloseDateAte(null);
			
		} 
		catch (Exception e) 
		{
			validator.add(new ValidationMessage("date.invalid", "Formato de data inválida"));
			validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).palletsManuten();
		}
		
		if ( filter.getPalletIdDe() > 0 && filter.getPalletIdAte() > 0 && filter.getPalletIdAte() < filter.getPalletIdDe() )
		{
			validator.add(new ValidationMessage("id.invalid", "'Pallet Ate' deve ser maior ou igual a 'Pallet De'"));
			validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).palletsManuten();
		}

		filter.setOnlyStocked(true);
		List<Pallet> pallets = GuiGlobals.getDaoFactory().getStockDao().listPallets(filter, false);
        result.include("pallets", pallets);			
		result.include("user", user);
        result.include("date1", date1);
        result.include("date2", date2);
        result.include("date3", date3);
        result.include("date4", date4);
        result.include("date5", date5);
        result.include("date6", date6);
        result.include("filter", filter);
        result.include("maxlengthCompanyId", String.valueOf( Company.getLengthFieldId() ) );
        GuiGlobals.closeDb(); // Fecha conexões com o banco
    }
	
	@Path("/stocks/pallets/remove/{pallet.id}")
	@Get	
	public void remove(Pallet pallet) 
	{
		Pallet palletRemove = GuiGlobals.getDaoFactory().getStockDao().findPalletStocked( pallet.getId() );
	    
	    removePallet(palletRemove,"99");
	    
		result.include("notice", "Pallet " + pallet.getId() + " removido com sucesso");
		result.use(Results.logic()).redirectTo(StocksController.class).palletsManuten();
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	private void removePallet(Pallet pallet, String motivo) 
	{
		validRemovePallet( pallet );
		PalletService ps = new PalletService(userInfo.getUser());
	    ps.leaveStock(pallet, Operation.PALLET_REMOVING, null, motivo);
	}

	@Path("/stocks/palletviewedit/{palletId}")
	@Get
	public void palletviewedit(int palletId) 
	{				
		Pallet pallet = GuiGlobals.getDaoFactory().getStockDao().findPallet(palletId); 
		List<StockOfPalletModel> stocksOfPallet = getStocksOfPallet(pallet, false);
		setVariablesOnPalletViewEdit(pallet, stocksOfPallet);		
	}
	
	@Path("/stocks/palletviewedit/{palletId}/{checked}")
	@Get
	public void palletviewedit(int palletId, boolean checked) 
	{
		Pallet pallet = GuiGlobals.getDaoFactory().getStockDao().findPallet(palletId); 
		List<StockOfPalletModel> stocksOfPallet = getStocksOfPallet(pallet, checked);
		setVariablesOnPalletViewEdit(pallet, stocksOfPallet);		
	}
	
	private void setVariablesOnPalletViewEdit(Pallet pallet, List<StockOfPalletModel> stocksOfPallet)
	{
		
		User user = userInfo.getUser();
		result.include("pallet", pallet);
		result.include("user", user);
		result.include("filter", new StockFilterManuten());
		result.include("palletStatus", PalletStatus.values());
		result.include("stocksOfPallet", stocksOfPallet);
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	private List<StockOfPalletModel> getStocksOfPallet( Pallet pallet, boolean checked) {
		
		List<StockOfPalletModel> stocksOfPallet = new ArrayList<StockOfPalletModel>();
		
		for ( Stock stock : pallet.getStocks() )
		{
			
			StockOfPalletModel stockOfPalletModel = new StockOfPalletModel();
			stockOfPalletModel.setChecked( checked );
			stockOfPalletModel.setCompany( stock.getCompany() );
			stockOfPalletModel.setId( stock.getId() );
			stockOfPalletModel.setProduct( stock.getProduct() );
			stockOfPalletModel.setPrimaryQty( stock.getPrimaryQty() );
			stockOfPalletModel.setNet( stock.getNet() );
			stockOfPalletModel.setBatch( stock.getBatch() );
			stockOfPalletModel.setManufactureDate( stock.getManufactureDate() );
						
			stocksOfPallet.add( stockOfPalletModel );
			stockOfPalletModel = null;
			
		}
		
		return stocksOfPallet;
		
	}

	public void palletsUpdate(final Pallet pallet, String openDate, String closeDate, String manufactureDate, String expirationDate) {
		
		Pallet palletUpdate = GuiGlobals.getDaoFactory().getPalletDao().find(pallet.getId());
		PalletService ps = new PalletService(userInfo.getUser());
		try {
			
			palletUpdate.setStatus(pallet.getStatus());
			palletUpdate.setTare(pallet.getTare());
			palletUpdate.setStrech(pallet.getStrech());
			palletUpdate.setTareCantoneira(pallet.getTareCantoneira());
			palletUpdate.setTareRack(pallet.getTareRack());
			palletUpdate.setTarePack(pallet.getTarePack());
			
			if (openDate != null){
				palletUpdate.setOpenDate( DateTimeUtils.strToDateTime(openDate) );
			}
			
			if (closeDate != null){
				palletUpdate.setCloseDate( DateTimeUtils.strToDateTime(closeDate) );
			}
			
			if (manufactureDate != null){
				palletUpdate.setManufactureDate( DateTimeUtils.strToDateTime(manufactureDate) );
			}
			
			if (expirationDate != null){
				palletUpdate.setExpirationDate( DateTimeUtils.strToDateTime(expirationDate) );
			}
			
			if ( palletUpdate.getStatus() == PalletStatus.OPEN )
			{
				palletUpdate.setCloseDate(null);
			}
			
			ps.updatePallet(palletUpdate, null);
			result.include("notice", "Pallet " + palletUpdate.getIdFormatted() + " atualizado com sucesso!");
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		finally{
			GuiGlobals.closeDb(); // Fecha conexões com o banco
		}
		
		result.redirectTo(this).palletsManuten();
		
	}

	@Post
	public void deleteSelectedOnStocksManuten(List<String> itensSelected, String motivo)
	{
		if ( motivo == null ) motivo = "99";
		deletingSelectedItens(itensSelected, motivo);
		User user = userInfo.getUser();
		result.include("user", user);
		result.include("filter", null);
		result.include("date1", null);
		result.include("date2", null);
		result.include("date3", null);
		result.include("date4", null);
		result.redirectTo(this).palletsManuten();
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	@Post
	public void deleteSelectedOnPalletsManuten(List<String> itensSelected, String motivo)
	{
		int palletId = 0;
		
		if (itensSelected != null && itensSelected.size() > 0)
		{
			Stock stock = GuiGlobals.getDaoFactory().getStockDao().find(Long.parseLong( itensSelected.get(0).trim() ) );
			palletId = stock.getPallet().getId();
		}
		
		if ( palletId > 0 )
		{
			if ( motivo == null ) motivo = "99";
			deletingSelectedItens(itensSelected, motivo);
			User user = userInfo.getUser();
			result.include("user", user);
			result.redirectTo(this).palletviewedit(palletId);
		}
		else
		{
			result.include("notice", "Nenhum item foi selecionado para exclusão!");
			result.redirectTo(this).palletsManuten();
		}
		
		GuiGlobals.closeDb(); 
	}
	
	@Post
	public void deleteSelectedOnPallets(List<String> itensSelected, String motivo)
	{
		int palletId = 0;
		
		if (itensSelected != null && itensSelected.size() > 0)
		{
			if ( motivo == null ) motivo = "";
			for ( String id : itensSelected )
			{
				palletId = Integer.parseInt(id);
				Pallet pallet = GuiGlobals.getDaoFactory().getPalletDao().find(palletId);
				
				if (  pallet != null && pallet.getId() > 0 )
				{
					removePallet(pallet, motivo);
				}
			}
			result.include("notice", "Paletes selecionados removidos com sucesso!");
		}
		else
		{
			result.include("notice", "Nenhum item foi selecionado para exclusão!");			
		}
		result.redirectTo(this).palletsManuten();
		
		GuiGlobals.closeDb(); 
	}

	private void deletingSelectedItens(List<String> itensSelected, String motivo) 
	{
		if (itensSelected != null && itensSelected.size() > 0)
		{		
			Stock stock;
			StockService ss = new StockService(userInfo.getUser());
			for ( String id : itensSelected ){
			
				stock = GuiGlobals.getDaoFactory().getStockDao().find(Long.parseLong(id) );
				if ( stock != null ){
	    			ss.leaveStock(stock, Operation.STOCK_REMOVING, null, true, motivo);
				}
				stock = null;
					
			}
			
			result.include("notice", "Itens selecionados removidos com sucesso");
			
		}
	}	
	
	private void setVariablesOnListManuten(StockFilterManuten filter, List<Stock> stocks, String date1, String date2, String date3, String date4)
	{		
		User user = userInfo.getUser();
		result.include("user", user);
		result.include("stocks", stocks);
        result.include("date1", date1);
        result.include("date2", date2);
        result.include("date3", date3);
        result.include("date4", date4);
        result.include("filter", filter);
        result.include("maxlengthCompanyId", String.valueOf( Company.getLengthFieldId() ) );
        result.include("listTypeStock", TypeStock.values());
        GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	@Path("/stocks/stockManuten")
	@Get
	public void stockManuten() 
	{
		validPermissionStock();
		setVariablesOnStockManuten(null, null);
    }
	
	@Path("/stocks/stockManuten")
	@Post
	public void stockManuten( StockFilterManuten filter ) 
	{	
		StockManuten stockManuten = null;
		if ( filter != null && filter.getProductIdMasc() != null && !filter.getProductIdMasc().isEmpty() && filter.getTypeStock() != null )
		{
			StockService service = new StockService();
			Stock stock = service.getStockKgFromTypeStock( filter.getProductIdMasc(), filter.getTypeStock() );
			
			if ( stock != null )
			{
				stockManuten = new StockManuten();
				stockManuten.setIdProduct(stock.getProduct().getId());
				stockManuten.setProduct(stock.getProduct());
				stockManuten.setCurrentQuantity(stock.getNet());
				stockManuten.setCurrentPrimaryQty( stock.getPrimaryQty() );
				stockManuten.setCurrentQtyBox( stock.getSecondaryQty() );
				stockManuten.setQuantityChange(0);
				stockManuten.setQuantityChangeBox(0);
				stockManuten.setQuantityChangePrimaryQty(0);
				stockManuten.setTypeStock(filter.getTypeStock());
			}
			else
			{
				result.include("notice", "Não foi encontrado estoque com os parâmetros informados!");
			}
		}
		else
		{
			result.include("notice", "Informe os parâmetros!");
		}
		
		setVariablesOnStockManuten(filter, stockManuten);
    }
		
	public void changeStocksManuten( StockManuten stockManuten )
	{
		if ( stockManuten != null && stockManuten.getProduct() != null && stockManuten.getQuantityChange() != 0 )
		{
			GuiGlobals.setUserInfo(userInfo);
			StockService service = new StockService();
			if ( service.leaveStocKg( stockManuten ) )
			{
				result.include("notice", "Arquivo de Kardex não pode ser atualizado!");
			}
			result.include("notice", "Ajuste realizado com sucesso!");
			result.use(Results.logic()).redirectTo(StocksController.class).stockManuten();
		}
	}
	
	private void setVariablesOnStockManuten(StockFilterManuten filter, StockManuten stockManuten)
	{				
		User user = userInfo.getUser();
		result.include("user", user);
		result.include("stockManuten", stockManuten);        
        result.include("filter", filter);        
        result.include("listTypeStock", TypeStock.getValuesToManutenStock());        
	}
	
	@Post
	public void deleteGranel()
	{
		StockService service = new StockService();
		service.removeAllTypeStockGranel();
		result.include("notice", "Processamento realizado com sucesso!");
		result.use(Results.logic()).redirectTo(StocksController.class).stockManuten();
	}
	
	@Path("/stocks/listManuten")
	@Get
	public void listManuten() 
	{
		validPermissionStockNotPallet();
		listManuten(null, null, null, null, null, false);        
    }
	
	@Path("/stocks/listManuten")
	@Post
	public void listManuten(StockFilterManuten filter, String date1, String date2, String date3, String date4) 
	{
		listManuten(filter, date1, date2, date3, date4, false);      
    }

	public void listManuten(StockFilterManuten filter, String date1, String date2, String date3, String date4, boolean checked)
	{
		List<Stock> stocks = getStocksByFilterFromListManuten(filter, date1, date2, date3, date4, checked);		
		setVariablesOnListManuten(filter, stocks, date1, date2, date3, date4);  
	}

	private List<Stock> getStocksByFilterFromListManuten(StockFilterManuten filter, String date1, String date2, String date3, String date4, boolean checked) 
	{
		getFilterAjustStockManuten(filter, date1, date2, date3, date4);
		
		List<Stock> stocks = GuiGlobals.getDaoFactory().getStockDao().listManuten(filter);
		
		checkedStock(checked, stocks);
		
		return stocks;
	}

	private void checkedStock(boolean checked, List<Stock> stocks) {
		for ( Stock stock : stocks )
		{
			stock.setSelected(checked);
		}
	}

	@Path("/stocks/view/{stock.id}")
	@Get
	public void view(Stock stock) {
		User user = userInfo.getUser();
			
		if(stock != null) {
			stock = GuiGlobals.getDaoFactory().getStockDao().find(stock.getId());
		}
		result.include("user", user);
		result.include("stock", stock);
		result.include("typeStock", TypeStock.values());
		result.include("units", GuiGlobals.getDaoFactory().getUnitDao().list());
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	@Path("/stocks/viewAdd/{stock.id}")
	@Get
	public void viewAdd(Stock stock) {
		User user = userInfo.getUser();
			
		result.include("user", user);
		result.include("stock", stock);
		result.include("typeStock", TypeStock.values());
//		result.include("units", unitDao.list());
	}

	@Path("/stocks/remove/{stock.id}")
	@Get	
	public void remove(Stock stock) {
		GuiGlobals.getDaoFactory().getStockDao().refresh(stock);
	    StockService ss = new StockService(userInfo.getUser());
	    ss.leaveStock(stock, Operation.STOCK_REMOVING, null, true, "99");
	    
		result.include("notice", "Item de estoque " + stock.getId() + " removido com sucesso");
		result.use(Results.logic()).redirectTo(StocksController.class).listManuten(null, null, null, null, null);
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	public void insert() {
		
	    result.forwardTo(this).viewAdd( null );
	}
	
	@Path("/stocks/add")
	@Post
	@Public
	public void add(final Stock stock, String enterDate, String manufactureDate, String expirationDate, String idMasc,
			String companyId, String terminalId, String palletId) {
		
		boolean isOk = true;
		User user = userInfo.getUser();
		Company company = null;
		Terminal terminal = null;
		Pallet pallet = null;
		Product product = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc( idMasc );		
		if ( product == null ){
			isOk = false;
			result.include("notice", "Produto " + idMasc + " não encontrado!");
		}
		
		
		if ( isOk ) {
			
			company = GuiGlobals.getDaoFactory().getCompanyDao().find(companyId);
			if (company == null ){
				isOk = false;
				result.include("notice", "Filial " + companyId + " não encontrado!");
			}
		}
		
		if ( isOk ) {
			
			terminal = GuiGlobals.getDaoFactory().getTerminalDao().find(companyId, terminalId);
			if (terminal == null ){
				isOk = false;
				result.include("notice", "Terminal " + terminalId + " não encontrado!");
			}
		}
		
		if ( isOk ) {
			
			pallet = GuiGlobals.getDaoFactory().getPalletDao().find( Integer.parseInt(palletId));
			if (pallet == null ){
				isOk = false;
				result.include("notice", "Pallet " + palletId + " não encontrado!");
			}
		}
		
		if ( isOk ){
					
			StockService ss = new StockService(userInfo.getUser());
			
			try {
				Long id = ss.generateStockId();
				stock.setId(id);
				
				stock.setProduct(product);
				stock.setCompany(company);
				stock.setTerminal(terminal);
				stock.setPallet(pallet);
				stock.setUser(user);
				
				if (enterDate != null){
					stock.setEnterDate( DateTimeUtils.strToDateTime(enterDate) );
				}
				
				if (manufactureDate != null){
					stock.setManufactureDate( DateTimeUtils.strToDateTime(manufactureDate) );
				}
				
				if (expirationDate != null){
					stock.setExpirationDate( DateTimeUtils.strToDateTime(expirationDate) );
				}
				
				ss.enterStock(stock, Operation.STOCK_MANUAL_INCLUDE);
				result.include("notice", "Stock " + stock.getId() + " adicionado com sucesso!");
			} catch (Exception e) {
				e.printStackTrace();
				result.include("notice", "Falha na gravação! Erro retornado: \n" + e.getMessage());
			}
			
		}
		
		result.include("user", user);
		result.redirectTo(this).listManuten(null, null, null, null, null);
		GuiGlobals.closeDb(); // Fecha conexões com o banco
	}
	
	public void update(final Stock stock, String enterDate, String manufactureDate, String expirationDate) {
		
		StockService ss = new StockService(userInfo.getUser());
		try {
			
			if (enterDate != null){
				stock.setEnterDate( DateTimeUtils.strToDateTime(enterDate) );
			}
			
			if (manufactureDate != null){
				stock.setManufactureDate( DateTimeUtils.strToDateTime(manufactureDate) );
			}
			
			if (expirationDate != null){
				stock.setExpirationDate( DateTimeUtils.strToDateTime(expirationDate) );
			}
			
			ss.enterStock(stock, Operation.STOCK_MANUAL_CHANGE);
			result.include("notice", "Stock " + stock.getId() + " atualizado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			result.include("notice", "Falha na gravação! Erro retornado: \n" + e.getMessage());
		}
		
		result.use(Results.logic()).redirectTo(StocksController.class).listManuten(null, null, null, null, null);
	}
	
	void validPermissionStock(){
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.MANUTENCAO_ESTOQUE), "stock", "user_not_insert_stocks");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validPermissionStockNotPallet(){
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.MANUTENCAO_ESTOQUE_NAO_PALETIZADO), "stock", "user_not_insert_stocks");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validPermissionPallet(){
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.MANUTENCAO_PALLET), "pallet", "user_not_insert_pallet");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validPermissionReportPallet(){
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.RELATORIO_PRODUCAO_PALLETS), "pallet", "user_not_access_report_pallet");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validRemovePallet( final Pallet pallet ){
		validator.checking(new Validations() {{
		    that(pallet.getStatus() != PalletStatus.OPEN, "Palete [" + pallet.getIdFormatted() + "]", "not_possible_remove_pallet_open");		    
		}});

		// redirects to the index page if any validation errors occur.
		validator.onErrorUse(Results.logic()).redirectTo(StocksController.class).palletsManuten();
	}
	
	void validInsert( final Stock stock ){
		
		validator.checking(new Validations() {{
			
			boolean idDoesNotExists;
			boolean idNull = stock.getId() != 0;
			
			that(idNull, "stock", "id_null");
			
			idDoesNotExists = GuiGlobals.getDaoFactory().getStockDao().find(stock.getId()) == null;
		    that(idDoesNotExists, "stock", "id_null");
		    
		    idDoesNotExists = GuiGlobals.getDaoFactory().getProductDao().findByIdMasc(stock.getProduct().getIdMasc()) == null;
		    that(idDoesNotExists, "product", "id_product_not_exists");
		    
		}});

		validator.onErrorUsePageOf(StocksController.class).listManuten(null, null, null, null, null);
		GuiGlobals.closeDb(); // Fecha conexões com o banco
		
	}
	
	private String getExportName( String nameFile ){
		
		String path = servletContext.getRealPath("/") + GuiGlobals.getSeparadorIvertido() + "stocks_export"; 
		
		if (!new File(path).exists()) { // Verifica se o diretório existe.   
	    	(new File(path)).mkdir();   // Cria o diretório   
	    } 
		
		return path + GuiGlobals.getSeparadorIvertido() + nameFile;
		
	}
	
	private String getFileNameToExport( TypeExportReport opcao, String nameArq){
		String fileName = DateTimeUtils.getDateForDb() + "_" + DateTimeUtils.getTimeForDb() ;
		return fileName + "_" + nameArq + "." + opcao.getExtensao();
	}
	
}
