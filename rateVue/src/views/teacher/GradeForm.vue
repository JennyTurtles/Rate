<template>
<div class="el-main my-page">
 <div >请选择需要导出成绩评定表的活动：
  <el-select style="width: 400px" @change="allowConfirm=false" v-model="selectedActivityID">
   <el-option
       v-for="item in options"
       :key="item.id"
       :label="item.name"
       :value="item.id">
   </el-option>
  </el-select>
  <el-button style="margin-left: 20px" :disabled="allowConfirm" type="primary" @click="exportGradeForm(selectedActivityID)">确定</el-button>
  <el-button style="margin-left: 20px" :disabled="allowExport" type="success" @click="getAllStudents">导出</el-button>
 </div>
 <div v-show="showGradeFormSetting" style="color: #FFFFFF">
  <el-form
      label-position="left"
      label-width="120px"
      :model="gradeForm"
      element-loading-body="element-loading-body"
      v-loading="exportGradeFormLoading" element-loading-text="导出中..."
  >
   <el-divider>评语设置
    <el-button type="danger"
               @click="gradeForm.instructorCommentActID='';gradeForm.reviewCommentActID='';gradeForm.defenseCommentActID='';disableComment()"
               size="mini" style="margin: 5px;padding: 5px" v-show="mode!=='secretary'">清空
    </el-button>
   </el-divider>
   <el-form-item label="指导教师评语:">
    <el-select
        style="width: 100%"
        v-model="gradeForm.instructorCommentActID"
        placeholder="请选择对应的子活动"
        @change="disableComment()"
        :disabled="mode==='secretary'"
    >
     <el-option
         v-for="item in subActsWithComment"
         :key="item.id"
         :label="item.name"
         :value="item.id"
         :disabled="item.disabled">
     </el-option>
    </el-select>
   </el-form-item>
   <el-form-item label="评阅教师评语:" prop="startDate">
    <el-select
        style="width: 100%"
        v-model="gradeForm.reviewCommentActID"
        placeholder="请选择对应的子活动"
        @change="disableComment()"
        :disabled="mode==='secretary'"
    >
     <el-option
         v-for="item in subActsWithComment"
         :key="item.id"
         :label="item.name"
         :value="item.id"
         :disabled="item.disabled">
     </el-option>
    </el-select>
   </el-form-item>
   <el-form-item label="答辩教师评语:" prop="startDate">
    <el-select
        style="width: 100%"
        v-model="gradeForm.defenseCommentActID"
        placeholder="请选择对应的子活动"
        @change="disableComment()"
        :disabled="mode==='secretary'"
    >
     <el-option
         v-for="item in subActsWithComment"
         :key="item.id"
         :label="item.name"
         :value="item.id"
         :disabled="item.disabled">
     </el-option>
    </el-select>
   </el-form-item>
   <el-divider>指导教师评分项设置
    <el-button type="danger"
               @click="emptyScoreItem('指导教师评语');disableScoreItem()"
               size="mini" style="margin: 5px;padding: 5px" v-show="mode!=='secretary'">清空
    </el-button>
   </el-divider>
   <el-form
       label-position="left"
       :model="gradeForm"
       label-width="800px"
       :inline="true"
   >
    <div v-for="(value, key) in gradeForm.instructorScoreItemsMap" :key="key">
     <el-form-item  :label="value[0]">
      <el-select
          style="width: 80%"
          v-model="gradeForm.instructorScoreItemsMap.get(value[0]).id"
          placeholder="请选择对应的评分项"
          @change="disableScoreItem()"
          :disabled="mode==='secretary'"
      >
       <el-option
           v-for="item in scoreItemsAll"
           :key="item.id"
           :label="item.name"
           :value="item.id"
           :disabled="item.disabled">
       </el-option>
      </el-select>
     </el-form-item>
     <el-form-item label="系数" label-width="40px">
      <el-input @input="$forceUpdate()" style="width: 60px"
                v-model="gradeForm.instructorScoreItemsMap.get(value[0]).coef"
                :disabled="mode==='secretary'"></el-input>
     </el-form-item>
    </div>
   </el-form>
   <el-divider>评阅教师评分项设置
    <el-button type="danger"
               @click="emptyScoreItem('评阅教师评语');;disableScoreItem()"
               size="mini" style="margin: 5px;padding: 5px" v-show="mode!=='secretary'">清空
    </el-button>
   </el-divider>
   <el-form
       label-position="left"
       :model="gradeForm"
       label-width="800px"
       :inline="true"
   >
    <div v-for="(value, key) in gradeForm.reviewScoreItemsMap" :key="key">
     <el-form-item :label="value[0]">
      <el-select
          style="width: 80%"
          v-model="gradeForm.reviewScoreItemsMap.get(value[0]).id"
          placeholder="请选择对应的评分项"
          @change="disableScoreItem"
          :disabled="mode==='secretary'"
      >
       <el-option
           v-for="item in scoreItemsAll"
           :key="item.id"
           :label="item.name"
           :value="item.id"
           :disabled="item.disabled">
       </el-option>
      </el-select>
     </el-form-item>
     <el-form-item label="系数" label-width="40px">
      <el-input @input="$forceUpdate()" style="width: 60px" v-model="gradeForm.reviewScoreItemsMap.get(value[0]).coef"
                :disabled="mode==='secretary'"></el-input>
     </el-form-item>
    </div>
   </el-form>
   <el-divider>答辩评分项设置
    <el-button type="danger"
               @click="emptyScoreItem('答辩教师评语');;disableScoreItem()"
               size="mini" style="margin: 5px;padding: 5px" v-show="mode!=='secretary'">清空
    </el-button>
   </el-divider>
   <el-form
       label-position="left"
       :model="gradeForm"
       label-width="800px"
       :inline="true"
   >
    <div v-for="(value, key) in gradeForm.defenseScoreItemsMap" :key="key">
     <el-form-item :label="value[0]">
      <el-select
          style="width: 80%"
          v-model="gradeForm.defenseScoreItemsMap.get(value[0]).id"
          placeholder="请选择对应的评分项"
          @change="disableScoreItem"
          :disabled="mode==='secretary'"
      >
       <el-option
           v-for="item in scoreItemsAll"
           :key="item.id"
           :label="item.name"
           :value="item.id"
           :disabled="item.disabled">
       </el-option>
      </el-select>
     </el-form-item>
     <el-form-item label="系数" label-width="40px">
      <el-input @input="$forceUpdate()" style="width: 60px" v-model="gradeForm.defenseScoreItemsMap.get(value[0]).coef"
                :disabled="mode==='secretary'"></el-input>
     </el-form-item>
    </div>
   </el-form>
  </el-form>
 </div>
 <el-dialog title="请选择您要导出的学生" v-loading="exportGradeFormLoading" :visible.sync="showStudents">
  <el-checkbox-group v-model="checkedStudents" @change="">
   <el-checkbox v-for="student in students" :label="student.id" :key="student.id">{{student.name}}</el-checkbox>
  </el-checkbox-group>
   <span slot="footer" class="dialog-footer">
    <el-button @click="showStudents = false;checkedStudents=[]">取 消</el-button>
    <el-button type="primary" @click="showStudents = false;goExportGradeForm()">确 定</el-button>
  </span>
 </el-dialog>
