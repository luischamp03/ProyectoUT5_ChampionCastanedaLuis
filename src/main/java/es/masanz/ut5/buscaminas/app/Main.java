package es.masanz.ut5.buscaminas.app;

import es.masanz.ut5.buscaminas.model.Buscaminas;
import es.masanz.ut5.buscaminas.model.Dashboard;
import es.masanz.ut5.buscaminas.model.Nivel;
import es.masanz.ut5.buscaminas.util.Configuracion;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        Dashboard dashboard = new Dashboard();

        while (true) {
            limpiarPantalla();

            System.out.println("======= BUSCAMINAS =====");
            System.out.println("1. FACIL");
            System.out.println("2. MEDIO");
            System.out.println("3. DIFICIL");
            System.out.println("4. Ver puntuaciones");
            System.out.println("5. Salir");
            System.out.print("Elige una opcion: ");
            int opcion = sc.nextInt();

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
                sc.nextLine();
                sc.nextLine();
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
            String[][] tablero = buscaminas.obtenerTablero();

            long tiempoInicio = System.currentTimeMillis();

            while (true) {
                limpiarPantalla();
                mostrarTablero(tablero);

                int fila = -1;

                while (fila < 0 || fila > dificultad.getFilas() - 1) {
                    System.out.print("Ingresa la fila de la celda (1-" + dificultad.getFilas() + "): ");
                    fila = sc.nextInt() - 1;
                }

                int columna = -1;

                while (columna < 0 || columna > dificultad.getColumnas() - 1) {
                    System.out.print("Ingresa la columna de la celda (1-" + dificultad.getColumnas() + "): ");
                    columna = sc.nextInt() - 1;
                }

                boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
                if (!estaBloqueada) {
                    buscaminas.actualizarReveladoCelda(fila, columna);
                    tablero = buscaminas.obtenerTablero();
                    if (buscaminas.getTablero()[fila][columna].getNumero() == -1) {
                        mostrarTablero(tablero);
                        System.out.println("Has perdido");
                        Thread.sleep(2000);
                        break;
                    }
                }

                if (buscaminas.estaResuelto()) {
                    tablero = buscaminas.obtenerTablero();
                    mostrarTablero(tablero);

                    long tiempoFinal = System.currentTimeMillis();
                    tiempo = (tiempoFinal - tiempoInicio) / 1000;

                    boolean registroValido = false;
                    while (!registroValido) {
                        System.out.println("Dificultad: " + dificultad);
                        System.out.println("Tiempo: " + tiempo + " seg");
                        System.out.print("Ingresa tu nombre: ");
                        sc.nextLine();
                        String nombre = sc.nextLine();

                        String[] registro = {dificultad.toString(), nombre, tiempo + ""};
                        registroValido = dashboard.nuevoRegistro(registro);

                        if (!registroValido) {
                            System.out.println("No has ingresado un nombre valido.");
                        }
                    }

                    break;
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

    private static void mostrarTablero(String[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                System.out.print("[ " + tablero[i][j] + " ]");
            }
            System.out.println();
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
            default:
                colorElegido = Configuracion.RESET_ANSI;
        }

        return colorElegido + texto + Configuracion.RESET_ANSI;
    }
}


