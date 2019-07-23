package com.example.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.models.MovieTrailer;
import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailersHolder> {

    private final static String TAG = MovieTrailerAdapter.class.getSimpleName();

    private final List<MovieTrailer> trailers;
    private final int mNumberItems;
    private final ListItemClickeListener mOnClickedListener;

    public MovieTrailerAdapter(List<MovieTrailer> trailers, ListItemClickeListener mOnClickedListener){
        int size = 0;

        this.trailers = trailers;
        if(trailers != null) size = trailers.size();

        this.mNumberItems = size;
        this.mOnClickedListener = mOnClickedListener;
    }


    public interface ListItemClickeListener {
        void onListeItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public TrailersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.movie_trailer_item, viewGroup, false);

        return new TrailersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersHolder trailersHolder, int i) {
        trailersHolder.bind(trailers.get(i));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class TrailersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTrailerName;

        private TrailersHolder(View itemView){
            super(itemView);
            mTrailerName = itemView.findViewById(R.id.tv_movie_trailer_name);
            itemView.setOnClickListener(this);
        }

        private void bind(MovieTrailer trailer){
            mTrailerName.setText(trailer.getName());
        }
        @Override
        public void onClick(View v) {
            int trailerPosition = getAdapterPosition();
            mOnClickedListener.onListeItemClick(trailerPosition);
        }
    }
}
