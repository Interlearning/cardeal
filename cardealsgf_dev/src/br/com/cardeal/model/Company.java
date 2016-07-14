package br.com.cardeal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

@Entity
public class Company {
	
	@Transient
	private static final int lengthFieldId = 2;
	
	@Id	
	@Column(length=lengthFieldId)
	private String id;

	@Column(length=80)
	private String name;

	public String getId() {
		return id;
	}
	
	public static int getLengthFieldId() {
		return lengthFieldId;
	}

	public void setId(String id) {
				
		//-------------------------------------------------------
		//- Valida se precisa completar id com zeros a esquerda -
		//-------------------------------------------------------
		if ( id.trim().length() < lengthFieldId ){
			this.id = StringUtils.leftPad( id.trim(), lengthFieldId, "0" );
		}
		else{
			this.id = id;
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Company other = (Company) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
