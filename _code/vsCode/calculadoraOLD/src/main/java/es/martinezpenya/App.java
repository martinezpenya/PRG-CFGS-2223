package es.martinezpenya;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("CalculadoraNew"), 410, 225);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        Locale.setDefault(Locale.ENGLISH);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("es.martinezpenya.i18n.calculadora");     
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"), resourceBundle);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}