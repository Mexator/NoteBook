package com.example.sam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.graphics.Bitmap.Config.RGB_565;

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
            AddImageButton.setVisibility(View.INVISIBLE);
            RecognizeImageButton.setVisibility(View.VISIBLE);
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

                try {
                    NoteEdit.setText(RecognizeImage(page.getPageImage()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
    public String RecognizeImage(Uri InputImage) throws IOException {
        final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";
        final String TESSDATA = "tessdata";

        File dir = new File(DATA_PATH+TESSDATA);
        String path = TESSDATA;
        try {
            String fileList[] = getAssets().list(path);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
        Bitmap bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(),InputImage);

        int neww = bitmap.getWidth();
        int newh = bitmap.getHeight();

        while((newh>1000)||(neww>1000))
        {
            newh/=2;
            neww/=2;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap,neww,newh,false);

        PageImageContent.setImageBitmap(bitmap);

        TessBaseAPI tessBaseApi = new TessBaseAPI();
        tessBaseApi.init(DATA_PATH, "eng");
        tessBaseApi.setImage(bitmap);
        String extractedText = tessBaseApi.getUTF8Text();
        tessBaseApi.end();
        return extractedText;
    }
}
