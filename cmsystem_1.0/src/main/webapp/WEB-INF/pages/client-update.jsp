<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>
		
		<div class="content-area" style="justify-content: center">
		
				<div class="form-wrapper" id="updateClientCard" 
					 style="width: 30%;height: fit-content; background-color:#FEFEFE">
					<div class="form-title">
						<h1 style="font-size: 2.4rem">Alterar Dados do Cliente</h1>
					</div>
					<form action="${pageContext.request.contextPath}/clients"
						method="get" id="client-form">
						<input type="hidden" name="action" value="updateClient" >
						<input type="hidden" name="clientID" value="${client.id}" >
						<div class="form-row">
							<label for="new-clientName" class="">Nome do cliente</label> 
							<input type="text" name="new-clientName" id="new-clientName" value="${client.name}" required/>
						</div>
						<div class="form-row">
							<label for="clientCPF" class="">CPF do cliente</label> 
							<input type="text" name="new-clientCPF" id="clientCPF" value="${client.cpf}" />
								<span id="message2"></span>
						</div>
						<div class="form-row">
							<label for="new-phoneNumber" class="">Telefone</label> <input
								type="text" name="new-phoneNumber" id="new-phoneNumber" value="${client.phoneNumber}" required/>
						</div>
						<div class="form-row">
							<label for="new-clientEmail" class="">E-mail</label> <input
								type="text" name="new-clientEmail" id="new-clientEmail"  value="${client.email}" required/>
						</div>
						<div class="form-row">
							<label for="new-clientAdress" class="">Endereço</label> <input
								type="text" name="new-clientAdress" id="new-clientAdress"  value="${client.adress}"required/>
						</div>
						<br>
					<div class="button-area">
						<input type="submit" class="btn-1" id="save-new-client-btn" value="Atualizar"> 
					</div>
					</form>
				</div>
		</div>
		
	</div>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>

</body>
</html>