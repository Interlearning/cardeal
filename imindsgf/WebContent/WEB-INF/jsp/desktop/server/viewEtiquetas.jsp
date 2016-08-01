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
					<h2>Etiquetas - Modelo Padrão</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
					</ul>
				</header>
				<section>
				<form class="form-horizontal" action="<c:url value="/server/update"/>" method="post">
				<div class="row">						
					<div class="col-md-8 col-md-offset-2">
						
						<div class="form-group">
								<label for="server.defaultLabelBox.id" class="col-sm-3 control-label">Modelo - Etiqueta Caixa</label>
								<div class="col-sm-3">
									<select class="form-control" name="server.defaultLabelBox.id">
										<option value="" selected> </option> <!-- Solicitado por Antonio em 27-04-2015 -->	
						                <c:forEach items="${labelsBox}" var="labelPathBox">
						                	<c:if test="${server.defaultLabelBox.id == labelPathBox.id}">
						                        <option value="${labelPathBox.id}" selected>${labelPathBox}</option>
						                	</c:if>
						                	<c:if test="${server.defaultLabelBox.id != labelPathBox.id}">
						                        <option value="${labelPathBox.id}">${labelPathBox}</option>
						                	</c:if>
						                </c:forEach>
						       		</select>
								</div>
							</div>
							<div class="form-group">
								<label for="server.defaultLabelPallet.id" class="col-sm-3 control-label">Modelo - Etiqueta Pallet</label>
								<div class="col-sm-3">
									<select class="form-control" name="server.defaultLabelPallet.id">
										<option value="" selected> </option> <!-- Solicitado por Antonio em 27-04-2015 -->	
						                <c:forEach items="${labelsPallet}" var="labelPathPallet">
						                	<c:if test="${server.defaultLabelPallet.id == labelPathPallet.id}">
						                        <option value="${labelPathPallet.id}" selected>${labelPathPallet}</option>
						                	</c:if>
						                	<c:if test="${server.defaultLabelPallet.id != labelPathPallet.id}">
						                        <option value="${labelPathPallet.id}">${labelPathPallet}</option>
						                	</c:if>
						                </c:forEach>
						       		</select>
								</div>
							</div>
						
						<div class="form-group">
							<div class="col-sm-6">
								<input type="hidden" class="form-control" name="server.labelsDirectory" id="server.labelsDirectory"  value="${server.labelsDirectory}" />
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-6">
								<input type="hidden" class="form-control" name="server.storeDirectory" id="server.storeDirectory"  value="${server.storeDirectory}" />
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-6">
								<input type="hidden" class="form-control" name="server.readDirectory" id="server.readDirectory"  value="${server.readDirectory}" />
							</div>
						</div>
						
					</div>
				</div>		
				<div class="row">
					<div class="col-md-8 col-md-offset-2">
						<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
						<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/"/>';return false;">Cancelar</button>					
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
