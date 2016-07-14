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
					<h2>Listagem de Companhias</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
						<li><a href="<c:url value='/companies/insert'/>">Nova Companhia</a></li>
					</ul>
				</header>
				<section>
					<div class="row hidden-xs">
						<article class="col-sm-12">
							<div class="dark data-block">	
							<header>
								<h2>Companhias</h2>
							</header>
							<c:if test="${not empty companies}">
									<br>
									<hr>
									<div class="data-block">
										<header>
											<h2>Opções para Exportação</h2>
											<ul class="data-header-actions">
												<li><a href="<c:url value='/companies/exportCompaniesToExcel'/>"> Excel </a></li>
											</ul>
										</header>						
									</div>					
								</c:if>							
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>Código</th>
											<th>Nome</th>
											<th>Ação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${companies}" var="company">
										<tr class="odd gradeX">
											<td>${company.id}</td>
											<td>${company.name}</td>										
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">													
													<button class="btn btn-md btn-info" title="Editar dados de ${company.name}" onclick="window.location.href='<c:url value="/companies/view/${company.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-md btn-info" title="Excluir ${company.name}" onclick="confirm_remove('${company.id}', '${company.name}','companies')"><span class="elusive icon-remove"></span></button>
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
										<th>Código</th>
										<th>Descrição</th>
										<th>Ação</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${companies}" var="company">
										<tr class="odd gradeX">
											<td>${company.id}</td>
											<td>${company.name}</td>										
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">													
													<button class="btn btn-xs btn-info" title="Editar dados de ${company.id}" onclick="window.location.href='<c:url value="/companies/view/${company.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-xs btn-info" title="Excluir ${company.id}" onclick="confirm_remove('${company.id}', '${company.name}','companies')"><span class="elusive icon-remove"></span></button>
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
				
<%-- <div class="ui">



<display:table class="simple"
               id="company" 
               name="${companies}" 
               requestURI="/companies" 
               style="width: 100%" 
               cellpadding="4"
               pagesize="20"
               >			               
	<display:column property="id" title="Código"/>
	<display:column property="name" title="Nome"/>
	<display:column title="Ação">
		<a href="<c:url value="/companies/view/${company.id}"/>"><img src="<c:url value='/resources/img/pencil.gif'/>" title="Editar dados de ${company.id}"/></a> 
		<a href="#" onclick="confirm_remove('${company.id}', 'id','companies')" ><img src="<c:url value='/resources/img/delete.gif'/>" title="Excluir ${company.id}"/></a> 
	</display:column>		
</display:table>

</div> --%>

<%@ include file="../annex/footer.jsp" %> 
