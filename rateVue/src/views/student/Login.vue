<template>
  <div>
    <Login :inf="loginForm" ref="login"></Login>
  </div>
</template>

<script>
import Login from "@/components/Login"
export default {
  name: "StuLogin",
  components:{
    'Login':Login,
  },
  data () {
    return {
      loginForm: {
        username:'student',
        password: '123456',
        role: 'student'
      },
    }
  },
  created() {
    // window.addEventListener("keydown", this.handkeyCode, true);
    document.onkeypress = (e)=>{
      var keycode = document.all ? event.keyCode : e.which;
      if (keycode == 13) {
        this.$refs.login.submitLogin();// 登录方法名
        return false;
      }
    };
  },
}
</script>

<style>

</style>
