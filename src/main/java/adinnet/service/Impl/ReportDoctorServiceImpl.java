package adinnet.service.Impl;

import com.adinnet.dao.DoctorAreaMapper;
import com.adinnet.dao.ReportDoctorMapper;
import com.adinnet.repository.*;
import com.adinnet.service.ReportDoctorService;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/10/31.
 */
@Service
public class ReportDoctorServiceImpl  implements ReportDoctorService {
    @Autowired
    private ReportDoctorMapper reportDoctorMapper;
    @Autowired
    private DoctorAreaMapper doctorAreaMapper;
    @Override
    public PageBean<ReportDoctor> pageList(ReportDoctor reportDoctor) {
        PageBean<ReportDoctor> page = new PageBean<>();
        String hql =  " SELECT d.*,"+
                " s.id AS sdId,da.area_name AS areaName, " +
                " (SELECT IFNULL(SUM(eu.credit), 0) FROM tb_examin_user eu " +
                "  JOIN tb_examin e ON eu.examin_id = e.id JOIN tb_course c ON e.course_id = c.id" +
                " WHERE eu.user_id = d.id and c.course_type = 1) AS credit " +
                " FROM tb_doctor d " +
                " LEFT JOIN tb_doctor_area da ON d.area = da.area_code JOIN tb_semester_doctor sd on sd.doctor_id = d.id " +
                " JOIN tb_semester s ON sd.semester_id = s.id HAVING 1=1 ";
        String countHql = " SELECT COUNT(1) FROM ( SELECT d.*," +
                "  s.id AS sdId,da.area_name AS areaName, " +
                " (SELECT IFNULL(SUM(eu.credit), 0) FROM tb_examin_user eu " +
                "  JOIN tb_examin e ON eu.examin_id = e.id JOIN tb_course c ON e.course_id = c.id" +
                " WHERE eu.user_id = d.id and c.course_type = 1) AS credit FROM tb_doctor d " +
                " LEFT JOIN tb_doctor_area da ON d.area= da.area_code JOIN tb_semester_doctor sd on sd.doctor_id = d.id " +
                " JOIN tb_semester s ON sd.semester_id = s.id HAVING 1=1 ";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != reportDoctor){
            if(null != reportDoctor.getCredit() && !"".equals(reportDoctor.getCredit())){
                str.append(" and credit = '"+reportDoctor.getCredit()+"' ");
                str1.append(" and credit = '"+reportDoctor.getCredit()+"' ");
            }

            if(null != reportDoctor.getName() && !"".equals(reportDoctor.getName())){
                str.append(" and  d.name like concat('%','"+reportDoctor.getName().trim()+"','%')   ");
                str1.append(" and  d.name like concat('%','"+reportDoctor.getName().trim()+"','%')   ");
            }
            if(null != reportDoctor.getArea() && !"".equals(reportDoctor.getArea())){
                str.append(" and  d.area = '"+reportDoctor.getArea()+"'  ");
                str1.append(" and  d.area = '"+reportDoctor.getArea()+"'  ");
            }
            if(null!= reportDoctor.getSdId() && !"".equals(reportDoctor.getSdId())){
                str.append(" and  s.id = '"+reportDoctor.getSdId()+"'  ");
                str1.append(" and  s.id = '"+reportDoctor.getSdId()+"'  ");
            }
        }
        str1.append(") as a");
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("d.update_time desc");
        page.setPageNum(reportDoctor.getOffset());
        page.setPageSize(reportDoctor.getLimit());
        page = reportDoctorMapper.findNatPage(page);
        List<ReportDoctor> reportDoctors = page.getDatas();

        for(ReportDoctor report: reportDoctors){
            String areaCode = report.getArea();
           DoctorArea doctorArea = doctorAreaMapper.findByareaCode(areaCode);
            if(doctorArea != null){
                report.setAreaName(doctorArea.getAreaName()==null?"":doctorArea.getAreaName());
            }
            Integer doctorId = report.getId();
            String credit=  reportDoctorMapper.findByDoctorId(doctorId);
            report.setCredit(credit==null?"":credit);
        }
        return page;
    }

    @Override
    public List<DoctorArea> findAllArea() {
        return doctorAreaMapper.findAllArea();
    }

    @Override
    public PageBean<ReportDoctor> page(ReportDoctor reportDoctor) {
        PageBean<ReportDoctor> page = new PageBean<>();
        String hql =  " SELECT d.*," +
               " s.id AS sdId,da.area_name AS areaName, " +
                " (SELECT IFNULL(SUM(eu.credit), 0) FROM tb_examin_user eu " +
                "  JOIN tb_examin e ON eu.examin_id = e.id JOIN tb_course c ON e.course_id = c.id" +
                " WHERE eu.user_id = d.id and c.course_type = 1) AS credit " +
                " FROM tb_doctor d " +
                " LEFT JOIN tb_doctor_area da ON d.area = da.area_code JOIN tb_semester_doctor sd on sd.doctor_id = d.id " +
                " JOIN tb_semester s ON sd.semester_id = s.id HAVING 1=1 ";
        String countHql = " SELECT COUNT(1) FROM ( SELECT d.*," +
              "  s.id AS sdId,da.area_name AS areaName, " +
                " (SELECT IFNULL(SUM(eu.credit), 0) FROM tb_examin_user eu " +
                "  JOIN tb_examin e ON eu.examin_id = e.id JOIN tb_course c ON e.course_id = c.id" +
                " WHERE eu.user_id = d.id and c.course_type = 1) AS credit FROM tb_doctor d " +
                " LEFT JOIN tb_doctor_area da ON d.area= da.area_code JOIN tb_semester_doctor sd on sd.doctor_id = d.id " +
                " JOIN tb_semester s ON sd.semester_id = s.id HAVING 1=1 ";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != reportDoctor){
            if(null != reportDoctor.getCredit() && !"".equals(reportDoctor.getCredit())){
                str.append(" and credit = '"+reportDoctor.getCredit()+"' ");
                str1.append(" and credit = '"+reportDoctor.getCredit()+"' ");
            }

            if(null != reportDoctor.getName() && !"".equals(reportDoctor.getName())){
                str.append(" and  d.name like concat('%','"+reportDoctor.getName().trim()+"','%')   ");
                str1.append(" and d.name like concat('%','"+reportDoctor.getName().trim()+"','%')   ");
            }
            if(null != reportDoctor.getArea() && !"".equals(reportDoctor.getArea())){
                str.append(" and  d.area = '"+reportDoctor.getArea()+"'  ");
                str1.append(" and d.area = '"+reportDoctor.getArea()+"'  ");
            }
            if(null!= reportDoctor.getSdId() && !"".equals(reportDoctor.getSdId())){
                str.append(" and  s.id = '"+reportDoctor.getSdId()+"'  ");
                str1.append(" and  s.id = '"+reportDoctor.getSdId()+"'  ");
            }
        }
        str1.append(") as a");
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("d.update_time desc");
        page = reportDoctorMapper.findNatPage(page);
        List<ReportDoctor> reportDoctors = page.getDatas();

        for(ReportDoctor report: reportDoctors){
            String areaCode = report.getArea();
            DoctorArea doctorArea = doctorAreaMapper.findByareaCode(areaCode);
            if(doctorArea != null){
                report.setAreaName(doctorArea.getAreaName()==null?"":doctorArea.getAreaName());
            }
            Integer doctorId = report.getId();
            String credit=  reportDoctorMapper.findByDoctorId(doctorId);
            report.setCredit(credit==null?"":credit);
        }
        return page;
    }
}
