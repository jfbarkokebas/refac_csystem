"use strict";

const exportarPdfButton = document.getElementById("exportarPDF");
const product = document.getElementById("product");
const clienteContrato = CLIENT.trim().replace(" ", "_")

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

function init() {

	exportarPdfButton.addEventListener("click", () => {
		exportarPDF();
	});
	
};

document.addEventListener("DOMContentLoaded", init);