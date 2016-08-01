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
					<h2>Cadastro de Unidade de Embalagem</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value='/units'/>"> Sair</a></li>
					</ul>
				</header>
				<section>
					<c:if test="${empty unit.id}">
						<form class="form-horizontal" action="<c:url value="/units/add"/>" method="post" style="margin:0">
					</c:if>
					<c:if test="${not empty unit.id}">
					  	<form class="form-horizontal" action="<c:url value="/units/update"/>" method="post" style="margin:0">
					</c:if>
					<div class="row">						
						<div class="col-md-8 col-md-offset-2">
							<div class="form-group">
								<label for="unit.id" class="col-sm-3 control-label">Código</label>
								<div class="col-sm-6">
									<c:if test="${empty unit}">
								  		<input class="form-control" type="text" maxlength="2" name="unit.id" id="unit.id" value="${unit.id}" />
								  	</c:if>
								  	<c:if test="${not empty unit}">
								  		<input class="form-control" type="text" maxlength="2" name="unit.id" id="unit.id" value="${unit.id}" readonly/>
								  	</c:if>
								</div>
							</div>
							<div class="form-group">
								<label for="unit.description" class="col-sm-3 control-label">Nome</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" maxlength="20" name="unit.description" id="unit.description"  value="${unit.description}" />
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-sm-3 control-label"></div>								
								<div class="col-sm-6">
									<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
									<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/units"/>';return false;">Cancelar</button>					
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
				
<%-- <div class="ui">

<h2></h2>

<p>
<c:if test="${empty unit.id}">
  <form action="<c:url value="/units/add"/>" method="post" style="margin:0">
</c:if>
<c:if test="${not empty unit.id}">
  <form action="<c:url value="/units/update"/>" method="post" style="margin:0">
</c:if>

  <table width="79%" border="0" cellpadding="0" cellspacing="2" class="tabela_borda">    
    <tr>
	  <td width="18%"><label for="unit.id"><strong>Código</strong></label></td>
	  <c:if test="${empty unit}">
	  	<td><input type="text" name="unit.id" id="unit.id" value="${unit.id}" /></td>
	  </c:if>
	  <c:if test="${not empty unit}">
	  	<td><input type="text" name="unit.id" id="unit.id" value="${unit.id}" readonly/></td>
	  </c:if>
    </tr>
    
	<tr>
		<td><label for="unit.description"><strong>Nome</strong></label></td>
		<td><input type="text" maxlength="8" name="unit.description" id="unit.description"  value="${unit.description}" /></td>
	</tr>
        
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	
	<tr>
		<td></td>
		<td>
			<button type="submit" id="submit">Salvar</button>
			<button type="button" onClick="window.location.href='<c:url value="/units"/>';return false;">Cancelar</button>					
		</td>
	</tr>
  </table>
</form>
</p>

</div> --%>

<%@ include file="../annex/footer.jsp" %> 
