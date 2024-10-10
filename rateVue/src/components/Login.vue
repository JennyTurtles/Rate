<template>
  <div>
    <el-row>
      <el-col :span="12" v-if="loginForm.role !== 'admin'">
        <div>
          <el-form style="margin-left: 200px"
                   class="loginContainer">
            <h3 class="loginTitle">登录提示</h3>
            <el-form-item>
              <div class="TextContainer">
                <p style="color: red">若拥有东华大学学号/工号，可点击以下按钮采用东华大学统一认证方式登录,若遇到登录问题，请联系管理员。</p>
              </div>
            </el-form-item>
            <div class="footer">
              <div style="margin-top: 15px">
                <el-button size="normal"
                           type="primary"
                           style="width: 100%;"
                           @click="loginWithDHU">使用东华大学统一认证登录</el-button>
<!--                <div v-if="responseHtml" v-html="responseHtml"></div>-->
              </div>
            </div>
          </el-form>
        </div>
      </el-col>
      <el-col :span="loginForm.role !== 'admin' ? 12 : 24">
        <div style="display: flex; justify-content: center; align-items: center; height: 100%;">
          <el-form @submit.native.prevent
                   :rules="rules"
                   ref="loginForm"
                   v-loading="loading"
                   element-loading-text="正在登录..."
                   element-loading-spinner="el-icon-loading"
                   element-loading-background="rgba(0, 0, 0, 0.8)"
                   :model="loginForm"
                   :style="{ marginLeft: loginForm.role === 'admin' ? '' : '70px' }"
                   class="loginContainer">
            <h3 class="loginTitle">{{ titleName }}</h3>
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
            <div class="setPassword" v-show="loginForm.role !== 'admin'">
              <span @click="forgetPassword">忘记密码?</span>
            </div>
            <div class="footer">
              <div>
                <el-button size="normal"
                           type="primary"
                           style="width: 100%;"
                           native-type="submit"
                           @click="submitLogin">登录</el-button>
              </div>
              <div style="margin-top: 15px" v-show="loginForm.role !== 'admin'">
                <el-button size="normal"
                           type="primary"
                           style="width: 100%;"
                           native-type="submit"
                           @click="submitRegist">注册</el-button>
              </div>
            </div>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import store from '../store'
import router from '../router.js'
import sha1 from "sha1";
import { initMenu } from "@/utils/menus";
export default {
  name: "Login",
  props:["inf"], // 接受父组的loginForm，通过role判断角色
  data () {
    return {
      timer:null,
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
    this.loginForm = this.inf
    window.handleCasResponse = this.handleCasResponse;
    // 获取URL中的参数
    const urlParams = new URLSearchParams(window.location.search);
    const casResponse = urlParams.get('casResponse');

    if (casResponse) {
      try {
        // 解析JSON数据
        const responseData = JSON.parse(decodeURIComponent(casResponse));

        this.handleCasResponse(responseData);
      } catch (error) {
        console.error("解析CAS响应数据时出错:", error);
      }
    }
  },
  computed: {
    titleName(){
      switch (this.loginForm.role){
        case "admin": return "管理员登录";
        case "teacher": return "教师登录";
        case "student": return "学生登录";
      }
    }
  },
  methods: {
    handleCasResponse(data) {
      console.log("handleCasResponse 被调用", data);
      // 清空地址栏中的参数
      window.history.replaceState({}, document.title, "/");

      if (data.status === "success") {
        if (data.obj === null){
          this.$message.error("统一认证登录失败，请先注册账号，再进行角色注册，然后才可进行统一认证登录!");
        }
        // if (data.obj.isCasLogin === true) {
        //   this.$message.warning("您未在系统中注册，已将您的用户名和密码都设置为学号/工号，请尽快修改密码！");
        // }
        this.$store.commit('INIT_CURRENTHR', data.obj);
        localStorage.setItem("user", JSON.stringify(data.obj));

        initMenu(router,store).then(()=>{
          this.$router.push({
            path: "/home",
          });
        })
      }else {
        this.$message.error("登录失败！");
      }
    },

    loginWithDHU() {
      sessionStorage.clear();
      localStorage.clear();

      // 创建表单元素
      const form = document.createElement('form');
      form.method = 'GET';
      form.action = 'https://cas.dhu.edu.cn/authserver/login';

      // 创建隐藏输入元素用于service参数
      const serviceInput = document.createElement('input');
      serviceInput.type = 'hidden';
      serviceInput.name = 'service';
      serviceInput.value = 'http://up.dhu.edu.cn/cas/casLogin?callback=handleCasResponse';
      form.appendChild(serviceInput);


      // 将表单添加到body并提交
      document.body.appendChild(form);
      form.submit();
    },
    forgetPassword(){//忘记密码选项
      this.$router.push({
        path:'/ResetPassword',
        query:{
          key:this.loginForm.role//把角色传递过去
        }
      })
    },
    //只有学生和teacher才有注册功能 专家不注册
    submitRegist(){
      //因为老师注册和学生注册有些地方不一样所以先弄成两个组件
      if(this.loginForm.role == 'student'){
        this.$router.replace({
          path:'/Student/Register'
        })
      }else if(this.loginForm.role == 'teacher'){
        this.$router.replace({
          path:'/Teacher/Register'
        })
      }
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
.footer{
  margin-top: 8px;
  text-align: center;
}
.setPassword span:hover{
  color: #4b8ffe;
}
.setPassword{
  cursor: pointer;
  margin-top: -10px;
  margin-bottom: 5px;
  font-size: 12px;
  color: gray;
  text-decoration: none;
}
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
.TextContainer {
  border-radius: 10px;
  background-clip: padding-box;
  margin: 5px auto 5px 8px;
  width: 300px;
  padding: 15px 35px 15px 35px;
  border: 1px solid #5d6464;
  box-shadow: 0 0 5px #cac6c6;
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
