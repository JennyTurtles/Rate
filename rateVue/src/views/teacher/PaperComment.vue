<template>
  <div>
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
          width="500px"
        ></el-table-column>

        <el-table-column
          prop="thesis.comment_total"
          align="center"
          label="已提交"
          width="100px"
        ></el-table-column>
        <el-table-column align="center" label="已评价" width="100px">
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

        <el-table-column align="center" label="操作">
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
  </div>
</template>

<script>
export default {
  name: "teaPaperComment",
  data() {
    return {
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
    // 获取已通过、已驳回、未评价三个数据
    setTimeout(() => {
      // 这里就写你要执行的语句即可，先让数据库的数据加载进去数组中你在从数组中取值就好了
      console.log(this.emps);
      var i = 0;
    }, 800);
  },
  filters: {},
  methods: {
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
      this.loading = true;
      this.teaID = JSON.parse(localStorage.getItem("user")).id;
      const _this = this;
      let url = "/paperComment/basic/getStuThesis?teaID=" + this.teaID;

      this.getRequest(url).then((resp) => {
        this.loading = false;
        console.log(resp);
        if (resp) {
          _this.emps = resp.data;
          // console.log(this.emps);
        }
      });
      // console.log(this.emps);
    },

    // 导出pdf
    exportPDF(data) {
      console.log("导出PDF");
      console.log(data);
      this.loading = true;
      // Message.success("正在导出");
      let url = "/paperComment/basic/exportPDF?thesisID=" + data.thesis.id;
      this.getRequest(url).then((resp) => {
        this.loading = false;
        // window.open(url);
        window.location.href = url;
      });
    },

    //查看详情
    showInfoItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/teacher/stuPaperComment",
        query: {
          keyword: data.id,
          keyname: data.sname,
        },
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
