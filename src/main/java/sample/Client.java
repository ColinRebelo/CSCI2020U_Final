package main.java.sample;

import java.io.*;
import java.net.Socket;

public class Client {
    //Data Streams
    private DataInputStream serverIn;
    private DataOutputStream serverOut;
    private ObjectInputStream serverInObj;
    private ObjectOutputStream serverOutObj;
    private Socket socket;

    public Client() {
        try {
            this.socket = new Socket("localhost", 8000);
            serverIn = new DataInputStream(socket.getInputStream()); //set input stream
            serverOut = new DataOutputStream(socket.getOutputStream()); //set output stream
            serverInObj = new ObjectInputStream(socket.getInputStream()); //Stream specifically to receive movie objects
            serverOutObj = new ObjectOutputStream(socket.getOutputStream()); //For sending movie objects
        }catch (IOException e) {
            System.out.println("Could not connect to server");
            System.exit(-1);
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

    public void saveMovies(Movie[] movies) {
        try {
            serverOut.writeUTF("save"); //tell the server to save the new movie list
            serverOut.writeInt(movies.length); //send the number of movies being sent
            for (Movie movie: movies) {
                serverOutObj.writeObject(movie);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
