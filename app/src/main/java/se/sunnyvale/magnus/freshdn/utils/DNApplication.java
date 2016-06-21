package se.sunnyvale.magnus.freshdn.utils;

import android.app.Application;

import se.sunnyvale.magnus.freshdn.entities.Article;

public class DNApplication extends Application
{
    private static Article sArticle;

    public static void setArticle(Article article)
    {
        sArticle = article;
    }

    public static Article getArticle()
    {
        return sArticle;
    }
}
