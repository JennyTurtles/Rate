<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button
              icon="el-icon-refresh"
              type="primary"
              @click="initEmps"
              :disabled="showAdvanceSearchView"
          >
            重置
          </el-button>
        </div>
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
            prop="addDate"
            align="center"
            label="修改时间"
            min-width="30%"
        >
        </el-table-column>
        <!-- width="200" -->
        <!-- width="200" -->
        <el-table-column
            prop="operator_name"
            label="操作者名称"
            align="center"
            min-width="30%"
        >
        </el-table-column>
        <!-- width="100" -->
        <el-table-column
            prop="category"
            label="类别"
            align="center"
            min-width="30%"
        >
        </el-table-column>
        <!-- width="70" -->
        <el-table-column
            prop="content"
            label="修改内容"
            align="center"
            min-width="30%"
        >
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

    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" center>
      <el-form
          :label-position="labelPosition"
          label-width="100px"
          :model="emp"
          :rules="rules"
          ref="empForm"
      >
        <el-form-item label="活动名称:" prop="name">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.name"
              placeholder="请输入单位名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="开始日期:" prop="startDate">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.startDate"
              type="date"
              placeholder="开始日期"
          ></el-input>
        </el-form-item>
        <el-form-item label="备 注: " prop="comment">
          <!-- <textarea v-model="emp.comment" placeholder="备注" style="height:100px;width: 350px"></textarea> -->
          <el-input
              type="textarea"
              :rows="2"
              v-model="emp.comment"
              placeholder="备注"
          >
          </el-input>
        </el-form-item>

        <!-- <el-form-item label="评分项数:" prop="scoreItemCount">
                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit" v-model="emp.scoreItemCount"
                          placeholder="评分项数"></el-input>
              </el-form-item> -->
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp">确 定</el-button>
      </span>
    </el-dialog>

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
export default {
  name: "SalLog",
  data() {
    return {
      labelPosition: "left",
      title: "",
      title_show: "",
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      emps: [],
      loading: false,
      dialogVisible: false,
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
      return this.$store.state.currentHr; //object信息
    },
  },
  created() {
  },
  mounted() {
    this.initEmps();
    this.initPositions();
  },
  methods: {
    rowClass() {
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    /** 查询角色列表 */
    onError(err, file, fileList) {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
    },
    onSuccess(response, file, fileList) {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
      this.initEmps();
    },
    beforeUpload() {
      this.importDataBtnText = "正在导入";
      this.importDataBtnIcon = "el-icon-loading";
      this.importDataDisabled = true;
    },
    // exportData() {
    //   window.open("/employee/basic/export", "_parent");
    // },
    emptyEmp() {
      this.emp = {
        id: null,
        startDate: null,
        name: "",
        scoreItemCount: "0",
        comment: "活动备注example：关于xxx的活动",
      };
    },
    exportEx(data){
      let url = '/participants/basic/export?activityID=' + data.id;
      window.open(url, "_parent");
    },
    exportAc(data){
      let url = '/participants/basic/export_ac?activityID=' + data.id;
      window.open(url, "_parent");
    },
    showEditEmpView(data) {
      this.initPositions();
      this.title = "编辑单位信息";
      this.emp = data;
      this.dialogVisible = true;
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
    },
    deleteEmp(data) {
      //console.log(data);
      this.getRequest("/activities/basic/check?id=" + data.id).then(res => {
        //console.log(res);
        if (res == true) {
          // console.log("this obj has no relatives")
          this.$confirm(
              "此操作将永久删除【" + data.name + "】, 是否继续?",
              "提示",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }
          ).then(() => {
            this.postRequest("/activities/basic/delete", data).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          });
        } else {
          this.$confirm(
              "尚存在与活动" + data.name + "相关的信息，所以放入回收站，如要永久删除，请先删除分组和评分项等相关信息，是否放入回收站?",
              "提示",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }
          ).then(() => {
            this.postRequest("/activities/basic/predelete", data).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          });
        }
      })


      // console.log(data);
      if (data.groupCount == 0)
      this.$confirm(
          "此操作将永久删除【" + data.name + "】, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      ).then(() => {
        this.postRequest("/activities/basic/delete", data).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        });
      });

    },
    endEmp(data) {
      // console.log(data);
      this.$confirm(
          "此操作将永久停止活动【" + data.name + "】, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      ).then(() => {
        this.postRequest("/activities/basic/end", data).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        });
      });
    },
    doAddEmp() {
      if (this.emp.id) {
        // console.log(this.emp);
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            const _this = this;
            this.postRequest("/activities/basic/update", _this.emp).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible = false;
                    this.initEmps();
                  }
                }
            );
          }
        });
      } else {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.institutionID = this.user.institutionID;
            // this.emp.institutionID = this.user.id;
            // console.log(this.emp);
            // console.log(this.user.id);
            const _this = this;
            this.postRequest("/activities/basic/insert", _this.emp).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible = false;
                    this.initEmps();
                  }
                }
            );
          }
        });
      }
    },
    initPositions() {
      // console.log(this.$store.state.hrs);
      /*this.getRequest('/employee/basic/positions').then(resp => {
        if (resp) {
          this.positions = resp;
        }
      })*/
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
      let url = "/log/basic/?page=" + this.page + "&size=" + this.size + "&institutionID=" + this.user.institutionID;
      // console.log(url);
      // console.log(this.user.companyName);
      // console.log(this.user.id);
      // console.log(this.user.institutionID);
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          // console.log("see ac name", resp);
          this.emps = resp.data;
          this.total = resp.total;
        }
      });
    },
    showGroupmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/table",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showInsertmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/group",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showteachermanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showScoreItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/month",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showInfoItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/infos",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
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
