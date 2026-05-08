package net.elpuig.daw2.servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet("/infoSesion")
public class InfoSesionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        ServletContext contexto = getServletContext();


        String idSesion = session.getId();
        Date creacion = new Date(session.getCreationTime());
        Date ultimoAcceso = new Date(session.getLastAccessedTime());


        Integer contador = (Integer) session.getAttribute("contador");
        if (contador == null) {
            contador = 0;
        } else {
            contador++;
        }
        session.setAttribute("contador", contador);


        String usuario = (String) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            usuario = "No validado";
        }


        Integer usuariosConectados = 0;
        Integer usuariosValidados = 0;

        synchronized (contexto) {
            usuariosConectados = (Integer) contexto.getAttribute("usuariosConectados");
            usuariosValidados = (Integer) contexto.getAttribute("usuariosValidados");
        }


        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Información de su sesión</title>");
            out.println("<style>");
            out.println("body { background-color: #633366; font-family: Arial; display: flex; flex-direction: column; align-items: center; padding-top: 50px; }");
            out.println("h1 { color: white; margin-bottom: 20px; }");
            out.println("table { background-color: #fff9a6; border-collapse: collapse; width: 600px; border: 2px solid #999; }");
            out.println("th, td { border: 1px solid #999; padding: 12px; text-align: left; }");
            out.println("th { text-align: center; color: #633366; font-size: 1.2em; }");
            out.println(".label { font-weight: bold; color: #633366; width: 40%; }");
            out.println("a { color: #fff9a6; margin-top: 20px; text-decoration: underline; }");
            out.println("</style></head><body>");

            out.println("<h1>Información de su sesión</h1>");
            out.println("<table>");
            out.println("<tr><th colspan='2'>Información</th></tr>");
            out.println("<tr><td class='label'>Atributo</td><td style='font-weight:bold; text-align:center;'>Valor</td></tr>");

            out.println("<tr><td class='label'>Identificador</td><td>" + idSesion + "</td></tr>");
            out.println("<tr><td class='label'>Fecha/hora creación</td><td>" + creacion.toString() + "</td></tr>");
            out.println("<tr><td class='label'>Hora último acceso</td><td>" + ultimoAcceso.toString() + "</td></tr>");
            out.println("<tr><td class='label'>Número previo de accesos</td><td>" + contador + "</td></tr>");
            out.println("<tr><td class='label'>Usuario</td><td>" + usuario + "</td></tr>");


            out.println("<tr><td class='label'>Número de usuarios conectados</td><td>" + (usuariosConectados != null ? usuariosConectados : 0) + "</td></tr>");
            out.println("<tr><td class='label'>Número de usuarios validados</td><td>" + (usuariosValidados != null ? usuariosValidados : 0) + "</td></tr>");

            out.println("</table>");
            out.println("<br><a href='index.jsp'>Ir a la pantalla inicial</a>");
            out.println("</body></html>");
        }
    }
}