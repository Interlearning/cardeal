package br.com.cardeal.controller.desktop;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import br.com.cardeal.dao.ProfileDao;
import br.com.cardeal.enums.ComponentPermission;
import br.com.cardeal.enums.TypeExportReport;
import br.com.cardeal.globals.DateTimeUtils;
import br.com.cardeal.globals.GuiGlobals;
import br.com.cardeal.globals.Utils;
import br.com.cardeal.interceptor.UserInfo;
import br.com.cardeal.model.Profile;
import br.com.cardeal.model.ProfilePermission;
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
public class ProfilesController {

    private final Validator validator;
    private final Result result;
    private final UserInfo userInfo;
    private final ProfileDao dao;
    private final ServletContext servletContext;
    private final HttpServletResponse servletResponse;

	public ProfilesController
		(
		ProfileDao dao
		, Result result
		, UserInfo userInfo
		, Validator validator
		, ServletContext servletContext
		, HttpServletResponse servletResponse
		) 
	{
		this.dao = dao;
		this.result = result;
		this.userInfo = userInfo;
		this.validator = validator;
		this.servletContext = servletContext;		
		this.servletResponse = servletResponse;
	}
	

	
	@Path("/profiles")
	public void list() {
		validPermission();
		User user = userInfo.getUser();	
		
        List<Profile> profiles = new ArrayList<Profile>();
        List<Profile> profilesFromDatabase = this.dao.list();
        for (Profile profile : profilesFromDatabase) {
            Profile newProfile = new Profile();
            newProfile.setId(profile.getId());
            newProfile.setName(profile.getName());
            profiles.add(newProfile);
        }
        
        result.include("user", user);
        result.include("profiles", profiles);

    }

	@Path("/profiles/view/{profile.id}")
	@Get
	public void view(Profile profile) 
	{
		User user = userInfo.getUser();
		ComponentPermission[] allPermissions = ComponentPermission.values();		
		List<ProfilePermission> profilePermissionsServer = new ArrayList<ProfilePermission>();
		List<ProfilePermission> profilePermissionsInfo = new ArrayList<ProfilePermission>();
		Profile currentProfile = profile = dao.find(profile.getId());
		
		List<String> deniedRoles = currentProfile.getDeniedRoles();
		
		for ( int i = 0; i < allPermissions.length; i++ )
		{
			boolean checked = false;
			if ( deniedRoles != null && deniedRoles.indexOf( allPermissions[i].getRoleFormattedToDeniedRoles() ) != -1 )
			{
				checked = true;
			}
			
			if ( allPermissions[i].getGrupo() == 1 )
			{
				profilePermissionsServer.add( new ProfilePermission(String.valueOf( allPermissions[i].getId() ), allPermissions[i].getName(), checked) );
			}
			else
			{
				profilePermissionsInfo.add( new ProfilePermission(String.valueOf( allPermissions[i].getId() ), allPermissions[i].getName(), checked) );
			}			
		}
		
		result.include("user", user);
		result.include("componentesServer", profilePermissionsServer);
		result.include("componentesStation", profilePermissionsInfo);
		
		if( currentProfile != null && currentProfile.getId() > 0 ) 
		{
			result.include("profile", currentProfile);
			if(profile != null)
				result.include("roles", Utils.buildFromList(profile.getDeniedRoles()));
		}
		else 
		{
		    result.include("profile", null);
		    result.include("roles", new ArrayList<String>());
		}
	}

	public void insert() {
		User user = userInfo.getUser();
		result.include("user", user);
	    result.forwardTo(this).view(null);
	}
	
	@Path("/profiles/remove/{profile.id}")
	@Get	
	public void remove(Profile profile) 
	{
	    String deleteMessage = this.dao.delete(profile);
	    if ( deleteMessage.equals("OK") )
	    {
	    	result.include("notice", "Perfil " + profile.getId() + " removido com sucesso");
	    }
	    else
	    {
	    	result.include("notice", "Perfil " + profile.getId() + " NÃO PODE SER REMOVIDO ==>  " + deleteMessage);
	    }
	    result.use(Results.logic()).redirectTo(ProfilesController.class).list();
	}	

	@Post
	public void update(List<String> rolesX, List<String> rolesS, Profile profile) {
		
		GuiGlobals.getDaoFactory().getProfileDao().refresh(profile);
		
		if ( profile != null && profile.getId() > 0 )
		{
			List<String> rolesUpdated = new ArrayList<String>();
			
			if ( rolesX != null && rolesX.size() > 0 )
			{
				for ( String idRole : rolesX )
				{
					rolesUpdated.add( ComponentPermission.getById( idRole ) );
				}
			}
			
			if ( rolesS != null && rolesS.size() > 0 )
			{
				for ( String idRole : rolesS )
				{
					rolesUpdated.add( ComponentPermission.getById( idRole ) );
				}
			}
			profile.setDeniedRoles(rolesUpdated);	
			this.dao.update(profile);
			result.include("notice", "Perfil " + profile.getId() + " atualizado com sucesso");
		}
		else
		{
			result.include("notice", "Perfil não foi atualizado!");
		}
		result.use(Results.logic()).redirectTo(ProfilesController.class).list();
	}
	
	void validPermission(){
		validator.checking(new Validations() {{
		    that(userInfo.isPermittedComponentUpdate(ComponentPermission.CADASTRO_PERFIL), "profile", "user_not_insert_profiles");		    
		}});
		validator.onErrorUsePageOf(HomeController.class).index();
	}
	
	void validInsert( final Profile profile ){
		
		validator.checking(new Validations() {{
		    boolean IdDoesNotExist = dao.find(profile.getId()) == null;
		    that(IdDoesNotExist, "profile", "id_already_exists");
		}});

		// redirects to the index page if any validation errors occur.
		validator.onErrorUsePageOf(this).view(profile);
		
		validUpdate();
		
	}
	
	void validUpdate(){
		
	
	}
	
	public void exportProfilesToExcel(){
		
		String nameFile = getFileNameToExport(TypeExportReport.EXCEL_XLS, "cadastroDePerfisDeAcesso");
		String fileFullName = getExportName( nameFile );
		
		if ( dao.exportObjProfileToExcel(fileFullName) ) {
			showExcelOnBrowser(fileFullName);
		}
		else{
			result.include("notice", "Não foi possível realizar a exportação do cadastro!");
			result.use(Results.logic()).redirectTo(ProfilesController.class).list();
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
