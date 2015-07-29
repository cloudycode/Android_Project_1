package uk.co.studio.neo.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

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
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class PostersFragment extends Fragment {

    public PostersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] data = {
                "Mad max",
                "Jurassic world",
                "Chappie",
                "Interstellar",
                "Jupiter",
                "It follows",
                "Inside Out"
        };

        ArrayList fakeData = new ArrayList<String>(Arrays.asList(data));

        ArrayAdapter<String> posterAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.grid_item_poster,
                R.id.grid_item_poster_textview,
                fakeData);



        GridView gridview = (GridView) rootView.findViewById(R.id.gridview_posters);
        gridview.setAdapter(posterAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }

    private void updateMovies(){
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        String sort_option = "popularity.desc";
        moviesTask.execute(sort_option);
    }

    @Override
    public void onStart() {
        updateMovies();
        super.onStart();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPostExecute(String[] results) {
//            mForecastAdapter.clear();
//            for (String dayForecastStr : results){
//                mForecastAdapter.add(dayForecastStr);
//            }
        }

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;


            // Most popular URL
            // https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=

            // Highest rated URL
            // https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=

            try {
                // Construct the URL for the "The Movie Database" query
                // Possible parameters are available at TMDb discovery's API page, at
                // https://www.themoviedb.org/documentation/api/discover

                // !!!!! TODO BLANK STRING BEFORE COMMIT
                String myApiKey = "";

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
                    // Nothing to do.
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

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
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
        private String[] getMoviesDataFromJson(String moviesJsonStr)
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

            String[] resultStrs = new String[resultsArray.length()];

            for(int i = 0; i < resultsArray.length(); i++) {
                String movieTitle;
                String moviePosterPath;
                String movieSynopsis;
                // TODO Make it a double
                String movieRating;
                // TODO Make it a date
                String movieReleaseDate;

                JSONObject aMovie = resultsArray.getJSONObject(i);

                movieTitle = aMovie.getString(TMDB_TITLE);
                moviePosterPath = aMovie.getString(TMDB_POSTER);
                movieSynopsis = aMovie.getString(TMDB_SYNOPSIS);
                movieRating = aMovie.getString(TMDB_RATING);
                movieReleaseDate = aMovie.getString(TMDB_RELEASE_DATE);

                resultStrs[i] = movieTitle + " - " + moviePosterPath
                        + " - " + movieSynopsis + " - " + movieRating
                        + " - " + movieReleaseDate;
            }
            return resultStrs;

        }
    }
}
