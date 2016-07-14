package br.com.cardeal.model;

public class TypeOperationPalletVirtual 
{
	public static final String TYPE_STANDARD = "Padr�o";
	public static final String TYPE_VARIABLE = "Vari�vel";
	
    private String name;
 
    public TypeOperationPalletVirtual(String name) 
    {
        super();
        this.name = name;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
     
}