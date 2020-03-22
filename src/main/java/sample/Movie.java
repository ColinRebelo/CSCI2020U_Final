package sample;

public class Movie {
    private String title;
    private String overview;
    private String[] genres;
    private String id;
    private String runtime;
    private String poster_path;

    public void setTitle(String title) { this.title = title; }
    public void setOverview(String overview) { this.overview = overview; }
    public void setGenres(String[] genres) { this.genres = genres; }
    public void setId(String id) { this.id = id; }
    public void setRuntime(String runtime) { this.runtime = runtime; }
    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }

    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public String[] getGenres() { return genres; }
    public String getId() { return id; }
    public String getRuntime() { return runtime; }
    public String getPosterPath() { return poster_path; }
}