package controllers;

import data.CandidatoDAOImpl;
import data.ClassicaDAOImpl;
import data.ElettoreDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Classica;
import models.Gruppo;
import models.Persona;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class VotaClasController implements Initializable {

    final Alert a = new Alert(Alert.AlertType.ERROR);
    List<Gruppo> lg = null;
    List<Persona> lp = null;
    @FXML
    Button selButton, giuParButton, suParButton, giuCanButton, suCanButton;
    @FXML
    Text classicaText;
    @FXML
    ListView<String> partitiListView, candidatiListView;
    @FXML
    Label candidatiLabel, partitiLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Classica c = ClassicaDAOImpl.getInstance().getAppoggio();
        classicaText.setText(c.toString());
        try {
            lg = CandidatoDAOImpl.getInstance().getGruppi(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(Gruppo g : lg)
            partitiListView.getItems().addAll(g.toString());

        switch(ClassicaDAOImpl.getInstance().getAppoggio().whichType()){
            case 0 -> { //ORDINALE
                partitiLabel.setText("Seleziona un partito e muovilo con le frecce");
                candidatiLabel.setText("");
                giuCanButton.setDisable(true);
                suCanButton.setDisable(true);
                selButton.setDisable(true);
            }
            case 1 -> { //CATEGORICA no pref
                partitiLabel.setText("Seleziona un partito e vota");
                candidatiLabel.setText("");
                giuCanButton.setDisable(true);
                suCanButton.setDisable(true);
                giuParButton.setDisable(true);
                suParButton.setDisable(true);
                selButton.setDisable(true);
            }
            case 2 -> { //CATEGORICA si pref
                partitiLabel.setText("Seleziona un partito");
                candidatiLabel.setText("Ordina i candidati del partito scelto");
                giuParButton.setDisable(true);
                suParButton.setDisable(true);
            }
        }
    }
    @FXML
    public void vota() throws IOException {
        Classica c = ClassicaDAOImpl.getInstance().getAppoggio();
        switch(c.whichType()){
            case 0 -> { //ORDINALE
                Map<Gruppo, Integer> voti = Util.getVotiOrdinale(lg);
                for(Gruppo g : lg)
                    CandidatoDAOImpl.getInstance().insertVoto(c, g, voti.get(g));
                ElettoreDAOImpl.getInstance().vota(c);
                goBack();
            }
            case 1 -> { //CATEGORICO SENZA PREFERENZA
                int ix = partitiListView.getSelectionModel().getSelectedIndex();
                Gruppo g = lg.get(ix);
                CandidatoDAOImpl.getInstance().insertVoto(c, g, 1);
                ElettoreDAOImpl.getInstance().vota(c);
                goBack();
            }
            case 2 -> { //CATEGORICO CON PREFERENZA
                int ix = partitiListView.getSelectionModel().getSelectedIndex();
                if(ix == -1){
                    a.setContentText("Seleziona un partito");
                    a.show();
                }else {
                    Gruppo g = lg.get(ix);
                    CandidatoDAOImpl.getInstance().insertVoto(c, g, 1);
                    Map<Persona, Integer> voti = Util.getVotiPreferenziale(lp);
                    for (Persona p : lp)
                        CandidatoDAOImpl.getInstance().insertVoto(p, voti.get(p));
                    ElettoreDAOImpl.getInstance().vota(c);
                    goBack();
                }
            }
            default -> System.out.println("ERRORE IN VOTAZIONE");
        }
    }

    @FXML
    public void bianca() throws IOException {
        Classica c = ClassicaDAOImpl.getInstance().getAppoggio();
        ElettoreDAOImpl.getInstance().vota(c);
        goBack();
    }

    private void updatePartito(){
        partitiListView.getItems().remove(0, partitiListView.getItems().toArray().length);
        for(Gruppo g : lg)
            partitiListView.getItems().addAll(g.toString());
    }

    @FXML
    public void suPartito() {
        if(partitiListView.getSelectionModel().getSelectedIndex() > 0){
            int ix = partitiListView.getSelectionModel().getSelectedIndex();
            Gruppo tmp = lg.get(ix-1);
            Gruppo[] ag = lg.toArray(new Gruppo[0]);
            ag[ix-1] = ag[ix];
            ag[ix] = tmp;
            lg = Arrays.stream(ag).toList();
            updatePartito();
        }
    }

    @FXML
    public void giuPartito() {
        if(partitiListView.getSelectionModel().getSelectedIndex() < lg.size() - 1){
            int ix = partitiListView.getSelectionModel().getSelectedIndex();
            Gruppo tmp = lg.get(ix+1);
            Gruppo[] ag = lg.toArray(new Gruppo[0]);
            ag[ix+1] = ag[ix];
            ag[ix] = tmp;
            lg = Arrays.stream(ag).toList();
            updatePartito();
        }
    }

    @FXML
    public void suCandidato() {
        if(candidatiListView.getSelectionModel().getSelectedIndex() > 0){
            int ix = candidatiListView.getSelectionModel().getSelectedIndex();
            Persona tmp = lp.get(ix-1);
            Persona[] ag = lp.toArray(new Persona[0]);
            ag[ix-1] = ag[ix];
            ag[ix] = tmp;
            lp = Arrays.stream(ag).toList();
            updateCandidati();
        }
    }

    @FXML
    public void giuCandidato() {
        if(candidatiListView.getSelectionModel().getSelectedIndex() < lp.size() - 1){
            int ix = candidatiListView.getSelectionModel().getSelectedIndex();
            Persona tmp = lp.get(ix+1);
            Persona[] ag = lp.toArray(new Persona[0]);
            ag[ix+1] = ag[ix];
            ag[ix] = tmp;
            lp = Arrays.stream(ag).toList();
            updateCandidati();
        }
    }

    public void updateCandidati() {
        while(!candidatiListView.getItems().isEmpty())
            candidatiListView.getItems().remove(0);
        for(Persona p : lp)
            candidatiListView.getItems().addAll(p.toString());
    }

    @FXML
    public void getCandidati(){
        while(!candidatiListView.getItems().isEmpty())
            candidatiListView.getItems().remove(0);
        int ix = partitiListView.getSelectionModel().getSelectedIndex();
        Gruppo g = lg.get(ix);
        try {
            lp = CandidatoDAOImpl.getInstance().getPersone(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(Persona p : lp)
            candidatiListView.getItems().addAll(p.toString());
    }

    @FXML
    public void goBack() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaElettore.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Inserisci Votazione/Referendum");
        primaryStage.setResizable(true);
        ((Stage) candidatiLabel.getScene().getWindow()).close();
        primaryStage.show();
    }
}
