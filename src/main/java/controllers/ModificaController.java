package controllers;

import data.ClassicaDAOImpl;
import data.ReferendumDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Classica;
import models.Referendum;

import java.io.IOException;
import java.util.Objects;

public class ModificaController {

    boolean s = false;
    Alert a = new Alert(Alert.AlertType.ERROR);
    @FXML
    TextArea descrizioneTextArea;
    @FXML
    CheckBox quorumCheckBox, assolutaCheckBox;
    @FXML
    DatePicker scadenzaDatePicker;
    @FXML
    ChoiceBox<String> tipoChoiceBox;
    @FXML
    Label infoLabel;


    @SuppressWarnings("uncheccked")
    @FXML
    public void setup(){
        if(!s){
            switch (((Stage) infoLabel.getScene().getWindow()).getTitle() ){
                case "Modifica Classica" -> {
                    ObservableList<String> votationsList = FXCollections.observableArrayList();
                    tipoChoiceBox.setItems(votationsList);
                    votationsList.addAll("Ordinale", "Categorico senza preferenze", "Categorico con preferenze");
                    tipoChoiceBox.setValue(votationsList.get(1));

                    assolutaCheckBox.setSelected(ClassicaDAOImpl.getInstance().getAppoggio().isAssoluta());
                    quorumCheckBox.setDisable(true);
                    descrizioneTextArea.setText(ClassicaDAOImpl.getInstance().getAppoggio().descrizione);
                    infoLabel.setText(ClassicaDAOImpl.getInstance().getAppoggio().toString());
                    scadenzaDatePicker.setValue(ClassicaDAOImpl.getInstance().getAppoggio().getScadenzaLD());
                }
                case "Modifica Referendum" -> {
                    quorumCheckBox.setSelected(ReferendumDAOImpl.getInstance().getAppoggio().quorum);
                    assolutaCheckBox.setDisable(true);
                    tipoChoiceBox.setDisable(true);
                    descrizioneTextArea.setText(ReferendumDAOImpl.getInstance().getAppoggio().descrizione);
                    infoLabel.setText(ReferendumDAOImpl.getInstance().getAppoggio().toString());
                    scadenzaDatePicker.setValue(ReferendumDAOImpl.getInstance().getAppoggio().getScadenzaLD());
                }
                default -> System.out.println("ERRORE IN ENTRATA Stage");
            }
            s = true;
        }
    }

    @FXML
    public void modifica() throws IOException {
        switch (((Stage) infoLabel.getScene().getWindow()).getTitle() ){
            case "Modifica Classica" -> {
                String sel = tipoChoiceBox.getSelectionModel().getSelectedItem();
                Classica nc = new Classica(descrizioneTextArea.getText(),
                        scadenzaDatePicker.getValue(),
                        sel.equals("Ordinale"),
                        sel.equals("Categorico con preferenze"),
                        ClassicaDAOImpl.getInstance().getAppoggio().getId(), assolutaCheckBox.isSelected());
                if(ClassicaDAOImpl.getInstance().updateVotazione(nc)) {
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("Votazione aggiornata correttamente");
                }else{
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("Errore nell'aggiornamento");
                }
                a.showAndWait();
            }
            case "Modifica Referendum" -> {
                Referendum nr = new Referendum(descrizioneTextArea.getText(), scadenzaDatePicker.getValue(), quorumCheckBox.isSelected(), ReferendumDAOImpl.getInstance().getAppoggio().getId());
                if(ReferendumDAOImpl.getInstance().updateVotazione(nr)){
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("Votazione aggiornata correttamente");
                }else{
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("Errore nell'aggiornamento");
                }
                a.showAndWait();
            }
            default -> System.out.println("ERRORE IN modifica Button");
        }
        goBack();
    }

    @FXML
    public void goBack() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/mod-del.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Inserisci Votazione/Referendum");
        primaryStage.setResizable(true);
        ((Stage) infoLabel.getScene().getWindow()).close();
        primaryStage.show();
    }
}
