<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body style="font-size: 12px">
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>

		<div class="content-area"  style="justify-content: center">
			
			<div class="doc-action-container" style="width: 700px; position: relative; font-family: sans-serif;">
				<div  id="doc-container">
					<div >
					<div class="receipt-title">
						<h4>Contrato de Estocagem</h4>
						<img class="receipt-logo" width="70" style="position: absolute; right: 15px;"
							src="${pageContext.request.contextPath}/assets/b_botazini-logo.png ">
					</div>
						
						<div>
							<span style=" font-weight: bold;">Estocado por Botazini</span>
							<br> CNPJ: 
							<span>61.357.461/0001-00</span>
						</div>
						<div >
							<span style=" font-weight: bold;">Cliente:</span>
							<br> Nome: 
							<span>${details.clientName}</span>, 
							CPF: 
							<span>${details.cpf}</span>
						</div>
						<div>
							<p style="margin-bottom: 0.3rem">
								<span style=" font-weight: bold;">Produto: </span>
								<span>${details.product}</span>								
							</p>
							<p style="margin-bottom: 0.3rem">
								<span style=" font-weight: bold;">Quantidade em ${details.productUnit}: </span>
								<span>${details.quantity}</span>								
							</p>
							<p style="margin-bottom: 0.3rem">
								<span style=" font-weight: bold;">Entrada em: </span>
								<span>${details.entryDate}</span>								
							</p>
						</div>
						
						<div >
							<span style=" font-weight: bold;">Data:</strong> 
							<span id="dataAtual"></span>
						</div>
						<div style="margin-top: 1rem;">
							<div> _________________________________
								<br> 
								<span >Café e Pimenta Ltda</span>
							</div>
							<div> _________________________________
								<br> 
								<span >${details.clientName}</span>
							</div>
						</div>
					</div>
					
					<br> 
					<br> 
					
					<div>
						<div class="receipt-title">
							<h4>Contrato de Estocagem</h4>
							<img class="receipt-logo" width="70" style="position: absolute; right: 15px;"
								src="${pageContext.request.contextPath}/assets/b_botazini-logo.png ">
						</div>
						<div>
							<span style=" font-weight: bold;">Estocado por Botazini</span>
							<br> CNPJ: 
							<span>61.357.461/0001-00</span>
						</div>
						<div >
							<span style=" font-weight: bold;">Cliente:</span>
							<br> Nome: 
							<span >${details.clientName}</span>, 
							CPF: 
							<span>${details.cpf}</span>
						</div>
						<div>
							<p style="margin-bottom: 0.3rem">
								<span style=" font-weight: bold;">Produto: </span>
								<span>${details.product}</span>								
							</p>
							<p style="margin-bottom: 0.3rem">
								<span style=" font-weight: bold;">Quantidade em ${details.productUnit}: </span>
								<span>${details.quantity}</span>								
							</p>
							<p style="margin-bottom: 0.3rem">
								<span style=" font-weight: bold;">Entrada em: </span>
								<span>${details.entryDate}</span>								
							</p>
						</div>
						
						<div >
							<span style=" font-weight: bold;">Data:</strong> 
							<span id="dataAtualCopy"></span>
						</div>
						<div  style="margin-top: 1rem;">
							<div> _________________________________
								<br> 
								<span >Café e Pimenta Ltda</span>
							</div>
							<div> _________________________________
								<br> 
								<span >${details.clientName}</span>
							</div>
						</div>
					</div>
					<br>
				</div>
				<div style="position: absolute; right: 10px; bottom: 20px">
					<button class="btn-2" id="exportarPDF" style="background-color: orange;">Exportar para PDF</button>
				</div>
			</div>

		</div>
	</div>
	 <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
	<script>const SITE_ROOT = "${pageContext.request.contextPath}"; const CLIENT = "${details.clientName}";</script>
	<script src="${pageContext.request.contextPath}/scripts/receipt.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
</body>
</html>