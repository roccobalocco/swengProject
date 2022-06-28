package controllers;

import data.CandidatoDAOImpl;
import data.ClassicaDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import models.Votazione;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class InserimentoCandidatiController implements Initializable {

    private final Alert a = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    CheckBox gruppoCheckBox;
    @FXML
    TextField nomeTextField;
    @FXML
    ListView<String> gruppoListView;

    private Classica getCurrentVotazione(){
        return (Classica) ((Stage) gruppoCheckBox.getScene().getWindow()).getUserData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Gruppo g : CandidatoDAOImpl.getInstance().getGruppi()){
            System.out.println("Classica: " + g.toString());
            gruppoListView.getItems().add(g.toString());
        }
        ClassicaDAOImpl.getInstance().addVotazione(getCurrentVotazione());
    }

    @FXML
    public void disable() {
        gruppoListView.setDisable(gruppoCheckBox.isSelected());
    }

    @FXML
    public void inserisci() {
        //TODO inserisici il candidato in corrispondenza della votazione

        a.setContentText("Inserito candidato X \n Per la votazione " + getCurrentVotazione().toString());
    }

    @FXML
    public void goBack() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/inserimento.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Inserisci Votazione/Referendum");
        primaryStage.setResizable(true);
        primaryStage.show();
        ((Stage) nomeTextField.getScene().getWindow()).close();
    }
}
