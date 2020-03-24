package main.java.sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

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

                    System.out.println("New client connected, starting new thread");
                    System.out.println("There are now " + clientNo + " clients connected");

                    //New tread for the client
                    new Thread(new HandleClient(socket, clientNo)).start();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class HandleClient implements Runnable {
        private Socket socket;
        int clientNum;
        public HandleClient(Socket socket, int clientNum) {
            this.socket = socket;
            this.clientNum = clientNum;
        }

        public void run() {
            try {
                //Get data steams
                DataInputStream clientIn = new DataInputStream(socket.getInputStream());
                DataOutputStream clientOut = new DataOutputStream(socket.getOutputStream());
                ObjectOutputStream clientOutObj = new ObjectOutputStream(socket.getOutputStream());
                while(true) {
                    //Manage the client requests
                    String data = clientIn.readUTF();
                    switch (data) {
                        case "movies": {
                            Movie[] movies = database.getMovies(); //Get the movies from the database

                            //database.getShowtimes(0,0);

                            clientOut.writeInt(movies.length);
                            clientOut.flush(); //send how many movies are being sent

                            //Send the movie object through the socket
                            for (Movie movie : movies) {
                                clientOutObj.writeObject(movie);
                                clientOut.flush();
                            }
                            System.out.println("Sent list of movies to client " + clientNum);
                            break;
                        }
                        case "showtimes": {
                            String title = clientIn.readUTF();
                            int pos = Integer.parseInt(clientIn.readUTF());
                            System.out.println("Sent showtimes for movie " + title + " to client " + clientNum);
                            break;
                        }
                        case "seats": {
                            String title = clientIn.readUTF();
                            String showtime = clientIn.readUTF();
//                            int[] seats = database.getSeats();
//                            for (int seat: seats) {
//                                clientOut.writeInt(seat);
//                            }
                            System.out.println("Sent seats for " + title + " at " + showtime + " to client " + clientNum);
                            break;
                        }
                        default: {
                            System.out.println("Server was sent a request it does not recognise: " + data);
                            break;
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
