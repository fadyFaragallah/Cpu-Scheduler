package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        window=primaryStage;
        window.setTitle("Scheduler");
        window.setScene(new Scene(root, 1100, 650));
        window.initStyle(StageStyle.TRANSPARENT);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
