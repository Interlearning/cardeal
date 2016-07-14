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
				<form class="form-horizontal" action="<c:url value="/stocks/add"/>" method="post" style="margin:0">
					<div class="row">						
						<div class="col-md-8 col-md-offset-2">
							
							<div class="form-group">
								<label for="companyId" class="col-sm-3 control-label">Filial</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="companyId" id="companyId"  value="${companyId}" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="stock.id" class="col-sm-3 control-label">Nr.Série</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="stock.idFormatSerial" id="stock.idFormatSerial" value="AUTOMÁTICO" readonly />
								</div>
							</div>
							
							<div class="form-group">
								<label for="terminalId" class="col-sm-3 control-label">Terminal</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="terminalId" id="terminalId" value="${terminalId}"/>
								</div>
							</div>
							
							<div class="form-group">
								<label for="palletId" class="col-sm-3 control-label">Pallet</label>
								<div class="col-sm-6">									
									<input type="text" class="form-control" name="palletId" id="palletId"  value="${palletId}" />									
								</div>
							</div>
							
							<div class="form-group">
								<label for="idMasc" maxlength="4" class="col-sm-3 control-label">Código do Produto</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="idMasc" id="idMasc"  value="${idMasc}" />
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
									<input type="text" class="form-control peso" name="stock.net" id="stock.net"  value="${stock.net}" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="stock.tare" class="col-sm-3 control-label">Tara</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" name="stock.tare" id="stock.tare"  value="${stock.tare}" />
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
									<script>
										$(function() {
											$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
											$( "#manufactureDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
										});
									</script>										
									<input type="text" class="form-control" name="manufactureDate" id="manufactureDate"  value="${stock.manufactureDateTimeFormat}" />								
								</div>
							</div>
							
							<div class="form-group">
								<label for="expirationDate" class="col-sm-3 control-label">Data de validade</label>
								<div class="col-sm-6">
									<script>
										$(function() {
											$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
											$( "#expirationDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
										});
									</script>
									<input type="text" class="form-control" name="expirationDate" id="expirationDate"  value="${stock.expirationDateTimeFormat}" />
								</div>
							</div>
													
							<div class="form-group">
								<label class="col-sm-3 control-label"></label>
								<div class="col-sm-6">
									<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
									<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/stocks/listManuten"/>';return false;">Cancelar</button>					
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
