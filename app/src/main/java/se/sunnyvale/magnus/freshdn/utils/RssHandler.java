package se.sunnyvale.magnus.freshdn.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import se.sunnyvale.magnus.freshdn.entities.Article;

import android.text.Html;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RssHandler extends DefaultHandler
{
    private Article article = new Article();
    public ArrayList<Article> articleList = new ArrayList<Article>();
    StringBuffer chars = new StringBuffer();

    public void startElement(String uri, String localName, String qName, Attributes atts)
    {
        chars = new StringBuffer();
    }

    public void characters(char ch[], int start, int length)
    {
        chars.append(new String(ch, start, length));
    }

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (localName.equalsIgnoreCase("title"))
        {
            article.title = Html.fromHtml(chars.toString()).toString();
        }
        else if (localName.equalsIgnoreCase("pubDate"))
        {
            article.setPubDate(Html.fromHtml(chars.toString()).toString());
        }
        else if (localName.equalsIgnoreCase("description"))
        {
            Document doc = Jsoup.parse(chars.toString());
            Elements img = doc.select("img");
            article.img_url = img.attr("src").toString();
            article.intro = "" + doc.body().text();
        }
        else if (localName.equalsIgnoreCase("link"))
        {
            try
            {
                article.url = new URL(chars.toString());
            } catch (MalformedURLException e)
            {
                Log.e("RSA Error", e.getMessage());
            }
        }

        if (localName.equalsIgnoreCase("item"))
        {
            articleList.add(article);
            article = new Article();
        }
    }
}