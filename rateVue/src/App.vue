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
    //页面加载时读取session
    // if(sessionStorage.getItem('initRoutes')){
    //   this.$store.replaceState(Object.assign({},this.$store.state.routes,JSON.parse(sessionStorage.getItem('initRoutes'))))
    //   this.$store.replaceState(Object.assign({},this.$store.state.routesAllSameForm,JSON.parse(sessionStorage.getItem('initRoutes_AllSameForm'))))
    //
    // }
    // // //页面刷新数据加载到浏览器
    // window.addEventListener('beforeunload',() => {
    //   sessionStorage.setItem('initRoutes',JSON.stringify(this.$store.state.routes))
    //   sessionStorage.setItem('initRoutes_AllSameForm',JSON.stringify(this.$store.state.routesAllSameForm))
    // })
  },
  mounted(){
    // let user = JSON.parse(localStorage.getItem("user"))
    // if(user == null){
    //     // this.$message.error('请重新登录！')
    //     this.$router.push('/')
    // }

  },
}
</script>
