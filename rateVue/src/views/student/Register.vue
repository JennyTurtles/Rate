<template>
  <div class="box">
      <el-form class="registerContainer" :label-width="labelWidth">
        <el-form-item label="请输入身份证号:">
          <el-input style="width: 60%"  @input="idNumberChange" v-model="user.idnumber"></el-input>
        </el-form-item>
        <el-form-item label="请输入学生姓名:">
          <el-input style="width: 60%" v-model="user.name"></el-input>
        </el-form-item>
        <el-form-item label="请输入学生电话:">
          <el-input style="width: 60%" v-model="user.telephone"></el-input>
        </el-form-item>
        <el-form-item label="请输入学生邮箱:">
          <el-input style="width: 60%" v-model="user.email"></el-input>
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
        <div v-show="selectStuType !== '没有本校学号' && selectStuType !== null && selectStuType !== ''">
          <el-form-item label="请输入学号:">
            <el-input style="width: 60%" v-model="user.studentnumber"></el-input>
          </el-form-item>
          <el-form-item label="请输入入学年份:">
            <el-input style="width: 60%" v-model="user.year" placeholder="2022"></el-input>
          </el-form-item>
        </div>
        <div v-show="selectStuType === '研究生'">
          <el-form-item label="请输入研究生类型:">
            <el-input style="width: 60%" v-model="user.gradType" placeholder="专硕/学硕"></el-input>
          </el-form-item>
        </div>
        <el-form-item label="请输入用户名:">
          <el-input style="width: 60%" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item label="请输入密码:">
          <el-input style="width: 60%" v-model="user.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="请输入密保问题:">
          <el-input style="width: 60%" v-model="user.registerQuestion"></el-input>
        </el-form-item>
        <el-form-item label="请输入密保答案:">
          <el-input style="width: 60%" v-model="user.registerAnswer"></el-input>
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
      selectStuType:'',
      stuType:['没有本校学号','本科生','研究生'],
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
    }
  },
  computed:{
    labelWidth(){
      return `${10 * 17}px`
    }
  },
  watch:{
    selectStuType:{
      handler(){
        this.getStu()
      }
    }
  },
  methods:{
    register(){
      if(this.user.idnumber == null || this.user.idnumber == ''){
        this.$message.warning('请输入身份证号！')
        return
      }
      if(this.selectStuType == null || this.selectStuType == ''){
        this.$message.warning('请选择注册的学生身份！')
        return
      }
      this.user.stuType = this.selectStuType
      postRequest('/registerUser/stu',this.user).then((response)=>{
        if(response){
          console.log(response)
          this.$message.success('注册成功！')
          this.$router.replace('/')
        }
      })
    },
    //填写身份证号
    idNumberChange: debounce(function (){this.getStu();},400),
    getStu(){
      if(this.user.idnumber == '' || this.user.idnumber == null){
        return
      }
      let url = '/student/basic/getStuByIDNumber?IDNumber=' +  this.user.idnumber + '&stuType=' + this.selectStuType
      getRequest(url).then((resp)=>{
        if(resp){
          if(resp.status == 200 && resp.obj != null){
            this.user = resp.obj
            //查到了这个学生把用户名密码设置为空，用户重新设置？
            this.user.username = ''
            this.user.password = ''
          }else if(resp.obj == null && (this.selectStuType == "" || this.selectStuType == null)){
            this.user.name = ''
            this.user.telephone = ''
            this.user.email = ''
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