package com.example.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.NetworkUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesHolder> {

    private final static String TAG = MoviesListAdapter.class.getSimpleName();

    private List<Movie> movies;
    private int mNumberItems;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MoviesListAdapter(List<Movie> movies, ListItemClickListener mOnClickListener) {
        this.movies = movies;
        this.mNumberItems = movies.size();
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public MoviesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.movie_list_item, viewGroup, false);

        return new MoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesHolder moviesHolder, int i) {
        moviesHolder.bind(movies.get(i));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;

        public MoviesHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.iv_movie_list_item);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            URL imgPath = NetworkUtil.buildImagePath(movie.getPoster());
            Log.v(TAG, "img: " + imgPath.toString());
            Picasso
                    .get()
                    .load(imgPath.toString()).networkPolicy(NetworkPolicy.OFFLINE)
                   // .placeholder(R.drawable.ic_launcher_background)
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

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    
}
