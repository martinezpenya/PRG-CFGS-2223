module es.martinezpenya {
    requires javafx.controls;
    requires javafx.fxml;

    opens es.martinezpenya to javafx.fxml;
    exports es.martinezpenya;
}
