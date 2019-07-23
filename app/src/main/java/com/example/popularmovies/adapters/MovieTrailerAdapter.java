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

    /**
     * MovieTrailerAdapter constructor
     * @param trailers the trailers to be added to the viewHolder
     * @param mOnClickedListener clicked listener that was implemented in the calling class
     */
    public MovieTrailerAdapter(List<MovieTrailer> trailers, ListItemClickeListener mOnClickedListener){
        int size = 0;

        this.trailers = trailers;
        if(trailers != null) size = trailers.size();

        this.mNumberItems = size;
        this.mOnClickedListener = mOnClickedListener;
    }


    /**
     * click listener interface
     */
    public interface ListItemClickeListener {
        void onListeItemClick(int clickedItemIndex);
    }

    /**
     * Creating viewHolder and inflate
     * @param viewGroup that will be shown
     * @param i
     * @return new TrailersHolder
     */
    @NonNull
    @Override
    public TrailersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.movie_trailer_item, viewGroup, false);

        return new TrailersHolder(view);
    }

    /**
     * Bind the view to viewHolder
     * @param trailersHolder data that will be bind
     * @param i position of the trailer
     */
    @Override
    public void onBindViewHolder(@NonNull TrailersHolder trailersHolder, int i) {
        trailersHolder.bind(trailers.get(i));
    }

    /**
     * Get the number of items in the viewHolder
     * @return
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    /**
     * TrailerHolder Recycle view holder class where we bind the data and set the clickListener
     *
     */
    class TrailersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTrailerName;

        /**
         * TrailersHolder's constructor
         * @param itemView
         */
        private TrailersHolder(View itemView){
            super(itemView);
            mTrailerName = itemView.findViewById(R.id.tv_movie_trailer_name);
            itemView.setOnClickListener(this);
        }

        /**
         * Binding data to view
         * @param trailer to be bind to view
         */
        private void bind(MovieTrailer trailer){
            mTrailerName.setText(trailer.getName());
        }

        /**
         * Clicked item class binding
         * @param v view that was clicked
         */
        @Override
        public void onClick(View v) {
            int trailerPosition = getAdapterPosition();
            mOnClickedListener.onListeItemClick(trailerPosition);
        }
    }
}
