package com.example.sam;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Антон on 08.05.2017.
 */

public class Chapter
{
    static TabHost tabHost;

    private int PageCounter;
    public boolean PagesExist;
    public List<Page> Pages;
    public int TabId;//ID of tab linked with Chapter
    View EmptyChapterLayout;
    View NotEmptyChapterLayout;

    public Chapter(Context context,int tabId, TabHost tab_host)
    {
        PageCounter=0;
        PagesExist = false;

        TabId = tabId;
        tabHost = tab_host;

        Pages = new ArrayList<Page>();
    }
    public Chapter(Context context,int tabId)
    {
        PageCounter=0;
        PagesExist = false;

        Pages = new ArrayList<Page>();
        TabId = tabId;
    }
    public void AddPage()
    {
        Pages.add(new Page("page"+PageCounter++));
        PagesExist=true;
    }
    public void AddPage(String name)
    {
        Pages.add(new Page(name));
        PagesExist=true;
    }
    public void RemovePage(int position)
    {
        Pages.remove(position);
        if(Pages.size()==0)
        {
            PagesExist = false;
        }
    }
    public List CreateHeadersList()
    {
        List Headers = new ArrayList<String>();

        for(Page CurPage: Pages)
        {
            Headers.add(CurPage.getHeader());
        }
        return Headers;
    }
}
