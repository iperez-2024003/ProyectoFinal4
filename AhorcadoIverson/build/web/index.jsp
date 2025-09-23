<%-- 
    Document   : index
    Created on : 22 jul 2025, 10:17:36
    Author     : informatica
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ahorcado IM | Inicio de Sesión</title>
    <link rel="stylesheet" href="Css/index.css">
</head>

<body>
    <nav>
        <a href="index.jsp" class="titulo">Ahorcado</a>
    </nav>

    <div class="login-contendor">
        <h2>Bienvenido</h2>
        <p>Ahorcado IM</p>
        <p class="subtitulo">Ingresa tus datos para iniciar sesión</p>

        <!-- Formulario de login -->
        <form action="Validar" method="POST" class="formulario">
            <input type="text" name="txtCorreo" placeholder="Usuario o Email" required />
            <input type="password" name="txtContrasena" placeholder="Contraseña" required />
            <button type="submit" name="btnIngresar" value="Ingresar" class="boton-brillante">
                Iniciar Sesión
            </button>
        </form>


        <% if (request.getAttribute("error") != null) { %>
            <p style="color:red; margin-top:10px;"><%= request.getAttribute("error") %></p>
        <% } %>
    </div>
</body>
</html>