package com.example.sam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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


public class EditPageActivity extends AppCompatActivity implements View.OnClickListener{
    final int GALLERY_REQUEST = 1;

    ImageButton SaveButton;
    ImageButton CloseButton;
    ImageButton AddImageButton,RecognizeImageButton;
    EditText HeaderEdit,NoteEdit;
    ImageView PageImageContent;

    Page page;
    Intent answerIntent = new Intent();
    Uri SourcePageImage,CompressedPageImage;

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
                String DataPath = getApplicationInfo().dataDir+"/"+page.getID()+"/";
                try
                {
                    String TextPath = DataPath+"textasset";
                    FileOutputStream textSave = new FileOutputStream(TextPath);
                    textSave.write(page.getHeader().getBytes());
                    textSave.write(page.getText().getBytes());
                    textSave.flush();
                    textSave.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                page.setHeader(HeaderEdit.getText().toString());
                page.setText(NoteEdit.getText().toString());
                try
                {
                    String ImPath = DataPath+"imgasset";
                    FileOutputStream imageSave = new FileOutputStream(ImPath);

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),CompressedPageImage);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, imageSave);
                    imageSave.flush();
                    imageSave.close();
                    CompressedPageImage = Uri.parse(ImPath);
                    bitmap.recycle();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                page.setPageImage(CompressedPageImage);
                page.isImage = (CompressedPageImage!=null);
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
                RecognizeImageButton.setVisibility(View.INVISIBLE);//Change buttons state
                AddImageButton.setVisibility(View.VISIBLE);
                try
                {
                    NoteEdit.setText(RecognizeImage(CompressedPageImage));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                NoteEdit.setVisibility(View.VISIBLE);
                PageImageContent.setVisibility(View.INVISIBLE);

                CompressedPageImage = null;
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
                    SourcePageImage = intent.getData();
                    Bitmap bitmap = null;
                    try
                    {
                        bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(),SourcePageImage);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    PageImageContent.setImageBitmap(bitmap);

                    String AppPath = getApplicationInfo().dataDir;
                    try
                    {
                        String ImPath = AppPath+"/"+"tmp"+".imgasset";
                        FileOutputStream imageSave = new FileOutputStream(ImPath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, imageSave);
                        imageSave.flush();
                        imageSave.close();
                        CompressedPageImage = Uri.parse(ImPath);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }
    }
    public String RecognizeImage(Uri InputImage) throws IOException {
        final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";
        final String TESSDATA = "tessdata";

        try {
            String path = TESSDATA;
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
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Bitmap bitmap = ((BitmapDrawable)PageImageContent.getDrawable()).getBitmap();
        int neww = bitmap.getWidth();
        int newh = bitmap.getHeight();

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
