<template>
  <div>
    {{ keywords }}、{{ keywords_name }}活动 {{GName}} {{ expertName }}专家评分情况
    <div style="display: flex; justify-content: left;margin-top:10px">
      <div style="margin-left: auto">
        <el-button icon="el-icon-refresh-right" type="danger" @click="revert" :disabled="isFinished==='false'">
          退回评分
        </el-button>
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          ref="multipleTable"
          :data="Scores"
          stripe
          border
          v-loading="loading"
          @cell-dblclick="tabClick"
          :row-class-name="tableRowClassName"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
      >
        <el-table-column
            prop="code"
            fixed
            align="center"
            label="选手编号"
            min-width="4%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" fixed label="选手姓名" min-width="3%">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="displaySequence"
            align="left"
            label="选手序号"
            min-width="5%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.displaySequence }}</span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            v-for="(v, i) in this.smap"
            prop="v"
            :label="v"
            min-width="5%" align="center">
          <template slot-scope="scope">
            <div  v-for="(value,key) in scope.row.scoremap" v-if="key===i">
              {{value.score}}
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!--弹窗-->
    </div>
  </div>
</template>

<script>
import {Message} from 'element-ui'

export default {
  name: "SalSituation",
  data() {
    return {
      mode:"",
      //当前焦点数据
      currentfocusdata: "",
      searchValue: {
        compnayName: null,
      },
      title: "",
      importDataBtnIcon: "el-icon-plus",
      importDataDisabled: false,
      page: 1,
      tabClickIndex: null, // 点击的单元格
      tabClickLabel: "", // 当前点击的列名
      keywords: "",
      isFinished: false,
      activitydata: [],
      keywords_name: "",
      groupID: '',
      size: 10,
      total: 0,
      loading: false,
      Scores: [],
      map: [],
      smap: [],
      unsureinfo: [],
      dialogVisible_edit: false,
      selectedRoles: [],
      allroles: [],
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
        name: {required: true, message: "请输入用户名", trigger: "blur"},
        phone: {required: true, message: "请输入电话", trigger: "blur"},
        role: {required: true, message: "请输入角色", trigger: "blur"},
        email: {required: true, message: "请输入邮箱", trigger: "blur"},
        enabled: {required: true, message: "请输入enable", trigger: "blur"},
        username: {
          required: true,
          message: "请输入username",
          trigger: "blur",
        },
        password: {required: true, message: "请输入密码", trigger: "blur"},
        comment: {required: false, message: "请输入备注", trigger: "blur"},
      },

      err_dialogVisible_edit: false,
      err_title: '',
      err_msg: ''
    };
  },
  created() {
    //this.keywords = this.$route.query.keywords;
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.GName = this.$route.query.keywords_name;
    this.groupID = this.$route.query.groupID;
    this.expertID = this.$route.query.expertID;
    this.ACNAME = this.$route.query.keywords_name;
    this.expertName= this.$route.query.expertName;
    this.institutionID= this.$route.query.institutionID;
    this.isFinished = this.$route.query.isFinished;
    console.log(this.isFinished);
    this.mode = this.$route.query.mode;
    this.initSituation();
    this.initData();
  },
  methods: {
    Delete_ExActivity(si) {
      this.$confirm("此操作将永久删除【" + si.name + "】, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.postRequest("/systemM/Experts/delete?groupid=" + this.groupID + "&activityid=" + this.keywords, si).then(resp => {
              if (resp) {
                this.initSituation();
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
    enabledChange(hr) {
      this.putRequest("/system/admin/", hr).then((resp) => {
        if (resp) {
          this.initSituation();
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
    initSituation() {
      this.getRequest(
          "/systemM/Experts/show_situ?keywords=" +
          this.groupID +
          "&expertID=" +
          this.expertID +
          "&page=" +
          this.page +
          "&size=" +
          this.size
      ).then((resp) => {
        if (resp) {
          // console.log(resp);
          for(var name in resp.data){
            var value =resp.data[name];
            for(var v in value.map){
              var vv =value.map[v];
              // console.log(vv);
              for(var vvv in vv){
                this.map=vv[vvv];
              }
            }
            for(var i in this.map){
              this.Scores.push(this.map[i]);
            }
            // console.log(this.Scores);
            this.smap=value.smap;
          }
          this.total = resp.total;
        }
      });
    },

    advancedSearch() {
      this.getRequest(
          "/groups/basic/advanced/?keywords=" +
          this.keywords +
          "&keywords_name=" +
          this.keywords_name +
          "&page=" +
          this.page +
          "&size=" +
          this.size
      ).then((resp) => {
        if (resp) {
          this.Scores = resp.data;
          this.total = resp.total;
        }
      });
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initSituation();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initSituation("advanced");
    },
    handleAddDetails() {
      if (this.Scores === undefined) {
        this.Scores = new Array();
      }
      let obj = {};
      obj.activityID = this.keywords;
      obj.expertCount = 0;
      obj.participantCount = 0;
      this.Scores.push(obj);
    },
    back() {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          keywords: this.keywords,
          keyword_name: this.ACNAME,
          keywords_name:this.keywords_name,
          groupID: this.groupID,
          mode:this.mode,
        },
      });
    },
    revert(){
      this.$confirm("是否确定撤销打分", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.postRequest(
                "/systemM/Experts/withdraw?activityID=" + this.keywords + "&groupID=" + this.groupID +"&expertID=" + this.expertID
            ).then((resp) => {
              if (resp) {
                this.$message({
                  type: 'success',
                  message: '撤销成功!'
                });
                this.isFinished=false;
              }
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消撤销",
            });
          });
    },
    tableRowClassName({row, rowIndex}) {
      // 把每一行的索引放进row
      row.index = rowIndex;
    },
    // 添加明细原因 row 当前行 column 当前列
    tabClick(row, column, cell, event) {
      switch (column.label) {
        case "专家姓名":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "证件号码":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "邮箱":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "部门":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "电话":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        default:
          return;
      }
    },

    showParticipantsM(data) {
      const _this = this;
      _this.$router.push({
        path: "/participantsM",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    beforehandleEdit(index, row, label) {
      this.currentfocusdata = row[label]
    },
    handleEdit(index, row, label) {
      if (row[label] === '') {
        Message.warning('输入内容不能为空！')
        row[label] = this.currentfocusdata
      } else {
        this.save(row)
      }
    },
    // 失去焦点初始化
    inputBlur() {
      this.tabClickIndex = null;
      this.tabClickLabel = "";
      this.currentfocusdata = "";
    },
    save(si) {
      const _this = this;
      this.postRequest("/systemM/Experts/update", si).then((resp) => {
        Message.success(resp)
      });
    },
    update(si) {
      const _this = this
      this.postRequest("/systemM/Experts/updateGroupId?groupid=" + this.groupID + "&activityid=" + this.keywords, si).then(resp => {
      })
    },
    initData() {
      this.getRequest("/activities/basic/get_activity_info").then((resp) => {
        if (resp) {
          this.activitydata = resp;
        }
      });
    },
    reset_password(row) {
      this.putRequest("/systemM/Experts/pass", row).then((resp) => {
        if (resp) {
          //弹窗
          if(resp==='已将密码设置为证件号码后6位!')
          {
            Message.success(resp);
          }
          else
          {
            Message.error(resp);
          }
        }
      });
    },
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

.tb-edit.el-input {
  display: none;
}

.tb-edit.current-row.el-input {
  display: block;
}

.tb-edit.current-row.el-input + span {
  display: none;
}

.spanscoped {
  width: 100%;
  height: 100%;
  display: inline-block;
}
</style>
