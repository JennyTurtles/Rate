<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加标准
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          stripe
          border
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
      >
        <el-table-column
            fixed
            prop="name"
            align="center"
            label="标准名称"
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
            label="制定人"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="indicator.name"
            align="center"
            label="标准类别"
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
            style="width:90px"
            align="center"
            label="备注"
            min-width="20%"
        >
        </el-table-column>
        <el-table-column align="center" width="275" label="操 作" min-width="20%">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row)"
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

    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :rules="rules"
          :model="currentStandardCopy"
          ref="currentStandardCopy"
      >
        <el-form-item label="标准名称:" prop="name" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentStandardCopy.name"
              placeholder="请输入标准名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="制定年月:" prop="date" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-date-picker
              v-model="currentStandardCopy.date"
              type="month"
              value-format="yyyy-MM"
              placeholder="制定年月">
          </el-date-picker>
        </el-form-item>
        <el-form-item  prop="author" label="制定人:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentStandardCopy.author"
              @blur="judgeStandardee()"
              placeholder="请输入制定人,如有多个用分号按顺位分隔"
          ></el-input>
        </el-form-item>
        <el-form-item label="指标点:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-button ref="selectBtn" size="mini" type="text" @click="initTree()">{{indicatorBtn}}</el-button>
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
        <span style="color:gray;font-size:10px">将会获得：{{standardPoint}}积分</span>
        <span style="color:gray;font-size:10px;margin-left: 8px">{{zeroPointReason}}</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAddStandard">取 消</el-button>
        <el-button type="primary" @click="addStandard">提 交</el-button>
      </span>
    </el-dialog>

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
          :model="currentStandard"
          style="margin-left: 20px">

        <el-form-item label="标准名称:" prop="name">
          <span>{{ currentStandard.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="制定年月:" prop="date">
          <span>{{ currentStandard.date }}</span
          ><br />
        </el-form-item>
        <el-form-item label="制定人:" prop="author">
          <span>{{ currentStandard.author }}</span
          ><br />
        </el-form-item>
        <el-form-item label="总人数:" prop="total">
          <span>{{ currentStandard.total }}</span
          ><br />
        </el-form-item>
        <el-form-item label="排名:" prop="rank">
          <span>{{ currentStandard.rank }}</span
          ><br />
        </el-form-item>
        <el-form-item label="积分:" prop="point">
          <span>{{ currentStandard.point }}</span
          ><br />
        </el-form-item>
        <el-form-item label="状态:" prop="state">
          <span>{{ currentStandard.state=="commit"
              ? "已提交"
              :currentStandard.state=="tea_pass"
                  ? "导师通过"
                  :currentStandard.state=="tea_reject"
                      ? "导师驳回"
                      :currentStandard.state=="adm_pass"
                          ? "管理员通过"
                          :"管理员驳回" }}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentStandard.url == '' || currentStandard.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer"
             @click="download(currentStandard)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{currentStandard.url|fileNameFilter}}</a>
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
export default {
  name: "SalSearch",
  data() {
    return {
      zeroPointReason: '',
      isAuthorIncludeSelf: false,
      indicatorBtn: '选择指标点',
      defaultProps: {
        children: "children",
        label: "label",
      },
      showTreeDialog: false,
      currentSelectedIndicator: {},
      indicatorData: [],
      //在编辑状态时，做一个副本，用户点击取消可恢复原始状态
      currentStandardCopy: {},
      standardPoint:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      operList:[],//每个项目的历史操作记录
      standardee:'',//和输入的作者列表绑定
      labelPosition: "left",
      title: "",
      title_show: "",
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '制定标准',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      currentStandard: {
        id: '',
        name:'',
        studentId: '',
        date:"",
        author:"",
        rank:"",
        total:"",
        point:"",
        url:'',
        state:" ",
        remark: '',
        indicatorId:''
      },
      rules: {
        name: [{ required: true, message: "请输入标准名称", trigger: "blur" }],
        author: [{ required: true, message: "请输入标准制定人", trigger: "blur" }],
        date: [{ required: true, message: "请选择制定年月", trigger: "blur" }],
      }
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  created() {},
  mounted() {
    this.currentStandardCopy = JSON.parse(JSON.stringify(this.currentStandard));
    this.initEmps();
    // this.initTutor(this.user);
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
        this.currentSelectedIndicator = data;
        this.currentStandardCopy.indicatorId = data.id;
        if (!this.isAuthorIncludeSelf) { //作者列表不包含自己
          this.standardPoint = 0;
          this.zeroPointReason = '参与人未包含自己';
        } else {
          this.standardPoint = data.score;
          this.zeroPointReason = '';
        }
        this.showTreeDialog = false;
      }
    },
    initTree() {
      this.getRequest("/indicator").then( resp => {
        this.showTreeDialog = true;
        if (resp) {
          this.indicatorData = resp.obj[1];
        }
      });
    },
    //添加 编辑框点击取消出发事件
    cancelAddStandard() {
      this.dialogVisible = false;
    },
    download(data){//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      var url = data.url
      axios({
        url: '/standard/basic/downloadByUrl?url='+url,
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
      var file={
        filepath:this.urlFile
      }
      this.postRequest1("/standard/basic/deleteFile",file).then(
          (response)=>{
            this.urlFile = '';
            this.files = [];
          },()=>{}
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
      axios.post("/standard/basic/upload",formData,{
        headers:{
          'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
      }).then(
          (response)=>{
            this.$message({
              message:'上传成功！'
            })
            //获取文件路径
            this.urlFile=response.data
          },()=>{}
      )
    },
    judgeStandardee(){//输入作者框 失去焦点触发事件
      var val = this.currentStandardCopy.author;
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
      var memberList = null
      var info = this.user;
      memberList = val.split(/[;；]/)
      memberList = memberList.map(item => {
        return item && item.replace(/\s*/g,"");
      }).filter(v => {
        return v
      })
      //不止一个作者 判断自己在不在其中
      if(memberList.indexOf(info.name) == -1){//不在 并且没有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        this.isAuthorIncludeSelf = false;
        this.zeroPointReason = '参与人未包含自己'
        this.standardPoint = 0;
      } else { //自己在里面
        if(this.currentSelectedIndicator) {
          this.standardPoint = this.currentSelectedIndicator.score;
          this.zeroPointReason = '';
        }else {
          this.standardPoint = '';
          this.zeroPointReason = '';
        }
        this.isAuthorIncludeSelf = true;
      }
      this.currentStandardCopy.total = memberList.length;
      this.currentStandardCopy.rank = memberList.indexOf(info.name) + 1;
    },

    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    showEditEmpView(data) {
      console.log(data)
      this.dialogVisible = true;
      this.title = "编辑标准信息";
      this.currentStandardCopy = JSON.parse(JSON.stringify(data));
      this.files = [
        {
          name: this.currentStandardCopy.url.split('/').reverse()[0],
          url: this.currentStandardCopy.url
        }
      ];
      this.indicatorBtn = data.indicator.name;
      this.currentSelectedIndicator = data.indicator;
      this.standardPoint = data.point;
      this.zeroPointReason = '';
      this.urlFile = this.currentStandardCopy.url;
      this.isAuthorIncludeSelf = true;
    },
    showInfo(data){
      this.title_show = "显示详情";
      this.currentStandard = data
      this.dialogVisible_showInfo = true
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=制定标准').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteEmpMethod(data) {
      return new Promise((resolve, reject) => {
            this.deleteRequest("/standard/basic/remove/" + data.id).then((resp) => {
              this.dialogVisible = false;
              resolve('success');
            })
          }
      )
    },
    deleteEmp(data) {
      this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
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
      params.prodType = '制定标准'
      return new Promise((resolve, reject) => {
        this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
          resolve('success');
        })
      })
    },
    editStandard(params) {
      this.$refs["currentStandardCopy"].validate((valid) => {
        if (valid) {
          this.postRequest1("/standard/basic/edit", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.doAddOper("commit", this.currentStandardCopy.id);
                  this.$message.success('编辑成功！')
                }
              }
          );
        }
      });
    },
    addStandard() {//项目提交确认
      const params = {};
      params.id = this.currentStandardCopy.id;
      params.name = this.currentStandardCopy.name;
      params.url = this.urlFile;
      params.rank = this.currentStandardCopy.rank;
      params.total = this.currentStandardCopy.total;
      params.author = this.currentStandardCopy.author;
      params.indicatorId = this.currentStandardCopy.indicatorId;
      params.author = this.currentStandardCopy.author;
      params.date = this.currentStandardCopy.date;
      params.point = this.standardPoint;
      params.state = "commit";
      if(JSON.stringify(this.currentSelectedIndicator) == '{}') {
        this.$message.error('请选择指标点!');
        return;
      }
      if(params.url == '' || params.url == null){
        this.$message.error('请上传证明材料！')
        return;
      }
      if(!this.isAuthorIncludeSelf) {
        this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        return;
      }
      if (this.currentStandardCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editStandard(params);
      } else {
        this.$refs["currentStandardCopy"].validate((valid) => {
          if (valid) {
            params.studentId = this.user.id
            this.postRequest1("/standard/basic/add", params).then(
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
    async doAddOper(state, standardID) {
      this.oper.state = state
      this.oper.prodId = standardID
      this.oper.operationName = "提交标准"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper)
      await this.initEmps();
    },
    showAddEmpView() {//点击添加科研项目按钮
      this.isAuthorIncludeSelf = false;
      this.title = "添加标准";
      this.dialogVisible = true;
      this.urlFile = '';
      this.files = [];
      this.standardPoint = '';
      this.zeroPointReason = '';
      this.currentSelectedIndicator = {};
      this.indicatorBtn = '选择指标点';
      this.currentStandardCopy = {};
    },
    initEmps() {
      this.loading = true;
      let url = "/standard/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.obj;
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
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all 0.8s ease;
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

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}
</style>