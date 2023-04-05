<template>
  <div>
    <div>
      <el-button type="primary" @click="downloadExcel">下载模版</el-button>
      <el-upload
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-success="onSuccess"
          style="display: inline-flex; margin-left: 8px"
          :action="UploadUrl()"
      >
        <el-button type="primary">上传学生excel</el-button>
      </el-upload>
    </div>
    <div style="margin-top: 10px">
      <span>
        请选择条件进行搜索：
      </span>
      <div class="select_div_input">
        <input
            autocomplete="off"
            style="width:95%;line-height:28px;
                              border:1px solid lightgrey;padding:0 10px 1px 15px;
                              border-radius:4px;color:gray"
            placeholder="请输入老师姓名"
            v-model="selectTeacerName"
            @focus="inputSelectTeacerNameFocus"
            @blur="isSelectShow = isSelectFlag"/>
        <div class="select_div"
             v-show="isSelectShow && selectTeacerName ? true:false"
             :style="'height:${menuHeight}'"
             @mouseover="isSelectFlag = true"
             @mouseleave="isSelectFlag = false"
             >
          <div
              class="select_div_div"
              v-for="val in select_teachers"
              :key="val"
              :value="val"
              @click="filter_teas(val)"
          >
            {{ val }}
          </div>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="graduateStudents">
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="stuNumber" label="学号" align="center"></el-table-column>
        <el-table-column prop="telephone" label="电话" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
        <el-table-column prop="year" label="入学年份" align="center"></el-table-column>
        <el-table-column prop="studentType" label="学生类别" align="center"></el-table-column>
        <el-table-column prop="point" label="积分" align="center"></el-table-column>
        <el-table-column prop="idnumber" label="身份证号" align="center"></el-table-column>
        <el-table-column prop="teachers.name" label="导师姓名" align="center"></el-table-column>
        <el-table-column  label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini"  icon="el-icon-edit" plain @click="editDialogShow(scope.row)" type="primary">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" plain @click="deleteUnder(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="编辑信息" :visible.sync="dialogEdit" center width="500px">
      <template>
        <el-form :model="currentGraduateStudentOfEdit">
          <el-form-item label="导师姓名">
            <el-input style="width: 50%" v-model="currentGraduateStudentOfEdit.teachers.name"></el-input>
          </el-form-item>
          <el-form-item label="导师工号">
            <el-input style="width: 50%" v-model="currentGraduateStudentOfEdit.teachers.jobnumber"></el-input>
          </el-form-item>
          <el-form-item label="学生姓名">
            <el-input style="width: 50%" v-model="currentGraduateStudentOfEdit.name"></el-input>
          </el-form-item>
          <el-form-item label="学生电话">
            <el-input style="width: 50%" v-model="currentGraduateStudentOfEdit.telephone"></el-input>
          </el-form-item>
          <el-form-item label="学生邮箱">
            <el-input style="width: 50%" v-model="currentGraduateStudentOfEdit.email"></el-input>
          </el-form-item>
          <el-form-item label="学生类别">
            <el-input style="width: 50%" v-model="currentGraduateStudentOfEdit.studentType"></el-input>
          </el-form-item>
          <el-form-item label="学生积分">
            <el-input style="width: 50%" v-model="currentGraduateStudentOfEdit.point"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editGraduate" type="primary">确定</el-button>
          <el-button @click="closeDialogEdit">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SalGraduateM",
  data(){
    return{
      isSelectFlag:false,
      isSelectShow:false,//搜索老师名字的搜索框
      timer:null,
      select_teachers:[],
      selectTeacerName:'',
      selectYear:'',
      dialogEdit:false,
      user:{},
      graduateStudents:[],
      currentGraduateStudentOfEdit:{
        ID:null,
        name:'',
        teachers:{
          name:'',
          jobnumber:''
        },
        studentID:null,
        tutorID:null,
        telephone:'',
        email:'',
        studentType:'',
        point:''
      },
    }
  },
  watch:{
    selectTeacerName:{
      handler(val){
        this.delayInputTimer(val)
      }
    }
  },
  computed:{
    menuHeight() {
      return this.selectTeacerName.length * 50 > 150
          ? 150 + 'px'
          : `${this.selectTeacerName.length * 50}px`
    },
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.initGraduateStudents()
  },
  methods:{
    filter_teas(val){//点击某个筛选出来的名字
      this.selectTeacerName = val
      this.isSelectShow=false
      this.isSelectFlag=false
    },
    delayInputTimer(val){
      if(this.timer){
        clearTimeout(this.timer)
      }
      if(!val){
        return
      }
      if(this.selectYear == ''){
        this.selectYear = 0
      }
      let that = this
      this.timer = setTimeout(()=>{
        let url = '/graduatestudentM/basic/getGraduateStudentsBySelect?year=' + parseInt(this.selectYear) + '&teaName=' + this.selectTeacerName
        that.getRequest(url).then((resp)=>{
          that.select_teachers = []
          if(resp){
            if(resp.status == 200){
              for(var i=0;i<resp.obj.length;i++){
                that.select_teachers.push(resp.obj[i])
              }
              that.select_teachers = Array.from(new Set(that.select_teachers));
            }
          }
        })
      },300);
    },
    inputSelectTeacerNameFocus(){//input获取焦点判断是否有下拉框，是否可输入
      this.isSelectShow = true//控制下拉框是否显示
    },
    closeDialogEdit(){//关闭对话框
      this.dialogEdit = false
      this.currentGraduateStudentOfEdit = {}
    },
    editDialogShow(data){
      this.dialogEdit = true
      this.currentGraduateStudentOfEdit = data
    },
    editGraduate(){//点击编辑中的确定按钮
      if(this.currentGraduateStudentOfEdit.teachers.name == '' || this.currentGraduateStudentOfEdit.teachers.jobnumber == '' ||
          this.currentGraduateStudentOfEdit.teachers.name == null || this.currentGraduateStudentOfEdit.teachers.jobnumber == null){
        this.$message.warning('请填写老师姓名和工号！')
        return
      }
      let data = this.currentGraduateStudentOfEdit
      this.postRequest('/graduatestudentM/basic/editGraduateStudent',data).then((resp)=>{
        if(resp){
          if(resp.status == 200){
            this.dialogEdit = false
            this.$message.success(resp.msg)
            this.initGraduateStudents()
          }else {
            this.$message.error(resp)
          }
        }
      })
    },
    deleteUnder(data){//删除研究生
      this.postRequest('/graduatestudentM/basic/deleteGraduateStudent',data).then((resp)=>{
        if(resp.code == 200){
          this.$message.success('删除成功')
          this.initGraduateStudents()
        }
      })
    },
    onSuccess(res){
      if(res.status == 200){
        this.$message.success("导入成功")
        this.initGraduateStudents()
      }else {
        this.$message.error("导入失败")
      }
    },
    beforeUpload() {
      this.$message.success("正在导入")
    },
    UploadUrl(){
      let url = '/graduatestudentM/basic/importGraduate?institutionID=' + this.user.institutionID
      return url;
    },
    downloadExcel(){
      let url = '/graduatestudentM/basic/exportGraduate'
      this.$message.success('正在下载')
      window.open(url,'_parent')
    },
    initGraduateStudents(){
      this.getRequest('/graduatestudentM/basic/getGraduateStudents').then((response)=>{
        if(response.code == 200){
          this.graduateStudents = response.extend.res
        }
      })
    }
  }
}
</script>

<style scoped>
.select_div_input{
  /*margin-left:3px;*/
  width:30%;
  height:32px;
  position:relative;
  display:inline-block
}
.select_div{
  border: .5px solid lightgray;
  border-radius: 3px;
  margin-top: 5px;
  font-size: 14px;
  position: absolute;
  background-color: #fff;
  z-index: 999;
  overflow: hidden;
  width: 90%;
  cursor: pointer;
}
.select_div_div:hover{
  background-color: lightgray;
}
.select_div_div{
  padding-bottom: 2px;
  /*padding-top: 7px;*/
  padding-left: 12px;
  width: 100%;
}
</style>