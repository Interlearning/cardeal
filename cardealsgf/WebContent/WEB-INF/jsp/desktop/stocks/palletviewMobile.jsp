<%@ include file="../annex/header.jsp" %> 

<script type="text/javascript" src="<c:url value='/resources/js/jquery.ui.datepicker-pt-BR.js'/>"></script>					
<script type="text/javascript" src="<c:url value='/resources/js/jquey-ui-timepicker-addon.js'/>"></script>					



<div class="ui">

<h2>Listagem de Caixas do Pallet ${pallet.id}</h2>

<section>

	<form class="form-horizontal" name="form_search" method="post" action="<c:url value='#'/>" >
	
		<div class="form-group">
			<div class="col-sm-6">
				<button type="button" class="btn btn-md btn-inverse pull-center" onclick="window.history.back();">Sair</button>
			</div>
		</div>						
	
		<div class="row">						
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.id" class="col-sm-3 control-label">Nr. Série</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.id" id="pallet.id"  value="${pallet.idFormatted}" readonly/>
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.company.id" class="col-sm-3 control-label">Filial</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.company.id" id="pallet.company.id"  value="${pallet.company.id}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.terminal.idTerminal" class="col-sm-3 control-label">Terminal</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.terminal.idTerminal" id="pallet.terminal.idTerminal"  value="${pallet.terminal.idTerminal}" readonly />
					</div>
				</div>
			</div>
				
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.product.idMasc" class="col-sm-3 control-label">Produto</label>
				<div class="col-sm-6">
					<div class="col-sm-6">	
						<input type="text" class="form-control" name="pallet.product.idMasc" id="pallet.product.idMasc"  value="${pallet.product.idMasc}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.secondaryQty" class="col-sm-3 control-label">Caixas</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.secondaryQty" id="pallet.secondaryQty"  value="${pallet.secondaryQty}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.primaryQty" class="col-sm-3 control-label">Peças</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.primaryQty" id="pallet.primaryQty"  value="${pallet.primaryQty}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.net" class="col-sm-3 control-label">Peso Líquido (kg)</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.net" id="pallet.net"  value="${pallet.netFormatted}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.openDateDesc" class="col-sm-3 control-label">Iniciado em</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.openDateDesc" id="pallet.openDateDesc"  value="${pallet.openDateDesc}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.closeDateDesc" class="col-sm-3 control-label">Fechado em</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.closeDateDesc" id="pallet.closeDateDesc"  value="${pallet.closeDateDesc}" readonly />
					</div>
				</div>
			</div>	
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.statusDesc" class="col-sm-3 control-label">Status</label>
				<div class="col-sm-6">
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.statusDesc" id="pallet.statusDesc"  value="${pallet.statusDesc}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.tare" class="col-sm-3 control-label">Tara do Pallet</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.tare" id="pallet.tare"  value="${pallet.tare}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.strech" class="col-sm-3 control-label">Strech</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.strech" id="pallet.strech"  value="${pallet.strech}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.tareCantoneira" class="col-sm-3 control-label">Tara da Cantoneira</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.tareCantoneira" id="pallet.tareCantoneira"  value="${pallet.tareCantoneira}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.tareRack" class="col-sm-3 control-label">Tara do Rack</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.tareRack" id="pallet.tareRack"  value="${pallet.tareRack}" readonly />
					</div>
				</div>
			</div>
			
			<div class="col-md-8 col-md-offset-2">
				<label for="pallet.tarePack" class="col-sm-3 control-label">Tara da Embalagem</label>
				<div class="col-sm-6">				
					<div class="col-sm-6">
						<input type="text" class="form-control" name="pallet.tarePack" id="pallet.tarePack"  value="${pallet.tarePack}" readonly />
					</div>
				</div>
			</div>
				
		</div>
		
	</form>
	
</section>			

<display:table class="simple"
               id="stock" 
               name="${pallet.stocks}" 
               requestURI="/stocks/palletview/${pallet.id}" 
               style="width: 100%" 
               cellpadding="4"
               pagesize="500"
               >			               
		<display:column property="idFormatSerial" title="Nr.Série"/>
		<display:column property="product.description" title="Produto"/>
		<display:column property="primaryQty" title="Peças"/>
		<display:column property="netFormatted" title="Peso (kg)"/>
		<display:column property="batch" title="Lote"/>
		<display:column property="enterDateTimeFormat" title="Entrada"/>
</display:table>

</div>

<%@ include file="../annex/footer.jsp" %> 
