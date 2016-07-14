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
					<h2>Listagem de Modelos de etiquetas</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>						
						<li><a href="<c:url value='/etiquetas/insert'/>">Nova Etiqueta</a></li>						
					</ul>
				</header>
				<section>
					<div class="row hidden-xs">
						<article class="col-sm-12">
							<div class="dark data-block">	
							<header>
								<h2>Etiqueta</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>ID</th>
											<th>Node do Arquivo</th>
											<th>Específica Pão de Açucar ?</th>											
											<th>Ação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${etiquetas}" var="etiqueta">
											<tr class="odd gradeX">
												<td>${etiqueta.id}</td>
												<td>${etiqueta.nameEtq}</td>
												<c:if test="${etiqueta.etqEspPaoAcucar}">
													<td>Sim</td>
												</c:if>
												<c:if test="${not etiqueta.etqEspPaoAcucar}">
													<td>Não</td>
												</c:if>
												<td class="toolbar toolbar-btn-link" style="text-align:center;">
													<div class="btn-group">													
														<button class="btn btn-md btn-info" title="Editar dados de ${etiqueta.nameEtq}" onclick="window.location.href='<c:url value="/etiquetas/view/${etiqueta.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
														<button class="btn btn-md btn-info" title="Excluir ${etiqueta.nameEtq}" onclick="confirm_remove('${etiqueta.id}', '${etiqueta.nameEtq}','etiquetas')"><span class="elusive icon-remove"></span></button>
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
										<th>Node do Arquivo</th>
										<th>Específica Pão de Açucar ?</th>
										<th>Ação</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${etiquetas}" var="etiqueta">
										<tr class="odd gradeX">
											<td>${etiqueta.id}</td>
											<td>${etiqueta.nameEtq}</td>
											<c:if test="${etiqueta.etqEspPaoAcucar}">
												<td>Sim</td>
											</c:if>
											<c:if test="${not etiqueta.etqEspPaoAcucar}">
												<td>Não</td>
											</c:if>
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">
													<button class="btn btn-xs btn-info" title="Editar dados de ${etiqueta.nameEtq}" onclick="window.location.href='<c:url value="/etiquetas/view/${etiqueta.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-xs btn-info" title="Excluir ${etiqueta.nameEtq}" onclick="confirm_remove('${etiqueta.id}', '${etiqueta.nameEtq}','etiquetas')"><span class="elusive icon-remove"></span></button>																										
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
