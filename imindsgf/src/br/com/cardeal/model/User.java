
package br.com.cardeal.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;


@Entity
@Component
@SessionScoped
@Table(name="Usert")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private int id;

	// Hibernate validator's annnotations/rules
	@NotNull
	@Column(length=18)
	private String login;
	
	// Hibernate validator's annnotations/rules
	@Column(name="passw", length=20)
	private String password;

	// Hibernate validator's annnotations/rules
	@NotNull
	@Column(length=100)
	private String name;
	
	@ManyToOne
	private Profile profile;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
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
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
}
