<template>
  <div>
    <div>
      <div style="display: flex; justify-content: space-between; margin: 15px 0">
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
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
          style="width:100%"
          v-loading.fullscreen.lock="loading">

        <el-table-column align="center" label="操作" width="220px">
          <template slot-scope="scope">
            <div
                style="text-align: center; height: 300px; display: flex; flex-direction: column; justify-content: center;">
              <div style="margin-bottom: 10px;left:0">提交次序:
                <span
                    style="display: inline-block; width: 100px; text-align: left; margin-left: 10px;">
                                    {{ scope.row.num }}
                                </span>
              </div>
              <div style="margin-bottom: 10px;">
                提交日期:
                <span
                    style="display: inline-block; width: 100px; text-align: left; margin-left: 10px;">{{
                    scope.row.dateStu
                  }}</span>
              </div>
              <div style="margin-bottom: 10px;left:0">审核结果:
                <span
                    style="display: inline-block; width: 100px; text-align: left; margin-left: 10px;"
                    :style="scope.row.isPass=='tea_deny' ? {'color':'red'}:{'color':'black'}">
                                    {{
                    scope.row.isPass == "tea_pass" ? "导师通过"
                        : scope.row.isPass == "tea_deny" ? "导师驳回"
                        : "暂无"
                  }}</span>
                <!-- <div :style="{ color: emp.isPass === '驳回' ? 'red' : '' }"> {{ emp.isPass }}
                </div> -->

              </div>
              <div style="display: flex; justify-content: center;">
                <el-button
                    @click="showEditEmpView(scope.row)"
                    style="padding: 4px; margin-right: 10px;"
                    size="mini"
                    icon="el-icon-edit"
                    type="primary"
                    plain="plain"
                    v-show="scope.row.isPass=='tea_pass'?false:true ">编辑
                </el-button>
                <el-button
                    @click="deleteEmp(scope.row)"
                    style="padding: 4px"
                    size="mini"
                    type="danger"
                    icon="el-icon-delete"
                    plain="plain"
                    v-show="scope.$index === total-1 && scope.row.isPass!='tea_pass'?true:false ">删除
                </el-button>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="详细信息">
          <template slot-scope="scope">
            <div class="table-cell">
              <p>
                <strong style="display: inline-block; min-width: 0px; text-align: left;">上期总结：</strong>
                {{ scope.row.preSum }}</p>

              <p>
                <strong style="display: inline-block; min-width: 0px;text-align: left;">下期计划：</strong>
                {{ scope.row.nextPlan }}</p>
              <p>
                <strong style="display: inline-block; min-width: 0px; text-align: left;">导师评价：</strong>
                {{ scope.row.tutorComment || '暂无评价' }}</p>
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
        center="center">
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="300px"
          :model="emp"
          :rules="rules"
          ref="empForm">
        <el-form-item
            label="提交次序:"
            prop="num"
            label-width="80px"
            style="margin-left: 20px">
          <span class="isMust">*</span>
          <div>
            {{ emp.num }}
          </div>
        </el-form-item>

        <el-row>
          <el-form-item
              prop="dateStu"
              label="指导时间:"
              label-width="80px"
              style="margin-left: 20px">
            <span class="isMust">*</span>
            <el-date-picker
                style="width: 200px;"
                value-format="yyyy-MM-dd"
                v-model="emp.dateStu"
                type="date"
                placeholder="请选择指导时间"
                :picker-options="pickerOptions"
                v-if="showTimeSelect2"></el-date-picker>
            <span v-if="showTimeSelect">{{ emp.dateStu }}</span>

            <el-tooltip
                effect="light"
                popper-class="btnitem"
                :content="prePlan"
                placement="top"
                v-if="showTooltip">
                            <span
                                style="display: inline-block; margin-left: 20px; color: #409eff; position: relative;">上期安排
                                <span
                                    style="position: absolute; bottom: 0px; left: 0; right: 0; height: 2px; background-color: #303133; top: 28px; transform: translateY(-1px);"></span>
                            </span>

            </el-tooltip>
          </el-form-item>
        </el-row>

        <el-form-item
            label="上期总结:"
            prop="preSum"
            label-width="80px"
            style="margin-left: 20px;">
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
              :maxlength="400"></el-input>

        </el-form-item>

        <el-form-item
            label="下期计划:"
            prop="nextPlan"
            label-width="80px"
            style="margin-left: 20px">
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
              :maxlength="400"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
                <el-button @click.stop.prevent="handleCancel">取 消</el-button>
                <el-button type="primary" @click="doAddEmp()" v-show="addButtonState">提 交</el-button>
            </span>
    </el-dialog>

    <!-- 查看详情按钮 -->
    <el-dialog
        class="showInfo_dialog"
        :title="title_show"
        :visible.sync="dialogVisible_showInfo"
        width="520px"
        center="center">
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          ref="empForm"
          style="margin-left: 20px">
        <el-form-item label="提交次序:" prop="num">
          <span>{{ emp.num }}</span><br/>
        </el-form-item>
        <el-form-item label="指导时间:" prop="dateStu">
          <span>{{ emp.dateStu }}</span><br/>
        </el-form-item>
        <el-form-item label="上期总结:" prop="preSum">
          <span>{{ emp.preSum }}</span><br/>
        </el-form-item>
        <el-form-item label="下期计划:" prop="nextPlan">
          <span>{{ emp.nextPlan }}</span><br/>
        </el-form-item>
        <el-form-item label="导师评价:" prop="tutorComment">
          <span>{{ emp.tutorComment }}</span><br/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button
                    id="but_reject"
                    @click="dialogVisible_showInfo = false"
                    type="primary">关闭</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "stuPaperComment",
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
        "Content-Type": "multipart/form-data"
      },
      pictLoading: false,
      emps: [],
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
      // curIndex: 0,
      showTooltip: true,
      pageMonth: new Date().getMonth(),
      showTimeSelect: true,
      showTimeSelect2: false,

      // tutorName:JSON.parse(sessionStorage.getItem('user')).teachers.name,//导师名字
      oper: {
        operatorRole: "student",
        operatorID: JSON
            .parse(localStorage.getItem("user"))
            .id,
        operatorName: JSON
            .parse(localStorage.getItem("user"))
            .name,
        paperID: null,
        paperName: null,
        pubID: null,
        pubName: null,
        operation: "",
        state: "",
        remark: ""
      },
      emp: {
        id: null,

        num: null,
        thesisID: null,
        preSum: "",
        nextPlan: "",
        dateStu: new Date()
      },
      defaultProps: {
        children: "children",
        label: "name"
      },
      rules: {
        dateStu: [
          {
            required: true,
            message: "请输入指导时间",
            trigger: "blur"
          }
        ],
        preSum: [
          {
            required: true,
            message: "请输入上期总结",
            trigger: "blur",
            min: 1
          }
        ],
        nextPlan: [
          {
            required: true,
            message: "请输入下期计划",
            trigger: "blur",
            min: 1
          }
        ]
      },
      pickerOptions: {
        disabledDate: (time) => {
          const date = new Date(time);
          return this.disabledTime(date);
        }
      }
    };
  },
  watch: {
    publicationName: {
      handler(val) {
        //函数抖动
        this.delayInputTimer(val);
      }
    },
    data_picker: {
      //清除时间input设置为不可输入
      handler(val) {
        if (!val) {
          this.disabledInput = true;
        }
      }
    }
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
    menuHeight() {
      return this.publicationName.length * 50 > 150
          ? 150 + "px"
          : "${this.publicationName.length * 50}px";
    }
  },
  created() {
  },
  mounted() {
    this.initEmps();
    ;
  },
  filters: {},
  methods: {
    exportPDF() {
      console.log("导出PDF");
      this.loading = true;
      // Message.success("正在导出");
      let url = "/paperComment/basic/exportPDF?thesisID=" + this.thesisID;
      this
          .getRequest(url)
          .then((resp) => {
            this.loading = false;
            // window.open(url);
            window.location.href = url;
          })
    },

    handleCancel(event) {
      // 阻止事件冒泡，避免点击其他元素也能关闭对话框
      event.stopPropagation();
      // 阻止默认行为，避免触发其他元素的默认行为
      event.preventDefault();
      // 关闭对话框
      this.dialogVisible = false;
      this.initEmps();
    },
    disabledTime(date) {

      const today = new Date();
      const sevenDaysAgo = new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000);
      // console.log(new Date(this.preDate).getTime());
      switch (this.timeChoose) {
        case 10:
        case 13:
          return false;
          break;
        case 15:
        case 11:
          return (
              date.getTime() <= new Date(this.preDate).getTime() || date.getTime() < sevenDaysAgo.getTime() || date.getTime() > today.getTime()
          );
          break;
        case 12:
          return (date.getTime() >= new Date(this.nextDate).getTime());
          break;
        case 14:
          return (
              date.getTime() >= new Date(this.nextDate).getTime() || date.getTime() <= new Date(this.preDate).getTime()
          );
          break;
        default:
          console.log("时间限制出现了其他的问题！请检查");
          return true;

      }

    },
    timechange(picker) {
      //选择日历调用的方法
      var data = new Date(picker);
      this.emp.year = data.getFullYear();
      this.emp.month = data.getMonth();
      this.disabledInput = false;
      this.options = [];
    },
    inputPubFocus() {
      //input获取焦点判断是否有下拉框，是否可输入
      if (this.emp.year) {
        this.ispubShow = true; //控制下拉框是否显示
        this.disabledInput = false;
      } else {
        this.publicationName = "";
        this
            .$message
            .error("请选择合适的指导时间！");
        this.disabledInput = true;
      }
    },
    filter_pub(val) {
      //选择下拉框的某个期刊 得到选择的期刊的id score等信息
      this.ispubFlag = false;
      this.ispubShow = false;
      var that = this;
      if (!val) {
        return;
      }
      this.publicationName = val.value;
      var url = "/publication/getScore/" + val.indicatorID;
      this
          .getRequest(url)
          .then((resp) => {
            this.loading = false;
            if (resp) {
              if (resp.obj) {
                this.view_point = resp.obj;
                const id = JSON
                    .parse(localStorage.getItem("user"))
                    .id;
                if (this.view_point === 2) {
                  // that.getRequest("/publication/checkScore/"+id).then((resp)=>{
                  //
                  // }
                }
                this.publicationID = val.publicationID;
              } else {
                this.publicationName = "";
                this
                    .$message
                    .error("无该期刊！请重新选择时间！");
              }
              clearInterval(this.timer);
            }
            console.log(this.view_point);
          });
    },
    download(data) {
      //下载证明材料
      var fileName = data
          .url
          .split("/")
          .reverse()[0];
      console.log(fileName);
      if (localStorage.getItem("user")) {
        var url = "/paper/basic/download?fileUrl=" + data.url + "&fileName=" +
            fileName;
        window.location.href = encodeURI(url);
      } else {
        this
            .$message
            .error("请重新登录！");
      }
    },
    handleDelete() {
      //删除选择的文件
      var file = {
        filepath: this.urlFile
      };
      this
          .postRequest1("/paper/basic/deleteFile", file)
          .then((response) => {
            console.log(response);
          }, () => {
          });
    },
    handleExceed() {
      //超过限制数量
      this
          .$message
          .error(`只允许上传1个文件`);
    },
    handleChangeFiles(file, fileList) {
      //文件列表数量改变
      this.files = [];
      var attachmentType = [
        "doc",
        "docx",
        "pdf",
        "jpg",
        "png",
        "jpeg",
        "rar",
        "zip"
      ];
      var type = file
          .name
          .split(".");
      if (file.size > 10 * 1024 * 1024) {
        this
            .$message
            .error("上传文件大小不能超过10MB!");
        return;
      }
      if (attachmentType.indexOf(type[type.length - 1].toLowerCase()) === -1) {
        this
            .$message
            .error("不支持上传该类型的附件");
        return;
      }
      var formData = new FormData();
      this
          .files
          .push(file);
      formData.append("file", this.files[0].raw);
      axios
          .post("/paper/basic/upload", formData, {
            headers: {
              // 'token': window.sessionStorage.getItem('user') ?
              // JSON.parse(window.sessionStorage.getItem('user')).token : ''
              token: localStorage.getItem("user")
                  ? JSON
                      .parse(localStorage.getItem("user"))
                      .token
                  : ""
            }
          })
          .then((response) => {
            this.$message({message: "上传成功！"});
            this.addButtonState = true;
            //获取文件路径
            this.urlFile = response.data;
            // console.log("response:"); console.log(this.urlFile);
          }, () => {
          });
    },

    judgeWriter() {
      //输入作者框 失去焦点触发事件
      var val = this.writer;
      var isalph = false; //判断输入中是否有英文字母
      for (var i in val) {
        var asc = val.charCodeAt(i);
        if ((asc >= 65 && asc <= 90) || (asc >= 97 && asc <= 122)) {
          isalph = true;
          break;
        }
      }
      var num = null;
      // var info=JSON.parse(window.sessionStorage.getItem("user"))
      var info = JSON.parse(localStorage.getItem("user"));
      if (val.indexOf("；") > -1 && val.indexOf(";") == -1) {
        //中文
        num = val.split("；");
      } else if (val.indexOf(";") > -1 && val.indexOf("；") == -1) {
        //英文
        num = val.split(";");
      } else if (val.indexOf("；") > -1 && val.indexOf(";") > -1) {
        //中英都有
        this
            .$message
            .error();
        ("输入不合法请重新输入！");
      } else if (val.indexOf("；") == -1 && val.indexOf(";") == -1) {
        //只有一个人
        if (this.writer != info.name && isalph) {
          this
              .$message
              .error(
                  "您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【" + info.name + " 】，注意拼写要完全正确。"
              );
          this.addButtonState = false;
        } else {
          this.addButtonState = true;
          this.emp.author = this.writer;
          this.emp.rank = 1;
          this.emp.total = 1;
        }
        return;
      }

      //判断自己在不在其中
      if (num.indexOf(info.name) == -1 && !isalph) {
        //不在 并且没有英文单词
        this
            .$message
            .error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【" + info.name + " 】");
        this.addButtonState = false;
      } else if (num.indexOf(info.name) == -1 && isalph) {
        //不在 里面有英文单词
        this
            .$message
            .error(
                "您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【" + info.name + " 】，注意拼写要完全正确。"
            );
        this.addButtonState = false;
      } else {
        //自己在里面 自己是一作不用做任何判断 导师无所谓
        if (num.indexOf(info.name) > 0) {
          //自己不是一作
          if (num.indexOf(info.teachers.name) > 0 || num.indexOf(info.teachers.name) == -1) {
            //导师在作者列表中,不是一作
            this
                .$confirm("第一作者不是导师【 " + info.teachers.name + " 】！积分将为【0】分", "提示", {
                  confirmButtonText: "确定",
                  // cancelButtonText: "取消",
                  type: "warning"
                })
                .then(() => {
                  //点击确定 num=[info.teachers.name,...num] this.writer = num.join(';')
                }, () => {
                });
            this.emp.point = 0;
            this.view_point = this.emp.point;
          }
        }
        this.addButtonState = true;
      }
      this.emp.total = num.length;
      if (num.indexOf(info.teachers.name) > -1) {
        num.splice(num.indexOf(info.teachers.name), 1);
      }
      this.emp.rank = num.indexOf(info.name) + 1;
      this.emp.author = this.writer;
      console.log(this.writer);
      console.log(this.emp.point);
    },
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:13px;text-align:center";
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
        dateStu: new Date(),
        id: null,
        num: null,
        preSum: "",
        nextPlan: "",
        thesisID: null,
        tutorComment: null
      };
    },
    mail(e) {
      console.log(1);
      this
          .postRequest("/test/mail/mail?content=" + e)
          .then((resp) => {
            console.log(resp);
            // this.loading = false;
            console.log(1);
          });
    },
    //编辑按钮
    showEditEmpView(data) {
      // this.initPositions();
      this.title = "编辑记录信息";
      console.log("编辑开始！");

      this.emp = data;
      this.emp.id = 1;
      // this.emps.num = data.num; console.log(this.emps);  也就是这个可以获取当前表格中的所有数据
      // 修改编辑时日期的显示状态
      this.showTimeSelect = (this.emp.isPass != "tea_pass")
          ? false
          : true;
      this.showTimeSelect2 = (this.emp.isPass != "tea_pass")
          ? true
          : false;

      if (data.num > 1) {
        this.showTooltip = true;
        this.prePlan = this
            .emps[data.num - 2]
            .nextPlan;
        // 不是第一条记录
        if (data.num < this.total) {
          this.timeChoose = 14;
          this.preDate = this
              .emps[data.num - 2]
              .dateStu;
          this.nextDate = this
              .emps[data.num]
              .dateStu;
        } else if (data.num = this.total) {
          this.timeChoose = 15;
          this.preDate = this
              .emps[data.num - 2]
              .dateStu;
        }
      } else {
        this.showTooltip = false;
        this.prePlan = "";
        // 是第一条记录
        if (this.total > 1) {
          this.timeChoose = 12;
          this.nextDate = this
              .emps[data.num]
              .dateStu;
        } else if (this.total == 1) {
          this.timeChoose = 13;
        }

      }

      this.dialogVisible = true;
    },
    deleteEmp(data) {
      //点击删除按钮
      console.log(data.thesisID);
      if (confirm("此操作将永久删除【第" + data.num + "条记录】, 是否继续?")) {
        axios
            .delete("/paperComment/basic/remove/" + data.num + "/" + data.thesisID)
            .then((resp) => {
              if (resp) {
                console.log(resp);
                this.dialogVisible = false;
                this.initEmps();
              }
            });
      }
    },
    doAddEmp() {

      //确定是添加记录还是新增记录
      if (this.emp.id) {
        //emptyEmp中没有将id设置为空 所以可以判断
        console.log("编辑记录：");
        var empdata = this.emp;
        this.emptyEmp();
        this
            .$refs["empForm"]
            .validate((valid) => {
              if (valid) {
                this.emp.num = empdata.num
                this.emp.preSum = empdata.preSum
                this.emp.nextPlan = empdata.nextPlan
                this.emp.dateStu = empdata.dateStu
                this.emp.studentID = JSON
                    .parse(localStorage.getItem("user"))
                    .id;
                this.emp.thesisID = empdata.thesisID;
                // 更新导师的一些信息
                this.emp.dateTea = null;
                this.emp.isPass = null;
                this.emp.tutorComment = "";
                const _this = this;
                console.log("_this.emp:");
                console.log(_this.emp);

                this
                    .postRequest1("/paperComment/basic/edit", _this.emp)
                    .then((resp) => {
                      if (resp) {
                        this.dialogVisible = false;
                        this.initEmps();
                      }
                    });

              }
            });
      } else {
        console.log("新增记录：");
        console.log(this.total);
        var empdata = this.emp;
        this
            .$refs["empForm"]
            .validate((valid) => {
              if (valid) {
                this.emp.num = this.total + 1
                this.emp.preSum = empdata.preSum
                this.emp.nextPlan = empdata.nextPlan
                this.emp.dateStu = empdata.dateStu
                this.emp.studentID = JSON
                    .parse(localStorage.getItem("user"))
                    .id;
                this.emp.thesisID = this.thesisID
                this.emp.isPass = null

                const _this = this;

                if (this.total > 0) {
                  // console.log("empdata.dateStu:" + Date.parse(this.emps[this.total
                  // -1].dateStu)); 为了兼容性考虑，火狐浏览器不直接支持2017-08-09格式，所以我现在给他转化为2017/08/09格式
                  console.log("debug:");
                  console.log(this.emps[this.total - 1].dateStu);
                  console.log(this.emp.dateStu);
                  let date1 = Date.parse(this.emps[this.total - 1].dateStu);
                  let date2 = Date.parse(this.emp.dateStu);

                  if (date1 + 86400000 > date2) {
                    this
                        .$message
                        .error({message: "请选择合适的指导时间！"});

                  } else {
                    this
                        .postRequest1("/paperComment/basic/add", _this.emp)
                        .then((resp) => {
                          if (resp) {
                            this.dialogVisible = false;
                            this.initEmps();
                          }
                        });
                  }
                } else {
                  this
                      .postRequest1("/paperComment/basic/add", _this.emp)
                      .then((resp) => {
                        if (resp) {
                          this.dialogVisible = false;
                          this.initEmps();
                        }
                      });
                }
              }
            });
      }
    },

    showAddEmpView() {
      //点击添加记录按钮
      this.addButtonState = true;
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
        this.prePlan = this
            .emps[this.total - 1]
            .nextPlan;
        // console.log(this.prePlan);
        this.timeChoose = 11;
        this.preDate = this
            .emps[this.total - 1]
            .dateStu;
      } else {
        this.showTooltip = false;
        this.prePlan = "";
        this.timeChoose = 10
      }
    },

    initEmps() {
      this.loading = true;
      this.studentID = JSON
          .parse(localStorage.getItem("user"))
          .id;
      // var tID;
      let getTIDPromise = new Promise((resolve, reject) => {
        this
            .getRequest("/paperComment/basic/getTID?stuID=" + this.studentID)
            .then((resp) => {
              if (resp) {
                this.thesisID = resp.data;
                resolve(this.thesisID);
              } else {
                reject(Error("Error getting TID"));
              }
            });
      });

      getTIDPromise.then((tID) => {
        let url = "/paperComment/basic/getAllCommentStu?thesisID=" + this.thesisID;
        // 通过学生ID获取其毕业设计的ID
        this
            .getRequest(url)
            .then((resp) => {
              this.loading = false;
              if (resp) {
                console.log("初始化页面:");
                console.log(resp);
                this.emps = resp.data;
                this.total = resp.data.length;

              }
            });
      });
    },

    searchEmps() {
      this.loading = true;
      console.log("---------", this.keyword);
      const _this = this;
      //let url =
      this
          .getRequest(
              "/paper/basic/byName?name=" + this.keyword + "&page=" + this.page + "&size=" +
              this.size
          )
          .then((resp) => {
            this.loading = false;
            if (resp) {
              this.emps = resp.data;
              this.total = resp.total;
            }
          });
    },
    //查看详情
    showInfo(data) {
      this.title_show = "显示详情";
      this.emp = data;
      // console.log(data);
      this.dialogVisible_showInfo = true;
    }
  }
};
</script>

<style>

.el-loading-spinner {
  font-size: 40px;
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