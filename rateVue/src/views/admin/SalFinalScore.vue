<template>
  <div>
    <div style="display: flex; justify-content: left">
      {{ keywords_name }}活动 {{groupName}} 选手总成绩

      <div style="margin-left: auto">
        <el-button icon="el-icon-download" type="primary" @click="exportExcel">
          导出选手评分
        </el-button>
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div>标红分数：小于该项分数满分的60%</div>
    <div style="margin-top: 15px">
      <span>请选择筛选依据：  </span>
      <el-select
          style="margin-right: 20px;width: 150px;"
          v-model="selectedGroupInfo"
      >
        <el-option
            v-for="(item,key,index) in groupInfoNums"
            :key="key"
            :label="key"
            :value="key">
        </el-option>
      </el-select>
      <template v-if="true">
        <span>请选择筛选值：</span>
        <el-select
            style="margin-right: 20px;width: 250px;"
            v-model="selectedSubGroupInfo"
            multiple
        >
          <el-option
              v-for="item in groupSubOfSelectedInfos"
              v-if="item != 'infoItemID'"
              :key="item"
              :label="item + '（' + groupInfoNums[selectedGroupInfo][item].length +'）人'"
              :value="item">
          </el-option>
        </el-select>
      </template>
      <el-button type="primary" @click="filterPar()">筛选</el-button>
      <el-button type="primary" @click="reset()">重置</el-button>
      <el-popover placement="bottom" style="float: right">
            <el-button slot="reference" type="success">
              显示列设置
            </el-button>
            <div class="column-display">
              <el-checkbox-group v-model="checkedTableColumns">
                <el-checkbox
                    v-for="column in columns"
                    :key="column.label"
                    :label="column.prop"
                >
                  {{ column.label }}
                </el-checkbox>
              </el-checkbox-group>
            </div>
          </el-popover>
      <el-button type="primary" @click="showDialog = true" style="float: right;margin-right: 10px">求加权和</el-button>
      <el-dialog
          title="求加权和"
          :visible.sync="showDialog"
          width="40%">
        <el-form :model="form" label-width="80px">
<!--          新增一个文本框：新列名称-->
          <div>
          新列名：<el-input v-model="form.newColumnName" placeholder="请输入新列名称" style="width: 350px;margin-bottom: 10px"></el-input>
          </div>
          <div>
          求和项：<el-select v-model="form.sumItem" placeholder="请选择" style="margin-right: 10px">
            <el-option
                v-for="(item,key,index) in groupInfoNums"
                :key="key"
                :label="key"
                :value="key">
            </el-option>
          </el-select>
          系数：<el-input v-model="form.newColumnName" placeholder="系数" style="width: 80px;margin-bottom: 10px"></el-input>
          </div>
          <div>
            求和项：<el-select v-model="form.sumItem" placeholder="请选择" style="margin-right: 10px">
            <el-option
                v-for="(item,key,index) in groupInfoNums"
                :key="key"
                :label="key"
                :value="key">
            </el-option>
          </el-select>
            系数：<el-input v-model="form.newColumnName" placeholder="系数" style="width: 80px;margin-bottom: 10px"></el-input>
          </div>
<!--          添加一个按钮：新增求和项，绿色按钮-->
          <el-button type="success">新增求和项</el-button>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="submitWeightedSum">确 定</el-button>
        </span>
      </el-dialog>

    </div>
    <div style="margin-top: 10px">
      <el-table
          ref = "excelTable"
          :data="score"
          :model="score"
          stripe
          border
          id='outTable'
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%">
        <el-table-column
            type="index"
            label="序号"
            min-width="10%">
        </el-table-column>
        <el-table-column
            prop="code"
            align="left"
            label="编号"
            min-width="10%">
        </el-table-column>
        <el-table-column
            prop="groupName"
            align="left"
            label="组名"
            min-width="10%">
        </el-table-column>
        <el-table-column
            prop="name"
            align="left"
            label="姓名"
            min-width="10%">
        </el-table-column>
        <el-table-column
            prop="totalscorewithdot"
            align="center"
            label="专家评分"
            min-width="10%">
        </el-table-column>
        <el-table-column
            sortable
            label="活动得分"
            align="center"
            min-width="10%"
            :sort-method="(a, b) => {
                      return Number(a.score)- Number(b.score);}"
            :sort-orders="['descending', 'ascending']">
          <template slot-scope="scope">
            <span v-if="scope.row.score<scope.row.fullScore*0.6" style="color: red">{{scope.row.score}}</span>
            <span v-else>{{scope.row.score}}</span>
          </template>
        </el-table-column>
        <el-table-column
            v-for="(v, i) in tmap"
            :prop="v"
            :label="v"
            :key="i"
            sortable
            min-width="10%" align="center"
            :sort-method="(a, b) => {
                      return Number(a.finalmap[i].score)- Number( b.finalmap[i].score);}"
            :sort-orders="['descending', 'ascending']">
          <template slot-scope="scope">
            <div v-for="(value,key) in scope.row.finalmap" v-if="key===i">
              <span v-if="value.score<value.fullScore*0.6" style="color: red">{{value.score}}</span>
              <span v-else>{{value.score}}</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
