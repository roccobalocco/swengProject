import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try{
            Parent root = FXMLLoader.load(Main.class.getResource("/views/login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.setResizable(false);
            primaryStage.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

