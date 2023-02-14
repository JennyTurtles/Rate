<template>
  <div>
    <!-- 表名 -->
    <div class="content">
      <div class="leftTitle">
        <el-form>
          <el-row>
            <el-col :span="10">
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-check"
                  :disabled="datalist.finished == true"
                  @click="commit()"
                  >提交评分
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="10" :offset="1">
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-plus"
                  @click="download()"
                  >下载评分表
                </el-button>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-button
                type="primary"
                icon="el-icon-document-checked"
                @click="isGetPdf()"
                >导出PDF</el-button
              >
            </el-col>
            <el-col :span="10" :offset="1">
              <el-upload
                :show-file-list="false"
                :on-success="onSuccess"
                :action="Upload()"
              >
                <el-button type="primary" icon="el-icon-upload2"
                  >上传评分表
                </el-button>
              </el-upload>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div class="center">
        <div>
          <div class="centerTxtFirst">{{ this.Aname }}评分表</div>
          <div class="centerTxtLast">{{ this.comment }}</div>
        </div>
      </div>
      <div class="rightTitle">
        <div>
          <div class="rightTitleTxt">组别：{{ groupName }}</div>
           
          <div>
            <el-button
              icon="el-icon-refresh"
              type="primary"
              :disabled="datalist.finished == true"
              @click="refreshact(false)"
            >
              刷新</el-button
            >
          </div>
        </div>
      </div>
    </div>
    <!-- 列表 -->
    <div class="tableStyle">
      <el-table
        :cell-style="rowStyle"
        :header-cell-style="rowClass"
        :data="datalist.participatesList"
        stripe
        :height="windowScreenHeight"
        border
        v-loading="fullscreenLoading"
      >
        <el-table-column
          prop="displaySequence"
          label="序号"
          min-width="50px"
          align="center"
          fixed
        >
        </el-table-column>
        <el-table-column
          prop="code"
          label="编号"
          min-width="160px"
          align="center"
          fixed
        >
          <template #default="scope">
            <div v-copy="scope.row.code" class="h-copy" title="点击即可复制">
              {{ scope.row.code }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="student.name"
          label="姓名"
          min-width="150px"
          align="center"
          fixed
        >
        </el-table-column>

        <el-table-column
          v-for="(v, i) in datalist.infoItems"
          :key="v.name"
          v-if="v.display == true"
          :label="v.name"
          min-width="150px"
          align="center"
        >
          <template slot-scope="scope">
            <span>{{ scope.row["info" + v.name] }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-for="(value, idx) in datalist.scoreitems"
          ref="setTableRef"
          :key="idx"
          v-if="value.name != '总分'"
          :label="
            value.name +
            '|(满分:' +
            value.score +
            '|，系数:|' +
            value.coef +
            '|' +
            (value.comment ? ',' + value.comment : '') +
            ')'
          "
          min-width="150px"
          :render-header="renderheader"
        >
          <template slot-scope="scope">
            <el-input
              placeholder="请打分"
              v-model.trim="scope.row['score' + idx]"
              :disabled="value.byexpert == false || datalist.finished == true"
              clearable
              @input="onInputConfirm(scope.row, scope.$index)"
              ><span>{{ scope.row["score" + idx] }}</span>
            </el-input>
          </template>
        </el-table-column>
        <el-table-column
          v-for="(value, idx) in datalist.scoreitems"
          :key="value.id"
          v-if="value.name == '总分'"
          :label="'总分'"
          min-width="120px"
          align="center"
        >
          <template slot-scope="scope">
            <span>{{ scope.row["sum"] }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          min-width="160px"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              @click="showEditEmpView_show(scope.row, scope.$index)"
              style="padding: 4px"
              size="mini"
              type="primary"
              plain
              >详细信息
            </el-button>
            <el-button
              v-show="scope.row.showSave"
              style="padding: 4px; margin: 2px"
              size="mini"
              type="danger"
              icon="el-icon-mobile"
              @click="saveScore(scope.$index, scope.row)"
              plain
              >保存</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
      :title="title_show"
      :visible.sync="dialogVisible_show"
      width="50%"
    >
      <div class="dialog">
        <el-form
          label-position="left"
          label-width="120px"
          :model="dialogdata"
          ref="dialogdataForm"
        >
          <template slot-scope="scope">
            <el-row>
              <el-col :span="10">
                <el-form-item label="姓 名 :">
                  <span>{{ dialogdata.name }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="10" :offset="1">
                <el-form-item label="编 号 :">
                  <span>{{ dialogdata.idCode }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item
                  v-for="(val, idx) in datalist.infoItems"
                  :key="idx"
                  :label="val.name + ' :'"
                  v-if="dialogdata['info' + val.name] && val.name == '报考专业'"
                >
                  <span>{{ dialogdata["info" + val.name] }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="10" :offset="1">
                <el-form-item
                  v-for="(val, idx) in datalist.infoItems"
                  :key="idx"
                  :label="val.name + ' :'"
                  v-if="dialogdata['info' + val.name] && val.name == '毕业单位'"
                >
                  <span>{{ dialogdata["info" + val.name] }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item
              v-for="(val, idx) in datalist.infoItems"
              :key="idx"
              :label="val.name + ' :'"
              width="130px"
              v-if="
                dialogdata['info' + val.name] &&
                val.name != '报考专业' &&
                val.name != '毕业单位'
              "
            >
              <span>{{ dialogdata["info" + val.name] }}</span>
            </el-form-item>
          </template>
        </el-form>
      </div>

      <template slot="footer" class="dialog-footer">
        <div class="divider"></div>
        <el-button type="primary" @click="dialogVisible_show = false"
          >关 闭</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Message } from "element-ui";
import { getRequest } from "@/utils/api";
export default {
  name: "score",
  inject: ["reload"],
  data() {
    return {
      Aname: "",
      groupName: "",
      Adata: {
        Aid: "",
        Auserid: "",
        AgroupId: "",
      },

      datalist: "",
      outdata: {},
      show: false,
      user: "",
      //表单数据
      title_show: "",
      dialogdata: {},
      dialogVisible_show: false,
      Tname: "",
      comment: "",
      fullscreenLoading: false,

      //自适应屏幕高度
      windowScreenHeight: 0,
    };
  },
  created() {
    this.dataRefreh();
  },
  computed: {
    list() {
      if (sessionStorage.getItem("peract")) {
        let list = JSON.parse(sessionStorage.getItem("peract"));
        return list;
      } else {
        return this.$store.state.peract;
      }
    },
    datal() {
      return this.$store.state.score;
    },
  },

  mounted() {
    this.$nextTick(() => {
      this.windowScreenHeight = window.innerHeight - 210;
    });
    this.user = JSON.parse(sessionStorage.getItem("teacher"));

    this.initdata();
    if (sessionStorage.getItem("score")) {
      let initscore = JSON.parse(sessionStorage.getItem("score"));
      this.datalist = initscore;
    } else {
      this.fullscreenLoading = true;
      this.initAct();
      setTimeout(() => {
        this.datalist = this.datal;
        this.initState();
        sessionStorage.setItem("score", JSON.stringify(this.datalist));
        if (!this.datalist.finished) {
          this.reload();
        }
        this.fullscreenLoading = false;
      }, 700);
    }
  },
  methods: {
    download() {
      this.loading = true;
      Message.success("正在导出");
      let url =
        "/system/Experts/exportRate?activitiesID=" +
        this.Adata.Aid +
        "&expertID=" +
        this.Adata.Auserid +
        "&groupId=" +
        this.Adata.AgroupId;
      this.loading = false;
      window.open(url, "_parent");
    },
    onSuccess(res) {
      if (res.msg === "success") {
        Message.success("数据导入成功！");
        this.refreshact(true);
        this.initState();
      } else {
        Message.error("导入失败，请检查文件格式！");
      }
      this.initAct();
    },
    Upload() {
      this.loading = true;
      // Message.success("正在导入");
      let url =
        "/system/Experts/importRate?activitiesID=" +
        this.Adata.Aid +
        "&expertID=" +
        this.Adata.Auserid +
        "&groupId=" +
        this.Adata.AgroupId;
      return url;
    },
    isGetPdf() {
      if (sessionStorage.getItem("score")) {
        let JSONfinish = JSON.parse(sessionStorage.getItem("score"));
        if (JSONfinish.finished) {
          // this.getPdfTable(this.Aname, this.datalist, this.Tname);
          this.loading = true;
          Message.success("正在导出");
          let url =
            "/system/Experts/exportPDF?activitiesID=" +
            this.Adata.Aid +
            "&expertID=" +
            this.Adata.Auserid +
            "&groupId=" +
            this.Adata.AgroupId;
          this.loading = false;
          window.open(url);
        } else {
          // setRowspan(1);
          this.$alert(
            "<span style='color:red'>请先点击“提交评分”按钮，才可导出PDF。</span>",
            {
              dangerouslyUseHTMLString: true,
            }
          );
        }
      }
    },
    showEditEmpView_show(row, index) {
      this.title_show =
        "学生 " +
        this.datalist.participatesList[index].student.name +
        " 详细信息";
      // this.title_show = "学生详细信息";
      this.dialogVisible_show = true;
      let q = this.datalist.infoItems.length;
      let w = this.datalist.infosList.length;
      this.dialogdata = {};
      this.dialogdata.idCode = this.datalist.participatesList[index].code;
      this.dialogdata.name = this.datalist.participatesList[index].student.name;
      for (var j = 0; j < w; j++) {
        if (row["id"] == this.datalist.infosList[j]["participantID"]) {
          for (var k = 0; k < q; k++) {
            if (
              this.datalist.infosList[j]["infoItemID"] ==
              this.datalist.infoItems[k]["id"]
            ) {
              var name = this.datalist.infoItems[k]["name"];
              this.$set(
                this.dialogdata,
                "info" + name,
                this.datalist.infosList[j]["content"]
              );
            }
          }
        }
      }
    },
    //表头换行
    renderheader(h, { column, $index }) {
      //先把数据以 | 拆分
      let initArr = column.label.split("|");
      let isOne = "";
      if (initArr[3] === "1") {
        isOne = initArr[1] + initArr[4];
      } else {
        isOne = initArr[1] + initArr[2] + initArr[3] + initArr[4];
      }

      return h("span", {}, [
        [h("span", {}, initArr[0]), h("br"), h("span", {}, isOne)],
        // ),
      ]);
    },
    //表头样式
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:10px;text-align:center";
    },
    rowStyle() {
      return "text-align:center";
    },
    initState() {
      let n = this.datalist.participatesList.length;
      let m = this.datalist.scoreitems.length;
      let p = this.datalist.scoresListNoExpert.length;
      let e = this.datalist.scoresListByExpert.length;
      let q = this.datalist.infoItems.length;
      let w = this.datalist.infosList.length;
      if (this.datalist.finished == true) {
        this.$alert("该活动已提交评分!", {
          type: "warning",
          center: true,
        });
      }
      for (var i = 0; i < n; i++) {
        this.datalist.participatesList[i]["showSave"] = false;
        this.datalist.participatesList[i]["sum"] = 0;
        var sum = 0;
        for (var j = 0; j < m; j++) {
          for (var k = 0; k < p; k++) {
            if (
              (this.datalist.scoreitems[j]["id"] ==
                this.datalist.scoresListNoExpert[k]["scoreItemID"]) &
              (this.datalist.participatesList[i]["id"] ==
                this.datalist.scoresListNoExpert[k]["participantID"]) &
              (this.datalist.scoreitems[j]["byexpert"] == true)
            ) {
              this.datalist.participatesList[i]["score" + j] =
                this.datalist.scoresListNoExpert[k]["score"];
            }
          }
          for (var l = 0; l < e; l++) {
            if (
              (this.datalist.scoreitems[j]["id"] ==
                this.datalist.scoresListByExpert[l]["scoreItemID"]) &
              (this.datalist.participatesList[i]["id"] ==
                this.datalist.scoresListByExpert[l]["participantID"])
            ) {
              if (j == m - 1) {
                this.$set(
                  this.datalist.participatesList[i],
                  "sum",
                  this.datalist.scoresListByExpert[l]["score"] - "0"
                );
              } else {
                this.$set(
                  this.datalist.participatesList[i],
                  "score" + j,
                  this.datalist.scoresListByExpert[l]["score"]
                );
              }
            }
          }
        }
        if (this.datalist.participatesList[i]["sum"] == 0) {
          for (var l = 0; l < m; l++) {
            if (this.datalist.participatesList[i]["score" + l]) {
              sum +=
                (this.datalist.participatesList[i]["score" + l] - "0") *
                this.datalist.scoreitems[l].coef;
            }
          }
          this.$set(this.datalist.participatesList[i], "sum", sum);
        }
        for (var s = 0; s < w; s++) {
          if (
            this.datalist.participatesList[i]["id"] ==
            this.datalist.infosList[s]["participantID"]
          ) {
            for (var d = 0; d < q; d++) {
              if (
                this.datalist.infosList[s]["infoItemID"] ==
                this.datalist.infoItems[d]["id"]
              ) {
                var name = this.datalist.infoItems[d]["name"];
                if (this.datalist.infoItems[d]["display"] == true) {
                  this.$set(
                    this.datalist.participatesList[i],
                    "info" + name,
                    this.datalist.infosList[s]["content"]
                  );
                }
              }
            }
          }
        }
      }
    },
    onInputConfirm(row, index) {
      this.$set(this.datalist.participatesList[index], "showSave", true);
      let m = this.datalist.scoreitems.length;
      var sum = 0;
      for (var j = 0; j < m; j++) {
        if (row["score" + j]) {
          sum += (row["score" + j] - "0") * this.datalist.scoreitems[j].coef;
        }
      }
      row["sum"] = sum.toFixed(2);
      this.$store.dispatch("setScoreParticipatesList", this.datalist);
    },
    async saveScore(index, row) {
      this.$forceUpdate();
      let m = this.datalist.scoreitems.length;
      var a = [];
      for (var i = 0; i < m; i++) {
        if (this.datalist.scoreitems[i].byexpert == true) {
          if (i == m - 1) {
            a.push({
              activityid: this.datalist.scoreitems[i]["activityid"],
              id: this.datalist.scoreitems[i]["id"],
              score: row["sum"],
            });
          } else {
            a.push({
              activityid: this.datalist.scoreitems[i]["activityid"],
              id: this.datalist.scoreitems[i]["id"],
              score: row["score" + i] - "0",
            });
          }
        }
      }
      this.$set(this.outdata, "expertID", this.user.id);
      this.$set(this.outdata, "participantID", row["id"]);
      this.$set(this.outdata, "scoreList", a);
      await this.postRequest(
        "/system/Experts/save",
        JSON.stringify(this.outdata)
      ).then((resp) => {
        if (resp) {
          this.$message({
            type: "success",
            message: "保存成功!",
          });
        }
      });
      this.datalist.participatesList[index].showSave = false;
      this.$store.dispatch("setScoreParticipatesList", this.datalist);
    },
    initdata() {
      this.Tname = this.user.name;
      this.Adata.Auserid = this.user.id;
      let num = this.$route.query.keywords;
      let listActivityTemp = this.list.activitiesList;
      console.log(listActivityTemp);
      this.Adata.Aid = listActivityTemp[num].activityID;
      this.Adata.AgroupId = listActivityTemp[num].groupId;
      this.Aname = listActivityTemp[num].activityLists[0].name;
      this.groupName = listActivityTemp[num].groupName;
      this.groupId = listActivityTemp[num].groupId;
      this.comment = listActivityTemp[num].activityLists[0].comment;
    },
    async initAct() {
      if (this.list.count != 0) {
        this.$store.dispatch("initAct", this.Adata);
        // const value =  await getRequest("/system/Experts/score?activitiesID=" + this.Adata.Aid + '&expertID=' + this.Adata.Auserid + '&groupId=' + this.Adata.AgroupId)
        // if(value){
        //   this.datalist = value.extend
        //   console.log(this.datalist)
        //   this.initState()
        // }
      }
    },
    // 刷新
    refreshact(auto) {
      this.initAct();
      // this.$store.dispatch("initAct", this.Adata);
      if (this.$store.state.changeList === true) {
        this.clear();
        this.reload();
        // this.$store.state.changeList = false
        this.$store.dispatch("initchangeList");
      }
      if (auto === false) {
        Message.success("刷新成功！");
      }
    },
    commit() {
      let n = this.datalist.participatesList.length;
      let flag = 0;
      for (var i = 0; i < n; i++) {
        if (this.datalist.participatesList[i].showSave == true) {
          flag = 1;
          // this.$alert(
          //   "<span style='color:red'>有评分未保存，请先保存!</span>",
          //   {
          //     dangerouslyUseHTMLString: true,
          //   }
          // );

          this.$message({
            type: "error",
            duration: 0,
            showClose: true,
            message: "有评分未保存，请先保存!   ",
          });
          return;
        }
      }
      if (flag == 0) {
        this.$confirm("提交之后不可修改, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        })
          .then(async () => {
            this.clear();
            this.datalist.finished = true;
            sessionStorage.setItem("score", JSON.stringify(this.datalist));
            this.$forceUpdate();
            await this.postRequest(
              "/system/Experts/ChangeState?activityId=" +
                this.Adata.Aid +
                "&expertID=" +
                this.user.id +
                "&groupId=" +
                this.Adata.AgroupId +
                "&finished=" +
                this.datalist.finished
            ).then((resp) => {
              if (resp) {
                this.$message({
                  type: "success",
                  message: "提交成功!",
                });
                // this.reload();
              }
            });
            // this.$router.go(-1);//返回上一页
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消提交",
            });
          });
      }
    },
    // 定时刷新数据函数
    dataRefreh() {
      // 计时器正在进行中，退出函数
      if (this.intervalId != null) {
        return;
      }
      // 计时器为空，操作
      this.intervalId = setInterval(() => {
        this.refreshact(true);
      }, 120000);
    },
    // 停止定时器
    clear() {
      clearInterval(this.intervalId); //清除计时器
      this.intervalId = null; //设置为null
    },
  },
  beforeDestroy() {
    this.clear();
  },
};
</script>

<style lang="scss"> 
.content {
  width: 100%;
  height: 80px;
  display: flex;
}
.leftTitle {
  width: 20%;
}
.center {
  flex: 1;
  display: flex;
  flex-direction: column-reverse;

}

.centerTxtFirst {
  font-size: 25px;
  font-weight: bold;
  text-align: center;
  margin: 15px 0;
}
.centerTxtLast {
  text-align: center;
}
.rightTitle {
  display: flex;
  width: 20%;
  flex-direction: row-reverse;
}
.rightTitleTxt {
  font-size: 14px;
  margin: 20px 20px 10px 0;
}

.el-dialog {
  border-radius: 8px;
}
.dialog {
  margin: 10px 10px 10px 40px;
}
.el-table-column {
  white-space: pre-line;
}
.el-dialog__header {
  background-color: #4b8ffe;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}
.el-dialog__title {
  color: white;
  font-size: 1.4rem;
}
.el-dialog__close{
  color:black
}
.el-dialog__close :hover{
  color:gray
}
.tableStyle {
  margin-top: 10px;
}

/* 滚动条的宽度 */
::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}

/* 滚动条的滑块 */
::-webkit-scrollbar-thumb {
  background-color: #a1a3a9;
  border-radius: 3px;
}
.divider {
  height: 1px;
  width: 100%;
  background-color: #a1a3a9;
  margin-bottom: 5px;
}
.h-copy {
  color: #1277c2;
  cursor: pointer;
}
</style>
