package business;

import javafx.application.Application;
import javafx.stage.Stage;
import presentation.GUI;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            GUI gui = new GUI();
            gui.start(primaryStage);
            GUIController guiController = new GUIController(gui);
            guiController.getControl();
            gui.getMyScene().getStylesheets().add(getClass().getResource("/business/application.css").toExternalForm());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}