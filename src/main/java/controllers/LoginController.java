package controllers;

import data.AdminDAOImpl;
import data.ElettoreDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Admin;
import models.Elettore;
import util.Util;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author Piemme
 */
public class LoginController {
    @FXML
    private TextField cf_TextField;
    @FXML
    private PasswordField psw_TextField;
    @FXML
    private CheckBox admin_CheckBox;

    final Alert a = new Alert(Alert.AlertType.NONE);

    private boolean existInDb(String cf, String psw) throws IOException {
        if(admin_CheckBox.isSelected())
            return AdminDAOImpl.getInstance().isAdmin(cf, psw);
        return ElettoreDAOImpl.getInstance().isElettore(cf, psw);
    }

    private void logged(Stage primaryStage) {
        try {
            if(admin_CheckBox.isSelected()) {
                Util.addAdminObs();
                System.out.println("Siamo arrivati prima del caricamento GUI Admin");
                Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaAdmin.fxml")));
                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                primaryStage.setTitle("Admin menu");
                primaryStage.setResizable(false);
                primaryStage.show();
                Admin.getInstance().update("[Login effettuato]");
            }else{
                Util.addElettoreObs();
                System.out.println("Siamo arrivati prima del caricamento GUI elettore");
                Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaElettore.fxml")));
                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                primaryStage.setTitle("Elettore menu");
                primaryStage.setResizable(false);
                primaryStage.show();
                Elettore.getInstance().update("[Login effettuato]");

            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Applica un controllo alla checkbox Admin ed alle due stringhe che l'utente inserisce,
     * bloccando tentativi di accessi con password nulle o con codici fiscali non validi
     */
    @FXML
    public void login() throws NoSuchAlgorithmException, IOException {
        String userString = cf_TextField.getText().toUpperCase();
        String pswString = psw_TextField.getText();
        if (!Util.check(userString)){
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Inserire un Codice Fiscale valido");
            a.show();
        }else if(pswString == null || pswString.equals("")){
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Inserire una password valida");
            a.show();
        }else if(existInDb(Util.bonify(userString), PassEncTech2.toHexString(PassEncTech2.getSHA(Util.bonify(pswString))))) {
			logged(new Stage());
            ((Stage) cf_TextField.getScene().getWindow()).close();
        }else {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Login non effettuato " + userString);
            a.show();
        }
        //System.out.println("The password is:\n" + PassEncTech2.toHexString(PassEncTech2.getSHA(pswString)));
        //Theangel23 --> 9675e7a7ad9d1c09d08e60b1ca898085faaeaa59c795cdd635b6f47ec1ed7ab9
    }


    private static class PassEncTech2 {
        public static byte[] getSHA(String input) throws NoSuchAlgorithmException
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }

        public static String toHexString(byte[] hash)
        {
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }

            return hexString.toString();
        }
    }
}