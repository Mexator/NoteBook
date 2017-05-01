package com.example.sam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import com.googlecode.tesseract.android.TessBaseAPI;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    TabHost Tabs;
    Button button;
    //Tabs.setOnTabChangedListener();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tabs = (TabHost)findViewById(R.id.tabHost);//Находим TabHost
        Tabs.setup();//Включаем TabHost

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        TabHost.TabSpec tabSpec = Tabs.newTabSpec("tag1");//Создаем первую вкладку
        tabSpec.setContent(R.id.tab1);

        tabSpec.setIndicator("Заметки");
        Tabs.addTab(tabSpec);

        Tabs.setCurrentTab(0);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button:
            {
                Intent intent = new Intent(MainActivity.this,ChapterNameEnterActivity.class);
                startActivityForResult(intent,2);

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data==null) return;
        String Name = data.getStringExtra("ChapterName");
        AddNewChapter(Name);
    }

    private void AddNewChapter(String name)
    {
        TabHost.TabSpec tabSpec = Tabs.newTabSpec("new_tab");
        tabSpec.setContent(R.id.empty_tab);
        tabSpec.setIndicator(name);
        Tabs.addTab(tabSpec);
    }
}
