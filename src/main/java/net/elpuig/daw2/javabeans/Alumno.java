package net.elpuig.daw2.javabeans;

public class Alumno {
    private int id;
    private String curso;
    private String nombre;

    public Alumno(int id, String curso, String nombre) {
        this.id = id;
        this.curso = curso;
        this.nombre = nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getCurso() {
        return curso;
    }

    public String getNombre() {
        return nombre;
    }

}