package es.masanz.ut5.buscaminas.app;

import es.masanz.ut5.buscaminas.model.Buscaminas;
import es.masanz.ut5.buscaminas.model.ColorNumero;
import es.masanz.ut5.buscaminas.model.Dashboard;
import es.masanz.ut5.buscaminas.model.Nivel;
import es.masanz.ut5.buscaminas.util.Configuracion;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.util.Duration;

public class BuscaminasApp extends Application {

    public static final Color COLOR_BOTON = Color.LIGHTGRAY;
    public static final Color COLOR_BORDE = Color.DARKGRAY;
    public static final Color COLOR_PRESIONADO = Color.DARKGRAY;
    public static final Color COLOR_HOVER = Color.LIGHTBLUE;

    private int tiempo = 0;
    private int bombasDetectatas = 0;
    private Text contadorBombasTexto;
    private GridPane grid;
    private BorderPane panelBuscaminas;
    private Nivel dificultad;
    private Timeline timeline;
    private Scene buscaminasScene;
    private Text tiempoTexto;
    private Stage stage;
    private Dashboard dashboard;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.dashboard = new Dashboard();
        this.stage = stage;
        Scene pantallaInicial = crearPantallaInicial();
        this.stage.setScene(pantallaInicial);
        this.stage.setTitle("Buscaminas - Interfaz");
        this.stage.setResizable(false);
        this.stage.show();
    }

    private Scene crearPantallaInicial() {
        VBox root = new VBox(10);
        Scene pantallaInicial = new Scene(root, 400, 400);
        root.setAlignment(Pos.CENTER);

        Button btnIniciarFacil = new Button("FACIL");
        btnIniciarFacil.setOnAction(event -> {
            dificultad = Nivel.FACIL;
            crearInterfazBuscaminas();
            buscaminasScene = new Scene(panelBuscaminas);
            stage.setScene(buscaminasScene);
        });
        root.getChildren().add(btnIniciarFacil);

        Button btnIniciarMedio = new Button("MEDIO");
        btnIniciarMedio.setOnAction(event -> {
            dificultad = Nivel.MEDIO;
            crearInterfazBuscaminas();
            buscaminasScene = new Scene(panelBuscaminas);
            stage.setScene(buscaminasScene);
        });
        root.getChildren().add(btnIniciarMedio);

        Button btnIniciarDificil = new Button("DIFICIL");
        btnIniciarDificil.setOnAction(event -> {
            dificultad = Nivel.DIFICIL;
            crearInterfazBuscaminas();
            buscaminasScene = new Scene(panelBuscaminas);
            stage.setScene(buscaminasScene);
        });
        root.getChildren().add(btnIniciarDificil);

        Button btnPuntuaciones = new Button("Ver puntuaciones");
        btnPuntuaciones.setOnAction(event -> {
            Scene scene = crearPantallaPuntuaciones(stage, pantallaInicial);
            stage.setScene(scene);
        });
        root.getChildren().add(btnPuntuaciones);

        return pantallaInicial;
    }

    public Scene crearPantallaPuntuaciones(Stage stage, Scene pantallaPrincipal) {

        Button btnVolver = new Button("Volver al menú principal");
        btnVolver.setOnAction(event -> stage.setScene(pantallaPrincipal));

        VBox topPanel = new VBox(btnVolver);
        topPanel.setSpacing(10);
        topPanel.setStyle("-fx-padding: 10; -fx-alignment: center;");

        GridPane tablaPuntuaciones = new GridPane();
        tablaPuntuaciones.setHgap(20);
        tablaPuntuaciones.setVgap(10);
        tablaPuntuaciones.setPadding(new Insets(20));
        tablaPuntuaciones.setAlignment(Pos.CENTER);

        Text headerNivel = new Text("Nivel");
        headerNivel.setFont(Font.font(16));
        Text headerNombre = new Text("Nombre");
        headerNombre.setFont(Font.font(16));
        Text headerTiempo = new Text("Tiempo (s)");
        headerTiempo.setFont(Font.font(16));

        tablaPuntuaciones.add(headerNivel, 0, 0);
        tablaPuntuaciones.add(headerNombre, 1, 0);
        tablaPuntuaciones.add(headerTiempo, 2, 0);

        String[][] puntuaciones = dashboard.obtenerContenido();

        for (int i = 0; i < puntuaciones.length; i++) {
            for (int j = 0; j < puntuaciones[i].length; j++) {
                Text dato = new Text(puntuaciones[i][j]);
                dato.setFont(Font.font(14));
                tablaPuntuaciones.add(dato, j, i + 1);
            }
        }

        BorderPane root = new BorderPane();
        root.setTop(topPanel);
        root.setCenter(tablaPuntuaciones);

        return new Scene(root, 400, 400);
    }

    private void crearInterfazBuscaminas() {
        panelBuscaminas = new BorderPane();
        HBox panelSuperior = crearPanelSuperior();
        GridPane tablero = crearTablero();
        panelBuscaminas.setTop(panelSuperior);
        panelBuscaminas.setCenter(tablero);
    }

    private HBox crearPanelSuperior() {
        tiempoTexto = new Text("Tiempo: 0");
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    tiempo++;
                    tiempoTexto.setText("Tiempo: " + tiempo);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        Button botonReinicio = new Button("Reiniciar");
        tiempo = 0;
        bombasDetectatas = 0;
        botonReinicio.setOnAction(event -> {
            reiniciar();
        });
        timeline.play();
        contadorBombasTexto = new Text("Bombas: 0");
        HBox panelSuperior = new HBox(10, tiempoTexto, botonReinicio, contadorBombasTexto);
        panelSuperior.setAlignment(Pos.CENTER);
        panelSuperior.setPadding(new Insets(10));
        return panelSuperior;
    }

    private void reiniciar(){
        tiempo = 0;
        bombasDetectatas = 0;
        tiempoTexto.setText("Tiempo: 0");
        contadorBombasTexto.setText("Bombas: 0");
        timeline.stop();
        timeline.play();
        panelBuscaminas.setCenter(crearTablero());
    }

    private GridPane crearTablero() {
        Buscaminas buscaminas = new Buscaminas(dificultad.getFilas(), dificultad.getColumnas(), dificultad.getBombas());
        buscaminas.generarTablero();
        this.grid = new GridPane();
        for (int fila = 0; fila < dificultad.getFilas(); fila++) {
            for (int columna = 0; columna < dificultad.getColumnas(); columna++) {
                StackPane celda = crearBoton(fila, columna, buscaminas);
                grid.add(celda, columna, fila);
            }
        }
        return grid;
    }

    private StackPane crearBoton(int fila, int columna, Buscaminas buscaminas) {
        Rectangle rect = new Rectangle(
                Configuracion.ANCHO_CASILLA,
                Configuracion.ALTO_CASILLA
        );
        rect.setFill(COLOR_BOTON);
        rect.setStroke(COLOR_BORDE);
        rect.setStrokeWidth(1);
        Text texto = new Text("");
        StackPane celda = new StackPane(rect, texto);
        agregarEventosBoton(celda, rect, fila, columna, buscaminas);
        return celda;
    }

    private void agregarEventosBoton(StackPane celda, Rectangle rect, int fila, int columna, Buscaminas buscaminas) {

        celda.setOnMouseEntered(event -> {
            boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
            boolean estaRevelada = buscaminas.estaRevelada(fila, columna);
            if(!estaBloqueada && !estaRevelada) {
                rect.setFill(COLOR_HOVER);
            }
        });

        // Restaurar color al salir
        celda.setOnMouseExited(event -> {
            boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
            boolean estaRevelada = buscaminas.estaRevelada(fila, columna);
            if(!estaBloqueada && !estaRevelada) {
                rect.setFill(COLOR_BOTON);
            }
        });

        celda.setOnMousePressed(event -> {
            boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
            boolean estaRevelada = buscaminas.estaRevelada(fila, columna);
            if(!estaBloqueada && !estaRevelada) {
                rect.setFill(COLOR_PRESIONADO);
            }
        });

        celda.setOnMouseReleased(event -> {
            boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
            boolean estaRevelada = buscaminas.estaRevelada(fila, columna);
            if(!estaBloqueada && !estaRevelada) {
                rect.setFill(COLOR_HOVER);
            }
        });

        // Mostrar número aleatorio al hacer clic
        celda.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
                if(!estaBloqueada){
                    buscaminas.actualizarReveladoCelda(fila, columna);
                    if(buscaminas.getTablero()[fila][columna].getNumero()==-1){
                        timeline.stop();
                        Stage dialogo = new Stage();
                        dialogo.initOwner(buscaminasScene.getWindow());
                        dialogo.initModality(Modality.APPLICATION_MODAL);

                        Label lblNombre = new Label("Ooohhh...\nNo has podido ganar.\n¿Quieres volver a intentarlo?");

                        Button btnEnviar = new Button("¡Vale!");

                        btnEnviar.setOnAction(event2 -> {
                            dialogo.close();
                            Scene pantallaInicial = crearPantallaInicial();
                            this.stage.setScene(pantallaInicial);
                            this.stage.setTitle("Buscaminas - Interfaz");
                            this.stage.setResizable(false);
                            this.stage.show();
                        });

                        dialogo.setOnCloseRequest(event2 -> {
                            dialogo.close();
                            Scene pantallaInicial = crearPantallaInicial();
                            this.stage.setScene(pantallaInicial);
                            this.stage.setTitle("Buscaminas - Interfaz");
                            this.stage.setResizable(false);
                            this.stage.show();
                        });

                        VBox dialogRoot = new VBox(lblNombre, btnEnviar);
                        dialogRoot.setSpacing(10);
                        dialogRoot.setStyle("-fx-padding: 20; -fx-alignment: center;");

                        Scene escenaDialogo = new Scene(dialogRoot, 300, 150);
                        dialogo.setScene(escenaDialogo);
                        dialogo.setTitle("¡Paquete!");
                        dialogo.showAndWait();
                    }
                    int auxBombasDetectatas = 0;
                    for (int filaAux = 0; filaAux < dificultad.getFilas(); filaAux++) {
                        for (int columnaAux = 0; columnaAux < dificultad.getColumnas(); columnaAux++) {
                            boolean estaRevelada = buscaminas.estaRevelada(filaAux, columnaAux);
                            if(estaRevelada) {
                                StackPane celdaAux = (StackPane) grid.getChildren().get(filaAux * dificultad.getColumnas() + columnaAux);
                                Rectangle auxRect = (Rectangle) celdaAux.getChildren().get(0);
                                Text auxTexto = (Text) celdaAux.getChildren().get(1);
                                int numero = buscaminas.obtenerPosicion(filaAux, columnaAux);
                                auxTexto.setText(String.valueOf(numero));
                                Color colorTexto = ColorNumero.obtenerColorFx(numero);
                                auxTexto.setFill(colorTexto);
                                auxTexto.setFont(Font.font(Configuracion.ALTO_CASILLA * 0.75));
                                auxRect.setFill(Configuracion.BLANCO_FX);
                            }
                            boolean auxEstaBloqueada = buscaminas.estaBloqueada(filaAux, columnaAux);
                            if(auxEstaBloqueada){
                                auxBombasDetectatas++;
                            }
                            bombasDetectatas = auxBombasDetectatas;
                            contadorBombasTexto.setText("Bombas: " + auxBombasDetectatas);
                        }
                    }
                    if(buscaminas.estaResuelto()){
                        timeline.stop();
                        Stage dialogo = new Stage();
                        dialogo.initOwner(buscaminasScene.getWindow());
                        dialogo.initModality(Modality.APPLICATION_MODAL); // Bloquear interacciones con la pantalla principal

                        Label lblNombre = new Label("Dificultad: "+dificultad+"\nTiempo: "+tiempo+" segundos.\nIngresa tu nombre:");
                        TextField campoNombre = new TextField();
                        Button btnEnviar = new Button("Enviar");

                        btnEnviar.setOnAction(event2 -> {
                            String nombre = campoNombre.getText();
                            String[] registro = {dificultad.toString(), nombre, tiempo+""};
                            boolean registroValido = dashboard.nuevoRegistro(registro);
                            if(registroValido) {
                                dialogo.close();
                                Scene pantallaInicial = crearPantallaInicial();
                                this.stage.setScene(pantallaInicial);
                                this.stage.setTitle("Buscaminas - Interfaz");
                                this.stage.setResizable(false);
                                this.stage.show();
                            } else {
                                lblNombre.setText("No has ingresado un nombre valido.");
                            }
                        });

                        dialogo.setOnCloseRequest(event2 -> {
                            dialogo.close();
                            Scene pantallaInicial = crearPantallaInicial();
                            this.stage.setScene(pantallaInicial);
                            this.stage.setTitle("Buscaminas - Interfaz");
                            this.stage.setResizable(false);
                            this.stage.show();
                        });

                        VBox dialogRoot = new VBox(lblNombre, campoNombre, btnEnviar);
                        dialogRoot.setSpacing(10);
                        dialogRoot.setStyle("-fx-padding: 20; -fx-alignment: center;");

                        Scene escenaDialogo = new Scene(dialogRoot, 300, 200);
                        dialogo.setScene(escenaDialogo);
                        dialogo.setTitle("¡Victoria!");
                        dialogo.showAndWait();
                    }
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                boolean estaRevelada = buscaminas.estaRevelada(fila, columna);
                if(!estaRevelada) {
                    buscaminas.actualizarBloqueoCelda(fila, columna);
                    boolean estaBloqueada = buscaminas.estaBloqueada(fila, columna);
                    if (estaBloqueada) {
                        rect.setFill(Color.YELLOW);
                        bombasDetectatas++;
                    } else {
                        rect.setFill(COLOR_BOTON);
                        bombasDetectatas--;
                    }
                    contadorBombasTexto.setText("Bombas: " + bombasDetectatas);
                }
            }
        });
    }
}