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
					<h2>Listagem da Produção Diária</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
					</ul>
				</header>
				<section>
				<form class="form-horizontal" name="form_search" 
					  method="post" id="form_search"
					  action="<c:url value='/stocks/producaoDiaria'/>" >
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
								<label for="date1" class="col-sm-3 control-label">Data De ?</label>
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
								<label for="date2" class="col-sm-3 control-label">Data Até ?</label>
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
						</div>
					</div>	
					<div class="row">
						<div class="col-md-8 col-md-offset-2">
							<input type="submit" value="Pesquisar" class="btn btn-md btn-inverse pull-right"/>							
							<input type="button" id="limpar" value="Limpar" class="btn btn-md btn-default pull-left"/>					
						</div>
					</div>
				</form>
				
				<!--  filtro para geracao de cvs -->
				<c:if test="${not empty producao}">
					<br>
					<hr>
					<input type="hidden" id="companyId" value="${companyId}" />
					<input type="hidden" id="companyId2" value="${companyId2}" />
					<input type="hidden" id="terminalId" value="${terminalId}" />
					<input type="hidden" id="terminalId2" value="${terminalId2}" />
					<input type="hidden" id="enterDateDe"  value="${date1}" />
					<input type="hidden" id="enterDateAte" value="${date2}" />
					<div class="data-block">
						<div id="dailyProduction"></div>
						<div id="targetExcel"></div>							
					</div>					
				</c:if>					
				
				<hr>
				<div class="row hidden-xs">
					<article class="col-sm-12">
						<div class="dark data-block">	
							<header>
								<h2>Produção Diária</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>Produto</th>
											<th>Descrição</th>
											<th>Caixas</th>
											<th>Peças</th>
											<th>Peso Líquido (kg)</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${producao}" var="producaoDiaria">
											<tr class="odd gradeX">
												<td>${producaoDiaria.product.idMasc}</td>
												<td>${producaoDiaria.product.description}</td>
												<td align=right>${producaoDiaria.secondaryQty}</td>
												<td align=right>${producaoDiaria.primaryQty}</td>
												<td align=right>${producaoDiaria.netFormatted}</td>
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
									<h2>Produção Diária</h2>
								</header>
								<section>
									<div class="table-responsive">
										<table class="table table-hover">
											<thead>
												<tr>
													<th>Produto</th>
													<th>Descrição</th>
													<th>Caixas</th>
													<th>Peças</th>
													<th>Peso Líquido (kg)</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${producao}" var="producaoDiaria">
													<tr class="odd gradeX">
														<td>${producaoDiaria.product.idMasc}</td>
														<td>${producaoDiaria.product.description}</td>
														<td align=right>${producaoDiaria.secondaryQty}</td>
														<td align=right>${producaoDiaria.primaryQty}</td>
														<td align=right>${producaoDiaria.netFormatted}</td>										
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</section>
							</div>
						</article>
					</div>		
					
					<c:if test="${not empty totalGeral}">
						<div class="row hidden-xs">
							<article class="col-sm-12">
								<div class="dark data-block">	
									<header>
										<h2>Total Geral</h2>
									</header>
									<section>
										<table class="table table-striped table-bordered table-hover">
											<thead>
												<tr>											
													<th>Embalagens</th>
													<th>Caixas</th>												
													<th>Peças</th>
													<th>Peso (kg)</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${totalGeral}" var="totalGer">
												<tr class="odd gradeX">											
													<td align=right>${totalGer.totEmb}</td>
													<td align=right>${totalGer.secondaryQty}</td>												
													<td align=right>${totalGer.primaryQty}</td>
													<td align=right>${totalGer.netFormatted}</td>
												</tr>
												</c:forEach>
											</tbody>
										</table>
									</section>
									<footer> </footer>
								</div>
							</article>
						</div>
					</c:if>
								
				</section>
				<footer></footer>
			</div>
		</article>
	</div>
</section>
<script type="text/javascript" src="<c:url value='/resources/js/ResetField.js'/>" ></script>
<%@ include file="../annex/footer.jsp" %> 
