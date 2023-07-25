<template>
  <div>
    <div>
      <div
        style="
          display: flex;
          justify-content: space-between;
          margin: 20px 0 20px 0px;
        "
      >
        <div>
          查看所有单位
          <el-input
            placeholder="请输入单位名进行搜索，可以直接回车搜索..."
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
        </div>
        <div>
<!--          <el-upload
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
          </el-button>-->
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加单位
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
        ref="multipleTable"
        :data="emps"
        tooltip-effect="dark"
        style="width: 100%; margin: 0px"
        stripe
        border
        highlight-current-row
       :header-cell-style="rowClass"
        v-loading="loading"
        element-loading-text="正在加载..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.12)"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="35px"> </el-table-column>
        <el-table-column
          prop="id"
          fixed
          align="center"
          label="单位编号"
          min-width="2%"
        >
        </el-table-column>
        <el-table-column
          prop="company"
          fixed
          align="left"
          label="单位名称"
          min-width="6%"
        >
        </el-table-column>

        <el-table-column
          prop="activityCount"
          label="单位总计活动数"
          align="center"
          min-width="3%"
        >
        </el-table-column>
        <el-table-column
          prop="uplimit"
          label="单位活动数上限"
          align="center"
          min-width="3%"
        >
        </el-table-column>
        <el-table-column
          prop="comment"
          min-width="6%"
          align="left"
          label="备注"
        >
        </el-table-column>
        <el-table-column align="center" min-width="7%" label="操作">
          <template slot-scope="scope">
            <el-button
              @click="showEditEmpView(scope.row)"
              style="padding: 4px"
              size="mini"
              type="primary"
              icon="el-icon-edit"
              plain
              >编辑</el-button
            >
            <el-button
                @click="showEditLimitView(scope.row)"
                style="padding: 4px"
                size="mini"
                type="primary"
                icon="el-icon-s-tools"
                plain
            >设置活动上限</el-button
            >
            <el-button
              @click="showadmin(scope.row)"
              style="padding: 4px"
              size="mini"
              type="primary"
              icon="el-icon-user"
              plain
              >管理员</el-button
            >
            <el-button
              @click="deleteEmp(scope.row)"
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
      <div style="margin: 20px 0; display: flex">
        <div>
          <el-button
            @click="toggleSelection()"
            type="primary"
            icon="el-icon-close"
            >取消选择</el-button
          >
        </div>
        <div style="margin-left: auto">
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
    </div>
    <el-dialog :title="title" :visible.sync="dialogVisible" width="35%" center>
      <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="emp"
        :rules="rules"
        ref="empForm"
        style="margin-left:20px"
      >
        <el-form-item label="单位名称:" prop="company">
          <el-input
            size="mini"
            style="width: 60%"
            prefix-icon="el-icon-edit"
            v-model="emp.company"
            placeholder="请输入单位名称"
          ></el-input>
        </el-form-item>
<!--        <el-form-item label="活动数上限:" prop="uplimit">-->
<!--          <el-input-->
<!--            size="mini"-->
<!--            style="width: 150px"-->
<!--            prefix-icon="el-icon-edit"-->
<!--            v-model="emp.uplimit"-->
<!--            placeholder="请输入单位活动数上限"-->
<!--          ></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="备 注:" prop="comment">
          <el-input
            prefix-icon="el-icon-edit"
            type="textarea"
            :rows="2"
            v-model="emp.comment"
            placeholder="请输入登录备注"
          >
          </el-input>
        </el-form-item>
<!--        <el-form-item label="总活动数:" prop="activityCount">
          <el-input
            size="mini"
            style="width: 150px"
            prefix-icon="el-icon-edit"
            v-model="emp.activityCount"
            placeholder="单位总计活动数"
          ></el-input>
        </el-form-item>-->
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :title="title" :visible.sync="dialogVisible_limit" width="35%" center>
      <el-form
          :label-position="labelPosition"
          label-width="100px"
          :model="emp"
          :rules="rules"
          ref="empForm"
          style="margin-left:20px"
      >
        <el-form-item label="活动数上限:" prop="uplimit">
          <el-input
              size="mini"
              style="width: 150px"
              prefix-icon="el-icon-edit"
              v-model="emp.uplimit"
              placeholder="请输入单位活动数上限"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "PerEmp",
  data() {
    return {
      labelPosition: "left",
      title: "",
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_limit:false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      positions: [],
      emp: {
        id: null,
        company: null,
        activityCount: "0",
        uplimit: "0",
        comment: "javaboy",
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        company: [{ required: true, message: "请输入单位名", trigger: "blur" }],
        uplimit: [
          {
            required: true,
            type: "number",
            message: "请输入正确数据",
            trigger: "blur",
            transform: (value) => Number(value),
          },
        ],
        comment: [{ required: false, message: "请输入备注", trigger: "blur" }],
      },
      //表格取消选择
      multipleSelection: [],
    };
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
        company: "",
        activityCount: "0",
        uplimit: null,
        comment: "xht",
      };
    },
    showEditEmpView(data) {
      this.initPositions();
      this.title = "编辑单位信息";
      this.emp = data;
      this.dialogVisible = true;
    },
    showEditLimitView(data){
      this.title = "设置活动上限";
      this.emp = data;
      this.dialogVisible_limit = true;
    },
    deleteEmp(data) {
      //console.log(data);
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
            //console.log(this.emp);
            const _this = this;
            this.postRequest("/institution/basic/update", _this.emp).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.dialogVisible_limit = false;
                  this.initEmps();
                  if(resp.msg==='更新成功!')
                  {Message.success(resp.msg)}
                  else
                  {Message.error(resp.msg)}
                }
              }
            );
          }
        });
      } else {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            //console.log(this.emp);
            const _this = this;
            this.postRequest("/institution/basic/insert", _this.emp).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.initEmps();
                  if(resp.msg==='添加成功!')
                  {Message.success(resp.msg)}
                  else
                  {Message.error(resp.msg)}
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
      this.title = "添加单位";
      this.dialogVisible = true;
    },
    initEmps() {
      this.loading = true;
      let url = "/institution/basic/?page=" + this.page + "&size=" + this.size;
      //console.log(url);
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
        }
      });
    },
    showadmin(data) {
      const _this = this;
      _this.$router.push({
        path: "/per/ec",
        query: {
          keywords: data.company,
          keywords_id: data.id,
        },
      });
    },
    searchEmps() {
      this.loading = true;
      //console.log(this.keyword);
      const _this = this;
      //let url =
      this.getRequest(
        "/institution/basic/search?company=" +
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
    //取消表格选择
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
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
