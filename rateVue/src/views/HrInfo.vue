<template>
  <div class="box">
    <h1>个人中心</h1>
    <el-form :model="hr" label-width="70px" class="formbox">
      <el-form-item label="用户名">
        <el-input v-model="hr.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="身份证号">
        <el-input v-model="hr.idnumber" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="hr.name" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="部门">
        <el-input v-model="hr.department" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="工号">
        <el-input v-model="hr.jobnumber" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="hr.phone" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="hr.email" @input="infoChange"></el-input>
      </el-form-item>
      <div class="footer">
        <el-button @click="saveInfo" type="primary" :disabled="!infoIsChanged">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "HrInfo",
  inject: ["reload"],
  data() {
    return {
      infoIsChanged:false,
      user:{},
      hr: {},
    }
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.initHr();
  },
  methods: {
    infoChange(){
      this.infoIsChanged = true
    },
    saveInfo(){
      let url = '/teacher/basic/updateByInfo'
      this.postRequest(url,this.hr).then((response)=>{
        if(response.status == 200){
          this.$message.success(response.msg)
          this.infoIsChanged = false
          this.user.name = this.hr.name
          localStorage.setItem("user",JSON.stringify(this.user))
          this.reload()
        }else {
          this.$message.error("更新失败")
        }
      })
    },
    initHr() {
      this.getRequest('/teacher/basic/getTeaInfo?id=' + this.user.id).then(resp => {
        if (resp) {
          if(resp.status == 200)
            this.hr = resp.obj;
          console.log(this.hr);
        }
      })
    }
  }
}
</script>

<style scoped>
.formbox{
  margin: 30px auto;
  width: 60%;
}
.box{
  overflow: scroll;
  text-align: center;
  width: 100%;
  height: 100%;
  background-color: white;
}
</style>