<!-- <el-dialog title="成绩评定表导出设置" :visible.sync="exportGradeFormVisible" width="70%">-->
<!--  <el-form-->
<!--      label-position="left"-->
<!--      label-width="120px"-->
<!--      :model="gradeForm"-->
<!--      element-loading-body="element-loading-body"-->
<!--      v-loading="exportGradeFormLoading" element-loading-text="导出中..."-->
<!--  >-->
<!--   <el-divider>评语设置-->
<!--    <el-button type="danger"-->
<!--               @click="gradeForm.instructorCommentActID='';gradeForm.reviewCommentActID='';gradeForm.defenseCommentActID='';disableComment()"-->
<!--               size="mini" style="margin: 5px;padding: 5px" v-show="mode!=='secretary'">清空-->
<!--    </el-button>-->
<!--   </el-divider>-->
<!--   <el-form-item label="指导教师评语:">-->
<!--    <el-select-->
<!--        style="width: 100%"-->
<!--        v-model="gradeForm.instructorCommentActID"-->
<!--        placeholder="请选择对应的子活动"-->
<!--        @change="disableComment()"-->
<!--        :disabled="mode==='secretary'"-->
<!--    >-->
<!--     <el-option-->
<!--         v-for="item in subActsWithComment"-->
<!--         :key="item.id"-->
<!--         :label="item.name"-->
<!--         :value="item.id"-->
<!--         :disabled="item.disabled">-->
<!--     </el-option>-->
<!--    </el-select>-->
<!--   </el-form-item>-->
<!--   <el-form-item label="评阅教师评语:" prop="startDate">-->
<!--    <el-select-->
<!--        style="width: 100%"-->
<!--        v-model="gradeForm.reviewCommentActID"-->
<!--        placeholder="请选择对应的子活动"-->
<!--        @change="disableComment()"-->
<!--        :disabled="mode==='secretary'"-->
<!--    >-->
<!--     <el-option-->
<!--         v-for="item in subActsWithComment"-->
<!--         :key="item.id"-->
<!--         :label="item.name"-->
<!--         :value="item.id"-->
<!--         :disabled="item.disabled">-->
<!--     </el-option>-->
<!--    </el-select>-->
<!--   </el-form-item>-->
<!--   <el-form-item label="答辩教师评语:" prop="startDate">-->
<!--    <el-select-->
<!--        style="width: 100%"-->
<!--        v-model="gradeForm.defenseCommentActID"-->
<!--        placeholder="请选择对应的子活动"-->
<!--        @change="disableComment()"-->
<!--        :disabled="mode==='secretary'"-->
<!--    >-->
<!--     <el-option-->
<!--         v-for="item in subActsWithComment"-->
<!--         :key="item.id"-->
<!--         :label="item.name"-->
<!--         :value="item.id"-->
<!--         :disabled="item.disabled">-->
<!--     </el-option>-->
<!--    </el-select>-->
<!--   </el-form-item>-->
<!--   <el-divider>指导教师评分项设置-->
<!--    <el-button type="danger"-->
<!--               @click="emptyScoreItem('指导教师评语');disableScoreItem()"-->
<!--               size="mini" style="margin: 5px;padding: 5px" v-show="mode!=='secretary'">清空-->
<!--    </el-button>-->
<!--   </el-divider>-->
<!--   <el-form-->
<!--       label-position="left"-->
<!--       :model="gradeForm"-->
<!--       label-width="600px"-->
<!--       :inline="true"-->
<!--   >-->
<!--    <div v-for="(value, key) in gradeForm.instructorScoreItemsMap" :key="key">-->
<!--     <el-form-item :label="value[0]">-->
<!--      <el-select-->
<!--          style="width: 80%"-->
<!--          v-model="gradeForm.instructorScoreItemsMap.get(value[0]).id"-->
<!--          placeholder="请选择对应的评分项"-->
<!--          @change="disableScoreItem()"-->
<!--          :disabled="mode==='secretary'"-->
<!--      >-->
<!--       <el-option-->
<!--           v-for="item in scoreItemsAll"-->
<!--           :key="item.id"-->
<!--           :label="item.name"-->
<!--           :value="item.id"-->
<!--           :disabled="item.disabled">-->
<!--       </el-option>-->
<!--      </el-select>-->
<!--     </el-form-item>-->
<!--     <el-form-item label="系数" label-width="40px">-->
<!--      <el-input @input="$forceUpdate()" style="width: 60px"-->
<!--                v-model="gradeForm.instructorScoreItemsMap.get(value[0]).coef"-->
<!--                :disabled="mode==='secretary'"></el-input>-->
<!--     </el-form-item>-->
<!--    </div>-->
<!--   </el-form>-->
<!--   <el-divider>评阅教师评分项设置-->
<!--    <el-button type="danger"-->
<!--               @click="emptyScoreItem('评阅教师评语');;disableScoreItem()"-->
<!--               size="mini" style="margin: 5px;padding: 5px" v-show="mode!=='secretary'">清空-->
<!--    </el-button>-->
<!--   </el-divider>-->
<!--   <el-form-->
<!--       label-position="left"-->
<!--       :model="gradeForm"-->
<!--       label-width="600px"-->
<!--       :inline="true"-->
<!--   >-->
<!--    <div v-for="(value, key) in gradeForm.reviewScoreItemsMap" :key="key">-->
<!--     <el-form-item :label="value[0]">-->
<!--      <el-select-->
<!--          style="width: 80%"-->
<!--          v-model="gradeForm.reviewScoreItemsMap.get(value[0]).id"-->
<!--          placeholder="请选择对应的评分项"-->
<!--          @change="disableScoreItem"-->
<!--          :disabled="mode==='secretary'"-->
<!--      >-->
<!--       <el-option-->
<!--           v-for="item in scoreItemsAll"-->
<!--           :key="item.id"-->
<!--           :label="item.name"-->
<!--           :value="item.id"-->
<!--           :disabled="item.disabled">-->
<!--       </el-option>-->
<!--      </el-select>-->
<!--     </el-form-item>-->
<!--     <el-form-item label="系数" label-width="40px">-->
<!--      <el-input @input="$forceUpdate()" style="width: 60px" v-model="gradeForm.reviewScoreItemsMap.get(value[0]).coef"-->
<!--                :disabled="mode==='secretary'"></el-input>-->
<!--     </el-form-item>-->
<!--    </div>-->
<!--   </el-form>-->
<!--   <el-divider>答辩评分项设置-->
<!--    <el-button type="danger"-->
<!--               @click="emptyScoreItem('答辩教师评语');;disableScoreItem()"-->
<!--               size="mini" style="margin: 5px;padding: 5px" v-show="mode!=='secretary'">清空-->
<!--    </el-button>-->
<!--   </el-divider>-->
<!--   <el-form-->
<!--       label-position="left"-->
<!--       :model="gradeForm"-->
<!--       label-width="600px"-->
<!--       :inline="true"-->
<!--   >-->
<!--    <div v-for="(value, key) in gradeForm.defenseScoreItemsMap" :key="key">-->
<!--     <el-form-item :label="value[0]">-->
<!--      <el-select-->
<!--          style="width: 80%"-->
<!--          v-model="gradeForm.defenseScoreItemsMap.get(value[0]).id"-->
<!--          placeholder="请选择对应的评分项"-->
<!--          @change="disableScoreItem"-->
<!--          :disabled="mode==='secretary'"-->
<!--      >-->
<!--       <el-option-->
<!--           v-for="item in scoreItemsAll"-->
<!--           :key="item.id"-->
<!--           :label="item.name"-->
<!--           :value="item.id"-->
<!--           :disabled="item.disabled">-->
<!--       </el-option>-->
<!--      </el-select>-->
<!--     </el-form-item>-->
<!--     <el-form-item label="系数" label-width="40px">-->
<!--      <el-input @input="$forceUpdate()" style="width: 60px" v-model="gradeForm.defenseScoreItemsMap.get(value[0]).coef"-->
<!--                :disabled="mode==='secretary'"></el-input>-->
<!--     </el-form-item>-->
<!--    </div>-->
<!--   </el-form>-->
<!--  </el-form>-->

