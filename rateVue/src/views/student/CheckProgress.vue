<template>
  <div>

    <el-table
        :data="tableData"
        stripe
        border
        v-loading="loading"
        :header-cell-style="rowClass"
        element-loading-text="正在加载..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.12)"
        style="width: 100%"
    >
      <el-table-column
          align="center"
          width="50px"
          label="编号"
          type="index"
      ></el-table-column>
      <el-table-column
          align="center"
          width="200px"
          label="期刊全称"
          prop="publicationName"
      ></el-table-column>
      <el-table-column
          align="center"
          width="100px"
          label="期刊简称"
          prop="publicationAbbr"
      ></el-table-column>
      <el-table-column
          align="center"
          width="100px"
          label="出版社"
          prop="publisherName"
      ></el-table-column>
      <el-table-column
          align="center"
          width="100px"
          label="期刊网址"
          prop="publicationUrl"
      ></el-table-column>
      <el-table-column
          align="center"
          width="100px"
          label="出版年"
          prop="year"
      ></el-table-column>
      <el-table-column
          align="left"
          label="指标点名称"
          prop="indicatorName"
      ></el-table-column>
      <el-table-column
          align="center"
          width="150px"
          label="提交时间"
          prop="date"
      ></el-table-column>
      <el-table-column
          align="center"
          width="100px"
          label="审核状态"
          prop="state"
      >
        <template slot-scope="scope">
            <span
                style="padding: 4px"
                :style="scope.row.state=='reject' ? {'color':'red'}:{'color':'black'}"
                size="mini"
            >
              {{
                scope.row.state == "commit"
                    ? "已提交"
                    : scope.row.state == "pass"
                        ? "管理员通过"
                        : "管理员驳回"
              }}
              </span>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          width="100px"
          label="操作"
          prop="operation"
      >
        <template slot-scope="scope">
          <el-button
              @click="showInfo(scope.row)"
              style="padding: 4px"
              size="mini"
          >查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        class="showInfo_dialog"
        :title="title_show"
        :visible.sync="dialogVisible_show"
        width="520px"
        center
    >
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          style="margin-left: 20px"
      >
        <el-form-item label="学生姓名:" prop="studentName">
          <span>{{ emp.studentName }}</span
          ><br/>
        </el-form-item>

        <el-form-item label="期刊全称:" prop="publicationName">
          <span>{{ emp.publicationName }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="期刊简称:" prop="publicationAbbr">
          <span>{{ emp.publicationAbbr }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="出版社:" prop="publisherName">
          <span>{{ emp.publisherName }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="期刊网址:" prop="publicationUrl">
          <span>{{ emp.publicationUrl }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="出版年:" prop="year">
          <span>{{ emp.year }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="指标点:" prop="indicatorName">
          <span>{{ emp.indicatorName }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="提交时间:" prop="date">
          <span>{{ emp.date }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="拒绝理由:" prop="comment" v-if="emp.comment !== null && emp.comment !== ''">
          <span style="color: red;">{{ emp.comment }}</span><br/>
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span
              v-if="
              emp.publicationProofUrl == '' || emp.publicationProofUrl == null
                ? true
                : false
            "
          >无证明材料</span
          >
          <a
              v-else
              style="
              color: gray;
              font-size: 11px;
              text-decoration: none;
              cursor: pointer;
            "
              @click="download(emp)"
              onmouseover="this.style.color = 'blue'"
              onmouseleave="this.style.color = 'gray'"
          >
            {{ emp.publicationProofUrl | fileNameFilter }}</a
          >
          <br/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" :model="emp">
        <el-button
            id="but_reject"

            @click="dialogVisible_show = false"
            type="primary"
        >关闭</el-button
        >
      </span>
    </el-dialog>

  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      isShowInfo: false,
      labelPosition: "left",
      dialogVisible_show: false,
      title_show: "",
      loading: false,
      activeName: "commit",
      tableData: [], // 表格数据
      emp: {
        id: "",
        indicatorId: "",
        indicatorName: "",
        publicationAbbr: "",
        publicationId: "",
        publicationName: "",
        publicationProofUrl: "",
        publicationUrl: "",
        publisherName: "",
        state: "",
        studentId: "",
        studentName: "",
        date: "",
        year: "",
        comment: "",
      },
    };
  },
  mounted() {
    this.init();
  },
  filters: {
    fileNameFilter: function (data) {
      //将证明材料显示出来
      if (data == null || data == "") {
        return "无证明材料";
      } else {
        var arr = data.split("/");
        return arr.reverse()[0];
      }
    },
  },
  methods: {
    async auditing_commit(status) {
      this.loading = true;
      this.emp.state = status;
      // console.log(this.emp);
      const url = `/publicationSubmission/editState/`;
      try {
        const resp = await this.postRequest1(url, this.emp);
        if (resp && resp.msg === "200") {
          this.loading = false;
          this.dialogVisible_show = false;
          this.$message.success("修改成功！");
          this.dialogVisible_publication = false;
          this.init();
        }
      } catch (error) {
        this.$message.error(error);
      } finally {
        this.loading = false;
        this.init()
      }
    },
    download(data) {
      var fileName = data.publicationProofUrl.split("/").reverse()[0];
      // console.log(fileName);
      var url = encodeURIComponent(data.publicationProofUrl);
      axios({
        url: "/paper/basic/downloadByUrl?url=" + url,
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        responseType: "blob",
      }).then((response) => {
        // console.log(response);
        const url = window.URL.createObjectURL(new Blob([response]));

        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      });
    },

    emptyEmp() {
      this.emp = {
        id: "",
        indicatorId: "",
        indicatorName: "",
        publicationAbbr: "",
        publicationId: "",
        publicationName: "",
        publicationProofUrl: "",
        publicationUrl: "",
        publisherName: "",
        state: "",
        studentId: "",
        studentName: "",
        date: "",
        year: "",
        comment: "",
      };
    },
    async showInfo(data) {
      // console.log(data);
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;

      if (this.emp.state == 'commit') {
        this.loading = true;
        try {
          // 进行查重，所以后端就不需要进行查重了。
          const url = `/publicationSubmission/check/`;
          const resp = await this.getRequest(url, this.emp);

          this.loading = false;
          // console.log(resp);

          if (resp.obj) {
            this.$message.success("该期刊已经审核过！");
            this.dialogVisible_show = false;
            await this.auditing_commit('reject')
            this.init();
          }
        } catch
            (error) {
          this.$message.error(error);
        }
      }
    },

    init() {
      this.loading = true;
      axios.get(`/publicationSubmission/getStuSubmission?stuID=` + JSON.parse(localStorage.getItem('user')).id)
          .then((response) => {
            this.loading = false;
            this.tableData = response.obj; // 将返回的数据赋值给 submission
            // console.log(this.tableData);
          })
          .catch((error) => {
            this.$message.error(error);
          });
    },

    rowClass() {
      return "background:#b3d8ff;color:black;font-size:13px;text-align:center";
    },
  },
};
</script>

<style>
.showInfo_dialog .el-form-item {
  margin-bottom: 5px;
}
</style>
