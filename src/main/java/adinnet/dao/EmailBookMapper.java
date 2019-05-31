package adinnet.dao;/**
 * @Description: 群发邮件模板
 * @author wangren
 * @create 2018-12-13 11:56
 **/

import com.adinnet.repository.EmailBook;
import com.adinnet.repository.ExaminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Description: 群发邮件模板
 * @author wangren
 * @create 2018-12-13 11:56
 **/
public interface EmailBookMapper extends JpaRepository<EmailBook, Integer>, JpaSpecificationExecutor<EmailBook> {
    @Query(value = "select\n" +
            "        teb.*\n" +
            "    from\n" +
            "        tb_email_book teb\n" +
            "    inner join\n" +
            "        tb_semester ts \n" +
            "            on teb.semester_id=ts.id \n" +
            "    where\n" +
            "        teb.is_delete=1 \n" +
            "        and ts.is_delete=1 \n" +
            "        and ts.time like CONCAT('%',:date,'%')",nativeQuery = true)
    public List<EmailBook> emailBookList(@Param(value = "date") String date);

}
