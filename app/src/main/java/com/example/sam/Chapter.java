package com.example.sam;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Антон on 08.05.2017.
 */

public class Chapter
{
    static TabHost tabHost;

    private int NoteNumber;
    public boolean PagesExist;
    public HashSet<String> Pages;
    public int TabId;//ID of tab linked with Chapter
    View EmptyChapterLayout;
    View NotEmptyChapterLayout;

    public Chapter(int tabId, TabHost tab_host)
    {
        NoteNumber=0;
        PagesExist = false;

        TabId = tabId;
        tabHost = tab_host;

        Pages = new HashSet<>();
    }
    public Chapter(int tabId)
    {
        NoteNumber=0;
        PagesExist = false;

        TabId = tabId;
    }
}
