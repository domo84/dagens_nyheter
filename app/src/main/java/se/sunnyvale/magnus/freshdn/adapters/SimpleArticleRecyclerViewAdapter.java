package se.sunnyvale.magnus.freshdn.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.sunnyvale.magnus.freshdn.ArticleDetailActivity;
import se.sunnyvale.magnus.freshdn.ArticleDetailFragment;
import se.sunnyvale.magnus.freshdn.R;
import se.sunnyvale.magnus.freshdn.entities.Article;
import se.sunnyvale.magnus.freshdn.utils.DNApplication;

public class SimpleArticleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleArticleRecyclerViewAdapter.ViewHolder>
{
    private final List<Article> mArticles;

    public SimpleArticleRecyclerViewAdapter(ArrayList<Article> articles)
    {
        mArticles = articles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final Article article = mArticles.get(position);

        holder.mArticle = article;
        holder.mTitleView.setText(article.title);
        holder.mPubDateView.setText(article.published);
        holder.mDescriptionView.setText(article.intro);

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DNApplication.setArticle(article);
                Context context = v.getContext();
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mArticles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public Article mArticle;
        public final View mView;
        public final TextView mTitleView;
        public final TextView mPubDateView;
        public final TextView mDescriptionView;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mPubDateView = (TextView) view.findViewById(R.id.pub_date);
            mDescriptionView = (TextView) view.findViewById(R.id.description);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
