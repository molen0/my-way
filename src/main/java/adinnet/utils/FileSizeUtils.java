package adinnet.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/9/30.
 */
public class FileSizeUtils {

    public static double getFileSize(MultipartFile file) {
        double resourceSizeMb = 0;
        try {
            FileInputStream fis = (FileInputStream) file.getInputStream();

            DecimalFormat df = new DecimalFormat("#.##");
           //resourceSizeMb= df.format((double) ((double) fis.available() / 1024));
           resourceSizeMb = (double) fis.available() / 1024;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceSizeMb;
    }
}
