<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加专著或教材
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="monographsList"
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
            label="专著或教材名称"
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
            prop="publisher"
            label="出版社"
            align="center"
            width="100px"
        >
        </el-table-column>
        <el-table-column
            prop="isbn"
            label="ISBN"
            align="center"
            width="100px"
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
    <!-- 添加科研专著或教材对话框 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :model="currentMonographCopy"
          :rules="rules"
          ref="currentMonographCopy"
      >
        <el-form-item label="著作名称:" label-width="80px" style="margin-left: 20px;" prop="name">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentMonographCopy.name"
              placeholder="请输入专著或教材名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="完成日期:" label-width="80px" style="margin-left: 20px;">
          <el-date-picker
              style="width: 80%"
              v-model="currentMonographCopy.date"
              type="month"
              value-format="yyyy-MM"
              placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="完成人:" label-width="80px" style="margin-left: 20px;">
          <el-input
              id="input_member"
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="member"
              @blur="judgeMember()"
              placeholder="请输入完成人,如有多个用分号分隔"
          ></el-input>
        </el-form-item>
        <el-form-item label="出版社:" label-width="80px" style="margin-left: 20px;" prop="publisher">
          <el-input
              id="input_member"
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="currentMonographCopy.publisher"
              placeholder="请输入专著或教材出版社"
          ></el-input>
        </el-form-item>
        <el-form-item label="ISBN:" label-width="80px" style="margin-left: 20px;" prop="isbn">
          <el-input
              id="input_member"
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="currentMonographCopy.isbn"
              placeholder="请输入专著或教材ISBN"
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
        <el-button @click="cancelAdd">取 消</el-button>
        <el-button type="primary" @click="addMonograph" v-show="addButtonState">提 交</el-button>
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
          :model="currentMonograph"
          ref="empForm"
          style="margin-left: 20px">
        <el-form-item label="著作名称:" prop="name" fixed>
          <span>{{ currentMonograph.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者名称:" prop="author">
          <span>{{ currentMonograph.author }}</span
          >
        </el-form-item>
        <el-form-item label="作者人数:" prop="rank">
          <span>{{ currentMonograph.total }}</span
          >
        </el-form-item>
        <el-form-item label="作者排名:" prop="rank">
          <span>{{ currentMonograph.rank }}</span
          >
        </el-form-item>
        <el-form-item label="完成年份:" prop="date">
          <span>{{ currentMonograph.date }}</span
          >
        </el-form-item>
        <el-form-item label="出版社:" prop="publisher">
          <span>{{ currentMonograph.publisher }}</span
          >
        </el-form-item>
        <el-form-item label="ISBN:" prop="isbn">
          <span>{{ currentMonograph.isbn }}</span
          >
        </el-form-item>
        <el-form-item label="相关备注:">
          <span>{{ currentMonograph.remark }}</span>
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentMonograph.url == '' || currentMonograph.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer"
             @click="download(currentMonograph)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{currentMonograph.url|fileNameFilter}}</a>
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
      empmonographName:'',//添加专著或教材中的专著或教材名称
      view_point:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      addButtonState: false,//是否允许添加专著或教材
      operList:[],//每个专著或教材的历史操作记录
      remarksort:[],//对显示的驳回理由做排序
      member:'',//和输入的作者列表绑定
      options:[],//存储所有类型对象
      labelPosition: "left",
      title: "",
      title_show: "",
      monographsList: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '专著教材',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      currentMonographCopy: {},
      currentMonograph: {
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
        publisher: '',
        isbn: ''
      },
      rules: {
        name: [{ required: true, message: "请输入专著或教材名称", trigger: "blur" }],
        publisher: [{ required: true, message: "请输入专著或教材出版社", trigger: "blur" }],
        isbn: [{ required: true, message: "请输入专著或教材ISBN", trigger: "blur" }]
      },
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  created() {},
  mounted() {
    this.currentMonographCopy = JSON.parse(JSON.stringify(this.currentMonograph));
    this.initMonographsList();
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
    changeMonographStatus(item){
      this.currentMonographCopy.grantedStatus = item.label;
      if(item.value < 0) {
        this.view_point = 0;
        this.currentMonographCopy.indicatorId = null;
      }else {
        this.getRequest('/indicator/getScoreById?indicatorId=' + item.value).then(response => {
          if(response.obj){
            this.view_point = response.obj;
          }
          this.currentMonographCopy.indicatorId = item.value;
        })
      }
      this.currentMonographCopy.point = this.view_point;
    },
    download(data) {//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      var url = data.url
      axios({
        url: '/monograph/basic/downloadByUrl?url=' + url,
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
      this.postRequest1("/monograph/basic/deleteFile",file).then(
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
      axios.post("/monograph/basic/upload",formData,{
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
          this.addButtonState = false
        }else{
          this.addButtonState = true
          this.currentMonographCopy.author = this.member
          this.currentMonographCopy.rank = 1
          this.currentMonographCopy.total = 1
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
      this.currentMonographCopy.rank = num.indexOf(info.name) + 1
      this.currentMonographCopy.author = this.member
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    //编辑按钮
    showEditEmpView(data, idx) {
      this.title = "编辑专著或教材信息";
      this.currentMonographCopy = JSON.parse(JSON.stringify(data));
      this.dialogVisible = true;
      this.member = this.currentMonographCopy.author
      this.options = []
      this.view_point = data.point
    },
    showInfo(data){
      this.loading = true;
      this.title_show = "显示详情";
      this.currentMonograph = data
      this.dialogVisible_showInfo = true
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=专著教材').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteEmp(data) {
      if(confirm(
          "此操作将永久删除【" + data.name + "】, 是否继续?",)){
        this.deleteRequest("/monograph/basic/remove/" + data.id).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.initMonographsList();
          }
        })
      }
    },
    editMonograph() {
      this.$refs["currentMonographCopy"].validate((valid) => {
        if (valid) {
          const params = {};
          this.currentMonographCopy.url = this.urlFile;
          this.currentMonographCopy.state = "commit";
          for(let key in this.currentMonographCopy) {
            if(key !== 'indicator' && key !== 'student' && key !== 'operationList') {
              params[key] = this.currentMonographCopy[key];
            }
          }
          if(this.currentMonographCopy.url == '' ||this.currentMonographCopy.url == null){
            this.$message.error('请上传证明材料！')
            return
          }
          this.postRequest1("/monograph/basic/edit", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.$message.success('编辑成功！')
                  this.doAddOper("commit", this.currentMonographCopy.id);
                }
              }
          );
        }
      });
    },
    addMonograph() {//专著或教材提交确认
      if (this.currentMonographCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editMonograph();
      } else {
        this.$refs["currentMonographCopy"].validate((valid) => {
          if (valid) {
            this.currentMonographCopy.url = this.urlFile;
            this.currentMonographCopy.state = "commit"
            this.currentMonographCopy.studentId = this.user.id
            this.currentMonographCopy.indicatorId = 26;
            if(this.currentMonographCopy.url == '' ||this.currentMonographCopy.url == null){
              this.$message.error('请上传证明材料！')
              return
            }
            this.postRequest1("/monograph/basic/add",this.currentMonographCopy).then(
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
    async doAddOper(state,monographID) {
      this.oper.state = state
      this.oper.prodId = monographID
      this.oper.operationName = "提交著作"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper);
      await this.initMonographsList();
    },
    showAddEmpView() {//点击添加科研专著或教材按钮
      this.urlFile = ''
      this.currentMonographCopy = {};
      this.addButtonState = false;
      this.member = ''
      this.title = "添加著作";
      this.dialogVisible = true;
    },
    initMonographsList() {
      this.loading = true;
      let url = "/monograph/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.monographsList = resp.data;
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
