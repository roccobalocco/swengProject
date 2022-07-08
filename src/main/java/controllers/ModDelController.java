package controllers;

import data.ClassicaDAOImpl;
import data.ReferendumDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.*;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static data.VotazioneDAO.getAllVotazioni;

public class ModDelController implements Initializable {

    private List<Classica> lc = new LinkedList<>();
    private List<Referendum> lr = new LinkedList<>();
    private final Alert a  = new Alert(Alert.AlertType.ERROR);

    @FXML
    CheckBox eliminaCheckBox;
    @FXML
    Text benvenutoText;
    @FXML
    ListView<String> votationListView;
    @FXML
    Button goButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goButton.setText("Aggiorna");
        benvenutoText.setText(Util.welcome() + " amministratore: \n" + Admin.getInstance().toString());
        updateList();
    }

    @FXML
    public void updateButton(){
        if(eliminaCheckBox.isSelected())
            goButton.setText("Elimina");
        else
            goButton.setText("Aggiorna");
    }

    @SuppressWarnings("unchecked")
    private void updateList(){
        while(votationListView.getItems().size() > 0)
            votationListView.getItems().remove(0);

        try {
            List<List<? extends Votazione>> l = getAllVotazioni();
            lc = (List<Classica>) l.get(0);
            List<Classica> toRemoveCla = new LinkedList<>();
            for (Classica c : lc)
                if (!c.fineVotazione())
                    votationListView.getItems().add(c.toString());
                else
                    toRemoveCla.add(c);
            lc.removeAll(toRemoveCla);
            List<Referendum> toRemoveRef = new LinkedList<>();

            lr = (List<Referendum>) l.get(1);
            for (Referendum r : lr)
                if (!r.fineVotazione())
                    votationListView.getItems().add(r.toString());
                else
                    toRemoveRef.add(r);
            lr.removeAll(toRemoveRef);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void go() throws IOException {
        int ix = votationListView.getSelectionModel().getSelectedIndex();
        if(ix != -1){
            if(eliminaCheckBox.isSelected()){ //eliminare
                if(ix < lc.size())
                    ClassicaDAOImpl.getInstance().deleteVotazione(lc.get(ix).getId());
                else
                    ReferendumDAOImpl.getInstance().deleteVotazione(lr.get(ix - lc.size()).getId());
                updateList();
            }else{ //modificare
                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/modifica.fxml")));
                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                if(ix < lc.size()) {
                    primaryStage.setTitle("Modifica Classica");
                    ClassicaDAOImpl.getInstance().setAppoggio(lc.get(ix));
                }else {
                    primaryStage.setTitle("Modifica Referendum");
                    ReferendumDAOImpl.getInstance().setAppoggio(lr.get(ix - lc.size()));
                }
                primaryStage.setResizable(true);
                primaryStage.show();
                ((Stage)  benvenutoText.getScene().getWindow()).close();
            }
        }else{
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Selezionare una votazione!");
            a.show();
        }
    }

}
