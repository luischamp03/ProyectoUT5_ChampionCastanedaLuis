package es.masanz.ut5.buscaminas.model;

import es.masanz.ut5.buscaminas.util.Configuracion;

public class Buscaminas {

    //Atributos privados
    private int filas, columnas;
    private int bombas;
    private Celda[][] tablero;

    public Buscaminas(int filas, int columnas, int bombas) {
        this.filas = filas;
        this.columnas = columnas;
        this.bombas = bombas;
    }

    public void generarTablero() {
        this.tablero = new Celda[this.filas][this.columnas];
        this.colocarBombas(this.tablero);
        this.colocarNumeros(this.tablero);
    }

    public void colocarBombas(Celda[][] tablero) {
        int contBombas = 0;

        while (contBombas < this.bombas) {
            int fila = (int) (Math.random() * tablero.length);
            int columna = (int) (Math.random() * tablero[0].length);

            if (tablero[fila][columna] == null) {
                tablero[fila][columna] = new Celda(-1);
                contBombas++;
            }
        }
    }

    public void colocarNumeros(Celda[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                /*if (tablero[i][j].getNumero() == -1) {
                    continue;
                }*/

                if (tablero[i][j] == null) {
                    tablero[i][j] = new Celda(this.contarBombasAdyacentes(i, j));
                }
            }
        }
    }

    public int contarBombasAdyacentes(int fila, int columna) {
        int contBombas = 0;

        if (this.esCeldaValida(fila - 1, columna) && this.tablero[fila - 1][columna] != null) {
            if (this.tablero[fila - 1][columna].getNumero() == -1) {
                contBombas++;
            }
        }

        if (this.esCeldaValida(fila + 1, columna) && this.tablero[fila + 1][columna] != null) {
            if (this.tablero[fila + 1][columna].getNumero() == -1) {
                contBombas++;
            }
        }

        if (this.esCeldaValida(fila, columna - 1) && this.tablero[fila][columna - 1] != null) {
            if (this.tablero[fila][columna - 1].getNumero() == -1) {
                contBombas++;
            }
        }

        if (this.esCeldaValida(fila, columna + 1) && this.tablero[fila][columna + 1] != null) {
            if (this.tablero[fila][columna + 1].getNumero() == -1) {
                contBombas++;
            }
        }

        if (this.esCeldaValida(fila - 1, columna - 1) && this.tablero[fila - 1][columna - 1] != null) {
            if (this.tablero[fila - 1][columna - 1].getNumero() == -1) {
                contBombas++;
            }
        }

        if (this.esCeldaValida(fila - 1, columna + 1) && this.tablero[fila - 1][columna + 1] != null) {
            if (this.tablero[fila - 1][columna + 1].getNumero() == -1) {
                contBombas++;
            }
        }

        if (this.esCeldaValida(fila + 1, columna - 1) && this.tablero[fila + 1][columna - 1] != null) {
            if (this.tablero[fila + 1][columna - 1].getNumero() == -1) {
                contBombas++;
            }
        }

        if (this.esCeldaValida(fila + 1, columna + 1) && this.tablero[fila + 1][columna + 1] != null) {
            if (this.tablero[fila + 1][columna + 1].getNumero() == -1) {
                contBombas++;
            }
        }

        return contBombas;
    }

    public boolean esCeldaValida(int fila, int columna) {
        if (fila >= 0 && fila < this.tablero.length && columna >= 0 && columna < this.tablero[fila].length) {
            return true;
        }

        return false;
    }

    public int obtenerPosicion(int fila, int columna) {
        if (this.esCeldaValida(fila, columna)) {
            return this.tablero[fila][columna].getNumero();
        }

        return -1;
    }

    public boolean estaBloqueada(int fila, int columna) {
        if (this.esCeldaValida(fila, columna)) {
            return this.tablero[fila][columna].isBloqueada();
        }

        return false;
    }

    public void actualizarBloqueoCelda(int fila, int columna) {
        if (this.esCeldaValida(fila, columna)) {
            this.tablero[fila][columna].setBloqueada(!this.tablero[fila][columna].isBloqueada());
        }
    }

    public boolean estaRevelada(int fila, int columna) {
        if (this.esCeldaValida(fila, columna)) {
            return this.tablero[fila][columna].isRevelada();
        }

        return false;
    }

    public void actualizarReveladoCelda(int fila, int columna) {
        //Recursividad
        if (!this.esCeldaValida(fila, columna) || this.tablero[fila][columna].isRevelada() /*|| this.tablero[fila][columna].getNumero() == -1*/) {
            return;
        }

        if (this.tablero[fila][columna].isBloqueada()) {
            this.tablero[fila][columna].setBloqueada(false);
        }
        this.tablero[fila][columna].setRevelada(true);

        if (this.tablero[fila][columna].getNumero() == 0) {
            this.actualizarReveladoCelda(fila - 1, columna);
            this.actualizarReveladoCelda(fila + 1, columna);
            this.actualizarReveladoCelda(fila, columna - 1);
            this.actualizarReveladoCelda(fila, columna + 1);
            this.actualizarReveladoCelda(fila - 1, columna - 1);
            this.actualizarReveladoCelda(fila - 1, columna + 1);
            this.actualizarReveladoCelda(fila + 1, columna - 1);
            this.actualizarReveladoCelda(fila + 1, columna + 1);
        }
    }

    public boolean estaResuelto() {
        int totalCeldas = this.filas * this.columnas;
        int contBombas = 0;
        int contCeldas = 0;

        for (int i = 0; i < this.tablero.length; i++) {
            for (int j = 0; j < this.tablero[i].length; j++) {
                if (this.tablero[i][j].getNumero() == -1 && !this.tablero[i][j].isRevelada()) {
                    contBombas++;
                } else if (this.tablero[i][j].getNumero() != -1 && this.tablero[i][j].isRevelada()) {
                    contCeldas++;
                }
            }
        }

        if (contBombas + contCeldas == totalCeldas) {
            return true;
        }

        return false;
    }

    public String obtenerTablero() {
        StringBuilder tableroString = new StringBuilder();
        
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                String celda;
                if (this.tablero[i][j].isBloqueada()) {
                    celda = "[ B ]";
                } else if (!this.tablero[i][j].isRevelada()) {
                    celda = "[ * ]";
                } else {
                    int numero = this.tablero[i][j].getNumero();
                    String numeroColor = ColorNumero.obtenerColorAnsi(numero) + numero + Configuracion.RESET_ANSI;
                    if (numero == -1) {
                        celda = "[" + numeroColor + " ]";
                    } else {
                        celda = "[ " + numeroColor + " ]";
                    }
                }
                tableroString.append(celda);
            }
            tableroString.append("\n");
        }
        return tableroString.toString();
    }

    public String obtenerTableroResuelto() {
        StringBuilder tableroString = new StringBuilder();

        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                String celda;
                int numero = this.tablero[i][j].getNumero();
                String numeroColor = ColorNumero.obtenerColorAnsi(numero) + numero + Configuracion.RESET_ANSI;
                if (numero == -1) {
                    celda = "[" + numeroColor + " ]";
                } else {
                    celda = "[ " + numeroColor + " ]";
                }
                tableroString.append(celda);
            }
            tableroString.append("\n");
        }
        return tableroString.toString();
    }

    public Celda[][] getTablero() {
        return this.tablero;
    }
}