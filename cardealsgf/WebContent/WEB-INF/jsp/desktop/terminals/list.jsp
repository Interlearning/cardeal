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
					<h2>Listagem de Terminais</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
						<li><a href="<c:url value='/terminals/insert'/>">Novo Terminal</a></li>
					</ul>
				</header>
				<c:if test="${not empty terminals}">
						<br>
						<hr>
						<div class="data-block">
							<header>
								<h2>Opções para Exportação</h2>
								<ul class="data-header-actions">
									<li><a href="<c:url value='/terminals/exportTerminalsToExcel'/>"> Excel </a></li>
								</ul>
							</header>						
						</div>					
					</c:if>
				<section>
					<div class="row hidden-xs">
					<article class="col-sm-12">
						<div class="dark data-block">
							<header>
								<h2>Terminais</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>Filial</th>
											<th>Código</th>
											<th>Descrição</th>
											<th>Ação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${terminals}" var="terminal">
										<input type="hidden" id="${terminal.id}" name="terminal.id" value="${terminal.id}"/>
										<tr class="odd gradeX">											
											<td>${terminal.company.id}</td>
											<td>${terminal.idTerminal}</td>
											<td>${terminal.description}</td>
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">											
													<button class="btn btn-md btn-info" title="Editar dados de ${terminal.description}" onclick="window.location.href='<c:url value="/terminals/viewUpd/${terminal.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-md btn-info" title="Excluir ${terminal.description}" onclick="confirm_remove('${terminal.id}', '${terminal.description}','terminals')"><span class="elusive icon-remove"></span></button>
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
				</section>
				<footer></footer>
			</div>
		</article>
	</div>
</section>
				
<%-- <div class="ui">


<display:table class="simple"
               id="terminal" 
               name="${terminals}" 
               requestURI="/terminals" 
               style="width: 100%" 
               cellpadding="4"
               pagesize="50"
               >			               
	<display:column property="id" title="Código"/>
	<display:column property="description" title="Descrição"/>
	<display:column title="Ação">
		<a href="<c:url value="/terminals/view/${terminal.id}"/>"><img src="<c:url value='/resources/img/pencil.gif'/>" title="Editar dados de ${terminal.id}"/></a> 
		<a href="#" onclick="confirm_remove('${terminal.id}', 'id','terminals')" ><img src="<c:url value='/resources/img/delete.gif'/>" title="Excluir ${terminal.id}"/></a> 
	</display:column>		
</display:table>

</div> --%>

<%@ include file="../annex/footer.jsp" %> 
