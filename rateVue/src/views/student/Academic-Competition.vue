<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="addCompetitionDialog">
            添加学科竞赛
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="competitionsList"
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
            label="学科竞赛名称"
            min-width="15%"
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
                :style="(scope.row.state=='tea_reject' || scope.row.state=='adm_reject') ? {'color':'red'}:{'color':'gray'}"
                size="mini"
            >
              {{scope.row.state=="commit"
                ? "已提交"
                :scope.row.state=="tea_pass"
                    ? "导师通过"
                    :scope.row.state=="tea_reject"
                        ? "导师驳回"
                        :scope.row.state=="adm_pass"
                            ? "管理员通过"
                            :"管理员驳回"}}
              </span>
          </template>
        </el-table-column>
        <el-table-column
            prop="author"
            align="center"
            label="获奖人"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="date"
            min-width="10%"
            align="center"
            label="获奖年月"
        >
        </el-table-column>
        <el-table-column
            prop="point"
            label="积分"
            align="center"
            min-width="8%"
        >
        </el-table-column>
        <el-table-column
            prop="operationList[0].remark"
            min-width="20%"
            align="center"
            label="备注"
        >
        </el-table-column>
        <el-table-column align="center" width="280px" label="操 作" min-width="20%">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row, scope.$index)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
                v-show="scope.row.state == 'commit' || scope.row.state == 'tea_reject' || scope.row.state == 'adm_reject'? true : false"
            >编辑</el-button
            >
            <el-button
                @click="showInfo(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情</el-button
            >
            <el-button
                @click="deleteCompetition(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
                v-show="scope.row.state == 'tea_reject' || scope.row.state == 'commit'? true:false"
            >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 添加科研学科竞赛对话框 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :model="currentCompetitionCopy"
          :rules="rules"
          ref="currentCompetitionCopy"
      >
        <el-form-item label="竞赛名称:" label-width="80px" style="margin-left: 20px;" prop="name">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentCompetitionCopy.name"
              placeholder="请输入学科竞赛名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="获奖年月:" label-width="80px" style="margin-left: 20px;" prop="date">
          <span class="isMust">*</span>
          <el-date-picker
              style="width: 80%"
              v-model="currentCompetitionCopy.date"
              type="month"
              @change="changeCompetitionStartDate($event)"
              value-format="yyyy-MM"
              placeholder="选择获奖年月">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="指标点:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-button size="mini" type="text" @click="initTree()">{{indicatorBtn}}</el-button>
        </el-form-item>
        <el-form-item label="竞赛类别:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-select
              :disabled="disabledSelectCompetitionType"
              v-model="selectCompetitionType"
              value-key="id"
              filterable
              remote
              clearable
              reserve-keyword
              placeholder="请输入学科竞赛类别"
              loading-text="搜索中..."
              @focus="selectCompetitionTypeMethod"
              :loading="searchTypeLoading">
            <el-option
                v-for="item in selectCompetitionTypeList"
                :key="item.id"
                :label="item.name"
                :value="item">
            </el-option>
          </el-select>
          <el-tooltip class="item" effect="dark" content="如：挑战杯、互联网+等" placement="top-start">
            <i class="el-icon-question" style="margin-left: 10px;font-size: 16px"></i>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="获奖人:" label-width="80px" style="margin-left: 20px;" prop="author">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="currentCompetitionCopy.author"
              @blur="judgeMember()"
              placeholder="请输入获奖人,如有多个用分号分隔"
          ></el-input>
        </el-form-item>
