package adinnet.service.Impl;

import com.adinnet.dao.AttachMapper;
import com.adinnet.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hasee on 2018/9/19.
 */
@Service
public class AttachServiceImpl implements AttachService {
    @Autowired
    private AttachMapper attachMapper;

}
