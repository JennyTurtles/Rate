<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="addProjectDialog">
            添加科研项目
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="projectsList"
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
            label="科研项目名称"
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
            prop="projectType.name"
            label="项目类别"
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
            prop="startDate"
            label="立项时间"
            align="center"
            width="75px"
        >
        </el-table-column>
        <el-table-column
            prop="endDate"
            width="140px"
            align="center"
            label="结项时间"
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
                @click="deleteProject(scope.row)"
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
    <!-- 添加科研科研项目对话框 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :model="currentProjectCopy"
          :rules="rules"
          ref="currentProjectCopy"
      >
        <el-form-item label="项目名称:" label-width="80px" style="margin-left: 20px;" prop="name">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentProjectCopy.name"
              placeholder="请输入科研项目名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="立项时间:" label-width="80px" style="margin-left: 20px;" prop="startDate">
          <span class="isMust">*</span>
          <el-date-picker
              style="width: 80%"
              v-model="currentProjectCopy.startDate"
              type="month"
              @change="changeProjectStartDate($event)"
              value-format="yyyy-MM"
              placeholder="选择立项时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结项时间:" label-width="80px" style="margin-left: 20px;">
          <el-date-picker
              style="width: 80%"
              v-model="currentProjectCopy.endDate"
              type="month"
              value-format="yyyy-MM"
              placeholder="选择结项时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="参与人:" label-width="80px" style="margin-left: 20px;" prop="author">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="currentProjectCopy.author"
              @blur="judgeMember()"
              placeholder="请输入参与人,如有多个用分号分隔"
          ></el-input>
        </el-form-item>
        <el-form-item label="项目类别:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-select
              :disabled="disabledSelectProjectType"
              v-model="selectProjectType"
              value-key="id"
              filterable
              remote
              clearable
              reserve-keyword
              @change="selectOption($event)"
              placeholder="请输入科研项目类别"
              :remote-method="selectProjectTypeMethod"
              :loading="loading">
            <el-option
                v-for="item in selectProjectTypeList"
                :key="item.id"
                :label="item.name"
                :value="item">
            </el-option>
          </el-select>
          <el-tooltip class="item" effect="dark" content="Top Left 提示文字" placement="top-start">
            <i class="el-icon-question" style="margin-left: 10px;font-size: 16px"></i>
          </el-tooltip>
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
        <span style="color:gray;font-size:10px">将会获得：{{projectPoint}}积分</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAdd">取 消</el-button>
        <el-button type="primary" @click="addProject" v-show="addButtonState">提 交</el-button>
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
          :model="currentProject"
          style="margin-left: 20px">
        <el-form-item label="项目名称:">
          <span>{{ currentProject.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者列表:">
          <span>{{ currentProject.author }}</span
          >
        </el-form-item>
        <el-form-item label="立项日期:">
          <span>{{ currentProject.startDate }}</span
          >
        </el-form-item>
        <el-form-item label="结项日期:">
          <span>{{ currentProject.endDate }}</span
          >
        </el-form-item>
        <el-form-item label="项目类别:">
          <span>{{ currentProject.projectType.name }}</span
          >
        </el-form-item>
        <el-form-item label="作者人数:">
          <span>{{ currentProject.total }}</span
          >
        </el-form-item>
        <el-form-item label="作者排名:">
          <span>{{ currentProject.rank }}</span
          >
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentProject.url == '' || currentProject.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer"
             @click="download(currentProject)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{currentProject.url|fileNameFilter}}</a>
          <br />
        </el-form-item>
        <el-form-item label="相关备注:">
          <span>{{ currentProject.remark }}</span>
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
  name: "SalSearch",
  data() {
    return {
      selectProjectType: '',
      selectProjectTypeName: '',
      isAuthorIncludeSelf: true,
      //先选择立项时间才可以输入项目类别
      disabledSelectProjectType: true,
      //项目类别下拉框可选列表
      selectProjectTypeList: [],
      projectPoint:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      addButtonState: false,//是否允许添加科研项目
      operList:[],//每个科研项目的历史操作记录
      labelPosition: "left",
      title: "",
      title_show: "",
      projectsList: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '科研项目',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      currentProjectCopy: {},
      currentProject: {
        id: null,
        name: null,
        author:"",
        state: '',
        startDate: "",
        endDate: "",
        rank: "",
        total:"",
        point:"",
        url:'',
        remark:'',
        projectTypeId: '',
        projectType: {}
      },
      rules: {
        name: [{ required: true, message: "请输入科研项目名称", trigger: "blur" }],
        startDate: [{ required: true, message: "请输入科研项目立项时间", trigger: "blur" }],
        author: [{ required: true, message: "请输入科研项目作者", trigger: "blur" }],
      },
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  watch: {},
  created() {
    this.debounceSearch = debounce(this.debounceSearchType,400);
  },
  mounted() {
    this.currentProjectCopy = JSON.parse(JSON.stringify(this.currentProject));
    this.initProjectsList();
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
        this.getRequest('/project/basic/getIndicatorScore?id=' + data.indicatorId).then(response => {
          if(response) {
            this.projectPoint = response.data;
          }else {
            this.projectPoint = 0;
          }
        })
        if(this.urlFile) {
          this.addButtonState = true;
        } else {
          this.addButtonState = false;
        }
      } else this.addButtonState = false;
    },
    //改变项目的立项时间
    changeProjectStartDate(data) {
      if(data) {
        this.disabledSelectProjectType = false;
      }else {
        this.disabledSelectProjectType = true;
      }
    },
    debounceSearchType(data) {
      if (this.currentProjectCopy.startDate != null && this.currentProjectCopy.startDate != '' && data != null && data != '') {
        this.getRequest('/project/basic/getIndicatorByYearAndType?year=' + this.currentProjectCopy.startDate.split('-')[0] + '&type=' + data).then(response => {
          if(response) {
            this.selectProjectTypeList = response.data;
          }
        })
      }
    },
    //输入项目类别 发送请求调用的函数
    selectProjectTypeMethod(data) {
      this.debounceSearch(data);
    },
    cancelAdd() {
      this.dialogVisible = false;
    },
    download(data) {//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      var url = data.url
      axios({
        url: '/project/basic/downloadByUrl?url=' + url,
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
      this.postRequest1("/project/basic/deleteFile",file).then((response)=>{
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
      axios.post("/project/basic/upload",formData,{
        headers:{
          'token': localStorage.getItem('user') ? this.user.token : ''
        }
      }).then((response)=> {
          this.$message({
            message: '上传成功！'
          })
          //获取文件路径
          this.urlFile = response.data
          if(JSON.parse(JSON.stringify(this.selectProjectType)) != '{}') {
            this.addButtonState = true;
          } else {
            this.addButtonState = false;
          }
        }
      )
    },
    judgeMember(){//输入作者框 失去焦点触发事件
      var val = this.currentProjectCopy.author;
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
          this.currentProjectCopy.rank = 1;
          this.currentProjectCopy.total = 1;
          this.isAuthorIncludeSelf = true;
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
        this.isAuthorIncludeSelf = true;
      }
      this.currentProjectCopy.total = num.length;
      this.currentProjectCopy.rank = num.indexOf(info.name) + 1;
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    //编辑按钮
    showEditEmpView(data, idx) {
      this.title = "编辑科研项目信息";
      this.currentProjectCopy = JSON.parse(JSON.stringify(data));
      this.dialogVisible = true;
      this.disabledSelectProjectType = false;
      this.files = [
        {
          name: this.currentProjectCopy.url.split('/').reverse()[0],
          url: this.currentProjectCopy.url
        }
      ];
      this.urlFile = this.currentProjectCopy.url;
      this.selectProjectType = data.projectType.name;
      this.projectPoint = data.point;
      this.isAuthorIncludeSelf = true;
      this.addButtonState = true;
    },
    showInfo(data){
      this.loading = true;
      this.title_show = "显示详情";
      this.currentProject = data
      this.dialogVisible_showInfo = true
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=科研项目').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteProject(data) {
      if(confirm(
          "此操作将永久删除【" + data.name + "】, 是否继续?",)){
        this.deleteRequest("/project/basic/remove/" + data.id).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.$message.success('删除成功！');
            this.initProjectsList();
          }
        })
      }
    },
    editProject(params) {
      this.$refs["currentProjectCopy"].validate((valid) => {
        if (valid) {
          params.id = this.currentProjectCopy.id;
          if(JSON.parse(JSON.stringify(this.selectProjectType)) == '{}' || this.selectProjectType == '') {
            this.$message.error('请选择项目类别！')
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
          this.postRequest1("/project/basic/edit", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.$message.success('编辑成功！')
                  this.doAddOper("commit", this.currentProjectCopy.id);
                }
              }
          );
        }
      });
    },
    addProject() {//科研项目提交确认
      const params = {};
      params.name = this.currentProjectCopy.name;
      params.url = this.urlFile;
      params.rank = this.currentProjectCopy.rank;
      params.total = this.currentProjectCopy.total;
      params.author = this.currentProjectCopy.author;
      params.startDate = this.currentProjectCopy.startDate;
      params.endDate = this.currentProjectCopy.endDate;
      params.point = this.projectPoint;
      params.projectTypeId = this.selectProjectType.id;
      params.state = "commit";
      if (this.currentProjectCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editProject(params);
      } else {
        this.$refs["currentProjectCopy"].validate((valid) => {
          if (valid) {
            if(JSON.parse(JSON.stringify(this.selectProjectType)) == '{}' || this.selectProjectType == '') {
              this.$message.error('请选择项目类别！')
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
            this.postRequest1("/project/basic/add", params).then(
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
    async doAddOper(state,projectID) {
      this.oper.state = state
      this.oper.prodId = projectID
      this.oper.operationName = "提交项目"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper);
      await this.initProjectsList();
    },
    addProjectDialog() {//点击添加科研科研项目按钮
      this.urlFile = '';
      this.files = [];
      this.currentProjectCopy = {};
      this.addButtonState = false;
      this.projectPoint = '';
      this.title = "添加项目";
      this.selectProjectType = '';
      this.isAuthorIncludeSelf = false;
      this.disabledSelectProjectType = true;
      this.dialogVisible = true;
      this.selectProjectTypeList = [];
    },
    initProjectsList() {
      this.loading = true;
      let url = "/project/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.projectsList = resp.data;
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
