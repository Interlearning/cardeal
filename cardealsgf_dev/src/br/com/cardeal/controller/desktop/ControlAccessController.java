package br.com.cardeal.controller.desktop;

import br.com.cardeal.dao.ControlAccessDao;
import br.com.cardeal.model.ControlAccess;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class ControlAccessController {
    
    private final ControlAccessDao dao;
    private final Result result;
    
	public ControlAccessController(ControlAccessDao dao, Result result) {
		this.dao = dao;
		this.result = result;
	}
	
	@Path("/controlAccess")
	@Get
	public void list() {
		result.include("access", dao.list(null) );
	}
	
	@Path("/controlAccess/remove/{process.id}")
	@Get	
	public void remove(ControlAccess process) {
	    this.dao.refresh(process);
	    this.dao.delete(process);
		result.include("notice", "Usuario " + process.getUser().getName() + " desconectado com sucesso");
		result.use(Results.logic()).redirectTo(ControlAccessController.class).list();
	}

}
