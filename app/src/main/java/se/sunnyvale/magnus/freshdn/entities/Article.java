package se.sunnyvale.magnus.freshdn.entities;

import android.graphics.Bitmap;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Article
{
    public static final String TITLE = "title";
    public static final String LINK = "link";

    public String title = "";
    public String intro = null;
    public URL url;
    public String published = null;
    public Bitmap image = null;
    public String photoCred = null;
    public String byline = null;
    public ArrayList<String> paragraphs = new ArrayList<String>();
    public String author = "";
    public String img_url = "";

    public void setPubDate(String pubDate)
    {
        // Fri, 10 May 2013 20:43:00 GMT
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Date date = null;
        try
        {
            date = inputFormat.parse(pubDate);
            this.published = outputFormat.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
