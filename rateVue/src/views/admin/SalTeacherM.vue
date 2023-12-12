<template>
  <div>
    <div>
      <el-button type="primary" @click="downloadExcel">下载模版</el-button>
      <el-upload
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-success="onSuccess"
          :headers="{
        'token': user.token
      }"
          style="display: inline-flex; margin-left: 8px"
          :action="UploadUrl()"
      >
        <el-button type="primary">上传教师excel</el-button>
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
      <el-button @click="filterBtn" style="margin-left: 30px;" type="primary">筛选</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="teachers">
        <el-table-column prop="jobnumber" label="工号" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center" width="80px"></el-table-column>
        <el-table-column prop="username" label="用户名" align="center"></el-table-column>
        <el-table-column prop="phone" label="电话" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
        <el-table-column prop="idnumber" label="身份证号" align="center"></el-table-column>
        <el-table-column prop="sex" label="性别" align="center" width="60px"></el-table-column>
        <el-table-column prop="department" label="部门" align="center"></el-table-column>
        <el-table-column  label="操作" align="center" width="180px">
          <template slot-scope="scope">
            <el-button size="mini" plain @click="editDialogShow(scope.row)" type="primary" style="padding: 4px">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="deleteUnder(scope.row)" style="padding: 4px">删除</el-button>
            <el-button size="mini" type="primary" plain @click="resetPasswordShow(scope.row)" style="padding: 4px">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="编辑信息" :visible.sync="dialogEdit" center width="500px" @close="closeDialogEdit">
      <template>
        <el-form :model="currentTeacherOfEdit">
          <el-form-item label="教师电话">
            <el-input style="width: 50%" v-model="currentTeacherOfEdit.phone"></el-input>
          </el-form-item>
          <el-form-item label="教师邮箱">
            <el-input style="width: 50%" v-model="currentTeacherOfEdit.email"></el-input>
          </el-form-item>
          <el-form-item label="教师部门">
            <el-input style="width: 50%" v-model="currentTeacherOfEdit.department"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editGraduate" type="primary">确定</el-button>
          <el-button @click="closeDialogEdit">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog title="重置密码" :visible.sync="dialogResetPassword" center width="400px" @close="closeDialogReset">
      <el-form >
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
export default {
  name: "SalTeacherM",
  data(){
    return{
      newPassword:'dhucst',//重置密码中的新密码
      pageSizes:[15,20,20,20,30],
      totalCount:0,
      currentPage:1,
      pageSize:15,
      dialogResetPassword:false,
      isSelectYearFlag:false,
      isSelectYearShow:false,
      selectYearsList:[],
      select_teachers:[],
      isSelectFlag:false,
      isSelectShow:false,//搜索老师名字的搜索框
      timer:null,
      selectTeacerName:'',
      teachers:[],
      dialogEdit:false,
      user:{},
      currentTeacherOfEdit:{
        ID:null,
        name:'',
        jobnumber:'',
        phone:'',
        email:'',
        point:'',
        department:'',
        password:'',
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
    this.initTeachers()
  },
  methods:{
    resetPassword(){//重制密码
      if(this.newPassword == '' || this.newPassword == null ){
        this.$message.warning('请输入密码！')
        return
      }
      this.currentTeacherOfEdit.password = this.newPassword
      const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[^a-zA-Z\d])[\S]{8,20}$/;
      if (this.newPassword!="dhucst" && !passwordRegex.test(this.newPassword)) {
        this.$message.error('密码必须是8-20位，包含至少一个英文字符，一个数字和一个特殊字符(@$!%*?&)');
        return
      }
      this.postRequest('/teacher/basic/updatePassword',this.currentTeacherOfEdit).then((response)=>{
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

    closeDialogReset(){
      this.dialogResetPassword = false
    },
    resetPasswordShow(data){//重制密码
      this.currentTeacherOfEdit = data
      this.dialogResetPassword = true
    },
    inputSelectTeacerNameFocus(){//input获取焦点判断是否有下拉框，是否可输入
      this.isSelectShow = true//控制下拉框是否显示
    },
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
      let that = this
      this.timer = setTimeout(()=>{
        let url = '/teacher/basic/getTeaNamesBySelect?teaName=' + this.selectTeacerName
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
    filterBtn(){//点击筛选按钮
      this.getRequest('/teacher/basic/getTeachers?pageNum=' + this.currentPage + '&pageSize=' + this.pageSize +
          '&teaName=' + this.selectTeacerName).then((response)=>{
        if(response.code == 200){
          this.teachers = response.extend.res[0]
          this.totalCount = response.extend.res[1]
        }
      })
    },
    closeDialogEdit(){//关闭对话框
      this.dialogEdit = false
      this.initTeachers()
      // this.currentGraduateStudentOfEdit = {}
    },
    editDialogShow(data){
      this.dialogEdit = true
      this.currentTeacherOfEdit = data
    },
    editGraduate(){//点击编辑中的确定按钮
      let data = this.currentTeacherOfEdit
      this.postRequest('/teacher/basic/update',data).then((resp)=>{
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
    deleteUnder(data){//删除教师
      this.$confirm('是否确定删除'+data.name+'教师?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() =>{
        this.postRequest('/teacher/basic/delete',data).then((resp)=>{
          console.log(resp)
          if(resp.status == 200){
            this.$message.success('删除成功')
            this.initTeachers()
          }
        })
      })
    },
    onSuccess(res){
      if(res.status == 200){
        this.$message.success("导入成功")
        this.initTeachers()
      }else {
        this.$message.error("导入失败")
      }
    },
    beforeUpload() {
      this.$message.success("正在导入")
    },
    UploadUrl(){
      let url = '/teacher/basic/importTeachers?institutionID=' + this.user.institutionID
      return url;
    },
    downloadExcel(){
      let url = '/teacher/basic/exportTeachersByAdmin'
      this.$message.success('正在下载')
      window.open(url,'_parent')
    },
    handleSizeChange(val) {
      // 改变每页显示的条数
      this.pageSize=val
      // 注意：在改变每页显示的条数时，要将页码显示到第一页
      this.currentPage=1
      if((this.selectTeacerName == '' || this.selectTeacerName == null)){
        this.initTeachers()
      }else {//筛选条件不为空
        this.filterBtn()
      }
    },
    // 显示第几页
    handleCurrentChange(val) {
      // 改变默认的页数
      this.currentPage=val;
      if((this.selectTeacerName == '' || this.selectTeacerName == null)){
        this.initTeachers()
      }else {//筛选条件不为空
        this.filterBtn()
      }
    },
    initTeachers(){
      this.getRequest('/teacher/basic/getTeachers?pageNum=' + this.currentPage + '&pageSize=' + this.pageSize +
          '&teaName=' + this.selectTeacerName).then((response)=>{
        if(response.code == 200){
          this.teachers = response.extend.res[0]
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
