/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package es.martinezpenya;

import java.net.URL;
import java.util.ResourceBundle;

import es.martinezpenya.model.Operaciones;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author David Martínez (wwww.martinezpenya.es|ieseduardoprimo.es)
 */
public class CalculadoraNewController implements Initializable {

    @FXML
    private Button btnCalcular;
    @FXML
    private RadioButton rdSuma;
    @FXML
    private RadioButton rdResta;
    @FXML
    private RadioButton rdMult;
    @FXML
    private RadioButton rdDivi;
    @FXML
    private TextField txtOp1;
    @FXML
    private TextField txtOp2;
    @FXML
    private TextField txtResultado;
    @FXML
    private Button btnSalir;
    @FXML
    private MenuItem mnuClose;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO+
        ToggleGroup tgRadio = new ToggleGroup();
        rdSuma.setToggleGroup(tgRadio);
        rdMult.setToggleGroup(tgRadio);
        rdResta.setToggleGroup(tgRadio);
        rdDivi.setToggleGroup(tgRadio);
    }

    @FXML
    private void action(ActionEvent event) {
        try {
            double op1 = Double.parseDouble(this.txtOp1.getText());
            double op2 = Double.parseDouble(this.txtOp2.getText());
            Operaciones op = new Operaciones(op1, op2);
            if (this.rdSuma.isSelected()) {
                this.txtResultado.setText(String.valueOf(op.suma()));
            } else if (this.rdResta.isSelected()) {
                this.txtResultado.setText(String.valueOf(op.resta()));
            } else if (this.rdMult.isSelected()) {
                this.txtResultado.setText(String.valueOf(op.multiplicacion()));
            } else if (this.rdDivi.isSelected()) {
                if (op2 != 0) {
                    this.txtResultado.setText(String.valueOf(op.division()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("El operador 2 no puede ser 0.");
                    alert.showAndWait();
                }
            }
        } catch (NumberFormatException numberFormatException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Formato incorrecto de algun operando");
            alert.showAndWait();
        }
    }

    @FXML
    private void close(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

}
