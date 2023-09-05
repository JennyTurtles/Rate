<template>
  <div class="box">
    <el-row type="flex" class="row-bg" justify="space-between">
      <el-col :span="18">
        <div style="width: 100%;text-align: center">
            <h1>个人中心</h1>
        </div>
      </el-col>
      <el-col :span="6" v-show="user.role.includes('11') && user.role.includes('10')" >
        <div class="grid-content bg-purple-light">
          <el-radio-group v-model="currentType" style="padding-top: 25px" @change="typeChange">
            <el-radio :label="'本科生'" >本科信息查看</el-radio>
            <el-radio :label="'研究生'" >研究生信息查看</el-radio>
          </el-radio-group>
        </div>
      </el-col>
    </el-row>
    <el-form :model="hr" label-width="85px" class="formbox">
      <el-form-item label="用户名">
        <el-input v-model="hr.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="学生类型">
        <el-input v-model="hr.stuType" disabled></el-input>
      </el-form-item>
      <el-form-item label="研究生类型"  v-show="hr.stuType === '研究生'">
        <el-input v-model="hr.studentType" disabled></el-input>
      </el-form-item>
      <el-form-item label="学号" v-show="hr.stuType !== '选手'">
        <el-input v-model="hr.stuNumber" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="hr.name" @input="infoChange"></el-input>
      </el-form-item>
      <div v-show="hr.stuType !== '选手'">
        <el-form-item label="入学年份">
          <el-input v-model="hr.year" @input="infoChange"></el-input>
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="hr.specialty" @input="infoChange"></el-input>
        </el-form-item>
        <el-form-item label="班级">
          <el-input v-model="hr.className" @input="infoChange"></el-input>
        </el-form-item>
      </div>
      <el-form-item label="电话">
        <el-input v-model="hr.telephone" @input="infoChange"></el-input>
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
      currentType:"",
      flag:true, //第一次进入界面
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
      let url = '';
      if (this.hr.stuType == "本科生")
        url = '/undergraduateM/basic/update'
      else if (this.hr.stuType == "研究生")
        url = '/graduatestudentM/basic/update'
      else
        url = '/student/basic/update'
      if(this.hr.studentID) this.$set(this.hr,'id',this.hr.studentID)
      this.postRequest(url,this.hr).then((response)=>{
        if(response.status == 200){
          this.$message.success(response.msg)
          this.infoIsChanged = false
          this.user.name = this.hr.name
          localStorage.setItem("user",JSON.stringify(this.user))
          this.reload()
        }else {
          this.$message.fail(response.msg)
        }
      })
    },
    initHr() {
      if (this.flag !== false){
        if (this.user.role.includes("11")){
          this.currentType = "研究生";
        }
        else if (this.user.role.includes("10")){
          this.currentType = "本科生";
        }
        else{
          this.currentType = "不是大学生";
        }
        this.flag = false;
      }
      this.getRequest('/student/basic/getStuInfo?id=' + this.user.id + "&stuType=" + this.currentType).then(resp => {
        if (resp) {
          if(resp.status == 200) this.hr = resp.obj;
        }
      })
    },
    typeChange(){
      this.initHr();
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
