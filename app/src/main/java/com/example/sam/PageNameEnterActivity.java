package com.example.sam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PageNameEnterActivity extends AppCompatActivity implements View.OnClickListener{

    Button CancelButton;
    Button OKButton;
    EditText EnterPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_name_enter);

        CancelButton = (Button)findViewById(R.id.page_create_cancel_button);
        OKButton = (Button)findViewById(R.id.page_create_ok_button);
        EnterPage = (EditText)findViewById(R.id.edit_page_name);

        CancelButton.setOnClickListener(this);
        OKButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.page_create_cancel_button:
            {
                Intent answerIntent = new Intent();
                setResult(RESULT_CANCELED,answerIntent);
                finish();
            }
            case R.id.page_create_ok_button:
            {
                Intent answerIntent = new Intent();
                String PageName = EnterPage.getText().toString();
                answerIntent.putExtra("PageName",PageName);
                setResult(RESULT_OK,answerIntent);
                finish();
            }
        }
    }
}
