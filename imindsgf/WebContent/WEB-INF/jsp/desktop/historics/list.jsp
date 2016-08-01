<%@ include file="../annex/header.jsp" %> 

<script type="text/javascript" src="<c:url value='/resources/js/jquery.ui.datepicker-pt-BR.js'/>"></script>					
<script type="text/javascript" src="<c:url value='/resources/js/jquey-ui-timepicker-addon.js'/>"></script>

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
					<h2>Histórico de processos</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/'/>"> Sair</a></li>						
					</ul>
				</header>
				<section>
					<form class="form-horizontal" role="form" name="form_search" 
						  id="form_search" 
						  method="post" action="<c:url value='/historics/list'/>" >
						<div class="row">						
							<div class="col-md-8 col-md-offset-2">
									
								<div class="form-group">
									<label for="dateBegin" class="col-sm-3 control-label">Date de:</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#dateBegin" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" 
												id="dateBegin" name="dateBegin" 
												value="${dateBegin}"/>
									</div>
								</div>
								
								<div class="form-group">
									<label for="dateEnd" class="col-sm-3 control-label">Date Ate:</label>
									<div class="col-sm-6">
										<script>
											$(function() {
												$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
												$( "#dateEnd" ).datetimepicker( $.datepicker.regional[ "pt-BR" ] );
											});
										</script>
										<input type="text" class="form-control" id="dateEnd" 
											   name="dateEnd" value="${dateEnd}"/>
									</div>
								</div>
								
								<div class="form-group">
									<label for="filter.user.id" class="col-sm-3 control-label">Id Usuário</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" name="filter.user.id" id="filter.user.id" value="${filter.user.id}" />
									</div>
								</div>
																		
							</div>
						</div>
						<div class="row">
						<div class="col-md-8 col-md-offset-2">
								<input type="submit" value="Pesquisar" 
									   class="btn btn-md btn-inverse pull-right"/>							
								<input type="button" id="limpar" value="Limpar"
									   class="btn btn-md btn-default pull-left" />							
							</div>
						</div>
					
					</form>
					
					<!--  filtro para geracao de cvs -->
					<form class="form-horizontal" name="form_search" method="post"
						action="<c:url value='/historics/exportObjHistoricToExcel'/>">
						<c:if test="${not empty historics}">
							<br>
							<hr>
							<input type="hidden" class="form-control" 
									id="dateBegin" name="dateBegin" id="dateBegin" value="${date1}"/>
							<input type="hidden" class="form-control"
									id="dateEnd" name="dateEnd" id="dateEnd" value="${date2}"/>
							<input type="hidden" class="form-control" name="filter.user.id" id="filter.user.id"
								 value="${filter.user.id}" />
							<div class="data-block">
								<div id="history"></div>
								<div id="targetExcel"></div>

							</div>
						</c:if>
					</form>
					
				<br>
				<hr>
				<br>
				<div class="row hidden-xs">
					<article class="col-sm-12">
						<div class="dark data-block">
							<header>
								<h2>Histórico</h2>
							</header>
							<section>
								<table class="datatable table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>Data</th>
											<th>Usuário</th>
											<th>Filial</th>
											<th>Terminal</th>
											<th>Ação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${historics}" var="historic">
											
											<tr class="odd gradeX">
												<td>${historic.dateTimeFormat}</td>
												<td>${historic.user.name}</td>
												<td>${historic.company.id}</td>
												<td>${historic.terminal.id}</td>
												<td>${historic.operation.name}</td>
											</tr>
											
										</c:forEach>
									</tbody>
								</table>

							</section>
						</div>
					</article>
				</div>
				<div class="row visible-xs">
					<article>
						<div class="dark data-block">
							<header>
								<h2>Histórico</h2>
							</header>
							<section>
								<div class="table-responsive">
									<table class="table table-hover">
										<thead>
											<tr>
												<th>Data</th>
												<th>Usuário</th>
												<th>Filial</th>
												<th>Terminal</th>
												<th>Ação</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${historics}" var="historic">
											
												<tr class="odd gradeX">
													<td>${historic.dateTimeFormat}</td>
													<td>${historic.user.name}</td>
													<td>${historic.company.id}</td>
													<td>${historic.terminal.id}</td>
													<td>${historic.operation.name}</td>
												</tr>
											
											</c:forEach>
										</tbody>
									</table>
								</div>		
							</section>
						</div>
					</article>		
				</div>
				</section>
				<footer> </footer>
			</div>
		</article>
		<!-- /Data block -->
	</div>	
</section>

<script type="text/javascript" src="<c:url value='/resources/js/ResetField.js'/>" ></script>
<%@ include file="../annex/footer.jsp" %> 
