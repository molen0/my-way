package com.adinnet.service.Impl;

import com.adinnet.dao.DoctorAreaMapper;
import com.adinnet.dao.ExaminUserMapper;
import com.adinnet.repository.DoctorArea;
import com.adinnet.repository.Semester;
import com.adinnet.repository.vo.DoctorAreaVo;
import com.adinnet.repository.vo.ReportOnlineSignUpVo;
import com.adinnet.repository.vo.ReportVo;
import com.adinnet.service.DoctorAreaService;
import com.adinnet.service.ReportService;
import com.adinnet.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangren
 * @Description: 学分报表
 * @create 2018-10-31 15:00
 **/
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private DoctorAreaMapper doctorAreaMapper;

    @Autowired
    private ExaminUserMapper examinUserMapper;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private DoctorAreaService doctorAreaService;

    @Override
    public List<ReportVo> generalList(Semester semester,Integer property) {
        if(null == semester || null == semester.getId()){
            semester=  semesterService.queryThisSemester();
        }
        List<DoctorArea> doctorAreaList = areaList(semester.getId(),property);
        List<ReportVo> reportVos = new ArrayList<>();
        Integer count = 0;
        Integer count5 = 0;
        Integer count3 = 0;
        Integer count1 = 0;
        Integer count0 = 0;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);//控制保留小数点后几位，2：表示保留2位小数点
        for (DoctorArea doctorArea : doctorAreaList){
            ReportVo reportVo = new ReportVo();
            reportVo.setName(doctorArea.getAreaName());
            reportVo.setCountAll(doctorArea.getCount());
            count += Integer.parseInt(doctorArea.getCount());
            Integer cont5 = totleCrateList(doctorArea.getAreaCode(), 6.0, 5.0, semester.getId(),property);
            count5 += cont5;
            reportVo.setCount5(cont5.toString());
            reportVo.setPercent5(nf.format(((float)cont5/Float.parseFloat(doctorArea.getCount()))));
            Integer cont3 = totleCrateList(doctorArea.getAreaCode(), 4.0, 3.0, semester.getId(),property);
            count3 += cont3;
            reportVo.setCount3(cont3.toString());
            reportVo.setPercent3(nf.format(((float)cont3/Float.parseFloat(doctorArea.getCount()))));
            Integer cont1 = totleCrateList(doctorArea.getAreaCode(), 2.5, 0.5, semester.getId(),property);
            count1 += cont1;
            reportVo.setCount1(cont1.toString());
            reportVo.setPercent1(nf.format(((float)cont1/Float.parseFloat(doctorArea.getCount()))));
            reportVo.setCount0((Integer.parseInt(doctorArea.getCount())-(cont1+cont3+cont5))+"");
            count0 += Integer.parseInt(reportVo.getCount0());
            reportVo.setPercent0(nf.format((Float.parseFloat(reportVo.getCount0())/Float.parseFloat(doctorArea.getCount()))));
            reportVos.add(reportVo);
        }
        if(count>0){
            ReportVo reportVo = new ReportVo();
            reportVo.setName("总计数");
            reportVo.setCountAll(count.toString());
            reportVo.setCount5(count5.toString());
            reportVo.setPercent5(nf.format((float)count5/(float)count));
            reportVo.setCount3(count3.toString());
            reportVo.setPercent3(nf.format((float)count3/(float)count));
            reportVo.setCount1(count1.toString());
            reportVo.setPercent1(nf.format((float)count1/(float)count));
            reportVo.setCount0(count0.toString());
            reportVo.setPercent0(nf.format((float)count0/(float)count));
            reportVos.add(reportVo);
            addTobben(reportVos);
        }
        return reportVos;
    }

    @Override
    public Semester getSemester(Semester semester) {
        if(null == semester || null == semester.getId()){
            semester=  semesterService.queryThisSemester();
        }else{
            semester=  semesterService.queryById(semester.getId());
        }
        return semester;
    }

    public void addTobben(List<ReportVo> reportVos){
        ReportVo reportVo = new ReportVo();
        reportVo.setCount5("优秀");
        reportVo.setCount3("良");
        reportVo.setCount1("一般");
        reportVo.setCount0("不合格/不及格");
        reportVo.setPercent5("");
        reportVo.setPercent3("");
        reportVo.setPercent1("");
        reportVo.setPercent0("");
        reportVo.setName("");
        reportVo.setCountAll("");
        reportVos.add(reportVo);
    }
    @Override
    public List<DoctorArea> areaList(Integer semesterId,Integer property) {
        List list = doctorAreaMapper.findAllArea(semesterId,property);
        List<DoctorArea> doctorAreaArrayList = new ArrayList<>();
        for(Object row:list){
            Object[] cells = (Object[]) row;
            DoctorArea doctorArea = new DoctorArea();
            doctorArea.setCount(cells[0].toString());
            doctorArea.setAreaName((String)cells[1]);
            doctorArea.setAreaCode((String)cells[2]);
            doctorAreaArrayList.add(doctorArea);
        }
        return doctorAreaArrayList;
    }

    @Override
    public Integer totleCrateList(String area, Double crateD, Double crateX, Integer semesterId,Integer property) {
        return examinUserMapper.totleCrateList(area,crateD,crateX,semesterId,property);
    }

    public List<ReportOnlineSignUpVo> getSignUpAndCourseList(Integer semesterId, Integer property){
        List<ReportOnlineSignUpVo> reportVos = new ArrayList<>();
        //区域医生报名情况
        List<DoctorArea> doctorAreaList = areaLists(semesterId,property);
        //在编人数
        Integer inCount = 0;
        //报名人数
        Integer signUpCount = 0;
        //学习人数
        Integer learnTotalCount = 0;
        //培训人数
        Integer studyCount = 0;
        //考试完成人数
        Integer courseCount = 0;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);//控制保留小数点后几位，2：表示保留2位小数点
        for (DoctorArea doctorArea : doctorAreaList){
            ReportOnlineSignUpVo reportOnlineSignUpVo = new ReportOnlineSignUpVo();
            String areaCode = doctorArea.getAreaCode();
            //万达区域医生接口数据
            List<DoctorAreaVo> dlist = doctorAreaService.getDoctorAreaInfo(property);
            Boolean tip = true;
            for(DoctorAreaVo doctorAreaVo:dlist){
                if(areaCode.equals(doctorAreaVo.getAreaCode())){
                    tip = false;
                    Integer doctorNumer = doctorAreaVo.getDoctorNumer();
                    reportOnlineSignUpVo.setInCount(doctorNumer.toString());
                    inCount += doctorNumer;
                    break;
                }
            }
            if(tip){
                reportOnlineSignUpVo.setInCount("0");
            }

            Integer suCount = Integer.parseInt(doctorArea.getCount());
            reportOnlineSignUpVo.setAreaName(doctorArea.getAreaName());
            reportOnlineSignUpVo.setSignUpCount(doctorArea.getCount());
            signUpCount += suCount;
            if(suCount>0){
                //有学习记录
                Integer learnCount = examinUserMapper.totleLearnList(areaCode, semesterId,property,0);
                learnTotalCount += learnCount;
                reportOnlineSignUpVo.setLearnCount(learnCount.toString());
                reportOnlineSignUpVo.setPercentLearnCount(nf.format(((float)learnCount/Float.parseFloat(doctorArea.getCount()))));
                //参加过一次考试
                Integer sCount = examinUserMapper.totleExaminList(areaCode, 0, semesterId,property);
                studyCount += sCount;
                reportOnlineSignUpVo.setStudyCount(sCount.toString());
                reportOnlineSignUpVo.setPercentStudyCount(nf.format(((float)sCount/Float.parseFloat(doctorArea.getCount()))));
                //参加过所有10次考试
                Integer cCount = examinUserMapper.totleExaminList(areaCode, 9, semesterId,property);
                courseCount += cCount;
                reportOnlineSignUpVo.setCourseCount(cCount.toString());
                reportOnlineSignUpVo.setPercentCourseCount(nf.format(((float)cCount/Float.parseFloat(doctorArea.getCount()))));
            }else{
                reportOnlineSignUpVo.setLearnCount("0");
                reportOnlineSignUpVo.setPercentLearnCount("0.00%");
                reportOnlineSignUpVo.setStudyCount("0");
                reportOnlineSignUpVo.setPercentStudyCount("0.00%");
                reportOnlineSignUpVo.setCourseCount("0");
                reportOnlineSignUpVo.setPercentCourseCount("0.00%");
            }
            reportVos.add(reportOnlineSignUpVo);
        }
        if(signUpCount>0){
            ReportOnlineSignUpVo reportOnlineSignUpVo = new ReportOnlineSignUpVo();
            reportOnlineSignUpVo.setAreaName("总计数");
            reportOnlineSignUpVo.setInCount(inCount.toString());
            reportOnlineSignUpVo.setSignUpCount(signUpCount.toString());
            reportOnlineSignUpVo.setLearnCount(learnTotalCount.toString());
            reportOnlineSignUpVo.setPercentLearnCount(signUpCount==0?"0":nf.format((float)learnTotalCount/(float)signUpCount));
            reportOnlineSignUpVo.setStudyCount(studyCount.toString());
            reportOnlineSignUpVo.setPercentStudyCount(signUpCount==0?"0":nf.format((float)studyCount/(float)signUpCount));
            reportOnlineSignUpVo.setCourseCount(courseCount.toString());
            reportOnlineSignUpVo.setPercentCourseCount(signUpCount==0?"0":nf.format((float)courseCount/(float)signUpCount));
            reportVos.add(reportOnlineSignUpVo);
        }
        return reportVos;
    }

    public List<DoctorArea> areaLists(Integer semesterId,Integer property) {
        List list = doctorAreaMapper.findAllAreas(semesterId,property);
        List<DoctorArea> doctorAreaArrayList = new ArrayList<>();
        for(Object row:list){
            Object[] cells = (Object[]) row;
            DoctorArea doctorArea = new DoctorArea();
            doctorArea.setCount(cells[3].toString());
            doctorArea.setAreaName((String)cells[2]);
            doctorArea.setAreaCode((String)cells[1]);
            doctorAreaArrayList.add(doctorArea);
        }
        return doctorAreaArrayList;
    }
}
