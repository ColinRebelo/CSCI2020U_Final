package main.java.sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Database {
    private String filename = "assets/movies.csv";
    DatabaseConnection dbc = new DatabaseConnection();
    String[][] showTs;

    public Movie[] getMovies() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename)); //new reader
            String fileRaw = br.readLine(); //read all movie ids at once
            String[] IDs = fileRaw.split(",");
            Movie[] movies = new Movie[IDs.length];
            showTs = new String[3][IDs.length];
            getShowtimes();
            for (int i = 0; i < IDs.length; i++) { //add them all to an array
                movies[i] = dbc.getMovie(IDs[i]);
                movies[i].setPos(i);
                for (int j = 0; j < 3; j++)
                {
                    movies[i].setShowTimes(showTs[j][i], j);
                }
            }
            return movies;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null; //default case if the try fails
    }

    public void getShowtimes() {
        //READ SHOWTIMES AND SEND THEM
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename)); //new reader
            String fileRaw = br.readLine(); //read all movie ids at once
            int i = 0;
            while ((fileRaw = br.readLine()) != null) {
                String[] tempST = fileRaw.split(",");
                for (int j = 0; j < tempST.length; j++) { //add them all to an array
                    showTs[i][j] = tempST[j];
                }
                i++;
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[] getSeats(String title, String showtime) {
        //TODO: READ THE SEAT STATUS (OR JUST STORE IT IN AN ARRAY TBH DOESN'T REALLY MATTER) FOR THE MOVIE AND SHOWTIME
        return null;
    }
}
