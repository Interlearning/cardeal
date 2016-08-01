package br.com.cardeal.controller.desktop;

import java.util.List;
import br.com.cardeal.dao.EtiquetaDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.ModeloEtiqueta;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Etiqueta;
import br.com.cardeal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.com.caelum.vraptor.view.Results;

@Resource
public class EtiquetasController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private final EtiquetaDao dao;

	public EtiquetasController(EtiquetaDao dao, Result result, UserInfo userInfo, Validator validator) {
		this.dao = dao;
		this.result = result;
		this.userInfo = userInfo;
		this.validator = validator;
	}
		
	@Path("/etiquetas")
	public void list() {
		validPermission();
		User user = userInfo.getUser();
		
        List<Etiqueta> etiquetas = this.dao.list();
        
        result.include("etiquetas", etiquetas);
        result.include("user", user);
    }

	@Path("/etiquetas/add")
	@Post
	@Public
	public void add(final Etiqueta etiqueta) {
		User user = userInfo.getUser();
		
		validInsert( etiqueta );
	    
		this.dao.add(etiqueta);
		
		result.include("user", user);
		result.include("notice", "Etiqueta " + etiqueta.getId() + " adicionada com sucesso");
		result.redirectTo(this).list();
	}

	@Path("/etiquetas/view/{etiqueta.id}")
	@Get
	public void view(Etiqueta etiqueta) {
		User user = userInfo.getUser();
		
		if(etiqueta != null)
			result.include("etiqueta", dao.find(etiqueta.getId()));
		else
		    result.include("etiqueta", null);
		
		result.include("modelosEtiquetas", ModeloEtiqueta.values());
		result.include("user", user);
	}

	public void insert() {
	    result.forwardTo(this).view(null);
	}
	
	@Path("/etiquetas/remove/{etiqueta.id}")
	@Get	
	public void remove(Etiqueta etiqueta) 
	{
		this.dao.refresh(etiqueta);
	    String deleteMessage = this.dao.delete(etiqueta);
	    if ( deleteMessage.equals("OK") )
	    {
			result.include("notice", "Etiqueta " + etiqueta.getId() + " removida com sucesso");
	    }
	    else
	    {
	    	result.include("notice", "ETIQUETA " + etiqueta.getId() + " NÃO PODE SER REMOVIDA ==>  " + deleteMessage);
	    }
		result.use(Results.logic()).redirectTo(EtiquetasController.class).list();
	}	

	public void update(final Etiqueta etiqueta) {
		
		validUpdate( etiqueta );	
		
		this.dao.update(etiqueta);
		result.include("notice", "Etiqueta " + etiqueta.getId() + " atualizada com sucesso");
		result.use(Results.logic()).redirectTo(EtiquetasController.class).list();
	}

	void validPermission()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.CADASTRO_FILIAIS), "etiqueta", "user_not_insert_etiquetas");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validInsert(final Etiqueta etiqueta ){
		
		validator.checking(new Validations() {{
						
		    // checks if there is already an unit with the specified login
		    boolean idExists = dao.find(etiqueta.getId()) == null;
		    that(idExists, "etiqueta", "id_already_exists");
		}});

		// redirects to the index page if any validation errors occur.
	    if(validator.hasErrors()) {
	    	etiqueta.setId(0);
	    	result.include("etiqueta", etiqueta);
	    }
	    else{
	    	validUpdate( etiqueta );
	    }
		validator.onErrorForwardTo(this).view(etiqueta);
		
	}
	
	void validUpdate( final Etiqueta etiqueta ){
		
		validator.checking(new Validations() {{
			
			Etiqueta etqFromDB = GuiGlobals.getDaoFactory().getEtiquetaDao().findByNameArq( etiqueta.getNameEtq().trim().toLowerCase() );
			
			if ( etqFromDB != null ){
				
				boolean nameEtqIsNotExistis = ( etqFromDB.getId() == etiqueta.getId() );
		    	that(nameEtqIsNotExistis,"etiqueta","valid_unique_name_etiqueta");
			}
		    	
		}});	
		validator.onErrorUse(Results.logic()).redirectTo(EtiquetasController.class).view(etiqueta);
	
	}

}
