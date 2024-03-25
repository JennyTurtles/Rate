<template>
  <div>
    <el-container ref="homePage">
      <el-header class="homeHeader">
        <div class="title">教学辅助系统</div>
        <div>
          <el-dropdown class="userInfo" @command="commandHandler">
            <span class="el-dropdown-link">您好，{{ user.name }}<a
                v-show="
                  (user.role.indexOf('3') !== -1 && user.role.indexOf('13') == -1) ||
                  (user.role.indexOf('4') !== -1 && user.role.indexOf('14') == -1) ||
                  user.role.indexOf('8') !== -1 ||
                  user.role.indexOf('9') !== -1
                "
            >老师</a> <i class="el-icon-arrow-down el-icon--right"></i></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-show="
                  (user.role == '' || user.role.indexOf('7') !== -1  || user.role.indexOf('11') !== -1 ||
                   user.role.indexOf('10') !== -1)"
              >
                <el-dropdown @command="registerHandler">
                  <span>注册为大学生</span>
                  <el-dropdown-menu slot="dropdown" class="el-dropdown-back">
                    <el-dropdown-item command="本科生" v-show="!user.role.includes('10')">本科生</el-dropdown-item>
                    <el-dropdown-item command="硕士研究生" v-show="!role.includes('11')">硕士研究生</el-dropdown-item>
                    <el-dropdown-item command="博士研究生" v-show="!role.includes('17')">博士研究生</el-dropdown-item>
                    <el-dropdown-item command="活动选手">活动选手</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </el-dropdown-item>
              <el-dropdown-item command="changePassword"
              >修改密码
              </el-dropdown-item
              >
              <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="logout" divided
              >注销登录
              </el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container class="homeContainer">
        <el-aside width="15%" class="aside">
          <div v-show="!isStudentRole"
               style="padding-left: 25px; font-size: 14px; line-height: 56px; font-weight: 500;cursor: pointer">
            <a @click="pendingMessageRoute">
              <span>
                待办任务（
              </span>
              <span :style="$store.state.pendingMessageTotal > 0 ? {'color' : 'red'} : {'color' : 'black'}">
                 {{ $store.state.pendingMessageTotal }}
              </span>
              <span>
                 ）
              </span>
            </a>
          </div>
          <el-menu router unique-opened class="menu" @open="handleOpen">
            <!-- <template> -->
            <template
                v-for="(item, index) in routes"
                v-if="item.enabled && item.name != '隐藏显示'"
            >
              <el-menu-item
                  :index="item.path"
                  v-if="!item.children && item.enabled"
                  :key="index"
                  @click="handleOpen"
              >
                {{ item.name }}
              </el-menu-item>
              <template v-else-if="item.children && item.enabled">
                <el-submenu :index="index + ''" :key="index" @open="handleOpen">
                  <template slot="title">
                    <i
                        style="color: #409eff; margin-right: 5px"
                        :class="item.iconCls"
                    ></i>
                    <span>{{ item.name }}</span>
                  </template>
                  <!-- 第二层 -->
                  <template
                      v-for="(subItem, indexSub) in item.children"
                      v-if="subItem.enabled"
                  >
                    <!-- 如果第二层有子菜单，则继续循环 -->
                    <el-menu-item
                        :index="subItem.path"
                        v-if="!subItem.children && subItem.enabled"
                        :key="indexSub"
                        class="submenu"
                        @click="handleOpen(subItem)"
                    >
                      {{ subItem.name }}
                    </el-menu-item>
                    <template v-else-if="subItem.enabled && subItem.children">
                      <el-submenu
                          :index="subItem.name"
                          :key="indexSub"
                          class="submenu"
                          @open="handleOpen"
                      >
                        <template slot="title">
                          <i
                              style="color: #409eff; margin-right: 5px"
                              :class="subItem.iconCls"
                          ></i>
                          <span slot="title">{{ subItem.name }}</span>
                        </template>
                        <template
                            v-for="(subItem2, indexsub2) in subItem.children"
                            v-if="subItem2.enabled"
                        >
                          <!-- 如果第三层有子菜单，则继续循环 -->
                          <el-menu-item
                              :index="subItem2.path"
                              v-if="!subItem2.children && subItem2.enabled"
                              :key="indexsub2"
                              @click="handleOpen"
                              class="submenu"
                          >
                            {{ subItem2.name }}
                          </el-menu-item>
                          <template
                              v-else-if="subItem2.enable && subItem2.children"
                          >
                            <el-submenu
                                :index="subItem2.path"
                                :key="indexsub2"
                                class="submenu"
                                @open="handleOpen"
                            >
                              <template slot="title">
                                <i
                                    style="color: #409eff; margin-right: 5px"
                                    :class="subItem2.iconCls"
                                ></i>
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
          <div v-show="roleName.indexOf('admin') >= 0"
               style="padding-left: 25px; font-size: 14px; line-height: 56px; font-weight: 500;cursor: pointer">
            <a @click="showException">
              <span>异常信息查看</span>
            </a>
          </div>
        </el-aside>
        <el-main>
          <div
              class="homeWelcome"
              v-if="this.$router.currentRoute.path == '/home'"
          >
            {{ user.name }}欢迎来到教学辅助系统!
          </div>
          <div
              style="color: red; text-align: center"
              v-if="this.$router.currentRoute.path == '/home' && user.role === ''"
          >
            <i class="el-icon-warning"></i>请注意：您尚未注册任何角色，无法进行任何操作。<br>
            请尽快在右上角的下拉菜单中选择”注册为大学生“，再根据需要进行对应角色的注册
          </div>
          <router-view class="homeRouterView"/>
        </el-main>
      </el-container>
    </el-container>
    <!--    一个对话框，让用户输入密码，并确认密码，输入的内容用*表示，"新密码"和"确认密码"靠左对齐-->
    <el-dialog title="修改密码" :visible.sync="showPassword" width="30%">
      <el-form :model="password" label-width="80px">
        <el-form-item label="旧密码">
          <el-input
              type="password"
              v-model="oldPassword"
              placeholder="请输入旧密码"
              @blur="checkOldPassword"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input
              type="password"
              v-model="password"
              placeholder="请输入新密码"
              @blur="checkPassword"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input
              type="password"
              v-model="password_confirm"
              placeholder="请再次输入新密码"
              :disabled="!checkPwdState"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showPassword = false">取 消</el-button>
        <el-button type="primary" @click="submitPassword">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog @close="registerRoleForm={};selectStuType='';tutorName='';selectActivity='';selectInstitution=''" :title="registerTitle()"
               :visible.sync="registerRoleVisible" width="30%">
      <el-form label-width="auto">
        <!--    <el-form-item label="姓名:">-->
        <!--     <el-input v-model="registerRoleForm.name"></el-input>-->
        <!--    </el-form-item>-->
        <el-form-item label="电话:">
          <el-input v-model="registerRoleForm.telephone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱:">
          <el-input v-model="registerRoleForm.email"></el-input>
        </el-form-item>
        <el-form-item label="单位:">
          <el-autocomplete
              style="width: 100%"
              v-model="selectInstitution"
              :fetch-suggestions="querySearchInstitution"
              @select="handleSelectInstitution"
              :trigger-on-focus="false"
              value-key="company"
          >
          </el-autocomplete>
        </el-form-item>
        <el-form-item label="学生类型:">
          <el-input v-model="selectStuType" disabled></el-input>
          <!--     <el-select v-model="selectStuType" clearable>-->
          <!--      <el-option-->
          <!--          v-for="val in stuType"-->
          <!--          :value="val"-->
          <!--          :label="val"-->
          <!--          :key="val">-->
          <!--      </el-option>-->
          <!--     </el-select>-->
        </el-form-item>
        <el-form-item v-show="selectStuType === '硕士研究生' || selectStuType === '博士研究生'" label="指导老师:">
          <el-autocomplete
              style="width: 100%"
              v-model="tutorName"
              :fetch-suggestions="querySearchAsync"
              @select="handleSelectTutor">
          </el-autocomplete>
        </el-form-item>
        <div v-show="selectStuType !== '活动选手'">
          <el-form-item label="学号:">
            <el-input v-model="registerRoleForm.studentnumber"></el-input>
          </el-form-item>
          <el-form-item label="入学年份:">
            <el-input v-model="registerRoleForm.year"></el-input>
          </el-form-item>
        </div>
        <div v-show="selectStuType === '活动选手'">
          <el-form-item label="活动名称:">
            <el-autocomplete
                style="width: 100%"
                v-model="selectActivity"
                :fetch-suggestions="querySearchAct"
                @select="handleSelectAct">
            </el-autocomplete>
          </el-form-item>
          <el-form-item label="编号:">
            <el-input v-model="registerRoleForm.studentnumber"></el-input>
          </el-form-item>
        </div>
        <div v-show="selectStuType === '硕士研究生'">
          <el-form-item label="研究生类型:">
            <el-select v-model="registerRoleForm.gradType">
              <el-option v-for="val in ['专硕','学硕']"
                         :value="val"
                         :label="val"
                         :key="val">
              </el-option>
            </el-select>
          </el-form-item>
        </div>
        <div v-show="selectStuType === '博士研究生'">
          <el-form-item label="研究生类型:">
            <el-select v-model="registerRoleForm.gradType">
              <el-option v-for="val in ['专博','学博']"
                         :value="val"
                         :label="val"
                         :key="val">
              </el-option>
            </el-select>
          </el-form-item>
        </div>
        <div style="text-align: center">
          <el-button @click="registerRole" type="primary">注册</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";
