package controllers;

import data.ReferendumDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Referendum;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class inserimento1Controller implements Initializable {

    private final Alert a = new Alert(Alert.AlertType.ERROR);
    @FXML
    CheckBox referendumCheckBox, quorumCheckBox;
    @FXML
    ChoiceBox<String> tipoChoiceBox;
    @FXML
    TextArea descrizioneTextArea;
    @FXML
    DatePicker scadenzaDatePicker;
    @FXML
    Button candidatiButton, referendumButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoChoiceBox.setDisable(true);
        candidatiButton.setDisable(true);
    }

    @FXML
    public void disable(){
        if(referendumCheckBox.isSelected()){
            tipoChoiceBox.setDisable(true);
            candidatiButton.setDisable(true);

            quorumCheckBox.setDisable(false);
            referendumButton.setDisable(false);
        }else{
            tipoChoiceBox.setDisable(false);
            candidatiButton.setDisable(false);

            quorumCheckBox.setDisable(true);
            referendumButton.setDisable(true);
        }
    }

    @FXML
    public void goBack() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaAdmin.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin menu");
        primaryStage.setResizable(true);
        primaryStage.show();
        ((Stage) descrizioneTextArea.getScene().getWindow()).close();
    }

    @FXML
    public void inserisciCandidati() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/inserimentoCandidati.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin menu");
        primaryStage.setResizable(true);
        primaryStage.show();
        ((Stage) descrizioneTextArea.getScene().getWindow()).close();
    }

    private boolean isOk(){
        if(descrizioneTextArea.getText().length() <= 5){
            a.setContentText("Inserisci una descrizione valida, piú lunga di 5 caratteri...");
            a.show();
            return false;
        }
        if(scadenzaDatePicker.getValue() == null){
            a.setContentText("Inserisci una scadenza...");
            a.show();
            return false;
        }
        if(scadenzaDatePicker.getValue().isBefore(LocalDate.now())){
            a.setContentText("Inserisci una scadenza valida...");
            a.show();
            return false;
        }
        return true;
    }
    @FXML
    public void inserisciReferendum() throws IOException {
        if(isOk()) {
            if (ReferendumDAOImpl.getInstance().addVotazione(new Referendum(descrizioneTextArea.getText(), scadenzaDatePicker.getValue(), quorumCheckBox.isSelected(), ReferendumDAOImpl.getInstance().getNextId()))){
                a.setContentText("Inserimento eseguito con successo!");
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.show();
                goBack();
            }else{
                a.setContentText("Inserimento fallito");
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.show();
            }
        }
    }

}
