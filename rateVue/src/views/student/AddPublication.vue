<template>
  <div>
    <el-dialog title="选择指标点分类" center :visible.sync="showTree" width="60%">
      <span class="el-tree-node">
        <el-tree
            :data="data"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :expand-on-click-node="false"
            :highlight-current="true"
            default-expand-all
        ></el-tree>
      </span>
    </el-dialog>

      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="180px"
          :model="publish"
          :rules="rulesPublication"
          ref="publicationForm"
      >
      <el-form-item label="期刊全称:" prop="publicationName" label-width="90px" style="margin-left: 20px;">
        <span class="isMust">*</span>
        <el-input
            size="mini"
            style="width:40%"
            prefix-icon="el-icon-edit"
            v-model="publish.publicationName"
            @blur="queryPublication"
            :disabled="inputDisabled"
            placeholder="请输入期刊全称"
        ></el-input>
      </el-form-item>
      <el-form-item label="刊物简称:" prop="publicationAbbr" label-width="90px" style="margin-left: 20px;">
        <span class="isMust">*</span>
        <el-input
            size="mini"
            style="width:40%"
            prefix-icon="el-icon-edit"
            v-model="publish.publicationAbbr"
            :disabled="inputDisabled"
            placeholder="请输入刊物简称"
        ></el-input>
      </el-form-item>
      <el-form-item label="出版社:" prop="publisherName" label-width="90px" style="margin-left: 20px;">
        <span class="isMust">*</span>
        <el-input
            size="mini"
            style="width:40%"
            prefix-icon="el-icon-edit"
            v-model="publish.publisherName"
            :disabled="inputDisabled"
            placeholder="请输入出版社"
        ></el-input>
      </el-form-item>
      <el-form-item label="网址:" prop="publicationUrl" label-width="90px" style="margin-left: 20px;">
        <span class="isMust">*</span>
        <el-input
            size="mini"
            style="width:40%"
            prefix-icon="el-icon-edit"
            v-model="publish.publicationUrl"
            :disabled="inputDisabled"
            placeholder="请输入网址"
        ></el-input>
      </el-form-item>
      <el-form-item label="录入年份:" prop="year" label-width="90px" style="margin-left: 20px;">
        <span class="isMust">*</span>
        <el-input
            size="mini"
            style="width:40%"
            prefix-icon="el-icon-edit"
            v-model="publish.year"
            @blur="checkYear"
            placeholder="请输入录入年份"
        ></el-input>
      </el-form-item>
      <el-form-item label="指标点分类:" prop="indicatorName" label-width="90px" style="margin-left: 20px;">
        <span class="isMust">*</span>
        <el-button ref="selectBtn" size="mini" type="text" @click="showTreeDialog">{{ buttonText }}</el-button>
      </el-form-item>
      <el-form-item label="证明材料:" prop="publicationProofUrl" label-width="90px" style="margin-left: 20px;">
        <el-upload
            :file-list="filesPublication"
            action="#"
            :limit="1"
            :headers="headers"
            :on-remove="handleDeletePublication"
            :auto-upload="false"
            :on-change="handleChangeFilesPublication"
            :on-exceed="handleExceed"
        >
          <el-button type="primary" icon="el-icon-upload2"
                     slot="trigger"
          >选择文件
          </el-button>&nbsp;&nbsp;&nbsp;&nbsp;
          <span style="color:gray;font-size:11px">只允许doc docx pdf jpg png jpe rar zip类型文件
                  &nbsp;&nbsp;大小不能超过10MB
                </span>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible_publication = false;dialogVisible=true">取 消</el-button>
        <el-button type="primary" @click="doAddPublish" :disabled="cannotAddPublish">提 交</el-button>
    </span>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AddPublication",
  data() {
    return {
      // 和开启tree相关的
      buttonText: '点击选择指标点分类',
      data: [],
      defaultProps: {
        children: "children",
        label: "label",
      },
      indicatorName: '', // 初始化为一个空字符串
      showTree: false, // 控制树形组件的显示与隐藏
      // 和添加or新增期刊信息相关的属性
      cannotAddPublish: true,
      inputDisabled: false,
      publish: {
        id: '',
        publicationId: '',
        indicatorId: '',
        indicatorName: '',
        year: '',
        student_id: '',
        date: '',
        state: '',
        publicationName: '',
        publicationAbbr: '',
        publisherName: '',
        publicationUrl: '',
        publicationProofUrl: ''
      },
      publishToDatabase: {
        id: '',
        publicationId: '',
        indicatorName: '',
        check_duplicates: {
          indicatorId: [],
          year: [],
        },
        student_id: '',
        date: '',
        state: '',
        publicationName: '',
        publicationAbbr: '',
        publisherName: '',
        publicationUrl: '',
        publicationProofUrl: ''
      },
      publication: {
        publicationName: '',
        publicationAbbr: '',
        publisherName: '',
        publicationUrl: '',
        publicationId: '',
      },
      disabledInput: true,
      timer: null,
      select_pubName: [],
      ispubFlag: false,
      ispubShow: false,
      empPaperName: '',//添加论文中的论文名称
      view_point: 0,
      publication_detail: "",
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files: [],//选择上传的文件列表
      filesPublication: [], // 这里上传的是期刊的证明材料
      urlFile: '',//文件路径
      addButtonState: true,//是否允许添加论文
      operList: [],//每个论文的历史操作记录
      remarksort: [],//对显示的驳回理由做排序
      writer: '',//和输入的作者列表绑定
      options: [],//存储所有刊物对象
      data_picker: "",//选择时间
      labelPosition: "left",
      title: "",
      title_show: "",
      title_publication: "",
      allDeps: [],
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_publication: true,
      dialogVisible_show: false,
      dialogVisible_showInfo: false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      publicationName: "",//所属期刊

      publicationId: -1,
      startPage: '',
      endPage: '',
      oper: {
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '学术论文',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      emp: {
        id: null,
        institutionID: null,
        name: null,//论文名称
        year: "",
        month: "",
        rank: "",//排名
        total: "",//总人数
        content: "",
        url: '',
        state: '',
        pubPage: '',
        publicationId: null
      },
      rules: {
        name: [{required: true, message: "请输入论文名", trigger: "blur"}],
        year: [
          {required: true, message: "请输入出版年份", trigger: "blur"},
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
      rulesPublication: {
        publicationName: [
          {required: true, message: '请输入期刊全称', trigger: 'blur'}
        ],
        publicationAbbr: [
          {required: true, message: '请输入刊物简称', trigger: 'blur'}
        ],
        publisherName: [
          {required: true, message: '请输入出版社', trigger: 'blur'}
        ],
        publicationUrl: [
          {required: true, message: '请输入网址', trigger: 'blur'}
        ],
        year: [
          {required: true, message: '请输入录入年份', trigger: 'blur'},
        ],
      }
    };
  },
  watch: {},
  computed: {},
  created() {
  },
  mounted() {
  },
  filters: {},
  methods: {
    // 和tree相关的代码
    showTreeDialog() {
      this.dialogVisible_publication = false;
      this.showTree = true;
      this.initTree()
    },
    // 初始化树
    initTree() {
      var that = this;
      this.getRequest("/indicator").then(function (resp) {
        //此处可以让父组件向子组件传递url,提高复用性
        // console.log(resp);
        that.id = resp.obj[0];
        that.data = resp.obj[1];
      });
    },

    handleNodeClick(data, node) {
      if (data.children.length == 0) {
        this.publish.indicatorId = data.id
        this.buttonText = data.label
        this.showTree = false;
        this.dialogVisible_publication = true;
      }
    },
    // 和添加期刊相关的代码
    openAddDialog() {
      this.emptyPublish()
      this.inputDisabled = false
      this.title_publication = "添加期刊"
      this.dialogVisible = false
      this.dialogVisible_publication = true
    }
    ,
    openUpdateDialog() {
      if (this.inputDisabled) {
        this.publish.publicationName = this.publication.publicationName
        this.publish.publicationAbbr = this.publication.publicationAbbr
        this.publish.publisherName = this.publication.publisherName
        this.publish.publicationUrl = this.publication.publicationUrl
        this.publish.publicationId = this.publication.publicationId
      }
      // 这里需要将之前获取的publication存储在this.publish中
      this.title_publication = "修改期刊"
      this.inputDisabled = true
      this.dialogVisible = false
      this.dialogVisible_publication = true
    }
    ,
    emptyPublish() {
      this.inputDisabled = false
      this.buttonText= '点击选择指标点分类',
      this.filesPublication = [],
      this.publish = {
        id: '',
        publicationId: '',
        indicatorId: '',
        indicatorName: '',
        year: '',
        student_id: '',
        date: '',
        state: '',
        publicationName: '',
        publicationAbbr: '',
        publisherName: '',
        publicationUrl: '',
        publicationProofUrl: ''
      };
      this.publishToDatabase = {
        id: '',
        publicationId: '',
        indicatorName: '',
        check_duplicates: {
          indicatorId: [],
          year: [],
        },
        student_id: '',
        date: '',
        state: '',
        publicationName: '',
        publicationAbbr: '',
        publisherName: '',
        publicationUrl: '',
        publicationProofUrl: ''
      };
    },
    handleDeletePublication() {//删除选择的文件
      var file = {
        filepath: this.publish.publicationProofUrl
      }
      this.postRequest1("/publicationSubmission/deleteFile", file).then(
          (res) => {
            this.$message.success('删除成功！')
          }, () => {
            this.$message.success('删除失败！')
          }
      )
    },

    handleChangeFilesPublication(file) {//文件列表数量改变
      this.filesPublication = []
      var attachmentType = [
        "doc", "docx", "pdf", "jpg", "png", "jpeg", "rar", "zip"]
      var type = file.name.split('.')
      if (file.size > 10 * 1024 * 1024) {
        this.$message.error('上传文件大小不能超过10MB!');
        return
      }
      if (attachmentType.indexOf(type[type.length - 1].toLowerCase()) === -1) {
        this.$message.error("不支持上传该类型的附件")
        return
      }
      var formData = new FormData();
      this.filesPublication.push(file);
      formData.append("file", this.filesPublication[0].raw)
      this.postRequest("/publicationSubmission/upload", formData, {
        headers: {
          'token': this.user ? this.user.token : ''
        }
      }).then(
          (response) => {
            this.$message({
              message: '上传成功！'
            })
            this.cannotAddPublish = false
            //获取文件路径
            this.publish.publicationProofUrl = response.data
          }, () => {
          }
      )
    }
    ,
    checkYear() {
      if (this.timer) {
        clearTimeout(this.timer)
      }
      this.timer = setTimeout(() => {
        if (!this.publish.year) {
          return
        }
        const isValidYear = /^\d{4}$/.test(this.publish.year);
        if (isValidYear) {
          return;
        } else {
          this.$message.error("年份不合法！请输入合理的年份")
        }
      }, 100);
    },
    queryIndicatorOrder() {
      if (this.timer) {
        clearTimeout(this.timer)
      }
      const regex = /^\d+\.\d+\.\d+$/;

      if (!regex.test(this.publish.indicatorName)) {
        this.$message.error('输入的指标点分类不合法，请参考格式1.1.1');
        return
      }

      this.timer = setTimeout(() => {
        var url = `/indicator/getIndicatorId/${encodeURIComponent(this.publish.indicatorName)}`;
        this.getRequest(url)
            .then((resp) => {
              if (resp && resp.obj) {
                this.publish.indicatorId = resp.obj;
              } else {
                this.$message.error('输入的指标点分类不存在，请查阅指标点分类');
              }
            });
      }, 100);
    },
    queryPublication() {
      // 查重
      if (this.timer) {
        clearTimeout(this.timer)
      }
      this.timer = setTimeout(() => {
        if (this.publish.publicationName) {
          var url = `/publication/getInf/${encodeURIComponent(this.publish.publicationName)}`;
          this.getRequest(url).then((resp) => {
            if (resp && resp.obj) {
              this.publish.publicationAbbr = resp.obj[0].abbr;
              this.publish.publicationName = resp.obj[0].name;
              this.publish.publisherName = resp.obj[0].publisher;
              this.publish.publicationUrl = resp.obj[0].url;
              this.publish.publicationId = resp.obj[0].id;
              this.inputDisabled = true;
              // console.log(resp)
              const years = [];
              const indicatorIds = [];
              for (let i = 0; i < resp.obj.length; i++) {
                const year = resp.obj[i].year;
                const indicatorId = resp.obj[i].id;
                years.push(year);
                indicatorIds.push(indicatorId);
              }
              this.publishToDatabase.check_duplicates.indicatorId = indicatorIds;
              this.publishToDatabase.check_duplicates.year = years;


              this.$message.success('该期刊已经在数据库中存在');
            }
          });
        }
      }, 200);
    },
// 添加期刊提交记录
    async doAddPublish() {
      // 表单验证
      this.cannotAddPublish = true
      if (this.publish.indicatorId !== null && this.publish.indicatorId !== "") {
        this.cannotAddPublish = false
        this.$refs["publicationForm"].validate(async valid => {
          if (valid) {
            const year = this.publish.year;
            const indicatorId = this.publish.indicatorId;
            const duplicates = this.publishToDatabase.check_duplicates;

            const length = duplicates.indicatorId.length;
            for (let i = 0; i < length; i++) {
              if (year == duplicates.year[i] && indicatorId == duplicates.indicatorId[i]) {
                this.$message.error("这个期刊已经在数据库中存在了！");
                return;
              }
            }

            this.publishToDatabase = {
              publicationAbbr: this.publish.publicationAbbr,
              publicationName: this.publish.publicationName,
              publisherName: this.publish.publisherName,
              publicationUrl: this.publish.publicationUrl,
              publicationId: this.publish.publicationId,
              indicatorId,
              year,
              studentId: JSON.parse(localStorage.getItem('user')).id,
              date: this.dateFormatFunc(new Date()),
              state: "commit",
              publicationProofUrl: this.publish.publicationProofUrl
            };


            const url = `/publicationSubmission/insert/`;
            try {
              const resp = await this.postRequest1(url, this.publishToDatabase);
              if (resp && resp.msg === "200") {
                this.$message.success("成功发送！");
                await this.emptyPublish()
              }
            } catch (error) {
              this.$message.error(error);
            }
          } else {
            // 表单验证不通过，给出错误提示
            this.$message.error("表单填写不完整或不正确");
          }
        });
      }
    },
    handleExceed() {//超过限制数量
      this.$message.error(`只允许上传1个文件`);
    },
  },
};
</script>

<style>
.el-tree-node {
  background: none;
  color: black;
}

.select_div_input {
  /*margin-left:3px;*/
  width: 80%;
  height: 32px;
  position: relative;
  display: inline-block
}

.select_div {
  border: .5px solid lightgray;
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
  color: #F56C6C;
  top: 2px;
  left: -100px;
}

.el-form-item label {
  text-align: justify;
}

#selectItem {
  display: 'none';
  border: 1px solid #eee;
  width: 200px;
  /* height:100px; */
  position: absolute;
  /* background-color: royalblue; */
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

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
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
  border: 3px solid rgba(255, 255, 255, .4);
  background-color: rgba(0, 0, 0, .5);
}
</style>
