package br.com.cardeal.enums;

public enum UnitStyle {
	WEIGHT(0, "KG"),
	PRIMARY(1, "PÇ"),
	SECONDARY(2, "CX");

	private final int id;
	private final String name;
	UnitStyle(int id, String name) { this.id = id; this.name = name;}
	public int getValue() { return id; }
	public String toString() { return name; }
	public String getName() { return name; }
	
	public static UnitStyle getUnitStyleForName( String name ){
		
		UnitStyle[] styles = UnitStyle.values();
		String nameStyle = name.trim().toUpperCase();
		
		for ( UnitStyle style: styles ){
			
			if ( style.getName().trim().toUpperCase().equals( nameStyle ) ) return style;
			
		}
		
		return null;
		
	}
}
