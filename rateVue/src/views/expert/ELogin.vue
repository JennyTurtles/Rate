<template>
  <div>
    <div class="expertLogin">
      <div class="expertLoginTxt">
        <img src="../../assets/dzlogo.png" alt="东华大学" class="imageLogo" />
        <h1>我们一起建设更美好的世界！</h1>
      </div>
      <el-form
        :rules="rules"
        ref="loginForm"
        v-loading="loading"
        element-loading-text="正在登录..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 0.8)"
        :model="loginForm"
        class="loginContainer"
        label-position="top"
      >
        <div class="loginTitle">身份认证</div>
        <div class="loginDivider"></div>
        <el-form-item prop="username">
          <span slot="label">
            <i class="el-icon-user"> 用户名</i>
          </span>
          <el-input
            type="text"
            v-model="loginForm.username"
            auto-complete="off"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <span slot="label">
            <i class="el-icon-lock"> 密码</i>
          </span>
          <el-input
            type="password"
            v-model="loginForm.password"
            auto-complete="off"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-button type="primary" class="loginBtn" @click="submitLogin"
          >登录</el-button
        >
      </el-form>
    </div>
  </div>
</template>

<script>
import { postRequest, getRequest } from "@/utils/api";
import sha1 from "sha1";
export default {
  name: "ExpertLogin",
  data() {
    return {
      loading: false,
      loginForm: {
        username: "",
        password: "",
        role: "teacher",
      },
      checked: true,
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
      loginResult: {},
    };
  },
  mounted() {
    sessionStorage.clear();
  },
  methods: {
    submitLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.loading = true;
          this.loginForm.password = sha1(this.loginForm.password)
          this.loginResult = await postRequest("/doLogin", this.loginForm);

          if (this.loginResult.status === 200) {
            // let user = JSON.parse(window.sessionStorage.getItem("teacher"));
            await this.$store.dispatch("initsize", this.loginResult.obj.id); //需要去掉
            // let path = this.$route.query.redirect;
            // if (this.$store.state.peract.count == 0) {
            //   this.$router.replace(
            //     path == "/Expert/Login" || path == undefined? "/Expert/peract/warn": path
            //   );
            // } else {
            //   this.$router.replace(path == "/Expert/Login" || path == undefined? "/Expert/peract/actList": path
            //   );
            // }
            // this.$router.replace(
            //   path == "/Expert/peract" || path == undefined? "/Expert/peract/warn": path
            // );
            this.initActivityList(this.loginResult.obj);

            this.$store.commit("INIT_CURRENTHR", this.loginResult.obj); //需要去掉
            // console.log(resp)
            localStorage.setItem(
              "teacher",
              JSON.stringify(this.loginResult.obj)
            ); //存用户session
          } else {
            this.$message({
              message: this.loginResult.msg,
              type: "error",
            });
          }
        }
      });
    },

    // 根据用户id请求活动列表
    async initActivityList(teacher) {
      const result = await getRequest(
        "/system/Experts/activities?expertID=" + teacher.id
      );
      if (result.code === 200) {
        this.loading = false;
        this.$message({
          message: this.loginResult.msg,
          type: "success",
        });
        if (result.extend.count !== 0) {
          // 存活动列表session
          sessionStorage.setItem(
            "activitiesList",
            JSON.stringify(result.extend.activitiesList)
          );
          this.$router.push({
            path: "/Expert/peract/actList",
          });
        } else {
          this.$router.push({
            path: "/Expert/peract/warn",
          });
        }
      } else {
        this.loading = false;
        this.$message({
          message: this.loginResult.msg,
          type: "error",
        });
      }
    },
  },
  //添加登录背景图片
  created() {
    document.body.classList.add("body-home");
  },
  beforeDestroy() {
    document.body.classList.remove("body-home");
  },
};
</script>

<style lang="scss">
.body-home {
  background-image: url("../../assets/dzmainbg.png");
}
.expertLogin {
  /* height: 600px; */
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 150px 100px;
}
.imageLogo {
  width: 330px;
  height: 120px;
  margin-bottom: 50px;
}
.expertLoginTxt {
  color: white;
}
.loginContainer {
  border-radius: 10px;
  background-clip: padding-box;
  width: 350px;
  padding: 15px 35px 15px 35px;
  background: #d6dbde;
  border: 1px solid #eaeaea;
}
.loginTitle {
  font-size: 20px;
  margin: 20px 10px 10px 0px;
  font-weight: bolder;
}
.loginDivider {
  width: 100%;
  height: 1px;
  background-color: gray;
  margin-bottom: 25px;
}
.loginBtn {
  width: 100%;
  margin: 20px 0 40px 0;
}
</style>
