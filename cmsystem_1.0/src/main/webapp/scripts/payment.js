"use strict";

const exportPDF = document.getElementById("exportPDF");
const table = document.getElementById("st-data");
const doc = document.getElementById("doc-container");
const title = document.getElementById("title");
const form1 = document.getElementById("paymentForm");
const payBtn = document.getElementById("payBtn");
const amount = document.getElementById("paid");
const description = document.getElementById("description");
const date = document.getElementById("date");


function hideElements(selector) {
	const elements = document.querySelectorAll(selector);
	elements.forEach(el => el.hidden = true);
}

function exportarPDF() {
	const element = document.getElementById("doc-container");
	html2pdf().set({
		margin: 10,
		filename: clienteContrato + '_ct_estoque.pdf',
		image: { type: 'jpeg', quality: 0.98 },
		html2canvas: { scale: 2 },
		jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
	}).from(element).save();
}

function compareValues() {
	const notPaid = document.querySelector('#notPaid');
	const paid = document.querySelector('#paid');

	// Remove vírgulas e converte o texto do span para número
	const notPaidValue = parseFloat(notPaid.textContent.replace(/,/g, ''));

	// Converte o valor do input (caso o usuário use vírgula por engano)
	const paidValue = parseFloat(paid.value.replace(',', '.'));

	if (isNaN(notPaidValue) || isNaN(paidValue)) {
		alert('Um dos valores não é numérico válido.');
		return;
	}

	// Comparação
	if (paidValue > notPaidValue) {
		return false;
	} else {
		return true;
	}
}

function fieldsNotEmpty() {
  const fields = [amount, description, date];
  let allFilled = true;

  fields.forEach(f => {
    if (!f.value.trim()) {
      f.style.borderColor = 'red';
      allFilled = false;
    } else {
      f.style.borderColor = ''; // volta ao normal
    }
  });

  return allFilled;
}


function init() {

	if (payBtn) {
		payBtn.addEventListener("click", (e) => {

			if (fieldsNotEmpty()) {

				if (compareValues()) {
					form1.submit();
				} else {
					alert("Os pagamentos não podem exceder o total da compra");
					document.querySelector('#paid').style.borderColor = 'red';
					document.querySelector('#paid').focus();
				}
			} else {
				e.preventDefault();
				alert("Preencha todos os campos");
			}

		});
	}

	exportPDF.addEventListener("click", () => {
		hideElements(".hiden")
		table.classList.remove()
		doc.style.width = "700px"
		exportPDF.hidden = true
		title.innerText = "EXTRATO"
		exportarPDF()

	});
};

document.addEventListener("DOMContentLoaded", init);