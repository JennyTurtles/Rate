<template>
  <div id="app">
     <router-view v-if="isRouterAlive"/>
  </div>
</template>
<script>
export default {
  provide(){
    return{
      reload:this.reload
    }
  },
  data(){
    return{
      isRouterAlive:true
    }
  },
  methods:{
    reload(){
      this.isRouterAlive=false;
      this.$nextTick(()=>{
        this.isRouterAlive=true
      });
    }
  },
  created(){
  },
  mounted(){
    let user = JSON.parse(localStorage.getItem("user"))
    if(user == null){
        // this.$message.error('请重新登录！')
        this.$router.push('/')
    }

  },
}
</script>
