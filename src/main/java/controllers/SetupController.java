package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import models.Admin;

import java.net.URL;
import java.util.*;

/**
 * @author Piemme
 */
public class SetupController implements Initializable {
    @FXML
    Text benvenutoText;

    @FXML
    public void aggiungi(){

    }

    @FXML
    public void modifica(){

    }

    @FXML
    public void elimina(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        benvenutoText.setText("Benvenuto " + Admin.getInstance().toString());
    }
}