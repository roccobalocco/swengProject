package controllers;

import data.ClassicaDAOImpl;
import data.ReferendumDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Classica;
import models.Referendum;
import util.AntiInjection;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class InserimentoVotazioniController implements Initializable {

    private final Alert a = new Alert(Alert.AlertType.ERROR);

    @FXML
    CheckBox referendumCheckBox, quorumCheckBox, assolutaCheckBox;
    @FXML
    ChoiceBox<String> tipoChoiceBox;
    @FXML
    TextArea descrizioneTextArea;
    @FXML
    DatePicker scadenzaDatePicker;
    @FXML
    Button candidatiButton, referendumButton;
    @FXML
    Label infoLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ENTRIAMO NELL'INSERIMENTO VOTAZIONE");
        ObservableList<String> votationsList = FXCollections.observableArrayList();
        tipoChoiceBox.setItems(votationsList);
        votationsList.addAll("Ordinale", "Categorico senza preferenze", "Categorico con preferenze");
        tipoChoiceBox.setValue(votationsList.get(1));
        infoLabel.setText("Togliere la spunta a 'Referendum' per inserire un altro tipo di votazione");

        assolutaCheckBox.setDisable(true);
        tipoChoiceBox.setDisable(true);
        candidatiButton.setDisable(true);
    }

    @FXML
    public void disable(){
        if(referendumCheckBox.isSelected()){
            referendumCheckBox.setText("Referendum");
            infoLabel.setText("Togliere la spunta a 'Referendum' per inserire un altro tipo di votazione");

            assolutaCheckBox.setDisable(true);
            tipoChoiceBox.setDisable(true);
            candidatiButton.setDisable(true);

            quorumCheckBox.setDisable(false);
            referendumButton.setDisable(false);
        }else{
            infoLabel.setText("Mettere la spunta a 'Votazione' per inserire un referndum");
            referendumCheckBox.setText("Votazione");

            assolutaCheckBox.setDisable(false);
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

    private boolean isOkVot(){
        if(descrizioneTextArea.getText().length() < 5){
            a.setContentText("Inserisci una descrizione valida, piú lunga di 5 caratteri...");
            a.show();
            return false;
        }
        return isScadenzaOk();
    }

    private boolean isScadenzaOk() {
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
    public void inserisciCandidati() throws IOException {
        if(isOkVot()) {
            Classica c = new Classica(AntiInjection.bonify(descrizioneTextArea.getText()),scadenzaDatePicker.getValue(), isOrdinale(), isPreferenziale(), ClassicaDAOImpl.getInstance().getNextId(), assolutaCheckBox.isSelected());
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setContentText("Sicuro di voler procedere all'inserimento dei candidati per la votazione (non ancora inserita): " + c);
            Optional<ButtonType> r = a.showAndWait();
            if(r.isPresent())
                if(r.get() == ButtonType.OK) {
                    ClassicaDAOImpl.getInstance().setAppoggio(c);
                    Stage primaryStage = new Stage();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/inserimentoCandidati.fxml")));
                    Scene scene = new Scene(root);

                    primaryStage.setScene(scene);
                    primaryStage.setTitle("Inserimento Candidati Votazione");
                    primaryStage.setResizable(true);
                    ((Stage) scadenzaDatePicker.getScene().getWindow()).close();
                    primaryStage.show();
                }
        }
        a.setAlertType(Alert.AlertType.ERROR);
    }

    private boolean isPreferenziale() {
        if(!isOrdinale())
            return tipoChoiceBox.getSelectionModel().getSelectedItem().equals("Categorico con preferenze");
        return false;
    }

    private boolean isOrdinale() {
        return tipoChoiceBox.getSelectionModel().getSelectedItem().equals("Ordinale");
    }

    private boolean isOkRef(){
        if(descrizioneTextArea.getText().length() <= 5){
            a.setContentText("Inserisci una descrizione valida, piú lunga di 5 caratteri...");
            a.show();
            return false;
        }
        return isScadenzaOk();
    }
    @FXML
    public void inserisciReferendum() throws IOException {
        if(isOkRef()) {
            Referendum ref = new Referendum(AntiInjection.bonify(descrizioneTextArea.getText()), scadenzaDatePicker.getValue(), quorumCheckBox.isSelected(), ReferendumDAOImpl.getInstance().getNextId());
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setContentText("Sicuro di voler procedere all'inserimento del Referendum: " + ref);
            Optional<ButtonType> r = a.showAndWait();
            if(r.isPresent())
                if(r.get() == ButtonType.OK) {
                    if (ReferendumDAOImpl.getInstance().addVotazione(ref)) {
                        a.setContentText("Inserimento eseguito con successo!");
                        a.setAlertType(Alert.AlertType.INFORMATION);
                        goBack();
                        a.show();
                    } else {
                        a.setContentText("Inserimento fallito");
                        a.setAlertType(Alert.AlertType.INFORMATION);
                        a.show();
                    }
                }
        }
        a.setAlertType(Alert.AlertType.ERROR);
    }

}
