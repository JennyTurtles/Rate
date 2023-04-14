package org.sys.rate.mapper;

import org.sys.rate.model.*;


import java.util.List;

/**
 * 老师Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
public interface TeachersMapper 
{
    int updatePassword(Integer ID,String pass);

    Teachers getTeaByIDNumber(String IDNumber);

    Teachers loadUserByUsername(String username);
    /**
     * 查询老师
     * 
     * @param ID 老师ID
     * @return 老师
     */
    public Teachers selectTeachersById(Long ID);

    /**
     * 查询老师列表
     * 
     * @param teachers 老师
     * @return 老师集合
     */
    public List<Teachers> selectTeachersList(Teachers teachers);

    /**
     * 新增老师
     * 
     * @param teachers 老师
     * @return 结果
     */
    public int insertTeachers(Teachers teachers);

    /**
     * 修改老师
     * 
     * @param teachers 老师
     * @return 结果
     */
    public int updateTeachers(Teachers teachers);

    List<Teachers> check(List<Teachers> teachersList);

    /**
     * 删除老师
     * 
     * @param ID 老师ID
     * @return 结果
     */
    public int deleteTeachersById(Long ID);

    /**
     * 批量删除老师
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteTeachersByIds(String[] IDs);

    public List<Teachers> selectList();
    List<Teachers> getTeachers(String teaName);

    Teachers selectByPrimaryId(Integer id);
    int updateFROMImport(List<Teachers> record);
    int insertFROMImport(List<Teachers> record);
    List<String> getTeaNamesBySelect(String teaName);

    public List<Teachers> selectTeasByJobnumber(List<String> list);
    Teachers selectTeaByJobnumber(String jobnumber);
    public List<Teachers> selectTeasByName(List<String> list);
    int updateRoleOfTeachers(List<Teachers> teas);
    int updateRoleOfOneTeacher(Teachers tea);

}
