package br.com.cardeal.interceptor;

import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.view.DefaultPathResolver;

@Component
public class CustomPathResolver extends DefaultPathResolver {

    public CustomPathResolver(FormatResolver resolver) {
		super(resolver);
	}

	@Override
    protected String getPrefix() {
        return super.getPrefix(); // "/pasta/raiz/";
    }

    @Override
    protected String getExtension() {
        return super.getExtension(); //"ftl"; // ou qualquer outra extensão
    }

    @Override
    protected String extractControllerFromName(String baseName) {
    	baseName = lowerFirstCharacter(baseName);
    	
        if (baseName.endsWith("Controller")) {
            String result = "desktop/" + baseName.substring(0, baseName.lastIndexOf("Controller"));
            return result;
        }
        else if (baseName.endsWith("Mobile")) {
            String result = "mobile/" + baseName.substring(0, baseName.lastIndexOf("Mobile"));
            return result;
        }
        
        return baseName;   
    }
    
    private String lowerFirstCharacter(String baseName) {
        return baseName.toLowerCase().substring(0, 1) + baseName.substring(1, baseName.length());
    }

}
