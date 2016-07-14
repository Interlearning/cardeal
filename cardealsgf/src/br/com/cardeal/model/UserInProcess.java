package br.com.cardeal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class UserInProcess 
{
	@Id
	@SequenceGenerator(name="seq", initialValue=1)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private long id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Company company;
	
	@ManyToOne
	private Terminal terminal;
	
	@ManyToOne
	private Enterprise enterprise;
	
	private Date dateTimeBegin;
	
	public UserInProcess(){
		super();
	}
	public UserInProcess(User user, Enterprise enterprise, Company company, Terminal terminal)
	{
		this.id = 0;
		this.user = user;
		this.company = company;
		this.terminal = terminal;
		this.enterprise = enterprise;
		this.dateTimeBegin = new Date();
	}
	
	public long getId()
	{
		return id;
	}
	
	public Date getDateTimeBegin() {
		return dateTimeBegin;
	}
	public void setDateTimeBegin(Date dateTimeBegin) {
		this.dateTimeBegin = dateTimeBegin;
	}
	public Enterprise getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}
	public Terminal getTerminal() {
		return terminal;
	}
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((enterprise == null) ? 0 : enterprise.hashCode());
		result = prime * result + ((terminal == null) ? 0 : terminal.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		UserInProcess other = (UserInProcess) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (enterprise == null) {
			if (other.enterprise != null)
				return false;
		} else if (!enterprise.equals(other.enterprise))
			return false;
		if (terminal == null) {
			if (other.terminal != null)
				return false;
		} else if (!terminal.equals(other.terminal))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
		
}
