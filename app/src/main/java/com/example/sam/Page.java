package com.example.sam;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Антон on 15.05.2017.
 */

public class Page implements Parcelable
{
    private static int IDCounter=0;
    private int ID;

    private Chapter ParentChapter;

    private String Header;
    public boolean isImage;
    private String Text;
    private Uri PageImage;
    private String Preview;

    private Page()
    {
        ID = IDCounter++;
        Header = "";
        Text = "";
        Preview = "";
        PageImage = null;
        isImage = false;
    }
    public Page(String header)//Creates Page object with header and auto-id
    {
        this();
        Header = header;
    }
    public Page(String ChapterPath,int id)//Gets folder with Page data and parse it to page object
    {
        this();
        ID = id;
        String PagePath = new String(ChapterPath+"/"+ID);
        Uri TextAssetUri = Uri.parse(PagePath+"/textasset");
        File TextAsset = new File(URI.create(TextAssetUri.toString()));
        try
        {
            FileReader fileReader = new FileReader(TextAsset);
            ID = fileReader.read();
            isImage = fileReader.read()!=0;
            int TmpChar=0;
            do
            {
                Text = Text+(char)TmpChar;
            }while (!(TmpChar!=0));
            if(isImage)
            {
                PageImage = Uri.parse(PagePath+"/imgasset");
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    public int SavePage(String DataPath)//DataPath - path where stored parental Chapter
    {
        DataPath = DataPath+ID;
        try
        {
            String TextPath = DataPath+"textasset";
            FileOutputStream textSave = new FileOutputStream(TextPath);
            textSave.write(ID);
            textSave.write((int)(isImage? 1:0));
            textSave.write("/n".getBytes());
            textSave.write(Header.getBytes());
            textSave.write(Text.getBytes());
            textSave.flush();
            textSave.close();

            String ImPath = DataPath+"imgasset";
            FileOutputStream imageSave = new FileOutputStream(ImPath);

            Bitmap bitmap = BitmapFactory.decodeFile(PageImage.toString());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, imageSave);
            imageSave.flush();
            imageSave.close();
            bitmap.recycle();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    protected Page(Parcel in)
    {
        ID = in.readInt();
        Header = in.readString();
        isImage = (in.readByte() != 0);
        PageImage = in.readParcelable(Bitmap.class.getClassLoader());
        Text = in.readString();
    }
    public int getID()
    {
        return ID;
    }
    public String getText()
    {
        return Text;
    }
    public void setText(String text)
    {
        Text = text;
    }
    public String getHeader()
    {
        return Header;
    }
    public void setHeader(String header)
    {
        Header = header;
    }
    public Uri getPageImage()
    {
        return PageImage;
    }
    public void setPageImage(Uri pageImage)
    {
        PageImage = pageImage;
    }
    public int CreatePreview()
    {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(ID);
        dest.writeString(Header);
        dest.writeByte((byte)(isImage? 1:0));
        dest.writeParcelable(PageImage,0);
        dest.writeString(Text);
    }
    public static final Parcelable.Creator<Page> CREATOR = new Parcelable.Creator<Page>() {

        @Override
        public Page createFromParcel(Parcel source) {
            return new Page(source);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };
}

