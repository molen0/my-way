package adinnet.utils.qiniu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.adinnet.common.QiniuConfig;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.http.HttpClient;
import sun.security.pkcs11.wrapper.Constants;

import javax.crypto.Mac;

/**
 * 七牛文件上传公用方法
 */
@Component
@PropertySource("classpath:application.yml")
public class QiniuUtils {

    private static String accessKey="kUCOKS5T5i6ygr-5i7MoqimI-EDl5OegkKwkxctw";
    private static String secertKey="q7p2Oyx56blsuh73Y8n-6Geg-y4dUSzKtix_lpPk";
    private static String bucketNm="healthcloud-study";

    private static String prefix="https://study.wdjky.com";

    private static String pipeline = "study-pipeline";  //处理队列

    //获取授权对象
    static Auth auth = Auth.create(accessKey, secertKey);
    //执行操作的管理对象
    static OperationManager operationMgr = new OperationManager(auth, new Configuration(Zone.zone0()));

    /*public static void main(String[] args) throws Exception {
        File file = new File("D:\\3.mp4");
        upload(bucketNm,file);
    }*/

    /**
     * 获取bucket里面所有文件的信息
     * @param bucketNm
     */
    public static void getFileInfo(String bucketNm) {
        try {
            BucketManager bucketManager = getBucketManager();

            //文件名前缀
            String prefix = "";
            //每次迭代的长度限制，最大1000，推荐值 1000
            int limit = 1000;
            //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            String delimiter = "";

            //列举空间文件列表
            BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketNm, prefix, limit, delimiter);
            while (fileListIterator.hasNext()) {
                //处理获取的file list结果
                FileInfo[] items = fileListIterator.next();
                for (FileInfo item : items) {
                    System.out.println(item.key);
                    /*System.out.println(item.hash);
                    System.out.println(item.fsize);
                    System.out.println(item.mimeType);
                    System.out.println(item.putTime);
                    System.out.println(item.endUser);*/
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有的bucket
     */
    public static void getBucketsInfo() {
        try {
            BucketManager bucketManager = getBucketManager();
            //获取所有的bucket信息
            String[]  bucketNms = bucketManager.buckets();
            for(String bucket:bucketNms) {
                System.out.println(bucket);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除多个文件
     * @param bucketNm bucket的名称
     * @param keys     文件名称数组
     * //单次批量请求的文件数量不得超过1000 , 这个是七牛所规定的
     * @return
     */
    public static Result deletes(String bucketNm ,String [] keys) {
        Result result = null;
        try {
            //当文件大于1000的时候，就直接不处理
            if(keys.length >1000) {
                return new Result(false);
            }

            //设定删除的数据
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucketNm, keys);
            BucketManager bucketManager = getBucketManager();
            //发送请求
            Response response = bucketManager.batch(batchOperations);

            //返回数据
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keys.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keys[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                    return new Result(false);
                }
            }
            result = new Result(true);
        }catch (Exception e) {
            result = new Result(false);
        }
        return result;
    }
    /**
     * 删除bucket中的文件名称
     * @param bucketNm bucker名称
     * @param key 文件名称
     * @return
     */
    public static Result delete(String bucketNm ,String key) {
        Result result = null;
        try {
            BucketManager mg = getBucketManager();
            mg.delete(bucketNm, key);
            result = new Result(true);
        }catch (Exception e) {
            result = new Result(false);
        }
        return result;
    }

    /**
     * 上传输入流
     * @param bucketNm  bucket的名称
     * @param in        输入流
     * @return
     */
    public static Result upload(String bucketNm,InputStream in,String key) {
        Result result = null;
        try {
            UploadManager uploadManager = getUploadManager(bucketNm);

            //获取token
            String token = getToken(bucketNm);

            //上传输入流
            Response response = uploadManager.put(in,key,token, null,null);

            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(prefix+"/"+putRet.key);
            System.out.println(putRet.hash);

            result = new Result(true,putRet.key);
        }catch (Exception e) {
            result = new Result(false);
        }
        return result;
    }

    /**
     * 通过文件来传递数据
     * @param file 文件
     * @param remoteDir 上传到七牛的文件目录
     * @return
     */
    public static String upload(MultipartFile file,String remoteDir) throws IOException {
        String path = "";
        try {
            UploadManager uploadManager = getUploadManager(bucketNm);
            String token = getToken(bucketNm);
            Response response = uploadManager.put(file.getInputStream(), remoteDir + "/" + newName(file.getOriginalFilename()), token, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            path = prefix+"/"+putRet.key;//返回域名+七牛返回路径
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 上传视频并转码——》视频文件控制为mp4，转码为m3u8
     * @param file 文件
     * @param remoteDir 上传到七牛的文件目录
     * @return
     */
    public static List<String> uploadVideo(MultipartFile file,String remoteDir) throws IOException {
        List<String> list = new ArrayList<>();
        String path = "";
        try {
            UploadManager uploadManager = getUploadManager(bucketNm);
            String token = getToken(bucketNm);
            Response response = uploadManager.put(file.getInputStream(),remoteDir+"/"+newName(file.getOriginalFilename()), token,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            path = prefix+"/"+putRet.key;//返回域名+七牛返回路径
            list.add(path);
            /*******************   MP4转m3u8 start  ******************/
            Auth auth = Auth.create(accessKey, secertKey);
            Zone z = Zone.zone0();
            Configuration c = new Configuration(z);
            //新建一个OperationManager对象
            OperationManager operater = new OperationManager(auth, c);
            //设置要转码的空间和key，并且这个key在你空间中存在
            String bucket = "healthcloud-study";
            String key = "Bucket_key";
            //设置转码操作参数
            //String fops = "avthumb/m3u8/noDomain/1/vb/500k/t/10";
            String fops = "avthumb/m3u8/noDomain/1/vb/500k";
            //设置转码的队列
            String pipeline = "study-pipeline";
            //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
            String newKey = UUID.randomUUID()+".m3u8";
            //String newKey = "201811120002.m3u8";
            String urlbase64 = UrlSafeBase64.encodeToString(bucketNm + ":" + newKey); //目标Bucket_Name:自定义文件key
            String pfops = fops + "|saveas/" + urlbase64;
            //设置pipeline参数
            StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);
            try {
                String persistid = operater.pfop(bucketNm, putRet.key, pfops, params);
                //打印返回的persistid
                System.out.println(persistid);
                //返回转码后的url:域名+空间名+文件名称
                String translatePath = prefix+key+newKey;
                list.add(translatePath);
            } catch (QiniuException e) {
                //捕获异常信息
                Response r = e.response;
                // 请求失败时简单状态信息
                System.out.println(r.toString());
                try {
                    // 响应的文本信息
                    System.out.println(r.bodyString());
                } catch (QiniuException e1) {
                }
            }
            /*******************   MP4转m3u8 end  ******************/
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String uploadPdfFile(InputStream image,String remoteDir) throws IOException {
        String path = "";
        try {
            UploadManager uploadManager = getUploadManager(bucketNm);
            String token = getToken(bucketNm);
            Response response = uploadManager.put(image,remoteDir+"/"+newName("1.png"), token,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            path = prefix+"/"+putRet.key;
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return path;
    }



    /**
     * 多文件上传至七牛云
     * @param files 文件数组
     * @param remoteDir 上传到七牛的文件目录
     * @return
     */
    public static List<String> uploadMore(MultipartFile[] files,String remoteDir) throws IOException {
        List<String> list = new ArrayList<>();
        try {
            if(files != null && files.length>0){
                for(int i=0;i<files.length;i++){
                    MultipartFile file =files[i];
                    UploadManager uploadManager = getUploadManager(bucketNm);
                    String token = getToken(bucketNm);
                    Response response = uploadManager.put(file.getInputStream(),remoteDir+"/"+newName(file.getOriginalFilename()), token,null,null);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    String path = prefix+"/"+putRet.key;
                    list.add(path);
                }
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过老文件的名称自动生成新的文件
     *
     * @param oldName
     * @return
     */
    public static String newName(String oldName) {
        String[] datas = oldName.split("\\.");
        String type = datas[datas.length - 1];
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        return newName;
    }

    /**
     * 获取上传管理器
     * @param bucketNm
     * @return
     */
    public static UploadManager getUploadManager(String bucketNm) {
        //构造一个带指定Zone对象的配置类
        //区域要和自己的bucket对上，不然就上传不成功
        //华东    Zone.zone0()
        //华北    Zone.zone1()
        //华南    Zone.zone2()
        //北美    Zone.zoneNa0()
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        return uploadManager;
    }
    /**
     * 获取Bucket的管理对象
     * @return
     */
    public static BucketManager getBucketManager() {
        //构造一个带指定Zone对象的配置类
        //区域要和自己的bucket对上，不然就上传不成功
        //华东    Zone.zone0()
        //华北    Zone.zone1()
        //华南    Zone.zone2()
        //北美    Zone.zoneNa0()
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(accessKey, secertKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        return bucketManager;
    }
    /**
     * 获取七牛云的上传Token
     * @param bucketNm
     * @return
     */
    public static String getToken(String bucketNm) {
        Auth auth = Auth.create(accessKey, secertKey);
        String upToken = auth.uploadToken(bucketNm);
        return upToken;
    }

    static class Result{
        private String url;
        private boolean error;

        public Result(boolean error) {
            super();
            this.error = error;
        }

        public Result( boolean error,String url) {
            super();
            this.url = url;
            this.error = error;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public boolean isError() {
            return error;
        }
        public void setError(boolean error) {
            this.error = error;
        }
    }
}
