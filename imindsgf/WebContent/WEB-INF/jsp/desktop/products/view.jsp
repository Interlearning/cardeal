<%@ include file="../annex/header.jsp" %> 

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
					<h2>Cadastro de Produtos</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/products'/>"> Sair</a></li>
					</ul>
				</header>
				<section>					
					<c:if test="${empty product || product.id == 0}">
						<form class="form-horizontal" action="<c:url value="/products/add"/>" method="post" style="margin:0">
					</c:if>
					<c:if test="${not empty product}">
						<form class="form-horizontal" action="<c:url value="/products/update"/>" method="post" style="margin:0">
					</c:if>
					<div class="row">						
						<div class="col-md-8 col-md-offset-2">
							<h5>CÓDIGOS</h5>
							<div class="form-group">
								<label for="product.idMasc" class="col-sm-3 control-label">Código</label>
								<div class="col-sm-6">
									<input type="hidden" class="form-control" name="product.id" id="product.id" value="${product.id}"/>
									<c:if test="${empty product}">
										<input type="text" class="form-control" name="product.idMasc" id="product.idMasc" value="${product.idMasc}" maxlength="4" required="required"/>
									</c:if>
									<c:if test="${not empty product}">
										<input type="hidden" class="form-control" name="creationDate" id="creationDate" value="${product.creationDateTimeFormat}"/>
										<input type="text" class="form-control" name="product.idMasc" id="product.idMasc" value="${product.idMasc}" readonly maxlength="4"/>
									</c:if>
								</div>
							</div>
							<div class="form-group">
								<label for="product.ean13" class="col-sm-3 control-label">EAN13</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="13" name="product.ean13" id="product.ean13"  value="${product.ean13}" onblur="validaEan13(this);" onkeypress="return onlyInteger(event);" />
								</div>
							</div>
							<div class="form-group">
								<label for="product.dun14" class="col-sm-3 control-label">DUN14</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="14" name="product.dun14" id="product.dun14"  value="${product.dun14}" onblur="validaDun14(this);" onkeypress="return onlyInteger(event);" />
								</div>
							</div>
							<div class="form-group">
								<label for="product.blocked" class="col-sm-3 control-label">Bloqueado</label>
								<div class="col-sm-1">
									<input type="checkbox" class="form-control" name="product.blocked" id="product.blocked"  value="true" <c:if test="${product.blocked}">checked</c:if>/>
								</div>
							</div>		
							
							<h5>DESCRIÇÕES</h5>
							<div class="form-group">
								<label for="product.description" class="col-sm-3 control-label">Nome do Produto</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="50" name="product.description" 
											id="product.description"  value="${product.description}" required="required"/>
								</div>							
							</div>
							<div class="form-group">
								<label for="product.qtyCaracteresDesc" class="col-sm-3 control-label">Qtd. Caracteres Na Etiqueta</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="2" name="product.qtyCaracteresDesc" id="product.qtyCaracteresDesc"  value="${product.qtyCaracteresDesc}" onkeypress="return onlyInteger(event);" />
								</div>
							</div>
							<div class="form-group">
								<label for="product.descriptionConservation" class="col-sm-3 control-label">Descrição Da Frase De Conservação</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="58" name="product.descriptionConservation" id="product.descriptionConservation"  value="${product.descriptionConservation}" />
								</div>
							</div>
							<div class="form-group">
								<label for="product.descriptionSif" class="col-sm-3 control-label">Descrição Da Frase Do SIF/DIPOA (MAPA)</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="120" name="product.descriptionSif" id="product.descriptionSif"  value="${product.descriptionSif}" />
								</div>
							</div>
							<div class="form-group">
								<label for="product.codSif" class="col-sm-3 control-label">ISO + 0 + Código SIF</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="11" name="product.codSif" id="product.codSif"  value="${product.codSif}" />
								</div>
							</div>
							<div class="form-group">
								<label for="product.prefixEnterpriseGS1" class="col-sm-3 control-label">Prefixo da empresa - GS1</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" maxlength="8" name="product.prefixEnterpriseGS1" id="product.prefixEnterpriseGS1"  value="${product.prefixEnterpriseGS1}" />
								</div>
							</div>
							<div class="form-group">
								<label for="product.sequenciaProcessProduct" class="col-sm-3 control-label">Sequencial de processo</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" maxlength="1" name="product.sequenciaProcessProduct" id="product.sequenciaProcessProduct"  value="${product.sequenciaProcessProduct}" />
								</div>
							</div>								
							<h5>ESPECIFICAÇÕES</h5>
							<div class="form-group">
								<label for="product.unit.id" class="col-sm-3 control-label">Unidade De Medida Para Venda</label>
								<div class="col-sm-6">
									<select class="form-control" name="product.unit.id" required="required">
										<option value="" selected> </option> <!-- Solicitado por Antonio em 27-04-2015 -->
						                <c:forEach items="${units}" var="unit">
						                	<c:if test="${product.unit.id == unit.id}">
						                        <option value="${unit.id}" selected>${unit.description}</option>
						                	</c:if>
						                	<c:if test="${product.unit.id != unit.id}">
						                        <option value="${unit.id}">${unit.description}</option>
						                	</c:if>
						                </c:forEach>
		 							</select>
								</div>
							</div>
							<div class="form-group">
								<label for="product.unitEtq.id" class="col-sm-3 control-label">Unidade De Medida Para Etiqueta</label>
								<div class="col-sm-6">
									<select class="form-control" name="product.unitEtq.id" required="required">
										<option value="" selected> </option> <!-- Solicitado por Antonio em 27-04-2015 -->
						                <c:forEach items="${units}" var="unit">
						                	<c:if test="${product.unitEtq.id == unit.id}">
						                        <option value="${unit.id}" selected>${unit.description}</option>
						                	</c:if>
						                	<c:if test="${product.unitEtq.id != unit.id}">
						                        <option value="${unit.id}">${unit.description}</option>
						                	</c:if>
						                </c:forEach>
		 							</select>
								</div>
							</div>
							<div class="form-group">
								<label for="product.unitEmb.id" class="col-sm-3 control-label">Unidade De Medida Para Expedição (Embalagem)</label>
								<div class="col-sm-6">
									<select class="form-control" name="product.unitEmb.id" required="required">
										<option value="" selected> </option>
						                <c:forEach items="${units}" var="unit">
						                	<c:if test="${product.unitEmb.id == unit.id}">
						                        <option value="${unit.id}" selected>${unit.description}</option>
						                	</c:if>
						                	<c:if test="${product.unitEmb.id != unit.id}">
						                        <option value="${unit.id}">${unit.description}</option>
						                	</c:if>
						                </c:forEach>
		 							</select>
								</div>
							</div>
							<div class="form-group">
								<label for="product.weighingStyle" class="col-sm-3 control-label">Tipo Pesagem</label>
								<div class="col-sm-6">
								
								<!--  validar  -->
								
									<select class="form-control" name="product.weighingStyle" id="product.weighingStyle" required="required">
										<option value="" selected> </option> <!-- Solicitado por Antonio em 27-04-2015 -->
						                <c:forEach items="${weighingStyles}" var="style">
						                	<c:if test="${product.weighingStyle == style}">
						                        <option value="${style}" selected>${style.name}</option>
						                	</c:if>
						                	<c:if test="${product.weighingStyle != style}">
						                        <option value="${style}">${style.name}</option>
						                	</c:if>
						                </c:forEach>
								 	</select>
								</div>
							</div>
							<div class="form-group">
								<label for="product.percentRemoveProductOnScale" class="col-sm-3 control-label">(%) Peso Libera Balança</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" title="Percentual do peso do produto para liberar balança antes de chegar no 0." maxlength="3" name="product.percentRemoveProductOnScale" id="product.percentRemoveProductOnScale"  value="${product.percentRemoveProductOnScale}" onkeypress="return onlyInteger(event);"/>
								</div>
							</div>
							<div class="form-group">
								<label for="product.percentShipmentComplement" class="col-sm-3 control-label">(%) Complemento na Expedição</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" title="Percentual para liberação do complemento de produto na expedição" maxlength="3" name="product.percentShipmentComplement" id="product.percentShipmentComplement"  value="${product.percentShipmentComplement}" onkeypress="return onlyInteger(event);"/>
								</div>
							</div>
							<!-- DESCONTINUADO
							<div class="form-group">
								<label for="product.stockStyle" class="col-sm-3 control-label">Tipo Estoque</label>
								<div class="col-sm-6">
									<select class="form-control" name="product.stockStyle">
						                <c:forEach items="${stockStyles}" var="style">
						                	<c:if test="${product.stockStyle == style}">
						                        <option value="${style}" selected>${style.name}</option>
						                	</c:if>
						                	<c:if test="${product.stockStyle != style}">
						                        <option value="${style}">${style.name}</option>
						                	</c:if>
						                </c:forEach>
								 	</select>
								</div>
							</div>
							-->
							<!-- DESCONTINUADO
							<div class="form-group">
								<label for="product.materialStyle" class="col-sm-3 control-label">Tipo Estoque</label>
								<div class="col-sm-6">
									<select class="form-control" name="product.materialStyle">										
						                <c:forEach items="${materialStyles}" var="style">
						                	<c:if test="${product.materialStyle == style}">
						                        <option value="${style}" selected>${style.name}</option>
						                	</c:if>
						                	<c:if test="${product.materialStyle != style}">
						                        <option value="${style}">${style.name}</option>
						                	</c:if>
						                </c:forEach>
								 	</select>
								</div>
							</div>
							-->
							<div class="form-group">
									<label for="product.typeStock" class="col-sm-3 control-label">Tipo de Estoque</label>
									<div class="col-sm-6">
										<select name="product.typeStock" class="form-control" id="product.typeStock" required="required">
								        	<c:forEach items="${listTypeStock}" var="type">
								            	<c:if test="${product.typeStock == type}">
								                	<option value="${type}" selected>${type.descricao}</option>
								                </c:if>
								                <c:if test="${product.typeStock != type}">
								                	<option value="${type}">${type.descricao}</option>
								               	</c:if>
								            </c:forEach>
										</select>
									</div>
								</div>	  
							<div class="form-group">
								<label for="etiquetaCaixa" class="col-sm-3 control-label">Modelo da Etiqueta Caixa utilizada </label>
								<div class="col-sm-6">
									<select class="form-control" name="etiquetaCaixa" id="etiquetaCaixa">
										<option value="" selected> </option> <!-- Solicitado por Antonio em 27-04-2015 -->	
						                <c:forEach items="${labelsBox}" var="labelPathBox">
						                	<c:if test="${product.labelFileName.id == labelPathBox.id}">
						                        <option value="${labelPathBox.id}" selected>${labelPathBox}</option>
						                	</c:if>
						                	<c:if test="${product.labelFileName.id != labelPathBox.id}">
						                        <option value="${labelPathBox.id}">${labelPathBox}</option>
						                	</c:if>
						                </c:forEach>
						       		</select>
								</div>
							</div>
							<div class="form-group">
								<label for="etiquetaPallet" class="col-sm-3 control-label">Modelo da Etiqueta Pallet utilizada </label>
								<div class="col-sm-6">
									<select class="form-control" name="etiquetaPallet" id="etiquetaPallet">
										<option value="" selected> </option> <!-- Solicitado por Antonio em 27-04-2015 -->	
						                <c:forEach items="${labelsPallet}" var="labelPathPallet">
						                	<c:if test="${product.labelPalletFileName.id == labelPathPallet.id}">
						                        <option value="${labelPathPallet.id}" selected>${labelPathPallet}</option>
						                	</c:if>
						                	<c:if test="${product.labelPalletFileName.id != labelPathPallet.id}">
						                        <option value="${labelPathPallet.id}">${labelPathPallet}</option>
						                	</c:if>
						                </c:forEach>
						       		</select>
								</div>
							</div>
							<div class="form-group">
								<label for="product.fifoEnabled" class="col-sm-3 control-label">Utiliza Conceito FIFO ?</label>
								<div class="col-sm-1">
									<input type="checkbox" class="form-control" name="product.fifoEnabled" id="product.fifoEnabled"  value="true" <c:if test="${product.fifoEnabled}">checked</c:if>/>
								</div>
							</div>
							<div class="form-group">
								<label for="product.virtual" class="col-sm-3 control-label">Virtual ?</label>
								<div class="col-sm-1">
									<input type="checkbox" class="form-control" name="product.virtual" id="product.virtual"  value="true" <c:if test="${product.virtual}">checked</c:if>/>
								</div>
							</div>
							<div class="form-group">
								<label for="product.changeQtyPecasEnabled" class="col-sm-3 control-label">Permite alterar quantidade de peças ?</label>
								<div class="col-sm-1">
									<input type="checkbox" class="form-control" name="product.changeQtyPecasEnabled" id="product.changeQtyPecasEnabled"  value="true" <c:if test="${product.changeQtyPecasEnabled}">checked</c:if>/>
								</div>
							</div>			
							<div class="form-group">
								<label for="product.changeQtyBoxEnabled" class="col-sm-3 control-label">Permite alterar quantidade de etiquetas ?</label>
								<div class="col-sm-1">
									<input type="checkbox" class="form-control" name="product.changeQtyBoxEnabled" id="product.changeQtyBoxEnabled"  value="true" <c:if test="${product.changeQtyBoxEnabled}">checked</c:if>/>
								</div>
							</div>
							<h5>LIMITES</h5>
							<div class="form-group">
								<label for="product.targetQty" class="col-sm-3 control-label">Quantidade por embalagem</label>
								
								<!--  validar  -->
								
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="3"
									 	name="product.targetQty" id="product.targetQty"  
									 	value="${product.targetQty}"
									 	onkeypress="return onlyInteger(event);"
									  	required="required"/>
								</div>
								
							</div>
							
							<div class="form-group">
								<label for="product.netWeight" class="col-sm-3 control-label">Peso padrão (Venda)</label>
								<div class="col-sm-6">
									<div class="input-group">
										<input type="text" class="form-control peso" maxlength="8" name="product.netWeight"
												 id="product.netWeight"  value="${product.netWeight}"/>
												 <span class="input-group-addon">kg</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="product.targetWeight" class="col-sm-3 control-label">Peso liquido (Etiqueta)</label>
								<div class="col-sm-6">
									<div class="input-group">
										<input type="text" class="form-control peso" 
											maxlength="8" name="product.targetWeight" 
											id="product.targetWeight"  
											value="${product.targetWeight}"/><span class="input-group-addon">kg</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="product.minWeight" class="col-sm-3 control-label">Peso mínimo</label>
								<div class="col-sm-6">
									<div class="input-group">
										<input type="text" class="form-control peso" 
											maxlength="8" name="product.minWeight" id="product.minWeight"  
											value="${product.minWeight}"/><span class="input-group-addon">kg</span>							
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="product.maxWeight" class="col-sm-3 control-label">Peso máximo</label>
								<div class="col-sm-6">
									<div class="input-group">
										<input type="text" class="form-control peso" maxlength="8"
											   name="product.maxWeight" id="product.maxWeight" 
											   value="${product.maxWeight}"/>
											   <span class="input-group-addon">kg</span>
									</div>
								</div>
							</div>
							<h5>EMBALAGEM</h5>	
							<div class="form-group">
								<label for="product.tareEmbala" class="col-sm-3 control-label">Tara (Pacote, Saco, Pote)</label>
								<div class="col-sm-6">
									<div class="input-group">
										<input type="text" class="form-control peso" maxlength="9" name="product.tareEmbala" id="product.tareEmbala"  value="${product.tareEmbala}" /><span class="input-group-addon">kg</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="product.tareBox" class="col-sm-3 control-label">Tara da Caixa</label>
								<div class="col-sm-6">
									<div class="input-group">
										<input type="text" class="form-control peso" maxlength="9" name="product.tareBox" id="product.tareBox"  value="${product.tareBox}"  /><span class="input-group-addon">kg</span>
									</div>
								</div>
							</div>
							<h5>OUTROS DADOS</h5>
							<div class="form-group">
								<label for="product.expirationDays" class="col-sm-3 control-label">Dias de validade</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="4" name="product.expirationDays" id="product.expirationDays"  value="${product.expirationDays}" onkeypress="return onlyInteger(event);"/>
								</div>
							</div>		
							<div class="form-group">
								<label for="product.palletQty" class="col-sm-3 control-label">Qtde. Embalagens para Fechamento do Pallet</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="4" name="product.palletQty" id="product.palletQty"  value="${product.palletQty}" onkeypress="return onlyInteger(event);" />
								</div>
							</div>							
							<div class="form-group">
								<label class="col-sm-3 control-label"></label>
								<div class="col-sm-6">
									<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
									<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/products"/>';return false;">Cancelar</button>					
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

<script type="text/javascript" src="<c:url value='/resources/js/MaskedReplaceCommabyPoint.js'/>"/></script>
<script type="text/javascript" src="<c:url value='/resources/js/ValidateFieldsTypeWeighingWhenQuantityPackingINtozero.js'/>"/></script>
<%@ include file="../annex/footer.jsp" %> 
