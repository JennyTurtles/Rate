package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.PEexport;
import org.sys.rate.model.Participates;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ParticipatesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Participates record);
    void insert_update(Participates record);

    Integer getID(@Param("idNumber") String idNumber);

    Integer getlastDisplaySequence(@Param("groupID") Integer groupID);

    Integer getPID(Participates idNumber);

    int insert_relationship(Participates record);

    int update_relationship(Participates record);

    Integer checkByIDandActivityID(@Param("id") Integer id, @Param("activityid") Integer activityid);

    Integer checkGroupIDExists(@Param("studentID") Integer id, @Param("activityID") Integer activityid);

    Integer getOldGroupByIDandActivityID(@Param("id") Integer id, @Param("activityid") Integer activityid);

    String selectGroupNameById(Integer groupId);

    int delete(Participates record);

    int update(Participates record);

    int insertSelective(Participates record);

    Participates selectByPrimaryKey(Integer id);

    List<Participates> getActivitiesByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("groupID") Integer groupID,@Param("emp") Participates employee);

    List<Participates> getPByGID(@Param("page") Integer page, @Param("size") Integer size, @Param("groupID") Integer groupID,@Param("emp") Participates employee);

    List<Participates> getPByACID(@Param("page") Integer page, @Param("size") Integer size, @Param("groupID") Integer groupID,@Param("emp") Participates employee);

    List<Participates> getParticipantsPageByACID(@Param("page") Integer page, @Param("size") Integer size, @Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);

    List<Participates> getLongParticipantsByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("groupID") Integer groupID, @Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);

    List<Participates> getActivitiesByPageAndCompany(@Param("page") Integer page, @Param("size") Integer size, @Param("company") String company);

    List<Participates> getParticipatesByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("emp") Participates employee,@Param("beginDateScope") Date[] beginDateScope);

    List<PEexport> getExpertScore(@Param("page") Integer page, @Param("size") Integer size,@Param("groupID") Integer groupID,@Param("activityID") Integer activityID);

    Long getTotalEA(@Param("activityID") Integer activityID,@Param("groupID") Integer groupID);

    Long getTotal(@Param("groupID") Integer groupID,@Param("emp") Participates employee);

    Long getTotalByACID(@Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);

    Long getTotaloutput(@Param("emp") Participates employee,@Param("beginDateScope") Date[] beginDateScope);

    Long getTotalByCompany(@Param("company") String company);

    Integer maxWorkID();


    Integer addParticipatess(@Param("list") List<Participates> list);

    Participates getEmployeeById(@Param("ID")Integer id);

    List<Participates> getEmployeeByPageWithSalary(@Param("page") Integer page, @Param("size") Integer size);

    Integer updateEmployeeSalaryById(@Param("eid") Integer eid, @Param("sid") Integer sid);

    Integer subDisplaySequence(@Param("groupID") Integer groupID, @Param("small") Integer small,@Param("big") Integer big);

    Integer addDisplaySequence(@Param("groupID") Integer groupID, @Param("small") Integer small,@Param("big") Integer big);

    Integer saveDisplaySequence(@Param("groupID") Integer groupID, @Param("newP") Integer newP,@Param("ID") Integer ID);

    List<Participates> getPartByGroupId(@Param("activitiesID") Integer activitiesID,
                                        @Param("groupId") Integer groupId);

    List<PEexport> getPartByGroupIdForPEexpert(@Param("activityID") Integer activitiesID);
    List<PEexport> getPartByGroupIdForPEexpert_null(@Param("activityID") Integer activitiesID);

    List<PEexport> getPartByGroupIdForPEexpertByGID(@Param("groupID") Integer groupID);
    List<PEexport> getPartByGroupIdForPEexpert_nullByGID(@Param("groupID") Integer groupID);

    List<PEexport> getExpertSituationByExpertID(@Param("page") Integer page, @Param("size") Integer size,@Param("groupID") Integer groupID,@Param("expertID") Integer expertID);

    Long getExpertSituationTotal(@Param("groupID") Integer groupID,@Param("expertID") Integer expertID);


    List<Participates> selectAllParticipants(@Param("activityId") Integer activityId,
                                             @Param("groupId") Integer groupId);

    Integer saveScore(@Param("participateId") Integer participateId, @Param("score") Double score);

    void saveLscore(@Param("participantID") Integer participantID,
                    @Param("sum") Double sum);

    Integer saveAvgscore(@Param("participantID") Integer participantID,
                         @Param("activityID") Integer activityID);

    @Select("SELECT COUNT(*) from participants WHERE studentID = #{stuId}")
    int isParticipants(Integer stuId);

    List<Participates> getParticipantByAIdAndID(Integer activityID, List<Integer> participantID);

    Participates getParticipantIDByIDNumber(Integer activityID,String IDNumber);

    void deleteGroupsOfParticipantsAnaAcitivity(Integer activityID);
    void deleteGroups(Integer activityID);

}
