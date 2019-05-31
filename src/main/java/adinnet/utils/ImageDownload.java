package adinnet.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author wangren
 * @Description: 111
 * @create 2018-11-02 16:09
 **/
public class ImageDownload {

    public static void main(String[] args) {
        String url = "http://study.wdjky.com/yinzhang/2e5718f6-9e90-434a-932d-5a6fc77f19e3.png";
        downloadPicture(url,"D:/test.png");
    }
    //链接url下载图片
    public static void downloadPicture( String urlList, String imageName) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream
                    fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
