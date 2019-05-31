
package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wangren
 * @Description: 医生区域
 * @create 2018-10-31 15:24
 **/
@Data
@Entity
@Table(name ="tb_doctor_area")
public class DoctorArea {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="area_code")
    private String areaCode;

    @Column(name="area_name")
    private String areaName;

    @Transient
    private String count;

}

