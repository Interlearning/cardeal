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
				<form class="form-horizontal" name="form_search"
					  method="post" id="form_search"
					  action="<c:url value='/stocks/pallets'/>" >
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
						</div>
					</div>	
					<div class="row">
						<div class="col-md-8 col-md-offset-2">
							<input type="submit" value="Pesquisar" 
								   class="btn btn-md btn-inverse pull-right"/>							
							<input type="button" id="limpar" value="Limpar"
								   class="btn btn-md btn-default pull-left"	/>						
						</div>
					</div>	
				</form>

				<c:if test="${not empty pallets}">
					<br>
					<hr>
					<input type="hidden" id="companyId" value="${companyId}" />
					<input type="hidden" id="companyId2" value="${companyId2}" />
					<input type="hidden" id="terminalId" value="${terminalId}" />
					<input type="hidden" id="terminalId2" value="${terminalId2}" />
					<input type="hidden" id="productIdMascDe"  value="${idMasc}" />
					<input type="hidden" id="productIdMascAte"  value="${idMasc_2}" />
					<input type="hidden" id="palletIdDe"  value="${palletIdDe}" />
					<input type="hidden" id="palletIdAte"  value="${palletIdAte}" />
					<input type="hidden" id="manufactureDateDe"  value="${date3}" />
					<input type="hidden" id="manufactureDateAte" value="${date4}" />
					<input type="hidden" id="typeStock" value="${typeStock}" />
					<div class="data-block">
						<div id="pallets"></div>
						<div id="targetExcel"></div>
					</div>
				</c:if>
					
				<hr>
				<br>
				<div class="row hidden-xs">
					<article class="col-sm-12">
						<div class="dark data-block">	
						<header>
							<h2>Pallets</h2>
						</header>
						<section>
							<table class="datatable table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>Nr.Série</th>
										<th>Cod.</th>
										<th>Descrição</th>
										<th>Emb</th>
										<th>Cx</th>
										<th>Pc</th>
										<th>Peso Líquido (kg)</th>
										<th>Fil</th>
										<th>Ter</th>
										<th>Iniciado em</th>
										<th>Fechado em</th>
										<th>Status</th>										
										<th wigth="10%">Ação</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${pallets}" var="pallet">
									<tr class="odd gradeX">
										<td>${pallet.idFormatted}</td>
										<td>${pallet.product.idMasc}</td>
										<td>${pallet.product.descriptionTruncated}</td>
										<td align=right>${pallet.embQty}</td>
										<td align=right>${pallet.secondaryQty}</td>
										<td align=right>${pallet.primaryQty}</td>
										<td align=right>${pallet.netFormatted}</td>
										<td>${pallet.company.id}</td>
										<td>${pallet.terminal.idTerminal}</td>
										<!-- WJSP 14/07/2016 -->
										<td align=right data-order="${pallet.openDate.time}">${pallet.openDateDesc}</td>
										<td align=right data-order="${pallet.closeDate.time}">${pallet.closeDateDesc}</td>
										<td>${pallet.statusDesc}</td>
										<td class="toolbar toolbar-btn-link" style="text-align:center;">
											<div class="btn-group">													
												<button class="btn btn-md btn-info" 
														title="Visualizar caixas do pallet ${pallet.id}" 
														onclick="window.location.href='<c:url value="/stocks/palletview/${pallet.id}"/>';return false;">
															<span class="elusive icon-search"></span></button>																						
											</div>
										</td>										
									</tr>
									</c:forEach>
								</tbody>
							</table>
							<br>
							<div class="row">
								<div class="col-md-8 col-md-offset-2">
								
									<form	name="form_anterior" method="post"
											action="<c:url value='/stocks/palletsBack'/>">
											
											<input type="hidden" id="companyId" value="${companyId}" />
											<input type="hidden" id="companyId2" value="${companyId2}" />
											<input type="hidden" id="terminalId" value="${terminalId}" />
											<input type="hidden" id="terminalId2" value="${terminalId2}" />
											<input type="hidden" id="productIdMascDe"  value="${idMasc}" />
											<input type="hidden" id="productIdMascAte"  value="${idMasc_2}" />
											<input type="hidden" id="palletIdDe"  value="${palletIdDe}" />
											<input type="hidden" id="palletIdAte"  value="${palletIdAte}" />
											<input type="hidden" id="manufactureDateDe"  value="${date3}" />
											<input type="hidden" id="manufactureDateAte" value="${date4}" />
											<input type="hidden" id="typeStock" value="${typeStock}" />																
											<input type="submit" value="Voltar ${qtyPerPage} registros" class="btn btn-md btn-default pull-left"/>
									</form>
									
									<form	name="form_proximo" method="post"
											action="<c:url value='/stocks/palletsForward'/>">
											
											<input type="hidden" id="companyId" value="${companyId}" />
											<input type="hidden" id="companyId2" value="${companyId2}" />
											<input type="hidden" id="terminalId" value="${terminalId}" />
											<input type="hidden" id="terminalId2" value="${terminalId2}" />
											<input type="hidden" id="productIdMascDe"  value="${idMasc}" />
											<input type="hidden" id="productIdMascAte"  value="${idMasc_2}" />
											<input type="hidden" id="palletIdDe"  value="${palletIdDe}" />
											<input type="hidden" id="palletIdAte"  value="${palletIdAte}" />
											<input type="hidden" id="manufactureDateDe"  value="${date3}" />
											<input type="hidden" id="manufactureDateAte" value="${date4}" />
											<input type="hidden" id="typeStock" value="${typeStock}" />																	
											<input type="submit" value="Avançar ${qtyPerPage} registros" class="btn btn-md btn-inverse pull-right"/>
									</form>
																						
								</div>
							</div>
							</section>
							<footer> </footer>
						</div>
					</article>
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
