package main.java.sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class StartServer extends Application {
    @Override
    public void start(Stage primaryStage) {
        Server server = new Server();
        server.start();
    }

    public static void main(String[] args) { launch(args); }
}
