package br.com.cardeal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Enterprise {
	
	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )		
	private int id;
	
	private String name;	
	
	/**
	 * ID maximo - Quando a entidade IdentifyLogisticProduct chegar com seu contador (campo idBase) neste inteiro, 
	 * seu campo de variavel logista será incrementado e seu contador retornará para 1;
	 */	
	private int maxIdSequenceLogisticBox;
	
	private int maxIdSequenceLogisticPallet;
	
	private int currentVarLogisctBox;
	
	private int currentVarLogisctPallet;
	
	private int currentIdBaseBox;
	
	private int currentIdBasePallet;

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

	public int getMaxIdSequenceLogisticBox() {
		return maxIdSequenceLogisticBox;
	}

	public void setMaxIdSequenceLogisticBox(int maxIdSequenceLogisticBox) {
		this.maxIdSequenceLogisticBox = maxIdSequenceLogisticBox;
	}

	public int getMaxIdSequenceLogisticPallet() {
		return maxIdSequenceLogisticPallet;
	}

	public void setMaxIdSequenceLogisticPallet(int maxIdSequenceLogisticPallet) {
		this.maxIdSequenceLogisticPallet = maxIdSequenceLogisticPallet;
	}
	
	public int getCurrentVarLogisctBox() {
		return currentVarLogisctBox;
	}

	public void setCurrentVarLogisctBox(int currentVarLogisctBox) {
		this.currentVarLogisctBox = currentVarLogisctBox;
	}

	public int getCurrentVarLogisctPallet() {
		return currentVarLogisctPallet;
	}

	public void setCurrentVarLogisctPallet(int currentVarLogisctPallet) {
		this.currentVarLogisctPallet = currentVarLogisctPallet;
	}

	public int getCurrentIdBaseBox() {
		return currentIdBaseBox;
	}

	public void setCurrentIdBaseBox(int currenteIdBaseBox) {
		this.currentIdBaseBox = currenteIdBaseBox;
	}

	public int getCurrentIdBasePallet() {
		return currentIdBasePallet;
	}

	public void setCurrentIdBasePallet(int currenteIdBasePallet) {
		this.currentIdBasePallet = currenteIdBasePallet;
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
		Enterprise other = (Enterprise) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}