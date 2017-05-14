package com.example.sam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, TabHost.OnTabChangeListener
{
    final int ChapterCreateRequest = 1;
    final int PageCreateRequest=2;

    NoteBook noteBook = new NoteBook();

    TabHost Tabs;
    ListView PageList;
    Button AddChapterButton,RemoveChapterButton;
    Button AddPageButton;
    ImageView CreateFirstPage;
    View EmptyLayout,NonEmptyLayout;
    int TabsID =0;

    ArrayAdapter<String> PageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindViews();

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
    private void FindViews()
    {
        AddChapterButton = (Button)findViewById(R.id.add_chapter_button);
        RemoveChapterButton = (Button)findViewById(R.id.delete_chapter_button);
        AddPageButton = (Button)findViewById(R.id.add_page_button);
        CreateFirstPage = (ImageView)findViewById(R.id.first_page_button);
        PageList = (ListView)findViewById(R.id.pages_list);
        NonEmptyLayout = (View)findViewById(R.id.pages_list).getParent();
        EmptyLayout = (View)findViewById(R.id.first_page_button).getParent();
    }
    private void SetListeners()
    {
        Tabs.setOnTabChangedListener(this);
        AddChapterButton.setOnClickListener(this);
        RemoveChapterButton.setOnClickListener(this);
        CreateFirstPage.setOnClickListener(this);
        AddPageButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.add_chapter_button:
            {
                Intent intent = new Intent(MainActivity.this,ChapterNameEnterActivity.class);
                startActivityForResult(intent,ChapterCreateRequest);
                break;
            }
            case R.id.first_page_button:
            {
                String TabTag = Tabs.getCurrentTabTag();
                PageAdapter = new ArrayAdapter<String>
                    (this,android.R.layout.simple_list_item_1,noteBook.Chapters.get(TabTag).Pages);
                PageList.setAdapter(PageAdapter);

                NonEmptyLayout.setVisibility(View.VISIBLE);
                EmptyLayout.setVisibility(View.INVISIBLE);

                break;
            }
            case R.id.delete_chapter_button:
            {
                String tag = Tabs.getCurrentTabTag();
                DeleteChapter(tag);
                break;
            }
            case R.id.add_page_button:
            {
                Intent intent = new Intent(MainActivity.this,PageNameEnterActivity.class);
                startActivityForResult(intent,PageCreateRequest);
                break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case ChapterCreateRequest:
            {
                if(data==null) return;
                if(resultCode != RESULT_CANCELED)
                {
                    String Name = data.getStringExtra("ChapterName");
                    AddNewChapter(Tabs,Name);
                }
                break;
            }
            case PageCreateRequest:
            {
                if(data==null) return;
                if(resultCode != RESULT_CANCELED)
                {
                    String Name = data.getStringExtra("PageName");
                    noteBook.Chapters.get(Tabs.getCurrentTabTag()).AddPage(Name);
                    PageList.setAdapter(PageAdapter);
                    PageAdapter.notifyDataSetChanged();
                }
                break;
            }
        }

    }
    private int DeleteChapter(String tag)
    {
        if(tag.equals("tab0"))
        {
            Toast.makeText(getBaseContext(),"Could not delete main chapter",Toast.LENGTH_SHORT).show();
            return 1;
        }
        int SenderID = Tabs.getCurrentTab();
        Tabs.setCurrentTabByTag(tag);
        int ReceiverID = Tabs.getCurrentTab();

        Tabs.getTabWidget().getChildTabViewAt(ReceiverID).setVisibility(View.GONE);

        noteBook.Chapters.remove(tag);

        if(SenderID!=ReceiverID) Tabs.setCurrentTab(SenderID);
        else Tabs.setCurrentTab(0);
        return 0;
    }
    private void AddNewChapter(TabHost tabHost,String name)//Creates new tab in tabhost
    {
        String tag = "tab"+Integer.toString(TabsID++);
        TabHost.TabSpec tabSpec = Tabs.newTabSpec(tag);
        tabSpec.setContent(R.id.empty_tab);
        tabSpec.setIndicator(name);
        Tabs.addTab(tabSpec);

        Chapter chapter = new Chapter(this.getBaseContext(),TabsID-1,tabHost);
        noteBook.Chapters.put(tag,chapter);
        noteBook.ChaptersNum++;

        Tabs.setCurrentTabByTag(tag);

        EmptyLayout.setVisibility(View.VISIBLE);
        NonEmptyLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTabChanged(String tabTag)
    {
        Chapter g = (Chapter)noteBook.Chapters.get(tabTag);
        if (g.PagesExist)
        {
            PageAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,noteBook.Chapters.get(tabTag).Pages);
            PageList.setAdapter(PageAdapter);
            PageAdapter.notifyDataSetChanged();

            EmptyLayout.setVisibility(View.INVISIBLE);
            NonEmptyLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            EmptyLayout.setVisibility(View.VISIBLE);
            NonEmptyLayout.setVisibility(View.INVISIBLE);
        }
        Toast.makeText(getBaseContext(),tabTag,Toast.LENGTH_SHORT).show();
    }
}
