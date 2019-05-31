package adinnet.service;

import com.adinnet.repository.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by adinnet on 2018/9/20.
 */
public interface DoctorService {
    public Integer save(Doctor doctor);

    public void update(Doctor doctor);

    public void delete(Doctor doctor);

    public Doctor queryById(Integer id);

    /**
     * 分页查询
     * @param doctor
     * @return
     */
    public Page<Doctor> pageList(Doctor doctor);

    public Doctor queryByUid(String uid);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Doctor getDoctorInfo(String uid, String token, Integer doctorId);

    public Doctor getOne(Integer id);
}
