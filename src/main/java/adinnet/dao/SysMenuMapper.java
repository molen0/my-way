package adinnet.dao;

import com.adinnet.repository.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wangren
 * @Description: 菜单
 * @create 2018-09-18 16:15
 **/
public interface SysMenuMapper extends JpaRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {
    @Query("from Menu  m where m.menuType != 'button' and m.isDelete = 1 order by m.listorder asc,m.createdAt asc")
    public List<Menu> queryAll();

    @Query("from Menu  m where  m.parentId = :parentid and m.isDelete = 1 order by m.listorder asc,m.updatedAt desc")
    public List<Menu> queryByParentId(@Param(value = "parentid") Integer parentid);

    @Query("update  Menu set isDelete =0 where menuId = :menuId")
    @Modifying
    public void upDateFlag(@Param(value = "menuId") Integer menuId);

    @Query(value ="select m.* from menu m " +
            "       left join role_menu rm on m.menu_id = rm.menu_id " +
            "        left join role r on r.role_id = rm.role_id" +
            "        left join admin_role ar on r.role_id = ar.role_id" +
            "        left join admin a on a.uid = ar.admin_id" +
            "        where a.uid = :uid and r.enable = 1 and m.menu_type != 'button' and ar.is_delete =1" +
            "        order by m.listorder asc,m.created_at asc",nativeQuery = true)
    public List<Menu> queryByUid(@Param(value = "uid") Integer uid);
}
