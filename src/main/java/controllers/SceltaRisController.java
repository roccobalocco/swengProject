package controllers;

import data.CandidatoDAOImpl;
import data.ClassicaDAOImpl;
import data.ReferendumDAOImpl;
import data.VotazioneDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.Classica;
import models.Referendum;
import models.Risultati;
import models.Votazione;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SceltaRisController implements Initializable {
    private final Alert a = new Alert(Alert.AlertType.ERROR);
    private final List<Classica> lc = new LinkedList<>();
    private final List<Referendum> lr = new LinkedList<>();
    @FXML
    ListView<String> votazioniListView;

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<List<? extends Votazione>> lt;
        try {
            lt = VotazioneDAO.getAllVotazioni();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(Classica c : (List<Classica>) lt.get(0))
            if(c.fineVotazione())
                lc.add(c);
        for(Referendum r : (List<Referendum>) lt.get(1))
            if(r.fineVotazione())
                lr.add(r);

        for(Classica c : lc)
            votazioniListView.getItems().addAll(c.toString());
        for(Referendum r : lr)
            if(r.fineVotazione())
                votazioniListView.getItems().addAll(r.toString());
    }

    @FXML
    public void goBack() throws IOException{
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaAdmin.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Scelta risultati Votazione");
        primaryStage.setResizable(true);
        ((Stage) votazioniListView.getScene().getWindow()).close();
        primaryStage.show();
    }

    @FXML
    public void go() throws IOException {
        int ix = votazioniListView.getSelectionModel().getSelectedIndex();
        if(ix == -1){
            a.setContentText("Seleziona una votazione dalla lista");
            a.showAndWait();
        }else{
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/aggiuntaVoti.fxml")));
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            if(ix < lc.size()){//É Classica
                Classica c = lc.get(ix);
                ClassicaDAOImpl.getInstance().setAppoggio(c);
                primaryStage.setTitle("Aggiungi voti a Classica");
            }else{//É Referendum
                Referendum r = lr.get(ix - lc.size());
                ReferendumDAOImpl.getInstance().setAppoggio(r);
                primaryStage.setTitle("Aggiungi voti a Referendum");
            }
            primaryStage.setResizable(true);
            ((Stage) votazioniListView.getScene().getWindow()).close();
            primaryStage.show();
        }
    }

    @FXML
    public void printRisultati() throws IOException {
        Alert infos = new Alert(Alert.AlertType.CONFIRMATION);
        int ix = votazioniListView.getSelectionModel().getSelectedIndex();
        infos.setContentText("Sicuro di voler ottenere i risultati?");
        Optional<ButtonType> o = infos.showAndWait();
        String path;
        if(o.isPresent() && ix != -1) {
            Risultati r;
            if (ix >= lc.size()) { //referendum
                ReferendumDAOImpl.getInstance().setAppoggio(lr.get(ix - lc.size()));
                r = new Risultati(lr.get(ix - lc.size()));
                path = "C://Users/Pietro/IdeaProjects/swengProject/PDFResult/" + lr.get(ix - lc.size()).descrizione.replaceAll("[ -/'\"]", "_") +
                        lr.get(ix - lc.size()).getScadenza().replaceAll("[ -/'\"]", "_") + ".pdf";
            } else { //Classica
                ClassicaDAOImpl.getInstance().setAppoggio(lc.get(ix));
                if (lc.get(ix).whichType() == 2) {
                    r = new Risultati(lc.get(ix),
                            CandidatoDAOImpl.getInstance().getMapG(),
                            CandidatoDAOImpl.getInstance().getMapP());
                } else {
                    r = new Risultati(ClassicaDAOImpl.getInstance().getAppoggio(),
                            CandidatoDAOImpl.getInstance().getMapG());
                }
                path = "C://Users/Pietro/IdeaProjects/swengProject/PDFResult/" + lc.get(ix).descrizione.replaceAll("[ -/'\"]", "_") +
                        lc.get(ix).getScadenza().replaceAll("[ -/'\"]", "_") + ".pdf";
            }

            if (r.printRisultati("C:/Users/Pietro/IdeaProjects/swengProject/PDFResult/")){
                infos.setAlertType(Alert.AlertType.INFORMATION);
                infos.setContentText("PDF salvato in Desktop/PDFResult");
            }else{
                infos.setAlertType(Alert.AlertType.WARNING);
                infos.setContentText("Errore nel salvataggio pdf in /swengProject/PDFResult/");
            }
            infos.showAndWait();
            Util.showResult(path);
        }
    }

}
