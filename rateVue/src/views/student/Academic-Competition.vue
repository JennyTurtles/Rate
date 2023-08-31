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
            width="200px"
        >
        </el-table-column>
        <el-table-column
            prop="state"
            label="状态"
            width="100px"
            align="center"
        >
          <template slot-scope="scope">
            <span
                style="padding: 4px"
                :style="scope.row.state=='tea_reject' ? {'color':'red'}:{'color':'gray'}"
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
            width="160px"
        >
        </el-table-column>
        <el-table-column
            prop="date"
            width="140px"
            align="center"
            label="获奖年月"
        >
        </el-table-column>
        <el-table-column
            prop="point"
            label="积分"
            align="center"
            width="75px"
        >
        </el-table-column>
        <el-table-column
            prop="operationList[0].remark"
            width="220px"
            align="center"
            label="备注"
        >
        </el-table-column>
        <el-table-column align="center" width="280px" label="操 作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row, scope.$index)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
                v-show="scope.row.state == 'commit' || scope.row.state == 'tea_reject'? true:false"
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
              @change="selectOption($event)"
              placeholder="请输入学科竞赛类别"
              loading-text="搜索中..."
              :remote-method="selectCompetitionTypeMethod"
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

        <el-form-item label="竞赛级别:" label-width="80px" style="margin-left: 20px;" prop="competitionLevel">
          <span class="isMust">*</span>
          <el-select v-model="currentCompetitionCopy.competitionLevel">
            <el-option v-for="item in competitionLevelList" :key="item" :value="item" :label="item"></el-option>
          </el-select>
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
        <span style="color:gray;font-size:10px">将会获得：{{competitionPoint}}积分</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAdd">取 消</el-button>
        <el-button type="primary" @click="addCompetition" v-show="addButtonState">提 交</el-button>
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
        <el-form-item label="获奖级别:">
          <span>{{ currentCompetition.competitionLevel }}</span
          >
        </el-form-item>
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
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer"
             @click="download(currentCompetition)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{currentCompetition.url|fileNameFilter}}</a>
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

  </div>
</template>

