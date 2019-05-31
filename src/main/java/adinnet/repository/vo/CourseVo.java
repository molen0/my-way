package adinnet.repository.vo;

import com.adinnet.repository.CourseOutline;
import lombok.Data;

import java.util.List;

/**
 * 课程Vo
 * Created by adinnet on 2018/9/20.
 */
@Data
public class CourseVo {
    private Integer id;

    //课程名称
    private String title;

    //课程图片
    private String photo;

    //课程章节数
    private Integer chapterCount;

    //主讲人
    private String teachers;
}
