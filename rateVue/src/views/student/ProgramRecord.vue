<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加记录
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
        <el-table-column align="center" label="操作" width="300px">
          <template slot-scope="scope">
            <div
                style="
                text-align: left;
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
                工作时长:
                <span
                    style="
                    display: inline-block;
                    width: 100px;
                    text-align: left;
                    margin-left: 10px;
                  "
                >{{ scope.row.workHours }}小时</span>
              </div>
              <div style="margin-bottom: 10px">
                工作时间:
                <span
                    style="
                    display: inline-block;
                    width: 200px;
                    text-align: left;
                    margin-left: 10px;
                  "
                >{{ scope.row.startDateStu }}至{{scope.row.endDateStu}}</span>
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
        @close="handleCancel"
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
              label="开始时间:"
              label-width="80px"
              style="margin-left: 20px"
          >
            <span class="isMust">*</span>
            <el-date-picker
                style="width: 200px"
                value-format="yyyy-MM-dd"
                v-model="emp.startDateStu"
                type="date"
                placeholder="请选择开始时间"
                @change="startDateChange"
                :picker-options="pickerOptions"
                :first-day-of-week="1"
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
            prop="dateStu"
            label="结束时间:"
            label-width="80px"
            style="margin-left: 20px"
        >
          <span class="isMust">*</span>
          <el-date-picker
              style="width: 200px"
              value-format="yyyy-MM-dd"
              v-model="emp.endDateStu"
              type="date"
              disabled
              v-if="showTimeSelect2"
          ></el-date-picker>
          <span v-if="showTimeSelect">{{ emp.endDateStu }}</span>
        </el-form-item>

        <el-form-item
            label="工作时长:"
            prop="workHours"
            label-width="80px"
            style="margin-left: 20px"
        ><span class="isMust">*</span>
          <el-input-number v-model="emp.workHours" :min="1" :max="maxHours"></el-input-number>
          <span style="margin-left: 10px;color: red">( 注：工作时长平均每天不超过10小时 )</span>
        </el-form-item>

        <el-form-item
            label="阶段小结:"
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
              placeholder="请输入阶段小结："
              :show-word-limit="true"
              :rows="8"
              :maxlength="400"
          ></el-input>
        </el-form-item>

<!--        <el-form-item-->
<!--            label="下期计划:"-->
<!--            prop="nextPlan"-->
<!--            label-width="80px"-->
<!--            style="margin-left: 20px"-->
<!--        >-->
<!--          <span class="isMust">*</span>-->
<!--          <el-input-->
<!--              type="textarea"-->
<!--              size="medium"-->
<!--              style="width: 80%"-->
<!--              prefix-icon="el-icon-edit"-->
<!--              v-model="emp.nextPlan"-->
<!--              placeholder="请输入下期安排"-->
<!--              :show-word-limit="true"-->
<!--              :rows="8"-->
<!--              :maxlength="400"-->
<!--          ></el-input>-->
<!--        </el-form-item>-->
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

import axios from "axios";

