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
    downloadExcel(){
      let url = ''
      this.$message.success('正在下载')
      window.open(url,'_parent')
    }
  }
}
</script>

<style scoped>

</style>