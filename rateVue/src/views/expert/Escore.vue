<template>
  <div>
    <!-- 表名 -->
    <div class="content" v-if="datalist != ''">
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
                  ref="uploadExcel"
                  :disabled="datalist.finished == true"
              >
                <el-button type="primary" icon="el-icon-upload2" @click.stop="uploadButton" :disabled="datalist.finished == true"
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
            min-width="110px"
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
            min-width="75px"
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
            <span
                v-if="((v.contentType.indexOf('pdf') >= 0 || v.contentType.indexOf('zip') >= 0
                            || v.contentType.indexOf('jpg') >= 0))">{{scope.row["info" + v.name] | fileNameFilter}}</span>
            <span v-else v-html="scope.row['info' + v.name]"></span>
          </template>
        </el-table-column>
        <el-table-column
            v-for="(value, idx) in datalist.scoreitems"
            ref="setTableRef"
            :key="idx"
            v-if="value.name != '活动得分'"
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
            min-width="84px"
            :render-header="renderheader"
        >
          <template slot-scope="scope">
            <el-input
                placeholder="请打分"
                v-model.trim="scope.row['score' + idx]"
                :disabled="value.byexpert == false || datalist.finished == true"
                clearable
                @input="onInputConfirm(scope.row, scope.$index,value,idx)"
            ><span>{{ scope.row["score" + idx] }}</span>
            </el-input>
          </template>
        </el-table-column>
        <el-table-column
            v-for="(value, idx) in datalist.scoreitems"
            :key="value.id"
            v-if="value.name == '活动得分'"
            :label="'总评分'"
            min-width="80px"
            align="center"
        >
          <template slot-scope="scope">
            <span>{{ scope.row["sum"] }}</span>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center"
            min-width="150px"
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
                  <span>{{ dialogdata["info" + val.name].content }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="10" :offset="1">
                <el-form-item
                    v-for="(val, idx) in datalist.infoItems"
                    :key="idx"
                    :label="val.name + ' :'"
                    v-if="dialogdata['info' + val.name] && val.name == '毕业单位'"
                >
                  <span>{{ dialogdata["info" + val.name].content }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item
                v-for="(val, idx) in datalist.infoItems"
                :key="idx"
                :label="val.name + ' :'"
                width="130px"
                v-if="
                val.name != '报考专业' &&
                val.name != '毕业单位'
              "
            >
              <span v-if="((val.contentType.indexOf('pdf') >= 0 || val.contentType.indexOf('zip') >= 0
                            || val.contentType.indexOf('jpg') >= 0)) && dialogdata['info' + val.name]"
                    style="color:gray;font-size:14px;text-decoration:none;cursor:pointer;"
                    onmouseover="this.style.color = 'blue'"
                    onmouseleave="this.style.color = 'gray'"
                    @click="downloadInfoItems(val)">
                {{dialogdata["info" + val.name].content | fileNameFilter}}
              </span>
              <span v-else-if="(val.contentType.indexOf('textarea') >= 0 || val.contentType.indexOf('textbox') >= 0) && dialogdata['info' + val.name]">
                {{ dialogdata["info" + val.name].content }}
              </span>
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
import axios from "axios";
export default {
  name: "score",
  inject: ["reload"],
  data() {
    return {
      successTimer:null,
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
    // this.initPage();
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
  // mounted() {
  //   this.user = JSON.parse(localStorage.getItem("teacher"));
  //   this.initdata();
  //   this.$nextTick(() => {
  //     this.windowScreenHeight = window.innerHeight - 210;
  //   });
  //   if (sessionStorage.getItem("score")) {
  //     let initscore = JSON.parse(sessionStorage.getItem("score"));
  //     this.datalist = initscore;
  //   } else {
  //     this.fullscreenLoading = true;
  //     this.initAct();
  //     setTimeout(() => {
  //       this.datalist = this.datal;
  //       this.initState();
  //       sessionStorage.setItem("score", JSON.stringify(this.datalist));
  //       // if (!this.datalist.finished) {
  //       this.reload();
  //       // }
  //       this.fullscreenLoading = false;
  //     }, 1500);
  //   }
  // },
  mounted() {
      this.initPage()
    },
  // this.$nextTick(() => {
  //   this.windowScreenHeight = window.innerHeight - 210;
  // });
  // this.user = JSON.parse(localStorage.getItem("teacher"));
  //
  // this.initdata();
  // if (sessionStorage.getItem("score")) {
  //   let initscore = JSON.parse(sessionStorage.getItem("score"));
  //   this.datalist = initscore;
  // } else {
  //   this.fullscreenLoading = true;
  //   this.initAct();
  //   setTimeout(() => {
  //     this.datalist = this.datal;
  //     this.initState();
  //     sessionStorage.setItem("score", JSON.stringify(this.datalist));
  //     if (!this.datalist.finished) {
  //       this.reload();
  //     }
  //     this.fullscreenLoading = false;
  //   }, 700);
  // }
  filters:{
    fileNameFilter:function(data){//将上传的材料显示出来
      if(data == null || data == ''){
        return ''
      }else{
        var arr= data.split('/')
        return  arr.reverse()[0]
      }
    }
  },
  methods: {
    async initPage(){
      this.user = JSON.parse(localStorage.getItem("teacher"));
      this.initdata();
      this.$nextTick(() => {
        this.windowScreenHeight = window.innerHeight - 210;
      });
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
          // if (!this.datalist.finished) {
          this.reload();
          // }
          this.fullscreenLoading = false;
        }, 1500);
      }
      // this.user = JSON.parse(localStorage.getItem("teacher"));
      // this.$store.dispatch('initsize',this.user.id).then(()=>{
      //   this.list = JSON.parse(sessionStorage.getItem("peract"));
      //   this.initdata();
      //   this.$nextTick(() => {
      //     this.windowScreenHeight = window.innerHeight - 210;
      //   });
      //   if (sessionStorage.getItem("score")) {
      //     let initscore = JSON.parse(sessionStorage.getItem("score"));
      //     this.datalist = initscore;
      //   } else {
      //     this.fullscreenLoading = true;
      //     this.initAct()
      //     setTimeout(() => {
      //       this.datalist = this.datal;
      //       this.initState();
      //       sessionStorage.setItem("score", JSON.stringify(this.datalist));
      //       // if (!this.datalist.finished) {
      //       this.reload();
      //       // }
      //       this.fullscreenLoading = false;
      //       console.log(this.datalist.scoreitems)
      //     }, 1500);
      //   }
      // })
    },
    downloadInfoItems(data){//下载证明材料
      const fileName = data.content.split('/').reverse()[0]
      axios({
        url:"/paper/basic/download?fileUrl=" + data.content + "&fileName=" + fileName,
        method: 'get',
        responseType: 'blob',
      }).then(res => {
        const filestream = res.data;  // 返回的文件流
        // {type: 'application/vnd.ms-excel'}指定对应文件类型为.XLS (.XLS的缩写就为application/vnd.ms-excel)
        const blob = new Blob([filestream]);
        const a = document.createElement('a');
        const href = window.URL.createObjectURL(blob); // 创建下载连接
        a.href = href;
        a.download = decodeURL(fileName );
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a); // 下载完移除元素
        window.URL.revokeObjectURL(href); // 释放掉blob对象
      })
    },
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
    uploadButton(){
      this.$confirm('请注意计算好学生的总评分再导入，无总评分的行将被认为未评分。系统将使用excel里的数据覆盖浏览器界面上的数据，而不会融合两者的数据。请确保excel文件里包含所有评分。',{
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(()=>{
        this.$refs['uploadExcel'].$refs['upload-inner'].handleClick()
      }).catch(()=>{})
    },
    async onSuccess(res) {
      if (res.msg === "success") {
        this.$confirm('您的评分已保存到数据库，为确保无误，请核对网页上显示的分数以及导出的pdf文件中的分数与上传的excel文件中分数是否一致，谢谢！',{
          confirmButtonClass:'确认',
          showCancelButton: false
        })
      }else if(res.obj == '有分数超过满分！'){
        Message.error(`${res.obj}请重新上传！`);
        return;
      } else if(res.msg === "fail"){
        Message.error("导入失败，请检查文件格式！");
        return;
      }else if(res.msg === 'nullRow'){
        var nullarr = res.obj.split(',')
        nullarr.pop()
        nullarr = nullarr.map(item=>{
          if(item != ','){
            return parseInt(item)
          }
        })
        nullarr.push(-1)
        var preFlag = 0
        var str = ''
        var addFlag = 0
        for(var i = 0;i < nullarr.length - 1;i ++){
          if(nullarr[i + 1] - nullarr[i] === 1 && addFlag == 0){
            preFlag = i
            addFlag = 1
          }else if(addFlag == 1 && nullarr[i + 1] - nullarr[i] !== 1){//可以添加进去了
            str += nullarr[preFlag] + '-' + nullarr[i] + ','
            addFlag = 0
          }else if(addFlag == 0 && nullarr[i + 1] - nullarr[i] !== 1){
            str += nullarr[i] + ','
          }
        }
        str = str.substr(0,str.length - 1)
        this.$confirm(`部分学生由于无完整的分数，系统认为未评分，请确认。如果有误，请将分数填写完整重新上传。
            序号为${str}的共计${nullarr.length - 1}个学生未评分，请确认。`,{
          confirmButtonClass:'确认',
          showCancelButton: false
        })
      }
      sessionStorage.removeItem("score")
      this.initAct()
      if(this.successTimer){
        this.successTimer = null
      }
      this.successTimer = setTimeout(()=>{
        this.datalist = this.datal
        // this.initState();
      },300)
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
      let routeUrl = this.$router.resolve({
        path:"/teacher/tperact/InformationDetails",
        query: {
          activityID: this.Adata.Aid,
          IDNumber: row.student.idnumber,
        },
      })
      window.open(routeUrl.href)    },
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
      console.log(this.datalist)
      let par = this.datalist.participatesList.length;
      let score = this.datalist.scoreitems.length;
      let scoreNoExpert = this.datalist.scoresListNoExpert.length;
      let scoreByExpert = this.datalist.scoresListByExpert.length;
      let infoitems = this.datalist.infoItems.length;
      let infos = this.datalist.infosList.length;
      for (var i = 0; i < par; i++) {
        this.datalist.participatesList[i]["showSave"] = false;
        this.datalist.participatesList[i]["sum"] = 0;
        var sum = 0;
        for (var j = 0; j < score; j++) {
          for (var k = 0; k < scoreNoExpert; k++) {
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
          for (var l = 0; l < scoreByExpert; l++) {
            if (
                (this.datalist.scoreitems[j]["id"] ==
                    this.datalist.scoresListByExpert[l]["scoreItemID"]) &
                (this.datalist.participatesList[i]["id"] ==
                    this.datalist.scoresListByExpert[l]["participantID"])
            ) {
              if (j == score - 1) {
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
          for (var l = 0; l < score; l++) {
            if (this.datalist.participatesList[i]["score" + l]) {
              sum +=
                  (this.datalist.participatesList[i]["score" + l] - "0") *
                  this.datalist.scoreitems[l].coef;
            }
          }
          this.$set(this.datalist.participatesList[i], "sum", sum);
        }
        for (var s = 0; s < infos; s++) {
          if (
              this.datalist.participatesList[i]["id"] ==
              this.datalist.infosList[s]["participantID"]
          ) {
            for (var d = 0; d < infoitems; d++) {
              if (
                  this.datalist.infosList[s]["infoItemID"] ==
                  this.datalist.infoItems[d]["id"]
              ) {
                var name = this.datalist.infoItems[d]["name"];
                if (this.datalist.infoItems[d]["display"] == true) {
                  this.$set(
                      this.datalist.participatesList[i],
                      "info" + name,
                      this.datalist.infosList[s]["content"].replace(/<br>/g,"\n").replace(/' '/g,"\s")
                  );
                }
              }
            }
          }
        }
      }
      if (this.datalist.finished == true) {
        this.$alert("该活动已提交评分!", {
          type: "warning",
          center: true,
        })
      }
    },
    onInputConfirm(row, index,value,idx) {//idx是scoreitem中的索引，index是选手所在的索引
      this.$set(this.datalist.participatesList[index], "showSave", true);
      let m = this.datalist.scoreitems.length;
      //找到没修改前的分数
      var firstscore = this.datalist.scoresListByExpert.find(
          (cur) => {
            if(cur.participantID == row.id && cur.scoreItemID == value.id){
              return cur.score
            }else {
              return 0
            }
          })
      var sum = 0;
      for (var j = 0; j < m; j++) {
        if (row["score" + j] && this.datalist.scoreitems[j].name != '活动得分') {
          sum += (row["score" + j] - "0") * this.datalist.scoreitems[j].coef;
        }
      }
      //判断修改的单个评分项是否超过这个评分项的满分
      if(this.judgeScore(this.datalist.participatesList[index]['score' + idx],idx)){
        if(JSON.stringify(sum).indexOf('.') >= 0){
          row["sum"] = sum.toFixed(2);
        }else {
          row["sum"] = sum
        }
        this.$store.dispatch("setScoreParticipatesList", this.datalist);
      }else {
        //如果超过满分就把之前的分数和sum重新展示在页面上
        this.$message.warning('分数超过满分！')
        this.datalist.participatesList[index]['score' + idx] = 0
        var sumscore = 0;
        for (var j = 0; j < m; j++) {
          if (row["score" + j] && this.datalist.scoreitems[j].name != '活动得分') {
            sumscore += (row["score" + j] - "0") * this.datalist.scoreitems[j].coef;
          }
        }
        if(JSON.stringify(sumscore).indexOf('.') >= 0){
          row["sum"] = sumscore.toFixed(2);
        }else {
          row["sum"] = sumscore
        }
      }
    },
    judgeScore(score,scoreitemidx){//判断分数有没有超过满分
      //得到每一项评分项的满分 进行判断
      if(score <= this.datalist.scoreitems[scoreitemidx].score){
        return true
      }else {
        return false
      }
    },
    //计算评分项满分 包括所有评分项*系数
    judgeFullScore(sum,scoreitemidx){
      var fullScore = this.datalist.scoreitems.reduce((pre,cur) => {
        if(cur.name != "活动得分"){
          return cur.coef * cur.score + pre
        }else {
          return 0 + pre
        }
      },0)
      if(sum <= fullScore){
        return true
      }else {
        return false
      }
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
      }
    },
    // 刷新
    refreshact(auto) {
      this.initAct();
      // this.$store.dispatch("initAct", this.Adata);
      // console.log(this.$store.state.changeList)
      if (this.$store.state.changeList === true) {
        this.clear();
        this.datalist = this.datal
        // this.reload();
        // this.$store.commit('INIT_initchangeList',false)
        // this.$store.state.changeList = false
        this.$store.dispatch("initchangeList");
      }
      if(this.datalist.finished){//提交了
        this.watchFinished()
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
              // this.clear();
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
    async watchFinished(){
      this.getRequest("/system/Experts/getState?activitiesID=" +this.Adata.Aid + '&expertID=' + this.user.id + '&groupId=' + this.Adata.AgroupId).then((resp)=>{
        if(resp.code == 200){
          if (resp.extend.success === false){//被退回
            this.$message.warning("评分被退回，页面将刷新")
            sessionStorage.removeItem('score')
            this.reload()
          }
        }
      })
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
        if(this.datalist.finished){//提交了
          this.watchFinished()
        }
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
*{
  margin: 0;
  padding: 0;
}
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
