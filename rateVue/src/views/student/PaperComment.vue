<template>
  <div>
    <!-- 添加记录按钮 -->
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

    <!-- 添加记录对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="60%"
      :center="true"
    >
      <el-form
        :hide-required-asterisk="false"
        label-width="300px"
        :model="emp"
        :rules="rules"
        ref="empForm"
      >
        <el-form-item
          label="第几次记录:"
          prop="whichTime"
          label-width="100px"
          style="margin-left: 20px"
        >
          <span class="isMust">*</span>
          <el-input
            size="medium"
            style="width: 80%"
            prefix-icon="el-icon-edit"
            v-model="emp.whichTime"
            placeholder="请输入这是第几次记录"
          ></el-input>
        </el-form-item>

        <el-form-item
          label="本期工作完成情况:"
          prop="thisWork"
          label-width="100px"
          style="margin-left: 20px"
        >
          <span class="isMust">*</span>
          <el-input
            size="medium"
            style="width: 80%"
            type="textarea"
            prefix-icon="el-icon-edit"
            v-model="emp.thisWork"
            placeholder="请输入本期工作完成情况"
          ></el-input>
        </el-form-item>

        <el-form-item
          label="下期工作安排:"
          prop="nextPlan"
          label-width="100px"
          style="margin-left: 20px"
        >
          <span class="isMust">*</span>
          <el-input
            size="medium"
            style="width: 80%"
            type="textarea"
            prefix-icon="el-icon-edit"
            v-model="emp.nextPlan"
            placeholder="请输入下期工作安排"
          ></el-input>
        </el-form-item>

        <el-form-item
          prop="year"
          label="日期选择:"
          label-width="100px"
          style="margin-left: 20px"
        >
          <span class="isMust">*</span>
          <el-date-picker
            style="width: 80%"
            v-model="data_picker"
            type="month"
            placeholder="请选择提交时间"
            @change="timechange"
          ></el-date-picker>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp()" v-show="addButtonState"
          >提 交</el-button
        >
      </span>
    </el-dialog>

    <div>
      <h3>第{{ emps.whichTime }}次毕业论文或设计评审记录</h3>

      <div style="margin-top: 10px">
        <h1>本期工作完成情况</h1>
        <span>{{ emps.thisWork }}</span>

        <h1>下期工作安排</h1>
        <p>{{ emps.nextPlan }}</p>

        <h1>导师评价</h1>
        <div class="comment-center">
          {{ emps.tutorComment }}
        </div>
      </div>

      <!--             这里可以设置一个按钮，传入whichTime，既可以刷新界面 -->
      <!--             分页功能 -->
      <div class="form-container">
        <form @submit="handleSubmit">
          <input type="number" v-model="emp.whichTime" />
          <button type="submit">Submit</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { postRequest1 } from "@/utils/api";

