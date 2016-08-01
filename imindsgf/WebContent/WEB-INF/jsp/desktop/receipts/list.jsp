<%@ include file="../annex/header.jsp" %> 
<%@ include file="../annex/menu.jsp" %> 
<section class="container"><%@ include file="../annex/messages.jsp" %></section>

<!-- Main page container -->
<section class="container" role="main">

	<!-- Grid row -->
	<div class="row">
		<article class="col-sm-12">
			<div class="data-block">
				<header>
					<h2>Parâmetros</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>						
					</ul>
				</header>
				<section>
					<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/receipts'/>" >
						
						<div class="row">						
							<div class="col-md-8 col-md-offset-2">
								
								<div class="form-group">
									<label for="filter.note" class="col-sm-3 control-label">Nota ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" maxlength="9" name="filter.note" id="filter.note" value="${filter.note}" required="required"/>
									</div>
								</div>
								
								<div class="form-group">
									<label for="filter.idPartner" class="col-sm-3 control-label">Fornecedor ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.idPartner" id="filter.idPartner" value="${filter.idPartner}" required="required"/>
									</div>
								</div>
																
								
						<div class="row">
							<div class="col-md-8 col-md-offset-2">
								<input type="submit" value="Pesquisar" class="btn btn-md btn-inverse pull-right"/>							
								<input type="submit" value="Limpar" onclick="form_search.filter.note=''; form_search.filter.idPartner=''; return true;" class="btn btn-md btn-default pull-left"/>
								
							</div>
						</div>
					</form>	
					<br>
					<hr>
					<br>
				</section>
			</div>
		</article>
	</div>
	<div>
		<article class="col-sm-12">
			<div class="data-block">
				<section>			
					<c:if test="${not empty itensReceipt}">			
						<div class="row hidden-xs">
							<article class="col-sm-12">
								<div class="dark data-block">
								
									<header>
										<h2>Itens da nota</h2>
									</header>
									
									<section>
										
										<table class="datatable table table-striped table-bordered table-hover">
											<thead>
												<tr>
													<th width="5%">SQ</th>
													<th width="25%">Produto</th>
													<th width="10%">Quant.</th>
													<th width="5%">UM.</th>
													<th width="5%">Tara</th>
													<th width="10%">Peso Líquido</th>
													<th width="10%">Peso Bruto</th>
													<th width="5%">Lote Externo</th>
													<th width="5%">Dt. Validade</th>
													<th width="5%">Destino</th>
													<th width="10%">Ação</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${itensReceipt}" var="item">
													<tr class="odd gradeX">
														<td>${item.index}</td>
														<td>${item.product}</td>
														<td>${item.quantityFormatted}</td>
														<td>${item.unit.id}</td>
														<td>${item.tareFormatted}</td>
														<td>${item.netFormatted}</td>
														<td>${item.grossWeightFormatted}</td>
														<td>${item.batchExternal}</td>
														<td>${item.dateValidateBatchExternalFormatted}</td>
														<td>${item.typeStock.sigla}</td>
														<td class="toolbar toolbar-btn-link" style="text-align:center;">
															<div class="btn-group" style="text-align:center;">													
																<button class="btn btn-md btn-info" title="Editar item ${item.index}" onclick="window.location.href='<c:url value="/receipts/view/${item.id}"/>';return false;"><span class="elusive icon-pencil"></span></button>
																<button class="btn btn-md btn-info" title="Excluir item ${item.index}" onclick="confirm_remove('${item.id}', '${item.product.description}','receipts')"><span class="elusive icon-remove"></span></button>
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
					</c:if>	
				</section>
				<footer> </footer>
			</div>
		</article>
		<!-- /Data block -->
	</div>	
</section>	

<%@ include file="../annex/footer.jsp" %> 
