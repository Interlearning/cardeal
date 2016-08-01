/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.cardeal.interceptor;

import java.io.Serializable;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.filter.ProductFilter;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.model.Profile;
import br.com.cardeal.model.User;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

/**
 * Represents the user logged in the system.
 * @author Caio Filipini
 */
@Component
@SessionScoped
public class UserInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private ProductFilter productFilter;

    public User getUser() {
        return user;
    }

    public void login(User user) {
        this.user = user;
    }

    public void logout() {
        this.user = null;
    }

	public ProductFilter getProductFilter() {
		return productFilter;
	}

	public void setProductFilter(ProductFilter productFilter) {
		this.productFilter = productFilter;
	}

	public boolean isPermittedComponentUpdate(ComponentPermission permission) {
		
		boolean retorno = false;
		Profile profile = null;
		
		GuiGlobals.refreshDaoFactory();
		GuiGlobals.getDaoFactory().getUserDao().refresh(user);
		profile = user.getProfile();
		GuiGlobals.getDaoFactory().getProfileDao().refresh(profile);
		
		if ( "admin".equalsIgnoreCase( user.getLogin().trim() ) ){
			retorno = true;
		}
		
		if( !retorno && profile != null ) {
			
			if ( profile.getDeniedRoles().size() == 0 ){
				
				if( profile.getName().toUpperCase().trim().contains(permission.getName()) || "ADMINISTRADORES".contains( profile.getName().toUpperCase().trim() ) ){
					retorno = true;
				}
				
			}
			else{
			
				for(String url : profile.getDeniedRoles() ) {
		    		
					if( url.toUpperCase().trim().contains(permission.getName()) || "ADMINISTRADORES".contains( url.toUpperCase().trim() ) ) {					
						retorno = true;
						break;
					}
					
	    		}
				
			}
						
    	}
		
		return retorno;
		
	}

}