export default {
  name: "SalSearch",
  data() {
    return {
      disabledInput: true,
      timer: null,
      select_pubName: [],
      ispubFlag: false,
      ispubShow: false,

      view_point: 0,
      headers: {
        "Content-Type": "multipart/form-data",
      },

      addButtonState: true, //是否允许添加论文

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
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo: false,
      total: 0,
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
        whichTime: null,
        thisWork: "",
        nextPlan: "",
        date1: "",
      },
      stuID: -1,
      currentNum: 1,

      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        whichTime: [
          {
            required: true,
            message: "请输入第几次记录",
            trigger: "blur",
          },
          {
            type: "number",
            message: "记录必须为数字值",
          },
        ],
        thisWork: [
          {
            required: true,
            message: "请输入本期工作完成情况",
            trigger: "blur",
          },
        ],
        nextPlan: [
          {
            required: true,
            message: "请输入下期工作安排",
            trigger: "blur",
          },
        ],
      },
    };
  },
  watch: {
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
  created() {},
  mounted() {
    this.initEmps();
  },
  methods: {
    handleSubmit(e) {
      console.log("emp");
      console.log(this.emp);
    },
    timechange(picker) {
      //选择日历调用的方法
      var data = new Date(picker);
      this.emp.year = data.getFullYear();
      this.emp.month = data.getMonth() + 1;
      this.disabledInput = false;
      this.options = [];
    },

    emptyEmp() {
      this.emp = {
        id: null,
        date1: "",
        thisWork: "",
        nextPlan: "",
        tutorComment: "",
      };
    },

    //编辑按钮
    showEditEmpView(data) {
      // this.initPositions();
      this.title = "编辑论文信息";
      this.emp = data;
      this.view_point = data.point;
      this.dialogVisible = true;
      this.publicationName = this.emp.publication.name;
      this.data_picker = new Date(this.emp.year, this.emp.month);
      this.writer = this.emp.author;
      var page = this.emp.pubPage.split("-");
      this.startPage = page[0];
      this.endPage = page[1];
      this.options = [];
      this.getRequest("/publication/basic/list/" + data.year).then((resp) => {
        if (resp) {
          for (var i = 0; i < resp.data.length; i++) {
            this.options.push({
              //修改
              index: resp.data[i].id,
              value: resp.data[i].name,
              point: resp.data[i].score,
            });
          }
        }
      });
      // let url = "/publication/basic/List" this.getRequest(url).then((resp) => {
      // this.loading = false;   if (resp) {     this.options=[]     for(var
      // i=0;i<resp.data[0].length;i++){  待修改        console.log(resp.data)
      // this.options.push({index:resp.data[0][i].id,value:resp.data[0][i].name,point:resp.data[1][i]})
      // }   } });
    },

    deleteEmp(data) {
      //点击删除按钮
      if (confirm("此操作将永久删除【" + data.name + "】, 是否继续?")) {
        axios.delete("/paper/basic/remove/" + data.id).then((resp) => {
          if (resp) {
            console.log(resp);
            this.dialogVisible = false;
            this.initEmps();
          }
        });
      }
    },
    doAddEmp() {
      //确定添加记录 emptyEmp中没有将id设置为空 所以可以判断 if(confirm('确定要提交吗？')){
      var empdata = this.emp;
      this.emptyEmp();
      this.$refs["empForm"].validate((valid) => {
        if (valid) {
          this.emp.id = empdata.id;
          this.emp.date1 = empdata.date1;
          this.emp.thisWork = empdata.thisWork;
          this.emp.nextPlan = empdata.nextPlan;
          this.emp.studentID = JSON.parse(localStorage.getItem("user")).id;
          const _this = this;

          this.postRequest1(
            "/paperComment/basic/getThesisID",
            _this.emp.studentID
          ).then((resp) => {
            if (resp) {
              console.log(resp);
              // _this.emp.underThesisDesignID = resp
            }
          });

          this.postRequest1("/paperComment/basic/edit", _this.emp).then(
            (resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            }
          );
        }
      });
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
      //点击添加论文按钮
      this.addButtonState = true;
      this.options = [];
      this.view_point = 0;
      this.emptyEmp(); //532
      this.writer = "";
      this.data_picker = "";
      this.publicationName = "";
      this.empPaperName = "";
      this.startPage = "";
      this.endPage = "";
      this.urlFile = "";
      this.title = "添加记录";
      this.dialogVisible = true; //440
    },
    initEmps() {
      this.loading = true;
      const _this = this;
      console.log("test");
      this.stuID = JSON.parse(localStorage.getItem("user")).id;
      this.getRequest(
        "/paperComment/basic/findOne?stuID=" + this.stuID + "&whichTime=" + 1
      ).then((resp) => {
        console.log("毕业设计resp:..");
        console.log(resp);
        _this.emps = resp.data;
      });
    },
  },
};
</script>

<style>
h3 {
  font-family: Verdana, Arial, Helvetica, sans-serif;
  font-size: 40px;
  /*color: #6600FF;*/
  text-align: center;
}
.comment-center {
  text-align: center;
  font-weight: bold;
  color: red;
}
.form-container {
  position: absolute;
  bottom: 0;
  right: 0;
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
  left: 0;
}

.el-form-item label {
  text-align: justify;
}
/*#selectItem {*/
/*    display: "none";*/
/*    border: 1px solid #eee;*/
/*    width: 200px;*/
/*    !* height:100px; *!*/
/*    position: absolute;*/
/*    !* background-color: royalblue; *!*/
/*}*/
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