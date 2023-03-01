<template>
  <div>
    <el-container ref="homePage">
      <el-header class="homeHeader">
        <div class="title">评分管理系统</div>
        <div>
          <el-dropdown class="userInfo" @command="commandHandler">
            <span class="el-dropdown-link"
              >你好，{{ teacher.name }}<i></i
            ></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="logout" divided
                >注销登录</el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="homeMain">
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "peract",
  data(){
    return{
      teacher:"",
    }
  },
  created() {
    this.teacher = JSON.parse(localStorage.getItem('teacher'))
  },
  mounted(){
  },
  methods: {
    commandHandler(cmd) {
      if (cmd == "logout") {
        this.$confirm("此操作将注销登录, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.getRequest("/logout");
            sessionStorage.clear(); //清session
            this.$router.replace("/Expert/Login");
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
      }
    },
  },
};
</script>
<style>
  .homeRouterView {
    margin-top: 10px;
  }
  
  .homeHeader {
    /* background-color:#409eff; */
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0px 15px;
    box-sizing: border-box;
    background-color: #409eff;
    /*background-image: */
  }
  
  .homeHeader .title {
    font-size: 30px;
    font-family: 华文行楷;
    color: #ffffff;
  
  }
  
  .homeHeader .userInfo {
    cursor: pointer;
  }
  
  .homeContainer .aside {
    background-color: #ecf5ff;
  }
  .homeContainer .aside .menu {
    background-color: #ecf5ff;
  }
  .homeContainer .aside .menu .submenu {
    background-color: #ecf5ff;
  }
  
  .el-dropdown-link img {
    width: 48px;
    height: 48px;
    border-radius: 24px;
    margin-left: 8px;
  }
  
  .el-dropdown-link {
    display: flex;
    align-items: center;
    color: white;
  }
  
  .homeMain{
    background-color: #f4f4f5;
    color: #333;
    border-radius: 15px;
    margin: 8px;
    box-shadow: 10px 0 10px -10px rgb(119, 117, 117),
      0px 10px 10px -10px rgb(119, 117, 117),
      0px 10px 10px -10px rgb(119, 117, 117);
  }
  </style>
  