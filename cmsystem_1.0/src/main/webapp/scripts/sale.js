"use strict";

const productName = document.getElementById("productName");
const quantityStock = document.getElementById("quantityStock");
const inputValorUnidade = document.getElementById("priceSale");
const inputQuantidade = document.getElementById("qtdSale");
const campoValorTotal = document.getElementById("totalAmount");
const saleBtn = document.getElementById("saleBtn");
const formSale = document.getElementById("formSale");

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

function disableButtom(buttom) {
    buttom.disabled = true;
	buttom.style.backgroundColor = "gray"
    buttom.innerText = "Processando"; // opcional, muda o texto
}

function setCurrentDate() {
  const input = document.getElementById("dateSale");
  const agora = new Date();

  // Ajusta para o formato aceito pelo input datetime-local
  const ano = agora.getFullYear();
  const mes = String(agora.getMonth() + 1).padStart(2, '0');
  const dia = String(agora.getDate()).padStart(2, '0');
  const hora = String(agora.getHours()).padStart(2, '0');
  const minuto = String(agora.getMinutes()).padStart(2, '0');

  input.value = `${ano}-${mes}-${dia}T${hora}:${minuto}`;
}

function init() {
	
	setCurrentDate();

	inputValorUnidade.addEventListener("input", () => {
		atualizarValorTotal();
	});
	
	inputQuantidade.addEventListener("input", () => {
		atualizarValorTotal();
	});
	
	saleBtn.addEventListener("click", () => {
		 disableButtom(saleBtn);
		 formSale.submit();
	});

	if (productName.textContent.trim() === "cafe") {
		productName.textContent = "Café";

	} else if (productName.textContent.trim() === "pimenta") {
		productName.textContent = "Pimenta";
	}

	if (quantityStock.textContent.trim().startsWith("-")) {
		quantityStock.style.color = "red";

	}

};

document.addEventListener("DOMContentLoaded", init)