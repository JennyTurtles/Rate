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
            prop="indicator.name"
            label="专利类别"
            align="center"
            width="300"
        >
        </el-table-column>
        <el-table-column
            prop="patentee"
            align="center"
            label="参与人"
            width="150px"
        >
        </el-table-column>
        <el-table-column
            prop="indicator.score"
            label="积分"
            align="center"
            width="75px"
        >
        </el-table-column>
        <!-- width="60" -->
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
          :model="emp"
          :rules="rules"
          ref="empForm"
      >
        <el-form-item label="专利名称:" prop="name" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="emp.name"
              placeholder="请输入专利名称"
          ></el-input>
        </el-form-item>
        <el-form-item prop="typename" label="专利类别" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <div class="select_div_input">
            <input
                autocomplete="off"
                style="margin-left:5px;width:80%;line-height:28px;
                              border:1px solid lightgrey;padding:0 10px 1px 15px;
                              border-radius:4px;color:gray"
                :disabled="disabledInput"
                placeholder="请输入专利类别"
                v-model="patentTypename"
                @focus="inputTypeFocus"
                @blur="isTypeShow=isTypeFlag"
                id="input_patentTypeName"/>
            <div class="select_div"
                 v-show="isTypeShow && patentTypename ? true:false"
                 :style="'height:${menuHeight}'"
                 @mouseover="isTypeFlag = true"
                 @mouseleave="isTypeFlag = false">
              <div
                  style="margin:12px 0px 3px 12px;width:90%"
                  v-for="val in select_TypeName"
                  :key="val.index"
                  :value="val.value"
                  @click="filter_type(val)"
              >
                {{ val.value }}
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="立项年月:" prop="month" label-width="80px" style="margin-left: 20px;">
          <el-date-picker
              v-model="emp.date"
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
              v-model="emp.grantedStatus"
              placeholder="请选择专利状态"
          >
            <el-option label="授权" value="授权" />
            <el-option label="受理" value="受理" />
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
          <div>

          </div>

        </el-form-item>
      </el-form>
      <div style="margin-left: 20px;">
        <span style="color:gray;font-size:10px">将会获得：{{view_point}}积分</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
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
          label-width="120px"
          :model="emp"
          :rules="rules"
          ref="empForm"
          style="margin-left: 20px">

        <el-form-item label="专利名称:" prop="name">
          <span>{{ emp.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="专利类别:" prop="typename">
          <span>{{ emp.typename }}</span
          ><br />
        </el-form-item>
        <el-form-item label="受理年月:" prop="month">
          <span>{{ emp.month }}</span
          ><br />
        </el-form-item>
        <el-form-item label="专利状态:" prop="grantedStatus">
          <span>{{ emp.grantedStatus }}</span
          ><br />
        </el-form-item>
        <el-form-item label="参与人:" prop="patentee">
          <span>{{ emp.patentee }}</span
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
          <!-- <el-button @click="download(emp)" type="primary" v-show="emp.url == '' || emp.url == null ? false:true">下载材料</el-button> -->
          &nbsp;&nbsp;&nbsp;&nbsp;
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
              <!-- <el-form-item  v-model="operList" v-for="item in operList" :key="item.time" style="margin-bottom:8px;"> -->
              <div >
                <p>{{item.time|dataFormat}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operatorName}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operation}}</p>
                <p v-show="item.remark == '' ? false : true">驳回理由：{{item.remark}}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible_showinfo = false"
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
      disabledInput:false,
      timer:null,
      select_TypeName:[],
      isTypeFlag:false,
      isTypeShow:false,
      view_point:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      addButtonState:true,//是否允许添加项目
      operList:[],//每个项目的历史操作记录
      remarksort:[],//对显示的驳回理由做排序
      patentee:'',//和输入的作者列表绑定
      options:[],//存储所有类型对象
      data_picker:"",//选择时间
      ulList:false,
      labelPosition: "left",
      title: "",
      title_show: "",
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      total: 0,
      page: 1,
      size: 10,
      positions: [],
      patentTypename:"",//项目类别
      patentTypeID:-1,
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: 'patent',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      emp: {
        id: null,
        institutionID: null,
        studentID:"",
        time:"",
        grantedStatus:'',
        patentee:"",
        rank:"",
        total:"",
        point:"",
        url:'',
        state:" ",
        patentTypeID:"",
      },
      rules: {
        name: [{ required: true, message: "请输入专利名", trigger: "blur" }],
        scoreItemCount: [
          {
            required: true,
            type: "number",
            message: "请输入正确数据",
            trigger: "blur",
            transform: (value) => Number(value),
          },
        ],
        comment: [{ required: true, message: "请输入备注", trigger: "blur" }],
      },
    };
  },
  watch:{
    patentTypename:{
      handler(val){//函数抖动
        this.delayInputTimer(val)
      }
    },
    "emp.time":{//清除时间input设置为不可输入
      handler(val){
        if(!val){
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
      return this.patentTypename.length * 50 > 150
          ? 150 + 'px'
          : '${this.patentTypename.length * 50}px'
    },
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
    delayInputTimer(val){//项目类别输入框，根据name查找全称 每隔300ms发送请求
      if(this.timer){
        clearInterval(this.timer)
      }
      if(!val){
        return
      }
      this.timer = setInterval(()=>{
        let url = "/patentType/basic/searchByName/"
        this.postRequest(url, val).then((resp) => {
          this.loading = false;
          if (resp) {
            this.select_TypeName=[]
            if(resp.obj){
              for(var i=0;i<resp.obj.length;i++){
                this.select_TypeName.push(
                    {
                      value:resp.obj[i].name,
                      indicatorID:resp.obj[i].indicatorId,
                      patentTypeID:resp.obj[i].id
                    }
                )
              }
            }else{
              this.$message.error(`请检查项目名称的拼写`);
            }
          }
          clearInterval(this.timer)
        });
      },300)
    },
    inputTypeFocus(){//input获取焦点判断是否有下拉框，是否可输入
      if(this.emp.time){
        this.ispubShow = true//控制下拉框是否显示
        this.disabledInput = false
      }else {
        this.$message.error('请选择时间！')
        this.disabledInput = true
      }
    },
    filter_type(val){//选择下拉框的某个项目类别 得到选择的类别的id 等信息
      this.isTypeFlag=false
      this.isTypeShow=false
      var that = this
      if(!val){
        return
      }
      this.patentTypename = val.value
      var url = "/patentType/basic/getScore/" + val.indicatorID
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          if(resp.obj){
            this.view_point = resp.obj
            const id = JSON.parse(localStorage.getItem('user')).id
            this.patentTypeID = val.patentTypeID
          }else {
            this.patentTypename = ''
            this.$message.error('无该专利类别！请重新检查类别名称！')
          }
          clearInterval(this.timer)
        }
      });
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
    timechange(picker){//选择日历调用的方法
      var data=new Date(picker)
      this.disabledInput = false
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
          this.emp.patentee=this.patentee
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
            this.emp.point=0
            this.view_point=this.emp.point
          }
        }
        this.addButtonState=true
      }
      this.emp.total=num.length
      if(num.indexOf(info.teachers.name)>-1){
        num.splice(num.indexOf(info.teachers.name),1)
      }
      this.emp.rank=num.indexOf(info.name)+1
      this.emp.patentee=this.patentee
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    exportData() {
      window.open("/employee/basic/export", "_parent");
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
        comment: "",
      };
    },
    showEditEmpView(data) {
      this.title = "编辑项目信息";
      this.emp = data;
      this.dialogVisible = true;
      this.patentTypename = this.emp.indicator.name
      this.data_picker = this.emp.date
      this.patentee = this.emp.patentee
      this.options = []
      let url = "/patentType/basic/searchByName/"
      this.postRequest(url, this.patentTypename).then((resp) => {
        this.loading = false;
        if (resp) {
          this.select_TypeName=[]
          if(resp.obj){
            for(var i=0;i<resp.obj.length;i++){
              this.select_TypeName.push(
                  {
                    value:resp.obj[i].name,
                    indicatorID:resp.obj[i].indicatorId,
                    patentTypeID:resp.obj[i].id
                  }
              )
            }
          }else{
            this.$message.error(`请检查项目名称的拼写`);
          }
        }
        clearInterval(this.timer)
      });
      this.view_point = data.point
    },
    showInfo(data){
      this.title_show = "显示详情";
      this.emp=data
      this.dialogVisible_showInfo=true
      this.getRequest("/patentoper/basic/List?ID="+data.id).then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList=resp.data
          this.operList.sort(function(a,b){
            return a.time > b.time ? -1 : 1
          })
        }
      });
    },
    deleteEmp(data) {
      if(confirm(
          "此操作将永久删除【" + data.name + "】, 是否继续?",)){
        this.deleteRequest("/patent/basic/remove/" + data.id).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        })
      }
    },
    doAddEmp() {//项目提交确认
      if (this.emp.id) {//emptyEmp中没有将id设置为空 所以可以判断
        // if(confirm('确定要提交吗？')){
        var empdata=this.emp
        this.emptyEmp()
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.name=empdata.name
            this.emp.ID=empdata.id
            this.emp.point = empdata.point
            this.emp.patentee=empdata.patentee
            this.emp.total = empdata.total
            this.patentTypename=document.getElementById("input_patentTypeName").value
            this.emp.typename = this.patentTypename;
            this.emp.patentTypeID = this.patentTypeID;
            this.emp.url = this.urlFile;
            this.emp.state="commit"

            const _this = this;
            if(this.emp.url == '' ||this.emp.url == null){
              this.$message({
                message:'请上传证明材料！'
              })
              return
            }
            this.postRequest1("/patent/basic/edit", _this.emp).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible = false;
                    this.initEmps();
                  }
                }
            );
          }
        });

      } else {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.patentTypename=document.getElementById("input_patentTypeName").value
            this.emp.typename = this.patentTypename;
            this.emp.patentTypeID = this.patentTypeID;
            this.emp.point = this.view_point
            this.emp.url = this.urlFile;
            this.emp.state="commit"
            this.emp.total = 1
            this.emp.rank = 1
            this.emp.studentID = JSON.parse(localStorage.getItem('user')).id
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
      this.oper.operation = "提交专利"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper)
      await this.initEmps();
    },
    showAddEmpView() {//点击添加科研项目按钮
      this.addButtonState=true
      // this.emptyEmp();
      this.data_picker=''
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