package com.adinnet.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author wangren
 * @Description: 下载文件到本地
 * @create 2018-10-09 10:56
 **/
public class GetFileUtils {

    public static void downloadFile(String remoteFilePath, String localFilePath){
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFilePath);
        try
        {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            System.out.println("上传成功");
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bis.close();
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String remoteFilePath="http://file.ubye.cn/UbyeBackgroundFiles/img/package_quick/201809191119208yq1.png";
       String localFilePath="D:/1.png";
       //System.out.println(this.getClass().getResource("/").getPath());
        downloadFile(remoteFilePath,localFilePath);
    }
}
