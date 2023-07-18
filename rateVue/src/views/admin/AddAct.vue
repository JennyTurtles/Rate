<template>
 <div>
 <AddActStep :active="0" :actID="$route.query.keywords"></AddActStep>
  <el-form
      label-position='left'
      label-width="120px"
      :model="emp_edit"
      :rules="rules"
      ref="empForm"
  >
   <el-form-item label="活动名称:" prop="name">
    <el-input
        size="mini"
        style="width: 200px"
        prefix-icon="el-icon-edit"
        v-model="emp_edit.name"
        placeholder="请输入活动名称"
    ></el-input>
   </el-form-item>
   <el-form-item label="专家可见时间:" prop="visibleDate">
    <div class="block">
     <div>
      <el-checkbox v-model="visibleDateSelected">不限</el-checkbox>
     </div>
     <div>
      <el-date-picker
          :disabled="visibleDateSelected"
          v-model="emp_edit.visibleDate"
          type="datetime"
          style="margin-left: 8px;"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择日期和时间">
      </el-date-picker>
      <span class="tip-title">专家在活动列表中可以看到该活动的时间</span>
     </div>
    </div>
   </el-form-item>
   <el-form-item label="专家可进入时间:" prop="enterDate">
    <div class="block">
     <div>
      <el-checkbox v-model="enterDateSelected">不限</el-checkbox>
     </div>
     <div>
      <el-date-picker
          :disabled="enterDateSelected"
          v-model="emp_edit.enterDate"
          type="datetime"
          style="margin-left: 8px;"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择日期和时间">
      </el-date-picker>
      <span class="tip-title">专家可进入到该活动中的时间</span>
     </div>
    </div>
   </el-form-item>
   <el-form-item label="开始时间:" prop="startDate">
    <div class="block">
     <el-date-picker
         v-model="emp_edit.startDate"
         type="datetime"
         value-format="yyyy-MM-dd HH:mm:ss"
         placeholder="选择日期和时间">
     </el-date-picker>
    </div>
   </el-form-item>

   <el-form-item label="备 注: " prop="comment">
    <el-input
        type="textarea"
        :rows="2"
        v-model="emp_edit.comment"
        placeholder="活动备注example：关于xxx的活动。备注信息将显示在专家评分表的活动标题下方。"
    >
    </el-input>
   </el-form-item>
   <el-form-item  label="包含子活动: " v-show="mode === 'admin'">
    <el-checkbox v-model="emp_edit.haveSub"></el-checkbox>
   </el-form-item>
   <el-form-item label="是否写评语: ">
    <el-checkbox v-model="emp_edit.haveComment"></el-checkbox>
    <span class="tip-title" style="margin-left: 10px">专家在评分时是否需要写评语</span>
   </el-form-item>
   <el-form-item label="成绩评定表类型: ">
    <el-select
        style="width: 100%"
        v-model="emp_edit.gradeFormType"
        placeholder="请选择成绩评定表类型xxx"
    >
     <el-option
         v-for="item in gradeFormTypes"
         :key="item.id"
         :label="item.label"
         :value="item.id"
         :disabled="item.disabled">
     </el-option>
    </el-select>
   </el-form-item>
  </el-form>

 </div>
</template>

<script>
import AddActStep from "@/components/AddActStep.vue";

