<%@ include file="../annex/header.jsp" %>  

<script type="text/javascript" src="<c:url value='/resources/js/jquery.ui.datepicker-pt-BR.js'/>"></script>					
<script type="text/javascript" src="<c:url value='/resources/js/jquey-ui-timepicker-addon.js'/>"></script>

<div class="ui">

<h2>Listagem de Caixas do Pallet ${pallet.id}</h2>

<section>

	<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/stocks/palletsUpdate'/>" >

		<div class="form-group">
			<div class="col-sm-6">
				<button type="button" class="btn btn-md btn-inverse pull-center" onclick="window.history.back();">Sair</button>
				<input type="submit" value="Salvar" class="btn btn-md btn-inverse pull-center"/>				
			</div>
		</div>					
						
		<div class="row">						
					
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.id" class="col-sm-3 control-label">Nr. Série</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="hidden" class="form-control" name="pallet.id" id="pallet.id"  value="${pallet.id}" />
						<input type="text" class="form-control" name="pallet.id" id="pallet.id"  value="${pallet.idFormatted}" readonly/>
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.company.id" class="col-sm-3 control-label">Filial</label>
				<div class="col-sm-6">					
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.company.id" id="pallet.company.id"  value="${pallet.company.id}" readonly/>
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.terminal.idTerminal" class="col-sm-3 control-label">Terminal</label>
				<div class="col-sm-6">					
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.terminal.idTerminal" id="pallet.terminal.idTerminal"  value="${pallet.terminal.idTerminal}" readonly/>
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.product.idMasc" class="col-sm-3 control-label">Produto</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="hidden" class="form-control" name="pallet.product.id" id="pallet.product.id"  value="${pallet.product.id}" readonly />
						<input type="text" class="form-control" name="pallet.product.idMasc" id="pallet.product.idMasc"  value="${pallet.product.idMasc}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.batch" class="col-sm-3 control-label">Lote</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.batch" id="pallet.batch"  value="${pallet.batch}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.sscc" class="col-sm-3 control-label">SSCC</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="hidden" class="form-control" name="pallet.identifyLogisticPallet.id" id="pallet.identifyLogisticPallet.id"  value="${pallet.identifyLogisticPallet.id}" />
						<input type="text" class="form-control" name="pallet.sscc" id="pallet.sscc"  value="${pallet.sscc}" readonly />
					</div>
				</div>
			</div>
					
			<div class="col-md-8 col-md-offset-2">
				<label for="openDate" class="col-sm-3 control-label">Iniciado em</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<script>
							$(function() {
								$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
								$( "#openDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
							});
						</script>										
						<input type="text" class="form-control" name="openDate" id="openDate"  value="${pallet.openDateFormat}" />
					</div>	
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="closeDate" class="col-sm-3 control-label">Fechado em</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<script>
							$(function() {
								$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
								$( "#closeDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
							});
						</script>
						<input type="text" class="form-control" id="closeDate" name="closeDate" value="${pallet.closeDateFormat}"/>
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="manufactureDate" class="col-sm-3 control-label">Última Produção em</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<script>
							$(function() {
								$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
								$( "#manufactureDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
							});
						</script>
						<input type="text" class="form-control" id="manufactureDate" name="manufactureDate" value="${pallet.manufactureDateFormat}" readonly/>
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="expirationDate" class="col-sm-3 control-label">Data de validade</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<script>
							$(function() {
								$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
								$( "#expirationDate" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
							});
						</script>
						<input type="text" class="form-control" id="expirationDate" name="expirationDate" value="${pallet.expirationDateFormat}" readonly/>
					</div>
				</div>
			</div>	
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.status" class="col-sm-3 control-label">Status</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<select class="form-control" name="pallet.status">
			                <c:forEach items="${palletStatus}" var="status">
			                	<c:if test="${pallet.status == status}">
			                        <option value="${status}" selected>${pallet.statusDesc}</option>
			                	</c:if>
			                	<c:if test="${pallet.status != status}">
			                        <option value="${status}">${status.desc}</option>
			                	</c:if>
			                </c:forEach>
						</select>
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.howSell" class="col-sm-3 control-label">Unidade</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.howSell" id="pallet.howSell" value="${pallet.howSell}" readonly/>
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.tare" class="col-sm-3 control-label">Tara do Pallet</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control peso" name="pallet.tare" id="pallet.tare"  value="${pallet.tareFormattedWithDot}" />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.strech" class="col-sm-3 control-label">Strech</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control peso" name="pallet.strech" id="pallet.strech"  value="${pallet.strechFormattedWithDot}" />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.tareCantoneira" class="col-sm-3 control-label">Tara da Cantoneira</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control peso" name="pallet.tareCantoneira" id="pallet.tareCantoneira"  value="${pallet.tareCantoneiraFormattedWithDot}" />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.tareRack" class="col-sm-3 control-label">Tara do Rack</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control peso" name="pallet.tareRack" id="pallet.tareRack"  value="${pallet.tareRackFormattedWithDot}" />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.tarePack" class="col-sm-3 control-label">Tara da Embalagem</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control peso" name="pallet.tarePack" id="pallet.tarePack"   value="${pallet.tarePackFormattedWithDot}" />
					</div>
				</div>
			</div>
			
		</div>
		
	</form>
	
</section>			

	<form class="form-horizontal" name="form_search" method="post" action="<c:url value='/stocks/deleteSelectedOnPalletsManuten'/>" >

		<div class="form-group">
			<div class="col-md-8 col-md-offset-2">
				<label for="motivo" class="col-sm-3 control-label">Código do Motivo (MANUTEN.TXT)</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="text" class="form-control" maxlength="2" name="motivo" id="motivo" value="${motivo}" onkeypress="return onlyInteger(event);"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row hidden-xs">
			<article class="col-sm-12">
				<div class="dark data-block">	
				<header>
					<h2>Etiquetas emitidas para o Palete</h2>
					<ul class="data-header-actions">
						<input type="submit" value="Excluir selecionados" class="btn btn-md btn-inverse pull-right"/>
					</ul>
					<ul class="data-header-actions">
						<input type="button" value="Selecionar todos" class="btn btn-md btn-inverse pull-right" onClick="window.location.href='<c:url value="/stocks/palletviewedit/${pallet.id}/${true}"/>';return false;"/>
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
								<th>Lote</th>											
								<th>Data de Produção</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${stocksOfPallet}" var="stock" varStatus="s">
								<tr class="odd gradeX">
									<td class="toolbar toolbar-btn-link" style="text-align:center;">
										<div class="checkbox">
											<label>
												<c:if test="${stock.checked}">
							                        <input type="checkbox" name="itensSelected[${s.index}]" value="${stock.id}" checked>
							                	</c:if>
							                	<c:if test="${!stock.checked}">
							                        <input type="checkbox" name="itensSelected[${s.index}]" value="${stock.id}">
							                	</c:if>
												
											</label>
										</div>
									</td>
									<td>${stock.company.id}</td>
									<td>${stock.idFormatSerial}</td>
									<td>${stock.product}</td>
									<td>${stock.unitDesc}</td>
									<td>${stock.primaryQty}</td>
									<td>${stock.netFormattedWithDot}</td>
									<td>${stock.batch}</td>											
									<td>${stock.manufactureDateFormat}</td>					
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</section>
					<footer> </footer>
				</div>
			</article>
		</div>

		<!--  
		<display:table class="simple"
		               id="stock" 
		               name="${stocksOfPallet}" 
		               requestURI="/stocks/palletview/${pallet.id}" 
		               style="width: 100%" 
		               cellpadding="4"
		               pagesize="500"
		               decorator="br.com.cardeal.decorator.TableOfBoxOfPalletsDecorator">		               
		               >
		    
		    <display:column property="check" title=" "/>    
			<display:column property="company" title="Filial"/>
			<display:column property="id" title="Nr.Série"/>
			<display:column property="product" title="Produto"/>
			<display:column property="unitDesc" title="Unidade"/>
			<display:column property="primaryQty" title="Peças"/>
			<display:column property="net" title="Peso (kg)"/>
			<display:column property="batch" title="Lote"/>
			<display:column property="manufactureDateFormat" title="Produção"/>			
		        
		</display:table>
		-->
		
	</form>	

</div>

<%@ include file="../annex/footer.jsp" %> 
		