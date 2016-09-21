package com.zy.xmlparser;

import java.io.InputStream;
import java.util.List;

/**
 * Created by bobowich
 * Time: 2016/9/21.
 */
public interface BookParser {
    List<Book> parseBook(InputStream is);

    void serializeBook(List<Book> books);
}
