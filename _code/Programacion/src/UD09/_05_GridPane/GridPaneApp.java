/*
 * Copyright (C) 2023 David Martínez (wwww.martinezpenya.es|ieseduardoprimo.es)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package UD09._05_GridPane;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author David Martínez (wwww.martinezpenya.es|ieseduardoprimo.es)
 */
public class GridPaneApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox vbox = new VBox();

        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10));
        gp.setHgap(4);
        gp.setVgap(8);

        VBox.setVgrow(gp, Priority.ALWAYS);

        Label lblTitle = new Label("Tiquet de soporte");

        Label lblEmail = new Label("Correo-e");
        TextField tfEmail = new TextField();

        Label lblPriority = new Label("Prioridad");
        ObservableList<String> priorities
                = FXCollections.observableArrayList("Media", "Alta", "Baja");
        ComboBox<String> cbPriority = new ComboBox<>(priorities);

        Label lblProblem = new Label("Problema");
        TextField tfProblem = new TextField();

        Label lblDescription = new Label("Descripción");
        TextArea taDescription = new TextArea();

        gp.add(lblTitle, 1, 1);  // empty item at 0,0
        gp.add(lblEmail, 0, 2);
        gp.add(tfEmail, 1, 2);
        gp.add(lblPriority, 0, 3);
        gp.add(cbPriority, 1, 3);
        gp.add(lblProblem, 0, 4);
        gp.add(tfProblem, 1, 4);
        gp.add(lblDescription, 0, 5);
        gp.add(taDescription, 1, 5);

        Separator sep = new Separator(); // hr

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding(new Insets(10));

        Button saveButton = new Button("Guardar");
        Button cancelButton = new Button("Cancelar");

        buttonBar.setButtonData(saveButton, ButtonBar.ButtonData.OK_DONE);
        buttonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);

        buttonBar.getButtons().addAll(saveButton, cancelButton);

        vbox.getChildren().addAll(gp, sep, buttonBar);

        //para mostrar las lineas de estructura descomenta la siguiente linea
        //gp.setGridLinesVisible(true);
        
        Scene scene = new Scene(vbox);

        primaryStage.setTitle("Grid Pane App");
        primaryStage.setScene(scene);
        primaryStage.setWidth(736);
        primaryStage.setHeight(414);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
