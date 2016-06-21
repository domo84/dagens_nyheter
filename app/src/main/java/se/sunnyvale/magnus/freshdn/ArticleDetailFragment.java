package se.sunnyvale.magnus.freshdn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.sunnyvale.magnus.freshdn.entities.Article;
import se.sunnyvale.magnus.freshdn.listeners.AppBarStateChangeListener;
import se.sunnyvale.magnus.freshdn.tasks.ArticleTask;
import se.sunnyvale.magnus.freshdn.utils.DNApplication;

public class ArticleDetailFragment extends Fragment
{
    private Article mArticle = new Article();
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mArticle = DNApplication.getArticle();

        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mArticle.title);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener()
        {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state)
            {
                if (state.name().equals("EXPANDED"))
                {
                    collapsingToolbarLayout.setTitle(" ");

                }
                else if (state.name().equals("COLLAPSED"))
                {
                    collapsingToolbarLayout.setTitle(mArticle.title);
                }
                else
                {
                    collapsingToolbarLayout.setTitle(" ");
                }
            }
        });

        new ArticleTask(this).execute(mArticle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.article_detail, container, false);
        ((TextView) rootView.findViewById(R.id.intro)).setText(mArticle.intro);

        return rootView;
    }

    public void onFetchArticle(final Article article)
    {
        Log.d("onFetchArticle", "TITLE: " + article.title);

        ImageView backdrop = (ImageView) getActivity().findViewById(R.id.something_else);
        backdrop.setVisibility(View.VISIBLE);

        if (article.image instanceof Bitmap)
        {
            Drawable drawable = new BitmapDrawable(getResources(), article.image);
            backdrop.setImageDrawable(drawable);
            appBarLayout.setExpanded(true);
        }

        if (article.paragraphs.size() > 0)
        {
            LinearLayout body = (LinearLayout) getActivity().findViewById(R.id.body);

            for (String paragraph : article.paragraphs)
            {
                TextView p = new TextView(getActivity());
                p.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                p.setText(paragraph);
                p.setPadding(0, 30, 0, 20);
                p.setTextSize(16);
                p.setTypeface(Typeface.create("serif", Typeface.NORMAL));
                p.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);

                body.addView(p);
            }
        }
        else
        {
            Snackbar.make(getView(), "Kunde inte hämta innehåll", Snackbar.LENGTH_INDEFINITE).
                    setAction("Visa på DN.se", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(article.url.toString()));
                            startActivity(intent);
                        }
                    }).show();
        }

        getActivity().findViewById(R.id.progress_spinner_lite).setVisibility(View.GONE);
    }
}
