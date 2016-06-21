package se.sunnyvale.magnus.freshdn;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import se.sunnyvale.magnus.freshdn.adapters.SimpleArticleRecyclerViewAdapter;
import se.sunnyvale.magnus.freshdn.entities.Article;
import se.sunnyvale.magnus.freshdn.tasks.RssTask;

public class ArticleListActivity extends AppCompatActivity
{
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private URL url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                update();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        progressBar.setVisibility(View.VISIBLE);

        onFetchArticles(new ArrayList<Article>()); // Init with empty values, while waiting for the Async

        try
        {
            // url = new URL("http://www.dn.se/nyheter/m/rss/");
            url = new URL("http://www.dn.se/rss/senaste-nytt/");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        update();
    }

    public void onFetchArticles(ArrayList<Article> articles)
    {
        SimpleArticleRecyclerViewAdapter adapter = new SimpleArticleRecyclerViewAdapter(articles);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.article_list);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void update()
    {
        new RssTask(this).execute(url);
    }
}