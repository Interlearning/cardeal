package br.com.cardeal.globals;

import java.util.Date;
import java.util.List;

import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.model.Company;
import br.com.cardeal.model.ItensPalletVirtual;
import br.com.cardeal.model.Pallet;
import br.com.cardeal.model.Partner;
import br.com.cardeal.model.Product;
import br.com.cardeal.model.Terminal;
import br.com.cardeal.model.User;
import br.com.cardeal.views.PackingPanelWindows;

public class PackingData {
	public static final int ACTION_PACK = 0;
	public static final int ACTION_CLOSE_PALLET = 1;
	public static final int ACTION_PRINT_BOX = 2;
	public static final int ACTION_PRINT_PALLET = 3;
	public static final int ACTION_REPALLETIZAR = 4;
	public static final int ACTION_PALLLET_VIRTUAL = 5;
	
	private String batch;
	private double net;
	private double netStorage;
	private double tare;
	private int primaryQty;	 	// Quantidade de Peças
	private int primaryQtyOperation;	 	// Quantidade de Peças digitada pelo usuario na tela
	private int secondaryQty; 	// Quantidade de Caixas
	private int qtdEtiquetas;  	// Quantidade de Estiquetas 
	private Product product;
	private Partner partner;
	private User user;
	private Terminal terminal;
	private Company company;
	private Pallet pallet;
	private boolean inFifo;
	private Date date;
	private int action;
	private boolean secondPrint = false;
	private boolean changedQty = false;
	private TypeStock typeStock;
	private String dun14;
	private double taraCaixa;
	private double taraEmbalagem;
	private String etqHowSell;
	private PackingPanelWindows packingPanel;
	private Date expirationDate;
	private boolean forceVariableWeight;
	private boolean exceptionStandardWeight = false;
	private String addOrRemove = null;
	private String loteExternal = "";
	private Date validateDateLoteExternal = null;
	private List<ItensPalletVirtual> itensPalletVirtual = null;

	public PackingPanelWindows getObjPackingPanel() {
		return packingPanel;		
	}

	public void setObjPanel(PackingPanelWindows objectPanel) {
		this.packingPanel = objectPanel;
	}
		
	public String getEtqHowSell() {
		return etqHowSell;
	}
	public void setEtqHowSell(String etqHowSell) {
		this.etqHowSell = etqHowSell;
	}
	public User getUser() {
		return user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isInFifo() {
		return inFifo;
	}
	public void setInFifo(boolean inFifo) {
		this.inFifo = inFifo;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Terminal getTerminal() {
		return terminal;
	}
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	public String getBatch() {
		return batch;
	}
	public Pallet getPallet() {
		return pallet;
	}
	public void setPallet(Pallet pallet) {
		this.pallet = pallet;
	}
	public int getPalletId() {
		if(pallet == null)
			return 0;
		else
			return pallet.getId();
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public double getNet() {
		return net;
	}
	public void setNet(double net) {
		this.net = net;
	}
	public double getTare() {
		return tare;
	}
	public void setTare(double tare) {
		this.tare = tare;
	}
	public int getPrimaryQty() {
		return primaryQty;
	}
	public void setPrimaryQty(int primaryQty) {		
		this.primaryQty = ( ( primaryQty == 0 ) ? 1 : primaryQty );
	}
	public int getSecondaryQty() {
		return secondaryQty;
	}
	public void setSecondaryQty(int secondaryQty) {
		this.secondaryQty = secondaryQty;
	}
	
	public void setQtdEtiquetas(int qtdEtiquetas){
		this.qtdEtiquetas = qtdEtiquetas;
	}
	public int getQtdEtiquetas(){
		return qtdEtiquetas;
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public boolean isSecondPrint() {
		return secondPrint;
	}
	public void setSecondPrint(boolean secondPrint) {
		this.secondPrint = secondPrint;
	}
	public boolean isChangedQty() {
		return changedQty;
	}
	public void setChangedQty(boolean changedQty) {
		this.changedQty = changedQty;
	}
	public TypeStock getTypeStock() {
		return typeStock;
	}
	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}
	
	public String getDun14() {
		return dun14;
	}
	public void setDun14(String dun14) {
		this.dun14 = dun14;
	}
	public void setTaraCaixa(double taraCaixa) {
		this.taraCaixa = taraCaixa;
	}
	public double getTaraCaixa() {
		return taraCaixa;
	}	
	public double getTaraEmbalagem() {
		return taraEmbalagem;
	}
	public void setTaraEmbalagem(double taraEmbalagem) {
		this.taraEmbalagem = taraEmbalagem;
	}
	public double getNetStorage() {
		return netStorage;
	}
	public void setNetStorage(double netStorage) {
		this.netStorage = netStorage;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public boolean isForceVariableWeight() {
		return forceVariableWeight;
	}
	
	public void setForceVariableWeight(boolean forceVariableWeight) {
		this.forceVariableWeight = forceVariableWeight;
	}

	public boolean isExceptionStandardWeight() {
		return exceptionStandardWeight;
	}

	public void setExceptionStandardWeight(boolean exceptionStandardWeight) {
		this.exceptionStandardWeight = exceptionStandardWeight;
	}

	public int getPrimaryQtyOperation() {
		return primaryQtyOperation;
	}

	public void setPrimaryQtyOperation(int primaryQtyOperation) {
		this.primaryQtyOperation = primaryQtyOperation;
	}

	public void setAddOrRemove(String addOrRemove) {
		this.addOrRemove = addOrRemove;
	}
	
	public String getAddOrRemove() {
		return addOrRemove;
	}

	public void setItensPalletVirtual(List<ItensPalletVirtual> itensPalletVirtual) 
	{
		this.itensPalletVirtual = itensPalletVirtual;
	}
	
	public List<ItensPalletVirtual> getItensPalletVirtual()
	{
		return itensPalletVirtual;
	}

	public String getLoteExternal() {
		return loteExternal;
	}

	public void setLoteExternal(String loteExternal) {
		this.loteExternal = loteExternal;
	}

	public Date getValidateDateLoteExternal() {
		return validateDateLoteExternal;
	}

	public void setValidateDateLoteExternal(Date validateDateLoteExternal) {
		this.validateDateLoteExternal = validateDateLoteExternal;
	}
	
}

