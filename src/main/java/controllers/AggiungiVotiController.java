package controllers;

import com.itextpdf.text.DocumentException;
import data.CandidatoDAOImpl;
import data.ClassicaDAOImpl;
import data.ReferendumDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.*;
import models.Risultati;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AggiungiVotiController{
    private final Alert a = new Alert(Alert.AlertType.ERROR);
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
        try{
            int ix = gruppoComboBox.getSelectionModel().getSelectedIndex();
            int voti = Integer.parseInt(gruppoTextField.getText());
            if(voti < 1 || ix == -1)
                throw new NumberFormatException();
            ClassicaDAOImpl.getInstance().addVoti(lg.get(ix), voti);
        }catch(NumberFormatException e){
            a.setContentText("Inserisci un numero di voti valido");
            a.show();
        }
    }
    @FXML
    public void insertTotale() {
        try {
            int v = Integer.parseInt(gruppoTextField.getText());
            if (v < 1)
                throw new NumberFormatException();

            if (gruppoTextField.isDisable()) //referendum
                ReferendumDAOImpl.getInstance().setTot(v);
            else //classica
                ClassicaDAOImpl.getInstance().setTot(v);
        }catch(NumberFormatException e){
            a.setContentText("Inserisci un numero di voti valido");
            a.show();
        } catch (IOException e) {
            throw new RuntimeException("Problema con aggiunta totale votanti possibili -/- " + e);
        }
    }

    @FXML
    public void printRisultati() throws DocumentException, IOException {
        Alert infos = new Alert(Alert.AlertType.CONFIRMATION);
        infos.setContentText("Sicuro di voler ottenere i risultati?");
        Optional<ButtonType> o = infos.showAndWait();
        if(o.isPresent()) {
            Risultati r;
            if (gruppoTextField.isDisable()) { //referendum
                r = new Risultati(ReferendumDAOImpl.getInstance().getAppoggio());
            } else { //Classica
                if (ClassicaDAOImpl.getInstance().getAppoggio().whichType() == 2) {
                    System.out.println(CandidatoDAOImpl.getInstance().getMapG());
                    System.out.println(CandidatoDAOImpl.getInstance().getMapP());
                    r = new Risultati(ClassicaDAOImpl.getInstance().getAppoggio(),
                            CandidatoDAOImpl.getInstance().getMapG(),
                            CandidatoDAOImpl.getInstance().getMapP());
                } else {
                    System.out.println(CandidatoDAOImpl.getInstance().getMapG());
                    r = new Risultati(ClassicaDAOImpl.getInstance().getAppoggio(),
                            CandidatoDAOImpl.getInstance().getMapG());
                }
            }

            if (!r.printRisultati("/swengProject/PDFResult/")){
                infos.setAlertType(Alert.AlertType.WARNING);
                infos.setContentText("Errore nel salvataggio pdf in /swengProject/PDFResult/");
            }else{
                infos.setAlertType(Alert.AlertType.INFORMATION);
                infos.setContentText("PDF salvato in /swengProject/PDFResult/");
            }
            infos.showAndWait();
            showResult();
        }
    }

    private void showResult() {
        //TODO
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
        try{
            int ix = candidatoComboBox.getSelectionModel().getSelectedIndex();
            int voti = Integer.parseInt(candidatoTextField.getText());
            if(voti < 1 || ix == -1)
                throw new NumberFormatException();
            ClassicaDAOImpl.getInstance().addVoti(lp.get(ix), voti);
        }catch(NumberFormatException e){
            a.setContentText("Inserisci un numero di voti valido");
            a.show();
        }
    }

    @FXML
    public void insertRef(){
        try{
            int si, no, bianca;
            si = Integer.parseInt(siTextField.getText());
            no = Integer.parseInt(noTextField.getText());
            bianca = Integer.parseInt(biancaTextField.getText());

            if(si < 0 || no < 0 || bianca < 0)
                throw new NumberFormatException();

            ReferendumDAOImpl.getInstance().addVoti(1, si);
            ReferendumDAOImpl.getInstance().addVoti(0, no);
            ReferendumDAOImpl.getInstance().addVoti(-1, bianca);
        }catch(NumberFormatException e){
            a.setContentText("Inserisci un numero di voti valido");
            a.show();
        } catch (IOException e) {
            throw new RuntimeException("ERRORE in addVoti -/- " + e);
        }
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
