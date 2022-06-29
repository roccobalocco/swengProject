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
                if(ClassicaDAOImpl.getInstance().addVotazione())
                    System.out.println("VOTAZIONE INSERITA");
                else
                    System.out.println("VOTAZIONE NON INSERITA");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (getCurrentVotazione().whichType() != 2) {
                gruppoCheckBox.setDisable(true);
                gruppoListView.setDisable(false);
            } else {
                gruppoListView.setDisable(false);
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
        this.lg = CandidatoDAOImpl.getInstance().getGruppi(ClassicaDAOImpl.getInstance().getAppoggio());
        for (Gruppo g : lg){
            System.out.println("Classica: " + g.toString());
            gruppoListView.getItems().add(g.toString());
        }
    }

    @FXML
    public void disable() {
        gruppoListView.setDisable(gruppoCheckBox.isSelected());
    }

    @FXML
    public void inserisci() throws IOException {
        //TODO inserisici il candidato in corrispondenza della votazione
        if(nomeTextField.getText().length() < 3) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Inserisci un nome valido");
            a.show();
        }else if(gruppoCheckBox.isSelected()){
            Gruppo g = new Gruppo(CandidatoDAOImpl.getInstance().getNextIdGruppo(), nomeTextField.getText());
            ClassicaDAOImpl.getInstance().addGruppo(getCurrentVotazione(), g);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Inserito Partito/Gruuppo " + g +" \n Per la votazione " + getCurrentVotazione().toString());
            a.show();
            updateGroup();
        }else{
            int ix = gruppoListView.getSelectionModel().getSelectedIndex();
            if(ix < 0 || ix > this.lg.size()){
                Persona p = new Persona(CandidatoDAOImpl.getInstance().getNextIdPersona(), nomeTextField.getText(), lg.get(ix).getId());
                ClassicaDAOImpl.getInstance().addPersona(getCurrentVotazione(), lg.get(ix), p);
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Inserito Persona " + p +" \n Per la votazione " + getCurrentVotazione().toString());
                a.show();
            }else{
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("Seleziona un Partito/Gruppo");
                a.show();
            }
        // Returns the index of the currently selected items in a single-selection mode
        }
    }

    @FXML
    public void goBack() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/inserimento.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Inserisci Votazione/Referendum");
        primaryStage.setResizable(true);
        ((Stage) nomeTextField.getScene().getWindow()).close();
        primaryStage.show();
    }
}
