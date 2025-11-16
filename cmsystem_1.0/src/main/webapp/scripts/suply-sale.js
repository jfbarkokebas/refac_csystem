"use strict";

const inputValorUnidade = document.getElementById("current-value-purchase");
const inputQuantidade = document.getElementById("amount-purchase");
const quantidadeEmEstoque = document.getElementById("stockQuantity");
const debito = document.getElementById("debit");
const copiaDebito = document.getElementById("debit-copy");
const unidade = document.getElementById("unidade");
const campoValorTotal = document.getElementById("valorTotal");
const copyInputValorUnidade = document.getElementById("current-value-purchase-copy");
const copyInputQuantidade = document.getElementById("amount-purchase-copy");
const copyValorTotal = document.getElementById("valorTotal-copy");
const dataAtual = document.getElementById("dataAtual");
const dataAtualCopy = document.getElementById("dataAtualCopy");
const exportarPdfButton = document.getElementById("exportarPDF");
const purchaseButton = document.getElementById("purchase-btn");
const contratoCopy = document.getElementById("contrato-copy");

function calcDebit() {

	const quantidade = parseFloat(inputQuantidade.value);
	const estoque = parseFloat(quantidadeEmEstoque.textContent);

	if (!isNaN(quantidade) && !isNaN(estoque)) {

		if (estoque < quantidade) {
			const result = Math.abs(estoque - quantidade);
			const unidadeLabel = typeof unidade !== "undefined" ? unidade.textContent.slice(0, -1) : "";
			debito.innerText = `( Débito de ${result.toFixed(2)} ${unidadeLabel} )`;
			copiaDebito.innerText = `( Débito de ${result.toFixed(2)} ${unidadeLabel} )`;
		} else {
			debito.innerText = "";
		}
	}
}

function purchase() {
	console.log("comprar...");

	const valor = document.getElementById("current-value-purchase").value;
	const quantidade = document.getElementById("amount-purchase").value;

	if (valor === "" || quantidade === "") {
		alert("Por favor, preencha todos os campos.");
		return;
	}

	if (valor < 0 || quantidade < 0) {
		alert("Valores negativos não são permitidos.");
		return;
	}


	const form = document.getElementById("form-purchase-values");

	exportarPDF();

	setTimeout(() => {
		form.submit();
	}, 1500)
}

function atualizarValorTotal() {
	const valorUnidade = parseFloat(inputValorUnidade.value.replace(',', '.'));
	const quantidade = parseFloat(inputQuantidade.value.replace(',', '.'));

	if (!isNaN(valorUnidade) && !isNaN(quantidade)) {
		const total = valorUnidade * quantidade;

		// Formatação com separador de milhar e vírgula para decimais
		const totalFormatado = new Intl.NumberFormat('pt-BR', {
			style: 'currency',
			currency: 'BRL'
		}).format(total);

		campoValorTotal.innerText = totalFormatado;
		copyValorTotal.innerText = totalFormatado;
	} else {
		campoValorTotal.innerText = "R$ 0,00";
		copyValorTotal.innerText = "R$ 0,00";
	}
}

function exportarPDF() {
	
	purchaseButton.style.display = "block";
	
	const element = document.getElementById("doc-container");
	html2pdf().set({
		margin: 10,
		filename: CLIENT_NAME +'_contrato_compra_estoque.pdf',
		image: { type: 'jpeg', quality: 0.98 },
		html2canvas: { scale: 2 },
		jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
	}).from(element).save();
}

function init() {


	dataAtual.innerText = new Date().toLocaleDateString("pt-BR");
	dataAtualCopy.innerText = new Date().toLocaleDateString("pt-BR");

	purchaseButton.addEventListener("click", () => {
		purchase();
	});

	inputValorUnidade.addEventListener("input", () => {
		copyInputValorUnidade.value = inputValorUnidade.value;
		atualizarValorTotal();
	});
	inputQuantidade.addEventListener("input", () => {
		copyInputQuantidade.value = inputQuantidade.value;
		atualizarValorTotal();
		calcDebit();

	});

};

document.addEventListener("DOMContentLoaded", init);

//END FILE