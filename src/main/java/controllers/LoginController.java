package controllers;

import data.AdminDAOImpl;
import data.ElettoreDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.util.*;

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
        System.out.println("Checkbox admin pari a :" + admin_CheckBox.getText());
        if(admin_CheckBox.getText().equals("true")){
            return AdminDAOImpl.getInstance().isAdmin(cf, psw);
        }else{
            return ElettoreDAOImpl.getInstance().isElettore(cf, psw);
        }
    }

    public void logged(Stage primaryStage) {
        //TO-DO
        /*try {
            Parent root = FXMLLoader.load(getClass().getResource("logged.fxml"));
            //Scene scene = new Scene(root);

            //primaryStage.setScene(scene);
            //primaryStage.setTitle("Logged");
            //primaryStage.setResizable(false);
            //primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    public void login() throws IIOException {
        String userString = cf_TextField.getText();
        String pswString = psw_TextField.getText();
        if(existInDb(userString, pswString)) {
			a.setAlertType(Alert.AlertType.INFORMATION);
			a.setContentText("Login effettuato con successo " + userString);
			a.show();
            //logged(new Stage());
        }else {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Login non effettuato " + userString);
            a.show();
        }

    }

}