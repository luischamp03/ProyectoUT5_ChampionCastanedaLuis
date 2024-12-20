package es.masanz.ut5.buscaminas.model;

public enum Nivel {
    FACIL(10, 10, 10), MEDIO(16, 16, 40), DIFICIL(20, 20, 99);

    private final int filas;
    private final int columnas;
    private final int bombas;

    Nivel(int filas, int columnas, int bombas) {
        this.filas = filas;
        this.columnas = columnas;
        this.bombas = bombas;
    }

    public int getFilas() {
        return this.filas;
    }

    public int getColumnas() {
        return this.columnas;
    }

    public int getBombas() {
        return this.bombas;
    }
}