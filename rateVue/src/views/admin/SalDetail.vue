<template>
  <div>
    <div style="display: flex; justify-content: left">
      {{ ACNAME }}活动 {{ keywords_name }} 学生成绩
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div><br/><br/></div>
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          :model="emps"
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
            width="100px">
        </el-table-column>
        <el-table-column
            prop="code"
            align="left"
            label="编号"
            width="200px">
        </el-table-column>
        <el-table-column
            prop="idnumber"
            align="left"
            label="身份证号码"
            width="200px">
        </el-table-column>
        <el-table-column
            prop="name"
            align="left"
            label="姓名"
            width="100px">
        </el-table-column>
        <el-table-column
            prop="score"
            sortable
            label="得分"
            align="center"
            width="80px">
        </el-table-column>
        <el-table-column align="left" min-width="10%" label="操作">

        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: flex-end;margin:10px 0">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios'
import {Message} from "element-ui";
export default {
  name: "SalPar",
  data() {
    return{
      title: '',
      labelPosition: "left",
      showAdvanceSearchView: false,
      keywords_name: "",
      ACNAME:"",
      datalist:'',
      loading: false,
      total: 0,
      page: 1,
      emps:[],
      keywords: '',
      keyword: '',
      activityID: '',
      groupID: '',
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
    this.groupID = this.$route.query.groupID;
    this.activityID = this.$route.query.activityID;
    this.initEmps();
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.ACNAME=this.$route.query.keywords_name;
  },
  methods: {
    initEmps() {
      this.loading = true;
      //console.log(this.groupID);
      let url = '/participants/basic/?page=' + this.page + '&size=' + this.size+ '&groupID=' + this.groupID+ '&activitiesID=' + this.activityID;
      //console.log(url);
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          //console.log("aha",resp);
          this.emps = resp.data;
          this.total = resp.total;
          //console.log("total",this.total);
        }
      });
    },
    back(){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/score",
        query: {
          keywords: this.activityID,
          keyword_name: this.ACNAME
        },
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

