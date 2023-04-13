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
<!--      <el-popover placement="bottom" style="float: right">-->
<!--            <el-button slot="reference" type="success">-->
<!--              显示列设置-->
<!--            </el-button>-->
<!--            <div class="column-display">-->
<!--              <el-checkbox-group v-model="checkedTableColumns">-->
<!--                <el-checkbox-->
<!--                    v-for="column in columns"-->
<!--                    :key="column.label"-->
<!--                    :label="column.prop"-->
<!--                >-->
<!--                  {{ column.label }}-->
<!--                </el-checkbox>-->
<!--              </el-checkbox-group>-->
<!--            </div>-->
<!--          </el-popover>-->

    </div>
    <div style="margin-top: 10px">
      <el-table
          ref = "excelTable"
          :data="displayPars"
          :model="displayPars"
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
            align="center"
            min-width="10%">
        </el-table-column>
        <el-table-column
            v-for="(v, i) in displayItem"
            :prop="v.name"
            :label="v.name"
            :key="i"
            sortable
            min-width="10%" align="center"
            :sort-method="(a, b) => {
                      return Number(a.map[v.name])- Number(b.map[v.name])}"
            :sort-orders="['descending', 'ascending']">
          <template v-slot:header='scope' v-if="displayItem[i].source.indexOf('*') !== -1">
            <span>
              	  <el-tooltip
                      :aa="scope"
                      class="item"
                      effect="dark"
                      placement="top-start"
                  >
               <i class="el-icon-info" style="color: #4b8ffe"> </i>
               <div style="width: 200px" slot="content">
                {{displayItem[i].sourceName}}
               </div>
	                </el-tooltip>
              {{scope.column.label}}
	          </span>
          </template>
          <template slot-scope="scope">
            <div>
              <span v-if="typeof displayItem[i].passScore !== 'undefined' && scope.row.map[v.name] < displayItem[i].passScore"
                    style="color: red">{{scope.row.map[v.name]}}</span>
              <span v-else>{{scope.row.map[v.name]}}</span>
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
      displayItem: [],
      displayPars: [],
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
  },
  methods: {
    initEmps() {
      this.loading = true;
      var url = '/displayItem/allPar?activityID=' + this.keywords
      if (typeof this.groupID !== 'undefined')
        url += '&groupID=' + this.groupID
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.displayPars = resp.obj[0];
          this.displayItem = resp.obj[1];
          // console.log(this.displayPars) // 行信息
          // console.log(this.displayItem) // 列信息
          this.initFitler()
        }
      });
    },
    initFitler(){
      this.selectedGroupInfo= ''
      this.groupInfoNums= {}
      this.groupSubOfSelectedInfos=[]
      this.selectedSubGroupInfo=[]
      var infoItems = []
      // 只需要id,name,content
      for (var i in this.displayPars) {
        for (var key in this.displayPars[i].map) {
          infoItems.push({id: this.displayPars[i].id, name: key, content: this.displayPars[i].map[key]})
        }
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
    },
    filterPar(){
      var newPar = []
      for (var i in this.displayPars) {
        if (this.selectedSubGroupInfo.indexOf(this.displayPars[i].map[this.selectedGroupInfo]) !== -1){
            newPar.push(this.displayPars[i])
        }
      }
        this.displayPars = newPar
    },
    reset(){
      this.initEmps()
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
      }
      else if (this.mode === "secretary"){
        url = "/secretary/ActManage"
      }
      else if (this.mode === "secretarySub"){
          if (typeof this.groupName === "undefined"){ // 此时查看的是整个子活动的选手分数
              url = "/secretary/SubActManage"
              query = {
                  id: this.$route.query.backActID,
                  actName: this.$route.query.backActName,
                  mode: this.mode,
                  groupID:  this.$route.query.backGroupID,
                  groupName: this.groupName,
              }
          }else{
              url = "/ActivitM/table"
              query = {
                  keywords: this.keywords,
                  keyword_name: this.keywords_name,
                  mode: this.mode,
                  groupName: this.$route.query.backGroupName,
                  groupID: this.$route.query.backGroupID,
                  backID: this.$route.query.backBackID,
                  backActName: this.$route.query.backActName,
              }
          }

      }
      else if (this.mode === "adminSub"){
          url = "/ActivitM/SubActManage"
          query = {
              id: this.$route.query.backID,
          }
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
    exportExcel () {
      let xlsxParam = { raw: true }
      var wb = XLSX.utils.table_to_book(document.querySelector('#outTable'),xlsxParam)
      console.log(wb)
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

