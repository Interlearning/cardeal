package br.com.cardeal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.cardeal.enums.PartnerStyle;
import br.com.cardeal.globals.Utils;

@Entity
public class Partner {
	
	@Id
	@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
	@GeneratedValue(strategy = GenerationType.IDENTITY,  generator="seq" )
	private int id;

	@Column(length=100)
	private String name;

	@Column(length=20)
	private String cnpj;
	
	@Column(length=20)
	private String codigoExterno;
	
	private PartnerStyle partnerStyle;

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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public PartnerStyle getPartnerStyle() {
		return partnerStyle;
	}

	public void setPartnerStyle(PartnerStyle partnerStyle) {
		this.partnerStyle = partnerStyle;
	}	
	
	public String getFormattedCnpj() {
		return Utils.formatToCnpjOrCpf(cnpj);
	}
	
	public void setFormattedCnpj(String value) {
		this.cnpj = Utils.clearCnpj(value);
	}
	
	public String toString() {
		return name;
	}
	
	public String getCodigoExterno() {
		return codigoExterno;
	}

	public void setCodigoExterno(String codigoExterno) {
		this.codigoExterno = codigoExterno;
	}
}
