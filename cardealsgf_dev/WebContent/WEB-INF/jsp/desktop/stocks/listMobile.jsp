
<%@ include file="../annex/header.jsp" %> 

<script type="text/javascript" src="<c:url value='/resources/js/jquery.ui.datepicker-pt-BR.js'/>"></script>					
<script type="text/javascript" src="<c:url value='/resources/js/jquey-ui-timepicker-addon.js'/>"></script>	

<%@ include file="../annex/menu.jsp" %> 
<section class="container"><%@ include file="../annex/messages.jsp" %></section>

<section class="container" role="main">

	<div class="row">
		<!-- Data block -->
		<article class="col-sm-12">
			<div class="data-block">
				<header>
					<h2>Parâmetros</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
					</ul>
				</header>
				<section>
					<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/stocks/listMobile'/>" >
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
										<input type="text" class="form-control" name="filter.id" id="filter.id" value="${filter.formattedId}" />
									</div>
								</div>
								<div class="form-group">
									<label for="filter.id_2" class="col-sm-3 control-label">Número de Série Até ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.id_2" id="filter.id_2" value="${filter.formattedId_2}" />
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
									<label for="date1" class="col-sm-3 control-label">Entrada de ?</label>
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
									<label for="date2" class="col-sm-3 control-label">Entrada até ?</label>
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
									<label for="date3" class="col-sm-3 control-label">Produção de ?</label>
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
									<label for="date4" class="col-sm-3 control-label">Produção até ?</label>
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
								<div class="form-group">
									<label for="showTotal" class="col-sm-3 control-label">Mostrar Totalizado</label>
									<div class="col-sm-1">
										<input type="checkbox" class="form-control" name="showTotal" id="showTotal"  value="true" <c:if test="${showTotal}">checked</c:if>/>
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
					<br>
					
					<c:if test="${showTotal}">
						<div class="row visible-xs">
							<article>
								<div class="dark data-block">
									<header>
										<h2>Total</h2>
									</header>
									<section>
										<div class="table-responsive">
											<table class="datatable table table-hover ui-responsive">
												<thead>
													<tr>
														<th>Produto</th>
														<th>Embalagem</th>
														<th>Caixas</th>														
														<th>Peças</th>
														<th>Peso (kg)</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${totals}" var="total">
													<tr class="odd gradeX">
														<td>${total.product}</td>
														<td>${total.totEmb}</td>
														<td>${total.secondaryQty}</td>														
														<td>${total.primaryQty}</td>
														<td>${total.netFormatted}</td>
													</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</section>
									</div>	
							</article>									
						</div>
					</c:if>
					
					<c:if test="${!showTotal}">
						<div class="row visible-xs">
							<article class="col-sm-12">
								<div class="dark data-block">	
									<header>
										<h2>Listagem de Estoque</h2>
									</header>
									<section>
										<div class="table-responsive">
											<table class="datatable table table-hover ui-responsive">
												<thead>
													<tr>												
														<th>Nr. Série</th>
														<th>Fil</th>
														<th>Cod.</th>
														<th>Descrição</th>
														<th>Dt. Produção</th>
														<th>Emb</th>
														<th>Cx</th>												
														<th>Pc</th>
														<th>Peso Líquido (kg)</th>
														<th>Pallet</th>
														<th>Flag</th>												
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${stocks}" var="stock">
													<tr class="odd gradeX">												
														<td>${stock.idFormatSerial}</td>
														<td>${stock.company.id}</td>
														<td>${stock.product.idMasc}</td>
														<td>${stock.product.descriptionTruncated}</td>
														<td>${stock.manufactureDateFormat}</td>
														<td>1</td>
														<td>${stock.secondaryQty}</td>												
														<td>${stock.primaryQty}</td>
														<td>${stock.netFormatted}</td>
														<td>${stock.pallet.idFormatted}</td>
														<td>${stock.operation}</td>																																				
													</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</section>
									<footer> </footer>
								</div>
							</article>
						</div>
					</c:if>
					
					<c:if test="${not empty totalGeral}">
						<div class="row visible-xs">
							<article class="col-sm-12">
								<div class="dark data-block">	
									<header>
										<h2>Total Geral - ${descTypeStock}</h2>
									</header>
									<section>
										<div class="table-responsive">
											<table class="datatable table table-hover ui-responsive">
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
														<td>${totalGer.totEmb}</td>
														<td>${totalGer.secondaryQty}</td>												
														<td>${totalGer.primaryQty}</td>
														<td>${totalGer.netFormatted}</td>
													</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
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
