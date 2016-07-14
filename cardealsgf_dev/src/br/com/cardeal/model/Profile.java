package br.com.cardeal.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Profile implements Serializable {
	
	private static final long serialVersionUID = 3345265389147885917L;

	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private int id;

	@Column(length=40)
	private String name;
	
	@ElementCollection
	private List<String> deniedRoles;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDeniedRoles() {
		return deniedRoles;
	}

	public void setDeniedRoles(List<String> deniedRoles) {
		this.deniedRoles = deniedRoles;
	}


}
