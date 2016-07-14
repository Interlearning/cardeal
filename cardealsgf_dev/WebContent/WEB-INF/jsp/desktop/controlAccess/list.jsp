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
					<h2>Monitor</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>						
					</ul>
				</header>
				<section>
					<div class="row hidden-xs">
					<article class="col-sm-12">
						<div class="dark data-block">
							<header>
								<h2>Usuários conectados</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>Instancia</th>
											<th>Filial</th>
											<th>Terminal</th>
											<th>Processo</th>
											<th>Usuário</th>
											<th>Primeiro Acesso</th>
											<th>Último Acesso</th>
											<th>Excluir</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${access}" var="process">
										<tr class="odd gradeX">
											<td>${process.id}</td>
											<td>${process.company.id}</td>
											<td>${process.terminal.idTerminal}</td>
											<td>${process.process.name}</td>
											<td>${process.user.name}</td>
											<td>${process.dateTimeInsertFormat}</td>
											<td>${process.dateLastAccessFormat}</td>
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">																								
													<button class="btn btn-md btn-info" title="Excluir ${process.id}" onclick="confirm_remove('${process.id}', '${process.id}','controlAccess')"><span class="elusive icon-remove"></span></button>
												</div>
											</td>
										</tr>
										</c:forEach>
									</tbody>
								</table>
							</section>
						</div>
					</article>
				</div>
				<div class="row visible-xs">
					<article>
						<div class="dark data-block">
							<header>
								<h2>Usuários conectados</h2>
							</header>
							<section>
								<div class="table-responsive">
									<table class="table table-hover">
										<thead>
											<tr>
												<th>Instancia</th>
												<th>Filial</th>
												<th>Terminal</th>
												<th>Processo</th>
												<th>Usuário</th>
												<th>Primeiro Acesso</th>
												<th>Último Acesso</th>
												<th>Excluir</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${access}" var="process">
											<tr class="odd gradeX">
												<td>${process.id}</td>
												<td>${process.company.id}</td>
												<td>${process.terminal.idTerminal}</td>
												<td>${process.process.name}</td>
												<td>${process.user.name}</td>
												<td>${process.dateTimeInsertFormat}</td>
												<td>${process.dateLastAccessFormat}</td>
												<td class="toolbar toolbar-btn-link" style="text-align:center;">
													<div class="btn-group">																								
														<button class="btn btn-md btn-info" title="Excluir ${process.id}" onclick="confirm_remove('${process.id}', '${process.id}','controlAccess')"><span class="elusive icon-remove"></span></button>
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
