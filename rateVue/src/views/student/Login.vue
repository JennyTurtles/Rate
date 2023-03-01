<template>
  <div>
    <Login :inf="loginForm"></Login>
  </div>
</template>
<script>
import Login from "@/components/Login"
export default {
  name: "StuLogin",
  components:{
    'Login':Login,
  },
  data() {
    return {
      loginForm: {
        username: 'student',
        password: '123456',
        role: 'student'
      },
    };
  },
  methods:{

  }
};
</script>