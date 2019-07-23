package com.example.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.models.MovieReview;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewHolder> {

    private final static String TAG = MovieReviewAdapter.class.getSimpleName();

    private final List<MovieReview> reviews;
    private final int mNumberOfItems;

    /**
     * Constructor of the adapter
     * @param reviews that will get bind to viewHolder
     */
    public MovieReviewAdapter(List<MovieReview> reviews){
        this.reviews = reviews;
        int size = 0;

        if(reviews != null) size = reviews.size();
        mNumberOfItems = size;
    }

    /**
     * Review view holder and inflate
     * @param viewGroup that will get inflated
     * @param i
     * @return new ReviewHolder
     */
    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.movie_review_item, viewGroup, false);

        return new ReviewHolder(view);
    }

    /**
     * Bind to viewHolder via the @ReviewHolder.bind method
     * @param reviewHolder to bind the data to
     * @param i position of the review
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) {
        reviewHolder.bind(reviews.get(i));
    }

    /**
     * Get the count of the items in the viewHolder
     * @return number of items
     */
    @Override
    public int getItemCount() {
        return this.mNumberOfItems;
    }

    /**
     * Recycle ViewHolder class that will bind the data to view
     */
    class ReviewHolder extends RecyclerView.ViewHolder {
        private final TextView mAuthor;
        private final TextView mContent;

        /**
         * ReviewHolder constructor
         * @param itemView
         */
        private ReviewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.tv_review_author);
            mContent = itemView.findViewById(R.id.tv_review_content);
        }

        /**
         * bind the data to view
         * @param review to be added to the view
         */
        private void bind(MovieReview review){
            mAuthor.setText(review.getAuthor());
            mContent.setText(review.getContent());
        }
    }
}
