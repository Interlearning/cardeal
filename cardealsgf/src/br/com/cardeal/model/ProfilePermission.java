package br.com.cardeal.model;

public class ProfilePermission 
{
	private String id;
	private String name;
	private boolean checked;
	
	public ProfilePermission(String id, String name, boolean checked)
	{
		this.id = id;
		this.name = name;
		this.setChecked(checked);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
