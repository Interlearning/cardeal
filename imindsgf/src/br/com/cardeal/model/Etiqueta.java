package br.com.cardeal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.cardeal.enums.ModeloEtiqueta;

@Entity
public class Etiqueta {
	
	@Id
	@SequenceGenerator(name = "seq", initialValue = 1, allocationSize = 100)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq")
	int id;
	
	String nameEtq;
	
	boolean etqEspPaoAcucar;
	
	boolean blockTara;

	ModeloEtiqueta modeloEtiqueta;
	
	public Etiqueta(){
		super();
	}
	
	public Etiqueta( String nameEtq, boolean etqEspPaoAcucar, ModeloEtiqueta modeloEtiqueta, boolean blockTara){
		setNameEtq(nameEtq);
		setEtqEspPaoAcucar(etqEspPaoAcucar);
		setModeloEtiqueta(modeloEtiqueta);
		setBlockTara(blockTara);
	}

	public boolean isEtqEspPaoAcucar() {
		return etqEspPaoAcucar;
	}

	public void setEtqEspPaoAcucar(boolean etqEspPaoAcucar) {
		this.etqEspPaoAcucar = etqEspPaoAcucar;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNameEtq() {
		return nameEtq;
	}

	public void setNameEtq(String nameEtq) {
		this.nameEtq = nameEtq.trim().toLowerCase();
	}
	
	public ModeloEtiqueta getModeloEtiqueta() {
		return modeloEtiqueta;
	}

	public void setModeloEtiqueta(ModeloEtiqueta modeloEtiqueta) {
		this.modeloEtiqueta = modeloEtiqueta;
	}
	
	public boolean isBlockTara() {
		return blockTara;
	}

	public void setBlockTara(boolean blockTara) {
		this.blockTara = blockTara;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((nameEtq == null) ? 0 : nameEtq.hashCode());
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
		Etiqueta other = (Etiqueta) obj;
		if (id != other.id)
			return false;
		if (nameEtq == null) {
			if (other.nameEtq != null)
				return false;
		} else if (!nameEtq.equals(other.nameEtq))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return nameEtq;
	}

}
