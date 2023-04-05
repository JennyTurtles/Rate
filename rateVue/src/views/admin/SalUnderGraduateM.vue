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
      <el-table
        :data="undergraduateStudents">
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="stuNumber" label="学号" align="center"></el-table-column>
        <el-table-column prop="telephone" label="电话" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
        <el-table-column prop="year" label="入学年份" align="center"></el-table-column>
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
        <el-form :model="currentUnderStudentOfEdit">
          <el-form-item label="导师姓名">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.teachers.name"></el-input>
          </el-form-item>
          <el-form-item label="导师工号">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.teachers.jobnumber"></el-input>
          </el-form-item>
          <el-form-item label="学生姓名">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.name"></el-input>
          </el-form-item>
          <el-form-item label="学生手机号">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.telephone"></el-input>
          </el-form-item>
          <el-form-item label="学生邮箱">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.email"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editUnder" type="primary">确定</el-button>
          <el-button @click="closeDialogEdit">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";
export default {
  name: "SalStudentM",
  data(){
    return {
      currentUnderStudentOfEdit:{
        ID:null,
        name:'',
        teachers:{
          name:'',
          jobnumber:''
        },
        studentID:null,
        tutorID:null,
        telephone:'',
        email:''
      },
      dialogEdit:false,
      user:{},
      undergraduateStudents:[]
    }
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.initUnderGraduateStudents()
  },
  methods:{
    closeDialogEdit(){//关闭对话框
      this.dialogEdit = false
      this.currentUnderStudentOfEdit = {}
    },
    editDialogShow(data){
      this.dialogEdit = true
      this.currentUnderStudentOfEdit = data
    },
    editUnder(){//点击编辑中的确定按钮
      if(this.currentUnderStudentOfEdit.teachers.name == '' || this.currentUnderStudentOfEdit.teachers.jobnumber == ''){
        this.$message.warning('请填写老师姓名和工号！')
        return
      }
      let data = this.currentUnderStudentOfEdit
      this.postRequest('/undergraduateM/basic/editUnderGraduateStudent',data).then((resp)=>{
        if(resp){
          if(resp.status == 200){
            this.dialogEdit = false
            this.$message.success(resp.msg)
            this.initUnderGraduateStudents()
          }else {
            this.$message.error(resp)
          }
        }
      })
    },
    deleteUnder(data){//删除本科生
      this.postRequest('/undergraduateM/basic/deleteUnderGraduateStudent',data).then((resp)=>{
        if(resp.code == 200){
          this.$message.success('删除成功')
          this.initUnderGraduateStudents()
        }
      })
    },
    onSuccess(res){
      if(res.status == 200){
        this.$message.success("导入成功")
        this.initUnderGraduateStudents();
      }else {
        this.$message.error("导入失败")
      }
    },
    beforeUpload() {
      this.$message.success("正在导入")
    },
    UploadUrl(){
      let url = '/undergraduateM/basic/importUnderGraduate?institutionID=' + this.user.institutionID
      return url;
    },
    downloadExcel(){
      let url = '/undergraduateM/basic/exportUnderGraduate'
      this.$message.success('正在下载')
      window.open(url,'_parent')
    },
    initUnderGraduateStudents(){
      this.getRequest('/undergraduateM/basic/getUnderGraduateStudents').then((response)=>{
        if(response.code == 200){
          this.undergraduateStudents = response.extend.res
        }
      })
    }
  }
}
</script>

<style scoped>

</style>