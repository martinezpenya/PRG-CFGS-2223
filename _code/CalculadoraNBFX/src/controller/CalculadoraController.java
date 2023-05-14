/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.Operaciones;

/**
 * FXML Controller class
 *
 * @author David Martínez (wwww.martinezpenya.es|www.ieseduardoprimo.es)
 */
public class CalculadoraController implements Initializable {

    @FXML
    private TextField txtOperador1;
    @FXML
    private TextField txtOperador2;
    @FXML
    private TextField txtResultado;
    @FXML
    private RadioButton rbResta;
    @FXML
    private RadioButton rbMultiplicacion;
    @FXML
    private RadioButton rbDivision;
    @FXML
    private Button btnOperar;
    @FXML
    private Button btnSalir;
    @FXML
    private RadioButton rbSuma;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup tgRadio = new ToggleGroup();
        rbSuma.setToggleGroup(tgRadio);
        rbMultiplicacion.setToggleGroup(tgRadio);
        rbResta.setToggleGroup(tgRadio);
        rbDivision.setToggleGroup(tgRadio);
    }

    @FXML
    private void operar(ActionEvent event) {
        try {
            double op1 = Double.parseDouble(this.txtOperador1.getText());
            double op2 = Double.parseDouble(this.txtOperador2.getText());
            Operaciones op = new Operaciones(op1, op2);
            if (this.rbSuma.isSelected()) {
                this.txtResultado.setText(String.valueOf(op.suma()));
            } else if (this.rbResta.isSelected()) {
                this.txtResultado.setText(String.valueOf(op.resta()));
            } else if (this.rbMultiplicacion.isSelected()) {
                this.txtResultado.setText(String.valueOf(op.multiplicacion()));
            } else if (this.rbDivision.isSelected()) {
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
    private void salir(ActionEvent event) {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }

}
