<template>
  <div>
    <Login :inf="loginForm"></Login>
  </div>
</template>
<script>
import Login from "@/components/Login"
export default {
  name: "AdminLogin",
  components:{
    'Login':Login,
  },
  data() {
    return {
      loginForm: {
        username: 'admin',
        password: '123456',
        role: 'admin'
      },
    };
  },
  methods:{

  }
};
</script>