module es.masanz.ut5.buscaminas.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports es.masanz.ut5.buscaminas.util;
    opens es.masanz.ut5.buscaminas.util to javafx.fxml;

    exports es.masanz.ut5.buscaminas.app;
    opens es.masanz.ut5.buscaminas.app to javafx.fxml;
}