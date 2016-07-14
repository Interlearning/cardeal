package br.com.cardeal.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import org.apache.commons.lang.StringUtils;
import br.com.cardeal.enums.StockStatus;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Utils;

@Entity
public class Stock {

	// cada registro desta tabela indica uma embalagem secundária

	@Id
	@SequenceGenerator(name = "seq", initialValue = 1, allocationSize = 100)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq")
	private long id;

	private long idOld;

	@ManyToOne
	private Product product;
	
	@ManyToOne
	private User user; // Operador que fez a entrada do produto em estoque

	@ManyToOne
	private Terminal terminal; // Terminal que fez a entrada do produto em
							// estoque
	
	@ManyToOne
	private IdentifyLogisticProduct logisticProduct; // id do produto da tabela IdentifyLogisticProduct

	private int primaryQty; // quantidade de peças que a etiqueta representa
	
	@Column(nullable = false, columnDefinition = "int default 0")
	private int primaryQtyOperation; // quantidade de peças que o usuario digitou na tela
	
	private int secondaryQty; // quantidade de caixas que a etiqueta representa

	private double net; // peso liquido da embalagem
	
	private double netEtq; // peso liquido na etiqueta

	private double tare;
	
	private double tareBox;
	
	private double taraEmbalagem;

	private Date enterDate;

	private Date manufactureDate;

	private Date expirationDate;

	@ManyToOne
	private Order order;

	@Column(length = 6)
	private String batch;

	@ManyToOne
	private Partner partner;

	@Transient
	private boolean selected;
	
	private boolean inFifo; // indica se este item em estoque está na FIFO, ou
							// seja, se pode ser expedida por quantidade

	@ManyToOne
	private Pallet pallet;

	private TypeStock typeStock; // Tipo do estoque

	private StockStatus status; // se está ou não em estoque

	private double availableNet; // quantidade de peso liquido disponivel em
								 // estoque para este item. Util somente para
								 // granel

	private int availableQty; // quantidade de peças disponiveis em estoque para
							  // este item. Util somente para granel
	
	private String sscc;

	@ManyToOne
	private Company company;
	
	private String howSell;
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getUnitDesc() {
		if (product != null && product.getUnit().getId() != null
				&& product.getUnit().getId().length() > 0)
			return "1 " + product.getUnit().getId();
		else
			return "";
	}

	public Pallet getPallet() {
		return pallet;
	}

	public void setPallet(Pallet pallet) {
		this.pallet = pallet;
	}

	public boolean isInFifo() {
		return inFifo;
	}

	public void setInFifo(boolean inFifo) {
		this.inFifo = inFifo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdOld() {
		return idOld;
	}

	public void setIdOld(long idOld) {
		this.idOld = idOld;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


	public int getPrimaryQty() {
		return primaryQty;
	}

	public void setPrimaryQty(int primaryQty) {
		this.primaryQty = primaryQty;
	}
	
	//WJSP 11/06/2015
	public int getSecondaryQty(){
		return secondaryQty;
	}
	public void setSecondaryQty(int secondaryQty){
		this.secondaryQty = secondaryQty;
	}
	
	public double getNet() {
		return net;
	}
	
	public String getNetFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(net, 3), 14, 3, false, false);
	}

	public void setNet(double net) {
		this.net = net;
	}

	public double getTare() {
		return tare;
	}
	
