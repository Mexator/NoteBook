package com.example.sam;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TabHost;

import java.io.File;
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
    private static int IDCounter=0;
    private int ID;
    public boolean PagesExist;
    public List<Page> Pages;
    public int TabId;//ID of tab linked with Chapter

    public Chapter(int tabId, TabHost tab_host)
    {
        ID = IDCounter++;
        PageCounter=0;
        PagesExist = false;

        TabId = tabId;
        tabHost = tab_host;

        Pages = new ArrayList<>();
    }
    public Chapter(int tabId)
    {
        ID = IDCounter++;
        PageCounter=0;
        PagesExist = false;

        Pages = new ArrayList<Page>();
        TabId = tabId;
    }
    public Chapter(String NotesPath,int id)
    {
        ID = id;
        if(IDCounter<ID) IDCounter = ID+1;
        File ChapterFolder = new File(NotesPath+'/'+ID);
        File[] PagesFolders = ChapterFolder.listFiles();
        String[]PagesFoldersName = ChapterFolder.list();
        for(int i=0;i<PagesFolders.length;i++)
        {
            File PageFolder = PagesFolders[i];
            if(PageFolder.isDirectory())
            {
                Page page = new Page(NotesPath+'/'+ID,Integer.parseInt(PagesFoldersName[i]));
                Pages.add(page);
            }
        }
    }
    public int getID()
    {
        return this.ID;
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
    public int SaveChapter(String ChaptersPath)
    {
        for (Page page : Pages) {
            page.SavePage(ChaptersPath+'/'+this.ID);
        }
        return 0;
    }
    public void LoadPages()
    {

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
