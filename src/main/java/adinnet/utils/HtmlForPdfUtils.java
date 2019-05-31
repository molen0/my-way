package adinnet.utils;

import ch.qos.logback.core.util.FileUtil;
import com.adinnet.common.HtmlToPdfInterceptor;
import org.w3c.tidy.Tidy;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.Semaphore;

/**
 * @author wangren
 * @Description: html转pdf工具
 * @create 2018-10-09 16:34
 **/
public class HtmlForPdfUtils {
    //final static String whktmlExe = "D:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe";
    public final static String MPURL = "/usr/local/bin/xfzs";
    //客户测试环境
     final static String whktmlExe = "/usr/local/bin/wkhtmltopdf";
    public static void main(String[] args) {
        String tmpFileName = UUID.randomUUID().toString();
        String url = "D:\\";
        String htmlUrl = "D:/workspace/yunque/03_Code/hibernate-jpa/target/classes/static/xfzs/view_credits1.html";
        String pdfFilePath = url+tmpFileName + ".pdf";
        System.out.println(pdfFilePath);
        try {
            pdfTest(htmlUrl, pdfFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void pdfTest(final String htmlUrl, final String pdfFilePath) {
        // 只能5个线程同时访问
           final Semaphore semaphore = new Semaphore(2);
        String osName = System.getProperty("os.name");
        try {
            System.out.println(" inside for before " + semaphore.availablePermits());
            // 获取许可
            semaphore.acquire();
            System.out.println(" inside for after " + " ---" + htmlUrl);
            new Thread() {
                public void run() {
                    try {
                        ProcessBuilder pb = new ProcessBuilder(whktmlExe, "--print-media-type", "--margin-left", "10mm", "--margin-right", "10mm", htmlUrl, pdfFilePath);
                        // 使用print样式
                        // ProcessBuilder pb = new ProcessBuilder(whktmlExe, htmlUrl, pdfFilePath);
                               pb.redirectErrorStream(true);
                               Process process = pb.start();
                        BufferedReader errStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line = errStreamReader.readLine();
                        while (line != null) {
                            System.err.println(line); //or whatever else
                            line = errStreamReader.readLine();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("in finally releasing " + semaphore.availablePermits());
                        // 访问完后，释放
                                    semaphore.release();
                    }
                }
            }.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(" *** Error in pdf generation *** ");
        }
    }

}
