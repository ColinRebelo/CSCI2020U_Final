package main.java.sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client {
    //Data Streams
    private DataInputStream serverIn;
    private DataOutputStream serverOut;
    ObjectInputStream serverInObj;
    private Socket socket;

    public Client() {
        try {
            this.socket = new Socket("localhost", 8000);
            serverIn = new DataInputStream(socket.getInputStream()); //set input stream
            serverOut = new DataOutputStream(socket.getOutputStream()); //set output stream
            serverInObj = new ObjectInputStream(socket.getInputStream()); //Stream specifically to receive movie objects
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Movie[] getMovies() {
        try {
            serverOut.writeUTF("movies"); //ask for the movies
            int numMovies = serverIn.readInt(); //read how many movies are being sent
            //Get the movie objects in
            Movie[] movies = new Movie[numMovies];
            for (int i = 0; i < numMovies; i++) {
                movies[i] = (Movie)serverInObj.readObject();
            }
            return movies;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null; //default case
    }

    public String getShowtimes(Movie movie) {
        try{
            serverOut.writeUTF("showtimes");
            serverOut.writeUTF(movie.getTitle());
            //serverOut.writeUTF(movie.getPos());
            return serverIn.readUTF();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int[] getSeats(Movie movie, String showtime) {
        try {
            serverOut.writeUTF("seats");
            serverOut.writeUTF(movie.getTitle());
            serverOut.writeUTF(showtime);
            int numSeats = serverIn.readInt(); //get the number of seats
            int[] seats = new int[numSeats];
            for (int i = 0; i < numSeats; i++) {
                seats[i] = serverIn.readInt(); //Read in the seat status for the while array
            }
            return seats;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
