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
					<h2>Listagem de Unidades Embalagens</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
						<li><a href="<c:url value='/units/insert'/>">Nova Unidade</a></li>
					</ul>
				</header>
				<c:if test="${not empty units}">
						<br>
						<hr>
						<div class="data-block">
							<header>
								<h2>Opções para Exportação</h2>
								<ul class="data-header-actions">
									<li><a href="<c:url value='/units/exportUnitsToExcel'/>"> Excel </a></li>
								</ul>
							</header>						
						</div>					
					</c:if>
				<section>
					<div class="row hidden-xs">
						<article class="col-sm-12">
							<div class="dark data-block">	
							<header>
								<h2>Unidades</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>Código</th>
											<th>Descrição</th>
											<th>Ação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${units}" var="unit">
										<tr class="odd gradeX">
											<td>${unit.id}</td>
											<td>${unit.description}</td>
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">													
													<button class="btn btn-md btn-info" title="Editar dados de ${unit.id}" onclick="window.location.href='<c:url value="/units/view/${unit.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-md btn-info" title="Excluir ${order.id}" onclick="confirm_remove('${unit.id}', '${unit.id}','units')"><span class="elusive icon-remove"></span></button>
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
								<h2>Unidades</h2>
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
											<c:forEach items="${units}" var="unit">
											<tr class="odd gradeX">
												<td>${unit.id}</td>
												<td>${unit.description}</td>
												<td class="toolbar toolbar-btn-link" style="text-align:center;">
													<div class="btn-group">													
														<button class="btn btn-xs btn-info" title="Editar dados de ${unit.id}" onclick="window.location.href='<c:url value="/units/view/${unit.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
														<button class="btn btn-xs btn-info" title="Excluir ${order.id}" onclick="confirm_remove('${unit.id}', '${unit.id}','units')"><span class="elusive icon-remove"></span></button>
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
