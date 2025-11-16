<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>

		<form id="loadForm" action="clients" method="get">
			<input type="hidden" name="action" value="login">
		</form>

		<div class="content-area">
			<div class="register-area">
				<div class="title-area">
					<h1 class="main-title">CADASTROS</h1>
				</div>
				<div class="box-buttons" style="gap: 0.5rem;">
					<button class="btn-1" id="openClient">Cadastro de Clientes</button>
					<button class="btn-1" id="openUser">Cadastro de Usuários</button>
					<button class="btn-1" id="openActivateClients">Clientes Inativos</button>
				</div>

				<!-- CLIENT DIALOG -->
				<div class="form-wrapper" id="clientCard">
					<div class="form-title">
						<h1>Cadastro de Clientes</h1>
					</div>
					<form action="${pageContext.request.contextPath}/clients"
						method="POST" id="client-form">
						<div class="form-row">
							<label for="clientName" class="">Nome do cliente</label> <input
								type="text" name="clientName" id="clientName" required/>
						</div>
						<div class="form-row">
							<label for="clientCPF" class="">CPF do cliente</label> 
							<input type="text" name="clientCPF" id="clientCPF" placeholder="apenas números" />
								<p id="message2"></p>
						</div>
						<div class="form-row">
							<label for="phoneNumber" class="">Telefone</label> <input
								type="text" name="phoneNumber" id="phoneNumber" required/>
						</div>
						<div class="form-row">
							<label for="clientEmail" class="">E-mail</label> <input
								type="text" name="clientEmail" id="clientEmail" />
						</div>
						<div class="form-row">
							<label for="clientAdress" class="">Endereço</label> <input
								type="text" name="clientAdress" id="clientAdress" required/>
						</div>
						<br>
					</form>
					<div class="button-area">
						<button class="btn-1" id="save-client-button">Salvar</button> 
					</div>
				</div>

				<!-- USER DIALOG -->
				<div class="form-wrapper" id="userCard">
					<div class="form-title">
						<h1>Cadastro de Usuários</h1>
					</div>
					<form id="formUser" method="post">
						<div class="form-row">
							<label for="userName" class="">Nome</label> <input type="text"
								name="userName" id="userName" />
						</div>
						<div class="form-row">
							<label for="userLogin" class="">Login</label> <input type="text"
								name="userLogin" id="userLogin" />
						</div>
						<div class="form-row">
							<label for="userPassword" class="">Senha</label> <input
								type="text" name="userPassword" id="userPassword" />
						</div>
						<p id="message"></p>
						<div class="form-row">
							<label for="confirmPassword" class="">Confirme a Senha</label> <input
								type="text" name="confirmPassword" id="confirmPassword" />
						</div>
						<br>
					</form>
					<div class="button-area">
						<input type="button" class="btn-1" id="save-user-button" value="Salvar">
					</div>
				</div>
<!-- 				REATIVAR CLIENTES(FORNECEDORES) DIALOG -->
				<div id="activeClients">
					<div class="form-wrapper" style="width: 100%">
						<form action="${pageContext.request.contextPath}/clients">
							<input type="hidden" name="action" value="include"> 
							<select class="select" name="clientId" style="padding: 2px 10px;">
								<c:forEach var="client" items="${inactiveClients}">
									<option class="option" value="${client.id}">${client.name}</option>
								</c:forEach>
								<c:if test="${empty inactiveClients}">
									<option>Clientes não encontrados.</option>
								</c:if>
							</select> 
							<input type="submit" id="activateBtn" value="Ativar">
						</form>
					</div>
				</div>
				
			</div>
			<div class="action-container">
				<h1 class="main-title" style="margin-bottom: 1rem;">LISTA DE CLIENTES</h1>
				
				<div id="filter">
					<form id="searchForm" action="" method="get" style="margin-bottom: 1.2rem;">
						<div id="filter-name-box" >
							<button class="btn-3">Nome</button>
							<input type="text" placeholder="Buscar por nome" name="searchName">
							<input type="hidden" name="action" value="search">
						</div>
					</form>
				</div>

				<div class="table-wrapper">

					<table id="stock-data">
						<tr>
							<th>Cliente</th>
							<th>CPF</th>
							<th>Telefone</th>
							<th>E-mail</th>
							<th>Endereço</th>
							<th></th>
						</tr>
						<c:if test="${not empty clients}">
							<c:forEach var="client" items="${clients}">
								<tr class="tr">
									<td>${client.name}</td>
									<td>${client.cpf}</td>
									<td>${client.phoneNumber}</td>
									<td>${client.email}</td>
									<td>${client.adress}</td>
									<td style="width: 120px; text-align: center">
										<a style="margin-right: 1rem" href="${pageContext.request.contextPath}/clients?action=getClient&clientId=${client.id}">
											<img width="25" src="${pageContext.request.contextPath}/assets/editar-30.png">
										</a>
										<a onclick="return confirmExclude(event)" href="${pageContext.request.contextPath}/clients?action=exclude&clientId=${client.id}">
											<img width="30" src="${pageContext.request.contextPath}/assets/lixo-45.png">
										</a>
									</td>
								</tr>
							</c:forEach>
						</c:if>
					</table>
				</div>
				<c:if test="${empty clients}">
					<br>
					<h3 id="data-info">Os dados não foram encontrados na sessão.</h3>
				</c:if>
			</div>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script type="text/javascript"> const SITE_ROOT = "${pageContext.request.contextPath}"</script>
	<script src="${pageContext.request.contextPath}/scripts/home.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
</body>
</html>