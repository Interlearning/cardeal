<%@ include file="../annex/header.jsp" %> 
<script type="text/javascript" src="<c:url value='/resources/js/mascaras.js'/>" ></script>

<!-- ----------------------------------------------------------------- -->
<!-- SCRIPT DE TRATAMENTO DE INSERT E REMOVE DE ITEMS DO PEDIDO        -->
<!-- ----------------------------------------------------------------- -->

<script type="text/javascript">

	//----------------------------------------
	//utilitários
	//----------------------------------------
	function waitCursor(){
		$("body").css("cursor", "wait");
	}
	function defaultCursor(){
		$("body").css("cursor", "default");
	}

	//----------------------------------------
	//variaveis globais
	//----------------------------------------
	var id_cont = 0;


	//----------------------------------------
	//funções
	//----------------------------------------
	$(function() {

		fillProdDesc();
		fillPartnerName();

		function fillProdDesc() {
			$(".productId").unbind("blur");
			$(".productId").bind(
				"blur",
				function() {
					var id = $(this).attr('id');
					var sepPos = id.lastIndexOf("_");
					var idSuffix = id.substr(sepPos + 1, id.length - sepPos - 1);
					var idOfDesc = "product_desc_"  + idSuffix;
					var prodId = $(this).val();

					var fullurl = '<c:url value="/products/search"/>';
					waitCursor();
					$.ajax({
						type : 'POST',
						url : fullurl,
						data : 'id=' + prodId,
						success : function(data) {
							defaultCursor();		
							$("#" + idOfDesc).val(data.string);
							return true;
						}
					});
				}
			);
		}

		function fillPartnerName() {
			$("#order\\.partner\\.id").unbind("blur");
			$("#order\\.partner\\.id").bind(
				"blur",
				function() {
					var partnerId = $(this).val();
					var fullurl = '<c:url value="/partners/search"/>';
					waitCursor();
					$.ajax({
						type : 'POST',
						url : fullurl,
						data : 'id=' + partnerId,
						success : function(data) {
							defaultCursor();		
							$("#order\\.partner\\.name").val(data.string);
							return true;
						}
					});
				}
			);
		}

		function removeCampo1() {
			$(".removerCampo1").unbind("click");
			$(".removerCampo1").bind(
					"click",
					function() {
						if ($("tr.linhas1").length > 1) {
							$(this).parent().parent().remove();
						}
						
						//reenumera os itens 
						var count = 1;
						$($("tr.linhas1")).each(function(){
							$(this).find("input:first").val(count);
							count++;
						});						
					});
		}

		$(".adicionarCampo1").click(function() {
			//gera um novo contador de id
			id_cont++;
			
			//faz uma copia da primeira linha
			linhas1 = $("tr.linhas1").size();
			novoCampo = $("tr.linhas1:first").clone();

			//inicializa valores dos campos da nova linha
			novoCampo.find("input").val("");
			novoCampo.find("input:first").val(linhas1 + 1);

			//altera o id dos campos da nova linha
	        novoCampo.find('input,select').each(function(i, el) {
	            var id = $(el).attr('id');
	            var sepPos = id.lastIndexOf("_");
	            var newIdPrefix = id.substr(0, sepPos);
	            var newIdSuffix = id_cont;
	            var newId = newIdPrefix + "_" + newIdSuffix;
	            $(el).attr('id', newId);
	        }); 
			

			novoCampo.insertAfter("tr.linhas1:last");
			removeCampo1();
			fillProdDesc();
		});
		
	});
