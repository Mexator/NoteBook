package com.example.sam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, TabHost.OnTabChangeListener,
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    final int ChapterCreateRequest = 1;
    final int PageCreateRequest=2;
    final int PageEditRequest=3;

    NoteBook noteBook = new NoteBook();

    TabHost Tabs;
    ListView PageList;
    Button AddChapterButton,RemoveChapterButton;
    Button AddPageButton;
    ImageView CreateFirstPage;
    View EmptyLayout,NonEmptyLayout;
    int TabsID =0;

    ArrayAdapter<String> PageAdapter;

    AlertDialog.Builder dialogBuilder;

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
        PageList.setOnItemClickListener(this);
        PageList.setOnItemLongClickListener(this);
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
            case R.id.delete_chapter_button:
            {
                String tag = Tabs.getCurrentTabTag();
                DeleteChapter(tag);
                break;
            }
            case R.id.first_page_button:
            {
                String TabTag = Tabs.getCurrentTabTag();
                PageAdapter = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1,noteBook.Chapters.get(TabTag).CreateHeadersList());
                PageList.setAdapter(PageAdapter);

                Intent intent = new Intent(MainActivity.this,PageNameEnterActivity.class);
                startActivityForResult(intent,PageCreateRequest);
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

                    String TabTag = Tabs.getCurrentTabTag();
                    PageAdapter = new ArrayAdapter<String>
                            (this,android.R.layout.simple_list_item_1,noteBook.Chapters.get(TabTag).CreateHeadersList());
                    PageList.setAdapter(PageAdapter);
                    PageAdapter.notifyDataSetChanged();

                    EmptyLayout.setVisibility(View.INVISIBLE);
                    NonEmptyLayout.setVisibility(View.VISIBLE);
                }
                break;
            }
            case PageEditRequest:
            {
                if(resultCode != RESULT_CANCELED)
                {
                    String Text = data.getStringExtra("Note");
                    String Header = data.getStringExtra("Header");
                    int Position = data.getIntExtra("Position",0);
                    String tag = Tabs.getCurrentTabTag();
                    noteBook.Chapters.get(tag).Pages.get(Position).Header = Header;
                    noteBook.Chapters.get(tag).Pages.get(Position).Text = Text;
                    noteBook.Chapters.get(tag).Pages.get(Position).CreatePreview();

                    PageAdapter = new ArrayAdapter<String>
                            (this,android.R.layout.simple_list_item_1,noteBook.Chapters.get(tag).CreateHeadersList());
                    PageList.setAdapter(PageAdapter);
                    PageAdapter.notifyDataSetChanged();
                }
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
        Chapter CurrentChapter = (Chapter)noteBook.Chapters.get(tabTag);
        if (CurrentChapter.PagesExist)
        {
            PageAdapter = new ArrayAdapter<String>
                    (this,android.R.layout.simple_list_item_1,noteBook.Chapters.get(tabTag).CreateHeadersList());
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
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long ID)
    {

        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Deleting note");
        dialogBuilder.setMessage("Delete note?");
        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1)
            {
                deletePage(position);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        dialogBuilder.setCancelable(true);
        dialogBuilder.show();
        return false;
    }
    private void deletePage(int position)
    {
        String Tag = Tabs.getCurrentTabTag();
        noteBook.Chapters.get(Tag).RemovePage(position);
        String TabTag = Tabs.getCurrentTabTag();
        PageAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,noteBook.Chapters.get(TabTag).CreateHeadersList());
        PageAdapter.notifyDataSetChanged();
        PageList.setAdapter(PageAdapter);

        if(!noteBook.Chapters.get(Tag).PagesExist)
        {
            EmptyLayout.setVisibility(View.VISIBLE);
            NonEmptyLayout.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Intent intent = new Intent(MainActivity.this,EditPageActivity.class);
        intent.putExtra("Position",position);
        Page page = noteBook.Chapters.get(Tabs.getCurrentTabTag()).Pages.get(position);
        intent.putExtra("Header",page.Header);
        intent.putExtra("Note",page.Text);
        startActivityForResult(intent,PageEditRequest);
    }
}
