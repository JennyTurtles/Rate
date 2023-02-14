<template>
  <div>
    <el-form :rules="rules"
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
      <!-- <el-form-item prop="code">
                <el-input size="normal" type="text" v-model="loginForm.code" auto-complete="off"
                          placeholder="点击图片更换验证码" @keydown.enter.native="submitLogin" style="width: 250px"></el-input>
                <img :src="vcUrl" @click="updateVerifyCode" alt="" style="cursor: pointer">
            </el-form-item> -->
      <!-- <el-checkbox size="normal" class="loginRemember" v-model="checked">记住状态</el-checkbox> -->
      <el-button size="normal"
                 type="primary"
                 style="width: 100%;"
                 @click="submitLogin">登录</el-button>
    </el-form>
  </div>
</template>

<script>

import sha1 from "sha1";

export default {
  name: "Login",
  props:["inf"], // 接受父组的loginForm，通过role判断角色
  data () {
    return {
      loading: false,
      // vcUrl: '/verifyCode?time='+new Date(),
      loginForm: {},
      checked: true,
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        // code: [{required: true, message: '请输入验证码', trigger: 'blur'}]
      },
      publicKey:''
    }
  },
  mounted () {
    // // 向后端获取公钥
    // this.getRequest('/getPublicKey').then(resp => {
    //   this.publicKey = resp.obj
    // })
    this.loginForm = this.inf
  },
  methods: {
    submitLogin () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {//判断是否符合prop要求
          this.loading = true;
          // 发送加密后的密码
          var loginFormPost = {username: this.loginForm.username, password: sha1(this.loginForm.password),role: this.loginForm.role}
          this.postRequest('/doLogin', loginFormPost).then(resp => {
            this.loading = false;
            if (resp) {
              this.$store.commit('INIT_CURRENTHR', resp.obj);//objet.set2
              localStorage.setItem("user", JSON.stringify(resp.obj));//存session
              let path = this.$route.query.redirect;
              this.$router.replace('/home').catch(err => err);
            } else {
              // this.vcUrl = '/verifyCode?time='+new Date();
            }
          })
        } else {
          return false;
        }
      });
    },
    // // // 放入公钥，构建加密器
    // encrypt(obj){
    //   let encrypt = new JsEncrypt()
    //   encrypt.setPublicKey(this.publicKey)
    //   return encrypt.encrypt(obj)
    // }
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
