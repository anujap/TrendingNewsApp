package com.example.anuja.trendingnewsapp.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anuja.trendingnewsapp.R;
import com.example.anuja.trendingnewsapp.databinding.ItemAllNewsBinding;
import com.example.anuja.trendingnewsapp.model.Articles;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This is an adapter class to display news
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private List<Articles> mArticlesList;
    private ListItemClickListener mListItemClickListener;
    private Context context;

    public interface ListItemClickListener {
        void onListItemClicked(Articles article);
    }

    public NewsAdapter(List<Articles> mArticlesList, ListItemClickListener mListItemClickListener) {
        this.mArticlesList = mArticlesList;
        this.mListItemClickListener = mListItemClickListener;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater mInflater = LayoutInflater.from(context);
        return new NewsHolder(ItemAllNewsBinding.inflate(mInflater, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder newsHolder, int i) {
        Articles mArticle = mArticlesList.get(i);
        if(!TextUtils.isEmpty(mArticle.getUrlToImage())){
            Picasso.with(context)
                    .load(mArticle.getUrlToImage())
                    .fit().centerCrop()
                    .placeholder(R.drawable.ic_news_thumbnail_placeholder)
                    .into(newsHolder.mBinding.ivNewsImage);
        }

        newsHolder.mBinding.tvNewsTitle.setText(mArticle.getTitle());
        newsHolder.mBinding.tvNewsDescription.setText(mArticle.getDescription());
    }

    public void swapLists(List<Articles> mArticlesList) {
        this.mArticlesList = mArticlesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mArticlesList == null)
            return 0;
        return mArticlesList.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemAllNewsBinding mBinding;

        public NewsHolder(ItemAllNewsBinding mBinding) {
            super(mBinding.getRoot());

            this.mBinding = mBinding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Articles mArticle = mArticlesList.get(position);
            mListItemClickListener.onListItemClicked(mArticle);
        }
    }

}
