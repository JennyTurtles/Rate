package org.sys.rate.utils;

import org.sys.rate.model.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-10-24 8:11
 */
public class HrUtils {
    public static Admin getCurrentHr() {
        return ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
