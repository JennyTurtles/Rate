<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          stripe
          border
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
      >
        <el-table-column type="selection" width="35px"></el-table-column>

        <el-table-column
            prop="id"
            fixed
            align="center"
            label="编号"
            width="50"
        >
        </el-table-column>
        <!-- width="70" -->
        <el-table-column
            prop="name"
            fixed
            align="left"
            label="活动名称"
            width="200"
        >
        </el-table-column>
        <!-- width="200" -->
        <!-- width="200" -->
        <el-table-column
            prop="startDate"
            label="开始时间"
            align="center"
            width="130"
        >
        </el-table-column>
        <!-- width="100" -->
        <el-table-column
            prop="scoreItemCount"
            label="评分项数"
            align="center"
            width="80"
        >
        </el-table-column>
        <!-- width="70" -->
        <el-table-column
            prop="score"
            label="总分"
            align="center"
            width="75"
        >
        </el-table-column>
        <el-table-column align="center" width="650" label="操 作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView_show(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情
            </el-button
            >
            <el-button
                @click="showInfoItem(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >信息项设置
            </el-button
            >
            <!-- <el-button
                @click="exportEx(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >{{text}}导出专家打分
            </el-button
            > -->
            <!-- <el-button
                @click="exportAc(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >导出选手分数
            </el-button
            > -->
            <!-- <el-button
                @click="endEmp(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-circle-close"
                plain
                :disabled="scope.row.status=='close'"
            >{{ scope.row.status == 'open' ? '结束活动' : '已结束' }}
            </el-button
            > -->
          </template>
        </el-table-column>
      </el-table>

      <div style="display: flex; justify-content: flex-end; margin: 10px 0">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total"
        >
        </el-pagination>
      </div>
    </div>

    <el-dialog
        :title="title_show"
        :visible.sync="dialogVisible_show"
        width="25%"
        center
    >
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          :rules="rules"
          ref="empForm"
          style="margin-left: 60px"
      >
        <el-form-item label="分组数:" prop="groupCount">
          <span>{{ emp.groupCount }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="专家数:" prop="expertCount">
          <span>{{ emp.expertCount }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="选手数:" prop="participantCount">
          <span>{{ emp.participantCount }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="备 注:">
          <span>{{ emp.comment }}</span>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible_show = false"
        >关 闭</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "SalSearch",
  data() {
    return {
      labelPosition: "left",
      title: "",
      text: "",
      title_show: "",
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      emps: [],
      loading: false,
      dialogVisible_show: false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      positions: [],
      emp: {
        id: null,
        institutionID: null,
        name: null,
        startDate: "2022/02/02",
        scoreItemCount: "0",
        score: "100",
        groupCount: "0",
        expertCount: "0",
        participantCount: "0",
        comment: "javaboy",
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        name: [{required: true, message: "请输入活动名", trigger: "blur"}],
        startDate: [
          {required: true, message: "请输入活动时间", trigger: "blur"},
        ],
        scoreItemCount: [
          {
            required: true,
            type: "number",
            message: "请输入正确数据",
            trigger: "blur",
            transform: (value) => Number(value),
          },
        ],
        comment: [{required: true, message: "请输入备注", trigger: "blur"}],
      },
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  created() {
  },
  mounted() {
    this.initEmps();
  },
  methods: {
    rowClass() {
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    initEmps() {
      this.loading = true;
      console.log("init")
      console.log(this.user);
      let url = "/activities/basic/?page=" + this.page + "&size=" + this.size + "&institutionID=" + this.user.institutionID;
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
        }
      });
    },

    showInfoItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/student/infos",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    // searchEmps() {
    //   this.loading = true;
    //   //console.log('---------', this.user.institutionID);
    //   const _this = this;
    //   //let url =
    //   this.getRequest(
    //       "/activities/basic/search?company=" +
    //       this.keyword +
    //       "&page=" +
    //       this.page +
    //       "&size=" +
    //       this.size +
    //       "&institutionID=" + this.user.institutionID
    //   ).then((resp) => {
    //     this.loading = false;
    //     if (resp) {
    //       this.emps = resp.data;
    //       this.total = resp.total;
    //     }
    //   });
    // },
  },
};
</script>

<style>
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>
