package es.masanz.ut5.buscaminas.model;

import es.masanz.ut5.buscaminas.util.Configuracion;
import javafx.scene.paint.Color;

public enum ColorNumero {
    ROJA(-1, Configuracion.ROJO_FX, Configuracion.ROJO_ANSI), BLANCO(0, Configuracion.BLANCO_FX, Configuracion.BLANCO_ANSI), AZUL(1, Configuracion.AZUL_FX, Configuracion.AZUL_ANSI), VERDE(2, Configuracion.VERDE_FX, Configuracion.VERDE_ANSI), NARANJA(3, Configuracion.NARANJA_FX, Configuracion.NARANJA_ANSI), MORADO(4, Configuracion.MORADO_FX, Configuracion.MORADO_ANSI), AMARILLO(5, Configuracion.AMARILLO_FX, Configuracion.AMARILLO_ANSI), CYAN(6, Configuracion.CIAN_FX, Configuracion.CIAN_ANSI), GRIS(7, Configuracion.GRIS_FX, Configuracion.GRIS_ANSI), NEGRO(8, Configuracion.NEGRO_FX, Configuracion.NEGRO_ANSI);

    private final int numero;
    private final Color colorFx;
    private final String colorAnsi;

    ColorNumero(int numero, Color colorFx, String colorAnsi) {
        this.numero = numero;
        this.colorFx = colorFx;
        this.colorAnsi = colorAnsi;
    }

    public static Color obtenerColorFx(int numero) {
        ColorNumero[] coloresFx = ColorNumero.values();
        for (int i = 0; i < coloresFx.length; i++) {
            if (coloresFx[i].numero == numero) {
                return coloresFx[i].colorFx;
            }
        }
        return null;
    }

    public static String obtenerColorAnsi(int numero) {
        ColorNumero[] coloresAnsi = ColorNumero.values();
        for (int i = 0; i < coloresAnsi.length; i++) {
            if (coloresAnsi[i].numero == numero) {
                return coloresAnsi[i].colorAnsi;
            }
        }
        return null;
    }
}