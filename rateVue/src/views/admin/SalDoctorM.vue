<template>
  <div>
    <div>
      导入学生第一步：<el-button icon="el-icon-upload" type="primary" @click="downloadExcel">下载模版</el-button>
      第二步：<el-upload
        :show-file-list="false"
        :headers="{
        'token': user.token
      }"
        :before-upload="beforeUpload"
        :on-success="onSuccess"
        style="display: inline-flex; margin-left: 8px"
        :action="UploadUrl()"
    >
      <el-button icon="el-icon-plus" type="success">导入学生</el-button>
    </el-upload>
    </div>
    <div style="margin-top: 10px">
      <span>
        请选择条件进行搜索：
      </span>
      <div class="select_div_input">
        <el-select
            v-model="selectTeacerName"
            filterable
            remote
            clearable
            reserve-keyword
            :remote-method="searchTeaNameMethod"
            placeholder="请输入老师姓名">
          <el-option
              v-for="item in select_teachers"
              :key="item"
              :label="item"
              :value="item">
          </el-option>
        </el-select>
      </div>
      <div class="select_div_input" style="margin-left: 30px">
        <el-select
            v-model="selectYear"
            clearable
            filterable
            placeholder="请输入入学年份"
        >
          <el-option
              v-for="val in selectYearsList"
              :key="val"
              :value="val"
          >
          </el-option>
        </el-select>
      </div>
      <el-button @click="filterBtn" style="margin-left: 30px;" type="primary">筛选</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="doctorStudents">
        <el-table-column prop="stuNumber" label="学号" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center" width="80px"></el-table-column>
        <el-table-column prop="username" label="用户名" align="center"></el-table-column>
        <el-table-column prop="telephone" label="电话" align="center" width="80px"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
        <el-table-column prop="year" label="入学年份" align="center" width="70px"></el-table-column>
        <el-table-column prop="studentType" label="学生类别" align="center"></el-table-column>
        <el-table-column prop="point" label="积分" align="center" width="60px"></el-table-column>
        <el-table-column prop="teachers.name" label="导师姓名" align="center" width="80px"></el-table-column>
        <el-table-column  label="操作" align="center" width="180px">
          <template slot-scope="scope">
            <el-button size="mini" plain @click="editDialogShow(scope.row)" type="primary" style="padding: 4px">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="deleteUnder(scope.row)" style="padding: 4px">删除</el-button>
            <el-button size="mini" type="primary" plain @click="resetPasswordShow(scope.row)" style="padding: 4px">重置密码</el-button>
            <el-button size="mini" type="primary" plain @click="showDetailInfo(scope.row)" style="padding: 4px">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="编辑信息" :visible.sync="dialogEdit" center width="400px">
      <template>
        <el-form :model="currentDoctorStudent" :label-width="labelWidth">
          <el-form-item label="导师信息">
            <div class="select_div_input" style="width: 70%">
              <el-select
                  v-model="currentDoctorStudent.teachers.name"
                  filterable
                  remote
                  clearable
                  reserve-keyword
                  @change="filterEditTeacher($event)"
                  :remote-method="searchTeaNameMethod"
                  placeholder="请输入老师姓名">
                <el-option
                    v-for="item in selectTeaNameAndJobnumber"
                    :key="item"
                    :label="item"
                    :value="item">
                </el-option>
              </el-select>
            </div>
          </el-form-item>
          <el-form-item label="学生姓名">
            <el-input style="width: 70%" v-model="currentDoctorStudent.name"></el-input>
          </el-form-item>
          <el-form-item label="学生电话">
            <el-input style="width: 70%" v-model="currentDoctorStudent.telephone"></el-input>
          </el-form-item>
          <el-form-item label="学生邮箱">
            <el-input style="width: 70%" v-model="currentDoctorStudent.email"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editDoctor" type="primary">确定</el-button>
          <el-button @click="closeDialogEdit">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog title="重置密码" :visible.sync="dialogResetPassword" center width="400px">
      <el-form>
        <el-form-item label="请输入新密码:">
          <el-input style="width: 60%" v-model="newPassword"></el-input>
        </el-form-item>
        <div class="footer">
          <el-button @click="resetPassword" type="primary">确认</el-button>
          <el-button @click="closeDialogReset" type="primary">取消</el-button>
        </div>
      </el-form>
    </el-dialog>
    <div style="margin-top: 10px;text-align:right">
      <el-pagination @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="currentPage"
                     :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                     :total="totalCount"
                     :page-sizes="pageSizes"
                     background
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {debounce} from "@/utils/debounce";

