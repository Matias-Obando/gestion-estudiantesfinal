package net.elpuig.daw2.servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class WebSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        ServletContext contexto = arg0.getSession().getServletContext();
        synchronized (contexto) {
            Integer conectados = (Integer) contexto.getAttribute("usuariosConectados");
            conectados = (conectados == null) ? 1 : conectados + 1;
            contexto.setAttribute("usuariosConectados", conectados);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
        ServletContext contexto = arg0.getSession().getServletContext();
        synchronized (contexto) {
            Integer conectados = (Integer) contexto.getAttribute("usuariosConectados");
            if (conectados != null && conectados > 0) {
                contexto.setAttribute("usuariosConectados", conectados - 1);
            }
        }
    }
}