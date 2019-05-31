package adinnet.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/9/20.
 */
@Data
@Component
@ConfigurationProperties(prefix="qiniu")
public class QiniuConfig {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String prefix;
}
