package br.com.cardeal.controller.mobile;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import br.com.cardeal.dao.UserDao;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;

@Resource
public class HomeMobile {

    private final Result result;
    private final Validator validator;
    private final UserInfo userInfo;
	private final UserDao dao;

	public HomeMobile(UserDao dao, UserInfo userInfo, Result result, Validator validator) {
	    this.dao = dao;
		this.result = result;
	    this.validator = validator;
        this.userInfo = userInfo;
	}

	@Post
	@Public
	@Path(value = "/mobile/login")
	public void login(String login, String password) {
		final User currentUser = dao.find(login, password);
		result.include("title", "login"); 
		
		validator.checking(new Validations() {{
		    that(currentUser, is(notNullValue()), "login", "invalid_login_or_password");
		}});
		validator.onErrorUsePageOf(this).login();
		userInfo.login(currentUser);
		result.redirectTo(HomeMobile.class).index();
	}

	@Path(value = "/mobile/logout")
	public void logout() {
	    userInfo.logout();
	    result.redirectTo(this).login();
	}

	@Path(value = "/mobile")
	public void index() {
		result.include("title", "home"); 
	}
	
	
	@Public
	@Get
	public void login() {
		result.include("title", "login"); 
	}

	public void error() {		
	}
}
