package com.adinnet.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/9/18.
 */
public class ServletUtils {
    private static final String DEFAULT_CHARSETNAME = "UTF-8";

    public static String getContent(HttpServletRequest request) throws IOException {
        return getContent(request, DEFAULT_CHARSETNAME);
    }

    public static String getContent(HttpServletRequest request, String charEncoding) throws IOException {
        byte buffer[] = getPostBytes(request);
        return new String(buffer, charEncoding);
    }

    public static byte[] getPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        InputStream is = request.getInputStream();
        for (int i = 0; i < contentLength; ) {
            int len = is.read(buffer, i, contentLength - i);
            if (len == -1) {
                break;
            }
            i += len;
        }
        return buffer;
    }
}
