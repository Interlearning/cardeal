<%@ include file="../annex/header.jsp" %> 

<%@ include file="../annex/menu.jsp" %> 
<section class="container"><%@ include file="../annex/messages.jsp" %></section>

<section class="container" role="main">

	<!-- Grid row -->
	<div class="row">
		<!-- Data block -->
		<article class="col-sm-12">
			<div class="data-block">
				<header>
					<h2>Listagem de Perfis de Acesso</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
						<li><a href="<c:url value='/profiles/insert'/>">Novo Perfil</a></li>
					</ul>
				</header>
				<c:if test="${not empty profiles}">
					<br>
					<hr>
					<div class="data-block">
						<header>
							<h2>Opções para Exportação</h2>
							<ul class="data-header-actions">
								<li><a href="<c:url value='/profiles/exportProfilesToExcel'/>"> Excel </a></li>
							</ul>
						</header>						
					</div>					
				</c:if>
				<section>
					<div class="row hidden-xs">
						<article class="col-sm-12">
							<div class="dark data-block">	
							<header>
								<h2>Usuários</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>ID</th>
											<th>Nome</th>
											<th>Ação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${profiles}" var="profile">
										<tr class="odd gradeX">
											<td>${profile.id}</td>
											<td>${profile.name}</td>										
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">													
													<button class="btn btn-md btn-info" title="Editar dados de ${profile.id}" onclick="window.location.href='<c:url value="/profiles/view/${profile.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-md btn-info" title="Excluir ${profile.id}" onclick="confirm_remove('${profile.id}', '${profile.id}','profiles')"><span class="elusive icon-remove"></span></button>
												</div>
											</td>
										</tr>
										</c:forEach>
									</tbody>
								</table>
								</section>
								<footer> </footer>
							</div>
						</article>
					</div>
					<div class="row visible-xs">
						<article>
						<div class="dark data-block">
							<header>
								<h2>Listagem de Perfis de Acesso</h2>
							</header>
							<section>
								<div class="table-responsive">
									<table class="table table-hover">
										<thead>
											<tr>
												<th>Código</th>
												<th>Descrição</th>
												<th>Ação</th>
											</tr>
										</thead>
										<tbody>
										<c:forEach items="${profiles}" var="profile">
										<tr class="odd gradeX">
											<td>${profile.id}</td>
											<td>${profile.name}</td>										
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">													
													<button class="btn btn-xs btn-info" title="Editar dados de ${profile.id}" onclick="window.location.href='<c:url value="/profiles/view/${profile.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-xs btn-info" title="Excluir ${profile.id}" onclick="confirm_remove('${profile.id}', '${profile.id}','profiles')"><span class="elusive icon-remove"></span></button>
												</div>
											</td>
										</tr>
										</c:forEach>
									</tbody>
								</table>
								</div>
							</section>
							</div>
						</article>														
					</div>
				</section>
				<footer></footer>
			</div>
		</article>
	</div>
</section>
				
<%-- <div class="ui">


<display:table class="simple"
               id="profile" 
               name="${profiles}" 
               requestURI="/profiles" 
               style="width: 100%" 
               cellpadding="4"
               pagesize="20"
               >			               
	<display:column property="id" title="Código"/>
	<display:column property="name" title="Nome"/>
	<display:column title="Ação">
		<a href="<c:url value="/profiles/view/${profile.id}"/>"><img src="<c:url value='/resources/img/pencil.gif'/>" title="Editar dados de ${profile.id}"/></a> 
		<a href="#" onclick="confirm_remove('${profile.id}', 'id','profiles')" ><img src="<c:url value='/resources/img/delete.gif'/>" title="Excluir ${profile.id}"/></a> 
	</display:column>		
</display:table>

</div> --%>

<%@ include file="../annex/footer.jsp" %> 
