<template>
  <div>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center">{{ keywords_name }}分组管理</div>
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div ><br/>单元格中内容双击后可编辑</div>
    <div style="margin-top: 10px">
      <el-table
          ref="multipleTable"
          :data="hrs"
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
        <el-table-column type="selection" min-width="1%"></el-table-column>
        <el-table-column
            prop="activityID"
            fixed
            align="left"
            label="活动编号"
            min-width="2%"
        >
        </el-table-column>
        <el-table-column
            prop="name"
            fixed
            align="name"
            label="分组名称"
            min-width="5%"
        >
          <!--          原来的change函数，不知道什么意思-->
          <!--          @change="handleEdit(scope.$index,scope.row)"-->
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex &&
                  tabClickLabel === '分组名称'
                "
                v-focus
                v-model.trim="scope.row.name"
                maxlength="50"
                size="mini"
                @focus="beforehandleEdit(scope.$index,scope.row)"
                @change="UpdateOrNew(scope.row)"
                @blur="inputBlur"
            />
            <span
                style="width: 100%; height: 100%; display: inline-block"
                v-else
            >{{ scope.row.name }}</span
            >
          </template>
        </el-table-column>

        <el-table-column
            prop="expertCount"
            label="专家数"
            align="center"
            min-width="3%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.expertCount }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="participantCount"
            label="选手数"
            align="center"
            min-width="3%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.participantCount }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="15%" label="操作">
          <template slot-scope="scope">
            <el-button
                @click="UpdateOrNew(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-collection"
                type="primary"
                plain
            >保存
            </el-button
            >
              <el-button
                      @click="assignPE(scope.row)"
                      v-show="mode === 'secretarySub'"
                      style="padding: 4px"
                      size="mini"
                      icon="el-icon-tickets"
                      type="primary"
                      plain
              >分配选手和专家
              </el-button
              >
            <el-button
                @click="showGroups(scope.row)"
                v-show="mode !== 'secretarySub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >专家管理
            </el-button
            >
            <el-button
                @click="showParticipantsM(scope.row)"
                v-show="mode !== 'secretarySub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-s-operation"
                type="primary"
                plain
            >选手管理
            </el-button
            >
<!--            <el-button-->
<!--                @click="output_group(scope.row)"-->
<!--                v-show="mode==='admin'"-->
<!--                :loading="loading"-->
<!--                style="padding: 4px"-->
<!--                size="mini"-->
<!--                icon="el-icon-plus"-->
<!--                type="primary"-->
<!--                plain-->
<!--            >导出本组选手分数-->
<!--            </el-button-->
<!--            >-->
            <el-button
                @click="showFinalScore(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >查看选手成绩
            </el-button
            >
            <el-button
                @click="exportTG(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >导出本组专家打分
            </el-button
            >
            <el-button
                @click="Delete_Score_Item(scope.row)"
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
        <!-- <div>
          <el-button
            @click="toggleSelection()"
            type="primary"
            icon="el-icon-close"
            >取消选择</el-button
          >
        </div> -->
        <div style="margin-left: 8px">
          <el-button
              @click="handleAddDetails()"
              type="primary"
              icon="el-icon-plus"
          >新增
          </el-button
          >
        </div>
<!--        <div style="margin-left: auto">-->
<!--          <el-pagination-->
<!--              background-->
<!--              @current-change="currentChange"-->
<!--              @size-change="sizeChange"-->
<!--              layout="sizes, prev, pager, next, jumper, ->, total, slot"-->
<!--              :total="total"-->
<!--          >-->
<!--          </el-pagination>-->
<!--        </div>-->
      </div>
    </div>
  </div>
</template>

<script>
import {Message} from 'element-ui'
import da from "element-ui/src/locale/lang/da";
import el from "element-ui/src/locale/lang/el";

