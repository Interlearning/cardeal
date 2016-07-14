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
					<h2>Configuração do Sistema</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>
					</ul>
				</header>
				<section>
				<form class="form-horizontal" action="<c:url value="/server/update"/>" method="post">
				<div class="row">						
					<div class="col-md-8 col-md-offset-2">
						
						<div class="form-group">
							<label for="server.labelsDirectory" class="col-sm-3 control-label">Diretório de etiquetas (Layouts)</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" maxlength="255" name="server.labelsDirectory" id="server.labelsDirectory"  value="${server.labelsDirectory}" required="required"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="server.storeDirectory" class="col-sm-3 control-label">Diretório de gravação (mapeado)</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" maxlength="255" name="server.storeDirectory" id="server.storeDirectory"  value="${server.storeDirectory}" required="required"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="server.readDirectory" class="col-sm-3 control-label">Diretório de leitura (mapeado)</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" maxlength="255" name="server.readDirectory" id="server.readDirectory"  value="${server.readDirectory}" required="required"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="server.storeDirectoryFull" class="col-sm-3 control-label">Diretório de gravação absoluto</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" maxlength="255" name="server.storeDirectoryFull" id="server.storeDirectoryFull"  value="${server.storeDirectoryFull}" required="required"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="server.readDirectoryFull" class="col-sm-3 control-label">Diretório de leitura absoluto</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" maxlength="255" name="server.readDirectoryFull" id="server.readDirectoryFull"  value="${server.readDirectoryFull}" required="required"/>
							</div>
						</div>
						
						<c:if test="${not empty server.defaultLabelBox}">
							<div class="form-group">
								<div class="col-sm-6">
									<input type="hidden" class="form-control" name="server.defaultLabelBox.id" id="server.defaultLabelBox.id"  value="${server.defaultLabelBox.id}" required="required"/>
								</div>
							</div>
						</c:if>
							
						<c:if test="${not empty server.defaultLabelPallet}">						
							<div class="form-group">
								<div class="col-sm-6">
									<input type="hidden" class="form-control" name="server.defaultLabelPallet.id" id="server.defaultLabelPallet.id"  value="${server.defaultLabelPallet.id}" required="required"/>
								</div>
							</div>
						</c:if>
						
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
