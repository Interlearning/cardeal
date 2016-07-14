<%@ include file="../annex/header.jsp" %> 

<div id="login">
<div class="lost-password">
		
		<!-- Main page container -->
		<section class="container" role="main">
			<div class="row">
			  <div class="col-md-6">
				<!-- Login header -->
				<div class="login-logo">
					<a href="#">SGF</a>
					<h1>Esqueceu a Senha</h1>
				</div>
			<!-- /Login header -->
			  </div>
			  <div class="col-md-6">
				<!-- Login form -->
				<form method="post" action="">
					<p>Por favor, digite seu endereço de e-mail para recuperar sua senha!</p>
						<div class="form-group">
							<label>E-mail</label>
							<input class="form-control" type="email" placeholder="Seu e-mail">
						</div>
						<button class="btn btn-primary" type="submit">Enviar</button>
						<a class="btn btn-primary" href="<c:url value="/home/login"/>">Voltar</a>
				</form>
				<!-- /Login form -->
			  </div>
			</div>
		</section>
		<!-- /Main page container -->

		<!-- Bootstrap scripts -->
		<script src="js/bootstrap/bootstrap.min.js"></script>
		
	</div>
</div>

<%@ include file="../annex/footer.jsp" %> 