package com.example.sam;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Антон on 15.05.2017.
 */

public class Page implements Parcelable
{
    private String Header;
    public boolean isImage;
    private String Text;
    private Bitmap PageImage;
    private String Preview;
    public Page(String header)
    {
        Header = header;
        Text = "";
        Preview="";
        PageImage = null;
        isImage = false;
    }
    public Page(String header,String text)
    {
        Header = header;
        Text = text;
        Preview = "";
        PageImage = null;
        isImage = false;
    }
    public Page(String header,Bitmap pageImage)
    {
        PageImage = pageImage;
        Header = header;
        isImage = true;
    }
    protected Page(Parcel in)
    {
        Header = in.readString();
        isImage = (in.readByte() != 0);
        PageImage = in.readParcelable(Bitmap.class.getClassLoader());
        Text = in.readString();
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
    public Bitmap getPageImage()
    {
        return PageImage;
    }
    public void setPageImage(Bitmap pageImage)
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
    public void writeToParcel(Parcel dest, int flags) {
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

