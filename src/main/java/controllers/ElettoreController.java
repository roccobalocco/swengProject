package controllers;

import data.ClassicaDAOImpl;
import data.VotazioneDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import models.Elettore;

import java.net.URL;
import java.util.ResourceBundle;

public class ElettoreController implements Initializable {
    @FXML
    Text benvenutoText;
    @FXML
    ListView votationListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        benvenutoText.setText("Benvenuto " + Elettore.getInstance().toString());
        //insert cose in votationListView
        //ClassicaDAOImpl.getInstance().getAllVotazioni();
    }

    @FXML
    public void vota(){

    }
}
