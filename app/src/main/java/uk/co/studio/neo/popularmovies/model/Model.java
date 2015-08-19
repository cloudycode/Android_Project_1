package uk.co.studio.neo.popularmovies.model;

import java.util.ArrayList;

/**
 * Created by Sonia Rakotomanantsoa on 03/08/15.
 */
public class Model {

    private static ArrayList<MovieVO> mMovies;

    public static void addMovie(MovieVO aMovie)
    {
        if(mMovies == null){
           mMovies = new ArrayList<MovieVO>();
        }
        mMovies.add(aMovie);
    }

    public static MovieVO getMovieAtPosition(int position)
    {
        return mMovies.get(position);
    }

    public static void clear(){

        if(mMovies != null) {
            mMovies.clear();
        }
    }
}
