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
import br.com.cardeal.enums.MaterialStyle;
import br.com.cardeal.enums.TypeStock;
import br.com.cardeal.enums.WeighingStyle;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.Utils;

@Entity
public class Product {
	
	@Transient
	private final int lenghtFieldIdMasc = 5;
	
	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private int id;
    
    @Column(length=lenghtFieldIdMasc)
    private String idMasc;
        
    private String codSif;

	@Column(length=80)
    private String descriptionConservation;
    
    @Column(length=120)
    private String descriptionSif;
        
    @Column(length=60)
    private String description;
    
    @Column(length=2)
    private int qtyCaracteresDesc;

	@Column(length=13)
    private String ean13;
    
    @Column(length=14)
    private String dun14;

    @ManyToOne
    private Unit unit;
    
    @ManyToOne
    private Unit unitEtq;
    
    @ManyToOne
    private Unit unitEmb;

	private WeighingStyle weighingStyle;
	
//	private StockStyle stockStyle;
	
	private MaterialStyle materialStyle; // Não está mais sendo utilizado
	
	private TypeStock typeStock;
	
	private int targetQty;
	
//	private int minQty;
	
//	private int maxQty;
	
	private double targetWeight;
	
	private double netWeight;
	
	private double minWeight;
	
	private double maxWeight;
	
	private double tareEmbala; 
	
	private double tareBox;
	
	@Transient
	private double tareTotal;
	
	private int palletQty;
	
	private Date creationDate;
	
	private int expirationDays;
	
	private boolean fifoEnabled;
	
	private boolean changeQtyPecasEnabled;
	
	private boolean changeQtyBoxEnabled;
	
	private boolean virtual;
	
    @ManyToOne
	private Etiqueta labelFileName; // Label da etiqueta de caixa
    
    @ManyToOne
	private Etiqueta labelPalletFileName; // Label da etiqueta de pallet
    
    private boolean blocked;
    
    @Column(length=4)
    private double percentRemoveProductOnScale;
    
    private String prefixEnterpriseGS1;
    
    private String sequenciaProcessProduct;

    @Column(nullable = false, columnDefinition="Decimal(5,2) default '0.00'")
    private double percentShipmentComplement;
    
	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public Etiqueta getLabelPalletFileName() {
		return labelPalletFileName;
	}

	public void setLabelPalletFileName(Etiqueta labelPalletFileName) {
		this.labelPalletFileName = labelPalletFileName;
	}
	
	public Etiqueta getLabelFileName() {
		return labelFileName;
	}

