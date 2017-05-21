package com.example.sam;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Антон on 15.05.2017.
 */

public class Page implements Parcelable
{
    private String Text;
    private String Header;
    private String Preview;
    boolean isImage;
    public Page(String header)
    {
        Header = header;
        Text = "";
        Preview="";
    }
    public Page(String header,String text)
    {
        Header = header;
        Text = text;
        Preview = "";
    }
    protected Page(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        Header = data[0];
        Text = data[1];
        isImage = in.readByte() != 0;
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
        dest.writeStringArray(new String[]{Header,Text});
        dest.writeByte((byte)(isImage? 1:0));
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

