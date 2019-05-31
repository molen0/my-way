package adinnet.dao;

import com.adinnet.repository.SysRole;
import com.adinnet.repository.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author wangren
 * @Description: 用户
 * @create 2018-09-13 15:43
 **/
public interface SysUserMapper extends JpaRepository<UserInfo, Integer>, JpaSpecificationExecutor<UserInfo> {

    @Query("from UserInfo p where p.username =:username")
    public UserInfo queryByUserName(@Param(value = "username") String username);

    @Query("update  UserInfo p set p.state =0 where p.uid = :uid")
    @Modifying
    public void delAllBeUid(@Param(value = "uid") Integer uid);
}
