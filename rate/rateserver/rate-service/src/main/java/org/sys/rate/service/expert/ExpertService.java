package org.sys.rate.service.expert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;

import javax.lang.model.element.NestingKind;
import javax.swing.text.Document;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service("expertService")
public class ExpertService implements UserDetailsService {
	@Autowired
	ExpertsMapper expertsMapper;
	@Autowired
	ExpertactivitiesMapper expertactivitiesMapper;
	@Autowired
	ActivitiesMapper activitiesMapper;
	@Autowired
	ParticipatesMapper participatesMapper;
	@Autowired
	ScoreItemMapper scoreItemMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Experts experts = expertsMapper.loadUserByUsername(username);
		if (experts == null) {
			throw new UsernameNotFoundException("experts用户名不存在!");
		}
		experts.setRoles(expertsMapper.getAdminRolesById(experts.getId()));
		return experts;
	}

	public Integer updateExpert(Experts experts) {
		int i = expertsMapper.updateByPrimaryKey(experts);
		return i;
	}

	public Integer insertExpert(Experts experts) {
		int i = expertsMapper.insert(experts);
		return i;
	}

	public Experts queryById(Integer id) {
		Experts experts = expertsMapper.selectByPrimaryId(id);
		return experts;
	}

	public Integer deleteById(Integer id) {
		int i = expertsMapper.deleteById(id);
		return i;
	}

	public List<Activities> selectActivitiesById(Integer id) {
		List<Activities> activities = expertsMapper.selectActivitiesById(id);
		return activities;
	}

	public RespPageBean selectActivitiesById(Integer page, Integer size, Integer id) {
		if (page != null && size != null) {
			page = (page - 1) * size;
		}
		List<Activities> data = expertsMapper.selectActivitiesById(id);
		Long total = expertsMapper.getTotalActivities(id);
		RespPageBean bean = new RespPageBean();
		bean.setData(data);
		bean.setTotal(total);
		return bean;

	}

	public List<Experts> getExpertsByPage(Integer keywords, Integer page, Integer size, Experts employee) {
		if (page != null && size != null) {
			page = (page - 1) * size;
		}
		Long total = expertsMapper.getTotal(keywords, employee);
		List<Experts> data = expertsMapper.getExpertsByPage(keywords, page, size, employee);
		RespPageBean bean = new RespPageBean();
		bean.setData(data);
		bean.setTotal(total);
		return data;
	}

	public RespPageBean getExpertSituationByExpertID(Integer keywords,Integer expertID, Integer page, Integer size) {
		if (page != null && size != null) {
			page = (page - 1) * size;
		}
		HashPEexport hashPEexport=new HashPEexport();
		HashMap<Integer, String> Gmap = new LinkedHashMap<>();
		HashMap<Integer, String> Emap = new LinkedHashMap<>();
		HashMap<Integer, String> Smap = new LinkedHashMap<>();
		HashMap<Integer,HashMap<Integer,HashMap<Integer,Participates>>> map = new LinkedHashMap<>();//ID,map(ExpertID,map(PID,P))
		Long total = participatesMapper.getExpertSituationTotal(keywords,expertID);
		List<PEexport> data = participatesMapper.getExpertSituationByExpertID(page, size,keywords,expertID);
		for(PEexport experts:data)//获得expert对应的participants和他们的分数
		{//专家给同组的选手打分的选手id//通过pid获得scoreitemMap//Pt.setSMap()
			Participates par=new Participates();
			par.setName(experts.getParticipantName());
			par.setCode(experts.getCode());
			par.setDisplaySequence(experts.getDisplaySequence());
			ScoreItemValue scoreItemValue=new ScoreItemValue();
			scoreItemValue.setScore(experts.getScore());
			scoreItemValue.setName(experts.getScoreItemName());
			scoreItemValue.setId(experts.getScoreItemID());
			/*HashMap<Integer, ScoreItemValue> mapScore = new HashMap<>();
			mapScore.put(experts.getScoreItemID(),scoreItemValue);
			par.setScoremap(mapScore);*/
			if(map.get(keywords)==null)
			{//getGroupID对应value-null//首次插入
				HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
				mapScore.put(experts.getScoreItemID(),scoreItemValue);
				par.setScoremap(mapScore);
				HashMap<Integer,Participates> p= new LinkedHashMap<>();
				p.put(experts.getParticipantID(),par);
				HashMap<Integer,HashMap<Integer,Participates>> e= new LinkedHashMap<>();
				e.put(expertID,p);
				map.put(keywords,e);
			}
			else{//有group
				if(map.get(keywords).get(expertID)==null) {//没有getExpertID
					//ParticipantID对应value-null
					//getScoremap-null
					HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
					mapScore.put(experts.getScoreItemID(),scoreItemValue);
					par.setScoremap(mapScore);
					HashMap<Integer,Participates> p= new LinkedHashMap<>();
					p.put(experts.getParticipantID(),par);
					map.get(keywords).put(expertID,p);
				}
				else{//有getExpertID
					if(map.get(keywords).get(expertID).get(experts.getParticipantID())==null) {//没有ParticipantID
						//getScoremap-null
						HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
						mapScore.put(experts.getScoreItemID(),scoreItemValue);
						par.setScoremap(mapScore);
						map.get(keywords).get(expertID).put(experts.getParticipantID(),par);
					}else {//有ParticipantID
						if(map.get(keywords).get(expertID).get(experts.getParticipantID()).getScoremap()==null) {//getScoremap-null
							HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
							mapScore.put(experts.getScoreItemID(),scoreItemValue);
							map.get(keywords).get(expertID).get(experts.getParticipantID()).setScoremap(mapScore);
						}
						else {
							map.get(keywords).get(expertID).get(experts.getParticipantID()).getScoremap().put(experts.getScoreItemID(),scoreItemValue);
						}
					}
				}
			}

			Gmap.put(keywords,"组名");
			Emap.put(expertID,"专家名");
			Smap.put(experts.getScoreItemID(),experts.getScoreItemName());
		}
		hashPEexport.setMap(map);
		hashPEexport.setEmap(Emap);
		hashPEexport.setGmap(Gmap);
		hashPEexport.setSmap(Smap);
		List<HashPEexport> list=new ArrayList<>();
		list.add(hashPEexport);
		RespPageBean bean = new RespPageBean();
		bean.setData(list);
		bean.setTotal(total);
		return bean;
	}

	public Integer updateTeacherCount(Experts id) {
		int result = expertsMapper.update(id);
		return result;
	}

	public boolean updatePasswd(Integer id, String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		password = encoder.encode(password.substring(12, 18));
		int result = expertsMapper.updatePasswd(id, password);
		if (result == 1) {
			return true;
		}
		return true;
	}

	public boolean withdrawScore(Integer activityID,Integer groupID,Experts teacher){
		Integer id = expertsMapper.getID(teacher.getIdnumber());
		int result = expertsMapper.withdrawScore(activityID,groupID,id);
		if (result == 1) {
			return true;
		}
		return true;
	}

	public String sh1(String password) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		assert digest != null;
		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		for (byte b : hash) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	//2.service
	public List<String> addTeachers(Integer groupid, Integer activityid, List<Experts> list) {
		List<String> result = new ArrayList<>();
		StringBuilder error = null;
		for (Experts experts : list) {
			Expertactivities expertactivities = new Expertactivities();
			expertactivities.setActivityid(activityid);
			expertactivities.setGroupid(groupid);
			expertactivities.setFinished(false);
			//如果是1，则为本单位，再设置为管理员的instituteId，否则为null
			if (experts.getInstitutionid() == 1) {
				int instituteId = activitiesMapper.selectByActivityId(activityid);//通过活动号查找管理员组织号，新增的ActivitiesMapper方法
				experts.setInstitutionid(instituteId);
			} else {
				experts.setInstitutionid(null);
			}
			//检查专家身份存不存在
			if (expertsMapper.check(experts) > 0) {//这里是idnumbercheck
				//存在就更新信息,不更新电话，邮箱，用户名，密码。
				int i = expertsMapper.updateByIdNumber(experts);
				System.out.println("专家信息更新！条数：" + i + " id: " + experts.getName());
			} else {
				//对密码进行处理，默认身份证后六位。
				if (experts.getPassword().equals("")) {
					experts.setPassword(sh1(experts.getIdnumber().substring(12, 18)));
				} else {
					experts.setPassword(sh1(experts.getPassword()));
				}
				//对用户名进行处理，如果没有读到默认为电话号码
				experts.setUsername(experts.getUsername().equals("") ? experts.getPhone() : experts.getUsername());
				if (expertsMapper.checkUsername(experts.getUsername()) > 0) {//这里是username_check
					//用户名存在不导入
					error = new StringBuilder();
					error.append("专家姓名：").append(experts.getName()).append(",").append("重复的用户名：").append(experts.getUsername()).append(";");
					result.add(error.toString());
					System.out.println(experts.getUsername() + " exists!");
				} else {
					int insert = expertsMapper.insert(experts);
					if (insert > 0) {
						System.out.println("insert->" + experts.getName() + " 的信息插入成功");
					} else {
						System.out.println("insert->" + experts.getName() + " 的信息插入失败");
					}
				}
			}
			//在活动组中加入专家
			expertactivities.setTeacherID(expertsMapper.getID(experts.getIdnumber()));
			Integer expertId = expertsMapper.getID(experts.getIdnumber());
			Integer pend = expertactivitiesMapper.checkByIDandActivityID(expertId, activityid);
			if (pend != null && pend.equals(groupid)) {
				System.out.println("该组已经有专家： " + experts.getName());
			} else {
				int insert = expertactivitiesMapper.insert(expertactivities);
				if (insert > 0) {
					System.out.println("专家插入成功！");
				} else {
					System.out.println("专家插入失败");
				}
			}
		}
		if(result.size()!=0){
			result.add("请将以上用户名重复的专家更正后再导入,其他专家已经导入成功！");
		}
		return result;
	}

	public Integer updateExActivityTable(Integer groupid, Integer activityid, Experts teacher) {
		Integer id = expertsMapper.getID(teacher.getIdnumber());
		return expertactivitiesMapper.updategroupid(groupid, activityid, id);
	}

	public Integer deleteExActivityTable(Integer groupid, Integer activityid, Experts teacher) {
		Integer id = expertsMapper.getID(teacher.getIdnumber());
		return expertactivitiesMapper.deletegroupid(groupid, activityid, id);
	}

	public HashPEexport  getExpertList(String defination,Integer activityID) {
		HashPEexport hashPEexport=new HashPEexport();
		HashMap<Integer, String> Gmap = new LinkedHashMap<>();
		HashMap<Integer, String> Emap = new LinkedHashMap<>();
		HashMap<Integer, String> Smap = new LinkedHashMap<>();
		HashMap<Integer, String> SNotByEmap = new LinkedHashMap<>();
		List<PEexport> data = null;
		if(defination=="activityID")
			data=participatesMapper.getPartByGroupIdForPEexpert(activityID);
		if(defination=="groupID")
			data=participatesMapper.getPartByGroupIdForPEexpertByGID(activityID);
		HashMap<Integer,HashMap<Integer,HashMap<Integer,Participates>>> map = new LinkedHashMap<>();//ID,map(ExpertID,map(PID,P))
		for(PEexport experts:data)//获得expert对应的participants和他们的分数
		{//专家给同组的选手打分的选手id//通过pid获得scoreitemMap//Pt.setSMap()
			Participates par=new Participates();
			par.setName(experts.getParticipantName());
			par.setCode(experts.getCode());
			par.setDisplaySequence(experts.getDisplaySequence());
			ScoreItemValue scoreItemValue=new ScoreItemValue();
			scoreItemValue.setScore(experts.getScore());
			scoreItemValue.setName(experts.getScoreItemName());
			scoreItemValue.setId(experts.getScoreItemID());
			/*HashMap<Integer, ScoreItemValue> mapScore = new HashMap<>();
			mapScore.put(experts.getScoreItemID(),scoreItemValue);
			par.setScoremap(mapScore);*/
			if(map.get(experts.getGroupID())==null)
			{//getGroupID对应value-null//首次插入
							HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
							mapScore.put(experts.getScoreItemID(),scoreItemValue);
							par.setScoremap(mapScore);
							HashMap<Integer,Participates> p= new LinkedHashMap<>();
							p.put(experts.getParticipantID(),par);
							HashMap<Integer,HashMap<Integer,Participates>> e= new LinkedHashMap<>();
							e.put(experts.getExpertID(),p);
							map.put(experts.getGroupID(),e);
			}
			else{//有group
				if(map.get(experts.getGroupID()).get(experts.getExpertID())==null) {//没有getExpertID
					//ParticipantID对应value-null
					//getScoremap-null
							HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
							mapScore.put(experts.getScoreItemID(),scoreItemValue);
							par.setScoremap(mapScore);
							HashMap<Integer,Participates> p= new LinkedHashMap<>();
							p.put(experts.getParticipantID(),par);
							map.get(experts.getGroupID()).put(experts.getExpertID(),p);
				}
				else{//有getExpertID
					if(map.get(experts.getGroupID()).get(experts.getExpertID()).get(experts.getParticipantID())==null) {//没有ParticipantID
						//getScoremap-null
							HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
							mapScore.put(experts.getScoreItemID(),scoreItemValue);
							par.setScoremap(mapScore);
							map.get(experts.getGroupID()).get(experts.getExpertID()).put(experts.getParticipantID(),par);
					}else {//有ParticipantID
						if(map.get(experts.getGroupID()).get(experts.getExpertID()).get(experts.getParticipantID()).getScoremap()==null) {//getScoremap-null
							HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
							mapScore.put(experts.getScoreItemID(),scoreItemValue);
							map.get(experts.getGroupID()).get(experts.getExpertID()).get(experts.getParticipantID()).setScoremap(mapScore);
						}
						else {
							map.get(experts.getGroupID()).get(experts.getExpertID()).get(experts.getParticipantID()).getScoremap().put(experts.getScoreItemID(),scoreItemValue);
						}
					}
				}
			}

			Gmap.put(experts.getGroupID(),experts.getGroupName());
			Emap.put(experts.getExpertID(),experts.getExpertName());
			Smap.put(experts.getScoreItemID(),experts.getScoreItemName());
		}
		List<PEexport> data_null=null;
		if(defination=="activityID")
			data_null=participatesMapper.getPartByGroupIdForPEexpert_null(activityID);
		if(defination=="groupID")
			data_null=participatesMapper.getPartByGroupIdForPEexpert_nullByGID(activityID);
		HashMap<Integer,HashMap<Integer,Participates>> null_map = new LinkedHashMap<>();//(Gid,PID(PID,P))
		for(PEexport participant:data_null){
			participant.getGroupID();
			participant.getParticipantID();
			participant.getScoreItemID();
			Participates par=new Participates();
			par.setName(participant.getParticipantName());
			par.setCode(participant.getCode());
			par.setDisplaySequence(participant.getDisplaySequence());
			ScoreItemValue scoreItemValue=new ScoreItemValue();
			scoreItemValue.setScore(participant.getScore());
			scoreItemValue.setName(participant.getScoreItemName());
			scoreItemValue.setId(participant.getScoreItemID());
			if(null_map.get(participant.getGroupID())==null)
			{//getGroupID对应value-null//首次插入
				HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
				mapScore.put(participant.getScoreItemID(),scoreItemValue);
				par.setScoremap(mapScore);
				HashMap<Integer,Participates> p= new LinkedHashMap<>();
				p.put(participant.getParticipantID(),par);
				null_map.put(participant.getGroupID(),p);
			}
			else{//有group
				if(null_map.get(participant.getGroupID()).get(participant.getParticipantID())==null) {//没有getPID
					//ParticipantID对应value-null
					//getScoremap-null
					HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
					mapScore.put(participant.getScoreItemID(),scoreItemValue);
					par.setScoremap(mapScore);
					null_map.get(participant.getGroupID()).put(participant.getParticipantID(),par);
				}
				else{//有getPID
					//有ParticipantID
						if(null_map.get(participant.getGroupID()).get(participant.getParticipantID()).getScoremap()==null) {//getScoremap-null
							HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
							mapScore.put(participant.getScoreItemID(),scoreItemValue);
							null_map.get(participant.getGroupID()).get(participant.getParticipantID()).setScoremap(mapScore);
						}
						else {
							null_map.get(participant.getGroupID()).get(participant.getParticipantID()).getScoremap().put(participant.getScoreItemID(),scoreItemValue);
						}
				}
			}
			SNotByEmap.put(participant.getScoreItemID(),participant.getScoreItemName());
		}
		// select s.expertID,p.groupID,g.name as groupName,s.participantID,stu.name as participantName,p.code,p.displaySequence,s.scoreitemID,sc.name as scoreitemName,s.score from rate2.scores s,rate2.student stu,rate2.participants p,rate2.scoreitem sc,rate2.`groups` g where s.expertID is null and stu.ID=p.studentID and p.ID=s.participantID and s.scoreitemID=sc.ID and p.groupID=g.ID and s.activityID='15' and sc.byExpert=false order by g.name,p.displaySequence,s.scoreitemID;
		hashPEexport.setMap(map);
		hashPEexport.setEmap(Emap);
		hashPEexport.setGmap(Gmap);
		hashPEexport.setSmap(Smap);
		hashPEexport.setSNotByEmap(SNotByEmap);
		hashPEexport.setNull_map(null_map);
		return hashPEexport;
	}

	public RespPageBean  getExpertGroupScore(Integer page,Integer size,Integer groupID,Integer activityID) {
		if (page != null && size != null) {
			page = (page - 1) * size;
		}
		HashPEexport hashPEexport=new HashPEexport();
		HashMap<Integer, String> Gmap = new LinkedHashMap<>();
		HashMap<Integer, String> Emap = new LinkedHashMap<>();
		HashMap<Integer, String> Smap = new LinkedHashMap<>();
		HashMap<Integer, String> SNotByEmap = new LinkedHashMap<>();
		List<PEexport> data = participatesMapper.getExpertScore(page,size,groupID,activityID);
		HashMap<Integer,HashMap<Integer,HashMap<Integer,Participates>>> map = new LinkedHashMap<>();//ID,map(ExpertID,map(PID,P))
		for(PEexport experts:data)//获得expert对应的participants和他们的分数
		{//专家给同组的选手打分的选手id//通过pid获得scoreitemMap//Pt.setSMap()
			Participates par=new Participates();
			par.setName(experts.getParticipantName());
			par.setCode(experts.getCode());
			par.setDisplaySequence(experts.getDisplaySequence());
			ScoreItemValue scoreItemValue=new ScoreItemValue();
			scoreItemValue.setScore(experts.getScore());
			scoreItemValue.setName(experts.getScoreItemName());
			scoreItemValue.setId(experts.getScoreItemID());
			scoreItemValue.setExpertID(experts.getExpertID());
			scoreItemValue.setExpertName(experts.getExpertName());
			/*HashMap<Integer, ScoreItemValue> mapScore = new HashMap<>();
			mapScore.put(experts.getScoreItemID(),scoreItemValue);
			par.setScoremap(mapScore);*/
			if(map.get(experts.getGroupID())==null)
			{//getGroupID对应value-null//首次插入
				HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
				mapScore.put(experts.getScoreItemID(),scoreItemValue);
				par.setScoremap(mapScore);
				HashMap<Integer,Participates> p= new LinkedHashMap<>();
				p.put(experts.getParticipantID(),par);
				HashMap<Integer,HashMap<Integer,Participates>> e= new LinkedHashMap<>();
				e.put(experts.getExpertID(),p);
				map.put(experts.getGroupID(),e);
			}
			else{//有group
				if(map.get(experts.getGroupID()).get(experts.getExpertID())==null) {//没有getExpertID
					//ParticipantID对应value-null
					//getScoremap-null
					HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
					mapScore.put(experts.getScoreItemID(),scoreItemValue);
					par.setScoremap(mapScore);
					HashMap<Integer,Participates> p= new LinkedHashMap<>();
					p.put(experts.getParticipantID(),par);
					map.get(experts.getGroupID()).put(experts.getExpertID(),p);
				}
				else{//有getExpertID
					if(map.get(experts.getGroupID()).get(experts.getExpertID()).get(experts.getParticipantID())==null) {//没有ParticipantID
						//getScoremap-null
						HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
						mapScore.put(experts.getScoreItemID(),scoreItemValue);
						par.setScoremap(mapScore);
						map.get(experts.getGroupID()).get(experts.getExpertID()).put(experts.getParticipantID(),par);
					}else {//有ParticipantID
						if(map.get(experts.getGroupID()).get(experts.getExpertID()).get(experts.getParticipantID()).getScoremap()==null) {//getScoremap-null
							HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
							mapScore.put(experts.getScoreItemID(),scoreItemValue);
							map.get(experts.getGroupID()).get(experts.getExpertID()).get(experts.getParticipantID()).setScoremap(mapScore);
						}
						else {
							map.get(experts.getGroupID()).get(experts.getExpertID()).get(experts.getParticipantID()).getScoremap().put(experts.getScoreItemID(),scoreItemValue);
						}
					}
				}
			}

			Gmap.put(experts.getGroupID(),experts.getGroupName());
			Emap.put(experts.getExpertID(),experts.getExpertName());
			Smap.put(experts.getScoreItemID(),experts.getScoreItemName());
		}
		List<PEexport> data_null=null;
		data_null=participatesMapper.getPartByGroupIdForPEexpert_nullByGID(groupID);
		HashMap<Integer,HashMap<Integer,Participates>> null_map = new LinkedHashMap<>();//(Gid,PID(PID,P))
		for(PEexport participant:data_null){
			participant.getGroupID();
			participant.getParticipantID();
			participant.getScoreItemID();
			Participates par=new Participates();
			par.setName(participant.getParticipantName());
			par.setCode(participant.getCode());
			par.setDisplaySequence(participant.getDisplaySequence());
			ScoreItemValue scoreItemValue=new ScoreItemValue();
			scoreItemValue.setScore(participant.getScore());
			scoreItemValue.setName(participant.getScoreItemName());
			scoreItemValue.setId(participant.getScoreItemID());
			for (HashMap<Integer, Participates> object : map.get(participant.getGroupID()).values()) {
				if (object.get(participant.getParticipantID()).getScoremap().get(participant.getParticipantID())==null){
					object.get(participant.getParticipantID()).getScoremap().put(participant.getScoreItemID(), scoreItemValue);
				}
			}
			if(null_map.get(participant.getGroupID())==null)
			{//getGroupID对应value-null//首次插入
				HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
				mapScore.put(participant.getScoreItemID(),scoreItemValue);
				par.setScoremap(mapScore);
				HashMap<Integer,Participates> p= new LinkedHashMap<>();
				p.put(participant.getParticipantID(),par);
				null_map.put(participant.getGroupID(),p);
			}
			else{//有group
				if(null_map.get(participant.getGroupID()).get(participant.getParticipantID())==null) {//没有getPID
					//ParticipantID对应value-null
					//getScoremap-null
					HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
					mapScore.put(participant.getScoreItemID(),scoreItemValue);
					par.setScoremap(mapScore);
					null_map.get(participant.getGroupID()).put(participant.getParticipantID(),par);
				}
				else{//有getPID
					//有ParticipantID
					if(null_map.get(participant.getGroupID()).get(participant.getParticipantID()).getScoremap()==null) {//getScoremap-null
						HashMap<Integer, ScoreItemValue> mapScore = new LinkedHashMap<>();
						mapScore.put(participant.getScoreItemID(),scoreItemValue);
						null_map.get(participant.getGroupID()).get(participant.getParticipantID()).setScoremap(mapScore);
					}
					else {
						null_map.get(participant.getGroupID()).get(participant.getParticipantID()).getScoremap().put(participant.getScoreItemID(),scoreItemValue);
					}
				}
			}
			SNotByEmap.put(participant.getScoreItemID(),participant.getScoreItemName());
		}
		Long total = participatesMapper.getTotalEA(activityID,groupID);

		// select s.expertID,p.groupID,g.name as groupName,s.participantID,stu.name as participantName,p.code,p.displaySequence,s.scoreitemID,sc.name as scoreitemName,s.score from rate2.scores s,rate2.student stu,rate2.participants p,rate2.scoreitem sc,rate2.`groups` g where s.expertID is null and stu.ID=p.studentID and p.ID=s.participantID and s.scoreitemID=sc.ID and p.groupID=g.ID and s.activityID='15' and sc.byExpert=false order by g.name,p.displaySequence,s.scoreitemID;
		hashPEexport.setMap(map);
		hashPEexport.setEmap(Emap);
		hashPEexport.setGmap(Gmap);
		hashPEexport.setSmap(Smap);
		hashPEexport.setSNotByEmap(SNotByEmap);
		hashPEexport.setNull_map(null_map);
		List<HashPEexport> list=new ArrayList<>();
		list.add(hashPEexport);
		RespPageBean bean = new RespPageBean();
		bean.setTotal(total);
		bean.setData(list);
		return bean;
	}

}
