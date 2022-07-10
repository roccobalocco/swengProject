package controllers;

import data.ClassicaDAOImpl;
import data.ReferendumDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static data.VotazioneDAO.getAllVotazioni;

public class ElettoreController implements Initializable {
    @FXML
    Text benvenutoText;
    @FXML
    ListView<String> votationListView;

    private List<Classica> lc = new LinkedList<>();
    private List<Referendum> lr = new LinkedList<>();

    final Alert a = new Alert(Alert.AlertType.NONE);

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        benvenutoText.setText(Util.welcome() + " elettore \n" + Elettore.getInstance().toString());
        try {
            List<List<? extends Votazione>> l = getAllVotazioni();
            lc = (List<Classica>) l.get(0);
            List<Classica> toRemoveCla = new LinkedList<>();
            for (Classica c : lc)
                if (!c.fineVotazione() && ClassicaDAOImpl.getInstance().canVote(c))
                    votationListView.getItems().add(c.toString());
                else
                    toRemoveCla.add(c);
            lc.removeAll(toRemoveCla);
            List<Referendum>toRemoveRef = new LinkedList<>();

            lr = (List<Referendum>) l.get(1);
            for (Referendum r : lr)
                if (!r.fineVotazione() && ReferendumDAOImpl.getInstance().canVote(r))
                    votationListView.getItems().add(r.toString());
                else
                    toRemoveRef.add(r);
            lr.removeAll(toRemoveRef);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void vota() throws IOException {
        //recupera votazione selezionata
        int ix = votationListView.getSelectionModel().getSelectedIndex();
        System.out.println("Selezionato indice: " + ix);
        if(ix != -1) {
            if (Elettore.getInstance().puoVotare()) {
                a.setAlertType(Alert.AlertType.CONFIRMATION);
                if(ix >= lc.size() ) {
                    ReferendumDAOImpl.getInstance().setAppoggio(lr.get(ix - lc.size()));
                    System.out.println("Appoggio settato per: " + ReferendumDAOImpl.getInstance().getAppoggio());
                    a.setContentText("É sicuro di voler votare per: " + lr.get(ix - lc.size()));
                    Optional<ButtonType> r = a.showAndWait();
                    if(r.isPresent())
                        if(r.get() == ButtonType.OK)
                            goTo(0);
                }else{
                    ClassicaDAOImpl.getInstance().setAppoggio((lc.get(ix)));
                    a.setContentText("É sicuro di voler votare per: " + lc.get(ix));
                    Optional<ButtonType> r = a.showAndWait();
                    if(r.isPresent())
                        if(r.get() == ButtonType.OK)
                            goTo(1);
                }

            } else {
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("L'Elettore non puó votare perché é troppo giovane");
                a.show();
            }
        }else{
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Selezioni una votazione");
            a.show();
        }
    }

    private void goTo(int where) throws IOException, IllegalArgumentException {
        switch(where){
            case 0 ->{ //Referendum
                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/votaReferendum.fxml")));
                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                primaryStage.setTitle(ReferendumDAOImpl.getInstance().getAppoggio().descrizione);
                primaryStage.setResizable(true);
                primaryStage.show();
            }
            case 1 ->{ //Classica
                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/votaClassica.fxml")));
                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                primaryStage.setTitle(ClassicaDAOImpl.getInstance().getAppoggio().descrizione);
                primaryStage.setResizable(true);
                primaryStage.show();
            }
            default -> throw new IllegalArgumentException("Non ci sono corrisponendeze per goTo -> " + where);
        }
        ((Stage) votationListView.getScene().getWindow()).close();
    }

}
