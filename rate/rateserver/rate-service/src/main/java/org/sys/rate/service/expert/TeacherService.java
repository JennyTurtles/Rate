package org.sys.rate.service.expert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sys.rate.mapper.ScoreItemMapper;
import org.sys.rate.mapper.ScoresMapper;
import org.sys.rate.mapper.TeacherMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.ParticipatesService;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;

@Service("TeacherService")
public class TeacherService implements UserDetailsService{
    private static Lock reentrantLock = new ReentrantLock();// 锁对象

    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ScoresMapper scoresMapper;
    @Autowired
    ScoreItemMapper scoreItemMapper;
    @Autowired
    ParticipatesService participatesService;


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

    @Transactional
    public List<String> addScores(Integer expertID,Integer activitiesID,List<Scores> scoresList) {
        //先找到评分项中的总分id(活动总评分)
        Integer scoreFullScore = scoreItemMapper.selectScoreItemFinal(activitiesID);
        List<String> result = new ArrayList<>();
        StringBuilder error = null;
        double sumTemp = 0;
        ArrayList arr = new ArrayList();
        List<Scores> fullScoreArr = new ArrayList();
        //对scoresList中对选手id拿出来并去重
        for(Scores s : scoresList){
            if(!arr.contains(s.getParticipantID())){
                arr.add(s.getParticipantID());
            }
        }
        boolean sumScoreFlage;
        //计算每个选手的活动总评分
        for(int i = 0;i < arr.size(); i++){
            sumScoreFlage = false;
            Scores fullScore = new Scores();
            sumTemp = 0;
            for (Scores score : scoresList){
                if(score.getParticipantID() == arr.get(i) && score.getScoreItemID() != scoreFullScore){
                    //有可能为空值
                    if(score.getScore() != null){
                        sumTemp += score.getScore();
                    }
                }
                //里面有总分对象
                if(score.getParticipantID() == arr.get(i) && score.getScoreItemID() == scoreFullScore){
                    sumScoreFlage = true;
                }
                if(fullScore.getActivityID() == null){
                    fullScore.setActivityID(score.getActivityID());
                    fullScore.setExpertID(score.getExpertID());
                    fullScore.setScoreItemID(scoreFullScore);
                }
            }
            if(sumScoreFlage){//有总分这个对象,就对分数进行修改
                for (Scores score : scoresList){
                    if(score.getParticipantID() == arr.get(i) && score.getScoreItemID() == scoreFullScore){
                        if(sumTemp == 0){//默认这个选手没有打分，所有都是null，和就是0
                            score.setScore(null);
                        }else {
                            score.setScore(sumTemp);
                        }
                    }
                }
            }else {//没有这个对象就添加
                if(sumTemp == 0){
                    fullScore.setScore(null);
                }else {
                    fullScore.setScore(sumTemp);
                }
                fullScore.setParticipantID((Integer) arr.get(i));
                fullScoreArr.add(fullScore);
            }

        }
        //将每个选手的活动总评分添加进去
        if(fullScoreArr.size() > 0){
            scoresList.addAll(fullScoreArr);
        }
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
                //为空就不插入
                if(score.getScore() == null){
                    continue;
                }
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
        reentrantLock.lock();
        for (int i = 0;i < arr.size();i ++){
            try {//开始算
                if (fullScoreArr.get(i).getScore() == null)
                    continue;
                Integer resp =participatesService.saveAvgscore((Integer) arr.get(i), activitiesID);
                Integer j = participatesService.saveAvgScores((Integer) arr.get(i), activitiesID);
                if(resp==0 || j == 0){result.add("平均分保存失败");}
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
        reentrantLock.unlock();
        if (result.size() != 0) {
            result.add("请将以上用户名重复的专家更正后再导入,其他专家已经导入成功！");
        }
        return result;
    }

}
