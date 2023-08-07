package org.sys.rate.service.admin;/**
 * ClassName: PublicationSubmissionService
 * Package: org.sys.rate.service.admin
 * Description:
 *
 * @Author ZYK
 * @Create 2023/7/7 20:15
 * @Version 1.0
 */

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.mapper.PublicationSubmissionMapper;
import org.sys.rate.model.Publication;
import org.sys.rate.model.PublicationSubmission;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Service
public class PublicationSubmissionService {
    @Resource
    private PublicationSubmissionMapper submissionMapper;

    public List<PublicationSubmission> getAllStuSubmission(Integer stuID) {
        List<PublicationSubmission> submissions = submissionMapper.getAllStuSubmission(stuID);
        return submissions;
    }
}
