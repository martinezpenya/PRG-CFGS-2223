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
package UD09;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author David Martínez (wwww.martinezpenya.es|ieseduardoprimo.es)
 */
public class E08_CheckBox extends Application {

    private Parent createContent() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Creamos el CheckBox vacio
        CheckBox check1 = new CheckBox();
        //Añadimos texto una vez creado
        check1.setText("Coche");
        //Añadimos el CheckBox a la columna 0 fila 0
        grid.add(check1, 0, 0);

        //Creamos los CheckBox con texto
        CheckBox check2 = new CheckBox("Moto");
        grid.add(check2, 0, 1);
        //Hademos aparezca marcado por defecto
        CheckBox check3 = new CheckBox("A pie");
        check3.setSelected(true);
        grid.add(check3, 0, 2);

        //Podemos añadir imágenes a los CheckBox
        //ImageView imageCoche = new ImageView("UD09/coche.png");
        //check1.setGraphic(imageCoche);
        final String[] nombres = new String[]{"coche", "moto", "pie"};
        final Image[] imagenes = new Image[nombres.length];
        final ImageView[] iconos = new ImageView[nombres.length];
        final CheckBox[] checkBox = new CheckBox[nombres.length];

        for (int i = 0; i < nombres.length; i++) {
            final Image image = imagenes[i] = new Image("UD09/" + nombres[i] + ".png");
            final ImageView icon = iconos[i] = new ImageView();
            final CheckBox cb = checkBox[i] = new CheckBox(nombres[i]);
            cb.selectedProperty().addListener(
                    (ObservableValue<? extends Boolean> ov,
                            Boolean old_val, Boolean new_val) -> {
                        icon.setImage(new_val ? image : null);
                    });
        }

        return grid;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent(), 300, 200);
        stage.setScene(scene);

        stage.setTitle("Ejemplo CheckBox");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