<script>
import axios from "axios";
import {postRequest1} from "@/utils/api";
import {debounce} from "@/utils/debounce";
export default {
  name: "Academic-Competition",
  data() {
    return {
      competitionLimitRankN: '',
      selectedIndicator: {},
      searchTypeLoading: false,
      selectCompetitionType: '',
      selectCompetitionTypeName: '',
      isAuthorIncludeSelf: true,
      //先选择立项时间才可以输入竞赛类别
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
      addButtonState: false,//是否允许添加学科竞赛
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
    this.debounceSearch = debounce(this.debounceSearchType,400);
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
    //选择下拉框的某个选项
    selectOption(data) {
      if(data) {
        this.getRequest('/competition/basic/getIndicatorScore?id=' + data.indicatorId).then(response => {
          if(response) {
            this.competitionLimitRankN = response.data.rankN;
            this.selectedIndicator = response.data;
          }else {
            this.competitionLimitRankN = '';
            this.selectedIndicator = {};
          }
        })
        if(this.urlFile) {
          this.addButtonState = true;
        } else {
          this.addButtonState = false;
        }
      } else this.addButtonState = false;
    },
    //改变竞赛的时间
    changeCompetitionStartDate(data) {
      if(data) {
        this.disabledSelectCompetitionType = false;
      }else {
        this.disabledSelectCompetitionType = true;
      }
    },
    debounceSearchType(data) {
      if (this.currentCompetitionCopy.date != null && this.currentCompetitionCopy.date != '' && data != null && data != '') {
        this.getRequest('/competition/basic/getIndicatorByYearAndType?year=' + this.currentCompetitionCopy.date.split('-')[0] + '&type=' + data).then(response => {
          if(response) {
            this.searchTypeLoading = false;
            this.selectCompetitionTypeList = response.data;
          }
        })
      }
    },
    //输入竞赛类别 发送请求调用的函数
    selectCompetitionTypeMethod(data) {
      this.searchTypeLoading = true;
      this.debounceSearch(data);
    },
    cancelAdd() {
      this.dialogVisible = false;
    },
    download(data) {//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      var url = data.url
      axios({
        url: '/competition/basic/downloadByUrl?url=' + url,
        method: 'GET',
        responseType: 'blob',
        headers: {
          'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
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
      axios.post("/competition/basic/upload",formData,{
        headers:{
          'token': localStorage.getItem('user') ? this.user.token : ''
        }
      }).then((response)=> {
            this.$message({
              message: '上传成功！'
            })
            //获取文件路径
            this.urlFile = response.data
            if(JSON.parse(JSON.stringify(this.selectCompetitionType)) != '{}') {
              this.addButtonState = true;
            } else {
              this.addButtonState = false;
            }
          }
      )
    },
    judgeMember(){//输入作者框 失去焦点触发事件
      var val = this.currentCompetitionCopy.author;
      if(!val) {
        return;
      }
      var isalph = false//判断输入中是否有英文字母
      for(var i in val){
        var asc = val.charCodeAt(i)
        if(asc >= 65 && asc <= 90 || asc >= 97 && asc <= 122){
          isalph = true
          break
        }
      }
      var num = null
      var info = this.user;
      if(val.indexOf("；")>-1 && val.indexOf(";") == -1){//中文
        num = val.split('；')
      }else if(val.indexOf(";")>-1 && val.indexOf("；") == -1){//英文
        num = val.split(';')
      }else if(val.indexOf("；")>-1 && val.indexOf(";")>-1){//不允许同时包含中文和英文逗号
        this.$message.error();('输入不合法请重新输入！')
      }else if(val.indexOf("；") == -1 && val.indexOf(";") == -1){//只有一个人
        if(val != info.name){//有英文字符
          this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。");
          this.isAuthorIncludeSelf = false;
        }else if (val === info.name) {
          this.currentCompetitionCopy.rank = 1;
          this.currentCompetitionCopy.total = 1;
          this.isAuthorIncludeSelf = true;
          this.judgeRankScore(1);
        }
        return
      }
      //不止一个作者 判断自己在不在其中
      if(num.indexOf(info.name) == -1 && !isalph){//不在 并且没有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】");
        this.isAuthorIncludeSelf = false;
      }else if(num.indexOf(info.name) == -1 && isalph){//不在 里面有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。");
        this.isAuthorIncludeSelf = false;
      } else { //自己在里面
        this.judgeRankScore(num.indexOf(info.name) + 1);
        this.isAuthorIncludeSelf = true;
      }
      this.currentCompetitionCopy.total = num.length;
      this.currentCompetitionCopy.rank = num.indexOf(info.name) + 1;
    },
    judgeRankScore(rank) {
      if(JSON.parse(JSON.stringify(this.selectedIndicator)) === '{}') this.competitionPoint = 0; //输入作者，但未选择指标点
      else { //指标点已选择，再次修改作者列表
        const indicatorRankN = this.selectedIndicator.rankN;
        if(rank > indicatorRankN && indicatorRankN > 0) {
          this.competitionPoint = 0;
        }
        else {
          this.competitionPoint = this.selectedIndicator.score;
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
      this.isAuthorIncludeSelf = true;
      this.addButtonState = true;
    },
    showInfo(data){
      this.loading = true;
      this.title_show = "显示详情";
      this.currentCompetition = data
      this.dialogVisible_showInfo = true
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
      this.$refs["currentCompetitionCopy"].validate((valid) => {
        if (valid) {
          params.id = this.currentCompetitionCopy.id;
          if(JSON.parse(JSON.stringify(this.selectCompetitionType)) == '{}' || this.selectCompetitionType == '') {
            this.$message.error('请选择竞赛类别！')
            return;
          }
          if(params .url == '' || params == null){
            this.$message.error('请上传证明材料！')
            return
          }
          if(!this.isAuthorIncludeSelf) {
            this.$message.error('请仔细检查作者列表！');
            return;
          }
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
      params.competitionLevel = this.currentCompetitionCopy.competitionLevel;
      params.state = "commit";
      if (this.currentCompetitionCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editCompetition(params);
      } else {
        this.$refs["currentCompetitionCopy"].validate((valid) => {
          if (valid) {
            if(JSON.parse(JSON.stringify(this.selectCompetitionType)) == '{}' || this.selectCompetitionType == '') {
              this.$message.error('请选择竞赛类别！')
              return;
            }
            params.studentId = this.user.id;
            if(params.url == '' || params.url == null){
              this.$message.error('请上传证明材料！')
              return
            }
            if(!this.isAuthorIncludeSelf) {
              this.$message.error('请仔细检查作者列表！');
              return;
            }
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
      this.addButtonState = false;
      this.competitionPoint = '';
      this.title = "添加竞赛";
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
