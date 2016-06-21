package se.sunnyvale.magnus.freshdn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;

import se.sunnyvale.magnus.freshdn.entities.Article;
import se.sunnyvale.magnus.freshdn.utils.DNApplication;

public class ArticleDetailActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_spinner_lite);
        progressBar.setVisibility(View.VISIBLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
        {
            ArticleDetailFragment fragment = new ArticleDetailFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.article_detail_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                NavUtils.navigateUpTo(this, new Intent(this, ArticleListActivity.class));
                break;
            }
            case R.id.share:
            {
                Article article = DNApplication.getArticle();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TITLE, article.title);
                shareIntent.putExtra(Intent.EXTRA_TEXT, article.url.toString());
                startActivity(Intent.createChooser(shareIntent, "Dela med"));
                break;
            }
            case R.id.browser:
            {
                Article article = DNApplication.getArticle();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(article.url.toString()));
                startActivity(intent);
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.article, menu);
        return true;
    }
}
