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
					<h2>Listagem de Clientes/Fornecedores</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
						<li><a href="<c:url value='/partners/insert'/>">Novo Cliente/Fornecedor</a></li>
					</ul>
				</header>
				<section>
					<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/partners/list'/>" >
					<div class="row">						
						<div class="col-md-8 col-md-offset-2">
							<div class="form-group">
								<label for="filter.text" class="col-sm-2 control-label">Filtro</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="filter.text" id="filter.text" value="${filter.text}" />
								</div>
							</div>
							<div class="form-group">
								<label for="filter.partnerStyle" class="col-sm-2 control-label">Tipo do Parceiro</label>
								<div class="col-sm-6">
									<select class="form-control" name="filter.partnerStyle">
						                <c:forEach items="${partnerStyles}" var="style">
						                	<c:if test="${filter.partnerStyle == style}">
						                        <option value="${style}" selected>${style.name}</option>
						                	</c:if>
						                	<c:if test="${filter.partnerStyle != style}">
						                        <option value="${style}">${style.name}</option>
						                	</c:if>
						                </c:forEach>
								 	</select>
								</div>
							</div>
							<div class="form-group">
								<label for="filter.partnerStyle" class="col-sm-2 control-label"></label>
								<div class="col-sm-6">
									<input type="submit" value="Pesquisar" class="btn btn-md btn-inverse pull-right"/>							
									<input type="submit" value="Limpar" onclick="form_search.filter.value=''; return true;" class="btn btn-md btn-default pull-left"/>							
								</div>
							</div>
						</div>
					</div>
					<c:if test="${not empty partners}">
						<br>
						<hr>
						<div class="data-block">
							<header>
								<h2>Opções para Exportação</h2>
								<ul class="data-header-actions">
									<li><a href="<c:url value='/partners/exportPartnersToExcel'/>"> Excel </a></li>
								</ul>
							</header>						
						</div>					
					</c:if>
					</form>	
					<br>
					<hr>
					<br>
					<div class="row hidden-xs">
						<article class="col-sm-12">
							<div class="dark data-block">	
							<header>
								<h2>Parceiros</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>Código</th>
											<th>CNPJ/CPF</th>
											<th>Nome</th>
											<th>Código Externo</th>
											<th>Tipo</th>
											<th>Ação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${partners}" var="partner">
										<tr class="odd gradeX">
											<td>${partner.id}</td>
											<td>${partner.formattedCnpj}</td>
											<td>${partner.name}</td>
											<td>${partner.codigoExterno}</td>
											<td>${partner.partnerStyle.name}</td>											
											<td class="toolbar toolbar-btn-link" style="text-align:center;">
												<div class="btn-group">													
													<button class="btn btn-md btn-info" title="Editar dados de ${partner.name}" onclick="window.location.href='<c:url value="/partners/view/${partner.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
													<button class="btn btn-md btn-info" title="Excluir ${partner.name}" onclick="confirm_remove('${partner.id}', '${partner.name}','partners')"><span class="elusive icon-remove"></span></button>
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
									<h2>Parceiros</h2>
								</header>
								<section>
									<div class="table-responsive">
										<table class="table table-hover">
											<thead>
												<tr>
													<th>Código</th>
													<th>CNPJ/CPF</th>
													<th>Nome</th>
													<th>Código Externo</th>
													<th>Tipo</th>
													<th>Ação</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${partners}" var="partner">
													<tr class="odd gradeX">
														<td>${partner.id}</td>
														<td>${partner.formattedCnpj}</td>
														<td>${partner.name}</td>
														<td>${partner.codigoExterno}</td>
														<td>${partner.partnerStyle}</td>											
														<td class="toolbar toolbar-btn-link" style="text-align:center;">
															<div class="btn-group">													
																<button class="btn btn-xs btn-info" title="Editar dados de ${partner.name}" onclick="window.location.href='<c:url value="/partners/view/${partner.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
																<button class="btn btn-xs btn-info" title="Excluir ${partner.name}" onclick="confirm_remove('${partner.id}', '${partner.name}','partners')"><span class="elusive icon-remove"></span></button>
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

<%--				
<div class="ui">



 <form name="form_search" method="post" action="<c:url value='/partners/list'/>" >
<fieldset>
	<table>
		<tr>
			<td>Filtro</td>
			<td><input type="text" name="filter.text" id="filter.text" value="${filter.text}" /></td>
		</tr>
		<tr>
			<td>Tipo do Parceiro</td>
			<td>
			 	<select name="filter.partnerStyle" style="width:202px">
	                <c:forEach items="${partnerStyles}" var="style">
	                	<c:if test="${filter.partnerStyle == style}">
	                        <option value="${style}" selected>${style.name}</option>
	                	</c:if>
	                	<c:if test="${filter.partnerStyle != style}">
	                        <option value="${style}">${style.name}</option>
	                	</c:if>
	                </c:forEach>
			 	</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="submit" value="Pesquisar"/>
				<input type="submit" value="Limpar" onclick="form_search.filter.value=''; return true;"/>
			</td>
		</tr>
	</table>	
</fieldset>
</form> --%>


<%-- <display:table class="simple"
               id="partner" 
               name="${partners}" 
               requestURI="/partners" 
               style="width: 100%" 
               cellpadding="4"
               pagesize="20"
               >			               
	<display:column property="id" title="Código"/>
	<display:column property="formattedCnpj" title="CNPJ/CPF"/>
	<display:column property="name" title="Nome"/>
	<display:column property="partnerStyle" title="Tipo"/>
	<display:column title="Ação">
		<a href="<c:url value="/partners/view/${partner.id}"/>"><img src="<c:url value='/resources/img/pencil.gif'/>" title="Editar dados de ${partner.id}"/></a> 
		<a href="#" onclick="confirm_remove('${partner.id}', 'id','partners')" ><img src="<c:url value='/resources/img/delete.gif'/>" title="Excluir ${partner.id}"/></a> 
	</display:column>		
</display:table>

</div> --%>

<%@ include file="../annex/footer.jsp" %> 
