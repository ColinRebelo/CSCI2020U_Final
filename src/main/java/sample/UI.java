package main.java.sample;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class UI {
    Movie[] movies;
    Group group = new Group();
    Button[] buttonArray = new Button[15];

    public void start(Stage primaryStage) throws Exception {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Scene scene = new Scene(scrollPane,900,700);        //create scene

        movies = getMovies(null);
        setBackground();
        setTitle(scene);
        makeSearchBar(scene);
        setMovieGrid(scene);

        scrollPane.setContent(group);                                    //puts group into scrollable page

        primaryStage.setTitle("Film Finder");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Movie[] getMovies(String title) {
        //pull normally
        if (title == null) {}
        //pull with title
        else{}
        return null;
    }

    private void setBackground() {
        BackgroundFill bgFill = new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(bgFill);
        VBox vbox = new VBox();
        vbox.setBackground(background);
        vbox.setMinHeight(1800);
        vbox.setMinWidth(900);
        group.getChildren().add(vbox);
    }

    private void setTitle(Scene scene) {
        Label title = new Label("Film Finder");
        title.setFont(Font.font("Palatino", FontWeight.BOLD, FontPosture.ITALIC,70));
        title.setLayoutX(scene.getWidth()/3.45);
        group.getChildren().add(title);
    }

    private void makeSearchBar(Scene scene) {
        HBox hbox = new HBox();
        Button btn = new Button("Search Films");
        btn.setFont(Font.font("Palatino",FontPosture.ITALIC,12));
        TextField searchBar = new TextField();
        searchBar.setFont(Font.font("Palatino",FontPosture.ITALIC,12));
        searchBar.setPrefSize(scene.getWidth()/2, 20);
        hbox.getChildren().addAll(searchBar,btn);
        hbox.setLayoutX(scene.getWidth()/4.8);
        hbox.setLayoutY(scene.getHeight()/6);
        group.getChildren().add(hbox);

        btn.setOnAction(e -> {
            String title = searchBar.getText();     //get whats in bar
            movies = getMovies(title);              //pulls new movies
            UpdateScreen();                         //updates screen
        });
    }

    private void UpdateScreen() {

    }

    private void setMovieGrid(Scene scene) throws FileNotFoundException {
        GridPane grid = new GridPane();
        grid.setLayoutX(scene.getWidth()/18);
        grid.setLayoutY(scene.getHeight()/3.5);
        grid.setHgap(scene.getWidth()/5.5);
        grid.setVgap(scene.getHeight()/5);

        double imageSize = (scene.getWidth()/6);                //set size of images
        int btnCount = 0;

        //for every movie
        for (int i=0; i<5; i++) {               //total rows
            for (int j=0; j<3; j++) {            //total columns
                //creates film selection
                VBox newBox = new VBox(5);
                Button newFilm = new Button("temp");                    //put name in here
                newFilm.setFont(Font.font("Palatino",FontPosture.ITALIC,12));
                newFilm.setPrefSize(imageSize,20);
                buttonArray[btnCount] = newFilm;                             //add to array
                Image newImage = new Image(new FileInputStream("assets/wal.png"));          //throw in url

                //formats in grid
                ImageView newView = new ImageView(newImage);
                newView.setFitHeight(imageSize);
                newView.setFitWidth(imageSize);
                newBox.getChildren().addAll(newView,newFilm);
                grid.add(newBox,j, i);
                btnCount++;
            }
        }
        group.getChildren().add(grid);
        //set all buttons
        for (Button btn : buttonArray) {
            btn.setOnAction(e -> { setButtons(btn,scene);});
        }
    }

    private void setButtons(Button btn,Scene scene) {
        double[] pos = {90,400,315,320};                //x initial, y initial, x inc, y inc
        int showTimes = 3;                              //get movie total play times
        int i = 0;

        //matches button to an index i
        for (Button iter : buttonArray) {
            if (iter == btn) { break; }
            i++;
        }

        //sets position of showtimes
        VBox vbox = new VBox();
        vbox.setLayoutX(pos[0]+pos[2]*((i%3)));
        vbox.setLayoutY(pos[1]+pos[3]*(Math.ceil(i/3)));

        //creates labels
        for (int j=0; j<showTimes; j++) {
            Label timeSlot = new Label("12 Jan 19:30");
            vbox.getChildren().add(timeSlot);
        }
        group.getChildren().add(vbox);
    }
}

