package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InserimentoCandidatiController implements Initializable {
    @FXML
    CheckBox gruppoCheckBox;
    @FXML
    TextField nomeTextField;
    @FXML
    ListView gruppoListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void disable() {
    }

    @FXML
    public void inserisci() {
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
