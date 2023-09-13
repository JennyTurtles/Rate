<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加决策
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="decisionsList"
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
            label="决策名称"
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
            prop="decisionType.name"
            label="决策类别"
            align="center"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="indicator.name"
            align="center"
            label="指标点"
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
                v-show="scope.row.state == 'commit' || scope.row.state == 'tea_reject' || scope.row.state == 'adm_reject'? true:false"
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
    <!-- 添加决策咨询对话框 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :model="currentDecisionCopy"
          :rules="rules"
          ref="currentDecisionCopy"
      >
        <el-form-item label="决策名称:" label-width="80px" style="margin-left: 20px;" prop="name">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentDecisionCopy.name"
              placeholder="请输入决策名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="制定年月:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-date-picker
              style="width: 80%"
              v-model="currentDecisionCopy.date"
              @change="changeProjectStartDate($event)"
              type="month"
              value-format="yyyy-MM"
              placeholder="选择年月">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="指标点:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-button size="mini" type="text" @click="initTree()">{{indicatorBtn}}</el-button>
        </el-form-item>
        <el-form-item label="决策类别:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-select
              :disabled="disabledSelectDecisionType"
              v-model="selectDecisionType"
              value-key="id"
              filterable
              remote
              clearable
              reserve-keyword
              loading-text="搜索中..."
              :loading="searchTypeLoading"
              placeholder="请输入决策咨询类别"
              @focus="selectDecisionTypeMethod">
            <el-option
                v-for="item in selectDecisionTypeList"
                :key="item.id"
                :label="item.name"
                :value="item">
            </el-option>
          </el-select>
          <el-tooltip class="item" effect="dark" content="如：xxx等" placement="top-start">
            <i class="el-icon-question" style="margin-left: 10px;font-size: 16px"></i>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="制定人:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              id="input_member"
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="currentDecisionCopy.author"
              @blur="judgeMember()"
              placeholder="请输入制定人,如有多个用分号分隔"
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
        <span style="color:gray;font-size:10px">将会获得：{{decisionPoint}}积分</span>
        <span style="color:gray;font-size:10px;margin-left: 8px">{{ zeroPointReason }}</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAdd">取 消</el-button>
        <el-button type="primary" @click="addDecision">提 交</el-button>
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
          :model="currentDecision"
          style="margin-left: 20px">
        <el-form-item label="决策名称:">
          <span>{{ currentDecision.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="制定人:">
          <span>{{ currentDecision.author }}</span
          >
        </el-form-item>
        <el-form-item label="制定年月:">
          <span>{{ currentDecision.date }}</span
          >
        </el-form-item>
        <el-form-item label="制定人数:">
          <span>{{ currentDecision.total }}</span
          >
        </el-form-item>
        <el-form-item label="作者排名:">
          <span>{{ currentDecision.rank }}</span
          >
        </el-form-item>
        <el-form-item label="成果状态:">
          <span>{{currentDecision.state=="commit"
              ? "已提交"
              :currentDecision.state=="tea_pass"
                  ? "导师通过"
                  :currentDecision.state=="tea_reject"
                      ? "导师驳回"
                      :currentDecision.state=="adm_pass"
                          ? "管理员通过"
                          :"管理员驳回"}}</span
          ><br />
        </el-form-item>
        <el-form-item label="备  注:">
          <span>{{ currentDecision.remark }}</span>
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentDecision.url == '' || currentDecision.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer"
             @click="download(currentDecision)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{currentDecision.url|fileNameFilter}}</a>
          <br />
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

    <el-dialog title="选择指标点分类" center :visible.sync="showTreeDialog" width="60%">
      <span class="el-tree-node">
        <el-tree
            :data="indicatorData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :expand-on-click-node="false"
            :highlight-current="true"
            default-expand-all
        ></el-tree>
      </span>
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
      showTreeDialog: false,
      indicatorData: [],
      indicatorBtn: '选择指标点',
      defaultProps: {
        children: "children",
        label: "label",
      },
      currentIndicator: {},
      zeroPointReason: '',
      searchTypeLoading: false,
      isAuthorIncludeSelf: false,
      selectDecisionType: {},
      selectDecisionTypeList: [],
      disabledSelectDecisionType: true,
      decisionPoint:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      operList:[],//每个项目的历史操作记录
      labelPosition: "left",
      title: "",
      title_show: "",
      decisionsList: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      decisionTypename:"",//决策类别
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '决策咨询',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      currentDecisionCopy: {},
      currentDecision: {
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
        decisionLevel:'',
        decisionTypeId:'',
        decisionType: {}
      },
      rules: {
        name: [{ required: true, message: "请输入决策名称", trigger: "blur" }]
      },
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
    menuHeight() {
      return this.decisionTypename.length * 50 > 150
          ? 150 + 'px'
          : '${this.decisionTypename.length * 50}px'
    },
  },
  created() {
    this.debounceSearch = debounce(this.debounceSearchType,400);
  },
  mounted() {
    this.currentDecisionCopy = JSON.parse(JSON.stringify(this.currentDecision));
    this.initDecisionsList();
  },
  filters:{
    fileNameFilter:function(data){//将证明材料显示出来
      if(data == null || data == ''){
        return '无证明材料'
      }else{
        var arr= data.split('/')
        return  arr.reverse()[0]
      }
    }
  },
  methods: {
    //不进行rankN判断
    handleNodeClick(data, node) {
      if (data.children.length == 0) {
        this.indicatorBtn = data.label;
        this.currentIndicator = data;
        this.currentDecisionCopy.indicatorId = data.id;
        if (!this.isAuthorIncludeSelf) {
          this.decisionPoint = 0;
          this.zeroPointReason = '参与人未包含自己'
        } else {
          this.decisionPoint = data.score;
          this.zeroPointReason = '';
        }
        this.showTreeDialog = false;
      }
    },
    initTree() {
      this.getRequest("/indicator/getAllByType?type=决策咨询").then( resp => {
        this.showTreeDialog = true;
        if (resp) {
          this.indicatorData = resp.obj[1];
        }
      });
    },
    changeProjectStartDate(data) {
      if(data) {
        this.disabledSelectDecisionType = false;
      }else {
        this.disabledSelectDecisionType = true;
      }
    },
    //选择下拉框的某个选项
    selectOption(data) {
      if(data) {
        this.getRequest('/decision/basic/getIndicatorScore?id=' + data.indicatorId).then(response => {
          if(response) {
            this.decisionPoint = response.data.score;
            this.currentIndicator = response.data;
            this.judgeMember();
          }else {
            this.decisionPoint = 0;
            this.zeroPointReason = '';
            this.currentIndicator = {};
          }
        })
      }
    },
    debounceSearchType(data) {
      if (this.currentDecisionCopy.date != null && this.currentDecisionCopy.date != '' && data != null && data != '') {
        this.getRequest('/decision/basic/getIndicatorByYearAndType?year=' + this.currentDecisionCopy.date.split('-')[0]).then(response => {
          if(response) {
            this.selectDecisionTypeList = response.data;
            this.searchTypeLoading = false;
          }
        })
      }
    },
    selectDecisionTypeMethod(data) {
      this.searchTypeLoading = true;
      this.debounceSearchType(data);
    },
    cancelAdd() {
      this.dialogVisible = false;
    },
    download(data){//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      if(localStorage.getItem("user")){
        var url="/decision/basic/download?fileUrl=" + data.url + "&fileName=" + fileName
        window.location.href = encodeURI(url);
      }else{
        this.$message.error("请重新登录！");
      }
    },
    handleDelete() {//删除选择的文件
      var file={
        filepath:this.urlFile
      }
      this.postRequest1("/decision/basic/deleteFile",file).then(
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
      axios.post("/decision/basic/upload",formData,{
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
      var val = this.currentDecisionCopy.author;
      if(!val || val === '') {
        this.isAuthorIncludeSelf = false;
        return;
      }
      var isalph = false//判断输入中是否有英文字母
      for(var i in val){
        var asc = val.charCodeAt(i)
        if(asc >= 65 && asc <= 90 || asc >= 97 && asc <= 122){
          isalph=true
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
      if(num.indexOf(info.name) == -1){//不在 并且没有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        this.isAuthorIncludeSelf = false;
        this.zeroPointReason = '参与人未包含自己'
        this.decisionPoint = 0;
      } else {
        if(JSON.stringify(this.currentIndicator) == '{}') { //未选择指标点
          this.decisionPoint = 0;
          this.zeroPointReason = '请选择指标点';
        }else {
          this.decisionPoint = this.currentIndicator.score;
          this.zeroPointReason = '';
        }
        this.isAuthorIncludeSelf = true;
      }
      this.currentDecisionCopy.total = num.length
      this.currentDecisionCopy.rank = num.indexOf(info.name) + 1
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    //编辑按钮
    showEditEmpView(data, idx) {
      this.title = "编辑决策信息";
      this.currentIndicator = data.indicator;
      this.indicatorBtn = this.currentIndicator.name;
      this.currentDecisionCopy = JSON.parse(JSON.stringify(data));
      this.isAuthorIncludeSelf = true;
      this.disabledSelectDecisionType = false;
      this.decisionPoint = data.point;
      const { id, name } = data.decisionType;
      this.selectDecisionType = name;
      this.dialogVisible = true;
      this.files = [
        {
          name: this.currentDecisionCopy.url.split('/').reverse()[0],
          url: this.currentDecisionCopy.url
        }
      ]
      this.urlFile = this.currentDecisionCopy.url;
    },
    showInfo(data){
      this.loading = true;
      this.title_show = "显示详情";
      this.currentDecision = data
      this.dialogVisible_showInfo = true
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=决策咨询').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteEmpMethod(data) {
      return new Promise((resolve, reject) => {
            this.deleteRequest("/decision/basic/remove/" + data.id).then((resp) => {
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
          this.initDecisionsList();
        }).catch(() => {
          this.$message.error('删除失败!');
        })
      })
    },
    deleteOperationList(data) {
      const params = {}
      params.prodId = data.id;
      params.prodType = '决策咨询'
      return new Promise((resolve, reject) => {
        this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
          resolve('success');
        })
      })
    },
    editDecision(params) {
      this.$refs["currentDecisionCopy"].validate((valid) => {
        if (valid) {
          params.id = this.currentDecisionCopy.id;
          params.decisionTypeId = this.currentDecisionCopy.decisionType.id;
          params.studentId = this.user.id;
          this.postRequest1("/decision/basic/edit", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.$message.success('编辑成功！')
                  this.doAddOper("commit", this.currentDecisionCopy.id);
                }
              }
          );
        }
      });
    },
    addDecision() {//项目提交确认
      const params = {};
      params.name = this.currentDecisionCopy.name;
      params.url = this.urlFile;
      params.rank = this.currentDecisionCopy.rank;
      params.total = this.currentDecisionCopy.total;
      params.author = this.currentDecisionCopy.author;
      params.date = this.currentDecisionCopy.date;
      params.point = this.decisionPoint;
      params.decisionLevel = this.currentDecisionCopy.decisionLevel;
      params.state = "commit";
      params.indicatorId = this.currentIndicator.id;
      if(JSON.stringify(this.currentIndicator) === '{}') {
        this.$message.error('请选择指标点！');
        return;
      }
      if(JSON.stringify(this.selectDecisionType) == '{}' || this.selectDecisionType == '') {
        this.$message.error('请选择决策类别！')
        return;
      }
      if(params.url == '' || params.url == null){
        this.$message.error('请上传证明材料！')
        return
      }
      if(!this.isAuthorIncludeSelf) {
        this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        return;
      }
      if (this.currentDecisionCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editDecision(params);
      } else {
        this.$refs["currentDecisionCopy"].validate((valid) => {
          if (valid) {
            params.studentId = this.user.id;
            params.decisionTypeId = this.selectDecisionType.id;
            this.postRequest1("/decision/basic/add", params).then(
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
    async doAddOper(state,decisionID) {
      this.oper.state = state
      this.oper.prodId = decisionID
      this.oper.operationName = "提交决策"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper);
      await this.initDecisionsList();
    },
    showAddEmpView() {//点击添加决策咨询按钮
      this.currentIndicator = {};
      this.urlFile = ''
      this.currentDecisionCopy = {};
      this.selectDecisionType = {};
      this.indicatorBtn = '选择指标点'
      this.title = "添加决策咨询";
      this.dialogVisible = true;
      this.decisionPoint = '';
      this.isAuthorIncludeSelf = false;
      this.disabledSelectDecisionType = true;
      this.selectDecisionTypeList = [];
    },
    initDecisionsList() {
      this.loading = true;
      let url = "/decision/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.decisionsList = resp.data;
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
