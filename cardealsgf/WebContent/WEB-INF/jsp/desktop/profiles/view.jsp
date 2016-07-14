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
					<h2>Permissões</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value="/profiles"/>"> Sair</a></li>
					</ul>
				</header>
				<section>
				<form class="form-horizontal" action="<c:url value="/profiles/update"/>" method="post" style="margin:0">
					<div class="row">						
						<div class="col-md-8 col-md-offset-2">
							<div class="form-group">
								<label for="profile.id" class="col-sm-3 control-label">Código</label>
								<div class="col-sm-6">
									<c:if test="${empty profile}">
								  		<input class="form-control" type="text" name="profile.id" id="profile.id" value="${profile.id}" readonly/>
								  	</c:if>
								  	<c:if test="${not empty profile}">
								  		<input class="form-control" type="text" name="profile.id" id="profile.id" value="${profile.id}" readonly/>
								  	</c:if>
								</div>
							</div>
							<div class="form-group">
								<label for="profile.name" class="col-sm-3 control-label">Nome</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" maxlength="80" name="profile.name" id="profile.name"  value="${profile.name}" />
								</div>
							</div>
							
							<div class="form-group">
								<div class="data-block">
									<header><h2>Servidor</h2></header>
									<br>
									<div class="form-group">
										<c:forEach items="${componentesServer}" var="componenteX" varStatus="x">
											<div class="col-sm-12">
												<input type="checkbox" name="rolesX[${x.index}]" value="${componenteX.id}" <c:if test="${componenteX.checked}">checked="true"</c:if> /> ${componenteX.name}
											</div>			                	
										</c:forEach>							
									</div>
								</div>
								<br>
								<div class="data-block">
									<header><h2>Estação</h2></header>
									<br>
									<div class="form-group">
										<c:forEach items="${componentesStation}" var="componenteS" varStatus="s">
											<div class="col-sm-12">
												<input type="checkbox" name="rolesS[${s.index}]" value="${componenteS.id}" <c:if test="${componenteS.checked}">checked="true"</c:if>/> ${componenteS.name}
											</div>			                	
										</c:forEach>							
									</div>
				                	<br>
									<div class="form-group">
										<div class="col-sm-6">
											<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
											<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/profiles"/>';return false;">Cancelar</button>					
										</div>
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