</script>

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
					<h2>Cadastro de Pedido</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/orders'/>"> Sair</a></li>
					</ul>
				</header>
				<section>
				<c:if test="${empty order}">
					<form class="form-horizontal" action="<c:url value="/orders/add"/>" method="post" style="margin:0">
				</c:if>
				<c:if test="${not empty order}">
					<form class="form-horizontal" action="<c:url value="/orders/update"/>" method="post" style="margin:0">
				</c:if>
				<div class="row">						
					<div class="col-md-8 col-md-offset-2">
						<h4>DADOS DO PEDIDO</h4>
						<div class="form-group">
							<label for="order.id" class="col-sm-3 control-label">Número</label>
							<div class="col-sm-6">
								<c:if test="${empty order}">
								  	<input type="text" class="form-control" name="order.id" id="order.id" value="${order.id}" readonly/>
								  </c:if>
								  <c:if test="${not empty order}">
								  	<input type="text" class="form-control" name="order.id" id="order.id" value="${order.id}" readonly/>
								  </c:if>				
							</div>
						</div>
						<div class="form-group">
							<label for="order.id" class="col-sm-3 control-label">Cliente</label>
							<div class="col-lg-2">
								<input type="text" class="form-control" maxlength="6" name="order.partner.id" id="order.partner.id"  value="${order.partner.id}" onkeypress="return onlyInteger(event);" />
							</div>
							<div class="col-lg-4">
								<input type="text" class="form-control" name="order.partner.name" id="order.partner.name"  value="${order.partner.name}"  readonly />
							</div>
						</div>
						
						<br>
						<h4>
							ITENS DO PEDIDO &nbsp; &nbsp;
							<a href="#" class='btn btn-md btn-info adicionarCampo1'><span class="elusive icon-plus"></span></a>
						</h4>
						 						 
						<c:if test="${not empty order.items}"> 
							<div class="table-responsive">
								<table class="table table-hover order_items" id="order_items">
									<thead>
										<tr>
											<th width="10%">Nr.</th>
											<th width="20%">Produto</th>
											<th width="35%">Descrição</th>
											<th width="15%">Qtde.</th>
											<th width="15%">Unidade</th>
											<th></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${order.items}" var="item" varStatus="count">
											<tr class="linhas1">
												<td>
													<input class="form-control" type="text" id="index_${count}" name="order.items[].index" value="${item.index}" readonly/>
													<input type="hidden" id="id_${count}" name="order.items[].id" value="${item.id}"/>
												</td>
												<td>
													<input class="form-control productId" type="text" id="product_id_${count}" name="order.items[].product.id"  onkeypress="return onlyInteger(event);" value="${item.product.id}" />
												</td>
												<td>
													<input class="form-control" type="text" id="product_desc_${count}" name="order.items[].product.description" placeholder="Descrição" value="${item.product.description}" readonly/>
												</td>
												<td>
													<input class="form-control" type="text" id="setpoint_${count}" name="order.items[].setpoint" value="${item.setpoint}" placeholder="Qtde"/>
												</td>
												<td>
													<select class="form-control"  id="unit_${count}" name="order.items[].unitStyle">
										                <c:forEach items="${units}" var="unit">
										                    <option value="${unit}" <c:if test="${unit eq item.unitStyle}">selected</c:if>>${unit.name}</option>
										                </c:forEach>
										 			</select>
												</td>
												<td>
													<a href="#" class='btn btn-md btn-info removerCampo1'><span class="elusive icon-remove"></span></a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>						
						</c:if>
						 
						 
						
						<c:if test="${empty order.items}"> 
							<div class="table-responsive">
								<table class="table table-hover order_items" id="order_items">
									<thead>
										<tr>
											<th width="10%">Nr.</th>
											<th width="20%">Produto</th>
											<th width="35%">Descrição</th>
											<th width="15%">Qtde.</th>
											<th width="15%">Unidade</th>
											<th></th>
										</tr>
									</thead>
									<tbody>
										<tr class="linhas1">
											<td>
												<input class="form-control" type="text" id="index_0" name="order.items[].index" value="1" readonly/>
												<input type="hidden" id="id_0" name="order.items[].id" value="0"/>
											</td>
											<td>
												<input class="form-control productId" type="text" id="product_id_0" name="order.items[].product.id"  onkeypress="return onlyInteger(event);" value="${item.product.id}" />
											</td>
											<td>
												<input class="form-control" type="text" id="product_desc_0" name="order.items[].product.description" placeholder="Descrição" readonly/>
											</td>
											<td>
												<input class="form-control" type="text" id="setpoint_0" name="order.items[].setpoint" placeholder="Qtde"/>
											</td>
											<td>
												<select class="form-control"  id="unit_0" name="order.items[].unitStyle">
									                <c:forEach items="${units}" var="unit">
									                    <option value="${unit}">${unit.name}</option>
									                </c:forEach>
									 			</select>
											</td>
											<td>
												<a href="#" class='btn btn-md btn-info removerCampo1'><span class="elusive icon-remove"></span></a>
											</td>
										</tr>
									</tbody>
								</table>
							</div>						
						</c:if>
					</div>
				</div>
				<br>
				<br>	
				<div class="row">
					<div class="col-md-8 col-md-offset-2">
						<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
						<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/orders"/>';return false;">Cancelar</button>					
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
