<template>
  <div>
    <div class="block">
      <label style="fontSize:15px;margin-left:16px">日期范围：</label>
      <el-date-picker
          v-model="startTime"
          type="datetime"
          :max-date="new Date()"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择日期时间">
      </el-date-picker>
      <label >&nbsp; - &nbsp;</label>
      <el-date-picker
          v-model="endTime"
          type="datetime"
          :min-date="startTime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择日期时间">
      </el-date-picker>
      <el-button @click="filterBtn" style="margin-left: 20px;" icon="el-icon-search" type="primary">筛选</el-button>
      <el-button @click="clear" style="margin-left: 20px;" icon="el-icon-refresh-left" type="success">重置</el-button>
      <el-button @click="Delete_multi" style="margin-left: 20px;" icon="el-icon-delete" type="danger">删除</el-button>
      <el-button @click="Delete_all" style="margin-left: 20px;" icon="el-icon-delete" type="danger">清空筛选数据</el-button>
    </div>
    <el-table
        :data="tableData"
        stripe
        border
        v-loading="loading"
        :header-cell-style="rowClass"
        @selection-change="handleSelectionChange"
        element-loading-text="正在加载..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.12)"
        style="width: 100%; margin-top: 20px"
    >
      <el-table-column
          type="selection"
          min-width="3%">
      </el-table-column>
      <el-table-column
          align="center"
          width="50px"
          label="编号"
          type="index"
          :index="indexMethod"
      ></el-table-column>
      <el-table-column
          align="center"
          min-width="10%"
          label="异常类型"
          prop="errorType"
      ></el-table-column>
      <el-table-column
          align="center"
          min-width="10%"
          label="异常时间"
          prop="timestamp"
      ></el-table-column>

      <el-table-column
          align="center"
          min-width="10%"
          label="操作"
          prop="operation"
      >
        <template slot-scope="scope">
          <el-button
              @click="showInfo(scope.row)"
              style="padding: 4px"
              size="mini"
          >查看异常详情
          </el-button>
          <el-button
              @click="Delete(scope.row)"
              style="padding: 4px"
              size="mini"
              type="danger"
              icon="el-icon-delete"
              plain
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display: flex; justify-content: flex-end; margin: 10px 0">
      <el-pagination
          background
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="pageSizes"
          @current-change="currentChange"
          @size-change="sizeChange"
          layout="sizes, prev, pager, next, jumper, ->, total, slot"
          :total="totalCount"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      totalCount: 0,
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 50, 100],
      labelPosition: "left",
      title_show: "",
      loading: false,
      tableData: [], // 表格数据
      deleteData:[], //选择删除的数据
      description:"",
      startTime:"",
      endTime:"",
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  created() {
    this.init();
  },
  methods: {
    sizeChange(currentSize) {
      this.pageSize = currentSize;
      this.startTime === "" ? this.fetchData(this.currentPage, this.pageSize) : this.filterBtn();
    },
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.startTime === "" ? this.fetchData(this.currentPage, this.pageSize) : this.filterBtn();
    },
    indexMethod(index) {
      return (this.currentPage - 1) * this.pageSize + index + 1;
    },
    fetchData(pageNum, pageSize) {
      this.loading = true;
      axios.get(`/exception/getAll?page=${pageNum}&size=${pageSize}`,{
        headers:{
          'token': localStorage.getItem('user') ? this.user.token : ''
        }
      }).then((response) => {
            this.loading = false;
            this.tableData = response.obj[0];
            this.totalCount = response.obj[1];
          })
          .catch((error) => {
            this.$message.error(error);
          });
    },
    init() {
      this.fetchData(this.currentPage,this.pageSize)
    },
    Delete_multi(){
      this.$confirm('是否确定删除选中异常信息', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.postRequest("/exception/delete",this.deleteData).then(resp => {
          console.log(resp)
          if (resp) {
            this.$message.success("删除成功！")
            this.startTime === "" ? this.fetchData(this.currentPage, this.pageSize) : this.filterBtn();
          }
        })
      })
    },
    Delete(data){
      this.deleteData.push(data);
      this.Delete_multi();
    },
    Delete_all(){
      this.$confirm('是否确定删除所有异常信息', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.postRequest("/exception/deleteAll?start="+this.startTime + "&end=" + this.endTime).then(resp => {
          if (resp) {
            this.$message.success("删除成功！")
            this.startTime === "" ? this.fetchData(this.currentPage, this.pageSize) : this.filterBtn();
          }
        })
      })
    },
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:13px;text-align:center";
    },
    handleSelectionChange(val){
      this.deleteData=val;
    },
    showInfo(data){
      var index
      for (var i = 0; i < this.tableData.length; i++){
        if (this.tableData[i].id === data.id){
          index = (this.currentPage - 1) * this.pageSize + i + 1
        }
      }
      const _this = this;
      _this.$router.push({
        path: "/Admin/ExceptionDetails",
        query: {
          id: data.id,
          Index: index,
        },
      });
    },
    clear(){
      this.startTime = "";
      this.endTime = "";
      this.currentPage = 1;
      this.init();
    },
    filterBtn(){
      var start = Date.parse(this.startTime)
      var end = Date.parse(this.endTime)
      if (end < start){
        this.$message.warning("结束时间不可小于开始时间！")
        return
      }
      this.loading = true;
      this.getRequest("/exception/filterByDate?page="+this.currentPage + "&size=" + this.pageSize + "&start=" + this.startTime + "&end=" + this.endTime).then(resp => {
        console.log(resp)
        if (resp) {
          this.loading = false;
          this.tableData = resp.obj[0];
          this.totalCount = resp.obj[1];
        }
      })
    }
  },
};
</script>

<style>
.showInfo_dialog .el-form-item {
  margin-bottom: 5px;
}
</style>