export default {
  name: "stuProgramRecord",
  data() {
    return {
      user: {},
      disabledInput: true,
      timer: null,
      headers: {
        "Content-Type": "multipart/form-data",
      },
      emps: [],
      loading: false,
      dialogVisible: false,
      labelPosition: "left",
      title: "",
      title_show: "",
      isEdit: false,

      total: 0, // 现在显示的数据个数
      prePlan: "", // 上期安排
      preDate: new Date(), // 上一条记录的时间
      nextDate: new Date(), // 下一条记录的时间
      timeChoose: 0,
      curIndex: 0,
      showTooltip: true,
      pageMonth: new Date().getMonth(),
      showTimeSelect: true,
      showTimeSelect2: false,
      fillMiss: 0,
      maxHours: 70,
      emp: {
        id: null,
        num: null,
        studentID: null,
        preSum: "",
        nextPlan: "",
        startDateStu: null,
        endDateStu: null,
        workHours: null
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        workHours:[
          {
            required: true,
            message: "请输入工作时长",
            trigger: "blur",
          },
        ],
        startDateStu: [
          {
            required: true,
            message: "请选择开始时间",
            trigger: "blur",
          },
        ],
        endDateStu: [
          {
            required: true,
            message: "请选择结束时间",
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
        disabledDate:(time) =>{
          const date = new Date(time);
          return this.disabledTime(date);
        },
        firstDayOfWeek: 1
      },
      selectProgram:'',
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
    this.getFillMiss();
  },
  methods: {
    handleCancel(event) {
      this.isEdit = false;
      this.dialogVisible = false;
      this.initEmps();
    },
    startDateChange(){
      const timestamp = Date.parse(this.emp.startDateStu)
      const start = new Date(timestamp)
      start.setHours(0,0,0,0)
      const currentTime = new Date();
      currentTime.setHours(0,0,0,0);

      if (new Date(start.getTime() + 6 * 24 * 60 * 60 * 1000) > currentTime)  // 结束日期不超过当天
        this.emp.endDateStu = currentTime
      else{
        if (start.getDay() !== 1)   // 开始日期不是周一，则结束日期为下周的周日
          this.emp.endDateStu = new Date(start.getTime() + (14 - start.getDay()) * 24 * 60 * 60 * 1000)
        else // 开始日期为周一，结束日期为当周的周日
          this.emp.endDateStu = new Date(start.getTime() + 6 * 24 * 60 * 60 * 1000)
      }
      //this.emp.endDateStu = new Date(start.getTime() + 6 * 24 * 60 * 60 * 1000) > new Date() ? new Date() : new Date(start.getTime() + 6 * 24 * 60 * 60 * 1000);
      const difference = Math.abs(this.emp.endDateStu.getTime() - start.getTime());
      const differenceInDays = Math.ceil(difference / (1000 * 60 * 60 * 24)) + 1;
      this.maxHours = differenceInDays * 10;

    },
    disabledTime(date) {
      const currentTime = new Date(date);
      currentTime.setHours(0,0,0,0); //只考虑日期，不考虑时间
      const today = new Date();
      today.setHours(0,0,0,0);
      //超过今天的日期不允许填写
      if (currentTime > today)
        return true;

      // 上次填写的结束日期的后一天设置为可以填写
      const lastEnd = new Date(this.emps[this.total - 1].endDateStu);
      const nextDay = lastEnd.getTime() + (24 * 60 * 60 * 1000);
      const nextDayDate = new Date(nextDay);
      nextDayDate.setHours(0,0,0,0)
      // if (this.formatDate(currentTime) === this.formatDate(nextDayDate) && nextDayDate <= today)
      //   return false;

      // 上次填写的结束日期之前的日期不可以填写
      const lastEndTime = lastEnd.setHours(0,0,0,0);
      if (currentTime <= lastEndTime)
        return true;

      // 禁止选择已经选过的工作区间
      const pickedRangesTimestamp = this.emps.map(item => {
        const startTimestamp = new Date(item.startDateStu);
        const endTimestamp = new Date(item.endDateStu);
        return [startTimestamp, endTimestamp];
      });
      for (const [start, end] of pickedRangesTimestamp) {
        start.setHours(0, 0, 0, 0);
        end.setHours(0, 0, 0, 0);
        if (currentTime >= start && currentTime <= end) {
          return true;
        }
      }

      // 根据是否可以补填补充规则
      if (this.fillMiss === 1){
        const day = currentTime.getDay();
        if (this.formatDate(currentTime) === this.formatDate(nextDayDate) && nextDayDate <= today)
          return false;
        return day !== 1;
      }
      else {
        // 获取当前周的第一天（即本周的星期一）
        const currentWeekFirstDay = today;
        currentWeekFirstDay.setDate(today.getDate() - (today.getDay() === 0 ? 7 : today.getDay()) + 1);
        // 获取上一周的第一天（即上周的星期一）
        const lastWeekFirstDay = new Date(currentWeekFirstDay);
        lastWeekFirstDay.setDate(currentWeekFirstDay.getDate() - 7);

        //console.log(currentWeekFirstDay,lastWeekFirstDay)
        if (currentTime.getTime() === nextDayDate.getTime() && nextDayDate <= today && nextDayDate >= lastWeekFirstDay)
          return false;
        // 只允许选择当前周和上一周的星期一
        return !(currentTime.getTime() === currentWeekFirstDay.getTime() || currentTime.getTime() === lastWeekFirstDay.getTime());
      }
    },
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:13px;text-align:center";
    },
    formatDate(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要加1
      const day = String(date.getDate()).padStart(2, '0');
      return year + '-' + month + '-' + day;
    },
    emptyEmp() {
      this.emp = {
        startDateStu: null,
        endDateStu: null,
        id: null,
        studentID: null,
        workHours: null,
        num: this.total + 1,
        preSum: "",
        nextPlan: "",
        tutorComment: null,
      };
    },

    //编辑按钮
    showEditEmpView(data) {
      this.title = "编辑记录信息";

      this.emp = data;
      this.isEdit = true;
      // 修改编辑时日期的显示状态
      this.showTimeSelect = this.emp.isPass !== "tea_pass" ? false : true;
      this.showTimeSelect2 = this.emp.isPass !== "tea_pass" ? true : false;

      if (data.num > 1) {
        this.showTooltip = true;
        this.prePlan = this.emps[data.num - 2].nextPlan;
      } else {
        this.showTooltip = false;
        this.prePlan = "";
      }
      this.dialogVisible = true;
    },
    deleteEmp(data) {
      const confirmationMessage = "此操作将永久删除【第" + data.num + "条记录】, 是否继续?";
      if (confirm(confirmationMessage)) {
        this.deleteRequest("/programRecord/basic/remove/" + data.num + "/" + data.studentID)
            .then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
      }
    },
    doAddEmp() {
      if (this.emp.workHours > this.maxHours){
        this.$message({
          message: '工作时长平均每天不超过10小时，请重新输入！',
          type: 'warning'
        });
        return;
      }
      if (this.emp.startDateStu === null){
        this.$message({
          message: '请选择工作开始日期！',
          type: 'warning'
        });
        return;
      }
      if (this.isEdit) {
        var empdata = this.emp;
        this.emptyEmp();
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.num = empdata.num;
            this.emp.preSum = empdata.preSum;
            this.emp.nextPlan = empdata.nextPlan;
            this.emp.startDateStu = empdata.startDateStu;
            this.emp.endDateStu = this.formatDate(empdata.endDateStu);
            this.emp.workHours = empdata.workHours;
            this.emp.studentID = this.user.id;
            this.emp.dateTea = null;
            this.emp.isPass = null;
            this.emp.tutorComment = "";

            const _this = this;

            console.log(this.emp)
            this.postRequest1("/programRecord/basic/edit", _this.emp).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          }
          else {
            alert('请确保所有必填项已填写!');
          }
        });
      } else {
        var empdata = this.emp;
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.num = this.total + 1;
            this.emp.preSum = empdata.preSum;
            this.emp.nextPlan = empdata.nextPlan;
            this.emp.startDateStu = empdata.startDateStu;
            this.emp.endDateStu = this.formatDate(empdata.endDateStu);
            this.emp.workHours = empdata.workHours;
            this.emp.studentID = this.user.id;
            this.emp.isPass = null;

            const _this = this;

            this.postRequest1("/programRecord/basic/add", _this.emp).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          }
          else {
            alert('请确保所有必填项已填写!');
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
      } else {
        this.showTooltip = false;
        this.prePlan = "";
      }
    },
    getFillMiss(){
      const url = '/programRecord/basic/getFillMiss?studentID=' + this.user.id;
      this.getRequest(url)
          .then((resp) => {
            if (resp.msg === 'success') {
              this.fillMiss = resp.obj;
            } else {
              this.$message.error(resp.msg);
            }
          })
          .catch((error) => {
            console.error(error);
          });

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
        const headers = {
          'token': localStorage.getItem('user') ? this.user.token : ''
        };

        const url = "/programRecord/basic/getAllRecordStu?studentID=" + studentID;
        const resp = await this.getRequest(url,{headers});
        this.emps = resp.data;
        console.log(this.emps)
        this.total = resp.data.length;
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
.custom-confirm {
  width: 550px; /* Set the width to your desired value */
}

.upload-demo {
  display: inline-block;
  margin-right: 10px;
}

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
