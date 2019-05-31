package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wangren
 * @Description: 资源权限
 * @create 2018-09-13 14:58
 **/
@Data
@Entity
@Table(name ="menu")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Menu  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="menu_id")
    private Integer menuId;//主键.
    @Column(name="menu_name")
    private String menuName;//名称.
    @Column(name="menu_type")
    private String menuType;//资源类型，[menu|button]
    @Column(name="menu_url")
    private String menuUrl;//资源路径.
    @Column(name="menu_code")
    private String menuCode; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    @Column(name="parent_id")
    private Integer parentId; //父编号
    @Column(name="listorder")
    private Integer listorder ;
    @Column(name="is_delete")
    private Integer isDelete; //
    @Column(name="created_at",updatable=false)
    private String createdAt; //父编号
    @Column(name="updated_at")
    private String updatedAt; //父编号列表
    @Transient
    private List<Menu> children;
}
