package se.sunnyvale.magnus.freshdn.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import se.sunnyvale.magnus.freshdn.ArticleListActivity;
import se.sunnyvale.magnus.freshdn.entities.Article;
import se.sunnyvale.magnus.freshdn.utils.RssHandler;

public class RssTask extends AsyncTask<URL, Integer, ArrayList<Article>>
{
    private final static String TAG = "Download Task";
    private ArticleListActivity mCallback;

    public RssTask(ArticleListActivity callback)
    {
        mCallback = callback;
    }

    @Override
    protected void onPreExecute()
    {
        Log.d(TAG, "Sync started");
        /*
        Intent intent = new Intent();
        intent.setAction(MainActivity.SYNC_START);
        mAcitivity.sendOrderedBroadcast(intent, null);
        */
    }

    @Override
    protected ArrayList<Article> doInBackground(URL... params)
    {
        RssHandler rh = new RssHandler();

        try
        {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp;
            sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            xr.setContentHandler(rh);
            xr.parse(new InputSource(params[0].openStream()));
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        } catch (SAXException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return rh.articleList;
    }

    protected void onPostExecute(ArrayList<Article> articles)
    {
        mCallback.onFetchArticles(articles);

        Log.d(TAG, "Sync ended");

        /*
        mArticles.clear();
        mArticles.addAll(articles);
        mAdapter.notifyDataSetChanged();

        Intent intent = new Intent();
        intent.setAction(MainActivity.SYNC_DONE);

        try
        {
            getActivity().sendOrderedBroadcast(intent, null);
        } catch (NullPointerException e)
        {
            // Om man kommer hit så har användaren avslutat applikationen
            // och denna async-task kan inte längre greppa 'current
            // activity'.
        }
        */
    }
}