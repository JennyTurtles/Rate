<template>
  <div>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center">{{ keywords_name }}分数统计</div>
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div><br/><br/>活动成绩统计
    </div>
    <div style="margin-top: 10px">
      <el-table
          ref="multipleTable"
          :data="activity"
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
            prop="activityname"
            fixed
            align="name"
            label="活动名称"
            min-width="4%"
        >
        </el-table-column>

        <el-table-column
            prop="maxscore"
            label="最高分"
            align="center"
            min-width="2%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.maxscore }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="minscore"
            label="最低分"
            align="center"
            min-width="2%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.minscore }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="avescore"
            label="平均分"
            align="center"
            min-width="2%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.avescore }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="varscore"
            label="方差"
            align="center"
            min-width="2%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.varscore }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div><br/><br/>小组成绩统计
    </div>
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
        <el-table-column
            prop="groupname"
            fixed
            align="name"
            label="分组名称"
            min-width="100%"
        >
          <!--          原来的change函数，不知道什么意思-->
          <!--          @change="handleEdit(scope.$index,scope.row)"-->
          <template slot-scope="scope">
            <span>{{ scope.row.groupname }}</span>
          </template>
        </el-table-column>

        <el-table-column
            prop="maxscore"
            label="最高分"
            sortable
            align="center"
            min-width="100%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.maxscore }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="minscore"
            label="最低分"
            sortable
            align="center"
            min-width="100%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.minscore }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="avescore"
            label="平均分"
            sortable
            align="center"
            min-width="100%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.avescore }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="varscore"
            label="方差"
            sortable
            align="center"
            min-width="100%"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.varscore }}</span>
          </template>
        </el-table-column>
<!--        <el-table-column align="center" min-width="15%" label="操作">-->
<!--          <template slot-scope="scope">-->
<!--            <el-button-->
<!--                @click="showParticipantsM (scope.row)"-->
<!--                style="padding: 4px"-->
<!--                size="mini"-->
<!--                icon="el-icon-collection"-->
<!--                type="primary"-->
<!--                plain-->
<!--            >查看小组分数-->
<!--            </el-button-->
<!--            >-->
<!--            <el-button-->
<!--                @click="showExpertScore (scope.row)"-->
<!--                style="padding: 4px"-->
<!--                size="mini"-->
<!--                icon="el-icon-collection"-->
<!--                type="primary"-->
<!--                plain-->
<!--            >查看专家评分-->
<!--            </el-button-->
<!--            >-->
<!--          </template>-->
<!--        </el-table-column>-->
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

export default {
  name: "SalScore",
  data() {
    return {
      mode: '',
      //当前焦点数据
      currentfocusdata: "",
      searchValue: {
        compnayName: null,
      },
      title: "",
      page: 1,
      tabClickIndex: null, // 点击的单元格
      tabClickLabel: "", // 当前点击的列名
      keywords: "",
      activitydata: [],
      keywords_name: "",
      size: 10,
      total: 0,
      loading: false,
      hrs: [],
      activity:[],
      selectedRoles: [],
      allroles: [],
      groupID: null
    };
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
  },
  created() {
    //this.keywords = this.$route.query.keywords;
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.mode = this.$route.query.mode;
    this.groupID = this.$route.query.groupID;
    this.initHrs();
    this.initActivity();
    // this.initData();
    //this.initAd();
  },
  methods: {

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
          this.initActivity();
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
      this.getRequest(
          "/groups/basic/score?keywords=" +
          this.keywords +
          "&page=" +
          1 +
          "&size=" +
          1000
      ).then((resp) => {
        if (resp) {
          if (this.groupID !== undefined && this.groupID !== null)
            this.hrs.push(resp.data.find(item => item.groupID == this.groupID));
          else
            this.hrs = resp.data;
          this.total = resp.total;
        }
      });
    },
    initActivity(){
      this.getRequest(
          "/activities/basic/score?activityID=" +
          this.keywords
      ).then((resp) => {
        if (resp) {
          this.activity = resp.data;
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
    back() {
      //this.$router.go(-1);
      const _this = this;
      if (this.mode === 'admin'){
        _this.$router.push({
          path: "/ActivitM/search",
        });
    }else if (this.mode ==='adminSub'){
          _this.$router.push({
              path: '/ActivitM/SubActManage',
              query: {
                  id: _this.$route.query.backID,
                  mode: _this.mode,
              }
          })
      }else if (this.mode ==='secretary'){
        _this.$router.push({
          path: '/secretary/ActManage',
          query: {
            id: _this.$route.query.backID,
            mode: _this.mode,
          }
        })
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
    showParticipantsM(data) {
      console.log(data)
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/final",
        query: {
          keywords: data.activityID,
          keyword_name: this.keywords_name,
          groupID: data.groupID,
          mode:this.mode,
          groupName: data.groupname,
          flag:1, // 1表示从"分数统计进入"。同一个界面被多处引用，逻辑不够清晰。以后再优化。
        },
      })
      // _this.$router.push({
      //   path: "/ActivitM/detail",
      //   query: {
      //     keyword_name: data.groupname,
      //     keywords_name:this.keywords_name,
      //     groupID: data.groupID,
      //     activityID: data.activityID
      //   },
      // });
    },
    showExpertScore(data) {
      console.log(data)
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          keywords: data.activityID,
          keyword_name: data.groupname,
          keywords_name:this.keywords_name,
          groupID: data.groupID,
          mode:this.mode,
          flag:1, // 1表示从"分数统计进入"。同一个界面被多处引用，逻辑不够清晰。以后再优化。
        }
      })
      // _this.$router.push({
      //   path: "/ActivitM/expertScore",
      //   query: {
      //     keywords: data.groupID,
      //     keyword_name: data.groupname,
      //     keywords_name:this.keywords_name,
      //     groupID: data.groupID,
      //     activityID: data.activityID
      //   },
      // });
    },
    reset(){
      this.initHrs();
    },
    initData() {
      this.getRequest("/activities/basic/get_activity_info").then((resp) => {
        if (resp) {
          this.activitydata = resp;
        }
      });
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
