package com.example.sam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

public class EditPageActivity extends AppCompatActivity implements View.OnClickListener{
    final int GALLERY_REQUEST = 1;

    ImageButton SaveButton;
    ImageButton CloseButton;
    ImageButton AddImageButton,RecognizeImageButton;
    EditText HeaderEdit,NoteEdit;
    ImageView PageImageContent;

    Page page;
    Intent answerIntent = new Intent();
    Uri image;

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

        AddImageButton.setOnClickListener(this);
        SaveButton.setOnClickListener(this);
        CloseButton.setOnClickListener(this);
        RecognizeImageButton.setOnClickListener(this);

        if(page.isImage)
        {
            PageImageContent.setImageURI(page.getPageImage());
            PageImageContent.setVisibility(View.VISIBLE);
            NoteEdit.setVisibility(View.INVISIBLE);
        }
        else
        {
            NoteEdit.setText(page.getText());
            PageImageContent.setVisibility(View.INVISIBLE);
            NoteEdit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.save_page_button:
            {
                page.setHeader(HeaderEdit.getText().toString());
                page.setText(NoteEdit.getText().toString());
                page.setPageImage(image);
                break;
            }
            case R.id.close_edit_button:
            {
                if(page.isImage)
                {
                    answerIntent.putExtra("Page", new Page(page.getHeader(), page.getPageImage()));
                }
                else
                {
                    answerIntent.putExtra("Page", new Page(page.getHeader(), page.getText()));
                }
                answerIntent.putExtra("Position",getIntent().getIntExtra("Position",0));
                setResult(RESULT_OK,answerIntent);
                finish();
                break;
            }
            case(R.id.add_image_button):
            {
                page.isImage = true;

                AddImageButton.setVisibility(View.INVISIBLE);//Change buttons state
                RecognizeImageButton.setVisibility(View.VISIBLE);

                NoteEdit.setVisibility(View.INVISIBLE);
                PageImageContent.setVisibility(View.VISIBLE);

                Intent GalleryIntent = new Intent(Intent.ACTION_PICK);
                GalleryIntent.setType("image/*");
                startActivityForResult(GalleryIntent,GALLERY_REQUEST);

                break;
            }
            case(R.id.recognize_image_button):
            {
                page.isImage = false;

                RecognizeImageButton.setVisibility(View.INVISIBLE);//Change buttons state
                AddImageButton.setVisibility(View.VISIBLE);

                NoteEdit.setVisibility(View.VISIBLE);
                PageImageContent.setVisibility(View.INVISIBLE);

                break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        switch(requestCode)
        {
            case GALLERY_REQUEST:
            {    if(resultCode == RESULT_OK)
                {
                    image = intent.getData();
                    PageImageContent.setImageURI(image);
                    break;
                }
            }
        }
    }
}
