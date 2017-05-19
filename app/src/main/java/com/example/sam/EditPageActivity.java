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
    EditText HeaderEdit,NoteEdit;

    boolean  Saved = false;

    Intent answerIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        SaveButton = (ImageButton) findViewById(R.id.save_page_button);
        CloseButton = (ImageButton)findViewById(R.id.close_edit_button);
        HeaderEdit = (EditText)findViewById(R.id.page_header_edit);
        NoteEdit=(EditText)findViewById(R.id.page_note_edit);

        HeaderEdit.setText(getIntent().getStringExtra("Header"));
        NoteEdit.setText(getIntent().getStringExtra("Note"));

        SaveButton.setOnClickListener(this);
        CloseButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.save_page_button:
            {
                answerIntent.putExtra("Header",HeaderEdit.getText().toString());
                answerIntent.putExtra("Note",NoteEdit.getText().toString());
                Saved = true;
                break;
            }
            case R.id.close_edit_button:
            {
                if(!Saved)
                {
                    answerIntent.putExtra("Header",getIntent().getStringExtra("Header"));
                    answerIntent.putExtra("Note",getIntent().getStringExtra("Note"));
                }
                answerIntent.putExtra("Position",getIntent().getIntExtra("Position",0));
                setResult(RESULT_OK,answerIntent);
                finish();
            }
        }
    }
}
