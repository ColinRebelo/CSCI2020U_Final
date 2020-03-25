package main.java.sample;

import javafx.scene.image.Image;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String overview;
    private String[] genres;
    private String id;
    private String runtime;
    private String poster_path;
    private String[] showTimes = new String[3];
    private String[] seating = new String[3];
    private int pos;
    private Image image;

    public void setTitle(String title) { this.title = title; }
    public void setOverview(String overview) { this.overview = overview; }
    public void setGenres(String[] genres) { this.genres = genres; }
    public void setId(String id) { this.id = id; }
    public void setRuntime(String runtime) { this.runtime = runtime; }
    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }
    public void setShowTimes(String showTimes, int i) { this.showTimes[i] = showTimes; }
    public void setSeating(String seating, int i) { this.seating[i] = seating; }
    public void setPos(int pos) { this.pos = pos; }
    public void setImage(Image image) { this.image = image; }

    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public String[] getGenres() { return genres; }
    public String getId() { return id; }
    public String getRuntime() { return runtime; }
    public String getPosterPath() { return poster_path; }
    public String getShowTimes(int i) { return showTimes[i]; }
    public String getSeating(int i) { return seating[i]; }
    public int getPos() { return pos; }
    public Image getImage() { return image; }

}