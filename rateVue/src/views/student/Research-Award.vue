<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加奖励
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="awardsList"
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
            label="奖励名称"
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
            prop="awardType.name"
            label="奖励类别"
            align="center"
            min-width="10%"
        >
        </el-table-column>
        <el-table-column
            prop="awardLevel"
            label="奖励级别"
            align="center"
            min-width="10%"
        >
        </el-table-column>
        <el-table-column
            prop="author"
            align="center"
            label="完成人"
            min-width="15%"
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
            width="140px"
            align="center"
            label="备注"
            min-width="20%"
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
                @click="deleteEmp(scope.row)"
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
    <!-- 添加科研获奖对话框 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :model="currentAwardCopy"
          :rules="rules"
          ref="currentAwardCopy"
      >
        <el-form-item label="奖励名称:" label-width="80px" style="margin-left: 20px;" prop="name">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentAwardCopy.name"
              placeholder="请输入奖励名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="获奖年月:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-date-picker
              style="width: 80%"
              v-model="currentAwardCopy.date"
              @change="changeAwardDate($event)"
              type="month"
              value-format="yyyy-MM"
              placeholder="选择年月">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="指标点:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-button size="mini" type="text" @click="initTree()">{{indicatorBtn}}</el-button>
        </el-form-item>
        <el-form-item label="奖励级别:" label-width="80px" style="margin-left: 20px;" prop="awardLevel">
          <span class="isMust">*</span>
          <el-select size="mini" v-model="currentAwardCopy.awardLevel" placeholder="请选择奖励级别" style="width: 80%" @change="selectedAwardLevel = false">
            <el-option v-for="item in awardLevelList" :key="item.value" :value="item.label" :label="item.label"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="奖励类别:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-select
              :disabled="disabledSelectAwardType || selectedAwardLevel"
              v-model="selectAwardType"
              value-key="id"
              filterable
              remote
              clearable
              reserve-keyword
              placeholder="请选择科研获奖类别"
              loading-text="搜索中..."
              @focus="selectAwardTypeMethod($event)"
              :loading="searchTypeLoading">
            <el-option
                v-for="item in selectAwardTypeList"
                :key="item.id"
                :label="item.name"
                :value="item">
            </el-option>
          </el-select>
          <el-tooltip class="item" effect="dark" content="如：国家科技进步奖、国家技术发明奖、国家自然科学奖等" placement="top-start">
            <i class="el-icon-question" style="margin-left: 10px;font-size: 16px"></i>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="获奖人:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              id="input_member"
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="currentAwardCopy.author"
              @blur="judgeMember()"
              placeholder="请输入获奖人,如有多个用分号分隔"
          ></el-input>
        </el-form-item>

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
        <span style="color:gray;font-size:10px">将会获得：{{ awardPoint }}积分</span>
        <span style="color:gray;font-size:10px;margin-left: 8px">{{ zeroPointReason }}</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAdd">取 消</el-button>
        <el-button type="primary" @click="addAward">提 交</el-button>
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
          label-width="80px"
          :model="currentAward"
          style="margin-left: 20px">
        <el-form-item label="奖励名称:">
          <span>{{ currentAward.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="获奖人:">
          <span>{{ currentAward.author }}</span
          >
        </el-form-item>
        <el-form-item label="奖励类别:">
          <span>{{ currentAward.awardType.name }}</span
          >
        </el-form-item>
        <el-form-item label="奖励级别:">
          <span>{{ currentAward.awardLevel }}</span
          >
        </el-form-item>
        <el-form-item label="奖励积分:">
          <span>{{ currentAward.point }}</span
          >
        </el-form-item>
        <el-form-item label="作者人数:">
          <span>{{ currentAward.total }}</span
          >
        </el-form-item>
        <el-form-item label="作者排名:">
          <span>{{ currentAward.rank }}</span
          >
        </el-form-item>
        <el-form-item label="获奖年月:">
          <span>{{ currentAward.date }}</span
          >
        </el-form-item>
        <el-form-item label="成果状态:">
          <span>{{currentAward.state=="commit"
              ? "已提交"
              :currentAward.state=="tea_pass"
                  ? "导师通过"
                  :currentAward.state=="tea_reject"
                      ? "导师驳回"
                      :currentAward.state=="adm_pass"
                          ? "管理员通过"
                          :"管理员驳回"}}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentAward.url == '' || currentAward.url == null ? true:false" >无证明材料</span>
          <div v-else>{{ currentAward.url | fileNameFilter }}</div>
        </el-form-item>
        <div v-show="currentAward.url == '' || currentAward.url == null ? false : true" style="margin-left: 80px">
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
        <div >
          <span>历史操作:</span>
          <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
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
    <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
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
export default {
  name: "SalSearch",
  data() {
    return {
      isImage: false,
      isPdf: false,
      dialogPreviewPdfFile: false,
      previewImageSrcList: [],
      previewUrl: '',
      selectedAwardLevel: true,
      defaultExpandedKeys: [],
      zeroPointReason: '',
      indicatorBtn: '选择指标点',
      defaultProps: {
        children: "children",
        label: "label",
      },
      showTreeDialog: false,
      indicatorData: [],
      searchTypeLoading: false,
      isAuthorIncludeSelf: false,
      awardLimitRankN: '',
      selectedIndicator: {},
      selectAwardType: {},
      selectAwardTypeList: [],
      disabledSelectAwardType: true,
      awardPoint:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      operList:[],//每个项目的历史操作记录
      labelPosition: "left",
      title: "",
      title_show: "",
      awardsList: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      total: 0,
      size: 10,
      positions: [],
      awardTypename:"",//奖励类别
      awardLevelList: [
        {
          label:'国家级',
          value: 1
        },
        {
          label:'省部级',
          value: 2
        }
      ],
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '科研获奖',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      currentAwardCopy: {},
      currentAward: {
        id: null,
        name: null,
        author:"",
        state: '',
        date: "",
        rank: "",
        total:"",
        point:"",
        url:'',
        remark:'',
        awardLevel:'',
        awardTypeId:'',
        awardType: {}
      },
      rules: {
        name: [{ required: true, message: "请输入奖励名称", trigger: "blur" }],
        awardClass: [{ required: true, message: "请输入奖励类别", trigger: "blur" }],
        awardLevel: [{ required: true, message: "请输入奖励级别", trigger: "blur" }]
      },
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
    menuHeight() {
      return this.awardTypename.length * 50 > 150
          ? 150 + 'px'
          : '${this.awardTypename.length * 50}px'
    },
  },
  created() {},
  mounted() {
    this.currentAwardCopy = JSON.parse(JSON.stringify(this.currentAward));
    this.initAwardsList();
  },
  watch: {
    selectedIndicator: {
      deep: true,
      handler: function (newV, oldV) {
        this.judgeMember();
      }
    }
  },
  methods: {
    previewMethod(type) {
      if(type == '1') {
        this.previewFileMethod(this.currentAward).then(res => {
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
        this.downloadFileMethod(this.currentAward);
      }
    },

    //不进行rankN判断
    handleNodeClick(data, node) {
      if (data.children.length == 0) {
        this.indicatorBtn = data.label;
        this.selectedIndicator = data;
        this.currentAwardCopy.indicatorId = data.id;
        this.awardLimitRankN = data.rankN;
        if (!this.isAuthorIncludeSelf) {
          this.awardPoint = 0;
          this.zeroPointReason = '参与人未包含自己'
        }
        else {
          this.awardPoint = data.score;
          this.zeroPointReason = ''
        }
        this.showTreeDialog = false;
      }
    },
    //初始化指标点树
    initTree() {
      this.getRequest("/indicator/getAllByType?type=科研获奖").then( resp => {
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
    changeAwardDate(data) {
      if(data) {
        this.disabledSelectAwardType = false;
      }else {
        this.disabledSelectAwardType = true;
      }
    },
    debounceSearchType() {
      if(this.currentAwardCopy.date == null || this.currentAwardCopy.date == '') return;
      if(this.currentAwardCopy.awardLevel == null || this.currentAwardCopy.awardLevel == '') return;
      this.searchTypeLoading = true;
      this.getRequest('/award/basic/getIndicatorByYearAndType?year=' + this.currentAwardCopy.date.split('-')[0] + '&type=' + this.currentAwardCopy.awardLevel).then(response => {
        if(response) {
          this.searchTypeLoading = false;
          this.selectAwardTypeList = response.data;
        }
      })
    },
    selectAwardTypeMethod() {
      this.debounceSearchType();
    },
    cancelAdd() {
      this.dialogVisible = false;
    },
    handleDelete() {//删除选择的文件
      var file={
        filepath:this.urlFile
      }
      this.postRequest1("/award/basic/deleteFile",file).then(
        ()=>{
          this.files = [];
          this.urlFile = '';
        }
      )
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
      }).then(
          (response)=>{
            this.$message({
              message:'上传成功！'
            })
            //获取文件路径
            this.urlFile = response.data
          },()=>{}
      )
    },
    judgeMember(){//输入作者框 失去焦点触发事件
      var author = this.currentAwardCopy.author;
      if(!author || author === '') {
        this.isAuthorIncludeSelf = false;
        return;
      }
      var isalph = false//判断输入中是否有英文字母
      for(var i in author){
        var asc = author.charCodeAt(i)
        if(asc >= 65 && asc <= 90 || asc >= 97 && asc <= 122){
          isalph=true
          break
        }
      }
      var memberList = null
      var info = this.user;
      memberList = author.split(/[;；]/)
      memberList = memberList.map(item => {
        return item && item.replace(/\s*/g,"");
      }).filter(v => {
        return v
      })
      //判断自己在不在其中
      if(memberList.indexOf(info.name) == -1){//不在 并且没有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        this.isAuthorIncludeSelf = false;
        this.zeroPointReason = '参与人未包含自己'
        this.awardPoint = 0;
      }else {
        //作者列表的rank大于规定的rankN，积分为0
        this.judgeRankScore(memberList.indexOf(info.name) + 1);
        this.isAuthorIncludeSelf = true;
      }
      this.currentAwardCopy.total = memberList.length
      this.currentAwardCopy.rank = memberList.indexOf(info.name) + 1
    },
    judgeRankScore(rank) {
      if(JSON.stringify(this.selectedIndicator) === '{}') {
        this.awardPoint = 0; //输入作者，但未选择指标点
        this.zeroPointReason = '请选择指标点'
      }
      else { //指标点已选择，再次修改作者列表
        const indicatorRankN = this.selectedIndicator.rankN;
        if(rank > indicatorRankN && indicatorRankN > 0) {
          this.awardPoint = 0;
          this.zeroPointReason = `获奖排名需在前${this.selectedIndicator.rankN}名以内`
        }
        else {
          this.awardPoint = this.selectedIndicator.score;
          this.zeroPointReason = ''
        }
      }
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    //编辑按钮
    showEditEmpView(data, idx) {
      this.selectedIndicator = data.indicator;
      this.indicatorBtn = this.selectedIndicator.name;
      this.title = "编辑奖励信息";
      this.currentAwardCopy = JSON.parse(JSON.stringify(data));
      this.isAuthorIncludeSelf = true;
      this.disabledSelectAwardType = false;
      this.selectedAwardLevel = false;
      this.awardPoint = data.point;
      this.zeroPointReason = '';
      const { id, name } = data.awardType;
      this.selectAwardType = name;
      this.dialogVisible = true;
      this.awardLimitRankN = data.indicator.rankN;
      this.files = [
        {
          name: this.currentAwardCopy.url.split('/').reverse()[0],
          url: this.currentAwardCopy.url
        }
      ]
      this.urlFile = this.currentAwardCopy.url;
    },
    showInfo(data){
      this.loading = true;
      this.title_show = "显示详情";
      this.currentAward = data
      this.dialogVisible_showInfo = true
      this.isPdf = this.isImage = false; //初始化
      this.previewUrl = '';
      this.previewImageSrcList = [];
      if(data.url.includes('.pdf')) { //判断文件类型
        this.isPdf = true;
      } else if(data.url.includes('.jpg') || data.url.includes('.png') || data.url.includes('.jpe') || data.url.includes('.JPG') || data.url.includes('.PNG') || data.url.includes('.JPE')) {
        this.isImage = true;
      }
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=科研获奖').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteEmpMethod(data) {
      return new Promise((resolve, reject) => {
            this.deleteRequest("/award/basic/remove/" + data.id).then((resp) => {
              this.dialogVisible = false;
              resolve('success');
            })
          }
      )
    },
    deleteEmp(data) {
      this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?",).then(() => {
        Promise.all([this.deleteEmpMethod(data), this.deleteOperationList(data)]).then(res => {
          this.$message.success('删除成功!');
          this.initAwardsList();
        }).catch(() => {
          this.$message.error('删除失败!');
        })
      })
    },
    deleteOperationList(data) {
      const params = {}
      params.prodId = data.id;
      params.prodType = '科研获奖'
      return new Promise((resolve, reject) => {
        this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
          resolve('success');
        })
      })
    },
    editAward(params) {
      this.$refs["currentAwardCopy"].validate((valid) => {
        if (valid) {
          params.id = this.currentAwardCopy.id;
          params.studentId = this.user.id
          params.awardTypeId = this.currentAwardCopy.awardType.id;
          this.postRequest1("/award/basic/edit", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.$message.success('编辑成功！')
                  this.doAddOper("commit", this.currentAwardCopy.id);
                }
              }
          );
        }
      });
    },
    addAward() {//项目提交确认
      const params = {};
      params.name = this.currentAwardCopy.name;
      params.url = this.urlFile;
      params.rank = this.currentAwardCopy.rank;
      params.total = this.currentAwardCopy.total;
      params.author = this.currentAwardCopy.author;
      params.date = this.currentAwardCopy.date;
      params.point = this.awardPoint;
      params.awardLevel = this.currentAwardCopy.awardLevel;
      params.state = "commit";
      params.indicatorId = this.selectedIndicator.id;
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
      if (this.currentAwardCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editAward(params);
      } else {
        this.$refs["currentAwardCopy"].validate((valid) => {
          if (valid) {
            params.studentId = this.user.id;
            params.awardTypeId = this.selectAwardType.id;
            this.postRequest1("/award/basic/add", params).then(
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
    async doAddOper(state,awardID) {
      this.oper.state = state
      this.oper.prodId = awardID
      this.oper.operationName = "提交奖励"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper);
      await this.initAwardsList();
    },
    showAddEmpView() {//点击添加科研获奖按钮
      this.urlFile = ''
      this.files = [];
      this.indicatorBtn = '选择指标点';
      this.selectedIndicator = {};
      this.currentAwardCopy = {};
      this.selectAwardType = {};
      this.title = "添加科研获奖";
      this.dialogVisible = true;
      this.awardLimitRankN = '';
      this.awardPoint = '';
      this.zeroPointReason = '';
      this.isAuthorIncludeSelf = false;
      this.disabledSelectAwardType = true;
      this.selectedAwardLevel = true;
      this.selectAwardTypeList = [];
    },
    initAwardsList() {
      this.loading = true;
      let url = "/award/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.awardsList = resp.data;
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
