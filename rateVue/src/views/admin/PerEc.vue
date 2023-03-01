<template>
  <div>
    <div style="margin: 20px 0 20px 0px; display: flex; justify-content: left">
      <div>
        <el-input
            v-model="keywords"
            placeholder="通过单位名搜索用户..."
            prefix-icon="el-icon-search"
            style="width: 400px; margin-right: 10px"
            @keydown.enter.native="doSearch"
            :disabled="showAdvanceSearchView"
        ></el-input>
        <el-button
            icon="el-icon-search"
            type="primary"
            @click="doSearch"
            :disabled="showAdvanceSearchView"
        >搜索</el-button
        >
        <el-button
            type="primary"
            @click="showAdvanceSearchView = !showAdvanceSearchView"
        >
          <i
              :class="
              showAdvanceSearchView
                ? 'fa fa-angle-double-up'
                : 'fa fa-angle-double-down'
            "
              aria-hidden="true"
          ></i>
          高级搜索
        </el-button>

        <el-button icon="el-icon-refresh" type="primary" @click="reset">
          重置
        </el-button>
      </div>

      <div style="margin-left: auto">
        <el-button icon="el-icon-plus" type="primary" @click="showAddEmpView"
        >添加新管理员</el-button
        >
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>

    <transition name="slide-fade">
      <div
          v-show="showAdvanceSearchView"
          style="
          border: 1px solid #409eff;
          border-radius: 5px;
          box-sizing: border-box;
          padding: 5px;
          margin: 10px 0px;
        "
      >
        <el-row style="display: flex">
          <el-col :span="10">
            用户名 :
            <el-input
                v-model="keywords_name"
                placeholder="通过姓名搜索用户..."
                prefix-icon="el-icon-search"
                style="width: 300px; margin-right: 10px"
                @keydown.enter.native="doSearch"
                :abled="showAdvanceSearchView"
            ></el-input>
          </el-col>
          <el-col :span="10">
            单位 :
            <el-input
                v-model="keywords"
                placeholder="通过单位名搜索用户..."
                prefix-icon="el-icon-search"
                style="width: 300px; margin-right: 10px"
                @keydown.enter.native="doSearch"
                :abled="showAdvanceSearchView"
            ></el-input>
          </el-col>
          <!--          <el-col :span="5">
            单位:
            <el-select v-model="searchValue.companyName" placeholder="单位" size="mini"
                       style="width: 130px;">
              <el-option
                  v-for="item in institutions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>-->
          <el-col :span="5" :offset="4" style="margin-left: auto">
            <el-button size="mini" icon="el-icon-close" @click="advancedBack"
            >取消</el-button
            >
            <el-button
                size="mini"
                icon="el-icon-search"
                type="primary"
                @click="advancedSearch"
            >搜索</el-button
            >
          </el-col>
        </el-row>
      </div>
    </transition>
    <div style="margin-top: 10px">
      <el-table
          ref="multipleTable"
          :data="hrs"
          stripe
          border
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          @selection-change="handleSelectionChange"
          style="width: 100%"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column
            prop="id"
            fixed
            align="left"
            label="管理员编号"
            min-width="2%"
        >
        </el-table-column>
        <el-table-column
            prop="name"
            fixed
            align="name"
            label="姓名"
            min-width="3%"
        >
        </el-table-column>

        <el-table-column
            prop="username"
            label="用户名"
            align="left"
            min-width="3%"
        >
        </el-table-column>
        <el-table-column
            prop="phone"
            label="手机号码"
            align="left"
            min-width="3%"
        >
        </el-table-column>
        <el-table-column
            prop="email"
            label="邮箱地址"
            align="left"
            min-width="6%"
        >
        </el-table-column>
        <el-table-column
            prop="companyName"
            label="所属单位"
            align="left"
            min-width="5%"
        >
        </el-table-column>
        <el-table-column
            prop="comment"
            min-width="3%"
            align="left"
            label="备注"
        >
        </el-table-column>
        <el-table-column align="left" min-width="4%" label="操作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row)"
                style="padding: 4px"
                size="mini"
                type="primary"
                icon="el-icon-edit"
                plain
            >编辑</el-button
            >
            <el-button
                @click="deleteHr(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0; display: flex; justify-content: left">
        <div>
          <el-button
              @click="toggleSelection()"
              type="primary"
              icon="el-icon-close"
          >取消选择</el-button
          >
        </div>
        <div style="margin-left: auto">
          <el-pagination
              background
              @current-change="currentChange"
              @size-change="sizeChange"
              layout="sizes, prev, pager, next, jumper, ->, total, slot"
              :total="total"
          >
          </el-pagination>
        </div>
      </div>
    </div>
    <el-dialog :title="title" :visible.sync="dialogVisible" width="40%" center>
      <el-form
          :label-position="labelPosition"
          label-width="100px"
          :model="hr_info_new"
          :rules="rules"
          ref="adminForm"
          style="margin-left: 10px"
      >
        <el-form-item label="姓名:" prop="name">
          <el-input
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="hr_info_new.name"
              placeholder="请输入姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号:" prop="phone">
          <el-input
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="hr_info_new.phone"
              placeholder="请输入电话"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮箱地址:" prop="email">
          <el-input
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="hr_info_new.email"
              placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户名:" prop="username">
          <el-input
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="hr_info_new.username"
              placeholder="请输入登录username"
          ></el-input>
        </el-form-item>
        <el-form-item label="登录密码:" prop="password">
          <el-input
              size="mini"
              style="width: 50%"
              prefix-icon="el-icon-edit"
              v-model="hr_info_new.password"
              placeholder="请输入登录password"
          ></el-input>
        </el-form-item>
