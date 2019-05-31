package adinnet.repository.dto;

import com.adinnet.repository.BaseEntity;
import lombok.Data;

/**
 * Created by wangx on 2018/9/30.
 */
@Data
public class CourseCoursewareDto extends BaseEntity {
    /**
     * 课程id
     */
    private int courseId;
    /**
     * 资源id
     */
    private int coursewareId;
    /**
     * 课程名
     */
    private String courseName;
    /**
     * 资源名
     */
    private String name;
    /**
     *pdf 路径
     */
    private String pdfPath;
    /**
     *
     */
    private String attachId;
}
