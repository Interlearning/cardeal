package br.com.cardeal.controller.desktop;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import br.com.cardeal.dao.ProfileDao;
import br.com.cardeal.dao.ServerSetupDao;
import br.com.cardeal.dao.UserDao;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Etiqueta;
import br.com.cardeal.model.Profile;
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
public class HomeController {

    private final Result result;
    private final Validator validator;
    private final UserInfo userInfo;
	private final UserDao dao;
	private final ProfileDao profileDao;
	private final ServerSetupDao ssDao;

	public HomeController(UserDao dao, ServerSetupDao ssDao, ProfileDao profileDao, UserInfo userInfo, Result result, Validator validator) 
	{
	    this.dao = dao;
	    this.profileDao = profileDao;
		this.result = result;
	    this.validator = validator;
        this.userInfo = userInfo;
        this.ssDao = ssDao;
	}

	@Post
	@Public
	public void login(String login, String password) {

		//cria uma configuracao se nao existir
		ServerSetup ss = ssDao.find();
		if(ss == null)
			ssDao.create();
				
		// search for the user in the database
		final User currentUser = dao.find(login, password);
		
		if(currentUser == null) {
			//verifica se tem algum usuario cadastrado, caso contrario
			//cadastra um inicial
			List<User> users = dao.listAll();
			if(users == null || users.size() == 0) {
				Profile profile = new Profile();
				profile.setName("ADMINISTRADORES");
				profileDao.add(profile);
				
				User user = new User();
				user.setLogin("admin");
				user.setName("ADMINISTRADOR");
				user.setPassword("admin");
				user.setProfile(profile);
				dao.add(user);
				
				result.include("notice", "Usuário inicial admin/admin foi criado. Faça novo login.");
				result.use(Results.logic()).redirectTo(HomeController.class).login();
				
				//-------------------------------------------------
				//- Cria o cadastro de etiqueta padrao do sistema -
				//-------------------------------------------------
				List<Etiqueta> etiquetas = GuiGlobals.getDaoFactory().getEtiquetaDao().list();
				
				if ( etiquetas == null || etiquetas.size() == 0 ){
					GuiGlobals.getDaoFactory().getEtiquetaDao().initCadLabels();
				}
				
				return;
			}
		}
		

		// if no user is found, adds an error message to the validator
		// "invalid_login_or_password" is the message key from messages.properties,
		// and that key is used with the fmt taglib in index.jsp, for example: <fmt:message key="error.key">
		validator.checking(new Validations() {{
		    that(currentUser, is(notNullValue()), "login", "invalid_login_or_password");
		}});
		// you can use "this" to redirect to another logic from this controller
		validator.onErrorUsePageOf(this).login();

		// the login was valid, add user to session
		userInfo.login(currentUser);

		// we don't want to go to default page (/WEB-INF/jsp/home/login.jsp)
		// we want to redirect to the user's home
		result.redirectTo(HomeController.class).index();
	}

	@Path(value = "/logout")
	public void logout() {
	    userInfo.logout();
	    // after logging out, we want to be redirected to home index.
	    // you can use "this" to redirect to another logic from this controller
	    result.redirectTo(this).login();
	}

	@Path(priority = 1, value = "/")
	public void index() {
		User user = null;
		try {
			userInfo.getUser();
		}
		catch(Exception e){}
		
		result.include("user", user);
	}
	
	
	@Public
	@Get
	public void login() {
	}
	
	public void error() {		
	}
	
	@Public
	public void lostPassword() {

	}
}
