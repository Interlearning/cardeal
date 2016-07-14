<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Main page header -->
<header id="header" class="container">

	<h1>
		<!-- Main page logo -->
		<a href="/cardealsgf_dev/home/login">SGF</a>

		<!-- Main page headline -->
		<span>Sistema Gerenciador de Frigoríficos</span>
	</h1>

	<!-- User profile -->
	<div class="user-profile">
		<figure>

			<!-- User profile info -->
			<figcaption>
				<c:if test="${empty user}">
					<strong><a href="#">${currentUser.name}</a></strong>
				</c:if>
				<c:if test="${not empty user}">
					<strong><a href="#">${user.name}</a></strong>
				</c:if>
				<ul>
					<!-- <li><a href="#" title="Message inbox">inbox</a></li>
					<li><a href="#" title="Account settings">settings</a></li> -->
					<li><a href="<c:url value='/logout'/>" title="Logout">logout</a></li>
				</ul>
			</figcaption>
			<!-- /User profile info -->

		</figure>
	</div>
	<!-- /User profile -->

	<div id="navigation"></div>

</header>
<!-- /Main page header -->