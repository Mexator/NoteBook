package com.example.sam;


import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Антон on 08.05.2017.
 */

public class NoteBook
{
    public HashMap<String,Chapter> Chapters;//TabID, Chapter
    private String AppPath;
    int ChaptersNum;
    public NoteBook()
    {
        Chapters = new HashMap<>();
        ChaptersNum=0;
    }
    public NoteBook(String appPath)
    {
        this();
        SetAppPath(appPath);
    }
    public int SaveChapters()
    {
        String NotesPath = AppPath+"/notes";
        for(Chapter chapter:Chapters.values())
        {
            chapter.SaveChapter(NotesPath);
        }
        return 0;
    }
    public void SetAppPath(String appPath)
    {
        AppPath = appPath;
    }
    public int LoadChapters()//returns 0 if chapters were loaded, 1 if no chapters
    {
        File NotesFolder = new File(AppPath+"/notes");
        String[] ChaptersFolders = NotesFolder.list();
        if(ChaptersFolders != null)
        {
            for(String Entry: ChaptersFolders)
            {
                String ChapterFolder = NotesFolder+Entry;
                File file = new File(ChapterFolder);
                if(file.isDirectory())
                {
                    Chapter chapter = new Chapter(AppPath+"/notes",Integer.parseInt(Entry));
                    Chapters.put("chapter"+chapter.getID(),chapter);
                }
            }
            return 0;
        }
        return 1;
    }
    public void AddChapter()
    {

    }
}
