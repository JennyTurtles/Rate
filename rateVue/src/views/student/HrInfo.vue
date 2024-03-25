<template>
  <div class="box">
    <div style="width: 100%;text-align: center">
      <h1>个人中心</h1>
    </div>
    <el-tabs v-model="currentType" @tab-click="typeChange" v-show="countRole()">
      <el-tab-pane label="本科信息查看" v-if="user.role.includes('10')" name="本科生"></el-tab-pane>
      <el-tab-pane label="硕士信息查看" v-if="user.role.includes('11')" name="硕士生"></el-tab-pane>
      <el-tab-pane label="博士信息查看" v-if="user.role.includes('17')" name="博士生"></el-tab-pane>
    </el-tabs>
<!--    <el-row type="flex" class="row-bg" justify="end" style="padding-right: 10px">-->
<!--      <div class="grid-content bg-purple-light" v-show="countRole()">-->
<!--        <el-radio-group v-model="currentType" style="padding-top: 25px" @change="typeChange">-->
<!--          <el-radio :label="'本科生'" v-show="user.role.includes('10')">本科信息查看</el-radio>-->
<!--          <el-radio :label="'硕士生'" v-show="user.role.includes('11')">硕士信息查看</el-radio>-->
<!--          <el-radio :label="'博士生'" v-show="user.role.includes('17')">博士信息查看</el-radio>-->
<!--        </el-radio-group>-->
<!--      </div>-->
<!--    </el-row>-->
    <el-form :model="hr" label-width="85px" class="formbox">
      <el-form-item label="用户名">
        <el-input v-model="hr.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="学生类型">
        <el-input v-model="hr.stuType" disabled></el-input>
      </el-form-item>
      <el-form-item label="研究生类型"  v-show="hr.stuType === '博士生' || hr.stuType === '硕士生'">
        <el-input v-model="hr.studentType" disabled></el-input>
      </el-form-item>
      <el-form-item label="指导老师"  v-show="hr.stuType === '博士生' || hr.stuType === '硕士生'">
        <el-input v-if="hr.teachers!==null" v-model="hr.teachers.name" disabled></el-input>
        <el-input v-if="hr.teachers===null" disabled></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="hr.name" @input="infoChange"></el-input>
      </el-form-item>
      <div v-show="hr.stuType !== '活动选手'">
        <el-form-item label="学号">
          <el-input v-model="hr.stuNumber" @input="infoChange"></el-input>
        </el-form-item>
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
      user:{},
      hr: {},
    }
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    if (this.user.role.includes("17")){
      this.currentType = "博士生";
    }
    else if (this.user.role.includes("11")){
      console.log("研究生")
      this.currentType = "硕士生";
    }
    else if (this.user.role.includes("10")){
      this.currentType = "本科生";
    }
    else{
      this.currentType = "活动选手";
    }
    this.initHr();
  },
  methods: {
    infoChange(){
      this.infoIsChanged = true
    },
    saveInfo(){
      let url = '';
      if (this.currentType === "本科生")
        url = '/undergraduateM/basic/update'
      else if (this.currentType === "硕士生")
        url = '/graduatestudentM/basic/update'
      else if (this.currentType === "博士生")
        url ='/doctorM/basic/update'
      else
        url = '/student/basic/update'
      if(this.hr.studentID) this.$set(this.hr,'id',this.hr.studentID)
      this.postRequest(url,this.hr).then((response)=>{
        if(response.status == 200){
          this.$message.success(response.msg)
          this.infoIsChanged = false
          this.user.name = this.hr.name
          localStorage.setItem("user",JSON.stringify(this.user))
          this.initHr()
        }else {
          this.$message.fail(response.msg)
        }
      })
    },
    initHr() {
      this.getRequest('/student/basic/getStuInfo?id=' + this.user.id + "&stuType=" + this.currentType).then(resp => {
        if (resp) {
          if(resp.status == 200) this.hr = resp.obj;
          console.log(this.hr)
        }
      })
    },
    typeChange(){
      this.initHr();
    },
    countRole(){
      var sum = 0;
      if (this.user.role.includes('10'))
        sum++;
      if (this.user.role.includes('11'))
        sum++;
      if (this.user.role.includes('17'))
        sum++;
      return sum > 1;
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
