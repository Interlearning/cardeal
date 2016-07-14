package br.com.cardeal.importacao;

import br.com.cardeal.dao.DaoFactory;
import br.com.cardeal.enums.LayoutProduto;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.log.LogDeProcessamento;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Unit;

public class ImportacaoProduto implements ImportacaoInterface {
	
	int controle = 0;
	
	private String fileProductImport;
	private String fileName;
	
	public ImportacaoProduto( String fileName ) {
		this.fileName = fileName.trim();
		setFileProductImport();
	}

	public String getFileProductImport() {
		return fileProductImport;
	}

	public void setFileProductImport() {
		this.fileProductImport = fileName;
	}

	@Override
	public String getFileImport() {
		return fileProductImport;
	}
	
	@Override
	public boolean sendToSGBD(String[] dados) {
		
		this.controle = this.controle + 1; //atributo da classe para controlar a linha do arquivo pra leitura 
		
		if(this.controle > 2 ) 
		{
			
			boolean addProduct = false;
			DaoFactory daoFactory = GuiGlobals.getDaoFactory();
			Product produto = null;
			
			try
			{
				String idMasc = String.format("%04d", Integer.parseInt( dados[LayoutProduto.CODIGO_PRODUTO.getPosicao()] ) );
				produto = daoFactory.getProductDao().findByIdMasc( idMasc );
				
				if (produto == null){
					produto = new Product();
					addProduct = true;
				}
				
				Unit unit = GuiGlobals.getDaoFactory().getUnitDao().find(dados[LayoutProduto.UM_VENDA.getPosicao()]);
				if ( unit == null )
				{
					String msg = "Unidade de venda [" + dados[LayoutProduto.UM_VENDA.getPosicao()] + "] não cadastrada. Produto [" + idMasc + "] desconsiderado!";
					LogDeProcessamento.gravaLog(null, msg);
					GuiGlobals.showMessageDlg(msg);
					produto = null;
					return true;
				}
				else{
					produto.setUnit(unit);
				}
							
				Unit unitEtq = GuiGlobals.getDaoFactory().getUnitDao().find(dados[LayoutProduto.UM_ETIQUETA.getPosicao()]);			
				if ( unitEtq == null )
				{
					String msg = "Unidade de etiqueta [" + dados[LayoutProduto.UM_ETIQUETA.getPosicao()] + "] não cadastrada. Produto [" + idMasc + "] desconsiderado!";
					GuiGlobals.showMessageDlg(msg);
					LogDeProcessamento.gravaLog(null, msg);
					produto = null;
					return true;
				}
				else{
					produto.setUnitEtq(unitEtq);
				}
				
				produto.setIdMasc(dados[LayoutProduto.CODIGO_PRODUTO.getPosicao()]);
				produto.setDescription(dados[LayoutProduto.NOME_PRODUTO.getPosicao()]);
				produto.setCreationDate(DateTimeUtils.now());
				
				if (dados[LayoutProduto.FIFO.getPosicao()].trim().equalsIgnoreCase("Sim") ) { 
					produto.setFifoEnabled(true);
				}
				else{
					produto.setFifoEnabled(false);
				}
				
				if (dados[LayoutProduto.TIPO_PESAGEM.getPosicao()].trim().equalsIgnoreCase("Pad")  ) { 
					produto.setWeighingStyle(WeighingStyle.STANDARD_WEIGHT);
				}
				else{
					produto.setWeighingStyle(WeighingStyle.VARIABLE_WEIGHT);
				}
						
				if (dados[LayoutProduto.VIRTUAL.getPosicao()].trim().equalsIgnoreCase("Sim") ) { 
					produto.setVirtual(true);
				}
				else{
					produto.setVirtual(false);
				}			
				
				produto.setTargetQty( Integer.parseInt(dados[LayoutProduto.QUANTIDADE_POR_EMBALAGEM.getPosicao()]) );			
				produto.setTargetWeight(NumberUtils.truncate(Double.parseDouble(dados[LayoutProduto.PESO_LIQUIDO.getPosicao()].replace(',', '.')),3));
				produto.setNetWeight(NumberUtils.truncate(Double.parseDouble(dados[LayoutProduto.PESO_PADRAO.getPosicao()].replace(',', '.')), 3));
				
				if(dados[LayoutProduto.TARA_PACOTE.getPosicao()].trim().isEmpty()){
					produto.setTareEmbala(0);
				}
				else{
					produto.setTareEmbala(NumberUtils.truncate(Double.parseDouble(dados[LayoutProduto.TARA_PACOTE.getPosicao()].replace(',', '.')),3));
				}
				
				
				if(dados[LayoutProduto.TARA_CAIXA.getPosicao()].trim().isEmpty()){
					produto.setTareBox(0);
				}
				else{
					produto.setTareBox(NumberUtils.truncate(Double.parseDouble(dados[LayoutProduto.TARA_CAIXA.getPosicao()].replace(',', '.')), 3));
				}
								
				produto.setExpirationDays( Integer.parseInt(dados[LayoutProduto.DIAS_VALIDADE.getPosicao()]) );
				produto.setPalletQty( Integer.parseInt(dados[LayoutProduto.QTDE_EMB_FECHAMENTO_PALLET.getPosicao()]) );
				
				if(dados[LayoutProduto.DESCRICAO_CONSERVACAO.getPosicao()].trim().isEmpty()){
					produto.setDescriptionConservation(null);
				}
				else{
					produto.setDescriptionConservation( dados[LayoutProduto.DESCRICAO_CONSERVACAO.getPosicao()] );
				}
				
				if(dados[LayoutProduto.DESCRICAO_SIF.getPosicao()].trim().isEmpty()){
					produto.setDescriptionSif(null);
				}
				else{
					produto.setDescriptionSif( dados[LayoutProduto.DESCRICAO_SIF.getPosicao()] );
				}	
				
				if ( !dados[LayoutProduto.TIPO_ESTOQUE.getPosicao()].trim().isEmpty() )
				{
					produto.setTypeStock(TypeStock.findByName( dados[LayoutProduto.TIPO_ESTOQUE.getPosicao()].trim() ));
				}
							
	//			DESCONTINUADO
	//			if ( dados[LayoutProduto.TIPO_ESTOQUE.getPosicao()].equals(MaterialStyle.FINISHED.getName()) ) { 
	//				produto.setMaterialStyle(MaterialStyle.FINISHED);
	//			}
	//			else{
	//				produto.setMaterialStyle(MaterialStyle.SEMI_FINISHED);
	//			}
				
				produto.setMinWeight(NumberUtils.truncate(Double.parseDouble(dados[LayoutProduto.PESO_MINIMO.getPosicao()].replace(',', '.')),3));
				produto.setMaxWeight(NumberUtils.truncate(Double.parseDouble(dados[LayoutProduto.PESO_MAXIMO.getPosicao()].replace(',', '.')),3));
				
				if(dados[LayoutProduto.EAN_13.getPosicao()].trim().isEmpty()){
					produto.setEan13("");
				}
				else{
					produto.setEan13(dados[LayoutProduto.EAN_13.getPosicao()].replace(" ", "").replace("-", ""));
				}
				
				if(dados[LayoutProduto.DUN_14.getPosicao()].trim().isEmpty()){
					produto.setDun14("");
				}
				else{
					produto.setDun14(dados[LayoutProduto.DUN_14.getPosicao()].replace(" ", "").replace("-", ""));
				}
				
				if (dados[LayoutProduto.ALTERA_PECAS.getPosicao()].trim().equalsIgnoreCase("Sim")  ) { 
					produto.setChangeQtyPecasEnabled(true);
				}
				else{
					produto.setChangeQtyPecasEnabled(false);
				}
				
				if (dados[LayoutProduto.ALTERA_ETIQUETA.getPosicao()].trim().equalsIgnoreCase("Sim")  ) { 
					produto.setChangeQtyBoxEnabled(true);
				}
				else{
					produto.setChangeQtyBoxEnabled(false);
				}
				
				if( ( dados.length - 1 ) >= LayoutProduto.CODIGO_SIF.getPosicao() && !dados[LayoutProduto.CODIGO_SIF.getPosicao()].trim().isEmpty()){
					produto.setCodSif( dados[LayoutProduto.CODIGO_SIF.getPosicao()].trim() );
				}
				
				if( ( dados.length - 1 ) >= LayoutProduto.PERCENTUAL_LIBERA_BALANCA.getPosicao() && !dados[LayoutProduto.PERCENTUAL_LIBERA_BALANCA.getPosicao()].trim().isEmpty()){
					produto.setPercentRemoveProductOnScale( Double.parseDouble( dados[LayoutProduto.PERCENTUAL_LIBERA_BALANCA.getPosicao()].trim().replace(",", ".") ) );
				}
				
				if( ( dados.length - 1 ) >= LayoutProduto.PREFIXO_GS1.getPosicao() && !dados[LayoutProduto.PREFIXO_GS1.getPosicao()].trim().isEmpty()){
					produto.setPrefixEnterpriseGS1( dados[LayoutProduto.PREFIXO_GS1.getPosicao()].trim() );
				}
				
				if( ( dados.length - 1 ) >= LayoutProduto.SEQUENCIA_PROCESSO.getPosicao() && !dados[LayoutProduto.SEQUENCIA_PROCESSO.getPosicao()].trim().isEmpty()){
					produto.setSequenciaProcessProduct( dados[LayoutProduto.SEQUENCIA_PROCESSO.getPosicao()].trim() );
				}
				
				if ( addProduct ){
					produto.setQtyCaracteresDesc(40);
					Unit unitEmb = GuiGlobals.getDaoFactory().getUnitDao().find("CX");
					produto.setUnitEmb(unitEmb);
				}
				
				produto.setPercentShipmentComplement(0.0);
			}
			catch ( Exception e )
			{
				LogDeProcessamento.gravaLog("error", "Falha na importação: " + e.getMessage());
				return false;
			}
			
			daoFactory.beginTransaction();	
			
			if ( addProduct )
			{
				daoFactory.getProductDao().add(produto);
			}
			else
			{
				daoFactory.getProductDao().update(produto);
			}
			
			daoFactory.commit();
		
		}
		
		return true;
		
	}

	@Override
	public void execute(boolean removeOnFinal) {
		ReadFile readFileCab = new ReadFile(this);
		try {
			readFileCab.read(false, null, 0, removeOnFinal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