import sha1 from "sha1";

export default {
  name: "Home",
  data: function () {
    return {
      checkPwdState: false,
      checkOldPwdState: false,
      isStudentRole: false, //判断当前角色是否是学生中的四个角色之一，后续判断是否发送待办消息的请求和页面显示
      tutorName: '',
      stuType: ['本科生', '硕士研究生', '博士研究生'],
      selectStuType: '',
      selectActivity: '',
      selectActivityID: '',
      selectInstitution: '',
      registerRoleForm: {},
      registerRoleVisible: false,
      routes: [],
      role: "",
      roleName: '',
      name: "",
      //获取页面高度
      clientHeight: "",
      showPassword: false,
      password: "",
      oldPassword: "",
      password_confirm: "",
      register_select: "",
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem("user"));
    },
  },
  mounted() {
    this.routes = JSON.parse(sessionStorage.getItem("initRoutes"));
    // 获取浏览器可视区域高度
    this.clientHeight = `${document.documentElement.clientHeight}`;
    this.role = JSON.parse(localStorage.getItem("user")).role;
    this.roleName = JSON.parse(localStorage.getItem("user")).roleName; //后来优化返回的一个字符串角色，不用固定数字(role id)代替了
    this.name = JSON.parse(localStorage.getItem("user")).name;
    window.onresize = function temp() {
      this.clientHeight = `${document.documentElement.clientHeight}`;
    };
    console.log(this.user)
    //如果是学生不显示待办消息，如果是专家并且角色只有专家（没有研究生导师等等的身份）就不显示待办消息
    if (this.roleName === '')
      this.isStudentRole = true;
    else if (this.roleName.indexOf('doctor') < 0 &&
        this.roleName.indexOf('undergraduate') < 0 &&
        this.roleName.indexOf('graduate') < 0 &&
        this.roleName.indexOf('participants') < 0 &&
        this.roleName !== 'expert' && this.roleName !== 'expert;') {
      this.isStudentRole = false;
    }
    else
      this.isStudentRole = true;

    if (!this.isStudentRole) {
      let roleParam = this.roleName.indexOf('admin') >= 0 ? 'admin' : this.roleName.indexOf('teacher') >= 0 ? 'teacher' : '';
      this.$store.dispatch('changePendingMessageange', roleParam); //不是学生才会发送请求
    }
    console.log(this.roleName)
  },
  watch: {
    // 如果 `clientHeight` 发生改变，这个函数就会运行
    clientHeight: function () {
      this.changeFixed(this.clientHeight);
    },
  },
  methods: {
    async checkOldPassword() {
      await this.getRequest(
          "/system/config/checkPassword?ID=" +
          this.user.id +
          "&password=" +
          sha1(this.oldPassword)
      ).then((res) => {
          this.checkOldPwdState = res.obj
      });
    },
    checkPassword() {
      const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[^a-zA-Z\d])[\S]{8,20}$/;
      if (!passwordRegex.test(this.password)) {
        this.checkPwdState = false
        this.$message.error('密码必须是8-20位，包含至少一个英文字符，一个数字和一个特殊字符(@$!%*?&)');
      } else {
        this.checkPwdState = true
      }

    },
    pendingMessageRoute() { //点击待办消息导航
      this.$router.push('/pending/message');
    },
    showException() { //点击异常信息查看
      this.$router.push('/Admin/ExceptionLog');
    },
    querySearchAsync(queryString, cb) {
      if (queryString.length < 1) {
        return cb([]);
      }
      this.getRequest('/system/teacher/searchByName?name=' + queryString)
          .then(response => {
            let results = response.obj;
            // 遍历result，把里面的name赋值给value
            results.forEach(result => {
              result.value = result.name;
            });
            // 调用回调函数返回搜索建议
            cb(results);
          })
    },
    querySearchAct(queryString, cb) {
      if (queryString.length < 1) {
        return cb([]);
      }
      this.getRequest('/activities/basic/searchByName?name=' + queryString)
          .then(response => {
            let results = response.obj;
            // 遍历result，把里面的name赋值给value
            results.forEach(result => {
              result.value = result.name;
            });
            // 调用回调函数返回搜索建议
            cb(results);
          })
    },
    querySearchInstitution(queryString, callback) {
      let results = []
      if (queryString.length == 0) {
        results.push(this.item);
        callback(results);
      }
      this.getRequest('/institution/basic/searchByName?name=' + queryString)
          .then((resp) => {
            if (resp) {
              results = resp.obj;
              if (results.length === 0) {
                this.item.company = '其他';
                results.push(this.item);
              }// 调用 callback 返回建议列表的数据
              callback(results);
            }
          })
    },
    handleSelectInstitution(item) {
      this.registerRoleForm.institutionID = item.id;
      this.selectInstitution = item.company;
    },
    handleSelectTutor(item) {
      this.registerRoleForm.tutorID = item.id
    },
    handleSelectAct(item) {
      this.selectActivity = item.name;
      this.selectActivityID = item.id;
    },
    registerRole() {
      this.registerRoleForm.ID = this.user.id
      this.registerRoleForm.name = this.user.name
      this.registerRoleForm.role = this.user.role
      this.registerRoleForm.username = this.user.username
      console.log(this.registerRoleForm)
      if (this.selectStuType === '硕士研究生') {
        //if (this.user.role.indexOf("11") == -1)
        this.registerRoleForm.role = this.registerRoleForm.role === "" ? this.user.role + "11" : this.user.role + ";11";
        this.postRequest1("/system/student/registerGraduate", this.registerRoleForm).then((res) => {
          if (res) {
            this.$message({
              type: "success",
              message: "注册成功,请重新登录!",
            });
            this.registerRoleVisible = false
          }
        });
      } else if (this.selectStuType === '本科生') {
        //if (this.user.role.indexOf("10") == -1)
        this.registerRoleForm.role = this.registerRoleForm.role === "" ? this.user.role + "10" : this.user.role + ";10";
        this.postRequest1("/system/student/registerUndergraduate", this.registerRoleForm).then((res) => {
          if (res) {
            this.$message({
              type: "success",
              message: "注册成功,请重新登录!",
            });
            this.registerRoleVisible = false
          }
        });
      } else if (this.selectStuType === '博士研究生') {
        //if (this.user.role.indexOf("17") == -1)
        this.registerRoleForm.role = this.registerRoleForm.role === "" ? this.user.role + "17" : this.user.role + ";17";
        this.postRequest1("/system/student/registerDoctor", this.registerRoleForm).then((res) => {
          if (res) {
            this.$message({
              type: "success",
              message: "注册成功,请重新登录!",
            });
            this.registerRoleVisible = false
          }
        });
      }else if (this.selectStuType === '活动选手') {
        console.log(this.selectActivityID)
        if (this.user.role.indexOf("7") == -1)
          this.registerRoleForm.role = this.registerRoleForm.role === "" ? this.user.role + "7" : this.user.role + ";7";
        this.postRequest1("/system/student/registerParticipant?activityID=" + this.selectActivityID, this.registerRoleForm).then((res) => {
          if (res) {
            this.$message({
              type: "success",
              message: "注册成功,请重新登录!",
            });
            this.registerRoleVisible = false
          }
        });
      }
    },
    registerTitle() {
      return "注册为" + this.selectStuType;
    },
    handleOpen(subItem) {
      if (
          this.role != JSON.parse(localStorage.getItem("user")).role ||
          this.name != JSON.parse(localStorage.getItem("user")).name
      ) {
        var url;
        Message.warning("无权限！请重新登录");
        if (this.role == "8" || this.role == "9") url = "/Teacher/Login";
        else if (this.role == "13" || this.role == "14" || this.role == "15" || this.role == "16") url = "/Admin/Login";
        else url = "/";
        this.$router.replace(url);
      }
      if (
          subItem.name == "活动列表" &&
          subItem.path == "/teacher/tperact/actList"
      ) {
        let routeUrl = this.$router.resolve({
          path: "/Expert/peract/actList",
        });
        window.open(routeUrl.href);
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
        })
            .then(() => {
              var url = "/";
              if (
                  this.role.indexOf("8") >=0 ||
                  this.role.indexOf("9") >=0 ||
                  this.role.indexOf("3") >=0 ||
                  this.role.indexOf("4") >=0
              )
                url = "/Teacher/Login";
              else if (this.role.indexOf("13") >= 0 || this.role.indexOf("14") >= 0 || this.role.indexOf("15") >= 0 || this.role.indexOf("16") >= 0) url = "/Admin/Login";
              else url = "/";
              this.getRequest("/system/config/logout").then(() => {
                if (localStorage.getItem("user")) {
                  localStorage.removeItem("user");
                }
                if (sessionStorage.removeItem("score")) {
                  sessionStorage.removeItem("score");
                }
                sessionStorage.removeItem("initRoutes");
                sessionStorage.removeItem("initRoutes_AllSameForm");
                this.$store.commit("initRoutes", []); //清空路由
                this.$store.commit("initRoutesAllSameForm", []); //清空路由
                this.$router.replace(url);
              });
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消操作",
              });
            });
      } else if (cmd == 'registerRole') {
        this.registerRoleVisible = true
      } else if (cmd == "changePassword") {
        this.showPassword = true;
      } else if (cmd == "userInfo") {
        if (this.user.role.indexOf("13") >= 0 || this.user.role.indexOf("14") >= 0 || this.user.role.indexOf("15") >= 0 || this.user.role.indexOf("16") >= 0) {
          this.$router.push({
            path: "/admin/PersonalCenter",
          });
        } //如果是专家，研究生导师 本科生导师去相同的个人中心界面
        else if (
            this.user.role.indexOf("3") >= 0 ||
            this.user.role.indexOf("8") >= 0 ||
            this.user.role.indexOf("9") >= 0
        ) {
          this.$router.push({
            path: "/teacher/PersonalCenter",
          });
        } else if (
            this.user.role.indexOf("10") >= 0 ||
            this.user.role.indexOf("11") >= 0 ||
            this.user.role.indexOf("7") >= 0
        ) {
          this.$router.push({
            path: "/student/PersonalCenter",
          });
        } else if (this.user.role == "6") {
          this.$router.push({
            path: "/superAdmin/PersonalCenter",
          });
        }
      }
    },
    submitPassword() {
      if (this.checkOldPwdState == false) {
        this.$message.error('旧密码输入错误，请重新输入');
        return;
      }
      if (this.password == "" || this.password_confirm == "") {
        this.$message({
          type: "warning",
          message: "密码不能为空!",
        });
        return;
      }
      if (this.checkPwdState == false) {
        this.$message.error('密码必须是8-20位，包含至少一个英文字符，一个数字和一个特殊字符(比如@$!%*?&)');
        return;
      }
      if (this.password !== this.password_confirm) {
        this.$message({
          type: "warning",
          message: "两次密码不一致!",
        });
        return;
      }
      this.postRequest(
          "/system/config/updatePassword?ID=" +
          this.user.id +
          "&password=" +
          sha1(this.password)
      ).then((res) => {
        if (res) {
          this.$message({
            type: "success",
            message: "修改成功!",
          });
          this.showPassword = false;
          this.oldPassword = "";
          this.password = "",
          this.password_confirm = ""
        }
      });
    },
    registerHandler(command) {
      this.selectStuType = command;
      this.registerRoleVisible = true;
    }
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

.el-form-item__label-wrap {
  margin-left: 0px !important;
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

.el-dropdown-back {
  background-color: rgb(220, 231, 251);
}

.el-main {
  background-color: #f4f4f5;
  color: #333;
  border-radius: 10px;
  margin-left: 0.25%;
  /*box-shadow: 15px 0 15px -15px rgb(119, 117, 117)*/
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