<!--        <el-form-item label="单位编号:" prop="institutionID">
          <el-input
              size="mini"
              style="width: 30%"
              prefix-icon="el-icon-edit"
              v-model="hr_info_new.institutionID"
              placeholder="请输入单位编号"
          ></el-input>
        </el-form-item>-->
<!--        <el-form-item label="角色:" prop="role">
          <el-input
              size="mini"
              style="width: 30%"
              prefix-icon="el-icon-edit"
              v-model="hr_info_new.role"
              placeholder="请输入角色"
          ></el-input>
        </el-form-item>-->
        <el-form-item label=" 备 注:" prop="comment">
          <el-input
              prefix-icon="el-icon-edit"
              type="textarea"
              :rows="2"
              v-model="hr_info_new.comment"
              placeholder="请输入登录备注"
          >
          </el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddHr">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
        :title="title"
        :visible.sync="dialogVisible_edit"
        width="40%"
        center
    >
      <el-form
          :label-position="labelPosition"
          label-width="100px"
          :model="hr_info"
          :rules="rules"
          ref="adminForm"
          style="margin-left: 20px"
      >
        <el-form-item label="姓名 :" prop="name">
          <el-input
              size="mini"
              style="width: 60%"
              prefix-icon="el-icon-edit"
              v-model="hr_info.name"
              placeholder="请输入姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号 :" prop="phone">
          <el-input
              size="mini"
              style="width: 60%"
              prefix-icon="el-icon-edit"
              v-model="hr_info.phone"
              placeholder="请输入电话"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮箱地址:" prop="email">
          <el-input
              size="mini"
              style="width: 90%"
              prefix-icon="el-icon-edit"
              v-model="hr_info.email"
              placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户名:" prop="username">
          <el-input
              size="mini"
              style="width: 90%"
              prefix-icon="el-icon-edit"
              v-model="hr_info.username"
              placeholder="请输入登录username"
          ></el-input>
        </el-form-item>
<!--        <el-form-item label="单位编号:" prop="institutionID">
          <el-input
              size="mini"
              style="width: 30%"
              prefix-icon="el-icon-edit"
              v-model="hr_info.institutionID"
              placeholder="请输入单位编号"
          ></el-input>
        </el-form-item>-->