export default {
  name: "SalTable",
  data() {
    return {
      //当前焦点数据
      currentfocusdata: "",
      mode:'',
      groupID:-1,
      searchValue: {
        compnayName: null,
      },
      title: "",
      page: 1,
      tabClickIndex: null, // 点击的单元格
      tabClickLabel: "", // 当前点击的列名
      keywords: "",
      keywords_name: "",
      size: 10,
      total: 0,
      loading: false,
      hrs: [],
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
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem("user")); //object信息
    },
  },
  created() {
    //this.keywords = this.$route.query.keywords;
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.groupID = this.$route.query.groupID;
    this.mode = this.$route.query.mode;
    this.initHrs();
    //this.initAd();
  },
  methods: {
    Delete_Score_Item(si) {
        // console.log("si")
        // console.log(si)
      this.$confirm("此操作将永久删除【" + si.name + "】, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.postRequest("/groups/basic/delete?institutionID="+this.user.institutionID, si).then((resp) => {
              if (resp) {
                if(resp==='删除成功!')
                {Message.success(resp)}
                else
                {Message.error(resp)}
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
    beforehandleEdit(index, row) {
      this.currentfocusdata = row.name
    },
    handleEdit(index, row) {
    },
    // 失去焦点初始化
    inputBlur() {
      if (this.currentfocusdata == null) {
        Message.error('输入内容不能为空！操作未保存！')
      }
      this.tabClickIndex = null;
      this.tabClickLabel = "";
      this.currentfocusdata = "";
    },
    // doSearch() {
    //   this.initHrs();
    // },
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
    initHrs() {
        this.getRequest("/groups/basic/?keywords=" + this.keywords + "&page=" + 1 + "&size=" + 1000).then((resp) => {
            if (resp) {
                this.hrs = resp.data;
                this.total = this.hrs.length;
            }
        });
      // if (this.mode === "admin") {
      //     this.getRequest("/groups/basic/?keywords=" + this.keywords + "&page=" + 1 + "&size=" + 1000).then((resp) => {
      //         if (resp) {
      //             this.hrs = resp.data;
      //             this.total = this.hrs.length;
      //         }
      //     });
      // }
      // else if (this.mode === "secretarySub"){
      //   this.getRequest("/groups/basic/sec?groupID="+this.groupID).then((resp) => {
      //     if (resp) {
      //       this.hrs = resp.obj;
      //       this.total = this.hrs.length;
      //     }
      //   });
      // }
    },
    showFinalScore(data){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/final",
        query: {
          keywords: data.activityID,
          groupID:data.id,
          keyword_name: this.keywords_name,
          groupName:data.name,
          mode:this.mode,
          backGroupName:this.$route.query.groupName,
          backGroupID:this.$route.query.groupID,
          backBackID:this.$route.query.backID,
          backActName:this.$route.query.backActName,
        },
      });
    },
    advancedSearch() {
      this.getRequest(
          "/groups/basic/advanced/?keywords=" +
          this.keywords +
          "&keywords_name=" +
          this.keywords_name +
          "&page=" +
          1 +
          "&size=" +
          1000
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
    //取消表格选择
    // toggleSelection(rows) {
    //   if (rows) {
    //     rows.forEach((row) => {
    //       this.$refs.multipleTable.toggleRowSelection(row);
    //     });
    //   } else {
    //     this.$refs.multipleTable.clearSelection();
    //   }
    // },
    handleAddDetails() {
      if (this.hrs == undefined) {
        this.hrs = new Array();
      }
      let obj = {};
      obj.activityID = this.keywords;
      obj.name="输入分组名称"
      obj.expertCount = 0;
      obj.participantCount = 0;
      this.hrs.push(obj);
    },
    back() {
      const _this = this;
      if (this.mode === "admin"){
        _this.$router.push({
          path: "/ActivitM/search",
        });
      }else if (this.mode === "secretary"){
        _this.$router.push({
          path: "/secretary/ActManage",
        });
      }else if (this.mode === "adminSub"){
        _this.$router.push({
          path: "/ActivitM/SubActManage",
          query:{
              id: this.$route.query.backID,
              mode: this.$route.query.mode,
          }
        });
      }else if (this.mode === "secretarySub"){
        _this.$router.push({
          path: "/secretary/SubActManage",
          query:{
            id: this.$route.query.backID,
            mode: this.$route.query.mode,
            actName: this.$route.query.backActName,
            groupName: this.$route.query.groupName,
            groupID: this.$route.query.groupID,
          }
        });
      }

    },
    tableRowClassName({row, rowIndex}) {
      // 把每一行的索引放进row
      row.index = rowIndex;
    },
    // 添加明细原因 row 当前行 column 当前列
    tabClick(row, column, cell, event) {
      switch (column.label) {
        case "分组名称":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        default:
          return;
      }
    },
    output_group(row) {
      this.loading=true;
      Message.success("正在导出");
      let url = '/participants/basic/export_group?groupID=' + row.id;
      this.loading=false;
      window.open(url,'_parent');
    },
    exportTG(row) {
      this.loading=true;
      Message.success("正在导出");
      let url = "/participants/basic/exportTG?groupID="+row.id;
      this.loading=false;
      window.open(url, '_parent');
    },
    showGroups(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          keywords: data.activityID,
          keyword_name: data.name,
          keywords_name:this.keywords_name,
          groupID: data.id,
          mode:this.mode
        }
      })
    },
      assignPE(data) {
          const _this = this;
          _this.$router.push({
              path: "/Expert/EassignPE",
              query: {
                  activityIDParent: this.$route.query.backID,
                  activityID: data.activityID,
                  groupIDParent: this.$route.query.groupID,
                  groupID: data.id,
                  mode:this.mode
              }
          })
      },
    showParticipantsM(data) {
      const _this = this;
      _this.$router.push({
        path: "/participantsM",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          keywords_name:this.keywords_name,
          groupID: data.id,
          activityID: data.activityID,
          mode:this.mode,
        },
      });
    },
    reset(){
      this.initHrs();
    },
    UpdateOrNew(groups) {
      const _this = this;
      this.postRequest("/groups/basic/UpdateOrNew?institutionID="+this.user.institutionID, groups).then((resp) => {
        if(resp==='更新成功!')
        {Message.success(resp);
        this.reset();
        }else if(resp==='新增成功!')
        {Message.success(resp);
          this.reset();
        }
        else
        {Message.error(resp);
          this.reset();
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
</style>