export default {
  name: "SalDoctorM",
  data(){
    return{
      tabsTableLoading: false,
      tabsTableData: [],
      tabsActivateName: 'paper',
      selectTeaNameAndJobnumber:[],//编辑框中导师搜索一栏的下拉框绑定数据
      newPassword:'dhucst',
      dialogResetPassword:false,
      pageSizes:[10,20,30,50,100],
      totalCount:0,
      currentPage:1,
      pageSize:20,
      selectYearsList:[],
      select_teachers:[],
      selectTeacerName:'',
      selectYear:'',
      dialogEdit:false,
      user:{},
      doctorStudents:[],
      currentDoctorStudent:{
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
  computed:{
    labelWidth(){
      return `${8 * 17}px`
    }
  },
  created() {
    //初始化防抖
    this.debounceSearch = debounce(this.delayInputTimer,500)
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.initSelectYearsList()
    this.initDoctorStudents(this.currentPage,this.pageSize)
  },
  methods:{
    showDetailInfo(data) { //点击查看详情按钮
      this.currentDoctorStudent = data;
      let url = this.$router.resolve({
        path:'/achievement/DoctorManageAchievementInfo',
        query: {
          studentId: this.currentDoctorStudent.studentID
        }
      })
      window.open(url.href, '_blank')
    },
    searchTeaNameMethod(val) {
      if(val) {
        if(this.dialogEdit){
          this.currentDoctorStudent.teachers.name = val
        }else {
          this.selectTeacerName = val
        }
        this.debounceSearch(val)
      }
    },
    closeDialogReset(){
      this.dialogResetPassword = false
    },
    resetPasswordShow(data){//重制密码
      this.currentDoctorStudent = JSON.parse(JSON.stringify(data));
      this.dialogResetPassword = true
    },
    resetPassword(){//重制密码
      if(this.newPassword == '' || this.newPassword == null ){
        this.$message.warning('请输入密码！')
        return
      }
      this.currentDoctorStudent.password = this.newPassword
      const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[^a-zA-Z\d])[\S]{8,20}$/;
      if (this.newPassword!="dhucst" && !passwordRegex.test(this.newPassword)) {
        this.$message.error('密码必须是8-20位，包含至少一个英文字符，一个数字和一个特殊字符(@$!%*?&)');
        return
      }
      this.postRequest('/doctorM/basic/resetUnderPassword',this.currentDoctorStudent).then((response)=>{
        if(response){
          if(response.status == 200){
            this.$message.success("重置密码成功");
            this.closeDialogReset()
          }else {
            this.$message.fail("重置失败")
          }
        }
      })
    },
    filterBtn(){//点击筛选按钮
      let tempYear = this.selectYear
      if(this.selectYear == ''){
        tempYear = 0
      }
      let url = '/doctorM/basic/getDoctorsBySelect?year=' + parseInt(tempYear) + '&teaName=' + this.selectTeacerName +
          '&pageNum=' + this.currentPage + '&pageSize=' + this.pageSize
      this.getRequest(url).then((resp)=>{
        if(resp){
          if(resp.status == 200){
            this.doctorStudents = resp.obj[0]
            this.totalCount = resp.obj[1]
          }
        }
      })
    },

    //编辑框中 搜索老师姓名之后点击下拉框的某个选项
    filterEditTeacher(val){
      this.currentDoctorStudent.teachers.name = val.split(":")[1]
      this.currentDoctorStudent.teachers.jobnumber = val.split(":")[0]
    },
    //防抖函数
    delayInputTimer(data){
      let url
      if(this.dialogEdit){
        url = '/doctorM/basic/getTeaNamesBySelect?teaName=' + data
      }else {
        url = '/doctorM/basic/getTeaNamesBySelect?teaName=' + data
      }
      this.getRequest(url).then((resp)=>{
        this.select_teachers = []
        this.selectTeaNameAndJobnumber = []
        if(resp){
          if(resp.status == 200){
            for(var i=0;i<resp.obj.length;i++){
              this.select_teachers.push(resp.obj[i].name)
              //工号+姓名显示 以防老师的名字是相同的
              this.selectTeaNameAndJobnumber.push(resp.obj[i].jobnumber + ":" + resp.obj[i].name)
            }
            //去重 为什么会有重复值 先放这
            this.select_teachers = Array.from(new Set(this.select_teachers));
            this.selectTeaNameAndJobnumber = Array.from(new Set(this.selectTeaNameAndJobnumber));
          }
        }
      })
    },
    closeDialogEdit(){//关闭对话框
      this.dialogEdit = false
      this.initDoctorStudents(this.currentPage,this.pageSize)
    },
    editDialogShow(data){
      this.dialogEdit = true
      this.currentDoctorStudent = JSON.parse(JSON.stringify(data));
    },
    editDoctor(){//点击编辑中的确定按钮
      // 应该进行表单验证（如手机号），以后再改
      if(this.currentDoctorStudent.teachers.name == '' || this.currentDoctorStudent.teachers.jobnumber == '' ||
          this.currentDoctorStudent.teachers.name == null || this.currentDoctorStudent.teachers.jobnumber == null){
        this.$message.warning('请填写老师姓名！')
        return
      }
      let data = this.currentDoctorStudent
      this.postRequest('/doctorM/basic/editDoctorStudent',data).then((resp)=>{
        if(resp){
          if(resp.status == 200){
            this.dialogEdit = false
            this.$message.success(resp.msg)
            this.filterBtn();
          }
        }
      })
    },
    deleteUnder(data){//删除研究生
      this.$confirm('确定删除吗？','提示',{
        confirmButtonText:'确定',
        cancelButtonText:'取消',
        type:"warning"
      }).then(()=>{
        this.postRequest('/doctorM/basic/deleteDoctorStudent',data).then((resp)=>{
          if(resp.code == 200){
            this.$message.success('删除成功')
            this.initDoctorStudents(1,this.pageSize)
          }else {
            this.$message.warning('删除失败！')
          }
        })
      })
    },
    onSuccess(res){
      if(res.status == 200){
        this.$message.success("导入成功")
        this.initDoctorStudents(1,this.pageSize)
      }else {
        this.$message.error("导入失败")
      }
    },
    beforeUpload() {
      this.$message.success("正在导入")
    },
    UploadUrl(){
      let url = '/doctorM/basic/importDoctors?institutionID=' + this.user.institutionID
      return url;
    },
    downloadExcel(){
      let url = '/doctorM/basic/exportDoctor'
      this.$message.success('正在下载')
      window.open(url,'_parent')
    },
    handleSizeChange(val) {
      // 改变每页显示的条数
      this.pageSize=val
      // 注意：在改变每页显示的条数时，要将页码显示到第一页
      this.currentPage=1
      //没有筛选条件
      if((this.selectYear == '' || this.selectYear == null) && (this.selectTeacerName == '' || this.selectTeacerName == null)){
        this.initDoctorStudents(this.currentPage,this.pageSize)
      }else {//筛选条件不为空
        this.filterBtn()
      }
    },
    // 显示第几页
    handleCurrentChange(val) {
      // 改变默认的页数
      this.currentPage=val;
      if((this.selectYear == '' || this.selectYear == null) && (this.selectTeacerName == '' || this.selectTeacerName == null)){
        this.initDoctorStudents(this.currentPage,this.pageSize)
      }else {//筛选条件不为空
        this.filterBtn()
      }
    },
    initSelectYearsList(){
      let timeDate = new Date()
      let temp1 = Array.from(Array(timeDate.getFullYear() - 20).keys(), n=>n+1)
      let temp2 = Array.from(Array(timeDate.getFullYear()).keys(), n=>n+1)
      this.selectYearsList = temp2.filter(item1 => !temp1.some(item2 => item2 === item1))//去掉两者相同的，留下不同的
      this.selectYearsList.sort((a,b)=>{
        return b - a;
      })
    },
    initDoctorStudents(curr,pagesize){
      this.getRequest('/doctorM/basic/getDoctorStudents?pageNum=' + curr + '&pageSize=' + pagesize).then((response)=>{
        if(response.code == 200){
          this.doctorStudents = response.extend.res[0]
          this.totalCount = response.extend.res[1]
        }
      })
    }
  }
}
</script>

<style scoped>
.footer{
  text-align: center;
}
.select_div_input{
  width:20%;
  height:32px;
  position:relative;
  display:inline-block

}
</style>