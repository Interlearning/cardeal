package br.com.cardeal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class IdentifyLogisticPallet {
	
	@Id
	@SequenceGenerator(name = "seq", initialValue = 1, allocationSize = 100)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq")
	private int id;
	
	private int variavelLogistica;
	
	private int idBase;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
		
	}

	public int getVariavelLogistica() {
		return variavelLogistica;
	}

	public void setVariavelLogistica(int variavelLogistica) {
		this.variavelLogistica = variavelLogistica;
	}

	public int getIdBase() {
		return idBase;
	}

	public void setIdBase(int idBase) {
		this.idBase = idBase;
	}	
}
