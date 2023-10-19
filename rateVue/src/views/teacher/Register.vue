<template>
  <div class="box">
    <el-form class="registerContainer" :label-width="labelWidth">
      <el-form-item label="请输入身份证号:">
        <el-input style="width: 60%"  @input="idNumberChange" v-model="user.idnumber"></el-input>
      </el-form-item>
      <el-form-item label="请输入教师姓名:">
        <el-input style="width: 60%" v-model="user.name"></el-input>
      </el-form-item>
      <el-form-item label="请输入教师电话:">
        <el-input style="width: 60%" v-model="user.phone"></el-input>
      </el-form-item>
      <el-form-item label="请输入教师邮箱:">
        <el-input style="width: 60%" v-model="user.email"></el-input>
      </el-form-item>
      <el-form-item label="请输入教师性别:">
        <el-input style="width: 60%" v-model="user.sex" placeholder="男/女"></el-input>
      </el-form-item>
      <el-form-item label="请输入教师工号:">
        <el-input style="width: 60%" v-model="user.jobnumber"></el-input>
      </el-form-item>
      <el-form-item label="请输入教师部门:">
        <el-input style="width: 60%" v-model="user.department"></el-input>
      </el-form-item>
      <el-form-item label="请输入用户名:">
        <el-input style="width: 60%" v-model="user.username"></el-input>
      </el-form-item>
      <el-form-item label="请输入密码:">
        <el-input style="width: 60%" v-model="user.password" type="password" @blur="checkPassword"></el-input>
      </el-form-item>
      <el-form-item label="请确认密码:" prop="confirmPassword">
        <el-input style="width: 60%" v-model="confirmPassword" type="password" @blur="checkPasswordSame" ></el-input>
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
import {getRequest, postRequest} from "@/utils/api";

export default {
  name: "TeaRegister",
  data(){
    return{
      confirmPassword:'',
      user:{
        name:'',
        phone:'',
        email:'',
        idnumber:'',
        department: '',
        sex:'',
        jobnumber:'',
        username:'',
        password:'',
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
  methods:{
    checkPasswordSame(){
      if(this.user.password != this.confirmPassword){
        this.checkPwdState = false
        this.$message.error('两次密码输入不一致，请检查');
      }else{
        this.checkPwdState = true
      }
    },
    checkPassword() {
      const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
      if (!passwordRegex.test(this.user.password)) {
        this.checkPwdState = false
        this.$message.error('密码必须是8-20位，包含至少一个英文字符，一个数字和一个特殊字符(@$!%*?&)');
      }
    },
    //点击注册按钮
    register(){
      if(this.checkPwdState == false){
        this.$message.error('密码必须是8-20位，包含至少一个英文字符，一个数字和一个特殊字符(@$!%*?&)')
        return
      }
      if(this.user.idnumber == null || this.user.idnumber == ''){
        this.$message.warning('请输入身份证号！')
        return
      }
      postRequest('/registerUser/tea',this.user).then((response)=>{
        if(response){
          console.log(response)
          this.$message.success('注册成功！')
          this.$router.replace('/Teacher/Login')
        }
      })
    },

    //输入身份证号 防抖查找是否有这个用户
    idNumberChange: debounce(function (){this.getTea();},400),
    getTea(){
      if(this.user.idnumber == '' || this.user.idnumber == null){
        return
      }
      let url = '/teacher/basic/getTeaByIdNumber?IDNumber=' + this.user.idnumber
      getRequest(url).then((resp)=>{
        if(resp){
          if(resp.status == 200 && resp.obj != null){//说明有这个老师
            this.user = resp.obj
            //查到了,把用户名密码设置为空
            this.user.username = ''
            this.user.password = ''
          }else if(resp.obj == null){
            this.user.name = ''
            this.user.phone = ''
            this.user.email = ''
          }
        }
      }).catch((err)=>{
        console.log(err)
      })
    },
  }
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