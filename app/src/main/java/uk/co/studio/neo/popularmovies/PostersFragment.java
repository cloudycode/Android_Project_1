package uk.co.studio.neo.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import uk.co.studio.neo.popularmovies.data.Movie;


/**
 * A placeholder fragment containing a simple view.
 */
public class PostersFragment extends Fragment {

    public final static String EXTRA_OBJECT = "uk.co.studio.neo.popularmovies.data.Movie";

    private PosterAdapter mPosterAdapter;

    public PostersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mPosterAdapter = new PosterAdapter(
                getActivity(),
                R.layout.grid_item_poster,
                new ArrayList<Movie>());

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview_posters);
        gridview.setAdapter(mPosterAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(EXTRA_OBJECT, mPosterAdapter.getItem(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovies(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_option = prefs.getString(getString(R.string.sort_options_key),
                getString(R.string.sort_default));

        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute(sort_option);
    }

    @Override
    public void onStart() {
        updateMovies();
        super.onStart();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPostExecute(ArrayList<Movie> results) {
            mPosterAdapter.clear();
            for (Movie aMovie:results) {
                mPosterAdapter.add(aMovie);
            }
            mPosterAdapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try {
                // Construct the URL for the "The Movie Database" query
                // Possible parameters are available at TMDb discovery's API page, at
                // https://www.themoviedb.org/documentation/api/discover

                String myApiKey = getString(R.string.api_key);

                final String FORECAST_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String KEY_PARAM = "api_key";

                Uri builtURI = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(KEY_PARAM, myApiKey)
                        .build();

                URL url = new URL(builtURI.toString());

                // Create the request to TMDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }


                inputStream.close();
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                moviesJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Take the String representing the complete movies list in JSON Format and
         * pull out the data we need to construct the Strings needed for the mockups.
         */
        private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS = "results";
            final String TMDB_TITLE = "original_title";
            final String TMDB_POSTER = "poster_path";
            final String TMDB_SYNOPSIS = "overview";
            final String TMDB_RATING = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJson.getJSONArray(TMDB_RESULTS);


            ArrayList<Movie> results = new ArrayList<Movie>(resultsArray.length());

            for(int i = 0; i < resultsArray.length(); i++) {

                String movieTitle;
                String moviePosterPath;
                String movieSynopsis;
                String movieRating;
                String movieReleaseDate;

                JSONObject aJSONMovie = resultsArray.getJSONObject(i);

                movieTitle = aJSONMovie.getString(TMDB_TITLE);
                moviePosterPath = aJSONMovie.getString(TMDB_POSTER);
                movieSynopsis = aJSONMovie.getString(TMDB_SYNOPSIS);
                movieRating = aJSONMovie.getString(TMDB_RATING);
                String releaseDate = aJSONMovie.getString(TMDB_RELEASE_DATE);

                movieReleaseDate = releaseDate == "null" ? "Unknown"
                    : aJSONMovie.getString(TMDB_RELEASE_DATE).substring(0,4);

                Movie aMovie = new Movie();
                aMovie.setMovieTitle(movieTitle);
                aMovie.setMoviePosterPath(moviePosterPath);
                aMovie.setMovieSynopsis(movieSynopsis);
                aMovie.setMovieRating(movieRating);
                aMovie.setMovieReleaseDate(movieReleaseDate);

                results.add(aMovie);

            }
            return results;

        }
    }
}
