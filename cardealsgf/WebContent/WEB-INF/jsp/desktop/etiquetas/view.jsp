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
					<h2>Cadastro de Etiquetas</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value="/etiquetas"/>"> Sair</a></li>
					</ul>
				</header>
				<section>
				<c:if test="${empty etiqueta}">
					<form class="form-horizontal" action="<c:url value="/etiquetas/add"/>" method="post" style="margin:0">
				</c:if>
				<c:if test="${not empty etiqueta}">
					<form class="form-horizontal" action="<c:url value="/etiquetas/update"/>" method="post" style="margin:0">
				</c:if>
				<div class="row">						
					<div class="col-md-8 col-md-offset-2">
						<div class="form-group">
							<label for="etiqueta.id" class="col-sm-3 control-label">ID</label>
							<div class="col-sm-6">							
							 	<input class="form-control" type="text" name="etiqueta.id" id="etiqueta.id" value="${etiqueta.id}" readonly />							
							</div>
						</div>
						<div class="form-group">
							<label for="etiqueta.nameEtq" class="col-sm-3 control-label">Nome do Arquivo</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="80" name="etiqueta.nameEtq" id="etiqueta.nameEtq"  value="${etiqueta.nameEtq}" />
							</div>
						</div>
						<div class="form-group">
								<label for="etiqueta.modeloEtiqueta" class="col-sm-3 control-label">Modelo da etiqueta</label>
								<div class="col-sm-6">
									<select class="form-control" name="etiqueta.modeloEtiqueta">										
						                <c:forEach items="${modelosEtiquetas}" var="modeloEtiqueta">
						                	<c:if test="${etiqueta.modeloEtiqueta == modeloEtiqueta}">
						                        <option value="${modeloEtiqueta}" selected>${modeloEtiqueta}</option>
						                	</c:if>
						                	<c:if test="${etiqueta.modeloEtiqueta != modeloEtiqueta}">
						                        <option value="${modeloEtiqueta}">${modeloEtiqueta}</option>
						                	</c:if>
						                </c:forEach>
		 							</select>
								</div>
							</div>
						<div class="form-group">
							<label for="etiqueta.etqEspPaoAcucar" class="col-sm-3 control-label">Modelo é específica Pão de Açucar ?</label>
							<div class="col-sm-1">
								<input type="checkbox" class="form-control" name="etiqueta.etqEspPaoAcucar" id="etiqueta.etqEspPaoAcucar"  value="true" <c:if test="${etiqueta.etqEspPaoAcucar}">checked</c:if>/>
							</div>
						</div>
						<div class="form-group">
							<label for="etiqueta.blockTara" class="col-sm-3 control-label">Bloqueia edição de Tara na embalagem ?</label>
							<div class="col-sm-1">
								<input type="checkbox" class="form-control" name="etiqueta.blockTara" id="etiqueta.blockTara"  value="true" <c:if test="${etiqueta.blockTara}">checked</c:if>/>
							</div>
						</div>								
						<div class="form-group">
							<div class="col-sm-3 control-label"></div>								
							<div class="col-sm-6">
								<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
								<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/etiquetas"/>';return false;">Cancelar</button>					
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