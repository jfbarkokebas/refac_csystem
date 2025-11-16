<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>

		<div class="content-area" style="justify-content: center">

			<div class="action-container" style="width: 90%; position: relative;">
				<div id="doc-container">
					<span id="title" class="main-title">Pagar Compra</span>
					<p style="font-size: 1.6rem; border-bottom: 2px solid grey;">
						<strong>Nome do cliente: </strong> <span>${clientName}</span> <strong>
							  Quantidade: </strong> <span id="product">${dataMap.purchase.formatedQuantityPurchased} </span>
						<strong>   Produto: </strong> <span id="product">${dataMap.purchase.productName} </span>
						<strong>   Valor da compŕa: </strong> <span>${dataMap.purchase.formatedTotalPurchase}
						</span> <strong>   Falta pagar: </strong> <span id="notPaid">${dataMap.notPaid} </span>
					</p>
					<br>
					<form action="payments" method="post" id="paymentForm">
						<c:if test="${not dataMap.purchase.isPaymentPaidOff}">

							<input type="hidden" name="purchaseID" value="${purchaseID}">
							<input type="hidden" name="clientID" value="${dataMap.clientID}">
							<div class="row-form">
								Valor: <input id="paid" type="number" step="any" required name="amount">
								Descrição: 
								<textarea rows="1" cols="35"
									style="padding: 0; padding-left: 8px" required
									name="description" id="description">
								</textarea>
								Data: <input type="datetime-local" name="date" id="date" required>
								<button class="btn-row-form" type="button" id="payBtn">Pagar</button>
							</div>

						</c:if>
					</form>
					<br>
					<div class="table-wrapper" style="border: none">
						<c:if test="${not empty dataMap.payments}">
							<table class="table-1" id="st-data">
								<tr>
									<th class="hiden"></th>
									<th>Valor</th>
									<th>Descrição</th>
									<th>Data</th>
								</tr>

								<c:forEach var="payment" items="${dataMap.payments}">
									<tr class="tr">
										<td class="hiden"><a onclick="return confirmDelete()"
											href="${pageContext.request.contextPath}/payments?action=delete&paymentID=${payment.id}">
												<img
												src="${pageContext.request.contextPath}/assets/lixo-45.png"
												style="width: 40px;">
										</a></td>
										<tdclass="hiden">${payment.id}</td>
										<td>${payment.amount}</td>
										<td>${payment.description}</td>
										<td>${payment.date}</td>
									</tr>
									
								</c:forEach>
						</c:if>

							</table>
					</div>
					<c:if test="${empty  dataMap.payments}">
						<br>
						<h3 id="data-info">Ainda não há pagamentos para essa compra.</h3>
					</c:if>
					<div style="position: absolute; right: 10px; bottom: 20px">
						<button class="btn-2" id="exportPDF"
							style="background-color: orange;">Exportar para PDF</button>
					</div>

				</div>
			</div>
		</div>

	</div>
	<script>
		const SITE_ROOT = "${pageContext.request.contextPath}";
		const clienteContrato = "${clientName}";
	</script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/payment.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>

</body>
</html>