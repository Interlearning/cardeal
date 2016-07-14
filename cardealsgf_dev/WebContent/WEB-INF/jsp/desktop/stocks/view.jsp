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
					<h2>Estoque</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/stocks/listManuten'/>"> Sair</a></li>
					</ul>
				</header>
				<section>					
					<c:if test="${empty stock}">
						<form class="form-horizontal" action="<c:url value="/stocks/add"/>" method="post" style="margin:0">
					</c:if>
					<c:if test="${not empty stock}">
						<form class="form-horizontal" action="<c:url value="/stocks/update"/>" method="post" style="margin:0">
					</c:if>
					<div class="row">						
						<div class="col-md-8 col-md-offset-2">
							
							<div class="form-group">
								<label for="stock.company.id" class="col-sm-3 control-label">Filial</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="stock.company.id" id="stock.company.id"  value="${stock.company.id}" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="stock.id" class="col-sm-3 control-label">Nr.Série</label>
								<div class="col-sm-6">
									<input type="hidden" class="form-control" name="stock.id" id="stock.id" value="${stock.id}" readonly />
									<input type="text" class="form-control" name="stock.idFormatSerial" id="stock.idFormatSerial" value="${stock.idFormatSerial}" readonly />
								</div>
							</div>
							
							<div class="form-group">
								<label for="stock.terminal.id" class="col-sm-3 control-label">Terminal</label>
								<div class="col-sm-6">
									<c:if test="${empty stock}">
										<input type="text" class="form-control" name="stock.terminal.id" id="stock.terminal.id" value="${stock.terminal.id}"/>
									</c:if>
									<c:if test="${not empty stock}">										
										<input type="text" class="form-control" name="stock.terminal.id" id="stock.terminal.id" value="${stock.terminal.id}" readonly />
									</c:if>
								</div>
							</div>
							
							<div class="form-group">
								<label for="stock.pallet.id" class="col-sm-3 control-label">Pallet</label>
								<div class="col-sm-6">
									<c:if test="${empty stock}">
										<input type="text" class="form-control" name="stock.pallet.id" id="stock.pallet.id"  value="${stock.pallet.id}" />
									</c:if>
									<c:if test="${not empty stock}">
										<input type="text" class="form-control" name="stock.pallet.id" id="stock.pallet.id"  value="${stock.pallet.id}" readonly />
									</c:if>
								</div>
							</div>
							
							<div class="form-group">
								<label for="stock.product.idMasc" class="col-sm-3 control-label">Código do Produto</label>
								<div class="col-sm-6">
									<input type="hidden" class="form-control" name="stock.product.id" id="stock.product.id" value="${stock.product.id}"/>
									<c:if test="${empty stock}">										
										<input type="text" class="form-control" name="stock.product.idMasc" id="stock.product.idMasc"  value="${stock.product.idMasc}" />
									</c:if>
									<c:if test="${not empty stock}">
										<input type="text" class="form-control" name="stock.product.idMasc" id="stock.product.idMasc"  value="${stock.product.idMasc}" readonly />
									</c:if>
								</div>
							</div>
							
							<!--  
							<div class="form-group">
								<label for="stock.partner.id" class="col-sm-3 control-label">Código Cliente/Fornecedor</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="stock.partner.id" id="stock.partner.id"  value="${stock.partner.id}" />
								</div>
							</div>
							-->
							<c:if test="${not empty stock}">
							
								<div class="form-group">
									<label for="stock.product.description" class="col-sm-3 control-label">Descrição do Produto</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="stock.product.description" id="stock.product.description"  value="${stock.product.description}" readonly />
									</div>
								</div>
							
							</c:if>
							
							<div class="form-group">
								<label for="stock.product.unit.id" class="col-sm-3 control-label">Unidade De Medida</label>
								<div class="col-sm-6">
									<select class="form-control" name="stock.product.unit.id">
						                <c:forEach items="${units}" var="unit">
						                	<c:if test="${stock.product.unit.id == unit.id}">
						                        <option value="${unit.id}" selected>${unit.description}</option>
						                	</c:if>
						                	<c:if test="${stock.product.unit.id != unit.id}">
						                        <option value="${unit.id}">${unit.description}</option>
						                	</c:if>
						                </c:forEach>
		 							</select>
								</div>
							</div>		
							
							<div class="form-group">
								<label for="stock.primaryQty" class="col-sm-3 control-label">Qtd. Peças</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="stock.primaryQty" id="stock.primaryQty"  value="${stock.primaryQty}" />
								</div>							
							</div>
							
							<div class="form-group">
								<label for="stock.net" class="col-sm-3 control-label">Peso Líquido</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="stock.net" id="stock.net"  value="${stock.netFormatted}" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="stock.tare" class="col-sm-3 control-label">Tara</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="stock.tare" id="stock.tare"  value="${stock.tareFormatted}" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="stock.batch" class="col-sm-3 control-label">Lote</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="stock.batch" id="stock.batch"  value="${stock.batch}" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="date1" class="col-sm-3 control-label">Entrada</label>
								<div class="col-sm-6">
									<script>
										$(function() {
											$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
											$( "#enterDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
										});
									</script>
									<input type="text" class="form-control" id="enterDate" name="enterDate" value="${stock.enterDateTimeFormat}"/>
								</div>
							</div>
																					
							<div class="form-group">
								<label for="stock.typeStock" class="col-sm-3 control-label">Estoque</label>
								<div class="col-sm-6">
									<select class="form-control" name="stock.typeStock">
						                <c:forEach items="${typeStock}" var="type">
						                	<c:if test="${stock.typeStock == type}">
						                        <option value="${type}" selected>${type}</option>
						                	</c:if>
						                	<c:if test="${stock.typeStock != type}">
						                        <option value="${type}">${type}</option>
						                	</c:if>
						                </c:forEach>
		 							</select>
								</div>
							</div>	
							
							<div class="form-group">
								<label for="manufactureDate" class="col-sm-3 control-label">Data de fabricação</label>
								<div class="col-sm-6">
									<c:if test="${empty stock}">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#manufactureDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>										
										<input type="text" class="form-control" name="manufactureDate" id="manufactureDate"  value="${stock.manufactureDateTimeFormat}" />
									</c:if>
									<c:if test="${not empty stock}">
										<input type="text" class="form-control" name="manufactureDate" id="manufactureDate"  value="${stock.manufactureDateTimeFormat}" readonly />
									</c:if>
								</div>
							</div>
							
							<div class="form-group">
								<label for="expirationDate" class="col-sm-3 control-label">Data de validade</label>
								<div class="col-sm-6">
									<c:if test="${empty stock}">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#expirationDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" name="expirationDate" id="expirationDate"  value="${stock.expirationDateTimeFormat}" />
									</c:if>
									<c:if test="${not empty stock}">
										<input type="text" class="form-control" name="expirationDate" id="expirationDate"  value="${stock.expirationDateTimeFormat}" readonly />
									</c:if>
								</div>
							</div>
													
							<div class="form-group">
								<label class="col-sm-3 control-label"></label>
								<div class="col-sm-6">
									<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
									<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/stocks/listManuten"/>';return false;">Cancelar</button>					
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-sm-6">
									<input type="hidden" class="form-control" name="stock.user.id" id="stock.user.id" value="${stock.user.id}"/>
									<input type="hidden" class="form-control" name="stock.idOld" id="stock.idOld" value="${stock.idOld}"/>
									<input type="hidden" class="form-control" name="stock.status" id="stock.status" value="${stock.status}"/>
									<input type="hidden" class="form-control" name="stock.inFifo" id="stock.inFifo" value="${stock.inFifo}"/>
									<input type="hidden" class="form-control" name="stock.availableNet" id="stock.availableNet" value="${stock.availableNet}"/>
									<input type="hidden" class="form-control" name="stock.availableQty" id="stock.availableQty" value="${stock.availableQty}"/>
								</div>
							</div>
														
						</div>	
					</div>	
					</form>
				</section>
				<footer></footer>
			</div>
		</article>
	</div>
</section>				


<%@ include file="../annex/footer.jsp" %> 
