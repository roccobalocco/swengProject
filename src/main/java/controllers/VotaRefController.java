package controllers;

import data.ReferendumDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class VotaRefController implements Initializable {

    private final Alert a = new Alert(Alert.AlertType.CONFIRMATION);
    @FXML
    Text referendumText;

    private void vota(int v) throws IOException {
        a.setContentText("Sei sicuro della tua scelta?");
        Optional<ButtonType> r = a.showAndWait();
        if(r.isPresent())
            if(r.get() == ButtonType.OK) {
                ReferendumDAOImpl.getInstance().vota(v);
                this.goBack();
            }
    }

    private void goBack() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/views/sceltaElettore.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin menu");
        primaryStage.setResizable(true);
        primaryStage.show();
        ((Stage) referendumText.getScene().getWindow()).close();
    }

    @FXML
    public void si() throws IOException {
        vota(1);
    }

    @FXML
    public void bianca() throws IOException {
        vota(-1);
    }
    @FXML
    public void no() throws IOException {
        vota(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        referendumText.setText(ReferendumDAOImpl.getInstance().getAppoggio().toString());
    }
}
