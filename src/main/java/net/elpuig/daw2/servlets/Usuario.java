package net.elpuig.daw2.servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;

public class Usuario implements HttpSessionBindingListener {
    private String nombre;

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        ServletContext contexto = event.getSession().getServletContext();
        synchronized (contexto) {
            Integer validados = (Integer) contexto.getAttribute("usuariosValidados");
            validados = (validados == null) ? 1 : validados + 1;
            contexto.setAttribute("usuariosValidados", validados);
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        ServletContext contexto = event.getSession().getServletContext();
        synchronized (contexto) {
            Integer validados = (Integer) contexto.getAttribute("usuariosValidados");
            if (validados != null && validados > 0) {
                contexto.setAttribute("usuariosValidados", validados - 1);
            }
        }
    }
}