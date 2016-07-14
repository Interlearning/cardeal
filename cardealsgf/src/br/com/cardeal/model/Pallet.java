package br.com.cardeal.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import br.com.cardeal.enums.PalletStatus;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.NumberUtils;
import br.com.cardeal.globals.Utils;

@Entity
public class Pallet {
	
	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private int id;
	
	private Date openDate;
	
	private Date closeDate;

	@ManyToOne
	private Terminal terminal;
	
	@ManyToOne
	private IdentifyLogisticPallet identifyLogisticPallet; // id do pallet da tabela IdentifyLogisticPallet
	
	private PalletStatus status;
	
	@ManyToOne
	private Product product;

	private double tare;  //peso da plataforma do pallet
	
	private double strech; //peso do strech
	
	private double tareCantoneira; // peso da cantoneira
	
	private double tareRack; // peso do rack

	private double tarePack; // peso da embalagem
	
	@Transient
	private int primaryQty;
	
	@Transient
	private int secondaryQty;
	
	@Transient
	private int embQty;
	
	@Transient
	private double net;

	@Transient
	private double tareOfPacks;
	
	@Transient
	private List<Stock> stocks;
	
	@Transient
	private Date expirationDate;
	
	@Transient
	private Date manufactureDate;
	
	private String batch;
	
	private String howSell;
	
	@Transient
	private int totalEtq;
	
	@ManyToOne
	private Company company;
	
	private String sscc;
	
	@Transient
	private String motivo;
	
	@ManyToOne
	private Order order;

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
	
	public int getId() {
		return id;
	}
	
	public String getIdFormatted() {
		return Utils.formatPallet(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	
	public IdentifyLogisticPallet getIdentifyLogisticPallet() {
		return identifyLogisticPallet;
	}

	public void setIdentifyLogisticPallet(IdentifyLogisticPallet logisticPallet) {
		this.identifyLogisticPallet = logisticPallet;
	}

	public PalletStatus getStatus() {
		return status;
	}

	public void setStatus(PalletStatus status) {
		this.status = status;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getTare() {
		return tare;
	}
	
	public String getTareFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(tare, 3), 14, 3, false, false);
	}
	
	public String getTareFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(tare, 3), 14, 3, false, true);
	}

	public void setTare(double tare) {
		this.tare = tare;
	}

	public int getPrimaryQty() {
		return primaryQty;
	}
	
	public String getPrimaryQtyFormatted() {
		return String.valueOf(primaryQty);
	}

	public void setPrimaryQty(int primaryQty) {
		this.primaryQty = primaryQty;
	}

	public int getSecondaryQty() {
		return secondaryQty;
	}
	
	public String getSecondaryQtyFormatted() {
		return String.valueOf( secondaryQty );
	}

	public void setSecondaryQty(int secondaryQty) {
		this.secondaryQty = secondaryQty;
	}

	public double getNet() {
		return net;
	}

	public void setNet(double net) {
		this.net = net;
	}
	
	public String getNetFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(net, 3), 14, 3, false, false);
	}
	
	public String getNetFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(net, 3), 14, 3, false, true);
	}

	public double getTareOfPacks() {
		return tareOfPacks;
	}

	public String getTareOfPacksFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(tareOfPacks, 3), 14, 3, false, false);
	}
	
	public String getTareOfPacksFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(tareOfPacks, 3), 14, 3, false, true);
	}
	
	public void setTareOfPacks(double tareOfPacks) {
		this.tareOfPacks = tareOfPacks;
	}

	public double getStrech() {
		return strech;
	}
	
	public String getStrechFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(strech, 3), 14, 3, false, false);
	}
	
	public String getStrechFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(strech, 3), 14, 3, false, true);
	}

	public void setStrech(double strech) {
		this.strech = strech;
	}	
	
	public String getOpenDateDesc() {
		if(openDate == null)
			return "";
		else
			return DateTimeUtils.formatDate(openDate);
	}
	
	public String getOpenDateFormat() {
		if(openDate == null)
			return "";
		else
			return DateTimeUtils.getDate( openDate, "dd/MM/yyyy HH:mm" );
	}
	
	public String getCloseDateDesc() {
		if(closeDate == null)
			return "";
		else
			return DateTimeUtils.formatDate(closeDate);
	}
	
	public String getCloseDateFormat() {
		if(closeDate == null)
			return "";
		else
			return DateTimeUtils.getDate( closeDate, "dd/MM/yyyy HH:mm" );
	}
	
	public String getManufactureDateFormat() {
		if(manufactureDate == null)
			return "";
		else
			return DateTimeUtils.getDate( manufactureDate, "dd/MM/yyyy HH:mm" );
	}
	
	public String getManufactureDateDesc() {
		if(manufactureDate == null)
			return "";
		else
			return DateTimeUtils.formatDate(manufactureDate);
	}
	
	public String getExpirationDateFormat() {
		if(expirationDate == null)
			return "";
		else
			return DateTimeUtils.getDate( expirationDate, "dd/MM/yyyy HH:mm" );
	}
	
	public String getStatusDesc() {
		return status.getDesc();
	}
	
	public String getTareOfPacksDesc() {
		return String.format("%.2f", tareOfPacks);
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public double getTareCantoneira() {
		return tareCantoneira;
	}
	
	public String getTareCantoneiraFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(tareCantoneira, 3), 14, 3, false, false);
	}
	
	public String getTareCantoneiraFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(tareCantoneira, 3), 14, 3, false, true);
	}

	public void setTareCantoneira(double tareCantoneira) {
		this.tareCantoneira = tareCantoneira;
	}

	public double getTareRack() {
		return tareRack;
	}
	
	public String getTareRackFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(tareRack, 3), 14, 3, false, false);
	}
	
	public String getTareRackFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(tareRack, 3), 14, 3, false, true);
	}

	public void setTareRack(double tareRack) {
		this.tareRack = tareRack;
	}

	public double getTarePack() {
		return tarePack;
	}
	
	public String getTarePackFormatted() {
		return NumberUtils.transform(NumberUtils.truncate(tarePack, 3), 14, 3, false, false);
	}
	
	public String getTarePackFormattedWithDot() {
		return NumberUtils.transform(NumberUtils.truncate(tarePack, 3), 14, 3, false, true);
	}

	public void setTarePack(double tarePack) {
		this.tarePack = tarePack;
	}
	
	
	/* WJSP 25/06/2015
	 * Set e Get do Total de Etiquetas para controle de fechamento do Pallet
	 */
	public void setTotalEtq(int totalEtq){
		this.totalEtq = totalEtq;
	}
	
	public int getTotalEtq(){
		return totalEtq;
	}

	public Date getExpirationDate() 
	{
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Date getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public String getHowSell() {
		return howSell;
	}

	public void setHowSell(String howSell) {
		this.howSell = howSell;
	}

	public String getSscc() {
		return sscc;
	}

	public void setSscc(String sscc) {
		this.sscc = sscc;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	public int getEmbQty() {
		return embQty;
	}

	public void setEmbQty(int embQty) {
		this.embQty = embQty;
	}
	
}
