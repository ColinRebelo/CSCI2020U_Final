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
    private Socket socket;

    public Client() {
        try {
            this.socket = new Socket("localhost", 8000);
            serverIn = new DataInputStream(socket.getInputStream()); //set input stream
            serverOut = new DataOutputStream(socket.getOutputStream()); //set output stream
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Movie[] getMovies() {
        try {
            serverOut.writeUTF("movies"); //ask for the movies
            int numMovies = serverIn.readInt(); //read how many movies are being sent
            //Get the movie objects in
            ObjectInputStream serverInObj = new ObjectInputStream(socket.getInputStream());
            Movie[] movies = new Movie[numMovies];
            for (int i = 0; i < numMovies; i++) {
                movies[i] = (Movie)serverInObj.readObject();
            }
            serverInObj.close();
            return movies;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null; //default case
    }

    public String getShowtimes(Movie movie) {
        //TODO: Get the showtime for the requested movie
        return null;
    }

    public int[] getSeats(Movie movie, String showtime) {
        //TODO: get the seats for the requested movie
        return null;
    }

}
