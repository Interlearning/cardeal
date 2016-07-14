<%@ include file="../annex/header.jsp" %> 
<script type="text/javascript" src="<c:url value='/resources/js/mascaras.js'/>" ></script>

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
					<h2>Cadastro de Clientes/Fornecedores</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value="/partners"/>"> Sair</a></li>
					</ul>
				</header>
				<section>
					<c:if test="${empty partner}">
						<form class="form-horizontal" action="<c:url value="/partners/add"/>" method="post" style="margin:0">
					</c:if>
					<c:if test="${not empty partner}">
					  	<form class="form-horizontal" action="<c:url value="/partners/update"/>" method="post" style="margin:0">
					</c:if>
					<div class="row">						
						<div class="col-md-8 col-md-offset-2">
							<div class="form-group">
								<label for="partner.id" class="col-sm-3 control-label">Código</label>
								<div class="col-sm-6">
									<c:if test="${empty partner}">
									  	<input class="form-control" type="text" name="partner.id" id="partner.id" value="${partner.id}" readonly/>
									</c:if>
									<c:if test="${not empty partner}">
										<input class="form-control" type="text" name="partner.id" id="partner.id" value="${partner.id}" readonly/>
									</c:if>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label" for="partner.name">Nome</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" maxlength="100" name="partner.name" id="partner.name"  value="${partner.name}" />
								</div>								
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label" for="partner.formattedCnpj">CNPJ/CPF</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" maxlength="18" name="partner.formattedCnpj" id="partner.formattedCnpj"  value="${partner.formattedCnpj}" onkeypress="formatCpfCnpj(this)"/>
								</div>								
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label" for="partner.formattedCnpj">Código Externo</label>
								<div class="col-sm-6">
									<input class="form-control" type="text" maxlength="20" name="partner.codigoExterno" id="partner.codigoExterno"  value="${partner.codigoExterno}" />
								</div>								
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label" for="partner.partnerStyle">Tipo</label>
								<div class="col-sm-6">
									<select class="form-control" name="partner.partnerStyle">
						                <c:forEach items="${partnerStyles}" var="style">
						                	<c:if test="${partner.partnerStyle == style}">
						                        <option value="${style}" selected>${style.name}</option>
						                	</c:if>
						                	<c:if test="${partner.partnerStyle != style}">
						                        <option value="${style}">${style.name}</option>
						                	</c:if>
						                </c:forEach>
								 	</select>
								</div>								
							</div>							
						</div>
					</div>		
					<div class="row">
						<div class="col-md-4 col-md-offset-4">
							<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
							<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/partners"/>';return false;">Cancelar</button>					
						</div>
					</div>
					</form>
				</section>
				<footer>
				</footer>
			</div>
		</article>
	</div>
</section>				

<%-- <div class="ui">

<h2></h2>

<p>
<c:if test="${empty partner}">
  <form action="<c:url value="/partners/add"/>" method="post" style="margin:0">
</c:if>
<c:if test="${not empty partner}">
  <form action="<c:url value="/partners/update"/>" method="post" style="margin:0">
</c:if>

  <table width="79%" border="0" cellpadding="0" cellspacing="2" class="tabela_borda">    
    <tr>
	  <td width="18%"><label for="partner.id"><strong>Código</strong></label></td>
	  <c:if test="${empty partner}">
	  	<td><input type="text" name="partner.id" id="partner.id" value="${partner.id}" readonly/></td>
	  </c:if>
	  <c:if test="${not empty partner}">
	  	<td><input type="text" name="partner.id" id="partner.id" value="${partner.id}" readonly/></td>
	  </c:if>
    </tr>
    
	<tr>
		<td><label for="partner.name"><strong>Nome</strong></label></td>
		<td><input type="text" maxlength="100" name="partner.name" id="partner.name"  value="${partner.name}" /></td>
	</tr>
        
	<tr>
		<td><label for="partner.formattedCnpj"><strong>CNPJ/CPF</strong></label></td>
		<td><input type="text" maxlength="20" name="partner.formattedCnpj" id="partner.formattedCnpj"  value="${partner.formattedCnpj}" onkeypress="formatCpfCnpj(this)"/></td>
	</tr>
        
	<tr>
		<td><label for="partner.partnerStyle"><strong>Tipo</strong></label></td>
		<td>
		 	<select name="partner.partnerStyle" style="width:202px">
                <c:forEach items="${partnerStyles}" var="style">
                	<c:if test="${partner.partnerStyle == style}">
                        <option value="${style}" selected>${style.name}</option>
                	</c:if>
                	<c:if test="${partner.partnerStyle != style}">
                        <option value="${style}">${style.name}</option>
                	</c:if>
                </c:forEach>
		 	</select>
		</td>
	</tr>
        
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	
	<tr>
		<td></td>
		<td>
			<button type="submit" id="submit">Salvar</button>
			<button type="button" onClick="window.location.href='<c:url value="/partners"/>';return false;">Cancelar</button>					
		</td>
	</tr>
  </table>
</form>
</p>

</div> --%>

<%@ include file="../annex/footer.jsp" %> 
