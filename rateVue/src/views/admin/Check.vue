<template>
  <div>
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="全部" name="all"></el-tab-pane>
      <el-tab-pane label="未审核" name="commit"></el-tab-pane>
      <el-tab-pane label="审核通过" name="pass"></el-tab-pane>
      <el-tab-pane label="审核失败" name="reject"></el-tab-pane>
    </el-tabs>

    <el-table :data="tableData"
              stripe
              border
              v-loading="loading"
              :header-cell-style="rowClass"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.12)"
              style="width: 100%;">
        <el-table-column align="center" width="50px" label="编号" type="index"></el-table-column>
        <el-table-column align="center" width="100px" label="学号" prop="studentId"></el-table-column>
        <el-table-column align="center" width="100px" label="姓名" prop="studentName"></el-table-column>
        <el-table-column align="center" width="200px" label="期刊全称" prop="publicationName"></el-table-column>
        <el-table-column align="center" width="100px" label="期刊简称" prop="publicationAbbr"></el-table-column>
        <el-table-column align="center" width="100px" label="出版社" prop="publisherName"></el-table-column>
        <el-table-column align="center" width="100px" label="期刊网址" prop="publicationUrl"></el-table-column>
        <el-table-column align="center" width="100px" label="出版年" prop="year"></el-table-column>
        <el-table-column align="center" width="100px" label="指标点名称" prop="indicatorName"></el-table-column>
        <el-table-column align="center" width="100px" label="证明材料" prop="publicationProofUrl"></el-table-column>
        <el-table-column align="center" width="200px" label="提交时间" prop="date"></el-table-column>
        <el-table-column align="center"  label="操作" prop="operation">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
                v-show="scope.row.state == 'commit' || scope.row.state == 'tea_reject' || scope.row.state == 'adm_reject' ? true:false"
            >编辑
            </el-button
            >
            <el-button
                @click="deleteEmp(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
                v-show="scope.row.state == 'tea_reject' || scope.row.state == 'commit' || scope.row.state == 'adm_reject'? true:false"
            >删除
            </el-button
            >
            <el-button
                @click="showInfo(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情
            </el-button
            >
          </template>
        </el-table-column>
    </el-table>
  </div>
</template>

<script>

import axios from "axios";
export default {
  data() {
    return {
      loading: false,
      activeName: 'commit',
      tableData: [] // 表格数据

    };
  },
  mounted() {
    this.init();
  },
  methods: {
    handleClick(tab) {
      console.log(tab.name);
      this.loading = true;
      axios.get(`/publicationSubmission/get?state=`+tab.name)
          .then(response => {
            this.loading = false;
            this.tableData = response.obj; // 将返回的数据赋值给 submission
          })
          .catch(error => {
            this.$message.error(error)
          });
    },
    init() {
      this.loading = true;
      axios.get(`/publicationSubmission/get?state=commit`)
          .then(response => {
            this.loading = false;
            this.tableData = response.obj; // 将返回的数据赋值给 submission
          })
          .catch(error => {
            this.$message.error(error)
          });
    }
    ,
    rowClass() {
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    }
    ,
  }
};
</script>
