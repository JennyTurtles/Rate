<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 5px 0">
    </div>
    <div style="margin-top: 0px">
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
        <el-table-column
            prop="name"
            fixed
            align="left"
            label="活动名称"
        >
        </el-table-column>
        <el-table-column
            prop="startDate"
            label="开始时间"
            align="center"
            width="170"
        >
        </el-table-column>
        <el-table-column
            prop="scoreItemCount"
            label="评分项数"
            align="center"
            width="80"
        >
        </el-table-column>
        <el-table-column
            prop="score"
            label="总分"
            align="center"
            width="75"
        >
        </el-table-column>
        <el-table-column
            label="是否有子活动"
            align="center"
            width="100"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.haveSub === 1">是</span>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column
            label="状态"
            align="center"
            width="150"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.status === 'close'">结束且已删除</span>
            <span v-else>已删除</span>
          </template>
        </el-table-column>
        <el-table-column align="center" width="500" label="操 作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView_show(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情
            </el-button
            >
            <el-button
                @click="recover(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-refresh-left"
                type="primary"
                plain
            >恢复
            </el-button
            >
            <el-button
                @click="deleteCompletely(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >彻底删除
            </el-button
            >
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
            :page-size="size"
            :page-sizes="[10, 20, 30, 40]"
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
  </div>
</template>

<script>
import {Message} from "element-ui";
import fa from "element-ui/src/locale/lang/fa";
import axios from "axios";
import index from "vuex";
import Score from "@/views/expert/Escore.vue";

export default {
  name: "ActRecycle",
  components: {Score},
  props: ["mode", "activityID", "actName", "groupName", "groupID"], // 四个地方复用组件
  data() {
    return {
      dialogVisible_show: false,
      enterDateSelected: true,
      enterDateOptions: {},
      visibleDateOptions: {},
      currentActivity: {},
      loading: false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      title_show: "",
      emps: [],
      emp: {
        id: null,
        institutionID: null,
        name: null,
        startDate: "",
        enterDate: "",
        visibleDate: "",
        scoreItemCount: "0",
        score: "100",
        groupCount: "0",
        expertCount: "0",
        participantCount: "0",
        comment: "javaboy",
        parentId: null,
        requireGroup: true,
        isShowPermissionBtn: false
      },
      labelPosition: "left",
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
      },
    };
  },
  computed: {
    index() {
      return index
    },
    fa() {
      return fa
    },
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  created() {
  },
  mounted() {
    console.log(this.mode)
    this.initEmps();
  },
  methods: {
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    initEmps() { // 在此适配不同的组件
      this.loading = true;
      let url = "/activities/basic/getRecycle?page=" + this.page + "&size=" + this.size + "&institutionID=" + this.user.institutionID + "&adminID=" + this.user.id;
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            this.emps = resp.data;
            this.total = resp.total;
            console.log(resp)
          }
        });
    },
    recover(row){
      this.$confirm(
          "是否确定恢复" + row.name,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      ).then(() => {
        this.postRequest("/activities/basic/recover?activityID=" + row.id).then((resp) => {
          if (resp) {
            this.$message({type: 'success', message: '恢复成功!'});
            this.initEmps();
          }
        });
      });
    },
    deleteCompletely(row){
      this.$confirm(
          "该操作将彻底删除活动的所有信息，是否确定彻底删除" + row.name,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      ).then(() => {
        this.postRequest("/activities/basic/deleteCompletely", row).then((resp) => {
          if (resp) {
            this.$message({type: 'success', message: '删除成功!'});
            this.initEmps();
          }
        });
      });
    },
    rowClass() {
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
    },
  },
};
</script>

<style scoped>
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

.tip-title {
  margin-left: 3px;
  font-size: 9px;
  color: darkgray;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}
.el-icon-arrow-down {
  font-size: 12px;
}
</style>
