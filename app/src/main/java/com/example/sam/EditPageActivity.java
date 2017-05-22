package com.example.sam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class EditPageActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton SaveButton;
    ImageButton CloseButton;
    ImageButton AddImageButton,RecognizeImageButton;
    EditText HeaderEdit,NoteEdit;
    ImageView PageImageContent;

    Page page;
    Intent answerIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        SaveButton = (ImageButton) findViewById(R.id.save_page_button);
        CloseButton = (ImageButton)findViewById(R.id.close_edit_button);
        AddImageButton =(ImageButton)findViewById(R.id.add_image_button);
        RecognizeImageButton = (ImageButton)findViewById(R.id.recognize_image_button);
        HeaderEdit = (EditText)findViewById(R.id.page_header_edit);
        NoteEdit=(EditText)findViewById(R.id.page_text_content);
        PageImageContent = (ImageView)findViewById(R.id.page_image_content);

        page = getIntent().getParcelableExtra("Page");

        HeaderEdit.setText(page.getHeader());
        NoteEdit.setText(page.getText());

        AddImageButton.setOnClickListener(this);
        SaveButton.setOnClickListener(this);
        CloseButton.setOnClickListener(this);
        RecognizeImageButton.setOnClickListener(this);
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
                AddImageButton.setVisibility(View.INVISIBLE);//Change buttons state
                RecognizeImageButton.setVisibility(View.VISIBLE);

                NoteEdit.setVisibility(View.INVISIBLE);
                PageImageContent.setVisibility(View.VISIBLE);

                break;
            }
            case(R.id.recognize_image_button):
            {
                RecognizeImageButton.setVisibility(View.INVISIBLE);//Change buttons state
                AddImageButton.setVisibility(View.VISIBLE);

                NoteEdit.setVisibility(View.VISIBLE);
                PageImageContent.setVisibility(View.INVISIBLE);

                break;
            }
        }
    }
}
