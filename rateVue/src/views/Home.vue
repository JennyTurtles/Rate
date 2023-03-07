<template>
  <div>
    <el-container ref="homePage">
      <el-header class="homeHeader">
        <div class="title">教学辅助系统</div>
        <div>
          <el-dropdown class="userInfo" @command="commandHandler">
            <span class="el-dropdown-link"> 你好，{{ user.name }}<i></i> </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
              <!--                            <el-dropdown-item command="setting">设置</el-dropdown-item>-->
              <el-dropdown-item command="logout" divided
              >注销登录</el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container class="homeContainer">
        <el-aside width="15%" class="aside">
          <el-menu router unique-opened class="menu" @open="handleOpen">
            <!-- <template> -->
            <template v-for="(item,index) in routes" v-if="item.enabled && item.name != '隐藏显示'">
              <el-menu-item :index="item.path" v-if="!item.children && item.enabled" :key="index" @click="handleOpen">
                {{item.name}}
              </el-menu-item>
              <template v-else-if="item.children && item.enabled">
                <el-submenu :index="index+''" :key="index" @open="handleOpen">
                  <template slot="title">
                    <i
                        style="color: #409eff; margin-right: 5px"
                        :class="item.iconCls"></i>
                    <span >{{ item.name }}</span>
                  </template>
                  <!-- 第二层 -->
                  <template v-for="(subItem,indexSub) in item.children" v-if="subItem.enabled">
                    <!-- 如果第二层有子菜单，则继续循环 -->
                    <el-menu-item :index="subItem.path" v-if="!subItem.children && subItem.enabled" :key="indexSub" class="submenu" @click="handleOpen">
                      {{subItem.name}}
                    </el-menu-item>
                    <template v-else-if="subItem.enabled && subItem.children">
                      <el-submenu :index="subItem.name" :key="indexSub" class="submenu" @open="handleOpen">
                        <template slot="title">
                          <i
                              style="color: #409eff; margin-right: 5px"
                              :class="subItem.iconCls"></i>
                          <span slot="title">{{ subItem.name }}</span>
                        </template>
                        <template v-for="(subItem2,indexsub2) in subItem.children" v-if="subItem2.enabled">
                          <!-- 如果第三层有子菜单，则继续循环 -->
                          <el-menu-item :index="subItem2.path" v-if="!subItem2.children && subItem2.enabled" :key="indexsub2" @click="handleOpen"
                                        class="submenu">
                            {{subItem2.name}}
                          </el-menu-item>
                          <template v-else-if="subItem2.enable && subItem2.children">
                            <el-submenu :index="subItem2.path" :key="indexsub2" class="submenu" @open="handleOpen">
                              <template slot="title">
                                <i
                                    style="color: #409eff; margin-right: 5px"
                                    :class="subItem2.iconCls"></i>
                                <span slot="title">{{ subItem2.name }}</span>
                              </template>
                            </el-submenu>
                          </template>
                        </template>
                      </el-submenu>
                    </template>
                  </template>
                </el-submenu>
              </template>
            </template>
            <!-- </template> -->

          </el-menu>
        </el-aside>
        <el-main>

          <div
              class="homeWelcome"
              v-if="this.$router.currentRoute.path == '/home'"
          >
            {{ user.name }}欢迎来到评分管理系统!
          </div>
          <router-view class="homeRouterView" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "Home",
  data() {
    return {
      routes:[],
      role:'',
      name:"",
      //获取页面高度
      clientHeight: "",
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem("user"))
    },
  },
  mounted() {
    this.routes = JSON.parse(sessionStorage.getItem('initRoutes'))
    console.log(this.routes)
    // 获取浏览器可视区域高度
    this.clientHeight = `${document.documentElement.clientHeight}`;
    this.role = JSON.parse(localStorage.getItem("user")).role
    this.name = JSON.parse(localStorage.getItem("user")).name
    window.onresize = function temp() {
      this.clientHeight = `${document.documentElement.clientHeight}`;
    };
  },
  watch: {
    // 如果 `clientHeight` 发生改变，这个函数就会运行
    clientHeight: function () {
      this.changeFixed(this.clientHeight);
    },
  },
  methods: {
    handleOpen() {
      if (this.role != JSON.parse(localStorage.getItem("user")).role || this.name != JSON.parse(localStorage.getItem("user")).name) {
        var url
        Message.warning('无权限！请重新登录')
        if (this.role == '8' || this.role == '9')
          url = "/Teacher/Login"
        else if (this.role == '1')
          url = "/Admin/Login"
        else
          url = "/"
        this.$router.replace(url);
      }
    },
    changeFixed(clientHeight) {
      //动态修改样式
      this.$refs.homePage.$el.style.height = clientHeight - 20 + "px";
    },
    commandHandler(cmd) {
      if (cmd == "logout") {
        this.$confirm("此操作将注销登录, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
              var url = '/'
              // console.log(this.role)
              // if (this.role == '8' || this.role == '9')
              //   url = "/Teacher/Login"
              // else if (this.role == 1)
              //   url = "/Admin/Login"
              // else if (this.role == 7)
              //   url = "/"
              this.getRequest("/logout");
              // window.sessionStorage.removeItem("user"); //清楚session
              localStorage.clear('user')
              sessionStorage.clear('initRoutes')
              sessionStorage.clear('initRoutes_AllSameForm')
          this.$store.commit("initRoutes", []); //清空路由
              this.$router.replace(url);
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消操作",
              });
            });
      } else if (cmd == "userinfo") {
        this.$router.push("/hrinfo");
      }
    },
  },
};
</script>

<style>
.homeRouterView {
  margin-top: 10px;
}

.homeWelcome {
  text-align: center;
  font-size: 30px;
  font-family: 华文行楷;
  color: #409eff;
  padding-top: 200px;
  /* padding-bottom: 600px; */
}

.homeHeader {
  /* background-color:#409eff; */
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0px 15px;
  box-sizing: border-box;
  background-image: url(../assets/Navigation_bar.png);
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

.el-main {
  background-color: #f4f4f5;
  color: #333;
  border-radius: 10px;
  margin-left: 0.25%;
  box-shadow: 15px 0 15px -15px rgb(119, 117, 117),
  /* 0px 15px 15px -15px rgb(119, 117, 117), */
  /* 0px 15px 15px -15px rgb(119, 117, 117); */
}
.el-aside {
  background-color: #ecf5ff;
  color: #333;
  /* /* margin-left: 0.5%; */
  border-radius: 10px;
  margin-right: 0.25%;
  box-shadow: 15px 0 15px -15px rgb(117, 119, 119),
  -15px 0 15px -15px rgb(119, 117, 117);
}
</style>
