package es.masanz.ut5.buscaminas.model;

import es.masanz.ut5.buscaminas.util.Configuracion;

public class Dashboard {

    private String contenido;

    public Dashboard() {
        this.contenido = Configuracion.DASHBOARD_INICIAL;
    }

    public boolean nuevoRegistro(String[] registro) {
        if (registro != null && registro.length == 3) {
            //Comprobar que sea uno de los modos de juego
            try {
                Nivel nivel = Nivel.valueOf(registro[0].trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return false;
            }

            //Comprobar que sea un nombre valido
            String nombre = registro[1].trim();
            if (this.contieneNombre(nombre) || nombre.isEmpty()) {
                return false;
            }

            //Comprobar que sea un numero
            int tiempo = 0;

            try {
                tiempo = Integer.parseInt(registro[2].trim());
                if (tiempo <= 0) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }

            this.contenido += String.join(",", registro) + ";";

            return true;
        }

        return false;
    }

    public boolean contieneNombre(String nombre) {
        String[] datos = this.contenido.split(";");
        String[] nombres = new String[datos.length];

        for (int i = 0; i < datos.length; i++) {
            nombres[i] = datos[i].split(",")[1];
        }

        for (int i = 0; i < nombres.length; i++) {
            if (nombre.equalsIgnoreCase(nombres[i])) {
                return true;
            }
        }

        return false;
    }

    public String[][] obtenerContenido() {
        String[] datos = this.contenido.split(";");

        String[][] registros = new String[datos.length][3];

        for (int i = 0; i < registros.length; i++) {
            String[] dato = datos[i].split(",");
            registros[i][0] = dato[0];
            registros[i][1] = dato[1];
            registros[i][2] = dato[2];
        }

        this.ordenarContenido(registros);

        return registros;
    }

    public void ordenarContenido(String[][] registros) {
        for (int i = 0; i < registros.length - 1; i++) {
            for (int j = 0; j < registros.length - 1 - i; j++) {
                if (registros[j][1].compareToIgnoreCase(registros[j + 1][1]) > 0) {
                    String[] aux = registros[j];
                    registros[j] = registros[j + 1];
                    registros[j + 1] = aux;
                }
            }
        }
    }

}
