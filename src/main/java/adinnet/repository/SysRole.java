package adinnet.repository;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author wangren
 * @Description: 角色
 * @create 2018-09-13 14:57
 **/
@Data
@Entity
@Table(name ="role")
public class SysRole extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer id; // 编号
    @Column(name="role_name")
    private String roleName; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    @Column(name="role_desc")
    private String roleDesc; // 角色描述,UI界面显示使用
    @Column(name="enable")
    private Integer enable; // 是否可用,如果不可用将不会添加给用户

    @Column(name="updated_at")
    private String updatedAt;
    @Column(name="created_at",updatable=false)
    private String createdAt;

}
