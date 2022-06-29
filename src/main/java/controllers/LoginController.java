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
import util.CF;

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
        System.out.println("Checkbox admin pari a :" + admin_CheckBox.isSelected());
        if(admin_CheckBox.isSelected()){
            return AdminDAOImpl.getInstance().isAdmin(cf, psw);
        }else{
            return ElettoreDAOImpl.getInstance().isElettore(cf, psw);
        }
    }

    private void logged(Stage primaryStage) {
        try {
            if(admin_CheckBox.isSelected()) {
                System.out.println("Siamo arrivati prima del caricamente GUI Admin");
                Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaAdmin.fxml")));
                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                primaryStage.setTitle("Admin menu");
                primaryStage.setResizable(true);
                primaryStage.show();
            }else{
                System.out.println("Siamo arrivati prima del caricamento GUI elettore");
                Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaElettore.fxml")));
                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                primaryStage.setTitle("Elettore menu");
                primaryStage.setResizable(true);
                primaryStage.show();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Applica un controllo alla checkbox Admin ed alle due stringhe che l'utente inserisce, bloccando tentativi di accessi con password nulle o con codici fiscali non validi
     */
    @FXML
    public void login() throws NoSuchAlgorithmException, IOException {
        String userString = cf_TextField.getText().toUpperCase();
        String pswString = psw_TextField.getText();
        if (!CF.check(userString)){
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Inserire un Codice Fiscale valido");
            a.show();
        }else if(pswString == null || pswString.equals("")){
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Inserire una password valida");
            a.show();
        }else if(existInDb(userString, PassEncTech2.toHexString(PassEncTech2.getSHA(pswString)))) {
			logged(new Stage());
            ((Stage) cf_TextField.getScene().getWindow()).close();
        }else {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Login non effettuato " + userString);
            a.show();
        }
        //System.out.println("The password encripted is:\n" + PassEncTech2.toHexString(PassEncTech2.getSHA(pswString)));
        //Theangel23 --> 9675e7a7ad9d1c09d08e60b1ca898085faaeaa59c795cdd635b6f47ec1ed7ab9
    }


    private static class PassEncTech2 {
        public static byte[] getSHA(String input) throws NoSuchAlgorithmException
        {
            /* MessageDigest instance for hashing using SHA256 */
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            /* digest() method called to calculate message digest of an input and return array of byte */
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }

        public static String toHexString(byte[] hash)
        {
            /* Convert byte array of hash into digest */
            BigInteger number = new BigInteger(1, hash);

            /* Convert the digest into hex value */
            StringBuilder hexString = new StringBuilder(number.toString(16));

            /* Pad with leading zeros */
            while (hexString.length() < 32)
            {
                hexString.insert(0, '0');
            }

            return hexString.toString();
        }

        /* Driver code */

    }
}