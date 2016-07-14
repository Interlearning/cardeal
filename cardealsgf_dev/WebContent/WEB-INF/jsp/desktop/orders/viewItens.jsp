<%@ include file="../annex/header.jsp" %> 

<script type="text/javascript" src="<c:url value='/resources/js/jquery.ui.datepicker-pt-BR.js'/>"></script>					
<script type="text/javascript" src="<c:url value='/resources/js/jquey-ui-timepicker-addon.js'/>"></script>					



<div class="ui">

<h2>Listagem de Itens do Pedido ${order.idPedidoImport}</h2>

<section>

	<form class="form-horizontal" name="form_search" method="post" action="<c:url value='#'/>" >
	
		<div class="form-group">
			<div class="col-sm-6">
				<button type="button" class="btn btn-md btn-inverse pull-center" onclick="window.history.back();">Sair</button>
			</div>
			<!--  
			<br>
			<hr>
			<div class="data-block">
				<header>
					<h2>Opções para Exportação</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/stocks/exportPalletToPdf/${pallet.id}'/>">  PDF  </a></li>
						<li><a href="<c:url value='/stocks/exportPalletToCsv/${pallet.id}'/>"> Excel </a></li>
					</ul>
				</header>						
			</div>					
			-->
		</div>						
	
		<div class="row">						
			
			<div class="col-md-8 col-md-offset-2">
				<label for="order.creationDate" class="col-sm-3 control-label">Data</label>
				<div class="form-group">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="order.creationDate" id="order.creationDate"  value="${order.creationDateFormat}" readonly/>
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="order.partner.id" class="col-sm-3 control-label">Cliente</label>
				<div class="form-group">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="order.partner.id" id="order.partner.id"  value="${order.partner.name}" readonly />
					</div>
				</div>
			</div>
				
			<div class="col-md-8 col-md-offset-2">
				<label for="order.status.id" class="col-sm-3 control-label">Status</label>
				<div class="form-group">
					<div class="col-sm-6">	
						<input type="text" class="form-control" name="order.status.id" id="order.status.id"  value="${order.status.name}" readonly />
					</div>
				</div>
			</div>
				
		</div>
		
	</form>
	
</section>			

<display:table class="simple"
               id="order" 
               name="${order.itemsCleaning}" 
               requestURI="/orders/viewItens/${order.id}" 
               style="width: 100%" 
               cellpadding="4"
               pagesize="500"
               >			               
		<display:column property="index" title="Item"/>
		<display:column property="product" title="Produto"/>
		<display:column property="qtyRequestedFormatted" title="Qtd. Solicitada"/>
		<display:column property="unit.id" title="Und. Solicitada"/>
		<display:column property="qtyIssuedFormatted" title="Qtd. Expedida"/>
		<display:column property="qtdMissingFormatted" title="Qtd. Faltante"/>
		<display:column property="company.id" title="Filial"/>
		<display:column property="terminal.idTerminal" title="Terminal"/>		
</display:table>

</div>

<%@ include file="../annex/footer.jsp" %> 
