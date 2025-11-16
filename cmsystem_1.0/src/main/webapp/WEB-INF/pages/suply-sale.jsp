<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>
		
		<div class="content-area">
			<div class="register-area" style="padding: 0 2rem;">
				<div id="details">
					<div class="title-area">
						<h1 class="main-title">Detalhes</h1>
					</div>
					<div class="details-ul">
					<ul>
						<li>
							<strong>Cliente:</strong>
							<span>${clientName}</span>
						</li>
						<li>
							<strong>ID:</strong>
							<span>${clientID}</span>
						</li>
						<li>
							<strong>Produto:</strong>
							<span>${clientProduct}</span>
						</li>
						<li>
							<strong>Quantidade:</strong>
							<span id="stockQuantity">${amount}</span>
						</li>
					</ul>
					</div>
				</div>
			</div>
			<div class="doc-action-container">
				<div id="doc-container">
					<div id="contrato">
						<div class="receipt-title">
						<h4>Contrato de Compra</h4>
						<img class="receipt-logo" width="110"
							src="${pageContext.request.contextPath}/assets/logo-bettim158.png ">
					</div>
						<div class="section">
							<strong>Comprador:</strong>
							<br> 
							Nome:  <span >Bettim Café e Pimenta Ltda</span> CNPJ: <span >59.685.363/0001-50</span>
						</div>
						<div class="section">
							<strong>Vendedor:</strong>
							<br> Nome: 
							<span >${clientName}</span>, 
							CPF: 
							<span >${cpf}</span>
						</div>
						<div class="section">
							<strong>Produto:</strong>
							 <span id="produto">${clientProduct}</span><br>
							<form id="form-purchase-values" method="post" 
								action="${pageContext.request.contextPath}/PurchaseServlet">
								<input type="hidden" name="action" value="suplyStock">
								<input type="hidden" name="stockId" value="${stockId}">
								<input type="hidden" name="product" value="${clientProduct}">
								<input type="hidden" name="clientID" value="${clientID}">
								<input type="hidden" name="clientName" value="${clientName}">
								<input type="hidden" name="quantityInStock" value="${amount}">
								<strong>Valor por unidade:</strong> R$ 
								<span id="valorUnidade">
									<input class="doc-input" id="current-value-purchase"
										name="current-value-purchase" type="number" min="0">
								</span> 
								<br> 
								<strong id="unidade">${unit}:</strong>
								 <span id="quantidade">
									<input class="doc-input" id="amount-purchase"
									name="amount-purchase" type="number" min="0">
								</span>
							</form>
							<p>
							 	<strong>Valor total:</strong>
							 	<span id="valorTotal"></span>
							 	<span id="debit"></span>
							</p>
						</div>
						<div class="section">
							<strong>Data:</strong> <span id="dataAtual"></span>
						</div>
						<div class="signature"  style="margin-top: 0.6rem;">
							<div> _________________________________
								<br> 
								<span id="compradorNomeAssinatura">Bettim Café e Pimenta Ltda</span>
							</div>
							<div> _________________________________
								<br> 
								<span id="vendedorNomeAssinatura">${clientName}</span>
							</div>
						</div>
					</div>
					<br> 
					<div id="contrato-copy">
						<div id="contrato">
						<div class="receipt-title">
						<h4>Contrato de Compra</h4>
						<img class="receipt-logo" width="110"
							src="${pageContext.request.contextPath}/assets/logo-bettim158.png ">
					</div>
						<div class="section">
							<strong>Comprador:</strong>
							<br> 
							Nome:  <span >Bettim Café e Pimenta Ltda</span> CNPJ: <span >59.685.363/0001-50</span>
						</div>
						<div class="section">
							<strong>Vendedor:</strong>
							<br> Nome: 
							<span >${clientName}</span>, 
							CPF: 
							<span id=>${cpf}</span>
						</div>
						<div class="section">
							<strong>Produto:</strong> 
							<span id="produto">${clientProduct}</span>
							<br>
							<strong>Valor por unidade:</strong>
							 R$ <span >
								<input class="doc-input" id="current-value-purchase-copy"
								 type="number" disabled>
							</span> 
							<br> 
							<strong>${unit}:</strong>
							 <span >
								<input class="doc-input" id="amount-purchase-copy" 
									type="number" disabled>
							</span> 
							<br> 
							<p>
								<strong>Valor total:</strong> 
								<span id="valorTotal-copy"></span>
								<span id="debit-copy"></span>
							</p>
						</div>
						<div class="section">
							<strong>Data:</strong>
							 <span id="dataAtualCopy"></span>
						</div>
						<div class="signature"  style="margin-top: 0.6rem;">
							<div> _________________________________
								<br> 
								<span id="compradorNomeAssinatura">Bettim Café e Pimenta Ltda</span>
							</div>
							<div> _________________________________
								<br> 
								<span id="vendedorNomeAssinatura">${clientName}</span>
							</div>
						</div>
					</div>
					<br>
				</div>
			</div>
			<div id="button-area" >
				<button class="btn-2" id="purchase-btn">Comprar</button>
			</div>

		</div>
	</div>
	 <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
	<script>
		const SITE_ROOT = "${pageContext.request.contextPath}";
		const CLIENT_NAME = "${clientName}";
	</script>
	<script src="${pageContext.request.contextPath}/scripts/stock-purchase.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
</body>
</html>