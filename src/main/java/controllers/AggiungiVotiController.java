package controllers;

import data.CandidatoDAOImpl;
import data.ClassicaDAOImpl;
import data.ReferendumDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Classica;
import models.Gruppo;
import models.Persona;
import models.Risultati;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AggiungiVotiController{
    private Risultati r; //TODO = new Risultati();
    private boolean s = false;
    private List<Gruppo> lg = null;
    private List<Persona> lp = null;
    @FXML
    ComboBox<String> gruppoComboBox, candidatoComboBox;
    @FXML
    TextField gruppoTextField, candidatoTextField, siTextField, noTextField, biancaTextField, totaleTextField;
    @FXML
    Button getCandidatiButton, insertGruppoButton, insertCandidatiButton, insertReferendumButton, insertTotButton;

    @FXML
    public void setup(){
        if(!s){
            candidatoComboBox.setDisable(true); candidatoTextField.setDisable(true); insertCandidatiButton.setDisable(true);
            switch (((Stage) gruppoComboBox.getScene().getWindow()).getTitle() ){
                case "Aggiungi voti a Classica" -> {
                    Classica c = ClassicaDAOImpl.getInstance().getAppoggio();
                    if(c.isAssoluta()) {
                        totaleTextField.setDisable(true);
                        insertTotButton.setDisable(true);
                    }
                    siTextField.setDisable(true); noTextField.setDisable(true); biancaTextField.setDisable(true);
                    insertReferendumButton.setDisable(true);
                    try {
                        lg = CandidatoDAOImpl.getInstance().getGruppi(c);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    for(Gruppo g : lg)
                        gruppoComboBox.getItems().addAll(g.toString());

                }
                case "Aggiungi voti a Referendum" -> {
                    if (ReferendumDAOImpl.getInstance().getAppoggio().hasQuorum()){
                        totaleTextField.setDisable(true);
                        insertTotButton.setDisable(true);
                    }
                    gruppoComboBox.setDisable(true); gruppoTextField.setDisable(true);
                    insertGruppoButton.setDisable(true); getCandidatiButton.setDisable(true);
                }
                default -> System.out.println("ERRORE IN ENTRATA Stage");
            }
            s = true;
            System.out.println("DIOMERDA");
        }
    }

    @FXML
    public void insertVotiGruppo() {
    }
    @FXML
    public void insertTotale() {
    }
    @FXML
    public void printRisultati() {
    }
    @FXML
    public void goBack() throws IOException{
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaRisultati.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Scelta risultati Votazione");
        primaryStage.setResizable(true);
        ((Stage) gruppoComboBox.getScene().getWindow()).close();
        primaryStage.show();
    }
    @FXML
    public void insertVotiCandidato() {
    }
    @FXML
    public void updateCandidati() {
        int ix = gruppoComboBox.getSelectionModel().getSelectedIndex();
        Gruppo g = lg.get(ix);
        candidatoComboBox.setDisable(false); candidatoTextField.setDisable(false);
        try {
            lp = CandidatoDAOImpl.getInstance().getPersone(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(Persona p : lp)
            gruppoComboBox.getItems().addAll(p.toString());
    }

    //TODO chiedi se eliminare la votazione dal db dopo aver stampato i risultati

}
