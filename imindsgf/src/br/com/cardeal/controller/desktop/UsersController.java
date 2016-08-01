package br.com.cardeal.controller.desktop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.com.cardeal.dao.ProfileDao;
import br.com.cardeal.dao.UserDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.interceptor.Public;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Profile;
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
public class UsersController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private final UserDao dao;
    private final ProfileDao profileDao;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;

	public UsersController
		(
		UserDao dao
		, ProfileDao profileDao
		, UserInfo userInfo
		, Result result
		, Validator validator
		, ServletContext servletContext
		, HttpServletResponse servletResponse
		) 
	{
		this.dao = dao;
		this.profileDao = profileDao;
		this.result = result;
		this.validator = validator;
		this.userInfo = userInfo;
		this.servletContext = servletContext;		
		this.servletResponse = servletResponse;
	}
	
	public void fillLists() {
		result.include("profiles", profileDao.list());
	}

	@Path("/users")
	@Get
	public void list() {
		validPermission();
		User currentUser = userInfo.getUser();	
		
        List<User> users = new ArrayList<User>();
        // search by hand example
        users.addAll( this.dao.listAll() );
        
		result.include("user", currentUser);
        result.include("users", users);
    }

	@Path("/users/add")
	@Post
	@Public
	public void add(final User user) {
		User currentUser = userInfo.getUser();
		
		validInsert( user );
		
		Profile profile = profileDao.find(user.getProfile().getId());
		user.setProfile(profile);
		this.dao.add(user);
		
		result.include("currentUser", currentUser);
		result.include("notice", "Usário " + user.getName() + " adiconado com sucesso");
		result.redirectTo(this).list();
	}

	@Path("/users/view/{user.login}")
	@Get
	public void view(User user) {
		User currentUser = userInfo.getUser();
		
		//result.include("user", currentUser);
		fillLists();
		if(user != null)
			result.include("user", dao.find(user.getLogin()));
		else
		    result.include("user", null);
		
		result.include("currentUser", currentUser);
	}

	//@Path("/users/insert")
	//@Get
	public void insert() {
		User currentUser = userInfo.getUser();
		result.include("currentUser", currentUser);
	    result.forwardTo(this).view(null);
	}
	
	@Path("/users/remove/{user.login}")
	@Get	
	public void remove(User user) {
		user = dao.find(user.getLogin());
	    this.dao.delete(user);
		result.include("notice", "Usuário " + user.getName() + " removido com sucesso");
		result.use(Results.logic()).redirectTo(UsersController.class).list();
	}	

	public void update(final User user) {
		
		validUpdate( user );
		
		Profile profile = profileDao.find(user.getProfile().getId());
		user.setProfile(profile);
		this.dao.update(user);
		result.include("notice", "Usuário " + user.getName() + " atualizado com sucesso");
		result.use(Results.logic()).redirectTo(UsersController.class).list();
	}
	
	void validPermission(){
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.CADASTRO_USUARIOS), "user", "user_not_insert_users");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validInsert( final User user ){
		
		validator.checking(new Validations() {{
		    // checks if there is already an user with the specified login
		    boolean loginDoesNotExist = !dao.containsUserWithLogin(user.getLogin());
		    that(loginDoesNotExist, "login", "login_already_exists");

		    that(user.getLogin().matches("[a-z0-9_]+"), "login", "invalid_login");
		}});

		// redirects to the index page if any validation errors occur.
		validator.onErrorUsePageOf(HomeController.class).login();
		
		validUpdate( user );
		
	}
	
	void validUpdate( final User user ){
		
		validator.checking(new Validations() {{
			boolean passwordExist = dao.containsPasswordWithLogin(user);
	    	
			that(user.getName() != null && !user.getName().isEmpty(),"user","name_null");
	    	that(user.getPassword() != null && !user.getPassword().isEmpty(),"user","pass_null");
	    	that(!passwordExist,"user","pass_already_exists");
	    	
		}});	
		validator.onErrorUse(Results.logic()).redirectTo(UsersController.class).view(user);	
	
	}
	
	public void exportUsersToExcel(){
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "cadastroDeUsuarios");
		String fileFullName = getExportName( nameFile );
		
		if ( dao.exportObjUserToExcel(fileFullName) ) {
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do cadastro!");
			result.use(Results.logic()).redirectTo(UsersController.class).list();
		}
		
	}
	
	private String getExportName( String nameFile ){
		
		String path = servletContext.getRealPath("/") + GuiGlobals.getSeparadorIvertido() + "stocks_export"; 
		
		if (!new File(path).exists()) { // Verifica se o diretório existe.   
	    	(new File(path)).mkdir();   // Cria o diretório   
	    } 
		
		return path + GuiGlobals.getSeparadorIvertido() + nameFile;
		
	}
	
	public void showExcelOnBrowser( String fileFullName )
	{
		byte[] arquivo = null;
		File file = new File( fileFullName );
		
		try {
			arquivo = GuiGlobals.fileToByte( file );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		servletResponse.setHeader("Content-disposition", "attachment;filename="+fileFullName);
		servletResponse.setContentType("application/vnd.ms-excel");
		servletResponse.setContentLength(arquivo.length);
		
		ServletOutputStream ouputStream;
		try {
			ouputStream = servletResponse.getOutputStream();
			ouputStream.write(arquivo, 0, arquivo.length);
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getFileNameToExport( TypeExportReport opcao, String nameArq){
		String fileName = DateTimeUtils.getDateForDb() + "_" + DateTimeUtils.getTimeForDb() ;
		return fileName + "_" + nameArq + "." + opcao.getExtensao();
	}
	
}
