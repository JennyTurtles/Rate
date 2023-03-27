<template>
  <div>
    <div style="display: flex; justify-content: left">
      {{ keywords_name }}活动 {{groupName}} 选手总成绩

      <div style="margin-left: auto">
        <el-button icon="el-icon-download" type="primary" @click="exportScore">
          导出选手评分
        </el-button>
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div><br/>
      标红分数：小于该项分数满分的60%
      <br/></div>
    <div style="margin-top: 10px">
      <el-table
          :data="score"
          :model="score"
          stripe
          border
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
<!--      <div style="display: flex;justify-content: flex-end;margin:10px 0">-->
<!--        <el-pagination-->
<!--            background-->
<!--            @current-change="currentChange"-->
<!--            @size-change="sizeChange"-->
<!--            layout="sizes, prev, pager, next, jumper, ->, total, slot"-->
<!--            :total="total">-->
<!--        </el-pagination>-->
<!--      </div>-->
    </div>

  </div>
</template>

<script>
import axios from 'axios'
import {Message} from "element-ui";
export default {
  name: "SalFinalScore",
  data() {
    return{
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
    }
  },
  computed: {
    user() {
      return this.$store.state.currentHr;//object信息
    }
  },
  created() {
  },
  mounted() {
    //this.init();//先获得评分项
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.mode = this.$route.query.mode;
    this.groupName = this.$route.query.groupName;
    this.flag = this.$route.query.flag;
    this.initEmps();
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
    exportScore(){
      this.loading=true;
      Message.success("正在导出");
      var url = ''
      if (typeof this.groupName === 'undefined')
        url = '/participants/basic/export_ac?activityID=' + this.keywords;
      else
        url = '/participants/basic/export_ac_group?activityID=' + this.keywords + '&groupName=' + this.groupName;
      // let url = '/participants/basic/export_ac?activityID=' + this.keywords;
      window.open(url, "_parent");
      this.loading=false;
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
</style>

