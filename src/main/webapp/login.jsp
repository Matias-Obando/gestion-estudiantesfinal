<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Validación de usuarios</title>
    <style>
        body { background-color: #6a396a; color: white; font-family: Arial; text-align: center; }

        .login-box {
            background-color: #fff9a6;
            color: #633366;
            display: inline-block;
            padding: 30px;
            border: 1px solid black;
            margin-top: 50px;
        }

        table td:first-child {
            color: #6a396a; /* Color lila/morado */
            font-weight: bold;
            text-align: left;
            padding-right: 20px;
        }

        input[type="text"], input[type="password"] {
            border: 1px solid #6a396a;
        }

        h1 { margin-top: 40px; }
    </style>
</head>
<body>
    <h1>Validación de usuarios</h1>
    <div class="login-box">
        <p><strong>La operación solicitada requiere validación<br>Por favor, introduzca sus credenciales</strong></p>
        <form action="consulta" method="post">
            <input type="hidden" name="accion" value="login">
            <table style="margin: 0 auto;">
                <tr>
                    <td>Usuario</td>
                    <td><input type="text" name="user"></td>
                </tr>
                <tr>
                    <td>Contraseña</td>
                    <td><input type="password" name="pass"></td>
                </tr>
            </table>
            <br>
            <input type="submit" value="Aceptar">
            <input type="reset" value="Borrar">
        </form>
    </div>
</body>
</html>