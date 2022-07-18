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
import java.nio.file.Paths;
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
        if(o.isPresent() && ix != -1 && o.get() == ButtonType.OK){
            System.out.println("Entro in ottenimento");
            Risultati r;
            if (ix >= lc.size()) { //referendum
                ReferendumDAOImpl.getInstance().setAppoggio(lr.get(ix - lc.size()));
                r = new Risultati(lr.get(ix - lc.size()));
                //noinspection DuplicateExpressions
                path = Paths.get(".").toAbsolutePath().normalize() + "/PDFResult/" +
                        Util.bonify2(lr.get(ix - lc.size()).descrizione) +
                        Util.bonify2(lr.get(ix - lc.size()).getScadenza()) + ".pdf";
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
                //noinspection DuplicateExpressions
                path = Paths.get(".").toAbsolutePath().normalize() + "/PDFResult/" + Util.bonify2(lc.get(ix).descrizione) +
                        Util.bonify2(lc.get(ix).getScadenza()) + ".pdf";
            }

            //noinspection DuplicateExpressions
            if (r.printRisultati(Paths.get(".").toAbsolutePath().normalize() + "/PDFResult/")){
                infos.setAlertType(Alert.AlertType.INFORMATION);
                infos.setContentText("PDF salvato in Desktop/PDFResult");
            }else{
                infos.setAlertType(Alert.AlertType.WARNING);
                infos.setContentText("Errore nel salvataggio pdf in /swengProject/PDFResult/");
            }
            infos.showAndWait();
            Util.showResult(path);
            deleteVotation(infos);
        }
    }

    private void deleteVotation(Alert infos) throws IOException {
        int ix = votazioniListView.getSelectionModel().getSelectedIndex();
        infos.setAlertType(Alert.AlertType.CONFIRMATION);
        infos.setContentText("Cancellare la votazione dal sistema?");
        Optional<ButtonType> o = infos.showAndWait();
        if(o.isPresent() && o.get() == ButtonType.OK)
            if (ix >= lc.size()) //referendum
                ReferendumDAOImpl.getInstance().deleteVotazione(ReferendumDAOImpl.getInstance().getAppoggio().getId());
            else //Classica
                ClassicaDAOImpl.getInstance().deleteVotazione(ClassicaDAOImpl.getInstance().getAppoggio().getId());
        else
            System.out.println("Votazione non eliminata");
    }

}
