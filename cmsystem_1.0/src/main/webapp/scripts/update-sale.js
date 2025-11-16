"use strict";

const inputValorUnidade = document.getElementById("priceSale");
const inputQuantidade = document.getElementById("qtdSale");
const campoValorTotal = document.getElementById("totalAmount");

function atualizarValorTotal() {
	const valorUnidade = parseFloat(inputValorUnidade.value);
	const quantidade = parseFloat(inputQuantidade.value);

	if (!isNaN(valorUnidade) && !isNaN(quantidade)) {
		const total = valorUnidade * quantidade;

		const totalFormatado = new Intl.NumberFormat('pt-BR', {
			style: 'currency',
			currency: 'BRL'
		}).format(total);

		campoValorTotal.innerText = totalFormatado;
	} else {
		campoValorTotal.innerText = "R$ 0,00";
	}
}

function init() {

	inputValorUnidade.addEventListener("input", () => {
		atualizarValorTotal();
	});
	inputQuantidade.addEventListener("input", () => {
		atualizarValorTotal();
	});

	if (productName.textContent.trim() === "cafe") {
		productName.textContent = "Café";

	} else if (productName.textContent.trim() === "pimenta") {
		productName.textContent = "Pimenta";
	}


}

document.addEventListener("DOMContentLoaded", init);

//END FILE