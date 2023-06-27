<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加专利
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
            prop="name"
            align="center"
            label="专利名称"
            width="200"
        >
        </el-table-column>
        <el-table-column
            prop="state"
            label="状态"
            width="127px"
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
            label="参与人"
            width="150px"
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
            prop="grantedStatus"
            label="授权状态"
            align="center"
            width="75px"
        >
        </el-table-column>
        <el-table-column
            prop="remark"
            style="width:90px"
            align="center"
            label="备注"
        >
        </el-table-column>
        <el-table-column align="center" width="275" label="操 作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row)"
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

    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :model="currentPatentCopy"
          ref="empForm"
      >
        <el-form-item label="专利名称:" prop="name" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="patentName"
              placeholder="请输入专利名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="立项年月:" prop="month" label-width="80px" style="margin-left: 20px;">
          <el-date-picker
              v-model="patentDate"
              type="month"
              value-format="yyyy-MM"
              placeholder="立项年月">
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="grantedStatus" label="专利状态" label-width="80px" style="margin-left: 20px;">
          <el-select
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="patentGrantedStatus"
              placeholder="请选择专利状态"
              @change="changePatentStatus($event)"
          >
            <el-option v-for="item in patentStatusList" :key="item.value" :value="item" :label="item.label"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item  prop="total" label="参与人:" label-width="80px" style="margin-left: 20px;">
          <el-input
              id="input_patentee"
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="patentee"
              @blur="judgePatentee()"
              placeholder="请输入参与人,如有多个用分号按顺位分隔"
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
        </el-form-item>
      </el-form>
      <div style="margin-left: 20px;">
        <span style="color:gray;font-size:10px">将会获得：{{view_point}}积分</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAddPatent">取 消</el-button>
        <el-button type="primary" @click="doAddEmp" v-show="addButtonState">提 交</el-button>
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
          :model="emp"
          ref="empForm"
          style="margin-left: 20px">

        <el-form-item label="专利名称:" prop="name">
          <span>{{ emp.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="受理日期:" prop="date">
          <span>{{ emp.date }}</span
          ><br />
        </el-form-item>
        <el-form-item label="专利状态:" prop="grantedStatus">
          <span>{{ emp.grantedStatus }}</span
          ><br />
        </el-form-item>
        <el-form-item label="参与人:" prop="author">
          <span>{{ emp.author }}</span
          ><br />
        </el-form-item>
        <el-form-item label="总人数:" prop="total">
          <span>{{ emp.total }}</span
          ><br />
        </el-form-item>
        <el-form-item label="排名:" prop="rank">
          <span>{{ emp.rank }}</span
          ><br />
        </el-form-item>
        <el-form-item label="积分:" prop="point">
          <span>{{ emp.point }}</span
          ><br />
        </el-form-item>
        <el-form-item label="状态:" prop="state">
          <span>{{ emp.state }}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="emp.url == '' || emp.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer"
             @click="download(emp)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{emp.url|fileNameFilter}}</a>
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
      patentName:'',
      patentDate:'',
      patentGrantedStatus: '',
      //在编辑状态时，做一个副本，用户点击取消可恢复原始状态
      currentPatentCopy: null,
      patentStatusList: [{
        label:'受理',
        value: -1
      },{
        label:'初审',
        value: -2
      },{
        label:'公布',
        value: -3
      },{
        label:'实审',
        value: 107
      },{
        label:'授权',
        value: 105
      },{
        label:'转让',
        value: 92
      }],
      view_point:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      addButtonState:true,//是否允许添加项目
      operList:[],//每个项目的历史操作记录
      patentee:'',//和输入的作者列表绑定
      labelPosition: "left",
      title: "",
      title_show: "",
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      total: 0,
      page: 1,
      size: 10,
      patentTypename:"",//项目类别
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '授权专利',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      emp: {
        id: null,
        name:'',
        studentId: JSON.parse(localStorage.getItem('user')).id,
        date:"",
        grantedStatus:'',
        author:"",
        rank:"",
        total:"",
        point:"",
        url:'',
        state:" ",
        remark: '',
        indicatorId:''
      },
      // rules: {
      //   name: [{ required: true, message: "请输入专利名", trigger: "blur" }]
      // }
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  created() {},
  mounted() {
    this.initEmps();
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
    //添加 编辑框点击取消出发事件
    cancelAddPatent() {
      this.setCurrentPatent();
      this.dialogVisible = false;
    },
    setCurrentPatent() {
      this.emp.grantedStatus = this.patentGrantedStatus;
      this.emp.name = this.patentName;
      this.emp.author = this.patentee;
      this.emp.date = this.patentDate;
      this.emp.point = this.view_point;
      this.emp.date = this.patentDate;
    },
    //下拉框中选择专利的某个状态
    changePatentStatus(item){
      this.emp.grantedStatus = item.label;
      if(item.value < 0) {
        this.view_point = 0;
      }else {
        this.getRequest('/indicator/getScoreById?indicatorId=' + item.value).then(response => {
          if(response.obj){
            this.view_point = response.obj;
          }
        })
      }
      this.emp.point = this.view_point;
      this.emp.indicatorId = item.value;
    },
    download(data){//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      if(localStorage.getItem("user")){
        var url="/patent/basic/download?fileUrl=" + data.url + "&fileName=" + fileName
        window.location.href = encodeURI(url);
      }else{
        this.$message.error("请重新登录！");
      }
    },
    handleDelete() {//删除选择的文件
      var file={
        filepath:this.urlFile
      }
      this.postRequest1("/patent/basic/deleteFile",file).then(
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
      axios.post("/patent/basic/upload",formData,{
        headers:{
          'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
      }).then(
          (response)=>{
            this.$message({
              message:'上传成功！'
            })
            this.addButtonState = true
            //获取文件路径
            this.urlFile=response.data
          },()=>{}
      )
    },
    judgePatentee(){//输入作者框 失去焦点触发事件
      var val=this.patentee;
      var isalph = false//判断输入中是否有英文字母
      for(var i in val){
        var asc = val.charCodeAt(i)
        if(asc >= 65 && asc <= 90 || asc >= 97 && asc <= 122){
          isalph=true
          break
        }
      }
      var num=null
      // var info=JSON.parse(window.sessionStorage.getItem("user"))
      var info=JSON.parse(localStorage.getItem("user"))
      if(val.indexOf("；")>-1 && val.indexOf(";") == -1){//中文
        num=val.split('；')
      }else if(val.indexOf(";")>-1 && val.indexOf("；") == -1){//英文
        num=val.split(';')
      }else if(val.indexOf("；")>-1 && val.indexOf(";")>-1){//中英都有
        this.$message.error();('输入不合法请重新输入！')
      }else if(val.indexOf("；") == -1 && val.indexOf(";") == -1){//只有一个人
        if(this.patentee != info.name && isalph){
          this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。");
          this.addButtonState=false
        }else{
          this.addButtonState=true
          this.emp.author=this.patentee
          this.emp.rank=1
          this.emp.total=1
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
      }else{//自己在里面 自己是一作不用做任何判断 导师无所谓
        if(num.indexOf(info.name) > 0){//自己不是一作
          if(num.indexOf(info.teachers.name) > 0 || num.indexOf(info.teachers.name) == -1){//导师在作者列表中,不是一作
            this.$confirm(
                "第一作者不是导师【 " + info.teachers.name + " 】！积分将为【0】分",
                "提示",
                {
                  confirmButtonText: "确定",
                  // cancelButtonText: "取消",
                  type: "warning",
                }
            ).then(() => {//点击确定
              // num=[info.teachers.name,...num]
              // this.writer = num.join(';')
            },()=>{});
            this.emp.point = 0
            this.view_point = this.emp.point
          }
        }
        this.addButtonState = true
      }
      this.emp.total = num.length
      if(num.indexOf(info.teachers.name)>-1){
        num.splice(num.indexOf(info.teachers.name),1)
      }
      this.emp.rank = num.indexOf(info.name)+1
      this.emp.author=this.patentee
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    emptyEmp() {
      this.emp = {
        //startDate: null,
        name: "",
        typename:"",
        time:"",
        patentee:"",
        total:"",
        rank:"",
        point:"",
        url:"",
        state:"",
        remark: "",
      };
    },
    showEditEmpView(data) {
      this.dialogVisible = true;
      this.title = "编辑项目信息";
      this.patentName = data.name;
      this.patentDate = data.date;
      this.patentGrantedStatus = data.grantedStatus;
      this.patentee = data.author;
      this.view_point = data.point;
      this.emp.indicatorId = data.indicatorId;
      this.emp.id = data.id;
      this.emp.rank = data.rank;
      this.emp.total = data.total;
    },
    showInfo(data){
      this.title_show = "显示详情";
      this.emp = data
      this.dialogVisible_showInfo = true
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=授权专利').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteEmp(data) {
      if(confirm(
          "此操作将永久删除【" + data.name + "】, 是否继续?",)){
        this.deleteRequest("/patent/basic/remove/" + data.id).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.$message.success('删除成功！');
            this.initEmps();
          }
        })
      }
    },
    doAddEmp() {//项目提交确认
      if (this.emp.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.name = this.patentName;
            this.emp.point = this.view_point;
            this.emp.date = this.patentDate;
            this.emp.author = this.patentee;
            this.emp.url = this.urlFile;
            this.emp.state = "commit";
            if(this.emp.url == '' ||this.emp.url == null){
              this.$message({
                message:'请上传证明材料！'
              })
              return
            }
            this.postRequest1("/patent/basic/edit", this.emp).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible = false;
                    this.initEmps();
                    this.$message.success('编辑成功！')
                  }
                }
            );
          }
        });

      } else {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.name = this.patentName;
            this.emp.date = this.patentDate;
            this.emp.point = this.view_point
            this.emp.url = this.urlFile;
            this.emp.author = this.patentee;
            this.emp.state = "commit"
            this.emp.studentId = this.user.id
            if(this.emp.url == '' ||this.emp.url == null){
              this.$message.error('请上传证明材料！')
              return
            }
            this.postRequest1("/patent/basic/add",this.emp).then(
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
    async doAddOper(state, patentID) {
      this.oper.state = state
      this.oper.prodId = patentID
      this.oper.operationName = "提交专利"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper)
      await this.initEmps();
    },
    showAddEmpView() {//点击添加科研项目按钮
      this.addButtonState = true
      this.title = "添加专利";
      this.dialogVisible = true;
    },
    initEmps() {
      this.loading = true;
      let url = "/patent/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = 10;
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