export default {
  name: "AddAct",
  components:{
  'AddActStep':AddActStep,
 },
  data() {
   return {
    emp: {
     id: null,
     institutionID: null,
     name: null,
     startDate: "",
     enterDate: "",
     visibleDate: "",
     scoreItemCount: "0",
     score: "100",
     groupCount: "0",
     expertCount: "0",
     participantCount: "0",
     comment: "javaboy",
     parentId: null,
     requireGroup: true,
     isShowPermissionBtn:false
    },
    gradeFormTypes:[{label:'无',id:0},{label:'计算机学院',id:1}],
    gradeFormType:0,
    haveComment:false,
    haveSub:false,
    mode:'',
    visibleDateSelected:true,
    enterDateSelected:true,
    emp_edit:{},
    rules: {
     name: [{required: true, message: "请输入活动名", trigger: "blur"}],
     startDate: [
      {required: true, message: "请输入活动时间", trigger: "blur"},
     ],
     scoreItemCount: [
      {
       required: true,
       type: "number",
       message: "请输入正确数据",
       trigger: "blur",
       transform: (value) => Number(value),
      },
     ],
     comment: [{required: true, message: "请输入备注", trigger: "blur"}],
    },
   }
  },
 mounted() {
  this.mode = this.$route.query.mode
  this.init()
 },
 computed: {
  user() {
   return JSON.parse(localStorage.getItem('user'));
  },
 },
 methods:{
  init(){
   if (typeof this.$route.query.keywords !== 'undefined'){ // 此时是从前面返回的
    this.getRequest("/activities/basic/one?activityID="+this.$route.query.keywords).then(res=> {
     this.emp_edit = res.obj
     this.emp_edit.haveSub = res.obj.haveSub === 1
     this.emp_edit.haveComment = res.obj.haveComment === 1
    })
   }
  },
  doAddEmp() {
   this.emp = this.emp_edit
   // if(this.mode === 'adminSub')
   //  this.emp.parentID = this.activityID;
   if(this.visibleDateSelected) {//不限
    this.emp.visibleDate = null
   }else {//即使用户选择了不限，datapicker也可以进行删除数据，判断是否为空 不为空修改时间格式
    if(this.emp.visibleDate !== '' && this.emp.visibleDate != null){
     if(this.emp.enterDate !== '' && this.emp.enterDate != null){
      if(this.emp.visibleDate > this.emp.enterDate){
       this.$message.warning('可见时间应不大于进入时间!')
       return
      }
     }
     if(this.emp.visibleDate > this.emp.startDate){
      this.$message.warning('可见时间应不大于开始时间!')
      return
     }
    }
    this.emp.visibleDate = this.dateFormatFunc(this.emp.visibleDate)
   }
   if(this.enterDateSelected) {
    this.emp.enterDate = null
   }else {
    if(this.emp.enterDate !== '' && this.emp.enterDate != null){
     if(this.emp.enterDate > this.emp.startDate){
      this.$message.warning('进入时间应不大于开始时间!')
      return
     }
     this.emp.enterDate = this.dateFormatFunc(this.emp.enterDate)
    }
   }
   this.emp.haveSub = this.emp.haveSub ? 1 : 0
   this.emp.haveComment = this.emp.haveComment ? 1 : 0
   this.$set(this.emp,"adminID",this.user.id)
   //添加活动 能看见的小于能进入的小于开始时间
   return new Promise((resolve, reject) => {
   this.$refs["empForm"].validate((valid) => {
    if (valid) {
     this.emp.institutionID = this.user.institutionID;
     this.$set(this.emp,"adminID",this.user.id)
     this.emp.startDate = this.dateFormatFunc(this.emp.startDate)
     const _this = this;
      _this.postRequest("/activities/basic/insert", _this.emp).then(
          (resp) => {
           if (resp) {
            this.$message({
             type: 'success',
             message: '添加成功!'
            });
           }
           resolve(resp);
          },
      );
    }
   });
   })
  },
  add(){
    return this.doAddEmp()
  },
  save(id){
   this.emp = this.emp_edit
   this.emp.institutionID = this.user.institutionID;
   this.emp.id = id
   this.emp.requireGroup = null
   this.emp.haveSub = this.emp.haveSub ? 1 : 0
   this.emp.haveComment = this.emp.haveComment ? 1 : 0
   const _this = this;
   this.postRequest("/activities/basic/update", _this.emp).then(
       (resp) => {
        if (resp) {
         this.$message({
          type: 'success',
          message: '修改成功!'
         });
        }
       }
   );
  }
  }
};
</script>

<style>
.userinfo-container div {
 font-size: 12px;
 color: #409eff;
}

</style>