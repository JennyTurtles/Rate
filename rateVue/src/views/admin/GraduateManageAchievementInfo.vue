<template>
  <div>
    <el-tabs v-model="tabsActivateName" @tab-click="tabChange">
      <el-tab-pane label="学术论文" name="paper"></el-tab-pane>
      <el-tab-pane label="授权专利" name="patent"></el-tab-pane>
      <el-tab-pane label="科研获奖" name="award"></el-tab-pane>
      <el-tab-pane label="专著教材" name="monograph"></el-tab-pane>
      <el-tab-pane label="学科竞赛" name="competition"></el-tab-pane>
      <el-tab-pane label="纵向项目" name="project"></el-tab-pane>
      <el-tab-pane label="横向项目" name="projectHorizontal"></el-tab-pane>
      <el-tab-pane label="决策咨询" name="decision"></el-tab-pane>
      <el-tab-pane label="产品应用" name="product"></el-tab-pane>
      <el-tab-pane label="制定标准" name="standard"></el-tab-pane>
    </el-tabs>
    <el-table :data="tabsTableData" v-loading="tabsTableLoading">
      <el-table-column
          key="1"
          prop="student.name"
          align="center"
          label="学生姓名"
      >
      </el-table-column>
      <el-table-column key="2" prop="name" label="成果名称" align="center"></el-table-column>
      <el-table-column key="3" prop="state" label="成果状态" align="center">
        <template slot-scope="scope">
            <span
                style="padding: 4px"
                :style="scope.row.state=='tea_reject' || scope.row.state=='adm_reject' ? {'color':'red'}:{'color':'gray'}"
                size="mini"
            >
              {{
                scope.row.state == "commit"
                    ? "已提交"
                    : scope.row.state == "tea_pass"
                        ? "导师通过"
                        : scope.row.state == "tea_reject"
                            ? "导师驳回"
                            : scope.row.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}
              </span>
        </template>
      </el-table-column>
      <el-table-column
          key="4"
          v-if="tabsActivateName == 'paper' ? true : false"
          prop="pubName"
          label="发表刊物"
          align="center"
      >
      </el-table-column>
      <el-table-column
          key="5"
          label="积分"
          prop="point"
          align="center"
      >
        <template slot-scope="scope">
          <span>{{scope.row.have_score == 1 ? scope.row.point : 0}}</span>
          <span>/</span>
          <span>{{scope.row.point}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="paperoperList[0].remark" label="备注" align="center" key="6" v-if="tabsActivateName == 'paper' ? true : false">
      </el-table-column>
      <el-table-column prop="operationList[0].remark" label="备注" align="center" key="7" v-if="tabsActivateName == 'paper' ? false : true">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "GraduateManageAchievementInfo",
  data() {
    return {
      tabsActivateName: 'paper',
      tabsTableLoading: false,
      tabsTableData: [],
    }
  },
  mounted() {
    this.getTableDataMethod();
  },
  methods: {
    tabChange(tab, event) {
      this.getTableDataMethod();
    },
    getTableDataMethod() {
      let url = '';
      if(this.tabsActivateName != 'projectHorizontal') {
        url = `/${this.tabsActivateName}/basic/studentID?studentID=${this.$route.query.studentId}`;
      } else {
        url = `/project/basic/studentID/horizontal?studentID=${this.$route.query.studentId}`;
      }
      this.tabsTableLoading = true;
      this.getRequest(url).then(response => {
        if(response) {
          this.tabsTableData = response.data;
          this.tabsTableLoading = false;
        }
      })
    },
  }
}
</script>

<style scoped>

</style>