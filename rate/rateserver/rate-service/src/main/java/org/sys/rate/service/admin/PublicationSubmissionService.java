package org.sys.rate.service.admin;/**
 * ClassName: PublicationSubmissionService
 * Package: org.sys.rate.service.admin
 * Description:
 *
 * @Author ZYK
 * @Create 2023/7/7 20:15
 * @Version 1.0
 */

import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.mapper.PublicationSubmissionMapper;

import javax.annotation.Resource;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Service
public class PublicationSubmissionService {
    @Resource
    private PublicationSubmissionMapper publicationSubmissionMapper;

    @Resource
    private PublicationMapper publicationMapper;


}
