<template>
  <div>
    <div>
      <label>选择毕业设计：</label>
      <el-select v-model="selectDate" placeholder="请选择" @change="handleSelectSemesterChange">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
    </div>
    <div style="margin-top: 10px">
      <el-button icon="el-icon-plus" type="success" style="margin-right: 10px" @click="importStudents">导入毕业设计
      </el-button>

    </div>
    <div style="margin-top: 20px">
      <el-table
          class="table-with-shadow"
          :data="emps"
          stripe="stripe"
          border="border"
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
          v-loading.fullscreen.lock="loading"
      >
        <el-table-column label="" type="index" width="50"></el-table-column>
        <el-table-column
            prop="studentnumber"
            align="center"
            label="学号"
            width="100px"
            :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
            prop="sname"
            align="center"
            label="姓名"
            width="100px"
            :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
            prop="thesis.name"
            align="center"
            label="论文题目"
        ></el-table-column>

        <el-table-column
            prop="thesis.comment_total"
            align="center"
            label="已提交"
            width="80px"
        ></el-table-column>
        <el-table-column align="center" label="已评价" width="80px">
          <template slot-scope="scope">
            {{
              scope.row.thesis.comment_deny != 0
                  ? scope.row.thesis.comment_pass +
                  scope.row.thesis.comment_deny +
                  "(" +
                  scope.row.thesis.comment_deny +
                  ")"
                  : scope.row.thesis.comment_pass
            }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作" width="200px">
          <template slot-scope="scope">
            <el-button
                @click="showInfoItem(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain="plain"
            >进入
            </el-button>

            <el-button
                @click="openEditDialog(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain="plain"
            >编辑
            </el-button>

            <el-button
                @click="exportPDF(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain="plain"
            >导出PDF
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
        title="编辑论文题目"
        :visible.sync="editDialogVisible"
        width="50%" center
    >
      <div style="margin-bottom: 20px;">
        <el-form>
          <el-form-item label="论文题目:" label-width="80px" style="margin-left: 20px;">
            <el-input v-model="editedThesisName"/>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
      <el-button @click="saveEditedThesisName" type="primary">保存</el-button>
      <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <el-dialog :visible.sync="dialogStudentVisible" width="30%">
      <template v-slot:title>
        <div style="text-align: center; font-size: 20px;">导入毕业设计信息</div>
      </template>
      <div style="margin-left: 10px">第一步：
        <el-button icon="el-icon-upload" type="primary" style="margin-right: 10px" @click="downloadExcel('thesisName')">
          下载模版
        </el-button>
      </div>

      <div style="margin-top: 10px;margin-left: 10px">第二步：
        <el-upload
            :show-file-list="false"
            :headers="{'token':user.token}"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            style="display: inline-flex; margin-left: 1px"
            :action="`/undergraduateM/basic/importThesisName?tutorId=${user.id}&institutionId=${user.institutionID}&year=${startYear}&semester=${selectSemester}`"
        >
          <el-button icon="el-icon-plus" type="success" :disabled="selectDate==''">导入毕业设计</el-button>
        </el-upload>
      </div>
      <template #footer>
        <div>
          <el-button @click="dialogStudentVisible = false">关闭</el-button>
        </div>
      </template>

    </el-dialog>
    <el-dialog :visible.sync="dialogImportResult" width="40%" title="导入毕业设计结果统计" :center="true">
      <p>
        共计<span :style="{ color: 'blue', fontWeight: 'bold' }">{{ totalRows }}</span>条记录，
        成功更新<span :style="{ color: 'orange', fontWeight: 'bold' }">{{ duplicateInsertRowsCount }}</span>条记录，
        有<span :style="{ color: 'red', fontWeight: 'bold' }">{{ failedRowsCount }}</span>条记录更新失败。
      </p>

      <p v-show="failedRowsCount>0">以下是失败原因的统计：</p>
      <ul>
        <li v-for="(reason, code) in failureReasons" :key="code">第{{ code }}行: {{ reason }}</li>
      </ul>

      <template #footer>
        <div>
          <el-button @click="dialogImportResult = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "teaPaperComment",
  data() {
    return {
      user: {},
      selectDate: '',
      selectSemester: '',
      startYear: null,
      duplicateInsertRowsCount: 0,
      failedRowsCount: 0,
      successfulRowsCount: 0,
      totalRows: 0,
      failureReasons: [],
      dialogImportResult: false,
      dialogStudentVisible: false,
      editDialogVisible: false, // 控制编辑对话框的显示状态
      editedThesisName: '', // 存储编辑后的论文题目
      editedThesisId: null,
      disabledInput: true,
      timer: null,
      select_pubName: [],
      ispubFlag: false,
      ispubShow: false,
      empPaperName: "", //添加论文中的论文名称
      view_point: 0,
      headers: {
        "Content-Type": "multipart/form-data",
      },
      files: [], //选择上传的文件列表
      urlFile: "", //文件路径
      addButtonState: true, //是否允许添加论文
      operList: [], //每个论文的历史操作记录
      remarksort: [], //对显示的驳回理由做排序
      writer: "", //和输入的作者列表绑定
      options: [], //存储所有刊物对象
      data_picker: "", //选择时间
      ulList: false,
      labelPosition: "left",
      title: "",
      title_show: "",
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      emps: [],
      commentTotal: [],
      commentPass: [],
      commentDeny: [],

      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo: false,

      total: 0, // 现在显示的数据个数
      prePlan: "", // 上期安排
      preDate: new Date(), // 上一条记录的时间
      nextDate: new Date(), // 下一条记录的时间
      thesisID: 0,
      timeChoose: 0,
      curIndex: 0,
      showTooltip: true,
      pageMonth: new Date().getMonth(),
      thesis: {},

      page: 1,
      keyword: "",
      size: 10,
      positions: [],
      publicationName: "", //所属期刊
      publicationID: -1,
      startPage: "",
      endPage: "",

      emp: {
        id: null,

        num: null,
        thesisID: null,
        preSum: "",
        nextPlan: "",
        dateStu: new Date(),
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },
  watch: {
    publicationName: {
      handler(val) {
        //函数抖动
        this.delayInputTimer(val);
      },
    },
    data_picker: {
      //清除时间input设置为不可输入
      handler(val) {
        if (!val) {
          this.disabledInput = true;
        }
      },
    },
  },
  computed: {
    menuHeight() {
      return this.publicationName.length * 50 > 150
          ? 150 + "px"
          : "${this.publicationName.length * 50}px";
    },
  },
  created() {
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.initEmps();
    this.fetchThesisExistDate()
  },
  filters: {},
  methods: {
    async fetchThesisExistDate() {
      try {
        const url = `/undergraduateM/basic/getThesisExistDate?institutionID=${this.user.institutionID}`;
        const response = await this.getRequest(url);
        if (response.status === 200) {
          this.options = this.transformOptions(response.obj);
        } else {
          throw new Error("请求失败!");
        }
      } catch (error) {
        throw new Error("请求出现异常!");
      }
    },
    transformOptions(options) {
      return options.map(option => {
        let year = option.substring(0, 4);
        let season = option.slice(-2) === '春季' ? 3 : 9;
        let optionValue = year + season

        return {value: optionValue, label: option};
      });
    },
    handleSelectSemesterChange() {
      this.startYear = parseInt(this.selectDate.substring(0, 4));
      this.selectSemester = parseInt(this.selectDate.charAt(4));
      this.initEmps();
    },
    onSuccess(res) {
      const {status, msg, obj} = res;

      if (status === 200) {
        this.handleSuccessfulImport(obj);
      } else {
        this.$message.error(msg)
      }
    },
    handleSuccessfulImport(obj) {
      const {
        duplicateInsertRowsCount,
        failedRowsCount,
        failureReasons,
        successfulRowsCount,
        total,
      } = obj;

      this.$message.success("导入成功");
      this.dialogStudentVisible = false
      this.initEmps();
      this.duplicateInsertRowsCount = duplicateInsertRowsCount;
      this.failedRowsCount = failedRowsCount;
      this.failureReasons = failureReasons;
      this.successfulRowsCount = successfulRowsCount;
      this.totalRows = total;
      this.dialogImportResult = true;
    },
    beforeUpload() {//上传前
      this.$message.success("正在导入")
    },
    downloadExcel(val) {
      let url = `/undergraduateM/basic/exportUnderGraduate?type=` + val
      this.$message.success('正在下载')
      window.open(url, '_parent')
    },
    importStudents() {
      this.dialogStudentVisible = true;
    },
    openEditDialog(row) {
      // 当点击编辑按钮时，设置要编辑的论文题目并显示对话框
      this.editedThesisName = row.thesis.name;
      this.editedThesisId = row.thesis.id;
      this.editDialogVisible = true;
    },
    async saveEditedThesisName() {
      try {
        let url = `/paperComment/basic/editThesisName?thesisId=${this.editedThesisId}&thesisName=${encodeURIComponent(this.editedThesisName)}`;
        const resp = await this.getRequest(url);

        if (resp.status === 200) {
          this.$message.success("修改论文题目成功！");
          await this.initEmps();
        } else {
          this.$message.error(resp.msg);
        }
      } catch (error) {
        console.error("编辑论文题目时出现错误:", error);
      } finally {
        this.editDialogVisible = false;
      }
    },
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:13px;text-align:center";
    },

    emptyEmp() {
      this.emp = {
        dateStu: new Date(),
        id: null,
        num: this.total + 1,
        preSum: "",
        nextPlan: "",
        thesisID: null,
        tutorComment: null,
      };
    },

    initEmps() {
      if (this.startYear == null) {
        return;
      }

      this.loading = true;
      const url = `/paperComment/basic/getStuThesis?tutorId=${this.user.id}&year=${this.startYear}&month=${this.selectSemester}`;

      this.getRequest(url)
          .then((resp) => {
            this.loading = false;
            if (resp.status == 200) {
              this.emps = resp.obj;
            } else {
              this.$message.error(resp.msg);
            }
          })
          .catch((error) => {
            console.error(error);
            this.loading = false;
          });
    },


    // 导出pdf
    exportPDF(data) {
      this.loading = true;
      if (this.thesisID !== null) {
        let url = `/paperComment/basic/exportPDF?thesisID=${encodeURIComponent(data.thesis.id)}`;
        fetch(url)
            .then((response) => response.blob())
            .then((blob) => {
              this.loading = false;
              const fileURL = URL.createObjectURL(blob);

              const a = document.createElement('a');
              a.href = fileURL;
              a.download = data.sname+'_毕业论文评审记录.pdf'; // 在这里设置你想要的文件名
              a.style.display = 'none';
              document.body.appendChild(a);
              a.click();
              document.body.removeChild(a);
            })
            .catch((error) => {
              this.loading = false;
              this.$message.error("导出PDF时发生错误！");
            });
      } else {
        this.loading = false;
        this.$message.info("抱歉该学生还未添加毕设设计或论文！");
      }
    },


    //查看详情
    showInfoItem(data) {
      const {id, sname, studentnumber} = data;
      const {startYear, selectSemester} = this;

      this.$router.push({
        path: `/teacher/stuPaperComment`,
        query: {keyword: id, keyname: sname, studentNumber: studentnumber, year: startYear, month: selectSemester},
      });
    },

  },
};
</script>

<style>
.el-loading-spinner {
  font-size: 20px;
  /*font-weight: bold;*/
}

.el-loading-spinner .el-loading-text {
  font-size: 18px;
}

.el-tooltip__popper {
  max-width: 750px;
}

.table-with-shadow {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.el-date-table td.is-disabled {
  visibility: hidden;
}

.red-cell {
  color: red;
}

/*选择日期，年份的隐藏 */
.el-date-editor {
  border: none;
  box-shadow: none;
}

.select_div_input {
  /*margin-left:3px;*/
  width: 80%;
  height: 32px;
  position: relative;
  display: inline-block;
}

.select_div {
  border: 0.5px solid lightgray;
  border-radius: 3px;
  margin-top: 5px;
  font-size: 14px;
  position: absolute;
  background-color: #fff;
  z-index: 999;
  overflow: hidden;
  width: 90%;
  cursor: pointer;
}

.select_div_div:hover {
  background-color: lightgray;
}

.select_div_div {
  padding-bottom: 2px;
  /*padding-top: 7px;*/
  padding-left: 12px;
  width: 100%;
}

/*input[type=text]::placeholder {*/
/*  color:lightgrey;*/
/*}*/
/*input:focus{*/
/*  border:1px solid lightblue;*/
/*}*/
.showInfo_dialog .el-form-item {
  margin-bottom: 5px;
}

.isMust {
  position: absolute;
  color: #f56c6c;
  top: 2px;
  left: -100px;
}

.el-form-item label {
  text-align: justify;
}

/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
#selectItem ul {
  list-style: none;
  /* height: 100px; */
  width: 200px;
  /* background: red; */
  /* border: 1px solid #eee; */
  /* visibility: hidden; */
}

#selectItem ul li {
  height: 28px;
  line-height: 28px;
  padding-left: 10px;
  cursor: pointer;
}

.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

/* .slide-fade-leave-active for below version 2.1.8 */
.slide-fade-enter,
.slide-fade-leave-to {
  transform: translateX(10px);
  opacity: 0;
}

div::-webkit-scrollbar {
  /* 隐藏默认的滚动条 */
  -webkit-appearance: none;
}

div::-webkit-scrollbar:vertical {
  /* 设置垂直滚动条宽度 */
  width: 6px;
}

/* 这里不需要用到这个 */
/* div::-webkit-scrollbar:horizontal{ */
/* 设置水平滚动条厚度 */
/* height: 2px; */
/* } */

div::-webkit-scrollbar-thumb {
  /* 滚动条的其他样式定制，注意，这个一定也要定制，否则就是一个透明的滚动条 */
  border-radius: 8px;
  border: 3px solid rgba(255, 255, 255, 0.4);
  background-color: rgba(0, 0, 0, 0.5);
}
</style>
