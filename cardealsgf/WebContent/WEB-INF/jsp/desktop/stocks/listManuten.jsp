<%@ include file="../annex/header.jsp" %> 

<script type="text/javascript" src="<c:url value='/resources/js/jquery.ui.datepicker-pt-BR.js'/>"></script>					
<script type="text/javascript" src="<c:url value='/resources/js/jquey-ui-timepicker-addon.js'/>"></script>

<script>
function action_form(id, nome, controller) {

	msg = "Tem certeza que deseja excluir " + nome + "?";
	if (confirm(msg)) {
		location.href = "<c:url value='/"+ controller +"/remove/"+ id +"'/>";
	}
}
</script>

<script>
function selecionar_tudo() {

	for (i=0;i<document.form_search_2.elements.length;i++) 
	      if(document.form_search_2.elements[i].type == "checkbox")	
	         document.form_search_2.elements[i].checked=1 
}

function desmarcar_tudo() {

	for (i=0;i<document.form_search_2.elements.length;i++) 
	      if(document.form_search_2.elements[i].type == "checkbox")	
	         document.form_search_2.elements[i].checked=0 
}
</script>

<%@ include file="../annex/menu.jsp" %> 
<section class="container"><%@ include file="../annex/messages.jsp" %></section>

<section class="container" role="main">

	<!-- Grid row -->
	<div class="row">
		<!-- Data block -->
		<article class="col-sm-12">
			<div class="data-block">
				<header>
					<h2>Parâmetros de filtro de Estoque</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
						<!--  <li><a href="<c:url value='/stocks/insert'/>">Novo Item de Estoque</a></li>-->
					</ul>
				</header>
				<section>
					<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/stocks/listManuten'/>" >
						<div class="row">						
							<div class="col-md-8 col-md-offset-2">
								<div class="form-group">
									<label for="filter.companyIdDe" class="col-sm-3 control-label">Filial De ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" maxlength="${maxlengthCompanyId}" name="filter.companyIdDe" id="filter.companyIdDe" value="${filter.companyIdDe}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.companyIdAte" class="col-sm-3 control-label">Filial Até ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" maxlength="${maxlengthCompanyId}" name="filter.companyIdAte" id="filter.companyIdAte" value="${filter.companyIdAte}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.terminalId" class="col-sm-3 control-label">Terminal De ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.terminalId" id="filter.terminalId" value="${filter.terminalId}"/>
									</div>
								</div>
								<div class="form-group">
									<label for="filter.terminalId_2" class="col-sm-3 control-label">Terminal Até ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="${filter.terminalId_2}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.id" class="col-sm-3 control-label">Número de Série De ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.id" id="filter.id" value="${filter.id}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.id_2" class="col-sm-3 control-label">Número de Série Até ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.id_2" id="filter.id_2" value="${filter.id_2}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.productIdMasc" class="col-sm-3 control-label">Produto De ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.productIdMasc" id="filter.productIdMasc" value="${filter.productIdMasc}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.productIdMasc_2" class="col-sm-3 control-label">Produto Até ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.productIdMasc_2" id="filter.productIdMasc_2" value="${filter.productIdMasc_2}" />
									</div>
								</div>							
								<div class="form-group">
									<label for="date1" class="col-sm-3 control-label">Data de Entrada De ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date1" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date1" name="date1" value="${date1}"/>
									</div>
								</div>
								<div class="form-group">
									<label for="date2" class="col-sm-3 control-label">Data de Entrada Até ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date2" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date2" name="date2" value="${date2}"/>
									</div>
								</div>
								<div class="form-group">
									<label for="date3" class="col-sm-3 control-label">Data de Produção De ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date3" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date3" name="date3" value="${date3}"/>
									</div>
								</div>
								<div class="form-group">
									<label for="date4" class="col-sm-3 control-label">Data de Produção Até ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date4" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date4" name="date4" value="${date4}"/>
									</div>
								</div>
								<div class="form-group">
									<label for="filter.typeStock" class="col-sm-3 control-label">Tipo de Estoque</label>
									<div class="col-sm-6">
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
												
							</div>
						</div>
						<div class="row">
							<div class="col-md-8 col-md-offset-2">
								<input type="submit" value="Pesquisar" class="btn btn-md btn-inverse pull-right"/>							
								<input type="submit" value="Limpar" onclick="form_search.filter.value=''; return true;" class="btn btn-md btn-default pull-left"/>
							</div>
						</div>		
					</form>
					<br>
					<hr>
					<br>	
					<form class="form-horizontal" name="form_search_2" method="post" action="<c:url value='/stocks/deleteSelectedOnStocksManuten'/>" >
					
						<div class="form-group">
							<div class="col-md-8 col-md-offset-2">
								<label for="motivo" class="col-sm-3 control-label">Código do Motivo (MANUTEN.TXT)</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="2" name="motivo" id="motivo" value="${motivo}" onkeypress="return onlyInteger(event);"/>
								</div>
							</div>
						</div>	
								
						<div class="row hidden-xs">
							<article class="col-sm-12">
								<div class="dark data-block">									
									
									<header>
										<h2>Estoque não paletizados</h2>
										<ul class="data-header-actions">
											<input type="submit" value="Excluir selecionados" class="btn btn-md btn-inverse pull-right"/>
										</ul>
									
										<ul class="data-header-actions">
											<input type="button" value="Marcar todos" class="btn btn-md btn-inverse pull-right" onClick="selecionar_tudo()"/>											
										</ul>
										
										<ul class="data-header-actions">
											<input type="button" value="Desmarcar todos" class="btn btn-md btn-inverse pull-right" onClick="desmarcar_tudo()"/>											
										</ul>
																				
									</header>	
															
									<section>
									
										<table class="datatable table table-striped table-bordered table-hover">
											<thead>
												<tr>
													<th></th>
													<th>Filial</th>
													<th>Nr. Série</th>
													<th>Produto</th>
													<th>Unidade</th>
													<th>Peças</th>
													<th>Peso (kg)</th>											
													<th>Entrada</th>
													<th>Dt. Produção</th>
													<th>Estoque</th>
													<th>Flag</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${stocks}" var="stock" varStatus="s">
													<tr class="odd gradeX">
														<td class="toolbar toolbar-btn-link" style="text-align:center;">
															<div class="checkbox">
																<label>																
																	 <input type="checkbox" name="itensSelected[${s.index}]" value="${stock.id}">																
																</label>
															</div>
														</td>
														<td>${stock.company.id}</td>
														<td>${stock.idFormatSerial}</td>
														<td>${stock.product}</td>
														<td>${stock.unitDesc}</td>
														<td>${stock.primaryQty}</td>
														<td>${stock.netFormatted}</td>											
														<td>${stock.enterDateFormat}</td>
														<td>${stock.manufactureDateFormat}</td>
														<td>${stock.typeStock}</td>
														<td>${stock.operation}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</section>
									<footer> </footer>
								</div>
							</article>
						</div>
											
					</form>										
				</section>
				<footer></footer>
			</div>
		</article>
	</div>
</section>				


<%@ include file="../annex/footer.jsp" %> 
