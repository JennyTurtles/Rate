<template>
  <div>
    <div>
      <div
        style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-input
            placeholder="请输入论文名称进行搜索，可以直接回车搜索..."
            prefix-icon="el-icon-search"
            clearable
            @clear="searchEmps"
            style="width: 350px; margin-right: 10px"
            v-model="keyword"
            @keydown.enter.native="searchEmps"
            :disabled="showAdvanceSearchView"
          ></el-input>
          <el-button
            icon="el-icon-search"
            type="primary"
            @click="searchEmps"
            :disabled="showAdvanceSearchView"
          >
            搜索
          </el-button>
          <el-button
            icon="el-icon-refresh"
            type="primary"
            @click="initEmps"
            :disabled="showAdvanceSearchView"
          >
            重置
          </el-button>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加论文
          </el-button>
        </div>
        <!-- <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
          添加论文
          </el-button>
        </div> -->
        <div>
          <el-upload
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            :on-error="onError"
            :disabled="importDataDisabled"
            style="display: inline-flex; margin-right: 8px"
            action="/employee/basic/import"
          >
            <el-button
              :disabled="importDataDisabled"
              type="primary"
              :icon="importDataBtnIcon"
            >
              {{ importDataBtnText }}
            </el-button>
          </el-upload>
          <el-button type="primary" @click="exportData" icon="el-icon-download">
            导出数据
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
        <el-table-column type="selection" width="35px"> </el-table-column>

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
          label="论文名称"
          width="200"
        >
        </el-table-column>
        <!-- width="200" -->
        <el-table-column
          prop="institutionID"
          fixed
          align="left"
          label="所属人Id"
          width="200"
        >
        </el-table-column>
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
          label="简介"
          align="left"
          width="500"
        >
        </el-table-column>
         <el-table-column
          prop="scoreItemCount"
          label="路径"
          align="left"
          width="250"
        >
        </el-table-column>
        <!-- width="70" -->
        <el-table-column
          prop="score"
          label="评分"
          align="center"
          width="75"
        >
        </el-table-column>
        <!-- width="60" -->
        <el-table-column
          prop="comment"
          width="90"
          align="center"
          label="备注"
        >
          <template slot-scope="scope">
            <el-button
              @click="showEditEmpView_show(scope.row)"
              style="padding: 4px"
              size="mini"
              >查看详情</el-button
            >
          </template>
        </el-table-column>
        <!-- width="80" -->
        <!-- width="550" -->
        <el-table-column align="center" width="650" label="操 作">
          <template slot-scope="scope">
            <el-button
              @click="showEditEmpView(scope.row)"
              style="padding: 4px"
              size="mini"
              icon="el-icon-edit"
              type="primary"
              plain
              >编辑</el-button
            >
            <el-button
              @click="showScoreItem(scope.row)"
              style="padding: 4px"
              size="mini"
              icon="el-icon-tickets"
              type="primary"
              plain
              >评分项设置</el-button
            >
            <el-button
              @click="showGroupmanagement(scope.row)"
              style="padding: 4px"
              size="mini"
              icon="el-icon-s-operation"
              type="primary"
              plain
              >分组管理</el-button
            >
            <el-button
              @click="showInsertmanagement(scope.row)"
              style="padding: 4px"
              size="mini"
              icon="el-icon-plus"
              type="primary"
              plain
              >导入选手</el-button
            >
            <el-button
                @click="showteachermanagement(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >导入专家</el-button
            >

            <el-button
              @click="deleteEmp(scope.row)"
              style="padding: 4px"
              size="mini"
              type="danger"
              icon="el-icon-circle-close"
              plain
              >结束论文</el-button
            >
            <el-button
              @click="deleteEmp(scope.row)"
              style="padding: 4px"
              size="mini"
              type="danger"
              icon="el-icon-delete"
              plain
              >删除</el-button
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
        <el-form-item label="论文名称:" prop="name">
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
          ><br />
        </el-form-item>
        <el-form-item label="专家数:" prop="expertCount">
          <span>{{ emp.expertCount }}</span
          ><br />
        </el-form-item>
        <el-form-item label="选手数:" prop="participantCount">
          <span>{{ emp.participantCount }}</span
          ><br />
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
  name: "SalSearch",
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
        name: [{ required: true, message: "请输入论文名", trigger: "blur" }],
        startDate: [
          { required: true, message: "请输入论文时间", trigger: "blur" },
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
        comment: [{ required: true, message: "请输入备注", trigger: "blur" }],
      },
    };
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
  },
  created() {},
  mounted() {
    this.initEmps();
    this.initPositions();
  },
  methods: {
    rowClass(){
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
    exportData() {
      window.open("/employee/basic/export", "_parent");
    },
    emptyEmp() {
      this.emp = {
        id: null,
        startDate: null,
        name: "",
        scoreItemCount: "0",
        comment: "论文备注example：关于xxx的论文",
      };
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
      this.$confirm(
        "此操作将永久删除【" + data.company + "】, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        this.postRequest("/institution/basic/delete", data).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        });
      });
    },
    doAddEmp() {
      if (this.emp.id) {
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
            this.emp.institutionID = this.user.id;
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
    showAddEmpView() {
      this.emptyEmp();
      this.title = "添加论文";
      this.dialogVisible = true;
    },
    initEmps() {
      this.loading = true;
      let url = "/projects/basic/List";
      this.getRequest(url).then((resp) => {
        // this.loading = false;
        // if (resp) {
        //   console.log(resp);
        //   this.emps = resp.data;
        //   this.total = resp.total;
        // }
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
    searchEmps() {
      this.loading = true;
      const _this = this;
      //let url =
      this.getRequest(
        "/activities/basic/search?company=" +
          this.keyword +
          "&page=" +
          this.page +
          "&size=" +
          this.size
      ).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
        }
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
  /* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}
</style>
