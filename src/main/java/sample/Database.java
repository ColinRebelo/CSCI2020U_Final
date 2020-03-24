package main.java.sample;

import java.io.*;

public class Database {
    private String filename = "assets/movies.csv";
    DatabaseConnection dbc = new DatabaseConnection();
    String[][] showTs;
    String[][] seats;

    public Movie[] getMovies() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename)); //new reader
            String fileRaw = br.readLine(); //read all movie ids at once
            String[] IDs = fileRaw.split(",");
            Movie[] movies = new Movie[IDs.length];
            showTs = new String[3][IDs.length]; // creates new string array 3 for each movie
            seats = new String[3][IDs.length]; // creates new string array 3 for each movie
            getShowtimes();
            getSeats();
            for (int i = 0; i < IDs.length; i++) { //add them all to an array
                movies[i] = dbc.getMovie(IDs[i]);
                movies[i].setPos(i);
                for (int j = 0; j < 3; j++)
                {
                    // sets current show time and seat layout for the current movie
                    movies[i].setShowTimes(showTs[j][i], j);
                    movies[i].setSeating(seats[j][i], j);
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
            String fileRaw = br.readLine(); //reads whole row at once
            // counts # of read rows
            int i = 0;
            while ((fileRaw = br.readLine()) != null) {
                String[] tempST = fileRaw.split(",");
                for (int j = 0; j < tempST.length; j++) { //add them all to an array
                    showTs[i][j] = tempST[j];
                }
                i++;
                // if 3 lines have been read exit loop
                if (i == 3){
                    break;
                }
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSeats() {
        // READ THE SEAT STATUS FOR THE MOVIE AND SHOWTIME
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename)); //new reader
            String fileRaw = br.readLine(); //reads whole row at once
            // counts # of read rows
            int i = 0;
            // rows to read
            int a = 0;
            while ((fileRaw = br.readLine()) != null) {
                String[] tempS = fileRaw.split(",");
                // if the first 4 rows have been read then add to array
                if (i >= 3)
                {
                   for (int j = 0; j < tempS.length; j++) { //add them all to an array
                        seats[a][j] = tempS[j];
                    }
                    a++;
                }
                i++;
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMovies(Movie[] movies) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            String idLine = "";
            String[] showtimeLines = {"","",""};
            String[] seatsLines = {"","",""};
            for (Movie movie: movies) {
                idLine += movie.getId() + ",";
                for (int i = 0; i < 3; i++) {
                    showtimeLines[i] += movie.getShowTimes(i) + ",";
                }
                for (int i = 0; i < 3; i++) {
                    seatsLines[i] += movie.getSeating(i) + ",";
                }
            }
            idLine = idLine.substring(0,idLine.length()-1);
            for (int i = 0; i < 3; i++) {
                showtimeLines[i] = showtimeLines[i].substring(0, showtimeLines[i].length() - 1);
            }
            for (int i = 0; i < 3; i++) {
                seatsLines[i] = seatsLines[i].substring(0,seatsLines[i].length()-1);
            }
            bw.write(idLine + "\n");
            for (int i = 0; i < 3; i++) {
                bw.write(showtimeLines[i] + "\n");
            }
            for (int i = 0; i < 3; i++) {
                bw.write(seatsLines[i] + "\n");
            }
            bw.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
