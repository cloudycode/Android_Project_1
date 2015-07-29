package uk.co.studio.neo.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class PosterFragment extends Fragment {

    public PosterFragment() {
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

        GridView gridview = (GridView)rootView.findViewById(R.id.gridview_posters);
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
}
