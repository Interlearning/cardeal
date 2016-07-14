<%@ include file="../annex/header.jsp" %> 

<script type="text/javascript" src="<c:url value='/resources/js/jquey-ui-timepicker-addon.js'/>"></script>					
<script type="text/javascript" src="<c:url value='/resources/js/jquery.ui.datepicker-pt-BR.js'/>"></script>					

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
					<h2>Listagem de Pedidos</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>						
					</ul>
				</header>
				<section>
					<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/orders/list'/>" >
						<div class="row">						
							<div class="col-md-8 col-md-offset-2">
								
								<div class="form-group">
									<label for="filter.idOrderImportDe" class="col-sm-3 control-label">Pedido De ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" maxlength="6" name="filter.idOrderImportDe" id="filter.idOrderImportDe" value="${filter.idOrderImportDe}" />
									</div>
								</div>
								
								<div class="form-group">
									<label for="filter.idOrderImportAte" class="col-sm-3 control-label">Pedido Até ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" maxlength="6" name="filter.idOrderImportAte" id="filter.idOrderImportAte" value="${filter.idOrderImportAte}" />
									</div>
								</div>
																
								<div class="form-group">
									<label for="date1" class="col-sm-3 control-label">Date De ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date1" ).datepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date1" name="date1" value="${date1}" />
									</div>
								</div>
								
								<div class="form-group">
									<label for="date2" class="col-sm-3 control-label">Data Até ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date2" ).datepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date2" name="date2" value="${date2}" />
									</div>
								</div>
								
								<div class="form-group">
									<label for="filter.orderStatus" class="col-sm-3 control-label">Status</label>
									<div class="col-sm-6">
										<select name="filter.orderStatus" class="form-control" id="filter.orderStatus">
								        	<c:forEach items="${listStatus}" var="status">
								            	<c:if test="${filter.orderStatus == status}">
								                	<option value="${status}" selected>${status.name}</option>
								                </c:if>
								                <c:if test="${filter.orderStatus != status}">
								                	<option value="${status}">${status.name}</option>
								               	</c:if>
								            </c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-8 col-md-offset-2">
								<input type="submit" value="Pesquisar" class="btn btn-md btn-inverse pull-right"/>							
								<input type="submit" value="Limpar" onclick="form_search.filter.idOrderImportDe=''; form_search.filter.idOrderImportAte=''; return true;" class="btn btn-md btn-default pull-left"/>
								
							</div>
						</div>
					</form>	
					<br>
					<br>
					<c:if test="${not empty orders}">
						<br>
						<hr>
						<input type="hidden" id="orderIdDe" value="${orderIdDe}" />
						<input type="hidden" id="orderIdAte" value="${orderIdAte}" />
						<input type="hidden" id="enterDateDe" value="${date1}" />
						<input type="hidden" id="enterDateAte" value="${date2}" />
						<input type="hidden" id="orderStatus"  value="${orderStatus}" />
						<div class="data-block">
							<div id="listOrders"></div>
							<div id="targetExcel"></div>
						</div>
					
					</c:if>
										
					<div class="row hidden-xs">
						<article class="col-sm-12">
							<div class="dark data-block">
							
								<header>
									<h2>Pedidos</h2>
								</header>
								<section>
									
									<table class="datatable table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>Pedido</th>
												<th>Data</th>
												<th>Cliente</th>
												<th>Status</th>
												<th>Ação</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${orders}" var="order">
												<tr class="odd gradeX">
													<td>${order.idPedidoImport}</td>
													<td>${order.creationDateFormat}</td>
													<td>${order.partner.name}</td>
													<td>${order.status.name}</td>
													<td class="toolbar toolbar-btn-link" style="text-align:center;">
														<div class="btn-group">													
															<button class="btn btn-md btn-info" title="Visualizar itens de ${order.idPedidoImport}" onclick="window.location.href='<c:url value="/orders/viewItens/${order.id}"/>';return false;"><span class="elusive icon-search"></span></button>
															<button class="btn btn-md btn-info" title="Excluir ${order.idPedidoImport}" 		onclick="confirm_remove('${order.id}', 'pedido ${order.idPedidoImport}','orders')"><span class="elusive icon-remove"></span></button>
															<button class="btn btn-md btn-info" title="Cancelar pedido ${order.idPedidoImport}" onclick="confirm_cancel('${order.id}', 'pedido ${order.idPedidoImport}','orders')"><span class="elusive icon-remove-sign"></span></button>											
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
			<footer> </footer>
			</div>
		</article>
		<!-- /Data block -->
	</div>	
</section>	

<%@ include file="../annex/footer.jsp" %> 
