package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wangren
 * @Description: 邮件
 * @create 2018-10-08 14:57
 **/
@Data
@Entity
@Table(name ="tb_mail")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;//主键.

    @Column(name="user_id")
    private Integer userId;//用户id

    @Column(name="examin_id")
    private Integer examinId;

    @Column(name="semester_id")
    private Integer semesterId;

    //@Transient
    //private Integer semesterId;
    @Transient
    private Integer dat;

    @Column(name="tittle")
    private String tittle;

    @Column(name="hand_book_url")
    private String handBookUrl;

    @Column(name="content")
    private String content;

    @Column(name="send_date")
    private String sendDate;

    @Column(name="user_mail")
    private String userMail;

    @Column(name="user_name")
    private String userName;

    @Column(name="fj")
    private String fj;

    @Column(name="is_delete")
    private Integer isDelete;

    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
