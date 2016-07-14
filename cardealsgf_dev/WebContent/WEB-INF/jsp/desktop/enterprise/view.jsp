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
					<h2>Cadastro de Empresas</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value="/enterprise"/>"> Sair</a></li>
					</ul>
				</header>
				<section>
				<c:if test="${empty enterprise}">
					<form class="form-horizontal" action="<c:url value="/enterprise/add"/>" method="post" style="margin:0">
				</c:if>
				<c:if test="${not empty enterprise}">
					<form class="form-horizontal" action="<c:url value="/enterprise/update"/>" method="post" style="margin:0">
				</c:if>
				<div class="row">						
					<div class="col-md-8 col-md-offset-2">
						<div class="form-group">
							<label for="enterprise.id" class="col-sm-3 control-label">ID</label>
							<div class="col-sm-6">							
							 	<input class="form-control" type="text" name="enterprise.id" id="enterprise.id" value="${enterprise.id}" readonly />							
							</div>
						</div>
						<div class="form-group">
							<label for="enterprise.name" class="col-sm-3 control-label">Nome</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="80" name="enterprise.name" id="enterprise.name"  value="${enterprise.name}" />
							</div>
						</div>
						
						<h2>Logística da Embalagem - SSCC</h2>
						<div class="form-group">
							<div class="form-group">
								<label for="enterprise.maxIdSequenceLogisticBox" class="col-sm-3 control-label">Sequencial Maximo - Embalagem</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" name="enterprise.maxIdSequenceLogisticBox" id="enterprise.maxIdSequenceLogisticBox"  value="${enterprise.maxIdSequenceLogisticBox}" onkeypress="return onlyInteger(event);" />
								</div>
							</div>
							<div class="form-group">
								<label for="enterprise.currentVarLogisctBox" class="col-sm-3 control-label">Variável Logística Atual - Embalagem</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" name="enterprise.currentVarLogisctBox" id="enterprise.currentVarLogisctBox"  value="${enterprise.currentVarLogisctBox}" onkeypress="return onlyInteger(event);" />
								</div>
							</div>
							<div class="form-group">
								<label for="enterprise.currentIdBaseBox" class="col-sm-3 control-label">Sequência Logística Atual - Embalagem</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" name="enterprise.currentIdBaseBox" id="enterprise.currentIdBaseBox"  value="${enterprise.currentIdBaseBox}" onkeypress="return onlyInteger(event);" />
								</div>
							</div>
						</div>
							
						<h2>Logística do Pallet - SSCC</h2>
						<div class="form-group">
							<div class="form-group">
								<label for="enterprise.maxIdSequenceLogisticPallet" class="col-sm-3 control-label">Sequencial Maximo Pallet</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" name="enterprise.maxIdSequenceLogisticPallet" id="enterprise.maxIdSequenceLogisticPallet"  value="${enterprise.maxIdSequenceLogisticPallet}" onkeypress="return onlyInteger(event);" />
								</div>
							</div>
							<div class="form-group">
								<label for="enterprise.currentVarLogisctPallet" class="col-sm-3 control-label">Variável Logística Atual - Palete</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" name="enterprise.currentVarLogisctPallet" id="enterprise.currentVarLogisctPallet"  value="${enterprise.currentVarLogisctPallet}" onkeypress="return onlyInteger(event);" />
								</div>
							</div>
							<div class="form-group">
								<label for="enterprise.currentIdBasePallet" class="col-sm-3 control-label">Sequência Logística Atual - Palete</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" name="enterprise.currentIdBasePallet" id="enterprise.currentIdBasePallet"  value="${enterprise.currentIdBasePallet}" onkeypress="return onlyInteger(event);" />
								</div>
							</div>
														
							<div class="form-group">
								<div class="col-sm-3 control-label"></div>								
								<div class="col-sm-6">
									<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
									<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/enterprise"/>';return false;">Cancelar</button>					
								</div>
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