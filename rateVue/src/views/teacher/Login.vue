<template>
  <div>
    <Login :inf=loginForm ></Login>
  </div>
</template>
<script>
import Login from "@/components/Login"
export default {
  name: "TeacherLogin",
  components:{
    'Login':Login,
  },
  data() {
    return {
      loginForm: {
        username: 'teacher',
        password: '123456',
        role: 'teacher'
      },
    };
  },
  methods:{

  }
};
</script>