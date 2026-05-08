
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión Estudiantes</title>
    <style>
        body {
            background-color: #934993;
            font-family: Arial;
            color: white;
            padding: 20px;
        }
        h2 { margin-top: 30px; }
        label { display: block; margin-top: 10px; }
        .form-row { margin: 8px 0; }
        nav a { color: #ffccff; text-decoration: none; font-weight: bold; }
        nav a:hover { text-decoration: underline; }
        .logout { color: #ff9999 !important; margin-left: 15px; }
    </style>
</head>

<body>

<nav>
    <a href="infoSesion">Información de Sesión</a>

    <%-- Lógica para mostrar Cerrar Sesión solo si hay un usuario validado --%>
    <%
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
    %>
        <a href="logout"
           class="logout"
           onclick="return confirm('Con esta acción se finaliza la sesión actual ¿Está seguro?');">
           [ Cerrar Sesión ]
        </a>
    <%
        }
    %>
</nav>

<hr style="border: 0.5px solid #c181b3; margin-top: 20px;">

<h2>Consulta</h2>

<form action="consulta" method="get">
    <label>
        Sentencia SQL:
        <input type="text" name="sql">
    </label>
    <input type="submit" value="Ejecutar consulta">
</form>

<h2>Alta alumno</h2>

<form method="post" action="consulta">
    <div class="form-row">
        <label>Id: <input name="id" required /></label>
    </div>
    <div class="form-row">
        <label>Curso: <input name="curso" required /></label>
    </div>
    <div class="form-row">
        <label>Nombre: <input name="nombre" required /></label>
    </div>
    <button type="submit">Crear</button>
</form>

<h3>Informes</h3>
<form action="Report" method="get">
    <p>
        <input type="radio" name="optInformes" value="application/pdf" checked> PDF
        <input type="radio" name="optInformes" value="application/vnd.ms-excel"> Excel
        <input type="radio" name="optInformes" value="application/msword"> Word
        <input type="radio" name="optInformes" value="text/html"> HTML
    </p>

    <input type="submit" value="Ver informe">
</form>

</body>
</html>