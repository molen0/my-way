package adinnet.repository.vo;

import lombok.Data;

/**
 * @author wangren
 * @Description: 学分管理视图
 * @create 2018-10-31 14:51
 **/
@Data
public class ReportVo {

    private Integer id;

    private String name;

    private String countAll;

    private String count5;

    private String percent5;

    private String count3;

    private String percent3;

    private String count1;

    private String percent1;

    private String count0;

    private String percent0;
}
