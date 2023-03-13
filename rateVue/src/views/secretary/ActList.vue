<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-input
              placeholder="请输入活动名称进行搜索，可以直接回车搜索..."
              prefix-icon="el-icon-search"
              clearable
              @clear="searchEmps"
              style="width: 350px; margin-right: 10px"
              v-model="keyword"
              @keydown.enter.native="searchEmps"
              :disabled="showAdvanceSearchView"
          ></el-input>
          <el-button
              icon="el-icon-search"
              type="primary"
              @click="searchEmps"
              :disabled="showAdvanceSearchView"
          >
            搜索
          </el-button>
          <el-button
              icon="el-icon-refresh"
              type="primary"
              @click="initEmps"
              :disabled="showAdvanceSearchView"
          >
            重置
          </el-button>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加活动
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="activities"
          stripe
          border
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
      >
        <el-table-column type="selection" width="35px"></el-table-column>
        <el-table-column
            prop="id"
            fixed
            align="center"
            label="编号"
            width="50"
        >
        </el-table-column>
        <el-table-column
            prop="name"
            fixed
            align="left"
            label="活动名称"
            width="200"
        >
        </el-table-column>
        <el-table-column
            prop="startDate"
            label="开始时间"
            align="center"
            width="130"
        >
        </el-table-column>
        <el-table-column
            prop="scoreItemCount"
            label="评分项数"
            align="center"
            width="80"
        >
        </el-table-column>
        <el-table-column
            prop="score"
            label="总分"
            align="center"
            width="75"
        >
        </el-table-column>
        <el-table-column align="center" width="650" label="操 作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView_show(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情
            </el-button
            >
            <el-button
                @click="showEditEmpView(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
            >编辑
            </el-button
            >
            <el-button
                @click="showScoreItem(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >评分项设置
            </el-button
            >
            <el-button
                @click="showInfoItem(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >信息项设置
            </el-button
            >
            <el-button
                @click="showGroupmanagement(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-s-operation"
                type="primary"
                plain
            >分组管理
            </el-button
            >
            <el-button
                @click="showGroupOfPartipicant(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-s-operation"
                type="primary"
                plain
            >选手分组
            </el-button
            >
            <el-button
                @click="showInsertmanagement(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >选手管理
            </el-button
            >
            <el-button
                @click="showScore(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >分数统计
            </el-button
            >
            <el-button
                @click="showSubActivity(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >子活动管理
            </el-button
            >

            <el-button
                @click="exportEx(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >{{text}}导出专家打分
            </el-button
            >
            <el-button
                @click="exportAc(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >导出选手分数
            </el-button
            >

            <el-button
                @click="endEmp(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-circle-close"
                plain
                :disabled="scope.row.status=='close'"
            >{{ scope.row.status == 'open' ? '结束活动' : '已结束' }}
            </el-button
            >
            <el-button
                @click="deleteEmp(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >删除
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <div style="display: flex; justify-content: flex-end; margin: 10px 0">
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
</template>

<script>
import {getRequest} from "@/utils/api";
import {Message} from "element-ui";
import loading from "element-ui/packages/loading";
import th from "element-ui/src/locale/lang/th";
export default {
  name: "ActList",
  data() {
    return {
      activities:[],
      loading : false,
      total: 0,
      size: 10,
      showAdvanceSearchView: false,
      keyword: "",
    };
  },
  computed: {
    loading() {
      return loading
    }
  },
  mounted(){
    this.initActs()
  },
  methods: {
    rowClass() {
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    emptyEmp() {
      this.emp = {
        id: null,
        startDate: null,
        name: "",
        scoreItemCount: "0",
        comment: "活动备注example：关于xxx的活动",
      };
    },
    exportEx(data){
      this.loading=true;
      this.text='正在';
      Message.success("正在导出");
      let url = '/participants/basic/export?activityID=' + data.id;
      window.open(url, "_parent");
      this.loading=false;
      this.text='';
    },
    exportAc(data){
      this.loading=true;
      Message.success("正在导出");
      let url = '/participants/basic/export_ac?activityID=' + data.id;
      window.open(url, "_parent");
      this.loading=false;
    },
    showEditEmpView(data) {
      this.initPositions();
      this.title = "编辑单位信息";
      this.emp = data;
      this.dialogVisible = true;
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
    },
    showScore(data){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/score",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    deleteEmp(data) {
      data.institutionID = this.user.institutionID;
      this.getRequest("/activities/basic/check?id=" + data.id).then(res => {
        //console.log(res);
        if (res == true) {
          // console.log("this obj has no relatives")
          this.$confirm(
              "此操作将永久删除【" + data.name + "】, 是否继续?",
              "提示",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }
          ).then(() => {
            this.postRequest("/activities/basic/delete", data).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          });
        } else {
          this.$confirm(
              "尚存在与活动" + data.name + "相关的信息，所以放入回收站，如要永久删除，请先删除分组和评分项等相关信息，是否放入回收站?",
              "提示",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }
          ).then(() => {
            this.postRequest("/activities/basic/predelete", data).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          });
        }
      })
    },
    endEmp(data) {
      data.institutionID = this.user.institutionID;
      // console.log(data);
      this.$confirm(
          "此操作将永久停止活动【" + data.name + "】, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      ).then(() => {
        this.postRequest("/activities/basic/end", data).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        });
      });
    },
    doAddEmp() {//添加活动
      if (this.emp.id) {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.institutionID = this.user.institutionID;
            const _this = this;
            this.postRequest("/activities/basic/update", _this.emp).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible = false;
                    this.initEmps();
                  }
                }
            );
          }
        });
      } else {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.institutionID = this.user.institutionID;
            const _this = this;
            this.postRequest("/activities/basic/insert", _this.emp).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible = false;
                    this.initEmps();
                  }
                }
            );
          }
        });
      }
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    searchEmps() {
      this.loading = true;
      const _this = this;
      //let url =
      this.getRequest(
          "/activities/basic/search?company=" +
          this.keyword +
          "&page=" +
          this.page +
          "&size=" +
          this.size +
          "&institutionID=" + this.user.institutionID
      ).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
        }
      });
    },
    initActs() {
      this.loading = true
      const id = JSON.parse(localStorage.getItem("user")).id
      this.getRequest("/secretary/getAct?teacherID="+id).then((resp)=>{
        this.loading = false;
        this.activities = resp.obj
      })
    },
  },
};
</script>
<style lang="css" scoped>

</style>