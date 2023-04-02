<template>
  <div>
    <div>
      <el-button type="primary" @click="downloadExcel">下载模版</el-button>
      <el-button type="primary">上传excel</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "SalTeacherM",
  data(){
    return{

    }
  },
  methods:{
    onSuccess(res){
      if(res.status == 200){
        this.$message.success("导入成功")
      }else {
        this.$message.error("导入失败")
      }
    },
    beforeUpload() {
      this.$message.success("正在导入")
    },
    UploadUrl(){
      let url = '/teacher/basic/importTeachers?role=' + toString(10)
      return url;
    },
    downloadExcel(){
      let url = '/teacher/basic/exportTeachersByAdmin'
      this.$message.success('正在下载')
      window.open(url,'_parent')
    }
  }
}
</script>

<style scoped>

</style>