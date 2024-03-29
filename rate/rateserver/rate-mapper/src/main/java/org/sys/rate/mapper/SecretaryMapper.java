package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;
import org.sys.rate.model.Activities;
import org.sys.rate.model.Experts;
import org.sys.rate.model.Participates;

import java.util.List;

public interface SecretaryMapper {
    @Select("SELECT COUNT(*) from expertactivities WHERE teacherID = #{Id} AND (role = '秘书' OR role = '组长') Limit 1")
    int isSecretary(Integer Id);

    @Update("UPDATE expertactivities SET role = #{role} WHERE teacherID = #{teacherID} AND activityID = #{activityID} AND groupID = #{groupID}")
    int setSecretary(Integer teacherID, Integer activityID, Integer groupID,String role);

    @Select("SELECT a.ID,groupID,a.name,startDate,scoreItemCount,score,groupCount,g.expertCount,g.participantCount,comment,g.name as groupName,a.haveSub,a.haveComment,a.gradeFormType\n" +
            "FROM expertactivities e, activities a, `groups` g\n" +
            "WHERE e.activityID = a.ID AND e.groupID = g.ID AND teacherID = #{teacherID} AND (role = '秘书' OR role = '组长')")
    List<Activities> getAct(Integer teacherID);

    @Select("SELECT s.ID,name,code\n" +
            "FROM participants p, student s \n" +
            "WHERE groupID = #{groupID} AND p.studentID = s.Id AND activityID = #{activityID}")
    List<Participates> getPar(Integer activityID,Integer groupID);

    @Select("SELECT t.ID,name,IDNumber,jobnumber\n" +
            "FROM expertactivities e,teacher t \n" +
            "WHERE groupID = #{groupID} AND e.teacherID = t.ID AND activityID = #{activityID}")
    List<Experts> getEx(Integer activityID,Integer groupID);
}