	public String getTareFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(tare, 3), 14, 3, false, false);
	}

	public void setTare(double tare) {
		this.tare = tare;
	}

	public Date getEnterDate() {
		return enterDate;
	}

	public String getEnterDateFormat() 
	{
		return DateTimeUtils.getDate(enterDate, "dd/MM/yyyy");
	}

	public String getEnterDateTimeFormat() {
		return DateTimeUtils.getDate(enterDate, "dd/MM/yyyy HH:mm");
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public String getFifoDesc() {
		if (inFifo)
			return GuiGlobals.getBundle().getString("STR000011");
		else
			return GuiGlobals.getBundle().getString("STR000012");
	}

	public Date getManufactureDate() {
		return manufactureDate;
	}

	public Date getManufactureDateFormat() {
		return DateTimeUtils.getDateFormat(manufactureDate, "dd/MM/yyyy");
	}

	public String getManufactureDateTimeFormat() {
		return DateTimeUtils.getDate(manufactureDate, "dd/MM/yyyy HH:mm");
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getExpirationDateFormat() {
		return DateTimeUtils.getDate(expirationDate, "dd/MM/yyyy");
	}

	public String getExpirationDateTimeFormat() {
		return DateTimeUtils.getDate(expirationDate, "dd/MM/yyyy HH:mm");
	}

	public User getUser() {
		return user;
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
	
	public IdentifyLogisticProduct getIdentifyLogisticProduct() {
		return logisticProduct;
	}

	public void setIdentifyLogisticProduct(IdentifyLogisticProduct logisticProduct) {
		this.logisticProduct = logisticProduct;
	}

	public StockStatus getStatus() {
		return status;
	}

	public void setStatus(StockStatus status) {
		this.status = status;
	}

	public double getAvailableNet() {
		return availableNet;
	}

	public void setAvailableNet(double availableNet) {
		this.availableNet = availableNet;
	}

	public int getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(int availableQty) {
		this.availableQty = availableQty;
	}

	public TypeStock getTypeStock() {
		return typeStock;
	}

	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}

	public String getIdFormatSerial() {
		return Utils.formatSerial(id);
	}

	public double getNetEtq() {
		return netEtq;
	}

	public void setNetEtq(double netEtq) {
		this.netEtq = netEtq;
	}

	public String getIdFormated() {
		return StringUtils.leftPad( String.valueOf(id), 8);
	}

	public String getHowSell() {
		return howSell;
	}

	public void setHowSell(String howSell) {
		this.howSell = howSell;
	}

	public double getTareBox() {
		return tareBox;
	}

	public void setTareBox(double tareBox) {
		this.tareBox = tareBox;
	}

	public double getTaraEmbalagem() {
		return taraEmbalagem;
	}

	public void setTaraEmbalagem(double taraEmbalagem) {
		this.taraEmbalagem = taraEmbalagem;
	}

	public String getSscc() {
		return sscc;
	}

	public void setSscc(String sscc) {
		this.sscc = sscc;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getPrimaryQtyOperation() {
		return primaryQtyOperation;
	}

	public void setPrimaryQtyOperation(int primaryQtyOperation) {
		this.primaryQtyOperation = primaryQtyOperation;
	}
	
	@Transient
	public String getOperation()
	{
		if ( ( getHowSell() == null || getHowSell().isEmpty() ) )
		{
			return "";
		}
		else
		{
			return getOperation( ( ( getHowSell().toUpperCase().trim().contains("EMB") ) ? false : true  ) );
		}
	}
	
	@Transient
	public String getOperation( boolean isPackingPanel )
	{
		String operation = "";
		if(!isPackingPanel)
		{
			operation +="A ";
		}
		else
		{ 
			if( isInFifo() )
			{
				operation += "F ";
			}
			else if (getProduct().getWeighingStyle() == WeighingStyle.STANDARD_WEIGHT) 
			{
				operation += "P ";
			}
			else if(getProduct().getWeighingStyle() == WeighingStyle.VARIABLE_WEIGHT) 
			{
				operation += "V ";
			}
		}
		
		return operation;
	}

	public double getHowSelled() 
	{
		double qtyReturn = 0.0;
		String howSell = getHowSell();
		
		if ( howSell != null && !( howSell.toUpperCase().trim().equals("KG") ) )
		{
			String howSellToArray[] = howSell.toUpperCase().trim().split(" ");
			qtyReturn = Double.valueOf( howSellToArray[0] );
		}
		else
		{
			qtyReturn = getNet();
		}
		
		return qtyReturn;
	}
	
	public String getHowSelledFormat() 
	{
		double qty = getHowSelled();
		return StringUtils.leftPad( String.format("%.3f", qty ), 8, "0" );
	}
	
	public Stock cloneStock()
	{
		Stock stock = new Stock();
		stock.setAvailableNet(this.getAvailableNet());
		stock.setAvailableQty(this.getAvailableQty());
		stock.setBatch(this.getBatch());
		stock.setCompany(this.getCompany());
		stock.setEnterDate(this.getEnterDate());
		stock.setExpirationDate(this.getExpirationDate());
		stock.setHowSell(this.getHowSell());
		stock.setIdentifyLogisticProduct(this.getIdentifyLogisticProduct());
		stock.setInFifo(this.isInFifo());
		stock.setManufactureDate(this.getManufactureDate());
		stock.setNet(this.getNet());
		stock.setNetEtq(this.getNetEtq());
		stock.setOrder(this.getOrder());
		stock.setPallet(this.getPallet());
		stock.setPartner(this.getPartner());
		stock.setPrimaryQty(this.getPrimaryQty());
		stock.setPrimaryQtyOperation(this.getPrimaryQtyOperation());
		stock.setProduct(this.getProduct());
		stock.setSecondaryQty(this.getSecondaryQty());
		stock.setSelected(this.isSelected());
		stock.setSscc(this.getSscc());
		stock.setStatus(this.getStatus());
		stock.setTaraEmbalagem(this.getTaraEmbalagem());
		stock.setTare(this.getTare());
		stock.setTareBox(this.getTareBox());
		stock.setTerminal(this.getTerminal());
		stock.setTypeStock(this.getTypeStock());
		stock.setUser(this.getUser());
		
		return stock;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stock other = (Stock) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
