package main.java.sample;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Platform;

public class Server {
    private int clientNo = 0;
    Database database = new Database();

    public void start() {
        new Thread( () -> {
            try {
                //Server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                System.out.println("Server Started at: " + new Date());
                while(true) {
                    //Wait for connections
                    Socket socket = serverSocket.accept();
                    clientNo ++;

                    Platform.runLater( () -> {
                        System.out.println("New client connected, starting new thread");
                        System.out.println("There are now " + clientNo + " clients connected");
                    });
                    //New tread for the client
                    new Thread(new HandleClient(socket)).start();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class HandleClient implements Runnable {
        private Socket socket;
        public HandleClient(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                //Get data steams
                DataInputStream clientIn = new DataInputStream(socket.getInputStream());
                DataOutputStream clientOut = new DataOutputStream(socket.getOutputStream());
                while(true) {
                    //Manage the client requests
                    String data = clientIn.readUTF();
                    if (data.equals("movies")) {
                        Movie[] movies = database.getMovies(); //Get the movies from the database
                        clientOut.writeInt(movies.length);
                        clientOut.flush(); //send how many movies are being sent
                        //Send the movie object through the socket
                        ObjectOutputStream clientOutObj = new ObjectOutputStream(socket.getOutputStream());
                        for (Movie movie: movies) {
                            clientOutObj.writeObject(movie);
                            clientOut.flush();
                        }
                        System.out.println("Sent list of movies to client");
                        clientOutObj.close();
                    }else if (data == "showtimes") {
                        //TODO load movies showtimes from csv
                        //TODO return movie showtimes as string to client
                    }else if (data == "seats") {
                        //TODO seat stuff
                    }else {
                        System.out.println("Server was sent a request it does not recognise: " + data);
                    }
                    clientIn.close();
                    clientOut.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
