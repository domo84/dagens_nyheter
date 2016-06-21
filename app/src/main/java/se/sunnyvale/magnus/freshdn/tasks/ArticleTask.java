package se.sunnyvale.magnus.freshdn.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import se.sunnyvale.magnus.freshdn.ArticleDetailFragment;
import se.sunnyvale.magnus.freshdn.entities.Article;

public class ArticleTask extends AsyncTask<Article, Integer, Article>
{
    private static final String TAG = "ArticleFetcher";
    private ArticleDetailFragment mCallback;

    public ArticleTask(ArticleDetailFragment callback)
    {
        mCallback = callback;
    }

    @Override
    protected void onPreExecute()
    {
        Log.d(TAG, "sync start");
    }

    @Override
    protected Article doInBackground(Article... params)
    {
        Article article = params[0];

        try
        {
            Document doc = Jsoup.connect(article.url.toString()).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/44.0.2403.89 Chrome/44.0.2403.89 Safari/537.36").get();

            Parser parser = new Parser(doc);
            article.image = parser.getImage();
            article.paragraphs = parser.getParagraphs();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return article;
    }

    private class Parser
    {
        private Document mDoc;

        public Parser(Document doc)
        {
            mDoc = doc;
        }

        public ArrayList<String> getParagraphs()
        {
            ArrayList<String> list = new ArrayList<String>();

            Elements paragraphs = mDoc.select("div.article__body-content").get(0).getElementsByTag("p");

            for (Element p : paragraphs)
            {
                Log.d("TAG", p.text());
                list.add(p.text());
            }

            return list;
        }

        public Bitmap getImage()
        {
            Bitmap bitmap = null;
            URL url = null;

            try
            {
                Elements images = mDoc.select("header img");
                String src = images.get(0).attr("src");
                url = new URL(src);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
            catch (MalformedURLException e1)
            {
                e1.printStackTrace();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            catch (IndexOutOfBoundsException e)
            {
                Log.d(TAG, "Headline image not found");
            }

            return bitmap;
        }
    }

    @Override
    protected void onPostExecute(Article article)
    {
        mCallback.onFetchArticle(article);
        Log.d(TAG, "sync end");
    }
}
