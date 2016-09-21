package com.zy.xmlparser;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobowich
 * Time: 2016/9/21.
 */
public class PullBookParser implements BookParser {
    public static final String TAG = PullBookParser.class.getSimpleName();
    List<Book> mBooks;
    Book mBook;
    @Override
    public List<Book> parseBook(InputStream is) {
        XmlPullParser pullParser = Xml.newPullParser();
        try {
            pullParser.setInput(is,"UTF-8");
            int eventType = pullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        mBooks = new ArrayList<Book>();
                        break;

                    case XmlPullParser.START_TAG:
                        //mBook = new Book(); //为什么不能写在这里???
                        if (pullParser.getName().equals("book")) {
                            mBook = new Book();
                            String name = pullParser.getAttributeValue(0);
                            mBook.setName(name);
                            int id = Integer.parseInt(pullParser.getAttributeValue(1));
                            mBook.setId(id);
                        } else if (pullParser.getName().equals("page")) {
                            int page = Integer.parseInt(pullParser.nextText());
                            mBook.setPage(page);
                        } else if (pullParser.getName().equals("price")) {
                            float price = Float.parseFloat(pullParser.nextText());
                            mBook.setPrice(price);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (pullParser.getName().equals("book")) {
                            Log.d(TAG, "parseBook: "+mBook.toString());
                            mBooks.add(mBook);
                        }
                        break;
                }
                eventType = pullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void serializeBook(List<Book> books) {

    }
}
