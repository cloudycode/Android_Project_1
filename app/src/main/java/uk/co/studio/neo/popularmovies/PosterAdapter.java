package uk.co.studio.neo.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.studio.neo.popularmovies.data.Movie;

/**
 * Created by Sonia Rakotomanantsoa on 03/08/15.
 */
public class PosterAdapter extends ArrayAdapter<Movie>{

    Context context;

    public PosterAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource ,objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = new ImageView(context);
            holder = new ViewHolder();
            holder.image = (ImageView)view;
            holder.image.setAdjustViewBounds(true);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String url = getItem(position).getMoviePosterCompleteURL();

        Picasso.with(context)
                .load(url)
                .error(R.drawable.no_image)
                .placeholder(R.drawable.placeholder)
                .fit()
                .tag(context)
                .into(holder.image);

        return view;
    }

    static class ViewHolder {
        ImageView image;
    }
}
