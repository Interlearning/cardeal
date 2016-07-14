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
package br.com.cardeal.dao;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Query;
import org.hibernate.Session;
import br.com.cardeal.model.User;
import br.com.caelum.vraptor.ioc.Component;

/**
 * Default implementation for UserDao
 *
 * @author Lucas Cavalcanti
 */
@Component
public class DefaultUserDao implements UserDao {

	private final Session session;

	/**
	 * Creates a new UserDao. You can receive dependencies through constructor,
	 * because this class is annotated with @Component. This class can be used
	 * as dependency of another class, as well.
	 * @param session Hibernate's Session.
	 */
	public DefaultUserDao(Session session) {
		this.session = session;
	}

	/* (non-Javadoc)
	 * @see br.com.caelum.vraptor.mydvds.dao.UserDao#find(java.lang.String, java.lang.String)
	 */
	public User find(String login, String password) {
		String hql = "from User u where u.login = :login and u.password = :password";

		Query query = session.createQuery(hql)
			.setParameter("login", login)
			.setParameter("password", password);

		return (User) query.uniqueResult();
	}

	public User find(String login) {
		
		String hql;
		Query query;
		
		// Busca usuario
		hql = "from User u where u.login = :login";
		query = session.createQuery(hql).setParameter("login", login);
		User user = (User) query.uniqueResult();
		
		return user;
	}

	public User find(int id) {
		String hql = "from User u where u.id = :id";

		Query query = session.createQuery(hql).setParameter("id", id);

		return (User) query.uniqueResult();
	}

	public void add(User user) {
		session.save(user);
	}

	public void refresh(User user) {
		session.refresh(user);
	}

	public void update(User user) {
		session.merge(user);
		//session.update(user);
	}

	@SuppressWarnings("unchecked")
	public List<User> listAll() {
		return session.createCriteria(User.class).list();
	}

	public boolean containsUserWithLogin(String login) {
		String hql = "from User user where user.login = :login";
		Query query = session.createQuery(hql).setParameter("login", login);

		return !query.list().isEmpty();
	}
	
	public boolean containsPasswordWithLogin(User user) {
		String hql = "from User user where user.password = :password and user.login <> :login";
		Query query =	session.createQuery(hql)
						.setParameter("password", user.getPassword())
						.setParameter("login", user.getLogin());

		return !query.list().isEmpty();
	}

	/*
	 * public void delete(User user) {
	 
		user = find(user.getLogin());
		if(user != null) {
			session.delete(user);
		}
	}
	
	/**
	 * @author samuel
	 * metodo alterado para acertar 
	 * um erro 500 que explodia na tela quando o
	 * banco rejeitava a exclusão de uma fk 
	 * 
	 */
	public void delete(User user) {
	
		 try {
			user = find(user.getId());
			 if (user != null) {
		        session.delete(user);
		        session.flush();
			 }
		    } catch (Exception e) {
		        throw e;
		    } finally {
		        session.clear();
		    }
	}

	

	@Override
	public User findPsw(String psw) {
		String hql;
		Query query;
		
		// Busca usuario
		hql = "from User u where u.password = :password";
		query = session.createQuery(hql).setParameter("password", psw);
		User user = (User) query.uniqueResult();
		
		return user;
	}
	
	@Override
	public boolean exportObjUserToExcel( String fileFullName ){
		
		List<User> users = listAll();
		short linha = 0;
		FileOutputStream stream = null;
		boolean isImportOk = false;
		
		if ( users != null && fileFullName != null )
		{
					
			//Cria um Arquivo Excel
	        @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
	        try {
				
	        	stream = new FileOutputStream( fileFullName );	
	        	
	        	//Cria uma planilha Excel
		        Sheet sheet = wb.createSheet("Usuários");   
				
				//Cria uma linha na Planilha.
		        Row cabecalho = sheet.createRow( linha );
		
		        //Cria as células na linha
		        cabecalho.createCell(0).setCellValue("ID");
		        cabecalho.createCell(1).setCellValue("DESCRIÇÃO");
		        cabecalho.createCell(2).setCellValue("LOGIN");
		        cabecalho.createCell(3).setCellValue("SENHA");
		        cabecalho.createCell(4).setCellValue("PERFIL");
		        		
		        linha++;
		        Row dados;
		        for ( User user : users){
		        
			        dados = sheet.createRow( linha );
			
			        dados.createCell(0).setCellValue(user.getId());
			        dados.createCell(1).setCellValue(user.getName());
			        dados.createCell(2).setCellValue(user.getLogin());
			        dados.createCell(3).setCellValue(user.getPassword());
			        dados.createCell(4).setCellValue(user.getProfile().getName());
			       
			        linha++;
		        }
	        	
		        wb.write(stream);
		        stream.flush();
		        stream.close();
				isImportOk = true;
				
			} catch (FileNotFoundException e) {				
				e.printStackTrace();
				
			}catch (IOException e) {
				e.printStackTrace();
				
			}

		}
		
		return isImportOk;
	}
	
}
