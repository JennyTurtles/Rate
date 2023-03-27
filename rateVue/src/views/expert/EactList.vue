<template>
  <div>
    <div class="acttitle">查看需要评分的活动</div>
    <div style="margin: 20px">
      <el-table
        :data="tablelist"
        :header-cell-style="rowClass"
        tooltip-effect="dark"
        stripe
        border
        highlight-current-row
        v-loading="loading"
        element-loading-text="正在加载..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.12)"
      >
        <el-table-column
          v-for="(item, index) in tableHeader"
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          :align="item.align"
        >
        </el-table-column>
        <el-table-column align="center" min-width="150px" label="操作">
          <template slot-scope="scope">
            <el-button
              @click="showEnterView(scope.row, scope.$index)"
              style="padding: 4px"
              type="primary"
              icon="el-icon-edit"
              v-if="scope.row.activityLists[0].status == 'open' && (scope.row.seconds <= 30 * 60)"
              >进入</el-button
            >
            <template v-else-if="scope.row.activityLists[0].status == 'open' &&
                                (scope.row.seconds > 30 * 60) && (scope.row.seconds <= 60 * 60)">
              <span>还剩{{scope.row.remainder}}</span>
              <el-button
                  style="padding: 4px;margin-left: 6px"
                  type="primary"
                  icon="el-icon-edit"
                  :disabled="true"
              >进入</el-button
              >
            </template>
            <template v-else-if="scope.row.activityLists[0].status != 'open'">
              <el-button
                  style="padding: 4px;"
                  type="primary"
                  icon="el-icon-edit"
                  disabled
                  title="该活动未在进行时间范围内"
              >进入</el-button
              >
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "actList",
  data() {
    return {
      timer:null,
      nowTime:new Date(),
      title: "",
      show: false,
      loading: false,
      activitiesList: [],
      tableHeader: [
        {
          prop: "activityLists[0].startDate",
          align: "center",
          label: "开始时间",
          width: "180px",
        },
        {
          prop: "activityLists[0].name",
          align: "center",
          label: "活动名称",
          width: "250px",
        },
        {
          prop: "groupName",
          align: "center",
          label: "组名",
          width: "180px",
        },
      ],
    };
  },
  computed: {
    tablelist() {
      var temp = JSON.parse(sessionStorage.getItem("activitiesList"))
      let flage = []
      for(var i = 0;i < temp.length; i++){
        var time = new Date(temp[i].activityLists[0].startDate)
        let time1 = time.getTime()
        let time2 = this.nowTime.getTime()
        if((time1 - time2) / (1000) < 0){//已经开始了
          temp[i].seconds = 0
          temp[i].remainder = ''
          continue;
        }//6282
        // console.log(time1 - time2)
        if((time1 - time2) / (60 * 60 * 1000) > 1){//大于一个小时
          flage.push(i)
          continue;
        }
        temp[i].remainder = ''
        if(Math.floor((time1 - time2) / ( 60 * 1000)) >= 1){//有分
          if(Math.floor((time1 - time2) / ( 60 * 1000)) >= 30){
            temp[i].remainder += Math.floor((time1 - time2) / ( 60 * 1000)) - 30 + '分'
          }
        }
        if(Math.floor((time1 - time2) / 1000) >= 1){//有秒
          var se = 0
          if(Math.floor((time1 - time2) / ( 60 * 1000)) >= 1){
            se = time1 - time2 - Math.floor((time1 - time2) / ( 60 * 1000)) * 60 * 1000
          }else {
            se = time1 - time2
          }
          temp[i].remainder += Math.floor(se / 1000) + '秒'
        }
        temp[i].seconds = Math.floor((time1 - time2) / 1000)
      }
      for (let i = 0;i< flage.length;i++){
        temp.splice(flage[i],1)
      }
      // console.log(temp)
      return temp;
    },
  },
  mounted(){
    let that = this
    if(this.timer == null){
      this.timer = setInterval(()=>{
        that.nowTime = new Date()
      },1000)
    }
  },
  beforeDestroy(){
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    dataFormat(value){
      var year = value.getFullYear();
      var month = value.getMonth() + 1 < 10 ? "0" + (value.getMonth() + 1) : value.getMonth() + 1;
      var day = value.getDate() < 10 ? "0" + value.getDate() : value.getDate();
      var hours = value.getHours() < 10 ? "0" + value.getHours() : value.getHours();
      var minutes = value.getMinutes() < 10 ? "0" + value.getMinutes() : value.getMinutes();
      var seconds = value.getSeconds() < 10 ? "0" + value.getSeconds() : value.getSeconds();
      return [year,month,day,hours,minutes,seconds];
    },
    //表头样式
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:10px;text-align:center";
    },
    showEnterView(data, index) {
      sessionStorage.removeItem('score')
      this.$router.push({
        path: "/Expert/peract/score",
        query: {
          keywords: index,
        },
      });
      
    },
  },
};
</script>
<style lang="css" scoped>
.acttitle {
  display: flex;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  margin: 20px;

}
</style>