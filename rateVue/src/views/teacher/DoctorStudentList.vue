<template>
  <div>
    <div style="margin-top: 10px">
      <span>
        请选择条件进行搜索：
      </span>
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
      <el-button @click="initDoctorStudents(1, 20)" style="margin-left: 30px;" type="primary">筛选</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="doctorStudents" v-loading="tableLoading">
        <el-table-column prop="stuNumber" label="学号" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center" width="80px"></el-table-column>
        <el-table-column prop="username" label="用户名" align="center"></el-table-column>
        <el-table-column prop="telephone" label="电话" align="center" width="80px"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
        <el-table-column prop="year" label="入学年份" align="center" width="70px"></el-table-column>
        <el-table-column prop="studentType" label="学生类别" align="center"></el-table-column>
        <el-table-column prop="point" label="积分" align="center" width="60px"></el-table-column>
        <el-table-column  label="操作" align="center" width="180px">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" plain @click="resetPasswordShow(scope.row)" style="padding: 4px">重置密码</el-button>
            <el-button size="mini" type="primary" plain @click="showDetailInfo(scope.row)" style="padding: 4px">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
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
  name: "DoctorList",
  data(){
    return{
      tableLoading: false,
      headers: {
        'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
      },
      newPassword:'dhucst@11',
      dialogResetPassword:false,
      pageSizes:[10,20,30,50,100],
      totalCount:0,
      currentPage:1,
      pageSize:20,
      selectYearsList:[],
      selectYear:'',
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
      this.postRequest('/doctorM/basic/resetUnderPassword',this.currentDoctorStudent).then((response)=>{
        if(response){
          if(response.status == 200){
            this.$message.success("重置成功，密码重置为"+this.newPassword);
            this.closeDialogReset()
          }else {
            this.$message.fail("重置失败")
          }
        }
      })
    },
    handleSizeChange(val) {
      // 改变每页显示的条数
      this.pageSize=val
      // 注意：在改变每页显示的条数时，要将页码显示到第一页
      this.currentPage=1
      //没有筛选条件
      this.initDoctorStudents(this.currentPage, this.pageSize)
    },
    // 显示第几页
    handleCurrentChange(val) {
      // 改变默认的页数
      this.currentPage=val;
      this.initDoctorStudents(this.currentPage,this.pageSize)
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
      let tempYear = this.selectYear
      if(this.selectYear == ''){
        tempYear = 0
      }
      this.tableLoading = true
      this.getRequest(`/doctorM/basic/getDoctorStudentsBySelectOfTeacher?tutorID=${this.user.id}&year=${tempYear}&pageNum=${curr}&pageSize=${pagesize}`).then((response)=>{
        if(response.status == 200){
          this.tableLoading = false
          this.doctorStudents = response.obj[0]
          this.totalCount = response.obj[1]
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
.select_div{
  border: .5px solid lightgray;
  border-radius: 3px;
  margin-top: 5px;
  font-size: 14px;
  position: absolute;
  background-color: #fff;
  z-index: 999;
  overflow: scroll;
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