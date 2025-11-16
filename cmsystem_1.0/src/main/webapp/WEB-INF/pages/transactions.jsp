<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>
		
		<div class="content-area">
			<div id="registerArea" class="register-area">
				<div class="title-area">
					<h1 id="cadastros" class="main-title">Transações</h1>
				</div>
				<form id="get-clients-to-select"
					action="${pageContext.request.contextPath}/clients"
					method="get">
					<input type="hidden" name="action" value="selectClients">
				</form>
				<div class="box-buttons">
					<button class="btn-1" id="open-st">Estocar</button>
				</div>
				<div class="box-buttons">
					<button class="btn-1" id="sale-coffe">Vender Café</button>
				</div>
				<div class="box-buttons">
					<button class="btn-1" id="sale-pepper">Vender Pimenta</button>
				</div>

				<!-- STOCK DIALOG -->
				<div id="stockup-card">
					<form id="form-post-entry"
						action="${pageContext.request.contextPath}/entries"
						method="post">
						<div class="st-form-row">
							<label for="clientStock" class="">Nome do cliente</label>
							<div style="border: 1px solid grey">
							<select id="clientSelect" name="clientStock" style="width: 300px"></select>
							</div>

						</div>

						<div class="st-form-row">
							<label for="productStock" class="">Produto</label> 
							<select
								name="productStock" id="productStock" required>
								<option value="1">Café</option>
								<option value="2">Pimenta</option>
							</select>
						</div>
						<div class="st-form-row">
							<label for="qtdStock" class="">Quantidade</label> 
							<input type="number" name="qtdStock" id="qtdStock" step="any" required/>
						</div>
						<div class="st-form-row">
							<label for="dateStock" class="">Data da entrada</label> 
							<input type="datetime-local" name="dateStock" id="dateStock" required/>
						</div>
						<div class="st-form-row">
							<label for="observationStock" class="">Observações</label>
							<textarea name="observationStock" id="observationStock" rows="2"></textarea>
						</div>
						<br>
						<div class="button-area">
							<button type="button" class="btn-1" id="stock-button">Salvar</button>
						</div>
					</form>
				</div>

			</div>
			<div class="action-container">
				<form id="stock-form"
					action="${pageContext.request.contextPath}/transactions"
					method="get">
					<input type="hidden" name="action" value="stock">
				</form>
				<div class="stock-information" id="stock-area">
					<div id="stock-title">
						<img id="reload-img"
							src="${pageContext.request.contextPath}/assets/reload.png">
						<span>Estoque atualizado em: <span
							style="margin-left: 1rem" id="updated-on">${dataStock.stockUpdateDate}</span>
						</span>
					</div>
					<div class="stock" id="stock-p1">
						<span>Café = </span>
						<span id="p1-value">${dataStock.cafe}</span>
						<span id="p1-measure">Sacas</span>

					</div>
					<div class="stock" id="stock-p2">
						<span>Pimenta =</span>
						 <span id="p2-value">${dataStock.pimenta}</span>
						<span id="p2-measure">Kg</span>
					</div>
				</div>
				<br>
				<span class="main-title">Lista de Entradas</span>
				<div id="filter">
				
					<form id="filter-entries-by-name" action="${pageContext.request.contextPath}/transactions">
						<input type="hidden" name="action" value="searchByName">
					</form>
					
					<div id="filter-name-box">
						<button id="entries-by-name" class="btn-3">Nome</button>
						<input id="search_name" type="text" name="s" placeholder="Buscar por nome">
					</div>
					<form id="formSearchProduct" action="${pageContext.request.contextPath}/transactions" method="get">
						<input type="hidden" name="action" value="searchProduct">
					</form>
					<button id="cofeBtn" class="btn-3">Café</button>
					<button id="pepperBtn" class="btn-3">Pimenta</button>
				</div>
				<div id='count-area'>
					<span> Resultados: </span>
					<span id="salesCount">${dataStock.count}</span>
				</div>
				<div class="table-wrapper">
					<table id="stock-data">
						<tr>
							<th>Fornecedor</th>
							<th>Produto</th>
							<th>Quantidade</th>
							<th>Unidade</th>
							<th>Data de entrada</th>
							<th>Observação</th>
							<th></th>
						</tr>

						<c:if test="${not empty dataStock.tableData}">
							<c:forEach var="entry" items="${dataStock.tableData}">
								<tr class="tr">
									<td>${entry.clientName}</td>
									<td>${entry.productName}</td>
									<td>${entry.formatedQuantity}</td>
									<td>${entry.unit}</td>
									<td>${entry.entryDate}</td>
									<td>${entry.observation}</td>
									<td style="text-align: center;">
										<a class="btn-2" style="margin-right: 1rem"
											 href="${pageContext.request.contextPath}/entries?action=detail&entryId=${entry.id}">
											 Recibo
										</a>
										<a id="delete-entry" onclick="return confirm('Tem certeza que deseja continuar?')"
										   href="${pageContext.request.contextPath}/entries?action=delete&entryId=${entry.id}">
 											 <img width="30"  src="${pageContext.request.contextPath}/assets/lixo-45.png">
										</a>
									</td>
								</tr>
							</c:forEach>
						</c:if>

					</table>
				</div>
				<c:if test="${empty dataStock.tableData}">
					<br>
					<h3 id="data-info">Os dados não foram encontrados na sessão.</h3>
				</c:if>
			</div>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
	<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
	<script type="module" src="${pageContext.request.contextPath}/scripts/transactions.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
</body>
</html>