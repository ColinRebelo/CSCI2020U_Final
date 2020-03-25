package main.java.sample;

import com.google.gson.*;
import javafx.scene.image.Image;

import java.util.Scanner;

import java.net.URL;
import java.net.URLConnection;

public class DatabaseConnection {
    private static final String API_KEY = "31c693705ad28da5b2e3033019542200";
    private static final String API_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_IMAGE_URL = "https://image.tmdb.org/t/p/original";

    public Movie getMovie(String movie_id) {
        JsonObject document = null;
        Movie movie = new Movie();

        String dataToParse = "";
        try {
            URL apiURL = new URL(API_MOVIE_URL + movie_id + "?api_key=" + API_KEY);
            URLConnection apiCon = apiURL.openConnection();

            Scanner conScanner = new Scanner(apiCon.getInputStream());
            while(conScanner.hasNextLine()) {
                dataToParse += conScanner.nextLine();
            }

            conScanner.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        JsonParser parser = new JsonParser();
        document = parser.parse(dataToParse).getAsJsonObject();

        if(document != null) {

            movie.setTitle(document.get("title").getAsString());
            movie.setOverview(document.get("overview").getAsString());

            JsonArray genreArr = document.get("genres").getAsJsonArray();
            String[] genres = new String[genreArr.size()];
            {
                int i=0;
                for (JsonElement gElem : genreArr) {
                    JsonObject gObj = gElem.getAsJsonObject();
                    genres[i++] = gObj.get("name").getAsString();
                }
            }
            movie.setGenres(genres);

            movie.setId(document.get("id").getAsString());
            movie.setRuntime(document.get("runtime").getAsString());
            movie.setPosterPath(document.get("poster_path").getAsString());
        }

        return movie;
    }

    public Image getImage(Movie movie) {
        Image image = new Image(API_IMAGE_URL + movie.getPosterPath());
        return image;
    }
}