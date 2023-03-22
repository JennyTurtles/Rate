package org.sys.rate.service.expert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ScoresMapper;
import org.sys.rate.mapper.TeacherMapper;
import org.sys.rate.model.*;

import java.util.ArrayList;
import java.util.List;

@Service("TeacherService")
public class TeacherService implements UserDetailsService{
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ScoresMapper scoresMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teacher teacher = teacherMapper.loadUserByUsername(username);
        if (teacher == null) {
            throw new UsernameNotFoundException("experts用户名不存在!");
        }
        //experts.setRoles(expertsMapper.getAdminRolesById(experts.getId()));
        //System.out.println("expertsloadUserByUsername");
        return teacher;
    }

    public String getExpertNameById(Integer expertID){
        String name = teacherMapper.selectNameById(expertID);
        return name;
    }

   public Integer updateExpert(Teacher teacher){
       int i = teacherMapper.updateByPrimaryKey(teacher);
       return i;
   }

    public Integer insertExpert(Teacher teacher){
        int i = teacherMapper.insert(teacher);
        return i;
    }
    public Teacher queryById(Integer id){
        Teacher teacher = teacherMapper.selectByPrimaryId(id);
        return teacher;
    }
    public Integer deleteById(Integer id){
        int i = teacherMapper.deleteById(id);
        return  i;
    }

    public List<Activities> selectActivitiesById(Integer id){
        List<Activities> activities = teacherMapper.selectActivitiesById(id);
        return  activities;
    }

    public RespPageBean selectActivitiesById(Integer page, Integer size, Integer id) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Activities> data = teacherMapper.selectActivitiesById(id);
        Long total = teacherMapper.getTotalActivities(id);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;

   }

    public Integer getexpertIdByUsername(String expertUsername) {
       Integer id= teacherMapper.selectByUsername(expertUsername);
       return id;
    }

    public List<String> addScores(Integer expertID,Integer activitiesID,List<Scores> scoresList) {
        List<String> result = new ArrayList<>();
        StringBuilder error = null;
        for (Scores score : scoresList) {
            //检查专家该项评分存不存在
            if (teacherMapper.check(score.getExpertID(),score.getActivityID(),score.getParticipantID(),score.getScoreItemID()) > 0) {
                //存在就更新分数
                int i = scoresMapper.updateScore(score.getExpertID(),score.getActivityID(),
                        score.getParticipantID(),score.getScoreItemID(),score.getScore());
                if(i>0){
                    System.out.println("评分更新！条数：" + i);
                }else{
                    error = new StringBuilder();
                    error.append("评分更新失败");
                    result.add(error.toString());
                }
            } else {
                //没有就插入该评分
                int insert = scoresMapper.insertScore(score.getExpertID(),score.getActivityID(),
                        score.getParticipantID(),score.getScoreItemID(),score.getScore());
                if (insert > 0) {
                    System.out.println("insert->" + score + " 的信息插入成功");
                } else {
                    error = new StringBuilder();
                    error.append("评分插入失败");
                    result.add(error.toString());
                    System.out.println("insert->" + score + " 的信息插入失败");
                }
            }
        }
        if (result.size() != 0) {
            result.add("请将以上用户名重复的专家更正后再导入,其他专家已经导入成功！");
        }
        return result;
    }

}
