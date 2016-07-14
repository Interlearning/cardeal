package br.com.cardeal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ServerSetup {
	@Id
	private int id;
	
	@Column(length=255)
	private String labelsDirectory;
	
	@Column(length=255)
	private String storeDirectory;
	
	@Column(length=255)
	private String readDirectory;
	
	@Column(length=255)
	private String storeDirectoryFull;
	
	@Column(length=255)
	private String readDirectoryFull;
	
	@ManyToOne
	private Etiqueta defaultLabelPallet; // Arquivo de layout Pallet
	
	@ManyToOne
	private Etiqueta defaultLabelBox; // Arquivo de layout Caixa

	public Etiqueta getDefaultLabelPallet() {
		return defaultLabelPallet;
	}

	public void setDefaultLabelPallet(Etiqueta defaultLabelPallet) {
		this.defaultLabelPallet = defaultLabelPallet;
	}

	public Etiqueta getDefaultLabelBox() {
		return defaultLabelBox;
	}

	public void setDefaultLabelBox(Etiqueta defaultLabelBox) {
		this.defaultLabelBox = defaultLabelBox;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStoreDirectory() {
		return storeDirectory;
	}
			
	public void setStoreDirectory(String storeDirectory) {
		this.storeDirectory = storeDirectory;
	}
	
	public String getReadDirectory() {
		return readDirectory;
	}
	
	public void setReadDirectory(String readDirectory) {
		this.readDirectory = readDirectory;
	}
	
	public String getStoreDirectoryFull() {
		return storeDirectoryFull;
	}

	public void setStoreDirectoryFull(String storeDirectoryFull) {
		this.storeDirectoryFull = storeDirectoryFull;
	}

	public String getReadDirectoryFull() {
		return readDirectoryFull;
	}

	public void setReadDirectoryFull(String readDirectoryFull) {
		this.readDirectoryFull = readDirectoryFull;
	}

	public String getLabelsDirectory() {
		return labelsDirectory;
	}

	public void setLabelsDirectory(String labelsDirectory) {
		this.labelsDirectory = labelsDirectory;
	}
	
}
