package adinnet.repository;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author wangren
 * @Description: 角色权限
 * @create 2018-09-13 16:04
 **/
@Data
@Entity
@Table(name ="role_menu")
public class SysRoleMenu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //
    @Column(name="role_id")
    private Integer roleId; //
    @Column(name="menu_id")
    private Integer menuId; //

}
