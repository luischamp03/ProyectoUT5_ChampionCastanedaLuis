package es.masanz.ut5.buscaminas.model;

public class Celda {

    private int numero;
    private boolean revelada;
    private boolean bloqueada;

    public Celda(int numero) {
        this.numero = numero;
        this.revelada = false;
        this.bloqueada = false;
    }

    public int getNumero() {
        return this.numero;
    }

    public boolean isRevelada() {
        return this.revelada;
    }

    public void setRevelada(boolean relevada) {
        this.revelada = relevada;
    }

    public boolean isBloqueada() {
        return this.bloqueada;
    }

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }
}