import axios from 'axios'
import {Message} from "element-ui";
import FileSaver from "file-saver";
import * as XLSX from 'xlsx'
import score from "@/views/teacher/Score.vue";
import optionGroup from "element-ui/packages/option-group";
export default {
  name: "SalFinalScore",
  data() {
    return{
      form: {
        weightedSum: '',
      },
      showDialog: false,
      selectedGroupInfo: '',
      groupInfoNums: {},
      groupSubOfSelectedInfos: [],
      selectedSubGroupInfo: [],
      flag:0,
      groupName: '',
      title: '',
      labelPosition: "left",
      showAdvanceSearchView: false,
      keywords_name: "",
      ACNAME:"",
      datalist:'',
      loading: false,
      total: 0,
      page: 1,
      score:[],
      tmap:[],
      map:[],
      emps:[],
      keywords: '', // 活动id
      keyword: '',
      size: 10,
      columns: [{ label: "性别", prop: "name", width: 100, show: true },
        { label: "民族", prop: "sex", width: 150, show: true },
        { label: "政治面貌", prop: "age", width: 100, show: true }],
    }
  },
  computed: {
    optionGroup() {
      return optionGroup
    },
    user() {
      return this.$store.state.currentHr;//object信息
    }
  },
  created() {
  },
  watch:{
    selectedGroupInfo:{//监听第一个下拉框的变化 信息项
      handler(val){
        //信息项和信息项的子选项都被选择了或者没有子信息项
        if(val != '') {
          //该信息项下的所有子信息项
          if (typeof this.groupInfoNums != 'undefined' && JSON.stringify(this.groupInfoNums) != '{}' && JSON.stringify(this.groupInfoNums[val]) != '{}') {

            this.groupSubOfSelectedInfos = Object.keys(this.groupInfoNums[val])
          }
        }
      }
    },
    selectedSubGroupInfo:{//第二个下拉框的变化 信息项的子信息项
      handler(val){
      }
    }
  },
  mounted() {
    //this.init();//先获得评分项
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.mode = this.$route.query.mode;
    this.groupName = this.$route.query.groupName;
    this.flag = this.$route.query.flag;
    this.groupID = this.$route.query.groupID;
    this.initEmps();
    this.initFitler();
  },
  methods: {

    initEmps() {
      this.loading = true;
      var url = ''
      if (typeof this.groupName === 'undefined')
        url = '/totalItem/basic/getFianl?activityID=' + this.keywords + '&page=' + 1 + '&size=' + 1000;
      else
        url = '/totalItem/basic/getFianlGroup?activityID=' + this.keywords + '&page=' + 1+ '&size=' + 1000 + '&groupName=' + this.groupName;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.score = [];
          this.emps = resp.data;
          this.total = resp.total;
          for(var name in resp.data){
            var value =resp.data[name];
            this.map = value.map;
            for(var i in this.map){
              if (this.mode === "secretary" && this.map[i].groupName !== this.groupName)
                continue
              this.score.push(this.map[i]);
            }
          }
          this.tmap=value.tmap;
          console.log(this.score)
          // 按照考试总分降序排列
          var key = '';
          for (var j in this.score[0].finalmap){
            if (this.score[0].finalmap[j].name === "考试总分")
              key = j;
          }
          this.score.sort(function(a,b){
            return b.finalmap[key].score-a.finalmap[key].score;
          });
        }
      });
    },
    initFitler(){
      this.selectedGroupInfo= ''
      this.groupInfoNums= {}
      this.groupSubOfSelectedInfos=[]
      this.selectedSubGroupInfo=[]
      let url = '/infoItem/basic/getAllInf?ID=' + this.keywords + '&groupID=' + (this.groupID == null ? 0 : this.groupID);
      this.getRequest(url).then((resp)=>{
        if(resp.code == 200){
          //存放infoItem
          var infoItems = resp.extend.infoItems
          if(resp.extend.infoItems.length === 0){
            this.$message.warning('该活动下没有未分组的选手！')
            return
          }
          for(var i = 0;i < infoItems.length;i ++){
            if(!(infoItems[i].name in this.groupInfoNums)){
              this.groupInfoNums[infoItems[i].name]={'infoItemID':infoItems[i].id}//将每一个信息项改为对象形式,再加上每个信息项的id
            }
            //如果每个信息项包含多个子信息项如报考专业包括电子xxx、xx开发等，将每个子信息项改为数组
            if(!(infoItems[i].content in this.groupInfoNums[infoItems[i].name])){
              this.groupInfoNums[infoItems[i].name][infoItems[i].content] = []
            }
            this.groupInfoNums[infoItems[i].name][infoItems[i].content].push(infoItems[i])
          }
          if(!this.groupNums){
            this.groupNums = Array.from(Array(10).keys(),n=>n+1)
          }
          this.dialogPartipicantGroups = true
        }
      })
    },
    filterPar(){
      let url = '/infoItem/basic/getFilteredFianlGroup?infoItemName=' + this.selectedGroupInfo + '&infoItemContent=' + this.selectedSubGroupInfo + '&activityID=' + this.keywords;
      this.getRequest(url).then((resp)=>{
        if (resp){
          this.emps = resp.data;
          this.total = resp.total;
          this.score= [];
          for(var name in resp.data){
            var value =resp.data[name];
            this.map = value.map;
            console.log(this.map)
            for(var i in this.map){
              if (typeof this.groupName !== 'undefined' && this.map[i].groupName !== this.groupName)
                continue
              this.score.push(this.map[i]);
            }
          }
          this.tmap=value.tmap;
          var key = '';
          for (var j in this.score[0].finalmap){
            if (this.score[0].finalmap[j].name === "考试总分")
              key = j;
          }
          this.score.sort(function(a,b){
            return b.finalmap[key].score-a.finalmap[key].score;
          });
        }
      })
    },
    reset(){
      this.initEmps()
      this.initFitler();
    },
    back(){
      const _this = this;
      var url = ""
      var query = ""
      if (this.mode === 'admin'){
        {
          if (typeof this.groupName === "undefined"){ // 此时是从活动管理进入的
            url = "/ActivitM/search"
          }else{ // 此时是从分组管理中进入的
            url = this.flag ? "/ActivitM/score" : "/ActivitM/table" // flag为1时是从评分进入的
            query = {
              keywords: this.keywords,
              keyword_name: this.keywords_name,
              mode: this.mode,
            }
          }
        }
      }else if (this.mode === "secretary"){
        url = "/secretary/ActManage"
      }
      _this.$router.push({
        path: url,
        query:query,
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
    // exportScore(){ // 基于后端的导出
    //   this.loading=true;
    //   Message.success("正在导出");
    //   var url = ''
    //   if (typeof this.groupName === 'undefined')
    //     url = '/participants/basic/export_ac?activityID=' + this.keywords;
    //   else
    //     url = '/participants/basic/export_ac_group?activityID=' + this.keywords + '&groupName=' + this.groupName;
    //   // let url = '/participants/basic/export_ac?activityID=' + this.keywords;
    //   window.open(url, "_parent");
    //   this.loading=false;
    // },
    exportExcel () {
      let xlsxParam = { raw: true }
      var wb = XLSX.utils.table_to_book(document.querySelector('#outTable'),xlsxParam)
      var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
      try {
        FileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '选手积分表.xlsx')
      } catch (e) { if (typeof console !== 'undefined') console.log(e, wbout) }
      return wbout
    },
  }
}
</script>

<style scoped>
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
.column-display {
  width: 180px;
}
</style>

