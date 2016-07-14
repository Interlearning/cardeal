package br.com.cardeal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.cardeal.enums.ProcessWorker;
import br.com.cardeal.globals.DateTimeUtils;

@Entity
public class ControlAccess {

	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private int id;
	
	@ManyToOne
	private Company company;
	
	@ManyToOne
	private Terminal terminal;
	
	private ProcessWorker process;
	
	private Date dateTimeInsert;
	
	private Date dateLastAccess;
	
	private String macAddress;
	
	@ManyToOne
	private User user;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public ProcessWorker getProcess() {
		return process;
	}

	public void setProcess(ProcessWorker process) {
		this.process = process;
	}

	public Date getDateTimeInsert() {
		return dateTimeInsert;
	}

	public void setDateTimeInsert(Date dateTimeInsert) {
		this.dateTimeInsert = dateTimeInsert;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateLastAccess() {
		return dateLastAccess;
	}

	public void setDateLastAccess(Date dateLastAccess) {
		this.dateLastAccess = dateLastAccess;
	}
	
	public String getDateTimeInsertFormat() {
		return DateTimeUtils.getDate( dateTimeInsert, "dd/MM/yyyy HH:mm" );
	}
	
	public String getDateLastAccessFormat() {
		return DateTimeUtils.getDate( dateLastAccess, "dd/MM/yyyy HH:mm" );
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
}
