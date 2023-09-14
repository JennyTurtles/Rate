<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加论文
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="emps"
          stripe
          border
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%;"
      >
        <el-table-column
            fixed
            prop="name"
            align="center"
            label="论文名称"
            min-width="20%"
        >
        </el-table-column>
        <el-table-column
            prop="state"
            label="状态"
            min-width="10%"
            align="center"
        >
          <template slot-scope="scope">
            <span
                style="padding: 4px"
                :style="scope.row.state=='tea_reject' || scope.row.state=='adm_reject' ? {'color':'red'}:{'color':'gray'}"
                size="mini"
            >
              {{
                scope.row.state == "commit"
                    ? "已提交"
                    : scope.row.state == "tea_pass"
                        ? "导师通过"
                        : scope.row.state == "tea_reject"
                            ? "导师驳回"
                            : scope.row.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}
              </span>
          </template>
        </el-table-column>
        <el-table-column
            prop="pubName"
            label="发表刊物"
            align="center"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="paperoperList[0].remark"
            label="备注"
            :formatter="checkScoreComent"
            align="center"
            style="width:220px"
            min-width="20%"
        >
        </el-table-column>
        <el-table-column
            prop="point"
            label="积分"
            align="center"
            min-width="8%"
        >
        </el-table-column>
        <el-table-column align="center" width="280px" label="操作" min-width="20%">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
                v-show="scope.row.state == 'commit' || scope.row.state == 'tea_reject' || scope.row.state == 'adm_reject' ? true:false"
            >编辑
            </el-button
            >
            <el-button
                @click="deleteEmp(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
                v-show="scope.row.state == 'tea_reject' || scope.row.state == 'commit' || scope.row.state == 'adm_reject'? true:false"
            >删除
            </el-button
            >
            <el-button
                @click="showInfo(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>

    </div>

    <!-- 添加论文对话框 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :model="currentEmp"
          :rules="rules"
          ref="currentEmp"
      >
        <el-form-item label="论文名称:" prop="name" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentEmp.name"
              placeholder="请输入论文名称"
          ></el-input>
        </el-form-item>
        <el-form-item prop="year" label="发表年月:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-date-picker
              style="width:155px"
              v-model="currentEmp.date"
              type="month"
              placeholder="请选择发表年月"
              @change="timechange"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="所属期刊:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-select
              :disabled="disabledInput"
              v-model="publicationName"
              value-key="id"
              filterable
              remote
              clearable
              reserve-keyword
              @change="filterPublication($event)"
              placeholder="请输入期刊名称"
              :remote-method="inputRemotePublication">
            <el-option
                v-for="item in select_pubName"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
<!--          <div class="select_div_input">-->
<!--            <input-->
<!--                autocomplete="off"-->
<!--                style="width:95%;line-height:28px;-->
<!--                              border:1px solid lightgrey;padding:0 10px 1px 15px;-->
<!--                              border-radius:4px;color:gray"-->
<!--                :disabled="disabledInput"-->
<!--                placeholder="请输入期刊名称"-->
<!--                v-model="publicationName"-->
<!--                @focus="inputPubFocus"-->
<!--                @blur="ispubShow=ispubFlag"-->
<!--                id="input_publicationName"/>-->
<!--            <div class="select_div"-->
<!--                 v-show="ispubShow && publicationName && currentEmp.year ? true:false"-->
<!--                 :style="'height: ${menuHeight}'"-->
<!--                 @mouseover="ispubFlag = true"-->
<!--                 @mouseleave="ispubFlag = false">-->
<!--              <div-->
<!--                  class="select_div_div"-->
<!--                  v-for="val in select_pubName"-->
<!--                  :key="val"-->
<!--                  :value="val"-->
<!--                  @click="filterPublication(val)"-->
<!--              >-->
<!--                {{ val }}-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
        </el-form-item>

        <el-form-item prop="author" label="作者列表:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentEmp.author"
              @blur="judgeWriter()"
              placeholder="请输入作者,如有多个用分号分隔"
          ></el-input>
        </el-form-item>

        <el-form-item label="页码:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width: 32%"
              prefix-icon="el-icon-edit"
              v-model="currentEmp.startPage"
              placeholder="开始页码"
          ></el-input> &nbsp;-&nbsp;
          <el-input
              size="mini"
              style="width: 32%"
              prefix-icon="el-icon-edit"
              v-model="currentEmp.endPage"
              placeholder="结尾页码"
          ></el-input>
        </el-form-item>
        <el-form-item label="证明材料:" prop="url" label-width="80px" style="margin-left: 20px;">
          <el-upload
              :file-list="files"
              action="#"
              :limit="1"
              :headers="headers"
              :on-remove="handleDelete"
              :auto-upload="false"
              :on-change="handleChangeFiles"
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

      <div style="margin-left: 20px;">
        <span style="color:gray;font-size:10px">{{ publication_detail }}</span>
        <span
            style="color: #409eff;  cursor: pointer;font-size:10px"
            @click="openUpdateDialog"
            v-if="publication_detail !== null && publication_detail.length > 0"
        >
            结果有误？点击修改此期刊
          </span>
      </div>
      <div style="margin-left: 20px;font-size: 10px;margin-top: 5px;">
        没有找到期刊？
        <span
            style="color: #409eff;  cursor: pointer;"
            @click="openAddDialog"
        >
            点击添加新的期刊
          </span>  &nbsp;&nbsp;&nbsp;&nbsp;
        <span
            style="color: #409eff;  cursor: pointer;"
            @click="openCheckVue"
        >
            查看期刊审核进度
          </span>
      </div>

      <div style="margin-left: 20px;margin-top: 5px;">
        <span style="color:gray;font-size:10px">将会获得：{{ paperPoint }}积分</span>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp()">提 交</el-button>
      </span>
    </el-dialog>

    <!-- 查看详情按钮 -->
    <el-dialog
        class="showInfo_dialog"
        :title="title_show"
        :visible.sync="dialogVisible_showInfo"
        width="520px"
        center>
      <el-form
          :label-position="labelPosition"
          label-width="100px"
          :model="emp"
          ref="empForm"
          style="margin-left: 20px"
      >
        <el-form-item label="论文名:">
          <span>{{ emp.name }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="积分:">
          <span>{{ emp.point }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="论文状态:">
          <span>{{ emp.state }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="作者人数:">
          <span>{{ emp.total }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="作者列表:">
          <span>{{ emp.author }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="排名:">
          <span>{{ emp.rank }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="发表年份:">
          <span>{{ emp.year }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="发表月份:">
          <span>{{ emp.month }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          &nbsp;&nbsp;&nbsp;&nbsp;
          <span v-if="emp.url == '' || emp.url == null ? true:false">无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer"
             @click="download(emp)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{ emp.url|fileNameFilter }}</a>
          <br/>
        </el-form-item>
        <div>
          <span>历史操作:</span>
          <div
              style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div v-for="item in operList" :key="item.time"
                 style="margin-top:18px;color:gray;font-size:10px;margin-left:14px">
              <div>
                <p>
                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                    item.operatorName
                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                <p v-show="item.remark == '' ? false : true">驳回理由：{{ item.remark }}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
            id="but_reject"
            @click="dialogVisible_showInfo= false"
            type="primary"
        >关闭</el-button>
      </span>
    </el-dialog>

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

    <!-- 添加或修改期刊对话框  -->
    <el-dialog :title="title_publication" :visible.sync="dialogVisible_publication" @close="cannotAddPublish=true"
               width="50%" center>
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
              style="width:80%"
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
              style="width:80%"
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
              style="width:80%"
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
              style="width:80%"
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
              style="width:80%"
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
    </el-dialog>
  </div>

</template>

<script>
// import { delete } from 'vue/types/umd';
import axios from "axios";
import {deleteRequest, postRequest1} from "@/utils/api";
import {debounce} from "@/utils/debounce";

export default {
  name: "SalSearch",
  data() {
    return {
      selectedPublicationScore: '',
      searchPublicationLoading: false, //输入期刊的loading状态
      isAuthorIncludeSelf: false,
      //点击编辑框，期刊名字框赋值会多调用一次请求，用于判断是否是第一次打开操作
      isInitEditDialog: false,
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
      inputDisabled: true,
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
      paperPoint: 0,
      publication_detail: "",
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files: [],//选择上传的文件列表
      filesPublication: [], // 这里上传的是期刊的证明材料
      urlFile: '',//文件路径
      operList: [],//每个论文的历史操作记录
      writer: '',//和输入的作者列表绑定
      data_picker: "",//选择时间
      labelPosition: "left",
      title: "",
      title_show: "",
      title_publication: "",
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_publication: false,
      dialogVisible_show: false,
      dialogVisible_showInfo: false,
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
      currentEmp: {},
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
        author: [{required: true, message: "请输入作者列表", trigger: "blur"}],
        year: [{required: true, message: "请选择发表年月", trigger: "blur"}]
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
  watch: {
    // publicationName: { //
    //   handler(val) {//函数抖动
    //     this.debounceSearch(val);
    //   }
    // },
    data_picker: {//清除时间input设置为不可输入
      handler(val) {
        if (!val) {
          this.disabledInput = true
        }
      }
    }
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
    menuHeight() {
      return this.select_pubName.length * 50 > 150
          ? 150 + 'px'
          : `${this.select_pubName.length * 50}px`
    },
  },
  created() {
    this.debounceSearch = debounce(this.debounceSearchInput,400);
  },
  mounted() {
    this.initTutor(this.user);
    this.initEmps();
  },
  filters: {
    fileNameFilter: function (data) {//将证明材料显示出来
      if (data == null || data == '') {
        return '无证明材料'
      } else {
        var arr = data.split('/')
        return arr.reverse()[0]
      }
    }
  },
  methods: {
    inputRemotePublication(val) { //所属期刊的输入框内容改变
      this.debounceSearch(val);
    },
    openCheckVue() {
      // 使用路由导航进行页面跳转
      this.$router.push({ path: '/student/CheckProgress' });
    },
    // 和tree相关的代码
    showTreeDialog() {
      this.dialogVisible_publication = false;
      this.showTree = true;
      this.initTree()
    },
    // 初始化树
    initTree() {
      var that = this;
      axios.get("/indicator").then(function (resp) {
        //此处可以让父组件向子组件传递url,提高复用性
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
      axios.post("/publicationSubmission/upload", formData, {
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
    },
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
              studentId: this.user.id,
              date: this.dateFormatFunc(new Date()),
              state: "commit",
              publicationProofUrl: this.publish.publicationProofUrl
            };
            const url = `/publicationSubmission/insert/`;
            try {
              const resp = await this.postRequest1(url, this.publishToDatabase);
              if (resp && resp.msg === "200") {
                this.$message.success("成功发送！");
                this.dialogVisible_publication = false
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

    debounceSearchInput(val) { //第一步得到期刊全称 第二步 名称+year 获取积分
      if (!val) {
        return
      }
      if(!this.isInitEditDialog && this.currentEmp.id) return;
      let url = `/publication/basic/listByNameYear/${encodeURIComponent(val)}/${encodeURIComponent(this.currentEmp.year)}`;
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.select_pubName = []
          if (resp.obj) {
            resp.obj.map(val => {
              this.select_pubName.push(val)
            })
          }
        }
      });
    },
    filterPublication(val) {//选择下拉框的某个期刊 得到选择的期刊的id score等信息
      if (!val) {
        return
      }
      this.publicationName = val
      var url = "/publication/getInfByNameYear?year=" + this.currentEmp.year + '&name=' + this.publicationName
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          if (resp.obj) {
            this.selectedPublicationScore = resp.obj.indicatorList[0].score;
            this.judgeWriter();
            let year = resp.obj.indicatorList[0].year;
            this.publication_detail = resp.obj.name + "录入于" + year + "年，按照东华大学毕业要求，属于" + resp.obj.indicatorList[0].order + "类"
            this.publicationId = resp.obj.id;
            this.publication.publicationName = this.publicationName
            this.publication.publicationAbbr = resp.obj.abbr
            this.publication.publisherName = resp.obj.publisher
            this.publication.publicationUrl = resp.obj.url
            this.publication.publicationId = resp.obj.id
            const years = [];
            const indicatorIds = [];
            for (let i = 0; i < resp.obj.indicatorList.length; i++) {
              const year = resp.obj.indicatorList[i].year;
              const indicatorId = resp.obj.indicatorList[i].id;
              years.push(year);
              indicatorIds.push(indicatorId);
            }
            this.publishToDatabase.check_duplicates.indicatorId = indicatorIds;
            this.publishToDatabase.check_duplicates.year = years;
            this.inputDisabled = true
          } else {
            this.$message.warning(this.publicationName + '在' + this.currentEmp.year + '年的积分为0！')
            this.publication_detail = ''
            this.paperPoint = 0
          }
        }
      });
    },
    download(data) {//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      var url = data.url
      axios({
        url: '/paper/basic/downloadByUrl?url=' + url,
        method: 'GET',
        responseType: 'blob'
      }).then(response => {
        const url = window.URL.createObjectURL(new Blob([response]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      });
    },
    handleDelete() {//删除选择的文件
      var file = {
        filepath: this.urlFile
      }
      this.postRequest1("/paper/basic/deleteFile", file).then(
          (res) => {
            this.$message.success('删除成功！')
          }, () => {
            this.$message.success('删除失败！')
          }
      )
    },
    handleExceed() {//超过限制数量
      this.$message.error(`只允许上传1个文件`);
    },
    handleChangeFiles(file, fileList) {//文件列表数量改变
      this.files = []
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
      this.files.push(file);
      formData.append("file", this.files[0].raw)
      axios.post("/paper/basic/upload", formData, {
        headers: {
          'token': this.user ? this.user.token : ''
        }
      }).then(
          (response) => {
            this.$message({
              message: '上传成功！'
            })
            //获取文件路径
            this.urlFile = response.data
          }, () => {
          }
      )
    },
    timechange(picker) {//选择日历调用的方法
      var data = new Date(picker)
      if (data.getFullYear() != this.currentEmp.year) {
        this.publicationName = ""
        this.paperPoint = 0
        this.publication_detail = ""
      }
      this.currentEmp.year = data.getFullYear()
      this.currentEmp.month = data.getMonth() + 1
      this.disabledInput = false
    },
    judgeWriter() {//输入作者框 失去焦点触发事件
      var val = this.currentEmp.author;
      if (val == '' || val == null) { // 作者列表为空
        this.isAuthorIncludeSelf = false;
        return;
      }
      var isalph = false//判断输入中是否有英文字母
      for (var i in val) {
        var asc = val.charCodeAt(i)
        if (asc >= 65 && asc <= 90 || asc >= 97 && asc <= 122) {
          isalph = true
          break
        }
      }
      var num = null
      var info = this.user;
      num = val.split(/[;；]/)
      num = num.map(item => {
        return item && item.replace(/\s*/g,"");
      }).filter(v => {
        return v
      })
      //判断自己在不在其中
      if (num.indexOf(info.name) == -1) {//不在
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        this.isAuthorIncludeSelf = false;
        this.paperPoint = 0;
      } else {//自己在里面 自己是一作不用做任何判断 导师无所谓
        if (num.indexOf(info.name) > 0) {//自己不是一作
          if (num.indexOf(info.teacherName) > 0 || num.indexOf(info.teacherName) == -1) {//导师在作者列表中且老师不是一作, 或者老师不在列表中
            this.$confirm("第一作者不是导师【 " + info.teacherName + " 】！积分将为【0】分", "提示", {
                  confirmButtonText: "确定",
                  type: "warning",
                }).then();
            // this.currentEmp.point = 0;
            this.paperPoint = 0;
          } else if(num.indexOf(info.teacherName) == 0 && num.indexOf(info.name) == 1) { //导师是一作，自己是二作
            this.paperPoint = this.selectedPublicationScore;
          } else {
            this.paperPoint = 0;
          }
        } else if(num.indexOf(info.name) == 0){
          this.paperPoint = this.selectedPublicationScore;
        }
        this.isAuthorIncludeSelf = true;
      }
      this.currentEmp.total = num.length
      if (num.indexOf(info.teacherName) > -1) { //去掉导师的排名
        num.splice(num.indexOf(info.teacherName), 1)
      }
      this.currentEmp.rank = num.indexOf(info.name) + 1
    },
    rowClass() {
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    mail(e) {
      this.postRequest('/test/mail/mail?content=' + e).then((resp) => {
        console.log()
      });
    },
    //编辑按钮
    showEditEmpView(data) {
      console.log(data)
      this.title = "编辑论文信息";
      this.currentEmp = JSON.parse(JSON.stringify(data));
      this.files = [
        {
          name: this.currentEmp.url.split('/').reverse()[0],
          url: this.currentEmp.url
        }
      ];
      this.currentEmp.date = this.currentEmp.year + "-" + this.currentEmp.month;
      this.currentEmp.startPage = this.currentEmp.pubPage.split('-')[0]
      this.currentEmp.endPage = this.currentEmp.pubPage.split('-')[1]
      this.paperPoint = data.point;
      this.disabledInput = false;
      this.urlFile = this.currentEmp.url;
      this.dialogVisible = true;
      this.publicationName = this.currentEmp.pubName
      this.isInitEditDialog = true;
      this.isAuthorIncludeSelf = true;
      this.publicationId = data.publication.id;
    },
    deleteEmpMethod(data) {
      return  new Promise((resolve, reject) => {
          this.deleteRequest("/paper/basic/remove/" + data.id).then((resp) => {
            this.dialogVisible = false;
            resolve('success');
          })
        }
      )
    },
    deleteEmp(data) {//点击删除按钮
      this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?",).then(() => {
        Promise.all([this.deleteEmpMethod(data), this.deleteOperationList(data)]).then(res => {
          this.$message.success('删除成功!');
          this.initEmps();
        }).catch(() => {
          this.$message.error('删除失败!');
        })
      })
    },
    deleteOperationList(data) {
      const params = {}
      params.prodId = data.id;
      params.prodType = '学术论文'
      return new Promise((resolve, reject) => {
        this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
          resolve('success');
        })
      })
    },
    editPaper(params) {
      this.$refs["currentEmp"].validate(async (valid) => {
        if (valid) {
          params.ID = this.currentEmp.id;
          this.postRequest1("/paper/basic/edit", params).then((resp) => {
            if (resp) {
              this.dialogVisible = false;
              this.$message.success('编辑成功');
              this.doAddOper("commit", this.currentEmp.id)
            }
          });
        }
      });
    },
    doAddEmp() {//确定添加论文
      const params = {};
      params.name = this.currentEmp.name;
      params.url = this.urlFile;
      params.rank = this.currentEmp.rank;
      params.total = this.currentEmp.total;
      params.author = this.currentEmp.author;
      params.year = this.currentEmp.year;
      params.month = this.currentEmp.month;
      params.point = this.paperPoint;
      params.state = "commit";
      params.studentID = this.user.id
      params.pubPage = `${this.currentEmp.startPage}-${this.currentEmp.endPage}`;
      if (params.url == '' || params.url == null) {
        this.$message.error('请上传证明材料！')
        return
      }
      if(!this.isAuthorIncludeSelf) {
        this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        return;
      }
      if(this.publicationName == '' || this.publicationName == null) {
        this.$message.error('请输入期刊名称！')
        return
      }
      if(params.publicationID < 0) return;
      params.publicationID = this.publicationId;
      if (this.currentEmp.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editPaper(params);
      } else {
        this.$refs["currentEmp"].validate(async (valid) => {
          if (valid) {
            this.postRequest1("/paper/basic/add", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.doAddOper("commit", resp.data)
                }
              }
            );
          }
        });
      }
    },
    async doAddOper(state, paperID) {
      this.oper.state = state
      this.oper.prodId = paperID
      this.oper.operationName = "提交论文"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper)
      await this.initEmps();
    }
    ,
    showAddEmpView() {//点击添加论文按钮
      this.currentEmp = {};
      this.paperPoint = '';
      this.publication_detail = ""
      this.publicationName = ''
      this.disabledInput = true;
      this.urlFile = ''
      this.files = [];
      this.title = "添加论文";
      this.isAuthorIncludeSelf = false;
      this.dialogVisible = true;
    },
    initEmps() {
      this.loading = true;
      let url = "/paper/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
        }
      });
    },
//查看详情
    showInfo(data) {
      this.title_show = "显示详情";
      this.emp = data
      this.dialogVisible_showInfo = true
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=学术论文').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
          this.operList.sort(function (a, b) {
            return a.time > b.time ? -1 : 1
          })
        }
      });
    }
    ,
    checkScoreComent(row) {
      if (row.state === "adm_pass" && row.point === 2 && row.have_score === 0) {
        return (row.remark === null ? "" : row.remark + ";") + "本类别论文只计算一篇，本论文积分不计入总分"
      }
      return row.remark
    }
    ,
  },
}
;
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
