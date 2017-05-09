package com.example.sam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements View.OnClickListener, TabHost.OnTabChangeListener
{
    NoteBook noteBook = new NoteBook();

    TabHost Tabs;
    ListView PageList;
    Button AddChapterButton;
    ImageView CreateFirstPage;
    int TabsID =0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitTabHost(Tabs);

        Tabs.setOnTabChangedListener(this);

        SetListeners();
    }
    private void InitTabHost(TabHost tabHost)
    {
        Tabs = (TabHost)findViewById(R.id.tabHost);//Находим TabHost
        Tabs.setup();//Включаем TabHost

        AddNewChapter(Tabs,"Заметки");
    }
    private void SetListeners()
    {
        Tabs.setOnTabChangedListener(this);

        AddChapterButton = (Button)findViewById(R.id.add_chapter_button);
        AddChapterButton.setOnClickListener(this);

        CreateFirstPage = (ImageView)findViewById(R.id.first_page_button);
        CreateFirstPage.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.add_chapter_button:
            {
                Intent intent = new Intent(MainActivity.this,ChapterNameEnterActivity.class);
                startActivityForResult(intent,1);
                break;
            }
            case R.id.first_page_button:
            {
                View layout = (View)CreateFirstPage.getParent();
                layout.setVisibility(View.INVISIBLE);

                layout = (View)findViewById(R.id.pages_list);
                layout.setVisibility(View.VISIBLE);

                String TabTag = Tabs.getCurrentTabTag();
                AddNewPage(TabTag,"g");
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data==null) return;
        if(requestCode != RESULT_CANCELED)
        {
            String Name = data.getStringExtra("ChapterName");
            AddNewChapter(Tabs,Name);
        }
    }
    private int DeleteChapter(int id)
    {
        if(id!=0)
        {
            Tabs.getTabWidget().getChildTabViewAt(id).setVisibility(View.GONE);
        }
        return 0;
    }
    private void AddNewPage(String tag,String name)
    {
        Chapter cu = noteBook.Chapters.get(tag);
        cu.Pages.add(name);
        cu.PagesExist=true;
    }
    private void AddNewChapter(TabHost tabHost,String name)//Creates new tab in tabhost
    {
        String tag = "tab"+Integer.toString(TabsID++);
        TabHost.TabSpec tabSpec = Tabs.newTabSpec(tag);
        tabSpec.setContent(R.id.empty_tab);
        tabSpec.setIndicator(name);
        Tabs.addTab(tabSpec);

        Chapter chapter = new Chapter(TabsID-1,tabHost);
        noteBook.Chapters.put(tag,chapter);
        noteBook.ChaptersNum++;

        Tabs.setCurrentTabByTag(tag);

        View layout = (View)findViewById(R.id.pages_list).getParent();
        layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTabChanged(String tabID)
    {
            Chapter g = (Chapter)noteBook.Chapters.get(tabID);
            if (g.PagesExist)
            {
                View layout = (View)findViewById(R.id.pages_list).getParent();
                layout.setVisibility(View.VISIBLE);

                layout = (View)findViewById(R.id.first_page_button).getParent();
                layout.setVisibility(View.INVISIBLE);
            }
            else
            {
                View layout = (View)findViewById(R.id.pages_list).getParent();
                layout.setVisibility(View.INVISIBLE);

                layout = (View)findViewById(R.id.first_page_button).getParent();
                layout.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getBaseContext(),tabID,Toast.LENGTH_SHORT).show();
    }
}
