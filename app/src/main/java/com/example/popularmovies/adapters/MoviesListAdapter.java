package com.example.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.R;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.NetworkUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesHolder> {

    private final static String TAG = MoviesListAdapter.class.getSimpleName();

    private List<Movie> movies;
    private int mNumberItems;
    final private ListItemClickListener mOnClickListener;

    /**
     * List item clicked listener interface
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * Constructor to initialize fields
     * @param movies list of the movies
     * @param mOnClickListener listener
     */
    public MoviesListAdapter(List<Movie> movies, ListItemClickListener mOnClickListener) {
        this.movies = movies;
        int size = 0;
        if(movies != null) size = movies.size();
        this.mNumberItems = size;
        this.mOnClickListener = mOnClickListener;
    }

    /**
     * Creating the view holder and inflate the movie item
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public MoviesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.movie_list_item, viewGroup, false);

        return new MoviesHolder(view);
    }

    /**
     * Binding the data to view @link MoviesHolder.bind
     * @param moviesHolder
     * @param i index of the movie
     */
    @Override
    public void onBindViewHolder(@NonNull MoviesHolder moviesHolder, int i) {
        moviesHolder.bind(movies.get(i));
    }

    /**
     * Getting the number of movies
     * @return
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    /**
     * The view holder for the recycle view
     */
    class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;

        /**
         * Movies holder class for the recycle view
         * @param itemView that will be shown
         */
        public MoviesHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.iv_movie_list_item);
            itemView.setOnClickListener(this);
        }

        /**
         * Binding the data to the view.
         * Using picasso for image cashing and loading
         * @param movie object to show
         */
        public void bind(Movie movie) {
            URL imgPath = NetworkUtil.buildImagePath(movie.getPoster());
            Log.v(TAG, "img: " + imgPath.toString());
            Picasso
                    .get()
                    .load(imgPath.toString())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            Log.e(TAG,"error ");
                        }
                    });
        }

        /**
         * binding the click listener
         * @param v view that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
