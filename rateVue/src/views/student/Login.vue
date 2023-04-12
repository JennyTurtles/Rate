<template>
  <div>
    <el-form @submit.native.prevent
             :rules="rules"
             ref="loginForm"
             v-loading="loading"
             element-loading-text="正在登录..."
             element-loading-spinner="el-icon-loading"
             element-loading-background="rgba(0, 0, 0, 0.8)"
             :model="loginForm"
             class="loginContainer">
      <h3 class="loginTitle">系统登录</h3>
      <el-form-item prop="username">
        <el-input size="normal"
                  type="text"
                  v-model="loginForm.username"
                  auto-complete="off"
                  placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input size="normal"
                  type="password"
                  v-model="loginForm.password"
                  auto-complete="off"
                  placeholder="请输入密码"></el-input>
      </el-form-item>
      <div>
        <el-button size="normal"
                   type="primary"
                   style="width: 100%;"
                   native-type="submit"
                   @click="submitLogin">登录</el-button>
      </div>
      <div>
        <el-button size="normal"
                   type="primary"
                   style="width: 100%;margin-top: 10px"
                   native-type="submit"
                   @click="submitRegist">注册</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import store from '../../store'
import router from '../../router.js'
import sha1 from "sha1";
import { initMenu } from "../../utils/menus.js";
import this_ from "@/main";
export default {
  name: "StuLogin",
  data () {
    return {
      timer:null,
      loading: false,
      loginForm: {
        username:'student',
        password: '123456',
        role: 'student'
      },
      checked: true,
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
      },
      publicKey:''
    }
  },
  methods: {
    submitRegist(){
      this.$router.push({
        path:'/Student/Register'
      })
    },
    submitLogin () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {//判断是否符合prop要求
          this.loading = true;
          sessionStorage.clear();
          localStorage.clear()
          // 发送加密后的密码
          var that = this
          var loginFormPost = {username: this.loginForm.username, password: sha1(this.loginForm.password),role: this.loginForm.role}
          this.postRequest('/doLogin', loginFormPost).then(resp => {
            this.loading = false;
            if (resp) {
              this.$store.commit('INIT_CURRENTHR', resp.obj);//objet.set2
              localStorage.setItem("user", JSON.stringify(resp.obj));//存session
              let path = this.$route.query.redirect;
              //保设置新登录账号的路由，个人信息在浏览器和vuex
              initMenu(router,store).then(()=>{
                this.$router.push({
                  path: "/home",
                });
              })
            }
          })
        } else {
          return false;
        }
      });
    },

    destroyed() {
      window.onkeydown = undefined;
    },
  }
}
</script>

<style>
.loginContainer {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 15px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.loginTitle {
  margin: 15px auto 20px auto;
  text-align: center;
  color: #505458;
}

.loginRemember {
  text-align: left;
  margin: 0px 0px 15px 0px;
}
.el-form-item__content {
  display: flex;
  align-items: center;
}
</style>
