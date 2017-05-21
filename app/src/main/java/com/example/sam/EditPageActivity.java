package com.example.sam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditPageActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton SaveButton;
    ImageButton CloseButton;
    ImageButton AddImageButton;
    EditText HeaderEdit,NoteEdit;
    Page page;
    Intent answerIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        SaveButton = (ImageButton) findViewById(R.id.save_page_button);
        CloseButton = (ImageButton)findViewById(R.id.close_edit_button);
        AddImageButton =(ImageButton)findViewById(R.id.add_image_button);
        HeaderEdit = (EditText)findViewById(R.id.page_header_edit);
        NoteEdit=(EditText)findViewById(R.id.page_note_edit);

        page = getIntent().getParcelableExtra("Page");

        HeaderEdit.setText(page.getHeader());
        NoteEdit.setText(page.getText());

        AddImageButton.setOnClickListener(this);
        SaveButton.setOnClickListener(this);
        CloseButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.save_page_button:
            {
                page.setHeader(HeaderEdit.getText().toString());
                page.setText(NoteEdit.getText().toString());
                break;
            }
            case R.id.close_edit_button:
            {
                answerIntent.putExtra("Page",page);
                answerIntent.putExtra("Position",getIntent().getIntExtra("Position",0));
                setResult(RESULT_OK,answerIntent);
                finish();
                break;
            }
            case(R.id.add_image_button):
            {

            }
        }
    }
}
