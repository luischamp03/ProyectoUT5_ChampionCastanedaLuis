package es.masanz.ut5.buscaminas.model;

public class Celda {

    private int numero;
    private boolean relevada;
    private boolean bloqueada;

    public Celda(int numero) {
        this.numero = numero;
        this.relevada = false;
        this.bloqueada = false;
    }

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isRelevada() {
        return this.relevada;
    }

    public void setRelevada(boolean relevada) {
        this.relevada = relevada;
    }

    public boolean isBloqueada() {
        return this.bloqueada;
    }

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }
}