<!--        <el-form-item label="竞赛级别:" label-width="80px" style="margin-left: 20px;" prop="competitionLevel">-->
<!--          <span class="isMust">*</span>-->
<!--          <el-select v-model="currentCompetitionCopy.competitionLevel">-->
<!--            <el-option v-for="item in competitionLevelList" :key="item" :value="item" :label="item"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item label="证明材料:" prop="url" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
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
            >选择文件</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
            <span style="color:gray;font-size:11px">只允许doc docx pdf jpg png jpe rar zip类型文件
                  &nbsp;&nbsp;大小不能超过10MB
                </span>
          </el-upload>
        </el-form-item>
      </el-form>
      <div style="margin-left: 20px;">
        <span style="color:gray;font-size:10px">将会获得：{{ competitionPoint }}积分</span>
        <span style="color:gray;font-size:10px;margin-left: 8px">{{ zeroPointReason }}</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAdd">取 消</el-button>
        <el-button type="primary" @click="addCompetition">提 交</el-button>
      </span>
    </el-dialog>
    <el-dialog title="" center :visible.sync="showTreeDialog" width="60%">
      <div slot="title">
        <div>选择指标点分类</div>
        <div style="font-size: 14px;margin-top: 10px">以下仅显示本类型的指标点</div>
      </div>
      <span class="el-tree-node">
        <el-tree
            :data="indicatorData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :expand-on-click-node="false"
            :highlight-current="true"
            node-key="id"
            :default-expanded-keys="defaultExpandedKeys"
        ></el-tree>
      </span>
    </el-dialog>

    <!--查看详情-->
    <el-dialog
        class="showInfo_dialog"
        :title="title_show"
        :visible.sync="dialogVisible_showInfo"
        width="520px"
        center
    >
      <el-form
          :label-position="labelPosition"
          label-width="120px"
          :model="currentCompetition"
          style="margin-left: 20px">
        <el-form-item label="竞赛名称:">
          <span>{{ currentCompetition.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="获奖人:">
          <span>{{ currentCompetition.author }}</span
          >
        </el-form-item>
        <el-form-item label="获奖类别:">
          <span>{{ currentCompetition.competitionType.name }}</span
          >
        </el-form-item>
<!--        <el-form-item label="获奖级别:">-->
<!--          <span>{{ currentCompetition.competitionLevel }}</span-->
<!--          >-->
<!--        </el-form-item>-->
        <el-form-item label="获奖年月:">
          <span>{{ currentCompetition.date }}</span
          >
        </el-form-item>
        <el-form-item label="获奖人数:">
          <span>{{ currentCompetition.total }}</span
          >
        </el-form-item>
        <el-form-item label="获奖排名:">
          <span>{{ currentCompetition.rank }}</span
          >
        </el-form-item>
        <el-form-item label="成果状态:">
          <span>{{currentCompetition.state=="commit"
              ? "已提交"
              :currentCompetition.state=="tea_pass"
                  ? "导师通过"
                  :currentCompetition.state=="tea_reject"
                      ? "导师驳回"
                      :currentCompetition.state=="adm_pass"
                          ? "管理员通过"
                          :"管理员驳回"}}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentCompetition.url == '' || currentCompetition.url == null ? true:false" >无证明材料</span>
          <div v-else>{{ currentCompetition.url | fileNameFilter }}</div>
          <br />
          <div v-show="currentCompetition.url == '' || currentCompetition.url == null ? false : true">
            <div>
              <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
              <el-button @click="previewMethod('2')">下载</el-button>
            </div>
            <div style="margin-top: 5px">
              <el-image
                  v-show="false"
                  ref="previewImage"
                  style="width: 100px; height: 100px"
                  :src="previewUrl"
                  :preview-src-list="previewImageSrcList">
              </el-image>
            </div>
          </div>
          <br />
        </el-form-item>
        <el-form-item label="相关备注:">
          <span>{{ currentCompetition.remark }}</span>
        </el-form-item>
        <div >
          <span>历史操作:</span>
          <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;font-size:5px;margin-left:5px">
              <div >
                <p>{{item.time | dataFormat}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operatorName}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operationName}}</p>
                <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{item.remark}}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible_showInfo = false"
        >关 闭</el-button
        >
      </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%">
      <template v-if="isPdf">
        <vue-office-pdf
            :src="previewUrl"
            style="height: 100vh;"
        />
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import {postRequest1} from "@/utils/api";
import {debounce} from "@/utils/debounce";
import { throttle } from "@/utils/throttle";
export default {
  name: "Academic-Competition",
  data() {
    return {
      isImage: false,
      isPdf: false,
      dialogPreviewPdfFile: false,
      previewImageSrcList: [],
      previewUrl: '',
      defaultExpandedKeys: [],
      zeroPointReason: '',
      indicatorBtn: '选择指标点',
      defaultProps: {
        children: "children",
        label: "label",
      },
      showTreeDialog: false,
      indicatorData: [],
      competitionLimitRankN: '',
      selectedIndicator: {},
      searchTypeLoading: false,
      selectCompetitionType: '',
      selectCompetitionTypeName: '',
      isAuthorIncludeSelf: true,
      //先选择时间才可以输入竞赛类别
      disabledSelectCompetitionType: true,
      //竞赛类别下拉框可选列表
      selectCompetitionTypeList: [],
      competitionPoint:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      competitionLevelList: ['全国一等奖', '全国二等奖', '全国三等奖', '省部级一等奖', '省部级二等奖'],
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      operList:[],//每个学科竞赛的历史操作记录
      labelPosition: "left",
      title: "",
      title_show: "",
      competitionsList: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '学科竞赛',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      currentCompetitionCopy: {},
      currentCompetition: {
        id: null,
        name: null,
        author:"",
        state: '',
        date: "",
        competitionLevel: '',
        rank: "",
        total:"",
        point:"",
        url:'',
        remark:'',
        competitionTypeId: '',
        competitionType: {}
      },
      rules: {
        name: [{ required: true, message: "请输入学科竞赛名称", trigger: "blur" }],
        date: [{ required: true, message: "请输入学科竞赛获奖年月", trigger: "blur" }],
        author: [{ required: true, message: "请输入学科竞赛获奖人", trigger: "blur" }],
        competitionLevel: [{ required: true, message: "请选择学科竞赛级别", trigger: "blur" }]
      },
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  watch: {
    selectedIndicator: {
      deep: true,
      handler: function (newV, oldV) {
        this.judgeMember();
      }
    }
  },
  created() {
    this.throttleSearch = throttle(this.throttleSearchType,400); //没有用到
  },
  mounted() {
    this.currentCompetitionCopy = JSON.parse(JSON.stringify(this.currentCompetition));
    this.initCompetitionsList();
  },
  filters:{
    fileNameFilter:function(data){//将证明材料显示出来
      if(data == null || data == ''){
        return '无证明材料'
      }else{
        var arr = data.split('/');
        return arr.reverse()[0];
      }
    }
  },
  methods: {
    previewMethod(type) {
      if(type == '1') {
        this.previewFileMethod(this.currentCompetition).then(res => {
          this.previewUrl = res;
          if(this.isImage) {
            this.previewImageSrcList = [res];
            this.$refs.previewImage.showViewer = true;
          }
          if(this.isPdf) {
            this.dialogPreviewPdfFile = true;
          }
        });
      } else {
        this.downloadFileMethod(this.currentCompetition);
      }
    },

    //不进行rankN判断
    handleNodeClick(data, node) {
      if (data.children.length == 0) {
        this.indicatorBtn = data.label;
        this.selectedIndicator = data;
        this.currentCompetitionCopy.indicatorId = data.id;
        if (!this.isAuthorIncludeSelf) {
          this.competitionPoint = 0;
          this.zeroPointReason = '参与人未包含自己'
        }
        else this.competitionPoint = data.score;
        this.showTreeDialog = false;
      }
    },
    //初始化指标点树
    initTree() {
      this.getRequest("/indicator/getAllByType?type=学科竞赛").then( resp => {
        this.showTreeDialog = true;
        this.defaultExpandedKeys = [];
        if (resp) {
          this.indicatorData = resp.obj[1];
          if(this.indicatorData.length > 0)
            if(this.indicatorData[0].children.length > 0) {
              this.defaultExpandedKeys.push(this.indicatorData[0].children[0].id);
            } else this.defaultExpandedKeys.push(this.indicatorData[0].id);
        }
      });
    },
    //改变竞赛的时间
    changeCompetitionStartDate(data) {
      if(data) {
        this.disabledSelectCompetitionType = false;
      }else {
        this.disabledSelectCompetitionType = true;
      }
    },
    throttleSearchType() {
      if(this.currentCompetitionCopy.date == null || this.currentCompetitionCopy.date == '') return;
      this.getRequest('/competition/basic/getIndicatorByYearAndType?year=' + this.currentCompetitionCopy.date.split('-')[0]).then(response => {
        if(response) {
          this.searchTypeLoading = false;
          this.selectCompetitionTypeList = response.data;
        }
      })
    },
    //输入竞赛类别 发送请求调用的函数
    selectCompetitionTypeMethod() {
      this.searchTypeLoading = true;
      this.throttleSearchType();
    },
    cancelAdd() {
      this.dialogVisible = false;
    },
    handleDelete() {//删除选择的文件
      var file={filepath:this.urlFile}
      this.postRequest1("/competition/basic/deleteFile",file).then((response)=>{
        this.urlFile = '';
        this.files = [];
      });
    },
    handleExceed(){//超过限制数量
      this.$message.error(`只允许上传1个文件`);
    },
    handleChangeFiles(file,fileList){//文件列表数量改变
      this.files=[]
      var attachmentType = [
        "doc","docx","pdf","jpg","png","jpeg","rar","zip"]
      var type=file.name.split('.')
      if(file.size > 10*1024*1024){
        this.$message.error('上传文件大小不能超过10MB!');
        return
      }
      if(attachmentType.indexOf(type[type.length-1].toLowerCase()) === -1) {
        this.$message.error("不支持上传该类型的附件")
        return
      }
      var formData=new FormData();
      this.files.push(file);
      formData.append("file",this.files[0].raw)
      axios.post("/achievements/basic/upload",formData,{
        headers:{
          'token': localStorage.getItem('user') ? this.user.token : ''
        }
      }).then((response)=> {
            this.$message({
              message: '上传成功！'
            })
            //获取文件路径
            this.urlFile = response.data
          }
      )
    },
    judgeMember(){//输入作者框 失去焦点触发事件
      var author = this.currentCompetitionCopy.author;
      if(!author || author === '') {
        this.isAuthorIncludeSelf = false;
        return;
      }
      var isalph = false//判断输入中是否有英文字母
      for(var i in author){
        var asc = author.charCodeAt(i)
        if(asc >= 65 && asc <= 90 || asc >= 97 && asc <= 122){
          isalph = true
          break
        }
      }
      var num = null
      var info = this.user;
      num = author.split(/[;；]/)
      num = num.map(item => {
        return item && item.replace(/\s*/g,"");
      }).filter(v => {
        return v
      })
      //不止一个作者 判断自己在不在其中
      if(num.indexOf(info.name) == -1){//不在 并且没有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        this.isAuthorIncludeSelf = false;
        this.zeroPointReason = '参与人未包含自己'
        this.competitionPoint = 0;
      }else { //自己在里面
        this.judgeRankScore(num.indexOf(info.name) + 1);
        this.isAuthorIncludeSelf = true;
      }
      this.currentCompetitionCopy.total = num.length;
      this.currentCompetitionCopy.rank = num.indexOf(info.name) + 1;
    },
    judgeRankScore(rank) {
      if(JSON.stringify(this.selectedIndicator) === '{}') {
        this.competitionPoint = 0; //输入作者，但未选择指标点
        this.zeroPointReason = '请选择指标点'
      }
      else { //指标点已选择，再次修改作者列表
        const indicatorRankN = this.selectedIndicator.rankN;
        if(rank > indicatorRankN && indicatorRankN > 0) {
          this.competitionPoint = 0;
          this.zeroPointReason = `获奖排名需在前${this.selectedIndicator.rankN}名以内`
        }
        else {
          this.competitionPoint = this.selectedIndicator.score;
          this.zeroPointReason = ''
        }
      }
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    //编辑按钮
    showEditEmpView(data, idx) {
      this.competitionLimitRankN = data.indicator.rankN;
      this.selectedIndicator = data.indicator;
      this.indicatorBtn = this.selectedIndicator.name;
      this.title = "编辑学科竞赛信息";
      this.currentCompetitionCopy = JSON.parse(JSON.stringify(data));
      this.dialogVisible = true;
      this.disabledSelectCompetitionType = false;
      this.files = [
        {
          name: this.currentCompetitionCopy.url.split('/').reverse()[0],
          url: this.currentCompetitionCopy.url
        }
      ];
      this.urlFile = this.currentCompetitionCopy.url;
      this.selectCompetitionType = data.competitionType.name;
      this.competitionPoint = data.point;
      this.zeroPointReason = '';
      this.isAuthorIncludeSelf = true;
    },
    showInfo(data){
      this.loading = true;
      this.title_show = "显示详情";
      this.currentCompetition = data
      this.dialogVisible_showInfo = true
      this.isPdf = this.isImage = false; //初始化
      this.previewUrl = '';
      this.previewImageSrcList = [];
      if(data.url.includes('.pdf')) { //判断文件类型
        this.isPdf = true;
      } else if(data.url.includes('.jpg') || data.url.includes('.png') || data.url.includes('.jpe')) {
        this.isImage = true;
      }
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=学科竞赛').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteEmpMethod(data) {
      return new Promise((resolve, reject) => {
            this.deleteRequest("/competition/basic/remove/" + data.id).then((resp) => {
              this.dialogVisible = false;
              resolve('success');
            })
          }
      )
    },
    deleteCompetition(data) {
      this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
        Promise.all([this.deleteEmpMethod(data), this.deleteOperationList(data)]).then(res => {
          this.$message.success('删除成功!');
          this.initCompetitionsList();
        }).catch(() => {
          this.$message.error('删除失败!');
        })
      })
    },
    deleteOperationList(data) {
      const params = {}
      params.prodId = data.id;
      params.prodType = '学科竞赛'
      return new Promise((resolve, reject) => {
        this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
          resolve('success');
        })
      })
    },
    editCompetition(params) {
      params.studentId = this.user.id;
      this.$refs["currentCompetitionCopy"].validate((valid) => {
        if (valid) {
          params.id = this.currentCompetitionCopy.id;
          this.postRequest1("/competition/basic/edit", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.$message.success('编辑成功！')
                  this.doAddOper("commit", this.currentCompetitionCopy.id);
                }
              }
          );
        }
      });
    },
    addCompetition() {//学科竞赛提交确认
      const params = {};
      params.name = this.currentCompetitionCopy.name;
      params.url = this.urlFile;
      params.rank = this.currentCompetitionCopy.rank;
      params.total = this.currentCompetitionCopy.total;
      params.author = this.currentCompetitionCopy.author;
      params.date = this.currentCompetitionCopy.date;
      params.point = this.competitionPoint;
      params.competitionTypeId = this.selectCompetitionType.id;
      params.competitionLevel = '';
      params.indicatorId = this.selectedIndicator.id;
      params.state = "commit";
      params.studentId = this.user.id;
      if(JSON.stringify(this.selectedIndicator) === '{}') {
        this.$message.error('请选择指标点！');
        return;
      }
      if(JSON.stringify(this.selectCompetitionType) == '{}' || this.selectCompetitionType == '') {
        this.$message.error('请选择竞赛类别！')
        return;
      }
      if(params.url == '' || params.url == null){
        this.$message.error('请上传证明材料！')
        return
      }
      if(params.url.indexOf("\\") >= 0) {
        params.url = params.url.replaceAll("\\", "/")
      }
      if(!this.isAuthorIncludeSelf) {
        this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        return;
      }

      if (this.currentCompetitionCopy.id) {
        this.editCompetition(params);
      } else {
        this.$refs["currentCompetitionCopy"].validate((valid) => {
          if (valid) {
            params.studentId = this.user.id;
            this.postRequest1("/competition/basic/add", params).then(
                (resp) => {
                  if (resp) {
                    this.$message.success('添加成功！')
                    this.dialogVisible = false;
                    this.doAddOper("commit", resp.data);
                  }
                }
            );
          }
        });
      }
    },
    async doAddOper(state,competitionID) {
      this.oper.state = state
      this.oper.prodId = competitionID
      this.oper.operationName = "提交竞赛"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper);
      await this.initCompetitionsList();
    },
    addCompetitionDialog() {//点击添加科研学科竞赛按钮
      this.urlFile = '';
      this.files = [];
      this.selectedIndicator = {};
      this.currentCompetitionCopy = {};
      this.competitionPoint = '';
      this.zeroPointReason = '';
      this.title = "添加竞赛";
      this.indicatorBtn = '选择指标点'
      this.competitionLimitRankN = '';
      this.selectCompetitionType = '';
      this.isAuthorIncludeSelf = false;
      this.disabledSelectCompetitionType = true;
      this.dialogVisible = true;
      this.selectCompetitionTypeList = [];
    },
    initCompetitionsList() {
      this.loading = true;
      let url = "/competition/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.competitionsList = resp.data;
        }
      });
    },
  },
};
</script>

<style>
.select_div_input{
  margin-left:3px;
  width:120px;
  height:32px;
  position:relative;
  display:inline-block
}
.select_div{
  border-radius: 3px;
  margin-top: 5px;
  font-size: 14px;
  position: absolute;
  background-color: #fff;
  z-index: 999;
  overflow: auto;
  width: 300px;
  cursor: pointer;
}
.showInfo_dialog .el-form-item{
  margin-bottom: 5px;
}
.isMust{
  position: absolute;
  color: #F56C6C;
  top: 2px;
  left: -100px;
}
.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}
</style>
