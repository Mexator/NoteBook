package com.example.sam;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import com.googlecode.tesseract.android.TessBaseAPI;

public class MainActivity extends Activity implements View.OnClickListener{
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
        button.setOnClickListener((View.OnClickListener)this);

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
                AddNewPage("fdgdfg");
        }
    }
    private void AddNewPage(String name)
    {
        TabHost.TabSpec tabSpec = Tabs.newTabSpec("new_tab");
        tabSpec.setContent(R.id.empty_tab);
        tabSpec.setIndicator(name);
        Tabs.addTab(tabSpec);

    }
}
