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
            prop="name"
            align="center"
            label="奖励名称"
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
            prop="grantedStatus"
            label="申请状态"
            align="center"
            width="100px"
        >
        </el-table-column>
        <el-table-column
            prop="awardClass"
            label="奖励类别"
            align="center"
            width="100px"
        >
        </el-table-column>
        <el-table-column
            prop="awardLevel"
            label="奖励级别"
            align="center"
            width="100px"
        >
        </el-table-column>
        <el-table-column
            prop="awardRank"
            label="奖励排名"
            align="center"
            width="80px"
        >
        </el-table-column>
        <el-table-column
            prop="author"
            align="center"
            label="完成人"
            width="100px"
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
            prop="remark"
            width="140px"
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
    <!-- 添加科研项目对话框 -->
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
        <el-form-item label="奖励级别:" label-width="80px" style="margin-left: 20px;" prop="awardLevel">
          <span class="isMust">*</span>
          <el-select size="mini" v-model="currentAwardCopy.awardLevel" placeholder="请选择奖励类别" style="width: 329px">
            <el-option v-for="item in awardLevelList" :key="item.value" :value="item.label" :label="item.label"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="奖励类别:" label-width="80px" style="margin-left: 20px;" prop="awardClass">
          <span class="isMust">*</span>
          <el-select size="mini" v-model="currentAwardCopy.awardClass" placeholder="请选择奖励类别" style="width: 329px">
            <el-option v-for="item in awardClassList" :key="item.value" :value="item.label" :label="item.label"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="奖励排名:" prop="awardRank" label-width="80px" style="margin-left: 20px;width: 329px">
          <span class="isMust">*</span>
          <el-select size="mini" v-model="currentAwardCopy.awardRank" placeholder="请选择奖励类别" style="width: 329px">
            <el-option v-for="item in awardRankList" :key="item" :value="item" :label="item"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="申报日期:" label-width="80px" style="margin-left: 20px;">
          <el-date-picker
              style="width: 329px"
              v-model="currentAwardCopy.date"
              type="month"
              value-format="yyyy-MM"
              placeholder="选择日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item prop="grantedStatus" label="奖励状态" label-width="80px" style="margin-left: 20px;">
          <el-select
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentAwardCopy.grantedStatus"
              placeholder="请选择奖励状态"
              @change="changeAwardStatus($event)"
          >
            <el-option v-for="item in awardStatusList" :key="item.value" :value="item" :label="item.label"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="完成人:" label-width="80px" style="margin-left: 20px;">
          <el-input
              id="input_member"
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="member"
              @blur="judgeMember()"
              placeholder="请输入完成人,如有多个用分号分隔"
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
            >选择文件</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
            <span style="color:gray;font-size:11px">只允许doc docx pdf jpg png jpe rar zip类型文件
                  &nbsp;&nbsp;大小不能超过10MB
                </span>
          </el-upload>
          <div>

          </div>

        </el-form-item>
      </el-form>
      <div style="margin-left: 20px;">
        <span style="color:gray;font-size:10px">将会获得：{{view_point}}积分</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAdd">取 消</el-button>
        <el-button type="primary" @click="addAward">提 交</el-button>
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
          :model="currentAward"
          ref="empForm"
          style="margin-left: 20px">
        <el-form-item label="奖励名称:" prop="name" fixed>
          <span>{{ currentAward.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="作  者:" prop="author">
          <span>{{ currentAward.author }}</span
          >
        </el-form-item>
        <el-form-item label="奖励类别:" prop="awardClass">
          <span>{{ currentAward.awardClass }}</span
          >
        </el-form-item>
        <el-form-item label="奖励级别:" prop="awardLevel">
          <span>{{ currentAward.awardLevel }}</span
          >
        </el-form-item>
        <el-form-item label="奖励排名:" prop="awardRank">
          <span>{{ currentAward.awardRank }}</span
          >
        </el-form-item>
        <el-form-item label="作者排名:" prop="rank">
          <span>{{ currentAward.rank }}</span
          >
        </el-form-item>
        <el-form-item label="获得年份:" prop="date">
          <span>{{ currentAward.date }}</span
          >
        </el-form-item>
        <el-form-item label="申请状态:" prop="grantedStatus">
          <span>{{ currentAward.grantedStatus}}</span
          ><br />
        </el-form-item>
        <el-form-item label="奖励状态:" prop="grantedStatus">
          <span>{{ currentAward.state }}</span
          ><br />
        </el-form-item>
        <el-form-item label="备  注:">
          <span>{{ currentAward.remark }}</span>
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentAward.url == '' || currentAward.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer"
             @click="download(currentAward)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{currentAward.url|fileNameFilter}}</a>
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
  </div>
</template>

<script>
import axios from "axios";
import {postRequest1} from "@/utils/api";
export default {
  name: "SalSearch",
  data() {
    return {
      adate: '',
      disabledInput:false,
      timer:null,
      select_TypeName:[],
      isTypeFlag:false,
      isTypeShow:false,
      empawardName:'',//添加项目中的项目名称
      view_point:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      addButtonState:true,//是否允许添加项目
      operList:[],//每个项目的历史操作记录
      remarksort:[],//对显示的驳回理由做排序
      member:'',//和输入的作者列表绑定
      options:[],//存储所有类型对象
      data_picker:"",//选择时间
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
      awardTypeID:-1,
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
      awardClassList: [
        {
          label:'一等奖',
          value: 1
        },
        {
          label:'二等奖',
          value: 2
        },
        {
          label:'三等奖',
          value: 3
        }
      ],
      awardStatusList: [{
        label:'受理',
        value: 75
      },{
        label:'初审',
        value: 76
      },{
        label:'公布',
        value: 107
      },{
        label:'实审',
        value: 105
      },{
        label:'授权',
        value: 92
      },{
        label:'转让',
        value: 77
      }],
      awardRankList: Array.from(Array(5).keys(), n => n + 1),
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
        grantedStatus: '',
        state: '',
        date: "",
        rank: "",
        total:"",
        point:"",
        url:'',
        remark:'',
        awardLevel:'',
        awardClass:'',
        awardRank:''
      },
      rules: {
        name: [{ required: true, message: "请输入奖励名称", trigger: "blur" }],
        awardClass: [{ required: true, message: "请输入奖励类别", trigger: "blur" }],
        awardLevel: [{ required: true, message: "请输入奖励级别", trigger: "blur" }],
        awardRank: [{ required: true, message: "请输入奖励排名", trigger: "blur" }]
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
    cancelAdd() {
      this.dialogVisible = false;
    },
    changeAwardStatus(item){
      this.currentAwardCopy.grantedStatus = item.label;
      if(item.value < 0) {
        this.view_point = 0;
        this.currentAwardCopy.indicatorId = null;
      }else {
        this.getRequest('/indicator/getScoreById?indicatorId=' + item.value).then(response => {
          if(response.obj){
            this.view_point = response.obj;
          }
          this.currentAwardCopy.indicatorId = item.value;
        })
      }
      this.currentAwardCopy.point = this.view_point;
    },
    download(data){//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      if(localStorage.getItem("user")){
        var url="/award/basic/download?fileUrl=" + data.url + "&fileName=" + fileName
        window.location.href = encodeURI(url);
      }else{
        this.$message.error("请重新登录！");
      }
    },
    handleDelete() {//删除选择的文件
      var file={
        filepath:this.urlFile
      }
      this.postRequest1("/award/basic/deleteFile",file).then(
          (response)=>{
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
      axios.post("/award/basic/upload",formData,{
        headers:{
          'token': localStorage.getItem('user') ? this.user.token : ''
        }
      }).then(
          (response)=>{
            this.$message({
              message:'上传成功！'
            })
            this.addButtonState = true
            //获取文件路径
            this.urlFile = response.data
          },()=>{}
      )
    },
    timechange(picker){//选择日历调用的方法
      var data=new Date(picker)
      this.currentAwardCopy.date = data
      this.options=[]
    },
    judgeMember(){//输入作者框 失去焦点触发事件
      var val = this.member;
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
      if(val.indexOf("；")>-1 && val.indexOf(";") == -1){//中文
        num=val.split('；')
      }else if(val.indexOf(";")>-1 && val.indexOf("；") == -1){//英文
        num=val.split(';')
      }else if(val.indexOf("；")>-1 && val.indexOf(";")>-1){//中英都有
        this.$message.error();('输入不合法请重新输入！')
      }else if(val.indexOf("；") == -1 && val.indexOf(";") == -1){//只有一个人
        if(this.member != info.name && isalph){
          this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。");
          this.addButtonState=false
        }else{
          this.addButtonState=true
          this.currentAwardCopy.author = this.member
          this.currentAwardCopy.rank = 1
          this.currentAwardCopy.total = 1
        }
        return
      }
      //判断自己在不在其中
      if(num.indexOf(info.name) == -1 && !isalph){//不在 并且没有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】");
        this.addButtonState=false
      }else if(num.indexOf(info.name) == -1 && isalph){//不在 里面有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。");
        this.addButtonState=false
      }
      this.currentAwardCopy.rank = num.indexOf(info.name) + 1
      this.currentAwardCopy.author = this.member
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    //编辑按钮
    showEditEmpView(data, idx) {
      this.title = "编辑奖励信息";
      this.currentAwardCopy = JSON.parse(JSON.stringify(data));
      this.dialogVisible = true;
      this.member = this.currentAwardCopy.author
      this.options = []
      this.view_point = data.point
    },
    showInfo(data){
      this.loading = true;
      this.title_show = "显示详情";
      this.currentAward = data
      this.dialogVisible_showInfo = true
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=科研获奖').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteEmp(data) {
      if(confirm(
          "此操作将永久删除【" + data.name + "】, 是否继续?",)){
        this.deleteRequest("/award/basic/remove/" + data.id).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.initAwardsList();
          }
        })
      }
    },
    editAward() {
      this.$refs["currentAwardCopy"].validate((valid) => {
        if (valid) {
          const params = {};
          this.currentAwardCopy.url = this.urlFile;
          this.currentAwardCopy.state = "commit";
          for(let key in this.currentAwardCopy) {
            if(key !== 'indicator' && key !== 'student' && key !== 'operationList') {
              params[key] = this.currentAwardCopy[key];
            }
          }
          if(this.currentAwardCopy.url == '' ||this.currentAwardCopy.url == null){
            this.$message.error('请上传证明材料！')
            return
          }
          this.postRequest1("/award/basic/edit", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.$message.success('编辑成功！')
                  this.doAddOper("commit", resp.data);
                }
              }
          );
        }
      });
    },
    addAward() {//项目提交确认
      if (this.currentAwardCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editAward();
      } else {
        this.$refs["currentAwardCopy"].validate((valid) => {
          if (valid) {
            this.currentAwardCopy.url = this.urlFile;
            this.currentAwardCopy.state = "commit"
            this.currentAwardCopy.studentId = this.user.id
            if(this.currentAwardCopy.url == '' ||this.currentAwardCopy.url == null){
              this.$message.error('请上传证明材料！')
              return
            }
            this.postRequest1("/award/basic/add",this.currentAwardCopy).then(
                (resp) => {
                  if (resp) {
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
    showAddEmpView() {//点击添加科研项目按钮
      this.urlFile = ''
      this.currentAwardCopy = {};
      this.addButtonState=true
      this.member=''
      this.data_picker=''
      this.title = "添加科研项目";
      this.dialogVisible = true;
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
