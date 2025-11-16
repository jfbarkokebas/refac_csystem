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
const dataAtual = document.getElementById("purchaseDate");
const dataAtualCopy = document.getElementById("dataAtualCopy");
const exportarPdfButton = document.getElementById("exportarPDF");
const purchaseButton = document.getElementById("purchase-btn");
const contratoCopy = document.getElementById("contrato-copy");
const confirmBtn = document.getElementById("confirm-btn");
const confirmQuantity = document.getElementById("confirm-quantity");
const confirmPrice = document.getElementById("confirm-price");
const confirmDate = document.getElementById("confirmDate");

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

	const valor = document.getElementById("current-value-purchase").value;
	const quantidade = document.getElementById("amount-purchase").value;

	if (valor === "" || quantidade === "" || dataAtual === "") {
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
		filename: CLIENT_NAME + '_contrato_compra_estoque.pdf',
		image: { type: 'jpeg', quality: 0.98 },
		html2canvas: { scale: 2 },
		jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
	}).from(element).save();
}

function disableButtom(buttom) {
	buttom.disabled = true;
	buttom.style.backgroundColor = "gray"
	buttom.innerText = "Processando"; // opcional, muda o texto
}

function showDate(){
	const valor = dataAtual.value; // Ex: "2025-10-23T17:47"

	 if (!valor) {
	   dataAtualCopy.value = "";
	   confirmDate.innerText = "";
	   return;
	 }

	 // Divide a string para separar data e hora
	 const [dataPart, horaPart] = valor.split("T"); // "2025-10-23", "17:47"

	 // Divide a parte da data
	 const [ano, mes, dia] = dataPart.split("-");

	 // Divide a parte da hora
	 const [hora, minuto] = horaPart.split(":");

	 // Cria a data corretamente (lembrando: mês começa em 0)
	 const data = new Date(ano, mes - 1, dia, hora, minuto);

	 // Formata a data no padrão brasileiro (apenas data)
	 const dataFormatada = data.toLocaleDateString("pt-BR");

	 // Se quiser data + hora, use:
	 // const dataFormatada = data.toLocaleString("pt-BR");

	 // Atualiza os campos
	 dataAtualCopy.innerText = dataFormatada;
	 confirmDate.innerText = dataFormatada;
}

function init() {

	purchaseButton.disabled = true;

	dataAtualCopy.innerText = dataAtual.value;

	confirmBtn.addEventListener("click", () => {
		purchaseButton.disabled = false;
		inputValorUnidade.readOnly = true;
		inputQuantidade.readOnly = true;
	});

	purchaseButton.addEventListener("click", () => {
		disableButtom(purchaseButton)
		purchase();
	});

	inputValorUnidade.addEventListener("input", () => {
		copyInputValorUnidade.value = inputValorUnidade.value;
		confirmPrice.innerText = inputValorUnidade.value;
		atualizarValorTotal();
	});

	dataAtual.addEventListener("input", () => {
	  showDate();
	});

	inputQuantidade.addEventListener("input", () => {
		copyInputQuantidade.value = inputQuantidade.value;
		confirmQuantity.innerText = inputQuantidade.value;
		atualizarValorTotal();
		calcDebit();
	});

};

document.addEventListener("DOMContentLoaded", init);

//END FILE