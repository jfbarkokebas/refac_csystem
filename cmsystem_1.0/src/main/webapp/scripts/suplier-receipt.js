"use strict";

const buttom = document.getElementById("exportarXlsx");

function exportTableToExcel(filename) {
	const table = document.getElementById("stock-data");
	const wb = XLSX.utils.table_to_book(table, { sheet: "Dados" });
	XLSX.writeFile(wb, filename);
}

function init() {
	buttom.addEventListener("click", ()=>{
		exportTableToExcel("Relatório_"+CLIENT+".xlsx")
	});
};

document.addEventListener("DOMContentLoaded", init);