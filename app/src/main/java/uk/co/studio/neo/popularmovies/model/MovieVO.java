package uk.co.studio.neo.popularmovies.model;

/**
 * Created by Sonia Rakotomanantsoa on 29/07/15.
 */
public class MovieVO {

    private final String BASE_URL = "http://image.tmdb.org/t/p/";
    private final String VOTE_DENOMINATOR = "/10";

    private String mMovieTitle;
    private String mMoviePosterPath;
    private String mMovieSynopsis;
    private String mMovieRating;
    private String mMovieReleaseDate;

    public MovieVO(String movieTitle, String moviePosterPath, String movieSynopsis,
                   String movieRating, String movieReleaseDate) {

        this.mMovieTitle = movieTitle;
        this.mMoviePosterPath = moviePosterPath;
        this.mMovieSynopsis = movieSynopsis;
        this.mMovieRating = movieRating + VOTE_DENOMINATOR;
        this.mMovieReleaseDate = movieReleaseDate;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public String getMoviePosterCompleteURL() {

        String size = "w185";

        String completePosterPath = BASE_URL + size + mMoviePosterPath;

        return completePosterPath;
    }

    public String getMovieSynopsis() {
        return mMovieSynopsis;
    }

    public String getMovieRating() {
        return mMovieRating;
    }

    public String getmMovieReleaseDate() {
        return mMovieReleaseDate;
    }

}
