package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.Risultati;

public class AggiungiVotiController{
    private Risultati r; //TODO = new Risultati();
    private boolean isLoad;
    @FXML
    ComboBox<String> gruppoComboBox, candidatoComboBox;
    @FXML
    TextField gruppoTextField, candidatoTextField, siTextField, noTextField, biancaTextField, totaleTextField;

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
    public void goBack() {
    }
    @FXML
    public void insertVotiCandidato() {
    }
    @FXML
    public void updateCandidati() {
    }
}
