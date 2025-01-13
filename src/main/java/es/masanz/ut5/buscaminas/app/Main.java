package es.masanz.ut5.buscaminas.app;

import es.masanz.ut5.buscaminas.model.Buscaminas;
import es.masanz.ut5.buscaminas.model.Dashboard;
import es.masanz.ut5.buscaminas.model.Nivel;
import es.masanz.ut5.buscaminas.util.Configuracion;

import java.util.BitSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);

        Dashboard dashboard = new Dashboard();

        while (true) {
            limpiarPantalla();

            String[] opciones = {"FACIL", "MEDIO", "DIFICIL", "Ver puntuaciones", "Salir"};

            int opcion = menuInicio(scInt, opciones);

            long tiempo = 0;

            if (opcion == 4) {
                String[][] puntuaciones = dashboard.obtenerContenido();

                String[] niveles = obtenerDatosColumna(puntuaciones, 0);
                String[] nombres = obtenerDatosColumna(puntuaciones, 1);
                String[] tiempos = obtenerDatosColumna(puntuaciones, 2);

                int anchoNivel = calcularMaxAncho("NIVEL", niveles);
                int anchoNombre = calcularMaxAncho("NOMBRE", nombres);
                int anchoTiempo = calcularMaxAncho("TIEMPO (S)", tiempos);
                int anchoTotal = anchoNivel + anchoNombre + anchoTiempo + 16;

                System.out.println();
                System.out.println("-".repeat(anchoTotal) + "\n");
                imprimirFilaPuntuacion(anchoNivel, anchoNombre, anchoTiempo, "NIVEL", "NOMBRE", "TIEMPO (S)");
                System.out.println("-".repeat(anchoTotal));

                for (int i = 0; i < puntuaciones.length; i++) {
                    imprimirFilaPuntuacion(anchoNivel, anchoNombre, anchoTiempo, niveles[i], nombres[i], tiempos[i]);
                }
                System.out.println("-".repeat(anchoTotal) + "\n");
                System.out.print(textoColor("Presiona \"enter\" para volver al menú principal...", "negrita"));
                scString.nextLine();
                continue;
            }

            if (opcion == 5) {
                break;
            }

            Nivel dificultad = elegirDificultad(opcion);

            if (dificultad == null) {
                System.out.println(textoColor("[!]: Opción incorrecta", "rojo"));
                Thread.sleep(1000);
                continue;
            }

            Buscaminas buscaminas = new Buscaminas(dificultad.getFilas(), dificultad.getColumnas(), dificultad.getBombas());
            buscaminas.generarTablero();

            long tiempoInicio = System.currentTimeMillis();

            int bombasDetectadas = 0;

            while (true) {
                limpiarPantalla();
                mostrarInfoJuego(buscaminas, dificultad.name(), bombasDetectadas, dificultad.getColumnas());

                String seleccion = "S";
                int cont = 0;
                while (!seleccion.equalsIgnoreCase("Y") && !seleccion.equalsIgnoreCase("N")) {
                    if (cont != 0) {
                        System.out.print(textoColor("[!]: Opcion invalida, ingrese 'Y' o 'N' por favor", "rojo"));
                        Thread.sleep(1500);
                        System.out.print("\r" + " ".repeat(50) + "\r");
                        //continue;
                    }
                    System.out.print(textoColor("¿Quieres bloquear o desbloquear una celda? (Y/N): ", "negrita"));
                    seleccion = scString.nextLine();
                    cont++;
                }

                int fila = -1;
                cont = 0;
                while (fila < 0 || fila > dificultad.getFilas() - 1) {
                    if (cont != 0) {
                        System.out.print(textoColor("[!]: Fila invalida, ingrese una fila entre 1 y " + dificultad.getFilas() + " por favor", "rojo"));
                        Thread.sleep(1500);
                        System.out.print("\r" + " ".repeat(50) + "\r");
                    }
                    System.out.print(textoColor("Ingresa la fila de la celda (1-" + dificultad.getFilas() + "): ", "negrita"));
                    fila = scInt.nextInt() - 1;
                    cont++;
                }

                int columna = -1;
                cont = 0;

                while (columna < 0 || columna > dificultad.getColumnas() - 1) {
                    if (cont != 0) {
                        System.out.print(textoColor("[!]: Columna invalida, ingrese una columna entre 1 y " + dificultad.getColumnas() + " por favor", "rojo"));
                        Thread.sleep(1500);
                        System.out.print("\r" + " ".repeat(50) + "\r");
                    }
                    System.out.print(textoColor("Ingresa la columna de la celda (1-" + dificultad.getColumnas() + "): ", "negrita"));
                    columna = scInt.nextInt() - 1;
                    cont++;

                }

                if (seleccion.equalsIgnoreCase("Y")) {
                    boolean estaRevelada = buscaminas.estaRevelada(fila, columna);
                    if (!estaRevelada) {
                        if (buscaminas.estaBloqueada(fila, columna)) {
                            bombasDetectadas--;
                        } else {
                            bombasDetectadas++;
                        }
                        buscaminas.actualizarBloqueoCelda(fila, columna);
                        System.out.println(buscaminas.obtenerTablero());
                    }
                } else {
                    boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
                    if (!estaBloqueada) {
                        buscaminas.actualizarReveladoCelda(fila, columna);
                        if (buscaminas.getTablero()[fila][columna].getNumero() == -1) {
                            System.out.println();
                            mostrarInfoJuego(buscaminas, dificultad.name(), bombasDetectadas, dificultad.getColumnas());
                            System.out.println(textoColor("¡Has perdido!", "rojo"));
                            Thread.sleep(3000);
                            break;
                        }
                    }

                    if (buscaminas.estaResuelto()) {
                        System.out.println(buscaminas.obtenerTablero());

                        long tiempoFinal = System.currentTimeMillis();
                        tiempo = (tiempoFinal - tiempoInicio) / 1000;

                        System.out.println(textoColor("¡Has ganado!", "azul"));
                        System.out.println(textoColor("Dificultad: ", "negrita") + dificultad);
                        System.out.println(textoColor("Tiempo: ", "negrita") + +tiempo + " seg");

                        boolean registroValido = false;
                        while (!registroValido) {

                            System.out.print(textoColor("Ingresa tu nombre: ", "negrita"));
                            String nombre = scString.nextLine();

                            String[] registro = {dificultad.toString(), nombre, tiempo + ""};
                            registroValido = dashboard.nuevoRegistro(registro);

                            if (!registroValido) {
                                System.out.print(textoColor("[!]: No has ingresado un nombre valido", "rojo"));
                                Thread.sleep(1500);
                                System.out.print("\r" + " ".repeat(50) + "\r");
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void limpiarPantalla() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static int menuInicio(Scanner scInt, String[] opciones) {
        System.out.println(textoColor("==== BUSCAMINAS ====", "negrita"));
        System.out.println("--------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println(textoColor((i + 1) + ". ", "negrita") + opciones[i]);
        }
        System.out.println("--------------------");
        System.out.print(textoColor("Elige una opción: ", "negrita"));
        return scInt.nextInt();
    }

    private static String[] obtenerDatosColumna(String[][] matriz, int columna) {
        String[] datos = new String[matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            datos[i] = matriz[i][columna];
        }
        return datos;
    }

    private static Nivel elegirDificultad(int opcion) {
        switch (opcion) {
            case 1:
                return Nivel.FACIL;
            case 2:
                return Nivel.MEDIO;
            case 3:
                return Nivel.DIFICIL;
            default:
                return null;
        }
    }

    private static int calcularMaxAncho(String texto, String[] datos) {
        int maxAncho = texto.length();
        for (int i = 0; i < datos.length; i++) {
            if (datos[i].length() > maxAncho) {
                maxAncho = datos[i].length();
            }
        }

        return maxAncho;
    }

    private static void imprimirFilaPuntuacion(int anchoNivel, int anchoNombre, int anchoTiempo, String nivel, String nombre, String tiempo) {
        System.out.printf("|  %-" + anchoNivel + "s  |  %-" + anchoNombre + "s  |  %" + anchoTiempo + "s  |\n", nivel, nombre, tiempo);
    }

    private static void mostrarInfoJuego(Buscaminas buscaminas, String dificultad, int bombasDetectadas, int columnas) {
        int totalAncho = ("DIFICULTAD: " + dificultad + "BOMBAS: " + bombasDetectadas).length() + 11;

        System.out.println("-".repeat(totalAncho));
        System.out.printf("|  %s%2s|%-2s%s  |\n", textoColor("DIFICULTAD: ", "azul") + dificultad, "", "", textoColor("BOMBAS: ", "rojo") + bombasDetectadas);
        System.out.println("-".repeat(totalAncho));

        String tableroConNumeros = "";
        tableroConNumeros += "     ";
        for (int i = 1; i <= columnas; i++) {
            tableroConNumeros += textoColor(String.format("%3d  ", i), "blanco");
        }
        tableroConNumeros += "\n";

        String tablero = buscaminas.obtenerTablero();
        String[] filas = tablero.split("\n");
        for (int i = 1; i <= filas.length; i++) {
            tableroConNumeros += textoColor(String.format("%3d  ", i), "blanco");
            tableroConNumeros += filas[i - 1] + "\n";
        }

        System.out.println(tableroConNumeros);
    }

    private static String textoColor(String texto, String color) {
        color = color.toUpperCase();
        String colorElegido;

        switch (color) {
            case "AZUL":
                colorElegido = Configuracion.AZUL_ANSI;
                break;
            case "ROJO":
                colorElegido = Configuracion.ROJO_ANSI;
                break;
            case "VERDE":
                colorElegido = Configuracion.VERDE_ANSI;
                break;
            case "MORADO":
                colorElegido = Configuracion.MORADO_ANSI;
                break;
            case "BLANCO":
                colorElegido = Configuracion.BLANCO_ANSI;
                break;
            case "GRIS":
                colorElegido = Configuracion.GRIS_ANSI;
                break;
            default:
                colorElegido = Configuracion.RESET_ANSI;
                break;
        }

        if (color.equalsIgnoreCase("negrita")) {
            return "\u001B[1m" + texto + Configuracion.RESET_ANSI;
        } else {
            String negrita = "\u001B";
            String colorNegrita = negrita + "[1;" + colorElegido.split("\\[")[1];
            return colorNegrita + texto + Configuracion.RESET_ANSI;
        }
    }
}


