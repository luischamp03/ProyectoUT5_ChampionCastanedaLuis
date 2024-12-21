package es.masanz.ut5.buscaminas.app;

import es.masanz.ut5.buscaminas.model.Buscaminas;
import es.masanz.ut5.buscaminas.model.Dashboard;
import es.masanz.ut5.buscaminas.model.Nivel;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.sound.midi.SysexMessage;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== BUSCAMINAS ===");
        System.out.println("1. FACIL");
        System.out.println("2. MEDIO");
        System.out.println("3. DIFICIL");
        System.out.println("4. Ver puntuaciones");
        System.out.print("Elige una opcion: ");
        int opcion = sc.nextInt();


        switch (opcion) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                Buscaminas buscaminas = new Buscaminas(Nivel.FACIL.getFilas(), Nivel.FACIL.getColumnas(), Nivel.FACIL.getBombas());
                buscaminas.generarTablero();
                String[][] tablero = buscaminas.obtenerTablero();

                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        System.out.print("[ " + tablero[i][j] + " ]");
                    }
                    System.out.println();
                }

                System.out.println("Ingresa la fila de la celda (1-10): ");
                int fila = sc.nextInt();
                System.out.println("Ingresa la columna de la celda (1-10): ");
                int columna = sc.nextInt();

                boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
                if (!estaBloqueada) {
                    buscaminas.actualizarReveladoCelda(fila, columna);
                    if (buscaminas.getTablero()[fila][columna].getNumero() == -1) {
                        System.out.println("Has perdido");
                    }
                }

                tablero = buscaminas.obtenerTablero();

                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        System.out.print("[ " + tablero[i][j] + " ]");
                    }
                    System.out.println();
                }
                
                break;
            case 4:
                Dashboard dashboard = new Dashboard();
                String[][] puntuaciones = dashboard.obtenerContenido();

                for (int i = 0; i < puntuaciones.length; i++) {
                    for (int j = 0; j < puntuaciones[i].length; j++) {
                        System.out.print(puntuaciones[i][j] + " ");
                    }
                    System.out.println();
                }
                break;
            default:
                System.out.println("Opcion incorrecta");
                break;
        }
    }
}
