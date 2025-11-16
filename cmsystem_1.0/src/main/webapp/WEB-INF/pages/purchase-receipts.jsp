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
						<h4 style="margin-top: 1rem;">Número de Registros</h4>
						<input class="datepicker" type="number" name="number" min="1"/>
						<br>
						<br> 
						<input class="btn-1" type="submit" value="Gerar Relatório">
					</form>
				</div>
			</div>

			<div style="width: 45%; position: relative;" class="action-container">
				<div id="doc-container">

					<div class="receipt-title">
						<h2>Botazini</h2>
						<img class="receipt-logo" width="90"
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

					<h5 style="font-size: 1.3rem; font-weight: 600">Últimas
						Entradas</h5>
					<table>
						<c:forEach var="entry" items="${reportData.entryList}">

							<tr>
								<td style="padding: 0 6px;">Data: ${entry.formatedEntryDate}</td>
								<td>/ Quantidade: ${entry.formatedQuantity}</td>
							</tr>

						</c:forEach>
					</table>
					<br>
					<c:if test="${not empty reportData.purchaseList}">
						<h5 style="font-size: 1.3rem; font-weight: 600">Últimas
							Saídas</h5>
						<table>
							<c:forEach var="purchase" items="${reportData.purchaseList}">
								<tr>
									<td style="padding: 0 6px;">Data: ${purchase.formatedCreatedAt}</td>
									<td style="padding: 0 6px;">/ Quantidade:
										${purchase.formatedQuantityPurchased}</td>
								</tr>
							</c:forEach>
						</table>
					</c:if>
					<br> 
					<br> 
					<div> _________________________________ <br> 
						<span >Café e Pimenta Ltda</span>
					</div>
				</div>
				<div style="position: absolute; right: 10px; bottom: 20px">
					<button class="btn-2" id="exportarPDF"
						style="background-color: orange;">Exportar para PDF</button>
				</div>
			</div>
		</div>
	</div>

	<script>
		const SITE_ROOT = "${pageContext.request.contextPath}";
		const CLIENT = "${reportData.stock.name}";
	</script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/scripts/purchase-receipts.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
<body>

</body>
</html>