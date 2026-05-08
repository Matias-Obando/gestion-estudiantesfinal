package net.elpuig.daw2.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/consulta")
public class ConsultaServlet extends HttpServlet {

    private String dbUrl;
    private String dbUser;
    private String dbPass;

    @Override
    public void init() {
        String url = getServletContext().getInitParameter("db.url");
        String user = getServletContext().getInitParameter("db.user");
        String pass = getServletContext().getInitParameter("db.pass");

        if (url == null || url.isBlank()) {
            url = "jdbc:mysql://127.0.0.1:3306/dbalumnos?useSSL=false&serverTimezone=UTC";
        }
        if (user == null || user.isBlank()) user = "root";
        if (pass == null) pass = "";

        dbUrl = url;
        dbUser = user;
        dbPass = pass;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Lista Alumnos</title>");
            out.println("<style>body{font-family:Arial; padding:20px; background-color:#fff9a6;} h2{color:#003366;}</style>");
            out.println("</head><body>");
            out.println("<h2>Lista de Alumnos</h2><hr>");

            String sql = "SELECT id, curso, nombre FROM alumnos";
            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                out.println("<br><strong>ID | CURSO | NOMBRE</strong><br><br>");
                while (rs.next()) {
                    out.println(rs.getInt("id") + " - " +
                            escapeHtml(rs.getString("curso")) + " - " +
                            escapeHtml(rs.getString("nombre")) + "<br>");
                }
            } catch (SQLException e) {
                out.println("<p style='color:red;'>Error BD: " + escapeHtml(e.getMessage()) + "</p>");
            }

            out.println("<br><br><a href='index.jsp'>Ir a la pantalla inicial</a>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");

        if ("login".equals(accion)) {
            validarLogin(req, resp);
        } else {
            insertarAlumno(req, resp);
        }
    }

    private void validarLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String user = req.getParameter("user");
        String pass = req.getParameter("pass");


        if ("Matias".equals(user) && "123456".equals(pass)) {
            HttpSession session = req.getSession(true);


            Usuario u = new Usuario(user);
            session.setAttribute("usuario", u);
            session.setAttribute("usuarioLogueado", u.getNombre());


            if (session.getAttribute("idTemp") != null) {
                ejecutarInsercionFinal(req, resp);
            } else {
                resp.sendRedirect("index.jsp");
            }
        } else {
            resp.sendRedirect("error_login.jsp");
        }
    }

    private void insertarAlumno(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(true);

        if (session.getAttribute("usuarioLogueado") == null) {
            session.setAttribute("idTemp", req.getParameter("id"));
            session.setAttribute("cursoTemp", req.getParameter("curso"));
            session.setAttribute("nombreTemp", req.getParameter("nombre"));
            resp.sendRedirect("login.jsp");
            return;
        }

        ejecutarInsercionFinal(req, resp);
    }

    private void ejecutarInsercionFinal(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        String id = (req.getParameter("id") != null) ? req.getParameter("id") : (String) session.getAttribute("idTemp");
        String curso = (req.getParameter("curso") != null) ? req.getParameter("curso") : (String) session.getAttribute("cursoTemp");
        String nombre = (req.getParameter("nombre") != null) ? req.getParameter("nombre") : (String) session.getAttribute("nombreTemp");

        if (id == null || id.isEmpty()) {
            resp.getWriter().println("Error: El ID es obligatorio.");
            return;
        }

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO alumnos (id, curso, nombre) VALUES (?, ?, ?)")) {

            ps.setInt(1, Integer.parseInt(id));
            ps.setString(2, curso);
            ps.setString(3, nombre);
            ps.executeUpdate();

            session.removeAttribute("idTemp");
            session.removeAttribute("cursoTemp");
            session.removeAttribute("nombreTemp");

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<html><head><style>");
            out.println("body { background-color: #fff9a6; font-family: Arial; padding: 50px; }");
            out.println("h2 { color: blue; border-bottom: 2px solid blue; padding-bottom: 10px; font-size: 24px; }");
            out.println("p { font-size: 18px; margin-top: 20px; }");
            out.println("a { color: purple; text-decoration: underline; }");
            out.println("</style></head><body>");
            out.println("<h2>Usa JPA para grabar un registro en una tabla</h2>");
            out.println("<p>Alumno añadido</p>");
            out.println("<br><a href='index.jsp'>Ir a la pantalla inicial</a>");
            out.println("</body></html>");

        } catch (SQLException e) {
            resp.getWriter().println("Error de base de datos: " + e.getMessage());
        } catch (NumberFormatException e) {
            resp.getWriter().println("Error: El ID debe ser un número.");
        }
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}