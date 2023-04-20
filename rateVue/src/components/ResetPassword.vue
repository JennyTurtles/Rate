<template>
  <div>
    <div v-show="!resetPassShow">
      <el-form class="resetPassContainer" :label-width="labelWidth" >
        <el-form-item label="请输入身份证号:">
          <el-input style="width: 60%"  v-model="idNumber" @input="checkIdNumber"></el-input>
        </el-form-item>
        <el-form-item label="请输入密保问题:">
          <el-input style="width: 60%"  v-model="passQuestion"></el-input>
        </el-form-item>
        <el-form-item label="请输入密保答案:">
          <el-input style="width: 60%" v-model="passAnswer" :disabled="pwdSecurityStatus"></el-input>
        </el-form-item>
        <div class="footer">
          <el-button @click="check" type="primary">确认</el-button>
        </div>
      </el-form>
    </div>

    <div v-show="resetPassShow">
      <el-form class="resetPassContainer" :label-width="labelWidth" :rules="rules">
        <el-form-item label="请输入新密码:">
          <el-input style="width: 60%" v-model="newPass" type="password"></el-input>
        </el-form-item>
        <el-form-item label="请确认新密码:" prop="confirmNewPass">
          <el-input style="width: 60%" v-model="confirmNewPass" type="password"></el-input>
        </el-form-item>
        <div class="footer">
          <el-button @click="reset" type="primary">确认</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import {debounce} from "@/utils/debounce";
import {getRequest,postRequest} from "@/utils/api";

export default {
  name: "ResetPassword",
  data(){
    return{
      pwdSecurityStatus:true,
      pwdSecurityNums:{
        count:0,
        times:new Date()
      },
      user:{},
      role:"",
      idNumber:'',
      passQuestion:'',
      passAnswer:'',
      resetPassShow:false,
      newPass:'',
      confirmNewPass:'',
      rules:{
        confirmNewPass:[
          {
            trigger:["blur","change"],
            message:"请仔细核对密码！",
            required:true,
            validator:(rule,value,callback)=>{
              if (this.newPass !== value) {
                callback(new Error("输入密码不一致!"));
              }
            }
          }
        ]
      },
    }

  },
  mounted() {
    this.role = this.$route.query.key
    //存在vuex会不会更安全一点？
    if(localStorage.getItem("pwdSecurityNums") === null){
      localStorage.setItem("pwdSecurityNums",JSON.stringify(this.pwdSecurityNums))
      this.$nextTick(() => this.pwdSecurityStatus = false)
    }else {
      //更好的方式是设置一个定时器，监视时间戳的改变，不过24之后用户基本都会刷新，定时器不需要应该也可以
      this.pwdSecurityNums = JSON.parse(localStorage.getItem("pwdSecurityNums"))
      this.$nextTick(()=>{
        this.clearPwdSecurity()
        this.judgePwdSecurity()
      })
    }
  },
  watch:{},
  computed:{
    labelWidth(){
      return `${10 * 17}px`
    }
  },
  methods:{
    clearPwdSecurity(){//判断是否超过24小时，超过直接赋值
      let hour = new Date().getHours()
      if(hour - this.pwdSecurityNums.times >= 24){
        let temp = {
          count:0,
          times:new Date()
        }
        localStorage.setItem("pwdSecurityNums",JSON.stringify(temp))
      }
    },
    judgePwdSecurity(){
      let hour = new Date().getHours()
      //改变密保输入框的状态
      if(this.pwdSecurityNums.count < 3 && (hour - this.pwdSecurityNums.times) < 24){
        this.pwdSecurityStatus = false//可以输入
      }else {
        this.$message.warning("密保输入错误超过限制！请在24小时之后尝试")
        this.pwdSecurityStatus = true//不可输入
      }
    },
    check(){
      if(this.passQuestion == '' || this.passQuestion == null){
        return
      }
      if(this.passAnswer == '' || this.passAnswer == null){
        return
      }
      if(this.idNumber == '' || this.idNumber == null){
        return
      }
      //密保问题正确并且根据身份证号查找到了学生
      if(this.passQuestion === this.user.registerQuestion && this.passAnswer === this.user.registerAnswer){
        this.resetPassShow = true
      }else {
        this.resetPassShow = false
        this.pwdSecurityNums.count ++;//时间不变，次数改变
        if(this.pwdSecurityNums.count == 3){
          this.$message.warning(`密保答案不正确！请24小时之后再次尝试`)
          this.pwdSecurityStatus = true
        }else {
          this.$message.warning(`密保答案不正确！还剩${3 - this.pwdSecurityNums.count}次机会`)
        }
        localStorage.setItem("pwdSecurityNums",JSON.stringify(this.pwdSecurityNums))
      }
    },
    //输入身份证号进行查找判断
    checkIdNumber(){
      if(this.idNumber.length == 15 || this.idNumber.length == 18){
        let url = '/registerUser/getUserByIdNumber?role=' + this.role + '&idNumber=' + this.idNumber
        this.getRequest(url).then((response)=>{
          if (response){
            if(response.status == 200){
              this.user = response.obj
              this.passQuestion = response.obj.registerQuestion
            }
          }
        })
      }
    },
    reset(){
      if(this.newPass == '' || this.newPass == null){
        return
      }
      if(this.confirmNewPass == '' || this.confirmNewPass == null){
        return
      }
      //验证通过
      if(this.newPass === this.confirmNewPass && this.resetPassShow){
        let url = ''
        if(this.role == 'student'){
          url = '/student/basic/updatePassword'
        } else if(this.role == 'teacher'){
          url = '/teacher/basic/updatePassword'
        }
        this.$set(
            this.user,
            "password",
            this.newPass
        )
        this.postRequest(url,this.user).then((response)=>{
          if(response){
            if(response.status == 200){
              this.$message.success('更新成功！')
              this.$router.replace("/")
            }else {
              this.$message.warning('更新失败！')
            }
          }
        })
      }else {
        this.$message.warning('验证不通过！')
      }
    }
  }
}
</script>

<style scoped>
.footer{
  text-align: center;
}
.resetPassContainer {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 50px auto;
  width: 500px;
  padding: 15px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
</style>