<%@ include file="../annex/header.jsp" %> 

<script type="text/javascript" src="<c:url value='/resources/js/jquery.ui.datepicker-pt-BR.js'/>"></script>					
<script type="text/javascript" src="<c:url value='/resources/js/jquey-ui-timepicker-addon.js'/>"></script>					

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
					<h2>Listagem de Pallets</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
					</ul>
				</header>
				<section>
					<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/stocks/palletsManuten'/>" >
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
										<input type="text" class="form-control" name="filter.terminalId" id="filter.terminalId" value="${filter.terminalId}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.terminalId_2" class="col-sm-3 control-label">Terminal Até ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="${filter.terminalId_2}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.palletIdDe" class="col-sm-3 control-label">Palete De ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.palletIdDe" id="filter.palletIdDe" value="${filter.formattedIdPalletDe}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.palletIdAte" class="col-sm-3 control-label">Palete Até ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.palletIdAte" id="filter.palletIdAte" value="${filter.formattedIdPalletAte}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.idMasc" class="col-sm-3 control-label">Produto De ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.idMasc" id="filter.idMasc" value="${filter.idMasc}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.idMasc_2" class="col-sm-3 control-label">Produto Até ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.idMasc_2" id="filter.idMasc_2" value="${filter.idMasc_2}" />
									</div>
								</div>
								<div class="form-group">
									<label for="date1" class="col-sm-3 control-label">Entrada De ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date1" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date1" name="date1" value="${date1}" />
									</div>
								</div>
								<div class="form-group">
									<label for="date2" class="col-sm-3 control-label">Entrada Até ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date2" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date2" name="date2" value="${date2}" />
									</div>
								</div>
								<div class="form-group">
									<label for="date3" class="col-sm-3 control-label">Produção De ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date3" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date3" name="date3" value="${date3}" />
									</div>
								</div>
								<div class="form-group">
									<label for="date4" class="col-sm-3 control-label">Produção Até ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date4" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date4" name="date4" value="${date4}" />
									</div>
								</div>
								<div class="form-group">
									<label for="date5" class="col-sm-3 control-label">Fechamento De ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date5" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date5" name="date5" value="${date5}" />
									</div>
								</div>
								<div class="form-group">
									<label for="date6" class="col-sm-3 control-label">Fechamento Até ?</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#date6" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="date6" name="date6" value="${date6}" />
									</div>
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
					<div class="row hidden-xs">

						<form class="form-horizontal" name="form_search_2" method="post" action="<c:url value='/stocks/deleteSelectedOnPallets'/>" >
						
							<div class="form-group">
								<div class="col-md-8 col-md-offset-2">
									<label for="motivo" class="col-sm-3 control-label">Código do Motivo (MANUTEN.TXT)</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" maxlength="2" name="motivo" id="motivo" value="${motivo}" onkeypress="return onlyInteger(event);"/>
									</div>
								</div>
							</div>
						
							<br>
							
							<article class="col-sm-12">
								<div class="dark data-block">	
								<header>
									<h2>Pallets</h2>
									<ul class="data-header-actions">
										<input type="submit" value="Excluir selecionados" class="btn btn-md btn-inverse pull-right"/>
									</ul>
								</header>
								<section>
									<table class="datatable table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th></th>
												<th>Nr.Série</th>
												<th>Produto</th>
												<th>Caixas</th>
												<th>Peças</th>
												<th>Peso Líquido (kg)</th>
												<th>Filial</th>
												<th>Terminal</th>
												<th>Iniciado em</th>
												<th>Fechado em</th>
												<th width="10%">Status</th>
												<th width="10%">Ação</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${pallets}" var="pallet" varStatus="s">
												<tr class="odd gradeX">
													<td class="toolbar toolbar-btn-link" style="text-align:center;">
														<div class="checkbox">
															<label>																
																 <input type="checkbox" name="itensSelected[${s.index}]" value="${pallet.id}">																
															</label>
														</div>
													</td>
													<td>${pallet.idFormatted}</td>
													<td>${pallet.product.description}</td>
													<td>${pallet.secondaryQty}</td>
													<td>${pallet.primaryQty}</td>
													<td>${pallet.netFormatted}</td>
													<td>${pallet.company.id}</td>
													<td>${pallet.terminal.idTerminal}</td>
													<td>${pallet.openDateDesc}</td>
													<td>${pallet.closeDateDesc}</td>
													<td>${pallet.statusDesc}</td>
													<td class="toolbar toolbar-btn-link" style="text-align:center;">
														<div class="btn-group">
															<button class="btn btn-md btn-info" title="Editar dados de ${pallet.id}" onclick="window.location.href='<c:url value="/stocks/palletviewedit/${pallet.id}"/>';return false;"><span class="elusive icon-wrench"></span></button>
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
						</form>
					</div>
				</section>
				<footer></footer>
			</div>
		</article>
	</div>
</section>
				

<%@ include file="../annex/footer.jsp" %> 
