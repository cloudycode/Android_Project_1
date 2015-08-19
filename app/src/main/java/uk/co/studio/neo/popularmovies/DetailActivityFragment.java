package uk.co.studio.neo.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import uk.co.studio.neo.popularmovies.data.Movie;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        Movie movie = bundle.getParcelable(PostersFragment.EXTRA_OBJECT);

        TextView movieTitle = (TextView)rootView.findViewById(R.id.movie_title);
        movieTitle.setText(movie.getMovieTitle());

        ImageView moviePoster = (ImageView)rootView.findViewById(R.id.movie_poster);
        Picasso.with(getActivity()).load(movie.getMoviePosterCompleteURL()).into(moviePoster);

        TextView movieYear = (TextView)rootView.findViewById(R.id.movie_release_year);
        movieYear.setText(movie.getmMovieReleaseDate());

        TextView movieRating = (TextView)rootView.findViewById(R.id.movie_user_rating);
        movieRating.setText(movie.getMovieRating());

        TextView movieSynopsis = (TextView)rootView.findViewById(R.id.movie_synopsis);
        movieSynopsis.setText(movie.getMovieSynopsis());

        return rootView;
    }

}
