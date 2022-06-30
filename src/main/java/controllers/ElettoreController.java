package controllers;

import data.ClassicaDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import models.*;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ElettoreController implements Initializable {
    @FXML
    Text benvenutoText;
    @FXML
    ListView<String> votationListView;

    final Alert a = new Alert(Alert.AlertType.NONE);

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        benvenutoText.setText(Util.welcome() + " elettore \n" + Elettore.getInstance().toString());
        try {
            List<List<? extends Votazione>> l = ClassicaDAOImpl.getInstance().getAllVotazioni();
            List<Classica> lc = (List<Classica>) l.get(0);
            for (Classica c : lc)
                if(!c.fineVotazione())
                    votationListView.getItems().add(c.toString());

            List<Referendum> lr = (List<Referendum>) l.get(1);
            for(Referendum r : lr)
                if(!r.fineVotazione())
                    votationListView.getItems().add(r.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void vota(){
        if(Elettore.getInstance().puoVotare()){
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setContentText("L'Elettore puó votare");
        }else{
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("L'Elettore non puó votare perché é troppo giovane");
        }
        a.show();
    }
}
