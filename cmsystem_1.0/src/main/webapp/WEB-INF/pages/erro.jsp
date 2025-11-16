<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>

<body class="body-login">

	<div class="erro-container" style="margin: 3rem;">
		<h1 class="title-error-page" style="color: #e74c3c">Ocorreu um erro:</h1>
		<p style="font-size: 1.3rem;">${errorMsg}</p>
		<span class="logout-link"> <a
			href="<%=request.getContextPath()%>/logout">Sair</a>
		</span>
		<button class="btn-2" id="back-btn">VOLTAR</button>
	</div>
	<form id="back-form" method="get"> </form>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
	<script src="${pageContext.request.contextPath}/scripts/erro.js"></script>
</body>
</html>