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
          <template slot-scope="scope" >
              <el-button
                  @click="showEnterView(scope.row, scope.$index)"
                  style="padding: 4px"
                  type="primary"
                  icon="el-icon-edit"
                  :disabled="false"
                  v-if="scope.row.activityLists[0].status == 'open' && scope.row.isEnter"
              >进入</el-button
              >
              <template v-else-if="scope.row.activityLists[0].status == 'open' && !scope.row.isEnter">
                <span>还剩{{scope.row.remainder}}</span>
                <el-button
                    style="padding: 4px;margin-left: 6px"
                    type="primary"
                    icon="el-icon-edit"
                    disabled
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
import {getRequest} from "@/utils/api";

export default {
  name: "actList",
  data() {
    return {
      tablelist:[],
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
  created() {
    this.$store.dispatch('initsize',JSON.parse(localStorage.getItem('user')).id).then(()=>{
      this.tablelist = this.tableDataHandle()
    })
  },
  computed: {
    tableData() {
      return this.tableDataHandle()
    },
  },
  mounted(){
    let that = this
    if(this.timer == null){
      this.timer = setInterval(()=>{
        that.nowTime = new Date()
        that.tablelist = that.tableData
      },1000)
    }
  },
  beforeDestroy(){
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    tableDataHandle(){
      var temp = JSON.parse(sessionStorage.getItem("peract")).activitiesList
      temp.map(item => { //判断是否有父活动 显示名字
        if(item.activityLists[0].parentName != '' && item.activityLists[0].parentName != null){
          item.activityLists[0].name = item.activityLists[0].name + '(' + item.activityLists[0].parentName + ')'
        }
      })
      //根据时间做筛选 isShow控制是否显示在专家活动列表中 isenter控制是否可进入（按钮可点）
      for(var i = 0;i < temp.length; i++){
        var time = new Date(temp[i].activityLists[0].startDate)
        let time1 = time.getTime() //开始时间
        let time2 = this.nowTime.getTime()//当前时间
        let visibleTime = new Date(temp[i].activityLists[0].visibleDate).getTime()//可见时间
        let enterTime = new Date(temp[i].activityLists[0].enterDate).getTime()//可进入时间
        temp[i].isShow = true//控制进入按钮是否显示
        if((time1 - time2) / (1000) < 0){//已经开始了
          temp[i].remainder = ''
          temp[i].isEnter = true
          continue;
        }
        if(visibleTime == '' || visibleTime == null){//任何时候都可见
          temp[i].isShow = true
        }else if(visibleTime - time2 > 0){//可见时间大于当前时间，说明不可见
          temp[i].isShow = false
          temp[i].isEnter = false
          continue;
        }
        temp[i].isEnter = false//可见不可进
        if(enterTime == '' || enterTime == null){//任何时候都可进入
          temp[i].isEnter = true
        }else if(enterTime - time2 <= 0){//可进入时间小于等于当前时间，说明已经可以进入
          temp[i].isEnter = true
          continue;
        }else {//不可进入但可见 需要有倒计时
          temp[i].remainder = ''
          var hourUnit = 60 * 60 * 1000
          var secondUnit = 60 * 1000
          var enterTimeMinusNowDate = enterTime - time2
          if(Math.floor(enterTimeMinusNowDate / hourUnit) >= 1){//有小时
            temp[i].remainder += Math.floor(enterTimeMinusNowDate / hourUnit) + '小时'
          }
          if(Math.floor(enterTimeMinusNowDate / secondUnit) >= 1){//有分
            if(Math.floor(enterTimeMinusNowDate / hourUnit) >= 1){//有小时
              var se = Math.floor(enterTimeMinusNowDate / hourUnit) * hourUnit
              temp[i].remainder += Math.floor((enterTime - time2 - se) / secondUnit) + '分'
            }else {
              temp[i].remainder += Math.floor(enterTimeMinusNowDate / secondUnit) + '分'
            }
          }
          if(Math.floor(enterTimeMinusNowDate / 1000) >= 1){//有秒
            var se = 0
            if(Math.floor(enterTimeMinusNowDate / secondUnit) >= 1){
              se = enterTime - time2 - Math.floor(enterTimeMinusNowDate / secondUnit) * secondUnit
            }else {
              se = enterTime - time2
            }
            temp[i].remainder += Math.floor(se / 1000) + '秒'
          }else {
            temp[i].remainder = '0秒'//初始化是空字符串所以额外判断一下
          }
        }
      }
      let newTemp = []
      for(let j = 0;j < temp.length;j++){
        if(temp[j].isShow){
          newTemp.push(temp[j])
        }
      }
      return newTemp;
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