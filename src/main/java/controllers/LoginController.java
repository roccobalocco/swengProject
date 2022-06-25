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

import javax.imageio.IIOException;
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

    Alert a = new Alert(Alert.AlertType.NONE);

    private boolean existInDb(String cf, String psw){
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

    @FXML
    public void login() throws IIOException {
        String userString = cf_TextField.getText();
        String pswString = psw_TextField.getText();
        if (CF.check(userString)){
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Inserire un Codice Fiscale valido");
            a.show();
        }else if(pswString == null || pswString.equals("")){
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Inserire una password valida");
            a.show();
        }else if(existInDb(userString, pswString)) {
			logged(new Stage());
            ((Stage) cf_TextField.getScene().getWindow()).close();
        }else {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Login non effettuato " + userString);
            a.show();
        }

    }

}