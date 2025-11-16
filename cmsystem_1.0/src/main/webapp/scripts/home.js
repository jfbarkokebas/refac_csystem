"use strict";

const clientCard = document.getElementById("clientCard");
const activeClientCard = document.getElementById("activeClients");
const userCard = document.getElementById("userCard");
const openClient = document.getElementById("openClient");
const openUser = document.getElementById("openUser");
const openActivateClients = document.getElementById("openActivateClients");
const inputPassword = document.getElementById("userPassword");
const inputConfirmPassword = document.getElementById("confirmPassword");
const saveBtn = document.getElementById("save-user-button");
const message = document.getElementById("message");
const message2 = document.getElementById("message2");
const menuMsg = document.getElementById("menu-msg");
const clientForm = document.getElementById("client-form");
const saveClientButton = document.getElementById("save-client-button");
const cpf = document.getElementById("clientCPF");

function confirmExclude(event) {
    const confirmed = confirm("Tem certeza que deseja excluir este cliente?");
    if (!confirmed) {
        event.preventDefault(); 
        return false;           
    }
    return true; 
}

function saveUser() {
	console.log("saveUser ...")
	const form = document.getElementById("formUser");

	const formData = new FormData(form);

	const params = new URLSearchParams();
	for (const pair of formData) {
		params.append(pair[0], pair[1]);
	}

	fetch(SITE_ROOT + "/UsuarioServlet", {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded"
		},
		body: params.toString()
	})
		.then(response => {
			if (!response.ok) {
				throw new Error("Erro HTTP: " + response.status);
			}
			return response.text();
		})
		.then(data => {
			menuMsg.textContent = "Usuário salvo com sucesso!";
			closeAllDialogs()
			form.reset();
		})
		.catch(error => {
			console.error("Erro ao enviar:", error);
			menuMsg.textContent = "Erro ao enviar os dados.";
		});
}

function validatePassword() {
	let password1 = 0;

	inputPassword.addEventListener("input", () => {
		let password = inputPassword.value;
		password1 = password;

		message.innerText = "A senha precisa ter 8 ou mais caracteres";
		message.style.color = "red";

		if (password.length > 7) {
			inputConfirmPassword.disabled = false;
			message.innerText = "";
		}
	});

	inputConfirmPassword.addEventListener("input", () => {
		let confirm = inputConfirmPassword.value;
		message.innerText = "A senha precisa ser igual ao campo acima";

		if (confirm === password1) {
			message.innerText = "Confirmado!";
			message.style.color = "#229954";
			saveBtn.disabled = false;
		}
	})

}

function openDialog(element) {
	closeAllDialogs();
	element.style.display = "block";
}

function closeAllDialogs() {
	clientCard.style.display = "none";
	userCard.style.display = "none";
	activeClientCard.style.display = "none";
}

function tableExixts() {
	const primeiroTD = document.querySelector(".tr");

	if (primeiroTD) {
		return true;
	} else {
		return false;
	}
}

function loadClientList() {
	if (!tableExixts()) {
		document.getElementById("loadForm").submit();
	} else {
		return
	}

}

function init() {

	loadClientList();

	saveClientButton.addEventListener("click", () => {
		const clientName = document.getElementById("clientName");
		const phoneNumber = document.getElementById("phoneNumber");

		if (clientName.value === "") {
			saveClientButton.disabled = true;
			alert("o nome precisa ser preenchido!")
			return
		}
		if (phoneNumber.value === "") {
			saveClientButton.disabled = true;
			alert("o telefone precisa ser preenchido!")
			return
		}

		clientForm.submit();
	});

	openClient.addEventListener("click", () => {
		openDialog(clientCard);
	});

	openUser.addEventListener("click", () => {
		openDialog(userCard);
	});
	
	openActivateClients.addEventListener("click", () => {
		openDialog(activeClientCard);
	});

	saveBtn.addEventListener("click", () => {
		saveUser();
	});

	saveBtn.disabled = true;
	inputConfirmPassword.disabled = true;
	validatePassword();

}

document.addEventListener("DOMContentLoaded", init);

//end file