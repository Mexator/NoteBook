package com.example.sam;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Антон on 08.05.2017.
 */

public class NoteBook
{
    public HashMap<String,Chapter> Chapters;//TabID, Chapter
    int ChaptersNum;
    public NoteBook()
    {
        Chapters = new HashMap<>();
        ChaptersNum=0;
    }
}
