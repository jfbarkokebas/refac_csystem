"use strict";

const deleteBtn = document.getElementById("crud-delete");
const purchasedInput = document.getElementById("purchasedInput");
const formDialog = document.getElementById("form-dialog");
const ulDialog = document.getElementById("ul-dialog");
const backBtn = document.getElementById("backBtn");
const container = document.getElementById("ul-dialog");
const entryID = document.getElementById("entryID").innerText;


function deleteTransaction(entryID) {

	const form = document.createElement("form");
	form.method = "post";
	form.action = SITE_ROOT + "/entries";

	const input1 = document.createElement("input");
	input1.type = "hidden";
	input1.name = "action";
	input1.value = "delete";

	const input2 = document.createElement("input");
	input2.type = "hidden";
	input2.name = "entryID";
	input2.value = entryID;

	const input3 = document.createElement("input");
	input3.type = "hidden";
	input3.name = "purchased";
	input3.value = purchasedInput.value;

	form.appendChild(input1);
	form.appendChild(input2);
	form.appendChild(input3);

	container.appendChild(form);

	form.submit();

}

function init() {

	deleteBtn.addEventListener("click", () => {

		if (purchasedInput.value === "true") {
			alert("Essa entrada já foi comprada.")
			return
		}
		deleteTransaction(entryID);
	});

	backBtn.addEventListener("click", () => {
		window.location.href = SITE_ROOT + "/transactions";

	});

};

document.addEventListener("DOMContentLoaded", init);


//END OF FILE