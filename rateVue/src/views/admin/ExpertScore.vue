vue.prototype.$logs = window.console.log;
<template>
  <div>
    <div style="display: flex; justify-content: left">
      {{ ACNAME }}活动 {{ groupname }} 专家打分情况
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div><br/><br/></div>
    <div style="margin-top: 10px">
      <el-table
          :data="Scores"
          :model="Scores"
          stripe
          border
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%">
        <el-table-column
            align="left"
            label="专家姓名"
            min-width="10%">
          <template slot-scope="scope">
            <div v-for="(value,key) in scope.row.scoremap" v-if="value.name==='活动得分'">
              {{value.expertName}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="displaySequence"
            align="left"
            label="选手序号"
            min-width="10%">
        </el-table-column>
        <el-table-column
            prop="code"
            align="left"
            label="选手编号"
            min-width="10%">
        </el-table-column>
        <el-table-column
            prop="name"
            align="left"
            label="选手姓名"
            min-width="10%">
        </el-table-column>
        <el-table-column
            v-for="(v, i) in this.snotByEmap"
            prop="v"
            :label="v"
            min-width="10%" align="center">
          <template slot-scope="scope">
            <div v-for="(value,key) in scope.row.scoremap" v-if="key===i">
              {{value.score}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
            v-for="(v, i) in this.smap"
            v-if="v!=='活动得分'"
            :label="v"
            min-width="10%" align="center">
          <template slot-scope="scope">
            <div v-for="(value,key) in scope.row.scoremap" v-if="key===i">
              {{value.score}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
            v-for="(v, i) in this.smap"
            v-if="v==='活动得分'"
            :label="v"
            min-width="10%" align="center">
          <template slot-scope="scope">
            <div v-for="(value,key) in scope.row.scoremap" v-if="key===i">
              {{value.score}}
            </div>
          </template>
        </el-table-column>
        <!--        <el-table-column align="left" min-width="10%" label="操作">-->

        <!--        </el-table-column>-->
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
  name: "ExpertScore",
  data() {
    return{
      title: '',
      labelPosition: "left",
      showAdvanceSearchView: false,
      groupname: "",
      ACNAME:"",
      datalist:'',
      expertmap:[],
      smap:[],
      map:[],
      Scores:[],
      null_map:[],
      snotByEmap:[],
      loading: false,
      total: 0,
      page: 1,
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
    this.groupID = this.$route.query.groupID;
    this.activityID = this.$route.query.activityID;
    this.initEmps();
    this.groupname = this.$route.query.keyword_name;
    this.ACNAME=this.$route.query.keywords_name;
  },
  methods: {
    initEmps() {
      this.loading = true;
      let url =  "/participants/basic/parscore?page=" +
          this.page +
          "&size=" +
          this.size +
          "&groupID=" +
          this.groupID +
          "&activityID=" +
          this.activityID;
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          console.log(resp);
          for (var name in resp.data) {
            var value = resp.data[name];//序号
            //console.log(value);
            for (var v in value.map) {
              var vv = value.map[v];      //map acticityid
              //console.log(vv);
              for (var vvv in vv) {
                var parexpert = vv[vvv];   //expertid
                // console.log(parexpert);
                for (var i in parexpert) {
                  this.Scores.push(parexpert[i]);//pid
                }
                for (var v2 in value.null_map) {
                  var vv2 = value.null_map[v2]; //activityid
                  //  console.log(vv2);
                  for (var i in vv2) {
                    this.null_map.push(vv2[i]);   //pid
                  }
                }
              }
            }
            this.expertmap = value.emap;
            console.log(this.Scores);
            console.log(this.expertmap);
            // this.map=Object.value(this.expertmap)[1034];
            // console.log(this.map);
            console.log(this.null_map);
            this.smap = value.smap;
            this.snotByEmap = value.snotByEmap;
            this.total = resp.total;
          }
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
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    getExpertname(key){

    }
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

