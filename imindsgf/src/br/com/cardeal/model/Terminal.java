package br.com.cardeal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.printer.PrinterModel;
import br.com.cardeal.scale.ScaleModel;
import br.com.cardeal.scale.ScaleUnit;

@Entity
public class Terminal {

	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private int id;
		
	@Column(length=2)
	private String idTerminal;

	@Column(length=50)
	private String description;
	
	@Column(length=50)
	private String printerName;
	
	private PrinterModel printerModel;
	
	@Column(length=18)
	private String printerPort;	
	
	@Column(length=18)
	private String scale1Port;

	@Column(length=18)
	private String scale2Port;
	
	private ScaleModel scale1Model;
	
	private ScaleModel scale2Model;
	
	private ScaleUnit scale1Unit;
	
	private ScaleUnit scale2Unit;
	
	private int timeWait1;
	
	private int timeWait2;
	
	@Column(length=18)
	private String scannerPort;
	
	@Column(length=255)
	private String tempDirectory;
	
	@ManyToOne
	private Company company;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int printerBaudRate;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int printerDataBits;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int printerParity;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int printerStopBits;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int scale1BaudRate;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int scale1DataBits;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int scale1Parity;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int scale1StopBits;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int scale2BaudRate;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int scale2DataBits;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int scale2Parity;
	
	@Column(nullable = false, columnDefinition = "int default -1")
	private int scale2StopBits;

	public int getScale2BaudRate() {
		return scale2BaudRate;
	}

	public void setScale2BaudRate(int scale2BaudRate) {
		this.scale2BaudRate = scale2BaudRate;
	}

	public int getScale2DataBits() {
		return scale2DataBits;
	}

	public void setScale2DataBits(int scale2DataBits) {
		this.scale2DataBits = scale2DataBits;
	}

	public int getScale2Parity() {
		return scale2Parity;
	}

	public void setScale2Parity(int scale2Parity) {
		this.scale2Parity = scale2Parity;
	}

	public int getScale2StopBits() {
		return scale2StopBits;
	}

	public void setScale2StopBits(int scale2StopBits) {
		this.scale2StopBits = scale2StopBits;
	}

	public int getScale1BaudRate() {
		return scale1BaudRate;
	}

	public void setScale1BaudRate(int scaleBaudRate) {
		this.scale1BaudRate = scaleBaudRate;
	}

	public int getScale1DataBits() {
		return scale1DataBits;
	}

	public void setScale1DataBits(int scaleDataBits) {
		this.scale1DataBits = scaleDataBits;
	}

	public int getScale1Parity() {
		return scale1Parity;
	}

	public void setScale1Parity(int scaleParity) {
		this.scale1Parity = scaleParity;
	}

	public int getScale1StopBits() {
		return scale1StopBits;
	}

	public void setScale1StopBits(int scaleStopBits) {
		this.scale1StopBits = scaleStopBits;
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

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public PrinterModel getPrinterModel() {
		return printerModel;
	}

	public void setPrinterModel(PrinterModel printerModel) {
		this.printerModel = printerModel;
	}

	public String getScale1Port() {
		return scale1Port;
	}

	public void setScale1Port(String scale1Port) {
		this.scale1Port = scale1Port;
	}

	public String getScale2Port() {
		return scale2Port;
	}

	public void setScale2Port(String scale2Port) {
		this.scale2Port = scale2Port;
	}

	public ScaleModel getScale1Model() {
		return scale1Model;
	}

	public void setScale1Model(ScaleModel scale1Model) {
		this.scale1Model = scale1Model;
	}

	public ScaleModel getScale2Model() {
		return scale2Model;
	}

	public void setScale2Model(ScaleModel scale2Model) {
		this.scale2Model = scale2Model;
	}

	public String getScannerPort() {
		return scannerPort;
	}

	public void setScannerPort(String scannerPort) {
		this.scannerPort = scannerPort;
	}

	public String getTempDirectory() {
		
		String tempDirectoryReturn = "";
		
		if ( tempDirectory != null )
		{
			if ( GuiGlobals.getSeparador().equals(tempDirectory.trim().substring(tempDirectory.trim().length()-1, tempDirectory.trim().length()))){
				tempDirectoryReturn = tempDirectory;
			}
			else{
				tempDirectoryReturn = tempDirectory + GuiGlobals.getSeparador();
			}
		}
		
		return tempDirectoryReturn;
	}

	public void setTempDirectory(String tempDirectory) {
		this.tempDirectory = tempDirectory;
	}

	/*
	public String getLabelsDirectory() {
		return labelsDirectory;
	}

	public void setLabelsDirectory(String labelsDirectory) {
		this.labelsDirectory = labelsDirectory;
	}
	*/

	public String getPrinterPort() {
		return printerPort;
	}

	public void setPrinterPort(String printerPort) {
		this.printerPort = printerPort;
	}
	
	public String toString() {
		if(idTerminal == null)
			return "";
		return idTerminal;
	}

	public ScaleUnit getScale1Unit() {
		return scale1Unit;
	}

	public void setScale1Unit(ScaleUnit scale1Unit) {
		this.scale1Unit = scale1Unit;
	}

	public ScaleUnit getScale2Unit() {
		return scale2Unit;
	}

	public void setScale2Unit(ScaleUnit scale2Unit) {
		this.scale2Unit = scale2Unit;
	}
	
	public String getIdTerminal() {
		return idTerminal;
	}

	public void setIdTerminal(String idTerminal) {
		this.idTerminal = idTerminal;
	}

	public int getTimeWait1() {
		return timeWait1;
	}
	
	public int getTimeWait1InMilis() {
		return timeWait1*1000;
	}

	public void setTimeWait1(int timeWait1) {
		this.timeWait1 = timeWait1;
	}

	public int getTimeWait2() {
		return timeWait2;
	}
	
	public int getTimeWait2InMilis() {
		return timeWait2*1000;
	}

	public void setTimeWait2(int timeWait2) {
		this.timeWait2 = timeWait2;
	}

	public int getPrinterBaudRate() {
		return printerBaudRate;
	}

	public void setPrinterBaudRate(int paramBaudRate) {
		this.printerBaudRate = paramBaudRate;
	}

	public int getPrinterDataBits() {
		return printerDataBits;
	}

	public void setPrinterDataBits(int paramDataBits) {
		this.printerDataBits = paramDataBits;
	}

	public int getPrinterParity() {
		return printerParity;
	}

	public void setPrinterParity(int paramParity) {
		this.printerParity = paramParity;
	}

	public int getPrinterStopBits() {
		return printerStopBits;
	}

	public void setPrinterStopBits(int paramStopBits) {
		this.printerStopBits = paramStopBits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
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
		Terminal other = (Terminal) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
}
