"use strict";

const backBtn = document.getElementById("back-btn");
const form = document.getElementById("back-form");

function init(){
	backBtn.addEventListener("click", () => {
			form.action = SITE_ROOT + "/transactions";
			form.submit();
		});
};

document.addEventListener("DOMContentLoaded", init);