package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Main extends Application {
//    DataBase db;
    Group group = new Group();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //throw group in a flow pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane,600,500);  //create scene

        setBackground();
        setTitle(scene);
        makeSearchBar(scene);
        setMovieGrid(scene);

        scrollPane.setContent(group);              //will add to scene

        primaryStage.setTitle("Film Finder");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    private void setBackground() {
        BackgroundFill bgFill = new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(bgFill);
        VBox vbox = new VBox();
        vbox.setBackground(background);
        vbox.setMinHeight(1000);
        vbox.setMinWidth(2000);
        group.getChildren().add(vbox);
    }

    private void setTitle(Scene scene) {
        //make  title
        Label title = new Label("Film Finder");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD,40));
        title.setLayoutX(scene.getWidth()/3);
        group.getChildren().add(title);
    }

    private void makeSearchBar(Scene scene) {
        //make search bar
        HBox hbox = new HBox();
        Button btn = new Button("Search Films");
        TextField searchBar = new TextField();
        hbox.getChildren().addAll(searchBar,btn);
        hbox.setLayoutX(scene.getWidth()/3.25);
        hbox.setLayoutY(scene.getHeight()/9);
        group.getChildren().add(hbox);

        btn.setOnAction(e -> {
            //pulls new movies
            //updates screen
        });
    }

    private void setMovieGrid(Scene scene) throws FileNotFoundException {
        GridPane grid = new GridPane();
        grid.setLayoutX(scene.getWidth()/18);
        grid.setLayoutY(scene.getHeight()/5);
        grid.setHgap(scene.getWidth()/5.5);
        grid.setVgap(scene.getHeight()/10);

        double imageSize = (scene.getWidth()/6);                //set size of images
        Button[] buttonArray = new Button[15];      //size of total movies

        //for every movie
        for (int i=0; i<5; i++) {               //total rows
            for (int j=0; j<3; j++) {            //total columns
                //creates film selection
                VBox newBox = new VBox(5);
                Button newFilm = new Button("temp");                    //put name in here
                newFilm.setPrefSize(100,20);
                buttonArray[i+j] = newFilm;                             //add to array
                Image newImage = new Image(new FileInputStream("C:/test/wal.png"));          //throw in url

                //formats in grid
                ImageView newView = new ImageView(newImage);
                newView.setFitHeight(imageSize);
                newView.setFitWidth(imageSize);
                newBox.getChildren().addAll(newView,newFilm);
                grid.add(newBox,j, i);
            }
        }
        group.getChildren().add(grid);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
