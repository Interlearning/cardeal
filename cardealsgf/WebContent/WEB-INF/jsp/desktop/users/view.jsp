<%@ include file="../annex/header.jsp" %> 

<%@ include file="../annex/menu.jsp" %> 
<section class="container"><%@ include file="../annex/messages.jsp" %></section>

<!-- Main page container -->
<section class="container" role="main">

	<!-- Grid row -->
	<div class="row">
		<!-- Data block -->
		<article class="col-sm-12">
			<div class="data-block">
				<header>
					<h2>Cadastro de Usuário</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value="/users"/>"> Sair</a></li>
					</ul>
				</header>
				<section>
				<c:if test="${empty user}">
					<form class="form-horizontal" action="<c:url value="/users/add"/>" method="post" style="margin:0">
				</c:if>
				<c:if test="${not empty user}">
					<form class="form-horizontal" action="<c:url value="/users/update"/>" method="post" style="margin:0">
				</c:if>
				<div class="row">						
					<div class="col-md-8 col-md-offset-2">
						<div class="form-group">
							<label for="user.id" class="col-sm-3 control-label">Código</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="user.id" id="user.id" value="${user.id}" readonly/>
							</div>
						</div>
						<div class="form-group">
							<label for="user.id" class="col-sm-3 control-label">Login</label>
							<div class="col-sm-6">
								<c:if test="${empty user}">
									<input class="form-control" type="text" name="user.login" id="user.login" value="${user.login}" maxlength="4" onkeypress="return onlyInteger(event);"/>
								</c:if>
								<c:if test="${not empty user}">
									<input class="form-control" type="text" name="user.login" id="user.login" value="${user.login}" readonly/>
								</c:if>
							</div>
						</div>
						<div class="form-group">
							<label for="user.name" class="col-sm-3 control-label">Nome</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" name="user.name" id="user.name"  value="${user.name}" maxlength="30"/>
							</div>
						</div>
						<div class="form-group">
							<label for="user.password" class="col-sm-3 control-label">Senha</label>
							<!--  Solicitado por Antonio em 27-04-2015
							<div class="col-sm-6">
								<input class="form-control" type="password" name="user.password" id="user.password"  value="${user.password}"/>
							</div>
							-->
							<div class="col-sm-6">
								<input class="form-control" type="text" name="user.password" id="user.password"  value="${user.password}" maxlength="4" onkeypress="return onlyInteger(event);"/>
							</div>
							
						</div>
						<div class="form-group">
							<label for="user.profile.id" class="col-sm-3 control-label">Perfil de Acesso</label>
							<div class="col-sm-6">
								<select class="form-control" name="user.profile.id">
					                <c:forEach items="${profiles}" var="profile">
					                	<c:if test="${user.profile.id == profile.id}">
					                        <option value="${profile.id}" selected>${profile.name}</option>
					                	</c:if>
					                	<c:if test="${user.profile.id != profile.id}">
					                        <option value="${profile.id}">${profile.name}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-3 control-label"></div>
							<div class="col-sm-6">
								<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
								<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/users"/>';return false;">Cancelar</button>					
							</div>
						</div>
					</div>
				</div>		
				</form>
				</section>
				<footer></footer>
			</div>
		</article>
	</div>
</section>
				
<%-- <div class="ui">

<h2></h2>

<p>
<c:if test="${empty user}">
  <form action="<c:url value="/users/add"/>" method="post" style="margin:0">
</c:if>
<c:if test="${not empty user}">
  <form action="<c:url value="/users/update"/>" method="post" style="margin:0">
</c:if>

  <table width="79%" border="0" cellpadding="0" cellspacing="2" class="tabela_borda">    
    <tr>
	  <td width="18%"><label for="user.id"><strong>Código</strong></label></td>
	  <td><input type="text" name="user.id" id="user.id" value="${user.id}" readonly/></td>
    </tr>

    <tr>
	  <td width="18%"><label for="user.id">Código/Login</label></td>
	  <td>
		<c:if test="${empty user}">
			<input type="text" name="user.login" id="user.login" value="${user.login}"/>
		</c:if>
		<c:if test="${not empty user}">
			<input type="text" name="user.login" id="user.login" value="${user.login}" readonly/>
		</c:if>
	  </td>
    </tr>
    
	<tr>
		<td><label for="user.name">Nome</label></td>
		<td><input type="text" name="user.name" id="user.name"  value="${user.name}" /></td>
	</tr>

	<tr>
		<td><label for="user.password">Senha</label></td>
		<td><input type="password" name="user.password" id="user.password"  value="${user.password}"/></td>
	</tr>

	<tr>
		<td><label for="user.profile.id">Perfil de Acesso</label></td>
		<td>
		 	<select name="user.profile.id" style="width:202px">
                <c:forEach items="${profiles}" var="profile">
                	<c:if test="${user.profile.id == profile.id}">
                        <option value="${profile.id}" selected>${profile.name}</option>
                	</c:if>
                	<c:if test="${user.profile.id != profile.id}">
                        <option value="${profile.id}">${profile.name}</option>
                	</c:if>
                </c:forEach>
		 	</select>
	 	</td>	
	</tr>


	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	
	<tr>
		<td></td>
		<td>
			<button type="submit" id="submit">Salvar</button>
			<button type="button" onClick="window.location.href='<c:url value="/users"/>';return false;">Cancelar</button>					
		</td>
	</tr>
  </table>
</form>
</p>

</div> --%>

<%@ include file="../annex/footer.jsp" %> 