<!--        <el-form-item label="单位名称:" prop="companyName">
          <el-input
              size="mini"
              style="width: 90%"
              prefix-icon="el-icon-edit"
              v-model="hr_info.companyName"
              placeholder="请输入单位名称"
          ></el-input>
        </el-form-item>-->
        <el-form-item label="备 注:" prop="comment">
          <el-input
              prefix-icon="el-icon-edit"
              type="textarea"
              :rows="2"
              v-model="hr_info.comment"
              placeholder="请输入登录备注"
          >
          </el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible_edit = false">取 消</el-button>
        <el-button type="primary" @click="doAddHr">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "PerEc",
  data() {
    return {
      labelPosition: "left",
      searchValue: {
        compnayName: null,
      },
      title: "",
      page: 1,
      keywords: "",
      keywords_id: "",
      keywords_name: "",
      size: 10,
      total: 0,
      loading: false,
      hrs: [],
      institutions: [],
      selectedRoles: [],
      allroles: [],
      dialogVisible: false,
      dialogVisible_edit: false,
      showAdvanceSearchView: false,
      hr_info: {
        id: null,
        compnayName: null,
        institutionID: null,
        name: "javaboy",
        phone: "18568128889",
        email: "123@126.com",
        enabled: 1,
        username: "test123",
        password: "123",
        role: 1,
        comment: null,
      },
      hr_info_new: {
        id: null,
        compnayName: null,
        institutionID: null,
        name: "javaboy",
        phone: "18568128889",
        email: "123@126.com",
        enabled: 1,
        username: "test123",
        password: "123",
        role: 1,
        comment: null,
      },
      rules: {
        compnayName: {
          required: true,
          message: "请输入单位名称",
          trigger: "blur",
        },
        institutionID: {
          required: true,
          message: "请输入单位编号",
          trigger: "blur",
        },
        name: { required: true, message: "请输入用户名", trigger: "blur" },
        phone: { required: true, message: "请输入电话", trigger: "blur" },
        role: { required: true, message: "请输入角色", trigger: "blur" },
        email: { required: true, message: "请输入邮箱", trigger: "blur" },
        enabled: { required: true, message: "请输入enable", trigger: "blur" },
        username: {
          required: true,
          message: "请输入username",
          trigger: "blur",
        },
        password: { required: true, message: "请输入密码", trigger: "blur" },
        comment: { required: false, message: "请输入备注", trigger: "blur" },
      },
      //表格取消选择
      multipleSelection: [],
    };
  },
  created() {
    //this.keywords = this.$route.query.keywords;
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_id=this.$route.query.keywords_id;
    this.initHrs();
    this.initData();
    //this.initAd();
  },
  methods: {
    deleteHr(hr) {
      this.$confirm("此操作将永久删除【" + hr.name + "】, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.postRequest("/system/admin/delete", hr).then((resp) => {
              if (resp) {
                this.initHrs();
              }
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
    },
    doSearch() {
      this.initHrs();
    },
    showPop(hr) {
      this.initAllRoles();
      let roles = hr.roles;
      this.selectedRoles = [];
      roles.forEach((r) => {
        this.selectedRoles.push(r.id);
      });
    },
    enabledChange(hr) {
      //delete hr.roles;
      this.putRequest("/system/admin/", hr).then((resp) => {
        if (resp) {
          this.initHrs();
        }
      });
    },
    initAllRoles() {
      this.getRequest("/system/hr/roles").then((resp) => {
        if (resp) {
          this.allroles = resp;
        }
      });
    },
    showAddEmpView() {
      this.title = "添加管理员";
      this.dialogVisible = true;
    },
    initHrs() {
      this.getRequest(
          "/system/admin/?keywords=" +
          this.keywords +
          "&page=" +
          this.page +
          "&size=" +
          this.size
      ).then((resp) => {
        if (resp) {
          this.hrs = resp.data;
          this.total = resp.total;
        }
      });
    },
    advancedBack() {
      this.showAdvanceSearchView = false;
    },

    advancedSearch() {
      this.getRequest(
          "/system/admin/advanced/?keywords=" +
          this.keywords +
          "&keywords_name=" +
          this.keywords_name +
          "&page=" +
          this.page +
          "&size=" +
          this.size
      ).then((resp) => {
        if (resp) {
          this.hrs = resp.data;
          this.total = resp.total;
        }
      });
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initHrs();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initHrs("advanced");
    },
    showEditEmpView(data) {
      this.initPositions();
      this.title = "编辑管理员信息";
      this.hr_info = data;
      this.inputDepName = data.company;
      this.dialogVisible_edit = true;
    },
    initPositions() {
      /*this.getRequest('/employee/basic/positions').then(resp => {
        if (resp) {
          this.positions = resp;
        }
      })*/
    },
    doAddHr() {
      if (this.hr_info.id) {
        //console.log(this.hr_info);
        const _this = this;
        this.$refs["adminForm"].validate((valid) => {
          if (valid) {
            this.postRequest("/system/admin/update", _this.hr_info).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible_edit = false;
                    this.initHrs();
                    if(resp.msg==='更新成功!')
                    {Message.success(resp.msg)}
                    else
                    {Message.error(resp.msg)}
                  }
                }
            );
          }
        });
      } else {
        //console.log(this.hr_info_new);
        this.$refs["adminForm"].validate((valid) => {
          // const _this = this;
          this.hr_info_new.institutionID=this.keywords_id;
          if (valid) {
            const _this = this;
            this.putRequest("/system/admin/insert", _this.hr_info_new).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible = false;
                    this.initHrs();

                    if(resp.msg==='添加成功!')
                    {Message.success(resp.msg)}
                    else
                    {Message.error(resp.msg)}
                  }
                }
            );
          }
        });
      }
    },
    back() {
      const _this = this;
      _this.$router.push({
        path: "/per/emp",
      });
    },
    reset() {
      this.keywords = "";
      this.keywords_name = "";
      this.initHrs();
    },
    //取消表格选择
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    initData() {
      //if (!window.sessionStorage.getItem("institutions")) {
      /* this.getRequest('/employee/basic/nations').then(resp => {
          if (resp) {
            //console.log(resp)
            this.institutions = resp;
            window.sessionStorage.setItem("institutions", JSON.stringify(resp));
          }
        })*/
      //} else {
      // this.institutions = JSON.parse(window.sessionStorage.getItem("institutions"));
      //}
    }, //
  },
};
</script>

<style>
.userinfo-container div {
  font-size: 12px;
  color: #409eff;
}

.userinfo-container {
  margin-top: 20px;
}

.img-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.userface-img {
  width: 72px;
  height: 72px;
  border-radius: 72px;
}

.hr-container {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}

.hr-card {
  width: 350px;
  margin-bottom: 20px;
}
</style>
