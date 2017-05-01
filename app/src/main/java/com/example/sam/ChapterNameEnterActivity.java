package com.example.sam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChapterNameEnterActivity extends AppCompatActivity implements View.OnClickListener {

    Button CancelButton;
    Button OKButton;
    EditText EnterChapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_name_enter);

        CancelButton = (Button)findViewById(R.id.chapter_create_cancel_button);
        OKButton = (Button)findViewById(R.id.chapter_create_ok_button);
        EnterChapter = (EditText)findViewById(R.id.edit_chapter_name);

        CancelButton.setOnClickListener(this);
        OKButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.chapter_create_cancel_button:
            {
                Intent answerIntent = new Intent();
                setResult(RESULT_CANCELED,answerIntent);
                finish();
            }
            case R.id.chapter_create_ok_button:
            {
                Intent answerIntent = new Intent();
                String ChapterName = EnterChapter.getText().toString();
                answerIntent.putExtra("ChapterName",ChapterName);
                setResult(RESULT_OK,answerIntent);
                finish();
            }
        }
    }


}
