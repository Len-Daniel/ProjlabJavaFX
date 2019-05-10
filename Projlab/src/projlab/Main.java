package projlab;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent game = FXMLLoader.load(getClass().getResource("Game.fxml"));
        Scene gameScene = new Scene(game);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Game");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}