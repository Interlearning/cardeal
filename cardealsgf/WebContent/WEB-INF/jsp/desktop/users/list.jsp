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
					<h2>Usu�rios do Sistema</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
						<li><a href="<c:url value='/users/insert'/>">Novo Usu�rio</a></li>
					</ul>
				</header>
				<c:if test="${not empty users}">
						<br>
						<hr>
						<div class="data-block">
							<header>
								<h2>Op��es para Exporta��o</h2>
								<ul class="data-header-actions">
									<li><a href="<c:url value='/users/exportUsersToExcel'/>"> Excel </a></li>
								</ul>
							</header>						
						</div>					
					</c:if>
				<section>
					<div class="row hidden-xs">
						<article class="col-sm-12">
							<div class="dark data-block">	
							<header>
								<h2>Usu�rios</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>C�digo</th>
											<th>Login</th>
											<th>Nome</th>
											<th>A��o</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${users}" var="user">
										<tr class="odd gradeX">
											<td>${user.id}</td>
											<td>${user.login}</td>
											<td>${user.name}</td>										
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">													
													<button class="btn btn-md btn-info" title="Editar dados de ${user.name}" onclick="window.location.href='<c:url value="/users/view/${user.login}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-md btn-info" title="Excluir ${user.name}" onclick="confirm_remove('${user.login}', '${user.name}','users')"><span class="elusive icon-remove"></span></button>
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
								<h2>Usu�rios</h2>
							</header>
							<section>
								<div class="table-responsive">
									<table class="table table-hover">
										<thead>
											<tr>
												<th>C�digo</th>
												<th>Login</th>
												<th>Nome</th>
												<th>A��o</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${users}" var="user">
											<tr class="odd gradeX">
												<td>${user.id}</td>
												<td>${user.login}</td>
												<td>${user.name}</td>										
												<td class="toolbar toolbar-btn-link" style="text-align:center;">
													<div class="btn-group">													
														<button class="btn btn-xs btn-info" title="Editar dados de ${user.name}" onclick="window.location.href='<c:url value="/users/view/${user.login}"/>';return false;"><span class="elusive icon-wrench"></span></button>
														<button class="btn btn-xs btn-info" title="Excluir ${user.name}" onclick="confirm_remove('${user.login}', '${user.name}','users')"><span class="elusive icon-remove"></span></button>
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

<%@ include file="../annex/footer.jsp" %> 
