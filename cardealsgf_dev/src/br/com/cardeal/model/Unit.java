package br.com.cardeal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//unidade de embalagem secundária: caixa, saco, fardo, balde, etc.

@Entity
public class Unit {
	
	@Id
	@Column(length=2)
	private String id;

	@Column(length=80)
	private String description;
	
	public Unit() {		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
