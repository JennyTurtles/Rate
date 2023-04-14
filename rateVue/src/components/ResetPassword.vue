<template>
  <div>
    <el-form class="resetPassContainer" :label-width="labelWidth">
      <el-form-item label="请输入身份证号:">
        <el-input style="width: 60%"  v-model="idNumber"></el-input>
      </el-form-item>
      <el-form-item label="请输入密保问题:">
        <el-input style="width: 60%"  v-model="passQuestion"></el-input>
      </el-form-item>
      <el-form-item label="请输入密保答案:">
        <el-input style="width: 60%" v-model="passAnswer"></el-input>
      </el-form-item>
      <div v-show="resetPassShow">
        <el-form-item label="请输入新密码:">
          <el-input style="width: 60%" v-model="newPass" type="password"></el-input>
        </el-form-item>
        <el-form-item label="请确认新密码:">
          <el-input style="width: 60%" v-model="confirmNewPass" type="password"></el-input>
        </el-form-item>
        <div class="footer">
          <el-button @click="reset" type="primary">确认</el-button>
        </div>
      </div>
    </el-form>
  </div>
</template>

<script>
import {debounce} from "@/utils/debounce";
import {getRequest,postRequest} from "@/utils/api";

export default {
  name: "ResetPassword",
  data(){
    return{
      user:{

      },
      role:"",
      idNumber:'',
      passQuestion:'',
      passAnswer:'',
      resetPassShow:false,
      newPass:'',
      confirmNewPass:''
    }
  },
  mounted() {
    this.role = this.$route.query.key
  },
  watch:{
    passAnswer:{
      handler(){
        debounce(this.check(),400)
      }
    },
    idNumber:{
      handler(){
        debounce(this.checkIdNumber(),400)
      }
    },

  },
  computed:{
    labelWidth(){
      return `${10 * 17}px`
    }
  },
  methods:{
    check(){//密保答案失去焦点，进行判断
      if(this.passQuestion == '' || this.passQuestion == null){
        return
      }
      if(this.passAnswer == '' || this.passAnswer == null){
        return
      }
      if(this.idNumber == '' || this.idNumber == null){
        return
      }
      //密保问题正确
      if(this.passQuestion === this.user.registerQuestion && this.passAnswer === this.user.registerAnswer){
        this.resetPassShow = true
      }else {
        this.resetPassShow = false
      }
    },
    checkIdNumber(){
      let url = '/registerUser/getUserByIdNumber?role=' + this.role + '&idNumber=' + this.idNumber
      this.getRequest(url).then((response)=>{
        if (response){
          if(response.status == 200){
            this.user = response.obj
          }
        }
      })

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