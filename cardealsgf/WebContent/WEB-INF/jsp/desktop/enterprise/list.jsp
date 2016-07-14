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
					<h2>Listagem de Empresas</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
						<c:if test="${empty enterprises}">
							<li><a href="<c:url value='/enterprise/insert'/>">Nova Empresa</a></li>
						</c:if>
					</ul>
				</header>
				<section>
					<div class="row hidden-xs">
						<article class="col-sm-12">
							<div class="dark data-block">	
							<header>
								<h2>Empresa</h2>
							</header>
							<c:if test="${not empty enterprises}">
									<br>
									<hr>
									<div class="data-block">
										<header>
											<h2>Opções para Exportação</h2>
											<ul class="data-header-actions">
												<li><a href="<c:url value='/enterprise/exportEnterprisesToExcel'/>"> Excel </a></li>
											</ul>
										</header>						
									</div>					
								</c:if>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>ID</th>
											<th>Descrição</th>											
											<th>Ação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${enterprises}" var="enterprise">
											<tr class="odd gradeX">
												<td>${enterprise.id}</td>
												<td>${enterprise.name}</td>												
												<td class="toolbar toolbar-btn-link" style="text-align:center;">
													<div class="btn-group">													
														<button class="btn btn-md btn-info" title="Editar dados de ${enterprise.name}" onclick="window.location.href='<c:url value="/enterprise/view/${enterprise.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
														<button class="btn btn-md btn-info" title="Excluir ${enterprise.name}" onclick="confirm_remove('${enterprise.id}', '${enterprise.name}','enterprise')"><span class="elusive icon-remove"></span></button>
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
						<div class="table-responsive">
							<table class="table table-hover">
								<thead>
									<tr>
										<th>ID</th>
										<th>Descrição</th>
										<th>Ação</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${enterprises}" var="enterprise">
										<tr class="odd gradeX">
											<td>${enterprise.id}</td>
											<td>${enterprise.name}</td>											
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">
													<button class="btn btn-xs btn-info" title="Editar dados de ${enterprise.name}" onclick="window.location.href='<c:url value="/enterprise/view/${enterprise.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-xs btn-info" title="Excluir ${enterprise.name}" onclick="confirm_remove('${enterprise.id}', '${enterprise.name}','enterprise')"><span class="elusive icon-remove"></span></button>																										
												</div>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>		
					</div>
				</section>
				<footer>
				</footer>
			</div>
		</article>
	</div>
	
</section>

<%@ include file="../annex/footer.jsp" %> 
