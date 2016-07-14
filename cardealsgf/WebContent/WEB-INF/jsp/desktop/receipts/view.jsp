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
					<c:if test="${not empty itemEdit}">			
						<div class="row hidden-xs">
							<article class="col-sm-12">
								<div class="dark data-block">
																									
									<section>
										<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/receipts/change'/>" >
										
											<input type="hidden" class="form-control" name="itemEdit.id" id="itemEdit.id" value="${itemEdit.id}" />
											
											<div class="form-group">
												<label for="itemEdit.product.idMasc" class="col-sm-3 control-label">Código</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="itemEdit.product.idMasc" id="itemEdit.product.idMasc" value="${itemEdit.product.idMasc}" readonly/>
												</div>
											</div>
											
											<div class="form-group">
												<label for="itemEdit.product.description" class="col-sm-3 control-label">Descrição</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="itemEdit.product.description" id="itemEdit.product.description" value="${itemEdit.product.description}" readonly/>
												</div>
											</div>
											
											<div class="form-group">
												<label for="itemEdit.unit.id" class="col-sm-3 control-label">Unidade de Medida</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="itemEdit.unit.id" id="itemEdit.unit.id" value="${itemEdit.unit.id}" readonly/>
												</div>
											</div>
											
											<div class="form-group">
												<label for="itemEdit.quantity" class="col-sm-3 control-label">Quantidade</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="itemEdit.quantity" id="itemEdit.quantity" value="${itemEdit.quantityFormattedWithDot}" />
												</div>
											</div>
											
											<div class="form-group">
												<label for="itemEdit.tare" class="col-sm-3 control-label">Tara</label>
												<div class="col-sm-6">
													<input type="text" class="form-control peso" name="itemEdit.tare" id="itemEdit.tare" value="${itemEdit.tareFormattedWithDot}" />
												</div>
											</div>
											
											<div class="form-group">
												<label for="itemEdit.net" class="col-sm-3 control-label">Peso Líquido</label>
												<div class="col-sm-6">
													<input type="text" class="form-control peso" name="itemEdit.net" id="itemEdit.net" value="${itemEdit.netFormattedWithDot}" />
												</div>
											</div>	
											
											<div class="form-group">
												<label for="itemEdit.batchExternal" class="col-sm-3 control-label">Lote Externo</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="itemEdit.batchExternal" id="itemEdit.batchExternal" value="${itemEdit.batchExternal}" />
												</div>
											</div>
											
											<div class="form-group">
												<label for="validateDate" class="col-sm-3 control-label">Dt. Validade</label>
												<div class="col-sm-6">
													<script>
														$(function() {
															$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
															$( "#validateDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
														});
													</script>
													<input type="text" class="form-control" id="validateDate" name="validateDate" value="${itemEdit.dateValidateBatchExternalFormat}"/>
												</div>
											</div>
											
											<div class="form-group">
												<label for="itemEdit.typeStock.descricao" class="col-sm-3 control-label">Destino</label>
												<div class="col-sm-6">
													<input type="hidden" class="form-control" name="itemEdit.typeStock" id="itemEdit.typeStock" value="${itemEdit.typeStock}" readonly/>
													<input type="text" class="form-control" name="itemEdit.typeStock.descricao" id="itemEdit.typeStock.descricao" value="${itemEdit.typeStock.descricao}" readonly/>
												</div>
											</div>
											
											<div class="row">
												<div class="col-md-8 col-md-offset-2">
													<input type="submit" value="Ajustar" class="btn btn-md btn-inverse pull-right"/>							
												</div>
											</div>
										</form>
										
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
