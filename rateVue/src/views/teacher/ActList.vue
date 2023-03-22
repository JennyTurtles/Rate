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
                v-if="scope.row.activityLists[0].status == 'open'"
            >进入</el-button
            >
            <el-button
                style="padding: 4px"
                type="primary"
                icon="el-icon-edit"
                disabled
                title="该活动未在进行时间范围内"
                v-else
            >进入</el-button
            >
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
      user:{

      },
      title: "",
      show: false,
      loading: false,
      activitiesList: [],

      tableHeader: [
        {
          prop: "activityLists[0].startDate",
          align: "center",
          label: "开始日期",
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
  computed: {},
  mounted(){
    this.user = JSON.parse(localStorage.getItem("user"))
    // new Promise((resolve, reject) => {
    this.$store.dispatch('initsize',this.user.id).then(()=>{
      this.tablelist = JSON.parse(sessionStorage.getItem("peract")).activitiesList
      for(var i = 0;i < this.tablelist.length; i++){
        var time = new Date(this.tablelist[i].activityLists[0].startDate)
        var year = time.getFullYear()
        var month = time.getMonth() + 1
        var date = time.getDate()
        if(month < 10){
          month = "0" + month
        }
        if(date < 10){
          date = "0" + date
        }
        this.tablelist[i].activityLists[0].startDate = year + "-" + month + "-" + date
      }
    })
  },
  methods: {
    //表头样式
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:10px;text-align:center";
    },
    showEnterView(data, index) {
      let routeUrl = this.$router.resolve({
        path:"/teacher/tperact/score",
        query: {
              keywords: index,
            },
      })
      window.open(routeUrl.href)
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