package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
/**
 * @author wangren
 * @Description: 用户角色
 * @create 2018-09-13 16:02
 **/
@Entity
@Data
@Table(name = "admin_role")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})

public class SysUserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //
    @Column(name="admin_id")
    private Integer adminId; //
    @Column(name="is_delete")
    private Integer isDelete; //
    @Transient
    private Integer roleId;
    @OneToOne
    @JoinColumn(name = "role_id")
    private SysRole sysRole;
}
