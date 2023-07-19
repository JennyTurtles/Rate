<template>
  <div class="box">
      <el-form class="registerContainer" :label-width="labelWidth" :rules="rules">
        <el-form-item label="请输入身份证号:">
          <el-input style="width: 60%"  @input="idNumberChange" v-model="user.idnumber"></el-input>
        </el-form-item>
        <el-form-item label="请输入学生姓名:">
          <el-input style="width: 60%" v-model="user.name" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="请输入学生电话:">
          <el-input style="width: 60%" v-model="user.telephone" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="请输入学生邮箱:">
          <el-input style="width: 60%" v-model="user.email" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="请选择注册的学生类型:">
<!--          <span class="selectTitle"></span>-->
          <el-select v-model="selectStuType" clearable>
            <el-option
                v-for="val in stuType"
                :value="val"
                :label="val"
                :key="val">
            </el-option>
          </el-select>
        </el-form-item>
<!--        <div v-show="selectStuType == '本科生' || selectStuType == '研究生'">-->
        <div v-show="selectStuType !== '没有本校学号' && selectStuType !== null && selectStuType !== ''">
          <el-form-item label="请输入学号:">
            <el-input style="width: 60%" v-model="user.studentnumber" ></el-input>
          </el-form-item>
          <el-form-item label="请输入入学年份:">
            <el-input style="width: 60%" v-model="user.year" :placeholder="defaultYear" ></el-input>
          </el-form-item>
        </div>
        <div v-show="selectStuType === '研究生'">
          <el-form-item label="请选择研究生类型:">
            <el-select v-model="user.gradType">
              <el-option v-for="val in ['专硕','学硕']"
                         :value="val"
                         :label="val"
                         :key="val">
              </el-option>
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="请输入用户名:">
          <el-input style="width: 60%" v-model="user.username" :disabled="usernameAndPwdIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="请输入密码:">
          <el-input style="width: 60%" v-model="user.password" type="password" :disabled="usernameAndPwdIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="请输入确认密码:" prop="confirmPassword">
          <el-input style="width: 60%" v-model="confirmPassword" type="password" :disabled="usernameAndPwdIsDisabled" ></el-input>
        </el-form-item>
        <el-form-item label="请输入密保问题:">
          <el-input style="width: 60%" v-model="user.registerQuestion" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="请输入密保答案:">
          <el-input style="width: 60%" v-model="user.registerAnswer" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <div class="footer">
          <el-button @click="register" type="primary">注册</el-button>
        </div>
      </el-form>
  </div>
</template>

<script>
import {debounce} from "@/utils/debounce";
import {getRequest,postRequest} from "@/utils/api";
export default {
  name: "StuRegister",
  data(){
    return{
      debounceCheckPwd:null,
      usernameAndPwdIsDisabled:false,
      userInfoIsDisabled:false,
      defaultYear:2022,
      selectStuType:'',
      confirmPassword:'',
      checkPwdState:false,
      stuType:['本科生','研究生','不是大学生'],
      user:{
        name:'',
        telephone:'',
        email:'',
        idnumber:'',
        studentnumber:'',
        year:'',
        username:'',
        password:'',
        gradType:'',//专硕/学硕
        registerQuestion:'',
        registerAnswer:''
      },
      rules:{
        confirmPassword: [
          {
            trigger:['blur','change'],
            required:true,
            // message:'输入密码不一致!',
            validator:(rule,value,callback)=>{
              if(!this.checkPwdState) callback(new Error("输入密码不一致!"));
            }
          }
        ]
      }
    }
  },
  mounted() {
    // this.debounceCheckPwd = debounce(this.checkPwd(),300)
  },
  computed:{
    labelWidth(){
      return `${10 * 17}px`
    }
  },
  watch:{
    confirmPassword:{
      handler(){
        //因为不想输入一个判断一次，所以只在长度一样的时候判断一次
        if(this.confirmPassword.length == this.user.password.length)this.checkPwd()
        else this.checkPwdState = false
        // this.debounceCheckPwd()
      }
    },
    selectStuType:{
      handler(){
        this.getStu()
      }
    }
  },
  methods:{
    //因为form自带的表单验证有问题，所以自定义验证方法
    checkPwd(){
      if(this.confirmPassword !== this.user.password) this.checkPwdState = false
      else this.checkPwdState = true
    },

    register(){
      if(this.user.idnumber == null || this.user.idnumber == ''){
        this.$message.warning('请输入身份证号！')
        return
      }
      if(this.selectStuType == null || this.selectStuType == ''){
        this.$message.warning('请选择注册的学生身份！')
        return
      }
      if(this.user.registerQuestion == null || this.user.registerQuestion == ''){
        this.$message.warning('请输入密保问题！')
        return
      }
      if(this.user.registerAnswer == null || this.user.registerAnswer == ''){
        this.$message.warning('请输入密保答案！')
        return
      }
      this.user.stuType = this.selectStuType
      postRequest('/registerUser/stu',this.user).then((response)=>{
        if(response){
          this.$message.success('注册成功！')
          this.$router.replace('/')
        }
      })
    },
    //填写身份证号
    idNumberChange(){
      if(this.user.idnumber.length == 15 || this.user.idnumber.length == 18){
        this.getStu()
      }
    },
    getStu(){
      if(this.user.idnumber == '' || this.user.idnumber == null){
        return
      }
      let url = '/student/basic/getStuByIDNumber?IDNumber=' +  this.user.idnumber + '&stuType=' + this.selectStuType
      getRequest(url).then((resp)=>{
        if(resp){
          if(resp.status == 200 && resp.obj != null){
            this.user = resp.obj
            if(this.user.registerQuestion != '' && this.user.registerQuestion != null && this.user.registerAnswer != '' && this.user.registerAnswer != null){
              this.userInfoIsDisabled = true
            }else {
              this.userInfoIsDisabled = false
            }
            if(this.user.username != '' && this.user.username != null && this.user.password != '' && this.user.password != null){
              this.usernameAndPwdIsDisabled = true
            }else {
              this.usernameAndPwdIsDisabled = false
            }
          }else if(resp.obj == null && (this.selectStuType == "" || this.selectStuType == null)){
            this.user.name = ''
            this.user.telephone = ''
            this.user.email = ''
            this.user.registerQuestion = ''
            this.user.registerAnswer = ''
            this.user.username = ''
            this.user.password = ''
            this.usernameAndPwdIsDisabled = false
            this.userInfoIsDisabled = false
          }
        }
      }).catch((err)=>{
        console.log(err)
      })
    },
    clearStuUser(){
      this.user = {
        name:'',
        telephone:'',
        email:'',
        idnumber:'',
        stuNumber:'',
        year:'',
        username:'',
        password:'',
        studentType:'',
        registerQuestion:'',
        registerAnswer:''
      }
    }
  },
}
</script>

<style scoped>
  .footer{
    text-align: center;
  }
  .registerContainer {
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