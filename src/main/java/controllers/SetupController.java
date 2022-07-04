package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Admin;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Piemme
 */
public class SetupController implements Initializable {
    @FXML
    Text benvenutoText;

    @FXML
    public void aggiungi() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/inserimento.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin menu");
        primaryStage.setResizable(true);
        primaryStage.show();
        ((Stage) benvenutoText.getScene().getWindow()).close();
    }

    @FXML
    public void mod_el() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/mod-del.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin menu");
        primaryStage.setResizable(true);
        primaryStage.show();
        ((Stage) benvenutoText.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        benvenutoText.setText("Benvenuto " + Admin.getInstance().toString());
    }

    @FXML
    public void risultati() {
    }
}