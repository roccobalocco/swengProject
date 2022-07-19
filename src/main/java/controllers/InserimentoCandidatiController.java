package controllers;

import data.CandidatoDAOImpl;
import data.ClassicaDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Classica;
import models.Gruppo;
import models.Persona;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class InserimentoCandidatiController implements Initializable {

    private List<Gruppo> lg = new LinkedList<>();
    private final Alert a = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    CheckBox gruppoCheckBox;
    @FXML
    TextField nomeTextField;
    @FXML
    ListView<String> gruppoListView;

    private Classica getCurrentVotazione() {
        return ClassicaDAOImpl.getInstance().getAppoggio();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(getCurrentVotazione() != null) {
            try {
                if(!ClassicaDAOImpl.getInstance().addVotazione())
                    throw new IllegalAccessError("Si é tentato di aggiungere una votazione settata in appoggio senza averla prima settata");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (getCurrentVotazione().whichType() != 2) {
                gruppoCheckBox.setDisable(true);
                gruppoListView.setDisable(true);
            } else {
                gruppoListView.setDisable(true);
                try {
                    updateGroup();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            throw new IllegalCallerException("metodo chiamato senza aver settato ClassicaDAOImpl.setAppoggio con una votazione");
        }
    }

    private void updateGroup() throws IOException {
        while (!gruppoListView.getItems().isEmpty())
            gruppoListView.getItems().remove(0);

        Classica c = ClassicaDAOImpl.getInstance().getAppoggio();
        this.lg = CandidatoDAOImpl.getInstance().getGruppi(c);
        for (Gruppo g : lg)
            gruppoListView.getItems().add(g.toString());
        //    System.out.println("Aggiunta di " + g);

    }

    @FXML
    public void disable() {
        gruppoListView.setDisable(gruppoCheckBox.isSelected());
    }

    @FXML
    public void inserisci() throws IOException {
        if(nomeTextField.getText().length() < 2) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Inserisci un nome valido");
            a.show();
        }else if(gruppoCheckBox.isSelected()){
            //INSERISCI GRUPPO
            Gruppo g = new Gruppo(CandidatoDAOImpl.getInstance().getNextIdGruppo(), Util.bonify(nomeTextField.getText()));
            ClassicaDAOImpl.getInstance().addGruppo(getCurrentVotazione(), g);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Inserito Partito/Gruuppo " + g +" \n Per la votazione " + getCurrentVotazione().toString());
            a.show();
            updateGroup();
        }else{
            //INSERISCI CANDIDATO
            int ix = gruppoListView.getSelectionModel().getSelectedIndex();
            //System.out.println("Selezionato il numero: " + ix);
            if(ix != -1){
                Persona p = new Persona(CandidatoDAOImpl.getInstance().getNextIdPersona(), Util.bonify(nomeTextField.getText()), lg.get(ix).getId());

                CandidatoDAOImpl.getInstance().addPersona(getCurrentVotazione(), lg.get(ix), p);

                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Inserito Persona " + p +" \n Per la votazione " + getCurrentVotazione().toString());
                a.show();

                updateGroup();
            }else{
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("Seleziona un Partito/Gruppo");
                a.show();
            }
        }
    }

    @FXML
    public void goBack() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaAdmin.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin menu");
        primaryStage.setResizable(false);
        ((Stage) nomeTextField.getScene().getWindow()).close();
        primaryStage.show();
    }
}