	public void setLabelFileName(Etiqueta labelFileName) {
		this.labelFileName = labelFileName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdMasc() {
		return idMasc;
	}
	
	public void setIdMasc(String idMasc) {
		
		if ( idMasc != null && idMasc.trim().length() < lenghtFieldIdMasc ){
			this.idMasc = StringUtils.leftPad(idMasc, (lenghtFieldIdMasc-1), "0");
		}
		else{
			this.idMasc = idMasc;
		}
		
	}

	public String getDescriptionConservation() {
		return descriptionConservation;
	}

	public void setDescriptionConservation(String descriptionConservation) {
		this.descriptionConservation = descriptionConservation;
	}

	public String getDescriptionSif() {
		return descriptionSif;
	}

	public void setDescriptionSif(String descriptionSif) {
		this.descriptionSif = descriptionSif;
	}
	public String getDescriptionTruncated() 
	{
		if ( description.length() > 40 )
			return description.substring(0, 39);
		else
			return description;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQtyCaracteresDesc() {
		return qtyCaracteresDesc;
	}

	public void setQtyCaracteresDesc(int qtyCaracteresDesc) {
		this.qtyCaracteresDesc = qtyCaracteresDesc;
	}
	
	public String getEan13() {
		return this.ean13;
	}

	public String getEan13Etq() {

		String ean13 = null;
		
		if ( this.ean13 == null || this.ean13.isEmpty() ){
			ean13 = StringUtils.leftPad( "", 13, "0" );
		}else{
			ean13 = this.ean13.trim();
		}
		
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	public String getDun14() {
		return this.dun14;
	}
	
	public String getDun14Etq() {
		
		String dun14 = null;
		
		if ( this.dun14 == null || this.dun14.isEmpty() ){
			dun14 = StringUtils.leftPad( "", 14, "0" );
		}else{
			dun14 = this.dun14.trim();
		}
		
		return dun14;
	}

	public void setDun14(String dun14) {
		this.dun14 = dun14;
	}

	public WeighingStyle getWeighingStyle() {
		return weighingStyle;
	}

	public void setWeighingStyle(WeighingStyle packStyle) {
		this.weighingStyle = packStyle;
	}

//	public StockStyle getStockStyle() {
//		return stockStyle;
//	}
//
//
//
//	public void setStockStyle(StockStyle stockStyle) {
//		this.stockStyle = stockStyle;
//	}

	public MaterialStyle getMaterialStyle() {
		return materialStyle;
	}

	public void setMaterialStyle(MaterialStyle materialStyle) {
		this.materialStyle = materialStyle;
	}

	public int getTargetQty() {
		return targetQty;
	}

	public void setTargetQty(int targetQty) {
		this.targetQty = targetQty;
	}

//	public int getMinQty() {
//		return minQty;
//	}
//
//
//
//	public void setMinQty(int minQty) {
//		this.minQty = minQty;
//	}
//
//
//
//	public int getMaxQty() {
//		return maxQty;
//	}
//
//
//
//	public void setMaxQty(int maxQty) {
//		this.maxQty = maxQty;
//	}

	public double getTargetWeight() {
		return targetWeight;
	}

	public void setTargetWeight(double targetWeight) {
		this.targetWeight = targetWeight;
	}	

	public double getMinWeight() {
		return minWeight;
	}
	
	public double getNetWeight() {
		return netWeight;
	}
	
	public boolean isStandardWeight()
	{
		return netWeight > 0;
	}

	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
	}

	public void setMinWeight(double minWeight) {
		this.minWeight = minWeight;
	}

	public double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public double getTareEmbala() {
		return tareEmbala;
	}

	public void setTareEmbala(double tareEmbala) {
		this.tareEmbala = tareEmbala;
	}

	public double getTareBox() {
		return tareBox;
	}

	public void setTareBox(double tareBox) {
		this.tareBox = tareBox;
	}

	public int getPalletQty() {
		return palletQty;
	}

	public void setPalletQty(int palletQty) {
		this.palletQty = palletQty;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getCreationDateTimeFormat() {
		return DateTimeUtils.getDate( creationDate, "dd/MM/yyyy HH:mm" );
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getExpirationDays() {
		return expirationDays;
	}

	public void setExpirationDays(int expirationDays) {
		this.expirationDays = expirationDays;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	public Unit getUnitEtq() {
		return unitEtq;
	}

	public void setUnitEtq(Unit unitEtq) {
		this.unitEtq = unitEtq;
	}
	
	public Unit getUnitEmb() {
		return unitEmb;
	}

	public void setUnitEmb(Unit unitEmb) {
		this.unitEmb = unitEmb;
	}

	public String toString() {
		if(description == null)
			return idMasc;
		else
			return "[" + idMasc.trim() + "] - " + description.trim();
	}

	public boolean isFifoEnabled() {
		return fifoEnabled;
	}

	public void setFifoEnabled(boolean fifoEnabled) {
		this.fifoEnabled = fifoEnabled;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	public double weightPerPiece() {
		if(targetQty > 0)
			return Double.parseDouble(Utils.formatWeight(targetWeight / targetQty));
		else
			return 0;
	}
	
	public double standartWeightUnit() {
		if(targetQty > 0)
			return Double.parseDouble(Utils.formatWeight(netWeight / targetQty));
		else
			return 0;
	}
	
	public boolean isChangeQtyPecasEnabled() {
		return changeQtyPecasEnabled;
	}

	public void setChangeQtyPecasEnabled(boolean changeQtyPecasEnabled) {
		this.changeQtyPecasEnabled = changeQtyPecasEnabled;
	}
	
	public boolean isChangeQtyBoxEnabled() {
		return changeQtyBoxEnabled;
	}

	public void setChangeQtyBoxEnabled(boolean changeQtyBoxEnabled) {
		this.changeQtyBoxEnabled = changeQtyBoxEnabled;
	}
	
	public double getPercentRemoveProductOnScale() {
		return percentRemoveProductOnScale;
	}

	public void setPercentRemoveProductOnScale(double percentRemoveProductOnScale) {
		this.percentRemoveProductOnScale = percentRemoveProductOnScale;
	}
	
	public String getCodSif() {
		return codSif;
	}

	public void setCodSif(String codSif) {
		this.codSif = codSif;
	}
	
	public String getPrefixEnterpriseGS1() {
		return prefixEnterpriseGS1;
	}

	public void setPrefixEnterpriseGS1(String prefixEnterpriseGS1) {
		this.prefixEnterpriseGS1 = prefixEnterpriseGS1;
	}

	public Product clone() {
		
	    Product newProduct = new Product();
	    newProduct.setId(this.getId());
	    newProduct.setIdMasc(this.getIdMasc());
	    newProduct.setDescriptionConservation(this.getDescriptionConservation());
	    newProduct.setDescriptionSif(this.getDescriptionSif());	    
	    newProduct.setDescription(this.getDescription());
	    newProduct.setQtyCaracteresDesc(this.getQtyCaracteresDesc());
	    newProduct.setCreationDate(this.getCreationDate());
	    newProduct.setEan13(this.getEan13());
	    newProduct.setWeighingStyle(this.getWeighingStyle());
	    newProduct.setExpirationDays(this.getExpirationDays());
	    newProduct.setDun14(this.getDun14());	    
	    newProduct.setTypeStock(this.getTypeStock());
	    newProduct.setMaxWeight(this.getMaxWeight());
	    newProduct.setMinWeight(this.getMinWeight());
	    newProduct.setWeighingStyle(this.getWeighingStyle());
	    newProduct.setNetWeight(this.getNetWeight());
	    newProduct.setPalletQty(this.getPalletQty());
	    newProduct.setTareEmbala(this.getTareEmbala());
	    newProduct.setTareBox(this.getTareBox());
	    newProduct.setTargetQty(this.getTargetQty());
	    newProduct.setTargetWeight(this.getTargetWeight());
	    newProduct.setUnitEtq(this.getUnitEtq());
	    newProduct.setUnitEmb(this.getUnitEmb());
	    newProduct.setFifoEnabled(this.isFifoEnabled());
	    newProduct.setChangeQtyPecasEnabled(this.isChangeQtyPecasEnabled());
	    newProduct.setChangeQtyBoxEnabled(this.isChangeQtyBoxEnabled());
	    newProduct.setVirtual(this.isVirtual());
	    newProduct.setBlocked(this.isBlocked());
	    newProduct.setLabelFileName(labelFileName);
	    newProduct.setLabelPalletFileName(labelPalletFileName);
	    newProduct.setUnit(this.getUnit());
	    newProduct.setPercentRemoveProductOnScale(this.getPercentRemoveProductOnScale());
	    newProduct.setPercentShipmentComplement(this.getPercentShipmentComplement());
	    newProduct.setCodSif( this.getCodSif() );
	    newProduct.setPrefixEnterpriseGS1(this.prefixEnterpriseGS1);
	    newProduct.setSequenciaProcessProduct(this.sequenciaProcessProduct);
	    
	    return newProduct;
	}

	public String getSequenciaProcessProduct() {
		return sequenciaProcessProduct;
	}

	public void setSequenciaProcessProduct(String sequenciaProcessProduct) {
		this.sequenciaProcessProduct = sequenciaProcessProduct;
	}

	public double getTareTotal() {
		return ( getTareBox() + ( getTareEmbala() * getTargetQty() ) );
	}

	public void setTareTotal(double tareTotal) {
		this.tareTotal = tareTotal;
	}

	public double getPercentShipmentComplement() {
		return percentShipmentComplement;
	}

	public void setPercentShipmentComplement(double percentShipmentComplement) {
		this.percentShipmentComplement = percentShipmentComplement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Product other = (Product) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public Date getExpirationDate( Date manufactureDate ){		
		int ajustCalc = ( getExpirationDays() == 0 ) ? 0 : 1;		
		return DateTimeUtils.addDays( manufactureDate, ( getExpirationDays()-ajustCalc));
	}

	public TypeStock getTypeStock() {
		return typeStock;
	}

	public void setTypeStock(TypeStock typeStock) {
		this.typeStock = typeStock;
	}
	
	
}
