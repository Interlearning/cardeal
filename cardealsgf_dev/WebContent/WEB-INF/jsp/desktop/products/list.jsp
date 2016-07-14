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
					<h2>Listagem de Produtos</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
						<li><a href="<c:url value='/products/insert'/>">Novo Produto</a></li>
					</ul>
				</header>
				<section>
					<form class="form-horizontal" role="form" name="form_search" method="post" action="<c:url value='/products/list'/>" >
					<div class="row">						
						<div class="col-md-8 col-md-offset-2">
							<div class="form-group">
								<label for="filter.idMasc" class="col-sm-2 control-label">Codigo</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="filter.idMasc" id="filter.idMasc" value="${filter.idMasc}" />
								</div>
							</div>
							<div class="form-group">
								<label for="filter.description" class="col-sm-2 control-label">Descrição</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" name="filter.description" id="filter.description" value="${filter.description}" />
								</div>
							</div>
							<div class="form-group">
								<label for="filter.materialStyle" class="col-sm-2 control-label">Tipo de Produto</label>
								<div class="col-sm-8">
									<select name="filter.typeStock" class="form-control" id="filter.typeStock">
							        	<c:forEach items="${listTypeStock}" var="type">
							            	<c:if test="${filter.typeStock == type}">
							                	<option value="${type}" selected>${type.descricao}</option>
							                </c:if>
							                <c:if test="${filter.typeStock != type}">
							                	<option value="${type}">${type.descricao}</option>
							               	</c:if>
							            </c:forEach>
									</select>
								</div>
							</div>
							<!-- 
							<div class="form-group">
								<label for="filter.stockStyle" class="col-sm-2 control-label">Tipo de Estoque</label>
								<div class="col-sm-8">
									<select name="filter.stockStyle" class="form-control" id="filter.stockStyle">
						                <c:forEach items="${stockStyles}" var="style">
						                	<c:if test="${filter.stockStyle == style}">
						                        <option value="${style}" selected>${style.name}</option>
						                	</c:if>
						                	<c:if test="${filter.stockStyle != style}">
						                        <option value="${style}">${style.name}</option>
						                	</c:if>
						                </c:forEach>
								 	</select>
								 </div>	
							</div>
							-->
							<div class="form-group">
								<label for="filter.weighingStyle" class="col-sm-2 control-label">Tipo de Pesagem</label>
								<div class="col-sm-8">
									<select name="filter.weighingStyle" class="form-control" id="filter.weighingStyle">
						                <c:forEach items="${weighingStyles}" var="style">
						                	<c:if test="${filter.weighingStyle == style}">
						                        <option value="${style}" selected>${style.name}</option>
						                	</c:if>
						                	<c:if test="${filter.weighingStyle != style}">
						                        <option value="${style}">${style.name}</option>
						                	</c:if>
						                </c:forEach>
								 	</select>
								</div>
							</div>				
							<div class="form-group">
								<div class="col-sm-2 control-label"></div>
								<div class="col-sm-8">
									<input type="submit" value="Pesquisar" class="btn btn-md btn-inverse pull-right"/>	
									<input type="submit" value="Limpar" onclick="form_search.filter.idMasc=''; form_search.filter.description=''return true;" class="btn btn-md btn-default pull-left"/>
								</div>
							</div>
										
						</div>
					</div>

					<c:if test="${not empty products}">
						<br>
						<hr>
						<div class="data-block">
							<header>
								<h2>Opções para Exportação</h2>
								<ul class="data-header-actions">
									<li><a href="<c:url value='/products/exportProductsToExcel'/>"> Excel </a></li>
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
								<h2>Produtos</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>Código</th>
											<th>Descrição</th>
											<th>EAN13</th>
											<th>DUN14</th>
											<th>Ação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${products}" var="product">
											<c:if test="${product.blocked}">
												<tr class="odd gradeX">
													<td style="background-color: silver;">${product.idMasc}</td>
													<td style="background-color: silver;">${product.description}</td>
													<td style="background-color: silver;">${product.ean13}</td>
													<td style="background-color: silver;">${product.dun14}</td>
													<td class="toolbar toolbar-btn-link" style="text-align:center; background-color: silver;">
														<div class="btn-group">
															<button class="btn btn-md btn-info" title="Editar dados de ${product.idMasc}" onclick="window.location.href='<c:url value="/products/view/${product.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
															<button class="btn btn-md btn-info" title="Excluir ${product.idMasc}" onclick="confirm_remove('${product.id}', '${product.idMasc}','products')"><span class="elusive icon-remove"></span></button>
														</div>
													</td>													
												</tr>
											</c:if>
											<c:if test="${not product.blocked}">
												<tr class="odd gradeX">
													<td>${product.idMasc}</td>
													<td width="40%" >${product.description}</td>
													<td>${product.ean13}</td>
													<td>${product.dun14}</td>
													<td class="toolbar toolbar-btn-link" style="text-align:center;">
														<div class="btn-group">
															<button class="btn btn-md btn-info" title="Editar dados de ${product.idMasc}" onclick="window.location.href='<c:url value="/products/view/${product.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
															<button class="btn btn-md btn-info" title="Excluir ${product.idMasc}" onclick="confirm_remove('${product.id}', '${product.idMasc}','products')"><span class="elusive icon-remove"></span></button>
														</div>
													</td>													
												</tr>
											</c:if>
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
								<h2>Produtos</h2>
							</header>
							<section>
								<div class="table-responsive">
									<table class="table table-hover">
										<thead>
											<tr>
												<th>Código</th>
												<th>Descrição</th>
												<th>EAN13</th>
												<th>DUN14</th>
												<th>Ação</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${products}" var="product">
												<tr class="odd gradeX">
													<td>${product.idMasc}</td>
													<td width="40%">${product.description}</td>
													<td>${product.ean13}</td>
													<td>${product.dun14}</td>
													<td class="toolbar toolbar-btn-link" style="text-align:center;">
														<div class="btn-group">
															<button class="btn btn-xs btn-info" title="Editar dados de ${product.idMasc}" onclick="window.location.href='<c:url value="/products/view/${product.idMasc}"/>';return false;"><span class="elusive icon-wrench"></span></button>
															<button class="btn btn-xs btn-info" title="Excluir ${product.idMasc}" onclick="confirm_remove('${product.idMasc}', '${product.idMasc}','products')"><span class="elusive icon-remove"></span></button>
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
				<footer> </footer>
			</div>
		</article>
		<!-- /Data block -->
	</div>	
</section>


<%@ include file="../annex/footer.jsp" %> 
