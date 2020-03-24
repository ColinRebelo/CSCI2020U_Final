package main.java.sample;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
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
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

import java.io.*;

public class UI {
    DatabaseConnection db = new DatabaseConnection();
    Client client = new Client();
    Movie[] movies;
    Group group = new Group();
    Button[] buttonArray = new Button[15];
    Button[][] showTs = new Button[3][15];

    public void start(Stage primaryStage) throws Exception {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Scene scene = new Scene(scrollPane,900,700);        //create scene

        getMovies(null);
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

    private void getMovies(String title) {
        movies = client.getMovies();                    //pulls full list
        //pull with title
        if (title != null) {
            if (title.equalsIgnoreCase("")) { return; }
            int i = 0;
            Movie[] temp = new Movie[movies.length];
            for (Movie movie : movies) {
                if (movie.getTitle().equalsIgnoreCase(title)) {
                    temp[i] = movie;
                    i++;
                }
            }
            movies = temp;
        }
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
            String title = searchBar.getText();          //get whats in bar
            getMovies(title);                            //pulls new movies
            UpdateScreen(scene);                         //updates screen
        });
    }

    private void UpdateScreen(Scene scene) {
        group.getChildren().clear();                     //reset group
        //re-call all functions
        setBackground();
        setTitle(scene);
        makeSearchBar(scene);
        setMovieGrid(scene);
    }

    private void setMovieGrid(Scene scene) {
        //sets grid layout coordinates and spacing
        GridPane grid = new GridPane();
        grid.setLayoutX(scene.getWidth()/18);
        grid.setLayoutY(scene.getHeight()/3.5);
        grid.setHgap(scene.getWidth()/5.5);
        grid.setVgap(scene.getHeight()/5);

        double imageSize = (scene.getWidth()/6);                //set size of images
        int btnCount = 0;                                       //current button
        int totalMovies = checkTotalMovies();                        //length of the movies
        System.out.println(totalMovies);

        //for every movie
        for (int i=0; i<5; i++) {                           //total rows
            if (btnCount >= totalMovies) {break;}           //returns if movies is less than 15
            for (int j=0; j<3; j++) {                       //total columns
                if (btnCount >= totalMovies) {break;}
                //creates film selection
                VBox newBox = new VBox(5);
                Button newFilm = new Button(movies[btnCount].getTitle());
                newFilm.setFont(Font.font("Palatino",FontPosture.ITALIC,12));
                newFilm.setPrefSize(imageSize,20);
                buttonArray[btnCount] = newFilm;

                Image newImage = db.getImage(movies[btnCount]); //movie Image

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
            btn.setOnAction(e -> { setButtons(btn); });
        }
    }

    private int checkTotalMovies() {
        int i = 0;
        for (Movie movie : movies) {
            if (movie != null) {i++;}
        }
        return i;
    }

    private void setButtons(Button btn) {
        double[] pos = {80,400,315,320};                //x initial, y initial, x inc, y inc
        int i = 0;

        //matches button to an index i
        for (Button iter : buttonArray) {
            if (iter == btn) { break; }
            i++;
        }

        //sets position of info
        VBox vbox = new VBox();
        vbox.setLayoutX(pos[0]+pos[2]*((i%3))-50);
        vbox.setLayoutY(pos[1]+pos[3]*(Math.ceil(i/3)));
        vbox.setAlignment(Pos.CENTER);

        //sets position of show times in info
        HBox hbox = new HBox(5);

        String[] genres = movies[i].getGenres();

        //creates labels
        Label genre = new Label("Genre: "+ genres[0]);
        Label time = new Label("Time: " + movies[i].getRuntime());
        Label show = new Label("Show Times:");

        //creates show times buttons
        showTs[0][i] = new Button(movies[i].getShowTimes(0));
        showTs[1][i] = new Button(movies[i].getShowTimes(1));
        showTs[2][i] = new Button(movies[i].getShowTimes(2));
        String showT1 = movies[i].getShowTimes(0);
        String showT2 = movies[i].getShowTimes(1);
        String showT3 = movies[i].getShowTimes(2);

        showTs[0][i].setOnAction(e -> { showSeats(showT1); });
        showTs[1][i].setOnAction(e -> { showSeats(showT2); });
        showTs[2][i].setOnAction(e -> { showSeats(showT3); });

        hbox.getChildren().addAll(showTs[0][i],showTs[1][i],showTs[2][i]);
        vbox.getChildren().addAll(genre,time,show,hbox);
        group.getChildren().add(vbox);
    }

    public void showSeats(String showTime) {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_LEFT);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 600, 350, Color.PINK);
        Stage seats = new Stage();
        seats.setTitle("Seating for " + " at " + showTime);

        pane.setPadding(new Insets(5));
        pane.setHgap(10);
        pane.setVgap(10);

        root.setCenter(pane);
        seats.setScene(scene);
        seats.show();
    }

}