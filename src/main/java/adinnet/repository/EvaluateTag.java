package adinnet.repository;

/**
 * Created by hasee on 2018/9/18.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangren
 * @Description: 角色
 * @create 2018-09-13 14:57
 **/
@Data
@Entity
@Table(name ="tb_evaluate_tag")
public class EvaluateTag extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id; // 编号
    @Column(name="type")
    private Integer type; //'0,视频印象；1，期待上的课程',
    @Column(name="content")
    private String content; //内容
    @Column(name="user_id")
    private Integer userId; //用戶Id

    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Transient
    public Integer nums;
}
