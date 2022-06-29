package controllers;

import data.ClassicaDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import models.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ElettoreController implements Initializable {
    @FXML
    Text benvenutoText;
    @FXML
    ListView<String> votationListView;

    final Alert a = new Alert(Alert.AlertType.NONE);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        benvenutoText.setText("Benvenut*: \n" + Elettore.getInstance().toString());

        List<List<? extends Votazione>> l = ClassicaDAOImpl.getInstance().getAllVotazioni();
        List<Classica> lc = (List<Classica>) l.get(0);
        for (Classica c : lc){
            if(!c.fineVotazione()) {
                System.out.println("Classica: " +  c);
                votationListView.getItems().add(c.toString());
            }
        }
        List<Referendum> lr = (List<Referendum>) l.get(1);
        for(Referendum c : lr) {
            if(!c.fineVotazione()) {
                System.out.println("Referendum: " + c);
                votationListView.getItems().add(c.toString());
            }
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
