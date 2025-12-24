package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;


    @Override
    public void start(Stage primaryStage) throws Exception {
        controller = new Controller();
        controller.connect();


        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Karate-Club Database");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        controller.disconnect();
    }

    public static void main(String[] args) {
        launch(args);
    }
}