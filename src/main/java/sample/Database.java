package main.java.sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Database {
    private String filename = "assets/movies.csv";
    DatabaseConnection dbc = new DatabaseConnection();

    public Movie[] getMovies() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename)); //new reader
            String fileRaw = br.readLine(); //read all movie ids at once
            String[] IDs = fileRaw.split(",");
            Movie[] movies = new Movie[IDs.length];
            for (int i = 0; i < IDs.length; i++) { //add them all to an array
                movies[i] = dbc.getMovie(IDs[i]);
            }
            return movies;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null; //default case if the try fails
    }

}
