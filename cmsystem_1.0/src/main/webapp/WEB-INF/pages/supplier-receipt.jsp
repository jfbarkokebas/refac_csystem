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

			<div class="register-area">
				<div id="">
					<form action="${pageContext.request.contextPath}/purchase-receipts">
						<h4>Selecione o Cliente</h4>
						<input class="datepicker" type="hidden" name="action" value="getReports"/>
						<div class="dropdown-container" >
							<select class="select-receipt" name="clientId" >
								<c:forEach var="client" items="${clients}">
									<option value="${client.id}">${client.name}</option>
								</c:forEach>
								<c:if test="${empty clients}">
									<option>Clientes não encontrados.</option>
								</c:if>
							</select>
						</div>
						<h4 style="margin-top: 1rem;">Selecione o produto</h4>
						<select class="form-select form-select-sm mb-3"
							aria-label=".form-select-lg example" name="productId"
							id="productStock" style="font-size: 1.2rem" required>
							<option value="1">Café</option>
							<option value="2">Pimenta</option>
						</select>
						<h4 style="margin-top: 1rem;">Início</h4>
						<input class="datepicker" type="datetime-local" name="startDate" />
						<h4 style="margin-top: 1rem;">Fim</h4>
						<input class="datepicker" type="datetime-local" name="endDate" />
						<br>
						<br> 
						<input class="btn-1" type="submit" value="Gerar Relatório">
					</form>
				</div>
			</div>

			<div style="width: inherit; position: relative;" class="action-container">
				<div id="doc-container">
					<div class="receipt-title" style="width: 100%">
						<h2>Relatório de Cliente</h2>
						<img class="receipt-logo" width="100"
							src="${pageContext.request.contextPath}/assets/b_botazini-logo.png ">
					</div>
					<p style="font-size: 1.3rem;">
						<strong>Nome do Cliente: </strong> <span>${reportData.stock.name}</span>
					</p>
					<p style="font-size: 1.3rem;">
						<strong>Estoque atual de </strong> <strong id="product">${reportData.stock.product}:
						</strong> <span>${reportData.stock.formatedQuantity}</span> <span>${reportData.stock.unit}</span>
					</p>
					<p style="font-size: 1.3rem;">
						<strong>Débito: </strong> <span>${reportData.debit} </span>${reportData.stock.unit}
					</p>

				<div class="table-wrapper">
					<table id="stock-data">
						<c:if test="${not empty reportData.list}">
							<tr>
								<th>Data</th>
								<th>Produto</th>
								<th>Tipo</th>
								<th>Quantidade</th>
								<th>Valor (unid)</th>
								<th>Total</th>
							</tr>

							<c:forEach var="report" items="${reportData.list}">
								<tr class="tr">
									<td>${report.date}</td>
									<td>${report.product}</td>
									<td>${report.type}</td>
									<td>${report.quantity}</td>
									<td>${report.pricePerUnit}</td>
									<td>${report.totalAmount}</td>
								</tr>
							</c:forEach>
						</c:if>
						
					</table>
				</div>
				<div style="position: absolute; right: 10px; bottom: 20px">
					<button class="btn-2" id="exportarXlsx" 
						style="background-color: orange;">Exportar para Excel</button>
				</div>
			</div>
		</div>
	</div>

	<script>
		const SITE_ROOT = "${pageContext.request.contextPath}";
		const CLIENT = "${reportData.stock.name}";
	</script>
	<script src="${pageContext.request.contextPath}/scripts/libs/xlsx.full.min.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/suplier-receipt.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>

</body>
</html>