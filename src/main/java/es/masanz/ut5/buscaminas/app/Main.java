package es.masanz.ut5.buscaminas.app;

import es.masanz.ut5.buscaminas.model.Buscaminas;
import es.masanz.ut5.buscaminas.model.Dashboard;
import es.masanz.ut5.buscaminas.model.Nivel;
import es.masanz.ut5.buscaminas.util.Configuracion;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);

        Dashboard dashboard = new Dashboard();

        while (true) {
            limpiarPantalla();

            System.out.println(textoColor("==== BUSCAMINAS ====", "negrita"));
            System.out.println(textoColor("1. ", "negrita") + "FACIL");
            System.out.println(textoColor("2. ", "negrita") + "MEDIO");
            System.out.println(textoColor("3. ", "negrita") + "DIFICIL");
            System.out.println(textoColor("4. ", "negrita") + "Ver puntuaciones");
            System.out.println(textoColor("5. ", "negrita") + "Salir");
            System.out.print(textoColor("Elige una opcion: ", "negrita"));
            int opcion = scInt.nextInt();

            long tiempo = 0;

            if (opcion == 4) {
                String[][] puntuaciones = dashboard.obtenerContenido();

                for (int i = 0; i < puntuaciones.length; i++) {
                    for (int j = 0; j < puntuaciones[i].length; j++) {
                        System.out.print(puntuaciones[i][j] + " ");
                    }
                    System.out.println();
                }

                System.out.print("Ingrese cualquier tecla para volver al menú principal...");
                scString.nextLine();
                continue;
            }

            if (opcion == 5) {
                break;
            }

            Nivel dificultad = elegirDificultad(opcion);

            if (dificultad == null) {
                System.out.println("Opción incorrecta");
                continue;
            }

            Buscaminas buscaminas = new Buscaminas(dificultad.getFilas(), dificultad.getColumnas(), dificultad.getBombas());
            buscaminas.generarTablero();

            long tiempoInicio = System.currentTimeMillis();

            while (true) {
                limpiarPantalla();
                int totalAncho = ("DIFICULTAD: " + dificultad.name() + "BOMBAS: " + dificultad.getBombas()).length() + 11;

                System.out.println("-".repeat(totalAncho));
                System.out.printf("|  %s%2s|%-2s%s  |\n", textoColor("DIFICULTAD: ", "azul") + dificultad.name(), "", "", textoColor("BOMBAS: ", "rojo") + dificultad.getBombas());
                System.out.println("-".repeat(totalAncho));

                String tableroConNumeros = "";
                tableroConNumeros += "     ";
                for (int i = 1; i <= dificultad.getColumnas(); i++) {
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
                        buscaminas.actualizarBloqueoCelda(fila, columna);
                        System.out.println(buscaminas.obtenerTablero());
                    }
                } else {
                    boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
                    if (!estaBloqueada) {
                        buscaminas.actualizarReveladoCelda(fila, columna);
                        System.out.println(buscaminas.obtenerTablero());
                        if (buscaminas.getTablero()[fila][columna].getNumero() == -1) {
                            //mostrarTablero(tablero);
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


