package br.com.cardeal.model;

public class TypeOperationPalletVirtual 
{
	public static final String TYPE_STANDARD = "Padrão";
	public static final String TYPE_VARIABLE = "Variável";
	
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