<!--  <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="exportGradeFormVisible = false">取 消</el-button>-->
<!--        <el-button type="success" @click="goExportGradeForm()">下 载</el-button>-->
<!--      </span>-->
<!-- </el-dialog>-->
</div>
</template>

<script>

import axios from "axios";

export default {
 name: "gradeForm",
 data() {
  return {
   checkedStudents: [],
   students:[],
   showStudents:false,
   allowExport:true,
   showGradeFormSetting:false,
   scoreItemsAll: [],
   options:[],
   selectedActivityID:'',
   allowConfirm:true,
   exportGradeFormLoading: false,
   gradeFormOrderList: [
    '指导教师评语',
    '评阅教师评语',
    '答辩教师评语',
    '能开发或选用满足特定需求的现代工具，对计算机专业工程问题进行模拟和预测，并分析其局限性。',
    '能在计算机专业项目设计开发过程中，运用计算机专业工程管理和经济决策方法。',
    '具备自主学习能力，包括对计算机专业技术问题的理解能力、归纳总结能力和提出及回答问题的能力等，实现学习目标。',
    '能清晰认识现阶段社会经济环境发展的基本需求和动态，发现影响社会可持续发展的关键问题，选择适应时代发展潮流的毕业设计题目和研究课题。',
    '能主动提出新的理念、采用新的方法和技术路线，开展实际计算机专业应用系统的设计。',
    '能从环保角度思考计算机专业工程实践的可持续性、以及可能造成的环境危害。',
    '能撰写毕业论文，以文档、图表等方式准确表达自己的观点。',
    '能对所设计和开发的计算机系统对社会和经济发展意义进行说明、分析和评价。',
    '具备分析和解释实验结果，并通过信息综合得到有效结论的能力。',
    '能通过陈述发言、指令回复等形式就计算机专业问题与同行，及不同文化背景人士进行交流。',
   ],
   subActsWithComment: [],
   gradeForm: {
    instructorCommentActID: '',
    reviewCommentActID: '',
    defenseCommentActID: '',
    instructorScoreItemsMap: new Map(),
    reviewScoreItemsMap: new Map(),
    defenseScoreItemsMap: new Map(),
    groupID: -1,
    exportForTutor: true,
   },
   exportGradeFormVisible: false,
   mode:'secretary'
  };
 },
 mounted() {
  this.init()
 },
 computed: {
  user() {
   return JSON.parse(localStorage.getItem("user"));
  },
 },
 methods:{
  init(){
   this.getRequest("/activities/basic/getWithGradeForm?teacherID="+this.user.id).then(res => {
    this.options = res.obj;
   })
  },
  disableScoreItemInit(index, gradeFormList) {
   if (gradeFormList[index].targetID !== null) {
    for (let j = 0; j < this.scoreItemsAll.length; j++) {
     if (this.scoreItemsAll[j].id === gradeFormList[index].targetID) {
      this.scoreItemsAll[j].disabled = true;
      break
     }
    }
   }
  },
  getAllStudents(){
   let tutorID = this.user.id
   let activityID = this.selectedActivityID
   this.getRequest("/system/Experts/studentsForTutor?tutorID="+tutorID+"&activityID="+activityID).then(res => {
    this.students = res.obj;
    for (let i = 0; i < this.students.length; i++) {
     this.checkedStudents.push(this.students[i].id)
    }
    this.showStudents = true
   })
  },
  goExportGradeForm() {
   this.exportGradeFormLoading = true
   // // 下载时不会保存
   var gradeFormConverted = this.saveGradeForm(false)
   if (gradeFormConverted === null)
    return
   gradeFormConverted.teacherID = this.user.id
   gradeFormConverted.studentIDs = this.checkedStudents
   this.postRequest("/system/Experts/checkLeader", gradeFormConverted).then((res) => { // 接收文件的时候无法同时接收信息，因此单独请求一次后端
    if (res.msg == 0) {
     this.$message({type: 'warning', message: '未获取到任何有效数据！'});
    } else {
     axios({
      url: "/system/Experts/exportGradeForm", method: 'post', data: gradeFormConverted,
      headers: {'Content-Type': 'application/json'},
      responseType: 'blob'
     }).then(res => {
      console.log(res)
      if (new Blob([res]) !== null)
       this.$message({type: 'success', message: '导出成绩评定表成功!'});
      else
       this.$message({type: 'error', message: '导出成绩评定表失败!'});
      const url = window.URL.createObjectURL(new Blob([res]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', (this.user.name) + '学生的成绩评定表.zip');
      document.body.appendChild(link);
      link.click();
     })
    }
    this.exportGradeFormLoading = false
    this.exportGradeFormVisible = false
    this.checkedStudents = []
   })
  },
  disableScoreItem() {
   this.$forceUpdate()
   for (let i = 0; i < this.scoreItemsAll.length; i++) {
    this.scoreItemsAll[i].disabled = false
    this.gradeForm.instructorScoreItemsMap.forEach((value, key, map) => {
     if (this.scoreItemsAll[i].id === value.id) {
      this.scoreItemsAll[i].disabled = true
     }
    })
    this.gradeForm.reviewScoreItemsMap.forEach((value, key, map) => {
     if (this.scoreItemsAll[i].id === value.id) {
      this.scoreItemsAll[i].disabled = true
     }
    })
    this.gradeForm.defenseScoreItemsMap.forEach((value, key, map) => {
     if (this.scoreItemsAll[i].id === value.id) {
      this.scoreItemsAll[i].disabled = true
     }
    })
   }
  },
  emptyScoreItem(mode) {
   switch (mode) {
    case '指导教师评语': {
     this.gradeForm.instructorScoreItemsMap.forEach((value, key, map) => {
      this.gradeForm.instructorScoreItemsMap.set(key, {id: null, coef: null})
     })
     this.$forceUpdate()
     break
    }
    case '评阅教师评语': {
     this.gradeForm.reviewScoreItemsMap.forEach((value, key, map) => {
      this.gradeForm.reviewScoreItemsMap.set(key, {id: null, coef: null})
     })
     this.$forceUpdate()
     break
    }
    case '答辩教师评语': {
     this.gradeForm.defenseScoreItemsMap.forEach((value, key, map) => {
      this.gradeForm.defenseScoreItemsMap.set(key, {id: null, coef: null})
     })
     this.$forceUpdate()
     break
    }
   }
  },
  disableComment() {
   this.$forceUpdate()
   for (let i = 0; i < this.subActsWithComment.length; i++) {
    this.subActsWithComment[i].disabled = false
    if (this.subActsWithComment[i].id === this.gradeForm.instructorCommentActID ||
        this.subActsWithComment[i].id === this.gradeForm.reviewCommentActID ||
        this.subActsWithComment[i].id === this.gradeForm.defenseCommentActID) {
     this.subActsWithComment[i].disabled = true
    }

   }
  },
  disableCommentInit(gradeFormList) {
   for (var i = 0; i < 3; i++) {
    if (gradeFormList[i].targetID !== null) {
     for (let j = 0; j < this.subActsWithComment.length; j++) {
      if (this.subActsWithComment[j].id === gradeFormList[i].targetID) {
       this.subActsWithComment[j].disabled = true;
       break
      }
     }
    }
   }
  },
  saveGradeForm(display) {
   var gradeFormConverted = this.convertGradeForm(this.gradeForm)
   if (gradeFormConverted === null)
    return null
   if (this.mode === 'admin') {
    this.postRequest("/system/Experts/saveGradeForm", gradeFormConverted).then((resp) => {
     if (resp.obj) {
      if (display)
       this.$message({type: 'success', message: '保存成绩评定表成功!'});
     } else
      this.$message({type: 'error', message: '保存成绩评定表失败!'});
    })
   }
   if (display)
    this.exportGradeFormVisible = false;
   return gradeFormConverted
  },
  convertGradeForm(gradeForm) { // 将评分表转换为后端可以接收的形式(保存到数据库)
   var form = {
    instructorCommentActID: this.gradeForm.instructorCommentActID,
    reviewCommentActID: this.gradeForm.reviewCommentActID,
    defenseCommentActID: this.gradeForm.defenseCommentActID,
    instructorScoreItems: {},
    reviewScoreItems: {},
    defenseScoreItems: {},
    instructorScoreName2ID: {},
    reviewScoreName2ID: {},
    defenseScoreName2ID: {},
    activityID: gradeForm.activityID,
    orderList: this.gradeFormOrderList,
    groupID: this.gradeForm.groupID
   }
   var regNumber = /^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$/
   var flag = 0
   gradeForm.instructorScoreItemsMap.forEach((value, key, map) => {
    if (flag)
     return
    if (value.id === null && value.coef !== null && value.coef !== '') {
     flag = 1
     this.$message({type: 'warning', message: '评分项不能为空'});
     this.exportGradeFormLoading = false;
     return
    }
    if (value.id !== null) {
     if (regNumber.test(value.coef) === false) {
      flag = 1
      this.$message({type: 'warning', message: '系数不合法'});
      this.exportGradeFormLoading = false;
     }
     form.instructorScoreItems[value.id] = value.coef
     form.instructorScoreName2ID[key] = value.id
    }
   })
   gradeForm.reviewScoreItemsMap.forEach((value, key, map) => {
    if (flag)
     return
    if (value.id === null && value.coef !== null && value.coef !== '') {
     flag = 1
     this.$message({type: 'warning', message: '评分项不能为空'});
     this.exportGradeFormLoading = false;
     return
    }
    if (value.id !== null) {
     if (regNumber.test(value.coef) === false) {
      this.$message({type: 'warning', message: '系数不合法'});
      this.exportGradeFormLoading = false;
      flag = 1
     }
     form.reviewScoreItems[value.id] = value.coef
     form.reviewScoreName2ID[key] = value.id
    }
   })
   gradeForm.defenseScoreItemsMap.forEach((value, key, map) => {
    if (flag)
     return
    if (value.id === null && value.coef !== null && value.coef !== '') {
     flag = 1
     this.$message({type: 'warning', message: '评分项不能为空'});
     this.exportGradeFormLoading = false;
     return
    }
    if (value.id !== null) {
     if (regNumber.test(value.coef) === false) {
      this.$message({type: 'warning', message: '系数不合法'});
      this.exportGradeFormLoading = false;
      flag = 1
     }
     form.defenseScoreItems[value.id] = value.coef
     form.defenseScoreName2ID[key] = value.id
    }
   })
   return flag === 0 ? form : null
  },
  exportGradeForm(actID) {
   this.showGradeFormSetting = true
   this.allowExport = false
   // 这里按照老师的要求，直接把名字写死
   const gradeFormScoreItemNames = this.gradeFormOrderList
   this.getRequest("/scoreItem/basic/SubScoreItem?activityID=" + actID).then((resp) => {
    this.scoreItemsAll = resp.obj
    for (var i = 0; i < this.scoreItemsAll.length; i++) {
     this.scoreItemsAll[i].disabled = false
     if (this.scoreItemsAll[i].name === "活动总评分") {
      this.scoreItemsAll.splice(i, 1)
      i--;
     }
    }
    this.getRequest("/activities/basic/subWithComment?activityID=" + actID).then((resp) => {
     this.subActsWithComment = resp.obj
     this.getRequest("/system/Experts/getGradeForm?activityID=" + actID).then((resp) => {
      const gradeFormList = resp.obj
      this.gradeForm.instructorCommentActID = gradeFormList[0].targetID
      this.gradeForm.reviewCommentActID = gradeFormList[1].targetID
      this.gradeForm.defenseCommentActID = gradeFormList[2].targetID
      this.disableCommentInit(gradeFormList)
      for (let i = 3; i < 6; i++) {
       this.disableScoreItemInit(i, gradeFormList)
       this.gradeForm.instructorScoreItemsMap.set(gradeFormScoreItemNames[i], {
        'id': gradeFormList[i].targetID,
        'coef': gradeFormList[i].coef
       })
      }
      for (let i = 6; i < 10; i++) {
       this.disableScoreItemInit(i, gradeFormList)
       this.gradeForm.reviewScoreItemsMap.set(gradeFormScoreItemNames[i], {
        'id': gradeFormList[i].targetID,
        'coef': gradeFormList[i].coef
       })
      }
      for (let i = 10; i < 13; i++) {
       this.disableScoreItemInit(i, gradeFormList)
       this.gradeForm.defenseScoreItemsMap.set(gradeFormScoreItemNames[i], {
        'id': gradeFormList[i].targetID,
        'coef': gradeFormList[i].coef
       })
      }
      // this.gradeForm.actName = data.name
      this.gradeForm.activityID = actID
      this.exportGradeFormVisible = true;
     });
    })
   })
  },
 }
};
</script>

<style scoped>
.my-page {
 background-color:#FFFFFF;
}
</style>
