<template>
  <div>
    <div style="width: 100%;text-align: center">异常信息详情</div>
    <div style="margin-left: auto">
      <el-button icon="el-icon-back" type="primary" @click="back">
        返回
      </el-button>
    </div>
    <el-table
        :data="data"
        stripe
        border
        v-loading="loading"
        :header-cell-style="rowClass"
        element-loading-text="正在加载..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.12)"
        style="width: 100%; margin-top: 20px"
    >
      <el-table-column
          align="center"
          min-width="5%"
          label="编号"
      >
        {{Index}}
      </el-table-column>
      <el-table-column
          align="center"
          min-width="10%"
          label="异常类型"
          prop="errorType"
      ></el-table-column>
      <el-table-column
          min-width="50%"
          label="异常栈信息"
      >
        <template slot-scope="scope">
          <el-input
              type="textarea"
              v-model="scope.row.errorDescription"
              autosize
              :autosize="{maxRows: 20}"
              class="borderNone"
              style="width: 100%"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          min-width="20%"
          label="邮件详情"
          prop="body"
      ></el-table-column>
      <el-table-column
          align="center"
          min-width="10%"
          label="异常时间"
          prop="timestamp"
      ></el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ExceptionDetails",
  data(){
    return{
      data:[],
      loading: false,
      id:"",
      Index:"",
    }
  },
  mounted() {
    this.id = this.$route.query.id;
    this.Index = this.$route.query.Index;
    this.init();
  },
  methods:{
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:13px;text-align:center";
    },
    init(){
      this.loading = true;
      this.getRequest("/exception/getById?id=" + this.id).then((response) => {
        this.loading = false;
        this.data.push(response.obj)
      })
        .catch((error) => {
          this.$message.error(error);
        });
    },
    back(){
      this.$router.go(-1);
    },
  }
}
</script>

<style scoped>

</style>
