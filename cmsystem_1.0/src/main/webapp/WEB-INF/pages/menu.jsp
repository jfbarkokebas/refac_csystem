<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<header>
	<div class="area-logo">
		<img style="width: 50px" alt=""
			src="${pageContext.request.contextPath}/assets/b_botazini-logo.png ">
		<span class="area-usuario">Olá <strong>${logedUser}</strong>!</span> 
		<span id="menu-msg">${msg}</span>
	</div>
	<div style="display: flex;">
		<form id="get-data-form" method="get">
			<input id="formInput" type="hidden">
		</form>
		<span class="logout-link"> 
			<a id="stock-link">Transações</a>
		</span> 
		<span class="logout-link"> 
			<a id="suppliers-link" href="${pageContext.request.contextPath}/clients?action=reportPage" 
			>Fornecedores
			</a>
		</span> 
		<!-- DROPDOWN DE VENDAS -->
		<div class="dropdown logout-link">
			<a id="sale-link">Vendas </a>
			<div class="dropdown-content">
				<a id="pendencySales">Abertas</a>
				<a id="undeliveredSales">Pendentes</a>
				<a id="concludedSales" >Concluídas</a>
			</div>
		</div> 
		
		<span class="logout-link"> 
			<a id="panel-link" href="${pageContext.request.contextPath}/purchase">Compras</a>
		</span>
		<div id="forms"></div>
		<!-- DROPDOWN DE ESTOQUE -->
		<div class="dropdown logout-link">
			<a  id="stock-menu">Estoque</a>
			<div class="dropdown-content">
				<a id="stockpage">Lista</a>
				<a href="${pageContext.request.contextPath}/clients?action=getClientList" >Recibos</a>
			</div>
		</div> 
		
		<span class="logout-link"> 
			<a id="panel-link" href="${pageContext.request.contextPath}/panel?action=goToPage">Painel</a>
		</span> 
		<span class="logout-link"> 
			<a id="resume-link"  href="${pageContext.request.contextPath}/resumo">Resumo</a>
		</span> 
		<span class="logout-link"> 
			<a id="register-link">Cadastros</a>
		</span> 
		<span class="logout-link"> 
			<a  href="${pageContext.request.contextPath}/help">Ajuda</a>
		</span> 
		<span class="logout-link"> 
			<a id="logout" href="${pageContext.request.contextPath}/logout">Sair</a>
		</span>

	</div>
</header>