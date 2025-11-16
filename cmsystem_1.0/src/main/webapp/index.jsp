<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
<title>Comoditties System</title>
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/favicon.ico">

<style type="text/css">

.body-login {
		width: 100%;
		height: 100vh;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #eee;
		background-image: 
		
			linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), 
			url('<%= request.getContextPath() %>/assets/bettim-BG.png');
        background-size: cover; 
        background-position: center; 
        background-repeat: no-repeat;
	}

</style>

</head>
<body class="body-login">
	<div id="main_area" class="main-area">
		<h1 class="h2">Entrar no sistema</h1>
		<div id="message-area">
			<span id="errorMessage">${msg}</span>
		</div>
		<form action="login" method="POST" id="form-login">
			<div class="mb-3">
				<label for="exampleInputEmail1" class="form-label">Login </label> <input
					type="text" class="form-control" id="exampleInputEmail1"
					name="login" required>
				<div id="login" class="form-text">
					Não compartilhe seus dados com ninguém<br>
				</div>
			</div>
			<div class="mb-3">
				<label for="exampleInputPassword1" class="form-label">Password</label>
				<input type="password" class="form-control" id="password"
					name="senha" required>
			</div>
			<button type="submit" class="btn">Entrar</button>
		</form>
		<br>
	</div>

 </body>
</html>