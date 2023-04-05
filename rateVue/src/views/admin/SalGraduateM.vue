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
            <el-button size="mini"  icon="el-icon-edit" plain @click="deleteUnder(scope.row)" type="primary">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" plain @click="deleteUnder(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "SalGraduateM",
  data(){
    return{
      user:{},
      graduateStudents:[]
    }
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.initGraduateStudents()
  },
  methods:{
    deleteUnder(data){//删除本科生
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
          console.log(response.extend.res)
          this.graduateStudents = response.extend.res
        }
      })
    }
  }
}
</script>

<style scoped>

</style>