<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>
		
		<div style="padding: 1rem; font-size: 1.2rem;">
		    <h1 style="font-size: 3rem;">C-System</h1>
		    <h2 style="font-size: 2rem;">Guia de uso</h2>
		    <h3>SOBRE O SISTEMA:</h3>
		    <p>O C-System é um sistema de gestão de compra venda e estocagem de commodities</p>
		    <p>De um lado do processo de gestão estão os clientes fornecedores (produtores) e na outra ponta os clientes finais
		        que serão responsáveis pelo beneficiamento da commodity
		    </p>
		    <p>O <strong>propietário</strong> é o dententor do sistema e responsável pelas transações de compra e venda do
		        produto</p>
		    <p>Existem 3 tipos básicos de transação dentro do sistema:
		        <strong> <a href="#estocar">estocar</a> , <a href="#comprar">comprar</a> e <a href="#vender">vender</a>
		        </strong>
		    </p>
		
		    <h3 id="estocar">ESTOCAR (tela de transações)</h3>
		
		    <p>Se trata de dar entrada no estoque dos clientes fornecedores</p>
		    <p>Cada fornecedor deve ser cadastrado como um cliente, após ser cadastrado já é possível estocar.</p>
		    <p>Caso queira é possível emitir um recibo de estocagem no formato PDF</p>
		    <p>Caso seja dada entrada no estoque de um cliente e este tenha algum débito com a empresa de produtos que faltaram
		        entregar em uma <a href="#comprar">compra</a> então a quantidade será debitada automaticamente e se
		        destinará ao estoque do proprietário.
		    </p>
		
		    <h3 id="comprar">COMPRAR (tela de estoque > lista)</h3>
		    <p>Ao passar o mouse por cima do botão <strong>Estoque</strong> aparecerã duas abas:
		        <a href="#list-compras">lista</a> e <a href="#recibos-entradas-saidas-clientes">recibos</a>
		    </p>
		    <p>Na aba lista é possível ver todos os clientes que tem estoque na empresa.</p>
		    <p>Ao clicar no botão <strong>[Comprar]</strong> você será redirecionado para uma pagina de compras</p>
		    <p>Na página de compras haverá uma área com um resumo do estoque do cliente fornecedor,
		        no centro um recibo para ser preenchido com o valor e a quantidade comprada e,
		        do lado direito uma aŕea de confirmação da compra.</p>
		    <p>IMPORTANTE:</p>
		    <ul>
		        <li>
		            <i>Caso tenha alguma venda aberta com o produto comprado ele será automaticamente encaminhado para a lista
		                de
		                compras da venda ativa.
		                Dessa forma não irá para o estoque do proprietário, mas ficará visível na lista de compras da venda
		                aberta
		                bastando clicar em [LISTAR] para visualizar ela</i>
		        </li>
		        <li>
		            <i>Para maiores informações sobre uma venda ver em <a href="#vender">vender</a></i>
		        </li>
		    </ul>
		
		    <h3 id="vender">VENDER (tela de transações)</h3>
		    <p>Para vender não é necessário ter nenhum produto ainda no estoque do propietário basta ir na tela de Transações
		        clicar em [Vender Produto] e executar a venda</p>
		    <p>Caso haja alguma quantidade do produto no estoque do propietário, o sistema irá informar e irá contar com essa
		        quantidade na compra já calculando o valor unitário e listando como se fosse uma compra na <a
		            href="lista-compras-venda">lista de compras</a> para uma venda
		    </p>
		
		    <p>As vendas podem estar em três estados: abertas, pendentes e concluidas</p>
		    <p>Ao passar o mouse sobre o botão [Vendas] no menu superior aparecerão as três opçoes</p>
		    <ul>
		        <li>
		            <p><strong>Aberta</strong>:
		                quando você cria uma venda ela já está no estado de aberta e toda compra para aquele
		                produto irá automaticamente para a venda aberta.
		            </p>
		            <img src="assets/venda-aberta.png" style="width: 60vw;">
		            <p>Se clicar no botão <a href="">Lista</a> da venda, aparecerá abaixo da venda uma lista de todas
		                as compras feitas para suprir ela
		            </p>
		            <p>Quando uma compra feita iguala ou ultrapassa a
		                <strong>pendência</strong> (valor que falta comprar pra terminar a venda) então a venda será
		                automaticamente tida como concluida.
		            </p>
		            <p>Se o valor comprado ultrapassa a quantidade vendida o excedente irá entrar no estoque do propietário</p>
		            <p>Só pode haver uma venda aberta por vez no sistema caso queira deixar uma em espera , ou quiser entregar
		                mesmo sem ter todo o produto no momento, basta clicar no ícone do caminhão e a venda irá para a lista de
		                pendentes.
		            </p>
		            <p>Uma venda pode ser editada exceto o valor unitário, caso a quantida comprada seja editada para a
		                quantidade atual comprada a venda também será concluida</p>
		
		        </li>
		        <li>
		            <p><strong>Pendente</strong>:
		                Na lista de vendas pendentes basta clicar em <a href="">Concluir</a> e ela retorna a lista de vendas
		                ativas
		                desde que não tenha uma outra ativa pois só pode ter uma venda ativa por vez
		                <img src="assets/venda-pendente.png" style="width: 60vw;">
		            </p>
		        </li>
		        <li>
		            <p><strong>Concluida</strong>:
		                A conclusão de uma venda sempre ocorre quando a quantidade comprada se iguala ou supera a quantidade
		                vendida.
		            </p>
		            <p>Na tela de vendas concluidas é possivel ver uma lista de todas as vendas feitas o ao clicar no ícone "i"
		                é possível ver os detalhes de cada compr feita para ela.
		            </p>
		            <img src="assets/venda-concluida.png" style="width: 60vw;">
		            <br>
		            <br>
		            <img src="assets/info-venda-concluida.png" style="width: 60vw;">
		            <br>
		            <br>
		            <p>Quando uma venda é concluida ela também entra no gráfico da tela <a href="#painel">Painel</a> que mostra
		                o lucro de todas as vendas dos ultimos 30 dias</p>
		
		        </li>
		    </ul>
		
		</div>
		
	</div>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
		<script src="${pageContext.request.contextPath}/scripts/page.js"></script>
		<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
<body>

</body>
</html>