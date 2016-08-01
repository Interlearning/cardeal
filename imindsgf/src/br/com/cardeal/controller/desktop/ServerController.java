package br.com.cardeal.controller.desktop;

import br.com.cardeal.dao.ServerSetupDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.ServerSetup;
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
public class ServerController {

	private final Validator validator;
    private final Result result;
    private final ServerSetupDao dao;
    private final UserInfo userInfo;    

	public ServerController(ServerSetupDao dao, Result result, Validator validator, UserInfo userInfo) {
		this.dao = dao;
		this.result = result;
		this.userInfo = userInfo;
		this.validator = validator;
	}

	@Path("/server/view")
	@Get
	public void view() {
		validPermission1();
		User user = userInfo.getUser();
		
		ServerSetup server = dao.find();
				
		result.include("user", user);
		result.include("server", server);
	}
	
	@Path("/server/viewEtiquetas")
	@Get
	public void viewEtiquetas() {
		validPermission2();
		User user = userInfo.getUser();
		
		ServerSetup server = dao.find();		
		
		result.include("user", user);
		result.include("server", server);
		result.include("labelsBox", GuiGlobals.listLabelsBox());
		result.include("labelsPallet", GuiGlobals.listLabelsPallet());
	}

	void validPermission1()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.MANUTENCAO_CONFIGURACOES), "manutenção", "user_not_access_manuten_config");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validPermission2()
	{
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.MANUTENCAO_ETIQUETAS), "manutenção", "user_not_access_manuten_etiquetas");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}

	@Path("/server/update")
	@Post
	public void update(final ServerSetup server) {
		server.setId(1);
		this.dao.update(server);
		result.include("notice", "Configuração do Sistema atualizada com sucesso");
		result.use(Results.logic()).redirectTo(HomeController.class).index();
		
	}
		

}
