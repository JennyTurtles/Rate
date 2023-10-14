<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView" :disabled="!isShowAddButton">
            添加记录
          </el-button>
          <el-button type="primary" icon="el-icon-download" @click="exportPDF">
            导出PDF
          </el-button>
        </div>
      </div>
    </div>

    <div style="margin-top: 10px">
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
        <el-table-column align="center" label="操作" width="220px">
          <template slot-scope="scope">
            <div
                style="
                text-align: center;
                height: 300px;
                display: flex;
                flex-direction: column;
                justify-content: center;
              "
            >
              <div style="margin-bottom: 10px; left: 0">
                提交次序:
                <span
                    style="
                    display: inline-block;
                    width: 100px;
                    text-align: left;
                    margin-left: 10px;
                  "
                >
                  {{ scope.row.num }}
                </span>
              </div>
              <div style="margin-bottom: 10px">
                提交日期:
                <span
                    style="
                    display: inline-block;
                    width: 100px;
                    text-align: left;
                    margin-left: 10px;
                  "
                >{{ scope.row.dateStu }}</span
                >
              </div>
              <div style="margin-bottom: 10px; left: 0">
                审核结果:
                <span
                    style="
                    display: inline-block;
                    width: 100px;
                    text-align: left;
                    margin-left: 10px;
                  "
                    :style="
                    scope.row.isPass == 'tea_deny'
                      ? { color: 'red' }
                      : { color: 'green' }
                  "
                >
                  {{
                    scope.row.isPass == "tea_pass"
                        ? "导师通过"
                        : scope.row.isPass == "tea_deny"
                            ? "导师驳回"
                            : "暂无"
                  }}</span
                >
                <!-- <div :style="{ color: emp.isPass === '驳回' ? 'red' : '' }"> {{ emp.isPass }}
                                </div> -->
              </div>
              <div style="display: flex; justify-content: center">
                <el-button
                    @click="showEditEmpView(scope.row)"
                    style="padding: 4px; margin-right: 10px"
                    size="mini"
                    icon="el-icon-edit"
                    type="primary"
                    plain="plain"
                    v-show="scope.row.isPass == 'tea_pass' ? false : true"
                >编辑
                </el-button>
                <el-button
                    @click="deleteEmp(scope.row)"
                    style="padding: 4px"
                    size="mini"
                    type="danger"
                    icon="el-icon-delete"
                    plain="plain"
                    v-show="
                    scope.$index === total - 1 && scope.row.isPass != 'tea_pass'
                      ? true
                      : false
                  "
                >删除
                </el-button>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="详细信息">
          <template slot-scope="scope">
            <div class="table-cell">
              <p>
                <strong
                    style="
                    display: inline-block;
                    min-width: 0px;
                    text-align: left;
                  "
                >上期总结：</strong
                >
                {{ scope.row.preSum }}
              </p>

              <p>
                <strong
                    style="
                    display: inline-block;
                    min-width: 0px;
                    text-align: left;
                  "
                >下期计划：</strong
                >
                {{ scope.row.nextPlan }}
              </p>
              <p>
                <strong
                    style="
                    display: inline-block;
                    min-width: 0px;
                    text-align: left;
                  "
                >导师评价：</strong
                >
                {{ scope.row.tutorComment || "暂无评价" }}
              </p>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加记录对话框 -->
    <el-dialog
        :close-on-click-modal="false"
        :title="title"
        :visible.sync="dialogVisible"
        width="50%"
        center="center"
    >
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="300px"
          :model="emp"
          :rules="rules"
          ref="empForm"
      >
        <el-form-item
            label="提交次序:"
            prop="num"
            label-width="80px"
            style="margin-left: 20px"
        >
          <!-- <span class="isMust">*</span> -->
          <span>
            {{ emp.num }}
          </span>
        </el-form-item>

        <el-row>
          <el-form-item
              prop="dateStu"
              label="指导时间:"
              label-width="80px"
              style="margin-left: 20px"
          >
            <span class="isMust">*</span>
            <el-date-picker
                style="width: 200px"
                value-format="yyyy-MM-dd"
                v-model="emp.dateStu"
                type="date"
                placeholder="请选择指导时间"
                :picker-options="pickerOptions"
                v-if="showTimeSelect2"
            ></el-date-picker>
            <span v-if="showTimeSelect">{{ emp.dateStu }}</span>

            <el-tooltip
                effect="light"
                popper-class="btnitem"
                :content="prePlan"
                placement="top"
                v-if="showTooltip"
            >
              <span
                  style="
                  display: inline-block;
                  margin-left: 20px;
                  color: #409eff;
                  position: relative;
                "
              >上期安排
                <span
                    style="
                    position: absolute;
                    bottom: 0px;
                    left: 0;
                    right: 0;
                    height: 2px;
                    background-color: #303133;
                    top: 28px;
                    transform: translateY(-1px);
                  "
                ></span>
              </span>
            </el-tooltip>
          </el-form-item>
        </el-row>

        <el-form-item
            label="上期总结:"
            prop="preSum"
            label-width="80px"
            style="margin-left: 20px"
        >
          <span class="isMust">*</span>
          <el-input
              type="textarea"
              size="medium"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="emp.preSum"
              placeholder="请输入上期总结"
              :show-word-limit="true"
              :rows="8"
              :maxlength="400"
          ></el-input>
        </el-form-item>

        <el-form-item
            label="下期计划:"
            prop="nextPlan"
            label-width="80px"
            style="margin-left: 20px"
        >
          <span class="isMust">*</span>
          <el-input
              type="textarea"
              size="medium"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="emp.nextPlan"
              placeholder="请输入下期安排"
              :show-word-limit="true"
              :rows="8"
              :maxlength="400"
          ></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click.stop.prevent="handleCancel">取 消</el-button>
        <el-button type="primary" @click="doAddEmp()" v-show="true"
        >提 交</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "stuPaperComment",
  data() {
    return {
      isShowAddButton: false,
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
      pictLoading: false,
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo: false,
      labelPosition: "left",
      title: "",
      title_show: "",

      total: 0, // 现在显示的数据个数
      prePlan: "", // 上期安排
      preDate: new Date(), // 上一条记录的时间
      nextDate: new Date(), // 下一条记录的时间
      thesisID: null,
      timeChoose: 0,
      curIndex: 0,
      showTooltip: true,
      pageMonth: new Date().getMonth(),
      showTimeSelect: true,
      showTimeSelect2: false,


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
      rules: {
        dateStu: [
          {
            required: true,
            message: "请输入指导时间",
            trigger: "blur",
          },
        ],
        preSum: [
          {
            required: true,
            message: "请输入上期总结",
            trigger: "blur",
            min: 1,
          },
        ],
        nextPlan: [
          {
            required: true,
            message: "请输入下期计划",
            trigger: "blur",
            min: 1,
          },
        ],
      },
      pickerOptions: {
        disabledDate: (time) => {
          const date = new Date(time);
          return this.disabledTime(date);
        },
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
    user() {
      return this.$store.state.currentHr; //object信息
    },
    menuHeight() {
      return this.publicationName.length * 50 > 150
          ? 150 + "px"
          : "${this.publicationName.length * 50}px";
    },
  },
  created() {
  },
  mounted() {
    this.initEmps();
  },
  filters: {},
  methods: {
    exportPDF() {
      this.loading = true;

      if (this.thesisID !== null) {
        let url = `/paperComment/basic/exportPDF?thesisID=${encodeURIComponent(this.thesisID)}`;
        fetch(url)
            .then((response) => response.blob())
            .then((blob) => {
              this.loading = false;
              const fileURL = URL.createObjectURL(blob);

              const a = document.createElement('a');
              a.href = fileURL;
              a.download = JSON.parse(localStorage.getItem('user')).name+'_毕业论文评审记录.pdf'; // 在这里设置你想要的文件名
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
        this.$message.info("抱歉你还未添加毕设设计或论文！");
      }
    },



    handleCancel(event) {
      event.stopPropagation();
      event.preventDefault();
      this.dialogVisible = false;
      this.initEmps();
    },
    disabledTime(date) {
      const today = new Date();
      const sevenDaysAgo = new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000);
      switch (this.timeChoose) {
        case 10:
        case 13:
          return false;
        case 15:
        case 11:
          return (
              date.getTime() <= new Date(this.preDate).getTime() ||
              date.getTime() < sevenDaysAgo.getTime() ||
              date.getTime() > today.getTime()
          );
        case 12:
          return date.getTime() >= new Date(this.nextDate).getTime();
        case 14:
          return (
              date.getTime() >= new Date(this.nextDate).getTime() ||
              date.getTime() <= new Date(this.preDate).getTime()
          );
        default:
          console.log("时间限制出现了其他的问题！请检查");
          return true;
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

    //编辑按钮
    showEditEmpView(data) {
      this.title = "编辑记录信息";

      this.emp = data;
      this.emp.id = 1;
      // 修改编辑时日期的显示状态
      this.showTimeSelect = this.emp.isPass !== "tea_pass" ? false : true;
      this.showTimeSelect2 = this.emp.isPass !== "tea_pass" ? true : false;

      if (data.num > 1) {
        this.showTooltip = true;
        this.prePlan = this.emps[data.num - 2].nextPlan;
        // 不是第一条记录
        if (data.num < this.total) {
          this.timeChoose = 14;
          this.preDate = this.emps[data.num - 2].dateStu;
          this.nextDate = this.emps[data.num].dateStu;
        } else if ((data.num = this.total)) {
          this.timeChoose = 15;
          this.preDate = this.emps[data.num - 2].dateStu;
        }
      } else {
        this.showTooltip = false;
        this.prePlan = "";
        // 是第一条记录
        if (this.total > 1) {
          this.timeChoose = 12;
          this.nextDate = this.emps[data.num].dateStu;
        } else if (this.total == 1) {
          this.timeChoose = 13;
        }
      }

      this.dialogVisible = true;
    },
    deleteEmp(data) {
      const confirmationMessage = "此操作将永久删除【第" + data.num + "条记录】, 是否继续?";
      if (confirm(confirmationMessage)) {
        this.deleteRequest("/paperComment/basic/remove/" + data.num + "/" + data.thesisID)
            .then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
      }
    },
    doAddEmp() {
      if (this.emp.id == 1) {
        var empdata = this.emp;
        this.emptyEmp();
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.num = empdata.num;
            this.emp.preSum = empdata.preSum;
            this.emp.nextPlan = empdata.nextPlan;
            this.emp.dateStu = empdata.dateStu;
            this.emp.studentID = JSON.parse(localStorage.getItem("user")).id;
            this.emp.thesisID = empdata.thesisID;
            this.emp.dateTea = null;
            this.emp.isPass = null;
            this.emp.tutorComment = "";

            const _this = this;

            this.postRequest1("/paperComment/basic/edit", _this.emp).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          }
        });
      } else {
        var empdata = this.emp;
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.num = this.total + 1;
            this.emp.preSum = empdata.preSum;
            this.emp.nextPlan = empdata.nextPlan;
            this.emp.dateStu = empdata.dateStu;
            this.emp.studentID = JSON.parse(localStorage.getItem("user")).id;
            this.emp.thesisID = this.thesisID;
            this.emp.isPass = null;

            const _this = this;

            if (this.total > 0) {
              let date1 = Date.parse(this.emps[this.total - 1].dateStu);
              let date2 = Date.parse(this.emp.dateStu);

              if (date1 + 86400000 > date2) {
                this.$message.error({message: "请选择合适的指导时间！"});
                return;
              }
            }

            this.postRequest1("/paperComment/basic/add", _this.emp).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          }
        });
      }
    },

    showAddEmpView() {
      //点击添加记录按钮
      this.options = [];
      this.emptyEmp();
      this.title = "添加记录";
      this.dialogVisible = true; //440
      this.curIndex = this.total + 1;
      this.showTimeSelect = false;
      this.showTimeSelect2 = true;

      // 添加限制条件 时间框选择
      if (this.total != 0) {
        this.showTooltip = true;
        this.prePlan = this.emps[this.total - 1].nextPlan;
        this.timeChoose = 11;
        this.preDate = this.emps[this.total - 1].dateStu;
      } else {
        this.showTooltip = false;
        this.prePlan = "";
        this.timeChoose = 10;
      }
    },

    async initEmps() {
      try {
        this.loading = true;
        const user = JSON.parse(localStorage.getItem("user"));
        const studentID = user && user.id;
        if (!studentID) {
          console.error("无法从存储中获取学生ID。");
          return;
        }
        const tidResp = await this.getRequest("/paperComment/basic/getThesisID?stuID=" + studentID);
        if (tidResp.data != null) {
          this.thesisID = tidResp.data;
          const url = "/paperComment/basic/getAllCommentStu?thesisID=" + this.thesisID;
          this.isShowAddButton = true;
          const resp = await this.getRequest(url);
          this.emps = resp.data;
          this.total = resp.data.length;
        } else {
          this.$message.info("请首先添加毕业论文！")
        }
      } catch (err) {
        console.error(err);
      } finally {
        this.loading = false;
      }
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
