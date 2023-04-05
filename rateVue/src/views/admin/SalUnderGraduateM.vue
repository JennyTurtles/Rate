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
            <el-button size="mini"  icon="el-icon-edit" plain @click="deleteUnder(scope.row)" type="primary">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" plain @click="deleteUnder(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import {Message} from "element-ui";
export default {
  name: "SalStudentM",
  data(){
    return {
      user:{},
      undergraduateStudents:[]
    }
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.initUnderGraduateStudents()
  },
  methods:{
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
          console.log(response.extend.res)
          this.undergraduateStudents = response.extend.res
        }
      })
    }
  }
}
</script>

<style scoped>

</style>