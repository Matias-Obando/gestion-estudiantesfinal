package net.elpuig.daw2.servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class EscuchadorSesiones implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext contexto = se.getSession().getServletContext();
        synchronized (contexto) {
            Integer total = (Integer) contexto.getAttribute("usuariosConectados");
            if (total == null) total = 0;
            contexto.setAttribute("usuariosConectados", total + 1);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext contexto = se.getSession().getServletContext();
        synchronized (contexto) {
            Integer total = (Integer) contexto.getAttribute("usuariosConectados");
            if (total != null && total > 0) {
                contexto.setAttribute("usuariosConectados", total - 1);
            }
        }
    }
}