<%@ include file="../annex/header.jsp" %> 

<script type="text/javascript" src="<c:url value='/resources/js/jquery.ui.datepicker-pt-BR.js'/>"></script>					
<script type="text/javascript" src="<c:url value='/resources/js/jquey-ui-timepicker-addon.js'/>"></script>

<%@ include file="../annex/menu.jsp" %> 
<section class="container"><%@ include file="../annex/messages.jsp" %></section>

<section class="container" role="main">

	<!-- Grid row -->
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
					<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/stocks/stockManuten'/>" >
						<div class="row">						
							<div class="col-md-8 col-md-offset-2">

								<div class="form-group">
									<label for="filter.typeStock" class="col-sm-3 control-label">Tipo de Estoque</label>
									<div class="col-sm-6">
										<select name="filter.typeStock" class="form-control" id="filter.typeStock" required="required">
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
									<label for="filter.productIdMasc" class="col-sm-3 control-label">Produto ?</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.productIdMasc" id="filter.productIdMasc" value="${filter.productIdMasc}" required="required"/>
									</div>
								</div>
												
							</div>
						</div>
						<div class="row">
							<div class="col-md-8 col-md-offset-2">
								<input type="submit" value="Carregar" class="btn btn-md btn-inverse pull-right"/>							
								<input type="submit" value="Limpar" onclick="form_search.filter.value=''; return true;" class="btn btn-md btn-default pull-left"/>								
							</div>
						</div>
						
					</form>
					<br>
					<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/stocks/deleteGranel'/>" >
						<div class="row">
							<div class="col-md-8 col-md-offset-2">
								<input type="submit" value="Excluir Granel" class="btn btn-md btn-default pull-left"/>							
							</div>
						</div>
					</form>					
					
					<hr>
					<br>
					
					<c:if test="${not empty stockManuten}">
						
						<form class="form-horizontal" name="form_search_2" method="post" action="<c:url value='/stocks/changeStocksManuten'/>" >
										
							<div class="row hidden-xs">
								<article class="col-sm-12">
									<div class="dark data-block">									
																
										<section>
										
											<div class="form-group">
												<label for="stockManuten.product.idMasc" class="col-sm-3 control-label">Produto</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="stockManuten.product.idMasc" id="stockManuten.product.idMasc" value="${stockManuten.product.idMasc}" readonly />
													<input type="hidden" class="form-control" name="stockManuten.typeStock" id="stockManuten.typeStock" value="${stockManuten.typeStock}" />
													<input type="hidden" class="form-control" name="stockManuten.idProduct" id="stockManuten.idProduct" value="${stockManuten.idProduct}" />
												</div>
											</div>
											
											<div class="form-group">
												<label for="stockManuten.product.description" class="col-sm-3 control-label">Descrição</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="stockManuten.product.description" id="stockManuten.product.description" value="${stockManuten.product.description}" readonly />
												</div>
											</div>
											
											<div class="form-group">
												<label for="stockManuten.product.unit.id" class="col-sm-3 control-label">Unidade de medida</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="stockManuten.product.unit.id" id="stockManuten.product.unit.id" value="${stockManuten.product.unit.id}" readonly />
												</div>
											</div>
											
											<div class="form-group">
												<label for="stockManuten.currentQtyBox" class="col-sm-3 control-label">Quantidade de Caixas atual</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="stockManuten.currentQtyBox" id="stockManuten.currentQtyBox" value="${stockManuten.currentQtyBox}" readonly />
												</div>
											</div>
											
											<div class="form-group">
												<label for="stockManuten.currentPrimaryQty" class="col-sm-3 control-label">Quantidade de Peças atual</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="stockManuten.currentPrimaryQty" id="stockManuten.currentPrimaryQty" value="${stockManuten.currentPrimaryQty}" readonly />
												</div>
											</div>
											
											<div class="form-group">
												<label for="stockManuten.currentQuantity" class="col-sm-3 control-label">Quantidade atual</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="stockManuten.currentQuantity" id="stockManuten.currentQuantity" value="${stockManuten.currentQuantity}" readonly />
												</div>
											</div>
											
											<div class="form-group">
												<label for="stockManuten.quantityChange" class="col-sm-3 control-label">Quantidade de ajuste</label>
												<div class="col-sm-6">
													<input type="text" class="form-control pesoNegativo" name="stockManuten.quantityChange" id="stockManuten.quantityChange" value="${stockManuten.quantityChange}" required="required"/>
												</div>
											</div>
											
											<div class="form-group">
												<label for="stockManuten.quantityChangePrimaryQty" class="col-sm-3 control-label">Quantidade de peças</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="stockManuten.quantityChangePrimaryQty" id="stockManuten.quantityChangePrimaryQty" value="${stockManuten.quantityChangePrimaryQty}" onkeypress="return onlyInteger(event);" required="required"/>
												</div>
											</div>
											
											<div class="form-group">
												<label for="stockManuten.quantityChangeBox" class="col-sm-3 control-label">Quantidade de Caixas</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="stockManuten.quantityChangeBox" id="stockManuten.quantityChangeBox" value="${stockManuten.quantityChangeBox}" onkeypress="return onlyInteger(event);" required="required"/>
												</div>
											</div>
											
											<div class="row">
												<div class="col-md-8 col-md-offset-2">
													<input type="submit" value="Ajustar" class="btn btn-md btn-inverse pull-right"/>																				
												</div>
											</div>	
											
										</section>
										
										<footer> </footer>
									</div>
								</article>
							</div>
												
						</form>	
						
					</c:if>		
												
				</section>
				<footer></footer>
			</div>
		</article>
	</div>
</section>				


<%@ include file="../annex/footer.jsp" %> 
