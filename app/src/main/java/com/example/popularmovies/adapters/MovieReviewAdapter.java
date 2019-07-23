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

    public MovieReviewAdapter(List<MovieReview> reviews){
        this.reviews = reviews;
        int size = 0;

        if(reviews != null) size = reviews.size();
        mNumberOfItems = size;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.movie_review_item, viewGroup, false);

        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) {
        reviewHolder.bind(reviews.get(i));
    }

    @Override
    public int getItemCount() {
        return this.mNumberOfItems;
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        private final TextView mAuthor;
        private final TextView mContent;

        private ReviewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.tv_review_author);
            mContent = itemView.findViewById(R.id.tv_review_content);
        }

        private void bind(MovieReview review){
            mAuthor.setText(review.getAuthor());
            mContent.setText(review.getContent());
        }
    }
}
