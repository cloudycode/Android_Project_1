package uk.co.studio.neo.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonia Rakotomanantsoa on 19/08/15.
 */
public class Movie implements Parcelable{

    private final String BASE_URL = "http://image.tmdb.org/t/p/";
    private final String VOTE_DENOMINATOR = "/10";

    private String mMovieTitle;
    private String mMoviePosterPath;
    private String mMovieSynopsis;
    private String mMovieRating;
    private String mMovieReleaseDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMovieTitle);
        dest.writeString(mMoviePosterPath);
        dest.writeString(mMovieSynopsis);
        dest.writeString(mMovieRating);
        dest.writeString(mMovieReleaseDate);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    protected Movie(Parcel in) {
        mMovieTitle = in.readString();
        mMoviePosterPath = in.readString();
        mMovieSynopsis = in.readString();
        mMovieRating = in.readString();
        mMovieReleaseDate = in.readString();
    }

}
