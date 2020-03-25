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
import javafx.scene.shape.*;
import javafx.scene.text.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UI {
    DatabaseConnection db = new DatabaseConnection();
    Client client = new Client();
    Movie[] movies;
    Movie[] allMovies;
    Group group = new Group();
    Button[] buttonArray = new Button[15];
    Button[][] showTs = new Button[3][15];
    HashMap<String, Image> images = new HashMap<>();

    public void start(Stage primaryStage) throws Exception {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Scene scene = new Scene(scrollPane,900,700);        //create scene

        getMovies();
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

    private void getMovies() {
        allMovies = client.getMovies();                    //pulls full list
        movies = allMovies;
        System.out.println("Loading movie images...");
        for (Movie movie: allMovies) {
            Image poster = db.getImage(movie);
            images.put(movie.getId(), poster);
        }
    }

    private void searchMovies(String title) {
        if (title != null) {
            if (title.equalsIgnoreCase("")) {
                movies = allMovies;
            }
            int i = 0;
            Movie[] temp = new Movie[movies.length];
            for (Movie movie : allMovies) {
                if (movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    temp[i] = movie;
                    i++;
                }
            }
            movies = temp;
        }
    }

    private void setBackground() {
        BackgroundFill bgFill = new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY);
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
            searchMovies(title);                            //pulls new movies
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

                Image newImage = images.get(movies[btnCount].getId()); //movie Image

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
        String showTimes[] = { movies[i].getShowTimes(0), movies[i].getShowTimes(1), movies[i].getShowTimes(2) };
        int movieIndex = i;

        // showtimes button actions
        showTs[0][i].setOnAction(e -> { setSeats(0, showTimes[0],movieIndex); });
        showTs[1][i].setOnAction(e -> { setSeats(1, showTimes[1],movieIndex); });
        showTs[2][i].setOnAction(e -> { setSeats(2, showTimes[2],movieIndex); });

        hbox.getChildren().addAll(showTs[0][i],showTs[1][i],showTs[2][i]);
        vbox.getChildren().addAll(genre,time,show,hbox);
        group.getChildren().add(vbox);
    }

    public void setSeats(int time, String showTime, int i) {
        // creates new scene
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_LEFT);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 700, 300, Color.PINK);
        Stage seats = new Stage();
        seats.setTitle("Seating for " + movies[i].getTitle() + " at " + showTime);
        seats.setResizable(false);

        // creates screen bar
        Rectangle screen = new Rectangle(10, 10, 680, 50);
        screen.setFill(Color.GRAY);
        Label screenL = new Label("SCREEN");
        screenL.setTextFill(Color.WHITE);
        screenL.setTranslateX(300);
        screenL.setTranslateY(5);
        screenL.setFont(Font.font("Palatino",FontPosture.ITALIC,25));
        screenL.setStyle("-fx-font-weight: bold");
        root.getChildren().add(screen);
        pane.add(screenL,0,0);

        // creates A B C D letters on both sides
        String[] lettersS = new String[]{"A", "B", "C", "D"};
        // column
        for (int u = 0; u < 2; u++) {
            Label[] lettersL = new Label[4];
            // letter
            for (int s = 0; s < 4; s++) {
                lettersL[s] = new Label(lettersS[s]);
                lettersL[s].setFont(Font.font("Palatino", FontPosture.ITALIC, 25));
                lettersL[s].setTextFill(Color.BLACK);
                lettersL[s].setTranslateX(15);
                lettersL[s].setTranslateY(19);
                if (u == 1)
                {
                    // right side letters
                    pane.add(lettersL[s], 52, s + 1);
                }
                else {
                    // left side letters
                    pane.add(lettersL[s], 0, s + 1);
                }
            }
        }

        // seat icon layout
        String seatLayout = movies[i].getSeating(time);
        // k is char index
        int k = 0;
        // a is for seats rows triangle grid
        int a = 6;
        // counts seats
        int count = 1;
        // initial x and y position for the seat grid
        int xPos = 215;
        int yPos = 75;

        // draws seats and numbers
        for (int x = 0; x < 4; x++){
            xPos = 215 - (x*46);
            for (int y = 0; y < a; y++){
                // stack pane for grouping the numbers on top of the rectangles
                StackPane stack = new StackPane();
                // creates rectangles
                Rectangle seat = new Rectangle();
                seat.setWidth(40);
                seat.setHeight(40);
                seat.setFill(Color.DARKBLUE);
                seat.setX(xPos);
                seat.setY(yPos);
                // creates numbers
                Text nums = new Text(Integer.toString(count));
                nums.setFont(Font.font("Palatino",FontPosture.ITALIC,20));
                nums.setFill(Color.WHITE);

                // draws light blue rectangle if seat is a 1(full) or a dark blue rectangle if the seat is 0(empty)
                if (seatLayout.charAt(k) == '1')
                {
                    seat.setFill(Color.LIGHTBLUE);
                    root.getChildren().add(seat);
                }
                else
                {
                    // adds rectangle and number to stack
                    stack.getChildren().addAll(seat, nums);
                    // moves stack x and y position
                    stack.setLayoutX(xPos+20);
                    stack.setLayoutY(yPos+20);
                    stack.toFront();
                    root.getChildren().add(stack);
                }
                // adds 1 to the number of seats
                count++;
                // goes to next char in the seatLayout string
                k++;
                // adds 46 pixels to the x coordinate
                xPos+=46;
            }
            // adds space for one more rectangle on the left and right sides of the previous row
            a+=2;
            // adds 46 pixels to the y coordinate
            yPos+=46;
        }

        //TEMP SELECTION SETUP
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.BOTTOM_CENTER);
        VBox vbox = new VBox();
        gp.add(new Label(""),0,0);
        gp.add(new Label("Select Seats:"),0,3);
        TextField select = new TextField();
        gp.add(select,1,3);
        Button res = new Button("Reserve");
        res.setOnAction(e -> {
            reserveSeats(select.getText(), i, time);
            seats.close();
        });
        gp.add(res,2,3);

        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);

        root.setCenter(pane);
        vbox.getChildren().addAll(root, gp); //also temp
        seats.setScene(new Scene(vbox,700, 320)); //also kinda temp
        seats.show();
    }

    private void reserveSeats(String text, int index, int time) {
        System.out.println("Reserving: " + text);
        String[] seatsStr = text.split(",");
        int[] seats = new int[seatsStr.length];
        for (int i = 0; i < seatsStr.length; i++) {
            seats[i] = Integer.valueOf(seatsStr[i]);
        }
        String movSeats = movies[index].getSeating(time);
        for (int i = 0; i < seats.length; i++) {
            movSeats = movSeats.substring(0,seats[i]-1) + '1' + movSeats.substring(seats[i]);
        }
        movies[index].setSeating(movSeats, time);
        client.saveMovies(allMovies);
    }

}